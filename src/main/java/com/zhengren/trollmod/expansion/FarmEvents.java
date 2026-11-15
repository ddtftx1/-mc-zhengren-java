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
        // PRANK —— 小整，笑一下就好
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

        TrollEvents.reg(TrollEvent.of("farm_wheat_gossip", "麦田八卦", TrollTier.ANNOYING)
                .desc("你身后的麦子开始小声议论你的操作")
                .weight(145)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "听说他连锄头都拿反了");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.7f, 1.6f);
                    ctx.msg("§7麦穗齐刷刷转向你§8，像是在确认传闻。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_cow_stare", "牛的凝视", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("farm_egg_donation", "天降鸡蛋", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("farm_bee_greeting", "蜜蜂认亲", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("farm_scarecrow_applause", "稻草人的掌声", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("farm_pumpkin_coronation", "南瓜加冕", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("farm_hoe_squeak", "锄头会唱歌", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("farm_drizzle", "局部灌溉", TrollTier.ANNOYING)
                .desc("只有你头顶这一小块地方在下雨，边界十分明确")
                .weight(115)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.setRaining(true, false, 240);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§9灌溉范围：§7以你为圆心，半径§b一格§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_sugarcane_dance", "甘蔗摇摆", TrollTier.ANNOYING)
                .desc("甘蔗们跟着节奏整齐摇摆，只有你不在节奏里")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.randomRotation();
                    ctx.actionBar("§a甘蔗：§7左右左右左右§8，你踩错了§a。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_berry_burst", "浆果爆浆", TrollTier.ANNOYING)
                .desc("你口袋里的一颗浆果自行爆开，位置相当刁钻")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.5f);
                    ctx.msg("§c浆果已就地牺牲§7，§8它选择死在你身上。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_frog_choir", "青蛙合唱团", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("farm_llama_opinion", "羊驼的看法", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("farm_hay_pillow", "干草枕头", TrollTier.BRUTAL)
                .desc("你头上多了一顶不太服帖的干草帽，摘不掉的那种存在感")
                .weight(100)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 15);
                    ctx.msg("§e干草帽已到货§7，§8尺寸是农场的尺寸。§7");
                })
                .build());

        // ================================================================
        // ANNOYING —— 烦人，会皱眉
        // ================================================================

        TrollEvents.reg(TrollEvent.of("farm_fence_pen", "按牲畜规格处理", TrollTier.NIGHTMARE)
                .desc("你被栅栏原地围起，规格完全参照牲畜，出口没有")
                .weight(145)
                .cooldownSeconds(80)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.surroundWith(ctx.pick(TrollPools.TRAP_BLOCKS));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.msg("§7新圈舍已交付§8，你被划分为§7存栏§8那一类。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_bucket_dunk", "浇地浇自己", TrollTier.NIGHTMARE)
                .desc("你要浇地的那桶水，最终从头顶浇了下来")
                .weight(140)
                .cooldownSeconds(85)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.placeFloor(ctx.pick(TrollPools.NASTY_BLOCKS), 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§9水桶表示：§7它更想待在你头上§8，这是它的自由。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_seed_spill", "种子撒了一地", TrollTier.BRUTAL)
                .desc("你手里的种子突然集体越狱，铺满附近地面")
                .weight(140)
                .cooldownSeconds(75)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.scatterBlocks(ctx.pick(TrollPools.TROLL_BLOCKS), 12);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.msg("§7种子已散落§8，请在一分钟内捡完§7（没有一分钟）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_scarecrow_swap", "稻草人换位", TrollTier.BRUTAL)
                .desc("你和田里那个稻草人在空间上发生了点误会")
                .weight(135)
                .cooldownSeconds(95)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.scatter(35);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.msg("§7你已经站到了稻草人的岗位上§8，请保持姿势。§7");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hoe_confiscated", "农具被没收", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("farm_harvest_shuffle", "收成调包", TrollTier.NIGHTMARE)
                .desc("全部家当被重新洗过一遍，顺序全凭天意")
                .weight(130)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.shuffleInventory();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§7粮仓管理员表示：§8这叫科学堆放§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hotbar_eggs", "快捷栏孵蛋", TrollTier.NIGHTMARE)
                .desc("快捷栏被塞进几颗来路不明的蛋，还顺手重排了一遍")
                .weight(130)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.scrambleHotbar();
                    ctx.give(ctx.pick(TrollPools.JUNK), 3);
                    ctx.msg("§e额外附赠鸡蛋若干§7，§8请自行辨认哪些是石头。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_chicken_swarm", "鸡群定居", TrollTier.NIGHTMARE)
                .desc("六只鸡同时认定你的脚边是唯一的家，且不打算搬走")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                ctx.x() + ctx.ri(-4, 4), ctx.y(), ctx.z() + ctx.ri(-4, 4));
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.msg("§e咯咯咯×6§7。它们已经在这里定居了。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_bee_irritated", "蜜蜂不高兴", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("farm_pig_ice_rink", "猪圈溜冰场", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("farm_cowbell_alarm", "牛铃警报", TrollTier.BRUTAL)
                .desc("四面同时响起牛铃，音量按重大事故级别配置")
                .weight(125)
                .cooldownSeconds(95)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.soundAt(ctx.pick(TrollPools.IMPACT), ctx.x() + 2, ctx.y(), ctx.z(), 1.0f, 1.6f);
                    ctx.soundAt(ctx.pick(TrollPools.IMPACT), ctx.x() - 2, ctx.y(), ctx.z(), 1.0f, 1.4f);
                    ctx.title("§6§l叮——", "§7牧场警报：假警报，但很响");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_sheared", "被剃了一遍", TrollTier.NIGHTMARE)
                .desc("你少了一圈保暖层，旁边多了一堆不属于你的羊毛")
                .weight(125)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                    ctx.give(ctx.pick(TrollPools.JUNK), 4);
                    ctx.msg("§f羊毛已回收§7，§8按批发价结算，你一分钱拿不到。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_mud_wallow", "泥坑体验", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("farm_melon_face", "西瓜糊脸", TrollTier.NIGHTMARE)
                .desc("你手里的工具被换成一整个西瓜，手感与尊严同时不同")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.renameHeld("§2一整个西瓜");
                    ctx.setCooldown(ctx.pick(TrollPools.JUNK), 120);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§2西瓜已上岗§7，§8它对这个职位一无所知。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_compost_theft", "肥料失窃", TrollTier.BRUTAL)
                .desc("堆肥桶一夜之间空了，还给你留了一封道歉信")
                .weight(120)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.giveRandomJunk();
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "肥料我拿走了，你那堆土其实挺好的");
                    ctx.msg("§7桶还在§8，内容物已出差。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_fence_maze", "栅栏迷宫", TrollTier.NIGHTMARE)
                .desc("你身边长出一圈栅栏，出口的设计充满了恶意幽默")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
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

        TrollEvents.reg(TrollEvent.of("farm_locust_scout", "蝗虫侦查", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("farm_bonemeal_backfire", "骨粉反噬", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("farm_bee_in_hat", "帽子里有蜜蜂", TrollTier.BRUTAL)
                .desc("你听见帽子里传来一阵很有耐心的嗡嗡声")
                .weight(110)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.glitch(GlitchIds.SHAKY, 60, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.7f);
                    ctx.actionBar("§e嗡……§7嗡……§8它不打算出来。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_trough_jelly", "饲料槽变果冻", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("farm_animal_strike", "牲畜罢工", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("farm_milking_prank", "挤奶惊魂", TrollTier.BRUTAL)
                .desc("你伸手去挤奶，接到的东西明显不是牛奶")
                .weight(105)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.msg("§7桶里的液体拒绝了§f牛奶§7这个身份。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_wheat_market", "期货暴跌", TrollTier.NIGHTMARE)
                .desc("你刚收的小麦遇到历史性行情，服务器还郑重通知了你")
                .weight(105)
                .cooldownSeconds(140)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.fakeBroadcast("§8[交易所] 小麦价格下跌 99%，原因：收成太好。");
                    ctx.sys("§7你的库存估值：§c约等于零§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hay_bounce", "干草弹射", TrollTier.NIGHTMARE)
                .desc("干草垛突然表现出极佳的弹性，你被就地发射")
                .weight(105)
                .cooldownSeconds(100)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.launch(1.3);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§e干草垛：§7弹性优秀§8，安全性概不负责。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_egg_bombardment", "鸡蛋轰炸", TrollTier.NIGHTMARE)
                .desc("天上开始密集投放鸡蛋，落点经过精心计算")
                .weight(100)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE),
                                ctx.x() + ctx.ri(-3, 3), ctx.y() + ctx.ri(8, 12), ctx.z() + ctx.ri(-3, 3));
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.msg("§e空袭警报：§7弹药为鸡蛋§8，杀伤力为侮辱性。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_rabbit_burrow", "脚下的住户", TrollTier.NIGHTMARE)
                .desc("地面塌了，洞里的兔子对你的坠落毫无歉意")
                .weight(100)
                .cooldownSeconds(110)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.breakUnder();
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                ctx.x() + ctx.ri(-2, 2), ctx.y(), ctx.z() + ctx.ri(-2, 2));
                    }
                    ctx.msg("§7兔子：§8你踩到我们家天花板了§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_potato_rain", "土豆雨", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("farm_wool_sticky", "羊毛粘脚", TrollTier.NIGHTMARE)
                .desc("你踩进一团羊毛，从此走路都带着阻力，甩不掉")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 180, 0);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.msg("§f羊毛粘性测试通过§7，§8受试者：你。§7");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_crop_circle", "田里的圈", TrollTier.NIGHTMARE)
                .desc("你的田一夜之间出现怪圈，圆心正好是你站的位置")
                .weight(95)
                .cooldownSeconds(130)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    for (int i = 0; i < 10; i++) {
                        double a = i * Math.PI / 5.0;
                        ctx.setBlock((int) Math.round(Math.cos(a) * 3.0), 0,
                                (int) Math.round(Math.sin(a) * 3.0), ctx.pick(TrollPools.TROLL_BLOCKS));
                    }
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                    ctx.msg("§7怪圈已完成§8，外星人表示这次只是路过。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_composter_swap", "堆肥桶换位", TrollTier.NIGHTMARE)
                .desc("你和某位同伴在堆肥桶旁边互换了人生位置，没有人解释")
                .weight(95)
                .cooldownSeconds(130)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.swapWith(ctx.other());
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.msg("§7换位成功§8，你们互相继承了对方面前的空气。§7");
                    ctx.addRage(6);
                })
                .build());

        // ================================================================
        // BRUTAL —— 抓狂，会骂人
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

        TrollEvents.reg(TrollEvent.of("farm_stampede", "牛群踩踏", TrollTier.NIGHTMARE)
                .desc("八头牛同时决定朝你所在的方向奔跑，官方说法是巧合")
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

        TrollEvents.reg(TrollEvent.of("farm_hoe_snap", "锄头当场折断", TrollTier.NIGHTMARE)
                .desc("手里的农具在一阵清脆的响声后结束了职业生涯")
                .weight(56)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.breakRandomTool();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.4f);
                    ctx.warn("§c耐磨度：0。§7感想：请节哀。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_harvest_lost", "整季收成归零", TrollTier.APOCALYPSE)
                .desc("粮仓门自己开了，里面的东西决定去看看外面，一件不回")
                .weight(55)
                .cooldownSeconds(230)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
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

        TrollEvents.reg(TrollEvent.of("farm_void_silo", "粮仓下面是虚空", TrollTier.APOCALYPSE)
                .desc("你脚下的地基一夜之间被改成无底洞，库存和你一起失重")
                .weight(52)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.voidPit(2, 6);
                    ctx.damage("fall", 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                    ctx.msg("§8往下看是虚空§7，往上看是你的选择。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_tnt_fertilizer", "TNT 施肥", TrollTier.NIGHTMARE)
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

        TrollEvents.reg(TrollEvent.of("farm_bee_fury", "蜂群暴怒", TrollTier.NIGHTMARE)
                .desc("整个蜂箱的住户集体出动，情绪统一，目标明确")
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

        TrollEvents.reg(TrollEvent.of("farm_wolf_pack", "狼群接管", TrollTier.NIGHTMARE)
                .desc("你的牧羊项目被一支执行力更强的团队接手了")
                .weight(48)
                .cooldownSeconds(250)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    }
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7它们接管了你的牲畜，和你的位置。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_greenhouse_glass", "温室玻璃全碎", TrollTier.APOCALYPSE)
                .desc("温室玻璃从内部炸开，碎片对你表达了充分的热情")
                .weight(48)
                .cooldownSeconds(230)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 70, 2);
                    ctx.damage("cactus", 4.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.5f);
                    ctx.msg("§f温室已全面通风§7，§c通风口由你构成§7。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_pesticide", "农药中毒", TrollTier.APOCALYPSE)
                .desc("你坚持认为那瓶是饮料，身体用一次濒死表达了不同意见")
                .weight(48)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.effectRandom(320, 2);
                    ctx.damage("wither", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.msg("§2农药：§7高效§8，对人也是。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_lightning_rod", "你是避雷针", TrollTier.NIGHTMARE)
                .desc("雷雨天里，全场最高的导电体恰好是你")
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

        TrollEvents.reg(TrollEvent.of("farm_mudslide", "泥石流", TrollTier.APOCALYPSE)
                .desc("山上的泥和石头同时决定下山看看，路线经过你")
                .weight(46)
                .cooldownSeconds(250)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.scatterBlocks(ctx.pick(TrollPools.NASTY_BLOCKS), 25);
                    ctx.damage("fall", 3.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.msg("§8地质灾害§7已到货§8，请查收。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_composter_boom", "堆肥桶爆炸", TrollTier.APOCALYPSE)
                .desc("堆肥桶里的微生物召开了一次爆炸性会议，你坐在会场正中")
                .weight(46)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.boomAtPlayer(2.2f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 70);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§2有机物§7宣布独立§8，方式：爆裂。§7");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_crop_blight", "枯萎病", TrollTier.NIGHTMARE)
                .desc("一场枯萎病横扫你的田，只有杂草活得很好，还很茂盛")
                .weight(45)
                .cooldownSeconds(260)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.scatterBlocks(ctx.pick(TrollPools.NASTY_BLOCKS), 20);
                    ctx.effectRandom(260, 1);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.6f);
                    ctx.msg("§2枯萎病§7已覆盖全田§8，杂草对此表示欢迎。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_chicken_plague", "鸡瘟", TrollTier.APOCALYPSE)
                .desc("鸡舍进入紧急状态，外面的东西闻着味道就来了")
                .weight(45)
                .cooldownSeconds(260)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.fakeBroadcast("§c[防疫] 本区域已封锁，请勿离开，也请勿停留。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hay_fire", "干草堆自燃", TrollTier.APOCALYPSE)
                .desc("干草堆不明原因起火，火势对你的位置表现出浓厚兴趣")
                .weight(45)
                .cooldownSeconds(250)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.setFire(8);
                    ctx.flameBurst();
                    ctx.scatterBlocks(ctx.pick(TrollPools.TROLL_BLOCKS), 10);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§6火灾原因调查中§7，§8嫌疑人：干草。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_tractor_accident", "拖拉机事故", TrollTier.APOCALYPSE)
                .desc("拖拉机决定自己开一段，你负责被甩出去的那部分")
                .weight(44)
                .cooldownSeconds(250)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.lift(12);
                    ctx.fling(ctx.rd(-3.0, 3.0), 0.4, ctx.rd(-3.0, 3.0));
                    ctx.damage("fall", 4.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_fence_launch", "栅栏弹射", TrollTier.APOCALYPSE)
                .desc("你扶了一下栅栏，栅栏把你送上了天，没有回程票")
                .weight(44)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.launch(1.8);
                    ctx.effectRandom(200, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§7栅栏：§8弹射起步，请系好自己§7。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_well_drown", "井里的手", TrollTier.NIGHTMARE)
                .desc("你往井里看了一眼，井把你拉了下去")
                .weight(43)
                .cooldownSeconds(270)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.teleport(ctx.x(), ctx.y() - 20.0, ctx.z());
                    ctx.setAir(60);
                    ctx.damage("drown", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.msg("§9井底风光§7：一般§8。空气：限量供应。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_silo_collapse", "粮仓塌陷", TrollTier.NIGHTMARE)
                .desc("整座粮仓朝你倒下，谷物把你原地打包")
                .weight(43)
                .cooldownSeconds(270)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.surroundWith(ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.msg("§7你已成为粮仓的一部分§8，暂不接受出库申请。§7");
                    ctx.addRage(20);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hoe_sacrifice", "农具献祭", TrollTier.NIGHTMARE)
                .desc("你的农具被某个存在点名要求上交，不接受申诉")
                .weight(42)
                .cooldownSeconds(280)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.curseHeld();
                    ctx.takeRandomItem();
                    ctx.glitch(GlitchIds.FLASHBANG, 25, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.msg("§5祭品已接受§7，§8回礼是这段白光。§7");
                    ctx.addRage(19);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_milk_curdle", "牛奶变质", TrollTier.APOCALYPSE)
                .desc("你灌下去的那桶牛奶，在胃里完成了二次发酵")
                .weight(42)
                .cooldownSeconds(260)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.starve();
                    ctx.effectRandom(240, 1);
                    ctx.exhaust(4.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 0.7f);
                    ctx.msg("§7保质期：§c已过§7。§8检测设备：你的胃。§7");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_sheep_inflate", "羊被充气", TrollTier.APOCALYPSE)
                .desc("牧场里某只羊膨胀到了物理极限，然后就没有然后了")
                .weight(42)
                .cooldownSeconds(260)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.boomAtPlayer(2.0f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 80);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.1f);
                    ctx.msg("§f羊毛§7的散落范围，§8远超你的预期。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_raccoon_raid", "浣熊洗劫", TrollTier.APOCALYPSE)
                .desc("一伙浣熊连夜搬空了你的仓库，动作比你收菜还熟练")
                .weight(41)
                .cooldownSeconds(270)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
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
        // NIGHTMARE / APOCALYPSE / OBLIVION —— 崩溃、地狱与绝望
        // ================================================================

        TrollEvents.reg(TrollEvent.of("farm_locust_plague", "蝗灾", TrollTier.OBLIVION)
                .desc("蝗虫先吃干净今年的收成，再抬头看你，并顺手预定了明年的")
                .weight(34)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.msg("§7蝗虫落在田里，先把作物吃干净，再抬头看你。");
                    ctx.later(50, () -> {
                        ctx.starve();
                        ctx.takeRandomItems(6);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                        ctx.msg("§4它们吃完了今年，又替你预定了明年——用你自己。");
                    });
                    ctx.later(140, () -> {
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 100, 3);
                        ctx.obliterate();
                        ctx.broadcast("§4蝗虫过境之后，田里只剩下土，和你没来得及收的骨。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hive_mind", "蜂群意识", TrollTier.APOCALYPSE)
                .desc("蜂箱里诞生了一个集体意识，它决定借用你的视角看世界")
                .weight(33)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    for (int i = 0; i < 8; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 5);
                    }
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 160, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                    ctx.msg("§e我们已接管你的视觉§8，画质请多见谅§e。");
                    ctx.addRage(25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_blood_harvest", "血祭丰收", TrollTier.APOCALYPSE)
                .desc("今年的收成异常好，代价从你身上直接收取")
                .weight(32)
                .cooldownSeconds(430)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.damage("magic", 6.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 2);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 90);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.msg("§4丰收§7已登记§8，付款方式：你。§7");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_void_granary", "粮仓虚空化", TrollTier.APOCALYPSE)
                .desc("粮仓地面被替换成虚空入口，库存和你一起失重下坠")
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

        TrollEvents.reg(TrollEvent.of("farm_genetic_nightmare", "基因噩梦", TrollTier.OBLIVION)
                .desc("繁殖箱里传出的声音不属于任何已知动物，且它们继承了你的气味")
                .weight(31)
                .cooldownSeconds(450)
                .run(ctx -> {
                    ctx.title("§2§l育种结果", "§7新一代牲畜看起来非常不正确");
                    ctx.msg("§2繁殖箱里传出的声音，不属于任何已知动物。");
                    ctx.later(60, () -> {
                        for (int i = 0; i < 4; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                        }
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                        ctx.msg("§4它们继承了你的坐标，也继承了你的气味。");
                    });
                    ctx.later(150, () -> {
                        ctx.glitch(GlitchIds.NAUSEA_SHADER, 140, 3);
                        ctx.obliterate();
                        ctx.broadcast("§4育种记录的最后一行写着：样本来源——你自己。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_scarecrow_awakens", "稻草人苏醒", TrollTier.NIGHTMARE)
                .desc("田里所有稻草人同时转头，然后从架子上走了下来")
                .weight(30)
                .cooldownSeconds(480)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    }
                    ctx.randomHorrorSound();
                    ctx.title("§4§l稻草人已下地", "§8它们决定自己动手");
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_carnivorous_crop", "会吃人的作物", TrollTier.OBLIVION)
                .desc("你种下的东西开始朝你生长，先缠住脚，再替你合上嘴")
                .weight(30)
                .cooldownSeconds(460)
                .run(ctx -> {
                    ctx.msg("§2你种下的东西开始朝你生长。速度很快，食欲很明确。");
                    ctx.later(50, () -> {
                        ctx.freeze(160);
                        ctx.damage(3.0f);
                        ctx.msg("§4藤蔓先缠住脚，再缠住手，最后替你合上了嘴。");
                    });
                    ctx.later(140, () -> {
                        ctx.stripAll();
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 120, 3);
                        ctx.obliterate();
                        ctx.broadcast("§4今年这块地有了新的肥料。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_granary_burial", "谷仓活埋", TrollTier.OBLIVION)
                .desc("谷物从四面八方涌来把你封在正中间，只留一口气听完接下来的部分")
                .weight(30)
                .cooldownSeconds(470)
                .run(ctx -> {
                    ctx.msg("§7谷物从四面八方涌来，把你封在正中间。");
                    ctx.later(50, () -> {
                        ctx.buryAlive();
                        ctx.damage(3.0f);
                        ctx.msg("§4它们给你留了一口气，专门用来听完接下来的部分。");
                    });
                    ctx.later(140, () -> {
                        ctx.meteorShower(3, 14.0);
                        ctx.obliterate();
                        ctx.broadcast("§4谷仓盘点结束：入库一件，出库零件。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_meteor_fertilizer", "陨石施肥", TrollTier.OBLIVION)
                .desc("农业公告说本季施用天肥，肥料从天上带着火落下来，落点是你")
                .weight(29)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e【农业提示】本季施用天肥，产量可观。");
                    ctx.later(50, () -> {
                        ctx.meteorShower(8, 18.0);
                        ctx.damage(3.0f);
                        ctx.glitch(GlitchIds.SHAKY, 100, 3);
                        ctx.msg("§4肥料从天上来，带着火，落点是你的田和你。");
                    });
                    ctx.later(150, () -> {
                        ctx.meteorShower(4, 14.0);
                        ctx.obliterate();
                        ctx.broadcast("§4农业局确认：本季产量为负。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_frost_famine", "霜冻饥荒", TrollTier.APOCALYPSE)
                .desc("气温骤降，作物冻死，你的身体决定跟着一起")
                .weight(29)
                .cooldownSeconds(480)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 360, 2);
                    ctx.starve();
                    ctx.damage("freeze", 4.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.msg("§b霜冻覆盖全田§7，§8下一个是你。§7");
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_swarm_devour", "虫群吞噬", TrollTier.OBLIVION)
                .desc("虫群不再满足于作物，它们对你进行了一次实地考察，结论是你可以吃")
                .weight(29)
                .cooldownSeconds(490)
                .run(ctx -> {
                    ctx.msg("§7虫群不满足于作物了。它们想对你进行一次实地考察。");
                    ctx.later(50, () -> {
                        ctx.takeRandomItems(9);
                        ctx.damage(3.0f);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                        ctx.msg("§4考察结论：可食用，且不会反抗。");
                    });
                    ctx.later(140, () -> {
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 120, 3);
                        ctx.obliterate();
                        ctx.broadcast("§4虫群离场时，田里多了一片干净的白色。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hell_harvest", "地狱丰收", TrollTier.OBLIVION)
                .desc("你脚下的土地变成熔岩，作物和你的处境实现了高度统一")
                .weight(28)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.msg("§4你脚下的土地变成了熔岩。作物和你的处境高度一致。");
                    ctx.later(50, () -> {
                        ctx.lavaPit(3);
                        ctx.setFire(6);
                        ctx.damage("lava", 3.0f);
                        ctx.msg("§4丰收的粮食装进了熔炉。收割的人也是。");
                    });
                    ctx.later(140, () -> {
                        ctx.meteorShower(4, 16.0);
                        ctx.obliterate();
                        ctx.broadcast("§4今年的丰收季结束了，炉温还很高。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_time_drought", "干旱三千年", TrollTier.APOCALYPSE)
                .desc("有人把时间快进了三千年，全程没有下过一滴雨")
                .weight(28)
                .cooldownSeconds(540)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.timeSkip(3000);
                    ctx.clearWeather();
                    ctx.effectRandom(340, 2);
                    ctx.damage("starve", 3.0f);
                    ctx.msg("§6气象记录§7：连续干旱§e3000 年§8，纪录保持者：你。§7");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_hive_invasion", "蜂巢入侵现实", TrollTier.OBLIVION)
                .desc("蜂巢的边界变模糊，蜜蜂从你视野的缝隙里钻出来——它们本来就在里面")
                .weight(28)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 120, 3);
                    ctx.msg("§4蜂巢的边界变得模糊，蜜蜂开始从你视野的缝隙里钻出来。");
                    ctx.later(50, () -> {
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 120, 3);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                        ctx.damage(3.0f);
                        ctx.msg("§4它们不是从外面进来的。它们本来就在里面。");
                    });
                    ctx.later(150, () -> {
                        ctx.obliterate();
                        ctx.broadcast("§4蜂巢把现实收回去了一个口子，收口处刚好是你。");
                    });
                })
                .build());

        // ================================================================
        // OBLIVION —— 绝望：假丰收、牲畜暴走、自然翻脸
        // ================================================================

        TrollEvents.reg(TrollEvent.of("farm_famine", "大饥荒", TrollTier.OBLIVION)
                .desc("全服公告说粮仓已补满，那条公告其实是从你胃里发出来的")
                .weight(25)
                .cooldownSeconds(1000)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e【全服】粮仓已补满，请放心取用。");
                    ctx.later(50, () -> {
                        ctx.stripAll();
                        ctx.starve();
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 800, 2);
                        ctx.msg("§4粮仓是空的。刚才那条公告是从你的胃里发出来的。");
                    });
                    ctx.later(150, () -> {
                        ctx.damage("starve", 3.0f);
                        ctx.obliterate();
                        ctx.broadcast("§4饥荒的第一条规矩：先饿死记账的人。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_cattle_apocalypse", "牲畜天启", TrollTier.OBLIVION)
                .desc("所有牲畜同时变异，排着队朝你发起总攻，秩序井然得像在收租")
                .weight(24)
                .cooldownSeconds(1100)
                .run(ctx -> {
                    ctx.msg("§4所有牲畜同时变异，然后朝你所在的方向发起了总攻。");
                    ctx.later(50, () -> {
                        for (int i = 0; i < 4; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                        }
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 9);
                        ctx.freeze(120);
                        ctx.msg("§4它们排着队，很有秩序，像在收租。");
                    });
                    ctx.later(150, () -> {
                        ctx.meteorShower(3, 14.0);
                        ctx.obliterate();
                        ctx.broadcast("§4农场牲畜登记表的最后一行，是你的名字。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_world_granary", "世界粮仓", TrollTier.OBLIVION)
                .desc("你被改造成世界粮仓的核心部件：永久产权、没有窗，也没有门")
                .weight(23)
                .cooldownSeconds(1200)
                .run(ctx -> {
                    ctx.title("§2§l入选通知", "§7你已成为世界粮仓的核心部件");
                    ctx.msg("§4永久产权，没有窗户，也没有门。");
                    ctx.later(60, () -> {
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 800, 2);
                        ctx.msg("§4他们说你负责储存。储存清单的第一项，是你自己。");
                    });
                    ctx.later(160, () -> {
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 160, 3);
                        ctx.obliterate();
                        ctx.broadcast("§4世界粮仓完成了一次库存更新。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_locust_judgment", "蝗虫审判", TrollTier.OBLIVION)
                .desc("蝗虫法庭开庭：被告一名，法官一片，陪审团无数，判决是没收收成的人")
                .weight(22)
                .cooldownSeconds(1300)
                .run(ctx -> {
                    ctx.title("§4§l蝗虫法庭", "§7被告一名，法官一片，陪审团无数");
                    ctx.later(50, () -> {
                        ctx.glitch(GlitchIds.SHAKY, 120, 3);
                        ctx.meteorShower(6, 16.0);
                        ctx.starve();
                        ctx.msg("§4判决书：没收全部收成，并处没收收成的人。");
                    });
                    ctx.later(150, () -> {
                        ctx.obliterate();
                        ctx.broadcast("§4退庭。地上只剩下一层会动的黑色。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_doomsday_harvest", "末日收割", TrollTier.OBLIVION)
                .desc("收割机从天的方向来了，它不区分作物和人，只会完成一次彻底清仓")
                .weight(22)
                .cooldownSeconds(1400)
                .run(ctx -> {
                    ctx.fakeAchievement("丰收年", "本季收成：负一整年");
                    ctx.later(50, () -> {
                        ctx.stripAll();
                        ctx.meteorShower(8, 18.0);
                        ctx.msg("§4收割机从天的方向来了。它不区分作物和人。");
                    });
                    ctx.later(160, () -> {
                        ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 5.0f);
                        ctx.obliterate();
                        ctx.broadcast("§4有人替你完成了彻底清仓，连你身上的那份也没放过。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_nature_revenge", "自然复仇", TrollTier.OBLIVION)
                .desc("土地、岩浆、虚空和守卫者开了一场会，会议纪要只有一句：处理完毕")
                .weight(21)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.broadcast("§4土地、岩浆、虚空和守卫者达成了一份共识。");
                    ctx.msg("§4共识的内容，是关于你的。");
                    ctx.later(60, () -> {
                        ctx.lavaPit(2);
                        ctx.voidPit(2, 12);
                        for (int i = 0; i < 3; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                        }
                    });
                    ctx.later(160, () -> {
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 160, 3);
                        ctx.obliterate();
                        ctx.broadcast("§4自然界的会议纪要只有一句：处理完毕。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("farm_last_harvest", "最后的丰收", TrollTier.OBLIVION)
                .desc("伪造死亡画面再把你这一季的成果全部归零，收割方从来不是你")
                .weight(20)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.fakeDeathScreen("§7本季最后一次丰收，主角是你");
                    ctx.msg("§a丰收了！§7（先别高兴，看清是谁在被收。）");
                    ctx.later(60, () -> {
                        ctx.glitch(GlitchIds.FAKE_CRASH, 120, 3);
                        ctx.stripAll();
                        ctx.msg("§4这一季的成果全部归零。你也是这一季的成果之一。");
                    });
                    ctx.later(170, () -> {
                        ctx.obliterate();
                        ctx.broadcast("§4最后的丰收只有一次，收获方不是你。");
                    });
                })
                .build());
    }
}
