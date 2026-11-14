package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【天气与时间】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。</p>
 *
 * <p>太阳、月亮、云、雨、雷、雪、雾、时钟和床——天空从来不是你的盟友，
 * 它只是一直在等你放下戒心。本文件刻意不引用任何 Minecraft 类型，
 * 全部通过 {@code ctx.*} 与 {@code TrollPools.*} 组合实现。</p>
 */
public final class SkyEvents {

    private SkyEvents() {
    }

    public static void register() {
        TrollEvents.category("天气与时间");

        // ================================================================
        // PRANK —— 小整，抬头看一眼就好（18 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("sky_sun_opinion", "太阳的意见", TrollTier.PRANK)
                .desc("阳光突然偏了一下，像是特意避开你的脸")
                .weight(140)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 8);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.7f, 1.7f);
                    ctx.actionBar("§7太阳：§e你站的位置不太好。");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_cloud_shape", "云的形状", TrollTier.PRANK)
                .desc("云朵缓缓拼成一个不太礼貌的形状，几秒后散掉")
                .weight(135)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.msg("§7你抬头看了一眼，云拼成了一个§e很难形容§7的形状。");
                    ctx.msg("§8它维持了三秒，然后心虚地散了。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.6f, 1.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_clock_backwards", "时钟逆行", TrollTier.PRANK)
                .desc("你手里的钟开始倒着走，走了几秒又恢复正常")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.4f);
                    ctx.actionBar("§7时钟：§8 12 → 11 → 10 → ……好像不太对。");
                    ctx.msg("§7你的钟决定§e倒退三秒§7，理由是不想上班。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_forecast_sass", "天气预报阴阳你", TrollTier.PRANK)
                .desc("一条假天气预报精准总结了你的处境")
                .weight(135)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeBroadcast("§9【天气】今日局部地区有雨，具体就是你所在的那一格。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.3f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_rain_smell", "雨的预告", TrollTier.PRANK)
                .desc("空气里突然出现一股雨味，但一滴雨都没下")
                .weight(125)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 1.1f);
                    ctx.actionBar("§7空气里有一股§b雨味§7……然后又没了。");
                    ctx.msg("§8你的鼻子刚刚骗了你一次。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_snowflake_nose", "精准落点", TrollTier.PRANK)
                .desc("一片雪花穿越风雨，准确落在你鼻尖上")
                .weight(125)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 12);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.9f);
                    ctx.msg("§7一片雪花做到了千军万马都没做到的事：§b精准命中你的鼻子§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_sunset_palette", "晚霞调色失败", TrollTier.PRANK)
                .desc("晚霞的颜色明显是随便调的，还带一点荧光")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.msg("§7今天的晚霞是§d荧光紫§7配§a荧光绿§7，审美由骰子决定。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.7f, 1.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_star_wink", "星星眨眼", TrollTier.PRANK)
                .desc("夜空中有一颗星星对你眨了下眼，你没有看错")
                .weight(120)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 10);
                    ctx.actionBar("§7有一颗星星对你眨了下眼。");
                    ctx.msg("§8你确定它眨眼了。你也确定自己没熬夜。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_moon_sleep_comment", "月亮评价你的作息", TrollTier.PRANK)
                .desc("月亮用一种很不客气的方式点评了你的睡眠时间")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.7f, 1.2f);
                    ctx.actionBar("§7月亮：§8都这个点了，你还醒着。");
                    ctx.msg("§7月亮表示它§e已经看你熬了三个通宵§7了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_rainbow_lie", "彩虹的尽头", TrollTier.PRANK)
                .desc("天边出现一道彩虹，尽头什么都没有，从来都没有")
                .weight(120)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 15);
                    ctx.msg("§7天边挂了一道彩虹。你追过去，§8尽头是一个坑，还是你自己挖的。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 1.4f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_wind_gossip", "风里的闲话", TrollTier.PRANK)
                .desc("风把别人的议论吹到你耳边，关键词是你的名字")
                .weight(125)
                .cooldownSeconds(65)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 1.3f);
                    ctx.fakeChat("风", "§7……那个 §f" + ctx.playerName() + " §7又站在岩浆边上了。");
                    ctx.msg("§8风说完就走了，没给你解释的机会。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_thermometer_lie", "体感温度是假的", TrollTier.PRANK)
                .desc("体感温度显示了一个物理上不可能的数值")
                .weight(115)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.actionBar("§7当前体感温度：§c37°C §8（你在雪地里）");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_sundial_lazy", "日晷开始敷衍", TrollTier.PRANK)
                .desc("日晷的影子动了一下，然后拒绝继续工作")
                .weight(115)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.msg("§7日晷的影子挪了半格，然后§8罢工§7了。");
                    ctx.msg("§8它说你也没在认真看时间，凭什么它要认真报时。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.7f, 1.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_weather_app_crash", "天气软件崩了", TrollTier.PRANK)
                .desc("模拟的天气应用闪退，并留下一条甩锅提示")
                .weight(115)
                .cooldownSeconds(85)
                .run(ctx -> {
                    ctx.closeScreen();
                    ctx.msg("§c【天气】应用已停止运行。");
                    ctx.msg("§8原因：§7天气自己也不知道今天是什么天气。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_hail_tap", "冰雹敲头盔", TrollTier.PRANK)
                .desc("几颗小冰雹敲在你的头盔上，节奏还挺好听")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.6f, 1.8f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 10);
                    ctx.actionBar("§7叮、叮、叮——§8你被冰雹当成鼓了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_dawn_snooze", "日出按了贪睡", TrollTier.PRANK)
                .desc("天亮了半秒，又缩回黑夜里去，像按了贪睡")
                .weight(125)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.timeSkip(200);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.2f);
                    ctx.msg("§7天亮了半秒，然后说§e再睡五分钟§7，缩回去了。");
                    ctx.later(120, () -> ctx.msg("§8……它是真的又睡了。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_barometer_mood", "气压计有情绪", TrollTier.PRANK)
                .desc("气压计的指针开始随心情摆动，气象学被冒犯了")
                .weight(115)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.7f, 1.4f);
                    ctx.actionBar("§7气压：§c即将暴风雨 §8/ §b晴 §8/ §d随便吧");
                    ctx.msg("§8气压计表示今天不想被科学定义。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_cloud_stalker", "这片云跟着你", TrollTier.PRANK)
                .desc("有一片云专门投在你头顶上，你走它也走")
                .weight(125)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 14);
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 1.0f);
                    ctx.msg("§7你走两步，头顶那片云§e也走了两步§7。");
                    ctx.msg("§8你停下，它也停下。它好像很有耐心。");
                })
                .build());

        // ================================================================
        // ANNOYING —— 烦人，节奏被打断（32 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("sky_drizzle", "毛毛雨", TrollTier.ANNOYING)
                .desc("下起一场不大不小的雨，什么都做不了但什么都湿了")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.setRaining(true, false, 1200);
                    ctx.soundToAll(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 1.1f);
                    ctx.msg("§7开始下雨了。§8不大，但足够让一切都湿掉。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_thunder_no_rain", "干打雷", TrollTier.ANNOYING)
                .desc("雷声滚滚，天上一片云都没有，也不下雨")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.soundToAll(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.actionBar("§7打雷了，但是§e没下雨§7，也没有云。");
                    ctx.msg("§8气象学在你这儿刚刚失效了一次。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_day_night_flicker", "昼夜闪烁", TrollTier.ANNOYING)
                .desc("天色忽明忽暗闪了几下，像世界在重启")
                .weight(115)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.timeSkip(600);
                    ctx.timeSkip(-1200);
                    ctx.timeSkip(600);
                    ctx.glitch(GlitchIds.SHAKY, 30);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.3f);
                    ctx.msg("§7天色闪了三下。§8你怀疑是世界在重启，还是在眨眼。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_sun_power_save", "太阳省电模式", TrollTier.ANNOYING)
                .desc("阳光亮度被调到最低，光照等级苟延残喘")
                .weight(115)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.actionBar("§7太阳已进入§e省电模式§7，亮度 12%。");
                    ctx.msg("§8它说电费太贵了，让你自己想办法。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_fog_roll_in", "雾滚进来了", TrollTier.ANNOYING)
                .desc("浓雾从远处滚来，能见度被吃掉了大半")
                .weight(120)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FOV_ZOOM, 100);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 0.9f);
                    ctx.msg("§7雾贴着地面滚过来，§8把远处的山一口吞了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_light_snow", "悄悄下雪", TrollTier.ANNOYING)
                .desc("细雪无声落下，你的脚步声被吸走了")
                .weight(115)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 50);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.6f, 1.4f);
                    ctx.msg("§7下雪了。§8雪把你的脚步声吸走了，只剩你自己的心跳。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_rain_loop_sfx", "雨声循环卡带", TrollTier.ANNOYING)
                .desc("雨声音效卡在一个音节上，反复播放")
                .weight(110)
                .cooldownSeconds(100)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 1.0f);
                    }
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 60);
                    ctx.msg("§7雨声卡住了，§8在一滴雨上反复循环。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_wind_shove", "风推你一把", TrollTier.ANNOYING)
                .desc("一阵横风把你推离了原本的路线")
                .weight(115)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.fling(ctx.rd(-1.2, 1.2), 0.3, ctx.rd(-1.2, 1.2));
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 1.2f);
                    ctx.actionBar("§7一阵风：§8借过一下。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_wet_chill", "淋湿的寒意", TrollTier.ANNOYING)
                .desc("衣服湿透带来的寒意让你一直发抖")
                .weight(115)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.7f, 1.1f);
                    ctx.msg("§b你湿透了。§7寒意从脚底开始往上爬。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_cloud_blanket", "云被子", TrollTier.ANNOYING)
                .desc("云层压得极低，整个天空像盖了床被子")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FOV_ZOOM, 80);
                    ctx.title("§8云压下来了", "§7天空看起来只有三格高");
                    ctx.soundToAll(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_forecast_spam", "天气预报刷屏", TrollTier.ANNOYING)
                .desc("假天气预报开始刷屏，每一条都在说你")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.fakeBroadcast("§9【天气】" + ctx.pick(TrollPools.FAKE_BROADCASTS));
                    }
                    ctx.glitch(GlitchIds.CHAT_SPAM, 60);
                    ctx.msg("§8天气预报已把今天的版面全部留给了你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_clock_cooldown", "时钟进冷却", TrollTier.ANNOYING)
                .desc("你手里的钟被强制进入冷却，时间拒绝被查看")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.setCooldown(ctx.pick(TrollPools.WEIRD_ITEMS), 200);
                    ctx.msg("§7你的钟被冷却了。§8它说看时间也是要付出代价的。");
                    ctx.later(200, () -> ctx.ok("§7钟冷却结束，它什么都没说。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_mild_sunburn", "轻微晒伤", TrollTier.ANNOYING)
                .desc("阳光在你身上留下不属于这个季节的晒痕")
                .weight(110)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.damage("fire", 2.0f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 12);
                    ctx.msg("§c你被晒伤了。§8太阳对此不承担任何责任。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_cold_snap", "倒春寒", TrollTier.ANNOYING)
                .desc("气温骤降，你的动作明显变慢")
                .weight(115)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 1.3f);
                    ctx.actionBar("§b气温骤降 20 度。§7你的关节在抗议。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_heat_wave", "热浪", TrollTier.ANNOYING)
                .desc("热浪扭曲了空气，视野开始晃动")
                .weight(115)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.exhaust(4.0f);
                    ctx.glitch(GlitchIds.SHAKY, 80);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.7f, 0.9f);
                    ctx.msg("§c热浪把空气烤得扭曲§7，远处的方块开始跳舞。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_dew_damp", "露水打湿装备", TrollTier.ANNOYING)
                .desc("清晨的露水让你所有装备都是湿的，手感变差")
                .weight(105)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.damageRandomTool(15);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.7f, 1.2f);
                    ctx.msg("§7露水把你的装备全打湿了。§8手感像握着一块肥皂。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_sleet", "雨夹雪", TrollTier.ANNOYING)
                .desc("雨和雪同时下，两种都很敷衍")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.setRaining(true, false, 900);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 30);
                    ctx.msg("§7雨夹雪。§8气象学最敷衍的一种天气。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_small_hail", "小冰雹", TrollTier.ANNOYING)
                .desc("冰粒噼里啪啦砸下来，疼但不致命")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.damage("freeze", 2.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.9f, 1.5f);
                    ctx.msg("§7冰雹不大，但§e每一颗都精准§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_tornado_warning_fake", "假龙卷风预警", TrollTier.ANNOYING)
                .desc("系统发布龙卷风预警，实际上什么都没有")
                .weight(105)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.fakeBroadcast("§4【紧急】龙卷风正在接近，请立即避难。");
                    ctx.titleToAll("§4龙卷风预警", "§7预计抵达时间：永远不会");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 0.8f, 1.4f);
                    ctx.later(200, () -> ctx.msg("§8【天气】预警已解除。刚才是演习，也可能是心情。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_moon_too_bright", "月亮太亮", TrollTier.ANNOYING)
                .desc("月光亮得像白天，你彻底睡不着了")
                .weight(105)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FLASHBANG, 20);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 0);
                    ctx.msg("§f月亮今晚亮得离谱§7，你怀疑它是不是忘了调暗。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_stars_on_strike", "星星集体请假", TrollTier.ANNOYING)
                .desc("夜空的星星一颗接一颗熄灭，只剩下黑")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.soundToAll(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 0.8f);
                    ctx.msg("§8星星一颗一颗熄灭了。它们也累了吧。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_timezone_shift", "时区被改了", TrollTier.ANNOYING)
                .desc("世界时间与你的体感突然错开六个小时")
                .weight(105)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.timeSkip(6000);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.2f);
                    ctx.actionBar("§7时区已由 §eUTC+8 §7改为 §cUTC+2§7。");
                    ctx.msg("§8没人通知你，但你的胃已经开始不舒服了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_daylight_sensor_freak", "日照传感器发疯", TrollTier.ANNOYING)
                .desc("附近的日照传感器开始狂闪，像在报警")
                .weight(110)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FLASHBANG, 15);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.6f);
                    ctx.msg("§7日照传感器开始疯狂输出信号。§8它在报警，但没人知道报什么警。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_sunrise_alarm", "日出闹钟", TrollTier.ANNOYING)
                .desc("日出被配上了闹钟音效，每天准时叫你起床")
                .weight(105)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.8f);
                    ctx.title("§e§l起 床", "§7太阳已就位，你没有反驳的权利");
                    ctx.msg("§8你没设闹钟，但太阳替你设了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_minor_eclipse", "小日食", TrollTier.ANNOYING)
                .desc("太阳被咬掉一小块，然后若无其事地复原")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.timeSkip(400);
                    ctx.soundToAll(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 0.7f);
                    ctx.msg("§8太阳被咬掉了一小块。§7三秒后它装作什么都没发生。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_rain_indoor", "屋里也下雨", TrollTier.ANNOYING)
                .desc("雨下进了室内，水滴穿过屋顶落在你头上")
                .weight(110)
                .cooldownSeconds(140)
                .when(ctx -> ctx.y() > 0)
                .run(ctx -> {
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 1.0f);
                    ctx.msg("§b水滴穿过屋顶落在你头上。§8物理学拒绝解释。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_personal_raincloud", "私人雨云", TrollTier.ANNOYING)
                .desc("只有你头顶这一格在下雨，别人那里是晴天")
                .weight(115)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 1.1f);
                    if (ctx.hasOthers()) {
                        ctx.msgToOther(ctx.other(), "§7你那边……在下雨吗？");
                    }
                    ctx.actionBar("§7局部地区有雨。§8局部地区=你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_night_thickens", "夜色变浓", TrollTier.ANNOYING)
                .desc("夜突然比刚才更黑了几个色号，视野被压低")
                .weight(115)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.glitch(GlitchIds.FOV_ZOOM, 70);
                    ctx.msg("§8夜色的浓度被谁调高了一档。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_puddle_foot", "脚下积水", TrollTier.ANNOYING)
                .desc("每个落脚点都恰好有一滩积水")
                .weight(105)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.1f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 15);
                    ctx.msg("§7你每踩一步都是水声。§8你怀疑水坑在埋伏你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_distant_rumble", "远处闷雷", TrollTier.ANNOYING)
                .desc("地平线传来闷雷，一直响但从不靠近")
                .weight(115)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.8f, 0.7f);
                    ctx.actionBar("§7远处一直有闷雷。§8它没有过来，也没有走。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_pollen_sneeze", "花粉过敏", TrollTier.ANNOYING)
                .desc("空气里的花粉让你的手抖了一下")
                .weight(105)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.5f);
                    ctx.randomSelectSlot();
                    ctx.msg("§7你打了个喷嚏。§8手里的东西换了一样。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_humidity_max", "湿度爆表", TrollTier.ANNOYING)
                .desc("空气湿度 100%，呼吸都变得黏糊糊")
                .weight(105)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 220, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.7f, 1.0f);
                    ctx.msg("§b湿度 100%。§7空气开始有了质地。");
                })
                .build());

        // ================================================================
        // BRUTAL —— 抓狂，明显损失（27 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("sky_thunderstorm_arrival", "雷暴降临", TrollTier.BRUTAL)
                .desc("一场真正的雷暴压境，闪电开始找目标")
                .weight(95)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.thunderstorm(2400);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.9f);
                    ctx.title("§8§l雷暴", "§7天空决定今天不讲道理");
                    ctx.lightningNear();
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_lightning_close", "近距落雷", TrollTier.BRUTAL)
                .desc("闪电劈在你身边两格，耳朵先于眼睛反应")
                .weight(90)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.lightningNear();
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 1.1f);
                    ctx.damage("lightning", 4.0f);
                    ctx.msg("§e闪电擦着你过去了§7。空气里全是焦味。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_jump_to_night", "直接入夜", TrollTier.BRUTAL)
                .desc("时间被硬生生拨到午夜，火把还没插好")
                .weight(90)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.timeOfDay(18000);
                    ctx.soundToAll(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.msg("§8天一下子就黑了。§7你连火把都来不及插。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_jump_to_noon", "直接正午", TrollTier.BRUTAL)
                .desc("时间跳到正午，你的夜视和眼睛同时失效")
                .weight(85)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.timeOfDay(6000);
                    ctx.glitch(GlitchIds.FLASHBANG, 25);
                    ctx.msg("§e太阳直接砸到头顶。§7你有一瞬间什么都看不见。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_fog_blindfold", "浓雾封眼", TrollTier.BRUTAL)
                .desc("浓雾把你的视野压到几乎为零")
                .weight(90)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.effectRandom(400, 1);
                    ctx.glitch(GlitchIds.FOV_ZOOM, 200, 2);
                    ctx.soundToAll(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 0.8f);
                    ctx.msg("§8雾浓到能用手抓住。§7你现在靠猜走路。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_blizzard", "暴雪", TrollTier.BRUTAL)
                .desc("暴雪封路，你被冻得几乎无法移动")
                .weight(90)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.setRaining(true, true, 3000);
                    ctx.freeze(200);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 80);
                    ctx.msg("§b暴雪来了。§7你每走一步都要和风商量。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_freezing_rain", "冻雨", TrollTier.BRUTAL)
                .desc("雨落在皮肤上直接结冰，持续掉血")
                .weight(85)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.setRaining(true, false, 2400);
                    ctx.damage("freeze", 6.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.msg("§b雨一落地就结成冰。§7你的手指已经不听使唤了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_sun_scorch", "烈日灼烧", TrollTier.BRUTAL)
                .desc("太阳把地面烤成岩浆，你脚下一片通红")
                .weight(85)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.timeOfDay(6000);
                    ctx.setFire(8);
                    ctx.lavaPit(2);
                    ctx.msg("§c太阳把地面烤红了。§7你站的地方开始冒烟。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_hail_bruise", "冰雹砸出淤青", TrollTier.BRUTAL)
                .desc("冰雹个头不小，砸在身上真疼")
                .weight(85)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.damage("freeze", 7.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 30);
                    ctx.msg("§7冰雹不客气了。§8你身上多了好几块淤青。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_gale_launch", "狂风卷起", TrollTier.BRUTAL)
                .desc("一阵狂风把你整整个人掀到半空")
                .weight(90)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.launch(1.8);
                    ctx.soundToAll(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§7风把你掀起来了。§8落地这件事，等下再说。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_bed_refuses", "床拒绝服务", TrollTier.BRUTAL)
                .desc("你想睡觉，床把你传送到了别的地方")
                .weight(90)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.scatterFar(64);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.msg("§7你刚躺下，床就把你§e送走了§7。");
                    ctx.msg("§8它的理由是：你打呼。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_sleep_deprivation", "睡眠剥夺", TrollTier.BRUTAL)
                .desc("你被强行判定为通宵，整夜没有合眼")
                .weight(85)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.effectRandom(600, 1);
                    ctx.exhaust(8.0f);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 120);
                    ctx.msg("§8系统判定你已连续清醒 72 小时。§7你的视野开始重影。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_clock_breakdown", "时钟彻底坏掉", TrollTier.BRUTAL)
                .desc("你手里的钟直接报废，时间从此失去参照")
                .weight(80)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.breakRandomTool();
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§c你的钟碎了。§7从现在起，时间由你猜。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_blood_moon", "血月", TrollTier.BRUTAL)
                .desc("月亮变红，夜里的怪开始变得积极")
                .weight(85)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.timeOfDay(18000);
                    ctx.title("§4§l血 月", "§7今晚的规则已经作废");
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 12);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_small_meteor", "小陨石", TrollTier.BRUTAL)
                .desc("一颗小型陨石落在你附近，砸出个坑和一声巨响")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    double mx = ctx.x() + ctx.rd(-12, 12);
                    double mz = ctx.z() + ctx.rd(-12, 12);
                    ctx.boomDestructive(mx, ctx.y() + 2, mz, 3.0f);
                    ctx.particleAt(ctx.pick(TrollPools.BOOM_PARTICLES), mx, ctx.y(), mz, 40, 2.0);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.8f);
                    ctx.msg("§c一颗小陨石砸在你附近。§8它的准头比你好一点。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_flash_flood", "骤雨成涝", TrollTier.BRUTAL)
                .desc("暴雨在几分钟内把地面变成水塘，你陷在里面")
                .weight(80)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.setRaining(true, true, 3600);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.placeFloor(ctx.pick(TrollPools.TROLL_BLOCKS), 4);
                    ctx.msg("§b雨下得太快，地面已经积起来了。§7你走得越来越慢。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_lightning_barrage", "连续落雷", TrollTier.BRUTAL)
                .desc("闪电一波接一波，整个区域被照亮又熄灭")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.lightningNear();
                    }
                    ctx.damage("lightning", 5.0f);
                    ctx.glitch(GlitchIds.FLASHBANG, 30);
                    ctx.msg("§e落雷没有停。§7你开始数自己还活着几秒。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_storm_summon_mobs", "风雨引怪", TrollTier.BRUTAL)
                .desc("雷雨把附近的怪全都引了过来")
                .weight(85)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.thunderstorm(1800);
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10);
                    }
                    ctx.msg("§7雷声把它们都叫醒了。§c它们正在朝你走。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_acid_rain", "酸雨", TrollTier.BRUTAL)
                .desc("雨水带着腐蚀性，装备和皮肤一起受损")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.setRaining(true, false, 2400);
                    ctx.damage("magic", 6.0f);
                    ctx.damageRandomTool(40);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 35);
                    ctx.msg("§a雨是酸性的。§7你的护甲在冒泡。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_heatstroke", "中暑", TrollTier.BRUTAL)
                .desc("高温让你头晕目眩，方向感开始失灵")
                .weight(85)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.effectRandom(400, 1);
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 120);
                    ctx.msg("§c你中暑了。§7现在前后左右是四个建议，不是方向。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_hypothermia", "失温", TrollTier.BRUTAL)
                .desc("体温掉得太快，你的手几乎握不住东西")
                .weight(85)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.freeze(240);
                    ctx.damage("freeze", 5.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.9f);
                    ctx.msg("§b你的体温在往下掉。§7手指已经没有知觉了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_sandstorm", "沙尘暴", TrollTier.BRUTAL)
                .desc("风沙扑面，你被推着走还看不清路")
                .weight(80)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.fling(ctx.rd(-2.0, 2.0), 0.5, ctx.rd(-2.0, 2.0));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 1);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 60);
                    ctx.msg("§6沙尘暴把你的视野磨成了砂纸。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_human_lightning_rod", "你成了避雷针", TrollTier.BRUTAL)
                .desc("所有的雷都开始往你身上招呼")
                .weight(80)
                .cooldownSeconds(320)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.thunderstorm(1200);
                    ctx.lightning();
                    ctx.damage("lightning", 6.0f);
                    ctx.msg("§e你被设定成了避雷针。§7这个身份不接受辞职。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_moon_tug", "月亮拉你上去", TrollTier.BRUTAL)
                .desc("月光变成了一股牵引力，你开始往上飘")
                .weight(80)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.lift(18.0);
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 200, 0);
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 1.3f);
                    ctx.msg("§f月亮好像在拉你。§7你脚已经离地了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_time_desync", "时间与体感脱节", TrollTier.BRUTAL)
                .desc("世界时间和你的体感彻底对不上，一切都慢半拍")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SHAKY, 200, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.msg("§7你动一下，世界要愣半秒才跟上。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_hail_ponding", "冰雹积成塘", TrollTier.BRUTAL)
                .desc("冰雹在地上堆成一片，你被卡在里面")
                .weight(75)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.placeFloor(ctx.pick(TrollPools.NASTY_BLOCKS), 4);
                    ctx.cageWith(ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.damage("freeze", 4.0f);
                    ctx.msg("§b冰雹把你围住了。§7这大概是天气版的牢房。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_sun_pulse", "太阳脉冲", TrollTier.BRUTAL)
                .desc("太阳毫无预兆地爆闪一下，地面被烤出焦痕")
                .weight(75)
                .cooldownSeconds(340)
                .run(ctx -> {
                    ctx.timeOfDay(6000);
                    ctx.glitch(GlitchIds.FLASHBANG, 40, 2);
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 2.5f);
                    ctx.setFire(6);
                    ctx.msg("§e太阳闪了一下。§7地面留下一圈焦黑。");
                })
                .build());

        // ================================================================
        // NIGHTMARE —— 崩溃，严重伤害（15 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("sky_eternal_night", "永夜开始", TrollTier.NIGHTMARE)
                .desc("太阳不再升起，黑夜被无限延长")
                .weight(60)
                .cooldownSeconds(600)
                .once()
                .run(ctx -> {
                    ctx.timeOfDay(18000);
                    ctx.title("§8§l太 阳 下 班 了", "§7下班时间：∞");
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 600, 1);
                    ctx.msg("§8天再也没有亮过。§7你开始怀疑白天是不是你想象出来的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_meteor_shower", "流星雨（灾）", TrollTier.NIGHTMARE)
                .desc("天上的流星全都朝你这片区域落下来")
                .weight(55)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.meteorShower(14, 30);
                    ctx.timeOfDay(18000);
                    ctx.title("§4§l流 星 雨", "§7许愿的时间已经过了");
                    ctx.msg("§c这次的流星雨不负责浪漫，只负责落点。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_thunder_war", "雷神开战", TrollTier.NIGHTMARE)
                .desc("雷暴强度拉满，整个天空都在爆炸")
                .weight(55)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.thunderstorm(4800);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.7f);
                    for (int i = 0; i < 5; i++) {
                        ctx.lightningNear();
                    }
                    ctx.damage("lightning", 8.0f);
                    ctx.msg("§e天上一共发生了多少事，你已经不敢数了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_sun_extinguished", "太阳熄灭", TrollTier.NIGHTMARE)
                .desc("太阳直接灭了，世界跌进无光的深冬")
                .weight(55)
                .cooldownSeconds(700)
                .run(ctx -> {
                    ctx.timeOfDay(18000);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 900, 2);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 120, 2);
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.msg("§8太阳熄灭了。§7没有人去点它。");
                    ctx.later(600, () -> ctx.msg("§8……它还是没亮。你没在做梦。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_time_loop", "时间循环", TrollTier.NIGHTMARE)
                .desc("同一个下午重复了三次，每次你都在同一个位置")
                .weight(50)
                .cooldownSeconds(700)
                .run(ctx -> {
                    double lx = ctx.x();
                    double ly = ctx.y();
                    double lz = ctx.z();
                    ctx.setRotation(ctx.rf(-180, 180), 0);
                    ctx.timeOfDay(9000);
                    ctx.later(60, () -> ctx.teleport(lx, ly, lz));
                    ctx.later(120, () -> {
                        ctx.timeOfDay(9000);
                        ctx.msg("§7……你有没有觉得这一幕刚刚发生过？");
                    });
                    ctx.later(200, () -> ctx.msg("§8第三次了。你已经能背下这段对话。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_bed_bomb", "床下有问题", TrollTier.NIGHTMARE)
                .desc("你想休息，结果床下埋了东西")
                .weight(55)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x(), ctx.y(), ctx.z(), 40);
                    ctx.scatterFar(40);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 1.0f);
                    ctx.msg("§c你的床底下有点东西。§7它比你更想让你起床。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_blood_moon_hunt", "血月狩猎", TrollTier.NIGHTMARE)
                .desc("血月之下，成群的东西被派来专门找你")
                .weight(55)
                .cooldownSeconds(650)
                .run(ctx -> {
                    ctx.timeOfDay(18000);
                    ctx.title("§4§l狩猎开始", "§7今晚你是猎物");
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10);
                    ctx.msg("§4它们不是为了经验来的。§7它们是为了你来的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_sky_hole", "天空破了个洞", TrollTier.NIGHTMARE)
                .desc("天顶裂开一个洞，你的东西被吸了进去")
                .weight(50)
                .cooldownSeconds(700)
                .run(ctx -> {
                    ctx.voidPit(3, 12);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 2);
                    ctx.dropEverything();
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.msg("§8天上破了个洞。§7你的背包先一步跑路了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_killing_blizzard", "致命暴雪", TrollTier.NIGHTMARE)
                .desc("暴雪持续不断，你在里面寸步难行")
                .weight(50)
                .cooldownSeconds(650)
                .run(ctx -> {
                    ctx.setRaining(true, true, 6000);
                    ctx.freeze(400);
                    ctx.damage("freeze", 10.0f);
                    ctx.glitch(GlitchIds.FOV_ZOOM, 300, 2);
                    ctx.msg("§b暴雪没有要停的意思。§7你开始计算自己还能走几步。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_hail_catastrophe", "重雹灾", TrollTier.NIGHTMARE)
                .desc("冰雹大到能砸穿屋顶，持续造成伤害")
                .weight(50)
                .cooldownSeconds(600)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.damage("freeze", 4.0f);
                    }
                    ctx.placeFloor(ctx.pick(TrollPools.NASTY_BLOCKS), 5);
                    ctx.soundToAll(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§b冰雹开始不讲道理了。§7你头上已经没有一个安全的格子。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_lightning_named", "天雷点名", TrollTier.NIGHTMARE)
                .desc("闪电不再随机，它开始指名道姓地找你")
                .weight(50)
                .cooldownSeconds(600)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.lightning();
                    }
                    ctx.damage("lightning", 9.0f);
                    ctx.title("§e§l点 名", "§7" + ctx.playerName() + "，到你了");
                    ctx.msg("§e雷说它记得你的名字。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_great_eclipse", "大日食", TrollTier.NIGHTMARE)
                .desc("太阳被完全吞掉，白天变成了错误的时间")
                .weight(50)
                .cooldownSeconds(700)
                .run(ctx -> {
                    ctx.timeOfDay(18000);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 150, 2);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.msg("§8太阳被整个吞掉了。§7现在是白天，但没人敢这么说。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_time_collapse", "时间崩塌", TrollTier.NIGHTMARE)
                .desc("时间线互相打架，昼夜在几秒内来回切换")
                .weight(50)
                .cooldownSeconds(700)
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.timeSkip(3000);
                    }
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 200, 3);
                    ctx.effectRandom(400, 2);
                    ctx.msg("§8昼夜在互相打架。§7你分不清现在是几点，也不知道是哪天。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_storm_surge", "风暴潮", TrollTier.NIGHTMARE)
                .desc("风暴把水推了上来，整个区域被淹没")
                .weight(50)
                .cooldownSeconds(650)
                .run(ctx -> {
                    ctx.thunderstorm(4800);
                    ctx.placeFloor(ctx.pick(TrollPools.EVIL_BLOCKS), 5);
                    ctx.scatterBlocks(ctx.pick(TrollPools.NASTY_BLOCKS), 25);
                    ctx.damage("drown", 6.0f);
                    ctx.msg("§b风暴把水推上来了。§7你脚下已经没有干的地方。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_night_terror", "深夜恐惧", TrollTier.NIGHTMARE)
                .desc("独自一人的深夜，天空开始发出不属于它的声音")
                .weight(55)
                .cooldownSeconds(600)
                .when(ctx -> !ctx.hasOthers())
                .run(ctx -> {
                    ctx.timeOfDay(18000);
                    ctx.randomHorrorSound();
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 200, 2);
                    ctx.title("§8§l只有你听见了", "§7天空在低声说着什么");
                    ctx.msg("§8今晚没有别人。§7所以这件事只能你来处理。");
                })
                .build());

        // ================================================================
        // APOCALYPSE —— 地狱，天灾级（7 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("sky_falling_sky", "天塌了", TrollTier.APOCALYPSE)
                .desc("天空整片压下来，落点覆盖你的整片区域")
                .weight(35)
                .cooldownSeconds(1200)
                .run(ctx -> {
                    ctx.title("§4§l天 塌 了", "§7请就地祈祷，或者就地跑");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.meteorShower(24, 45);
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 6.0f);
                    ctx.glitch(GlitchIds.FAKE_BAN, 200, 3);
                    ctx.msg("§4天空决定不再承担支撑宇宙的义务。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_meteor_extinction", "陨石灭绝", TrollTier.APOCALYPSE)
                .desc("陨石成群落下，整片地表被反复清洗")
                .weight(30)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.timeOfDay(18000);
                    for (int i = 0; i < 4; i++) {
                        ctx.boomDestructive(ctx.x() + ctx.rd(-20, 20), ctx.y() + 4,
                                ctx.z() + ctx.rd(-20, 20), 5.0f);
                    }
                    ctx.meteorShower(20, 50);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.msg("§4这不是天气了。§7这是地质事件。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_endless_storm", "永不停歇的风暴", TrollTier.APOCALYPSE)
                .desc("风暴被设成永久，天亮这件事被无限期推迟")
                .weight(30)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.thunderstorm(24000);
                    ctx.timeOfDay(18000);
                    ctx.setFire(10);
                    ctx.damage("lightning", 10.0f);
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 200, 3);
                    ctx.msg("§4风暴将永远持续。§7进度条卡在 99%，和你的希望一样。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_sun_death", "太阳之死", TrollTier.APOCALYPSE)
                .desc("太阳当场死掉，世界进入永恒的冷却")
                .weight(30)
                .cooldownSeconds(1800)
                .run(ctx -> {
                    ctx.timeOfDay(18000);
                    ctx.lavaPit(4);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 1200, 3);
                    ctx.titleToAll("§8§l太阳已死", "§7从现在起，热是一种回忆");
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.msg("§4太阳死了。§7没有人知道该向谁报修。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_lightning_judgment", "雷罚", TrollTier.APOCALYPSE)
                .desc("天雷不再随机，它执行判决")
                .weight(30)
                .cooldownSeconds(1800)
                .run(ctx -> {
                    ctx.thunderstorm(12000);
                    for (int i = 0; i < 6; i++) {
                        ctx.lightning();
                    }
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 6.0f);
                    ctx.glitch(GlitchIds.FLASHBANG, 80, 3);
                    ctx.title("§e§l判 决", "§7天认为你有罪");
                    ctx.kill();
                    ctx.msg("§4雷罚执行完毕。§7申诉通道：无。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_time_end", "时间尽头", TrollTier.APOCALYPSE)
                .desc("时间停止前进，世界卡在最后一秒，再也回不去")
                .weight(25)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.timeOfDay(18000);
                    ctx.voidPit(4, 20);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 1800, 3);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 300, 3);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.msg("§4时间走到尽头了。§7接下来的一秒，会持续很久。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("sky_weather_finale", "天气系统终章", TrollTier.APOCALYPSE)
                .desc("天气系统决定谢幕，把全部极端天气一次性放完")
                .weight(25)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.titleToAll("§4§l天 气 终 章", "§7感谢你陪它走到最后");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.thunderstorm(24000);
                    ctx.setRaining(true, true, 24000);
                    ctx.meteorShower(28, 55);
                    ctx.lightning();
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 7.0f);
                    ctx.glitch(GlitchIds.FAKE_BAN, 300, 3);
                    ctx.msg("§4天气系统已卸任。§7它把所有的脾气都留给了你。");
                })
                .build());
    }
}
