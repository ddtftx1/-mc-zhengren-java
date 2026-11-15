package com.zhengren.trollmod.core;

import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffects;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 「残酷惩罚」池 —— v3.0.0 新增的引擎级狠招。
 *
 * <p>很多事件本质上只是"嘴上说说"（嘲讽一句、闪一下画面）。为了让 2000 个事件里
 * 那些温和的家伙也真的咬人，{@link TrollScheduler} 会在执行完
 * <b>等级 ≤ 烦人</b> 的事件之后，从这里随机抽一个重击追加执行。</p>
 *
 * <p>惩罚的强度会随 {@link TrollConfig#brutality} 自然放大，因为池子里用的都是
 * {@link TrollContext} 上已被残酷度接管的入口（damage / effect / boom / scatterFar …）。</p>
 */
public final class PunishmentPool {

    private static final List<Consumer<TrollContext>> PUNISHMENTS = new ArrayList<>();

    static {
        // ---------- 肉体 ----------
        add(ctx -> {
            ctx.warn("§4你被什么东西咬了一口，看不见伤口。");
            ctx.damage("magic", 6.0f);
        });
        add(ctx -> {
            ctx.warn("§4你的血正在自己往外渗。");
            ctx.effect(StatusEffects.WITHER, 200, 1);
        });
        add(ctx -> {
            ctx.warn("§4三、二、一……");
            ctx.lightning();
            ctx.lightningNear();
            ctx.setFire(8);
        });
        add(ctx -> {
            ctx.warn("§4你全身的骨头同时决定辞职。");
            ctx.effect(StatusEffects.SLOWNESS, 300, 2);
            ctx.effect(StatusEffects.WEAKNESS, 300, 2);
            ctx.effect(StatusEffects.MINING_FATIGUE, 300, 2);
        });
        add(ctx -> {
            ctx.warn("§4什么东西在你体内孵化。");
            ctx.effect(StatusEffects.POISON, 240, 1);
            ctx.effect(StatusEffects.HUNGER, 400, 2);
        });
        add(ctx -> {
            ctx.warn("§4你的视野被主动收走了。");
            ctx.effect(StatusEffects.BLINDNESS, 240, 0);
            ctx.effect(StatusEffects.NAUSEA, 300, 1);
        });
        add(ctx -> {
            ctx.warn("§4地心在叫你。");
            ctx.effect(StatusEffects.LEVITATION, 100, 1);
        });
        add(ctx -> {
            ctx.warn("§4你脚下突然什么都没有了。");
            ctx.voidPit(1, 12);
            ctx.damage("fall", 8.0f);
        });

        // ---------- 装备与财产 ----------
        add(ctx -> {
            ctx.warn("§4你的东西开始自己往外跑。");
            ctx.takeRandomItems(3);
        });
        add(ctx -> {
            ctx.warn("§4你的手持物决定不再属于你。");
            ctx.dropHeld();
        });
        add(ctx -> {
            ctx.warn("§4你的装备同时到达了寿命终点。");
            ctx.breakRandomTool();
            ctx.damageRandomTool(400);
        });
        add(ctx -> {
            ctx.warn("§4你的经验被重新分配给了别人。");
            ctx.setExperience(0);
        });
        add(ctx -> {
            ctx.warn("§4你的背包现在是一个哲学问题：它为什么是空的？");
            ctx.clearInventory();
        });
        add(ctx -> {
            ctx.warn("§4你饿了。不是那种饿。");
            ctx.starve();
            ctx.exhaust(20.0f);
        });

        // ---------- 禁锢 ----------
        add(ctx -> {
            ctx.warn("§4有人给你盖了个房子。你出不去。");
            ctx.cageWith(Blocks.OBSIDIAN);
        });
        add(ctx -> {
            ctx.warn("§4地面变得不太适合站立。");
            ctx.lavaPit(1);
        });
        add(ctx -> {
            ctx.warn("§4你被垂直移出了舒适区。");
            ctx.launch(2.5);
            ctx.effect(StatusEffects.SLOW_FALLING, 1, 0);
        });
        add(ctx -> {
            ctx.warn("§4远方有什么在等你，但没人告诉你在哪。");
            ctx.scatterFar(400);
        });
        add(ctx -> {
            ctx.warn("§4这个位置被系统回收了。");
            ctx.buryAlive();
        });

        // ---------- 生物与爆炸 ----------
        add(ctx -> {
            ctx.warn("§4你周围突然热闹了起来。");
            for (int i = 0; i < 4; i++) {
                ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
            }
        });
        add(ctx -> {
            ctx.warn("§4有人在你脚下埋了惊喜。");
            for (int i = 0; i < 4; i++) {
                ctx.spawnTnt(ctx.x() + ctx.ri(-3, 3), ctx.y(), ctx.z() + ctx.ri(-3, 3), ctx.ri(15, 35));
            }
        });
        add(ctx -> {
            ctx.warn("§4天上有东西正在下落，而且是朝你。");
            ctx.spawnFallingBlock(Blocks.ANVIL);
            ctx.boomAtPlayer(3.0f);
        });
        add(ctx -> {
            ctx.warn("§4气象部门对你个人发布了预警。");
            ctx.thunderstorm(1200);
            ctx.timeOfDay(18000L);
        });
        add(ctx -> {
            ctx.warn("§4天上要下点什么了。");
            ctx.meteorShower(4, 14.0);
        });

        // ---------- 心理 ----------
        add(ctx -> {
            ctx.warn("§4你的屏幕刚刚做了一个决定。");
            ctx.glitch(GlitchIds.SHAKY, 80, 2);
            ctx.glitch(GlitchIds.HIDE_HUD, 60, 1);
            ctx.glitch(GlitchIds.INVERT_MOUSE, 100, 1);
        });
        add(ctx -> {
            ctx.warn("§4所有人都会看见你现在的样子。");
            ctx.effectAll(StatusEffects.BLINDNESS, 120, 0);
            ctx.broadcast("§4" + ctx.playerName() + " 刚刚触发了一次惩罚。这与你无关，但你也被牵连了。");
        });
        add(ctx -> {
            ctx.warn("§4你听见了某种不该出现的声音。");
            ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
            ctx.effect(StatusEffects.DARKNESS, 200, 0);
        });

        // ---------- 终结技（仅在最高残酷度时启用） ----------
        add(ctx -> {
            ctx.warn("§0§l你被彻底抹除了。这只是个开始。");
            ctx.obliterate();
        });
    }

    private PunishmentPool() {
    }

    private static void add(Consumer<TrollContext> p) {
        PUNISHMENTS.add(p);
    }

    /** 池子里有多少种惩罚。 */
    public static int size() {
        return PUNISHMENTS.size();
    }

    /**
     * 对玩家追加一次随机残酷惩罚。
     *
     * <p>残酷度低于 4 时不会抽取"终结技"那一档（池子最后一项）。</p>
     */
    public static void apply(TrollContext ctx) {
        if (ctx == null || PUNISHMENTS.isEmpty()) {
            return;
        }
        int limit = PUNISHMENTS.size();
        if (ctx.config != null && ctx.config.brutality < 4) {
            limit = Math.max(1, limit - 1);
        }
        Consumer<TrollContext> p = PUNISHMENTS.get(ctx.random().nextInt(limit));
        try {
            p.accept(ctx);
        } catch (Throwable t) {
            // 惩罚失败也要继续整人，不能因为一个异常让整条链断掉
        }
    }
}
