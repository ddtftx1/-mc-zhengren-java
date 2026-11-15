package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【世界与地形】分类事件。
 * 负责整活：方块、地形、天气、时间、建筑、传送点。
 *
 * <p>本文件刻意**不引用任何 Minecraft 类型**，全部通过 {@code ctx.*} 与 {@code TrollPools.*} 组合实现，
 * 因此不会因为版本 API 变动而编译失败。</p>
 */
public final class WorldEvents {

    // ===== TrollPools.TROLL_BLOCKS 的固定下标（与 TrollPools 中的声明顺序一一对应）=====
    private static final int B_DIRT = 0;      // Blocks.DIRT
    private static final int B_COBBLE = 1;    // Blocks.COBBLESTONE
    private static final int B_GRAVEL = 3;    // Blocks.GRAVEL
    private static final int B_ICE = 6;       // Blocks.ICE
    private static final int B_SLIME = 8;     // Blocks.SLIME_BLOCK
    private static final int B_MAGMA = 10;    // Blocks.MAGMA_BLOCK
    private static final int B_CACTUS = 11;   // Blocks.CACTUS
    private static final int B_OBSIDIAN = 14; // Blocks.OBSIDIAN
    private static final int B_SPONGE = 15;   // Blocks.SPONGE
    private static final int B_TNT = 16;      // Blocks.TNT
    private static final int B_COBWEB = 17;   // Blocks.COBWEB

    // ===== TrollPools.EVIL_BLOCKS 的固定下标 =====
    private static final int E_BEDROCK = 0;   // Blocks.BEDROCK
    private static final int E_OBSIDIAN = 2;  // Blocks.OBSIDIAN
    private static final int E_WATER = 4;     // Blocks.WATER

    private WorldEvents() {
    }

