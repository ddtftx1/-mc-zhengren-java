package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;
import net.minecraft.entity.effect.StatusEffects;

/**
 * 【多人与社交】分类事件。
 * 负责整活：假 OP、假封禁、假私聊、假组队、假举报、假投票踢人、假交易、假道歉、假背叛、
 * 真人互换位置、全员连坐、全员失明、全员掉血、把人拉到一个点、假人数已满、假全服封锁。
 *
 * <p><b>单人兼容规则</b>：{@code ctx.other()} 在单人游戏里返回 {@code null}，
 * 本文件里所有用到它的地方都先存进 {@code var o} 再判空；
 * 绝大多数事件在无人时会退化成「伪造假玩家消息 / 假公告」，保证单人也有节目。
 * 只有极少数纯粹依赖第二名玩家的整活才会加 {@code .when(ctx -> ctx.hasOthers())}。
 * 这些事件的 run 里同样保留判空分支，绝不裸奔。</p>
 */
public final class SocialEvents {

    private SocialEvents() {
    }

    // ==================================================================
    // 自己的文案素材（纯字符串数组，不涉及任何 Minecraft API）
    // ==================================================================

    private static final String[] FAKE_DM_LINES = {
            "喂，在吗？", "你刚才是不是挖了我的矿？", "别装了，我都看见了",
            "兄弟，借点钻石，不还的那种", "你家在哪儿？我送点东西过去（是 TNT）",
            "刚才那个坑是我挖的，抱歉（并不抱歉）", "你死后装备我帮你捡了，现在归我了",
            "加个好友呗，我保证不炸你家", "你掉的那把剑我拿走了啊", "别下线，我还没整够"
    };

    private static final String[] TEAM_INVITE_LINES = {
            "邀请你加入队伍「冤种联盟」",
            "邀请你加入队伍「待宰羔羊」",
            "邀请你加入队伍「背锅侠小队」",
            "邀请你加入队伍「最后一个死的」",
            "邀请你加入队伍「移动经验包」"
    };

    private static final String[] APOLOGY_LINES = {
            "对不起，上次炸你家真的是意外。",
            "抱歉，你的钻石是我拿的，但我不会还。",
            "上次把你推进岩浆是手滑，真的。",
            "我错了，下次还敢。",
            "对不起，我把你的床拆了——因为你睡得太香了。",
            "抱歉，你的牛是我放的岩浆，它走得很快。"
    };

    private static final String[] TRADE_LINES = {
            "请求与你交易：1 个钻石 ⇄ 你的全部家当",
            "请求与你交易：1 块腐肉 ⇄ 你手里的剑",
            "请求与你交易：一空气 ⇄ 你的整个背包",
            "请求与你交易：1 个土豆 ⇄ 64 个铁锭",
            "请求与你交易：不交易 ⇄ 你的时间"
    };

    private static final String[] REPORT_LINES = {
            "举报了你：原因「菜得影响服务器运算速度」",
            "举报了你：原因「长得像外挂」",
            "举报了你：原因「挖矿姿势过于专业」",
            "举报了你：原因「呼吸声太大，吵到怪物了」",
            "举报了你：原因「把服务器玩成了单机」"
    };

    private static final String[] PVP_LINES = {
            "§cPVP 已开启：从此刻起，队友也是敌人。",
            "§cPVP 已开启：请互相残杀，本模组会为你们鼓掌。",
            "§cPVP 已开启：你的后背最好自己守着。",
            "§cPVP 已开启：友情已过期，请重新购买。"
    };

    private static final String[] COORDS_LINES = {
            "坐标 128, 64, -742 —— 快来，这里全是钻石！",
            "坐标 -9999, -60, 9999 —— 满地下界合金，速来！",
            "坐标 0, 320, 0 —— 上来，我在天上等你。",
            "坐标 42, 11, 42 —— 别问，来了就知道。"
    };

    private static final String[] PARTY_KICK_LINES = {
            "§c你已被移出队伍「冤种联盟」。",
            "§c队长把你踢了，理由：你太菜，带队压力大。",
            "§c队伍已解散，你被移出了所有群聊。"
    };

    private static final String[] SOCIAL_TAUNTS = {
            "§7你在这个服务器里还有朋友吗？",
            "§7别装了，他们都在看你笑话。",
            "§7社交也是需要天赋的，你没有。",
            "§7没人回你消息，是因为没人想回。",
            "§7你的队友正在假装没看见你。"
    };

