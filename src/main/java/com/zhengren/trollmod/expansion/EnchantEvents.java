package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【附魔与装备】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。</p>
 *
 * <p>与原生事件文件一致：<b>刻意不引用任何 Minecraft 类型</b>，全部通过 {@code ctx.*}
 * 与 {@code TrollPools.*} 组合实现，因此不会因为 MC API 版本变动而编译失败。</p>
 *
 * <p>主题范围：附魔、装备、盔甲、武器属性、耐久、诅咒、下界合金、铁砧、锻造台、
 * 附魔台、经验修补、属性修饰符。</p>
 */
public final class EnchantEvents {

    private EnchantEvents() {
    }

    public static void register() {
        TrollEvents.category("附魔与装备");

        // ================================================================
        // PRANK —— 小整：让你的装备开口说话，笑一下就好
        // ================================================================

        TrollEvents.reg(TrollEvent.of("ench_sword_insult", "剑的嫌弃", TrollTier.PRANK)
                .desc("你手里的武器突然开始评价你的操作水平")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.msg("§7你的剑开口了：§8「刚才那下，是认真的吗？」");
                    ctx.actionBar("§8武器满意度：§c★☆☆☆☆");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.addRage(1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_tool_applause", "工具鼓掌", TrollTier.PRANK)
                .desc("整背包的工具同时为你鼓掌，声音整齐得有点吓人")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.7f, 1.8f);
                    ctx.particle(TrollPools.TAUNT_PARTICLES[0], 12);
                    ctx.msg("§7背包深处传来一阵§e掌声§7，你的工具们起立了。");
                    ctx.actionBar("§7工具们在鼓掌，你最好也笑一下。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_armor_flattery", "盔甲的恭维", TrollTier.PRANK)
                .desc("身上的盔甲把你夸了一遍，字字都像反话")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.msg("§b头盔§7：这件装备，配你，实在是……§o太有勇气了§r§7。");
                    ctx.msg("§b胸甲§7：我们尽力了。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.1f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_helmet_critique", "头盔点评头围", TrollTier.PRANK)
                .desc("头盔表示尺寸勉强，并附赠一句专业建议")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.actionBar("§7头盔：§8内衬有点挤，建议换个小点的脑袋。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.addRage(1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_bow_string_grumble", "弓弦的抱怨", TrollTier.PRANK)
                .desc("弓弦抱怨拉力太小，要求你加大力度")
                .weight(110)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.msg("§7弓弦：§8「再拉这么点，我就自己弹回去了。」");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_pickaxe_philosophy", "镐子讲哲学", TrollTier.PRANK)
                .desc("你的镐子开始思考石头与存在的意义")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.msg("§7镐子：§8「我挖的每一块石头，最后都变成了别人家的墙。」");
                    ctx.msg("§7镐子：§8「那我到底在挖什么？」");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_shield_nap", "盾牌打了个盹", TrollTier.PRANK)
                .desc("盾牌发出轻微的鼾声，提醒你它昨晚没睡好")
                .weight(100)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.actionBar("§7盾牌：§8zzz… 有事明天再挡。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.6f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_boots_squeak", "靴子吱吱叫", TrollTier.PRANK)
                .desc("每走一步都自带廉价音效，且无法关掉")
                .weight(120)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.9f);
                    ctx.msg("§7你的靴子：§e吱——§7。");
                    ctx.msg("§8（它还会继续响，你忍一下。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_anvil_distant_ring", "远方的铁砧", TrollTier.PRANK)
                .desc("不知从哪传来一声铁砧落地，只有你听见")
                .weight(110)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.sys("检测到附近铁砧……状态：§c已落地§7。");
                    ctx.msg("§8你环顾四周，什么也没有。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_book_poem_title", "附魔书念诗", TrollTier.PRANK)
                .desc("附魔光效在书页上拼出一首莫名其妙的诗")
                .weight(100)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.msg("§d书页自己翻开了：§7「经验如水，等级如火，手一抖，全没了。」");
                    ctx.particleAtPlayer(TrollPools.SPOOKY_PARTICLES[0], 15);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 1.3f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_xp_bottle_leak", "经验瓶漏气", TrollTier.PRANK)
                .desc("背包里飘出一缕绿色，像极了经验在偷偷溜走")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.particleAtPlayer(TrollPools.BOOM_PARTICLES[0], 18);
                    ctx.msg("§a一缕经验从背包缝里钻出来，飘走了。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.7f, 1.7f);
                    ctx.addRage(1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_grindstone_regret", "砂轮的悔意", TrollTier.PRANK)
                .desc("砂轮表示上次磨掉的那个附魔，它到现在还内疚")
                .weight(90)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.msg("§7砂轮：§8「那个附魔……我其实可以留一点的。」");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.6f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_elytra_flutter", "鞘翅抖翅", TrollTier.PRANK)
                .desc("背上的鞘翅无风自动，抖了两下又安静了")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.2f);
                    ctx.actionBar("§7鞘翅：抖一下，表示还活着。");
                    ctx.particleAtPlayer(TrollPools.SPOOKY_PARTICLES[0], 10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_trident_hiccup", "三叉戟打嗝", TrollTier.PRANK)
                .desc("手里的三叉戟打了个嗝，附带一道小闪电特效")
                .weight(90)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.8f, 1.5f);
                    ctx.particleAtPlayer(TrollPools.BOOM_PARTICLES[0], 8);
                    ctx.msg("§b三叉戟：§7嗝。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_hoe_identity_crisis", "锄头的身份危机", TrollTier.PRANK)
                .desc("锄头坚称自己是剑，并要求你尊重它的选择")
                .weight(90)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.msg("§7锄头：§8「我也是有刃的，凭什么不能是剑？」");
                    ctx.msg("§7锄头：§8「我要改名叫锄剑。」");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_armor_stand_parade", "盔甲架列队围观", TrollTier.PRANK)
                .desc("周围冒出几个盔甲架，整齐地朝向你站好")
                .weight(90)
                .cooldownSeconds(160)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.giveMobCustomName(
                                ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                        ctx.x() + ctx.ri(-3, 3),
                                        ctx.y(),
                                        ctx.z() + ctx.ri(-3, 3)),
                                "§7围观群众");
                    }
                    ctx.msg("§7它们不说话，只是看着你换装备。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_lore_rewrite", "介绍被改写", TrollTier.PRANK)
                .desc("手上物品的介绍文字被悄悄改成了别人的评价")
                .weight(100)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.renameHeld("§7「此物已由第三方重新鉴定」");
                    ctx.msg("§7你手上那件东西的介绍被改了，你还没来得及阻止。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_repair_tax_notice", "修补收税通知", TrollTier.PRANK)
                .desc("伪造一份公告，宣布经验修补开始收取手续费")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e[公告] 即日起，经验修补将收取 5% 手续费，谢谢配合。");
                    ctx.msg("§7你摸了摸经验条，它好像真的少了一点点。");
                    ctx.addExperience(-2);
                })
                .build());

        // ================================================================
        // ANNOYING —— 烦人：不痛，但烦到你想摔鼠标
        // ================================================================

        TrollEvents.reg(TrollEvent.of("ench_durability_anxiety", "耐久焦虑", TrollTier.ANNOYING)
                .desc("随机一件工具的耐久被悄悄磨掉一截")
                .weight(140)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.damageRandomTool(25);
                    ctx.warn("你的某件工具发出一声不祥的§e咯吱§c声。");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.1f);
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_unbreaking_betrayal", "耐久III的背叛", TrollTier.ANNOYING)
                .desc("号称永不磨损的附魔今天决定休息")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.damageRandomTool(40);
                    ctx.msg("§7耐久III：§8「今天不加班。」");
                    ctx.msg("§8（它确实没加班，但你的工具加班了。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_vanishing_hint", "消失诅咒的温馨提示", TrollTier.ANNOYING)
                .desc("背包里飘来一句关于消失诅咒的友好提醒")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.msg("§c§l[提示]§r §7检测到你身上有东西§4随时可能消失§7。");
                    ctx.msg("§7具体是哪一件？§8保密。");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.7f, 1.4f);
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_binding_tighten", "绑定诅咒收紧", TrollTier.ANNOYING)
                .desc("护甲突然收紧一圈，让你当场体验什么叫束手束脚")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.msg("§7你的护甲深吸一口气，然后收紧了。");
                    ctx.actionBar("§8绑定感 +1（并没有真的 +1）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_armor_shrink_one", "盔甲缩水一号", TrollTier.ANNOYING)
                .desc("身上的装备被随机调换了一件，尺码明显不对")
                .weight(120)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.swapEquipmentRandomly();
                    ctx.msg("§7你身上某件装备默默换成了别的，尺码还挺尴尬。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_helmet_askew", "头盔歪了", TrollTier.ANNOYING)
                .desc("头盔自己歪向一边，视野跟着一起歪")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.spin(ctx.rf(-25f, 25f));
                    ctx.actionBar("§7头盔：歪一点点，更有型。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.7f, 1.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_boots_swapped", "左右脚穿反了", TrollTier.ANNOYING)
                .desc("快捷栏被打乱，你开始怀疑自己到底穿没穿靴子")
                .weight(120)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.msg("§7你的靴子：§8「我们换了个位置，感受一下。」");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_sword_suddenly_heavy", "剑突然变沉", TrollTier.ANNOYING)
                .desc("手里的剑重量暴涨，挥一下都要下决心")
                .weight(130)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 180, 0);
                    ctx.msg("§7剑：§8「我今天只想被拖着走。」");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.8f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_shield_lock", "盾牌进入冷却", TrollTier.ANNOYING)
                .desc("盾牌被塞了一个长得离谱的使用冷却")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.setCooldown(ctx.pick(TrollPools.JUNK), 200);
                    ctx.msg("§7盾牌：§8「冷却中，请稍后再被砍。」");
                    ctx.actionBar("§c盾牌冷却：有点久");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_anvil_fee", "铁砧乱收费", TrollTier.ANNOYING)
                .desc("铁砧按次收费，这次直接伸手到你背包里拿")
                .weight(120)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§7铁砧：§8「手续费，一件，不退。」");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_table_ransom", "附魔台坐地起价", TrollTier.ANNOYING)
                .desc("附魔台宣布涨价，并附赠一份安慰奖")
                .weight(120)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.msg("§5附魔台：§7「涨价了。这个，算找零。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_bookshelf_shuffle", "书架集体换位", TrollTier.ANNOYING)
                .desc("附魔台周围的智慧被重新排列，背包也一起遭殃")
                .weight(110)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.shuffleInventory();
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.0f);
                    ctx.msg("§7书架们换了位置，顺便把你的背包也重新排了排。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_lapis_tax", "青金石使用税", TrollTier.ANNOYING)
                .desc("每次附魔都要交税，这次直接从你身上扣")
                .weight(110)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.sys("青金石库存：§c-1§7（税）。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_xp_pickpocket", "经验小偷", TrollTier.ANNOYING)
                .desc("经验条被人顺手抹掉一小段")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.addExperience(-7);
                    ctx.particleAtPlayer(TrollPools.SPOOKY_PARTICLES[0], 14);
                    ctx.msg("§7你感觉经验条轻了一点点。§8（不止一点点。）");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_mending_misroute", "经验修补发错货", TrollTier.ANNOYING)
                .desc("本该修装备的经验跑去磨了另一件工具")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.damageRandomTool(30);
                    ctx.msg("§a经验修补§7：货已送达，收件人……§8不是你手上那件§7。");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.7f, 1.4f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_template_swapped", "锻造模板被掉包", TrollTier.ANNOYING)
                .desc("你手里的模板好像换了材质，手感不太对")
                .weight(100)
                .cooldownSeconds(170)
                .run(ctx -> {
                    ctx.renameHeld("§8「仿制锻造模板·限量版」");
                    ctx.msg("§7锻造模板的纹路好像……§8糊了一点§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_netherite_plated", "下界合金只是镀的", TrollTier.ANNOYING)
                .desc("装备被悄悄改名为镀层版，心理伤害拉满")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.renameHeld("§7镀金下界合金锭（非正品）");
                    ctx.msg("§8你盯着它看了三秒，越看越像铜。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.1f);
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_diamond_glass_feel", "钻石手感像玻璃", TrollTier.ANNOYING)
                .desc("钻石装备敲起来叮当响，你开始担心它下一秒就碎")
                .weight(110)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 2.0f);
                    ctx.msg("§b叮——§7这声儿，怎么听怎么像玻璃。");
                    ctx.damageRandomTool(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_armor_creak", "盔甲吱呀作响", TrollTier.ANNOYING)
                .desc("每走一步，盔甲都在提醒你它年久失修")
                .weight(120)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 0.8f);
                    ctx.actionBar("§7你的盔甲：§8吱呀……吱呀……");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_hotbar_scroll", "快捷栏自己滚", TrollTier.ANNOYING)
                .desc("快捷栏选中格疯狂跳动，你的手完全跟不上")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.randomSelectSlot();
                    ctx.actionBar("§7快捷栏：我自己来。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.4f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_tool_sticky", "工具有点黏", TrollTier.ANNOYING)
                .desc("手里的工具像是被胶水粘住，换都换不掉")
                .weight(110)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.randomSelectSlot();
                    ctx.randomSelectSlot();
                    ctx.msg("§7你换了两次手，它又自己回来了。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_glint_without_stats", "光效还在，属性走了", TrollTier.ANNOYING)
                .desc("装备附魔光效依旧闪亮，实际数值悄悄离场")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 140, 0);
                    ctx.msg("§d附魔光效：§7在的。§8属性：§7先走了。");
                    ctx.particleAtPlayer(TrollPools.SPOOKY_PARTICLES[0], 20);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_attribute_trim", "属性被修剪", TrollTier.ANNOYING)
                .desc("属性修饰符被精修了一番，方向是往下")
                .weight(100)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.msg("§7属性修饰符：§8「小小的优化。」");
                    ctx.actionBar("§8属性：已优化（向下）");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_speed_boots_lag", "迅捷之靴慢半拍", TrollTier.ANNOYING)
                .desc("号称加速的靴子今天决定体验一下生活")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 220, 1);
                    ctx.msg("§b迅捷之靴：§7「急什么，风景挺好的。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_feather_falling_irony", "摔落保护的黑色幽默", TrollTier.ANNOYING)
                .desc("摔落保护IV在关键时刻选择沉默")
                .weight(110)
                .cooldownSeconds(110)
                .when(ctx -> !ctx.isOnGround())
                .run(ctx -> {
                    ctx.damage("fall", 1.0f);
                    ctx.msg("§7摔落保护IV：§8「我以为你能行。」");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.3f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_respiration_wheeze", "水下呼吸先喘三秒", TrollTier.ANNOYING)
                .desc("水下呼吸附魔开场先让你憋一会儿")
                .weight(100)
                .cooldownSeconds(130)
                .when(ctx -> ctx.isInWater())
                .run(ctx -> {
                    ctx.setAir(60);
                    ctx.msg("§3水下呼吸：§7「我先做个热身。」");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_aqua_affinity_fear", "水下速掘怕水", TrollTier.ANNOYING)
                .desc("水下速掘附魔表示自己其实有点恐水")
                .weight(90)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 150, 0);
                    ctx.msg("§3水下速掘：§7「水……水太多了。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_thorns_selfpoke", "荆棘扎自己", TrollTier.ANNOYING)
                .desc("荆棘附魔找错了对象，先扎了穿戴者一下")
                .weight(110)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.damage("cactus", 1.5f);
                    ctx.msg("§a荆棘：§7「谁？哦，是你。」");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_fire_aspect_backfire", "火焰附加回火", TrollTier.ANNOYING)
                .desc("火焰附加先点着了持有者的袖子")
                .weight(110)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.setFire(2);
                    ctx.msg("§6火焰附加：§7「方向反了，抱歉。」");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_knockback_selfpush", "击退把自己推走", TrollTier.ANNOYING)
                .desc("击退附魔的后坐力全部作用在你身上")
                .weight(110)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.fling(ctx.rd(-1.2, 1.2), 0.5, ctx.rd(-1.2, 1.2));
                    ctx.msg("§7击退：§8「力是相互的，这个你学过吧。」");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 1.1f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_looting_cut", "抢夺要抽成", TrollTier.ANNOYING)
                .desc("抢夺附魔表示战利品它也有份")
                .weight(110)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.msg("§c抢夺III：§7「三成，谢谢。」");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_fortune_misfortune", "时运倒挂", TrollTier.ANNOYING)
                .desc("时运附魔今天运气不太好，掉的东西也跟着不太对")
                .weight(110)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), 2);
                    ctx.msg("§e时运III：§7「翻倍了，方向不太好说。」");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.3f);
                })
                .build());

        // ================================================================
        // BRUTAL —— 抓狂：真金白银的损失
        // ================================================================

        TrollEvents.reg(TrollEvent.of("ench_durability_zero", "耐久当场归零", TrollTier.BRUTAL)
                .desc("随机一件工具的耐久被直接抽干")
                .weight(120)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.breakRandomTool();
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.warn("你听见一声清脆的§c碎裂§c。至于是哪件——自己看。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_tool_snap", "毫无预兆地碎了", TrollTier.BRUTAL)
                .desc("手上的东西连个招呼都不打，说没就没")
                .weight(120)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.dropHeld();
                    ctx.damageRandomTool(80);
                    ctx.msg("§c咔嚓。§7你手里空了。");
                    ctx.particleAtPlayer(TrollPools.BOOM_PARTICLES[0], 22);
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_armor_melt", "盔甲在融化", TrollTier.BRUTAL)
                .desc("护甲表面开始冒烟，并顺手掉了一点耐久下限")
                .weight(110)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.smokeBurst();
                    ctx.setFire(3);
                    ctx.damageRandomTool(60);
                    ctx.warn("§6你的护甲在冒烟，它说这不是它的错。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_helmet_gone", "头上的东西没了", TrollTier.BRUTAL)
                .desc("头盔凭空消失，只留下一声轻轻的『谢谢惠顾』")
                .weight(110)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                    ctx.msg("§7空气里飘来一句：§8「谢谢惠顾。」");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_boots_walk_off", "靴子离家出走", TrollTier.BRUTAL)
                .desc("你的靴子决定独自去探索世界")
                .weight(100)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.dropRandom();
                    ctx.dropRandom();
                    ctx.msg("§7你的靴子（也许还有别的）朝远处走了。");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_sword_dull", "剑刃卷了", TrollTier.BRUTAL)
                .desc("武器伤害肉眼可见地下滑，还附赠一声嘲笑")
                .weight(120)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.damageRandomTool(70);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.msg("§7剑：§8「卷了，凑合用吧。」");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_bow_string_snap", "弓弦崩了", TrollTier.BRUTAL)
                .desc("弓弦当场断裂，你还保持着拉弓的姿势")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.breakRandomTool();
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.4f);
                    ctx.msg("§c啪。§7弦断得干脆利落。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_shield_crack", "盾牌裂了", TrollTier.BRUTAL)
                .desc("格挡瞬间盾牌出现裂纹，并霸占了你的冷却条")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.damageRandomTool(55);
                    ctx.setCooldown(ctx.pick(TrollPools.JUNK), 260);
                    ctx.msg("§7盾牌：§8「我裂开了。」§7（字面意思）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_elytra_tear", "鞘翅破了个洞", TrollTier.BRUTAL)
                .desc("飞行途中鞘翅漏风，高度掉得比心情还快")
                .weight(100)
                .cooldownSeconds(220)
                .when(ctx -> !ctx.isOnGround())
                .run(ctx -> {
                    ctx.damageRandomTool(65);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 1);
                    ctx.msg("§7鞘翅：§8「嘶——」");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_anvil_from_sky", "铁砧从天而降", TrollTier.BRUTAL)
                .desc("一块铁砧精确制导，落在你头顶上方三格")
                .weight(120)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.spawnFallingBlock(ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.soundAt(ctx.pick(TrollPools.IMPACT), ctx.x(), ctx.y() + 12, ctx.z(), 1.2f, 0.6f);
                    ctx.msg("§8头顶传来了越来越近的风声。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_table_xp_burn", "附魔台吞了你的等级", TrollTier.BRUTAL)
                .desc("附魔台一次性吃掉一大截经验，只回赠一点青金石味的安慰")
                .weight(120)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.addExperience(-18);
                    ctx.msg("§5附魔台：§7「等级我收下了，这个给你。」");
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.playHurtSound();
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_curse_spread", "诅咒传染", TrollTier.BRUTAL)
                .desc("一件装备的诅咒蔓延到了另一件身上")
                .weight(100)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.curseHeld();
                    ctx.curseHeld();
                    ctx.msg("§5诅咒：§7「一件不够，我全都要。」");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 1.1f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_netherite_downgrade", "下界合金降级", TrollTier.BRUTAL)
                .desc("你的下界合金装备被现场重铸成了更廉价的版本")
                .weight(100)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.renameHeld("§7下界合金锭（纯度可疑）");
                    ctx.damageRandomTool(75);
                    ctx.msg("§8锻造台：§7「退回原厂设置。」");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_attribute_inverted", "属性全部取反", TrollTier.BRUTAL)
                .desc("属性修饰符被集体反向挂载，你成了自己的反面教材")
                .weight(100)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 0);
                    ctx.msg("§7属性面板：§c已取反§7。反向也是方向。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_armor_weight", "盔甲突然千斤重", TrollTier.BRUTAL)
                .desc("整套护甲同时申请加薪，代价是你的腿")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.freeze(60);
                    ctx.exhaust(3.0f);
                    ctx.msg("§7整套盔甲：§8「我们今天很重。」");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_weapon_swap_owner", "武器换了主人", TrollTier.BRUTAL)
                .desc("你的主手武器被随机塞进了背包深处，位置保密")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.shuffleInventory();
                    ctx.msg("§7你的武器：§8「换个位置，冷静一下。」");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_inventory_reforge", "背包被重铸", TrollTier.BRUTAL)
                .desc("锻造台顺手把你的背包也重铸了一遍，顺序全乱")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.shuffleInventory();
                    ctx.give(ctx.pick(TrollPools.JUNK), 3);
                    ctx.msg("§8锻造台：§7「余料，给你留着。」");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_hotbar_reforge", "快捷栏被重铸", TrollTier.BRUTAL)
                .desc("快捷栏顺序被彻底打乱，战斗准备时间 +10 秒")
                .weight(110)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.selectSlot(ctx.ri(0, 8));
                    ctx.warn("快捷栏：§c已重排§c。祝你在战斗中慢慢找。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_xp_zeroed", "等级清零", TrollTier.BRUTAL)
                .desc("经验条被一只手轻轻抹平，干净得像刚进服")
                .weight(110)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.setExperience(0);
                    ctx.playHurtSound();
                    ctx.msg("§7经验条：§8「重新开始，也挺好的。」");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_mending_clawback", "经验修补收回本金", TrollTier.BRUTAL)
                .desc("修补附魔表示之前的服务要连本带利收回去")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.addExperience(-14);
                    ctx.damageRandomTool(70);
                    ctx.msg("§a经验修补：§7「连本带利，谢谢惠顾。」");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_binding_permanent", "绑死在手上", TrollTier.BRUTAL)
                .desc("绑定诅咒发力，你连换手都做不到")
                .weight(100)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.curseHeld();
                    ctx.freeze(80);
                    ctx.msg("§5绑定诅咒：§7「跑啊。」");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_armor_pins", "盔甲里进了针", TrollTier.BRUTAL)
                .desc("穿戴护甲反而持续掉血，荆棘刺向了里面")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.damage("cactus", 1.0f);
                    }
                    ctx.msg("§c你感觉护甲内衬里全是细小的刺痛。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_lava_quench", "岩浆淬火", TrollTier.BRUTAL)
                .desc("有人坚持要用岩浆给你的装备淬火，就在你脚下")
                .weight(90)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.warn("§c淬火完成度：1%§c。装备存活率：§c未知§c。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_forge_blast", "锻造台炸了", TrollTier.BRUTAL)
                .desc("锻造台在一次尝试中决定自我了断，顺带把你送上天")
                .weight(90)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.boomAtPlayer(2.2f);
                    ctx.explosionFx();
                    ctx.msg("§8锻造台：§7「这次真的修不好了。」");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_grindstone_strip", "砂轮把附魔磨掉了", TrollTier.BRUTAL)
                .desc("砂轮自动启动，替你清理了一件装备的附魔")
                .weight(100)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.8f);
                    ctx.warn("砂轮：§7「不客气，我看它挺多余的。」");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_netherite_shards", "合金成了碎片", TrollTier.BRUTAL)
                .desc("背包里的下界合金相关物被锻造成了碎片")
                .weight(90)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.give(ctx.pick(TrollPools.JUNK), 4);
                    ctx.msg("§8「合金没了，碎片管够。」");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_thorn_storm", "荆棘风暴", TrollTier.BRUTAL)
                .desc("荆棘附魔一次结算了全年份的反伤，全部记在你头上")
                .weight(100)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.damage("cactus", 3.0f);
                    ctx.particleAtPlayer(TrollPools.BOOM_PARTICLES[0], 25);
                    ctx.msg("§a荆棘：§7「年度总结，一次性付清。」");
                    ctx.addRage(7);
                })
                .build());

        // ================================================================
        // NIGHTMARE —— 崩溃：恐怖体验 + 大量损失
        // ================================================================

        TrollEvents.reg(TrollEvent.of("ench_armor_resign", "全套护甲集体辞职", TrollTier.NIGHTMARE)
                .desc("所有护甲同一时间离开岗位，装备栏瞬间清爽")
                .weight(90)
                .cooldownSeconds(400)
                .once()
                .run(ctx -> {
                    ctx.clearInventory();
                    ctx.playHurtSound();
                    ctx.msg("§4整套护甲递上了辞呈，理由：§8「与使用者理念不合」§4。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_gear_stripped", "装备被扒光", TrollTier.NIGHTMARE)
                .desc("背包被清空，只在地板上留了一件破烂当纪念")
                .weight(80)
                .cooldownSeconds(450)
                .once()
                .run(ctx -> {
                    ctx.clearInventory();
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.msg("§4你的装备被收走了。地上那件，是纪念品。");
                    ctx.randomHorrorSound();
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_weapon_possessed", "武器被附身", TrollTier.NIGHTMARE)
                .desc("手里的武器自己动了起来，方向朝内")
                .weight(90)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.spin(180f);
                    ctx.damage("magic", 4.0f);
                    ctx.glitch(GlitchIds.SHAKY, 60);
                    ctx.msg("§4你的武器转了个身，朝你笑了一下。");
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_curse_of_doom", "末日诅咒", TrollTier.NIGHTMARE)
                .desc("一件装备被挂上最恶毒的诅咒，并开始计时")
                .weight(80)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.curseHeld();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 2);
                    ctx.glitch(GlitchIds.BLACKOUT, 50);
                    ctx.warn("§4诅咒已生效。你还有一点时间。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_anvil_hail", "铁砧冰雹", TrollTier.NIGHTMARE)
                .desc("天空开始下铁砧，密度参考暴雨")
                .weight(80)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.meteorShower(10, 20);
                    ctx.soundEverywhere(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.title("§8§l铁砧季", "§7天气预报没预报这个");
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_table_breathes", "附魔台在呼吸", TrollTier.NIGHTMARE)
                .desc("附魔台开始有节奏地起伏，声音从书架后面传来")
                .weight(80)
                .cooldownSeconds(380)
                .run(ctx -> {
                    ctx.randomHorrorSound();
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 80);
                    ctx.msg("§5附魔台……在呼吸。");
                    ctx.msg("§8你决定以后站着附魔。");
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_xp_void_taken", "经验被虚空收走", TrollTier.NIGHTMARE)
                .desc("等级和经验一起消失，虚空表示概不退还")
                .weight(80)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.setExperience(0);
                    ctx.damage("outofworld", 3.0f);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 40);
                    ctx.warn("§4虚空：§7「经验这种东西，留在你身上可惜了。」");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_netherite_vanish", "合金直接消失", TrollTier.NIGHTMARE)
                .desc("背包里最贵的那件东西，被虚空点名带走")
                .weight(80)
                .cooldownSeconds(450)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.takeRandomItem();
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 40);
                    ctx.msg("§4你听到虚空打了个饱嗝。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_armor_squeeze", "护甲自己勒紧", TrollTier.NIGHTMARE)
                .desc("整套护甲开始收缩，你被固定在了原地")
                .weight(90)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.freeze(120);
                    ctx.damage("magic", 3.0f);
                    ctx.msg("§4护甲：§7「别动，我们在测你的尺寸。」");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_forge_golem", "锻造台活了", TrollTier.NIGHTMARE)
                .desc("你身后的锻造台站了起来，手里拿着你的材料")
                .weight(80)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.giveMobCustomName(ctx.spawnHostileBehind(ctx.pick(TrollPools.BOSS)), "§8会走路的锻造台");
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.title("§4§l锻造完成", "§7成品是你自己");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_cursed_forge", "被诅咒的锻造台", TrollTier.NIGHTMARE)
                .desc("用诅咒换属性，这笔交易立刻生效")
                .weight(80)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.curseHeld();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 1);
                    ctx.msg("§5锻造台：§7「属性给你了。代价，稍后寄到。」");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_durability_doom", "耐久末日", TrollTier.NIGHTMARE)
                .desc("多件工具的耐久同时进入倒计时")
                .weight(90)
                .cooldownSeconds(380)
                .run(ctx -> {
                    ctx.breakRandomTool();
                    ctx.breakRandomTool();
                    ctx.damage("magic", 2.0f);
                    ctx.warn("§4你听见背包里传来此起彼伏的碎裂声。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_attribute_drained", "属性被抽干", TrollTier.NIGHTMARE)
                .desc("所有属性修饰符被一次性抽走，你变回了新手")
                .weight(80)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.clearEffects();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 360, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 360, 2);
                    ctx.msg("§4属性面板：§7「已恢复出厂设置。」");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_gear_lightning", "装备引雷", TrollTier.NIGHTMARE)
                .desc("你身上的金属装备今天特别招雷")
                .weight(90)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.lightning();
                    ctx.damage("lightning", 4.0f);
                    ctx.setFire(4);
                    ctx.glitch(GlitchIds.FLASHBANG, 20);
                    ctx.warn("§e雷说：§7「你身上金属挺多啊。」");
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_weapon_revenge", "武器的复仇", TrollTier.NIGHTMARE)
                .desc("被冷落已久的武器决定来一次彻底清算")
                .weight(80)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.damage("magic", 6.0f);
                    ctx.damageRandomTool(100);
                    ctx.randomHorrorSound();
                    ctx.msg("§4武器：§7「这些日子，我记得很清楚。」");
                    ctx.addRage(18);
                })
                .build());

        // ================================================================
        // APOCALYPSE —— 地狱：毁灭级，别指望拾取回来
        // ================================================================

        TrollEvents.reg(TrollEvent.of("ench_apocalypse_disarm", "万件归零", TrollTier.APOCALYPSE)
                .desc("背包与身上的装备被一次性清空，全部散落在世界里")
                .weight(60)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.dropEverything();
                    ctx.clearInventory();
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 60);
                    ctx.title("§4§l装备清算日", "§7请到附近地面自取");
                    ctx.addRage(25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_apocalypse_forge_god", "附魔台成神", TrollTier.APOCALYPSE)
                .desc("附魔台吸收了整片区域的等级，并开始说话")
                .weight(50)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.setExperience(0);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    ctx.glitch(GlitchIds.MASS_CHAT, 80);
                    ctx.titleToAll("§5§l附魔台已升格", "§7它现在只接受顶礼膜拜");
                    ctx.addRage(25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_apocalypse_anvil_world", "全世界铁砧雨", TrollTier.APOCALYPSE)
                .desc("天灾级铁砧暴雨，落地还会炸开")
                .weight(50)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.meteorShower(24, 28);
                    ctx.boomDestructive(ctx.x() + 3, ctx.y(), ctx.z() + 3, 4.0f);
                    ctx.soundEverywhere(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.title("§4§l锻造界的天谴", "§7铁砧：我们决定不修了");
                    ctx.addRage(25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_apocalypse_curse_all", "全套诅咒", TrollTier.APOCALYPSE)
                .desc("身上每一件装备都被同时挂上最恶毒的诅咒")
                .weight(50)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.curseHeld();
                    }
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.glitch(GlitchIds.FAKE_BAN, 50);
                    ctx.warn("§4全部装备：§7「我们商量好了，一起。」");
                    ctx.addRage(25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_apocalypse_void_gear", "装备被虚空吞噬", TrollTier.APOCALYPSE)
                .desc("脚边张开虚空，把你的装备与立足之地一起收走")
                .weight(50)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.clearInventory();
                    ctx.voidPit(3, 12);
                    ctx.damage("outofworld", 5.0f);
                    ctx.glitch(GlitchIds.FAKE_OOM, 45);
                    ctx.title("§0§l虚空已收货", "§7签收人：你");
                    ctx.addRage(25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_apocalypse_forge_kill", "锻造台要你的命", TrollTier.APOCALYPSE)
                .desc("锻造台认定你才是那件待加工的废料")
                .weight(40)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 5.0f);
                    ctx.kill();
                    ctx.glitch(GlitchIds.FAKE_DEATH, 60);
                    ctx.fakeDeathScreen("被自己的锻造台重铸了");
                    ctx.addRage(30);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("ench_apocalypse_fake_crash", "附魔台蓝屏", TrollTier.APOCALYPSE)
                .desc("附魔台把这次附魔做成了客户端崩溃体验，纯视觉")
                .weight(40)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 100);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 100);
                    ctx.setExperience(0);
                    ctx.fakeSaveNotice();
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_ACHIEVEMENTS), "在附魔台前学会了放弃");
                    ctx.addRage(28);
                })
                .build());
    }
}
