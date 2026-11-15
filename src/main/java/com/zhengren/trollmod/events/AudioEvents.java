package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;

/**
 * 【声音与假消息】分类事件。
 * 负责整活：贴脸恐怖音效、苦力怕引信、凋灵生成、末影龙咆哮、监守者怒吼、远古守卫者诅咒、
 * 假玩家上下线、假聊天、假公告、假管理员、假成就、假物品损坏、突然巨响、聊天刷屏、假封号、假人数已满……
 *
 * <p>本文件刻意只使用 {@code ctx.*} 与 {@code TrollPools.*}；
 * 少数点名要用的常量只从契约白名单里取（{@code SoundEvents} / {@code StatusEffects} / {@code GlitchIds}）。</p>
 */
public final class AudioEvents {

    private AudioEvents() {
    }

    // ==================================================================
    // 常量池下标：只用来「指名道姓」地取某一个音效，不新引入任何 Minecraft API。
    // 下标对应 core/TrollPools.java 里的数组顺序（那个文件是只读契约，顺序稳定）。
    // ==================================================================

    /** TrollPools.HORROR[3] = 末影龙咆哮 */
    private static final int H_DRAGON_GROWL = 3;
    /** TrollPools.HORROR[4] = 监守者怒吼 */
    private static final int H_WARDEN_ROAR = 4;
    /** TrollPools.HORROR[5] = 远古守卫者诅咒 */
    private static final int H_GUARDIAN_CURSE = 5;
    /** TrollPools.HORROR[6] = 幻翼俯冲 */
    private static final int H_PHANTOM_SWOOP = 6;
    /** TrollPools.HORROR[9] = 洞穴环境音 */
    private static final int H_CAVE = 9;
    /** TrollPools.HORROR[10] = 钟声回荡 */
    private static final int H_BELL = 10;
    /** TrollPools.HORROR[11] = 凋灵低语 */
    private static final int H_WITHER_AMBIENT = 11;
    /** TrollPools.HORROR[12] = 监守者心跳 */
    private static final int H_HEARTBEAT = 12;

    /** TrollPools.CREEPY[1] = 末影人凝视 */
    private static final int C_ENDERMAN_STARE = 1;
    /** TrollPools.CREEPY[2] = 末影人传送 */
    private static final int C_ENDERMAN_TELEPORT = 2;
    /** TrollPools.CREEPY[3] = 打开箱子 */
    private static final int C_CHEST_OPEN = 3;
    /** TrollPools.CREEPY[4] = 关闭箱子 */
    private static final int C_CHEST_CLOSE = 4;
    /** TrollPools.CREEPY[5] = 物品损坏 */
    private static final int C_ITEM_BREAK = 5;

    /** TrollPools.FUNNY[1] = 村民「不行」 */
    private static final int F_VILLAGER_NO = 1;

    /** TrollPools.IMPACT[5] = TNT 引信 */
    private static final int I_TNT_PRIMED = 5;
    /** TrollPools.IMPACT[8] = 末影龙死亡 */
    private static final int I_DRAGON_DEATH = 8;

    // ==================================================================
    // 纯文本素材（自己写的字符串数组，不涉及任何 Minecraft API）
    // ==================================================================

    private static final String[] FAKE_ACHIEVEMENTS = {
            "「第一次被吓到跳起来」",
            "「对着空气挥剑 30 秒」",
            "「把钻石亲手扔进岩浆」",
            "「连续 7 次死在同一个坑里」",
            "「相信了聊天框里的每一个字」",
            "「把家安在苦力怕刷新点上」",
            "「认真研读了一条假成就」",
            "「被模组作者悄悄盯上」",
            "「在虚假的服务器公告前慌了 3 秒」",
            "「把背包塞满腐肉还嫌不够」"
    };

    private static final String[] ACHIEVEMENT_QUIPS = {
            "§7奖励：一块腐肉。（其实并没有发给你。）",
            "§7这个成就没有奖励，就像你的努力一样。",
            "§7全服解锁人数：1。而且那个人也不是你。",
            "§7继续保持，反正也没人看。",
            "§7你确定这是成就，不是诊断报告？",
            "§7进度：0%。数据来源：我随口说的。"
    };

    private static final String[] FAKE_ADMINS = {
            "Admin", "OP", "Console", "Server_Guard", "服主", "夜班巡查员"
    };

