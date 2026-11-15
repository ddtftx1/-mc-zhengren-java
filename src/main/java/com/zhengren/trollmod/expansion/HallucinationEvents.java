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
        TrollEvents.reg(TrollEvent.of("hall_hud_blink", "界面打了个哈欠", TrollTier.BRUTAL)
                .desc("HUD 消失三秒又回来，让你怀疑是不是自己眼花")
                .weight(150)
                .cooldownSeconds(40)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
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
        TrollEvents.reg(TrollEvent.of("hall_spin_drift", "手滑转半圈", TrollTier.ANNOYING)
                .desc("视角温柔地自转一会儿，纯属手滑")
                .weight(130)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.msg("§7刚才谁碰了一下你的鼠标？");
                    ctx.glitch(GlitchIds.SPIN, 20 * 3, 1);
                })
                .build());

        // [4]
        TrollEvents.reg(TrollEvent.of("hall_fov_breathe", "镜头在呼吸", TrollTier.ANNOYING)
                .desc("视野像呼吸一样伸缩四秒，看着有点晕但不是你的问题")
                .weight(130)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.actionBar("§8焦距异常，已尝试自动对焦");
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 4, 1);
                })
                .build());

        // [5]
        TrollEvents.reg(TrollEvent.of("hall_hotbar_wiggle", "快捷栏打了个喷嚏", TrollTier.ANNOYING)
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
        TrollEvents.reg(TrollEvent.of("hall_menu_flash", "标题界面闪回", TrollTier.ANNOYING)
                .desc("画面跳到标题菜单两秒又回来，纯属眼疾")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.sys("正在保存并退出世界……（开玩笑的）");
                    ctx.glitch(GlitchIds.MENU_TEASE, 20 * 2, 1);
                })
                .build());

        // [7]
        TrollEvents.reg(TrollEvent.of("hall_fake_save_blink", "世界已保存（假的）", TrollTier.ANNOYING)
                .desc("弹出假自动保存提示，配一声清脆的音效")
                .weight(140)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 1.5f);
                })
                .build());

        // [8]
        TrollEvents.reg(TrollEvent.of("hall_fake_join_ghost", "好像有人进来了", TrollTier.ANNOYING)
                .desc("伪造一条玩家加入提示，名字还是编的")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), true);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.5f, 1.3f);
                })
                .build());

        // [9]
        TrollEvents.reg(TrollEvent.of("hall_ore_glint", "钻石在反光", TrollTier.ANNOYING)
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
        TrollEvents.reg(TrollEvent.of("hall_ghost_step", "身后有脚步声", TrollTier.BRUTAL)
                .desc("身侧三格外响起一声脚步，其实什么都没有")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.soundAt(ctx.pick(TrollPools.AMBIENT_CREEPY),
                            ctx.x() + 3, ctx.y(), ctx.z(), 0.55f, 1.0f);
                    ctx.sys("有人在你附近移动。");
                })
                .build());

        // [12]
        TrollEvents.reg(TrollEvent.of("hall_invert_blip", "鼠标打嗝", TrollTier.BRUTAL)
                .desc("鼠标上下突然反过来三秒，然后自己好了")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.actionBar("§7手感不对？好像是有点。");
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 3, 1);
                })
                .build());

        // [13]
        TrollEvents.reg(TrollEvent.of("hall_blackout_blink", "灯闪了一下", TrollTier.BRUTAL)
                .desc("屏幕黑一秒就恢复，像是接触不良")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.msg("§8刚才是不是停电了？");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.6f, 0.8f);
                    ctx.glitch(GlitchIds.BLACKOUT, 20, 1);
                })
                .build());

        // [14]
        TrollEvents.reg(TrollEvent.of("hall_fake_achievement_small", "假成就：你被吓到了", TrollTier.BRUTAL)
                .desc("弹出假成就弹窗，和真的一模一样")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.fakeAchievement("见鬼了", "在幻觉里坚持十秒");
                    ctx.glitch(GlitchIds.FAKE_ACHIEVEMENT, 20 * 4, 1);
                    ctx.playLevelUp();
                })
                .build());

        // [15]
        TrollEvents.reg(TrollEvent.of("hall_fake_tip_loading", "一条没用的提示", TrollTier.BRUTAL)
                .desc("以加载提示的口吻胡说八道一句")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.sys(ctx.pick(TrollPools.FAKE_TIPS));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 1.4f);
                })
                .build());

        // [16]
        TrollEvents.reg(TrollEvent.of("hall_visual_ash", "眼前有东西在飘", TrollTier.ANNOYING)
                .desc("视野里飘过几缕灰烬，其实是粒子在演戏")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 10);
                    ctx.actionBar("§8视野出现异常颗粒……？");
                })
                .build());

        // [17]
        TrollEvents.reg(TrollEvent.of("hall_nausea_blip", "轻轻晃一下", TrollTier.ANNOYING)
                .desc("画面眩晕感闪三秒，像低血糖犯了")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.actionBar("§7你的椅子好像也在转");
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 3, 1);
                })
                .build());

        // [18]
        TrollEvents.reg(TrollEvent.of("hall_random_sound_blip", "哪来的声音", TrollTier.ANNOYING)
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
        TrollEvents.reg(TrollEvent.of("hall_fake_diamond_vein", "脚下有钻石——它放的", TrollTier.NIGHTMARE)
                .desc("脚边凭空出现一簇钻石矿，唯一的作用是让你弯腰。")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
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
        TrollEvents.reg(TrollEvent.of("hall_phantom_creeper_hiss", "假苦力怕", TrollTier.NIGHTMARE)
                .desc("身后响起熟悉的前置音。你回头，什么都没有——这正是它要的姿势。")
                .weight(120)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.title("§c嘶——", "§7它就在你身后");
                    ctx.msg("§c你听见了。你确定你听见了。");
                })
                .build());

        // [21]
        TrollEvents.reg(TrollEvent.of("hall_hud_gone_15", "HUD 离家出走", TrollTier.BRUTAL)
                .desc("隐藏整个 HUD 十五秒：血量、饥饿、快捷栏全都不见")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.msg("§e正在优化你的界面……优化没了。");
                    ctx.actionBar("§cHUD 加载失败，请凭感觉游戏");
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 15, 1);
                })
                .build());

        // [22]
        TrollEvents.reg(TrollEvent.of("hall_fake_death_broadcast", "全服都看到你的死讯", TrollTier.BRUTAL)
                .desc("向全服广播一条你的死讯，而你其实还活着")
                .weight(110)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.fakeBroadcast("§c玩家 §f" + ctx.playerName() + " §c" + ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.warn("（但你还站着，这就很尴尬了。）");
                })
                .build());

        // [23]
        TrollEvents.reg(TrollEvent.of("hall_mouse_inverted_15", "鼠标上下颠倒", TrollTier.NIGHTMARE)
                .desc("鼠标上下颠倒十五秒。你的手会先于你的判断出错。")
                .weight(120)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.msg("§e检测到鼠标驱动版本过旧，已自动反转校准。");
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 15, 2);
                })
                .build());

        // [24]
        TrollEvents.reg(TrollEvent.of("hall_dizzy_spin_12", "原地转圈十二秒", TrollTier.NIGHTMARE)
                .desc("视角持续自转十二秒。世界把你甩在原地，方向感先行离场。")
                .weight(115)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.actionBar("§7别转了，是你自己在转");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.8f, 0.9f);
                    ctx.glitch(GlitchIds.SPIN, 20 * 12, 2);
                })
                .build());

        // [25]
        TrollEvents.reg(TrollEvent.of("hall_fake_player_join", "一个不存在的玩家上线了", TrollTier.BRUTAL)
                .desc("伪造加入提示 + 一句诡异的问候")
                .weight(110)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
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
        TrollEvents.reg(TrollEvent.of("hall_chat_flood_10", "聊天栏发大水", TrollTier.BRUTAL)
                .desc("聊天栏被垃圾信息刷满十秒")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
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
        TrollEvents.reg(TrollEvent.of("hall_ghost_block_3s", "脚下多了个方块", TrollTier.BRUTAL)
                .desc("脚边出现一个来路不明的方块，三秒后自己消失")
                .weight(105)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.msg("§7这个方块……刚才是这个吗？");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.7f, 1.3f);
                    ctx.later(60, () -> ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS)));
                })
                .build());

        // [30]
        TrollEvents.reg(TrollEvent.of("hall_fake_lava_floor", "地板在发光", TrollTier.BRUTAL)
                .desc("脚下地板突然变成岩浆色，四秒后恢复原样")
                .weight(105)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 20);
                    ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.msg("§6你脚下的地板……好像软了。");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.6f, 1.4f);
                    ctx.later(80, () -> ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS)));
                })
                .build());

        // [31]
        TrollEvents.reg(TrollEvent.of("hall_silverfish_whisper", "蠹虫在你耳边爬", TrollTier.BRUTAL)
                .desc("连着三声蠹虫细响，让你开始检查脚底")
                .weight(115)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    for (int i = 0; i < 3; i++) {
                        ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.5f, 1.5f);
                    }
                    ctx.msg("§7你确定那不是石头里发出来的声音？");
                })
                .build());

        // [32]
        TrollEvents.reg(TrollEvent.of("hall_fake_ban_warning", "假封禁预警", TrollTier.BRUTAL)
                .desc("严肃地宣布你要被封禁，倒计时到 3 就不念了")
                .weight(100)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.fakeBroadcast("§c" + ctx.pick(TrollPools.FAKE_EXCUSES));
                    ctx.title("§c封禁倒计时", "§73…2…1…（骗你的）");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 0.9f);
                })
                .build());

        // [33]
        TrollEvents.reg(TrollEvent.of("hall_fake_oom_warning", "假内存溢出警告", TrollTier.NIGHTMARE)
                .desc("弹出内存溢出警告。你不知道该信它，还是该信自己刚看到的画面。")
                .weight(105)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
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
        TrollEvents.reg(TrollEvent.of("hall_fake_pickup_sound", "你捡到了空气", TrollTier.NIGHTMARE)
                .desc("清脆的拾取声响起，背包什么都没多。你开始怀疑刚才弯腰的那一下。")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.msg("§7东西呢？");
                })
                .build());

        // [36]
        TrollEvents.reg(TrollEvent.of("hall_ghost_chat_other", "有人单独跟你说悄悄话", TrollTier.BRUTAL)
                .desc("只发给你的伪造聊天，别人一个都看不见")
                .weight(110)
                .cooldownSeconds(180)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.msgToOther(ctx.other(), "§8<" + ctx.pick(TrollPools.FAKE_NAMES) + "> §7我看见你了。");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.5f, 1.2f);
                })
                .build());

        // [37]
        TrollEvents.reg(TrollEvent.of("hall_hotbar_lock_8", "快捷栏被封印八秒", TrollTier.NIGHTMARE)
                .desc("快捷栏被封八秒。你只能看着，而对方显然知道这八秒。")
                .weight(110)
                .cooldownSeconds(200)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.warn("§c你的快捷栏已被临时锁定。");
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 20 * 8, 2);
                })
                .build());

        // [38]
        TrollEvents.reg(TrollEvent.of("hall_random_sound_loop", "耳边持续有人说话", TrollTier.NIGHTMARE)
                .desc("耳边的低语持续十五秒，来源不断变化，每次都离你更近一点。")
                .weight(110)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
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
        TrollEvents.reg(TrollEvent.of("hall_fake_update_tease", "更新卡在 99%", TrollTier.NIGHTMARE)
                .desc("更新界面停在 99%。它不打算走完，只需要你一直看着。")
                .weight(100)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.sys("正在应用补丁，请勿关闭游戏……进度 99%。");
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 20 * 6, 2);
                })
                .build());

        // [41]
        TrollEvents.reg(TrollEvent.of("hall_nausea_short", "晕一会儿", TrollTier.BRUTAL)
                .desc("眩晕式视角乱转十秒，屏幕自己在晃")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.actionBar("§7画面在转，但你的椅子没转");
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 20 * 10, 2);
                })
                .build());

        // [42]
        TrollEvents.reg(TrollEvent.of("hall_fake_ore_underfoot", "脚底下有东西", TrollTier.BRUTAL)
                .desc("脚下凭空冒出一个假矿，四秒后消失")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.ORE_TEASE));
                    ctx.msg("§e等一下，这块颜色不对！");
                    ctx.particleAt(ctx.pick(TrollPools.TAUNT_PARTICLES),
                            ctx.x(), ctx.y(), ctx.z(), 8, 1.0);
                    ctx.later(80, () -> ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS)));
                })
                .build());

        // [43]
        TrollEvents.reg(TrollEvent.of("hall_ghost_afk_notice", "你被判定挂机了", TrollTier.NIGHTMARE)
                .desc("系统判定你挂机，尽管你的手一直在动。你不确定是谁在替你操作。")
                .weight(100)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.fakeBroadcast("§e玩家 §f" + ctx.playerName() + " §e因长时间无操作被标记为挂机。");
                    ctx.actionBar("§c挂机检测中…… 你觉得很委屈。");
                })
                .build());

        // [44]
        TrollEvents.reg(TrollEvent.of("hall_illusion_double", "重影", TrollTier.BRUTAL)
                .desc("画面抖动叠上鬼影粒子，看什么都像两个")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.msg("§8你的视野好像对不上焦。");
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 14);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 8, 2);
                })
                .build());

        // [45]
        TrollEvents.reg(TrollEvent.of("hall_fake_inventory_full", "背包满了（假的）", TrollTier.BRUTAL)
                .desc("郑重警告背包已满，然后你发现它空得很")
                .weight(105)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.warn("§c背包已满，无法拾取物品。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 0.7f);
                    ctx.msg("§7（其实还空着。）");
                })
                .build());

        // [46]
        TrollEvents.reg(TrollEvent.of("hall_fake_kick_warning", "连接超时警告", TrollTier.NIGHTMARE)
                .desc("连接超时警告配诡异音效，让你提前为一次不会发生的掉线心慌。")
                .weight(100)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.warn("连接超时，正在重连……");
                    ctx.title("§c连接中断", "§7请检查你的网络（才怪）");
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 5, 2);
                })
                .build());

        // [47]
        TrollEvents.reg(TrollEvent.of("hall_hud_pulse", "界面脉冲", TrollTier.BRUTAL)
                .desc("HUD 消失八秒再回来，还贴心通知你一声")
                .weight(105)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.actionBar("§8界面正在重启……");
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 8, 2);
                    ctx.later(180, () -> ctx.msg("§7界面回来了。惊不惊喜？"));
                })
                .build());

        // [48]
        TrollEvents.reg(TrollEvent.of("hall_monster_chorus", "没有怪的合唱", TrollTier.NIGHTMARE)
                .desc("全服都听见一片此起彼伏的叫声，却一只怪也没有。声音在向你靠拢。")
                .weight(110)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 0.8f, 0.7f);
                    ctx.titleToAll("§c它们来了", "§7不过它们在别的维度");
                })
                .build());

        // [49]
        TrollEvents.reg(TrollEvent.of("hall_visual_static", "雪花屏", TrollTier.BRUTAL)
                .desc("视野里飘满白色噪点，像老电视没信号")
                .weight(105)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 18);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 5, 2);
                })
                .build());

        // [50]
        TrollEvents.reg(TrollEvent.of("hall_dark_advancement", "假成就：见鬼了", TrollTier.NIGHTMARE)
                .desc("成就弹窗亮起又暗下。负面效果留在身上，像是领奖的代价。")
                .weight(100)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.fakeAchievement("见鬼了", "与不存在的东西对视超过 3 秒");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                })
                .build());

        // ==================================================================
        // BRUTAL —— 抓狂：明显影响操作与心态（27 个）
        // ==================================================================

        // [51]
        TrollEvents.reg(TrollEvent.of("hall_fake_diamond_haul", "发财了……吗", TrollTier.NIGHTMARE)
                .desc("眼前铺开一整片矿脉，十秒后全部变回石头。你捡过的每一块都不算。")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
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
        TrollEvents.reg(TrollEvent.of("hall_blackout_whisper_br", "黑屏里的耳语", TrollTier.APOCALYPSE)
                .desc("长时间黑屏，只留心跳般的音效陪你。那个节奏不像你的。")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.msg("§8停电通知：本区域将在今晚检修。");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 12, 3);
                })
                .build());

        // [53]
        TrollEvents.reg(TrollEvent.of("hall_spin_hell_br", "滚筒", TrollTier.NIGHTMARE)
                .desc("视角高速旋转二十秒。你不是在转，是被世界反复搅动。")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.title("§4§l转起来了", "§7晕了别怪我");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.SPIN, 20 * 20, 3);
                })
                .build());

        // [54]
        TrollEvents.reg(TrollEvent.of("hall_mouse_chaos_br", "鼠标彻底疯了", TrollTier.APOCALYPSE)
                .desc("鼠标反转、上下颠倒、灵敏度归零。你的手被逐出了决策层。")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.warn("§c输入设备已被接管。");
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 20, 3);
                    ctx.glitch(GlitchIds.INVERT_PITCH, 20 * 20, 3);
                    ctx.glitch(GlitchIds.SENS_ZERO, 20 * 18, 3);
                })
                .build());

        // [55]
        TrollEvents.reg(TrollEvent.of("hall_fake_crash_tease", "崩溃前兆", TrollTier.NIGHTMARE)
                .desc("一连串假错误提示刷过，让你在真正的崩溃到来前先崩一次。")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
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
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.fakeDeathScreen(ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.glitch(GlitchIds.FAKE_DEATH, 20 * 15, 2);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "§7又躺了，笑死");
                })
                .build());

        // [57]
        TrollEvents.reg(TrollEvent.of("hall_fov_madness_br", "焦距失控", TrollTier.APOCALYPSE)
                .desc("视野疯狂伸缩二十秒，太阳穴跟着一起跳，距离感彻底失效。")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
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
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.warn("§c鼠标灵敏度已设置为 2000%。");
                    ctx.glitch(GlitchIds.SENS_MAX, 20 * 18, 3);
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 18, 2);
                })
                .build());

        // [59]
        TrollEvents.reg(TrollEvent.of("hall_hotbar_prison_br", "快捷栏监狱", TrollTier.APOCALYPSE)
                .desc("快捷栏锁死还乱跳。换不了武器，只能眼看着对方走近。")
                .weight(55)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.warn("§c你的快捷栏已被管理员封印。");
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 20 * 20, 3);
                    ctx.glitch(GlitchIds.HOTBAR_JUMP, 20 * 20, 3);
                })
                .build());

        // [60]
        TrollEvents.reg(TrollEvent.of("hall_screen_tear_br", "画面撕裂", TrollTier.NIGHTMARE)
                .desc("剧烈抖动叠上视野乱跳，像有东西正从屏幕的缝里往外看。")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.title("§c信号丢失", "§7请拍打显示器");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 18, 3);
                })
                .build());

        // [61]
        TrollEvents.reg(TrollEvent.of("hall_freeze_input_br", "输入冻结", TrollTier.NIGHTMARE)
                .desc("灵敏度归零、HUD 隐藏、视距锁死。这十秒里你不是玩家，是观众。")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.warn("§c输入设备无响应，已切换到观战模式。");
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 10, 3);
                })
                .build());

        // [62]
        TrollEvents.reg(TrollEvent.of("hall_invert_controls_br", "前进变后退", TrollTier.APOCALYPSE)
                .desc("所有方向键反着来十五秒。你想往前，身体带你去它想去的地方。")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.msg("§e检测到坐标系异常，已为你翻转输入。");
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 20 * 15, 3);
                })
                .build());

        // [63]
        TrollEvents.reg(TrollEvent.of("hall_fake_corrupt_br", "存档已损坏", TrollTier.APOCALYPSE)
                .desc("假存档损坏界面亮起。你明知是假的，心还是先沉了一下。")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.warn("§c检测到存档区块校验失败。");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 20, 3);
                })
                .build());

        // [64]
        TrollEvents.reg(TrollEvent.of("hall_fake_virus_br", "假病毒警告", TrollTier.NIGHTMARE)
                .desc("假木马警告带着倒计时。倒计时走完之前，你什么都不敢碰。")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.sys("检测到未知进程 java.exe 正在读取你的存档。");
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 20 * 12, 3);
                })
                .build());

        // [65]
        TrollEvents.reg(TrollEvent.of("hall_ghost_swarm", "幻影军团", TrollTier.APOCALYPSE)
                .desc("身边凭空聚起一群小东西，配着音效和标题。你分不清它们有没有实体。")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7它们不是真的……大概");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 8);
                })
                .build());

        // [66]
        TrollEvents.reg(TrollEvent.of("hall_fake_lava_pit", "脚下成了岩浆池", TrollTier.NIGHTMARE)
                .desc("脚下铺开一层岩浆。你分不清那是幻觉，还是地形真的改了。")
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
        TrollEvents.reg(TrollEvent.of("hall_void_illusion", "地板不见了", TrollTier.NIGHTMARE)
                .desc("脚下被掏空一块。像世界观缺了一角，而缺的正是你站的地方。")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.title("§8§l下面什么都没有", "§7你确定还要往下看？");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 0.6f);
                    ctx.voidPit(1, 3);
                })
                .build());

        // [68]
        TrollEvents.reg(TrollEvent.of("hall_bleed_illusion", "幻痛", TrollTier.APOCALYPSE)
                .desc("没有任何攻击者，但血条是真的在掉。你找不到来源，也止不住。")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.damage("magic", 3.0f);
                    ctx.particleAt(ctx.pick(TrollPools.SPOOKY_PARTICLES),
                            ctx.x(), ctx.y() + 1, ctx.z(), 16, 0.6);
                    ctx.msg("§c你感觉被什么东西抓了一下。");
                })
                .build());

        // [69]
        TrollEvents.reg(TrollEvent.of("hall_blind_illusion", "突然看不见了", TrollTier.APOCALYPSE)
                .desc("先失明，再黑屏。两种意义上的看不见，同时到位。")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
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
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.msg("§8……");
                    ctx.randomHorrorSound();
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 15, 3);
                })
                .build());

        // [71]
        TrollEvents.reg(TrollEvent.of("hall_mass_chat_br", "聊天栏被淹没", TrollTier.NIGHTMARE)
                .desc("海量假聊天涌进来，把你说过的话冲得无影无踪。")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.warn("§c正在恢复聊天历史记录……");
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 12, 3);
                })
                .build());

        // [72]
        TrollEvents.reg(TrollEvent.of("hall_ban_countdown", "封禁倒计时", TrollTier.APOCALYPSE)
                .desc("倒计时配着假木马界面走完。它最后说那是个玩笑——而你还在数秒。")
                .weight(45)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
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
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 15, 3);
                })
                .build());

        // [74]
        TrollEvents.reg(TrollEvent.of("hall_visual_meltdown_br", "显卡要坏了", TrollTier.NIGHTMARE)
                .desc("抖动、视野乱变、闪白。像显卡在烧，而你是唯一闻到味道的人。")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.warn("§c显卡温度 118°C，风扇已阵亡。");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 18, 3);
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 18, 3);
                    ctx.glitch(GlitchIds.FLASHBANG, 20 * 4, 2);
                })
                .build());

        // [75]
        TrollEvents.reg(TrollEvent.of("hall_corrupt_cascade", "损坏连锁反应", TrollTier.NIGHTMARE)
                .desc("假存档损坏之后紧接着一记假掉线，让你确认刚才那次是真的坏了。")
                .weight(45)
                .cooldownSeconds(480)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
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
        TrollEvents.reg(TrollEvent.of("hall_mirror_world", "镜子世界", TrollTier.APOCALYPSE)
                .desc("鼠标反转、视角颠倒、持续旋转。你整个人被倒过来放回了世界。")
                .weight(50)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.title("§8§l镜像已启用", "§7左右也是反的");
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 20 * 15, 3);
                    ctx.glitch(GlitchIds.INVERT_PITCH, 20 * 15, 3);
                    ctx.glitch(GlitchIds.SPIN, 20 * 15, 2);
                })
                .build());

        // [77]
        TrollEvents.reg(TrollEvent.of("hall_fake_ore_jackpot", "假矿大奖", TrollTier.APOCALYPSE)
                .desc("一口气铺出四块假矿，十秒后连影子都不剩。贪念被完整演示了一遍。")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
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
        TrollEvents.reg(TrollEvent.of("hall_fake_corrupt_nm", "存档报废", TrollTier.APOCALYPSE)
                .desc("假存档损坏界面持续二十五秒。足够你开始回忆上次备份是什么时候。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.warn("§c世界数据已损坏，无法恢复。");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 25, 3);
                })
                .build());

        // [79]
        TrollEvents.reg(TrollEvent.of("hall_fake_oom_nm", "内存溢出", TrollTier.APOCALYPSE)
                .desc("长时间假内存溢出界面配满屏假堆栈。你会开始犹豫该不该强制关掉它。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.sys("java.lang.OutOfMemoryError: 你的耐心不足");
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_OOM, 20 * 25, 3);
                })
                .build());

        // [80]
        TrollEvents.reg(TrollEvent.of("hall_menu_tease_nm", "回到标题", TrollTier.APOCALYPSE)
                .desc("画面跳回标题菜单。你还在游戏里，只是视角被换成了空屏。")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.msg("§7正在保存并退出世界……");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.glitch(GlitchIds.MENU_TEASE, 20 * 25, 3);
                })
                .build());

        // [81]
        TrollEvents.reg(TrollEvent.of("hall_black_death_nm", "黑屏里的死亡", TrollTier.OBLIVION)
                .desc("黑屏里会传出你自己的死讯。你看不见，但能听见有人开始收拾你的东西。")
                .weight(40)
                .cooldownSeconds(540)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.title("§4§l别关灯", "§8你会看不见自己是怎么死的");
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 18, 3);
                    ctx.glitch(GlitchIds.FAKE_DEATH, 20 * 18, 2);
                    ctx.randomHorrorSound();
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.4f);
                    // 假救援：黑暗里有人报点
                    ctx.later(120, () -> {
                        ctx.msg("§a别怕，我看见你了。往左走。");
                    });
                    ctx.later(240, () -> {
                        ctx.warn("§4……那边是墙。§7刚才那句话，不是你队友说的。");
                    });
                    ctx.later(380, () -> {
                        ctx.obliterate();
                        ctx.takeRandomItems(3);
                        ctx.msg("§8黑暗里有人替你收好了东西。§7不是还给你，是收走。");
                    });
                })
                .build());

        // [82]
        TrollEvents.reg(TrollEvent.of("hall_display_collapse_nm", "显示器阵亡", TrollTier.OBLIVION)
                .desc("画面会先告诉你它坏了，然后真的坏给你看。你分不清哪一帧是真的。")
                .weight(45)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.warn("§c显示驱动已停止响应，正在重启……");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 20, 3);
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 20, 3);
                    ctx.glitch(GlitchIds.FLASHBANG, 20 * 5, 3);
                    // 假 HUD：假装修好了
                    ctx.later(180, () -> {
                        ctx.ok("§a显示驱动已恢复。");
                        ctx.later(70, () -> {
                            ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 15, 3);
                            ctx.obliterate();
                            ctx.msg("§8「已恢复」这三个字，本身也是画面的一部分。");
                        });
                    });
                    ctx.later(420, () -> ctx.glitch(GlitchIds.HIDE_HUD, 20 * 15, 3));
                })
                .build());

        // [83]
        TrollEvents.reg(TrollEvent.of("hall_total_blackout_nm", "真正的黑", TrollTier.OBLIVION)
                .desc("二十五秒硬黑屏。陪你一起等的只有心跳，而那个心跳不属于你。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§8§l什么都看不见了", "§7但你还能听见");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 25, 3);
                    ctx.buryAlive();
                    // 黑暗里的假信息，一次比一次近
                    ctx.later(100, () -> ctx.msg("§7（远处）有人喊了你的名字。"));
                    ctx.later(230, () -> ctx.msg("§7（更近了）还是那个声音，语气不太对。"));
                    ctx.later(370, () -> {
                        ctx.obliterate();
                        ctx.takeRandomItems(2);
                        ctx.warn("§8灯亮的时候你不会记得这些。§7但你的背包会记得。");
                    });
                })
                .build());

        // [84]
        TrollEvents.reg(TrollEvent.of("hall_ghost_realm", "幽灵领域", TrollTier.APOCALYPSE)
                .desc("黑暗降临，身边响起一片不属于这个维度的声音。它们在讨论你。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.title("§4§l你踏进了不该来的地方", "§8它们已经注意到你了");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.ANNOYING_MOBS), 6);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 10, 2);
                })
                .build());

        // [85]
        TrollEvents.reg(TrollEvent.of("hall_fake_kick_nm", "你被移出了服务器", TrollTier.APOCALYPSE)
                .desc("假掉线界面配一条当众宣读的理由。全服都看见了，只有你不是真被踢。")
                .weight(35)
                .cooldownSeconds(540)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.msg("§c连接中断。");
                    ctx.glitch(GlitchIds.FAKE_KICK, 20 * 25, 3);
                    ctx.fakeBroadcast("§c玩家 " + ctx.playerName() + " 已被请出服务器（"
                            + ctx.pick(TrollPools.FAKE_EXCUSES) + "）");
                })
                .build());

        // [86]
        TrollEvents.reg(TrollEvent.of("hall_insanity_loop", "精神污染循环", TrollTier.OBLIVION)
                .desc("刷屏、噪音、HUD 消失会一直循环。循环的尽头还是同一句「你还好吗」。")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§4§l你还好吗", "§7不好也没关系，反正要一直问下去");
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 20, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 12, 3);
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 20, 3);
                    ctx.randomHorrorSound();
                    // 三次「这次真的结束了」
                    ctx.later(140, () -> ctx.msg("§7刷屏停了。§8结束了。"));
                    ctx.later(270, () -> {
                        ctx.glitch(GlitchIds.MASS_CHAT, 20 * 10, 3);
                        ctx.msg("§7……又开始了。§8这次总该是真的结束了吧。");
                    });
                    ctx.later(410, () -> {
                        ctx.obliterate();
                        ctx.takeRandomItems(3);
                        ctx.msg("§4……你还好吗。§8它只是想听你回答一次。");
                    });
                })
                .build());

        // [87]
        TrollEvents.reg(TrollEvent.of("hall_fake_virus_nm", "病毒警告升级", TrollTier.OBLIVION)
                .desc("它会告诉你世界文件正在被写入。然后你眼睁睁看着背包先一步消失。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.sys("警告：检测到 3 个恶意进程正在写入 level.dat");
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 20 * 25, 3);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 10, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    // 假 HUD：假杀毒成功
                    ctx.later(170, () -> {
                        ctx.ok("§a威胁已清除。世界文件完好。");
                        ctx.later(100, () -> {
                            ctx.obliterate();
                            ctx.takeRandomItems(4);
                            ctx.warn("§4「已清除」是它写给你看的最后一行。§7你的东西已经不在了。");
                        });
                    });
                })
                .build());

        // [88]
        TrollEvents.reg(TrollEvent.of("hall_visual_death_nm", "视觉死亡", TrollTier.OBLIVION)
                .desc("它先给你放一遍你的死亡回放，再告诉你那只是预告片。")
                .weight(40)
                .cooldownSeconds(540)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.fakeDeathScreen(ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.glitch(GlitchIds.FAKE_DEATH, 20 * 20, 3);
                    ctx.damage("magic", 2.0f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    // 假救援：假重生
                    ctx.later(160, () -> {
                        ctx.ok("§a你重生了。");
                        ctx.msg("§7你摸了摸胸口——血条是满的。");
                    });
                    ctx.later(300, () -> {
                        ctx.obliterate();
                        ctx.takeRandomItems(3);
                        ctx.warn("§4那不是重生。§7它只是把死亡回放又放了一遍，顺手清空了你的背包。");
                    });
                })
                .build());

        // [89]
        TrollEvents.reg(TrollEvent.of("hall_freeze_forever_nm", "永久冻结（二十五秒）", TrollTier.APOCALYPSE)
                .desc("输入冻结加灵敏度归零。二十五秒里你只能看着自己站在原地挨打。")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.warn("§c输入子系统已挂起，请等待管理员处理。");
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 25, 3);
                    ctx.glitch(GlitchIds.SENS_ZERO, 20 * 25, 3);
                })
                .build());

        // [90]
        TrollEvents.reg(TrollEvent.of("hall_nightmare_swarm", "噩梦涌来", TrollTier.APOCALYPSE)
                .desc("真怪物从幻象里走了出来。这次不是幻觉，而你已经不敢确认了。")
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
        TrollEvents.reg(TrollEvent.of("hall_world_reload_nm", "世界重载卡死", TrollTier.OBLIVION)
                .desc("进度会卡在 1%。它不卡在别的地方，就卡在你能看见的那个数字上。")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.sys("正在重新加载世界数据……（进度 1%，已卡住）");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 20 * 25, 3);
                    ctx.buryAlive();
                    // 假希望：进度动了，然后退回去
                    ctx.later(150, () -> ctx.msg("§e进度 2%……它动了。"));
                    ctx.later(280, () -> {
                        ctx.msg("§e进度 1%……§8它退回去了。");
                        ctx.later(110, () -> {
                            ctx.obliterate();
                            ctx.takeRandomItems(2);
                            ctx.warn("§4它不是在加载世界。§7它是在加载你剩下的耐心。");
                        });
                    });
                })
                .build());

        // [92]
        TrollEvents.reg(TrollEvent.of("hall_ghost_face", "它就在你面前", TrollTier.APOCALYPSE)
                .desc("惊恐标题、闪白、恐怖音效。三秒之内把心率推到不该有的位置。")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
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
        TrollEvents.reg(TrollEvent.of("hall_fake_crash_apoc", "游戏崩了（这次点退出是真的）", TrollTier.OBLIVION)
                .desc("完整的假崩溃报告，退出按钮是真的。它给你留了一条真的能结束的路。")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.msg("§7渲染线程好像出了点问题……");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 10, 3);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 20 * 30, 3);
                    ctx.obliterate();
                    // 假希望：假修复
                    ctx.later(210, () -> {
                        ctx.ok("§a已恢复。可继续游戏。");
                        ctx.later(130, () -> {
                            ctx.takeRandomItems(4);
                            ctx.warn("§4恢复按钮点不动。§7唯一能点动的是退出，而那是真的退出。");
                        });
                    });
                })
                .build());

        // [94]
        TrollEvents.reg(TrollEvent.of("hall_fake_ban_apoc", "你被永久封禁了（假的）", TrollTier.OBLIVION)
                .desc("带申诉码的假封禁界面。申诉码格式是真的，只是没有人在看。")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.fakeBroadcast("§4玩家 §f" + ctx.playerName() + " §4已被永久封禁。");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.FAKE_BAN, 20 * 30, 3);
                    ctx.stripAll();
                    // 假希望：假申诉成功
                    ctx.later(230, () -> {
                        ctx.ok("§a申诉已受理。正在解除封禁……");
                        ctx.later(150, () -> {
                            ctx.buryAlive();
                            ctx.takeRandomItems(3);
                            ctx.warn("§4申诉入口是假的。§7你的东西是真的没了。");
                        });
                    });
                })
                .build());

        // [95]
        TrollEvents.reg(TrollEvent.of("hall_total_meltdown_apoc", "全面熔毁", TrollTier.OBLIVION)
                .desc("闪白、甩视角、黑屏、假存档损坏，组合技只为让你相信世界已经没了。")
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
                    ctx.stripAll();
                    // 假希望：假存档恢复成功
                    ctx.later(250, () -> {
                        ctx.fakeSaveNotice();
                        ctx.ok("§a存档已从备份恢复。");
                        ctx.later(150, () -> {
                            ctx.obliterate();
                            ctx.takeRandomItems(4);
                            ctx.warn("§4没有备份。§7「恢复成功」这句话，是它自己写的。");
                        });
                    });
                })
                .build());

        // [96]
        TrollEvents.reg(TrollEvent.of("hall_kick_menu_combo_apoc", "踢出，然后退到标题", TrollTier.APOCALYPSE)
                .desc("刷屏、假掉线、假回标题、恐怖音效。一套走完，只给你留一个空屏。")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.fakeBroadcast("§c服务器已关闭（原因：你）");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.CHAT_SPAM, 20 * 15, 3);
                    ctx.glitch(GlitchIds.FAKE_KICK, 20 * 25, 3);
                    ctx.glitch(GlitchIds.MENU_TEASE, 20 * 25, 3);
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 25, 3);
                })
                .build());

        // [97]
        TrollEvents.reg(TrollEvent.of("hall_reality_collapse", "现实崩塌", TrollTier.OBLIVION)
                .desc("它同时夺走你的操作和你的视野，让你开始怀疑哪一帧才是真的。")
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
                    ctx.stripAll();
                    // 假希望：画面"恢复"，可以动了
                    ctx.later(210, () -> {
                        ctx.ok("§a显示已恢复。你可以动了。");
                        ctx.later(130, () -> {
                            ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 15, 3);
                            ctx.obliterate();
                            ctx.msg("§8「可以动了」也是幻觉的一部分。");
                        });
                    });
                })
                .build());

        // [98]
        TrollEvents.reg(TrollEvent.of("hall_void_swallow", "虚空吞咽", TrollTier.OBLIVION)
                .desc("脚下会裂开。然后它让你在坠落途中收到一条「救援已在路上」。")
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
                    // 假救援
                    ctx.later(130, () -> {
                        ctx.ok("§a救援已在路上。请保持不动。");
                    });
                    ctx.later(260, () -> {
                        ctx.obliterate();
                        ctx.buryAlive();
                        ctx.takeRandomItems(3);
                        ctx.warn("§4救援是真的在路上。§7只是它走的方向，和你相反。");
                    });
                })
                .build());

        // [99]
        TrollEvents.reg(TrollEvent.of("hall_everything_hall", "所有幻觉一起上", TrollTier.APOCALYPSE)
                .desc("愤怒值爆表时，所有画面整活无差别砸下来，不留一处可以相信的地方。")
                .weight(16)
                .cooldownSeconds(900)
                .when(ctx -> ctx.rage() >= 80.0)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
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
