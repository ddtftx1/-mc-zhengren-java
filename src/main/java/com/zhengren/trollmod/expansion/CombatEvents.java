package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【战斗与武器】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。</p>
 *
 * <p>与原生事件文件一致，本文件刻意**不引用任何 Minecraft 类型**，
 * 全部通过 {@code ctx.*} 与 {@code TrollPools.*} 组合实现。</p>
 */
public final class CombatEvents {

    private CombatEvents() {
    }

    public static void register() {
        TrollEvents.category("战斗与武器");

        // ================================================================
        // PRANK —— 小整，笑一下就好（18 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("combat_sword_insult", "剑嫌弃你", TrollTier.ANNOYING)
                .desc("你的剑突然当众表达了对使用者水平的不满")
                .weight(150)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.renameHeld("§7被嫌弃的剑");
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "这把剑我熟，它上一位主人好歹会握");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.actionBar("§7你的武器已就绪，§8并已开始阴阳怪气。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_crit_whiff", "暴击变杂技", TrollTier.ANNOYING)
                .desc("你蓄力的一次暴击，最终演成了一段原地转圈")
                .weight(145)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.spin(540.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.6f);
                    ctx.msg("§e暴击判定：§7已改为§b表演判定§7。§8观众：零人。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_swing_miss", "挥空了", TrollTier.ANNOYING)
                .desc("你对着空气打出一套非常完整的连招")
                .weight(140)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.5f, 1.8f);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.actionBar("§7（划破空气的声音，格外清脆）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_dull_blade", "刀刃钝了", TrollTier.BRUTAL)
                .desc("你的武器手感突然变得像一根木棍")
                .weight(140)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.renameHeld("§8很贵的木棍");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§7锋利度：§8主观上没了§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_arrow_opinion", "箭有自己的想法", TrollTier.BRUTAL)
                .desc("你射出的箭在半空做出了一个与战术无关的决定")
                .weight(135)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE),
                            ctx.x() + ctx.ri(-2, 2), ctx.y() + 3, ctx.z() + ctx.ri(-2, 2));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.5f);
                    ctx.msg("§7箭表示：§8目标是它自己选的§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_shield_polish", "盾牌要保养", TrollTier.ANNOYING)
                .desc("盾牌向你索要一笔根本用不上的保养费")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "盾牌记得上油，不然格挡会刮花");
                    ctx.msg("§7维修工单已生成§8，费用从你的耐心扣除§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_armor_fashion", "护甲时尚点评", TrollTier.ANNOYING)
                .desc("一位看不见的评委对你的战甲给出了一段犀利点评")
                .weight(130)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.fakeChat("§8[时尚评审]", "配色还行，就是人撑不起来");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.msg("§7本次护甲评分：§e6.5§7。§8扣分项：你。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_potion_sip", "药水味道不对", TrollTier.BRUTAL)
                .desc("你喝下的药水味道偏向水果，效果也偏向随机")
                .weight(125)
                .cooldownSeconds(65)
                .run(ctx -> {
                    ctx.effectRandom(80, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.7f);
                    ctx.msg("§d口味：§7蜜桃§8；功效：§7待定§8。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_bowstring_twang", "弓弦打脸", TrollTier.BRUTAL)
                .desc("弓弦在松手之后，选择了回弹到你的脸上")
                .weight(125)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.playHurtSound();
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.7f, 1.6f);
                    ctx.actionBar("§7弓：§8这一下不算伤害，算提醒。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_training_dummy", "陪练登场", TrollTier.BRUTAL)
                .desc("凭空冒出一个自称陪练的家伙，站姿非常不专业")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.giveMobCustomName(
                            ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                    ctx.x() + ctx.ri(-3, 3), ctx.y(), ctx.z() + ctx.ri(-3, 3)),
                            "§7陪练（业余）");
                    ctx.msg("§7陪练已就位§8，计时收费，先记在你账上§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_sword_sparkle", "剑身反光", TrollTier.BRUTAL)
                .desc("你的武器突然反光到让你短暂怀疑人生")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 35);
                    ctx.glitch(GlitchIds.FLASHBANG, 8, 0);
                    ctx.msg("§f反光度：§7过高§8。敌人没瞎，你先瞎了§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_battle_cry", "战吼跑调", TrollTier.BRUTAL)
                .desc("你蓄势待发的战吼，第三个音就彻底跑偏了")
                .weight(115)
                .cooldownSeconds(80)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                    ctx.title("§6§l啊啊啊——", "§7后半段是哭声");
                    ctx.msg("§7战吼效果：§c士气 -3§7，§8敌人 -0§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_honor_duel", "堂堂正正", TrollTier.BRUTAL)
                .desc("系统郑重宣布这是一场公平决斗，并立即收回了公平")
                .weight(115)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.fakeBroadcast("§e[竞技场] 本场决斗规则：公平、公开、单方面。");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.8f, 1.2f);
                    ctx.msg("§7公平已发放§8，回收时间：现在。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_axe_philosophy", "斧头的哲学", TrollTier.PRANK)
                .desc("你的斧头突然开始思考存在的意义，并且说话")
                .weight(110)
                .cooldownSeconds(85)
                .run(ctx -> {
                    ctx.fakeChat("§8你的斧头", "我劈的是木头，还是自己的命运");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.5f);
                    ctx.msg("§7它暂时不打算劈任何东西§8，包括敌人。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_helmet_as_hat", "头盔当帽戴", TrollTier.BRUTAL)
                .desc("你的头盔被重新定义成了一个时尚单品")
                .weight(110)
                .cooldownSeconds(80)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.renameHeld("§e限量款帽子");
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.msg("§7防护值不变§8，气质提升约§e300%§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_xp_leak", "经验漏了", TrollTier.BRUTAL)
                .desc("你身上的经验肉眼可见地往外飘了几粒")
                .weight(105)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.4f);
                    ctx.actionBar("§a经验粒子§7正在离你而去§8，数量：个位数。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_creeper_hug", "苦力怕想抱抱", TrollTier.ANNOYING)
                .desc("一只自称性格温和的苦力怕从背后靠近了你")
                .weight(105)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.giveMobCustomName(
                            ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                    ctx.x() + ctx.ri(-3, 3), ctx.y(), ctx.z() + ctx.ri(-3, 3)),
                            "§a只是想抱抱");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.msg("§a它说它不会爆§8，它说的时候离你很近。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_trophy_show", "战利品展示", TrollTier.ANNOYING)
                .desc("系统为你颁发了一个根本没赢过的战斗成就")
                .weight(100)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), ctx.ri(1, 3));
                    ctx.fakeAchievement("§6参与奖", "§7你参加了，这已经很难得。");
                    ctx.msg("§7奖杯已发放§8，材质：你的想象力。§7");
                })
                .build());

        // ================================================================
        // ANNOYING —— 烦人，会皱眉（32 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("combat_blade_dull", "武器变钝", TrollTier.BRUTAL)
                .desc("你的武器在关键时刻损失了一大截耐久")
                .weight(145)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.damageRandomTool(80);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.warn("§7维修费你先别算，先说还剩多少。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_shield_cooldown", "盾牌进入冷却", TrollTier.NIGHTMARE)
                .desc("你举盾的动作被判定为需要休息。盾牌在你最需要它的时候请了假。")
                .weight(140)
                .cooldownSeconds(85)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.setCooldown(ctx.pick(TrollPools.DANGEROUS), 160);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.msg("§7盾牌冷却已激活§8，理由：它累了。§7");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_armor_shrink", "护甲缩水", TrollTier.BRUTAL)
                .desc("你的护甲被重新分配了一遍，理由写在说明书背面")
                .weight(140)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.swapEquipmentRandomly();
                    ctx.msg("§7装备已重新搭配§8，风格：随机。§7");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_knockback_self", "击退自己", TrollTier.NIGHTMARE)
                .desc("你的攻击产生了击退，而方向指向你自己。")
                .weight(135)
                .cooldownSeconds(85)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.fling(ctx.rd(-1.5, 1.5), 0.4, ctx.rd(-1.5, 1.5));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.4f);
                    ctx.msg("§7物理引擎表示：§8这符合动量守恒§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_arrow_backfire", "箭往自己飞", TrollTier.BRUTAL)
                .desc("你的箭以一条优美的弧线回到了发射者身上")
                .weight(135)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.damage("magic", 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.5f);
                    ctx.msg("§7弹道修正完成§8，修正方向：反的。§7");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_friendly_fire", "队友误伤", TrollTier.NIGHTMARE)
                .desc("你的一击精准命中了唯一不该打的那个人。")
                .weight(130)
                .cooldownSeconds(95)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.damageOther(ctx.other(), 3.0f);
                    ctx.msgToOther(ctx.other(), "§c你被队友误伤了§7，对方表示这属于战术。");
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_potion_swap", "药水调包", TrollTier.NIGHTMARE)
                .desc("增益药水被换成了另一种颜色的液体。味道也不对。")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.effectRandom(160, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.8f, 1.2f);
                    ctx.msg("§d标签写着§a生命恢复§d，内容物拒绝签字。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_hunger_midfight", "打着打着饿了", TrollTier.BRUTAL)
                .desc("战斗正酣，你的胃突然提出了一项紧急议题")
                .weight(130)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.exhaust(6.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.actionBar("§7胃：§8先吃饭，再打架。§7");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_weapon_jam", "武器卡壳", TrollTier.BRUTAL)
                .desc("你的攻击动作在中途被一股神秘阻力叫停")
                .weight(125)
                .cooldownSeconds(95)
                .run(ctx -> {
                    ctx.setCooldown(ctx.pick(TrollPools.DANGEROUS), 120);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 60, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§7卡壳原因：§8未查明§7。修复方式：§8等。§7");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_crit_fail", "暴击失败", TrollTier.BRUTAL)
                .desc("暴击特效已经播完，伤害数字决定不跟上")
                .weight(125)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.damage("magic", 1.0f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 30);
                    ctx.msg("§e暴击特效：§7满配§8。伤害：§7意思一下§8。§7");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_durability_rain", "耐久狂掉", TrollTier.NIGHTMARE)
                .desc("武器经历了一次强度堪忧的连续消耗，开始在你手里发软。")
                .weight(125)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.damageRandomTool(60);
                    ctx.damageRandomTool(60);
                    ctx.damageRandomTool(60);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.1f);
                    ctx.warn("§7耐久条正在做自由落体运动。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_block_broken", "格挡失败", TrollTier.BRUTAL)
                .desc("你举起了盾，攻击从盾的另一侧绕了过来")
                .weight(120)
                .cooldownSeconds(95)
                .run(ctx -> {
                    ctx.damage("flyintowall", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§7格挡判定：§c无效§7。§8盾：我很努力了。§7");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_mob_buff", "怪物集体变强", TrollTier.ANNOYING)
                .desc("全场敌对生物收到了一份不该有的强化buff")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                    ctx.title("§c§l敌方强化", "§7数值不改，气势加了");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_boss_tease", "Boss 假登场", TrollTier.NIGHTMARE)
                .desc("压迫感十足的音乐响起，过来的却是个普通家伙。你为一个假目标浪费了警惕。")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.8f);
                    ctx.giveMobCustomName(
                            ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                    ctx.x() + ctx.ri(-4, 4), ctx.y(), ctx.z() + ctx.ri(-4, 4)),
                            "§c最终Boss（§7实习§c）");
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§8登场时间：三秒");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_miss_streak", "连续落空", TrollTier.NIGHTMARE)
                .desc("连续三次攻击全部落空。节奏被打乱之后，你的手开始不信自己。")
                .weight(115)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    for (int i = 0; i < 3; i++) {
                        ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.6f, 1.6f + i * 0.2f);
                    }
                    ctx.actionBar("§7命中率：§c0 / 3§7。§8建议：靠近一点。§7");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_armor_rust", "护甲生锈", TrollTier.BRUTAL)
                .desc("你的护甲以肉眼可见的速度长出了一层锈")
                .weight(115)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 140, 0);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.msg("§6锈蚀§7已覆盖全身§8，防护值正在随缘。§7");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_weapon_swap", "武器被换", TrollTier.BRUTAL)
                .desc("你握着的东西已经不是刚才那把了")
                .weight(115)
                .cooldownSeconds(105)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.scrambleHotbar();
                    ctx.randomSelectSlot();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.msg("§7手中武器已变更§8，变更内容：不告诉你。§7");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_inventory_lag", "战斗中翻包", TrollTier.BRUTAL)
                .desc("你的背包在战斗瞬间自动完成了一次大扫除")
                .weight(110)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.shuffleInventory();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§7整理完毕§8，你现在需要花三秒找东西。§7");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_trident_lost", "三叉戟脱手", TrollTier.NIGHTMARE)
                .desc("三叉戟决定暂时不参与这场战斗。它没告诉你什么时候回来。")
                .weight(110)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.dropHeld();
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§7它落地了§8，还滚了两下，像是在告别。§7");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_bow_dryfire", "弓空放", TrollTier.BRUTAL)
                .desc("你拉满了弓，松手时听见的却是一声哑响")
                .weight(110)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                    ctx.setCooldown(ctx.pick(TrollPools.DANGEROUS), 100);
                    ctx.actionBar("§7啪。§8就这一声，没有别的。§7");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_potion_spill", "药水洒了", TrollTier.BRUTAL)
                .desc("你身上的所有药水效果被一次性倒在了地上")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.clearEffects();
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.msg("§d药水效果已清空§8，地面表示吸收良好。§7");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_helmet_blind", "头盔遮眼", TrollTier.NIGHTMARE)
                .desc("头盔在关键时刻滑下来一点。就一点，刚好挡住你需要的视野。")
                .weight(105)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.glitch(GlitchIds.SHAKY, 70, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 1.4f);
                    ctx.actionBar("§8视野被一小块铁挡住了，但正好挡住要害。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_taunt_lag", "嘲讽延迟", TrollTier.BRUTAL)
                .desc("你精心准备的垃圾话，晚了整整一个回合才送达")
                .weight(105)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.later(40, () -> ctx.fakeChat(ctx.playerName(), "……你妈贵姓？"));
                    ctx.msg("§7嘲讽已进入队列§8，预计延迟：一个回合。§7");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_swap_teammate", "与队友换位", TrollTier.NIGHTMARE)
                .desc("你和队友在战斗中互换了站位。双方都愣了一瞬，而对方没有。")
                .weight(105)
                .cooldownSeconds(120)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.swapWith(ctx.other());
                    ctx.msg("§7换位成功§8，你们互相继承了对方的敌人。§7");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_pull_enemies", "把敌人拉过来", TrollTier.BRUTAL)
                .desc("你的周围产生了一股引力，只是拉错了对象")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.pullPlayersToMe(12.0);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.msg("§7引力场已生成§8，中心点：你。§7");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_knockback_mob", "被反手击退", TrollTier.NIGHTMARE)
                .desc("你打出去的击退被原样退回，还带了利息。")
                .weight(100)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.fling(ctx.rd(-2.5, 2.5), 0.5, ctx.rd(-2.5, 2.5));
                    ctx.damage("flyintowall", 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_armor_squeak", "护甲吱吱响", TrollTier.BRUTAL)
                .desc("你每动一下，护甲就发出一声令人不适的摩擦音")
                .weight(100)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.renameRandomItem("§8会响的装备");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.7f);
                    ctx.actionBar("§7吱。§8嘎。§7吱。§8你已经无法忽视它了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_sword_curse_mild", "轻度诅咒", TrollTier.NIGHTMARE)
                .desc("武器获得了一份不太正式的附魔。它对你的态度也跟着变了。")
                .weight(100)
                .cooldownSeconds(130)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.curseHeld();
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 0.9f);
                    ctx.msg("§5诅咒已生效§8，等级：轻微。影响：心理。§7");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_xp_fee", "经验手续费", TrollTier.ANNOYING)
                .desc("系统从你身上扣除了一笔名目不清的战斗手续费")
                .weight(95)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.setExperience(0);
                    ctx.fakeChat("§8[结算中心]", "本次战斗产生手续费，已从经验中扣除");
                    ctx.msg("§7经验余额：§c0§7。§8感谢配合。§7");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_regen_denied", "回血被拒", TrollTier.BRUTAL)
                .desc("你的恢复效果被系统以材料不全为由驳回")
                .weight(95)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.removeEffect(ctx.pick(TrollPools.WEIRD_EFFECTS));
                    ctx.damage("magic", 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.msg("§7恢复申请已被驳回§8，驳回理由：你自己看看。§7");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_shield_reverse", "盾牌装反", TrollTier.BRUTAL)
                .desc("你的盾牌朝向了后方，防御姿态变得很有创意")
                .weight(95)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.randomRotation();
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 60, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.msg("§7格挡面：§8背对敌人§7。战术价值：§8无。§7");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_duel_callout", "决斗邀请", TrollTier.ANNOYING)
                .desc("全服收到了一份以你名义发出的决斗邀请")
                .weight(95)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.fakeBroadcast("§6[决斗] " + ctx.playerName() + " 已向全场发起挑战，请做好准备。");
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.1f);
                    ctx.msg("§7请柬已寄出§8，署名：你，未经你同意。§7");
                    ctx.addRage(8);
                })
                .build());

        // ================================================================
        // BRUTAL —— 抓狂，会骂人（27 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("combat_disarm", "武器被打飞", TrollTier.APOCALYPSE)
                .desc("武器在一次冲击后离开你的手，并走得很远。")
                .weight(60)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.dropHeld();
                    ctx.fling(ctx.rd(-2.0, 2.0), 0.7, ctx.rd(-2.0, 2.0));
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.warn("§c你被缴械了§7，捡回来之前请先活着。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_armor_break", "护甲碎一地", TrollTier.APOCALYPSE)
                .desc("整套护甲被判定为超期服役，当场解散。")
                .weight(58)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.swapEquipmentRandomly();
                    for (int i = 0; i < 2; i++) {
                        ctx.dropRandom();
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_ambush", "背后偷袭", TrollTier.APOCALYPSE)
                .desc("有人在你专心攻击的时候，从死角完成了背刺。")
                .weight(56)
                .cooldownSeconds(210)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.damage("flyintowall", 5.0f);
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.msg("§c背后判定：§7命中§8。你的背后：§7一直空着§8。§7");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_headshot", "被爆头", TrollTier.APOCALYPSE)
                .desc("一次教科书级别的爆头打击，附带满屏白光。")
                .weight(55)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.damage("magic", 5.0f);
                    ctx.glitch(GlitchIds.FLASHBANG, 25, 1);
                    ctx.playHurtSound();
                    ctx.msg("§c暴击等级：§4致命§c。§7对方的手感非常好。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_potion_overdose", "药水过量", TrollTier.NIGHTMARE)
                .desc("一口气灌下的药水，在体内开始了不受控的混合反应。")
                .weight(54)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.effectRandom(300, 2);
                    ctx.damage("magic", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 1.1f);
                    ctx.msg("§d体内化学反应§7：§8正在进行§d。结果：§8拭目以待§d。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_mob_swarm_elite", "精英怪包围", TrollTier.NIGHTMARE)
                .desc("围住你的不是普通怪，是明确加强过的版本，而且它们知道。")
                .weight(54)
                .cooldownSeconds(240)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                    }
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 220, 1);
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_weapon_break", "武器当场断裂", TrollTier.NIGHTMARE)
                .desc("在一记本该决定胜负的攻击中，你的武器选择了退出。")
                .weight(53)
                .cooldownSeconds(230)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.breakRandomTool();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.5f);
                    ctx.warn("§c你听见了一声很清脆、很不吉利的响。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_arrow_volley", "箭雨", TrollTier.NIGHTMARE)
                .desc("一整轮箭雨朝你落下。来源不明，密度很高，像在等你站着不动。")
                .weight(52)
                .cooldownSeconds(240)
                .run(ctx -> {
                    for (int i = 0; i < 8; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE),
                                ctx.x() + ctx.ri(-4, 4), ctx.y() + ctx.ri(6, 10), ctx.z() + ctx.ri(-4, 4));
                    }
                    ctx.damage("magic", 3.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_ground_slam", "落地重击", TrollTier.NIGHTMARE)
                .desc("你被抬到高处，然后被安排了一次质量很大的落地。")
                .weight(51)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.lift(18);
                    ctx.damage("fall", 5.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§7落地姿势：§8不建议参考§7。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_lava_arena", "竞技场变岩浆", TrollTier.NIGHTMARE)
                .desc("脚下的竞技场被重新铺成了岩浆。这才是这一场的赛制。")
                .weight(50)
                .cooldownSeconds(250)
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.setFire(6);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§c主场优势§7已转移给场地本身§8。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_bleed", "持续流血", TrollTier.APOCALYPSE)
                .desc("身上多了一道不会自己停下来的伤口。你捂不住它。")
                .weight(50)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.damage("wither", 3.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_eclipse_duel", "决斗场断电", TrollTier.APOCALYPSE)
                .desc("视野里的光被一次性收走。敌人显然不需要照明。")
                .weight(49)
                .cooldownSeconds(250)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.glitch(GlitchIds.BLACKOUT, 60);
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    }
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_shield_break", "盾牌被击碎", TrollTier.APOCALYPSE)
                .desc("你赖以生存的格挡手段，被一次性作废。")
                .weight(48)
                .cooldownSeconds(250)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.curseHeld();
                    ctx.damage("flyintowall", 4.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.warn("§c格挡值归零§7，接下来请靠走位。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_counter_crit", "反击暴击", TrollTier.NIGHTMARE)
                .desc("对方的反击来得又快又准，像是等了这一刻很久。")
                .weight(48)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.damage("flyintowall", 5.0f);
                    ctx.fling(ctx.rd(-2.0, 2.0), 0.5, ctx.rd(-2.0, 2.0));
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_explosive_arrow", "爆炸箭", TrollTier.NIGHTMARE)
                .desc("落在你脚边的箭，表现出了远超箭支的威力。")
                .weight(47)
                .cooldownSeconds(250)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.boomAtPlayer(2.2f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 70);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§c那是箭吗§7？§8现在不是了。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_armor_crush", "护甲被压扁", TrollTier.NIGHTMARE)
                .desc("你的防护在一记重击下被彻底压扁，连同自信。")
                .weight(47)
                .cooldownSeconds(250)
                .run(ctx -> {
                    ctx.effectRandom(280, 2);
                    ctx.damage("flyintowall", 4.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_boss_minion", "首领派来了全部下属", TrollTier.APOCALYPSE)
                .desc("某个你看不见的首领，一次性把它的全部下属派到了你面前。")
                .weight(46)
                .cooldownSeconds(260)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7它们只是先遣队");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_wither_touch", "凋零之触", TrollTier.NIGHTMARE)
                .desc("有一只看不见的手碰了你一下，然后你开始腐烂。")
                .weight(46)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.damage("wither", 4.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 280, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.msg("§8触碰者：§7未知§8。后果：§7明确§8。§7");
                    ctx.addRage(19);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_trident_storm", "三叉戟风暴", TrollTier.APOCALYPSE)
                .desc("天空配合着三叉戟的节奏，开始往你头上落雷。")
                .weight(45)
                .cooldownSeconds(260)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.thunderstorm(900);
                    ctx.lightningNear();
                    ctx.lightningNear();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.addRage(19);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_friendly_fire_back", "误伤反弹", TrollTier.APOCALYPSE)
                .desc("你对队友下手。伤害按双倍，返还给了你自己。")
                .weight(45)
                .cooldownSeconds(250)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.damageOther(ctx.other(), 3.0f);
                    ctx.damage("magic", 5.0f);
                    ctx.msg("§c误伤仲裁§7：§8责任方是你，赔偿方也是你§7。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_poison_blade", "淬毒之刃", TrollTier.APOCALYPSE)
                .desc("对方的武器涂了点东西。你已经从伤口里感觉到了。")
                .weight(45)
                .cooldownSeconds(260)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.damage("magic", 3.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.1f);
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_rage_mode", "狂暴化", TrollTier.NIGHTMARE)
                .desc("你被强行切进狂暴状态，控制权不在你手里。")
                .weight(44)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 260, 1);
                    ctx.damage("magic", 3.0f);
                    ctx.fling(ctx.rd(-3.0, 3.0), 0.6, ctx.rd(-3.0, 3.0));
                    ctx.title("§c§l狂暴", "§8目标是最近的活物，包括你");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_armor_strip", "护甲被缴械", TrollTier.BRUTAL)
                .desc("你身上的装备被一件件拿了下来，动作很专业")
                .weight(44)
                .cooldownSeconds(270)
                .run(ctx -> {
                    ctx.swapEquipmentRandomly();
                    ctx.dropRandom();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§7对方手法很熟练§8，看得出来做过功课。§7");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_arena_walls", "竞技场封闭", TrollTier.APOCALYPSE)
                .desc("场地四周升起墙，唯一的出入口被墙自己堵上了。")
                .weight(43)
                .cooldownSeconds(280)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.surroundWith(ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.setBlock(0, 3, 0, ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 0);
                    ctx.msg("§8场地已封闭§7，比赛继续，直到有人不行。");
                    ctx.addRage(20);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_execute", "处决一击", TrollTier.NIGHTMARE)
                .desc("一记精准打击把你打入残血。最危险的区间，从这里开始。")
                .weight(43)
                .cooldownSeconds(280)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.damage("magic", 6.0f);
                    ctx.playHurtSound();
                    ctx.glitch(GlitchIds.FLASHBANG, 20, 1);
                    ctx.warn("§4血量进入危险区§7，接下来每一次失误都算数。");
                    ctx.addRage(20);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_reinforcements", "敌方增援", TrollTier.NIGHTMARE)
                .desc("你刚打出优势，对面就叫来了三个明显更强的人。")
                .weight(42)
                .cooldownSeconds(280)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    }
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.msg("§c增援已抵达§7，根据战场惯例，你这边没有。");
                    ctx.addRage(19);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_last_stand", "背水一战", TrollTier.NIGHTMARE)
                .desc("系统宣布你已无路可退，然后把退路也一起封了。")
                .weight(42)
                .cooldownSeconds(290)
                .run(ctx -> {
                    ctx.damage("flyintowall", 4.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.fakeBroadcast("§c[战场] 退路已关闭，请战斗到最后一刻。");
                    ctx.addRage(20);
                })
                .build());

        // ================================================================
        // NIGHTMARE —— 崩溃，会砸键盘（15 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("combat_boss_arena", "Boss 竞技场", TrollTier.OBLIVION)
                .desc("三个首领同时入场，场地在你脚下爆开，观众席上没有人。")
                .weight(34)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.title("§4§l首领入场", "§8共三位，不设观众席");
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                    }
                    ctx.boomAtPlayer(2.5f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    ctx.buryAlive();
                    // 假希望：它们退后了一步
                    ctx.later(120, () -> {
                        ctx.msg("§7它们退后了一步。§8好像有机会。");
                        ctx.later(80, () -> {
                            ctx.obliterate();
                            for (int i = 0; i < 2; i++) {
                                ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                            }
                            ctx.warn("§4它们不是在退。§7它们是在给后面那两位腾位置。");
                        });
                    });
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_fake_death_loop", "死亡循环假象", TrollTier.APOCALYPSE)
                .desc("屏幕反复演示你死亡的画面，一共三次，而你还活着。")
                .weight(33)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.fakeDeathScreen("§7被自己的攻击反噬");
                    ctx.glitch(GlitchIds.FAKE_DEATH, 90, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.msg("§8……你还在这儿§7，这一点很重要。");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_army_of_dead", "亡灵大军", TrollTier.OBLIVION)
                .desc("地里爬起来的东西多得超出这场战斗的规格。它们记得你，你记不清它们。")
                .weight(32)
                .cooldownSeconds(430)
                .run(ctx -> {
                    ctx.title("§8§l亡灵大军", "§7它们记得你，你记不清它们");
                    for (int i = 0; i < 8; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    }
                    ctx.glitch(GlitchIds.SHAKY, 20 * 7, 2);
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 8, 2);
                    ctx.buryAlive();
                    ctx.msg("§8你数不清了。§7它们不打算让你数清。");
                    ctx.later(170, () -> {
                        ctx.obliterate();
                        ctx.takeRandomItems(3);
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                        ctx.warn("§4它们不急着杀你。§7它们只是轮流站到你面前，让你一直数下去。");
                    });
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_instant_kill_tease", "秒杀预告", TrollTier.NIGHTMARE)
                .desc("你被锁定了一瞬。那一下的力度，足够清空血条。")
                .weight(32)
                .cooldownSeconds(440)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.glitch(GlitchIds.FLASHBANG, 30, 2);
                    ctx.damage("magic", 6.0f);
                    ctx.title("§4§l锁定中", "§8躲开，或者不躲");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_weapon_curse_full", "武器全面诅咒", TrollTier.APOCALYPSE)
                .desc("武器被追加了多重诅咒，并开始厌恶你。")
                .weight(31)
                .cooldownSeconds(450)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.curseHeld();
                    ctx.enchantHeldRandomly();
                    ctx.damage("magic", 4.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                    ctx.msg("§5它现在属于你§8，但它的态度非常明确。§7");
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_health_drain", "生命虹吸", TrollTier.NIGHTMARE)
                .desc("有什么东西在持续从你身上取走生命，而你看不见饮者。")
                .weight(31)
                .cooldownSeconds(460)
                .run(ctx -> {
                    ctx.damage("wither", 6.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 340, 2);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 80);
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_arena_collapse", "竞技场坍塌", TrollTier.APOCALYPSE)
                .desc("整个场地在你脚下塌下去，连同天花板一起。")
                .weight(30)
                .cooldownSeconds(470)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.scatterBlocks(ctx.pick(TrollPools.TROLL_BLOCKS), 30);
                    ctx.boomAtPlayer(2.5f);
                    ctx.damage("fall", 5.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_void_duel", "虚空决斗", TrollTier.APOCALYPSE)
                .desc("你被挪到虚空边缘。对手表示，这里它比较熟。")
                .weight(30)
                .cooldownSeconds(480)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.teleport(ctx.x(), -56.0, ctx.z());
                    ctx.damage("outofworld", 2.0f);
                    ctx.glitch(GlitchIds.BLACKOUT, 50);
                    ctx.msg("§8下面没有地面§7，上面没有退路。");
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_hellfire_blade", "地狱火之刃", TrollTier.APOCALYPSE)
                .desc("对方每一次挥砍都带着火，地板也配合地变成了岩浆。")
                .weight(30)
                .cooldownSeconds(470)
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.setFire(12);
                    ctx.damage("fire", 3.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_mob_apocalypse_buff", "怪物全面强化", TrollTier.OBLIVION)
                .desc("全场怪物同时被强化，并收到了你的实时坐标。而它把这件事也通知了你。")
                .weight(29)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.fakeBroadcast("§4[战报] 敌方已全体强化，你的坐标已同步。");
                    ctx.title("§4§l你 已 被 定 位", "§7这条通知是发给你看的，不是给它们看的");
                    ctx.stripAll();
                    ctx.later(160, () -> {
                        ctx.obliterate();
                        ctx.takeRandomItems(2);
                        ctx.warn("§4它们不需要坐标。§7它们顺着你身上那点光走。");
                    });
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_lightning_duel", "雷神决斗", TrollTier.NIGHTMARE)
                .desc("你的对手和天气有点交情。连劈三下，都在你附近。")
                .weight(29)
                .cooldownSeconds(490)
                .run(ctx -> {
                    ctx.lightningNear();
                    ctx.lightningNear();
                    ctx.lightning();
                    ctx.glitch(GlitchIds.FLASHBANG, 22, 2);
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_frost_arena", "冰封竞技场", TrollTier.APOCALYPSE)
                .desc("场地温度骤降。你的动作开始跟不上你的意图。")
                .weight(29)
                .cooldownSeconds(480)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.freeze(80);
                    ctx.damage("freeze", 4.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 1);
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_berserk_curse", "狂战士诅咒", TrollTier.APOCALYPSE)
                .desc("一股不属于你的怒火被灌了进来，身体开始自行行动。")
                .weight(28)
                .cooldownSeconds(510)
                .run(ctx -> {
                    ctx.damage("magic", 5.0f);
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 300, 2);
                    ctx.fling(ctx.rd(-4.0, 4.0), 0.7, ctx.rd(-4.0, 4.0));
                    ctx.glitch(GlitchIds.SHAKY, 140, 2);
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_shadow_clone", "暗影分身", TrollTier.OBLIVION)
                .desc("从你的影子里走出几个和你一模一样的东西。它们用的是你的姿势。")
                .weight(28)
                .cooldownSeconds(500)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.giveMobCustomName(ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE)), "§8你的影子");
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.msg("§8它们用的是你的姿势§7，这一点最让人难受。");
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 20 * 8, 2);
                    // 假希望：它们散开了
                    ctx.later(130, () -> {
                        ctx.msg("§7它们散开了。§8好像结束了。");
                        ctx.later(90, () -> {
                            ctx.obliterate();
                            ctx.takeRandomItems(3);
                            ctx.warn("§4散开，是为了从四个方向同时看你。§7它们学得很快。");
                        });
                    });
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_blood_arena", "血之竞技场", TrollTier.OBLIVION)
                .desc("全场开始渗血，观众席上坐满了看不清脸的东西。它们买的是你的票。")
                .weight(28)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.title("§4§l血之竞技场", "§8本场没有观众，只有参与者");
                    ctx.damage("magic", 4.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 100);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 10, 3);
                    ctx.buryAlive();
                    ctx.later(170, () -> {
                        ctx.stripAll();
                        ctx.obliterate();
                        ctx.msg("§8观众席上那些东西开始鼓掌了。§7它们等的就是这一刻。");
                    });
                    ctx.addRage(28);
                })
                .build());

        // ================================================================
        // APOCALYPSE —— 地狱，会退游戏（7 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("combat_final_duel", "最终决斗", TrollTier.APOCALYPSE)
                .desc("四位首领同时入场，天雷伴奏。你是唯一被安排好的对手。")
                .weight(25)
                .cooldownSeconds(1000)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 9);
                    }
                    ctx.thunderstorm(1800);
                    ctx.boomAtPlayer(3.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.title("§4§l最终决斗", "§8规则：你输，或者你输");
                    ctx.addRage(42);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_annihilation", "湮灭", TrollTier.OBLIVION)
                .desc("天火、地裂与全屏震动同时到位。战场会被从地图上划掉，连同你。")
                .weight(24)
                .cooldownSeconds(1100)
                .run(ctx -> {
                    ctx.title("§4§l湮灭", "§8战场已从地图上划掉");
                    ctx.meteorShower(9, 24);
                    ctx.boomAtPlayer(3.0f);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 20 * 5, 3);
                    ctx.damage("magic", 5.0f);
                    ctx.buryAlive();
                    // 假希望：震动停了
                    ctx.later(140, () -> {
                        ctx.msg("§7……停了。");
                        ctx.later(90, () -> {
                            ctx.obliterate();
                            ctx.takeRandomItems(4);
                            ctx.warn("§4它不是停了。§7它是把最后一下，留在了你脚下。");
                        });
                    });
                    ctx.addRage(44);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_world_war", "世界大战", TrollTier.OBLIVION)
                .desc("所有阵营同时向你宣战，兵力按倾巢而出计算。没有人打算调解。")
                .weight(23)
                .cooldownSeconds(1200)
                .run(ctx -> {
                    ctx.fakeBroadcast("§4[总动员] 全体敌对生物已向你宣战，无调解可能。");
                    for (int i = 0; i < 10; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    ctx.title("§4§l世 界 大 战", "§7参战方：所有东西。对面：只有你");
                    ctx.buryAlive();
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 8, 3);
                    ctx.later(190, () -> {
                        ctx.obliterate();
                        ctx.stripAll();
                        ctx.takeRandomItems(3);
                        ctx.warn("§4它们不谈判。§7它们甚至不需要赢，只需要一直在。");
                    });
                    ctx.addRage(45);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_deaths_embrace", "死亡拥抱", TrollTier.OBLIVION)
                .desc("你身上的一切会被同时清空，只留下一个拥抱的力气。")
                .weight(22)
                .cooldownSeconds(1300)
                .run(ctx -> {
                    ctx.dropEverything();
                    ctx.damage("wither", 5.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 120);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 8, 3);
                    ctx.title("§4§l死 亡 拥 抱", "§7它抱得很紧，也很久");
                    ctx.buryAlive();
                    ctx.later(170, () -> {
                        ctx.obliterate();
                        ctx.msg("§8它没有拿走你的命。§7它只是让你确认了一遍：你其实什么都留不住。");
                    });
                    ctx.addRage(46);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_unstoppable_force", "势不可挡", TrollTier.APOCALYPSE)
                .desc("一股你无法抵抗的力量，替你把要去的地方选好了。")
                .weight(22)
                .cooldownSeconds(1400)
                .run(ctx -> {
                    ctx.fling(ctx.rd(-5.0, 5.0), 3.0, ctx.rd(-5.0, 5.0));
                    ctx.lightning();
                    ctx.damage("fall", 6.0f);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 80, 3);
                    ctx.addRage(45);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_apocalypse_blade", "天启之刃", TrollTier.APOCALYPSE)
                .desc("你被封进剑形牢笼，成为这把武器唯一的核心部件。")
                .weight(21)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.cageWith(ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.glitch(GlitchIds.HIDE_HUD, 200, 1);
                    ctx.setHealth(6.0f);
                    ctx.setFire(18);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.title("§4§l天启之刃", "§8你已被铸入其中，永久保修");
                    ctx.addRage(47);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("combat_last_stand_true", "最后一战", TrollTier.OBLIVION)
                .desc("伪造崩溃与终局通告，再顺手结束你这一局。只此一次，所以它是真的。")
                .weight(20)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 20 * 6, 3);
                    ctx.fakeBroadcast("§4你的战斗记录已归档。感谢你为这场战争付出的全部。");
                    ctx.fakeAchievement("§4§l最后一战", "§7你战斗到了最后一刻，然后就没有然后了。");
                    ctx.obliterate();
                    // 假希望：你以为自己还活着
                    ctx.later(150, () -> {
                        ctx.msg("§7……我还在。§8那个提示是假的。");
                        ctx.later(110, () -> {
                            ctx.kill();
                            ctx.msg("§8对。§7这个才是真的。");
                        });
                    });
                    ctx.addRage(50);
                })
                .build());
    }
}
