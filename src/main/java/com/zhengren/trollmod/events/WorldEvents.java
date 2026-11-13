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
        // PRANK —— 小整，笑一下就好
        // ================================================================

        TrollEvents.reg(TrollEvent.of("world_floor_gone", "脚下一空", TrollTier.PRANK)
                .desc("脚下的方块当场辞职，你负责自由落体")
                .weight(140)
                .cooldownSeconds(40)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.breakUnder();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.5f);
                    ctx.actionBar("§7地板：§8我不干了§7。");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_sudden_rain", "局部有雨", TrollTier.PRANK)
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

        TrollEvents.reg(TrollEvent.of("world_midnight_snap", "太阳提前下班", TrollTier.PRANK)
                .desc("白天瞬间切成午夜，怪物们集体开始上班")
                .weight(110)
                .cooldownSeconds(120)
                .when(ctx -> ctx.worldTime() < 12000L)
                .run(ctx -> {
                    ctx.timeOfDay(18000L);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 0.7f);
                    ctx.msg("§8太阳：§7今天先到这儿，§8晚安。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_bat_swarm", "良心发现", TrollTier.PRANK)
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

        TrollEvents.reg(TrollEvent.of("world_chicken_rain", "天降正义", TrollTier.PRANK)
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

        TrollEvents.reg(TrollEvent.of("world_fake_bed_break", "床没了", TrollTier.PRANK)
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
        // ANNOYING —— 烦人，会皱眉
        // ================================================================

        TrollEvents.reg(TrollEvent.of("world_slime_shoes", "弹性地板", TrollTier.ANNOYING)
                .desc("把脚下换成弹性方块，再顺手把你弹上天")
                .weight(140)
                .cooldownSeconds(60)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[B_SLIME]);
                    ctx.launch(1.1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                    ctx.msg("§a地板申请了§l弹跳§r§a专利，申请费你出。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_ice_rink", "免费溜冰场", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("world_cactus_step", "给你垫个仙人掌", TrollTier.ANNOYING)
                .desc("脚底换成仙人掌，让你体会什么叫带刺的人生")
                .weight(120)
                .cooldownSeconds(90)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
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

        TrollEvents.reg(TrollEvent.of("world_web_trap", "蛛网大礼包", TrollTier.ANNOYING)
                .desc("往你身上糊满蜘蛛网，寸步难行还要吃个负面效果")
                .weight(130)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[B_COBWEB]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 100, 1);
                    ctx.msg("§7恭喜获得 §f蜘蛛网§7 ×一堆，§8概不退货§7。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_ceiling_plate", "天花板快递", TrollTier.ANNOYING)
                .desc("头顶三格处加盖一整块地板，你的天空被回收了")
                .weight(120)
                .cooldownSeconds(100)
                .run(ctx -> {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dz = -1; dz <= 1; dz++) {
                            ctx.setBlock(dx, 3, dz, ctx.pick(TrollPools.TROLL_BLOCKS));
                        }
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§7头顶已封顶，§8你的天空被征收了§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_pit_fall", "随机挖矿事故", TrollTier.ANNOYING)
                .desc("在你脚边连开几个洞，位置全凭缘分")
                .weight(125)
                .cooldownSeconds(80)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.breakAt(ctx.pos());
                    for (int i = 0; i < 3; i++) {
                        ctx.breakAt(ctx.randomNearbyPos(2));
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§7你附近的地面出现了 §8几个§7 洞，§8不是我挖的§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_scatter_trip", "随机旅游券", TrollTier.ANNOYING)
                .desc("把你随机丢到几十格外，行李留在原地自生自灭")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
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

        TrollEvents.reg(TrollEvent.of("world_water_rise", "涨水了", TrollTier.ANNOYING)
                .desc("在脚边灌水，把干燥的你改造成落汤鸡")
                .weight(110)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.EVIL_BLOCKS[E_WATER], 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§9水位上涨§7，这条街现在归海管。");
                })
                .build());

        // ================================================================
        // BRUTAL —— 抓狂，会骂人
        // ================================================================

        TrollEvents.reg(TrollEvent.of("world_sky_drop", "高空卸货", TrollTier.BRUTAL)
                .desc("把你抬到一百二十格高空，然后全权交给重力")
                .weight(55)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.lift(120);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.title("§c§l自由落体", "§7落地姿势请自理");
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_obsidian_cage", "黑曜石套房", TrollTier.BRUTAL)
                .desc("用黑曜石把你原地打包，还贴心地加了天花板")
                .weight(50)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[E_OBSIDIAN]);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.msg("§8新房已交付：§7三面黑曜石，一堵天花板，§c没有门§7。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_buried_alive", "活埋体验卡", TrollTier.BRUTAL)
                .desc("用沙砾把周围和头顶填死，让你体验考古学家的快乐")
                .weight(52)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[B_GRAVEL]);
                    ctx.setBlock(0, 3, 0, TrollPools.TROLL_BLOCKS[B_GRAVEL]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§8你已被考古队正式收录§7，编号 #" + ctx.gameTicks() % 1000 + "。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_fire_bath", "免费桑拿", TrollTier.BRUTAL)
                .desc("先抬到高空再点火，落地之前你会有很多时间思考人生")
                .weight(48)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.lift(35);
                    ctx.launch(0.6);
                    ctx.setFire(12);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§c桑拿房已就绪§7，温度不可调，时长不可控。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_gravel_avalanche", "头顶塌方", TrollTier.BRUTAL)
                .desc("在头顶摆满会掉下来的方块，然后撒手不管")
                .weight(50)
                .cooldownSeconds(220)
                .run(ctx -> {
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

        TrollEvents.reg(TrollEvent.of("world_tnt_scatter", "到处都是 TNT", TrollTier.BRUTAL)
                .desc("周围一圈塞满 TNT，然后只引爆其中一个，让你自己找")
                .weight(45)
                .cooldownSeconds(260)
                .run(ctx -> {
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

        TrollEvents.reg(TrollEvent.of("world_lightning_near_miss", "雷差点劈到你", TrollTier.BRUTAL)
                .desc("在离你两步远的地方劈一道闪电，主要目的是吓人")
                .weight(46)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.lightningNear();
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 1.0f);
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7差 §c两格§7。");
                    ctx.addRage(10);
                })
                .build());

        // ================================================================
        // NIGHTMARE —— 崩溃，会砸键盘
        // ================================================================

        TrollEvents.reg(TrollEvent.of("world_void_edge", "虚空边缘一日游", TrollTier.NIGHTMARE)
                .desc("把你直接扔到世界底部附近，脚下什么都没有")
                .weight(32)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.teleport(ctx.x(), -56.0, ctx.z());
                    ctx.damage("outofworld", 2.0f);
                    ctx.glitch(GlitchIds.BLACKOUT, 40);
                    ctx.msg("§8下面是虚空§7，上面是你不该去的地方。");
                    ctx.addRage(22);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_lightning_crown", "天罚", TrollTier.NIGHTMARE)
                .desc("闪电精准砸在你头顶，附带火焰、耳鸣和满屏白光")
                .weight(30)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.lightning();
                    ctx.setFire(6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.glitch(GlitchIds.FLASHBANG, 20, 2);
                    ctx.msg("§4§l天罚§r§7：你已进入雷神的本周工作列表。");
                    ctx.addRage(20);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_web_choke", "被按在原地", TrollTier.NIGHTMARE)
                .desc("全身被蜘蛛网糊死，头顶再压一层会掉下来的东西")
                .weight(30)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[B_COBWEB]);
                    ctx.setBlock(0, 2, 0, TrollPools.TROLL_BLOCKS[B_GRAVEL]);
                    ctx.setBlock(0, 3, 0, TrollPools.TROLL_BLOCKS[B_SPONGE]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.msg("§7你现在是一件标本§8，请保持安静§7。");
                    ctx.addRage(20);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_storm_siege", "雷暴围城", TrollTier.NIGHTMARE)
                .desc("停电、雷暴、黑夜、负面效果加怪物围殴，一条龙服务")
                .weight(28)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.thunderstorm(1200);
                    ctx.timeOfDay(18000L);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 0);
                    ctx.glitch(GlitchIds.SHAKY, 120, 2);
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    }
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7它们都知道你在哪。");
                    ctx.addRage(25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_time_screw", "时间精神分裂", TrollTier.NIGHTMARE)
                .desc("把时间在正午和午夜之间来回抽三下，顺便让你头晕")
                .weight(30)
                .cooldownSeconds(360)
                .run(ctx -> {
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

        TrollEvents.reg(TrollEvent.of("world_terrain_flatten", "地形改造工程", TrollTier.NIGHTMARE)
                .desc("把你周围一大片地皮直接抹平，施工队拒绝签字")
                .weight(26)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[B_COBBLE], 6);
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[B_DIRT]);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.boomAtPlayer(2.0f);
                    ctx.msg("§7这片地皮已重新规划§8，规划内容：什么都没有§7。");
                    ctx.addRage(22);
                })
                .build());

        // ================================================================
        // APOCALYPSE —— 地狱，会退游戏
        // ================================================================

        TrollEvents.reg(TrollEvent.of("world_bedrock_coffin", "基岩棺材", TrollTier.APOCALYPSE)
                .desc("用基岩把你钉在盒子里，通风口设计得非常人性化（没有）")
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

        TrollEvents.reg(TrollEvent.of("world_void_exile", "流放虚空", TrollTier.APOCALYPSE)
                .desc("抬到世界顶端再一脚踹下去，中途还告诉你存档坏了")
                .weight(22)
                .cooldownSeconds(1200)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 100, 2);
                    ctx.teleport(ctx.x(), 260.0, ctx.z());
                    ctx.launch(-2.5);
                    ctx.thunderstorm(600);
                    ctx.msg("§4§l存档已损坏§r§4，正在把你移出这个世界。");
                    ctx.addRage(40);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_collapse_combo", "世界终章", TrollTier.APOCALYPSE)
                .desc("雷暴加午夜加岩浆加怪物潮加 TNT 烟花，一次性全套端上来")
                .weight(20)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.thunderstorm(2400);
                    ctx.timeOfDay(18000L);
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[B_MAGMA]);
                    for (int i = 0; i < 6; i++) {
                        ctx.setBlock(ctx.ri(-4, 4), ctx.ri(2, 5), ctx.ri(-4, 4),
                                TrollPools.TROLL_BLOCKS[B_TNT]);
                    }
                    for (int i = 0; i < 7; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 120);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.boomAtPlayer(3.0f);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 60, 3);
                    ctx.title("§4§l世界终章", "§8接下来发生的事与你无关");
                    ctx.addRage(45);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("world_save_corrupt_tease", "存档已删除（骗你的）", TrollTier.APOCALYPSE)
                .desc("假崩溃加假装清空世界，整局只来一次，来一次记一辈子")
                .weight(20)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 120, 3);
                    ctx.fakeBroadcast("§4你的存档已被删除。感谢游玩。");
                    ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), false);
                    ctx.clearInventory();
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[B_OBSIDIAN], 3);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.msg("§8……开玩笑的。§7不过背包是真的没了。");
                    ctx.addRage(50);
                })
                .build());
    }
}
