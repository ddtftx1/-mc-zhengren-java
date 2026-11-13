package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;
import net.minecraft.item.Items;

/**
 * 【物品与背包】分类事件。
 * 负责整活：背包、快捷栏、装备、工具耐久、掉落物、经验与饥饿。
 * 所有事件只使用 ctx.* 与 TrollPools.*，外加契约白名单里的 Items。
 */
public final class ItemEvents {

    /** 用来羞辱玩家的物品名 */
    private static final String[] SHAME_NAMES = {
            "§7捡来的破烂",
            "§7这根不值钱",
            "§c别人的东西",
            "§7没什么卵用的垃圾",
            "§e淘宝九块九包邮",
            "§c我配不上用它",
            "§7祖传废铁，有划痕",
            "§c已过期，请勿食用",
            "§7偷偷藏起来的小玩具",
            "§8不知道是谁塞进来的",
            "§7你确定这是你的？",
            "§e二手货，不退不换"
    };

    private ItemEvents() {
    }

    public static void register() {
        TrollEvents.category("物品与背包");

        // ==================================================================
        // PRANK —— 小整，笑一下就好
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("item_potato_delivery", "送你一个土豆", TrollTier.PRANK)
                .desc("往背包里塞一堆没用的东西，还配上一声饱嗝")
                .weight(170)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.giveRandomJunk();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.msg("§7有人往你背包里塞了点东西……别问是谁。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_pocket_eggs", "兜里多了鸡蛋", TrollTier.PRANK)
                .desc("凭空多出几颗鸡蛋，摔了算你的")
                .weight(160)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.give(Items.EGG, ctx.ri(1, 3));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.4f);
                    ctx.actionBar("§e你感觉口袋里有点沉。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_rename_petty", "名字被改了", TrollTier.PRANK)
                .desc("随机给背包里某件东西改一个羞辱性的名字")
                .weight(150)
                .cooldownSeconds(60)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.renameRandomItem(ctx.pick(SHAME_NAMES));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.9f, 1.1f);
                    ctx.msg("§7你的某件物品被贴上了新标签。§8（打开背包看看）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_durability_bite", "耐久被啃了一口", TrollTier.PRANK)
                .desc("随机一件有耐久的工具被啃掉一小截")
                .weight(150)
                .cooldownSeconds(60)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.damageRandomTool(ctx.ri(1, 8));
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.8f, 1.3f);
                    ctx.msg("§7你听见什么东西被咬了一口的脆响。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_slot_hijack", "槽位被夺舍", TrollTier.PRANK)
                .desc("把玩家的选中快捷栏槽位随机挪走")
                .weight(140)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.randomSelectSlot();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 1.2f);
                    ctx.actionBar("§e你手里的东西自己换了一件。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_fake_pickpocket", "好像被摸了", TrollTier.PRANK)
                .desc("纯造谣：假装有小偷路过，其实什么都没拿")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.9f, 1.0f);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "刚路过，顺手拿了你点东西，不谢。");
                    ctx.msg("§7你下意识打开背包检查了一遍……");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_stack_tease", "堆叠被拆散了", TrollTier.PRANK)
                .desc("随机一件物品被改名成被拆散的半摞，纯精神攻击")
                .weight(120)
                .cooldownSeconds(120)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.renameRandomItem("§7半摞被拆散的残骸");
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "你那一摞我帮你拆成单份了，方便使用。");
                    ctx.msg("§8[系统] 物品堆叠完整性：§c已损坏");
                })
                .build());

        // ==================================================================
        // ANNOYING —— 烦人，会皱眉
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("item_hotbar_jumble", "快捷栏大乱", TrollTier.ANNOYING)
                .desc("把快捷栏九格的顺序彻底打乱")
                .weight(150)
                .cooldownSeconds(90)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.warn("你的快捷栏刚刚被洗了一遍，习惯的位置全没了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_silent_pickpocket", "神不知鬼不觉", TrollTier.ANNOYING)
                .desc("随机偷走一格物品，只留一句含糊的提示")
                .weight(140)
                .cooldownSeconds(120)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    if (ctx.takeRandomItem()) {
                        ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.2f);
                        ctx.sys("背包里好像少了点什么。也许是你记错了。");
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_butterfingers", "手滑了", TrollTier.ANNOYING)
                .desc("手中的物品莫名其妙掉在地上")
                .weight(130)
                .cooldownSeconds(120)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.dropHeld();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§7啪嗒。你的手突然不属于你了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_backpack_shuffle", "背包大洗牌", TrollTier.ANNOYING)
                .desc("把玩家背包里的东西全部打乱")
                .weight(120)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.shuffleInventory();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.warn("你的背包刚刚翻了个身，现在它自己都找不到东西了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_curse_vanishing", "消失的诅咒", TrollTier.ANNOYING)
                .desc("给手中物品附上消失诅咒，死了就真没了")
                .weight(110)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.curseHeld();
                    ctx.sound(ctx.pick(TrollPools.HORROR), 0.8f, 1.2f);
                    ctx.warn("你手里的东西获得了一个它并不想要的附魔。");
                    ctx.msg("§8提示：死亡时它会跟着你一起消失。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_junk_rain", "垃圾雨", TrollTier.ANNOYING)
                .desc("连续往背包里塞好几批垃圾")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    int rounds = ctx.ri(2, 5);
                    for (int i = 0; i < rounds; i++) {
                        ctx.giveRandomJunk();
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§7天上不会掉馅饼，但会掉垃圾。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_food_to_rot", "新鲜的腐肉", TrollTier.ANNOYING)
                .desc("偷走一件东西，然后还你几块腐肉当补偿")
                .weight(110)
                .cooldownSeconds(150)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.give(Items.ROTTEN_FLESH, ctx.ri(2, 4));
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.9f, 1.0f);
                    ctx.msg("§7你的食物被置换了。§8（这不叫变质，这叫升级）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_armor_shuffle", "装备穿错了", TrollTier.ANNOYING)
                .desc("随机交换玩家身上的两件装备，来回折腾几次")
                .weight(100)
                .cooldownSeconds(120)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    int times = ctx.ri(2, 5);
                    for (int i = 0; i < times; i++) {
                        ctx.swapEquipmentRandomly();
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.warn("你的护甲决定换个位置站岗。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_cooldown_lock", "手上东西按不动了", TrollTier.ANNOYING)
                .desc("给手中物品挂上一段冷却，短时间内用不了")
                .weight(110)
                .cooldownSeconds(150)
                .when(ctx -> !ctx.invEmpty() && !ctx.held().isEmpty())
                .run(ctx -> {
                    var held = ctx.held();
                    if (!held.isEmpty()) {
                        ctx.setCooldown(held.getItem(), ctx.ri(60, 160));
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 0.8f);
                    ctx.actionBar("§c物品冷却中……再等等吧。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_exp_dice", "经验条抽风", TrollTier.ANNOYING)
                .desc("把玩家的经验等级随机改成一个小数字")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    int lv = ctx.ri(0, 30);
                    ctx.setExperience(lv);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.9f, 1.2f);
                    ctx.warn("你的经验条被重新排版了，现在它是 " + lv + " 级。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_hunger_zero_light", "饥饿清零（轻）", TrollTier.ANNOYING)
                .desc("把饥饿值直接归零，让你听着肚子叫")
                .weight(110)
                .cooldownSeconds(180)
                .when(ctx -> ctx.foodLevel() > 0)
                .run(ctx -> {
                    ctx.starve();
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.9f, 0.9f);
                    ctx.warn("你的胃刚刚提交了辞职信。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_toss_a_few", "手滑丢几件", TrollTier.ANNOYING)
                .desc("随机丢掉背包里两三件东西")
                .weight(110)
                .cooldownSeconds(150)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    int n = ctx.ri(2, 3);
                    for (int i = 0; i < n; i++) {
                        ctx.dropRandom();
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§7地上好像多了点东西，是你的吗？");
                })
                .build());

        // ==================================================================
        // BRUTAL —— 抓狂，会骂人
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("item_tool_scrap", "当场报废", TrollTier.BRUTAL)
                .desc("随机一件工具被削到只剩最后一滴耐久")
                .weight(60)
                .cooldownSeconds(240)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.breakRandomTool();
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.8f, 1.2f);
                    ctx.warn("§c咔。你有一件工具已经站在悬崖边上了。");
                    ctx.addRage(1.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_diamond_to_dirt", "钻石变泥土", TrollTier.BRUTAL)
                .desc("偷走一件物品，然后还你几块泥土")
                .weight(55)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.give(Items.DIRT, ctx.ri(6, 12));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.warn("§c恭喜！你的宝贝成功完成了向泥土的形态转变。");
                    ctx.addRage(1.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_sword_to_stick", "神剑变木棍", TrollTier.BRUTAL)
                .desc("偷走一件物品，换给你几根木棍")
                .weight(55)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.give(Items.STICK, ctx.ri(2, 5));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.warn("§c你的武器已经退化成了它的祖先。");
                    ctx.addRage(1.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_inventory_wiped", "背包被清空", TrollTier.BRUTAL)
                .desc("直接清空玩家的整个背包")
                .weight(55)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.clearInventory();
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 1.0f);
                    ctx.warn("§c你的背包刚刚学会了什么叫断舍离。");
                    ctx.msg("§8[系统] 已为你整理背包：整理得很彻底。");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_bait_and_snatch", "好东西当场抢走", TrollTier.BRUTAL)
                .desc("先塞给你一件好东西，再连着抽走两格")
                .weight(58)
                .cooldownSeconds(240)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), ctx.ri(1, 3));
                    ctx.flashTotem();
                    ctx.ok("检测到稀有物品已发放！");
                    ctx.takeRandomItem();
                    ctx.takeRandomItem();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.warn("§c发放完毕，回收完毕。别问，问就是系统错误。");
                    ctx.addRage(2.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_drop_everything", "双手一抖", TrollTier.BRUTAL)
                .desc("把背包里的东西全部丢到地上")
                .weight(45)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.dropEverything();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.warn("§c你身上所有的东西都决定去地上待一会儿。");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_exp_wiped", "经验清零", TrollTier.BRUTAL)
                .desc("把玩家的经验等级直接清零")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.setExperience(0);
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.warn("§c你的经验等级已被格式化，感谢你的配合。");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_hunger_saturation_wipe", "饱食度抹杀", TrollTier.BRUTAL)
                .desc("清空饥饿与饱食度，再额外加上疲劳")
                .weight(50)
                .cooldownSeconds(240)
                .when(ctx -> ctx.foodLevel() > 0)
                .run(ctx -> {
                    ctx.starve();
                    ctx.saturation(0.0f);
                    ctx.exhaust(ctx.ri(4, 10));
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.warn("§c你的饱食度被抹掉了，连回忆都不剩。");
                    ctx.addRage(2.0);
                })
                .build());

        // ==================================================================
        // NIGHTMARE —— 崩溃，会砸键盘
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("item_hotbar_lock", "快捷栏锁死", TrollTier.NIGHTMARE)
                .desc("锁死快捷栏并让槽位乱跳，手上永远不是想要的那把")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 200, 2);
                    ctx.glitch(GlitchIds.HOTBAR_JUMP, 200, 2);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.warn("§c你的快捷栏已经被焊死了，别挣扎了。");
                    ctx.addRage(2.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_cobble_flood", "背包变采石场", TrollTier.NIGHTMARE)
                .desc("用几十块圆石把你的背包灌满")
                .weight(40)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.fillWith(Items.COBBLESTONE, ctx.ri(24, 36));
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.warn("§c有人给你送了一整套圆石家具，请签收。");
                    ctx.msg("§8[系统] 你的背包现在很有分量。");
                    ctx.addRage(2.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_poison_potato_flood", "毒马铃薯大礼包", TrollTier.NIGHTMARE)
                .desc("用几十个毒马铃薯灌满背包")
                .weight(40)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.fillWith(Items.POISONOUS_POTATO, ctx.ri(24, 36));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.warn("§c恭喜获得毒马铃薯大礼包，附赠食物中毒风险。");
                    ctx.addRage(2.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_inventory_overflow", "塞到捡不下东西", TrollTier.NIGHTMARE)
                .desc("用垃圾把背包塞到一格不剩，之后捡什么都掉地上")
                .weight(35)
                .cooldownSeconds(420)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    var junk = ctx.pick(TrollPools.JUNK);
                    ctx.fillWith(junk, 36);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.warn("§c背包已满。接下来你捡到的任何东西都会直接掉在地上。");
                    ctx.warn("§8（这就是传说中的负重前行。）");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_diamond_dream", "钻石梦碎", TrollTier.NIGHTMARE)
                .desc("当众发给你一把钻石，然后当着你的面全部抽走")
                .weight(45)
                .cooldownSeconds(360)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.give(Items.DIAMOND, ctx.ri(4, 8));
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 30);
                    ctx.playLevelUp();
                    ctx.ok("§l幸运降临！发放钻石中……");
                    for (int i = 0; i < 4; i++) {
                        ctx.takeRandomItem();
                    }
                    ctx.title("§c§l梦该醒了", "§7钻石只是路过");
                    ctx.warn("§c发放程序出现了一点小小的意外。");
                    ctx.addRage(4.0);
                })
                .build());

        // ==================================================================
        // APOCALYPSE —— 地狱，会退游戏
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("item_apocalypse_purge", "背包大清洗", TrollTier.APOCALYPSE)
                .desc("全部丢光、清空背包、经验与饥饿一起归零，并附赠假存档损坏")
                .weight(22)
                .cooldownSeconds(600)
                .once()
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.dropEverything();
                    ctx.clearInventory();
                    ctx.setExperience(0);
                    ctx.starve();
                    ctx.saturation(0.0f);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 120, 2);
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                    ctx.broadcast("§4§l[整人大师] " + ctx.playerName() + " 刚刚体验了一次彻底的重置。");
                    ctx.warn("§4§l你的背包、经验、饱食度，全部清零。祝你重新做人。");
                    ctx.addRage(8.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_apocalypse_fake_gift", "假礼物", TrollTier.APOCALYPSE)
                .desc("先给你一大堆好东西并配上颁奖仪式，随后全部抢走")
                .weight(20)
                .cooldownSeconds(600)
                .once()
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.give(ctx.pick(TrollPools.LOOT), ctx.ri(1, 3));
                    }
                    ctx.title("§6§l恭喜获奖", "§e年度最佳受害者");
                    ctx.flashTotem();
                    ctx.playLevelUp();
                    ctx.ok("奖励发放中，请勿关闭游戏……");
                    for (int i = 0; i < 6; i++) {
                        ctx.takeRandomItem();
                    }
                    ctx.curseHeld();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                    ctx.warn("§4§l奖励回收完毕。感谢你为整人大师做出的贡献。");
                    ctx.msg("§8[系统] " + ctx.pick(TrollPools.TAUNTS));
                    ctx.addRage(8.0);
                })
                .build());
    }
}