    private static final String[] ADMIN_WARNINGS = {
            "§c检测到你在破坏服务器平衡，请立刻证明你不是菜鸟。",
            "§c你的操作已被记录。别问为什么，这就是规定。",
            "§c请停止当前行为，否则后果自负。（后果是什么？我也不清楚。）",
            "§c有人举报你。举报内容：菜得影响他人游戏体验。",
            "§c你已被加入观察名单。恭喜，你终于被看见了。",
            "§c请不要挖穿基岩。就算你挖不到，也请不要尝试。"
    };

    private static final String[] FAKE_PVP_LINES = {
            "§ePVP 模式已开启。祝你好运——你需要的不止是运气。",
            "§ePVP 已启用：现在连你自己都可能打到自己。",
            "§e自由对战开启。你的死亡不会得到任何同情。"
    };

    private static final String[] FAKE_OP_QUIPS = {
            "§7（这是假的。你依然什么权限都没有。）",
            "§7（除了被整的权限，你什么权限都没有。）",
            "§7（管理员名单里没有你，以后也不会有。）"
    };

    private static final String[] FULL_SERVER_LINES = {
            "§c服务器人数已满（20/20）：请稍后再试。",
            "§c连接被拒绝：服务器满载。你是被挤出去的那一个。",
            "§c登录失败：人满了，位置留给更会玩的人。"
    };

    // ==================================================================
    // 事件注册
    // ==================================================================

