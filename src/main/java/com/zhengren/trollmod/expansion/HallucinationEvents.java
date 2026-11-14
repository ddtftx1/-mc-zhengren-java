package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【幻觉与视觉】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。</p>
 *
 * <p>主题：假方块、假生物、假 UI、幻听、假提示、假玩家、假消息、视觉失真、
 * 假矿物、幽灵、错觉、假音效、画面闪烁、假死亡提示。
 * 本分类**不引用任何 Minecraft 类型**，全部通过 {@code ctx.*} + {@code TrollPools.*}
 * + {@code GlitchIds.*} 组合实现，因此对 MC 版本免疫。</p>
 *
 * <p>等级分布：PRANK 18 / ANNOYING 32 / BRUTAL 27 / NIGHTMARE 15 / APOCALYPSE 7 = 99。</p>
 */
public final class HallucinationEvents {

    private HallucinationEvents() {
    }

    public static void register() {
        TrollEvents.category("幻觉与视觉");

        // ==================================================================
        // PRANK —— 小整：眼睛一花的错觉，笑一下就过去了（18 个）
        // ==================================================================

        // [1]
        TrollEvents.reg(TrollEvent.of("hall_hud_blink", "界面打了个哈欠", TrollTier.PRANK)
                .desc("HUD 消失三秒又回来，让你怀疑是不是自己眼花")
                .weight(150)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.msg("§7你的界面是不是……刚才闪了一下？");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 1.6f);
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 3, 1);
                })
                .build());

        // [2]
        TrollEvents.reg(TrollEvent.of("hall_shaky_yawn", "腿有点软", TrollTier.PRANK)
                .desc("画面轻微抖动四秒，像低血糖站不稳")
                .weight(140)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.actionBar("§7……有点站不稳");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.6f, 1.4f);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 4, 1);
                })
                .build());

        // [3]
        TrollEvents.reg(TrollEvent.of("hall_spin_drift", "手滑转半圈", TrollTier.PRANK)
                .desc("视角温柔地自转一会儿，纯属手滑")
                .weight(130)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.msg("§7刚才谁碰了一下你的鼠标？");
                    ctx.glitch(GlitchIds.SPIN, 20 * 3, 1);
                })
                .build());

        // [4]
        TrollEvents.reg(TrollEvent.of("hall_fov_breathe", "镜头在呼吸", TrollTier.PRANK)
                .desc("视野像呼吸一样伸缩四秒，看着有点晕但不是你的问题")
                .weight(130)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.actionBar("§8焦距异常，已尝试自动对焦");
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 4, 1);
                })
                .build());

        // [5]
        TrollEvents.reg(TrollEvent.of("hall_hotbar_wiggle", "快捷栏打了个喷嚏", TrollTier.PRANK)
                .desc("快捷栏选中的格子抖三秒，像它有自己的想法")
                .weight(120)
                .cooldownSeconds(50)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.msg("§7你的键盘好像有点痒。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.9f, 1.3f);
                    ctx.glitch(GlitchIds.HOTBAR_JUMP, 20 * 3, 1);
                })
                .build());

        // [6]
        TrollEvents.reg(TrollEvent.of("hall_menu_flash", "标题界面闪回", TrollTier.PRANK)
                .desc("画面跳到标题菜单两秒又回来，纯属眼疾")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.sys("正在保存并退出世界……（开玩笑的）");
                    ctx.glitch(GlitchIds.MENU_TEASE, 20 * 2, 1);
                })
                .build());

        // [7]
        TrollEvents.reg(TrollEvent.of("hall_fake_save_blink", "世界已保存（假的）", TrollTier.PRANK)
                .desc("弹出假自动保存提示，配一声清脆的音效")
                .weight(140)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 1.5f);
                })
                .build());

        // [8]
        TrollEvents.reg(TrollEvent.of("hall_fake_join_ghost", "好像有人进来了", TrollTier.PRANK)
                .desc("伪造一条玩家加入提示，名字还是编的")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), true);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.5f, 1.3f);
                })
                .build());

        // [9]
        TrollEvents.reg(TrollEvent.of("hall_ore_glint", "钻石在反光", TrollTier.PRANK)
                .desc("眼角闪过一串附魔粒子，回头什么都没有")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.msg("§7眼角好像有什么亮了一下。");
                    ctx.particleAt(ctx.pick(TrollPools.TAUNT_PARTICLES),
                            ctx.x() + ctx.rd(-4, 4), ctx.y() + 1, ctx.z() + ctx.rd(-4, 4), 10, 0.4);
                })
                .build());

        // [10]
        TrollEvents.reg(TrollEvent.of("hall_whisper_left", "耳边有人说话", TrollTier.PRANK)
                .desc("左耳传来一句很轻的话，内容听不清")
                .weight(130)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.45f, 1.7f);
                    ctx.msg("§8……你听见了吗？");
                })
                .build());

        // [11]
        TrollEvents.reg(TrollEvent.of("hall_ghost_step", "身后有脚步声", TrollTier.PRANK)
                .desc("身侧三格外响起一声脚步，其实什么都没有")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.soundAt(ctx.pick(TrollPools.AMBIENT_CREEPY),
                            ctx.x() + 3, ctx.y(), ctx.z(), 0.55f, 1.0f);
                    ctx.sys("有人在你附近移动。");
                })
                .build());

        // [12]
        TrollEvents.reg(TrollEvent.of("hall_invert_blip", "鼠标打嗝", TrollTier.PRANK)
                .desc("鼠标上下突然反过来三秒，然后自己好了")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.actionBar("§7手感不对？好像是有点。");
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 3, 1);
                })
                .build());

        // [13]
        TrollEvents.reg(TrollEvent.of("hall_blackout_blink", "灯闪了一下", TrollTier.PRANK)
                .desc("屏幕黑一秒就恢复，像是接触不良")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.msg("§8刚才是不是停电了？");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.6f, 0.8f);
                    ctx.glitch(GlitchIds.BLACKOUT, 20, 1);
                })
                .build());

        // [14]
        TrollEvents.reg(TrollEvent.of("hall_fake_achievement_small", "假成就：你被吓到了", TrollTier.PRANK)
                .desc("弹出假成就弹窗，和真的一模一样")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.fakeAchievement("见鬼了", "在幻觉里坚持十秒");
                    ctx.glitch(GlitchIds.FAKE_ACHIEVEMENT, 20 * 4, 1);
                    ctx.playLevelUp();
                })
                .build());

        // [15]
        TrollEvents.reg(TrollEvent.of("hall_fake_tip_loading", "一条没用的提示", TrollTier.PRANK)
                .desc("以加载提示的口吻胡说八道一句")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.sys(ctx.pick(TrollPools.FAKE_TIPS));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 1.4f);
                })
                .build());

        // [16]
        TrollEvents.reg(TrollEvent.of("hall_visual_ash", "眼前有东西在飘", TrollTier.PRANK)
                .desc("视野里飘过几缕灰烬，其实是粒子在演戏")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 10);
                    ctx.actionBar("§8视野出现异常颗粒……？");
                })
                .build());

        // [17]
        TrollEvents.reg(TrollEvent.of("hall_nausea_blip", "轻轻晃一下", TrollTier.PRANK)
                .desc("画面眩晕感闪三秒，像低血糖犯了")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.actionBar("§7你的椅子好像也在转");
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 3, 1);
                })
                .build());

        // [18]
        TrollEvents.reg(TrollEvent.of("hall_random_sound_blip", "哪来的声音", TrollTier.PRANK)
                .desc("随机一声恐怖音效，短促且没有下文")
                .weight(120)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.randomHorrorSound();
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 3, 1);
                })
                .build());

        // ==================================================================
        // ANNOYING —— 烦人：开始影响判断和手感（32 个）
        // ==================================================================

        // [19]
        TrollEvents.reg(TrollEvent.of("hall_fake_diamond_vein", "脚下有钻石矿！", TrollTier.ANNOYING)
                .desc("脚边凭空出现一簇钻石矿，五秒后变回泥土")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.setBlock(1, -1, 0, ctx.pick(TrollPools.ORE_TEASE));
                    ctx.setBlock(0, -1, 1, ctx.pick(TrollPools.ORE_TEASE));
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 12);
                    ctx.msg("§e等等，这是什么？！");
                    ctx.later(100, () -> {
                        ctx.setBlock(1, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS));
                        ctx.setBlock(0, -1, 1, ctx.pick(TrollPools.TROLL_BLOCKS));
                    });
                })
                .build());

        // [20]
        TrollEvents.reg(TrollEvent.of("hall_phantom_creeper_hiss", "假苦力怕", TrollTier.ANNOYING)
                .desc("经典的嘶声配上惊悚标题，但背后什么都没有")
                .weight(120)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.title("§c嘶——", "§7它就在你身后");
                    ctx.msg("§c你听见了。你确定你听见了。");
                })
                .build());

        // [21]
        TrollEvents.reg(TrollEvent.of("hall_hud_gone_15", "HUD 离家出走", TrollTier.ANNOYING)
                .desc("隐藏整个 HUD 十五秒：血量、饥饿、快捷栏全都不见")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.msg("§e正在优化你的界面……优化没了。");
                    ctx.actionBar("§cHUD 加载失败，请凭感觉游戏");
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 15, 1);
                })
                .build());

        // [22]
        TrollEvents.reg(TrollEvent.of("hall_fake_death_broadcast", "全服都看到你的死讯", TrollTier.ANNOYING)
                .desc("向全服广播一条你的死讯，而你其实还活着")
                .weight(110)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.fakeBroadcast("§c玩家 §f" + ctx.playerName() + " §c" + ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.warn("（但你还站着，这就很尴尬了。）");
                })
                .build());

        // [23]
        TrollEvents.reg(TrollEvent.of("hall_mouse_inverted_15", "鼠标上下颠倒", TrollTier.ANNOYING)
                .desc("反转鼠标 Y 轴十五秒，你的大脑会先崩")
                .weight(120)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.msg("§e检测到鼠标驱动版本过旧，已自动反转校准。");
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 15, 2);
                })
                .build());

        // [24]
        TrollEvents.reg(TrollEvent.of("hall_dizzy_spin_12", "原地转圈十二秒", TrollTier.ANNOYING)
                .desc("视角持续自转，方向感当场报废")
                .weight(115)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.actionBar("§7别转了，是你自己在转");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.8f, 0.9f);
                    ctx.glitch(GlitchIds.SPIN, 20 * 12, 2);
                })
                .build());

        // [25]
        TrollEvents.reg(TrollEvent.of("hall_fake_player_join", "一个不存在的玩家上线了", TrollTier.ANNOYING)
                .desc("伪造加入提示 + 一句诡异的问候")
                .weight(110)
                .cooldownSeconds(240)
                .run(ctx -> {
                    String name = ctx.pick(TrollPools.FAKE_NAMES);
                    ctx.fakeJoinLeave(name, true);
                    ctx.fakeChat(name, "§7我一直在看你玩。");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.6f, 1.1f);
                })
                .build());

        // [26]
        TrollEvents.reg(TrollEvent.of("hall_fake_op_speech", "管理员对你说话了", TrollTier.ANNOYING)
                .desc("伪造一条管理员发言，内容阴阳怪气")
                .weight(105)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.fakeChat("服务器管理员", ctx.pick(TrollPools.FAKE_OPERATOR_LINES));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                })
                .build());

        // [27]
        TrollEvents.reg(TrollEvent.of("hall_chat_flood_10", "聊天栏发大水", TrollTier.ANNOYING)
                .desc("聊天栏被垃圾信息刷满十秒")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.warn("服务器正在同步聊天记录，请稍候。");
                    ctx.glitch(GlitchIds.CHAT_SPAM, 20 * 10, 2);
                })
                .build());

        // [28]
        TrollEvents.reg(TrollEvent.of("hall_sens_zero_10", "灵敏度归零", TrollTier.ANNOYING)
                .desc("鼠标灵敏度调到 0 十秒，转个头像搬砖")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.warn("鼠标灵敏度已设置为 0。祝你好运。");
                    ctx.glitch(GlitchIds.SENS_ZERO, 20 * 10, 2);
                })
                .build());

        // [29]
        TrollEvents.reg(TrollEvent.of("hall_ghost_block_3s", "脚下多了个方块", TrollTier.ANNOYING)
                .desc("脚边出现一个来路不明的方块，三秒后自己消失")
                .weight(105)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.msg("§7这个方块……刚才是这个吗？");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.7f, 1.3f);
                    ctx.later(60, () -> ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS)));
                })
                .build());

        // [30]
        TrollEvents.reg(TrollEvent.of("hall_fake_lava_floor", "地板在发光", TrollTier.ANNOYING)
                .desc("脚下地板突然变成岩浆色，四秒后恢复原样")
                .weight(105)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 20);
                    ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.msg("§6你脚下的地板……好像软了。");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.6f, 1.4f);
                    ctx.later(80, () -> ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS)));
                })
                .build());

        // [31]
        TrollEvents.reg(TrollEvent.of("hall_silverfish_whisper", "蠹虫在你耳边爬", TrollTier.ANNOYING)
                .desc("连着三声蠹虫细响，让你开始检查脚底")
                .weight(115)
                .cooldownSeconds(180)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.5f, 1.5f);
                    }
                    ctx.msg("§7你确定那不是石头里发出来的声音？");
                })
                .build());

        // [32]
        TrollEvents.reg(TrollEvent.of("hall_fake_ban_warning", "假封禁预警", TrollTier.ANNOYING)
                .desc("严肃地宣布你要被封禁，倒计时到 3 就不念了")
                .weight(100)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.fakeBroadcast("§c" + ctx.pick(TrollPools.FAKE_EXCUSES));
                    ctx.title("§c封禁倒计时", "§73…2…1…（骗你的）");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 0.9f);
                })
                .build());

        // [33]
        TrollEvents.reg(TrollEvent.of("hall_fake_oom_warning", "假内存溢出警告", TrollTier.ANNOYING)
                .desc("弹一个内存溢出警告，看得人手心冒汗")
                .weight(105)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.sys("Java 堆内存不足，正在释放你的耐心。");
                    ctx.glitch(GlitchIds.FAKE_OOM, 20 * 12, 2);
                })
                .build());

        // [34]
        TrollEvents.reg(TrollEvent.of("hall_screen_tear_mild", "画面有点撕", TrollTier.ANNOYING)
                .desc("轻微的撕裂感加抖动，像显卡接触不良")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.actionBar("§c显示输出异常，正在自动修复……");
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 6, 2);
                })
                .build());

        // [35]
        TrollEvents.reg(TrollEvent.of("hall_fake_pickup_sound", "你捡到了……空气", TrollTier.ANNOYING)
                .desc("清脆的拾取音效响起，但背包什么都没多")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.msg("§7东西呢？");
                })
                .build());

        // [36]
        TrollEvents.reg(TrollEvent.of("hall_ghost_chat_other", "有人单独跟你说悄悄话", TrollTier.ANNOYING)
                .desc("只发给你的伪造聊天，别人一个都看不见")
                .weight(110)
                .cooldownSeconds(180)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    ctx.msgToOther(ctx.other(), "§8<" + ctx.pick(TrollPools.FAKE_NAMES) + "> §7我看见你了。");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.5f, 1.2f);
                })
                .build());

        // [37]
        TrollEvents.reg(TrollEvent.of("hall_hotbar_lock_8", "快捷栏被封印八秒", TrollTier.ANNOYING)
                .desc("换不了武器，只能眼睁睁看着怪物走过来")
                .weight(110)
                .cooldownSeconds(200)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.warn("§c你的快捷栏已被临时锁定。");
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 20 * 8, 2);
                })
                .build());

        // [38]
        TrollEvents.reg(TrollEvent.of("hall_random_sound_loop", "耳边持续有人说话", TrollTier.ANNOYING)
                .desc("随机恐怖音效持续轰炸十五秒，假装是客户端在闹鬼")
                .weight(110)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.msg("§8……你听见了吗？");
                    ctx.randomHorrorSound();
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 15, 2);
                })
                .build());

        // [39]
        TrollEvents.reg(TrollEvent.of("hall_world_reload_short", "正在重新加载世界", TrollTier.ANNOYING)
                .desc("假世界重载进度条卡住不动，卡得你心跳加速")
                .weight(100)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.sys("正在重新加载世界数据……（进度 7%）");
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 20 * 6, 2);
                })
                .build());

        // [40]
        TrollEvents.reg(TrollEvent.of("hall_fake_update_tease", "正在更新（99%）", TrollTier.ANNOYING)
                .desc("假更新界面卡在 99%，逼死强迫症")
                .weight(100)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.sys("正在应用补丁，请勿关闭游戏……进度 99%。");
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 20 * 6, 2);
                })
                .build());

        // [41]
        TrollEvents.reg(TrollEvent.of("hall_nausea_short", "晕一会儿", TrollTier.ANNOYING)
                .desc("眩晕式视角乱转十秒，屏幕自己在晃")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.actionBar("§7画面在转，但你的椅子没转");
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 10, 2);
                })
                .build());

        // [42]
        TrollEvents.reg(TrollEvent.of("hall_fake_ore_underfoot", "脚底下有东西", TrollTier.ANNOYING)
                .desc("脚下凭空冒出一个假矿，四秒后消失")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.ORE_TEASE));
                    ctx.msg("§e等一下，这块颜色不对！");
                    ctx.particleAt(ctx.pick(TrollPools.TAUNT_PARTICLES),
                            ctx.x(), ctx.y(), ctx.z(), 8, 1.0);
                    ctx.later(80, () -> ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS)));
                })
                .build());

        // [43]
        TrollEvents.reg(TrollEvent.of("hall_ghost_afk_notice", "你被判定挂机了", TrollTier.ANNOYING)
                .desc("伪造挂机警告，明明你手还在动")
                .weight(100)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e玩家 §f" + ctx.playerName() + " §e因长时间无操作被标记为挂机。");
                    ctx.actionBar("§c挂机检测中…… 你觉得很委屈。");
                })
                .build());

        // [44]
        TrollEvents.reg(TrollEvent.of("hall_illusion_double", "重影", TrollTier.ANNOYING)
                .desc("画面抖动叠上鬼影粒子，看什么都像两个")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.msg("§8你的视野好像对不上焦。");
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 14);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 8, 2);
                })
                .build());

        // [45]
        TrollEvents.reg(TrollEvent.of("hall_fake_inventory_full", "背包满了（假的）", TrollTier.ANNOYING)
                .desc("郑重警告背包已满，然后你发现它空得很")
                .weight(105)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.warn("§c背包已满，无法拾取物品。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 0.7f);
                    ctx.msg("§7（其实还空着。）");
                })
                .build());

        // [46]
        TrollEvents.reg(TrollEvent.of("hall_fake_kick_warning", "连接超时警告", TrollTier.ANNOYING)
                .desc("假掉线预警配上诡异音效，让你先冒一身汗")
                .weight(100)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.warn("连接超时，正在重连……");
                    ctx.title("§c连接中断", "§7请检查你的网络（才怪）");
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 5, 2);
                })
                .build());

        // [47]
        TrollEvents.reg(TrollEvent.of("hall_hud_pulse", "界面脉冲", TrollTier.ANNOYING)
                .desc("HUD 消失八秒再回来，还贴心通知你一声")
                .weight(105)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.actionBar("§8界面正在重启……");
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 8, 2);
                    ctx.later(180, () -> ctx.msg("§7界面回来了。惊不惊喜？"));
                })
                .build());

        // [48]
        TrollEvents.reg(TrollEvent.of("hall_monster_chorus", "怪物大合唱", TrollTier.ANNOYING)
                .desc("全服都听见一片此起彼伏的怪物叫声，但一只怪也没有")
                .weight(110)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 0.8f, 0.7f);
                    ctx.titleToAll("§c它们来了", "§7不过它们在别的维度");
                })
                .build());

        // [49]
        TrollEvents.reg(TrollEvent.of("hall_visual_static", "雪花屏", TrollTier.ANNOYING)
                .desc("视野里飘满白色噪点，像老电视没信号")
                .weight(105)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 18);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 5, 2);
                })
                .build());

        // [50]
        TrollEvents.reg(TrollEvent.of("hall_dark_advancement", "假成就：见鬼了", TrollTier.ANNOYING)
                .desc("假成就弹窗加一层负面效果，仪式感拉满")
                .weight(100)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.fakeAchievement("见鬼了", "与不存在的东西对视超过 3 秒");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                })
                .build());

        // ==================================================================
        // BRUTAL —— 抓狂：明显影响操作与心态（27 个）
        // ==================================================================

        // [51]
        TrollEvents.reg(TrollEvent.of("hall_fake_diamond_haul", "发财了！……吗？", TrollTier.BRUTAL)
                .desc("眼前铺开一片假矿脉，十秒后全部变回石头")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.setBlock(ctx.ri(-2, 2), -1, ctx.ri(-2, 2), ctx.pick(TrollPools.ORE_TEASE));
                    }
                    ctx.msg("§e§l整片矿脉！今天是我的日子！");
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 25);
                    ctx.later(200, () -> {
                        ctx.msg("§7（矿脉已下班。）");
                        ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS));
                    });
                })
                .build());

        // [52]
        TrollEvents.reg(TrollEvent.of("hall_blackout_whisper_br", "黑屏里的耳语", TrollTier.BRUTAL)
                .desc("长时间黑屏，只留心跳般的音效陪你")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.msg("§8停电通知：本区域将在今晚检修。");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 12, 3);
                })
                .build());

        // [53]
        TrollEvents.reg(TrollEvent.of("hall_spin_hell_br", "滚筒洗衣机", TrollTier.BRUTAL)
                .desc("视角高速持续旋转二十秒，方向感直接报废")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.title("§4§l转起来了", "§7晕了别怪我");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.SPIN, 20 * 20, 3);
                })
                .build());

        // [54]
        TrollEvents.reg(TrollEvent.of("hall_mouse_chaos_br", "鼠标彻底疯了", TrollTier.BRUTAL)
                .desc("鼠标反转 + 上下颠倒 + 灵敏度归零，三连击")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.warn("§c输入设备已被接管。");
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 20, 3);
                    ctx.glitch(GlitchIds.INVERT_PITCH, 20 * 20, 3);
                    ctx.glitch(GlitchIds.SENS_ZERO, 20 * 18, 3);
                })
                .build());

        // [55]
        TrollEvents.reg(TrollEvent.of("hall_fake_crash_tease", "崩溃前兆", TrollTier.BRUTAL)
                .desc("一连串假错误提示，让你以为下一秒就要崩")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.warn("§c渲染线程返回了未知状态：0xDEAD。");
                    ctx.glitch(GlitchIds.FAKE_OOM, 20 * 10, 2);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 10, 3);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 0.6f);
                })
                .build());

        // [56]
        TrollEvents.reg(TrollEvent.of("hall_fake_death_screen_br", "你已经死了（假的）", TrollTier.BRUTAL)
                .desc("假死亡界面让你先崩溃，再发现自己还活着")
                .weight(60)
                .cooldownSeconds(300)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.fakeDeathScreen(ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.glitch(GlitchIds.FAKE_DEATH, 20 * 15, 2);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "§7又躺了，笑死");
                })
                .build());

        // [57]
        TrollEvents.reg(TrollEvent.of("hall_fov_madness_br", "焦距失控", TrollTier.BRUTAL)
                .desc("视野疯狂伸缩二十秒，看得太阳穴直跳")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.actionBar("§c镜头马达故障，正在自动检修……");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.7f);
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 20, 3);
                })
                .build());

        // [58]
        TrollEvents.reg(TrollEvent.of("hall_sens_overflow_br", "灵敏度爆炸", TrollTier.BRUTAL)
                .desc("灵敏度拉满再叠一层鼠标反转，轻轻一动转三圈")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.warn("§c鼠标灵敏度已设置为 2000%。");
                    ctx.glitch(GlitchIds.SENS_MAX, 20 * 18, 3);
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 18, 2);
                })
                .build());

        // [59]
        TrollEvents.reg(TrollEvent.of("hall_hotbar_prison_br", "快捷栏监狱", TrollTier.BRUTAL)
                .desc("快捷栏锁死还乱跳，换不了武器只能挨打")
                .weight(55)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.warn("§c你的快捷栏已被管理员封印。");
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 20 * 20, 3);
                    ctx.glitch(GlitchIds.HOTBAR_JUMP, 20 * 20, 3);
                })
                .build());

        // [60]
        TrollEvents.reg(TrollEvent.of("hall_screen_tear_br", "画面撕裂加强版", TrollTier.BRUTAL)
                .desc("剧烈抖动叠上视野乱跳，像显示器要裂开")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.title("§c信号丢失", "§7请拍打显示器");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 18, 3);
                })
                .build());

        // [61]
        TrollEvents.reg(TrollEvent.of("hall_freeze_input_br", "输入冻结", TrollTier.BRUTAL)
                .desc("灵敏度归零 + 隐藏 HUD + 视距锁死，十秒里你是个观众")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.warn("§c输入设备无响应，已切换到观战模式。");
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 10, 3);
                })
                .build());

        // [62]
        TrollEvents.reg(TrollEvent.of("hall_invert_controls_br", "前进变后退", TrollTier.BRUTAL)
                .desc("所有方向键反着来十五秒，走两步就撞墙")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.msg("§e检测到坐标系异常，已为你翻转输入。");
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 20 * 15, 3);
                })
                .build());

        // [63]
        TrollEvents.reg(TrollEvent.of("hall_fake_corrupt_br", "存档损坏（假）", TrollTier.BRUTAL)
                .desc("假存档损坏界面，看一眼血压直接上来")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.warn("§c检测到存档区块校验失败。");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 20, 3);
                })
                .build());

        // [64]
        TrollEvents.reg(TrollEvent.of("hall_fake_virus_br", "假病毒警告", TrollTier.BRUTAL)
                .desc("弹一个假木马警告，还带倒计时")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.sys("检测到未知进程 java.exe 正在读取你的存档。");
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 20 * 12, 3);
                })
                .build());

        // [65]
        TrollEvents.reg(TrollEvent.of("hall_ghost_swarm", "幻影军团", TrollTier.BRUTAL)
                .desc("身边凭空出现一群小东西，配上惊悚音效和标题")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7它们不是真的……大概");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 8);
                })
                .build());

        // [66]
        TrollEvents.reg(TrollEvent.of("hall_fake_lava_pit", "脚下成了岩浆池", TrollTier.BRUTAL)
                .desc("脚下铺开一层岩浆（受 allowWorldDamage 控制），幻觉与真实的边界")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.msg("§6你踩到了一片温热的东西。");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.lavaPit(1);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 100, 0);
                })
                .build());

        // [67]
        TrollEvents.reg(TrollEvent.of("hall_void_illusion", "地板不见了", TrollTier.BRUTAL)
                .desc("脚下被掏出一个浅坑，视觉上像世界观崩了一角")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.title("§8§l下面什么都没有", "§7你确定还要往下看？");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 0.6f);
                    ctx.voidPit(1, 3);
                })
                .build());

        // [68]
        TrollEvents.reg(TrollEvent.of("hall_bleed_illusion", "幻痛", TrollTier.BRUTAL)
                .desc("没有任何攻击者，但你真的掉血了")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.damage("magic", 3.0f);
                    ctx.particleAt(ctx.pick(TrollPools.SPOOKY_PARTICLES),
                            ctx.x(), ctx.y() + 1, ctx.z(), 16, 0.6);
                    ctx.msg("§c你感觉被什么东西抓了一下。");
                })
                .build());

        // [69]
        TrollEvents.reg(TrollEvent.of("hall_blind_illusion", "突然看不见了", TrollTier.BRUTAL)
                .desc("先给你失明效果再黑屏，双重意义上的一片黑")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.warn("§c光照引擎无响应。");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 8, 2);
                })
                .build());

        // [70]
        TrollEvents.reg(TrollEvent.of("hall_hard_blackout_br", "硬黑屏", TrollTier.BRUTAL)
                .desc("长时间硬黑屏配心跳音，让你怀疑游戏卡死了")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.msg("§8……");
                    ctx.randomHorrorSound();
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 15, 3);
                })
                .build());

        // [71]
        TrollEvents.reg(TrollEvent.of("hall_mass_chat_br", "聊天栏海啸", TrollTier.BRUTAL)
                .desc("海量假聊天刷屏，比普通刷屏猛五倍")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.warn("§c正在恢复聊天历史记录……");
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 12, 3);
                })
                .build());

        // [72]
        TrollEvents.reg(TrollEvent.of("hall_ban_countdown", "封禁倒计时", TrollTier.BRUTAL)
                .desc("倒计时配上假木马界面，最后告诉你是在开玩笑")
                .weight(45)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.title("§4§l封禁倒计时", "§73…2…1…");
                    ctx.fakeBroadcast("§c" + ctx.pick(TrollPools.FAKE_EXCUSES));
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 20 * 8, 2);
                    ctx.later(140, () -> ctx.fakeChat("服务器管理员", "§7开玩笑的，继续玩吧。"));
                })
                .build());

        // [73]
        TrollEvents.reg(TrollEvent.of("hall_ghost_army_sound", "幻听大军", TrollTier.BRUTAL)
                .desc("全服同时听见一片恐怖音效，但没人看得见东西")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 15, 3);
                })
                .build());

        // [74]
        TrollEvents.reg(TrollEvent.of("hall_visual_meltdown_br", "显卡要坏了", TrollTier.BRUTAL)
                .desc("抖动 + 视野乱变 + 闪白，演一出显卡起火")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.warn("§c显卡温度 118°C，风扇已阵亡。");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 18, 3);
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 18, 3);
                    ctx.glitch(GlitchIds.FLASHBANG, 20 * 4, 2);
                })
                .build());

        // [75]
        TrollEvents.reg(TrollEvent.of("hall_corrupt_cascade", "损坏连锁反应", TrollTier.BRUTAL)
                .desc("先假存档损坏，几秒后再补一记假掉线")
                .weight(45)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.warn("§c区块校验失败，正在回滚……");
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 15, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.later(160, () -> {
                        ctx.glitch(GlitchIds.FAKE_KICK, 20 * 10, 2);
                        ctx.fakeBroadcast("§c连接被服务器主动断开。");
                    });
                })
                .build());

        // [76]
        TrollEvents.reg(TrollEvent.of("hall_mirror_world", "镜子世界", TrollTier.BRUTAL)
                .desc("鼠标反转 + 视角颠倒 + 持续旋转，整个人倒过来玩")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.title("§8§l镜像已启用", "§7左右也是反的");
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 15, 3);
                    ctx.glitch(GlitchIds.INVERT_PITCH, 20 * 15, 3);
                    ctx.glitch(GlitchIds.SPIN, 20 * 15, 2);
                })
                .build());

        // [77]
        TrollEvents.reg(TrollEvent.of("hall_fake_ore_jackpot", "假矿大奖", TrollTier.BRUTAL)
                .desc("一口气铺出四块假矿，十秒后连影子都不剩")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.setBlock(ctx.ri(-3, 3), -1, ctx.ri(-3, 3), ctx.pick(TrollPools.ORE_TEASE));
                    }
                    ctx.msg("§6§l这是什么运气！");
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 30);
                    ctx.later(200, () -> {
                        ctx.msg("§7运气已被回收，感谢参与。");
                        ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS));
                    });
                })
                .build());

        // ==================================================================
        // NIGHTMARE —— 崩溃：恐怖体验，会砸键盘（15 个）
        // ==================================================================

        // [78]
        TrollEvents.reg(TrollEvent.of("hall_fake_corrupt_nm", "存档报废（假）", TrollTier.NIGHTMARE)
                .desc("假存档损坏界面持续二十五秒，看得人心跳过速")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.warn("§c世界数据已损坏，无法恢复。");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 25, 3);
                })
                .build());

        // [79]
        TrollEvents.reg(TrollEvent.of("hall_fake_oom_nm", "内存溢出（假）", TrollTier.NIGHTMARE)
                .desc("长时间的假内存溢出界面，配满屏假堆栈")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.sys("java.lang.OutOfMemoryError: 你的耐心不足");
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_OOM, 20 * 25, 3);
                })
                .build());

        // [80]
        TrollEvents.reg(TrollEvent.of("hall_menu_tease_nm", "回到标题界面（假）", TrollTier.NIGHTMARE)
                .desc("画面跳回标题菜单，其实你还在原地看着空屏")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.msg("§7正在保存并退出世界……");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.glitch(GlitchIds.MENU_TEASE, 20 * 25, 3);
                })
                .build());

        // [81]
        TrollEvents.reg(TrollEvent.of("hall_black_death_nm", "黑屏里的死亡", TrollTier.NIGHTMARE)
                .desc("黑屏叠假死亡提示，视觉与心理的双重打击")
                .weight(40)
                .cooldownSeconds(540)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.title("§4§l别关灯", "§8你会看不见自己怎么死的");
                    ctx.randomHorrorSound();
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 18, 3);
                    ctx.glitch(GlitchIds.FAKE_DEATH, 20 * 18, 2);
                })
                .build());

        // [82]
        TrollEvents.reg(TrollEvent.of("hall_display_collapse_nm", "显示器阵亡", TrollTier.NIGHTMARE)
                .desc("抖动 + 视野乱变 + 闪白，三管齐下演显卡升天")
                .weight(45)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.warn("§c显示驱动已停止响应，正在重启……");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 20, 3);
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 20, 3);
                    ctx.glitch(GlitchIds.FLASHBANG, 20 * 5, 3);
                })
                .build());

        // [83]
        TrollEvents.reg(TrollEvent.of("hall_total_blackout_nm", "真正的黑", TrollTier.NIGHTMARE)
                .desc("二十五秒硬黑屏，只有心跳声证明游戏还活着")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§8§l什么都看不见了", "§7但你还能听见");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 25, 3);
                })
                .build());

        // [84]
        TrollEvents.reg(TrollEvent.of("hall_ghost_realm", "幽灵领域", TrollTier.NIGHTMARE)
                .desc("黑暗降临，身边响起一片不属于这个维度的声音")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§4§l你踏进了不该来的地方", "§8它们已经注意到你了");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.ANNOYING_MOBS), 6);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 10, 2);
                })
                .build());

        // [85]
        TrollEvents.reg(TrollEvent.of("hall_fake_kick_nm", "你被踢了（假）", TrollTier.NIGHTMARE)
                .desc("假掉线界面配一条屈辱的踢出理由，全服都看得见")
                .weight(35)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.msg("§c连接中断。");
                    ctx.glitch(GlitchIds.FAKE_KICK, 20 * 25, 3);
                    ctx.fakeBroadcast("§c玩家 " + ctx.playerName() + " 已被请出服务器（"
                            + ctx.pick(TrollPools.FAKE_EXCUSES) + "）");
                })
                .build());

        // [86]
        TrollEvents.reg(TrollEvent.of("hall_insanity_loop", "精神污染循环", TrollTier.NIGHTMARE)
                .desc("恐怖音效 + 海量刷屏 + HUD 消失，三件套一起上")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§4§l你还好吗", "§7不好也没关系");
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 20, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 12, 3);
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 20, 3);
                    ctx.randomHorrorSound();
                })
                .build());

        // [87]
        TrollEvents.reg(TrollEvent.of("hall_fake_virus_nm", "病毒警告升级", TrollTier.NIGHTMARE)
                .desc("假病毒警告接着假存档损坏，营造不可挽回的氛围")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.sys("警告：检测到 3 个恶意进程正在写入 level.dat");
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 20 * 25, 3);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 10, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                })
                .build());

        // [88]
        TrollEvents.reg(TrollEvent.of("hall_visual_death_nm", "视觉死亡", TrollTier.NIGHTMARE)
                .desc("假死亡界面配上真实掉血，分不清虚实")
                .weight(40)
                .cooldownSeconds(540)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.fakeDeathScreen(ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.glitch(GlitchIds.FAKE_DEATH, 20 * 20, 3);
                    ctx.damage("magic", 2.0f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                })
                .build());

        // [89]
        TrollEvents.reg(TrollEvent.of("hall_freeze_forever_nm", "永久冻结（二十五秒）", TrollTier.NIGHTMARE)
                .desc("输入冻结加灵敏度归零，二十五秒里你只能看着")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.warn("§c输入子系统已挂起，请等待管理员处理。");
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 25, 3);
                    ctx.glitch(GlitchIds.SENS_ZERO, 20 * 25, 3);
                })
                .build());

        // [90]
        TrollEvents.reg(TrollEvent.of("hall_nightmare_swarm", "噩梦涌来", TrollTier.NIGHTMARE)
                .desc("真怪物从幻象里走出来，这次不是幻觉了")
                .weight(35)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.title("§4§l这次是真的", "§8抱歉，幻觉结束了");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                })
                .build());

        // [91]
        TrollEvents.reg(TrollEvent.of("hall_world_reload_nm", "世界重载卡死", TrollTier.NIGHTMARE)
                .desc("假世界重载界面卡在 1% 二十五秒，比真掉线还折磨")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.sys("正在重新加载世界数据……（进度 1%，已卡住）");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 20 * 25, 3);
                })
                .build());

        // [92]
        TrollEvents.reg(TrollEvent.of("hall_ghost_face", "它就在你面前", TrollTier.NIGHTMARE)
                .desc("惊恐标题 + 闪白 + 恐怖音效，三秒内让你心率飙升")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§8别眨眼");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FLASHBANG, 20 * 4, 3);
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 12, 3);
                })
                .build());

        // ==================================================================
        // APOCALYPSE —— 地狱：致命 / 毁灭 / 天灾级（7 个）
        // ==================================================================

        // [93]
        TrollEvents.reg(TrollEvent.of("hall_fake_crash_apoc", "游戏崩了（这次点退出是真的）", TrollTier.APOCALYPSE)
                .desc("完整假崩溃报告界面，点退出会真的退出游戏；整局只来一次")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.msg("§7渲染线程好像出了点问题……");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 10, 3);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 20 * 30, 3);
                })
                .build());

        // [94]
        TrollEvents.reg(TrollEvent.of("hall_fake_ban_apoc", "你被永久封禁了（假的）", TrollTier.APOCALYPSE)
                .desc("带申诉码的假永久封禁界面，退出按钮是真的；整局只来一次")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.fakeBroadcast("§4玩家 §f" + ctx.playerName() + " §4已被永久封禁。");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.FAKE_BAN, 20 * 30, 3);
                })
                .build());

        // [95]
        TrollEvents.reg(TrollEvent.of("hall_total_meltdown_apoc", "全面熔毁", TrollTier.APOCALYPSE)
                .desc("闪白 + 甩视角 + 黑屏 + 假存档损坏，整局只来一次的组合技")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.title("§4§l致命错误", "§c你的世界正在蒸发");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.FLASHBANG, 20 * 5, 3);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 20 * 8, 3);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 12, 3);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 20, 3);
                    ctx.randomHorrorSound();
                })
                .build());

        // [96]
        TrollEvents.reg(TrollEvent.of("hall_kick_menu_combo_apoc", "踢出去再退出去", TrollTier.APOCALYPSE)
                .desc("刷屏 + 假掉线 + 假回标题 + 恐怖音效，一套带走心态")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.fakeBroadcast("§c服务器已关闭（原因：你）");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.CHAT_SPAM, 20 * 15, 3);
                    ctx.glitch(GlitchIds.FAKE_KICK, 20 * 25, 3);
                    ctx.glitch(GlitchIds.MENU_TEASE, 20 * 25, 3);
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 25, 3);
                })
                .build());

        // [97]
        TrollEvents.reg(TrollEvent.of("hall_reality_collapse", "现实崩塌", TrollTier.APOCALYPSE)
                .desc("输入冻结 + 撕裂 + 硬黑屏 + 海量刷屏，四重奏；整局只来一次")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.title("§4§l现实已停止响应", "§8请相信你的眼睛（不建议）");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 20, 3);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 20, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 15, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 15, 3);
                })
                .build());

        // [98]
        TrollEvents.reg(TrollEvent.of("hall_void_swallow", "虚空吞咽", TrollTier.APOCALYPSE)
                .desc("脚下裂开深坑 + 雷击 + 虚空伤害 + 黑屏，天灾全套")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.title("§8§l世界正在往下掉", "§7抓紧了");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.voidPit(1, 4);
                    ctx.lightningNear();
                    ctx.damage("outofworld", 4.0f);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 10, 3);
                })
                .build());

        // [99]
        TrollEvents.reg(TrollEvent.of("hall_everything_hall", "所有幻觉一起上", TrollTier.APOCALYPSE)
                .desc("愤怒值爆表时，把所有画面整活无差别砸下来")
                .weight(16)
                .cooldownSeconds(900)
                .when(ctx -> ctx.rage() >= 80.0)
                .run(ctx -> {
                    ctx.title("§4§l你惹到幻觉了", "§c接下来二十秒请自便");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.SPIN, 20 * 20, 3);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 20, 3);
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 20, 3);
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 20, 3);
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 20 * 20, 3);
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 20, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 10, 3);
                })
                .build());
    }
}