    public static void register() {
        TrollEvents.category("世界与地形");

        // ================================================================
        // 序章 —— 小小的失足：伤害不大，但地板已经不可信
        // ================================================================

        TrollEvents.reg(TrollEvent.of("world_floor_gone", "脚下一空", TrollTier.BRUTAL)
                .desc("脚下的方块当场辞职，你负责自由落体")
                .weight(140)
                .cooldownSeconds(40)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.breakUnder();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.5f);
                    ctx.actionBar("§7地板：§8我不干了§7。");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_sudden_rain", "局部有雨", TrollTier.ANNOYING)
                .desc("瞬间拉来一场雷暴，下雨范围恰好覆盖你一个人")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.thunderstorm(600);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.title("§8§l下雨了", "§7伞？这里不卖伞。");
                    ctx.msg("§7天气预报：§9局部有雨§7，范围——§e就你一个人§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_midnight_snap", "太阳提前下班", TrollTier.BRUTAL)
                .desc("白天瞬间切成午夜，怪物们集体开始上班")
                .weight(110)
                .cooldownSeconds(120)
                .when(ctx -> ctx.worldTime() < 12000L)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.timeOfDay(18000L);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 0.7f);
                    ctx.msg("§8太阳：§7今天先到这儿，§8晚安。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_bat_swarm", "良心发现", TrollTier.ANNOYING)
                .desc("往你头上撒一把会飞的东西，并且给它们统一起了名字")
                .weight(120)
                .cooldownSeconds(50)
                .run(ctx -> {
                    for (int i = 0; i < 6; i++) {
                        ctx.giveMobCustomName(
                                ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                        ctx.x() + ctx.ri(-4, 4),
                                        ctx.y() + ctx.ri(2, 4),
                                        ctx.z() + ctx.ri(-4, 4)),
                                "§8你的良心");
                    }
                    ctx.msg("§7天上掉下来 §8六只§7 你的良心，它们都在看着你。");
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_chicken_rain", "天降正义", TrollTier.BRUTAL)
                .desc("从十几格高空往下空投一群无辜的小动物")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.giveMobCustomName(
                                ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                        ctx.x() + ctx.ri(-5, 5),
                                        ctx.y() + ctx.ri(14, 20),
                                        ctx.z() + ctx.ri(-5, 5)),
                                "§e空投包裹");
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§e麻烦签收一下，天降的§l正义§r§e。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_fake_bed_break", "床没了", TrollTier.ANNOYING)
                .desc("脚下一空外加一句假聊天，让你当场怀疑重生点还在不在")
                .weight(120)
                .cooldownSeconds(70)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.breakUnder();
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "你的床我拆了，别客气");
                    ctx.msg("§7重生点状态：§c未知§7。祝你好运。");
                })
                .build());

        // ================================================================
        // 加压 —— 环境开始与你为敌：灼烧、封顶、持续消耗
        // ================================================================

        TrollEvents.reg(TrollEvent.of("world_slime_shoes", "弹簧与岩浆", TrollTier.NIGHTMARE)
                .desc("脚下被换成岩浆与弹簧的组合，弹起的高度刚好够你看清落点")
                .weight(140)
                .cooldownSeconds(60)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[B_SLIME]);
                    ctx.launch(1.1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                    ctx.msg("§a地板申请了§l弹跳§r§a专利，申请费你出。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_ice_rink", "免费溜冰场", TrollTier.BRUTAL)
                .desc("脚底铺满冰，再附赠一个随机负面效果当配菜")
                .weight(130)
                .cooldownSeconds(75)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[B_ICE], 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                    ctx.msg("§b溜冰场已开业，§7摔倒是套餐里的一部分。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_cactus_step", "带刺的落脚点", TrollTier.NIGHTMARE)
                .desc("脚下塞进仙人掌，再补一层凋零——你连站着都在被消耗")
                .weight(120)
                .cooldownSeconds(90)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[B_CACTUS]);
                    ctx.damage("cactus", 2.0f);
                    ctx.msg("§7这是一块垫脚石，§c字面意思§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_magma_step", "会走路的烤炉", TrollTier.ANNOYING)
                .desc("脚下变岩浆块，站着不动也会持续掉血")
                .weight(115)
                .cooldownSeconds(90)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[B_MAGMA]);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§c地板已预热至 300℃，§7请勿长时间停留，也请勿离开。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_web_trap", "裹尸布", TrollTier.NIGHTMARE)
                .desc("蜘蛛网从四面八方合拢，饥饿被清空——你连挣扎的力气都要省着用")
                .weight(130)
                .cooldownSeconds(80)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[B_COBWEB]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 100, 1);
                    ctx.msg("§7恭喜获得 §f蜘蛛网§7 ×一堆，§8概不退货§7。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_ceiling_plate", "天花板快递", TrollTier.BRUTAL)
                .desc("头顶三格处加盖一整块地板，你的天空被回收了")
                .weight(120)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            ctx.setBlock(dx, 3, dz, ctx.pick(TrollPools.TROLL_BLOCKS));
                        }
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§7头顶已封顶，§8你的天空被征收了§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_pit_fall", "地基被抽走", TrollTier.NIGHTMARE)
                .desc("身边的地面被连续凿穿，工具同时报废——掉下去之前，先从里面烂掉")
                .weight(125)
                .cooldownSeconds(80)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.breakAt(ctx.pos());
                    for (int i = 0; i < 3; i++) {
                        ctx.breakAt(ctx.randomNearbyPos(2));
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§7你附近的地面出现了 §8几个§7 洞，§8不是我挖的§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_scatter_trip", "随机旅游券", TrollTier.BRUTAL)
                .desc("把你随机丢到几十格外，行李留在原地自生自灭")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.scatter(60);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.msg("§7传送成功，目的地：§8我也不知道§7。");
                    ctx.addRage(4);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_floor_pave", "强行铺路", TrollTier.ANNOYING)
                .desc("把你脚下铺成五乘五的诡异地板，材料随机")
                .weight(120)
                .cooldownSeconds(90)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(ctx.pick(TrollPools.TROLL_BLOCKS), 2);
                    ctx.msg("§7施工队路过，顺便把你家地皮§l翻新§r§7了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_water_rise", "沉没", TrollTier.NIGHTMARE)
                .desc("脚下灌满水，背包同时被抽走两格——你连一根救援绳都没有")
                .weight(110)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.placeFloor(TrollPools.EVIL_BLOCKS[E_WATER], 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§9水位上涨§7，这条街现在归海管。");
                })
                .build());

        // ================================================================
        // 封锁 —— 空间收窄：被活埋、被抛送、被围起
        // ================================================================

        TrollEvents.reg(TrollEvent.of("world_sky_drop", "一百二十格的判决", TrollTier.NIGHTMARE)
                .desc("抬到一百二十格高空，饥饿清零——你只能看着地面慢慢变大")
                .weight(55)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.lift(120);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.title("§c§l自由落体", "§7落地姿势请自理");
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_obsidian_cage", "无门的房间", TrollTier.NIGHTMARE)
                .desc("黑曜石从三面合拢，天花板落下的那一刻你才明白：这间房没有出口")
                .weight(50)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[E_OBSIDIAN]);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.msg("§8新房已交付：§7三面黑曜石，一堵天花板，§c没有门§7。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_buried_alive", "考古编号", TrollTier.NIGHTMARE)
                .desc("先抛到一百二十格外，再用沙砾把你砌进土里——你成了别人的发掘对象")
                .weight(52)
                .cooldownSeconds(230)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[B_GRAVEL]);
                    ctx.setBlock(0, 3, 0, TrollPools.TROLL_BLOCKS[B_GRAVEL]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§8你已被考古队正式收录§7，编号 #" + ctx.gameTicks() % 1000 + "。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_fire_bath", "燃烧的上升", TrollTier.APOCALYPSE)
                .desc("抬到高处点火，火焰会陪你走完整个下坠过程")
                .weight(48)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.lift(35);
                    ctx.launch(0.6);
                    ctx.setFire(12);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§c桑拿房已就绪§7，温度不可调，时长不可控。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_gravel_avalanche", "头顶的重量", TrollTier.APOCALYPSE)
                .desc("半空塞满会坠落的方块，你站在最中间——抬头是唯一会后悔的动作")
                .weight(50)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    for (int dx = -2; dx <= 2; dx++) {
                        for (int dz = -2; dz <= 2; dz++) {
                            if (ctx.chance(0.7)) {
                                ctx.setBlock(dx, ctx.ri(4, 8), dz, TrollPools.TROLL_BLOCKS[B_GRAVEL]);
                            }
                        }
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.msg("§7头顶正在办理§8拆迁§7手续，请勿抬头。");
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_tnt_scatter", "引信在你脚下", TrollTier.NIGHTMARE)
                .desc("四周埋满 TNT，只点一个；剩下那些会在你心里一直滴答下去")
                .weight(45)
                .cooldownSeconds(260)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    for (int i = 0; i < 7; i++) {
                        ctx.setBlock(ctx.ri(-3, 3), 0, ctx.ri(-3, 3), TrollPools.TROLL_BLOCKS[B_TNT]);
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 60);
                    ctx.boomAtPlayer(2.5f);
                    ctx.msg("§c方圆三格全是 TNT§7，我只点了§8一个§7，祝你好运。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_lightning_near_miss", "第一道是警告", TrollTier.APOCALYPSE)
                .desc("闪电落在两步之外，同时把你活埋——下一道不会偏")
                .weight(46)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.lightningNear();
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 1.0f);
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7差 §c两格§7。");
                    ctx.addRage(10);
                })
                .build());

        // ================================================================
        // 处刑 —— 活埋、高空与持续伤害，损失开始不可逆
        // ================================================================

        TrollEvents.reg(TrollEvent.of("world_void_edge", "世界底部", TrollTier.APOCALYPSE)
                .desc("直接扔到世界底部，经验清零；脚下没有底，抬头也没有归路")
                .weight(32)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.teleport(ctx.x(), -56.0, ctx.z());
                    ctx.damage("outofworld", 2.0f);
                    ctx.glitch(GlitchIds.BLACKOUT, 40);
                    ctx.msg("§8下面是虚空§7，上面是你不该去的地方。");
                    ctx.addRage(22);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_lightning_crown", "点名", TrollTier.OBLIVION)
                .desc("先广播别人被选中，让你松一口气，再让那道雷落在你头顶。死亡不会结束它——引擎已经关掉了怜悯。")
                .weight(30)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e[天候系统] 本次雷击目标已选定：§f"
                            + ctx.pick(TrollPools.FAKE_NAMES) + "§e。其余玩家安全。");
                    ctx.ok("§a你不在名单上。");
                    ctx.later(60, () -> {
                        ctx.warn("§4名单更新了。");
                        ctx.voidPit(2, 16);
                        ctx.stripAll();
                        ctx.obliterate();
                        ctx.meteorShower(4, 16.0);
                        ctx.lightning();
                        ctx.setFire(6);
                        ctx.glitch(GlitchIds.FLASHBANG, 20, 2);
                        ctx.title("§4§l点名", "§7你现在在名单上了");
                        ctx.msg("§0雷声里，有人在念你的名字。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_web_choke", "标本", TrollTier.OBLIVION)
                .desc("先用一句『我来救你』让你抬头，再让蛛网从四面八方合拢——等你反应过来，已经没有人需要救了。")
                .weight(30)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.fakeChat("救援队", "别动，我这就把你挖出来！");
                    ctx.ok("§a救援队正在接近……");
                    ctx.later(70, () -> {
                        ctx.fakeChat("救援队", "……等等，你背后那个是什么");
                        ctx.effectRandom(1200, 3);
                        ctx.surroundWith(TrollPools.TROLL_BLOCKS[B_COBWEB]);
                        ctx.setBlock(0, 2, 0, TrollPools.TROLL_BLOCKS[B_GRAVEL]);
                        ctx.setBlock(0, 3, 0, TrollPools.TROLL_BLOCKS[B_SPONGE]);
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                        ctx.voidPit(2, 16);
                        ctx.stripAll();
                        ctx.obliterate();
                        ctx.msg("§8救援队没有再说第二句话。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_storm_siege", "停电", TrollTier.OBLIVION)
                .desc("先掐掉你的视野，再让雷暴与脚步声一起靠近——你只能靠听觉确认自己被围得有多紧。")
                .weight(28)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 120, 3);
                    ctx.thunderstorm(2400);
                    ctx.timeOfDay(18000L);
                    ctx.effectRandom(1200, 3);
                    ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), true);
                    ctx.msg("§7有人进来了。你听不清是谁。");
                    ctx.later(80, () -> {
                        ctx.glitch(GlitchIds.SHAKY, 120, 2);
                        for (int i = 0; i < 6; i++) {
                            ctx.giveMobCustomName(ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE)),
                                    "§8邻居");
                        }
                        ctx.voidPit(2, 16);
                        ctx.buryAlive();
                        ctx.meteorShower(4, 16.0);
                        ctx.title("§0§l它们都进来了", "§7而你还没找到门");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_time_screw", "昼夜错乱", TrollTier.NIGHTMARE)
                .desc("正午与午夜被反复撕扯，饥饿清零、视野扭曲——你分不清哪些记忆是真的")
                .weight(30)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.timeOfDay(6000L);
                    ctx.timeOfDay(18000L);
                    ctx.timeOfDay(6000L);
                    ctx.timeOfDay(18000L);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 100, 1);
                    ctx.msg("§7时间：§8这儿我说了算§7。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_terrain_flatten", "抹平", TrollTier.APOCALYPSE)
                .desc("方圆数格被铲成空白，地皮、退路与你的心态一起被推平")
                .weight(26)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[B_COBBLE], 6);
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[B_DIRT]);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.boomAtPlayer(2.0f);
                    ctx.msg("§7这片地皮已重新规划§8，规划内容：什么都没有§7。");
                    ctx.addRage(22);
                })
                .build());

        // ================================================================
        // 绝望 —— 世界本身动手，死亡不再是终点
        // ================================================================

        TrollEvents.reg(TrollEvent.of("world_bedrock_coffin", "永久产权", TrollTier.APOCALYPSE)
                .desc("基岩四面合盖、头顶封死，氧气与血量同时被抽走——不会有人来敲这扇门")
                .weight(24)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[E_BEDROCK]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.glitch(GlitchIds.HIDE_HUD, 200, 1);
                    ctx.title("§4§l棺材已合盖", "§8恭喜入住永久产权房");
                    ctx.msg("§4你现在住的是 §l基岩§r§4：没有窗，没有门，没有你。");
                    // 保底出口：基岩封死 + 着火，保证玩家一定会死亡重生，不会永久卡在笼子里
                    ctx.setHealth(6.0f);
                    ctx.setFire(20);
                    ctx.starve();
                    ctx.addRage(35);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_void_exile", "假传送门", TrollTier.OBLIVION)
                .desc("告诉你回家的门开了，等你走进去才发现那只是一个洞口。存档提示是假的，脚下的虚空是真的。")
                .weight(22)
                .cooldownSeconds(1200)
                .run(ctx -> {
                    ctx.fakeBroadcast("§b[传送门] 出口已生成，请在 5 秒内进入。");
                    ctx.ok("§a传送门就在你脚下。");
                    ctx.later(100, () -> {
                        ctx.glitch(GlitchIds.FAKE_CORRUPT, 100, 2);
                        ctx.msg("§4存档读取失败。");
                        ctx.stripAll();
                        ctx.voidPit(2, 24);
                        ctx.teleport(ctx.x(), 260.0, ctx.z());
                        ctx.launch(-2.5);
                        ctx.thunderstorm(600);
                        ctx.meteorShower(4, 16.0);
                        ctx.obliterate();
                        ctx.msg("§8门是真的。只是通向的那一头，什么都没有。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_collapse_combo", "终章", TrollTier.OBLIVION)
                .desc("先为你保存世界，再亲手删掉它。雷暴、岩浆与怪物潮只是背景音，真正被清空的是你留下的痕迹。")
                .weight(20)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.ok("§a世界已保存。");
                    ctx.later(60, () -> {
                        ctx.timeOfDay(18000L);
                        ctx.thunderstorm(2400);
                        ctx.glitch(GlitchIds.SHAKE_BURST, 60, 3);
                        ctx.surroundWith(TrollPools.TROLL_BLOCKS[B_MAGMA]);
                        for (int i = 0; i < 6; i++) {
                            ctx.setBlock(ctx.ri(-4, 4), ctx.ri(2, 5), ctx.ri(-4, 4),
                                    TrollPools.TROLL_BLOCKS[B_TNT]);
                        }
                        for (int i = 0; i < 8; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                        }
                        ctx.voidPit(2, 20);
                        ctx.stripAll();
                        ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 120);
                        ctx.boomAtPlayer(3.0f);
                        ctx.obliterate();
                        ctx.title("§0§l终章", "§8接下来发生的事，不再需要你参与");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_save_corrupt_tease", "存档已删除", TrollTier.OBLIVION)
                .desc("它会当着你的面显示崩溃、清空世界、再还你一件纪念品。整局只来一次，但你会记很久——死亡也追不上它。")
                .weight(20)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.glitch(GlitchIds.FAKE_CRASH, 120, 3);
                    ctx.fakeBroadcast("§4你的存档已被删除。感谢游玩。");
                    ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), false);
                    ctx.later(120, () -> {
                        ctx.placeFloor(TrollPools.TROLL_BLOCKS[B_OBSIDIAN], 3);
                        ctx.buryAlive();
                        ctx.voidPit(2, 16);
                        ctx.effectRandom(1200, 3);
                        ctx.stripAll();
                        ctx.obliterate();
                        ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                        ctx.msg("§8……开玩笑的。§7不过背包是真的没了。");
                        ctx.msg("§0§o（引擎已关闭怜悯：这一次，重生点也不会替你保留什么。）");
                    });
                })
                .build());
    }
}
