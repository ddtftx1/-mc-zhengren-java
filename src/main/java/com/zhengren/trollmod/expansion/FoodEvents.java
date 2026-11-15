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
        TrollEvents.reg(TrollEvent.of("food_fake_bite", "空气咬了一口", TrollTier.BRUTAL)
                .desc("清脆的咀嚼声响起，但你手里什么都没有")
                .weight(150)
                .cooldownSeconds(40)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.3f);
                    ctx.actionBar("§7你刚刚是不是咬到空气了？");
                })
                .build());

        // [2]
        TrollEvents.reg(TrollEvent.of("food_taste_weird", "味道不太对", TrollTier.ANNOYING)
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
        TrollEvents.reg(TrollEvent.of("food_bread_crumb", "面包掉了一路渣", TrollTier.BRUTAL)
                .desc("你走过的地方会掉面包渣，看着就很浪费")
                .weight(130)
                .cooldownSeconds(50)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.particleAt(ctx.pick(TrollPools.TAUNT_PARTICLES),
                            ctx.x(), ctx.y() + 1, ctx.z(), 8, 0.8);
                    ctx.msg("§7你的面包正在缓慢解体。");
                })
                .build());

        // [5]
        TrollEvents.reg(TrollEvent.of("food_cookie_steal", "曲奇被偷了半块", TrollTier.BRUTAL)
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
        TrollEvents.reg(TrollEvent.of("food_milk_expired", "牛奶过期了", TrollTier.BRUTAL)
                .desc("温馨提示：你的牛奶已经过期三天")
                .weight(140)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.sys("提示：背包里的牛奶已过期 3 天（喝了也没事）。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 0.7f);
                })
                .build());

        // [7]
        TrollEvents.reg(TrollEvent.of("food_burp", "当众打嗝", TrollTier.BRUTAL)
                .desc("一声响亮的饱嗝，附近的人都听得见")
                .weight(140)
                .cooldownSeconds(50)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.soundToAll(ctx.pick(TrollPools.FUNNY), 0.9f, 0.8f);
                    ctx.actionBar("§7嗝。");
                })
                .build());

        // [8]
        TrollEvents.reg(TrollEvent.of("food_apple_worm", "苹果里有半条虫", TrollTier.ANNOYING)
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
        TrollEvents.reg(TrollEvent.of("food_soup_splash", "汤洒了一身", TrollTier.BRUTAL)
                .desc("热汤洒在衣服上，视觉上很狼狈")
                .weight(130)
                .cooldownSeconds(55)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.9f, 0.6f);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 10);
                    ctx.msg("§7你的衣服现在有一股浓汤味。");
                })
                .build());

        // [10]
        TrollEvents.reg(TrollEvent.of("food_honey_sticky", "蜂蜜粘手了", TrollTier.ANNOYING)
                .desc("手上黏糊糊的，短时间内干什么都不顺手")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.msg("§7你的手黏得像涂了胶水。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 1.1f);
                })
                .build());

        // [11]
        TrollEvents.reg(TrollEvent.of("food_carrot_bunny", "胡萝卜被兔子盯上了", TrollTier.BRUTAL)
                .desc("附近传来一双小眼睛的动静，胡萝卜开始危险")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.5f, 1.4f);
                    ctx.msg("§7你感觉有什么东西在盯着你的胡萝卜。");
                })
                .build());

        // [12]
        TrollEvents.reg(TrollEvent.of("food_raw_potato", "生土豆的味道", TrollTier.BRUTAL)
                .desc("一股生土豆的涩味在嘴里散开，纯心理攻击")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.actionBar("§7你的舌头刚刚后悔了一下。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.6f, 0.8f);
                })
                .build());

        // [13]
        TrollEvents.reg(TrollEvent.of("food_berry_stain", "甜浆果染红的手", TrollTier.BRUTAL)
                .desc("手上一片红，看着像刚干完什么不得了的事")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.msg("§c你的手被浆果染红了。看着挺吓人的。");
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 8);
                })
                .build());

        // [14]
        TrollEvents.reg(TrollEvent.of("food_fake_achievement_eater", "假成就：干饭人", TrollTier.BRUTAL)
                .desc("假成就弹窗，庆祝你成功吃了一口饭")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.fakeAchievement("干饭人", "在幻觉模组里坚持按时吃饭");
                    ctx.playLevelUp();
                })
                .build());

        // [15]
        TrollEvents.reg(TrollEvent.of("food_cake_candle", "蛋糕上的蜡烛还在烧", TrollTier.BRUTAL)
                .desc("蛋糕上冒着小火苗，等你想起来已经烧没了")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 8);
                    ctx.msg("§e你的蛋糕上还插着蜡烛。祝谁生日快乐来着？");
                })
                .build());

        // [16]
        TrollEvents.reg(TrollEvent.of("food_golden_apple_taste", "金苹果的回味", TrollTier.ANNOYING)
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
        TrollEvents.reg(TrollEvent.of("food_stew_gossip", "可疑的炖菜在说话", TrollTier.BRUTAL)
                .desc("锅里传来窸窸窣窣的声音，好像在议论你")
                .weight(120)
                .cooldownSeconds(80)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.6f, 1.2f);
                    ctx.msg("§8炖菜：……他真的打算把这个喝下去吗？");
                })
                .build());

        // [18]
        TrollEvents.reg(TrollEvent.of("food_burnt_toast", "烤糊了", TrollTier.BRUTAL)
                .desc("一股焦味飘过，你的食物悄悄变成黑炭")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 0.6f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 8);
                    ctx.msg("§8你的食物烤糊了。放弃吧。");
                })
                .build());

        // ==================================================================
        // ANNOYING —— 烦人：真的开始饿、开始难受（32 个）
        // ==================================================================

        // [19]
        TrollEvents.reg(TrollEvent.of("food_hunger_drain", "饥饿值悄悄溜走", TrollTier.NIGHTMARE)
                .desc("饥饿值无声无息地掉。你甚至没吃东西，胃却先空了。")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.feed(ctx.foodLevel() - 3);
                    ctx.msg("§7你的胃发出了一声不太友善的响动。");
                })
                .build());

        // [20]
        TrollEvents.reg(TrollEvent.of("food_saturation_zero", "吃饱是错觉", TrollTier.NIGHTMARE)
                .desc("饱和度瞬间清零。刚才那一顿，像是你替别人吃的。")
                .weight(115)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
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
        TrollEvents.reg(TrollEvent.of("food_raw_chicken", "生肉的代价", TrollTier.NIGHTMARE)
                .desc("饥饿效果悄悄爬上来。你只是饿，现在还要为吃这件事负责。")
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
        TrollEvents.reg(TrollEvent.of("food_pufferfish_mishandled", "河豚没处理干净", TrollTier.BRUTAL)
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
        TrollEvents.reg(TrollEvent.of("food_spicy_magma", "岩浆奶油太辣", TrollTier.BRUTAL)
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
        TrollEvents.reg(TrollEvent.of("food_brain_freeze", "冰镇脑冻结", TrollTier.BRUTAL)
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
        TrollEvents.reg(TrollEvent.of("food_too_full", "吃撑了走不动", TrollTier.BRUTAL)
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
        TrollEvents.reg(TrollEvent.of("food_food_theft", "食物被顺走一件", TrollTier.NIGHTMARE)
                .desc("背包里少了一件东西，连渣都没留下。你不知道它去了谁那里。")
                .weight(115)
                .cooldownSeconds(150)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.takeRandomItem();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§7有人（或者什么东西）拿走了你的口粮。");
                })
                .build());

        // [30]
        TrollEvents.reg(TrollEvent.of("food_junk_fill", "背包被塞满垃圾食物", TrollTier.NIGHTMARE)
                .desc("三件没人要的食材出现在你包里。你的背包被当成了垃圾场。")
                .weight(115)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.fillWith(ctx.pick(TrollPools.JUNK), 3);
                    ctx.msg("§7有人往你包里塞了点『心意』。");
                })
                .build());

        // [31]
        TrollEvents.reg(TrollEvent.of("food_cursed_recipe", "被诅咒的食谱", TrollTier.BRUTAL)
                .desc("你手上的一件东西被改了名字，像被下了咒")
                .weight(110)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
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
        TrollEvents.reg(TrollEvent.of("food_mystery_meat", "神秘不明肉", TrollTier.BRUTAL)
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
        TrollEvents.reg(TrollEvent.of("food_bad_water", "水有问题", TrollTier.NIGHTMARE)
                .desc("喝下去的水带着一股说不清的味道，肚子从里面开始翻涌。")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.sys("水质检测：不合格。");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                })
                .build());

        // [36]
        TrollEvents.reg(TrollEvent.of("food_farm_trample", "田被踩了", TrollTier.BRUTAL)
                .desc("脚下突然变成一块恶心的东西，像谁在你的田里施了肥")
                .weight(105)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
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
        TrollEvents.reg(TrollEvent.of("food_soup_too_salty", "汤太咸了", TrollTier.BRUTAL)
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
        TrollEvents.reg(TrollEvent.of("food_hunger_meter_broken", "饥饿条坏了", TrollTier.BRUTAL)
                .desc("饥饿条消失六秒，只能凭感觉判断自己饱不饱")
                .weight(105)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.warn("饥饿显示模块异常，请用心感受。");
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 6, 2);
                })
                .build());

        // [41]
        TrollEvents.reg(TrollEvent.of("food_golden_carrot_bait", "金胡萝卜诱饵", TrollTier.NIGHTMARE)
                .desc("拿到一根金胡萝卜。五秒后它自己消失，像是从没打算给你。")
                .weight(105)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.give(ctx.pick(TrollPools.LOOT), 1);
                    ctx.msg("§e捡到一个好东西！");
                    ctx.later(100, () -> {
                        ctx.takeRandomItem();
                        ctx.msg("§7……刚才那个呢？");
                    });
                })
                .build());

        // [42]
        TrollEvents.reg(TrollEvent.of("food_steak_moo", "牛排叫了一声", TrollTier.NIGHTMARE)
                .desc("那块牛排叫了一声。你听见了，而它还冒着热气。")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 0.7f);
                    ctx.msg("§8哞——（来自你的背包）");
                })
                .build());

        // [43]
        TrollEvents.reg(TrollEvent.of("food_bread_rising", "面包还在发酵", TrollTier.BRUTAL)
                .desc("包里的面团正在膨胀，随时可能把你的背包撑开")
                .weight(105)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.msg("§7你的面包正在缓慢变大。真的。");
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 10);
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                })
                .build());

        // [44]
        TrollEvents.reg(TrollEvent.of("food_apple_falls", "苹果从天而降", TrollTier.BRUTAL)
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
        TrollEvents.reg(TrollEvent.of("food_berry_bush_trap", "甜浆果丛", TrollTier.BRUTAL)
                .desc("脚下冒出一丛甜浆果，踩上去会一直掉血")
                .weight(105)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.placeUnder(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.warn("你踩进了一丛甜浆果。");
                })
                .build());

        // [46]
        TrollEvents.reg(TrollEvent.of("food_sugar_rush", "糖分上头", TrollTier.NIGHTMARE)
                .desc("先是健步如飞，然后身体开始连本带利地讨还。")
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
        TrollEvents.reg(TrollEvent.of("food_caffeine_overload", "咖啡因过载", TrollTier.BRUTAL)
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
        TrollEvents.reg(TrollEvent.of("food_expired_can", "过期罐头", TrollTier.BRUTAL)
                .desc("罐头上的日期比你建号还早，你还是打开了")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.warn("你打开了一罐……历史。");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 140, 0);
                })
                .build());

        // [49]
        TrollEvents.reg(TrollEvent.of("food_mushroom_roulette", "蘑菇轮盘", TrollTier.BRUTAL)
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
        TrollEvents.reg(TrollEvent.of("food_cheese_dream", "奶酪之后", TrollTier.NIGHTMARE)
                .desc("睡前那块奶酪的后果：梦里有人在追你，醒来还没停。")
                .weight(105)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.title("§e梦到奶酪了", "§7但你才是那块奶酪");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.6f, 0.9f);
                })
                .build());

        // ==================================================================
        // BRUTAL —— 抓狂：真的饿、真的掉血、真的损失（27 个）
        // ==================================================================

        // [51]
        TrollEvents.reg(TrollEvent.of("food_starve_hard", "饿到见底", TrollTier.NIGHTMARE)
                .desc("饥饿和饱食被一起清空。胃部传来一次很闷的钝击。")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.starve();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.warn("§c你的胃发出了最后通牒。");
                })
                .build());

        // [52]
        TrollEvents.reg(TrollEvent.of("food_poison_feast", "中毒写在菜单上", TrollTier.APOCALYPSE)
                .desc("「中毒」两个字就写在菜单上。你还是吃了——这才是最难受的。")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.damage("magic", 2.0f);
                    ctx.msg("§c这顿饭的代价开始结算了。");
                })
                .build());

        // [53]
        TrollEvents.reg(TrollEvent.of("food_rotten_everything", "全部变成腐肉", TrollTier.APOCALYPSE)
                .desc("五件腐肉凭空塞进包里，从此多了一股洗不掉的味道。")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.fillWith(ctx.pick(TrollPools.JUNK), 5);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.warn("你的背包现在散发着一种气味。");
                })
                .build());

        // [54]
        TrollEvents.reg(TrollEvent.of("food_meal_reversal", "吃进去的又出来了", TrollTier.NIGHTMARE)
                .desc("饥饿清零，脚下多出一摊说不清的东西。它刚刚还在你胃里。")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.starve();
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.5f);
                    ctx.msg("§7刚才那顿饭……不算数了。");
                })
                .build());

        // [55]
        TrollEvents.reg(TrollEvent.of("food_cursed_cake", "被诅咒的蛋糕", TrollTier.NIGHTMARE)
                .desc("假成就配随机负面效果。甜味留在嘴里，代价留在身上。")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.fakeAchievement("甜蜜的诅咒", "吃掉了一整个不该吃的蛋糕");
                    ctx.effectRandom(300, 1);
                    ctx.feed(20);
                })
                .build());

        // [56]
        TrollEvents.reg(TrollEvent.of("food_explosive_pepper", "爆炸辣椒", TrollTier.NIGHTMARE)
                .desc("辣到原地炸开，火从嘴里一直烧到脚下。")
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
        TrollEvents.reg(TrollEvent.of("food_lava_soup", "岩浆浓汤", TrollTier.NIGHTMARE)
                .desc("脚下被铺开一层岩浆。这才是这道汤真正的温度。")
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
        TrollEvents.reg(TrollEvent.of("food_bone_bread", "面包里有骨头", TrollTier.NIGHTMARE)
                .desc("咬到硬东西的代价：掉血，以及一颗再也长不回来的牙。")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.damage("magic", 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§c你咬到了一根骨头。还是别人的。");
                })
                .build());

        // [59]
        TrollEvents.reg(TrollEvent.of("food_gluttony", "暴饮暴食", TrollTier.APOCALYPSE)
                .desc("饱食度被拉满，换来的是灌了铅一样的沉重。")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.feed(20);
                    ctx.saturation(0.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 2);
                    ctx.exhaust(12.0f);
                })
                .build());

        // [60]
        TrollEvents.reg(TrollEvent.of("food_famine", "饥荒降临", TrollTier.APOCALYPSE)
                .desc("强制饥饿加长时间负面效果。肚子空了，而且看不到尽头。")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.title("§6饥荒", "§7你的胃已经放弃了");
                    ctx.starve();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                })
                .build());

        // [61]
        TrollEvents.reg(TrollEvent.of("food_food_theft_all", "口粮被洗劫", TrollTier.APOCALYPSE)
                .desc("连着三件东西不翼而飞。你甚至没能看清是哪一秒。")
                .weight(55)
                .cooldownSeconds(330)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    for (int i = 0; i < 3; i++) {
                        ctx.takeRandomItem();
                    }
                    ctx.warn("§c你的口粮被洗劫了。");
                })
                .build());

        // [62]
        TrollEvents.reg(TrollEvent.of("food_raw_meat_curse", "生肉诅咒", TrollTier.APOCALYPSE)
                .desc("生肉吃多了，身上开始出现不该有的反应。")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.give(ctx.pick(TrollPools.JUNK), 2);
                    ctx.effectRandom(300, 1);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 18);
                })
                .build());

        // [63]
        TrollEvents.reg(TrollEvent.of("food_mold_spread", "霉斑扩散", TrollTier.APOCALYPSE)
                .desc("身边的地面被一片恶心的方块占领，还在继续往外扩。")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.scatterBlocks(ctx.pick(TrollPools.NASTY_BLOCKS), 6);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.6f);
                    ctx.msg("§2霉斑正在你周围生长。");
                })
                .build());

        // [64]
        TrollEvents.reg(TrollEvent.of("food_kitchen_fire", "厨房起火", TrollTier.APOCALYPSE)
                .desc("锅里起火，火苗顺着地面朝你蔓延过来。")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.setFire(5);
                    ctx.scatterBlocks(ctx.pick(TrollPools.TRAP_BLOCKS), 4);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.warn("§c你的厨房起火了。");
                })
                .build());

        // [65]
        TrollEvents.reg(TrollEvent.of("food_potion_mixup", "药水配错了", TrollTier.APOCALYPSE)
                .desc("喝下去才发现配方拿反了。效果随机，剂量格外大。")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.effectRandom(300, 2);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.msg("§d这瓶药水的颜色有点太鲜艳了。");
                })
                .build());

        // [66]
        TrollEvents.reg(TrollEvent.of("food_bad_milk", "变质牛奶", TrollTier.APOCALYPSE)
                .desc("一口闷下去，中毒和眩晕一起发作。")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 8, 2);
                })
                .build());

        // [67]
        TrollEvents.reg(TrollEvent.of("food_meat_swarm", "生肉引来怪物", TrollTier.APOCALYPSE)
                .desc("血腥味把不该来的东西招来了。它们比你先闻到。")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.title("§4它们闻到味道了", "§7下次别带生肉");
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                })
                .build());

        // [68]
        TrollEvents.reg(TrollEvent.of("food_sugar_crash", "糖分崩塌", TrollTier.NIGHTMARE)
                .desc("先给你满格精力，再把你整个人抽空。落差才是惩罚。")
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
        TrollEvents.reg(TrollEvent.of("food_cake_bomb", "蛋糕炸弹", TrollTier.APOCALYPSE)
                .desc("插着蜡烛的蛋糕，其实是引信。")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.spawnTnt(ctx.x() + 1, ctx.y() + 1, ctx.z() + 1, 40);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§c生日快乐！");
                })
                .build());

        // [70]
        TrollEvents.reg(TrollEvent.of("food_hunger_swap", "饱食与虚弱互换", TrollTier.APOCALYPSE)
                .desc("吃饱了，但身体软得使不上劲。饱腹感被换成了别的东西。")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
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
        TrollEvents.reg(TrollEvent.of("food_poison_broadcast", "全服食物中毒", TrollTier.APOCALYPSE)
                .desc("所有人都吃到同一种不明效果，公告还当众点了你的名字。")
                .weight(55)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.fakeBroadcast("§e服务器食堂今日菜单：可疑的炖菜。");
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.soundToAll(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                })
                .build());

        // [73]
        TrollEvents.reg(TrollEvent.of("food_taunt_meal", "吃饭被围观", TrollTier.APOCALYPSE)
                .desc("进食的时候被怪物当面嘲笑，负面效果和嘲笑一起留在身上。")
                .weight(55)
                .cooldownSeconds(330)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), ctx.pick(TrollPools.TAUNTS));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 220, 1);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                })
                .build());

        // [74]
        TrollEvents.reg(TrollEvent.of("food_rotten_banquet", "腐肉宴", TrollTier.NIGHTMARE)
                .desc("六件腐肉加长时间饥饿。这顿饭你会记住很久。")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.fillWith(ctx.pick(TrollPools.JUNK), 6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.msg("§2宴席已备好，请慢用。");
                })
                .build());

        // [75]
        TrollEvents.reg(TrollEvent.of("food_cursed_steak", "诅咒牛排", TrollTier.APOCALYPSE)
                .desc("吃下去之后会慢慢腐烂。痛感真实，而且持久。")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.damage("wither", 2.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 0.7f);
                })
                .build());

        // [76]
        TrollEvents.reg(TrollEvent.of("food_belly_bomb", "肚子里的炸弹", TrollTier.APOCALYPSE)
                .desc("肚子里传来滴答声。五秒之后，真的有东西在里面炸了。")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.spawnTnt(ctx.x(), ctx.y() + 2, ctx.z(), 50);
                    ctx.msg("§c……滴答。滴答。");
                    ctx.later(80, () -> ctx.boomAtPlayer(2.0f));
                })
                .build());

        // [77]
        TrollEvents.reg(TrollEvent.of("food_expired_potion", "过期药水", TrollTier.APOCALYPSE)
                .desc("过期药水的效果完全随机，剂量还特别大。")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.effectRandom(400, 3);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 10, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.6f);
                })
                .build());

        // ==================================================================
        // NIGHTMARE —— 崩溃：饿到绝望、吃到吐血（15 个）
        // ==================================================================

        // [78]
        TrollEvents.reg(TrollEvent.of("food_endless_hunger", "永远吃不饱", TrollTier.OBLIVION)
                .desc("你会一直饿。每一口都有效，但饱腹感在下一秒就被连本带利收回。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.feed(20);
                    ctx.saturation(20.0f);
                    ctx.msg("§a你吃饱了。");
                    // 假希望：吃饱，随即全部收回
                    ctx.later(60, () -> {
                        ctx.starve();
                        ctx.exhaust(8.0f);
                        ctx.warn("§4饱腹感是借给你的。§7现在连本带利收回去。");
                    });
                    ctx.later(170, () -> {
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                        ctx.damage("starve", 4.0f);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 10, 2);
                        ctx.msg("§8你会一直饿。§7这不是惩罚，这是新的常态。");
                    });
                    ctx.later(290, () -> {
                        ctx.obliterate();
                        ctx.takeRandomItems(2);
                        ctx.warn("§4连你身上带着的食物也会被拿走。§7饿着的时候，人更容易忘事。");
                    });
                })
                .build());

        // [79]
        TrollEvents.reg(TrollEvent.of("food_poison_apocalypse", "剧毒盛宴", TrollTier.NIGHTMARE)
                .desc("中毒加凋零，一波未平一波又起。")
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
        TrollEvents.reg(TrollEvent.of("food_food_void", "食物被虚空吞了", TrollTier.OBLIVION)
                .desc("你吃下去的东西会直接消失在别的地方，而不是你的肚子里。")
                .weight(30)
                .cooldownSeconds(600)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.feed(12);
                    ctx.saturation(12.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§7你吃下去了。§8味道不错。");
                    ctx.later(70, () -> {
                        ctx.starve();
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 8, 2);
                        ctx.warn("§4它没有进你的肚子。§7它去了别的地方，而那个地方不打算还。");
                    });
                    ctx.later(200, () -> {
                        ctx.takeRandomItems(3);
                        ctx.obliterate();
                        ctx.msg("§8你背包里剩下的食物，也在往同一个方向走。");
                    });
                })
                .build());

        // [81]
        TrollEvents.reg(TrollEvent.of("food_rotten_realm", "腐化领域", TrollTier.APOCALYPSE)
                .desc("周围的一切都在腐烂，连怪物都是被那股气味引来的。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.title("§2§l一切都在腐烂", "§7包括你的午饭");
                    ctx.scatterBlocks(ctx.pick(TrollPools.NASTY_BLOCKS), 8);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                })
                .build());

        // [82]
        TrollEvents.reg(TrollEvent.of("food_cannibal_feast", "同类相食", TrollTier.APOCALYPSE)
                .desc("你获得了不该获得的「食材」，附带一份不容易消化的记忆。")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.title("§4§l这不太合适", "§8但你已经吃了");
                    ctx.give(ctx.pick(TrollPools.JUNK), 3);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                })
                .build());

        // [83]
        TrollEvents.reg(TrollEvent.of("food_blood_soup", "血色浓汤", TrollTier.APOCALYPSE)
                .desc("汤是红的，你的血也是。你分不清这锅汤用了谁的。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.damage("magic", 3.0f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.msg("§c这碗汤的颜色……有点太正了。");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                })
                .build());

        // [84]
        TrollEvents.reg(TrollEvent.of("food_starvation_death", "假饿死", TrollTier.OBLIVION)
                .desc("它会先让你看见满桌的食物，然后把桌子和你手上那份一起搬走。")
                .weight(40)
                .cooldownSeconds(540)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 4);
                    ctx.msg("§a桌上全是吃的。§7你伸手过去。");
                    ctx.later(80, () -> {
                        ctx.takeRandomItems(5);
                        ctx.starve();
                        ctx.exhaust(10.0f);
                        ctx.damage("starve", 6.0f);
                        ctx.warn("§4桌子被搬走了。§7连你手上那份一起。");
                    });
                    ctx.later(210, () -> {
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                        ctx.obliterate();
                        ctx.msg("§8饿到最后，你会开始计算自己还能站多久。");
                    });
                })
                .build());

        // [85]
        TrollEvents.reg(TrollEvent.of("food_curse_of_gluttony", "暴食诅咒", TrollTier.APOCALYPSE)
                .desc("饱食度拉满，换来一整套随机诅咒。吃饱本身成了债。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.fakeAchievement("暴食之罪", "把所有人的口粮都吃了");
                    ctx.feed(20);
                    ctx.saturation(0.0f);
                    ctx.effectRandom(500, 2);
                })
                .build());

        // [86]
        TrollEvents.reg(TrollEvent.of("food_meal_of_despair", "绝望之餐", TrollTier.APOCALYPSE)
                .desc("手里的食物被扔了一地，饥饿感还留在原处。")
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
        TrollEvents.reg(TrollEvent.of("food_poison_well", "水井被投毒", TrollTier.OBLIVION)
                .desc("唯一的干净水源是它特意留给你的。喝完之后它会告诉你那是什么。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.feed(6);
                    ctx.msg("§b你找到了一口井。§7水看起来是干净的。");
                    ctx.later(70, () -> {
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 700, 3);
                        ctx.damage("magic", 5.0f);
                        ctx.starve();
                        ctx.warn("§4那不是水。§7那只是看起来像水。");
                    });
                    ctx.later(210, () -> {
                        ctx.obliterate();
                        ctx.takeRandomItems(2);
                        ctx.msg("§8它把井留着，就是为了让你有得选。");
                    });
                })
                .build());

        // [88]
        TrollEvents.reg(TrollEvent.of("food_eternal_thirst", "永恒干渴", TrollTier.NIGHTMARE)
                .desc("饥饿长时间压顶，体力像被抽水机一点点抽走。")
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
                .desc("一个看不见的厨师给你上了菜。菜是凉的，筷子却动过。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.title("§8§l主厨已上菜", "§7主厨没有实体");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.give(ctx.pick(TrollPools.JUNK), 3);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                })
                .build());

        // [90]
        TrollEvents.reg(TrollEvent.of("food_cursed_buffet", "诅咒自助餐", TrollTier.OBLIVION)
                .desc("自助餐无限供应。每一道菜在你嘴里都会变成同一种味道。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.give(ctx.pick(TrollPools.MYSTERY_ITEMS), 2);
                    }
                    ctx.feed(20);
                    ctx.saturation(20.0f);
                    ctx.msg("§a自助餐开始了。§7随便吃。");
                    ctx.later(90, () -> {
                        ctx.starve();
                        ctx.effectRandom(600, 2);
                        ctx.damage("starve", 5.0f);
                        ctx.warn("§4所有菜都是同一个味道。§7那个味道叫「不够」。");
                    });
                    ctx.later(250, () -> {
                        ctx.obliterate();
                        ctx.takeRandomItems(4);
                        ctx.msg("§8自助餐还在供应。§7只是你已经吃不下了。");
                    });
                })
                .build());

        // [91]
        TrollEvents.reg(TrollEvent.of("food_meat_hook", "肉钩", TrollTier.APOCALYPSE)
                .desc("被一股力量吊了起来。落地的时候，血已经少了一截。")
                .weight(35)
                .cooldownSeconds(540)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.damage("magic", 4.0f);
                    ctx.lift(8);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                })
                .build());

        // [92]
        TrollEvents.reg(TrollEvent.of("food_wasting_away", "逐渐消瘦", TrollTier.APOCALYPSE)
                .desc("饥饿连绵不断，血条被一点点磨掉，没有哪一下特别疼。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
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
                .desc("全服饥饿清零且长时间无法恢复。这是天灾，不是饿。")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.fakeBroadcast("§4警告：世界食物产出已归零。");
                    ctx.title("§6§l大饥荒", "§7这一次没有下一顿");
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 800, 2);
                    ctx.starve();
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                })
                .build());

        // [94]
        TrollEvents.reg(TrollEvent.of("food_poison_banquet", "毒宴", TrollTier.APOCALYPSE)
                .desc("全服剧毒加凋零。桌上没有一道菜是安全的。")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.fakeBroadcast("§4宴会开始，请各位就座。");
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                    ctx.damage("wither", 4.0f);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                })
                .build());

        // [95]
        TrollEvents.reg(TrollEvent.of("food_hunger_plague", "饥饿瘟疫", TrollTier.OBLIVION)
                .desc("饥饿会传染给在场所有人，包括你本来指望分你一口的那些人。")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 900, 2);
                    ctx.starve();
                    ctx.exhaust(9.0f);
                    ctx.titleToAll("§4§l饥 饿 瘟 疫", "§7没有人有富余可以分给你");
                    ctx.later(150, () -> {
                        ctx.takeRandomItems(3);
                        ctx.damage("starve", 5.0f);
                        ctx.warn("§4所有人都在饿。§7所以你谁都指望不上。");
                    });
                    ctx.later(290, () -> {
                        ctx.obliterate();
                        ctx.buryAlive();
                        ctx.msg("§8瘟疫不杀人。§7它只是让每个人都变成竞争者。");
                    });
                })
                .build());

        // [96]
        TrollEvents.reg(TrollEvent.of("food_last_supper", "最后的晚餐", TrollTier.OBLIVION)
                .desc("它给你一顿真正的饱饭，然后让你用身上剩下的全部东西来付账。")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.feed(20);
                    ctx.saturation(20.0f);
                    ctx.heal(20.0f);
                    ctx.ok("§a你吃饱了，伤也好了。");
                    ctx.msg("§7这是你最近吃得最好的一顿。");
                    // 先给后夺：全部收回
                    ctx.later(140, () -> {
                        ctx.stripAll();
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 12, 3);
                        ctx.takeRandomItems(5);
                        ctx.starve();
                        ctx.damage("starve", 7.0f);
                        ctx.warn("§4这顿饭的账单是：你身上所有的东西。");
                    });
                    ctx.later(310, () -> {
                        ctx.obliterate();
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 900, 3);
                        ctx.msg("§8它叫最后的晚餐，是因为之后你不会再有机会吃。");
                    });
                })
                .build());

        // [97]
        TrollEvents.reg(TrollEvent.of("food_belly_explosion", "肚子爆炸", TrollTier.APOCALYPSE)
                .desc("吃下去的东西在体内发生了连锁反应。从里面开始。")
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
        TrollEvents.reg(TrollEvent.of("food_void_meal", "虚空之餐", TrollTier.OBLIVION)
                .desc("食物会在你嘴里变成虚空。你会一直饿，同时记得它的味道。")
                .weight(16)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.feed(20);
                    ctx.msg("§7你咬了一口。§8是热的。");
                    ctx.later(60, () -> {
                        ctx.starve();
                        ctx.voidPit(2, 8);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 6, 2);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                        ctx.warn("§4你吞下去的是虚空。§7它会从里面开始吃你。");
                    });
                    ctx.later(210, () -> {
                        ctx.obliterate();
                        ctx.takeRandomItems(3);
                        ctx.msg("§8你会一直记得那个味道，§7并且永远吃不到第二口。");
                    });
                })
                .build());

        // [99]
        TrollEvents.reg(TrollEvent.of("food_everything_food", "一切与食物有关的一起上", TrollTier.OBLIVION)
                .desc("所有东西都会开始尝起来像食物，包括你不该咬的那些。")
                .weight(16)
                .cooldownSeconds(900)
                .when(ctx -> ctx.rage() >= 80.0)
                .run(ctx -> {
                    ctx.feed(20);
                    ctx.saturation(20.0f);
                    ctx.msg("§a你手边的一切都可以吃了。§7包括石头。");
                    ctx.later(90, () -> {
                        ctx.starve();
                        ctx.exhaust(10.0f);
                        ctx.effectRandom(700, 3);
                        ctx.warn("§4能吃，不代表该吃。§7你的胃已经开始拒绝了。");
                    });
                    ctx.later(240, () -> {
                        ctx.obliterate();
                        ctx.stripAll();
                        ctx.damage("starve", 6.0f);
                        ctx.msg("§8最后你会发现：什么都吃得下，等于什么都没吃。");
                    });
                })
                .build());
    }
}
