package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【心理战】分类事件。
 * 负责整活：假监视、假跟随、假统计、假排行榜、假倒计时、假队友、假成就、
 * 假任务、假警告、假封禁、假崩溃、假"最后一关"。
 *
 * <p>本文件**不动手，只动嘴**：不使用任何直接伤害或击杀，全部靠
 * {@code ctx} 的假 UI / 假聊天 / 假公告 API 与 {@code TrollPools} 文本池，
 * 让对方自己把自己吓死。不 import 任何 net.minecraft.*。</p>
 */
public final class MindEvents {

    private MindEvents() {
    }

    public static void register() {
        TrollEvents.category("心理战");

        // ==================================================================
        // PRANK —— 小整，笑一下就好
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("mind_watcher_breath", "身后有呼吸声", TrollTier.PRANK)
                .desc("在玩家耳边放一声极轻的环境音，然后什么都不做")
                .weight(150)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 0.85f);
                    ctx.actionBar("§8你听见了不属于自己的呼吸。");
                    ctx.msg("§7（你回头了。§o这一点被记录在案。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_footsteps", "并不存在的脚步", TrollTier.PRANK)
                .desc("远处传来三声脚步，然后永远停在那里")
                .weight(140)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.soundAt(ctx.pick(TrollPools.AMBIENT_CREEPY),
                            ctx.x() + ctx.rd(-4, 4), ctx.y(), ctx.z() + ctx.rd(-4, 4), 0.9f, 1.0f);
                    ctx.msg("§7远处传来三声脚步：§8咚…咚…咚…§7然后就停了。");
                    ctx.msg("§8（它没有靠近，也没有离开。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_fake_tip", "假加载提示", TrollTier.PRANK)
                .desc("随机给一条毫无用处的加载提示")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.sys(ctx.pick(TrollPools.FAKE_TIPS));
                    ctx.actionBar("§8（本提示的存在意义：让你多想三秒。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_countdown_prank", "3…2…1…", TrollTier.PRANK)
                .desc("倒计时结束后什么都不发生，专门骗呼吸")
                .weight(130)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.title("§c§l3", "§7准备迎接一些事情");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.6f, 0.9f);
                    ctx.msg("§7§l3");
                    ctx.msg("§7§l2");
                    ctx.msg("§7§l1");
                    ctx.msg("§f……§o什么都没有发生。你刚才是不是屏住呼吸了？");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_inner_monologue", "你的内心独白", TrollTier.PRANK)
                .desc("以玩家自己的名字伪造一条聊天")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.fakeChat(ctx.playerName(), "我是不是被什么东西盯上了");
                    ctx.msg("§8（这条消息不是你发的。§o但它确实是你的心声。）");
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_teammate_join", "来了又走了", TrollTier.PRANK)
                .desc("假队友上线、说一句话、立刻下线")
                .weight(130)
                .cooldownSeconds(120)
                .run(ctx -> {
                    var name = ctx.pick(TrollPools.FAKE_NAMES);
                    ctx.fakeJoinLeave(name, true);
                    ctx.fakeChat(name, ctx.pick(TrollPools.FAKE_LINES));
                    ctx.fakeJoinLeave(name, false);
                    ctx.msg("§8[系统] §7" + name + " 的本次在线时长：§c0.4 秒§7。");
                    ctx.msg("§8（他不是来看你的。§o他是来让你以为有人来过的。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_afk_check", "挂机检测", TrollTier.PRANK)
                .desc("假装检测玩家是否还在键盘前")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.sys("检测到玩家长时间未移动，正在确认你是否还在……");
                    ctx.actionBar("§e请按任意键证明你还活着");
                    ctx.msg("§8（其实我们并不在乎。§o反正你已经被盯上了。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_fake_save", "假自动保存", TrollTier.PRANK)
                .desc("弹一个假的自动保存提示")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.msg("§8（本次保存的内容：§7你的恐惧§8。§o其余数据均未被保存，因为并不存在。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_birthday", "假生日祝福", TrollTier.PRANK)
                .desc("随机挑一天祝玩家生日快乐")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.title("§e§l生日快乐！", "§7祝你今天……被整得开心");
                    ctx.fakeBroadcast("§e今天是 §f" + ctx.playerName() + " §e的生日！全体玩家请送上祝福！");
                    ctx.msg("§7（虽然今天不是你的生日，§o但模组说了算。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_name_whisper", "有人喊你的名字", TrollTier.PRANK)
                .desc("用一个假玩家名喊出玩家的名字")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), ctx.playerName() + "？");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.6f, 0.8f);
                    ctx.msg("§8（你确认过四周了。§o没有人。）");
                    ctx.addRage(2);
                })
                .build());

        // ==================================================================
        // ANNOYING —— 烦人，会皱眉
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("mind_death_counter", "死亡统计", TrollTier.ANNOYING)
                .desc("随便编一个死亡次数并全服公告")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    int n = ctx.ri(30, 99);
                    ctx.fakeBroadcast("§7本局统计：你已死亡 §c" + n + " §7次，超过全服 §c99.8%§7 的玩家。");
                    ctx.msg("§8（统计方式：§7随机数生成器§8。§o误差范围：全部。）");
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_leaderboard", "本局排行榜", TrollTier.ANNOYING)
                .desc("三项排行榜，第三项一定是玩家本人")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.title("§6§l排行榜", "§7刷新时间：永远不会");
                    ctx.msg("§7§l本局排行榜：");
                    ctx.msg("§7 1. §f" + ctx.pick(TrollPools.FAKE_NAMES) + " §8（已离线）");
                    ctx.msg("§7 2. §f一只僵尸 §8（它什么都没做）");
                    ctx.msg("§7 3. §f" + ctx.playerName() + " §8（并列倒数第一）");
                    ctx.warn("§c你的排名：§l倒数第一§c。评语：§o还得练。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_stats_panel", "假数据面板", TrollTier.ANNOYING)
                .desc("把玩家的本局数据随机编一串出来")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.msg("§8===== §7你的本局数据 §8=====");
                    ctx.msg("§7挖方块：§f" + ctx.ri(20, 900) + " §8（其中 §c90%§8 是泥土）");
                    ctx.msg("§7步行距离：§f" + ctx.ri(100, 9000) + " 格 §8（其中 §c80%§8 是原地打转）");
                    ctx.msg("§7被苦力怕炸：§f" + ctx.ri(0, 12) + " §7次");
                    ctx.msg("§7哭的次数：§f" + ctx.ri(1, 5) + " §8（摄像头拍到的）");
                    ctx.warn("§c摄像头数量：§l1§c（就在你头顶那格。）");
                    ctx.addRage(4);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_quest_impossible", "不可能完成的任务", TrollTier.ANNOYING)
                .desc("派发一个永远做不完的假任务")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.title("§b§l新任务", "§f" + ctx.pick(TrollPools.FAKE_QUESTS));
                    ctx.msg("§7任务奖励：§e一组钻石§7（先到先得，永远不到）。");
                    ctx.actionBar("§7任务进度：§c0%");
                    ctx.msg("§8（提示：§7这个任务没有完成条件§8，因为它没有实体。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_achievement_99", "差一点点", TrollTier.ANNOYING)
                .desc("假成就，进度永远卡在 99%")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.fakeAchievement("差一点点", "成就进度停在 §e99%§7，永远不会完成");
                    ctx.msg("§8（在 99% 前面，你还有 1%。§o在 1% 前面，你还有一辈子。）");
                    ctx.addRage(4);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_fake_achievement", "恶心人的成就", TrollTier.ANNOYING)
                .desc("随机弹一个羞辱性成就")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_ACHIEVEMENTS), "恭喜你，又混过了一天");
                    ctx.msg("§8（这个成就全服只有 §f1 §8个人拿到过：§7你的上一世。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_playtime_warning", "你已经玩了太久", TrollTier.ANNOYING)
                .desc("编一个超长的连续游戏时长")
                .weight(100)
                .cooldownSeconds(240)
                .run(ctx -> {
                    int h = ctx.ri(6, 23);
                    ctx.sys("检测到连续游戏时长：§c" + h + " §7小时。建议立即休息。");
                    ctx.msg("§8（模组无权建议你休息。§o它只是想让你心里不舒服。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_health_warning", "假健康警告", TrollTier.ANNOYING)
                .desc("来自一个不存在的医疗机构的体检报告")
                .weight(100)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.title("§c§l健康警告", "§7该起来走走了，你的椅子已经记住了你的形状");
                    ctx.sys("检测结果：颈椎曲度异常、眨眼频率偏低、生命体征……哦，那是血量。");
                    ctx.msg("§8（本条警告来自 §7一个不存在的医疗机构§8，请勿当真。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_friend_invite", "假好友邀请", TrollTier.ANNOYING)
                .desc("一个假玩家想加你好友，按钮全是假的")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "加个好友吧，我看你被整得挺坚强的");
                    ctx.msg("§7[§a接受§7] [§c拒绝§7] §8（两个按钮都没反应，因为它不是真的。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_operator_watch", "管理员在看你", TrollTier.ANNOYING)
                .desc("伪造一条管理员发言")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.fakeChat("§c[管理员]§r", ctx.pick(TrollPools.FAKE_OPERATOR_LINES));
                    ctx.sys("管理员 §f" + ctx.pick(TrollPools.FAKE_NAMES) + " §7正在观察你。§8（此人不存在。）");
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_position_tracked", "位置已暴露", TrollTier.ANNOYING)
                .desc("用玩家真实坐标做一次假警告")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.warn("§c[警告] 检测到你的位置已被上传：§f"
                            + (int) ctx.x() + ", " + (int) ctx.y() + ", " + (int) ctx.z());
                    ctx.msg("§7精确到 §c小数点后 §l17 §r§7位。§8（上传目的地：一个不存在的地方。）");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_forged_chatlog", "伪造的聊天记录", TrollTier.ANNOYING)
                .desc("三条假聊天，中间一条冒充玩家自己")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), ctx.pick(TrollPools.FAKE_LINES));
                    ctx.fakeChat(ctx.playerName(), "别吓我啊");
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "我没跟你说话");
                    ctx.msg("§8（你确定中间那条是你自己发的吗？）");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_rage_monitor", "愤怒值监测", TrollTier.ANNOYING)
                .desc("播报玩家真实的愤怒值（这个数字是真的）")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.sys("愤怒值实时监测：§c" + Math.round(ctx.rage()) + " / 100");
                    ctx.msg("§8（这个数字是真的。§c我们真的在测。§8而且它会一直涨。）");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_inventory_audit", "背包审计报告", TrollTier.ANNOYING)
                .desc("随机编一份背包审计结论")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.sys("背包审计完成：共发现 §c" + ctx.ri(3, 36) + " §7件物品，其中 §c"
                            + ctx.ri(1, 30) + " §7件属于垃圾。");
                    ctx.msg("§8（审计员：§7一只路过的流浪商人§8。他看完就走了，什么都没买。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_fake_update_notice", "假更新预告", TrollTier.ANNOYING)
                .desc("宣布一次永远不会到来的更新")
                .weight(100)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e服务器将在 §f3 §e分钟后更新到 §fv2.0§e：新增 §f200 §e个整人事件");
                    ctx.msg("§8（本次更新的全部内容：§7把刚才那句话重复一遍§8。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_ban_warning", "封禁预警", TrollTier.ANNOYING)
                .desc("随机挑一个荒谬理由宣布封禁预警")
                .weight(90)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.title("§c§l封禁预警", "§7你已被列入观察名单");
                    ctx.sys("理由：§c" + ctx.pick(TrollPools.FAKE_EXCUSES));
                    ctx.msg("§8（申诉通道：§7不存在§8。申诉费用：§7也不存在§8。）");
                    ctx.addRage(3);
                })
                .build());

        // ==================================================================
        // BRUTAL —— 抓狂，会骂人
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("mind_all_alone", "只剩你一个", TrollTier.BRUTAL)
                .desc("假装所有人陆续下线，在线人数变成 1")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    var o = ctx.other();
                    if (o != null) {
                        ctx.msgToOther(o, "§8（你听见远处有人在喊你的名字。）");
                    }
                    for (int i = 0; i < 3; i++) {
                        ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), false);
                    }
                    ctx.fakeBroadcast("§c服务器在线人数：§l1§c。就你一个了。");
                    ctx.msg("§7现在没有人在看着你。§8（除了我们。）");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_teammate_betray", "队友把你卖了", TrollTier.BRUTAL)
                .desc("假队友公开分享你的坐标")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    var name = ctx.pick(TrollPools.FAKE_NAMES);
                    ctx.fakeChat(name, "他家的坐标我发群里了");
                    ctx.fakeChat(name, "对，就是那个天天被整的");
                    var o = ctx.other();
                    if (o != null) {
                        ctx.msgToOther(o, "§8[队伍] §7" + name + " 分享了你的坐标。");
                    }
                    ctx.warn("§c[警告] 你已被队友标记为「可牺牲目标」。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_dead_chat", "来自死亡频道", TrollTier.BRUTAL)
                .desc("用假死因伪造死人的聊天")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.fakeChat("§8[死亡频道]§r " + ctx.pick(TrollPools.FAKE_NAMES),
                            "死因：§f" + ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.fakeChat("§8[死亡频道]§r " + ctx.pick(TrollPools.FAKE_NAMES), "下一个就是你");
                    ctx.msg("§8（你不在死亡频道里。§o但你收到了消息。）");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_stalker_steps", "跟着你的脚步", TrollTier.BRUTAL)
                .desc("连续几声脚步停在玩家身后三格")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    int loops = ctx.ri(3, 5);
                    for (int i = 0; i < loops; i++) {
                        ctx.soundAt(ctx.pick(TrollPools.AMBIENT_CREEPY),
                                ctx.x() + ctx.rd(-6, 6), ctx.y(), ctx.z() + ctx.rd(-6, 6), 0.9f, 0.9f);
                    }
                    ctx.actionBar("§8脚步声停在你身后 §c3 §8格。");
                    ctx.msg("§7你转身——§8（转身也是白转。）");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_invisible_stalker", "隐形的跟踪者", TrollTier.BRUTAL)
                .desc("在玩家身边撒恐怖粒子并配标题")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.particleAt(ctx.pick(TrollPools.SPOOKY_PARTICLES),
                            ctx.x() + ctx.rd(-2, 2), ctx.y() + 1, ctx.z() + ctx.rd(-2, 2), 30, 0.5);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 0.7f);
                    ctx.title("§8§l它一直跟着你", "§7只是你一直没有回头");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_daily_quest_fail", "每日任务失败", TrollTier.BRUTAL)
                .desc("宣布一个玩家从没接过的任务失败了")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.title("§c§l任务失败", "§f" + ctx.pick(TrollPools.FAKE_QUESTS));
                    ctx.fakeBroadcast("§c" + ctx.playerName() + " §7的每日任务已失败，原因：§f"
                            + ctx.pick(TrollPools.FAKE_EXCUSES));
                    ctx.msg("§8（你甚至不知道你接过这个任务。）");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_pvp_flagged", "你被选为猎杀目标", TrollTier.BRUTAL)
                .desc("假 PVP 公告把玩家标成猎物")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.fakeBroadcast("§c[PVP] §f" + ctx.playerName() + " §c已被随机选为本轮猎杀目标。");
                    var o = ctx.other();
                    if (o != null) {
                        ctx.msgToOther(o, "§c[PVP] 猎杀目标已刷新：§f" + ctx.playerName());
                        ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    } else {
                        ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    }
                    ctx.msg("§7（本轮 PVP 的参赛选手只有你一个。§o祝你好运。）");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_fake_kick_tease", "连接波动", TrollTier.BRUTAL)
                .desc("假掉线界面（配置允许时才会弹出）")
                .weight(45)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_KICK, 60, 1);
                    ctx.sys("与服务器的连接出现波动……");
                    ctx.msg("§8（你还在。§7服务器也还在。§8只有你的心态可能不在了。）");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_anticheat", "反作弊系统", TrollTier.BRUTAL)
                .desc("以荒谬理由宣布反作弊已标记玩家")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.title("§c§l反作弊系统", "§7检测到异常行为：§f过于正常的操作");
                    ctx.sys("已记录 §c" + ctx.ri(3, 12) + " §7条可疑日志。正在上传……");
                    ctx.fakeBroadcast("§e反作弊系统已为你的账号添加标记：§c" + ctx.pick(TrollPools.FAKE_EXCUSES));
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_camera_feed", "调取你的录像", TrollTier.BRUTAL)
                .desc("播报两段根本不存在的录像片段")
                .weight(45)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.sys("正在调取玩家 §f" + ctx.playerName() + " §7的最近 §c30 §7分钟录像……");
                    ctx.msg("§7[录像片段 00:" + ctx.ri(10, 59) + "] 你对着空气挥了一下剑。");
                    ctx.msg("§7[录像片段 00:" + ctx.ri(10, 59) + "] 你被一只鸡推下了悬崖。");
                    ctx.warn("§c录像已归档。归档理由：§f" + ctx.pick(TrollPools.FAKE_EXCUSES));
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_last_level", "最后一关", TrollTier.BRUTAL)
                .desc("宣布进入最后一关，然后什么都不给")
                .weight(45)
                .cooldownSeconds(420)
                .when(ctx -> ctx.gameTicks() > 2400)
                .run(ctx -> {
                    ctx.title("§4§l最后一关", "§7通过即可获得自由");
                    ctx.msg("§7关卡目标：§f" + ctx.pick(TrollPools.FAKE_QUESTS));
                    ctx.msg("§7剩余时间：§c00:0" + ctx.ri(1, 9));
                    ctx.msg("§8（没有关卡。§o自由本来就有。§8但你现在已经开始怀疑了。）");
                    ctx.addRage(10);
                })
                .build());

        // ==================================================================
        // NIGHTMARE —— 崩溃，会砸键盘
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("mind_fake_crash", "假崩溃", TrollTier.NIGHTMARE)
                .desc("弹出假崩溃界面（退出按钮是真的会退出）")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 80, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 0.6f, 1.0f);
                    ctx.msg("§8（右边那个退出按钮是真的。§7建议你忍一忍。）");
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_fake_oom", "假内存溢出", TrollTier.NIGHTMARE)
                .desc("假装游戏因为内存不足崩了")
                .weight(28)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_OOM, 70, 1);
                    ctx.sys("java.lang.OutOfMemoryError: 你的脑子装不下这么多恐怖");
                    ctx.addRage(11);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_fake_corrupt", "假存档损坏", TrollTier.NIGHTMARE)
                .desc("假装存档校验失败，然后宣称已修复")
                .weight(28)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 90, 1);
                    ctx.sys("存档完整性校验：§c失败§7。正在尝试恢复……");
                    ctx.fakeSaveNotice();
                    ctx.msg("§8（恢复成功：§7你的存档从来没坏过§8。我们只是喜欢看你脸色发白。）");
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_fake_virus", "假病毒警告", TrollTier.NIGHTMARE)
                .desc("弹出假病毒警告，理由极其侮辱")
                .weight(25)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 90, 1);
                    ctx.title("§4§l检测到威胁", "§7文件 §f你的勇气.dll §7已被删除");
                    ctx.msg("§8（建议操作：§7继续玩§8。我们实在想不出别的建议。）");
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_mass_chat", "海量刷屏", TrollTier.NIGHTMARE)
                .desc("疯狂刷屏的假聊天，加上几条真假聊天")
                .weight(28)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.MASS_CHAT, 120, 2);
                    for (int i = 0; i < 4; i++) {
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), ctx.pick(TrollPools.FAKE_LINES));
                    }
                    ctx.msg("§8（聊天栏里的其他人不存在。§7但他们正在讨论你。）");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_hard_blackout", "硬黑屏", TrollTier.NIGHTMARE)
                .desc("长时间硬黑屏 + 一声心跳")
                .weight(28)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 100, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.7f, 0.6f);
                    ctx.actionBar("§8……");
                    ctx.msg("§7（黑暗中，你数了 §c" + ctx.ri(8, 40) + " §7次心跳。§8它们不是你的。）");
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_world_reload", "假世界重载", TrollTier.NIGHTMARE)
                .desc("假装正在重新加载世界数据，进度卡在 99%")
                .weight(25)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 100, 1);
                    ctx.sys("正在重新加载世界数据…… §c99%");
                    ctx.msg("§8（进度条已经卡了很久了。§7它可以一直卡着，直到你先崩溃。）");
                    ctx.addRage(13);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_you_will_die", "六十秒预言", TrollTier.NIGHTMARE)
                .desc("预言玩家将在 60 秒后死亡，然后什么都不做")
                .weight(25)
                .cooldownSeconds(900)
                .when(ctx -> ctx.gameTicks() > 2400)
                .run(ctx -> {
                    ctx.title("§4§l你将在 60 秒后死亡", "§7倒计时已开始");
                    ctx.fakeBroadcast("§c[预言系统] 目标：§f" + ctx.playerName() + "§c，剩余时间 §l60§c 秒。");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 0.7f);
                    ctx.msg("§7（预言可信度：§c0%§7。§8但你已经开始盯着血条看了。）");
                    ctx.addRage(15);
                })
                .build());

        // ==================================================================
        // APOCALYPSE —— 地狱，会退游戏（依旧不动手，只动嘴）
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("mind_final_ban", "假永久封禁", TrollTier.APOCALYPSE)
                .desc("假封禁界面 + 全服公告，退出按钮是真的。整局只触发一次")
                .weight(15)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_BAN, 200, 2);
                    ctx.title("§4§l你已被永久封禁", "§7理由：§f" + ctx.pick(TrollPools.FAKE_EXCUSES));
                    ctx.fakeBroadcast("§c" + ctx.playerName() + " §7已被模组作者亲自封禁。");
                    ctx.msg("§8（申诉码：§7" + ctx.ri(100000, 999999) + "§8。申诉通道：§7不存在§8。）");
                    ctx.addRage(20);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_game_over", "游戏结束", TrollTier.APOCALYPSE)
                .desc("假死亡提示 + 假保存 + 全服讣告，但玩家根本没死。整局只触发一次")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.fakeDeathScreen(ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.glitch(GlitchIds.FAKE_DEATH, 100, 2);
                    ctx.fakeSaveNotice();
                    ctx.fakeBroadcast("§8[§c整人大师§8] §7本局游戏结束。感谢参与，§f"
                            + ctx.playerName() + "§7。");
                    ctx.msg("§8（你没有死。§c但你刚才有一瞬间信了。）");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mind_final_countdown", "最终倒计时", TrollTier.APOCALYPSE)
                .desc("全服最终公告 + 全服画面撕裂，然后宣布没有结局。整局只触发一次")
                .weight(12)
                .cooldownSeconds(1500)
                .once()
                .run(ctx -> {
                    ctx.titleToAll("§4§l最终倒计时", "§7所有人，包括你");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.7f);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 120, 3);
                    ctx.fakeBroadcast("§4[最终公告] 本模组的整人次数已达上限。正在结算……");
                    ctx.msg("§7结算结果：你被打扰了 §c" + ctx.ri(200, 999) + " §7次，成功反抗 §c0 §7次。");
                    ctx.msg("§8（没有结局。§7模组只是想让所有人安静三秒。）");
                    ctx.addRage(22);
                })
                .build());
    }
}
