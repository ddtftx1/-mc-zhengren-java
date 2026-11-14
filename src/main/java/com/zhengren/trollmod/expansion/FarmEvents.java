package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【农田与畜牧】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。</p>
 *
 * <p>与原生事件文件一致，本文件刻意**不引用任何 Minecraft 类型**，
 * 全部通过 {@code ctx.*} 与 {@code TrollPools.*} 组合实现。</p>
 */
public final class FarmEvents {

    private FarmEvents() {
    }

    public static void register() {
        TrollEvents.category("农田与畜牧");

        // ================================================================
        // PRANK —— 小整，笑一下就好（18 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("farm_chicken_census", "鸡口普查", TrollTier.PRANK)
                .desc("凭空出现一只鸡，自带工号，并开始记录你")
                .weight(150)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.giveMobCustomName(
                            ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                    ctx.x() + ctx.ri(-3, 3), ctx.y(), ctx.z() + ctx.ri(-3, 3)),
                            "§e第 " + ctx.ri(1, 999) + " 号鸡");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.actionBar("§7普查员已就位，§8请问你养了几只？§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_wheat_gossip", "麦田八卦", TrollTier.PRANK)
                .desc("你身后的麦子开始小声议论你的操作")
                .weight(145)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "听说他连锄头都拿反了");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.7f, 1.6f);
                    ctx.msg("§7麦穗齐刷刷转向你§8，像是在确认传闻。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_cow_stare", "牛的凝视", TrollTier.PRANK)
                .desc("一头牛静静盯着你，直到你忍不住回头看它")
                .weight(140)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.giveMobCustomName(
                            ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                    ctx.x() + ctx.ri(-2, 2), ctx.y(), ctx.z() + ctx.ri(-2, 2)),
                            "§7审判者");
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 0.9f);
                    ctx.msg("§7它什么都没说，§8但你已经输了§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_egg_donation", "天降鸡蛋", TrollTier.PRANK)
                .desc("从天上匀几个鸡蛋给你，外壳普遍有裂缝")
                .weight(140)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), ctx.ri(2, 5));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.msg("§e鸡蛋已空投§7，签收即视为接受§8碎裂风险§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_rabbit_thief", "兔子偷菜", TrollTier.PRANK)
                .desc("一只兔子光明正大从你田里搬走一颗菜，眼神很坦然")
                .weight(135)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.giveMobCustomName(
                            ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                    ctx.x() + ctx.ri(-4, 4), ctx.y(), ctx.z() + ctx.ri(-4, 4)),
                            "§7惯犯");
                    ctx.msg("§7它抱着你的菜走了过去§8，还回头看了你一眼§7。");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_bee_greeting", "蜜蜂认亲", TrollTier.PRANK)
                .desc("一只蜜蜂当众认定你是它失散多年的远房亲戚")
                .weight(130)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.giveMobCustomName(
                            ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING),
                                    ctx.x() + ctx.ri(-2, 2), ctx.y() + 2, ctx.z() + ctx.ri(-2, 2)),
                            "§e表舅");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.8f);
                    ctx.msg("§e它绕着你飞了三圈§7，礼数相当周全。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_scarecrow_applause", "稻草人的掌声", TrollTier.PRANK)
                .desc("田里的稻草人给了你一段敷衍到极点的掌声")
                .weight(125)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.6f, 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.5f, 1.7f);
                    ctx.actionBar("§7（远处传来两声干巴巴的拍手）");
                    ctx.msg("§7稻草人表示：§8演得不错§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_pumpkin_coronation", "南瓜加冕", TrollTier.PRANK)
                .desc("一颗看不见的南瓜在你头顶完成了加冕仪式")
                .weight(120)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.title("§6§l万圣加冕", "§7王冠是南瓜做的");
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 25);
                    ctx.msg("§6恭喜你成为§e本季南瓜王§6，§8任期到今晚。§6");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_sheep_lullaby", "数羊助眠", TrollTier.PRANK)
                .desc("眼前飘过一群羊，但没人告诉你该数到几")
                .weight(120)
                .cooldownSeconds(75)
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                ctx.x() + ctx.ri(-4, 4), ctx.y() + ctx.ri(3, 6), ctx.z() + ctx.ri(-4, 4));
                    }
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 1.0f);
                    ctx.actionBar("§7一只羊…两只羊…§8你数不清了§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hoe_squeak", "锄头会唱歌", TrollTier.PRANK)
                .desc("你的锄头突然发出很难听的摩擦声，并有了艺名")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.renameHeld("§e会唱歌的锄头");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.6f);
                    ctx.msg("§7你的工具申请了§e艺名§7，理由是§8为了提高产量§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_compost_aroma", "堆肥的芬芳", TrollTier.PRANK)
                .desc("一股浓烈的有机气息精准笼罩你，风向很配合")
                .weight(115)
                .cooldownSeconds(65)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 30);
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 100, 0);
                    ctx.msg("§7空气突然很有营养§8，深呼吸是你的自由。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_drizzle", "局部灌溉", TrollTier.PRANK)
                .desc("只有你头顶这一小块地方在下雨，边界十分明确")
                .weight(115)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.setRaining(true, false, 240);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§9灌溉范围：§7以你为圆心，半径§b一格§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_sugarcane_dance", "甘蔗摇摆", TrollTier.PRANK)
                .desc("甘蔗们跟着节奏整齐摇摆，只有你不在节奏里")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.randomRotation();
                    ctx.actionBar("§a甘蔗：§7左右左右左右§8，你踩错了§a。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_berry_burst", "浆果爆浆", TrollTier.PRANK)
                .desc("你口袋里的一颗浆果自行爆开，位置相当刁钻")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.5f);
                    ctx.msg("§c浆果已就地牺牲§7，§8它选择死在你身上。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_frog_choir", "青蛙合唱团", TrollTier.PRANK)
                .desc("三只青蛙在你脚边开了一场水平堪忧的小型音乐会")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING),
                                ctx.x() + ctx.ri(-3, 3), ctx.y(), ctx.z() + ctx.ri(-3, 3));
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.9f, 0.7f);
                    ctx.msg("§a呱。§7呱。§8呱。§7感谢聆听，§c谢绝退票§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_trivia", "农业小知识", TrollTier.PRANK)
                .desc("系统突然推送一条毫无用处、但确实是真的农业冷知识")
                .weight(105)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.sys("§b[农技站] §7" + ctx.pick(TrollPools.FAKE_TIPS));
                    ctx.actionBar("§8（本条知识对产量没有任何帮助）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_llama_opinion", "羊驼的看法", TrollTier.PRANK)
                .desc("一只路过的羊驼用它的方式，清晰表达了它的看法")
                .weight(105)
                .cooldownSeconds(65)
                .run(ctx -> {
                    ctx.giveMobCustomName(
                            ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                    ctx.x() + ctx.ri(-3, 3), ctx.y(), ctx.z() + ctx.ri(-3, 3)),
                            "§7评论家");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.msg("§7羊驼对你吐了一口§8，评价委婉但明确§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hay_pillow", "干草枕头", TrollTier.PRANK)
                .desc("你头上多了一顶不太服帖的干草帽，摘不掉的那种存在感")
                .weight(100)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 15);
                    ctx.msg("§e干草帽已到货§7，§8尺寸是农场的尺寸。§7");
                })
                .build());

        // ================================================================
        // ANNOYING —— 烦人，会皱眉（32 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("farm_fence_pen", "临时畜栏", TrollTier.ANNOYING)
                .desc("用栅栏把你原地围一圈，规格按牲畜标准执行")
                .weight(145)
                .cooldownSeconds(80)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.surroundWith(ctx.pick(TrollPools.TRAP_BLOCKS));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.msg("§7新圈舍已交付§8，你被划分为§7存栏§8那一类。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_bucket_dunk", "水桶扣头", TrollTier.ANNOYING)
                .desc("你要浇地的那桶水，最终浇在了你自己身上")
                .weight(140)
                .cooldownSeconds(85)
                .run(ctx -> {
                    ctx.placeFloor(ctx.pick(TrollPools.NASTY_BLOCKS), 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§9水桶表示：§7它更想待在你头上§8，这是它的自由。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_seed_spill", "种子撒了一地", TrollTier.ANNOYING)
                .desc("你手里的种子突然集体越狱，铺满附近地面")
                .weight(140)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.TROLL_BLOCKS), 12);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.msg("§7种子已散落§8，请在一分钟内捡完§7（没有一分钟）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_scarecrow_swap", "稻草人换位", TrollTier.ANNOYING)
                .desc("你和田里那个稻草人在空间上发生了点误会")
                .weight(135)
                .cooldownSeconds(95)
                .run(ctx -> {
                    ctx.scatter(35);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.msg("§7你已经站到了稻草人的岗位上§8，请保持姿势。§7");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hoe_confiscated", "农具被没收", TrollTier.ANNOYING)
                .desc("村委会认为你的农具不符合规范，当场没收一件")
                .weight(135)
                .cooldownSeconds(90)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "农具我先替你保管，别谢我");
                    ctx.msg("§7没收单号：§8#" + (ctx.gameTicks() % 1000) + "§7。申诉渠道：没有。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_harvest_shuffle", "收成调包", TrollTier.ANNOYING)
                .desc("你的全部家当被重新洗了一遍，顺序全凭天意")
                .weight(130)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.shuffleInventory();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§7粮仓管理员表示：§8这叫科学堆放§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hotbar_eggs", "快捷栏孵蛋", TrollTier.ANNOYING)
                .desc("快捷栏被强制重新排布，还塞进来几颗来路不明的蛋")
                .weight(130)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.give(ctx.pick(TrollPools.JUNK), 3);
                    ctx.msg("§e额外附赠鸡蛋若干§7，§8请自行辨认哪些是石头。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_chicken_swarm", "鸡群入侵", TrollTier.ANNOYING)
                .desc("六只鸡同时认定你的脚边是唯一的家")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                ctx.x() + ctx.ri(-4, 4), ctx.y(), ctx.z() + ctx.ri(-4, 4));
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.msg("§e咯咯咯×6§7。它们已经在这里定居了。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_bee_irritated", "蜜蜂不高兴", TrollTier.ANNOYING)
                .desc("蜂箱里的住户对你的存在表达了强烈抗议，方式比较直接")
                .weight(130)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 6);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.5f);
                    ctx.actionBar("§e嗡嗡声正在快速接近，§7请勿挥手。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_pig_ice_rink", "猪圈溜冰场", TrollTier.ANNOYING)
                .desc("猪圈地面被换成冰面，猪和你都没准备好")
                .weight(125)
                .cooldownSeconds(100)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(ctx.pick(TrollPools.TROLL_BLOCKS), 2);
                    ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS), ctx.x() + 1, ctx.y(), ctx.z());
                    ctx.launch(0.5);
                    ctx.msg("§b猪：§7我先滑为敬。§8你：§7我不会滑。§b");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_cowbell_alarm", "牛铃警报", TrollTier.ANNOYING)
                .desc("四面同时响起牛铃，音量按重大事故级别配置")
                .weight(125)
                .cooldownSeconds(95)
                .run(ctx -> {
                    ctx.soundAt(ctx.pick(TrollPools.IMPACT), ctx.x() + 2, ctx.y(), ctx.z(), 1.0f, 1.6f);
                    ctx.soundAt(ctx.pick(TrollPools.IMPACT), ctx.x() - 2, ctx.y(), ctx.z(), 1.0f, 1.4f);
                    ctx.title("§6§l叮——", "§7牧场警报：假警报，但很响");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_sheared", "被剃了羊毛", TrollTier.ANNOYING)
                .desc("醒来发现自己少了一圈保暖层，旁边多了一堆羊毛")
                .weight(125)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                    ctx.give(ctx.pick(TrollPools.JUNK), 4);
                    ctx.msg("§f羊毛已回收§7，§8按批发价结算，你一分钱拿不到。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_mud_wallow", "泥坑体验", TrollTier.ANNOYING)
                .desc("脚下突然变得很有营养，附赠一点生理不适")
                .weight(120)
                .cooldownSeconds(95)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(ctx.pick(TrollPools.NASTY_BLOCKS), 2);
                    ctx.effectRandom(100, 0);
                    ctx.msg("§8泥浆§7已到位§8，猪对此表示非常羡慕。§7");
                    ctx.addRage(4);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_melon_face", "西瓜糊脸", TrollTier.ANNOYING)
                .desc("你手里的工具被换成了一整个西瓜，手感完全不同")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.renameHeld("§2一整个西瓜");
                    ctx.setCooldown(ctx.pick(TrollPools.JUNK), 120);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§2西瓜已上岗§7，§8它对这个职位一无所知。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_compost_theft", "肥料失窃", TrollTier.ANNOYING)
                .desc("堆肥桶一夜之间空了，还给你留了一封道歉信")
                .weight(120)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.giveRandomJunk();
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "肥料我拿走了，你那堆土其实挺好的");
                    ctx.msg("§7桶还在§8，内容物已出差。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_fence_maze", "栅栏迷宫", TrollTier.ANNOYING)
                .desc("你身边长出一圈栅栏，出口的设计充满了幽默感")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    for (int dx = -2; dx <= 2; dx++) {
                        ctx.setBlock(dx, 1, -2, ctx.pick(TrollPools.TRAP_BLOCKS));
                        ctx.setBlock(dx, 1, 2, ctx.pick(TrollPools.TRAP_BLOCKS));
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.1f);
                    ctx.msg("§7迷宫已生成§8，难度：幼儿园；出口：随缘。§7");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_cursed_hoe", "被诅咒的锄头", TrollTier.ANNOYING)
                .desc("你的农具获得了一份不太正派的附魔，还带着低语")
                .weight(115)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.curseHeld();
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 0.8f);
                    ctx.warn("§c你的工具开始说一些不该说的话。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_drought_notice", "干旱通知", TrollTier.ANNOYING)
                .desc("天气被强行清空，土壤和你的嗓子一起发干")
                .weight(115)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.clearWeather();
                    ctx.effectRandom(200, 0);
                    ctx.sys("§6[气象站] §7未来一周：晴。再一周：还是晴。");
                    ctx.addRage(4);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_locust_scout", "蝗虫侦查", TrollTier.ANNOYING)
                .desc("几只先遣蝗虫在你头顶盘旋，负责评估此地口粮")
                .weight(115)
                .cooldownSeconds(105)
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING),
                                ctx.x() + ctx.ri(-4, 4), ctx.y() + ctx.ri(2, 5), ctx.z() + ctx.ri(-4, 4));
                    }
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.9f, 1.4f);
                    ctx.msg("§8侦察完毕§7，报告结论：§c可以吃§7。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_bonemeal_backfire", "骨粉反噬", TrollTier.ANNOYING)
                .desc("你用骨粉催熟作物，催熟的其实是自己")
                .weight(115)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.lift(4);
                    ctx.effectRandom(140, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.6f);
                    ctx.msg("§a生长加速成功§7，方向：§c向上§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_bee_in_hat", "帽子里有蜜蜂", TrollTier.ANNOYING)
                .desc("你听见帽子里传来一阵很有耐心的嗡嗡声")
                .weight(110)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SHAKY, 60, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.7f);
                    ctx.actionBar("§e嗡……§7嗡……§8它不打算出来。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_trough_jelly", "饲料槽变果冻", TrollTier.ANNOYING)
                .desc("脚下的饲料槽获得了弹性，你获得了垂直位移")
                .weight(110)
                .cooldownSeconds(95)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.launch(0.9);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§a饲料槽已升级为§l弹性版§r§a，配合胃部使用更佳。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_animal_strike", "牲畜罢工", TrollTier.ANNOYING)
                .desc("牧场全体成员停下工作，并推举了四位代表围住你")
                .weight(110)
                .cooldownSeconds(130)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.giveMobCustomName(
                                ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                        ctx.x() + ctx.ri(-3, 3), ctx.y(), ctx.z() + ctx.ri(-3, 3)),
                                "§7罢工代表");
                    }
                    ctx.fakeBroadcast("§e[牧场工会] 在你恢复产粮之前，我们拒绝产奶。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_milking_prank", "挤奶惊魂", TrollTier.ANNOYING)
                .desc("你伸手去挤奶，接到的东西明显不是牛奶")
                .weight(105)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.msg("§7桶里的液体拒绝了§f牛奶§7这个身份。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_wheat_market", "小麦期货暴跌", TrollTier.ANNOYING)
                .desc("你刚收的小麦遇到历史性行情，服务器还郑重通知了你")
                .weight(105)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.fakeBroadcast("§8[交易所] 小麦价格下跌 99%，原因：收成太好。");
                    ctx.sys("§7你的库存估值：§c约等于零§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hay_bounce", "干草垛弹跳", TrollTier.ANNOYING)
                .desc("干草垛突然表现出极佳的弹性，你被就地发射")
                .weight(105)
                .cooldownSeconds(100)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.launch(1.3);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§e干草垛：§7弹性优秀§8，安全性概不负责。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_egg_bombardment", "鸡蛋轰炸", TrollTier.ANNOYING)
                .desc("天上开始密集投放鸡蛋，落点经过精心计算")
                .weight(100)
                .cooldownSeconds(110)
                .run(ctx -> {
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE),
                                ctx.x() + ctx.ri(-3, 3), ctx.y() + ctx.ri(8, 12), ctx.z() + ctx.ri(-3, 3));
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.msg("§e空袭警报：§7弹药为鸡蛋§8，杀伤力为侮辱性。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_rabbit_burrow", "脚下一窝兔子", TrollTier.ANNOYING)
                .desc("你脚下的地面塌了，洞里的住户对此毫无歉意")
                .weight(100)
                .cooldownSeconds(110)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.breakUnder();
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                ctx.x() + ctx.ri(-2, 2), ctx.y(), ctx.z() + ctx.ri(-2, 2));
                    }
                    ctx.msg("§7兔子：§8你踩到我们家天花板了§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_potato_rain", "土豆雨", TrollTier.ANNOYING)
                .desc("头顶开始有规律地落下土豆，落速符合物理但不符人心")
                .weight(100)
                .cooldownSeconds(110)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnFallingBlock(ctx.pick(TrollPools.TROLL_BLOCKS));
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§6局部地区有§e土豆§6，请勿用头接。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_wool_sticky", "羊毛粘脚", TrollTier.ANNOYING)
                .desc("你踩进一团羊毛，从那以后走路都带着阻力")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 180, 0);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.msg("§f羊毛粘性测试通过§7，§8受试者：你。§7");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_crop_circle", "农田怪圈", TrollTier.ANNOYING)
                .desc("你的田里一夜之间出现了麦田怪圈，圈就在你脚下")
                .weight(95)
                .cooldownSeconds(130)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    for (int i = 0; i < 10; i++) {
                        double a = i * Math.PI / 5.0;
                        ctx.setBlock((int) Math.round(Math.cos(a) * 3.0), 0,
                                (int) Math.round(Math.sin(a) * 3.0), ctx.pick(TrollPools.TROLL_BLOCKS));
                    }
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                    ctx.msg("§7怪圈已完成§8，外星人表示这次只是路过。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_composter_swap", "堆肥桶换位", TrollTier.ANNOYING)
                .desc("你和某位同伴在堆肥桶旁边互换了人生位置")
                .weight(95)
                .cooldownSeconds(130)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    ctx.swapWith(ctx.other());
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.msg("§7换位成功§8，你们互相继承了对方面前的空气。§7");
                    ctx.addRage(6);
                })
                .build());

        // ================================================================
        // BRUTAL —— 抓狂，会骂人（27 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("farm_cattle_rustle", "牲畜大盗", TrollTier.BRUTAL)
                .desc("有人趁你喂牛的时候，把牛和你身上值钱的东西一起牵走了")
                .weight(60)
                .cooldownSeconds(200)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.takeRandomItem();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.warn("§c你的牧场刚刚完成了一次非自愿的产权转移。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_stampede", "牛群踩踏", TrollTier.BRUTAL)
                .desc("八头牛同时决定朝你所在的方向奔跑，纯属巧合")
                .weight(58)
                .cooldownSeconds(210)
                .run(ctx -> {
                    for (int i = 0; i < 8; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                ctx.x() + ctx.ri(-6, 6), ctx.y(), ctx.z() + ctx.ri(-6, 6));
                    }
                    ctx.damage("fall", 3.0f);
                    ctx.launch(0.7);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hoe_snap", "锄头当场折断", TrollTier.BRUTAL)
                .desc("你手里的农具在一阵清脆的响声后结束了职业生涯")
                .weight(56)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.breakRandomTool();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.4f);
                    ctx.warn("§c耐磨度：0。§7感想：请节哀。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_harvest_lost", "整季收成没了", TrollTier.BRUTAL)
                .desc("粮仓门自己开了，里面的东西决定去看看外面的世界")
                .weight(55)
                .cooldownSeconds(230)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.dropRandom();
                    }
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 40);
                    ctx.msg("§7本季产量：§c0§7。§8下季再说。§7");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_lava_irrigation", "岩浆灌溉", TrollTier.BRUTAL)
                .desc("灌溉系统接错了管子，出水口温度偏高")
                .weight(54)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.setFire(5);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§c水温：§4适宜§c。§7作物：§8不需要了§7。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_void_silo", "粮仓下面是虚空", TrollTier.BRUTAL)
                .desc("你脚下的粮仓地基一夜之间被改成了无底洞")
                .weight(52)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.voidPit(2, 6);
                    ctx.damage("fall", 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                    ctx.msg("§8往下看是虚空§7，往上看是你的选择。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_tnt_fertilizer", "TNT 当肥料", TrollTier.BRUTAL)
                .desc("有人把炸药施进了土里，肥力当场兑现")
                .weight(52)
                .cooldownSeconds(250)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnTnt(ctx.x() + ctx.ri(-3, 3), ctx.y(), ctx.z() + ctx.ri(-3, 3), 30 + ctx.ri(0, 20));
                    }
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 60);
                    ctx.boomAtPlayer(2.0f);
                    ctx.msg("§c施肥完成§7，§8肥力超标 $2^{8}$ 倍。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_bee_fury", "蜂群暴怒", TrollTier.BRUTAL)
                .desc("整个蜂箱的住户集体出动，情绪非常统一")
                .weight(50)
                .cooldownSeconds(240)
                .run(ctx -> {
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 5);
                    }
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 220, 1);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 90, 1);
                    ctx.title("§e§l嗡嗡嗡嗡", "§7你已经被列入蜂群黑名单");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_ram_charge", "公羊冲撞", TrollTier.BRUTAL)
                .desc("一只公羊从你看不见的角度完成了助跑")
                .weight(50)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.fling(ctx.rd(-2.0, 2.0), 0.6, ctx.rd(-2.0, 2.0));
                    ctx.damage("flyintowall", 4.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§7羊：§8对不起，我看错人了§7。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_wolf_pack", "狼群接管牧场", TrollTier.BRUTAL)
                .desc("你的牧羊犬项目被一群更有执行力的团队接手了")
                .weight(48)
                .cooldownSeconds(250)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    }
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7它们接管了你的牲畜，和你的位置。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_greenhouse_glass", "温室玻璃全碎", TrollTier.BRUTAL)
                .desc("温室玻璃从内部炸开，碎片对你表达了充分的热情")
                .weight(48)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 70, 2);
                    ctx.damage("cactus", 4.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.5f);
                    ctx.msg("§f温室已全面通风§7，§c通风口由你构成§7。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_pesticide", "农药中毒", TrollTier.BRUTAL)
                .desc("你坚持认为那瓶是饮料，身体不同意")
                .weight(48)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.effectRandom(320, 2);
                    ctx.damage("wither", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.msg("§2农药：§7高效§8，对人也是。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_lightning_rod", "你就是避雷针", TrollTier.BRUTAL)
                .desc("雷雨天里，全场最高导电体恰好是你")
                .weight(47)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.lightning();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 220, 0);
                    ctx.setFire(4);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 1.0f);
                    ctx.msg("§e避雷针§7已激活§8，安装位置：你。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_mudslide", "泥石流", TrollTier.BRUTAL)
                .desc("山上的泥和石头同时决定下山看看，路线经过你")
                .weight(46)
                .cooldownSeconds(250)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.NASTY_BLOCKS), 25);
                    ctx.damage("fall", 3.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.msg("§8地质灾害§7已到货§8，请查收。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_composter_boom", "堆肥桶压力过大", TrollTier.BRUTAL)
                .desc("堆肥桶内部的微生物召开了一次爆炸性会议")
                .weight(46)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.boomAtPlayer(2.2f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 70);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§2有机物§7宣布独立§8，方式：爆裂。§7");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_crop_blight", "作物枯萎病", TrollTier.BRUTAL)
                .desc("一场枯萎病横扫你的田，只有杂草活得很好")
                .weight(45)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.NASTY_BLOCKS), 20);
                    ctx.effectRandom(260, 1);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.6f);
                    ctx.msg("§2枯萎病§7已覆盖全田§8，杂草对此表示欢迎。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_chicken_plague", "鸡瘟", TrollTier.BRUTAL)
                .desc("鸡舍进入紧急状态，外面的东西闻着味就来了")
                .weight(45)
                .cooldownSeconds(260)
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.fakeBroadcast("§c[防疫] 本区域已封锁，请勿离开，也请勿停留。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hay_fire", "干草堆起火", TrollTier.BRUTAL)
                .desc("干草堆不明原因自燃，火势对你的位置很感兴趣")
                .weight(45)
                .cooldownSeconds(250)
                .run(ctx -> {
                    ctx.setFire(8);
                    ctx.flameBurst();
                    ctx.scatterBlocks(ctx.pick(TrollPools.TROLL_BLOCKS), 10);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§6火灾原因调查中§7，§8嫌疑人：干草。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_tractor_accident", "拖拉机事故", TrollTier.BRUTAL)
                .desc("拖拉机决定自己开一段，你负责被甩出去的部分")
                .weight(44)
                .cooldownSeconds(250)
                .run(ctx -> {
                    ctx.lift(12);
                    ctx.fling(ctx.rd(-3.0, 3.0), 0.4, ctx.rd(-3.0, 3.0));
                    ctx.damage("fall", 4.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_fence_launch", "栅栏弹射", TrollTier.BRUTAL)
                .desc("你扶了一下栅栏，栅栏把你送上了天")
                .weight(44)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.launch(1.8);
                    ctx.effectRandom(200, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§7栅栏：§8弹射起步，请系好自己§7。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_well_drown", "水井溺水", TrollTier.BRUTAL)
                .desc("你往井里看了一眼，井把你拉了下去")
                .weight(43)
                .cooldownSeconds(270)
                .run(ctx -> {
                    ctx.teleport(ctx.x(), ctx.y() - 20.0, ctx.z());
                    ctx.setAir(60);
                    ctx.damage("drown", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.msg("§9井底风光§7：一般§8。空气：限量供应。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_silo_collapse", "粮仓塌陷", TrollTier.BRUTAL)
                .desc("整座粮仓朝你倒下，谷物把你原地打包")
                .weight(43)
                .cooldownSeconds(270)
                .run(ctx -> {
                    ctx.surroundWith(ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.msg("§7你已成为粮仓的一部分§8，暂不接受出库申请。§7");
                    ctx.addRage(20);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hoe_sacrifice", "锄头献祭", TrollTier.BRUTAL)
                .desc("你的农具被某个存在看上了，点名要求上交")
                .weight(42)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.curseHeld();
                    ctx.takeRandomItem();
                    ctx.glitch(GlitchIds.FLASHBANG, 25, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.msg("§5祭品已接受§7，§8回礼是这段白光。§7");
                    ctx.addRage(19);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_milk_curdle", "牛奶变质", TrollTier.BRUTAL)
                .desc("你灌下去的那桶牛奶，在胃里完成了二次发酵")
                .weight(42)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.starve();
                    ctx.effectRandom(240, 1);
                    ctx.exhaust(4.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 0.7f);
                    ctx.msg("§7保质期：§c已过§7。§8检测设备：你的胃。§7");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_sheep_inflate", "羊被充气", TrollTier.BRUTAL)
                .desc("牧场里某只羊膨胀到了物理极限，然后就没有然后了")
                .weight(42)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.boomAtPlayer(2.0f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 80);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.1f);
                    ctx.msg("§f羊毛§7的散落范围，§8远超你的预期。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_raccoon_raid", "浣熊洗劫", TrollTier.BRUTAL)
                .desc("一伙浣熊连夜搬空了你的仓库，动作比你收菜还快")
                .weight(41)
                .cooldownSeconds(270)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.dropRandom();
                    }
                    ctx.giveRandomJunk();
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "借点东西，明年还你");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_golem_rage", "铁傀儡失控", TrollTier.BRUTAL)
                .desc("村里那个大铁块把你误判成了害虫，并已开始执行")
                .weight(41)
                .cooldownSeconds(280)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    }
                    ctx.damage("flyintowall", 4.0f);
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7误判等级：致命");
                    ctx.addRage(19);
                })
                .build());

        // ================================================================
        // NIGHTMARE —— 崩溃，会砸键盘（15 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("farm_locust_plague", "蝗灾", TrollTier.NIGHTMARE)
                .desc("遮天蔽日的蝗虫落到你的田里，先吃作物，再看你")
                .weight(34)
                .cooldownSeconds(400)
                .run(ctx -> {
                    for (int i = 0; i < 10; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 6);
                    }
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.glitch(GlitchIds.SHAKY, 140, 2);
                    ctx.title("§8§l蝗 灾", "§7能见度归零，产量归零");
                    ctx.addRage(25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hive_mind", "蜂群意识", TrollTier.NIGHTMARE)
                .desc("蜂箱里诞生了一个集体意识，它决定用你的视角看世界")
                .weight(33)
                .cooldownSeconds(420)
                .run(ctx -> {
                    for (int i = 0; i < 8; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 5);
                    }
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 160, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                    ctx.msg("§e我们已接管你的视觉§8，画质请多见谅§e。");
                    ctx.addRage(25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_blood_harvest", "血祭丰收", TrollTier.NIGHTMARE)
                .desc("今年的收成异常好，代价从你身上直接收取")
                .weight(32)
                .cooldownSeconds(430)
                .run(ctx -> {
                    ctx.damage("magic", 6.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 2);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 90);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.msg("§4丰收§7已登记§8，付款方式：你。§7");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_void_granary", "粮仓虚空化", TrollTier.NIGHTMARE)
                .desc("粮仓的地面被替换成虚空入口，库存和你一起失重")
                .weight(32)
                .cooldownSeconds(450)
                .run(ctx -> {
                    ctx.voidPit(3, 10);
                    for (int i = 0; i < 3; i++) {
                        ctx.dropRandom();
                    }
                    ctx.damage("outofworld", 2.0f);
                    ctx.glitch(GlitchIds.BLACKOUT, 50);
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_livestock_pyre", "牲畜火葬", TrollTier.NIGHTMARE)
                .desc("整个畜栏在同一时刻起火，火势有明确的主观意愿")
                .weight(31)
                .cooldownSeconds(440)
                .run(ctx -> {
                    ctx.setFire(15);
                    ctx.flameBurst();
                    ctx.boomAtPlayer(2.5f);
                    ctx.effectRandom(300, 1);
                    ctx.msg("§6集体火化§7已安排§8，参与者：你的牧场。§7");
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_genetic_nightmare", "基因噩梦", TrollTier.NIGHTMARE)
                .desc("繁殖箱出了点问题，新一代牲畜看起来非常不正确")
                .weight(31)
                .cooldownSeconds(450)
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.giveMobCustomName(
                                ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                        ctx.x() + ctx.ri(-5, 5), ctx.y(), ctx.z() + ctx.ri(-5, 5)),
                                "§5" + ctx.pick(TrollPools.CURSED_NAMES));
                    }
                    ctx.glitch(GlitchIds.SHAKY, 140, 2);
                    ctx.msg("§5它们都知道你的名字§8，而且叫得很准§5。");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_scarecrow_awakens", "稻草人苏醒", TrollTier.NIGHTMARE)
                .desc("田里所有的稻草人同时转头，然后从架子上走了下来")
                .weight(30)
                .cooldownSeconds(480)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    }
                    ctx.randomHorrorSound();
                    ctx.title("§4§l稻草人已下地", "§8它们决定自己动手");
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_carnivorous_crop", "会吃人的作物", TrollTier.NIGHTMARE)
                .desc("你种下的东西开始朝你生长，速度和食欲都很吓人")
                .weight(30)
                .cooldownSeconds(460)
                .run(ctx -> {
                    ctx.surroundWith(ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.freeze(70);
                    ctx.damage("cactus", 5.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.msg("§2作物：§7谢谢你把我养大§8。现在轮到我了。§7");
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_granary_burial", "谷仓活埋", TrollTier.NIGHTMARE)
                .desc("谷物从四面八方涌来，把你封在中间，只留一口气")
                .weight(30)
                .cooldownSeconds(470)
                .run(ctx -> {
                    ctx.surroundWith(ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.setBlock(0, 3, 0, ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 340, 1);
                    ctx.msg("§7你已被谷物完全覆盖§8，出库日期：另行通知。§7");
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_meteor_fertilizer", "陨石施肥", TrollTier.NIGHTMARE)
                .desc("天上掉下来的肥料速度极快，还带着火")
                .weight(29)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.meteorShower(6, 22);
                    ctx.damage("fall", 4.0f);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 70, 2);
                    ctx.title("§c§l天降肥料", "§7不建议在户外签收");
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_frost_famine", "霜冻饥荒", TrollTier.NIGHTMARE)
                .desc("气温骤降，作物冻死，你的身体决定跟着一起")
                .weight(29)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 360, 2);
                    ctx.starve();
                    ctx.damage("freeze", 4.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.msg("§b霜冻覆盖全田§7，§8下一个是你。§7");
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_swarm_devour", "虫群吞噬", TrollTier.NIGHTMARE)
                .desc("虫群不满足于作物，开始对你进行实地考察")
                .weight(29)
                .cooldownSeconds(490)
                .run(ctx -> {
                    for (int i = 0; i < 7; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    }
                    ctx.damage("magic", 4.0f);
                    ctx.glitch(GlitchIds.SHAKY, 130, 2);
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hell_harvest", "地狱丰收", TrollTier.NIGHTMARE)
                .desc("你脚下的土地变成了熔岩，作物和你的处境高度一致")
                .weight(28)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.lavaPit(3);
                    ctx.setFire(10);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    }
                    ctx.msg("§4丰收季§7已提前§8，场地温度：§c不适宜。§7");
                    ctx.addRage(29);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_time_drought", "干旱三千年", TrollTier.NIGHTMARE)
                .desc("有人把时间快进了三千年，全程没有下过一滴雨")
                .weight(28)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.timeSkip(3000);
                    ctx.clearWeather();
                    ctx.effectRandom(340, 2);
                    ctx.damage("starve", 3.0f);
                    ctx.msg("§6气象记录§7：连续干旱§e3000 年§8，纪录保持者：你。§7");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hive_invasion", "蜂巢入侵现实", TrollTier.NIGHTMARE)
                .desc("蜂巢的边界变得模糊，蜜蜂开始从你视野的缝隙里钻出来")
                .weight(28)
                .cooldownSeconds(500)
                .run(ctx -> {
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 5);
                    }
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 90, 2);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 80);
                    ctx.msg("§e现实渲染错误§7：§8检测到过多蜜蜂。§7");
                    ctx.addRage(28);
                })
                .build());

        // ================================================================
        // APOCALYPSE —— 地狱，会退游戏（7 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("farm_famine", "大饥荒", TrollTier.APOCALYPSE)
                .desc("全服粮仓清零，饥饿值直接见底，连同你的背包一起")
                .weight(25)
                .cooldownSeconds(1000)
                .run(ctx -> {
                    ctx.starve();
                    ctx.clearInventory();
                    ctx.damage("starve", 5.0f);
                    ctx.fakeBroadcast("§4[警报] 全服粮仓存量：0。请自行寻找食物。");
                    ctx.title("§4§l大饥荒", "§8这一次没有救济粮");
                    ctx.addRage(40);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_cattle_apocalypse", "牲畜天启", TrollTier.APOCALYPSE)
                .desc("所有牲畜同时变异，朝你所在的方向发起了总攻")
                .weight(24)
                .cooldownSeconds(1100)
                .run(ctx -> {
                    for (int i = 0; i < 10; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    }
                    ctx.boomAtPlayer(3.0f);
                    ctx.thunderstorm(1800);
                    ctx.title("§4§l牲畜天启", "§8它们不再吃草了");
                    ctx.addRage(42);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_world_granary", "世界粮仓", TrollTier.APOCALYPSE)
                .desc("你被改造成世界粮仓的核心部件，永久产权，没有门")
                .weight(23)
                .cooldownSeconds(1200)
                .run(ctx -> {
                    ctx.cageWith(ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.glitch(GlitchIds.HIDE_HUD, 200, 1);
                    ctx.setHealth(6.0f);
                    ctx.setFire(18);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.title("§4§l粮仓已封顶", "§8感谢你为粮食安全作出的贡献");
                    ctx.addRage(44);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_locust_judgment", "蝗虫审判", TrollTier.APOCALYPSE)
                .desc("天上下起火雨，地上爬满蝗虫，中间站着你这个被告")
                .weight(22)
                .cooldownSeconds(1300)
                .run(ctx -> {
                    ctx.meteorShower(10, 26);
                    ctx.thunderstorm(1800);
                    for (int i = 0; i < 8; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 5);
                    }
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 110, 2);
                    ctx.msg("§4§l判决已下达§r§4：没收全部收成，立即执行。");
                    ctx.addRage(45);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_doomsday_harvest", "末日收割", TrollTier.APOCALYPSE)
                .desc("有人替你完成了一次彻底清仓，连你身上的那份也没放过")
                .weight(22)
                .cooldownSeconds(1400)
                .run(ctx -> {
                    ctx.dropEverything();
                    ctx.damage("magic", 5.0f);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 90, 3);
                    ctx.title("§4§l收割完毕", "§8本季产量：全服第一");
                    ctx.addRage(46);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_nature_revenge", "自然复仇", TrollTier.APOCALYPSE)
                .desc("土地、岩浆、虚空和守卫者达成共识，一起处理你")
                .weight(21)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.lavaPit(3);
                    ctx.voidPit(2, 6);
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 9);
                    }
                    ctx.boomAtPlayer(3.0f);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 80, 3);
                    ctx.msg("§2自然：§7你种了那么多§8，现在轮到我们种你了§2。");
                    ctx.addRage(48);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_last_harvest", "最后的丰收", TrollTier.APOCALYPSE)
                .desc("伪造死亡画面与崩溃，再顺手把你这一季的成果全部归零，只此一次")
                .weight(20)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.fakeDeathScreen("§7被自己的农作物反杀");
                    ctx.glitch(GlitchIds.FAKE_CRASH, 120, 3);
                    ctx.fakeBroadcast("§4你的农田已归档。感谢你这一季的付出。");
                    ctx.kill();
                    ctx.msg("§8……丰收是真的，死亡也是。§7下季见。");
                    ctx.addRage(50);
                })
                .build());
    }
}
