package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【陷阱与机关】分类事件。
 * 负责整活：压力板、绊线、活板门假地板、活塞夹人、发射器齐射、蛛网封锁、仙人掌包围、
 * 火焰路径、岩浆陷阱、铁砧雨、天花板掉落、隐蔽坑洞、假矿诱饵、隐藏 TNT、闭锁房间。
 *
 * <p>本文件刻意不引用任何 Minecraft 类型，全部通过 {@code ctx.*} 与 {@code TrollPools.*} 组合实现。</p>
 */
public final class TrapEvents {

    // ===== TrollPools.TRAP_BLOCKS 固定下标（与 TrollPools 声明顺序一一对应）=====
    private static final int T_PRESSURE = 0;     // Blocks.STONE_PRESSURE_PLATE
    private static final int T_TRIPWIRE = 2;     // Blocks.TRIPWIRE
    private static final int T_DISPENSER = 3;    // Blocks.DISPENSER
    private static final int T_PISTON = 5;       // Blocks.PISTON
    private static final int T_NOTE = 9;         // Blocks.NOTE_BLOCK
    private static final int T_CAULDRON = 10;    // Blocks.CAULDRON
    private static final int T_CAMPFIRE = 11;    // Blocks.CAMPFIRE
    private static final int T_POWDER_SNOW = 14; // Blocks.POWDER_SNOW
    private static final int T_BERRY = 15;       // Blocks.SWEET_BERRY_BUSH
    private static final int T_COBWEB = 16;      // Blocks.COBWEB
    private static final int T_HONEY = 20;       // Blocks.HONEY_BLOCK
    private static final int T_SCAFFOLD = 22;    // Blocks.SCAFFOLDING
    private static final int T_REDSTONE = 24;    // Blocks.REDSTONE_BLOCK
    private static final int T_LEVER = 25;       // Blocks.LEVER

    // ===== TrollPools.TROLL_BLOCKS 固定下标 =====
    private static final int B_COBBLE = 1;       // Blocks.COBBLESTONE
    private static final int B_SAND = 2;         // Blocks.SAND
    private static final int B_GRAVEL = 3;       // Blocks.GRAVEL
    private static final int B_TNT = 16;         // Blocks.TNT
    private static final int B_COBWEB = 17;      // Blocks.COBWEB

    // ===== TrollPools.EVIL_BLOCKS 固定下标 =====
    private static final int E_BEDROCK = 0;      // Blocks.BEDROCK
    private static final int E_OBSIDIAN = 2;     // Blocks.OBSIDIAN

    private TrapEvents() {
    }

