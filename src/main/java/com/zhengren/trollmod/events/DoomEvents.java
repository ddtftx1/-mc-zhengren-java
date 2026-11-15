package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【末日与毁灭】分类事件（v3.0.0 残酷化·内容层）。
 *
 * <p>全场最狠的一包：陨石雨、虚空流放、岩浆浴场、假封禁、世界终章、全员末日。
 * 每条事件都同时拉动至少三套系统：天象/时间 + 地形 + 刷怪 + 客户端画面 + 药水 + 音效。</p>
 *
 * <p>v3.0.0 的内容层重点在于「末日是个过程，不是一下」：OBLIVION 档的每一场天灾都会先给一次
 * 撤销、一次救援、一次清场完毕，然后再把这份希望亲手碾碎。伤害在这里不是主角，
 * 反复燃起又熄灭的希望才是。</p>
 *
 * <p>本文件只使用 ctx / TrollPools / GlitchIds，不直接触碰任何 Minecraft API。</p>
 */
public final class DoomEvents {

    private DoomEvents() {
    }

    public static void register() {
        TrollEvents.category("末日与毁灭");

        // ==================== BRUTAL / ANNOYING ====================

        TrollEvents.reg(TrollEvent.of("doom_false_prophecy", "末日预言", TrollTier.BRUTAL)
                .desc("广播、标题、阴森音效一起放，预言时间写着「永远不会」——但你已经开始等它了")
                .weight(120)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.exhaust(12.0f);
                    ctx.fakeBroadcast(ctx.pick(TrollPools.FAKE_BROADCASTS));
                    ctx.title("§4§l末日将至", "§7预计抵达时间：永远不到");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 0.8f);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "预言是真的，我从不骗人");
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.later(200, () -> ctx.msg("§8[预言] 倒计时：永远。也就是说，你会一直等。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_diamond_miracle", "钻石神迹", TrollTier.ANNOYING)
                .desc("发几件好东西、弹假成就、撒爱心粒子——纯粹为了让你高兴三秒，再惦记很久")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 3);
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_ACHIEVEMENTS), "挖到了三颗闪亮的东西");
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.later(120, () -> ctx.msg("§7（我们很快就会为这次的慷慨后悔——你也是。）"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_tip_of_the_day", "末日小贴士", TrollTier.ANNOYING)
                .desc("假加载提示、假自动保存、气人音效、系统嘲讽，三套界面系统一起骚扰你")
                .weight(100)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.actionBar(ctx.pick(TrollPools.FAKE_TIPS));
                    ctx.fakeSaveNotice();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.3f);
                    ctx.sys(ctx.pick(TrollPools.TAUNTS));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_ash_rain", "灰烬之雨", TrollTier.ANNOYING)
                .desc("满屏白灰、随机药水、阴森音效，让你以为世界正在烧成灰——灰是真的，火还没有")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 80);
                    ctx.effectRandom(200, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.1f);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "天上下灰了，你还好吗");
                    ctx.actionBar("§8空气里全是灰，你的肺表示强烈抗议。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_weather_mood", "天气心情", TrollTier.NIGHTMARE)
                .desc("随机天气 + 时间跳跃 + 顺手抽走两件东西，天空归模组管，不归你管")
                .weight(130)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.takeRandomItems(2);
                    ctx.setWeatherRandom();
                    ctx.timeSkip(ctx.ri(-6000, 6000));
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 0.9f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.actionBar("§7天气已由模组重新分配，不接受申诉。丢失的物品同理。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_quest_board", "假任务板", TrollTier.NIGHTMARE)
                .desc("弹一个根本不存在的任务并自动接受，形势转化为饥饿，且任务永远不会完成")
                .weight(120)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.starve();
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_QUESTS), "目标：活着");
                    ctx.giveRandomJunk();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.later(160, () -> ctx.sys("任务进度：0%。失败条件：你已经满足了。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_low_gravity", "低重力演练", TrollTier.NIGHTMARE)
                .desc("失重加弹射，工具被顺手掰坏；它想让你体验当一颗土豆，而且是一颗会摔的土豆")
                .weight(110)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.breakRandomTool();
                    ctx.noGravity(120);
                    ctx.launch(0.7);
                    ctx.setSprinting(false);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.4f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 30);
                    ctx.title("§b§l重力已离线", "§7落地时间未知，落点也不由你");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_save_scare", "假保存惊吓", TrollTier.BRUTAL)
                .desc("假保存界面 + 客户端假保存提示，你被抽走一件东西，而系统说进度真的存了")
                .weight(130)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.takeRandomItems(1);
                    ctx.fakeSaveNotice();
                    ctx.glitch(GlitchIds.FAKE_SAVING, 60, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.later(80, () -> ctx.msg("§7世界数据已保存。你丢掉的那件，不在存档范围内。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_junk_rain", "杂物天降", TrollTier.BRUTAL)
                .desc("头顶连掉三块恶心方块、铁砧巨响加缓慢药水，物理伤害与心理伤害齐飞")
                .weight(58)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.spawnFallingBlock(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.spawnFallingBlock(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.spawnFallingBlock(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.8f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 1);
                    ctx.title("§4§l天降杂物", "§7都是给你的，别客气");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_mount_escape", "强制上马", TrollTier.BRUTAL)
                .desc("强制骑上一只不知道是什么的东西，然后把你甩出去，末尾再安排一只追兵")
                .weight(55)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.mount(ctx.pick(TrollPools.RIDEABLE));
                    ctx.setSprinting(true);
                    ctx.fling(0.0, 1.1, 0.0);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.2f, 0.9f);
                    ctx.actionBar("§e你的坐骑不归你管，你的方向也不归你管。");
                })
                .build());

        // ==================== NIGHTMARE ====================

        TrollEvents.reg(TrollEvent.of("doom_meteor_warning", "陨石预警", TrollTier.NIGHTMARE)
                .desc("预警标题 + 巨响 + 陨石雨 + 雷暴：警告发完了，陨石也到了，顺序只是礼貌")
                .weight(65)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.title("§4§l陨石接近中", "§c抬头，或者不抬，都一样");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.2f, 0.8f);
                    ctx.meteorShower(3, 14.0);
                    ctx.thunderstorm(600);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 30);
                    ctx.later(140, () -> ctx.warn("预警结束。感谢你在预警期间保持原地不动。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_cursed_loot", "诅咒战利品", TrollTier.NIGHTMARE)
                .desc("塞你一件神秘物品、改个羞辱名字、挂上消失诅咒，再把你送到百格外")
                .weight(58)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.scatterFar(120);
                    ctx.give(ctx.pick(TrollPools.MYSTERY_ITEMS), 1);
                    ctx.renameRandomItem(ctx.pick(TrollPools.CURSED_NAMES));
                    ctx.curseHeld();
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.1f, 0.9f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 30);
                    ctx.warn("好东西都带点代价，这不是常识吗。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_lava_arena", "熔岩竞技场", TrollTier.NIGHTMARE)
                .desc("脚下铺满岩浆、缓慢加凋零、背后贴脸刷怪，全服标题宣布第一轮选手是你")
                .weight(30)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.lavaPit(3);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.titleToAll("§c§l熔岩竞技场开赛", "§7第一轮选手：你");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.3f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_input_slavery", "输入奴隶", TrollTier.NIGHTMARE)
                .desc("前后左右反转 + 输入冻结 + 灵敏度拉满 + 强制潜行，再顺手放两只怪进来")
                .weight(30)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 320, 3);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 200, 2);
                    ctx.glitch(GlitchIds.SENS_MAX, 240, 2);
                    ctx.freeze(240);
                    ctx.setSneaking(true);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 0.7f);
                    ctx.warn("你的手现在归模组管，方向感也是。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_sky_hammer", "天锤", TrollTier.NIGHTMARE)
                .desc("高空陨石 + 双闪雷电 + 剧烈甩镜 + 天罚标题，这不是比喻")
                .weight(31)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.meteorShower(5, 32.0);
                    ctx.lightningAt(ctx.x() + ctx.ri(-5, 5), ctx.y(), ctx.z() + ctx.ri(-5, 5));
                    ctx.lightningAt(ctx.x() + ctx.ri(-5, 5), ctx.y(), ctx.z() + ctx.ri(-5, 5));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.4f, 1.0f);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 120, 3);
                    ctx.title("§4§l天锤落下", "§7请保持站立，方便瞄准");
                })
                .build());

        // ==================== APOCALYPSE ====================

        TrollEvents.reg(TrollEvent.of("doom_sinkhole_trap", "地陷陷阱", TrollTier.APOCALYPSE)
                .desc("脚下挖空六格、背后贴脸刷怪、缓慢药水，你家地基已被回收，回收理由不公开")
                .weight(60)
                .cooldownSeconds(300)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(2, 6);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.7f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 40);
                    ctx.warn("你家地基已被回收，感谢配合城市建设。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_lava_bath", "岩浆浴场", TrollTier.APOCALYPSE)
                .desc("脚下铺岩浆 + 抬高三格 + 着火 + 缓慢，一条龙洗浴服务，水温由它决定")
                .weight(55)
                .cooldownSeconds(320)
                .when(ctx -> !ctx.isInLava())
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.lift(3.0);
                    ctx.setFire(4);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 1.0f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 40);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_tnt_gift", "礼物是TNT", TrollTier.APOCALYPSE)
                .desc("先送你五件「礼物」，再在你脚边点着两枚，配点火音效和画面抖动")
                .weight(60)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.meteorShower(2, 12.0);
                    ctx.give(ctx.pick(TrollPools.DANGEROUS), 5);
                    ctx.ok("服务器送你 5 件礼物，请签收（附赠两份已点燃的）。");
                    ctx.spawnTnt(ctx.x() + ctx.ri(-3, 3), ctx.y() + 1.0, ctx.z() + ctx.ri(-3, 3), 40);
                    ctx.spawnTnt(ctx.x() + ctx.ri(-3, 3), ctx.y() + 1.0, ctx.z() + ctx.ri(-3, 3), 60);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 1.0f);
                    ctx.glitch(GlitchIds.SHAKY, 60, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_haunted_mines", "幽灵矿脉", TrollTier.APOCALYPSE)
                .desc("周围撒一堆假矿、经验被清零、失明加身；挖不挖都是坑，因为它根本不在矿里")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.setExperience(0);
                    ctx.scatterBlocks(ctx.pick(TrollPools.ORE_TEASE), 10);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "你旁边那堆钻石是我放的");
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "挖吧，挖到算你厉害");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.1f, 0.9f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 100, 0);
                    ctx.msg("§7（它们看起来是真的，摸起来也是真的，可惜不值钱）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_freeze_field", "冰封战场", TrollTier.APOCALYPSE)
                .desc("冻结定身 + 三向刷怪 + 环境恐怖音 + 白灰粒子，你只能站着挨打，站着是唯一选项")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.freeze(200);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.2f, 0.8f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 60);
                    ctx.actionBar("§b你的关节已被模组接管。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_pit_and_punish", "陷坑惩罚", TrollTier.APOCALYPSE)
                .desc("挖坑、前后夹击刷怪、缓慢药水、巨响加恐怖粒子，掉下去只是流程第一步")
                .weight(62)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.voidPit(1, 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.8f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_trap_garden", "陷阱花园", TrollTier.APOCALYPSE)
                .desc("周围撒满陷阱方块、随机药水、陨石两发，假广播说环境美化完成")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.meteorShower(2, 12.0);
                    ctx.scatterBlocks(ctx.pick(TrollPools.TRAP_BLOCKS), 14);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                    ctx.effectRandom(300, 0);
                    ctx.title("§6§l你家装修好了", "§7免费，不用谢");
                    ctx.fakeBroadcast("§e检测到玩家周边环境美化工程已完成。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_endless_night", "无尽长夜", TrollTier.APOCALYPSE)
                .desc("直接拨到午夜、雷暴拉满、三向刷怪、黑暗药水——太阳今天不排班，明天也不")
                .weight(65)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.timeOfDay(18000L);
                    ctx.thunderstorm(2400);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.2f, 0.7f);
                    ctx.title("§8§l太阳已下架", "§7今晚没有月亮，也没有明天");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_siege_line", "围城线", TrollTier.APOCALYPSE)
                .desc("四向刷怪、虚弱药水、全服巨响加公告，包围圈形成后不接受投降，也不接受解释")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 0);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.9f);
                    ctx.sendToAll("§c[末日] 包围圈已形成，请勿走动，走了也没用。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_meteor_storm", "陨石风暴", TrollTier.APOCALYPSE)
                .desc("八连陨石 + 雷暴 + 全服标题 + 全服巨响 + 画面撕裂，坏消息由全服共享")
                .weight(35)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.damage("outofworld", 3.0f);
                    ctx.meteorShower(8, 24.0);
                    ctx.thunderstorm(1200);
                    ctx.titleToAll("§4§l陨石风暴", "§c本次天灾为全服共享套餐");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.4f, 0.7f);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 120, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_void_exile", "虚空流放", TrollTier.APOCALYPSE)
                .desc("脚下挖空十二格、浮空、两向刷怪、硬黑屏，最后告诉你地基是自愿离开的")
                .weight(32)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.voidPit(3, 12);
                    ctx.noGravity(60);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 100, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.4f, 0.6f);
                    ctx.warn("你脚下那块地我们已经回收了，连同地基一起。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_obsidian_tomb", "黑曜石棺材", TrollTier.APOCALYPSE)
                .desc("黑曜石原地封棺、岩浆铺底、失明、血量压到四颗心，出口只有重生点——如果它允许")
                .weight(34)
                .cooldownSeconds(480)
                .when(ctx -> ctx.isOnGround() && !ctx.isInLava())
                .run(ctx -> {
                    ctx.buryAlive();
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 2);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 2);
                    ctx.setHealth(Math.min(ctx.health(), 8.0f));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.6f);
                    ctx.warn("棺材用黑曜石打的，算是对你审美的一点尊重。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_tnt_chain", "连锁爆破", TrollTier.APOCALYPSE)
                .desc("三枚延时 TNT 布成三角 + 原地无伤爆炸 + 缓慢药水 + 剧烈甩镜 + 结局级巨响")
                .weight(34)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x() + 4.0, ctx.y() + 1.0, ctx.z(), 30);
                    ctx.spawnTnt(ctx.x() - 4.0, ctx.y() + 1.0, ctx.z(), 45);
                    ctx.spawnTnt(ctx.x(), ctx.y() + 1.0, ctx.z() + 4.0, 60);
                    ctx.boom(ctx.x(), ctx.y() + 1, ctx.z(), 3.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 1);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 80, 3);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.4f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_swap_doom", "换位末日", TrollTier.APOCALYPSE)
                .desc("和另一名玩家互换位置，把人扔到三十格外，背包清空，全服上眩晕")
                .weight(30)
                .cooldownSeconds(480)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    ctx.clearInventory();
                    var o = ctx.other();
                    if (o != null) {
                        ctx.swapWith(o);
                        ctx.teleportOtherTo(o, ctx.x() + 30.0, ctx.y() + 12.0, ctx.z() + 30.0);
                        ctx.giveOther(o, ctx.pick(TrollPools.DANGEROUS), 1);
                        ctx.msgToOther(o, "§7刚才那一下不是我干的，真的。");
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                        ctx.titleToAll("§5§l位置已重新分配", "§7谁站哪儿由模组说了算");
                        ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.2f, 0.9f);
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_meteor_apocalypse", "陨石天启", TrollTier.APOCALYPSE)
                .desc("二十连陨石 + 雷暴 + 午夜 + 全服凋零 + 假崩溃，天灾由全服平摊，谁也别想独享")
                .weight(18)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.meteorShower(20, 40.0);
                    ctx.thunderstorm(4800);
                    ctx.timeOfDay(18000L);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.titleToAll("§4§l陨石天启", "§4全服共享，一个都跑不掉");
                    ctx.glitch(GlitchIds.FAKE_CRASH, 140, 3);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_no_escape", "无处可逃", TrollTier.APOCALYPSE)
                .desc("封场、脚下铺岩浆、冻结定身、凋零加身、血量压到一格半，出口只剩重生点")
                .weight(16)
                .cooldownSeconds(900)
                .once()
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.cageWith(ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.lavaPit(1);
                    ctx.freeze(300);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 2);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 140, 3);
                    ctx.setHealth(3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.5f, 0.5f);
                    ctx.warn("这间屋子没有门，只有一条出路叫死亡。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_mass_extinction", "万物灭绝", TrollTier.APOCALYPSE)
                .desc("十六连陨石 + 八向怪潮 + 全服凋零 + 海量刷屏 + 灭绝公告：没有幸存者名单")
                .weight(14)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.meteorShower(16, 26.0);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.glitch(GlitchIds.MASS_CHAT, 200, 3);
                    ctx.titleToAll("§4§l万物灭绝", "§8本次没有幸存者名单");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.6f, 0.5f);
                })
                .build());

        // ==================== OBLIVION —— 希望是工具，不是礼物 ====================

        TrollEvents.reg(TrollEvent.of("doom_fake_ban_wave", "假封禁潮", TrollTier.OBLIVION)
                .desc("先封禁你、再宣布「误封已撤销」，让你松一口气——真正的封禁在你放松的那一刻落下")
                .weight(33)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.buryAlive();
                    ctx.effectRandom(1200, 3);
                    ctx.voidPit(2, 16);
                    ctx.glitch(GlitchIds.FAKE_BAN, 280, 3);
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 240, 2);
                    ctx.glitch(GlitchIds.MASS_CHAT, 200, 2);
                    ctx.fakeBroadcast("§c封禁理由：" + ctx.pick(TrollPools.FAKE_EXCUSES));
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.3f, 0.7f);
                    ctx.later(300, () -> ctx.ok("复核完成：本次为误封，封禁已撤销。欢迎回来。"));
                    ctx.later(480, () -> {
                        ctx.warn("§4复核对象搞错了。撤销的是别人的。你的封禁刚刚生效。");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_world_reload_loop", "假重载循环", TrollTier.OBLIVION)
                .desc("重载进度 0% → 99% → 0% 循环三次，每次都告诉你「快好了」，第三次后不再有第四次")
                .weight(32)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.buryAlive();
                    ctx.effectRandom(1200, 3);
                    ctx.voidPit(2, 16);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 320, 3);
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 240, 2);
                    ctx.fakeChat("服务器管理员", ctx.pick(TrollPools.FAKE_OPERATOR_LINES));
                    ctx.fakeSaveNotice();
                    ctx.later(140, () -> ctx.actionBar("§e重载进度 99%……快好了。"));
                    ctx.later(260, () -> ctx.actionBar("§c校验失败，进度回滚至 0%。请再等一次。"));
                    ctx.later(380, () -> ctx.actionBar("§e重载进度 99%……这次真的快好了。"));
                    ctx.later(500, () -> {
                        ctx.actionBar("§4不会再有下一次了。");
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 200, 2);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_grave_collapse", "墓地塌陷", TrollTier.OBLIVION)
                .desc("墓地先自己塌了，然后「救援队」赶到——救援的方式是把你埋得更深一点")
                .weight(33)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.voidPit(4, 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.effectRandom(600, 2);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 60);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.3f, 0.6f);
                    ctx.fakeBroadcast("§8本地块的承载能力已到期，请自行处理坠落。");
                    ctx.later(200, () -> {
                        ctx.ok("救援队已抵达，正在将你转移至安全区域。");
                        ctx.resetFallDistance();
                    });
                    ctx.later(380, () -> {
                        ctx.warn("§4转移完成：你被移到了坑底。安全区域是指没有目击者的地方。");
                        ctx.buryAlive();
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_flood_of_beasts", "兽潮", TrollTier.OBLIVION)
                .desc("六向刷怪、全员被拉到一处、全服失明；它会宣布「清场完毕」，然后放出第二波")
                .weight(35)
                .cooldownSeconds(460)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.pullPlayersToMe(64.0);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                    ctx.titleToAll("§4§l兽潮", "§7它们只对活物感兴趣");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.3f, 0.8f);
                    ctx.later(220, () -> ctx.titleToAll("§a§l清场完毕", "§7第一波已经处理干净"));
                    ctx.later(360, () -> {
                        ctx.titleToAll("§0§l第二波", "§8第一波是开胃菜，你们是主食");
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 3);
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 3);
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_world_end", "世界终章", TrollTier.OBLIVION)
                .desc("十二连陨石 + 挖空十六格 + 岩浆铺地 + 假存档损坏 + 全服悼词；世界会重生，但里面没有你")
                .weight(15)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.meteorShower(12, 30.0);
                    ctx.voidPit(4, 16);
                    ctx.lavaPit(3);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 200, 3);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 160, 3);
                    ctx.sendToAll("§4§l[末日] 世界数据正在被重写，请不要保存，反正也保存不了。");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.5f);
                    ctx.later(180, () -> ctx.sendToAll("§a[系统] 世界重写完成，新世界已生成。"));
                    ctx.later(360, () -> {
                        ctx.sendToAll("§4[系统] 新世界的玩家名单里没有你。你不在任何一份名单里。");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_judgment_day", "审判日", TrollTier.OBLIVION)
                .desc("全服凋零加黑暗、雷劈其他玩家；审判结束会给你一次「申诉」，申诉的唯一结果是加刑")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 0);
                    var o = ctx.other();
                    if (o != null) {
                        ctx.lightningAtPlayer(o);
                        ctx.damageOther(o, 4.0f);
                    }
                    ctx.lightningNear();
                    ctx.titleToAll("§4§l审判日", "§7你的所有行为都已被记录（并且会算在你头上）");
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 160, 3);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.6f);
                    ctx.later(240, () -> ctx.sys("判决：有罪。你可以在 3 秒内提出申诉。"));
                    ctx.later(320, () -> {
                        ctx.warn("申诉已受理。申诉被视为认罪，刑期加倍。");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_singularity", "奇点坍缩", TrollTier.OBLIVION)
                .desc("挖空二十格、失重浮空、满屏传送门粒子；它说「你被拉回来了」，拉回来的只有坐标")
                .weight(20)
                .cooldownSeconds(720)
                .run(ctx -> {
                    ctx.voidPit(5, 20);
                    ctx.noGravity(200);
                    ctx.effectRandom(1200, 3);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 400, 3);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 100);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.5f, 0.5f);
                    ctx.title("§5§l奇点坍缩", "§7欢迎来到没有地面的世界");
                    ctx.later(220, () -> {
                        ctx.ok("检测到你的坐标，正在把你拉回来……");
                        ctx.playLevelUp();
                    });
                    ctx.later(400, () -> {
                        ctx.title("§0§l坐标已找回", "§8找回的是坐标。人没有。");
                        ctx.stripAll();
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_hell_on_earth", "人间地狱", TrollTier.OBLIVION)
                .desc("岩浆 + 雷暴 + 午夜 + 四向怪潮 + 凋零；它会宣布「地狱难度已下调」，下调的是你的抗性")
                .weight(19)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.thunderstorm(3600);
                    ctx.timeOfDay(18000L);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.glitch(GlitchIds.FAKE_OOM, 120, 2);
                    ctx.later(260, () -> ctx.ok("检测到难度过高，已为你下调地狱难度。"));
                    ctx.later(400, () -> {
                        ctx.warn("§4难度下调完成：你的抗性被移除了。地狱还是那个地狱。");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_countdown", "末日倒计时", TrollTier.OBLIVION)
                .desc("数到零，它说「开玩笑的」，你刚松手，真正的末日才开始——希望是用来延长痛苦的")
                .weight(21)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.stripAll();
                    ctx.buryAlive();
                    ctx.effectRandom(1200, 3);
                    ctx.titleToAll("§4§l3", "§c最后三秒，请保持体面");
                    ctx.fakeDeathScreen(ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 140, 3);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.6f);
                    ctx.later(120, () -> ctx.titleToAll("§4§l1", "§c最后…一…秒"));
                    ctx.later(200, () -> {
                        ctx.titleToAll("§a§l开玩笑的", "§7倒计时取消。你可以继续了。");
                        ctx.playLevelUp();
                    });
                    ctx.later(380, () -> {
                        ctx.titleToAll("§0§l真正的倒计时现在开始", "§8刚才那次只是排练你的表情");
                        ctx.meteorShower(8, 30.0);
                        ctx.sendToAll("§4[末日] 倒计时结束后请自行处理后果。");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_apocalypse_protocol", "末日协议", TrollTier.OBLIVION)
                .desc("全员被拉到一处、全服失明、脚下铺岩浆、五向怪潮；协议会「终止」，终止的是你的豁免")
                .weight(17)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.pullPlayersToMe(120.0);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 160, 1);
                    ctx.lavaPit(2);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.titleToAll("§4§l末日协议已启动", "§7所有玩家合并进同一个坏消息");
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 120, 2);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.6f);
                    ctx.later(240, () -> ctx.titleToAll("§a§l协议已终止", "§7威胁已解除，各位安全了。"));
                    ctx.later(400, () -> {
                        ctx.titleToAll("§0§l终止的是你的豁免", "§8协议还在跑。只是不再保护任何人了。");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_extinction_event", "灭绝事件", TrollTier.OBLIVION)
                .desc("十连陨石 + 监守者入场 + 存活率 0% 公告；随后它宣布「统计错误，实际是 100%」——统计的是它自己")
                .weight(16)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.meteorShower(10, 35.0);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 200, 2);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 2);
                    ctx.glitch(GlitchIds.FAKE_BAN, 140, 3);
                    ctx.sendToAll("§4[灭绝事件] 本次事件的存活率已统计完毕：0%。");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.6f, 0.5f);
                    ctx.later(200, () -> {
                        ctx.ok("[更正] 统计出现错误。实际存活率：100%。");
                        ctx.heal(4.0f);
                    });
                    ctx.later(360, () -> {
                        ctx.sendToAll("§4[更正] 上述 100% 指的是本事件自身的存活率。与玩家无关。");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_final_save", "最终保存", TrollTier.OBLIVION)
                .desc("它认真地对你说「数据已保存」，并打开回收站给你看——里面是空的，一直都是空的")
                .weight(13)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.buryAlive();
                    ctx.obliterate();
                    ctx.fakeSaveNotice();
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 200, 3);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 160, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 120, 3);
                    ctx.sendToAll("§4§l[系统] 世界已保存。玩家数据：已归档进模组的收藏夹。");
                    ctx.later(220, () -> ctx.fakeBroadcast("§e正在打开归档目录……"));
                    ctx.later(360, () -> ctx.sendToAll("§4[归档] 目录为空。收藏夹里什么都没有，从来都没有。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_last_light", "最后一缕光", TrollTier.OBLIVION)
                .desc("午夜、全服黑暗、挖空十四格、硬黑屏；然后光回来了——是别人关灯时漏进来的")
                .weight(22)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.voidPit(3, 14);
                    ctx.obliterate();
                    ctx.timeOfDay(18000L);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 400, 0);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 140, 3);
                    ctx.soundToAll(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.4f, 0.6f);
                    ctx.titleToAll("§8§l最后一缕光", "§7然后就没有然后了");
                    ctx.later(240, () -> {
                        ctx.clearEffects();
                        ctx.titleToAll("§e§l光回来了", "§7你还有机会。");
                        ctx.playLevelUp();
                    });
                    ctx.later(400, () -> {
                        ctx.titleToAll("§0§l那只是别人关灯", "§8光源从来不属于你。");
                        ctx.obliterate();
                    });
                })
                .build());
    }
}
