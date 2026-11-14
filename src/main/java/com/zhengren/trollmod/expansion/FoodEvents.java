package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【饥饿与食物】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。</p>
 *
 * <p>主题：饥饿值、饱和度、食物中毒、生肉熟肉、面包蛋糕曲奇、汤与炖菜、金苹果、
 * 牛奶、药水、蜂蜜、可疑的炖菜、吃太快、吃不下、暴饮暴食。
 * 主力 API 是 {@code ctx.feed / starve / exhaust / saturation / foodLevel}，
 * 本分类**不引用任何 Minecraft 类型**，全部通过 {@code ctx.*} + {@code TrollPools.*}
 * + {@code GlitchIds.*} 组合实现。</p>
 *
 * <p>等级分布：PRANK 18 / ANNOYING 32 / BRUTAL 27 / NIGHTMARE 15 / APOCALYPSE 7 = 99。</p>
 */
public final class FoodEvents {

    private FoodEvents() {
    }

    public static void register() {
        TrollEvents.category("饥饿与食物");

        // ==================================================================
        // PRANK —— 小整：嘴上一句玩笑，饿不死人（18 个）
        // ==================================================================

        // [1]
        TrollEvents.reg(TrollEvent.of("food_fake_bite", "空气咬了一口", TrollTier.PRANK)
                .desc("清脆的咀嚼声响起，但你手里什么都没有")
                .weight(150)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.3f);
                    ctx.actionBar("§7你刚刚是不是咬到空气了？");
                })
                .build());

        // [2]
        TrollEvents.reg(TrollEvent.of("food_taste_weird", "味道不太对", TrollTier.PRANK)
                .desc("提醒你嘴里的东西味道可疑，然后什么都不做")
                .weight(150)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.msg("§7你嘴里的东西……味道有点说不上来。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 0.9f);
                })
                .build());

        // [3]
        TrollEvents.reg(TrollEvent.of("food_hunger_blip", "饥饿条抖了一下", TrollTier.PRANK)
                .desc("饥饿值掉一点又好像没掉，纯属错觉")
                .weight(140)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.feed(ctx.foodLevel() - 1);
                    ctx.actionBar("§7你的胃轻轻抗议了一声。");
                })
                .build());

        // [4]
        TrollEvents.reg(TrollEvent.of("food_bread_crumb", "面包掉了一路渣", TrollTier.PRANK)
                .desc("你走过的地方会掉面包渣，看着就很浪费")
                .weight(130)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.particleAt(ctx.pick(TrollPools.TAUNT_PARTICLES),
                            ctx.x(), ctx.y() + 1, ctx.z(), 8, 0.8);
                    ctx.msg("§7你的面包正在缓慢解体。");
                })
                .build());

        // [5]
        TrollEvents.reg(TrollEvent.of("food_cookie_steal", "曲奇被偷了半块", TrollTier.PRANK)
                .desc("只偷一点点，让你怀疑是不是自己吃太快")
                .weight(130)
                .cooldownSeconds(60)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.msg("§7咦，怎么少了一件？");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.5f);
                })
                .build());

        // [6]
        TrollEvents.reg(TrollEvent.of("food_milk_expired", "牛奶过期了", TrollTier.PRANK)
                .desc("温馨提示：你的牛奶已经过期三天")
                .weight(140)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.sys("提示：背包里的牛奶已过期 3 天（喝了也没事）。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 0.7f);
                })
                .build());

        // [7]
        TrollEvents.reg(TrollEvent.of("food_burp", "当众打嗝", TrollTier.PRANK)
                .desc("一声响亮的饱嗝，附近的人都听得见")
                .weight(140)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.soundToAll(ctx.pick(TrollPools.FUNNY), 0.9f, 0.8f);
                    ctx.actionBar("§7嗝。");
                })
                .build());

        // [8]
        TrollEvents.reg(TrollEvent.of("food_apple_worm", "苹果里有半条虫", TrollTier.PRANK)
                .desc("你发现时已经吃了一半，虫子也是")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.msg("§7你咬到了某种……有弹性的东西。");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.7f, 1.6f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 6);
                })
                .build());

        // [9]
        TrollEvents.reg(TrollEvent.of("food_soup_splash", "汤洒了一身", TrollTier.PRANK)
                .desc("热汤洒在衣服上，视觉上很狼狈")
                .weight(130)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.9f, 0.6f);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 10);
                    ctx.msg("§7你的衣服现在有一股浓汤味。");
                })
                .build());

        // [10]
        TrollEvents.reg(TrollEvent.of("food_honey_sticky", "蜂蜜粘手了", TrollTier.PRANK)
                .desc("手上黏糊糊的，短时间内干什么都不顺手")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.msg("§7你的手黏得像涂了胶水。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 1.1f);
                })
                .build());

        // [11]
        TrollEvents.reg(TrollEvent.of("food_carrot_bunny", "胡萝卜被兔子盯上了", TrollTier.PRANK)
                .desc("附近传来一双小眼睛的动静，胡萝卜开始危险")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.5f, 1.4f);
                    ctx.msg("§7你感觉有什么东西在盯着你的胡萝卜。");
                })
                .build());

        // [12]
        TrollEvents.reg(TrollEvent.of("food_raw_potato", "生土豆的味道", TrollTier.PRANK)
                .desc("一股生土豆的涩味在嘴里散开，纯心理攻击")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.actionBar("§7你的舌头刚刚后悔了一下。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.6f, 0.8f);
                })
                .build());

        // [13]
        TrollEvents.reg(TrollEvent.of("food_berry_stain", "甜浆果染红的手", TrollTier.PRANK)
                .desc("手上一片红，看着像刚干完什么不得了的事")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.msg("§c你的手被浆果染红了。看着挺吓人的。");
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 8);
                })
                .build());

        // [14]
        TrollEvents.reg(TrollEvent.of("food_fake_achievement_eater", "假成就：干饭人", TrollTier.PRANK)
                .desc("假成就弹窗，庆祝你成功吃了一口饭")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.fakeAchievement("干饭人", "在幻觉模组里坚持按时吃饭");
                    ctx.playLevelUp();
                })
                .build());

        // [15]
        TrollEvents.reg(TrollEvent.of("food_cake_candle", "蛋糕上的蜡烛还在烧", TrollTier.PRANK)
                .desc("蛋糕上冒着小火苗，等你想起来已经烧没了")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 8);
                    ctx.msg("§e你的蛋糕上还插着蜡烛。祝谁生日快乐来着？");
                })
                .build());

        // [16]
        TrollEvents.reg(TrollEvent.of("food_golden_apple_taste", "金苹果的回味", TrollTier.PRANK)
                .desc("嘴里突然泛起金苹果味，但你根本没吃")
                .weight(120)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.6f);
                    ctx.msg("§e你嘴里莫名泛起一股金苹果味。");
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 10);
                })
                .build());

        // [17]
        TrollEvents.reg(TrollEvent.of("food_stew_gossip", "可疑的炖菜在说话", TrollTier.PRANK)
                .desc("锅里传来窸窸窣窣的声音，好像在议论你")
                .weight(120)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.6f, 1.2f);
                    ctx.msg("§8炖菜：……他真的打算把这个喝下去吗？");
                })
                .build());

        // [18]
        TrollEvents.reg(TrollEvent.of("food_burnt_toast", "烤糊了", TrollTier.PRANK)
                .desc("一股焦味飘过，你的食物悄悄变成黑炭")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 0.6f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 8);
                    ctx.msg("§8你的食物烤糊了。放弃吧。");
                })
                .build());

        // ==================================================================
        // ANNOYING —— 烦人：真的开始饿、开始难受（32 个）
        // ==================================================================

        // [19]
        TrollEvents.reg(TrollEvent.of("food_hunger_drain", "饥饿值悄悄溜走", TrollTier.ANNOYING)
                .desc("饥饿值无声无息掉三点，你甚至没吃东西")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.feed(ctx.foodLevel() - 3);
                    ctx.msg("§7你的胃发出了一声不太友善的响动。");
                })
                .build());

        // [20]
        TrollEvents.reg(TrollEvent.of("food_saturation_zero", "吃饱是错觉", TrollTier.ANNOYING)
                .desc("饱和度瞬间清零，刚吃的东西好像白吃了")
                .weight(115)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.saturation(0.0f);
                    ctx.warn("你的饱腹感被系统回收了。");
                })
                .build());

        // [21]
        TrollEvents.reg(TrollEvent.of("food_poison_berry", "浆果中毒", TrollTier.ANNOYING)
                .desc("肚子一阵绞痛，开始怀疑那丛浆果")
                .weight(115)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                    ctx.msg("§c你的肚子开始抗议了。");
                })
                .build());

        // [22]
        TrollEvents.reg(TrollEvent.of("food_raw_chicken", "生鸡肉的代价", TrollTier.ANNOYING)
                .desc("饥饿效果悄悄爬上身，谁让你不等熟")
                .weight(115)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.msg("§e你感觉那只鸡在肚子里扑腾。");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.exhaust(4.0f);
                })
                .build());

        // [23]
        TrollEvents.reg(TrollEvent.of("food_rotten_flesh_taste", "腐肉回味", TrollTier.ANNOYING)
                .desc("一阵腐肉味从喉咙里泛上来，挥之不去")
                .weight(115)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.warn("你打了个嗝，味道非常有纪念意义。");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 100, 0);
                })
                .build());

        // [24]
        TrollEvents.reg(TrollEvent.of("food_pufferfish_mishandled", "河豚没处理干净", TrollTier.ANNOYING)
                .desc("嘴开始发麻，视野开始晃，厨师已跑路")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.warn("§c你的嘴唇失去知觉了。");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 1);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 8, 2);
                })
                .build());

        // [25]
        TrollEvents.reg(TrollEvent.of("food_spicy_magma", "岩浆奶油太辣", TrollTier.ANNOYING)
                .desc("辣得你原地起火，还咳出了烟")
                .weight(115)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.setFire(3);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§c辣！辣！辣！");
                })
                .build());

        // [26]
        TrollEvents.reg(TrollEvent.of("food_brain_freeze", "冰镇脑冻结", TrollTier.ANNOYING)
                .desc("一口冰下去，脑子当场罢工")
                .weight(115)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                    ctx.title("§b脑冻结", "§7请扶住墙");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 0.5f);
                })
                .build());

        // [27]
        TrollEvents.reg(TrollEvent.of("food_too_full", "吃撑了走不动", TrollTier.ANNOYING)
                .desc("饱食度拉满但腿变沉了，得不偿失")
                .weight(115)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.feed(20);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 1);
                    ctx.msg("§7你吃得太多，现在连转身都费劲。");
                })
                .build());

        // [28]
        TrollEvents.reg(TrollEvent.of("food_false_famine_warning", "假饥荒警告", TrollTier.ANNOYING)
                .desc("全服公告说食物储备归零，其实只是吓你")
                .weight(105)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e服务器食物储备已归零，请自求多福。");
                    ctx.feed(ctx.foodLevel() - 2);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.7f, 0.9f);
                })
                .build());

        // [29]
        TrollEvents.reg(TrollEvent.of("food_food_theft", "食物被顺走一件", TrollTier.ANNOYING)
                .desc("背包里少了一件东西，地上连渣都没有")
                .weight(115)
                .cooldownSeconds(150)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§7有人（或者什么东西）拿走了你的口粮。");
                })
                .build());

        // [30]
        TrollEvents.reg(TrollEvent.of("food_junk_fill", "背包被塞满垃圾食物", TrollTier.ANNOYING)
                .desc("三件没人要的食材凭空出现在你包里")
                .weight(115)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.fillWith(ctx.pick(TrollPools.JUNK), 3);
                    ctx.msg("§7有人往你包里塞了点『心意』。");
                })
                .build());

        // [31]
        TrollEvents.reg(TrollEvent.of("food_cursed_recipe", "被诅咒的食谱", TrollTier.ANNOYING)
                .desc("你手上的一件东西被改了名字，像被下了咒")
                .weight(110)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.renameRandomItem(ctx.pick(TrollPools.CURSED_NAMES));
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.8f, 0.9f);
                })
                .build());

        // [32]
        TrollEvents.reg(TrollEvent.of("food_moldy_bread", "面包长毛了", TrollTier.ANNOYING)
                .desc("包里多出一块长了绿毛的面包，扔也不是留也不是")
                .weight(115)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.warn("你的面包上出现了一片可疑的绿色。");
                })
                .build());

        // [33]
        TrollEvents.reg(TrollEvent.of("food_mystery_meat", "神秘不明肉", TrollTier.ANNOYING)
                .desc("一块不知道来源的肉，吃了可能会发生点什么")
                .weight(115)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), 2);
                    ctx.effectRandom(120, 0);
                    ctx.msg("§7包装上写着『肉』，仅此而已。");
                })
                .build());

        // [34]
        TrollEvents.reg(TrollEvent.of("food_meal_delay", "食物进入冷却", TrollTier.ANNOYING)
                .desc("吃掉的东西全进了冷却，短时间内吃不了第二口")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.setCooldown(ctx.pick(TrollPools.JUNK), 200);
                    ctx.msg("§7你的消化系统需要排队。");
                })
                .build());

        // [35]
        TrollEvents.reg(TrollEvent.of("food_bad_water", "水有问题", TrollTier.ANNOYING)
                .desc("喝下去的水带着一股怪味，肚子开始翻涌")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.sys("水质检测：不合格。");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                })
                .build());

        // [36]
        TrollEvents.reg(TrollEvent.of("food_farm_trample", "田被踩了", TrollTier.ANNOYING)
                .desc("脚下突然变成一块恶心的东西，像谁在你的田里施了肥")
                .weight(105)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.placeUnder(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.msg("§7你的农田刚刚遭受了一次不明袭击。");
                })
                .build());

        // [37]
        TrollEvents.reg(TrollEvent.of("food_milk_curdle", "牛奶变酸奶", TrollTier.ANNOYING)
                .desc("提示你牛奶已变质，喝了会更糟")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.sys("牛奶已凝固成块状物，建议不要饮用。");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                })
                .build());

        // [38]
        TrollEvents.reg(TrollEvent.of("food_soup_too_salty", "汤太咸了", TrollTier.ANNOYING)
                .desc("咸得你掉体力，还得自己找水喝")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.exhaust(6.0f);
                    ctx.warn("这锅汤用的是一整袋盐吗？");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 0.6f);
                })
                .build());

        // [39]
        TrollEvents.reg(TrollEvent.of("food_cake_is_lie", "蛋糕是个谎", TrollTier.ANNOYING)
                .desc("蛋糕还在，但你已经吃不到它了")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.msg("§7蛋糕还在原处，但你的饥饿值说它不存在。");
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 12);
                    ctx.feed(ctx.foodLevel() - 2);
                })
                .build());

        // [40]
        TrollEvents.reg(TrollEvent.of("food_hunger_meter_broken", "饥饿条坏了", TrollTier.ANNOYING)
                .desc("饥饿条消失六秒，只能凭感觉判断自己饱不饱")
                .weight(105)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.warn("饥饿显示模块异常，请用心感受。");
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 6, 2);
                })
                .build());

        // [41]
        TrollEvents.reg(TrollEvent.of("food_golden_carrot_bait", "金胡萝卜诱饵", TrollTier.ANNOYING)
                .desc("拿到一根金胡萝卜，五秒后它自己消失了")
                .weight(105)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 1);
                    ctx.msg("§e捡到一个好东西！");
                    ctx.later(100, () -> {
                        ctx.takeRandomItem();
                        ctx.msg("§7……刚才那个呢？");
                    });
                })
                .build());

        // [42]
        TrollEvents.reg(TrollEvent.of("food_steak_moo", "牛排发出牛叫", TrollTier.ANNOYING)
                .desc("那块牛排叫了一声，你还得装作没听见")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 0.7f);
                    ctx.msg("§8哞——（来自你的背包）");
                })
                .build());

        // [43]
        TrollEvents.reg(TrollEvent.of("food_bread_rising", "面包还在发酵", TrollTier.ANNOYING)
                .desc("包里的面团正在膨胀，随时可能把你的背包撑开")
                .weight(105)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.msg("§7你的面包正在缓慢变大。真的。");
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 10);
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                })
                .build());

        // [44]
        TrollEvents.reg(TrollEvent.of("food_apple_falls", "苹果从天而降", TrollTier.ANNOYING)
                .desc("一颗苹果精准砸在你头上，牛顿看了都摇头")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.damage("magic", 1.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.8f, 1.4f);
                    ctx.msg("§7一颗苹果完成了它的使命。");
                })
                .build());

        // [45]
        TrollEvents.reg(TrollEvent.of("food_berry_bush_trap", "甜浆果丛", TrollTier.ANNOYING)
                .desc("脚下冒出一丛甜浆果，踩上去会一直掉血")
                .weight(105)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.placeUnder(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.warn("你踩进了一丛甜浆果。");
                })
                .build());

        // [46]
        TrollEvents.reg(TrollEvent.of("food_sugar_rush", "糖分上头", TrollTier.ANNOYING)
                .desc("先是健步如飞，然后付出代价")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 120, 1);
                    ctx.msg("§e你感觉现在能跑赢一匹马。");
                    ctx.later(140, () -> {
                        ctx.exhaust(10.0f);
                        ctx.msg("§7……然后糖分崩了。");
                    });
                })
                .build());

        // [47]
        TrollEvents.reg(TrollEvent.of("food_caffeine_overload", "咖啡因过载", TrollTier.ANNOYING)
                .desc("手抖、心跳快、还得承受随后的崩溃")
                .weight(110)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 160, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.8f);
                    ctx.later(180, () -> ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 140, 0));
                })
                .build());

        // [48]
        TrollEvents.reg(TrollEvent.of("food_expired_can", "过期罐头", TrollTier.ANNOYING)
                .desc("罐头上的日期比你建号还早，你还是打开了")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.warn("你打开了一罐……历史。");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 140, 0);
                })
                .build());

        // [49]
        TrollEvents.reg(TrollEvent.of("food_mushroom_roulette", "蘑菇轮盘", TrollTier.ANNOYING)
                .desc("这一口是补还是毒，全看运气")
                .weight(110)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.msg("§7你吃下了一朵颜色很鲜艳的蘑菇。");
                    ctx.effectRandom(200, 0);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.7f, 1.3f);
                })
                .build());

        // [50]
        TrollEvents.reg(TrollEvent.of("food_cheese_dream", "奶酪梦", TrollTier.ANNOYING)
                .desc("睡前吃奶酪的后果：做梦都在被追")
                .weight(105)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.title("§e梦到奶酪了", "§7但你才是那块奶酪");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.6f, 0.9f);
                })
                .build());

        // ==================================================================
        // BRUTAL —— 抓狂：真的饿、真的掉血、真的损失（27 个）
        // ==================================================================

        // [51]
        TrollEvents.reg(TrollEvent.of("food_starve_hard", "直接饿到见底", TrollTier.BRUTAL)
                .desc("饥饿值和饱和度被一起清空，胃部遭到重击")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.starve();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.warn("§c你的胃发出了最后通牒。");
                })
                .build());

        // [52]
        TrollEvents.reg(TrollEvent.of("food_poison_feast", "中毒盛宴", TrollTier.BRUTAL)
                .desc("「中毒」两个字写在菜单上，你还是吃了")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.damage("magic", 2.0f);
                    ctx.msg("§c这顿饭的代价开始结算了。");
                })
                .build());

        // [53]
        TrollEvents.reg(TrollEvent.of("food_rotten_everything", "全部变成腐肉", TrollTier.BRUTAL)
                .desc("五件腐肉凭空塞进包里，背包瞬间有了味道")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.fillWith(ctx.pick(TrollPools.JUNK), 5);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.warn("你的背包现在散发着一种气味。");
                })
                .build());

        // [54]
        TrollEvents.reg(TrollEvent.of("food_meal_reversal", "吃进去的又出来了", TrollTier.BRUTAL)
                .desc("饥饿清零，脚下多出一摊说不清的东西")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.starve();
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.5f);
                    ctx.msg("§7刚才那顿饭……不算数了。");
                })
                .build());

        // [55]
        TrollEvents.reg(TrollEvent.of("food_cursed_cake", "被诅咒的蛋糕", TrollTier.BRUTAL)
                .desc("假成就加随机负面效果，甜得发苦")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    ctx.fakeAchievement("甜蜜的诅咒", "吃掉了一整个不该吃的蛋糕");
                    ctx.effectRandom(300, 1);
                    ctx.feed(20);
                })
                .build());

        // [56]
        TrollEvents.reg(TrollEvent.of("food_explosive_pepper", "爆炸辣椒", TrollTier.BRUTAL)
                .desc("辣到原地爆炸，还带火")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    ctx.boomAtPlayer(2.0f);
                    ctx.setFire(4);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§c这辣椒是火药做的吧！");
                })
                .build());

        // [57]
        TrollEvents.reg(TrollEvent.of("food_lava_soup", "岩浆浓汤", TrollTier.BRUTAL)
                .desc("脚下被铺开一层岩浆，这才是真正的重口味")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.lavaPit(1);
                    ctx.setFire(3);
                    ctx.warn("§6你点的岩浆浓汤上桌了。");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                })
                .build());

        // [58]
        TrollEvents.reg(TrollEvent.of("food_bone_bread", "面包里有骨头", TrollTier.BRUTAL)
                .desc("咬到硬东西的代价是掉血和一颗坏牙")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.damage("magic", 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§c你咬到了一根骨头。还是别人的。");
                })
                .build());

        // [59]
        TrollEvents.reg(TrollEvent.of("food_gluttony", "暴饮暴食", TrollTier.BRUTAL)
                .desc("饱食度拉满但换来强烈的沉重感")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    ctx.feed(20);
                    ctx.saturation(0.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 2);
                    ctx.exhaust(12.0f);
                })
                .build());

        // [60]
        TrollEvents.reg(TrollEvent.of("food_famine", "饥荒降临", TrollTier.BRUTAL)
                .desc("强制饥饿加长时间负面效果，肚子彻底空了")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.title("§6饥荒", "§7你的胃已经放弃了");
                    ctx.starve();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                })
                .build());

        // [61]
        TrollEvents.reg(TrollEvent.of("food_food_theft_all", "口粮被洗劫", TrollTier.BRUTAL)
                .desc("连着三件东西不翼而飞，连渣都没剩")
                .weight(55)
                .cooldownSeconds(330)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.takeRandomItem();
                    }
                    ctx.warn("§c你的口粮被洗劫了。");
                })
                .build());

        // [62]
        TrollEvents.reg(TrollEvent.of("food_raw_meat_curse", "生肉诅咒", TrollTier.BRUTAL)
                .desc("生肉吃多了，身上开始出现奇怪的反应")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), 2);
                    ctx.effectRandom(300, 1);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 18);
                })
                .build());

        // [63]
        TrollEvents.reg(TrollEvent.of("food_mold_spread", "霉斑扩散", TrollTier.BRUTAL)
                .desc("身边的地面被一片恶心的方块占领")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.NASTY_BLOCKS), 6);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.6f);
                    ctx.msg("§2霉斑正在你周围生长。");
                })
                .build());

        // [64]
        TrollEvents.reg(TrollEvent.of("food_kitchen_fire", "厨房起火", TrollTier.BRUTAL)
                .desc("锅里起火，火苗顺着地面蔓延过来")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.setFire(5);
                    ctx.scatterBlocks(ctx.pick(TrollPools.TRAP_BLOCKS), 4);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.warn("§c你的厨房起火了。");
                })
                .build());

        // [65]
        TrollEvents.reg(TrollEvent.of("food_potion_mixup", "药水配错了", TrollTier.BRUTAL)
                .desc("喝下去才发现配方拿反了，效果随机且猛烈")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    ctx.effectRandom(300, 2);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.msg("§d这瓶药水的颜色有点太鲜艳了。");
                })
                .build());

        // [66]
        TrollEvents.reg(TrollEvent.of("food_bad_milk", "变质牛奶", TrollTier.BRUTAL)
                .desc("一口闷下去，中毒加眩晕一起发作")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 8, 2);
                })
                .build());

        // [67]
        TrollEvents.reg(TrollEvent.of("food_meat_swarm", "生肉引来怪物", TrollTier.BRUTAL)
                .desc("血腥味把不该来的东西招来了")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.title("§4它们闻到味道了", "§7下次别带生肉");
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                })
                .build());

        // [68]
        TrollEvents.reg(TrollEvent.of("food_sugar_crash", "糖分崩塌", TrollTier.BRUTAL)
                .desc("先是精力充沛，紧接着整个人被抽空")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 100, 2);
                    ctx.later(120, () -> {
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 2);
                        ctx.exhaust(14.0f);
                        ctx.msg("§7糖分崩塌，你连胳膊都不想抬。");
                    });
                })
                .build());

        // [69]
        TrollEvents.reg(TrollEvent.of("food_cake_bomb", "蛋糕炸弹", TrollTier.BRUTAL)
                .desc("插着蜡烛的蛋糕其实是引信")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x() + 1, ctx.y() + 1, ctx.z() + 1, 40);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§c生日快乐！");
                })
                .build());

        // [70]
        TrollEvents.reg(TrollEvent.of("food_hunger_swap", "饱食与虚弱互换", TrollTier.BRUTAL)
                .desc("吃饱了，但身体软得像面条")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    ctx.feed(20);
                    ctx.saturation(0.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 0);
                })
                .build());

        // [71]
        TrollEvents.reg(TrollEvent.of("food_digestive_chaos", "消化系统罢工", TrollTier.BRUTAL)
                .desc("恶心、饥饿、虚弱三件套一起上")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.warn("§c你的消化系统已提交辞职信。");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.exhaust(16.0f);
                    ctx.starve();
                })
                .build());

        // [72]
        TrollEvents.reg(TrollEvent.of("food_poison_broadcast", "全服食物中毒", TrollTier.BRUTAL)
                .desc("所有人都吃到同一种不明效果，公告还公开点名")
                .weight(55)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e服务器食堂今日菜单：可疑的炖菜。");
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.soundToAll(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                })
                .build());

        // [73]
        TrollEvents.reg(TrollEvent.of("food_taunt_meal", "吃饭被嘲讽", TrollTier.BRUTAL)
                .desc("吃东西的时候被怪物当面嘲笑，还附带负面效果")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), ctx.pick(TrollPools.TAUNTS));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 220, 1);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                })
                .build());

        // [74]
        TrollEvents.reg(TrollEvent.of("food_rotten_banquet", "腐肉宴", TrollTier.BRUTAL)
                .desc("六件腐肉加长时间饥饿，这顿饭注定难忘")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.fillWith(ctx.pick(TrollPools.JUNK), 6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.msg("§2宴席已备好，请慢用。");
                })
                .build());

        // [75]
        TrollEvents.reg(TrollEvent.of("food_cursed_steak", "诅咒牛排", TrollTier.BRUTAL)
                .desc("吃下去会慢慢腐烂，痛感真实且持久")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.damage("wither", 2.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 0.7f);
                })
                .build());

        // [76]
        TrollEvents.reg(TrollEvent.of("food_belly_bomb", "肚子里的炸弹", TrollTier.BRUTAL)
                .desc("肚子里传来滴答声，五秒后真的有东西炸了")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x(), ctx.y() + 2, ctx.z(), 50);
                    ctx.msg("§c……滴答。滴答。");
                    ctx.later(80, () -> ctx.boomAtPlayer(2.0f));
                })
                .build());

        // [77]
        TrollEvents.reg(TrollEvent.of("food_expired_potion", "过期药水", TrollTier.BRUTAL)
                .desc("喝下过期药水后效果完全随机，剂量还特别大")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.effectRandom(400, 3);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 10, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.6f);
                })
                .build());

        // ==================================================================
        // NIGHTMARE —— 崩溃：饿到绝望、吃到吐血（15 个）
        // ==================================================================

        // [78]
        TrollEvents.reg(TrollEvent.of("food_endless_hunger", "永远吃不饱", TrollTier.NIGHTMARE)
                .desc("饥饿清零且长时间无法恢复，吃什么都像没吃")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§6§l你永远吃不饱", "§7胃是无底洞");
                    ctx.starve();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                    ctx.exhaust(20.0f);
                })
                .build());

        // [79]
        TrollEvents.reg(TrollEvent.of("food_poison_apocalypse", "剧毒盛宴", TrollTier.NIGHTMARE)
                .desc("中毒加凋零，痛感一波接一波")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.damage("wither", 3.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                })
                .build());

        // [80]
        TrollEvents.reg(TrollEvent.of("food_food_void", "食物被虚空吞了", TrollTier.NIGHTMARE)
                .desc("五件口粮被一张看不见的嘴吃掉了")
                .weight(30)
                .cooldownSeconds(600)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.takeRandomItem();
                    }
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 30);
                    ctx.warn("§8虚空打了个嗝。");
                })
                .build());

        // [81]
        TrollEvents.reg(TrollEvent.of("food_rotten_realm", "腐化领域", TrollTier.NIGHTMARE)
                .desc("周围的一切都在腐烂，连怪物都被味道吸引过来")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§2§l一切都在腐烂", "§7包括你的午饭");
                    ctx.scatterBlocks(ctx.pick(TrollPools.NASTY_BLOCKS), 8);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                })
                .build());

        // [82]
        TrollEvents.reg(TrollEvent.of("food_cannibal_feast", "同类相食", TrollTier.NIGHTMARE)
                .desc("你获得了不该获得的『食材』，还附带心理创伤")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§4§l这不太合适", "§8但你已经吃了");
                    ctx.give(ctx.pick(TrollPools.JUNK), 3);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                })
                .build());

        // [83]
        TrollEvents.reg(TrollEvent.of("food_blood_soup", "血色浓汤", TrollTier.NIGHTMARE)
                .desc("汤是红的，你的血也是")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.damage("magic", 3.0f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.msg("§c这碗汤的颜色……有点太正了。");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                })
                .build());

        // [84]
        TrollEvents.reg(TrollEvent.of("food_starvation_death", "假饿死", TrollTier.NIGHTMARE)
                .desc("假死亡界面加真实饥饿伤害，虚实难分")
                .weight(40)
                .cooldownSeconds(540)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.fakeDeathScreen("饥饿而死");
                    ctx.damage("starve", 2.0f);
                    ctx.starve();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                })
                .build());

        // [85]
        TrollEvents.reg(TrollEvent.of("food_curse_of_gluttony", "暴食诅咒", TrollTier.NIGHTMARE)
                .desc("饱食度拉满却换来一整套随机诅咒")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.fakeAchievement("暴食之罪", "把所有人的口粮都吃了");
                    ctx.feed(20);
                    ctx.saturation(0.0f);
                    ctx.effectRandom(500, 2);
                })
                .build());

        // [86]
        TrollEvents.reg(TrollEvent.of("food_meal_of_despair", "绝望之餐", TrollTier.NIGHTMARE)
                .desc("手里的食物被扔了一地，饥饿感还在")
                .weight(35)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.title("§8§l绝望之餐", "§7餐具已就位，食物没有");
                    ctx.dropHeld();
                    ctx.dropRandom();
                    ctx.starve();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                })
                .build());

        // [87]
        TrollEvents.reg(TrollEvent.of("food_poison_well", "水井被投毒", TrollTier.NIGHTMARE)
                .desc("全服同时中毒，公告还若无其事")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e公共水源已完成例行维护。");
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                })
                .build());

        // [88]
        TrollEvents.reg(TrollEvent.of("food_eternal_thirst", "永恒干渴", TrollTier.NIGHTMARE)
                .desc("饥饿长时间压顶，体力像被抽水机抽走")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§6§l渴", "§7没有水，只有更饿");
                    ctx.starve();
                    ctx.exhaust(20.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                })
                .build());

        // [89]
        TrollEvents.reg(TrollEvent.of("food_ghost_chef", "幽灵厨师", TrollTier.NIGHTMARE)
                .desc("一个看不见的厨师给你上了菜，菜是凉的")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§8§l主厨已上菜", "§7主厨没有实体");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.give(ctx.pick(TrollPools.JUNK), 3);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                })
                .build());

        // [90]
        TrollEvents.reg(TrollEvent.of("food_cursed_buffet", "诅咒自助餐", TrollTier.NIGHTMARE)
                .desc("随便吃，但每一口都带着代价，餐具上还有字")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.fillWith(ctx.pick(TrollPools.JUNK), 4);
                    ctx.renameRandomItem(ctx.pick(TrollPools.CURSED_NAMES));
                    ctx.effectRandom(450, 2);
                    ctx.warn("§c自助餐规则：吃多少，还多少。");
                })
                .build());

        // [91]
        TrollEvents.reg(TrollEvent.of("food_meat_hook", "肉钩", TrollTier.NIGHTMARE)
                .desc("被一股力量吊起来，落地时已经掉了不少血")
                .weight(35)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.damage("magic", 4.0f);
                    ctx.lift(8);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                })
                .build());

        // [92]
        TrollEvents.reg(TrollEvent.of("food_wasting_away", "逐渐消瘦", TrollTier.NIGHTMARE)
                .desc("饥饿连绵不断，血条一点点被磨掉")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.starve();
                    ctx.damage("starve", 4.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 500, 2);
                    ctx.msg("§7你感觉自己在慢慢变薄。");
                })
                .build());

        // ==================================================================
        // APOCALYPSE —— 地狱：这顿饭吃完就没命了（7 个）
        // ==================================================================

        // [93]
        TrollEvents.reg(TrollEvent.of("food_great_famine", "大饥荒", TrollTier.APOCALYPSE)
                .desc("全服饥饿清零并长时间无法恢复，真正的天灾")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.fakeBroadcast("§4警告：世界食物产出已归零。");
                    ctx.title("§6§l大饥荒", "§7这一次没有下一顿");
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 800, 2);
                    ctx.starve();
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                })
                .build());

        // [94]
        TrollEvents.reg(TrollEvent.of("food_poison_banquet", "毒宴", TrollTier.APOCALYPSE)
                .desc("全服剧毒加凋零，餐桌上没有一道菜是安全的")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.fakeBroadcast("§4宴会开始，请各位就座。");
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                    ctx.damage("wither", 4.0f);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                })
                .build());

        // [95]
        TrollEvents.reg(TrollEvent.of("food_hunger_plague", "饥饿瘟疫", TrollTier.APOCALYPSE)
                .desc("饥饿蔓延到所有人，同时把怪物引到每个人身边")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.title("§4§l瘟疫席卷", "§7饥饿也是一种传染病");
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 800, 2);
                    ctx.starve();
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                })
                .build());

        // [96]
        TrollEvents.reg(TrollEvent.of("food_last_supper", "最后的晚餐", TrollTier.APOCALYPSE)
                .desc("先给你吃饱，再在十秒后把一切收走")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.feed(20);
                    ctx.msg("§e这顿饭，请慢慢享用。");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.later(200, () -> {
                        ctx.fakeDeathScreen("最后的晚餐吃完了");
                        ctx.starve();
                        ctx.damage("starve", 5.0f);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    });
                })
                .build());

        // [97]
        TrollEvents.reg(TrollEvent.of("food_belly_explosion", "肚子爆炸", TrollTier.APOCALYPSE)
                .desc("吃下去的东西在体内发生了连锁反应")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.title("§4§l消化不良", "§c非常严重的那种");
                    ctx.boomAtPlayer(4.0f);
                    ctx.spawnTnt(ctx.x(), ctx.y() + 1, ctx.z(), 30);
                    ctx.damage("magic", 5.0f);
                    ctx.setFire(5);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                })
                .build());

        // [98]
        TrollEvents.reg(TrollEvent.of("food_void_meal", "虚空之餐", TrollTier.APOCALYPSE)
                .desc("连背包带人都被虚空惦记上了，脚下还裂开一个洞")
                .weight(16)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.title("§8§l虚空饿了", "§7它点的是你");
                    ctx.dropEverything();
                    ctx.voidPit(1, 4);
                    ctx.damage("outofworld", 5.0f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                })
                .build());

        // [99]
        TrollEvents.reg(TrollEvent.of("food_everything_food", "一切与食物有关的一起上", TrollTier.APOCALYPSE)
                .desc("愤怒值爆表时，把所有食物整活无差别砸下来")
                .weight(16)
                .cooldownSeconds(900)
                .when(ctx -> ctx.rage() >= 80.0)
                .run(ctx -> {
                    ctx.title("§4§l你惹到食堂了", "§c接下来请自己保重");
                    ctx.fakeBroadcast("§4今日特供：所有菜一起上。");
                    ctx.starve();
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                    ctx.fillWith(ctx.pick(TrollPools.JUNK), 6);
                    ctx.dropRandom();
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                })
                .build());
    }
}
