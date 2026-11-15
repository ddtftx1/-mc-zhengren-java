package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;
import net.minecraft.item.Items;

/**
 * 【宝藏与诱饵】分类事件。
 * 主题：先给你希望，再让你绝望。
 * 负责整活：假钻石、假宝藏箱、挖矿惊魂、附魔骗局、交易骗局、装备「升级」成垃圾、
 * 藏宝图骗局、假成就奖励、假传送门、陨石宝藏……
 * 只用 ctx.* + TrollPools.* + GlitchIds.*，外加契约白名单内的 Items。
 */
public final class LootEvents {

    private LootEvents() {
    }

    public static void register() {
        TrollEvents.category("宝藏与诱饵");

        // ==================================================================
        // PRANK —— 小整，逗你玩一下
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("loot_fake_vein_signal", "假矿脉信号", TrollTier.PRANK)
                .desc("发出「附近有矿脉」的诱饵，然后在附近撒几块矿石逗你")
                .weight(180)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.ORE_TEASE), ctx.ri(2, 4));
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 1.0f);
                    ctx.actionBar("§e探测到矿脉信号！就在附近！");
                    ctx.msg("§7（信号由模组随机生成，不承担任何责任。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_chest_echo", "空箱回声", TrollTier.BRUTAL)
                .desc("播放开箱与关箱音效，让你以为旁边真有个宝箱")
                .weight(170)
                .cooldownSeconds(45)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.8f, 0.9f);
                    ctx.fakeBroadcast("附近检测到未开启的宝箱……大概。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_miner_allowance", "挖矿补贴", TrollTier.ANNOYING)
                .desc("以「挖矿补贴」的名义塞给你一堆垃圾")
                .weight(175)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.giveRandomJunk();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.ok("挖矿补贴已发放，请注意查收。");
                    ctx.msg("§7补贴的形式是……算了，你自己看吧。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fishing_junk", "钓鱼上钩垃圾", TrollTier.BRUTAL)
                .desc("假装你钓上了什么，结果是一只旧靴子")
                .weight(165)
                .cooldownSeconds(45)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.giveRandomJunk();
                    ctx.msg("§7水面炸开——你钓上来一只湿透的旧靴子。");
                    ctx.actionBar("§e上钩了！……大概是。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_mystery_trinket", "神秘小玩意", TrollTier.ANNOYING)
                .desc("送你一件神秘物品，用途未知，价值更未知")
                .weight(155)
                .cooldownSeconds(60)
                .run(ctx -> {
                    var it = ctx.pick(TrollPools.MYSTERY_ITEMS);
                    ctx.give(it, 1);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.9f, 1.2f);
                    ctx.msg("§7神秘物品已入账。用途：未知。价值：更未知。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fake_achievement_pop", "假成就弹窗", TrollTier.PRANK)
                .desc("弹出一个假成就，奖励是零")
                .weight(150)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_ACHIEVEMENTS), "奖励：本成就没有任何奖励");
                    ctx.sys("成就进度已保存（并没有）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_autosave_lie", "假自动保存", TrollTier.ANNOYING)
                .desc("假装世界正在保存，制造一种「刚好卡了」的错觉")
                .weight(145)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.sys("如果你现在卡住了，那是保存的错，不是模组的错。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_loading_tip", "假加载提示", TrollTier.ANNOYING)
                .desc("用一条没用的加载提示占满你的注意力")
                .weight(140)
                .cooldownSeconds(90)
                .run(ctx -> ctx.fakeBroadcast(ctx.pick(TrollPools.FAKE_TIPS)))
                .build());

        TrollEvents.reg(TrollEvent.of("loot_quest_offer", "假任务邀请", TrollTier.ANNOYING)
                .desc("发布一个根本做不完的假任务")
                .weight(135)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.fakeChat("系统", "新任务： " + ctx.pick(TrollPools.FAKE_QUESTS));
                    ctx.actionBar("§e按 J 查看任务详情（本模组没有 J 键功能）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_buried_whisper", "地下的低语", TrollTier.BRUTAL)
                .desc("用环境音和一句话让你怀疑脚下埋着东西")
                .weight(140)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 0.8f);
                    ctx.msg("§7你脚下的土里好像有东西……大概只是石头。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_lucky_draw", "假抽奖", TrollTier.BRUTAL)
                .desc("宣布你中了幸运大奖，然后用安慰奖打发你")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.fakeBroadcast("恭喜 " + ctx.playerName() + " 抽中幸运大奖！");
                    ctx.giveRandomJunk();
                    ctx.sys("奖品运输途中遗失，已改为发放安慰奖。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fake_disc", "假唱片", TrollTier.PRANK)
                .desc("给你一张唱片，放出来只有静电噪声")
                .weight(135)
                .cooldownSeconds(120)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.give(Items.MUSIC_DISC_CAT, 1);
                    ctx.renameRandomItem(ctx.pick(TrollPools.CURSED_NAMES));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.9f, 0.8f);
                    ctx.msg("§7放进唱片机后，它开始播放长达三分钟的静电噪声。");
                })
                .build());

        // ==================================================================
        // ANNOYING —— 烦人，会皱眉
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("loot_ore_vein_tease", "假矿脉", TrollTier.BRUTAL)
                .desc("撒一把矿石方块骗你开挖，同时给手上工具上冷却")
                .weight(150)
                .cooldownSeconds(150)
                .when(ctx -> !ctx.invEmpty() && !ctx.held().isEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.scatterBlocks(ctx.pick(TrollPools.ORE_TEASE), ctx.ri(4, 8));
                    ctx.ok("探测到矿脉！开挖吧！");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    var held = ctx.held();
                    if (!held.isEmpty()) {
                        ctx.setCooldown(held.getItem(), ctx.ri(80, 160));
                    }
                    ctx.warn("你的工具突然需要冷静一下。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fools_gold", "愚人金", TrollTier.NIGHTMARE)
                .desc("给你几块金锭，随后鉴定为「不含金的矿石」")
                .weight(140)
                .cooldownSeconds(150)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.give(Items.GOLD_INGOT, ctx.ri(2, 3));
                    ctx.ok("你捡到了几块金锭！");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.renameRandomItem("§7愚人金（不含金）");
                    ctx.msg("§7经鉴定：主要成分为黄铁矿，市场价约 0 元。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_map_to_nowhere", "藏宝图骗局", TrollTier.NIGHTMARE)
                .desc("给你一张藏宝图，坐标是编的，图是空白的")
                .weight(135)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.give(Items.MAP, 1);
                    ctx.renameRandomItem("§7空白藏宝图");
                    ctx.fakeBroadcast("藏宝坐标已发送：X=" + ctx.ri(-999, 999) + " Z=" + ctx.ri(-999, 999));
                    ctx.msg("§7你展开地图——上面什么都没有，因为它是一张白纸。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_enchanted_bait", "附魔骗局", TrollTier.NIGHTMARE)
                .desc("它免费为你的装备附魔。耐久是真的，消失诅咒也是真的，只是它没说")
                .weight(130)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.ok("检测到你的装备，正在免费附魔……");
                    ctx.enchantHeldRandomly();
                    ctx.curseHeld();
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.9f, 1.1f);
                    ctx.warn("附魔完成：一份耐久，外加一份小礼物。");
                    ctx.msg("§8（礼物是消失诅咒，不用谢。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_exp_bottle_swindle", "经验瓶骗局", TrollTier.BRUTAL)
                .desc("发几瓶经验瓶，然后说有一瓶在运输途中蒸发了")
                .weight(130)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.give(Items.EXPERIENCE_BOTTLE, ctx.ri(2, 4));
                    ctx.ok("获得经验瓶，请及时使用。");
                    ctx.takeRandomItem();
                    ctx.warn("有一瓶在运输途中蒸发了，物流不赔。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_trap_crate", "陷阱箱", TrollTier.NIGHTMARE)
                .desc("用宝箱音效和一堆机关方块欢迎你开箱")
                .weight(125)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                    ctx.scatterBlocks(ctx.pick(TrollPools.TRAP_BLOCKS), ctx.ri(3, 6));
                    ctx.ok("宝藏箱已就位！");
                    ctx.msg("§7（能触发机关的也是它。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_quest_reward_bait", "任务奖励", TrollTier.BRUTAL)
                .desc("宣布任务完成并发放奖励，实际奖励是两批垃圾")
                .weight(120)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.fakeChat("系统", "任务完成！奖励发放中……");
                    ctx.giveRandomJunk();
                    ctx.giveRandomJunk();
                    ctx.sys("奖励已发放：两份垃圾，不设找零。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_gravel_panning", "淘金梦", TrollTier.BRUTAL)
                .desc("让你以为淘到了金粒，顺手筛走你一件东西")
                .weight(120)
                .cooldownSeconds(150)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.give(Items.GRAVEL, ctx.ri(4, 8));
                    ctx.give(Items.GOLD_NUGGET, 1);
                    ctx.ok("恭喜！你淘到了一粒金！");
                    ctx.takeRandomItem();
                    ctx.warn("作为筛金的代价，你失去了别的东西。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fake_operator", "假管理员", TrollTier.NIGHTMARE)
                .desc("用一句管理员口吻的话让你紧张三十秒")
                .weight(115)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.fakeBroadcast(ctx.pick(TrollPools.FAKE_OPERATOR_LINES));
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_loot_swap", "掉落物置换", TrollTier.NIGHTMARE)
                .desc("偷走一件东西，然后塞给你一批垃圾当作补偿")
                .weight(120)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.takeRandomItem();
                    ctx.giveRandomJunk();
                    ctx.sys("检测到掉落物异常，已为你自动置换为等价的……东西。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_dowsing_rod", "探宝器", TrollTier.BRUTAL)
                .desc("送你一件奇怪道具，然后让你的视角原地打转")
                .weight(115)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.give(ctx.pick(TrollPools.WEIRD_ITEMS), 1);
                    ctx.spin(ctx.rf(-180f, 180f));
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.9f, 1.2f);
                    ctx.actionBar("§e指针疯狂旋转……它也不知道宝藏在哪。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_ender_chest_prank", "末影箱恶作剧", TrollTier.BRUTAL)
                .desc("假装有人翻过你的末影箱，其实什么都没动")
                .weight(110)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "你的末影箱我看过了，东西不错，就是有点少。");
                    ctx.msg("§7你检查了一下末影箱——什么都没少。但你就是不放心。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_villager_trade_scam", "村民交易骗局", TrollTier.NIGHTMARE)
                .desc("冒充村民和你做一笔稳赚不赔的交易")
                .weight(110)
                .cooldownSeconds(240)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.fakeChat("村民", "交易吗？我用好东西换你背包里一格东西。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.takeRandomItem();
                    ctx.giveRandomJunk();
                    ctx.warn("交易完成。他赚了，你没赚。");
                })
                .build());

        // ==================================================================
        // BRUTAL —— 抓狂，会骂人
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("loot_fools_gold_reclaim", "愚人金回收", TrollTier.APOCALYPSE)
                .desc("发给你几块金锭，再当着你的面全部回收")
                .weight(58)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.give(Items.GOLD_INGOT, 3);
                    ctx.ok("稀有掉落！三块金锭已发放！");
                    ctx.flashTotem();
                    ctx.takeRandomItem();
                    ctx.takeRandomItem();
                    ctx.takeRandomItem();
                    ctx.warn("§c愚人金已全部回收，感谢你的配合。");
                    ctx.addRage(2.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_diamond_switcheroo", "钻石调包", TrollTier.APOCALYPSE)
                .desc("给你钻石，然后把它们换成木炭")
                .weight(56)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.give(Items.DIAMOND, ctx.ri(2, 4));
                    ctx.ok("§b你挖到了钻石！");
                    ctx.playLevelUp();
                    ctx.takeRandomItem();
                    ctx.give(Items.CHARCOAL, ctx.ri(2, 4));
                    ctx.warn("§c钻石已替换为木炭，手感相近，请谅解。");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_gem_to_glass", "宝石变玻璃", TrollTier.APOCALYPSE)
                .desc("绿宝石当场熔炼成几块玻璃")
                .weight(55)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.give(Items.EMERALD, ctx.ri(2, 4));
                    ctx.ok("获得绿宝石！");
                    ctx.takeRandomItem();
                    ctx.give(Items.GLASS, ctx.ri(3, 6));
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.8f, 1.2f);
                    ctx.warn("§c宝石已完成玻璃化处理，透光性极佳。");
                    ctx.addRage(2.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fake_netherite", "假下界合金", TrollTier.APOCALYPSE)
                .desc("宣布你拿到了下界合金碎片，然后兑换成石头")
                .weight(55)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.give(Items.NETHERITE_SCRAP, 1);
                    ctx.title("§6§l稀有掉落！", "§7下界合金碎片");
                    ctx.flashTotem();
                    ctx.takeRandomItem();
                    ctx.give(Items.COBBLESTONE, ctx.ri(3, 6));
                    ctx.warn("§c碎片已被兑换成等重的石头，含金量：0。");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fake_elytra", "假鞘翅", TrollTier.NIGHTMARE)
                .desc("它宣布你的鞘翅已经在路上了。你等到的，是几块够缝一只翅膀的皮革")
                .weight(54)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.ok("检测到鞘翅发放中……");
                    ctx.playLevelUp();
                    ctx.takeRandomItem();
                    ctx.give(Items.LEATHER, ctx.ri(2, 4));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.warn("§c发放失败，已改为等价的皮革。飞行功能请自备。");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_enchanted_book_scam", "空白附魔书", TrollTier.APOCALYPSE)
                .desc("给你一本「经验修补」，翻开只有一句谢谢惠顾")
                .weight(54)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.give(Items.BOOK, 1);
                    ctx.ok("获得附魔书：§d经验修补！");
                    ctx.renameRandomItem("§7空白书（原·经验修补）");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.warn("翻开后只有一句话：谢谢惠顾。");
                    ctx.addRage(2.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_gear_upgrade_scam", "装备升级骗局", TrollTier.BRUTAL)
                .desc("免费帮你「升级」装备，升级结果是耐久见底")
                .weight(52)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.ok("检测到你的装备，正在免费升级……");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.swapEquipmentRandomly();
                    ctx.damageRandomTool(40);
                    ctx.breakRandomTool();
                    ctx.warn("§c升级完成：你的装备现在更「轻」了。");
                    ctx.addRage(3.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fake_vein_collapse", "假矿脉塌方", TrollTier.BRUTAL)
                .desc("喊你来看矿脉，然后让你脚下的地陷下去")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.ORE_TEASE), ctx.ri(6, 12));
                    ctx.ok("发现矿脉！就在你脚下！");
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.voidPit(1, 3);
                    ctx.warn("§c矿脉没挖到，倒是先挖到了自己的脚。");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_treasure_ambush", "宝藏伏击", TrollTier.APOCALYPSE)
                .desc("用宝箱音效引诱你过去，那边等着的是三只怪")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                    ctx.ok("宝藏就在前面！快过去！");
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    }
                    ctx.title("§c§l宝箱有主人", "§7而且不止一个");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fake_golden_apple", "假附魔金苹果", TrollTier.APOCALYPSE)
                .desc("发放附魔金苹果，然后把 buff 换成普通苹果")
                .weight(52)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.give(Items.GOLDEN_APPLE, 1);
                    ctx.ok("获得附魔金苹果！");
                    ctx.flashTotem();
                    ctx.takeRandomItem();
                    ctx.give(Items.APPLE, ctx.ri(1, 3));
                    ctx.warn("§c附魔效果已被替换为「苹果味」。");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_mystery_junk_bundle", "看起来很强的垃圾", TrollTier.APOCALYPSE)
                .desc("一口气发一堆神秘物品，随后收走其中几件")
                .weight(50)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    int n = ctx.ri(3, 5);
                    for (int i = 0; i < n; i++) {
                        ctx.give(ctx.pick(TrollPools.MYSTERY_ITEMS), 1);
                    }
                    ctx.ok("一大堆神秘物品已到账！");
                    ctx.takeRandomItem();
                    ctx.takeRandomItem();
                    ctx.takeRandomItem();
                    ctx.warn("§c神秘物品的实用价值：约等于零。回收完毕。");
                    ctx.addRage(2.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fake_spawn_egg", "蛋里孵出的东西", TrollTier.NIGHTMARE)
                .desc("它把蛋放进你手里。孵出来的东西，从一开始就不是给你养的")
                .weight(48)
                .cooldownSeconds(330)
                .run(ctx -> {
                    ctx.giveRandomJunk();
                    ctx.ok("获得神秘刷怪蛋！");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING), ctx.x() + 1.0, ctx.y() + 1.0, ctx.z());
                    ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING), ctx.x() - 1.0, ctx.y() + 1.0, ctx.z());
                    ctx.warn("蛋里孵出来的东西，跟你想的不太一样。");
                    ctx.addRage(2.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_spyglass_hallucination", "望远镜幻觉", TrollTier.NIGHTMARE)
                .desc("送你一个望远镜。镜筒里那东西本来在很远的地方，现在它也在看你")
                .weight(48)
                .cooldownSeconds(330)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.give(ctx.pick(TrollPools.WEIRD_ITEMS), 1);
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 0.9f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.ANNOYING_MOBS), 10);
                    ctx.title("§4§l你看到了什么", "§7（其实什么都没有，真的）");
                    ctx.addRage(2.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_void_treasure", "虚空藏宝", TrollTier.APOCALYPSE)
                .desc("探测到脚下有高价值目标，然后给你挖了个坑")
                .weight(46)
                .cooldownSeconds(330)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.sys("探测到地下 4 格有高价值目标。");
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.9f, 0.9f);
                    ctx.voidPit(1, 4);
                    ctx.spawnFallingBlock(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.warn("§c宝藏没找到，坑倒是有一个。");
                    ctx.addRage(2.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fake_death_loot", "假死亡掉宝", TrollTier.APOCALYPSE)
                .desc("弹出假死亡提示，同时真的丢你两件东西")
                .weight(48)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.fakeDeathScreen("被自己的贪心害死");
                    ctx.dropRandom();
                    ctx.dropRandom();
                    ctx.warn("你并没有真的死，但东西是真的掉了。");
                    ctx.addRage(3.0);
                })
                .build());

        // ==================================================================
        // NIGHTMARE —— 崩溃，会砸键盘
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("loot_hard_blackout_treasure", "黑暗宝箱", TrollTier.NIGHTMARE)
                .desc("开箱的瞬间黑屏。黑暗里传来翻找声，而且它知道你每件东西放在哪")
                .weight(45)
                .cooldownSeconds(420)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.ok("§e宝箱已开启！");
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 200, 2);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.8f);
                    ctx.takeRandomItem();
                    ctx.takeRandomItem();
                    ctx.warn("§c黑暗中，有人替你收好了战利品。");
                    ctx.addRage(4.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_world_reload_scam", "世界重载骗局", TrollTier.OBLIVION)
                .desc("宣称正从备份里为你捞回被吞的宝藏，进度条走满之前你只能等，而它知道你在等")
                .weight(42)
                .cooldownSeconds(480)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.ok("§e检测到你的宝物流失，正在从备份中恢复……");
                    ctx.sys("恢复进度：§e12%");
                    ctx.later(50, () -> {
                        ctx.sys("恢复进度：§e47%");
                        ctx.sound(ctx.pick(TrollPools.IMPACT), 0.8f, 0.9f);
                    });
                    ctx.later(110, () -> {
                        ctx.sys("恢复进度：§e99%");
                        ctx.fakeSaveNotice();
                        ctx.msg("§8（进度条停了。§7它就停在那里，看着你。）");
                    });
                    ctx.later(180, () -> {
                        ctx.glitch(GlitchIds.WORLD_RELOAD, 160, 3);
                        ctx.buryAlive();
                        ctx.takeRandomItems(4);
                        ctx.warn("§4恢复完成：备份里 §l没有你的东西§r§4，只有你的位置。");
                        ctx.obliterate();
                    });
                    ctx.addRage(6.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_virus_chest", "病毒宝箱", TrollTier.APOCALYPSE)
                .desc("假病毒警告，顺便偷走你一件东西")
                .weight(40)
                .cooldownSeconds(480)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 120, 2);
                    ctx.fakeBroadcast("检测到木马文件：宝藏_免费版.exe");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.takeRandomItem();
                    ctx.warn("§c清理程序顺手删掉了你一件物品。");
                    ctx.addRage(4.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_meteor_shower_big", "陨石宝藏", TrollTier.NIGHTMARE)
                .desc("它让你抬头看宝藏。落下来的东西确实很重，每一下都算数")
                .weight(38)
                .cooldownSeconds(480)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.ok("天降宝藏！抬头看！");
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.9f);
                    ctx.meteorShower(ctx.ri(6, 10), 16);
                    ctx.title("§6§l天降宝藏", "§7请自备头盔");
                    ctx.addRage(4.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_gift_burn", "礼物焚毁", TrollTier.OBLIVION)
                .desc("先亲手把三件好东西放进你背包，再让你看着它们在运输途中一件件烧掉")
                .weight(45)
                .cooldownSeconds(420)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.give(ctx.pick(TrollPools.LOOT), 1);
                    }
                    ctx.flashTotem();
                    ctx.ok("§a三件礼物已签收，请查收。");
                    ctx.later(60, () -> {
                        ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                        ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 40);
                        ctx.takeRandomItems(3);
                        ctx.warn("§c礼物在运输途中自燃了 §c1/3§7。保险不予理赔。");
                    });
                    ctx.later(140, () -> {
                        ctx.setFire(10);
                        ctx.stripAll();
                        ctx.give(Items.CHARCOAL, 9);
                        ctx.warn("§4剩下的两件也烧了。§7作为补偿，给你 §8九块木炭§7。");
                        ctx.obliterate();
                    });
                    ctx.addRage(7.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_treasure_cage", "宝箱牢笼", TrollTier.OBLIVION)
                .desc("把宝藏和你一起关进笼子，然后当着你的面，把钥匙和钻石一起抬走")
                .weight(40)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.TROLL_BLOCKS[1]);
                    ctx.give(Items.DIAMOND, 3);
                    ctx.title("§6§l宝箱是你的了", "§7钥匙在里面，门在外面");
                    ctx.ok("§b钻石已放入笼中，请自行取用。");
                    ctx.later(80, () -> {
                        ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                        for (int i = 0; i < 3; i++) {
                            ctx.takeRandomItem();
                        }
                        ctx.warn("§c钻石连同箱子一起被人抬走了。§7你在笼子里，看得很清楚。");
                    });
                    ctx.later(160, () -> {
                        ctx.effectRandom(600, 2);
                        ctx.buryAlive();
                        ctx.obliterate();
                    });
                    ctx.addRage(7.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_freeze_search", "冻结搜身", TrollTier.OBLIVION)
                .desc("冻住你的手脚，让你清醒地感受背包被人一件件翻过去，而你数得很清楚")
                .weight(38)
                .cooldownSeconds(480)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 120, 3);
                    ctx.freeze(100);
                    ctx.msg("§7你动不了了。§8（只是暂时。）");
                    ctx.later(40, () -> {
                        ctx.soundAt(ctx.pick(TrollPools.CREEPY), ctx.x() + 1, ctx.y(), ctx.z(), 0.9f, 1.2f);
                        ctx.takeRandomItems(2);
                        ctx.warn("§c第一件被拿走了。§7你听见了拉链声。");
                    });
                    ctx.later(100, () -> {
                        ctx.takeRandomItems(3);
                        ctx.warn("§c第二件、第三件。§7你数得很清楚。");
                    });
                    ctx.later(170, () -> {
                        ctx.msg("§8（搜身结束。§7他走之前，帮你整理了一下衣领。）");
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.obliterate();
                    });
                    ctx.addRage(8.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_lucky_block", "幸运方块", TrollTier.NIGHTMARE)
                .desc("它保证开出的东西一定是你现在最需要的，并且不解释为什么")
                .weight(36)
                .cooldownSeconds(540)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.title("§6§l幸运方块！", "§7开出什么都不许生气");
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.9f);
                    ctx.effectRandom(240, 1);
                    ctx.giveRandomJunk();
                    ctx.giveRandomJunk();
                    ctx.warn("§c开出的奖励是：一个负面效果，两份垃圾。");
                    ctx.addRage(3.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fake_key", "假宝箱钥匙", TrollTier.APOCALYPSE)
                .desc("它给你一把钥匙，说是开箱用的。等你握紧，钥匙就已经在手心里消失了")
                .weight(36)
                .cooldownSeconds(540)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.give(Items.TRIPWIRE_HOOK, 1);
                    ctx.ok("获得宝箱钥匙！去找对应的箱子吧。");
                    ctx.takeRandomItem();
                    ctx.takeRandomItem();
                    ctx.fakeBroadcast("钥匙孔不匹配。");
                    ctx.warn("§c钥匙已遗失在某个你永远找不到的地方。");
                    ctx.addRage(4.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fake_portal", "假传送门", TrollTier.NIGHTMARE)
                .desc("传送门确实激活了。它只是没有义务告诉你，另一头是不是还有空气")
                .weight(34)
                .cooldownSeconds(540)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.title("§5§l传送门已激活", "§7目的地：未知");
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 60);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.8f);
                    ctx.glitch(GlitchIds.SPIN, 100, 2);
                    ctx.scatterFar(60);
                    ctx.warn("§c传送成功。至于传送到哪，模组也不知道。");
                    ctx.addRage(4.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_fake_beacon", "假信标", TrollTier.OBLIVION)
                .desc("为你点亮一座信标，效果每几秒换一次，最后一次叫「把你的坐标交出去」")
                .weight(34)
                .cooldownSeconds(540)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.title("§b§l信标已激活", "§7效果：随机（真的随机）");
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 1.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.later(60, () -> {
                        ctx.effectRandom(400, 1);
                        ctx.sys("信标效果刷新：§e再来一次§7。");
                    });
                    ctx.later(130, () -> {
                        for (int i = 0; i < 4; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                        }
                        ctx.takeRandomItems(3);
                        ctx.warn("§c信标效果刷新：§l坐标广播§r§c。附近所有生物都收到通知了。");
                    });
                    ctx.later(200, () -> {
                        ctx.obliterate();
                        ctx.fakeBroadcast("§4[信标] 目标已锁定并移交。感谢使用。");
                    });
                    ctx.addRage(8.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_achievement_price", "成就的代价", TrollTier.APOCALYPSE)
                .desc("它为你颁发一个成就，把奖励和代价写在同一行：奖励是你失去的全部物品")
                .weight(36)
                .cooldownSeconds(480)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_ACHIEVEMENTS), "奖励：你失去的全部物品");
                    ctx.clearInventory();
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.warn("§c成就达成！代价已从你的背包中扣除。");
                    ctx.addRage(5.0);
                })
                .build());

        // ==================================================================
        // APOCALYPSE —— 地狱，会退游戏
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("loot_ban_chest", "封禁宝箱", TrollTier.APOCALYPSE)
                .desc("它发给你「解封大礼」，再当着全服的面，把礼物和你的账号一起封进去")
                .weight(25)
                .cooldownSeconds(600)
                .once()
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.give(Items.DIAMOND, 16);
                    ctx.flashTotem();
                    ctx.ok("§b解封大礼已发放！共 16 颗钻石！");
                    for (int i = 0; i < 4; i++) {
                        ctx.takeRandomItem();
                    }
                    ctx.glitch(GlitchIds.FAKE_BAN, 200, 3);
                    ctx.fakeBroadcast(ctx.pick(TrollPools.FAKE_EXCUSES));
                    ctx.warn("§4§l礼物与账号一起被回收了。申诉入口：不存在。");
                    ctx.addRage(8.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_golden_trap", "黄金陷阱", TrollTier.OBLIVION)
                .desc("十六块金锭是真的。你伸手去拿的那一刻，它们开始一块块变成别人的东西")
                .weight(22)
                .cooldownSeconds(600)
                .once()
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.give(Items.GOLD_INGOT, 16);
                    ctx.flashTotem();
                    ctx.title("§6§l终极大奖", "§7金锭 ×16，请走近一点领取");
                    ctx.ok("§6金锭 ×16 已到账。");
                    ctx.later(70, () -> {
                        ctx.takeRandomItems(4);
                        ctx.warn("§c你手里的金锭正在蒸发。§7第 1 到第 4 块，已经没了。");
                    });
                    ctx.later(140, () -> {
                        ctx.clearInventory();
                        ctx.give(Items.GOLD_INGOT, 1);
                        ctx.warn("§4只剩一块了。§7是纪念品。");
                        ctx.broadcast("§4[整人大师] " + ctx.playerName() + " 刚刚清空了自己的背包。");
                    });
                    ctx.later(220, () -> {
                        ctx.title("§6§l谢谢惠顾", "§7欢迎下次光临");
                        ctx.obliterate();
                    });
                    ctx.addRage(10.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_update_loop", "更新卡死", TrollTier.APOCALYPSE)
                .desc("它开始下载一个「宝藏扩展包」。进度停在 99%，它不打算让你知道还剩多少")
                .weight(22)
                .cooldownSeconds(600)
                .once()
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.ok("正在下载「宝藏扩展包」……");
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 240, 2);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 240, 2);
                    ctx.fakeSaveNotice();
                    ctx.warn("§4§l进度 99%，已持续三小时。请勿关闭游戏。");
                    ctx.addRage(7.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_virus_heist", "木马洗劫", TrollTier.OBLIVION)
                .desc("一款名叫「宝藏_免费版」的程序请求管理员权限，你点了同意，它开始逐项清点")
                .weight(20)
                .cooldownSeconds(600)
                .once()
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.ok("§e正在下载「宝藏_免费版」…… §a完成。");
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 180, 3);
                    ctx.fakeBroadcast("§c检测到木马：宝藏_免费版.exe §7正在清点你的资产。");
                    ctx.later(50, () -> {
                        ctx.takeRandomItems(4);
                        ctx.setExperience(0);
                        ctx.warn("§c资产清点 1/3：§7物品在减少，经验已经归零。");
                    });
                    ctx.later(120, () -> {
                        ctx.takeRandomItems(4);
                        ctx.starve();
                        ctx.warn("§c资产清点 2/3：§7你开始饿了。这不是错觉。");
                    });
                    ctx.later(190, () -> {
                        ctx.stripAll();
                        ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.7f);
                        ctx.warn("§4§l资产清点 3/3：清算完成。§7祝你好运。");
                        ctx.obliterate();
                    });
                    ctx.addRage(10.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("loot_meteor_final", "终焉陨石", TrollTier.APOCALYPSE)
                .desc("最后一箱宝藏从天而降，砸在你头上")
                .weight(20)
                .cooldownSeconds(600)
                .once()
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.titleToAll("§4§l终焉宝藏", "§7从天而降");
                    ctx.broadcast("§4[整人大师] " + ctx.playerName() + " 抢到了最后一箱宝藏。");
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.7f);
                    ctx.meteorShower(12, 20);
                    ctx.msg("§8[系统] " + ctx.pick(TrollPools.TAUNTS));
                    ctx.addRage(9.0);
                })
                .build());
    }
}
