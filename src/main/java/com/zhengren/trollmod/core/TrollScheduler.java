package com.zhengren.trollmod.core;

import com.zhengren.trollmod.ZhengrenMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * 事件调度器：决定什么时候、对谁、触发什么事件。
 */
public final class TrollScheduler {

    private static final Map<UUID, PlayerTrollState> STATES = new HashMap<>();
    private static final Random RNG = new Random();

    /** 狂暴模式 */
    public static long chaosUntilTick = 0;
    public static int chaosIntervalTicks = 20;

    private TrollScheduler() {
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(TrollScheduler::onServerTick);
    }

    public static PlayerTrollState state(ServerPlayerEntity player) {
        return STATES.computeIfAbsent(player.getUuid(),
                id -> new PlayerTrollState(id, player.getName().getString()));
    }

    public static Map<UUID, PlayerTrollState> states() {
        return STATES;
    }

    public static boolean chaosActive(long now) {
        return now < chaosUntilTick;
    }

    public static void startChaos(long now, int seconds) {
        chaosUntilTick = now + Math.max(1, seconds) * 20L;
    }

    public static void stopChaos() {
        chaosUntilTick = 0;
    }

    private static void onServerTick(MinecraftServer server) {
        TrollConfig cfg = ZhengrenMod.CONFIG;
        if (cfg == null || !cfg.enabled) {
            return;
        }
        long now = server.getTicks();
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            try {
                tickPlayer(server, player, cfg, now);
            } catch (Throwable t) {
                ZhengrenMod.LOGGER.error("[整人大师] 调度异常: {}", t.toString());
            }
        }
    }

    private static void tickPlayer(MinecraftServer server, ServerPlayerEntity player, TrollConfig cfg, long now) {
        if (player.isSpectator()) {
            return;
        }
        if (!cfg.affectCreative && player.isCreative()) {
            return;
        }
        if (!cfg.isTarget(player.getName().getString())) {
            return;
        }
        PlayerTrollState st = state(player);
        if (st.joinedTick == 0) {
            st.joinedTick = now;
            st.nextEventTick = now + 20L * 15;
            return;
        }
        if (now < st.nextEventTick) {
            return;
        }
        if (!cfg.allowFire(now)) {
            st.nextEventTick = now + 40;
            return;
        }
        TrollEvent event = pick(server, player, cfg, st, now);
        if (event == null) {
            st.nextEventTick = now + 40;
            return;
        }
        fire(server, player, cfg, st, event);
        st.nextEventTick = now + nextInterval(cfg, st, now);
    }

    /** 计算下一次事件的间隔（tick） */
    public static int nextInterval(TrollConfig cfg, PlayerTrollState st, long now) {
        if (chaosActive(now)) {
            return chaosIntervalTicks;
        }
        int base = (cfg.minIntervalSeconds + RNG.nextInt(
                Math.max(1, cfg.maxIntervalSeconds - cfg.minIntervalSeconds + 1))) * 20;
        if (cfg.escalation) {
            double factor = 1.0 - Math.min(0.65, st.rage / 400.0);
            base = (int) (base * factor);
        }
        return Math.max(60, base);
    }

    /** 按权重挑一个可用的整人事件 */
    public static TrollEvent pick(MinecraftServer server, ServerPlayerEntity player,
                                  TrollConfig cfg, PlayerTrollState st, long now) {
        List<TrollEvent> pool = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        int total = 0;
        for (TrollEvent e : TrollRegistry.all()) {
            if (cfg.isDisabled(e.id())) {
                continue;
            }
            if (TrollEvents.CATEGORY_MINDBREAK.equals(e.category()) && !cfg.mindbreakEnabled) {
                continue;
            }
            if (e.tier().level() > cfg.maxTier) {
                continue;
            }
            if (e.oncePerPlayer() && st.firedOnce.contains(e.id())) {
                continue;
            }
            Long last = st.lastRun.get(e.id());
            if (last != null && now - last < e.cooldownTicks()) {
                continue;
            }
            if (e.id().equals(st.lastEventId)) {
                continue;
            }
            if (!e.canRun(new TrollContext(server, player, cfg, st, RNG, e))) {
                continue;
            }
            pool.add(e);
            int w = effectiveWeight(e, cfg);
            weights.add(w);
            total += w;
        }
        if (pool.isEmpty() || total <= 0) {
            return null;
        }
        int roll = RNG.nextInt(total);
        for (int i = 0; i < pool.size(); i++) {
            roll -= weights.get(i);
            if (roll < 0) {
                return pool.get(i);
            }
        }
        return pool.get(pool.size() - 1);
    }

    /**
     * 事件在本次抽取中的有效权重。
     * 「精神污染」扩展事件的权重会按 {@link TrollConfig#mindbreakWeightScale()} 实时缩放，
     * 因此 /zr intensity 一改就立刻生效，无需重启或重载事件表。
     */
    public static int effectiveWeight(TrollEvent e, TrollConfig cfg) {
        if (cfg != null && TrollEvents.CATEGORY_MINDBREAK.equals(e.category())) {
            return Math.max(1, (int) Math.round(e.weight() * cfg.mindbreakWeightScale()));
        }
        return Math.max(1, e.weight());
    }

    public static boolean fire(MinecraftServer server, ServerPlayerEntity player, TrollConfig cfg,
                               PlayerTrollState st, TrollEvent event) {
        TrollContext ctx = new TrollContext(server, player, cfg, st, RNG, event);
        try {
            event.run(ctx);
        } catch (Throwable t) {
            ZhengrenMod.LOGGER.error("[整人大师] 事件 {} 执行失败: {}", event.id(), t.toString());
            return false;
        }
        st.markRun(event.id(), server.getTicks());
        if (event.oncePerPlayer()) {
            st.firedOnce.add(event.id());
        }
        st.rage += event.tier().level() * 1.5;
        if (cfg.announce) {
            player.sendMessage(Text.literal("§8[§c整人大师§8] " + event.tier().colored()
                    + " §f" + event.display() + " §7(#" + event.id() + ")"), false);
        }
        return true;
    }

    public static boolean fireById(MinecraftServer server, ServerPlayerEntity player, String id) {
        TrollEvent e = TrollRegistry.get(id);
        if (e == null) {
            return false;
        }
        TrollConfig cfg = ZhengrenMod.CONFIG;
        PlayerTrollState st = state(player);
        return fire(server, player, cfg, st, e);
    }

    public static TrollEvent randomEvent(TrollConfig cfg) {
        List<TrollEvent> pool = TrollRegistry.upTo(cfg.maxTier);
        if (pool.isEmpty()) {
            return null;
        }
        return pool.get(RNG.nextInt(pool.size()));
    }

    /**
     * 按配置挑一个"随时可手动触发"的随机事件：只按等级、黑名单与精神污染开关过滤，
     * 不考虑冷却与 {@code when()} 条件（手动触发应当总是能出结果）。
     *
     * <p>与 {@link #randomEvent(TrollConfig)} 的区别：后者只按等级过滤，会把黑名单事件和
     * 已关闭的精神污染事件也抽出来。物品、{@code /zr random}、{@code /mindbreak chaos}
     * 都应改用本方法，这样配置对它们才真正生效。</p>
     */
    public static TrollEvent randomEventFiltered(TrollConfig cfg) {
        if (cfg == null) {
            cfg = ZhengrenMod.CONFIG;
        }
        if (cfg == null) {
            return null;
        }
        List<TrollEvent> pool = new ArrayList<>();
        int total = 0;
        for (TrollEvent e : TrollRegistry.all()) {
            if (cfg.isDisabled(e.id())) {
                continue;
            }
            if (TrollEvents.CATEGORY_MINDBREAK.equals(e.category()) && !cfg.mindbreakEnabled) {
                continue;
            }
            if (e.tier().level() > cfg.maxTier) {
                continue;
            }
            pool.add(e);
            total += effectiveWeight(e, cfg);
        }
        if (pool.isEmpty() || total <= 0) {
            return null;
        }
        int roll = RNG.nextInt(total);
        for (TrollEvent e : pool) {
            roll -= effectiveWeight(e, cfg);
            if (roll < 0) {
                return e;
            }
        }
        return pool.get(pool.size() - 1);
    }
}
