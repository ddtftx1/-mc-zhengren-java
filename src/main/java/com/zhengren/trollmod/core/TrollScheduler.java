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
        int total = 0;
        for (TrollEvent e : TrollRegistry.all()) {
            if (cfg.isDisabled(e.id())) {
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
            total += e.weight();
        }
        if (pool.isEmpty() || total <= 0) {
            return null;
        }
        int roll = RNG.nextInt(total);
        for (TrollEvent e : pool) {
            roll -= e.weight();
            if (roll < 0) {
                return e;
            }
        }
        return pool.get(pool.size() - 1);
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
}
