package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【结局级画面】分类事件（v3.0.0 残酷化·内容层）。
 *
 * <p>本文件的主题只有一个：让你相信事情结束了，然后告诉你还没有。
 * 高频套路是「假希望」——先给一个喘息的窗口（保存成功、加载完成、封禁撤销、维护取消），
 * 再用 {@code ctx.later(...)} 把那个窗口亲手关上。OBLIVION 档的事件不再是单纯的画面轰炸，
 * 而是一段有起承转合的折磨：给你希望，让你松手，然后收走更多。</p>
 *
 * <p>本文件只使用 ctx / TrollPools / GlitchIds，不直接触碰任何 Minecraft API。</p>
 */
public final class NightmareEvents {

    private NightmareEvents() {
    }

    public static void register() {
        TrollEvents.category("结局级画面");

        // ==================================================================
        // BRUTAL —— 抓狂：痛感明确，但还会让你自己站起来
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("nightmare_save_notice", "正在保存……", TrollTier.BRUTAL)
                .desc("假保存提示先给你三秒安全感，等你松手再抽走两件东西——存档是假的，损失是真的")
                .weight(160)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.glitch(GlitchIds.FAKE_SAVING, 80, 1);
                    ctx.later(70, () -> {
                        int taken = ctx.takeRandomItems(2);
                        ctx.warn("保存完成。你的背包少了 " + taken + " 件东西，这是正常损耗。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_fake_achievement", "成就达成！", TrollTier.BRUTAL)
                .desc("弹一个叫「资深受害者」的假成就；你被归档了，而不是被感谢了")
                .weight(150)
                .cooldownSeconds(60)
                .run(ctx -> {
                    String name = ctx.pick(TrollPools.FAKE_ACHIEVEMENTS);
                    ctx.setFire(3);
                    ctx.fakeAchievement(name, "§7在毫无察觉的情况下被耍了");
                    ctx.glitch(GlitchIds.FAKE_ACHIEVEMENT, 100, 1);
                    ctx.later(60, () -> ctx.sys("该成就已写入你的档案。档案不公开，因为没有人在意。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_mild_tear", "画面抽了一下", TrollTier.BRUTAL)
                .desc("轻微画面撕裂三秒，让你以为是显示器的问题——显示器没问题，问题是你")
                .weight(140)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.setFire(3);
                    ctx.actionBar("§8显示器连接不太稳定……");
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.6f, 1.2f);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 60, 1);
                    ctx.later(80, () -> ctx.msg("§8刚才那不是显示器的问题。是它开始看你了。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_controls_slip", "按键打了个滑", TrollTier.BRUTAL)
                .desc("前后左右反转四秒，并在恢复的瞬间告诉你「校准成功」——校准的是它，不是你")
                .weight(140)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.setFire(3);
                    ctx.msg("§7你的键盘刚才好像歪了一下。");
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 80, 1);
                    ctx.later(90, () -> ctx.ok("方向校准成功。你刚才走反的路不能退。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_loading_tips", "加载提示", TrollTier.BRUTAL)
                .desc("往聊天栏丢一条没用的假加载提示，那条提示在嘲笑你的操作记录")
                .weight(140)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.effectRandom(240, 1);
                    ctx.sys(ctx.pick(TrollPools.FAKE_TIPS));
                    ctx.glitch(GlitchIds.FAKE_SAVING, 40, 1);
                    ctx.later(70, () -> ctx.sys("提示加载完毕。顺带一提：上面那条提示是专门为你挑的。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_freeze_ui", "输入冻结", TrollTier.BRUTAL)
                .desc("灵敏度归零、HUD 消失、视距锁死；解冻比冻结更可怕，因为你会以为它真的放过了你")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.takeRandomItems(1);
                    ctx.warn("检测到输入设备延迟，已临时冻结操作。");
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 160, 1);
                    ctx.later(180, () -> {
                        ctx.ok("操作权限已归还。");
                        ctx.glitch(GlitchIds.FREEZE_INPUT, 40, 1);
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_tear_wave", "撕裂波动", TrollTier.BRUTAL)
                .desc("画面撕裂十二秒，看什么都像坏掉的电视机，而电视机不会累，你会")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.exhaust(12.0f);
                    ctx.actionBar("§8正在重新同步显示信号……");
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 240, 2);
                    ctx.later(260, () -> ctx.warn("同步完成。你的眼睛被记录为「已适应异常」。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_mass_chat_lite", "聊天栏决堤", TrollTier.BRUTAL)
                .desc("海量假聊天灌进聊天栏，每一条都在播报你刚才做错的事")
                .weight(105)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.setFire(3);
                    ctx.fakeBroadcast("§e服务器正在广播全体玩家动态……");
                    ctx.glitch(GlitchIds.MASS_CHAT, 240, 1);
                    ctx.later(120, () -> ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES),
                            "别刷了，他的聊天栏已经满了，但他还在看。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_save_loop", "保存了二十秒", TrollTier.BRUTAL)
                .desc("假保存提示反复出现，让你永远不敢关游戏——因为关掉就等于承认白玩了")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.setFire(3);
                    ctx.fakeSaveNotice();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 1.5f);
                    ctx.glitch(GlitchIds.FAKE_SAVING, 400, 2);
                    ctx.later(300, () -> ctx.msg("§7……骗你的，根本没有在保存。你刚才等了 15 秒。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_quest_popup", "新任务", TrollTier.BRUTAL)
                .desc("弹出一个不可能完成的任务并自动接受，然后在你尝试时宣布任务失败")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.damage("magic", 1.5f);
                    ctx.sys("新任务：" + ctx.pick(TrollPools.FAKE_QUESTS));
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_ACHIEVEMENTS), "§7任务已自动接受");
                    ctx.glitch(GlitchIds.FAKE_ACHIEVEMENT, 300, 2);
                    ctx.later(200, () -> ctx.warn("任务失败。失败原因：你太当真了。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_achievement_spam", "成就刷屏", TrollTier.BRUTAL)
                .desc("假成就弹窗连着弹二十五秒，弹到你麻木——麻木正是它想要的结果")
                .weight(105)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.takeRandomItems(1);
                    ctx.playLevelUp();
                    ctx.msg("§e你今天似乎特别有天赋。");
                    ctx.glitch(GlitchIds.FAKE_ACHIEVEMENT, 500, 2);
                    ctx.later(520, () -> ctx.sys("成就展示结束。没有一个是你的。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_black_flash", "黑一下再亮", TrollTier.BRUTAL)
                .desc("突然黑屏再闪白，纯纯的视网膜按摩；它想让你习惯黑暗，好在后面省点事")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.takeRandomItems(1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 1.1f);
                    ctx.glitch(GlitchIds.BLACKOUT, 80, 2);
                    ctx.glitch(GlitchIds.FLASHBANG, 60, 2);
                    ctx.later(100, () -> ctx.msg("§8适应训练完成。下一次黑屏会久一点。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_view_meltdown", "视角融化", TrollTier.BRUTAL)
                .desc("视角乱转 + 视野乱变 + 黑屏，方向感报废后你会发现自己其实不需要眼睛")
                .weight(55)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.actionBar("§c镜头校准失败，正在瞎转。");
                    ctx.glitch(GlitchIds.SPIN, 360, 3);
                    ctx.glitch(GlitchIds.FOV_WILD, 360, 3);
                    ctx.glitch(GlitchIds.BLACKOUT, 120, 2);
                    ctx.later(380, () -> ctx.warn("镜头最终停在了一个你不想看的方向。"));
                })
                .build());

        // ==================================================================
        // ANNOYING —— 烦人：轻，但已经开始不对劲
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("nightmare_ambient_dread", "有点冷", TrollTier.ANNOYING)
                .desc("一声很轻的环境恐怖音加轻微抖动，纯气氛组——但气氛组也是它的人")
                .weight(150)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 0.9f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 12);
                    ctx.glitch(GlitchIds.SHAKY, 60, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_controls_backwards", "前后左右反了", TrollTier.ANNOYING)
                .desc("按前进就是后退，十五秒足够你掉进坑里，而坑早就挖好了")
                .weight(115)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.msg("§e方向盘校准中，请勿乱动。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 1.3f);
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 300, 2);
                    ctx.later(310, () -> ctx.warn("校准结束。你往回走的那段路，白走了。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_tear_and_spam", "撕裂 + 刷屏", TrollTier.ANNOYING)
                .desc("画面在裂，聊天在刷，两个都不让你好过——这只是热身")
                .weight(105)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.warn("显示驱动与聊天服务同时异常。");
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 240, 2);
                    ctx.glitch(GlitchIds.CHAT_SPAM, 240, 2);
                })
                .build());

        // ==================================================================
        // NIGHTMARE —— 崩溃：信息、操作、听觉一起被剥夺
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("nightmare_hidden_frozen", "看不见也动不了", TrollTier.NIGHTMARE)
                .desc("HUD 隐藏叠输入冻结，脚下同时开始渗岩浆——标准的信息、操作、退路三重剥夺")
                .weight(105)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.lavaPit(1);
                    ctx.msg("§8正在为你优化界面与操作……");
                    ctx.glitch(GlitchIds.HIDE_HUD, 300, 2);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 200, 2);
                    ctx.later(320, () -> ctx.msg("§8优化完成。你现在什么都不用做，也什么都做不了。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_tear_heavy", "撕成两半", TrollTier.NIGHTMARE)
                .desc("高强度画面撕裂二十秒，眼睛和耐心一起碎，碎片还会被收集起来给你看")
                .weight(60)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.effectRandom(400, 2);
                    ctx.title("§4§l信号丢失", "§7你的显示器正在自我了断");
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.8f);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 400, 3);
                    ctx.later(420, () -> ctx.title("§8信号找回", "§7画面回来了，你的耐心没有"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_controls_hell", "操作地狱", TrollTier.NIGHTMARE)
                .desc("输入反转再叠输入冻结，想走走不动，想停停不下，脚下还有一片岩浆在等你")
                .weight(55)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.lavaPit(1);
                    ctx.warn("§c输入映射与灵敏度同时损坏。");
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 360, 3);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 240, 3);
                    ctx.later(380, () -> ctx.msg("§c输入系统仍在自检。请原地不动——虽然你也动不了。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_mass_chat_storm", "聊天栏海啸", TrollTier.NIGHTMARE)
                .desc("海量刷屏二十五秒，重要信息全部淹没；等刷屏停下，你会发现聊天栏只剩一句话")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.effectRandom(400, 2);
                    ctx.broadcast("§c[广播] 全体玩家请注意，频道即将失控。");
                    ctx.glitch(GlitchIds.MASS_CHAT, 500, 3);
                    ctx.later(520, () -> ctx.fakeChat("系统", "刷屏结束。刚才那 25 秒里没有人找你。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_hard_blackout", "硬黑屏", TrollTier.NIGHTMARE)
                .desc("长时间硬黑屏配心跳音，安静得让人发毛；它要你在黑暗里自己想象接下来的事")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.lavaPit(1);
                    ctx.msg("§8正在进入深度渲染模式……");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 360, 3);
                    ctx.later(380, () -> {
                        ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.4f);
                        ctx.msg("§8……你刚才在黑暗里想的那些，有一半是对的。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_virus_alert", "病毒警告", TrollTier.NIGHTMARE)
                .desc("假病毒/木马警告弹窗，你被传送到别处，再被告知「扫描对象：你本人」")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.scatterFar(120);
                    ctx.warn("§c检测到可疑程序正在修改游戏内存。");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.9f);
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 500, 2);
                    ctx.later(260, () -> ctx.warn("§4扫描完成。可疑程序位置：正在操作键盘的那个人。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_driver_lost", "显卡驱动没响应", TrollTier.NIGHTMARE)
                .desc("撕裂 + 黑屏 + 巨响，演一出显卡当场去世；它会「重启」驱动，然后继续演")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.takeRandomItems(2);
                    ctx.title("§c§l显示驱动已停止响应", "§7并且拒绝重启");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 300, 3);
                    ctx.glitch(GlitchIds.BLACKOUT, 160, 3);
                    ctx.later(330, () -> {
                        ctx.ok("显示驱动已重启。");
                        ctx.glitch(GlitchIds.SCREEN_TEAR, 100, 3);
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_save_write_failed", "存档写入失败", TrollTier.NIGHTMARE)
                .desc("假存档损坏配假保存提示，让你分不清哪个是真的——两个都是假的，饿是真的")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.starve();
                    ctx.warn("§c存档写入失败：磁盘返回 I/O 错误。");
                    ctx.glitch(GlitchIds.FAKE_SAVING, 200, 2);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 440, 2);
                    ctx.later(460, () -> ctx.msg("§7重试结果：写入成功。你刚吃的东西写进去了，别的没有。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_menu_ghost", "悄悄退回标题", TrollTier.NIGHTMARE)
                .desc("画面跳回标题菜单，其实你人还站在原地；退出是一次测试，测试的是你会不会真按")
                .weight(30)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.scatterFar(120);
                    ctx.msg("§7正在保存并退出世界……");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.glitch(GlitchIds.MENU_TEASE, 500, 3);
                    ctx.later(520, () -> ctx.msg("§7测试结束。你没有退出，很好。下一次就不一定了。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_fake_ban", "你已被永久封禁", TrollTier.NIGHTMARE)
                .desc("弹出假永久封禁界面并附一条荒谬的封禁理由，退出按钮是真的，申诉入口是假的")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.scatterFar(120);
                    String excuse = ctx.pick(TrollPools.FAKE_EXCUSES);
                    ctx.warn("§4你的账号已被永久封禁。");
                    ctx.broadcast("§c[封禁] 玩家 " + ctx.playerName() + " 被封禁：§7" + excuse);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_BAN, 600, 3);
                    ctx.later(300, () -> ctx.fakeChat("客服", "您的申诉已提交，预计处理时间：下辈子。"));
                })
                .build());

        // ==================================================================
        // APOCALYPSE —— 地狱：全线失控，但你还留着最后一口气
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("nightmare_death_notice", "你已阵亡", TrollTier.APOCALYPSE)
                .desc("假死亡界面加假死因，死了但没完全死，等级被清零；三秒后它说「刚才那次是预演」")
                .weight(60)
                .cooldownSeconds(300)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.setExperience(0);
                    ctx.fakeDeathScreen(ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.glitch(GlitchIds.FAKE_DEATH, 360, 2);
                    ctx.later(80, () -> {
                        ctx.title("§8§l预演结束", "§7正式的那次，会由你自己按下去");
                        ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.1f, 0.6f);
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_connection_timeout", "连接超时", TrollTier.APOCALYPSE)
                .desc("海量刷屏叠硬黑屏，背包被清空；它假装在重连，重连次数永远不会到 99")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.clearInventory();
                    ctx.fakeBroadcast("§c连接超时，正在尝试重连（1/99）……");
                    ctx.glitch(GlitchIds.MASS_CHAT, 300, 2);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 200, 2);
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 300, 2);
                    ctx.later(340, () -> ctx.fakeBroadcast("§c重连失败（2/99）。你的物品不会跟着重连。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_oom_hidden", "内存不足 + 无界面", TrollTier.APOCALYPSE)
                .desc("假内存溢出叠隐藏 HUD 再活埋，让你在看不见、动不了、没退路的状态下恐慌")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.buryAlive();
                    ctx.sys("Java 堆空间不足，界面已被回收。");
                    ctx.glitch(GlitchIds.FAKE_OOM, 440, 2);
                    ctx.glitch(GlitchIds.HIDE_HUD, 400, 2);
                    ctx.later(460, () -> ctx.warn("垃圾回收完成。回收的包括：你的位置、你的界面、你的计划。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_update_99", "更新卡在 99%", TrollTier.APOCALYPSE)
                .desc("假更新界面进度停在 99%，经验被清零；它会爬到 99.9%，然后回滚到 1%")
                .weight(35)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.setExperience(0);
                    ctx.title("§e正在更新……", "§7请勿关闭游戏（进度 99%）");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 560, 2);
                    ctx.later(300, () -> ctx.actionBar("§e更新进度 99.9%……请再等一会……"));
                    ctx.later(420, () -> {
                        ctx.warn("更新校验失败，进度已回滚到 1%。");
                        ctx.glitch(GlitchIds.FAKE_UPDATE, 200, 2);
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_hidden_update", "看不见的更新", TrollTier.APOCALYPSE)
                .desc("HUD 隐藏叠假更新界面，你在更新中盲打，而更新内容正是取消你的操作")
                .weight(30)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.meteorShower(2, 12.0);
                    ctx.actionBar("§8更新安装中：0% ……");
                    ctx.glitch(GlitchIds.HIDE_HUD, 500, 3);
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 500, 2);
                    ctx.later(520, () -> ctx.warn("更新说明：本次更新移除了你的界面，理由是你不配看。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_ban_and_kick", "踢了，然后封了", TrollTier.APOCALYPSE)
                .desc("假掉线紧接假永久封禁，一套流程把你送走，并顺手让旁边的玩家也担惊受怕")
                .weight(22)
                .cooldownSeconds(600)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    ctx.voidPit(1, 10);
                    var o = ctx.other();
                    if (o != null) {
                        ctx.msgToOther(o, "§c你也跑不掉。");
                    }
                    ctx.broadcast("§c[管理员] " + ctx.pick(TrollPools.FAKE_OPERATOR_LINES));
                    ctx.glitch(GlitchIds.FAKE_KICK, 400, 3);
                    ctx.later(200, () -> {
                        ctx.glitch(GlitchIds.FAKE_BAN, 600, 3);
                        ctx.warn("§4掉线只是前菜。封禁才是主菜。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_triple_meltdown", "崩溃三连", TrollTier.APOCALYPSE)
                .desc("假崩溃 + 硬黑屏 + 海量刷屏一次上齐，三件套循环三遍，标题写着「马上就好」")
                .weight(28)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.buryAlive();
                    ctx.title("§4§l致命错误", "§8正在收集崩溃信息……");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 600, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 300, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 400, 3);
                    ctx.later(320, () -> ctx.title("§c§l马上就好", "§8（这是第三次说这句话了）"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_update_forever", "更新永无止境", TrollTier.APOCALYPSE)
                .desc("假更新界面长时间卡死，再配海量刷屏；更新日志里只有你一个人的名字")
                .weight(30)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.buryAlive();
                    ctx.fakeBroadcast("§e更新包下载速度：0.1 KB/s");
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 600, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 360, 2);
                    ctx.later(620, () -> ctx.fakeBroadcast("§e更新日志：修复了一个玩家过于乐观的问题。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_input_dead", "输入彻底死掉", TrollTier.APOCALYPSE)
                .desc("输入冻结 + 画面撕裂 + 硬黑屏，键盘鼠标一起失去意义；它说「请等待响应」")
                .weight(30)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.meteorShower(2, 12.0);
                    ctx.title("§8§l无响应", "§7请等待程序响应……它不会");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 440, 3);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 440, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 240, 2);
                    ctx.later(460, () -> ctx.msg("§8响应超时。系统已放弃等待你，改由它自己决定。"));
                })
                .build());

        // ==================================================================
        // OBLIVION —— 绝望：假希望、连续假结束、剥夺之后的假怜悯
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("nightmare_world_reload_stuck", "重新加载世界", TrollTier.OBLIVION)
                .desc("先让你看到进度条走完、听到升级音，再告诉你回来的只是世界的残骸——绝望从第二次坠落开始")
                .weight(35)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.msg("§e正在重新加载世界数据，请稍候……");
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 560, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.7f);
                    ctx.later(200, () -> {
                        ctx.title("§a§l加载完成", "§7世界回来了");
                        ctx.playLevelUp();
                        ctx.ok("区块已恢复，请继续你的存档。");
                    });
                    ctx.later(400, () -> {
                        ctx.title("§0§l但那不是你的世界", "§8回来的是它的残骸");
                        ctx.voidPit(2, 16);
                        ctx.buryAlive();
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_reload_crash", "重载到一半崩了", TrollTier.OBLIVION)
                .desc("连续三次宣布「这次真的结束了」，每一次之后都还有下一页——最后告诉你根本没开始过")
                .weight(25)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§4§l重载失败", "§c世界数据未能恢复");
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 440, 3);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 560, 3);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.later(120, () -> ctx.msg("§8……结束了。"));
                    ctx.later(240, () -> ctx.msg("§8真的结束了。"));
                    ctx.later(360, () -> ctx.msg("§8这次是最后一次：结束了。"));
                    ctx.later(500, () -> {
                        ctx.title("§0§l不。还没开始。", "§8前面那三次只是热身");
                        ctx.meteorShower(6, 20.0);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_virus_lockdown", "病毒隔离模式", TrollTier.OBLIVION)
                .desc("先隔离你、冻结你，再宣布「未发现威胁，已解除隔离」，最后告诉你威胁就是你本人")
                .weight(35)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.buryAlive();
                    ctx.voidPit(2, 16);
                    ctx.warn("§c已进入安全隔离模式，请勿操作电脑。");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 500, 3);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 400, 3);
                    ctx.later(300, () -> {
                        ctx.clearEffects();
                        ctx.ok("扫描完成：未发现威胁。隔离已解除，欢迎回来。");
                    });
                    ctx.later(480, () -> {
                        ctx.warn("§4解除失败。重新扫描结果：威胁就是你。");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_corrupt_reload", "损坏 + 重载", TrollTier.OBLIVION)
                .desc("剥夺你的一切，再假装要还给你：所谓「恢复完成」的是一捧灰和一份消失诅咒")
                .weight(30)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.stripAll();
                    ctx.warn("§c区块校验失败，正在从备份中恢复你的物品……");
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 500, 3);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 500, 3);
                    ctx.later(220, () -> {
                        ctx.giveRandomJunk();
                        ctx.giveRandomJunk();
                        ctx.giveRandomJunk();
                        ctx.renameHeld("§8灰烬");
                        ctx.curseHeld();
                        ctx.msg("§7恢复完成：一捧灰，和一份诅咒。不用谢。");
                    });
                    ctx.later(420, () -> {
                        ctx.msg("§4……备份里其实还有你的东西。但那份备份刚才也被格式化了。");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_menu_and_ban", "先回标题再封号", TrollTier.OBLIVION)
                .desc("假退出 → 假「误判已撤销」 → 真封禁：它先把你放回来，只为了让你看清封禁两个字")
                .weight(25)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.msg("§7正在退出世界……");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.MENU_TEASE, 500, 3);
                    ctx.later(240, () -> {
                        ctx.fakeJoinLeave(ctx.playerName(), true);
                        ctx.ok("检测到一次误判，封禁已撤销。欢迎回来。");
                    });
                    ctx.later(420, () -> {
                        ctx.warn("§4撤销的是上一次封禁。这一次是新的。");
                        ctx.glitch(GlitchIds.FAKE_BAN, 600, 3);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_end_of_world", "世界终结", TrollTier.OBLIVION)
                .desc("假封禁 + 假世界重载 + 硬黑屏 + 结局级巨响，整局只来一次；它连悼词都替你写好了")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.title("§4§l世界已终止", "§c感谢你的一路陪葬");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 600, 3);
                    ctx.glitch(GlitchIds.FAKE_BAN, 600, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 300, 3);
                    ctx.randomHorrorSound();
                    ctx.later(200, () -> ctx.sendToAll("§4[悼词] 这里曾经有一位玩家。他觉得自己还有下一次。"));
                    ctx.later(400, () -> {
                        ctx.voidPit(3, 20);
                        ctx.buryAlive();
                        ctx.effectRandom(1200, 3);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_game_shutdown", "游戏即将关闭", TrollTier.OBLIVION)
                .desc("十秒倒计时演到最后一秒，宣布「维护已取消」，然后立刻关闭——希望是用来延长痛苦的工具")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.broadcast("§c服务器将在 10 秒后关闭，原因：你。");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 560, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 400, 3);
                    ctx.later(120, () -> ctx.broadcast("§c倒计时：3……"));
                    ctx.later(200, () -> ctx.broadcast("§c倒计时：1……"));
                    ctx.later(260, () -> ctx.ok("维护已取消。你安全了。"));
                    ctx.later(420, () -> {
                        ctx.broadcast("§4……取消失败。关闭程序已启动。");
                        ctx.glitch(GlitchIds.FAKE_BAN, 600, 3);
                        ctx.glitch(GlitchIds.SCREEN_TEAR, 360, 3);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_all_screens_once", "所有界面一起上", TrollTier.OBLIVION)
                .desc("把结局级画面一次性砸下去，然后宣布「表演结束」，再补一句「谢幕还没到」")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.stripAll();
                    ctx.meteorShower(4, 16.0);
                    ctx.buryAlive();
                    ctx.effectRandom(1200, 3);
                    ctx.title("§4§l§k全部失效§r", "§8你只剩看着的份");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.FAKE_BAN, 600, 3);
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 600, 3);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 600, 3);
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 500, 3);
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 600, 3);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 400, 3);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 500, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 500, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 240, 3);
                    ctx.later(300, () -> ctx.title("§8§l表演结束", "§7谢谢观看"));
                    ctx.later(480, () -> {
                        ctx.title("§0§l谢幕还没到", "§8真正的部分是现在");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_mod_detected", "检测到修改游戏文件", TrollTier.OBLIVION)
                .desc("罪名安排得明明白白，物品全部没收；申诉会被「受理」，处理时间是下辈子")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.stripAll();
                    ctx.meteorShower(4, 16.0);
                    ctx.buryAlive();
                    ctx.effectRandom(1200, 3);
                    ctx.warn("§4检测到游戏文件被修改，正在隔离你的世界。");
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 500, 3);
                    ctx.glitch(GlitchIds.FAKE_BAN, 600, 3);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 500, 3);
                    ctx.later(220, () -> ctx.fakeChat("申诉通道", "申诉已受理。处理进度：0%。预计完成时间：下辈子。"));
                    ctx.later(420, () -> {
                        ctx.warn("§4申诉窗口已关闭。它从来没有存在过。");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_world_recovery", "正在恢复世界", TrollTier.OBLIVION)
                .desc("恢复进度条一路走到 100%，然后告诉你：恢复已完成，恢复的是一个没有你的存档")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.meteorShower(4, 16.0);
                    ctx.buryAlive();
                    ctx.effectRandom(1200, 3);
                    ctx.voidPit(2, 16);
                    ctx.title("§e正在恢复世界", "§8预计剩余时间：∞");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 600, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 300, 3);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 500, 3);
                    ctx.later(260, () -> {
                        ctx.actionBar("§a恢复进度：100%");
                        ctx.playLevelUp();
                        ctx.ok("世界恢复完成。");
                    });
                    ctx.later(440, () -> {
                        ctx.title("§0§l恢复完成", "§8已恢复到没有你的那个版本");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_replay_recording", "回放录制中", TrollTier.OBLIVION)
                .desc("它说你在被录制、被永久保存，还给你编号；观众不存在，但耻辱是永久的")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.voidPit(2, 16);
                    ctx.obliterate();
                    ctx.title("§c§l回放录制中", "§7你的表现将会被永久保存");
                    ctx.playLevelUp();
                    ctx.sendToAll("§8[回放] 正在录制 " + ctx.playerName() + " 的受难过程……");
                    ctx.glitch(GlitchIds.FAKE_ACHIEVEMENT, 500, 3);
                    ctx.glitch(GlitchIds.HIDE_HUD, 500, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 400, 3);
                    ctx.glitch(GlitchIds.FAKE_SAVING, 500, 3);
                    ctx.later(280, () -> ctx.sendToAll("§8[回放] 已归档。编号：第 "
                            + ctx.ri(1000, 9999) + " 号受害者。"));
                    ctx.later(460, () -> ctx.sendToAll("§4[回放] 观看人数：0。但那不重要。"));
                })
                .build());
    }
}