    public static void register() {
        TrollEvents.category("声音与假消息");

        // ---------------------------------------------------------------
        // PRANK（小整）
        // ---------------------------------------------------------------

        TrollEvents.reg(TrollEvent.of("audio_ghost_step", "身后的脚步", TrollTier.BRUTAL)
                .desc("在你身边的随机方位播放一串阴森音效，让你以为有人跟着你")
                .weight(160)
                .cooldownSeconds(40)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.soundAt(ctx.pick(TrollPools.CREEPY),
                            ctx.x() + ctx.ri(-5, 5), ctx.y(), ctx.z() + ctx.ri(-5, 5),
                            1.4f, ctx.rf(0.7f, 0.95f));
                    if (ctx.chance(0.6)) {
                        ctx.msg("§7……你刚才听到了吗？");
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_fake_chat", "假聊天", TrollTier.BRUTAL)
                .desc("伪造一条其他玩家的聊天信息，内容十分欠揍")
                .weight(170)
                .cooldownSeconds(45)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), ctx.pick(TrollPools.FAKE_LINES));
                    if (ctx.chance(0.35)) {
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), ctx.pick(TrollPools.FAKE_LINES));
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_fake_join", "有人来了", TrollTier.BRUTAL)
                .desc("伪造一条玩家加入游戏的提示，配上一声欢快的音效")
                .weight(140)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), true);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 1.25f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_fake_leave", "他走了，只剩你", TrollTier.PRANK)
                .desc("伪造一条玩家退出游戏的提示，并顺手补一句扎心的话")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), false);
                    ctx.msg("§7服务器里又只剩你一个了。连句再见都没有。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_fake_achievement", "假成就", TrollTier.ANNOYING)
                .desc("伪造一条「成就已达成」提示，配上真实的升级音效，奖励却是个笑话")
                .weight(130)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.msg("§6§l[成就已达成] §r§f" + ctx.pick(FAKE_ACHIEVEMENTS));
                    ctx.msg(ctx.pick(ACHIEVEMENT_QUIPS));
                    ctx.playLevelUp();
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_chest_creak", "有人翻你的箱子", TrollTier.BRUTAL)
                .desc("贴着耳朵播放开箱 + 关箱音效，让你立刻回头看")
                .weight(140)
                .cooldownSeconds(50)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.soundSelf(TrollPools.CREEPY[C_CHEST_OPEN], 1.1f, 1.0f);
                    ctx.soundSelf(TrollPools.CREEPY[C_CHEST_CLOSE], 0.9f, 0.85f);
                    ctx.msg("§7（你的箱子完好无损。这次。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_villager_mock", "村民的鄙视", TrollTier.BRUTAL)
                .desc("用村民的拒绝音效嘲笑你，并伪造一句村民发言")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.soundSelf(TrollPools.FUNNY[F_VILLAGER_NO], 1.0f, 1.0f);
                    ctx.fakeChat("村民", "§e哼。（他拒绝了你全部的 7 笔交易）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_bell_toll", "钟声回荡", TrollTier.ANNOYING)
                .desc("播放一声悠长钟声，同时弹出假公告通知你被观察了多久")
                .weight(125)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.sound(TrollPools.HORROR[H_BELL], 1.8f, 0.8f);
                    ctx.fakeBroadcast("§7整点报时：你已经被本模组观察 §e" + ctx.ri(30, 180) + " §7分钟。");
                })
                .build());

        // ---------------------------------------------------------------
        // ANNOYING（烦人）
        // ---------------------------------------------------------------

        TrollEvents.reg(TrollEvent.of("audio_cave_echo", "洞穴回声", TrollTier.BRUTAL)
                .desc("从三个不同方位依次播放洞穴环境音，营造「你被包围了」的错觉")
                .weight(135)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    for (int i = 0; i < 3; i++) {
                        ctx.soundAt(TrollPools.HORROR[H_CAVE],
                                ctx.x() + ctx.ri(-9, 9), ctx.y() + ctx.ri(0, 3), ctx.z() + ctx.ri(-9, 9),
                                1.3f, ctx.rf(0.7f, 1.0f));
                    }
                    ctx.actionBar("§8（回声……从三个方向同时传来）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_creeper_primed", "嘶——", TrollTier.BRUTAL)
                .desc("贴脸播放苦力怕引信声，有三成概率再补一声爆炸，然后告诉你什么都没发生")
                .weight(135)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.soundSelf(SoundEvents.ENTITY_CREEPER_PRIMED, 1.7f, 1.0f);
                    if (ctx.chance(0.35)) {
                        ctx.soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.4f, 0.7f);
                        ctx.smokeBurst();
                    }
                    ctx.msg("§2嘶……§7然后什么也没发生。你的心跳倒是挺响的。");
                    ctx.addRage(1.5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_fake_item_break", "假的耐久告急", TrollTier.BRUTAL)
                .desc("播放物品损坏音效并谎报耐久为 0（物品其实一点事都没有）")
                .when(ctx -> !ctx.invEmpty())
                .weight(125)
                .cooldownSeconds(75)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.soundSelf(TrollPools.CREEPY[C_ITEM_BREAK], 1.5f, 1.0f);
                    ctx.warn("你的装备好像坏了。");
                    ctx.actionBar("§c耐久度：0 / 1561");
                    ctx.msg("§7（骗你的。它好得很，就是你的手在抖。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_fake_broadcast", "假服务器公告", TrollTier.BRUTAL)
                .desc("弹出一条服务器公告样式的假消息，配上一声阴森音效")
                .weight(150)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.fakeBroadcast(ctx.pick(TrollPools.FAKE_BROADCASTS));
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.9f, 0.75f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_fake_admin", "管理员正在看着你", TrollTier.NIGHTMARE)
                .desc("伪造的管理员私聊只是掩护：在你低头读字的那几秒里，身上有用的东西被一件一件收走")
                .weight(120)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.fakeChat("§c" + ctx.pick(FAKE_ADMINS), "§c别动。你的操作已经进入记录。");
                    ctx.soundSelf(TrollPools.CREEPY[H_CAVE], 0.6f, 0.5f);
                    ctx.takeRandomItems(2);
                    ctx.effectRandom(400, 2);
                    ctx.later(70, () -> {
                        ctx.fakeChat("§c" + ctx.pick(FAKE_ADMINS),
                                "§c我知道你现在站在哪。别回头，也别停下手里的动作。");
                        ctx.glitch(GlitchIds.SHAKY, 60, 1);
                        ctx.soundSelf(TrollPools.CREEPY[C_ENDERMAN_STARE], 1.3f, 0.7f);
                        ctx.msg("§8（记录还在继续。记录的是你，不是你的操作。）");
                    });
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_chat_flood", "聊天栏刷屏", TrollTier.BRUTAL)
                .desc("让一群假玩家在聊天栏里连续刷 6~12 条消息，把你的聊天记录彻底淹没")
                .weight(115)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    int n = ctx.ri(6, 12);
                    for (int i = 0; i < n; i++) {
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), ctx.pick(TrollPools.FAKE_LINES));
                        if (ctx.chance(0.3)) {
                            ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, ctx.rf(0.9f, 1.4f));
                        }
                    }
                    ctx.sys("（以上消息全部由本模组代发，与本服务器无关。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_wither_whisper", "墙后有什么在呼吸", TrollTier.NIGHTMARE)
                .desc("腐败的呼吸声从墙的另一侧传来。你转身时那边什么都没有，但你的血条少了一截")
                .weight(120)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.soundAt(TrollPools.HORROR[H_WITHER_AMBIENT],
                            ctx.x() + ctx.ri(-16, -7), ctx.y() + 3, ctx.z() + ctx.ri(-16, -7),
                            1.2f, 0.7f);
                    ctx.damage("wither", 3.0f);
                    ctx.effectRandom(320, 1);
                    ctx.later(55, () -> {
                        ctx.soundSelf(TrollPools.HORROR[H_WITHER_AMBIENT], 1.7f, 0.6f);
                        ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                        ctx.msg("§8这次它在你身后。而且它不打算再走了。");
                        ctx.addRage(4);
                    });
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_heartbeat_close", "心跳越来越近", TrollTier.ANNOYING)
                .desc("连续播放三次监守者心跳音效，一次比一次响，然后戛然而止")
                .weight(115)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.soundSelf(TrollPools.HORROR[H_HEARTBEAT], 0.5f, 0.9f);
                    ctx.soundSelf(TrollPools.HORROR[H_HEARTBEAT], 0.9f, 0.95f);
                    ctx.soundSelf(TrollPools.HORROR[H_HEARTBEAT], 1.5f, 1.0f);
                    ctx.actionBar("§4它停下来了。");
                    ctx.msg("§7心跳声停了。这通常不是什么好消息。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_fake_tnt_fuse", "引信在你脚底", TrollTier.NIGHTMARE)
                .desc("引信声贴着脚底响起。它数完了，你才想起该跑——而这一次它连着的东西是真的")
                .weight(120)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.soundSelf(TrollPools.IMPACT[I_TNT_PRIMED], 1.7f, 1.0f);
                    ctx.breakRandomTool();
                    ctx.effect(StatusEffects.SLOWNESS, 60, 1);
                    ctx.actionBar("§6嘶……§7（你有三秒时间做一个决定）");
                    ctx.later(45, () -> {
                        ctx.spawnTnt(ctx.x(), ctx.y(), ctx.z(), 8);
                        ctx.boomAtPlayer(2.0f);
                        ctx.smokeBurst();
                        ctx.damage("magic", 1.5f);
                        ctx.msg("§c是真的。§8你犹豫的那半秒，就是它留给你的全部仁慈。");
                        ctx.addRage(5);
                    });
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_portal_whisper", "有人从你背后穿过来", TrollTier.NIGHTMARE)
                .desc("末影人的传送声在你背后响起。你回头——那里确实站着东西，只是它没有脸")
                .weight(125)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.soundAt(TrollPools.CREEPY[C_ENDERMAN_TELEPORT],
                            ctx.x() + ctx.ri(-3, 3), ctx.y() + 1, ctx.z() + ctx.ri(-3, 3),
                            1.6f, ctx.rf(0.8f, 1.1f));
                    ctx.takeRandomItems(2);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.later(35, () -> {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                        ctx.randomRotation();
                        ctx.glitch(GlitchIds.INVERT_MOUSE, 50, 1);
                        ctx.soundSelf(SoundEvents.ENTITY_ENDERMAN_SCREAM, 1.6f, 0.9f);
                        ctx.msg("§5它没有走。它只是站到了你看不见的那一侧，然后一直站着。");
                        ctx.addRage(5);
                    });
                    ctx.addRage(6);
                })
                .build());

        // ---------------------------------------------------------------
        // BRUTAL（抓狂）
        // ---------------------------------------------------------------

        TrollEvents.reg(TrollEvent.of("audio_ghast_scream", "地狱在你耳边尖叫", TrollTier.NIGHTMARE)
                .desc("音量拉满的恶魂尖叫贴着耳膜炸开，求生本能比理智先一步做出反应——而这次它不追你，它只是复制自己")
                .weight(90)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§c跑。别问往哪跑。");
                    ctx.soundSelf(SoundEvents.ENTITY_GHAST_SCREAM, 2.0f, 0.75f);
                    ctx.damage("wither", 3.0f);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "别回头。它靠声音找你。");
                    ctx.later(40, () -> {
                        ctx.soundSelf(SoundEvents.ENTITY_GHAST_SCREAM, 2.0f, 0.55f);
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                        ctx.effectRandom(300, 1);
                        ctx.msg("§7你回头看的那一瞬间，它记住了你的脸。");
                        ctx.addRage(5);
                    });
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_wither_spawn", "凋灵生成·责任玩家：你", TrollTier.APOCALYPSE)
                .desc("公告把凋灵生成的责任推给了你。在你读完这行字之前，脚下的坑已经挖好了")
                .weight(80)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.voidPit(2, 14);
                    ctx.soundSelf(SoundEvents.ENTITY_WITHER_SPAWN, 1.9f, 0.85f);
                    ctx.fakeBroadcast(ctx.format("§c检测到凋灵生成，已将玩家 §b%p §c标记为责任玩家。"));
                    ctx.warn("你要为此负责。现在、以后、以及你重生的那一刻。");
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 80);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                    ctx.effect(StatusEffects.WITHER, 200, 1);
                    ctx.addRage(13);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_warden_roar", "它闻到你的时候，你已经看不见了", TrollTier.NIGHTMARE)
                .desc("监守者的怒吼先夺走你的视野，再夺走你手里的工具，最后才慢慢靠近")
                .weight(75)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.scatterFar(120);
                    ctx.soundSelf(SoundEvents.ENTITY_WARDEN_ROAR, 2.0f, 0.8f);
                    ctx.effect(StatusEffects.DARKNESS, ctx.ri(160, 260), 0);
                    ctx.breakRandomTool();
                    ctx.title("§8§l它闻到你了", "§7而你什么也看不见，什么也握不住");
                    ctx.later(65, () -> {
                        ctx.soundSelf(TrollPools.HORROR[H_HEARTBEAT], 2.0f, 0.8f);
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                        ctx.msg("§8黑暗里有什么在数你的脚步。数到三的时候，它就不数了。");
                        ctx.addRage(6);
                    });
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_fake_op", "你被提升为管理员（然后被注销）", TrollTier.NIGHTMARE)
                .desc("升级音效是真的，公告是真的，只有权限是假的——而代价是真的")
                .weight(70)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.playLevelUp();
                    ctx.fakeBroadcast(ctx.format("§e已将 §b%p §e提升为管理员（OP）。"));
                    ctx.ok("你现在是管理员了！试试 /gamemode creative 吧。");
                    ctx.later(75, () -> {
                        ctx.takeRandomItems(3);
                        ctx.clearEffects();
                        ctx.fakeChat("§c控制台", "§c管理员权限已回收。理由：你不适合被看见。");
                        ctx.soundSelf(TrollPools.FUNNY[F_VILLAGER_NO], 1.2f, 0.7f);
                        ctx.effect(StatusEffects.WEAKNESS, 220, 1);
                        ctx.msg("§7从 OP 到什么都没有，用了 3.5 秒。这 3.5 秒是你今天唯一的高光。");
                        ctx.addRage(7);
                    });
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_enderman_stare", "别眨眼，它在数你的心跳", TrollTier.NIGHTMARE)
                .desc("凝视声与尖叫同时贴脸，你的视角被强行扭向背后——那里有东西正在等你看过去")
                .weight(85)
                .cooldownSeconds(240)
                .when(ctx -> !ctx.isInWater())
                .run(ctx -> {
                    ctx.soundSelf(TrollPools.CREEPY[C_ENDERMAN_STARE], 1.9f, 0.85f);
                    ctx.soundSelf(SoundEvents.ENTITY_ENDERMAN_SCREAM, 1.6f, 0.95f);
                    ctx.randomRotation();
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "别眨眼。它只在你不看它的时候动。");
                    ctx.later(50, () -> {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                        ctx.effect(StatusEffects.NAUSEA, 220, 1);
                        ctx.glitch(GlitchIds.INVERT_PITCH, 60, 1);
                        ctx.msg("§5你眨了。它也动了。");
                        ctx.addRage(6);
                    });
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_fake_pvp_mode", "假 PVP 开启", TrollTier.BRUTAL)
                .desc("假公告宣布 PVP 模式开启，配上受击音效，让你怀疑身边每个人")
                .weight(70)
                .cooldownSeconds(480)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.fakeBroadcast(ctx.pick(FAKE_PVP_LINES));
                    ctx.soundSelf(SoundEvents.BLOCK_ANVIL_LAND, 1.4f, 0.8f);
                    ctx.playHurtSound();
                    if (ctx.randomOtherPlayer() != null) {
                        ctx.msg("§c（最近的那名玩家刚刚看了你一眼。就一眼。）");
                    }
                    ctx.addRage(5);
                })
                .build());

        // ---------------------------------------------------------------
        // NIGHTMARE（崩溃）
        // ---------------------------------------------------------------

        TrollEvents.reg(TrollEvent.of("audio_fake_death_broadcast", "全服都以为你死了", TrollTier.OBLIVION)
                .desc("先向全服播报你的死讯，再假装把你从存档里捞回来——回滚完成的那一刻你才发现，回来的东西不是完整的你")
                .weight(45)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.broadcast("§7☠ §c" + ctx.playerName() + " §7" + ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.soundSelf(TrollPools.IMPACT[I_DRAGON_DEATH], 1.7f, 0.85f);
                    ctx.playHurtSound();
                    ctx.glitch(GlitchIds.BLACKOUT, 70);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "刚看到播报了……节哀。");
                    ctx.later(85, () -> {
                        ctx.sys("§a[恢复服务] 正在从最近的存档回滚玩家数据……");
                        ctx.playLevelUp();
                        ctx.fakeJoinLeave(ctx.playerName(), true);
                        ctx.msg("§7你回来了。大概。");
                        ctx.addRage(6);
                    });
                    ctx.later(175, () -> {
                        ctx.glitch(GlitchIds.FAKE_DEATH, 90, 2);
                        ctx.fakeChat("§8恢复服务", "回滚完成。丢失的数据：你的全部。");
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.msg("§8存档把你的身体还回来了。别的，一样都没还。");
                        ctx.obliterate();
                        ctx.addRage(18);
                    });
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_dragon_growl", "末影龙醒了，只为你一个人", TrollTier.NIGHTMARE)
                .desc("龙的咆哮在耳边炸开，视野随之被黑暗吞掉——它不需要找到你，它只需要你找不到它")
                .weight(45)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.soundSelf(TrollPools.HORROR[H_DRAGON_GROWL], 2.0f, 0.85f);
                    ctx.title("§4§l末影龙醒了", "§7而它只盯着你一个人");
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 100);
                    ctx.effect(StatusEffects.DARKNESS, 200, 0);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "它说它认识你。它说它一直认识你。");
                    ctx.later(60, () -> {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                        ctx.soundSelf(TrollPools.HORROR[H_DRAGON_GROWL], 2.0f, 0.55f);
                        ctx.msg("§8第二声比第一声近。第三声之后就没有然后了。");
                        ctx.addRage(6);
                    });
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_silence_then_boom", "静音之后", TrollTier.OBLIVION)
                .desc("先宣布音频已静音，让你在死寂里等；再告诉你恢复了，让你放松——真正的巨响是在你放松的那一刻来的")
                .weight(40)
                .cooldownSeconds(900)
                .when(ctx -> ctx.isOnGround() && ctx.health() > 6.0f)
                .run(ctx -> {
                    ctx.sys("§7音频设备已静音。");
                    ctx.actionBar("§8（静音中……世界只剩下你自己的呼吸）");
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 55);
                    ctx.later(75, () -> {
                        ctx.sys("§a音频已恢复。");
                        ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.35f, 1.9f);
                        ctx.msg("§8（你放松了。这就是它一直在等的东西。）");
                    });
                    ctx.later(110, () -> {
                        ctx.soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 2.0f, 0.5f);
                        ctx.soundSelf(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 2.0f, 0.55f);
                        ctx.soundSelf(SoundEvents.ENTITY_WITHER_SPAWN, 2.0f, 0.6f);
                        ctx.glitch(GlitchIds.FLASHBANG, 30, 3);
                        ctx.meteorShower(5, 16.0);
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.warn("我说静音，又没说真的静音。");
                        ctx.addRage(20);
                    });
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_guardian_curse", "远古的诅咒落下来了", TrollTier.APOCALYPSE)
                .desc("诅咒音效响起的一瞬间，视野、方向感和背包里的东西被同时拿走，只留下呼吸声和一堵墙")
                .weight(40)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.soundSelf(TrollPools.HORROR[H_GUARDIAN_CURSE], 2.0f, 0.65f);
                    ctx.takeRandomItems(2);
                    ctx.buryAlive();
                    ctx.effect(StatusEffects.BLINDNESS, ctx.ri(180, 280), 1);
                    ctx.effect(StatusEffects.NAUSEA, ctx.ri(220, 320), 1);
                    ctx.title("§3§l你被诅咒了", "§7挖掘疲劳 III（假的。但你确实什么都做不了）");
                    ctx.msg("§3诅咒的期限是「直到你习惯为止」。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_phantom_swarm", "幻翼在天花板上等你", TrollTier.APOCALYPSE)
                .desc("四个方向同时响起俯冲声，你抬头寻找幻翼的那一刻，有什么从更高的地方落了下来")
                .weight(45)
                .cooldownSeconds(600)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.soundAt(TrollPools.HORROR[H_PHANTOM_SWOOP],
                                ctx.x() + ctx.ri(-8, 8), ctx.y() + ctx.ri(1, 5), ctx.z() + ctx.ri(-8, 8),
                                1.5f, ctx.rf(0.8f, 1.25f));
                    }
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "抬头");
                    ctx.actionBar("§c3 只幻翼正在接近……（并没有）");
                    ctx.later(50, () -> {
                        ctx.lift(14);
                        ctx.damage("fall", 3.0f);
                        ctx.spawnHostileNear(ctx.pick(TrollPools.ANNOYING_MOBS), 5);
                        ctx.msg("§8没有幻翼。只有下坠，和下坠结束之后等着你的东西。");
                        ctx.addRage(9);
                    });
                    ctx.addRage(9);
                })
                .build());

        // ---------------------------------------------------------------
        // APOCALYPSE（地狱）
        // ---------------------------------------------------------------

        TrollEvents.reg(TrollEvent.of("audio_server_full", "你的位置被别人占了", TrollTier.OBLIVION)
                .desc("伪造「人数已满」的踢出界面，再假装为你找回位置——位置确实找回来了，只是坐了别人")
                .weight(25)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.fakeBroadcast(ctx.pick(FULL_SERVER_LINES));
                    ctx.title("§c§l连接中断", "§7服务器人数已满（20/20）");
                    ctx.glitch(GlitchIds.FAKE_KICK, 80, 3);
                    ctx.soundSelf(SoundEvents.BLOCK_ANVIL_LAND, 1.5f, 0.6f);
                    ctx.fakeChat("§c" + ctx.pick(FAKE_ADMINS), "腾个位置出来，你被挤掉了。");
                    ctx.later(90, () -> {
                        ctx.sys("§a[队列] 检测到空位，正在为你保留……");
                        ctx.playLevelUp();
                        ctx.msg("§7（你松了一口气。这一步是设计好的。）");
                    });
                    ctx.later(185, () -> {
                        ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), true);
                        ctx.fakeChat("§7系统", "§7那个位置刚刚被 §f" + ctx.pick(TrollPools.FAKE_NAMES)
                                + " §7拿走了。它比你更适合站在你的位置上。");
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.voidPit(2, 14);
                        ctx.effectRandom(1200, 3);
                        ctx.obliterate();
                        ctx.addRage(20);
                    });
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("audio_wall_of_sound", "声音的地狱", TrollTier.APOCALYPSE)
                .desc("凋灵、监守者、末影龙、雷霆与爆炸在同一秒贴脸炸响；而在这一切开始之前，你的视野就已经被拿走了")
                .weight(20)
                .cooldownSeconds(1800)
                .when(ctx -> ctx.health() > 8.0f)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 40);
                    ctx.soundSelf(SoundEvents.ENTITY_WITHER_SPAWN, 2.0f, 0.7f);
                    ctx.soundSelf(SoundEvents.ENTITY_WARDEN_ROAR, 2.0f, 0.8f);
                    ctx.soundSelf(TrollPools.HORROR[H_DRAGON_GROWL], 2.0f, 0.95f);
                    ctx.soundSelf(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 2.0f, 0.65f);
                    ctx.soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 2.0f, 0.6f);
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7四面八方都在响，只有你是静的");
                    ctx.fakeBroadcast(ctx.pick(TrollPools.FAKE_BROADCASTS));
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), ctx.pick(TrollPools.FAKE_LINES));
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 80);
                    ctx.msg(ctx.pick(TrollPools.TAUNTS));
                    ctx.buryAlive();
                    ctx.addRage(16);
                })
                .build());
    }
}
