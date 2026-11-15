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
        // 序章 —— 背包里的小动作：多出来的、被改名的
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("item_potato_delivery", "送你一个土豆", TrollTier.BRUTAL)
                .desc("往背包里塞一堆没用的东西，还配上一声饱嗝")
                .weight(170)
                .cooldownSeconds(40)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.giveRandomJunk();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.msg("§7有人往你背包里塞了点东西……别问是谁。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_pocket_eggs", "兜里多了鸡蛋", TrollTier.ANNOYING)
                .desc("凭空多出几颗鸡蛋，摔了算你的")
                .weight(160)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.give(Items.EGG, ctx.ri(1, 3));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.4f);
                    ctx.actionBar("§e你感觉口袋里有点沉。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_rename_petty", "名字被改了", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("item_durability_bite", "耐久被啃了一口", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("item_slot_hijack", "槽位被夺舍", TrollTier.ANNOYING)
                .desc("把玩家的选中快捷栏槽位随机挪走")
                .weight(140)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.randomSelectSlot();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 1.2f);
                    ctx.actionBar("§e你手里的东西自己换了一件。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_fake_pickpocket", "好像被摸了", TrollTier.ANNOYING)
                .desc("纯造谣：假装有小偷路过，其实什么都没拿")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.9f, 1.0f);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "刚路过，顺手拿了你点东西，不谢。");
                    ctx.msg("§7你下意识打开背包检查了一遍……");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_stack_tease", "堆叠被拆散了", TrollTier.ANNOYING)
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
        // 加压 —— 一件件离开你：偷、丢、冷却、耗损
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("item_hotbar_jumble", "顺序被抽走", TrollTier.NIGHTMARE)
                .desc("快捷栏被彻底打乱，脚下同时变成岩浆——你连换武器都要先重新认识自己的手")
                .weight(150)
                .cooldownSeconds(90)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.scrambleHotbar();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.warn("你的快捷栏刚刚被洗了一遍，习惯的位置全没了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_silent_pickpocket", "神不知鬼不觉", TrollTier.BRUTAL)
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

        TrollEvents.reg(TrollEvent.of("item_butterfingers", "手不再属于你", TrollTier.NIGHTMARE)
                .desc("手中的东西被夺走，你被扔到一百二十格外——连同它一起")
                .weight(130)
                .cooldownSeconds(120)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.dropHeld();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§7啪嗒。你的手突然不属于你了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_backpack_shuffle", "内部崩塌", TrollTier.NIGHTMARE)
                .desc("背包被翻了个底朝天，凋零从里面渗出来——你连自己的东西都认不出了")
                .weight(120)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.shuffleInventory();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.warn("你的背包刚刚翻了个身，现在它自己都找不到东西了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_curse_vanishing", "它不想留下", TrollTier.NIGHTMARE)
                .desc("手上物品被刻进消失诅咒，脚下岩浆翻涌——它会比你先离开这个世界")
                .weight(110)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.curseHeld();
                    ctx.sound(ctx.pick(TrollPools.HORROR), 0.8f, 1.2f);
                    ctx.warn("你手里的东西获得了一个它并不想要的附魔。");
                    ctx.msg("§8提示：死亡时它会跟着你一起消失。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_junk_rain", "只有垃圾会留下", TrollTier.NIGHTMARE)
                .desc("一批又一批垃圾灌进背包，脚下岩浆封住退路——这就是你全部的补给")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
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

        TrollEvents.reg(TrollEvent.of("item_armor_shuffle", "装备穿错了", TrollTier.BRUTAL)
                .desc("随机交换玩家身上的两件装备，来回折腾几次")
                .weight(100)
                .cooldownSeconds(120)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    int times = ctx.ri(2, 5);
                    for (int i = 0; i < times; i++) {
                        ctx.swapEquipmentRandomly();
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.warn("你的护甲决定换个位置站岗。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_cooldown_lock", "手被焊死", TrollTier.NIGHTMARE)
                .desc("手持物被挂上长冷却，凋零同时开始计时——你只能看着它一点点变钝")
                .weight(110)
                .cooldownSeconds(150)
                .when(ctx -> !ctx.invEmpty() && !ctx.held().isEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    var held = ctx.held();
                    if (!held.isEmpty()) {
                        ctx.setCooldown(held.getItem(), ctx.ri(60, 160));
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 0.8f);
                    ctx.actionBar("§c物品冷却中……再等等吧。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_exp_dice", "等级被随机重写", TrollTier.NIGHTMARE)
                .desc("饥饿清零，等级被重排成一个毫无意义的小数字——你过去的努力被当成了骰子")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
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

        TrollEvents.reg(TrollEvent.of("item_toss_a_few", "手滑丢几件", TrollTier.BRUTAL)
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
        // 清空 —— 值得心疼的东西开始被点名
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("item_tool_scrap", "最后一滴耐久", TrollTier.NIGHTMARE)
                .desc("随机一件工具被削到只剩一丝耐久，凋零先一步啃上你——它下次一定会断")
                .weight(60)
                .cooldownSeconds(240)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.breakRandomTool();
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.8f, 1.2f);
                    ctx.warn("§c咔。你有一件工具已经站在悬崖边上了。");
                    ctx.addRage(1.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_diamond_to_dirt", "形态转变", TrollTier.APOCALYPSE)
                .desc("先来一场陨石雨，再把你的宝贝换成泥土——你甚至来不及记住它原来是什么")
                .weight(55)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.takeRandomItem();
                    ctx.give(Items.DIRT, ctx.ri(6, 12));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.warn("§c恭喜！你的宝贝成功完成了向泥土的形态转变。");
                    ctx.addRage(1.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_sword_to_stick", "退化", TrollTier.APOCALYPSE)
                .desc("武器被换成木棍，等级清零——你已经退回了刚进服的那一天")
                .weight(55)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.takeRandomItem();
                    ctx.give(Items.STICK, ctx.ri(2, 5));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.warn("§c你的武器已经退化成了它的祖先。");
                    ctx.addRage(1.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_inventory_wiped", "断舍离", TrollTier.NIGHTMARE)
                .desc("一件工具先被折断，然后整个背包被清空——系统把这一步称为『整理』")
                .weight(55)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.clearInventory();
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 1.0f);
                    ctx.warn("§c你的背包刚刚学会了什么叫断舍离。");
                    ctx.msg("§8[系统] 已为你整理背包：整理得很彻底。");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_bait_and_snatch", "发放与回收", TrollTier.NIGHTMARE)
                .desc("先发给你稀有物品并打上光效，再连抽两格——那个提示音是它唯一的真心")
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

        TrollEvents.reg(TrollEvent.of("item_drop_everything", "地面上见", TrollTier.APOCALYPSE)
                .desc("陨石雨落下，所有东西同时离开你——你只能听着它们一颗颗砸在地上")
                .weight(45)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.dropEverything();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.warn("§c你身上所有的东西都决定去地上待一会儿。");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_exp_wiped", "格式化", TrollTier.APOCALYPSE)
                .desc("等级被直接格式化：没有提示，没有备份，也没有理由")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.setExperience(0);
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.warn("§c你的经验等级已被格式化，感谢你的配合。");
                    ctx.addRage(3.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_hunger_saturation_wipe", "胃里什么都没有", TrollTier.NIGHTMARE)
                .desc("饥饿、饱食与体力一起被抹平——连回忆里那顿饭都不算数")
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
        // 剥夺 —— 连身份一起拿走：装备、等级、饱食
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("item_hotbar_lock", "焊死的手", TrollTier.APOCALYPSE)
                .desc("快捷栏被锁死并持续跳动，等级清零——你永远拿不到自己想要的那把")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 200, 2);
                    ctx.glitch(GlitchIds.HOTBAR_JUMP, 200, 2);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.warn("§c你的快捷栏已经被焊死了，别挣扎了。");
                    ctx.addRage(2.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_cobble_flood", "负重填埋", TrollTier.OBLIVION)
                .desc("先把你砌进石堆，再用圆石填满每一格背包。你活着，但每一步都像在拖着自己的墓碑。")
                .weight(40)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.ok("§a[系统] 检测到背包空缺，正在为你补充物资……");
                    ctx.later(60, () -> {
                        ctx.msg("§7物资到了。");
                        ctx.meteorShower(4, 16.0);
                        ctx.buryAlive();
                        ctx.effectRandom(1200, 3);
                        ctx.voidPit(2, 16);
                        ctx.fillWith(Items.COBBLESTONE, ctx.ri(24, 36));
                        ctx.takeRandomItems(3);
                        ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                        ctx.warn("§4补充完毕：圆石管够。§7你原来的东西，就当是保管费。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_poison_potato_flood", "唯一的配给", TrollTier.APOCALYPSE)
                .desc("背包被毒马铃薯灌满，负面效果随之而来——这就是你接下来所有的食物")
                .weight(40)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.fillWith(Items.POISONOUS_POTATO, ctx.ri(24, 36));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.warn("§c恭喜获得毒马铃薯大礼包，附赠食物中毒风险。");
                    ctx.addRage(2.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_inventory_overflow", "再也捡不起东西", TrollTier.OBLIVION)
                .desc("先夺走你的一切，再用垃圾把背包塞到一格不剩——从这一刻起，你捡到的任何东西都会掉在地上。")
                .weight(35)
                .cooldownSeconds(420)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.later(40, () -> {
                        ctx.voidPit(2, 16);
                        ctx.stripAll();
                        ctx.obliterate();
                        ctx.meteorShower(4, 16.0);
                        ctx.fillWith(ctx.pick(TrollPools.JUNK), 36);
                        ctx.warn("§4背包已满。§7从现在起，你捡到的每一样东西都不再属于你。");
                        ctx.msg("§8（负重前行的人，最后连路都带不走。）");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("item_diamond_dream", "路过", TrollTier.APOCALYPSE)
                .desc("当众给你钻石、放升级音效，再当着你的面全部抽走——你甚至来不及合上背包")
                .weight(45)
                .cooldownSeconds(360)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
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
        // 重置 —— 从零开始做人，附赠假存档损坏
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("item_apocalypse_purge", "重置", TrollTier.APOCALYPSE)
                .desc("背包、经验、饱食全部归零，假存档损坏警告随后弹出——祝你重新做人")
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

        TrollEvents.reg(TrollEvent.of("item_apocalypse_fake_gift", "颁奖典礼", TrollTier.OBLIVION)
                .desc("给你一场颁奖典礼：五件稀有物品、升调音效、一顶桂冠。掌声停下时它们会一起消失。整局一次，死亡不召回。")
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
                    ctx.later(80, () -> {
                        ctx.msg("§7颁奖嘉宾鼓完掌了。");
                        ctx.takeRandomItems(6);
                        ctx.curseHeld();
                        ctx.buryAlive();
                        ctx.voidPit(2, 16);
                        ctx.effectRandom(1200, 3);
                        ctx.meteorShower(4, 16.0);
                        ctx.obliterate();
                        ctx.warn("§4§l奖励回收完毕。感谢你为整人大师做出的贡献。");
                        ctx.msg("§8[系统] " + ctx.pick(TrollPools.TAUNTS));
                    });
                })
                .build());
    }
}
