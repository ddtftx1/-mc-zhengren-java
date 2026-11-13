package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【画面崩溃】分类事件（需要客户端也装了本模组）。
 * 负责整活：假崩溃、假掉线、假内存溢出、假存档损坏、隐藏 HUD、鼠标反转、
 * 灵敏度归零、视野乱变、黑屏、刷屏、跳回标题界面、快捷栏锁死乱跳……
 * 本文件只使用 ctx / TrollPools / GlitchIds，不直接触碰任何 Minecraft API。
 */
public final class GlitchEvents {

    private GlitchEvents() {
    }

    public static void register() {
        TrollEvents.category("画面崩溃");

        // ==================================================================
        // PRANK —— 小整，笑一下就好
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("glitch_hud_blink", "界面打了个哈欠", TrollTier.PRANK)
                .desc("HUD 消失三秒又回来，让你怀疑是自己眼花")
                .weight(160)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.msg("§7你的界面好像……刚才闪了一下？");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 1.6f);
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 3, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_shaky_knee", "腿有点软", TrollTier.PRANK)
                .desc("画面轻微抖动五秒，像低血糖")
                .weight(150)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.actionBar("§7……有点站不稳");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.6f, 1.4f);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 5, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_spin_tiny", "原地转半圈", TrollTier.PRANK)
                .desc("视角温柔地转一会儿，纯属手滑")
                .weight(130)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.msg("§7刚才谁碰了一下你的鼠标？");
                    ctx.glitch(GlitchIds.SPIN, 20 * 4, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_fov_breath", "镜头在呼吸", TrollTier.PRANK)
                .desc("视野像呼吸一样伸缩，看着有点晕但不是我的问题")
                .weight(130)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.actionBar("§8焦距异常，已尝试自动对焦");
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 4, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_scroll_lock_liar", "卷不动了", TrollTier.PRANK)
                .desc("假提示说快捷栏被锁，其实就是吓吓你")
                .weight(120)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.sys("快捷栏已被服务器锁定（假的）。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 20 * 3, 1);
                })
                .build());

        // ==================================================================
        // ANNOYING —— 烦人，会皱眉
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("glitch_hud_vanished", "HUD 已离家出走", TrollTier.ANNOYING)
                .desc("隐藏整个 HUD 二十秒：血量、饥饿、快捷栏全都不见")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.msg("§e正在优化你的界面……优化没了。");
                    ctx.actionBar("§cHUD 加载失败，请凭感觉游戏");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 20, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_mouse_mirror", "鼠标上下颠倒了", TrollTier.ANNOYING)
                .desc("反转鼠标 Y 轴二十五秒，你的大脑会先崩")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.msg("§e检测到鼠标驱动版本过旧，已自动反转校准。");
                    ctx.title("§e手感不对？", "§7对，就是不对");
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 25, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_sens_gone", "灵敏度归零", TrollTier.ANNOYING)
                .desc("鼠标灵敏度调到 0 十五秒，转个头像搬砖")
                .weight(110)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.warn("鼠标灵敏度已设置为 0。祝你好运。");
                    ctx.glitch(GlitchIds.SENS_ZERO, 20 * 15, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_hotbar_jive", "快捷栏蹦迪", TrollTier.ANNOYING)
                .desc("快捷栏选中的格子自己乱跳二十秒")
                .weight(120)
                .cooldownSeconds(90)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.msg("§7你的键盘好像有自己的想法。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.9f, 1.3f);
                    ctx.glitch(GlitchIds.HOTBAR_JUMP, 20 * 20, 1);
                    ctx.randomSelectSlot();
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_fov_squint", "失焦了", TrollTier.ANNOYING)
                .desc("视野锁死在 28，像三天没睡在盯屏幕")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.sys("检测到长时间用眼，已为你调近镜头。");
                    ctx.glitch(GlitchIds.FOV_ZOOM, 20 * 18, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_screen_slam", "啪，界面没了", TrollTier.ANNOYING)
                .desc("强制关掉你正在开的箱子/工作台，手速白费")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.closeScreen();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.8f, 1.5f);
                    ctx.msg("§e界面被风吹走了。");
                    ctx.glitch(GlitchIds.CLOSE_SCREEN, 20 * 4, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_chat_flood", "聊天栏发大水", TrollTier.ANNOYING)
                .desc("聊天栏被垃圾信息刷满二十秒")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.warn("服务器正在同步聊天记录，请稍候。");
                    ctx.glitch(GlitchIds.CHAT_SPAM, 20 * 20, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_whisper_loop", "耳边有人说话", TrollTier.ANNOYING)
                .desc("随机恐怖音效持续轰炸，假装是客户端在闹鬼")
                .weight(115)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.msg("§8……你听见了吗？");
                    ctx.randomHorrorSound();
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 25, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_nausea_swirl", "喝多了的感觉", TrollTier.ANNOYING)
                .desc("持续眩晕式视角乱转，屏幕自己在晃")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.actionBar("§7画面在转，但你的椅子没转");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.8f, 0.9f);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 20, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_anti_aim", "反向瞄准", TrollTier.ANNOYING)
                .desc("上下视角反转，射箭时请自求多福")
                .weight(105)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.warn("瞄准辅助已开启（反向模式）。");
                    ctx.glitch(GlitchIds.INVERT_PITCH, 20 * 18, 2);
                })
                .build());

        // ==================================================================
        // BRUTAL —— 抓狂，会骂人
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("glitch_spin_hell", "滚筒洗衣机", TrollTier.BRUTAL)
                .desc("视角高速持续旋转二十五秒，方向感直接报废")
                .weight(60)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.title("§4§l旋转吧", "§7晕了别怪我");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.SPIN, 20 * 25, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_mouse_chaos", "鼠标彻底疯了", TrollTier.BRUTAL)
                .desc("鼠标反转 + 视角上下颠倒 + 灵敏度归零，三连击")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.warn("§c输入设备已被接管。");
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 25, 3);
                    ctx.glitch(GlitchIds.INVERT_PITCH, 20 * 25, 3);
                    ctx.glitch(GlitchIds.SENS_ZERO, 20 * 20, 3);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_hotbar_prison", "快捷栏封印", TrollTier.BRUTAL)
                .desc("快捷栏被锁死还附带乱跳，换不了武器只能挨打")
                .weight(55)
                .cooldownSeconds(240)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.warn("§c你的快捷栏已被管理员封印。");
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 20 * 22, 3);
                    ctx.glitch(GlitchIds.HOTBAR_JUMP, 20 * 22, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_blackout_whisper", "谁把灯关了", TrollTier.BRUTAL)
                .desc("长时间黑屏，只留心跳般的音效陪你")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.msg("§8停电通知：本区域将在今晚检修。");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 15, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_fov_madness", "焦距失控", TrollTier.BRUTAL)
                .desc("视野疯狂伸缩二十二秒，看得人太阳穴直跳")
                .weight(55)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.actionBar("§c镜头马达故障，正在自动检修……");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.7f);
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 22, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_sens_overflow", "灵敏度爆炸", TrollTier.BRUTAL)
                .desc("灵敏度拉满再叠一层鼠标反转，轻轻一动转三圈")
                .weight(50)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.warn("§c鼠标灵敏度已设置为 2000%。");
                    ctx.glitch(GlitchIds.SENS_MAX, 20 * 20, 3);
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 20, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_flash_slam", "闪瞎 + 甩飞", TrollTier.BRUTAL)
                .desc("闪白巨响外加一次性剧烈甩视角，眼睛和方向一起没")
                .weight(55)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.title("§f§l§k恭喜§r", "§7奖励一次眼部护理");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 30);
                    ctx.glitch(GlitchIds.FLASHBANG, 20 * 6, 3);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 20 * 6, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_death_notice", "你已经死了", TrollTier.BRUTAL)
                .desc("弹出假死亡提示，让你先崩溃再发现人还活着")
                .weight(60)
                .cooldownSeconds(300)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.msg("§c§l你死了！");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.glitch(GlitchIds.FAKE_DEATH, 20 * 20, 2);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "§7笑死，又躺了");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_oom_warning", "内存溢出警告", TrollTier.BRUTAL)
                .desc("假内存溢出界面，弹得跟真的一样")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.sys("Java 堆内存不足，正在释放你的耐心。");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.FAKE_OOM, 20 * 25, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_hud_extinction", "界面彻底消失", TrollTier.BRUTAL)
                .desc("HUD 消失一分钟再配刷屏，纯纯的信息黑洞")
                .weight(45)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.title("§8§l界面已卸载", "§7请使用第六感游玩");
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 60, 3);
                    ctx.glitch(GlitchIds.CHAT_SPAM, 20 * 25, 2);
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                })
                .build());

        // ==================================================================
        // NIGHTMARE —— 崩溃，会砸键盘
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("glitch_fake_crash_report", "游戏崩了（假）", TrollTier.NIGHTMARE)
                .desc("弹出完整的假崩溃报告界面，点退出会真的退出游戏")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.msg("§7渲染线程好像出了问题……");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 20 * 30, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_fake_kick_reason", "你被踢了（假）", TrollTier.NIGHTMARE)
                .desc("假掉线界面配一条屈辱的踢出理由")
                .weight(35)
                .cooldownSeconds(480)
                .run(ctx -> {
                    String reason = ctx.pick(new String[]{
                            "登出：因为你太菜了",
                            "与服务器失去连接：你的手速超时",
                            "被管理员移出游戏：原因你心里清楚",
                            "网络异常：检测到你在用土豆上网"
                    });
                    ctx.msg("§c连接中断。");
                    ctx.glitch(GlitchIds.FAKE_KICK, 20 * 25, 3);
                    ctx.fakeBroadcast("§c玩家 " + ctx.playerName() + " 已被请出服务器（" + reason + "）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_menu_ghost", "回到标题界面", TrollTier.NIGHTMARE)
                .desc("画面跳回标题菜单，其实你还在原地看着空屏发呆")
                .weight(28)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.msg("§7正在保存并退出世界……");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.glitch(GlitchIds.MENU_TEASE, 20 * 25, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_save_corrupted", "存档损坏（假）", TrollTier.NIGHTMARE)
                .desc("假存档损坏界面，看一眼血压直接上来")
                .weight(35)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.warn("§c检测到存档区块校验失败。");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 28, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_black_death", "黑屏里的死亡", TrollTier.NIGHTMARE)
                .desc("黑屏叠假死亡提示，视觉与心理的双重打击")
                .weight(40)
                .cooldownSeconds(420)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.title("§4§l别关灯", "§8你会看不见自己怎么死的");
                    ctx.randomHorrorSound();
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 18, 3);
                    ctx.glitch(GlitchIds.FAKE_DEATH, 20 * 18, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_display_collapse", "显示器要坏了", TrollTier.NIGHTMARE)
                .desc("抖动 + 视野乱变 + 闪烁，演一出显卡起火")
                .weight(45)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.warn("§c显卡温度 118°C，风扇已阵亡。");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 20, 3);
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 20, 3);
                    ctx.glitch(GlitchIds.FLASHBANG, 20 * 4, 2);
                })
                .build());

        // ==================================================================
        // APOCALYPSE —— 地狱，会退游戏
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("glitch_total_meltdown", "全面熔毁", TrollTier.APOCALYPSE)
                .desc("假崩溃 + 闪白 + 黑屏 + 甩视角，整局只来一次的组合技")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.title("§4§l致命错误", "§c你的世界正在蒸发");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FLASHBANG, 20 * 5, 3);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 20 * 8, 3);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 12, 3);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 20 * 30, 3);
                    ctx.randomHorrorSound();
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_kick_menu_combo", "踢出去再退出去", TrollTier.APOCALYPSE)
                .desc("假掉线 + 假回标题 + 刷屏 + 恐怖音效，一套带走心态")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.fakeBroadcast("§c服务器已关闭（原因：你）");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.CHAT_SPAM, 20 * 15, 3);
                    ctx.glitch(GlitchIds.FAKE_KICK, 20 * 25, 3);
                    ctx.glitch(GlitchIds.MENU_TEASE, 20 * 25, 3);
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 25, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_corrupt_oom_apocalypse", "存档和内存一起没了", TrollTier.APOCALYPSE)
                .desc("假存档损坏 + 假内存溢出 + 假标题界面 + 黑屏，整局只来一次")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.title("§4§l已无可挽回", "§8请检查你的心脏");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 25, 3);
                    ctx.glitch(GlitchIds.FAKE_OOM, 20 * 25, 3);
                    ctx.glitch(GlitchIds.MENU_TEASE, 20 * 25, 3);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 8, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_everything_at_once", "所有整活一起上", TrollTier.APOCALYPSE)
                .desc("当愤怒值爆表时，把所有画面整活无差别砸下来")
                .weight(18)
                .cooldownSeconds(900)
                .when(ctx -> ctx.rage() >= 60.0)
                .run(ctx -> {
                    ctx.title("§4§l你惹到模组了", "§c享受接下来的三十秒");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.SPIN, 20 * 30, 3);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 30, 3);
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 30, 3);
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 30, 3);
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 20 * 30, 3);
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 30, 3);
                })
                .build());
    }
}
