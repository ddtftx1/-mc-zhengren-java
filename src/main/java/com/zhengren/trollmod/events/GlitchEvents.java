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

        TrollEvents.reg(TrollEvent.of("glitch_hud_blink", "界面打了个哈欠", TrollTier.BRUTAL)
                .desc("HUD 消失三秒又回来，让你怀疑是自己眼花")
                .weight(160)
                .cooldownSeconds(40)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.msg("§7你的界面好像……刚才闪了一下？");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 1.6f);
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 3, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_shaky_knee", "腿有点软", TrollTier.BRUTAL)
                .desc("画面轻微抖动五秒，像低血糖")
                .weight(150)
                .cooldownSeconds(40)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.actionBar("§7……有点站不稳");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.6f, 1.4f);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 5, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_spin_tiny", "原地转半圈", TrollTier.BRUTAL)
                .desc("视角温柔地转一会儿，纯属手滑")
                .weight(130)
                .cooldownSeconds(45)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.msg("§7刚才谁碰了一下你的鼠标？");
                    ctx.glitch(GlitchIds.SPIN, 20 * 4, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_fov_breath", "镜头在呼吸", TrollTier.ANNOYING)
                .desc("视野像呼吸一样伸缩，看着有点晕但不是我的问题")
                .weight(130)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.actionBar("§8焦距异常，已尝试自动对焦");
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 4, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_scroll_lock_liar", "卷不动了", TrollTier.BRUTAL)
                .desc("假提示说快捷栏被锁，其实就是吓吓你")
                .weight(120)
                .cooldownSeconds(50)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
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

        TrollEvents.reg(TrollEvent.of("glitch_mouse_mirror", "你的手不再属于你", TrollTier.NIGHTMARE)
                .desc("反转鼠标 Y 轴二十五秒：你越是想瞄准，就越是在把自己往刀刃上送")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 500, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 0.7f);
                    ctx.actionBar("§8（手在往上抬，头却在往下低。）");
                    ctx.later(80, () -> {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                        ctx.msg("§8它一直站在你视野的边缘。你只是没办法把头转过去。");
                        ctx.addRage(5);
                    });
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_sens_gone", "灵敏度归零", TrollTier.BRUTAL)
                .desc("鼠标灵敏度调到 0 十五秒，转个头像搬砖")
                .weight(110)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.warn("鼠标灵敏度已设置为 0。祝你好运。");
                    ctx.glitch(GlitchIds.SENS_ZERO, 20 * 15, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_hotbar_jive", "快捷栏蹦迪", TrollTier.BRUTAL)
                .desc("快捷栏选中的格子自己乱跳二十秒")
                .weight(120)
                .cooldownSeconds(90)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
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

        TrollEvents.reg(TrollEvent.of("glitch_chat_flood", "聊天栏发大水", TrollTier.BRUTAL)
                .desc("聊天栏被垃圾信息刷满二十秒")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
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

        TrollEvents.reg(TrollEvent.of("glitch_nausea_swirl", "屏幕开始替你呕吐", TrollTier.NIGHTMARE)
                .desc("视角持续摇晃、画面扭曲，你的胃和你的准星一起失控")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 420, 2);
                    ctx.glitch(GlitchIds.SHAKY, 420, 1);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.msg("§7世界在晃。晃的不是世界。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_anti_aim", "反向瞄准", TrollTier.BRUTAL)
                .desc("上下视角反转，射箭时请自求多福")
                .weight(105)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.warn("瞄准辅助已开启（反向模式）。");
                    ctx.glitch(GlitchIds.INVERT_PITCH, 20 * 18, 2);
                })
                .build());

        // ==================================================================
        // BRUTAL —— 抓狂，会骂人
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("glitch_spin_hell", "滚筒", TrollTier.APOCALYPSE)
                .desc("视角高速旋转二十五秒，直到你彻底分不清哪一个方向站着敌人")
                .weight(60)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SPIN, 500, 2);
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 200, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.2f, 0.6f);
                    ctx.later(90, () -> {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                        ctx.damage("magic", 2.0f);
                        ctx.msg("§8停下来的时候你已经站在它面前了。这不是巧合。");
                        ctx.addRage(9);
                    });
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_mouse_chaos", "控制器已经不听你的了", TrollTier.APOCALYPSE)
                .desc("鼠标反转、视角上下颠倒、灵敏度归零：你的手在做动作，结果却发生在别的地方")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 400, 2);
                    ctx.glitch(GlitchIds.INVERT_PITCH, 400, 2);
                    ctx.glitch(GlitchIds.SENS_ZERO, 400, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.1f, 0.6f);
                    ctx.msg("§8你的身体在执行指令。执行的是它自己的指令。");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_hotbar_prison", "武器锁在盒子里", TrollTier.APOCALYPSE)
                .desc("快捷栏被锁死还附带乱跳——你有武器，只是它永远不在你手上")
                .weight(55)
                .cooldownSeconds(240)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 340, 2);
                    ctx.glitch(GlitchIds.HOTBAR_JUMP, 340, 2);
                    ctx.scrambleHotbar();
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.8f);
                    ctx.later(100, () -> {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                        ctx.damage("magic", 2.0f);
                        ctx.msg("§8它打了你三下，你的手一直按在错误的格子上。");
                        ctx.addRage(10);
                    });
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_blackout_whisper", "谁把灯关了", TrollTier.BRUTAL)
                .desc("长时间黑屏，只留心跳般的音效陪你")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.msg("§8停电通知：本区域将在今晚检修。");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 15, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_fov_madness", "目标忽远忽近", TrollTier.APOCALYPSE)
                .desc("视野疯狂伸缩二十二秒：你以为它离你很远，其实它已经贴到你脸上了")
                .weight(55)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FOV_WILD, 440, 2);
                    ctx.glitch(GlitchIds.FOV_ZOOM, 440, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.later(110, () -> {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                        ctx.damage("magic", 2.0f);
                        ctx.msg("§8距离感是它留给你唯一的、也是最后的错觉。");
                        ctx.addRage(9);
                    });
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_sens_overflow", "轻轻一动，转三圈", TrollTier.NIGHTMARE)
                .desc("灵敏度拉满再叠一层鼠标反转：你挪一下手腕，整个人就转进了敌人的正面")
                .weight(50)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SENS_MAX, 300, 2);
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 300, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                    ctx.later(70, () -> {
                        ctx.glitch(GlitchIds.SPIN, 40, 2);
                        ctx.msg("§8你在原地转了三圈，最后停下来的时候正好面对着它。");
                        ctx.addRage(6);
                    });
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_flash_slam", "闪白之后，你已经在别的地方了", TrollTier.APOCALYPSE)
                .desc("满屏白光加一记剧烈甩视角：等你重新看见东西，位置、朝向和敌人全都变了")
                .weight(55)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FLASHBANG, 60, 3);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 60, 3);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.8f, 0.6f);
                    ctx.scatter(40);
                    ctx.randomRotation();
                    ctx.later(60, () -> {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                        ctx.msg("§8你看不见的那几秒里，有人替你换了个站位。");
                        ctx.addRage(10);
                    });
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_death_notice", "死亡提示先到，死亡随后就来", TrollTier.APOCALYPSE)
                .desc("先弹假死亡界面让你崩一次，再把你扔进真正危险的地方——这一次提示不是假的")
                .weight(60)
                .cooldownSeconds(300)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_DEATH, 80, 2);
                    ctx.playHurtSound();
                    ctx.fakeBroadcast("§7☠ §c" + ctx.playerName() + " §7" + ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.later(85, () -> {
                        ctx.msg("§7（骗你的。你还活着。现在才是重点。）");
                        ctx.voidPit(1, 12);
                        ctx.damage("fall", 2.0f);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                        ctx.glitch(GlitchIds.BLACKOUT, 70);
                        ctx.addRage(8);
                    });
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_oom_warning", "内存不够了，它开始删东西", TrollTier.APOCALYPSE)
                .desc("假内存溢出警告之后，它真的开始腾空间——腾的是你的背包和你的经验")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_OOM, 90, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.3f, 0.7f);
                    ctx.sys("§c[java] OutOfMemoryError: Java heap space（假的）");
                    ctx.later(75, () -> {
                        ctx.sys("§7[回收] 正在释放不需要的资源……");
                        ctx.takeRandomItems(3);
                        ctx.clearEffects();
                        ctx.msg("§8释放完毕。被你需要的那些资源，也在「不需要」的名单里。");
                        ctx.addRage(9);
                    });
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_hud_extinction", "他们关掉了你的仪表盘", TrollTier.NIGHTMARE)
                .desc("先把 HUD 整块撤掉，再一条一条打假警报：你看不见血、看不见饿，只能听见倒计时")
                .weight(45)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.HIDE_HUD, 400, 2);
                    ctx.actionBar("§8（你的界面被拿走了。别找，找不回来。）");
                    ctx.later(60, () -> {
                        ctx.sys("§c[警告] 生命值：未知");
                        ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 0.7f);
                    });
                    ctx.later(120, () -> ctx.sys("§c[警告] 饥饿值：未知"));
                    ctx.later(180, () -> {
                        ctx.sys("§c[警告] 附近敌对目标：12");
                        ctx.glitch(GlitchIds.CHAT_SPAM, 100, 2);
                    });
                    ctx.later(265, () -> {
                        ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.5f, 0.6f);
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                        ctx.damage("magic", 2.0f);
                        ctx.msg("§8你一直以为警报是假的。最后这一条不是。");
                        ctx.addRage(8);
                    });
                    ctx.addRage(7);
                })
                .build());

        // ==================================================================
        // NIGHTMARE —— 崩溃，会砸键盘
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("glitch_fake_crash_report", "游戏崩了（假）", TrollTier.APOCALYPSE)
                .desc("弹出完整的假崩溃报告：点退出会真的退出游戏，而你只有三秒决定要不要相信它")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 100, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.4f, 0.6f);
                    ctx.later(70, () -> {
                        ctx.glitch(GlitchIds.FAKE_CORRUPT, 60, 1);
                        ctx.fakeChat("§8崩溃报告", "报告已提交。你的世界没有受到影响——除了你。");
                        ctx.msg("§7你以为你点了「不发送」。你其实什么都没点。");
                        ctx.addRage(7);
                    });
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_fake_kick_reason", "踢出理由写着你最不想看到的那句", TrollTier.APOCALYPSE)
                .desc("假掉线界面配一条精准戳心的踢出理由，而且它会在你眼前停留很久")
                .weight(35)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_KICK, 100, 2);
                    ctx.fakeBroadcast("§c你已被移出服务器：原因——" + ctx.pick(TrollPools.FAKE_EXCUSES));
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.4f, 0.6f);
                    ctx.later(90, () -> {
                        ctx.fakeChat("§c" + ctx.pick(TrollPools.FAKE_NAMES), "刚才那条……是真的吗？");
                        ctx.glitch(GlitchIds.FAKE_BAN, 70, 2);
                        ctx.msg("§8你被踢了出去，又好像没有。区别只是没人会来问你。");
                        ctx.addRage(9);
                    });
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_menu_ghost", "你回到了标题界面，世界还在继续", TrollTier.APOCALYPSE)
                .desc("画面跳回标题菜单，你动不了、退不出；而屏幕后面那个世界并没有为你暂停")
                .weight(28)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.MENU_TEASE, 110, 2);
                    ctx.glitch(GlitchIds.CLOSE_SCREEN, 60, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.2f, 0.7f);
                    ctx.later(100, () -> {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                        ctx.damage("magic", 2.0f);
                        ctx.msg("§8菜单背后有人在替你继续玩。它玩得比你狠。");
                        ctx.addRage(9);
                    });
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_save_corrupted", "他们说你的存档没了", TrollTier.NIGHTMARE)
                .desc("假存档损坏界面，配上一句「已尝试恢复」——恢复的是你的恐惧，不是你的存档")
                .weight(35)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 90, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.3f, 0.7f);
                    ctx.sys("§c[存档] 检测到区块数据损坏，正在尝试恢复……");
                    ctx.later(80, () -> {
                        ctx.sys("§a[存档] 恢复完成。");
                        ctx.actionBar("§8（它没告诉你恢复了多少。）");
                        ctx.takeRandomItems(2);
                        ctx.msg("§7你检查了背包。这就是「恢复完成」的意思。");
                        ctx.addRage(7);
                    });
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_black_death", "黑屏里有人在等你死", TrollTier.APOCALYPSE)
                .desc("黑屏叠加假死亡提示，你在完全的黑暗里只能听见自己的心跳——和别人的脚步")
                .weight(40)
                .cooldownSeconds(420)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 90, 2);
                    ctx.glitch(GlitchIds.FAKE_DEATH, 90, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.5f, 0.6f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.later(95, () -> {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                        ctx.glitch(GlitchIds.BLACKOUT, 60);
                        ctx.msg("§8黑屏里它不需要看见你。它只需要你还在原地。");
                        ctx.addRage(9);
                    });
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_display_collapse", "显示器要坏了", TrollTier.OBLIVION)
                .desc("先抖动、撕裂、拉伸，演一出显卡着火的戏；再告诉你「已修复」——修复完成的那一刻，屏幕才真正黑下去")
                .weight(45)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SHAKY, 160, 3);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 160, 3);
                    ctx.glitch(GlitchIds.FOV_WILD, 160, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.4f, 0.6f);
                    ctx.msg("§8（画面在抖。你开始担心显示器。这正是它想让你担心的。）");
                    ctx.later(100, () -> {
                        ctx.sys("§a[显示驱动] 异常已修复。");
                        ctx.playLevelUp();
                        ctx.actionBar("§7（你松了一口气。）");
                    });
                    ctx.later(190, () -> {
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 100, 3);
                        ctx.glitch(GlitchIds.FLASHBANG, 20, 3);
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.msg("§8显示器没问题。有问题的是显示器前面那个人。");
                        ctx.obliterate();
                        ctx.addRage(18);
                    });
                    ctx.addRage(10);
                })
                .build());

        // ==================================================================
        // APOCALYPSE —— 地狱，会退游戏
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("glitch_total_meltdown", "全面熔毁", TrollTier.OBLIVION)
                .desc("假崩溃、闪白、黑屏、甩视角轮番上阵；中间还会假装恢复正常一次，只为让你看清第二次崩得更彻底")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 90, 3);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.8f, 0.6f);
                    ctx.later(80, () -> {
                        ctx.glitch(GlitchIds.FLASHBANG, 25, 3);
                        ctx.glitch(GlitchIds.SPIN, 60, 2);
                        ctx.msg("§7（画面回来了。你以为结束了。）");
                    });
                    ctx.later(150, () -> {
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 90, 3);
                        ctx.glitch(GlitchIds.SHAKE_BURST, 80, 3);
                        ctx.scatterFar(160);
                        ctx.randomRotation();
                        ctx.takeRandomItems(4);
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                        ctx.damage("magic", 3.0f);
                        ctx.msg("§8第一次崩溃是为了让你相信第二次不会来。");
                        ctx.addRage(16);
                    });
                    ctx.later(260, () -> {
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.glitch(GlitchIds.FAKE_DEATH, 120, 3);
                        ctx.obliterate();
                        ctx.addRage(20);
                    });
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_kick_menu_combo", "踢出去、退出去、再骗你回来", TrollTier.APOCALYPSE)
                .desc("假掉线、假回标题、假重连、假欢迎回来——每一步都是设计好的，只有陷阱是真的")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_KICK, 80, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.5f, 0.6f);
                    ctx.later(70, () -> {
                        ctx.glitch(GlitchIds.MENU_TEASE, 70, 2);
                        ctx.sys("§c连接已断开。");
                    });
                    ctx.later(150, () -> {
                        ctx.sys("§a正在重新连接……");
                        ctx.playLevelUp();
                        ctx.fakeJoinLeave(ctx.playerName(), true);
                        ctx.msg("§7（欢迎回来。你从来没有离开过。）");
                    });
                    ctx.later(235, () -> {
                        ctx.glitch(GlitchIds.MASS_CHAT, 120, 3);
                        ctx.glitch(GlitchIds.SHAKE_BURST, 60, 3);
                        ctx.takeRandomItems(3);
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                        ctx.damage("magic", 3.0f);
                        ctx.msg("§8「欢迎回来」的意思是：欢迎回到它给你留的位置。");
                        ctx.addRage(14);
                    });
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_corrupt_oom_apocalypse", "存档和内存一起没了", TrollTier.OBLIVION)
                .desc("假存档损坏、假内存溢出、假标题界面、真黑屏；中间那句「已修复」是整场骗局里最残忍的一句")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 90, 3);
                    ctx.glitch(GlitchIds.FAKE_OOM, 90, 3);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.5f, 0.6f);
                    ctx.sys("§c[存档] 世界数据损坏。§c[内存] 即将耗尽。");
                    ctx.later(95, () -> {
                        ctx.glitch(GlitchIds.MENU_TEASE, 70, 2);
                        ctx.sys("§a[修复] 已从备份恢复全部数据。");
                        ctx.playLevelUp();
                        ctx.msg("§7（你信了。这是全场唯一一次你主动配合它。）");
                    });
                    ctx.later(195, () -> {
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 100, 3);
                        ctx.glitch(GlitchIds.FAKE_SAVING, 80, 2);
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.fakeChat("§8修复程序", "「全部数据」不包含你。你不在备份里。");
                        ctx.obliterate();
                        ctx.addRage(20);
                    });
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("glitch_everything_at_once", "所有整活一起上", TrollTier.APOCALYPSE)
                .desc("当崩溃指数爆表时，全部画面酷刑无差别砸下来——它不挑，也不停")
                .weight(18)
                .cooldownSeconds(900)
                .when(ctx -> ctx.rage() >= 60.0)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SHAKY, 200, 3);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 200, 3);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 200, 2);
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 160, 2);
                    ctx.glitch(GlitchIds.FOV_WILD, 160, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.6f, 0.6f);
                    ctx.later(120, () -> {
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 80, 3);
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                        ctx.damage("magic", 3.0f);
                        ctx.msg("§8画面全部恢复的时候，你会发现有些东西没有跟着恢复。");
                        ctx.addRage(14);
                    });
                    ctx.addRage(12);
                })
                .build());
    }
}
