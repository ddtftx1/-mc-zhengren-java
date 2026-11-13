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

        TrollEvents.reg(TrollEvent.of("trap_tripwire_ambush", "满地绊线", TrollTier.PRANK)
                .desc("在你周围铺一层绊线，走两步就响一声")
                .weight(150)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.scatterBlocks(TrollPools.TRAP_BLOCKS[T_TRIPWIRE], 18);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.6f);
                    ctx.msg("§e地上全是绊线§7，触发一次响一次，祝你好运。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_note_jumpscare", "没人碰它也在响", TrollTier.PRANK)
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

        TrollEvents.reg(TrollEvent.of("trap_falling_scaffold", "头顶施工", TrollTier.PRANK)
                .desc("从六格高处砸下一块脚手架，吓你一跳但不疼")
                .weight(140)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.spawnFallingBlock(TrollPools.TRAP_BLOCKS[T_SCAFFOLD]);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.8f, 1.5f);
                    ctx.msg("§7天花板掉了点东西下来§8，质量堪忧§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_falling_sand_puff", "沙包警告", TrollTier.PRANK)
                .desc("天上掉一坨沙子，纯粹是为了让你抬头")
                .weight(140)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.spawnFallingBlock(TrollPools.TROLL_BLOCKS[B_SAND]);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.msg("§e天降沙子§7，请自行躲避（躲不躲都一样）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_whisper_corner", "墙角有人说话", TrollTier.PRANK)
                .desc("极轻的环境恐怖音，加一句假聊天，纯心理战")
                .weight(150)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 0.8f);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "别找了，我不在你后面");
                    ctx.msg("§8你确定刚才没有说话声吗。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_fake_quest_popup", "新任务已接取", TrollTier.PRANK)
                .desc("凭空弹出一个根本完不成的任务")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeAchievement("新任务", ctx.pick(TrollPools.FAKE_QUESTS));
                    ctx.actionBar("§7任务进度：§c0%§7（不会涨的）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_fake_achievement_popup", "恭喜获奖", TrollTier.PRANK)
                .desc("给你发一个羞辱性的假成就")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_ACHIEVEMENTS), "奖杯材质：空气");
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_ore_tease_sparkle", "这绝对是钻石", TrollTier.PRANK)
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

        TrollEvents.reg(TrollEvent.of("trap_fake_save_scare", "正在保存世界", TrollTier.PRANK)
                .desc("假装自动保存，顺便给你一句假提示")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.sys(ctx.pick(TrollPools.FAKE_TIPS));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_redstone_click", "机关已启动", TrollTier.PRANK)
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

        TrollEvents.reg(TrollEvent.of("trap_fake_nether_portal", "下界门已激活", TrollTier.PRANK)
                .desc("周围撒黑曜石加传送门粒子，然后告诉你还差一块")
                .weight(130)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.scatterBlocks(TrollPools.EVIL_BLOCKS[E_OBSIDIAN], 8);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 0.8f);
                    ctx.msg("§5下界门已激活§8……才怪，你还差§c一块§8黑曜石。");
                })
                .build());

        // ================================================================
        // ANNOYING —— 烦人，会皱眉
        // ================================================================

        TrollEvents.reg(TrollEvent.of("trap_web_ankle", "脚踝上的蛛网", TrollTier.ANNOYING)
                .desc("脚下铺一整片蛛网，走路像在糖浆里游泳")
                .weight(160)
                .cooldownSeconds(70)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TRAP_BLOCKS[T_COBWEB], 1);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 100, 0);
                    ctx.msg("§7你的移动速度已被模组回收。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_berry_bush_step", "甜甜的陷阱", TrollTier.ANNOYING)
                .desc("脚边种满甜浆果丛，每一步都在献血")
                .weight(150)
                .cooldownSeconds(80)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TRAP_BLOCKS[T_BERRY], 2);
                    ctx.damage("cactus", 1.5f);
                    ctx.msg("§c甜浆果丛已种植§7，请光脚踩进去试试。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_powder_snow_sink", "脚下是雪", TrollTier.ANNOYING)
                .desc("地板换成细雪，你会慢慢沉下去，而且爬不出来")
                .weight(150)
                .cooldownSeconds(90)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TRAP_BLOCKS[T_POWDER_SNOW], 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                    ctx.msg("§f细雪§7：一种会让你怀疑人生的建筑材料。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_honey_floor", "被地板黏住了", TrollTier.ANNOYING)
                .desc("脚下换蜂蜜块，你连转身都要申请审批")
                .weight(150)
                .cooldownSeconds(75)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, TrollPools.TRAP_BLOCKS[T_HONEY]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 1);
                    ctx.msg("§6你被地板黏住了§7，解约流程请咨询模组作者。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_campfire_floor", "免费的篝火", TrollTier.ANNOYING)
                .desc("脚边支起营火，站着不动就算露天烧烤")
                .weight(150)
                .cooldownSeconds(85)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TRAP_BLOCKS[T_CAMPFIRE], 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§6篝火已点燃§7，请勿离开火堆（也请勿站在火堆里）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_cauldron_soak", "你被泡了", TrollTier.ANNOYING)
                .desc("四周摆满炼药锅，再往你脚下灌水，体验湿身")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TRAP_BLOCKS[T_CAULDRON]);
                    ctx.placeFloor(TrollPools.EVIL_BLOCKS[4], 1);
                    ctx.msg("§9你已经湿透了§7，这是炼药流程的一部分。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_dispenser_volley", "发射器齐射", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("trap_piston_sandwich", "活塞三明治", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("trap_freeze_static", "定身术", TrollTier.ANNOYING)
                .desc("短时间冻结，手还能动，脚不听使唤")
                .weight(140)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.freeze(120);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.6f);
                    ctx.msg("§b你的双腿已被模组临时接管。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_gravity_float", "失重十秒", TrollTier.ANNOYING)
                .desc("让你飘起来，落地方式由重力决定")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.noGravity(100);
                    ctx.msg("§7重力已下线§8，坠机责任自负§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_mount_piggy", "强制骑猪", TrollTier.ANNOYING)
                .desc("往你胯下塞一只动物，然后立刻把它收走")
                .weight(140)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.mount(ctx.pick(TrollPools.FARM_ANIMALS));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.msg("§e这是你的新坐骑§7，租期§c三秒§7。");
                    ctx.dismount();
                    ctx.msg("§8租期结束。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_void_pit_shallow", "浅坑陷阱", TrollTier.ANNOYING)
                .desc("脚下挖一个三格浅坑，摔不死但很丢人")
                .weight(140)
                .cooldownSeconds(80)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(1, 3);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§7这是一个坑§8，字面意义上的§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_lava_pit_small", "温泉水疗", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("trap_scatter_junk_blocks", "满地都是石头", TrollTier.ANNOYING)
                .desc("周围随机撒一堆方块，挡住你的路还不给理由")
                .weight(150)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.scatterBlocks(TrollPools.TROLL_BLOCKS[B_COBBLE], 24);
                    ctx.msg("§7你周围多了一堆§8圆石§7，搬运费自理。");
                })
                .build());

        // ================================================================
        // BRUTAL —— 抓狂，会骂人
        // ================================================================

        TrollEvents.reg(TrollEvent.of("trap_anvil_drop", "铁砧雨", TrollTier.BRUTAL)
                .desc("六块铁砧从天上排队砸下来，砸不死但很疼")
                .weight(58)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.meteorShower(6, 18);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 40);
                    ctx.title("§c§l低头", "§7天上有点东西。");
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_hidden_tnt", "地板下有东西", TrollTier.BRUTAL)
                .desc("往你脚边埋一枚带长引信的 TNT，让你听完倒计时")
                .weight(55)
                .cooldownSeconds(210)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(1, 2);
                    ctx.spawnTnt(ctx.x(), ctx.y(), ctx.z(), 70);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.4f);
                    ctx.msg("§c滴。§e滴。§c滴。§7——你还有三秒半。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_void_pit_deep", "十二格自由落体", TrollTier.BRUTAL)
                .desc("脚下挖出十二格深的坑，爬上来是你的问题")
                .weight(52)
                .cooldownSeconds(220)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(2, 12);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§8坑已挖好§7，深度十二格，梯子自备。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_lava_pit_bath", "岩浆浴缸", TrollTier.BRUTAL)
                .desc("脚下一整片岩浆，配置关闭也会变成滚烫的岩浆块")
                .weight(50)
                .cooldownSeconds(230)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.setFire(6);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§4岩浆浴缸已注水§7，请勿在里面呼吸。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_freeze_solid", "彻底冻住", TrollTier.BRUTAL)
                .desc("长时间冻结，你只能眼睁睁看着怪走过来")
                .weight(50)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.freeze(320);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 80, 1);
                    ctx.msg("§b你已被冻结§8，思考是唯一被允许的动作§7。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_gravity_off", "重力请了年假", TrollTier.BRUTAL)
                .desc("失重两百多 tick，你会飘到不该去的高度")
                .weight(50)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.noGravity(220);
                    ctx.launch(0.9);
                    ctx.msg("§7重力暂时离职§8，回来时会通知你的落点§7。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_mount_wild", "随机坐骑体验", TrollTier.BRUTAL)
                .desc("强制塞给你一只随机载具，包含 TNT 这种离谱选项")
                .weight(45)
                .cooldownSeconds(250)
                .run(ctx -> {
                    ctx.mount(ctx.pick(TrollPools.RIDEABLE));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§e你的新坐骑已到货§7，请不要问它是什么。");
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_dismount_sky", "高空强制下车", TrollTier.BRUTAL)
                .desc("先把你抬到高空，再把你从坐骑上踹下去")
                .weight(50)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.mount(ctx.pick(TrollPools.RIDEABLE));
                    ctx.lift(45);
                    ctx.dismount();
                    ctx.title("§c§l已下车", "§7地面正在接近");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_gravel_roof", "天花板要塌了", TrollTier.BRUTAL)
                .desc("头顶一层沙砾天花板，全部会掉下来活埋你")
                .weight(52)
                .cooldownSeconds(220)
                .run(ctx -> {
                    for (int dx = -2; dx <= 2; dx++) {
                        for (int dz = -2; dz <= 2; dz++) {
                            ctx.setBlock(dx, ctx.ri(4, 7), dz, TrollPools.TROLL_BLOCKS[B_GRAVEL]);
                        }
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.msg("§7天花板正在办理§8离职§7手续。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_tiny_swarm", "微型军团", TrollTier.BRUTAL)
                .desc("一堆又小又烦的生物把你围起来，打不完也甩不掉")
                .weight(50)
                .cooldownSeconds(230)
                .run(ctx -> {
                    for (int i = 0; i < 8; i++) {
                        ctx.giveMobCustomName(
                                ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING),
                                        ctx.x() + ctx.ri(-3, 3), ctx.y() + ctx.ri(0, 2), ctx.z() + ctx.ri(-3, 3)),
                                "§8模组的实习生");
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.4f);
                    ctx.msg("§7它们没有工资§8，所以特别有干劲§7。");
                    ctx.addRage(13);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_swap_trap", "换位陷阱", TrollTier.BRUTAL)
                .desc("和另一名玩家原地交换位置，两个人的陷阱都白设了")
                .weight(45)
                .cooldownSeconds(240)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    var o = ctx.other();
                    if (o == null) {
                        return;
                    }
                    ctx.meteorShower(4, 12);
                    ctx.swapWith(o);
                    ctx.sendToAll("§8[整人大师] §7有人踩到了换位陷阱。");
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_web_slow_cell", "蛛网小房间", TrollTier.BRUTAL)
                .desc("用蛛网和石墙围出一个小房间，你在里面慢慢挪")
                .weight(48)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.TROLL_BLOCKS[B_COBWEB]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.msg("§7这是你的房间§8，物业费已收§7。");
                    ctx.addRage(16);
                })
                .build());

        // ================================================================
        // NIGHTMARE —— 崩溃，会砸键盘
        // ================================================================

        TrollEvents.reg(TrollEvent.of("trap_meteor_barrage", "铁砧暴雨", TrollTier.NIGHTMARE)
                .desc("十四块铁砧从高空覆盖式轰炸，躲哪儿都挨一下")
                .weight(38)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.meteorShower(14, 28);
                    ctx.glitch(GlitchIds.SHAKY, 100, 2);
                    ctx.title("§4§l天上下铁了", "§7没有伞能救你。");
                    ctx.addRage(24);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_tnt_carpet", "TNT 地毯", TrollTier.NIGHTMARE)
                .desc("周围铺满 TNT 方块，再点燃其中三枚")
                .weight(34)
                .cooldownSeconds(420)
                .run(ctx -> {
                    for (int i = 0; i < 8; i++) {
                        ctx.setBlock(ctx.ri(-3, 3), 0, ctx.ri(-3, 3), TrollPools.TROLL_BLOCKS[B_TNT]);
                    }
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnTnt(ctx.x() + ctx.ri(-3, 3), ctx.y() + 1, ctx.z() + ctx.ri(-3, 3), ctx.ri(40, 70));
                    }
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.8f);
                    ctx.msg("§c地毯是可燃的§7，这是我最后一次提醒你。");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_void_maw", "深渊大口", TrollTier.NIGHTMARE)
                .desc("脚下挖穿三十格，掉下去基本等于重新开局")
                .weight(30)
                .cooldownSeconds(480)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(3, 30);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.BLACKOUT, 60);
                    ctx.msg("§8你脚下的世界被删掉了三十层§7。");
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_lava_lake", "岩浆湖开业", TrollTier.NIGHTMARE)
                .desc("脚下一整座岩浆湖，站上去的每一秒都在烧钱（和血）")
                .weight(30)
                .cooldownSeconds(460)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.lavaPit(3);
                    ctx.setFire(12);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.7f);
                    ctx.msg("§4岩浆湖已营业§7，退票窗口在岩浆底部。");
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_freeze_lockdown", "完全封锁", TrollTier.NIGHTMARE)
                .desc("长时间冻结加输入冻结，你只能看着屏幕上自己被杀")
                .weight(28)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.freeze(700);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 200, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.title("§b§l已封锁", "§7键盘归模组管了。");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_zero_g_ride", "零重力专车", TrollTier.NIGHTMARE)
                .desc("失重四百多 tick 并持续抬升，直到世界尽头")
                .weight(28)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.noGravity(420);
                    ctx.lift(60);
                    ctx.glitch(GlitchIds.FOV_WILD, 120, 2);
                    ctx.msg("§7本次航班不提供降落服务§8，谢谢乘坐§7。");
                    ctx.addRage(25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_mount_nightmare", "骑上不该骑的东西", TrollTier.NIGHTMARE)
                .desc("强制骑上一只精英怪，然后在它头顶劈一道闪电")
                .weight(26)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.mount(ctx.pick(TrollPools.BOSS));
                    ctx.lightning();
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.9f);
                    ctx.title("§4§l祝旅途愉快", "§7它好像不太高兴。");
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_iron_maiden", "铁处女", TrollTier.NIGHTMARE)
                .desc("黑曜石笼子加脚边 TNT，你被关在自己的爆炸现场里")
                .weight(26)
                .cooldownSeconds(560)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[E_OBSIDIAN]);
                    ctx.lavaPit(1);
                    ctx.spawnTnt(ctx.x(), ctx.y(), ctx.z(), 60);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 80, 2);
                    ctx.msg("§8你的房间已经准备好了§7，请享受私人空间。");
                    ctx.addRage(30);
                })
                .build());

        // ================================================================
        // APOCALYPSE —— 地狱，会退游戏
        // ================================================================

        TrollEvents.reg(TrollEvent.of("trap_judgement_day", "审判日", TrollTier.APOCALYPSE)
                .desc("铁砧雨加岩浆湖加假封禁界面，整局只来一次")
                .weight(24)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_BAN, 120, 3);
                    ctx.meteorShower(16, 32);
                    ctx.lavaPit(3);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.sendToAll("§4[服务器] §c陷阱系统已进入最终阶段。");
                    ctx.titleToAll("§4§l审判日", "§8陷阱已全部激活");
                    ctx.addRage(45);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_bedrock_tomb", "基岩墓室", TrollTier.APOCALYPSE)
                .desc("用基岩把你封死在原地，再送你长时间黑屏")
                .weight(22)
                .cooldownSeconds(1400)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[E_BEDROCK]);
                    ctx.freeze(900);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 200, 3);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 600, 1);
                    ctx.title("§4§l墓室已封闭", "§8出口正在施工（没有施工队）");
                    // 保底出口：基岩封死 + 压低血量 + 着火，保证玩家一定会死亡重生，不会永久卡在墓室里
                    ctx.setHealth(6.0f);
                    ctx.setFire(20);
                    ctx.starve();
                    ctx.addRage(42);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_void_banish", "虚空流放", TrollTier.APOCALYPSE)
                .desc("挖穿六十格深坑加失重，把你直接送进虚空")
                .weight(22)
                .cooldownSeconds(1500)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(3, 60);
                    ctx.noGravity(300);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 120, 3);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.msg("§4§l你已被流放§r§8，虚空负责接待。");
                    ctx.addRage(44);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_tnt_chain_reaction", "连锁反应", TrollTier.APOCALYPSE)
                .desc("六枚不同引信的 TNT 围着你，按远近依次开花")
                .weight(20)
                .cooldownSeconds(1600)
                .run(ctx -> {
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnTnt(ctx.x() + ctx.ri(-5, 5), ctx.y() + ctx.ri(1, 4), ctx.z() + ctx.ri(-5, 5),
                                20 + i * 12);
                    }
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.7f);
                    ctx.title("§c§l倒计时", "§7数一数还有几声。");
                    ctx.addRage(40);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trap_final_circus", "终极马戏团", TrollTier.APOCALYPSE)
                .desc("铁砧、岩浆、精英坐骑、假封禁、海量刷屏，一次性全上")
                .weight(20)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.meteorShower(18, 34);
                    ctx.lavaPit(3);
                    ctx.mount(ctx.pick(TrollPools.BOSS));
                    ctx.spawnTnt(ctx.x() + 4, ctx.y() + 3, ctx.z() + 4, 60);
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 100, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 140, 3);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.titleToAll("§4§l终极马戏团", "§8请把票根交给下一个受害者");
                    ctx.sendToAll("§8[模组] §7" + ctx.pick(TrollPools.FAKE_OPERATOR_LINES));
                    ctx.addRage(50);
                })
                .build());
    }
}