    // ==================================================================
    // 事件注册
    // ==================================================================

    public static void register() {
        TrollEvents.category("多人与社交");

        // ===============================================================
        // PRANK（小整）
        // ===============================================================

        TrollEvents.reg(TrollEvent.of("social_fake_op_announce", "假 OP 通知", TrollTier.ANNOYING)
                .desc("伪造一条「你被设为管理员」的全服公告，再配上升级音效")
                .weight(170)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.playLevelUp();
                    ctx.sendToAll(ctx.format("§e[服务器] 已将 §b%p §e设为服务器管理员。"));
                    ctx.msg("§7（假的。你现在连 /kick 都用不了。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_friend_join_dm", "好友上线了", TrollTier.BRUTAL)
                .desc("伪造好友上线提示，再补一条对方发来的私聊")
                .weight(165)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    var fake = ctx.pick(TrollPools.FAKE_NAMES);
                    ctx.fakeJoinLeave("§b" + fake, true);
                    ctx.fakeChat(fake, "§f我来啦，你在哪儿？你家还是老地方吧？");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_team_invite", "假组队邀请", TrollTier.ANNOYING)
                .desc("伪造一条组队邀请，队名一个比一个难听")
                .weight(160)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "§e" + ctx.pick(TEAM_INVITE_LINES));
                    ctx.msg("§7（这个队伍不存在。就像你的队友一样。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_fake_dm_admin", "假管理员私聊", TrollTier.ANNOYING)
                .desc("伪装成管理员发来一条私聊，内容全是阴阳怪气")
                .weight(160)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.fakeChat("§c[管理员] §f",
                            ctx.pick(TrollPools.FAKE_OPERATOR_LINES));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_fake_apology", "假道歉", TrollTier.ANNOYING)
                .desc("伪造一条玩家的道歉私聊，越看越气")
                .weight(150)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "§f" + ctx.pick(APOLOGY_LINES));
                    ctx.msg("§7（他道歉了，但他什么都没还。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_trade_request", "假交易请求", TrollTier.BRUTAL)
                .desc("伪造一条交易请求，条件离谱到让人想报警")
                .weight(150)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    var o = ctx.other();
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "§6" + ctx.pick(TRADE_LINES));
                    if (o != null) {
                        ctx.msgToOther(o, "§7[交易] 你对 " + ctx.playerName() + " 发起了交易请求（其实是假的）。");
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_report_notice", "假举报通知", TrollTier.ANNOYING)
                .desc("伪造一条「有人举报了你」的系统通知，理由特别欠揍")
                .weight(150)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.fakeBroadcast("§c[举报系统] " + ctx.pick(TrollPools.FAKE_NAMES) + " "
                            + ctx.pick(REPORT_LINES));
                    ctx.msg("§7（举报属实，但你不会受到任何惩罚。这才是最气人的。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_bed_occupied", "有人睡了你的床", TrollTier.BRUTAL)
                .desc("伪造一条「有人在你的床上睡觉」的提示，逼你立刻往家跑")
                .weight(145)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.fakeBroadcast(ctx.format("§e[提示] 玩家 %p 的床当前被 §b"
                            + ctx.pick(TrollPools.FAKE_NAMES) + " §e占用中。"));
                    ctx.msg("§7（家还是你的。床也是。睡意没了。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_house_claimed", "你的房子被认领了", TrollTier.BRUTAL)
                .desc("伪造一条「领地认领成功」的公告，房主写的却是别人的名字")
                .weight(145)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.fakeBroadcast("§e[领地] 建筑认领成功，所有者：§b"
                            + ctx.pick(TrollPools.FAKE_NAMES));
                    ctx.warn("你的房子现在是别人的了。行李请自行搬走。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_party_kick", "你被踢出队伍", TrollTier.BRUTAL)
                .desc("伪造一条「你已被移出队伍」的提示，理由让人无法反驳")
                .weight(145)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.fakeBroadcast(ctx.pick(PARTY_KICK_LINES));
                    ctx.msg("§7（你本来就没在队伍里。但你还是被踢了。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_swap_lie", "有人跟你换了位置", TrollTier.BRUTAL)
                .desc("谎称有人和你互换了位置，同时真的把你挪了一下（单人兜底版）")
                .weight(130)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    var o = ctx.other();
                    if (o != null) {
                        ctx.sendToAll(ctx.format("§d[提示] %p 的位置刚刚被「整理」了一下。"));
                        ctx.scatter(6);
                    } else {
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "我把你挪了个地方，惊喜吗？");
                        ctx.scatter(12);
                    }
                    ctx.msg("§7（站的位置变了，但没人在你旁边。这更吓人。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_gift_lie", "假礼物", TrollTier.BRUTAL)
                .desc("谎称有人给你送了礼物，然后你真的收到一堆垃圾（单人兜底版）")
                .weight(140)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    var o = ctx.other();
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "给你寄了点东西，签收一下。");
                    ctx.giveRandomJunk();
                    if (o != null) {
                        ctx.msgToOther(o, "§7[系统] 你把一份「礼物」寄给了 " + ctx.playerName() + "（并没有）。");
                    }
                    ctx.msg("§7（包裹已签收，内容物概不退换。）");
                })
                .build());

        // ===============================================================
        // ANNOYING（烦人）
        // ===============================================================

        TrollEvents.reg(TrollEvent.of("social_operator_speech", "假管理员训话", TrollTier.BRUTAL)
                .desc("让假管理员在全服频道上发表一段训话，字字诛心")
                .weight(140)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.fakeBroadcast("§c[管理员] §f" + ctx.pick(TrollPools.FAKE_OPERATOR_LINES));
                    ctx.titleToAll("§c§l管理员广播", "§7请全体玩家反思自己");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_mute_notice", "你被禁言了", TrollTier.BRUTAL)
                .desc("伪造一条禁言通知，让你下一秒怀疑自己发的消息到底出没出去")
                .weight(135)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.fakeBroadcast(ctx.format("§c[聊天] 玩家 %p 已被禁言 5 分钟，原因：说话太多。"));
                    ctx.msg("§7（你并没有被禁言。但你现在不敢打字了，对吧？）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_pvp_declared", "假 PVP 开启", TrollTier.BRUTAL)
                .desc("全服弹出 PVP 开启标题并配上公告，让所有人开始互相提防")
                .weight(135)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.titleToAll("§4§lPVP 已开启", "§7从此刻起，没有人是安全的");
                    ctx.sendToAll("§c" + ctx.pick(PVP_LINES));
                    ctx.soundToAll(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_vote_kick", "假投票踢人", TrollTier.BRUTAL)
                .desc("伪造一场针对你的投票，票数还会实时上涨，让你眼睁睁看着自己被投出去")
                .weight(130)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.fakeBroadcast(ctx.format("§e[投票] 正在表决是否踢出玩家 %p ……"));
                    int a = ctx.ri(2, 4);
                    int b = ctx.ri(5, 9);
                    ctx.fakeBroadcast("§e[投票] 赞成 §c" + a + " §e/ 反对 §a" + b + " §e——反对无效，正在执行。");
                    ctx.title("§c§l投票结果", "§7你被投票踢出了（并没有）");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_dm_flood", "私聊轰炸", TrollTier.BRUTAL)
                .desc("让一堆假玩家轮流给你发私聊，顺手也给服务器里其他人各塞几条")
                .weight(130)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    for (int i = 0; i < ctx.ri(5, 9); i++) {
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES),
                                "§7[私聊] §f→ 你：" + ctx.pick(FAKE_DM_LINES));
                    }
                    for (var p : ctx.allPlayers()) {
                        if (p == ctx.player()) {
                            continue;
                        }
                        ctx.msgToOther(p, "§7[私聊] §b" + ctx.pick(TrollPools.FAKE_NAMES) + " §f→ 你："
                                + ctx.pick(FAKE_DM_LINES));
                    }
                    ctx.sys("（以上私聊均为本模组代发。你的社交圈比想象中热闹。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_ban_warning", "假封禁警告", TrollTier.NIGHTMARE)
                .desc("它把你的名字写进封禁候补名单，理由是从别人的聊天记录里摘出来的")
                .weight(125)
                .cooldownSeconds(360)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.fakeBroadcast(ctx.format("§c[警告] 玩家 %p 因「" + ctx.pick(TrollPools.FAKE_EXCUSES)
                            + "」被列入封禁候补名单。"));
                    ctx.warn("请立刻表现出悔意。虽然并不会有用。");
                    ctx.addRage(4);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_all_same_name", "全员被同名", TrollTier.BRUTAL)
                .desc("宣布全服物品已被统一改名，并真的给你的随机一件物品换个羞辱名字")
                .when(ctx -> !ctx.invEmpty())
                .weight(120)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    String cursed = ctx.pick(TrollPools.CURSED_NAMES);
                    ctx.renameRandomItem(cursed);
                    ctx.sendToAll("§e[服务器] 全服物品命名规范已更新，统一前缀：「"
                            + cursed + "§e」。");
                    ctx.msg("§7（只有你的物品被改了。这是一次精准的规范化。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_server_full_notice", "假人数已满", TrollTier.NIGHTMARE)
                .desc("它宣布服务器满了。从此刻起，外面的朋友一个都进不来")
                .weight(125)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.titleToAll("§c§l服务器已满", "§7新玩家正在门口排队");
                    ctx.sendToAll("§c[服务器] 在线人数已达上限，请勿再邀请好友。");
                    ctx.msg("§7（其实还有一个空位。就是留给整你的那个。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_gift_junk_real", "真的给你塞垃圾", TrollTier.BRUTAL)
                .desc("当着你的面把一堆垃圾塞进另一个玩家的背包；单人时改成给自己寄垃圾")
                .weight(120)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    var o = ctx.other();
                    var item = ctx.pick(TrollPools.JUNK);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    if (o != null) {
                        ctx.giveOther(o, item, ctx.ri(8, 32));
                        ctx.msg("§7你把一堆「礼物」悄悄塞进了别人的背包。");
                        ctx.msgToOther(o, "§e你收到了一份来路不明的礼物，已自动放入背包。");
                    } else {
                        ctx.give(item, ctx.ri(8, 32));
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "礼物收到了吗？不用谢。");
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_inventory_steal_claim", "假偷窃通知", TrollTier.NIGHTMARE)
                .desc("有人说他翻过你的背包，并且把你亲手摆好的东西又摆了一遍")
                .when(ctx -> !ctx.invEmpty())
                .weight(120)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "你背包我翻过了，东西还行，就是有点寒酸。");
                    ctx.scrambleHotbar();
                    ctx.warn("快捷栏被动过了。请自行整理，别问是谁。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_friend_request", "假好友申请", TrollTier.NIGHTMARE)
                .desc("它替你收到一条好友申请，又替你点了拒绝，还很贴心地附上了理由")
                .weight(130)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.fakeChat("§7[好友] §b" + ctx.pick(TrollPools.FAKE_NAMES), "§f请求添加你为好友。");
                    ctx.fakeChat("§7[好友] §b" + ctx.pick(TrollPools.FAKE_NAMES), "§f请求已自动拒绝。");
                    ctx.msg(ctx.pick(SOCIAL_TAUNTS));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_coords_share", "假队友坐标", TrollTier.BRUTAL)
                .desc("伪造队友发来的「满地钻石」坐标，全都是假的，去了只有空气")
                .weight(125)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    var o = ctx.other();
                    var line = ctx.pick(COORDS_LINES);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "§f" + line);
                    if (o != null) {
                        ctx.msgToOther(o, "§7[队伍] " + ctx.playerName() + " 分享了坐标：" + line
                                + " §8（这条坐标是编的）");
                    }
                    if (ctx.chance(0.5)) {
                        ctx.msg("§7你信了。你真的信了。");
                        ctx.scatter(24);
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_whisper_loop", "私聊循环", TrollTier.BRUTAL)
                .desc("给你和服务器里其他人互相投递假私聊，制造一场所有人都在聊天的幻觉")
                .weight(120)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    for (int i = 0; i < ctx.ri(4, 7); i++) {
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES),
                                "§8[悄悄话] §8» §7" + ctx.pick(SOCIAL_TAUNTS));
                    }
                    for (var p : ctx.allPlayers()) {
                        if (p == ctx.player()) {
                            continue;
                        }
                        ctx.msgToOther(p, "§8[悄悄话] §b" + ctx.pick(TrollPools.FAKE_NAMES) + " §8» §7"
                                + ctx.pick(TrollPools.FAKE_LINES));
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.1f);
                })
                .build());

        // ===============================================================
        // BRUTAL（抓狂）
        // ===============================================================

        TrollEvents.reg(TrollEvent.of("social_swap_places", "位置对调", TrollTier.NIGHTMARE)
                .desc("真的和另一名玩家互换位置，两人同时在空中凌乱（纯多人事件）")
                .when(ctx -> ctx.hasOthers())
                .weight(80)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    var o = ctx.other();
                    if (o != null) {
                        ctx.swapWith(o);
                        ctx.sendToAll(ctx.format("§d[提示] %p 和另一名玩家的位置已完成「友好交流」。"));
                        ctx.soundToAll(ctx.pick(TrollPools.CREEPY), 1.2f, 0.8f);
                        ctx.msgToOther(o, "§d你被换到了一个陌生的地方。别担心，那边风景不错。");
                    } else {
                        // 兜底：when 已经保证有人，但这里绝不裸奔
                        ctx.fakeBroadcast("§d[提示] 位置交换目标不足，已改为交换你的上下半身。");
                        ctx.randomRotation();
                    }
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_pull_everyone", "全体集合", TrollTier.NIGHTMARE)
                .desc("把半径内的所有人强行拉到你身边开「线下会议」（纯多人事件）")
                .when(ctx -> ctx.hasOthers())
                .weight(75)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    var o = ctx.other();
                    ctx.pullPlayersToMe(256.0);
                    ctx.soundToAll(ctx.pick(TrollPools.FUNNY), 1.2f, 0.75f);
                    if (o != null) {
                        ctx.titleToAll("§e§l全体集合", "§7会议地点：你的脸上");
                        ctx.sendToAll(ctx.format("§e[队伍] 全员已集合到 %p 身边，请互相贴脸。"));
                    } else {
                        ctx.fakeBroadcast("§e[队伍] 全员集合完毕（虽然只有你一个人）。");
                    }
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_betrayal_teleport", "假背叛", TrollTier.APOCALYPSE)
                .desc("谎称队友把你传送走了，实际上是你把对方拉到了自己脸上")
                .weight(80)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    var o = ctx.other();
                    if (o != null) {
                        ctx.teleportOtherToMe(o);
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "我把你传走了，别回头。");
                        ctx.msgToOther(o, "§c你被队友卖了一次。他现在就站在你面前，笑得很开心。");
                    } else {
                        ctx.scatter(30);
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "我把你传走了，别回头。");
                        ctx.msg("§7（确实没人背叛你。你只是单纯被整了。）");
                    }
                    ctx.randomRotation();
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_lightning_duel", "定点雷击", TrollTier.BRUTAL)
                .desc("在另一名玩家头顶劈下一道雷；单人时改劈你自己脚下")
                .weight(70)
                .cooldownSeconds(480)
                .run(ctx -> {
                    var o = ctx.other();
                    if (o != null) {
                        ctx.lightningAtPlayer(o);
                        ctx.sendToAll("§e[天气] 检测到局部雷暴，落点恰好是某位玩家。太巧了。");
                        ctx.soundToAll(ctx.pick(TrollPools.IMPACT), 1.2f, 0.8f);
                    } else {
                        ctx.lightningNear();
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "我看你头顶云有点厚，帮你引了道雷。");
                    }
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_all_damage", "全员掉血", TrollTier.APOCALYPSE)
                .desc("给全服每个人来一下「不明来源」的伤害，连你自己也跑不掉")
                .weight(75)
                .cooldownSeconds(480)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    for (var p : ctx.allPlayers()) {
                        if (p == ctx.player()) {
                            continue;
                        }
                        ctx.damageOther(p, 3.0f);
                    }
                    ctx.sendToAll("§c[服务器] 全体玩家受到了「服务器情绪波动」影响。");
                    ctx.soundToAll(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    if (ctx.chance(0.5)) {
                        ctx.damage(2.0f);
                        ctx.msg("§7（连坐规则：你也得挨一下，公平。）");
                    }
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_all_blind", "全员失明", TrollTier.BRUTAL)
                .desc("全服一起失明，谁也看不见谁，谁也不知道该怪谁")
                .weight(70)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.effectAll(StatusEffects.BLINDNESS, ctx.ri(120, 240), 0);
                    ctx.titleToAll("§0§l全体失明", "§7别乱动，你旁边的人也不知道你在哪儿");
                    ctx.sendToAll("§8[服务器] 照明系统已下线。请凭记忆行动。");
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 0.9f, 0.8f);
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_fake_ban_ui", "假封禁界面", TrollTier.NIGHTMARE)
                .desc("弹出带申诉码的假永久封禁界面，全服一起看你的「罪状」")
                .weight(70)
                .cooldownSeconds(900)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.fakeBroadcast(ctx.format("§c[封禁] 玩家 %p 已被永久封禁，理由："
                            + ctx.pick(TrollPools.FAKE_EXCUSES)));
                    ctx.title("§4§l你已被封禁", "§7申诉码：NOPE-0000-YOU");
                    ctx.glitch(GlitchIds.FAKE_BAN, 100, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.7f);
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_give_tnt", "礼尚往来", TrollTier.APOCALYPSE)
                .desc("把一叠危险品塞给另一名玩家，再祝他「使用愉快」")
                .weight(70)
                .cooldownSeconds(600)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    var o = ctx.other();
                    var item = ctx.pick(TrollPools.DANGEROUS);
                    if (o != null) {
                        ctx.giveOther(o, item, ctx.ri(4, 16));
                        ctx.msgToOther(o, "§c有人送了你一批「危险品」，已放入背包。祝使用愉快。");
                        ctx.sendToAll("§e[物流] 一份加急包裹已送达，收件人拒绝透露姓名。");
                    } else {
                        ctx.give(item, ctx.ri(2, 6));
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "礼物收到了吗？拆的时候小心点。");
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_scatter_other", "把队友送走", TrollTier.APOCALYPSE)
                .desc("把另一名玩家随机传送到远处，然后假装是「服务器波动」")
                .weight(70)
                .cooldownSeconds(540)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    var o = ctx.other();
                    if (o != null) {
                        ctx.scatterOther(o, 250);
                        ctx.sendToAll("§e[服务器] 检测到局部坐标抖动，已自动将一名玩家「重新分布」。");
                        ctx.msgToOther(o, "§e你被传送到了一个新地方。这不是 bug，这是服务。");
                    } else {
                        ctx.scatterFar(200);
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "刚才那下传送是我干的，不用谢。");
                    }
                    ctx.soundToAll(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_death_match", "假死亡竞赛", TrollTier.APOCALYPSE)
                .desc("宣布死亡竞赛开始，全服弹标题、全员掉血，气氛直接拉满")
                .weight(65)
                .cooldownSeconds(720)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.titleToAll("§4§l死亡竞赛", "§7活着的人才有资格继续被整");
                    ctx.sendToAll("§c[赛事] 死亡竞赛已开始，请各位互相伤害，不要客气。");
                    for (var p : ctx.allPlayers()) {
                        if (p == ctx.player()) {
                            continue;
                        }
                        ctx.damageOther(p, 2.0f);
                    }
                    ctx.soundToAll(ctx.pick(TrollPools.IMPACT), 1.2f, 0.75f);
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_tag_team_punish", "全员连坐", TrollTier.NIGHTMARE)
                .desc("宣布「队友犯的错由你承担」，然后把全服拖下水一起掉血")
                .weight(65)
                .cooldownSeconds(720)
                .run(ctx -> {
                    ctx.sendToAll(ctx.format("§c[连坐] 由于 %p 的队友表现不佳，全服玩家共同承担后果。"));
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), ctx.ri(120, 240), 0);
                    for (var p : ctx.allPlayers()) {
                        if (p == ctx.player()) {
                            continue;
                        }
                        ctx.damageOther(p, 1.5f);
                    }
                    ctx.damage(1.0f);
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.addRage(8);
                })
                .build());

        // ===============================================================
        // NIGHTMARE（崩溃）
        // ===============================================================

        TrollEvents.reg(TrollEvent.of("social_all_to_me", "全员到我这里来", TrollTier.OBLIVION)
                .desc("队友说他们马上就到，让你撑住。他们确实来了——只是不是来救你的")
                .when(ctx -> ctx.hasOthers())
                .weight(50)
                .cooldownSeconds(900)
                .run(ctx -> {
                    var o = ctx.other();
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "撑住！我们马上到，别下线！");
                    ctx.title("§c§l支援在路上", "§7所有人正在赶来");
                    ctx.later(70, () -> {
                        ctx.pullPlayersToMe(512.0);
                        ctx.titleToAll("§4§l天罚", "§7所有人已被集中处理");
                        ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.2f, 0.8f);
                        if (o != null) {
                            ctx.msgToOther(o, "§4你被选中了。原因是：你离得最近。");
                        }
                    });
                    ctx.later(150, () -> {
                        for (int i = 0; i < 5; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                        }
                        ctx.lightningNear();
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                        ctx.sendToAll("§4[天罚] 支援已抵达。§7请各位保持队形，不要回头。");
                    });
                    ctx.later(230, () -> {
                        ctx.obliterate();
                        ctx.stripAll();
                    });
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_permanent_ban", "永久封禁倒计时", TrollTier.APOCALYPSE)
                .desc("全服播报你的永久封禁倒计时。中途有人说要替你去说情，然后再也没回来")
                .weight(45)
                .cooldownSeconds(1200)
                .run(ctx -> {
                    ctx.sendToAll(ctx.format("§c[封禁] 玩家 %p 的永久封禁将在 3 秒后生效。"));
                    ctx.titleToAll("§4§l封禁倒计时", "§7请看最后一眼这个世界");
                    ctx.fakeBroadcast("§c[封禁] 3 …… 2 …… 1 ……（开玩笑的，但你的心跳是真的）");
                    ctx.glitch(GlitchIds.FAKE_BAN, 120, 3);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.7f);
                    var name = ctx.pick(TrollPools.FAKE_NAMES);
                    ctx.fakeChat(name, "别慌，我去找管理员说情，你等我。");
                    ctx.later(120, () -> {
                        ctx.fakeChat(name, "……他不肯见我。祝你好运。");
                        ctx.fakeJoinLeave(name, false);
                        ctx.msg("§8（他说的是真的吗？§7你怎么确定刚才那个「他」是真的？）");
                    });
                    ctx.later(240, () -> ctx.effectRandom(300, 2));
                    ctx.addRage(11);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_all_cursed", "全员被诅咒", TrollTier.OBLIVION)
                .desc("它把你们的痛苦编成一场团队建设，还要求你们在心里感谢彼此")
                .weight(45)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.sendToAll("§5[团队建设] 全员已获得随机祝福（本模组对「祝福」的定义比较宽泛）。");
                    ctx.titleToAll("§5§l全员被诅咒", "§7让我们痛苦地团结在一起");
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.75f);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), ctx.ri(200, 400), 1);
                    ctx.later(100, () -> {
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), ctx.ri(200, 400), 1);
                        ctx.sendToAll("§5[团队建设] 第二轮开始了。请在心里感谢你的队友。");
                    });
                    ctx.later(200, () -> {
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 2);
                        for (var p : ctx.allPlayers()) {
                            if (p != ctx.player()) {
                                ctx.damageOther(p, 2.0f);
                            }
                        }
                        ctx.buryAlive();
                        ctx.obliterate();
                    });
                    ctx.addRage(11);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_world_reload", "假世界重载", TrollTier.APOCALYPSE)
                .desc("全服弹出「正在重新加载世界数据」的卡死进度，配合假公告制造恐慌")
                .weight(45)
                .cooldownSeconds(1200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.sendToAll("§e[服务器] 正在重新加载世界数据，请勿关闭游戏。");
                    ctx.titleToAll("§e§l重新加载中", "§799% …… 99% …… 99%");
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 140, 3);
                    ctx.fakeSaveNotice();
                    ctx.soundToAll(ctx.pick(TrollPools.IMPACT), 0.9f, 0.8f);
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_betrayal_gift_tnt", "背叛的礼物", TrollTier.NIGHTMARE)
                .desc("他给你寄了一份礼物，还特意提醒你：拆的时候记得站起来")
                .weight(45)
                .cooldownSeconds(900)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    var o = ctx.other();
                    if (o != null) {
                        ctx.giveOther(o, ctx.pick(TrollPools.DANGEROUS), 8);
                        ctx.msgToOther(o, "§c你的队友送来了一份「礼物」。建议不要打开。");
                        ctx.sendToAll(ctx.format("§e[物流] %p 给某人寄了一份会响的礼物。"));
                    } else {
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "我给你寄了份礼物，记得签收。");
                        ctx.give(ctx.pick(TrollPools.DANGEROUS), 4);
                    }
                    ctx.soundToAll(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg(ctx.pick(SOCIAL_TAUNTS));
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_curse_all_names", "全员被改名羞辱", TrollTier.APOCALYPSE)
                .desc("宣布全服物品统一改名，顺手给全服上个随机负面效果，再当众羞辱你一句")
                .weight(40)
                .cooldownSeconds(1200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    if (!ctx.invEmpty()) {
                        ctx.renameRandomItem(ctx.pick(TrollPools.CURSED_NAMES));
                    }
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), ctx.ri(160, 320), 0);
                    ctx.sendToAll("§5[服务器] 全服物品命名规范已更新，请各位自查。");
                    ctx.title("§5§l你被点名了", ctx.pick(TrollPools.TAUNTS));
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.addRage(10);
                })
                .build());

        // ===============================================================
        // APOCALYPSE（地狱）
        // ===============================================================

        TrollEvents.reg(TrollEvent.of("social_server_lockdown", "全服封锁", TrollTier.OBLIVION)
                .desc("它宣布全服封锁，所有人原地待命——包括那些本来可以来救你的人（每局一次）")
                .weight(25)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.sendToAll("§4[服务器] 检测到严重违规，全服进入封锁状态，所有人原地待命。");
                    ctx.titleToAll("§4§l全服封锁", "§7没有例外，包括你");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 2.0f, 0.6f);
                    ctx.glitch(GlitchIds.FAKE_BAN, 160, 3);
                    ctx.effectAll(StatusEffects.SLOWNESS, 200, 1);
                    ctx.later(100, () -> {
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "门锁了，出不去了。谁去跟管理员说一声？");
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "别看我，我的号也没了。");
                    });
                    ctx.later(200, () -> {
                        ctx.fakeBroadcast("§4[服务器] 封锁结束。§7违规者已处理：§f" + ctx.playerName());
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 200, 2);
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.obliterate();
                    });
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_purge", "全员大清洗", TrollTier.OBLIVION)
                .desc("它说这是例行维护。维护内容是把所有人拖到同一个点，然后逐一处理（纯多人事件）")
                .when(ctx -> ctx.hasOthers())
                .weight(22)
                .cooldownSeconds(1800)
                .run(ctx -> {
                    ctx.pullPlayersToMe(1024.0);
                    ctx.sendToAll("§4[服务器] 例行维护即将开始，请各位到指定地点集合。");
                    ctx.titleToAll("§4§l大清洗", "§7所有人都是目标，包括你");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 2.0f, 0.6f);
                    ctx.later(90, () -> {
                        for (var p : ctx.allPlayers()) {
                            if (p != ctx.player()) {
                                ctx.damageOther(p, 6.0f);
                            }
                        }
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), ctx.ri(200, 400), 1);
                        ctx.sendToAll("§4[服务器] 维护进度 50%。感谢配合。");
                    });
                    ctx.later(190, () -> {
                        ctx.sendToAll("§4[服务器] 例行维护已完成，请各位检查自己的血量。");
                        ctx.buryAlive();
                        ctx.obliterate();
                        ctx.stripAll();
                    });
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_last_one_left", "只剩你一个人", TrollTier.OBLIVION)
                .desc("所有人一个个下线，服务器把在线名单念给你听，最后只剩一个名字")
                .weight(25)
                .cooldownSeconds(1800)
                .run(ctx -> {
                    int n = 0;
                    for (var p : ctx.allPlayers()) {
                        if (p != ctx.player()) {
                            n++;
                            ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), false);
                        }
                    }
                    if (n == 0) {
                        ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), true);
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "我在。别怕。");
                        ctx.later(120, () -> {
                            ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), false);
                            ctx.msg("§8他走了。§7他甚至没等你回答。");
                        });
                    } else {
                        ctx.msg("§8他们都还在。§7只是没有人再回你的消息。");
                    }
                    ctx.title("§8§l只剩你了", "§7现在没人能听见你说话");
                    ctx.soundToAll(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 0.8f);
                    ctx.later(220, () -> {
                        ctx.buryAlive();
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 2);
                        ctx.obliterate();
                    });
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("social_apocalypse_party", "社交末日派对", TrollTier.OBLIVION)
                .desc("它请来了所有人。派对的流程是：先抱在一起，再一起消失")
                .weight(20)
                .cooldownSeconds(2400)
                .run(ctx -> {
                    var o = ctx.other();
                    ctx.titleToAll("§4§l社交末日", "§7从今往后，请不要再相信任何人");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 2.0f, 0.6f);
                    if (o != null) {
                        ctx.swapWith(o);
                        ctx.pullPlayersToMe(1024.0);
                        ctx.msgToOther(o, "§4你被卷进了一场末日派对。组织者就在你旁边。");
                    } else {
                        ctx.scatterFar(120);
                        ctx.fakeBroadcast("§4[末日] 派对人数不足，已改为单人专场。");
                    }
                    ctx.later(100, () -> {
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), ctx.ri(240, 480), 1);
                        ctx.glitch(GlitchIds.FAKE_BAN, 120, 3);
                        for (var p : ctx.allPlayers()) {
                            if (p != ctx.player()) {
                                ctx.damageOther(p, 3.0f);
                            }
                        }
                        ctx.sendToAll("§4[服务器] 派对进入第二环节：请互相道别。");
                    });
                    ctx.later(210, () -> {
                        ctx.damage(3.0f);
                        ctx.sendToAll("§4[服务器] 派对已开始。请尽情享受剩下的时间。");
                        ctx.obliterate();
                    });
                    ctx.addRage(20);
                })
                .build());
    }
}