    public static void register() {
        TrollEvents.category("陷阱与机关");

        // ================================================================
        // PRANK —— 小整，笑一下就好
        // ================================================================

        TrollEvents.reg(TrollEvent.of("trap_pressure_greeting", "欢迎光临", TrollTier.PRANK)
                .desc("脚下铺一块压力板，配一个机关启动音，其实什么都没发生")
                .weight(170)
                .cooldownSeconds(35)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, TrollPools.TRAP_BLOCKS[T_PRESSURE]);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.actionBar("§7咔哒。");
                    ctx.msg("§7某个机关被触发了§8，具体是哪个我也不知道§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_tripwire_ambush", "满地绊线", TrollTier.BRUTAL)
                .desc("在你周围铺一层绊线，走两步就响一声")
                .weight(150)
                .cooldownSeconds(45)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.scatterBlocks(TrollPools.TRAP_BLOCKS[T_TRIPWIRE], 18);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.6f);
                    ctx.msg("§e地上全是绊线§7，触发一次响一次，祝你好运。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_note_jumpscare", "没人碰它也在响", TrollTier.ANNOYING)
                .desc("四周塞满音符盒，然后把音量一次拉满")
                .weight(150)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.scatterBlocks(TrollPools.TRAP_BLOCKS[T_NOTE], 12);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 1.4f);
                    ctx.msg("§7音乐开始§8，演奏者：不存在的人§7。");
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_falling_scaffold", "头顶施工", TrollTier.ANNOYING)
                .desc("从六格高处砸下一块脚手架，吓你一跳但不疼")
                .weight(140)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.spawnFallingBlock(TrollPools.TRAP_BLOCKS[T_SCAFFOLD]);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.8f, 1.5f);
                    ctx.msg("§7天花板掉了点东西下来§8，质量堪忧§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_falling_sand_puff", "沙包警告", TrollTier.ANNOYING)
                .desc("天上掉一坨沙子，纯粹是为了让你抬头")
                .weight(140)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.spawnFallingBlock(TrollPools.TROLL_BLOCKS[B_SAND]);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.msg("§e天降沙子§7，请自行躲避（躲不躲都一样）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_whisper_corner", "墙角有人说话", TrollTier.BRUTAL)
                .desc("极轻的环境恐怖音，加一句假聊天，纯心理战")
                .weight(150)
                .cooldownSeconds(50)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 0.8f);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "别找了，我不在你后面");
                    ctx.msg("§8你确定刚才没有说话声吗。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_fake_quest_popup", "新任务已接取", TrollTier.ANNOYING)
                .desc("凭空弹出一个根本完不成的任务")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeAchievement("新任务", ctx.pick(TrollPools.FAKE_QUESTS));
                    ctx.actionBar("§7任务进度：§c0%§7（不会涨的）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_fake_achievement_popup", "恭喜获奖", TrollTier.BRUTAL)
                .desc("给你发一个羞辱性的假成就")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_ACHIEVEMENTS), "奖杯材质：空气");
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_ore_tease_sparkle", "这绝对是钻石", TrollTier.ANNOYING)
                .desc("周围瞬间冒出一堆高级矿，但它们一块也带不走")
                .weight(140)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.ORE_TEASE), 14);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 30);
                    ctx.fakeAchievement("找到了不存在的钻石", "奖励：什么都没有");
                    ctx.msg("§e矿就在眼前§8，能不能挖走是你的事§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_fake_save_scare", "正在保存世界", TrollTier.ANNOYING)
                .desc("假装自动保存，顺便给你一句假提示")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.sys(ctx.pick(TrollPools.FAKE_TIPS));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_redstone_click", "机关已启动", TrollTier.ANNOYING)
                .desc("身边铺红石块和拉杆，声音很唬人，功能为零")
                .weight(140)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.scatterBlocks(TrollPools.TRAP_BLOCKS[T_REDSTONE], 6);
                    ctx.setBlock(0, 0, 0, TrollPools.TRAP_BLOCKS[T_LEVER]);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§c机关已启动§7。至于启动了什么——§8保密§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_fake_loot_chest", "路边有箱子", TrollTier.PRANK)
                .desc("骗你去捡东西，然后把你身上的东西改个丢人名字")
                .weight(140)
                .cooldownSeconds(50)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "我在你旁边放了个箱子，快去看");
                    ctx.renameRandomItem(ctx.pick(TrollPools.CURSED_NAMES));
                    ctx.msg("§7箱子？§8不存在的§7。不过你背包里有件东西改名了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_fake_nether_portal", "下界门已激活", TrollTier.BRUTAL)
                .desc("周围撒黑曜石加传送门粒子，然后告诉你还差一块")
                .weight(130)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.scatterBlocks(TrollPools.EVIL_BLOCKS[E_OBSIDIAN], 8);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 0.8f);
                    ctx.msg("§5下界门已激活§8……才怪，你还差§c一块§8黑曜石。");
                })
                .build());

        // ================================================================
        // ANNOYING —— 烦人，会皱眉
        // ================================================================

        TrollEvents.reg(TrollEvent.of("trap_web_ankle", "脚踝上的蛛网", TrollTier.NIGHTMARE)
                .desc("脚下铺满蛛网：你不是不能走，是只能一格一格地挪，而它已经在门口站好了")
                .weight(160)
                .cooldownSeconds(70)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TRAP_BLOCKS[T_COBWEB], 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.msg("§8从你被粘住的那一刻起，你就不再是在逃跑，只是在挪。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_berry_bush_step", "甜甜的陷阱", TrollTier.NIGHTMARE)
                .desc("脚边种满甜浆果丛，每一步都在献血；等你走出去，血条已经不够再跑一次了")
                .weight(150)
                .cooldownSeconds(80)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TRAP_BLOCKS[T_BERRY], 2);
                    ctx.damage("cactus", 2.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.msg("§c浆果：§7甜的。§c代价：§7你的。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_powder_snow_sink", "脚下是雪", TrollTier.NIGHTMARE)
                .desc("地板换成细雪，你会慢慢沉下去、爬不出来，而沉下去的每一秒都在扣血")
                .weight(150)
                .cooldownSeconds(90)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TRAP_BLOCKS[T_POWDER_SNOW], 2);
                    ctx.freeze(60);
                    ctx.damage("freeze", 2.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.msg("§b雪很安静。安静到你能听见自己在往下沉。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_honey_floor", "被地板黏住了", TrollTier.NIGHTMARE)
                .desc("脚下换蜂蜜块，你连转身都要申请审批；而你申请的时候，它已经走到你背后了")
                .weight(150)
                .cooldownSeconds(75)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TRAP_BLOCKS[T_HONEY], 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 220, 1);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                    ctx.msg("§6甜的东西最容易让人走不动。这不是比喻。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_campfire_floor", "免费的篝火", TrollTier.BRUTAL)
                .desc("脚边支起营火，站着不动就算露天烧烤")
                .weight(150)
                .cooldownSeconds(85)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.placeFloor(TrollPools.TRAP_BLOCKS[T_CAMPFIRE], 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§6篝火已点燃§7，请勿离开火堆（也请勿站在火堆里）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_cauldron_soak", "你被泡了", TrollTier.BRUTAL)
                .desc("四周摆满炼药锅，再往你脚下灌水，体验湿身")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.surroundWith(TrollPools.TRAP_BLOCKS[T_CAULDRON]);
                    ctx.placeFloor(TrollPools.EVIL_BLOCKS[4], 1);
                    ctx.msg("§9你已经湿透了§7，这是炼药流程的一部分。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_dispenser_volley", "发射器齐射", TrollTier.BRUTAL)
                .desc("四面架起发射器，再朝你脸上丢一堆投射物")
                .weight(140)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.scatterBlocks(TrollPools.TRAP_BLOCKS[T_DISPENSER], 8);
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE),
                                ctx.x() + ctx.ri(-3, 3), ctx.y() + ctx.ri(0, 2), ctx.z() + ctx.ri(-3, 3));
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§7发射器：§8我们只是打了个招呼§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_piston_sandwich", "活塞三明治", TrollTier.BRUTAL)
                .desc("用活塞把你夹在中间，动一下都费劲")
                .weight(140)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TRAP_BLOCKS[T_PISTON]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 140, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§7活塞三明治已上桌§8，你是夹心§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_freeze_static", "定身术", TrollTier.NIGHTMARE)
                .desc("短时间冻结：手还能动，脚不听使唤，你只能原地等着被处理")
                .weight(140)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.freeze(80);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.6f);
                    ctx.later(60, () -> ctx.damage("magic", 2.0f));
                    ctx.msg("§8你想动。指令发出去了。脚没有收到。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_gravity_float", "失重十秒", TrollTier.NIGHTMARE)
                .desc("让你飘起来，落地方式由重力决定——而重力今天心情不好")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.noGravity(200);
                    ctx.lift(10);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.msg("§7你浮起来了。没有降落伞，也没有人接着你。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_mount_piggy", "强制骑猪", TrollTier.BRUTAL)
                .desc("往你胯下塞一只动物，然后立刻把它收走")
                .weight(140)
                .cooldownSeconds(80)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.mount(ctx.pick(TrollPools.FARM_ANIMALS));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.msg("§e这是你的新坐骑§7，租期§c三秒§7。");
                    ctx.dismount();
                    ctx.msg("§8租期结束。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_void_pit_shallow", "浅坑陷阱", TrollTier.NIGHTMARE)
                .desc("脚下挖穿几层，摔不死，但足够让你在坑底抬头看很久")
                .weight(140)
                .cooldownSeconds(80)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(1, 6);
                    ctx.damage("fall", 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§8坑不深。但爬上去的路，是它给你的下一道题。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_lava_pit_small", "温泉水疗", TrollTier.BRUTAL)
                .desc("脚下铺一格岩浆，配置关闭时会退化成岩浆块（也够疼）")
                .weight(130)
                .cooldownSeconds(100)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.lavaPit(1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§c温泉水疗已开放§7，温度 1200℃，时长不限。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_scatter_junk_blocks", "满地都是石头", TrollTier.NIGHTMARE)
                .desc("周围随机撒一堆方块：挡住你的路、堵住你的门，而且没人会来清理")
                .weight(150)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.TROLL_BLOCKS), 20);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.msg("§7这条路现在是石头的地盘，你只是路过。");
                    ctx.addRage(7);
                })
                .build());

        // ================================================================
        // BRUTAL —— 抓狂，会骂人
        // ================================================================

        TrollEvents.reg(TrollEvent.of("trap_anvil_drop", "铁砧雨", TrollTier.APOCALYPSE)
                .desc("六块铁砧排队砸下来：砸不死，但足够让你记住每一次落点的声音")
                .weight(58)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.meteorShower(6, 18.0);
                    ctx.damage("fall", 2.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.4f, 0.6f);
                    ctx.msg("§8第一块是提醒，第二块是教训，后面四块纯粹是它想砸。");
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_hidden_tnt", "地板下有东西", TrollTier.NIGHTMARE)
                .desc("脚边埋一枚长引信 TNT：它给你足够的时间听完倒计时，也足够的时间想明白跑不掉")
                .weight(55)
                .cooldownSeconds(210)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x() + ctx.rd(-1.0, 1.0), ctx.y(), ctx.z() + ctx.rd(-1.0, 1.0), 50);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.4f, 1.0f);
                    ctx.breakUnder();
                    ctx.msg("§e引信很长。§8长到你有时间后悔刚才那个决定。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_void_pit_deep", "十二格自由落体", TrollTier.APOCALYPSE)
                .desc("脚下挖出十二格深坑：摔下去不会死，但爬上来之前它有的是时间做事")
                .weight(52)
                .cooldownSeconds(220)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(2, 12);
                    ctx.damage("fall", 3.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.msg("§8坑底很安静。安静到你开始怀疑上面已经没人在等你了。");
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_lava_pit_bath", "岩浆浴缸", TrollTier.NIGHTMARE)
                .desc("脚下一整片岩浆，配置关闭也会变成滚烫的岩浆块——总之你脚下不会有好东西")
                .weight(50)
                .cooldownSeconds(230)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.setFire(8);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.7f);
                    ctx.addRage(11);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_freeze_solid", "彻底冻住", TrollTier.APOCALYPSE)
                .desc("长时间冻结：你只能眼睁睁看着它们走过来，一步、一步，走得很慢")
                .weight(50)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.freeze(160);
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    }
                    ctx.damage("freeze", 2.0f);
                    ctx.title("§b§l别动", "§8它们也不急");
                    ctx.addRage(13);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_gravity_off", "重力请了年假", TrollTier.APOCALYPSE)
                .desc("失重两百多 tick，你会一路飘到不该去的高度，然后在它选定的时刻掉下来")
                .weight(50)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.noGravity(420);
                    ctx.lift(30);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 0.8f);
                    ctx.later(120, () -> {
                        ctx.launch(0.5);
                        ctx.msg("§8上升的时候你以为自己是自由的。下降的时候你不会这么想。");
                        ctx.addRage(6);
                    });
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_mount_wild", "随机坐骑体验", TrollTier.APOCALYPSE)
                .desc("强制塞给你一只随机载具，然后告诉你这趟没有刹车，也没有终点站")
                .weight(45)
                .cooldownSeconds(250)
                .run(ctx -> {
                    ctx.mount(ctx.pick(TrollPools.RIDEABLE));
                    ctx.lift(12);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.2f, 0.7f);
                    ctx.msg("§7系好安全带。这里没有安全带。");
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_dismount_sky", "高空强制下车", TrollTier.NIGHTMARE)
                .desc("先把你抬到高空，再把你从坐骑上踹下去：坐骑留在天上，你负责落地")
                .weight(50)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.lift(25);
                    ctx.dismount();
                    ctx.noGravity(40);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.3f, 0.8f);
                    ctx.msg("§8它松手了。你甚至没来得及说再见。");
                    ctx.addRage(11);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_gravel_roof", "天花板要塌了", TrollTier.NIGHTMARE)
                .desc("头顶一层沙砾天花板，全部会掉下来活埋你；而你已经没有时间挖出去了")
                .weight(52)
                .cooldownSeconds(220)
                .run(ctx -> {
                    for (int dx = -2; dx <= 2; dx++) {
                        for (int dz = -2; dz <= 2; dz++) {
                            if (ctx.chance(0.75)) {
                                ctx.setBlock(dx, 4, dz, TrollPools.TROLL_BLOCKS[B_GRAVEL]);
                            }
                        }
                    }
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.msg("§7别抬头。抬了也没用。");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_tiny_swarm", "微型军团", TrollTier.NIGHTMARE)
                .desc("一堆又小又烦的东西把你围住：打不完、甩不掉，而它们有的是时间")
                .weight(50)
                .cooldownSeconds(230)
                .run(ctx -> {
                    for (int i = 0; i < 7; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 5);
                    }
                    ctx.damage("magic", 2.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 0);
                    ctx.msg("§8一只不可怕。七只不需要你觉得可怕，它们只需要你累。");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_swap_trap", "换位陷阱", TrollTier.APOCALYPSE)
                .desc("和另一名玩家原地交换位置：两个人的陷阱都白设了，但只有一个人是被指定的")
                .weight(45)
                .cooldownSeconds(240)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    ctx.swapWith(ctx.other());
                    ctx.damageOther(ctx.other(), 2.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.2f, 0.9f);
                    ctx.msgToOther(ctx.other(), "§c你和某个人换了位置。是你被换过去了。");
                    ctx.msg("§8你站到了他的陷阱上。他站到了你的。");
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_web_slow_cell", "蛛网小房间", TrollTier.APOCALYPSE)
                .desc("用蛛网和石墙围出一个小房间：你在里面慢慢挪，它在外面慢慢等")
                .weight(48)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[B_COBWEB]);
                    ctx.setBlock(0, 3, 0, TrollPools.TROLL_BLOCKS[B_COBBLE]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 280, 1);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.msg("§8房间不大，正好够你一个人待着，也正好够它堵在门口。");
                    ctx.addRage(13);
                })
                .build());

        // ================================================================
        // NIGHTMARE —— 崩溃，会砸键盘
        // ================================================================

        TrollEvents.reg(TrollEvent.of("trap_meteor_barrage", "铁砧暴雨", TrollTier.OBLIVION)
                .desc("十四块铁砧覆盖式轰炸；中途会有个声音告诉你屋棚底下能躲，而那个屋棚就在落点正上方")
                .weight(38)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "往北边那个棚子底下躲！铁砧砸不穿屋顶！");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.5f, 0.6f);
                    ctx.later(40, () -> {
                        ctx.meteorShower(14, 24.0);
                        ctx.damage("fall", 3.0f);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 1);
                        ctx.msg("§8棚子是真的。屋顶也是真的。只是铁砧来得比屋顶结实。");
                        ctx.addRage(16);
                    });
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_tnt_carpet", "TNT 地毯", TrollTier.APOCALYPSE)
                .desc("周围铺满 TNT，再点燃其中三枚：你不知道哪三枚，也不知道该往哪边迈脚")
                .weight(34)
                .cooldownSeconds(420)
                .run(ctx -> {
                    for (int i = 0; i < 8; i++) {
                        ctx.setBlock(ctx.ri(-3, 3), 0, ctx.ri(-3, 3), TrollPools.TROLL_BLOCKS[B_TNT]);
                    }
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnTnt(ctx.x() + ctx.ri(-3, 3), ctx.y(), ctx.z() + ctx.ri(-3, 3), 40 + i * 15);
                    }
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 60);
                    ctx.boomAtPlayer(2.0f);
                    ctx.msg("§c三枚已经在烧了。剩下的五枚在等你想清楚下一步。");
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_void_maw", "深渊大口", TrollTier.APOCALYPSE)
                .desc("脚下挖穿三十格：掉下去基本等于重新开局，只差你自己愿不愿意承认")
                .weight(30)
                .cooldownSeconds(480)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(2, 30);
                    ctx.damage("outofworld", 2.0f);
                    ctx.glitch(GlitchIds.BLACKOUT, 50);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.5f, 0.6f);
                    ctx.msg("§8下面什么都没有。上面也没有人拉你。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_lava_lake", "岩浆湖开业", TrollTier.OBLIVION)
                .desc("脚下一整座岩浆湖；它会先给你指一条安全的路，等你跑到一半再让那条路也烧起来")
                .weight(30)
                .cooldownSeconds(460)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.setFire(10);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "往东跑！东边是实地！");
                    ctx.later(60, () -> {
                        ctx.teleport(ctx.x() + 8, ctx.y(), ctx.z());
                        ctx.lavaPit(2);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 2);
                        ctx.msg("§8东边确实有实地。实地也确实是热的。");
                        ctx.addRage(17);
                    });
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_freeze_lockdown", "完全封锁", TrollTier.APOCALYPSE)
                .desc("长时间冻结加输入冻结：你只能看着屏幕上自己被杀，连视角都不归你管")
                .weight(28)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.freeze(200);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 200, 2);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    }
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 1);
                    ctx.title("§8§l输入已断开", "§7你还在，只是不能动了");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_zero_g_ride", "零重力专车", TrollTier.OBLIVION)
                .desc("失重五百 tick 并持续抬升；中途广播会告诉你终点站到了，然后你才发现「终点站」在半空中")
                .weight(28)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.noGravity(500);
                    ctx.lift(20);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.3f, 0.7f);
                    ctx.later(110, () -> {
                        ctx.sys("§e[车厢广播] 终点站到了，请所有乘客下车。");
                        ctx.lift(15);
                        ctx.msg("§8你四下看了看，没有车站，没有门，也没有「下」这个选项。");
                        ctx.addRage(17);
                    });
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_mount_nightmare", "骑上不该骑的东西", TrollTier.OBLIVION)
                .desc("强制骑上一只精英怪，在它头顶劈一道闪电；你以为它在带你逃，其实它只是在往它家走")
                .weight(26)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.mount(ctx.pick(TrollPools.BOSS));
                    ctx.lightning();
                    ctx.setFire(6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.glitch(GlitchIds.SHAKY, 100, 2);
                    ctx.later(90, () -> {
                        ctx.lift(18);
                        ctx.dismount();
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                        ctx.msg("§8它把你带到了它的家。然后它松开了你。");
                        ctx.addRage(18);
                    });
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_iron_maiden", "铁处女", TrollTier.OBLIVION)
                .desc("黑曜石笼子合拢，脚边埋好 TNT；笼子外面有人喊你的名字说来救你了——那是引信在说话")
                .weight(26)
                .cooldownSeconds(560)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[E_OBSIDIAN]);
                    ctx.setBlock(0, 3, 0, TrollPools.EVIL_BLOCKS[E_BEDROCK]);
                    ctx.spawnTnt(ctx.x(), ctx.y(), ctx.z(), 70);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 340, 2);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 70, 2);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "撑住！我已经看到你了，我马上把你挖出来！");
                    ctx.later(100, () -> {
                        ctx.boomAtPlayer(3.0f);
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "……对不起，我来晚了。");
                        ctx.msg("§8救援是真的。晚也是真的。只剩下这两样。");
                        ctx.addRage(19);
                    });
                    ctx.addRage(15);
                })
                .build());

        // ================================================================
        // APOCALYPSE —— 地狱，会退游戏
        // ================================================================

        TrollEvents.reg(TrollEvent.of("trap_judgement_day", "审判日", TrollTier.OBLIVION)
                .desc("铁砧雨、岩浆湖、假封禁界面；中途会有一次「误判已撤销」的通知，你会相信，然后判决加重")
                .weight(24)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.meteorShower(10, 22.0);
                    ctx.lavaPit(2);
                    ctx.glitch(GlitchIds.FAKE_BAN, 90, 3);
                    ctx.fakeBroadcast("§c[审判] 玩家 " + ctx.playerName() + " 触发了永久封禁程序。");
                    ctx.later(90, () -> {
                        ctx.glitch(GlitchIds.FAKE_UPDATE, 60, 1);
                        ctx.fakeBroadcast("§a[审判] 经复核，本次判决属误判，已撤销。");
                        ctx.playLevelUp();
                        ctx.msg("§7（你松了一口气。这是整场审判里唯一一次你能喘气。）");
                    });
                    ctx.later(180, () -> {
                        ctx.fakeBroadcast("§4[审判] 复核中发现新增罪名。判决撤销无效，立即执行。");
                        ctx.meteorShower(12, 20.0);
                        ctx.setFire(12);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 380, 2);
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 90, 3);
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.addRage(22);
                    });
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_bedrock_tomb", "基岩墓室", TrollTier.OBLIVION)
                .desc("用基岩把你封死在原地，再撤掉全部视野；当你终于看见一条缝的时候，缝也已经被封上了")
                .weight(22)
                .cooldownSeconds(1400)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[E_BEDROCK]);
                    ctx.setBlock(0, 3, 0, TrollPools.EVIL_BLOCKS[E_BEDROCK]);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 90, 3);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 360, 2);
                    ctx.later(120, () -> {
                        ctx.glitch(GlitchIds.BLACKOUT, 40);
                        ctx.msg("§7（墙上出现了一道缝。你朝它伸手。）");
                    });
                    ctx.later(200, () -> {
                        ctx.setBlock(0, 1, 0, TrollPools.EVIL_BLOCKS[E_BEDROCK]);
                        ctx.msg("§8缝没了。它从来就不是给你留的，只是给你看的。");
                        ctx.addRage(10);
                    });
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_void_banish", "虚空流放", TrollTier.OBLIVION)
                .desc("挖穿脚下六十格加失重，直接把你送进虚空；有人会朝你扔下一根绳子，绳子另一头什么也没有")
                .weight(22)
                .cooldownSeconds(1500)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(2, 60);
                    ctx.noGravity(300);
                    ctx.fling(0.0, -1.5, 0.0);
                    ctx.glitch(GlitchIds.BLACKOUT, 80);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "抓住这根绳子！我拉你上来！");
                    ctx.later(90, () -> {
                        ctx.msg("§8绳子下来了。它的另一端，也在往下掉。");
                        ctx.damage("outofworld", 3.0f);
                        ctx.addRage(12);
                    });
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_tnt_chain_reaction", "连锁反应", TrollTier.OBLIVION)
                .desc("六枚不同引信的 TNT 围着你按远近依次开花；拆掉最远那枚之后，它会告诉你「拆弹成功」——然后点亮最近的一枚")
                .weight(20)
                .cooldownSeconds(1600)
                .run(ctx -> {
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnTnt(ctx.x() + ctx.rd(-4.0, 4.0), ctx.y(), ctx.z() + ctx.rd(-4.0, 4.0), 30 + i * 20);
                    }
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 70);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.5f, 0.6f);
                    ctx.msg("§e引信在数。你也在数。你们数的不是同一个数。");
                    ctx.later(110, () -> {
                        ctx.sys("§a[拆弹] 最远端引信已排除，拆弹成功。");
                        ctx.playLevelUp();
                        ctx.msg("§7（你信了。）");
                    });
                    ctx.later(170, () -> {
                        ctx.boomAtPlayer(3.0f);
                        ctx.setFire(10);
                        ctx.stripAll();
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 360, 2);
                        ctx.glitch(GlitchIds.SHAKE_BURST, 80, 3);
                        ctx.msg("§8最后一枚一直在等你放松。它等到了。");
                        ctx.addRage(20);
                    });
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_final_circus", "终极马戏团", TrollTier.APOCALYPSE)
                .desc("铁砧、岩浆、精英坐骑、假封禁、海量刷屏一次性全上——谢幕的时候没人鼓掌，因为观众也只有你")
                .weight(20)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.meteorShower(8, 20.0);
                    ctx.lavaPit(2);
                    ctx.mount(ctx.pick(TrollPools.BOSS));
                    ctx.glitch(GlitchIds.FAKE_BAN, 80, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 120, 3);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 2);
                    ctx.title("§4§l马戏团开场", "§8本场演出的全部节目都由你一个人完成");
                    ctx.addRage(18);
                })
                .build());
    }
}
