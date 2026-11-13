package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【结局级画面】分类事件（第二批客户端整活）。
 * 负责整活：假永久封禁、假更新卡 99%、假世界重载、假病毒警告、输入反转、
 * 输入冻结、画面撕裂、海量刷屏、硬黑屏+心跳、假成就、假保存，以及把上面这些叠起来的地狱组合。
 * 本文件只使用 ctx / TrollPools / GlitchIds，不直接触碰任何 Minecraft API。
 */
public final class NightmareEvents {

    private NightmareEvents() {
    }

    public static void register() {
        TrollEvents.category("结局级画面");

        // ==================================================================
        // PRANK —— 小整，笑一下就好
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("nightmare_save_notice", "正在保存……", TrollTier.PRANK)
                .desc("角上一个假保存提示，让你以为自己刚捡回一条命")
                .weight(160)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.glitch(GlitchIds.FAKE_SAVING, 20 * 4, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_fake_achievement", "成就达成！", TrollTier.PRANK)
                .desc("弹一个假成就，名字还挺羞辱人的")
                .weight(150)
                .cooldownSeconds(60)
                .run(ctx -> {
                    String name = ctx.pick(TrollPools.FAKE_ACHIEVEMENTS);
                    ctx.fakeAchievement(name, "§7在毫无察觉的情况下被耍了");
                    ctx.glitch(GlitchIds.FAKE_ACHIEVEMENT, 20 * 5, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_mild_tear", "画面抽了一下", TrollTier.PRANK)
                .desc("轻微画面撕裂三秒，让你怀疑是显示器的问题")
                .weight(140)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.actionBar("§8显示器连接不太稳定……");
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.6f, 1.2f);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 3, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_controls_slip", "按键打了个滑", TrollTier.PRANK)
                .desc("前后左右短暂反转四秒，走两步就发现不对")
                .weight(140)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.msg("§7你的键盘刚才好像歪了一下。");
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 20 * 4, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_ambient_dread", "有点冷", TrollTier.PRANK)
                .desc("一声很轻的环境恐怖音加轻微抖动，纯气氛组")
                .weight(150)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 0.9f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 12);
                    ctx.glitch(GlitchIds.SHAKY, 20 * 3, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_loading_tips", "加载提示", TrollTier.PRANK)
                .desc("往聊天栏丢一条没用的假加载提示，顺便假装在保存")
                .weight(140)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.sys(ctx.pick(TrollPools.FAKE_TIPS));
                    ctx.glitch(GlitchIds.FAKE_SAVING, 20 * 2, 1);
                })
                .build());

        // ==================================================================
        // ANNOYING —— 烦人，会皱眉
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("nightmare_freeze_ui", "输入冻结", TrollTier.ANNOYING)
                .desc("灵敏度归零、HUD 消失、视距锁死，八秒的睁眼瞎")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.warn("检测到输入设备延迟，已临时冻结操作。");
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 8, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_controls_backwards", "前后左右反了", TrollTier.ANNOYING)
                .desc("按前进就是后退，十五秒足够你掉进坑里")
                .weight(115)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.msg("§e方向盘校准中，请勿乱动。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 1.3f);
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 20 * 15, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_tear_wave", "撕裂波动", TrollTier.ANNOYING)
                .desc("画面撕裂十二秒，看什么都像坏掉的电视机")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.actionBar("§8正在重新同步显示信号……");
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 12, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_mass_chat_lite", "聊天栏决堤", TrollTier.ANNOYING)
                .desc("海量假聊天灌进聊天栏，比普通刷屏猛得多")
                .weight(105)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e服务器正在广播全体玩家动态……");
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 12, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_save_loop", "保存了二十秒", TrollTier.ANNOYING)
                .desc("假保存提示反复出现，让你永远不敢关游戏")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 1.5f);
                    ctx.glitch(GlitchIds.FAKE_SAVING, 20 * 20, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_quest_popup", "新任务", TrollTier.ANNOYING)
                .desc("弹出一个根本不可能完成的任务，并配一个假成就音")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.sys("新任务：" + ctx.pick(TrollPools.FAKE_QUESTS));
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_ACHIEVEMENTS), "§7任务已自动接受");
                    ctx.glitch(GlitchIds.FAKE_ACHIEVEMENT, 20 * 15, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_achievement_spam", "成就刷屏", TrollTier.ANNOYING)
                .desc("假成就弹窗连着弹二十五秒，弹到你麻木")
                .weight(105)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.playLevelUp();
                    ctx.msg("§e你今天似乎特别有天赋。");
                    ctx.glitch(GlitchIds.FAKE_ACHIEVEMENT, 20 * 25, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_tear_and_spam", "撕裂 + 刷屏", TrollTier.ANNOYING)
                .desc("画面在裂，聊天在刷，两个都不让你好过")
                .weight(105)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.warn("显示驱动与聊天服务同时异常。");
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 12, 2);
                    ctx.glitch(GlitchIds.CHAT_SPAM, 20 * 12, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_black_flash", "黑一下再亮", TrollTier.ANNOYING)
                .desc("突然黑屏再闪白，纯纯的视网膜按摩")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 1.1f);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 4, 2);
                    ctx.glitch(GlitchIds.FLASHBANG, 20 * 3, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_hidden_frozen", "看不见也动不了", TrollTier.ANNOYING)
                .desc("HUD 隐藏叠输入冻结，标准的信息与操作双重剥夺")
                .weight(105)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.msg("§8正在为你优化界面与操作……");
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 15, 2);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 10, 2);
                })
                .build());

        // ==================================================================
        // BRUTAL —— 抓狂，会骂人
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("nightmare_tear_heavy", "撕成两半", TrollTier.BRUTAL)
                .desc("高强度画面撕裂二十秒，眼睛和耐心一起碎")
                .weight(60)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.title("§4§l信号丢失", "§7你的显示器正在自我了断");
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.8f);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 20, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_controls_hell", "操作地狱", TrollTier.BRUTAL)
                .desc("输入反转再叠输入冻结，想走走不动，想停停不下")
                .weight(55)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.warn("§c输入映射与灵敏度同时损坏。");
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 20 * 18, 3);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 12, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_mass_chat_storm", "聊天栏海啸", TrollTier.BRUTAL)
                .desc("海量刷屏二十五秒，重要信息全部淹没")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.broadcast("§c[广播] 全体玩家请注意，频道即将失控。");
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 25, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_hard_blackout", "硬黑屏", TrollTier.BRUTAL)
                .desc("长时间硬黑屏配心跳音，安静得让人发毛")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.msg("§8正在进入深度渲染模式……");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 18, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_virus_alert", "病毒警告", TrollTier.BRUTAL)
                .desc("假病毒/木马警告弹窗，看一眼血压就上来了")
                .weight(55)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.warn("§c检测到可疑程序正在修改游戏内存。");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.9f);
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 20 * 25, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_death_notice", "你已阵亡", TrollTier.BRUTAL)
                .desc("假死亡界面加假死因，死了但没完全死")
                .weight(60)
                .cooldownSeconds(300)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.fakeDeathScreen(ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.glitch(GlitchIds.FAKE_DEATH, 20 * 18, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_driver_lost", "显卡驱动没响应", TrollTier.BRUTAL)
                .desc("撕裂 + 黑屏 + 巨响，演一出显卡当场去世")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.title("§c§l显示驱动已停止响应", "§7并且拒绝重启");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 15, 3);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 8, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_save_write_failed", "存档写入失败", TrollTier.BRUTAL)
                .desc("假存档损坏配假保存提示，让你分不清哪个是真的")
                .weight(50)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.warn("§c存档写入失败：磁盘返回 I/O 错误。");
                    ctx.glitch(GlitchIds.FAKE_SAVING, 20 * 10, 2);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 22, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_connection_timeout", "连接超时", TrollTier.BRUTAL)
                .desc("海量刷屏叠硬黑屏，假装网络断了世界还在转")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.fakeBroadcast("§c连接超时，正在尝试重连（1/99）……");
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 15, 2);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 10, 2);
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 20 * 15, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_view_meltdown", "视角融化", TrollTier.BRUTAL)
                .desc("视角乱转 + 视野乱变 + 黑屏，方向感彻底报废")
                .weight(55)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.actionBar("§c镜头校准失败，正在瞎转。");
                    ctx.glitch(GlitchIds.SPIN, 20 * 18, 3);
                    ctx.glitch(GlitchIds.FOV_WILD, 20 * 18, 3);
                    ctx.glitch(GlitchIds.BLACKOUT, 20 * 6, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_oom_hidden", "内存不足 + 无界面", TrollTier.BRUTAL)
                .desc("假内存溢出叠隐藏 HUD，让你在裸奔状态下恐慌")
                .weight(50)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.sys("Java 堆空间不足，界面已被回收。");
                    ctx.glitch(GlitchIds.FAKE_OOM, 20 * 22, 2);
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 20, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_menu_ghost", "悄悄退回标题", TrollTier.BRUTAL)
                .desc("画面跳回标题菜单，其实你人还站在原地")
                .weight(30)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.msg("§7正在保存并退出世界……");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.glitch(GlitchIds.MENU_TEASE, 20 * 25, 3);
                })
                .build());

        // ==================================================================
        // NIGHTMARE —— 崩溃，会砸键盘
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("nightmare_fake_ban", "你已被永久封禁", TrollTier.NIGHTMARE)
                .desc("弹出假永久封禁界面并附一条荒谬的封禁理由，退出按钮是真的")
                .weight(30)
                .cooldownSeconds(600)
                .run(ctx -> {
                    String excuse = ctx.pick(TrollPools.FAKE_EXCUSES);
                    ctx.warn("§4你的账号已被永久封禁。");
                    ctx.broadcast("§c[封禁] 玩家 " + ctx.playerName() + " 被封禁：§7" + excuse);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_BAN, 20 * 30, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_update_99", "更新卡在 99%", TrollTier.NIGHTMARE)
                .desc("假更新界面进度永远停在 99%，并让游戏失去响应")
                .weight(35)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.title("§e正在更新……", "§7请勿关闭游戏（进度 99%）");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 20 * 28, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_world_reload_stuck", "重新加载世界", TrollTier.NIGHTMARE)
                .desc("假世界重载进度条卡死，你的世界看起来回不来了")
                .weight(35)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.msg("§e正在重新加载世界数据，请稍候……");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.7f);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 20 * 28, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_hidden_update", "看不见的更新", TrollTier.NIGHTMARE)
                .desc("HUD 隐藏叠假更新界面，你在更新中盲打")
                .weight(30)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.actionBar("§8更新安装中：0% ……");
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 25, 3);
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 20 * 25, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_reload_crash", "重载到一半崩了", TrollTier.NIGHTMARE)
                .desc("假世界重载叠假崩溃报告，礼貌地告诉你全没了")
                .weight(25)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§4§l重载失败", "§c世界数据未能恢复");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 20 * 22, 3);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 20 * 28, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_ban_and_kick", "踢了，然后封了", TrollTier.NIGHTMARE)
                .desc("假掉线紧接假永久封禁，一套流程把你送走")
                .weight(22)
                .cooldownSeconds(600)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    var o = ctx.other();
                    if (o != null) {
                        ctx.msgToOther(o, "§c你也跑不掉。");
                    }
                    ctx.broadcast("§c[管理员] " + ctx.pick(TrollPools.FAKE_OPERATOR_LINES));
                    ctx.glitch(GlitchIds.FAKE_KICK, 20 * 20, 3);
                    ctx.glitch(GlitchIds.FAKE_BAN, 20 * 30, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_triple_meltdown", "崩溃三连", TrollTier.NIGHTMARE)
                .desc("假崩溃 + 硬黑屏 + 海量刷屏，三件套一次上齐")
                .weight(28)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.title("§4§l致命错误", "§8正在收集崩溃信息……");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 20 * 30, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 15, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 20, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_virus_lockdown", "病毒隔离模式", TrollTier.NIGHTMARE)
                .desc("假病毒警告叠输入冻结，说你被隔离了你还真动不了")
                .weight(35)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.warn("§c已进入安全隔离模式，请勿操作电脑。");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 20 * 25, 3);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 20, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_corrupt_reload", "损坏 + 重载", TrollTier.NIGHTMARE)
                .desc("假存档损坏紧接假世界重载，让你以为世界真的在救")
                .weight(30)
                .cooldownSeconds(540)
                .run(ctx -> {
                    ctx.warn("§c区块校验失败，正在尝试重新加载世界……");
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 25, 3);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 20 * 25, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_menu_and_ban", "先回标题再封号", TrollTier.NIGHTMARE)
                .desc("假标题界面接假封禁，你以为退出了其实被判刑了")
                .weight(25)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.msg("§7正在退出世界……");
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.MENU_TEASE, 20 * 25, 3);
                    ctx.glitch(GlitchIds.FAKE_BAN, 20 * 30, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_update_forever", "更新永无止境", TrollTier.NIGHTMARE)
                .desc("假更新界面长时间卡死，再配海量刷屏折磨你的眼睛")
                .weight(30)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e更新包下载速度：0.1 KB/s");
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 20 * 30, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 18, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_input_dead", "输入彻底死掉", TrollTier.NIGHTMARE)
                .desc("输入冻结 + 画面撕裂 + 硬黑屏，键盘鼠标一起失去意义")
                .weight(30)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.title("§8§l无响应", "§7请等待程序响应……它不会");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 22, 3);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 22, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 12, 2);
                })
                .build());

        // ==================================================================
        // APOCALYPSE —— 地狱，会退游戏
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("nightmare_end_of_world", "世界终结", TrollTier.APOCALYPSE)
                .desc("假封禁 + 假世界重载 + 硬黑屏 + 结局级巨响，整局只来一次")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.title("§4§l世界已终止", "§c感谢你的一路陪葬");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 20 * 30, 3);
                    ctx.glitch(GlitchIds.FAKE_BAN, 20 * 30, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 15, 3);
                    ctx.randomHorrorSound();
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_game_shutdown", "游戏即将关闭", TrollTier.APOCALYPSE)
                .desc("假更新叠假封禁再刷屏撕裂，演一出你被请出游戏的戏")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.broadcast("§c服务器将在 10 秒后关闭，原因：你。");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 20 * 28, 3);
                    ctx.glitch(GlitchIds.FAKE_BAN, 20 * 30, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 20, 3);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 18, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_all_screens_once", "所有界面一起上", TrollTier.APOCALYPSE)
                .desc("把第二批所有结局级画面一次性砸下去，整局只来一次")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.title("§4§l§k全部失效§r", "§8你只剩看着的份");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.glitch(GlitchIds.FAKE_BAN, 20 * 30, 3);
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 20 * 30, 3);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 20 * 30, 3);
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 20 * 25, 3);
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 20 * 30, 3);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 20 * 20, 3);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 20 * 25, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 25, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 12, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_mod_detected", "检测到修改游戏文件", TrollTier.APOCALYPSE)
                .desc("假病毒 + 假封禁 + 假世界重载，罪名安排得明明白白")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.warn("§4检测到游戏文件被修改，正在隔离你的世界。");
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 20 * 25, 3);
                    ctx.glitch(GlitchIds.FAKE_BAN, 20 * 30, 3);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 20 * 25, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_world_recovery", "正在恢复世界", TrollTier.APOCALYPSE)
                .desc("假世界重载叠硬黑屏和假存档损坏，恢复得让人绝望")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.title("§e正在恢复世界", "§8预计剩余时间：∞");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 20 * 30, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 20 * 15, 3);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 20 * 25, 3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("nightmare_replay_recording", "回放录制中", TrollTier.APOCALYPSE)
                .desc("假成就说你在被录制，同时隐藏 HUD 狂刷屏假装这里有观众")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.title("§c§l回放录制中", "§7你的表现将会被永久保存");
                    ctx.playLevelUp();
                    ctx.sendToAll("§8[回放] 正在录制 " + ctx.playerName() + " 的受难过程……");
                    ctx.glitch(GlitchIds.FAKE_ACHIEVEMENT, 20 * 25, 3);
                    ctx.glitch(GlitchIds.HIDE_HUD, 20 * 25, 3);
                    ctx.glitch(GlitchIds.MASS_CHAT, 20 * 20, 3);
                    ctx.glitch(GlitchIds.FAKE_SAVING, 20 * 25, 3);
                })
                .build());
    }
}
