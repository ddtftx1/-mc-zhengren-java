package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【节日与彩蛋】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。</p>
 *
 * <p>春节的鞭炮、中秋的月亮、圣诞的袜子、万圣的南瓜、愚人节的假公告、跨年的倒计时——
 * 节日从来是最适合整人的场合，因为受害者会以为你在开玩笑。
 * 本文件刻意不引用任何 Minecraft 类型，全部通过 {@code ctx.*} 与 {@code TrollPools.*} 组合实现。</p>
 */
public final class FestivalEvents {

    private FestivalEvents() {
    }

    public static void register() {
        TrollEvents.category("节日与彩蛋");

        // ================================================================
        // PRANK —— 小整，节日气氛组（18 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("fest_spring_couplet", "春联自己改了词", TrollTier.ANNOYING)
                .desc("门口的春联内容被悄悄换成了对你的评价")
                .weight(140)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.msg("§c上联：§f今年运势一般§c　下联：§f主要看你自己");
                    ctx.msg("§c横批：§e§l别抱太大希望");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.5f);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_spring_red_packet", "红包是空的", TrollTier.BRUTAL)
                .desc("你抢到了一个红包，里面只有一张纸条")
                .weight(135)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.playLevelUp();
                    ctx.msg("§c你抢到了一个红包！§7打开后里面写着：§8「新年快乐，钱没有。」");
                    ctx.giveRandomJunk();
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_spring_dumpling", "饺子里有惊喜", TrollTier.ANNOYING)
                .desc("你咬到了那枚代表好运的硬币，牙有点疼")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.8f, 1.6f);
                    ctx.msg("§7你咬到了硬币。§8传统上这意味着好运，但你的牙不这么认为。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_midautumn_mooncake", "月饼馅料离谱", TrollTier.BRUTAL)
                .desc("你切开月饼，里面的馅料拒绝被归类")
                .weight(135)
                .cooldownSeconds(55)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.msg("§6你切开了月饼。§7里面的馅是§e韭菜鸡蛋§7味的。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_midautumn_moon_greet", "月亮祝你中秋快乐", TrollTier.BRUTAL)
                .desc("月亮用它自己的方式送上了节日祝福")
                .weight(130)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 18);
                    ctx.actionBar("§e月亮：§7中秋快乐。§8你一个人过节吧？");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_christmas_sock", "袜子里只有煤", TrollTier.BRUTAL)
                .desc("你挂了一晚上的袜子，里面确实有东西")
                .weight(135)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.give(ctx.pick(TrollPools.JUNK), 3);
                    ctx.msg("§8袜子里有东西。§7是煤。§8而且是用过的。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.3f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_christmas_bell", "铃儿响叮当（跑调）", TrollTier.ANNOYING)
                .desc("圣诞铃铛响了起来，音准明显有问题")
                .weight(125)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.7f);
                    ctx.actionBar("§7叮叮当，叮叮当，§8叮——当——（走音）");
                    ctx.msg("§8铃铛说它今年没调音。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_halloween_pumpkin", "南瓜灯对你笑", TrollTier.ANNOYING)
                .desc("南瓜灯里的火苗歪了一下，像在笑")
                .weight(130)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 14);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.4f);
                    ctx.msg("§6南瓜灯笑了一下。§7或者只是火苗在抖。你选一个信。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_april_fool_joke", "假更新公告", TrollTier.ANNOYING)
                .desc("一条煞有介事的版本更新公告，内容是假的")
                .weight(130)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.fakeBroadcast("§b【更新】v2.0.0 已发布：修复了玩家过于自信的问题。");
                    ctx.fakeSaveNotice();
                    ctx.msg("§8……今天是四月一日。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_birthday_song", "跑调生日歌", TrollTier.ANNOYING)
                .desc("有人在为你唱生日歌，但调子已经离家出走了")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeChat("全体村民", "§d祝你生日快乐~ 祝你生日快——§c（破音）");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.5f);
                    ctx.msg("§7有人记得你的生日。§8可惜没人会唱歌。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_birthday_candle", "蜡烛吹不灭", TrollTier.ANNOYING)
                .desc("你许完愿吹蜡烛，它又自己亮了")
                .weight(125)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 12);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.2f);
                    ctx.msg("§7你吹灭了蜡烛。§8它自己亮了。§7你又吹。§8它又亮了。");
                    ctx.msg("§8它好像不太想让你许愿。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_valentine_chocolate", "苦巧克力", TrollTier.BRUTAL)
                .desc("你收到的巧克力是 99% 纯度的，苦得很有教育意义")
                .weight(125)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.msg("§d你收到了一盒巧克力。§7包装很甜，味道很苦。");
                    ctx.msg("§8附言：§7「希望你习惯苦的。」");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.1f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_newyear_countdown", "倒计时数错了", TrollTier.BRUTAL)
                .desc("跨年倒计时在 3 和 2 之间卡了很久")
                .weight(130)
                .cooldownSeconds(80)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.title("§e§l3", "§7新年快乐（还没到）");
                    ctx.later(30, () -> ctx.title("§e§l2", "§7快了快了"));
                    ctx.later(80, () -> ctx.title("§e§l2", "§7还是 2"));
                    ctx.later(140, () -> ctx.title("§a§l1", "§7新年快乐"));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_firework_fizzle", "烟花哑火", TrollTier.BRUTAL)
                .desc("你点燃了烟花，它冒了一口烟就放弃了")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.smokeBurst();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.4f);
                    ctx.msg("§7烟花「噗」了一声，然后就没有然后了。");
                    ctx.msg("§8可能是受潮了。可能是它不想。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_gift_box_shake", "礼物盒在动", TrollTier.ANNOYING)
                .desc("桌上的礼物盒自己在动，你决定先不打开")
                .weight(125)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.9f, 1.2f);
                    ctx.msg("§d礼物盒动了一下。§7你说服自己那是风。");
                    ctx.later(100, () -> ctx.msg("§8它又动了一下。这次窗是关的。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_easter_egg_hint", "彩蛋提示是假的", TrollTier.PRANK)
                .desc("系统给了一条彩蛋坐标提示，往那边挖只有石头")
                .weight(125)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e【彩蛋】在坐标 X:§f" + (int) ctx.x() + "§e Z:§f" + (int) ctx.z()
                            + "§e 附近埋着一个彩蛋。");
                    ctx.later(120, () -> ctx.msg("§8你挖了三格。§7什么都没有。§8提示说它记错了。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_greeting_card", "贺卡写错名字", TrollTier.ANNOYING)
                .desc("节日贺卡上收件人的名字写错了，而且错得很具体")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.msg("§d贺卡上写着：§f「祝 §c玩家 §f节日快乐！」");
                    ctx.msg("§8对方显然不知道你是谁，但心意到了。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_lantern_float", "灯笼飘走了", TrollTier.BRUTAL)
                .desc("你放的孔明灯带着你的愿望飘走了，方向不太对")
                .weight(125)
                .cooldownSeconds(75)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 1.3f);
                    ctx.msg("§6孔明灯飘走了。§8你的愿望跟着走了，但方向朝下。");
                })
                .build());

        // ================================================================
        // ANNOYING —— 烦人，节日气氛过头（32 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("fest_spring_lion_dance", "舞狮堵门", TrollTier.NIGHTMARE)
                .desc("舞狮队伍卡在你家门口，鼓声不停")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.1f);
                    for (int i = 0; i < 3; i++) {
                        ctx.giveMobCustomName(ctx.spawnHostileNear(ctx.pick(TrollPools.ANNOYING_MOBS), 6),
                                "§c舞狮");
                    }
                    ctx.msg("§c锣鼓喧天。§7舞狮表示它今天就在这儿了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_spring_firecracker", "鞭炮没完没了", TrollTier.NIGHTMARE)
                .desc("鞭炮声持续不断，持续到下一位受害者出现")
                .weight(120)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    for (int i = 0; i < 4; i++) {
                        ctx.sound(ctx.pick(TrollPools.IMPACT), 0.9f, 1.4f);
                    }
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 80);
                    ctx.msg("§c鞭炮已经响了十分钟。§8没人敢去问什么时候停。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_spring_nian", "年兽在附近", TrollTier.NIGHTMARE)
                .desc("传说里的年兽在你附近踱步，但还没有决定要不要进来")
                .weight(115)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.giveMobCustomName(ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 12), "§c年兽");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 0.9f, 0.9f);
                    ctx.msg("§c年兽在附近。§7它说明年再来，但看样子今晚就想进来。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_spring_reunion_dinner", "年夜饭被抢了", TrollTier.ANNOYING)
                .desc("你准备了一桌年夜饭，桌上的东西少得很快")
                .weight(115)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.feed(2);
                    ctx.msg("§6年夜饭上桌了。§8你回头拿双筷子，菜少了一半。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_midautumn_lantern_riddle", "灯谜答案气人", TrollTier.NIGHTMARE)
                .desc("你猜中了灯谜，但官方答案不是这个")
                .weight(115)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.fakeChat("灯谜会", "§e谜底公布：§f就是你没有猜的那个。");
                    ctx.msg("§8灯谜会表示解释权归他们所有。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.4f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_midautumn_rabbit", "玉兔偷东西", TrollTier.BRUTAL)
                .desc("传说里的玉兔借走了你一样东西，也没打算还")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.giveMobCustomName(ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING),
                            ctx.x() + 2, ctx.y(), ctx.z()), "§f玉兔");
                    ctx.msg("§f一只兔子从你脚边跑过。§7你的背包轻了一点。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_midautumn_mooncake_box", "月饼盒套娃", TrollTier.BRUTAL)
                .desc("一个月饼盒里套了七个盒子，最后一个是空的")
                .weight(110)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.msg("§6第一层：§7包装精美。");
                    ctx.later(40, () -> ctx.msg("§6第二层：§7还是包装。"));
                    ctx.later(80, () -> ctx.msg("§6第七层：§7空的。"));
                    ctx.later(120, () -> ctx.warn("§8月饼：我从来没说过里面有月饼。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_christmas_elf", "精灵在监视你", TrollTier.NIGHTMARE)
                .desc("圣诞精灵躲在暗处记录你今年的表现")
                .weight(115)
                .cooldownSeconds(130)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.giveMobCustomName(ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING),
                            ctx.x() + ctx.rd(-3, 3), ctx.y(), ctx.z() + ctx.rd(-3, 3)), "§a圣诞精灵");
                    ctx.msg("§a有个小东西在记录你的行为。§8今年的报告可能不太好看。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_christmas_snow_globe", "雪球糊脸", TrollTier.BRUTAL)
                .desc("有人朝你扔了一个雪球，精准得不像巧合")
                .weight(115)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.damage("freeze", 2.0f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.6f);
                    ctx.msg("§f雪球正中面门。§7扔它的人已经跑了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_christmas_carol_loop", "圣诞歌单曲循环", TrollTier.NIGHTMARE)
                .desc("那段圣诞旋律卡在同一个音节上，不肯往前走")
                .weight(110)
                .cooldownSeconds(140)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    for (int i = 0; i < 3; i++) {
                        ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    }
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 100);
                    ctx.msg("§7「Jingle bells, jingle bells, jingle bells, jingle bells……」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_halloween_trick", "不给糖就捣蛋", TrollTier.BRUTAL)
                .desc("敲门声响了一夜，你开门的时候外面没人")
                .weight(120)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    for (int i = 0; i < 3; i++) {
                        ctx.sound(ctx.pick(TrollPools.CREEPY), 0.9f, 1.2f);
                    }
                    ctx.msg("§7敲门声。§8你开了门。§7外面没人。§8门关上了。§7又响了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_halloween_costume", "你的皮肤被改了", TrollTier.BRUTAL)
                .desc("系统给你套了一件万圣节服装，你的意见被无视了")
                .weight(115)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.HIDE_HUD, 60);
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 300, 0);
                    ctx.msg("§5系统给你换上了万圣节服装。§8镜子里的你看起来很开心。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_halloween_candy", "糖果是假的", TrollTier.NIGHTMARE)
                .desc("你要来的糖全是用纸包的石子")
                .weight(110)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.give(ctx.pick(TrollPools.JUNK), 5);
                    ctx.msg("§6你收到了一把糖。§7拆开全是石子。");
                    ctx.msg("§8包装纸上写着：§7「万圣节快乐。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_april_fool_fake_ban", "愚人节假封禁", TrollTier.NIGHTMARE)
                .desc("你被弹回了主界面，但世界还在后台跑着")
                .weight(115)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.glitch(GlitchIds.MENU_TEASE, 100);
                    ctx.fakeBroadcast("§c你已被封禁。原因：在模拟服务器上呼吸过快。");
                    ctx.later(120, () -> ctx.msg("§8……四月一号。§7你还在游戏里。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_april_fool_swap", "愚人节身份互换", TrollTier.BRUTAL)
                .desc("你和另一个玩家的位置被节日气氛交换了")
                .weight(110)
                .cooldownSeconds(140)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.swapWith(ctx.other());
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.msg("§d节日快乐！§7顺便，你换了个位置。");
                    ctx.msgToOther(ctx.other(), "§d节日快乐！§8刚才是你吗？");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_april_fool_inventory", "背包恶作剧", TrollTier.ANNOYING)
                .desc("有人趁节日把你的背包重新排列了一遍")
                .weight(115)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.shuffleInventory();
                    ctx.scrambleHotbar();
                    ctx.msg("§d愚人节快乐！§7你的背包刚刚被重新理解了一下。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_birthday_cake_face", "蛋糕糊脸", TrollTier.BRUTAL)
                .desc("有人把生日蛋糕按在了你脸上，这也是祝福的一种")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.smokeBurst();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.msg("§d生日快乐！§7蛋糕在你脸上。§8他们说这是传统。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_birthday_gift_junk", "生日礼物是垃圾", TrollTier.NIGHTMARE)
                .desc("你收到了一堆生日礼物，拆开后没有一件能用")
                .weight(115)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    for (int i = 0; i < 4; i++) {
                        ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    }
                    ctx.msg("§d礼物拆完了。§8每一件都很有诚意，每一件都没用。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_birthday_wish", "许愿被驳回", TrollTier.NIGHTMARE)
                .desc("你的生日愿望提交后收到了一条驳回通知")
                .weight(110)
                .cooldownSeconds(130)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.actionBar("§c【许愿结果】您的愿望已被驳回。");
                    ctx.msg("§8驳回理由：§7「与本人现有能力不符。」");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_valentine_flower_wilt", "玫瑰当场枯萎", TrollTier.BRUTAL)
                .desc("你手上的玫瑰在送出之前就枯萎了")
                .weight(115)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.1f);
                    ctx.msg("§d玫瑰当场谢了。§8它可能比你先看清了形势。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_valentine_arrow", "丘比特射偏", TrollTier.NIGHTMARE)
                .desc("丘比特的箭射中了旁边的生物，不是你")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.giveMobCustomName(ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8), "§c恋爱中的怪");
                    ctx.msg("§d丘比特射偏了。§7中箭的是别的东西。§8它现在很爱你。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_newyear_resolution", "新年目标被嘲笑", TrollTier.NIGHTMARE)
                .desc("你写下新年目标，系统逐条给了批注")
                .weight(115)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.msg("§e目标 1：§7早睡。§8批注：§c去年也是这么写的。");
                    ctx.msg("§e目标 2：§7变强。§8批注：§c数据不支持。");
                    ctx.later(100, () -> ctx.warn("§8【系统】已为您归档到「历年未完成」文件夹。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_newyear_clock", "跨年钟表慢了", TrollTier.BRUTAL)
                .desc("零点的钟声比世界时间晚了整整十秒")
                .weight(110)
                .cooldownSeconds(130)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.timeSkip(200);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.1f);
                    ctx.msg("§e钟声晚了十秒。§8你的新年也晚了十秒开始。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_firework_misfire", "烟花朝你飞", TrollTier.BRUTAL)
                .desc("你点的烟花没有上天，而是贴着地朝你冲过来")
                .weight(115)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.boom(ctx.x() + ctx.rd(-2, 2), ctx.y() + 1, ctx.z() + ctx.rd(-2, 2), 1.5f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.msg("§c烟花没有向上。§7它选择了你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_gift_box_junk", "礼物盒开出垃圾", TrollTier.BRUTAL)
                .desc("你拆开礼物盒，里面的东西和包装完全不匹配")
                .weight(115)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.give(ctx.pick(TrollPools.JUNK), 3);
                    ctx.msg("§d盒子很精致。§7里面是泥土。§8一块。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_gift_box_many", "礼物盒套娃", TrollTier.NIGHTMARE)
                .desc("礼物盒里装着礼物盒，礼物盒里还有礼物盒")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.giveRandomJunk();
                    ctx.later(60, () -> ctx.giveRandomJunk());
                    ctx.later(140, () -> ctx.msg("§8最里面那个盒子上写着：§7「到此为止」。")); 
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_trick_or_treat_spam", "敲门声不断", TrollTier.BRUTAL)
                .desc("全服的门外都在同时响起敲门声")
                .weight(110)
                .cooldownSeconds(140)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    for (int i = 0; i < 5; i++) {
                        ctx.soundToAll(ctx.pick(TrollPools.CREEPY), 0.8f, 1.1f);
                    }
                    ctx.glitch(GlitchIds.CHAT_SPAM, 60);
                    ctx.broadcast("§6不给糖就捣蛋。§8糖已经不重要了，捣蛋才是重点。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_easter_egg_fake", "假彩蛋坐标", TrollTier.BRUTAL)
                .desc("第二个彩蛋提示同样指向了一无所有的地方")
                .weight(110)
                .cooldownSeconds(140)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.fakeBroadcast("§e【彩蛋】本次彩蛋就在你的脚下。真的。");
                    ctx.later(80, () -> {
                        ctx.breakUnder();
                        ctx.warn("§8你挖了两格。§7彩蛋不在。§8你的地板也不在了。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_homage_herobrine", "致敬梗：那个名字", TrollTier.NIGHTMARE)
                .desc("聊天栏里出现了那个不该被提起的名字")
                .weight(115)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.fakeJoinLeave("§k???", true);
                    ctx.fakeChat("???", "§7我一直在看你们玩。");
                    ctx.soundToAll(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 0.9f);
                    ctx.msg("§8有些名字不该被念出来。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_homage_rickroll", "致敬梗：你被套路了", TrollTier.BRUTAL)
                .desc("全服同时收到一条链接，内容你懂的")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.fakeBroadcast("§b点击查看 §n节日惊喜大礼包§r§b → §9[已拦截]");
                    ctx.msg("§7你点开了。§8画面里有人在跳舞。");
                    ctx.msg("§8你早该知道的。");
                    ctx.soundToAll(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_greeting_chorus", "节日祝福刷屏", TrollTier.BRUTAL)
                .desc("几乎所有假账号同时给你发来了节日祝福")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    for (int i = 0; i < 5; i++) {
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "§d节日快乐！祝你的背包永远装得下。");
                    }
                    ctx.glitch(GlitchIds.CHAT_SPAM, 80);
                    ctx.msg("§8你收到了很多祝福。§7没有一条是真人发的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_festival_music", "节日 BGM 关不掉", TrollTier.BRUTAL)
                .desc("节日背景音乐开始播放，并且拒绝被关闭")
                .weight(110)
                .cooldownSeconds(160)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.soundToAll(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.glitch(GlitchIds.HIDE_HUD, 40);
                    ctx.msg("§7节日 BGM 已开始播放。§8它没有停止按钮。");
                })
                .build());

        // ================================================================
        // BRUTAL —— 抓狂，节日变成事故（26 个）
        // ================================================================

        TrollEvents.reg(TrollEvent.of("fest_spring_firecracker_blast", "鞭炮炸手", TrollTier.APOCALYPSE)
                .desc("鞭炮在你手里提前炸了，整个节日都听见了")
                .weight(95)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 2.0f);
                    ctx.damage("fire", 6.0f);
                    ctx.setFire(4);
                    ctx.msg("§c鞭炮在你手里完成了它的使命。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_spring_nian_attack", "年兽来袭", TrollTier.NIGHTMARE)
                .desc("年兽决定不再等候，直接从正面进来")
                .weight(90)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.giveMobCustomName(ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 10), "§c年兽");
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.msg("§c年兽来了。§7它今年不想走了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_spring_lion_stampede", "舞狮冲撞", TrollTier.NIGHTMARE)
                .desc("舞狮队伍突然开始狂奔，你正好在路线上")
                .weight(90)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.fling(ctx.rd(-2.5, 2.5), 0.8, ctx.rd(-2.5, 2.5));
                    ctx.damage("flyintowall", 5.0f);
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.ANNOYING_MOBS), 6);
                    }
                    ctx.msg("§c舞狮队伍冲过来了。§7它们没有减速的意思。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_spring_red_envelope_bait", "红包陷阱", TrollTier.BRUTAL)
                .desc("地上有一个诱人的红包，底下连着机关")
                .weight(90)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 1);
                    ctx.later(40, () -> {
                        ctx.spawnTnt(ctx.x(), ctx.y(), ctx.z(), 20);
                        ctx.warn("§c红包底下连着引线。§8现在你知道了。");
                    });
                    ctx.msg("§c地上有个红包。§7你捡起来了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_midautumn_mooncake_brick", "月饼硬如砖", TrollTier.APOCALYPSE)
                .desc("这个月饼的硬度已经超出了食品的范畴")
                .weight(90)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.damage("cactus", 6.0f);
                    ctx.damageRandomTool(45);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§6你咬了一口月饼。§c你的牙先投降了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_midautumn_moon_pull", "中秋引力", TrollTier.NIGHTMARE)
                .desc("中秋的月亮比平时更近，也更用力")
                .weight(85)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.lift(20.0);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 1.2f);
                    ctx.msg("§e月亮在中秋这天特别有吸引力。§7物理意义上的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_midautumn_rabbit_raid", "玉兔大军", TrollTier.APOCALYPSE)
                .desc("玉兔不是一只，是一群，而且它们不打算只偷东西")
                .weight(85)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 7);
                    }
                    ctx.damage("magic", 5.0f);
                    ctx.msg("§f玉兔成群结队地来了。§7它们比传说里凶得多。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_christmas_gift_tnt", "圣诞礼物是 TNT", TrollTier.APOCALYPSE)
                .desc("包装纸下面不是礼物，是一个已经点着的引信")
                .weight(90)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.spawnTnt(ctx.x(), ctx.y(), ctx.z(), 30);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.1f);
                    ctx.msg("§c圣诞快乐！§7礼物已经开着了，帮你省了一步。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_christmas_tree_fire", "圣诞树着火", TrollTier.NIGHTMARE)
                .desc("圣诞树上的灯太亮，然后就不是灯的问题了")
                .weight(90)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.lightningNear();
                    ctx.setFire(6);
                    ctx.scatterBlocks(ctx.pick(TrollPools.TRAP_BLOCKS), 12);
                    ctx.msg("§c圣诞树着火了。§7彩灯还在闪，气氛很好。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_christmas_krampus", "坎普斯上门", TrollTier.NIGHTMARE)
                .desc("圣诞老人没来，来的是负责处理坏孩子的那位")
                .weight(90)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.giveMobCustomName(ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 9), "§4坎普斯");
                    ctx.damage("magic", 6.0f);
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.msg("§4坎普斯来了。§7它手里拿着名单，上面有你的名字。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_halloween_haunt", "万圣夜闹鬼", TrollTier.APOCALYPSE)
                .desc("万圣夜的鬼不是装饰，它们开始有实体了")
                .weight(90)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 9);
                    }
                    ctx.randomHorrorSound();
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 120);
                    ctx.msg("§5今晚的鬼是真的。§7它们不打算只要糖。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_halloween_pumpkin_bomb", "南瓜灯爆炸", TrollTier.NIGHTMARE)
                .desc("你雕的南瓜灯在夜里变成了一件武器")
                .weight(90)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x() + 1, ctx.y(), ctx.z() + 1, 25);
                    ctx.spawnTnt(ctx.x() - 1, ctx.y(), ctx.z() - 1, 25);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§6南瓜灯的表情变了。§7然后它笑了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_halloween_candy_corn_curse", "糖果诅咒", TrollTier.APOCALYPSE)
                .desc("你吃下的糖带着一层很薄但很持久的恶意")
                .weight(85)
                .cooldownSeconds(260)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.effectRandom(500, 1);
                    ctx.damage("magic", 6.0f);
                    ctx.starve();
                    ctx.msg("§6糖很甜。§7甜完之后有点不对劲。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_april_fool_real_prank", "愚人节来真的", TrollTier.APOCALYPSE)
                .desc("你以为是玩笑，结果这次是真的")
                .weight(90)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.spawnTnt(ctx.x(), ctx.y(), ctx.z(), 35);
                    ctx.boomDestructive(ctx.x() + 2, ctx.y(), ctx.z() + 2, 2.0f);
                    ctx.msg("§c愚人节快乐！§7这个是真的，所以更好笑。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_birthday_cake_explode", "生日蛋糕爆炸", TrollTier.NIGHTMARE)
                .desc("蜡烛不是蜡烛，蛋糕也不是蛋糕")
                .weight(90)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 2.5f);
                    ctx.damage("fire", 6.0f);
                    ctx.setFire(5);
                    ctx.msg("§d生日快乐！§c蛋糕炸了。§8这在你许愿范围之内吗？");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_birthday_surprise_mob", "生日惊喜是怪", TrollTier.APOCALYPSE)
                .desc("朋友们说要给你一个惊喜，他们说的是实话")
                .weight(90)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.playLevelUp();
                    ctx.title("§d§l生日快乐", "§7惊喜已送达");
                    ctx.msg("§d惊喜来了。§c它们都是活的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_valentine_heartbreak", "情人劫", TrollTier.APOCALYPSE)
                .desc("情人节的箭这次射中了你的血条")
                .weight(90)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.damage("magic", 8.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 30);
                    ctx.msg("§c丘比特这次瞄得很准。§7对准的是你的心。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_newyear_fireworks_barrage", "跨年烟花洗地", TrollTier.BRUTAL)
                .desc("零点的烟花装药量明显超标了")
                .weight(90)
                .cooldownSeconds(240)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.boomDestructive(ctx.x() + ctx.rd(-8, 8), ctx.y() + 2,
                                ctx.z() + ctx.rd(-8, 8), 2.0f);
                    }
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 1.0f);
                    ctx.msg("§e新年快乐！§c烟花落点有点低。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_firework_rocket_ride", "烟花带你上天", TrollTier.BRUTAL)
                .desc("你被绑在了烟花上，现在正在升空")
                .weight(85)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.launch(2.4);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.soundToAll(ctx.pick(TrollPools.IMPACT), 1.0f, 1.4f);
                    ctx.msg("§e你被绑在烟花上。§7落点由空气动力学决定，不由你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_firework_inferno", "烟花引发大火", TrollTier.APOCALYPSE)
                .desc("烟花的火星落进了不该落的地方")
                .weight(85)
                .cooldownSeconds(280)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.boomDestructive(ctx.x() + ctx.rd(-10, 10), ctx.y() + 1,
                                ctx.z() + ctx.rd(-10, 10), 2.0f);
                    }
                    ctx.lavaPit(2);
                    ctx.setFire(6);
                    ctx.msg("§c烟花很美。§7火势也很配合。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_gift_box_mimic", "礼物盒是活的", TrollTier.NIGHTMARE)
                .desc("你拆开的那个盒子，自己也长出了牙")
                .weight(90)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.giveMobCustomName(ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 2), "§6礼物盒");
                    ctx.damage("magic", 5.0f);
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 1.2f);
                    ctx.msg("§6礼物盒张开了。§c里面不是礼物。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_easter_egg_trap", "彩蛋陷阱", TrollTier.NIGHTMARE)
                .desc("你终于找到了一个真彩蛋，但它下面有东西")
                .weight(85)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x(), ctx.y(), ctx.z(), 25);
                    ctx.voidPit(2, 6);
                    ctx.msg("§e你找到了彩蛋！§c彩蛋下面连着陷阱。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_homage_tnt_dance", "致敬梗：经典操作", TrollTier.APOCALYPSE)
                .desc("天上开始掉那种最经典的节日惊喜")
                .weight(90)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnTnt(ctx.x() + ctx.rd(-6, 6), ctx.y() + 8, ctx.z() + ctx.rd(-6, 6), 40);
                    }
                    ctx.soundToAll(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§c经典的节日惊喜。§7经典之所以经典，是因为它一直有效。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_parade_crush", "节日游行踩踏", TrollTier.NIGHTMARE)
                .desc("游行队伍从你身上经过，没人注意到")
                .weight(85)
                .cooldownSeconds(260)
                .run(ctx -> {
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.FARM_ANIMALS), 5);
                    }
                    ctx.damage("flyintowall", 7.0f);
                    ctx.fling(ctx.rd(-2, 2), 0.6, ctx.rd(-2, 2));
                    ctx.msg("§7游行队伍过来了。§8它们没有绕路这个选项。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_carnival_ride", "节日过山车失控", TrollTier.NIGHTMARE)
                .desc("节日的游乐设施把你甩了出去，安全带是装饰品")
                .weight(85)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.teleport(ctx.x(), ctx.y() + 25, ctx.z());
                    ctx.spin(ctx.rf(-360, 360));
                    ctx.damage("fall", 6.0f);
                    ctx.msg("§c过山车脱离轨道了。§7你暂时是自由落体。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_pinata", "皮纳塔里全是怪", TrollTier.NIGHTMARE)
                .desc("你一棍子打碎了皮纳塔，里面掉出来的东西会动")
                .weight(85)
                .cooldownSeconds(260)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 4);
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§6皮纳塔碎了。§c掉出来的东西开始爬。");
                })
                .build());

        // ================================================================
        // 终章 —— 节日不再是节日，而是永远停不下来的刑罚
        // ================================================================

        TrollEvents.reg(TrollEvent.of("fest_newyear_doomsday_countdown", "末日倒计时", TrollTier.OBLIVION)
                .desc("它数的不是新年，是你还能重来几次；每数到零，就再数一遍")
                .weight(60)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.buryAlive();
                    ctx.effectRandom(1200, 3);
                    ctx.voidPit(2, 16);
                    ctx.title("§4§l10", "§7倒计时开始");
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.later(40, () -> ctx.title("§4§l5", "§8这不是新年的倒计时"));
                    ctx.later(80, () -> ctx.title("§4§l1", "§8准备好了吗"));
                    // 假希望：让你以为熬过去了
                    ctx.later(110, () -> {
                        ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 4.0f);
                        ctx.titleToAll("§a§l新年快乐", "§7恭喜你，熬过来了");
                        ctx.msg("§a倒计时结束。§7世界还在，你也在。");
                    });
                    // 然后重数一遍，一遍比一遍短
                    ctx.later(260, () -> {
                        ctx.warn("§4……倒计时重新开始。§7从现在起，每一年都算。");
                        ctx.title("§4§l10", "§8又来了");
                        ctx.stripAll();
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 900, 2);
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 120, 3);
                    });
                    ctx.later(460, () -> {
                        ctx.msg("§4这一次不会再有『新年快乐』了。");
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_spring_nian_king", "年兽之王", TrollTier.OBLIVION)
                .desc("它不怕鞭炮，也不吃人——它只是来把『年』从你身上收走，连明年一起")
                .weight(55)
                .cooldownSeconds(650)
                .run(ctx -> {
                    ctx.title("§4§l年 兽 之 王", "§7它不回避任何东西");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.giveMobCustomName(ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8), "§4年兽之王");
                    ctx.giveMobCustomName(ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 12), "§4年兽近卫");
                    // 假希望：它留下了压岁钱
                    ctx.give(ctx.pick(TrollPools.LOOT), 2);
                    ctx.msg("§e它放下一个红包。§7「拿着，这是你今年的运气。」");
                    ctx.later(140, () -> {
                        ctx.stripAll();
                        ctx.warn("§4红包是空的。§7你今年的运气已经提前用完了。");
                    });
                    ctx.later(300, () -> {
                        ctx.voidPit(2, 16);
                        ctx.meteorShower(6, 18.0);
                        ctx.damage("magic", 3.0f);
                        ctx.msg("§4它说门联贴反了，所以它把门也一起拆了。");
                    });
                    ctx.later(460, () -> ctx.obliterate());
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_midautumn_blood_moon", "中秋血月", TrollTier.APOCALYPSE)
                .desc("今年中秋的月亮是红的，而且它看着你")
                .weight(55)
                .cooldownSeconds(700)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.timeOfDay(18000);
                    ctx.title("§4§l中 秋 血 月", "§7团圆的对象不太对");
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 9);
                    }
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 600, 1);
                    ctx.msg("§4月亮是红的。§7它说今晚所有人都得团圆。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_christmas_krampus_night", "坎普斯之夜", TrollTier.OBLIVION)
                .desc("名单永远念不完，而你的名字被写在了每一页上")
                .weight(55)
                .cooldownSeconds(700)
                .run(ctx -> {
                    ctx.title("§4§l坎 普 斯 之 夜", "§7名单已确认");
                    for (int i = 0; i < 3; i++) {
                        ctx.giveMobCustomName(ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 10),
                                "§4坎普斯");
                    }
                    ctx.stripAll();
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.msg("§7它递给你一样东西。§e「今年你表现得不错。」");
                    ctx.later(120, () -> {
                        ctx.stripAll();
                        ctx.warn("§4袋子里只有煤。§7而且它顺手把你家的炉子也带走了。");
                    });
                    ctx.later(300, () -> {
                        ctx.meteorShower(6, 18.0);
                        ctx.buryAlive();
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 140, 3);
                        ctx.msg("§4名单上还有一页。§7下一页还是你。");
                    });
                    ctx.later(480, () -> {
                        ctx.damage("wither", 3.0f);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_halloween_night_terror", "万圣夜永夜", TrollTier.APOCALYPSE)
                .desc("万圣节的夜被无限延长，糖果早就分完了")
                .weight(55)
                .cooldownSeconds(700)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.timeOfDay(18000);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 150, 2);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 900, 2);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10);
                    }
                    ctx.msg("§5万圣节的夜不会再结束了。§7糖发完了，鬼留下了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_april_fool_total_chaos", "愚人节全面失控", TrollTier.NIGHTMARE)
                .desc("玩笑开到最后，没人记得哪个是真的")
                .weight(55)
                .cooldownSeconds(650)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_BAN, 150, 3);
                    ctx.shuffleInventory();
                    ctx.scatter(30);
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 120);
                    ctx.effectRandom(400, 2);
                    ctx.msg("§d愚人节快乐！§c顺便，这一条是真的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_birthday_curse", "生日诅咒", TrollTier.APOCALYPSE)
                .desc("你许的愿被一个不太友善的对象听到了")
                .weight(55)
                .cooldownSeconds(700)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 800, 2);
                    ctx.damage("wither", 8.0f);
                    ctx.title("§d§l愿望已受理", "§4受理人：不是你希望的那个");
                    ctx.msg("§d你的愿望被受理了。§c受理方表示会严格按字面执行。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_valentine_curse", "情人劫", TrollTier.OBLIVION)
                .desc("所有关于爱的信号都被劫持了，包括你以为自己收到的那一份")
                .weight(50)
                .cooldownSeconds(700)
                .run(ctx -> {
                    ctx.titleToAll("§4§l情 人 劫", "§7今天没有玫瑰，只有账单");
                    ctx.give(ctx.pick(TrollPools.MYSTERY_ITEMS), 1);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "§d这个给你，我一直想给你。");
                    ctx.msg("§d有人送你东西了。§7你甚至有点感动。");
                    ctx.later(140, () -> {
                        ctx.stripAll();
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 700, 2);
                        ctx.warn("§4礼物是诱饵。§7劫匪说它也想有个伴——就是你。");
                    });
                    ctx.later(320, () -> {
                        for (int i = 0; i < 3; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                        }
                        ctx.voidPit(2, 16);
                        ctx.effectRandom(1200, 3);
                        ctx.msg("§4它把『爱』的定义也一并收走了。");
                    });
                    ctx.later(480, () -> ctx.obliterate());
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_gift_box_pandora", "潘多拉礼物盒", TrollTier.OBLIVION)
                .desc("盒子打开之前一切都还来得及；打开之后，连『希望』都不打算留下")
                .weight(50)
                .cooldownSeconds(750)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 3);
                    ctx.msg("§e盒子里是礼物。§7你甚至有点期待。");
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.later(120, () -> {
                        ctx.stripAll();
                        ctx.voidPit(3, 10);
                        ctx.msg("§4礼物都跑光了。§7盒底还剩一样东西：希望。");
                    });
                    // 假希望：它说把希望留给你
                    ctx.later(260, () -> {
                        ctx.give(ctx.pick(TrollPools.MYSTERY_ITEMS), 1);
                        ctx.msg("§a「这个留给你。」§7你把它握紧了。");
                    });
                    ctx.later(420, () -> {
                        ctx.takeRandomItems(8);
                        ctx.effectRandom(600, 2);
                        ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 4.0f);
                        ctx.warn("§4希望说它不负责这个。§7它从来没打算留下。");
                    });
                    ctx.later(560, () -> {
                        ctx.voidPit(2, 16);
                        ctx.meteorShower(6, 18.0);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_homage_404", "致敬梗：找不到的彩蛋", TrollTier.APOCALYPSE)
                .desc("你追寻的彩蛋在系统的定义里不存在")
                .weight(50)
                .cooldownSeconds(700)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 150, 2);
                    ctx.timeSkip(9000);
                    ctx.title("§c§l404", "§7你要找的东西从来没有被实现");
                    ctx.msg("§8你翻遍了整个世界。§7开发者在注释里写着：TODO。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_festival_of_death", "亡者节", TrollTier.APOCALYPSE)
                .desc("今天纪念的人有点太多了，而且他们想亲自参加")
                .weight(50)
                .cooldownSeconds(750)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.timeOfDay(18000);
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 9);
                    }
                    ctx.fakeDeathScreen("被节日气氛带走");
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 700, 2);
                    ctx.msg("§4亡者节快乐。§7他们真的来了，而且不打算只待一天。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_parade_of_damned", "亡者游行", TrollTier.OBLIVION)
                .desc("游行队伍穿过你的世界，也穿过你；队伍没有尽头，也没有出口")
                .weight(50)
                .cooldownSeconds(700)
                .run(ctx -> {
                    ctx.titleToAll("§8§l游 行", "§7请不要加入队伍");
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                    for (int i = 0; i < 6; i++) {
                        ctx.giveMobCustomName(ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 12),
                                "§8游行队伍");
                    }
                    ctx.scatterFar(120);
                    ctx.msg("§8队伍很长，看不到头。§7它们也不看路。");
                    ctx.later(160, () -> {
                        ctx.scatterFar(160);
                        ctx.stripAll();
                        ctx.warn("§4你已经被裹进队伍里了。§7没有人告诉你怎么退出。");
                    });
                    ctx.later(340, () -> {
                        ctx.buryAlive();
                        ctx.meteorShower(6, 18.0);
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 800, 2);
                        ctx.msg("§8队伍继续前进。§4前面还是队伍。");
                    });
                    ctx.later(520, () -> ctx.obliterate());
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_carnival_nightmare", "噩梦嘉年华", TrollTier.OBLIVION)
                .desc("游乐场只在夜里开门，所有项目都免费，因为出口已经拆掉了")
                .weight(50)
                .cooldownSeconds(750)
                .run(ctx -> {
                    ctx.titleToAll("§5§l噩 梦 嘉 年 华", "§7门票已经用你的名字买了");
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 200, 3);
                    ctx.scatterFar(80);
                    ctx.buryAlive();
                    ctx.msg("§5第一站：旋转木马。§7它转起来就不打算停。");
                    ctx.later(140, () -> {
                        ctx.scatterFar(160);
                        ctx.voidPit(2, 16);
                        ctx.msg("§5第二站：鬼屋。§7鬼是真的，屋也是。");
                    });
                    ctx.later(300, () -> {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                        ctx.effectRandom(1200, 3);
                        ctx.msg("§5第三站：你。§7这一站是专门为你准备的。");
                    });
                    ctx.later(460, () -> {
                        ctx.effectRandom(500, 2);
                        ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 4.0f);
                        ctx.msg("§5所有项目都不需要排队。§4因为你跑不掉。");
                    });
                    ctx.later(600, () -> ctx.obliterate());
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_eternal_celebration", "永无止境的庆典", TrollTier.APOCALYPSE)
                .desc("庆典决定永远办下去：音乐关不掉，烟花不停，祝福永远在刷屏")
                .weight(50)
                .cooldownSeconds(800)
                .once()
                .run(ctx -> {
                    ctx.timeOfDay(18000);
                    ctx.glitch(GlitchIds.MASS_CHAT, 200, 2);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.8f);
                    ctx.titleToAll("§5§l庆 典 开 始", "§7这一场不会结束");
                    ctx.msg("§5庆典不会结束。§7烟花会一直放，音乐不会停，你也不许走。");
                    // BGM 关不掉：一轮接一轮，没有人喊停
                    ctx.later(120, () -> {
                        ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.7f);
                        ctx.glitch(GlitchIds.MASS_CHAT, 200, 2);
                        ctx.msg("§5第一轮烟花。§7还有下一轮。");
                    });
                    ctx.later(260, () -> {
                        ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                        ctx.glitch(GlitchIds.MASS_CHAT, 240, 3);
                        ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 3.0f);
                        ctx.msg("§5第二轮。§7没有人喊停。");
                    });
                    ctx.later(400, () -> {
                        ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 1200, 2);
                        ctx.buryAlive();
                        ctx.msg("§5庆典继续。§7你现在是唯一的嘉宾了。");
                    });
                    ctx.later(540, () -> {
                        ctx.msg("§4烟花不会停。§7因为已经没有人留下来关掉它。");
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 1800, 3);
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_april_fool_wipe", "愚人节背包消失", TrollTier.NIGHTMARE)
                .desc("最经典的那个愚人节玩笑，这次是真的")
                .weight(50)
                .cooldownSeconds(800)
                .run(ctx -> {
                    ctx.dropEverything();
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 150, 2);
                    ctx.fakeBroadcast("§c【提示】检测到玩家背包异常，已为您清空以排查问题。");
                    ctx.later(200, () -> ctx.msg("§8愚人节快乐。§7东西在你脚下了，去捡吧。"));
                })
                .build());

        // ================================================================
        // 终章 —— 绝望：烟花放完了，却总还有「最后一场」
        // ================================================================

        TrollEvents.reg(TrollEvent.of("fest_final_fireworks", "终末烟花", TrollTier.OBLIVION)
                .desc("它说这是最后一场；它没说这是第几次最后一场")
                .weight(35)
                .cooldownSeconds(1200)
                .run(ctx -> {
                    ctx.titleToAll("§4§l终 末 烟 花", "§7请抬头，这是最后一场");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    for (int i = 0; i < 6; i++) {
                        ctx.boomDestructive(ctx.x() + ctx.rd(-18, 18), ctx.y() + 3,
                                ctx.z() + ctx.rd(-18, 18), 4.0f);
                    }
                    ctx.effectRandom(1200, 3);
                    ctx.voidPit(2, 16);
                    ctx.msg("§4烟花很漂亮。§7漂亮到地面已经不存在了。");
                    ctx.later(180, () -> {
                        ctx.titleToAll("§4§l谢 幕", "§7感谢观看");
                        ctx.meteorShower(18, 40);
                        ctx.stripAll();
                        ctx.msg("§8……幕布没有落下。");
                    });
                    ctx.later(400, () -> {
                        ctx.titleToAll("§4§l最 后 一 场（2）", "§7真的，这次是最后一次");
                        for (int i = 0; i < 6; i++) {
                            ctx.boomDestructive(ctx.x() + ctx.rd(-18, 18), ctx.y() + 3,
                                    ctx.z() + ctx.rd(-18, 18), 4.0f);
                        }
                        ctx.warn("§4它数错了。§7它每次都会数错。");
                    });
                    ctx.later(620, () -> ctx.obliterate());
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_newyear_world_end", "跨年即世界末日", TrollTier.OBLIVION)
                .desc("倒计时归零，世界归零；然后新的一年开始，倒计时再归零")
                .weight(30)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.titleToAll("§4§l3", "§7准备迎接新年");
                    ctx.later(40, () -> ctx.titleToAll("§4§l2", "§8也准备迎接别的"));
                    ctx.later(80, () -> ctx.titleToAll("§4§l1", "§c祝你新年快乐"));
                    ctx.later(120, () -> {
                        ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 7.0f);
                        ctx.lightning();
                        ctx.stripAll();
                        ctx.meteorShower(18, 40);
                        ctx.buryAlive();
                        ctx.msg("§4新年快乐。§7这是你今年听到的第一句，也是最后一句。");
                    });
                    // 跨年循环：世界翻页，你留下
                    ctx.later(320, () -> {
                        ctx.titleToAll("§4§l新的一年", "§8旧世界已结算");
                        ctx.effectRandom(1200, 3);
                        ctx.msg("§8日历翻到了下一页。§7世界没有跟着翻。");
                    });
                    ctx.later(520, () -> {
                        ctx.titleToAll("§4§l3", "§7准备迎接下一个新年");
                        ctx.warn("§4每一年都会这样结束。§7你已经知道了。");
                    });
                    ctx.later(700, () -> ctx.obliterate());
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_spring_nian_extinction", "年兽灭世", TrollTier.OBLIVION)
                .desc("它学会了不怕；接着它会让所有东西都不再怕你的鞭炮")
                .weight(30)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.titleToAll("§4§l年 兽 不 怕 了", "§7传统方法已失效");
                    for (int i = 0; i < 4; i++) {
                        ctx.giveMobCustomName(ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 9),
                                "§4年兽");
                    }
                    ctx.lavaPit(4);
                    ctx.voidPit(2, 16);
                    ctx.stripAll();
                    ctx.msg("§4鞭炮没有用了。§7它现在已经知道那只是响声。");
                    ctx.later(200, () -> {
                        for (int i = 0; i < 4; i++) {
                            ctx.giveMobCustomName(ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10),
                                    "§4学会了不怕的");
                        }
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 900, 2);
                        ctx.msg("§4它把这件本事教给了别的东西。§7现在它们都不怕了。");
                    });
                    ctx.later(420, () -> {
                        ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 6.0f);
                        ctx.meteorShower(24, 50);
                        ctx.msg("§8明年的鞭炮，也已经没有用了。");
                    });
                    ctx.later(600, () -> ctx.obliterate());
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_halloween_harvest", "万圣收割", TrollTier.OBLIVION)
                .desc("南瓜只是练习；收割按顺序进行，而你是最后一批容器")
                .weight(30)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.timeOfDay(18000);
                    ctx.titleToAll("§4§l万 圣 收 割", "§7第一批：南瓜");
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 300, 3);
                    ctx.stripAll();
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10);
                    }
                    ctx.msg("§4南瓜都被雕空了。§7接下来轮到别的容器。");
                    ctx.later(220, () -> {
                        ctx.dropEverything();
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 1200, 3);
                        ctx.msg("§4第二批：你的行李。§7它们说你用不着了。");
                    });
                    ctx.later(440, () -> {
                        ctx.buryAlive();
                        ctx.meteorShower(24, 50);
                        ctx.msg("§4第三批：你。§7收割者说你会被留到最后处理。");
                    });
                    ctx.later(640, () -> ctx.obliterate());
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_christmas_judgment", "圣诞审判", TrollTier.OBLIVION)
                .desc("名单念完了，你被放在最后一页；审判不是判决，是过程")
                .weight(30)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.titleToAll("§4§l名 单 已 念 完", "§7你是最后一个");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.glitch(GlitchIds.FAKE_BAN, 300, 3);
                    ctx.give(ctx.pick(TrollPools.LOOT), 1);
                    ctx.msg("§e「先拿着礼物。」§7你被告知还需要复核。");
                    ctx.later(160, () -> {
                        ctx.stripAll();
                        ctx.setFire(10);
                        ctx.msg("§4复核结果：礼物收回。§7理由一栏写着「没有理由」。");
                    });
                    ctx.later(340, () -> {
                        ctx.voidPit(2, 16);
                        ctx.effectRandom(1200, 3);
                        ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 6.0f);
                        ctx.msg("§4今年没有人被标记为『好孩子』。§7一个都没有。");
                    });
                    ctx.later(560, () -> ctx.obliterate());
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_birthday_last", "最后一个生日", TrollTier.OBLIVION)
                .desc("蜡烛永远吹不灭，于是这个生日会一直过下去")
                .weight(25)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.titleToAll("§d§l生 日 快 乐", "§4这将是最后一次");
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.0f, 0.5f);
                    ctx.buryAlive();
                    ctx.effectRandom(1200, 3);
                    ctx.msg("§d祝你生日快乐。§4许个愿吧，它听得见。");
                    ctx.later(180, () -> {
                        ctx.voidPit(2, 16);
                        ctx.lavaPit(4);
                        ctx.msg("§4蜡烛灭了一根。§7然后它自己又亮了。");
                    });
                    ctx.later(380, () -> {
                        ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 7.0f);
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 1500, 3);
                        ctx.titleToAll("§d§l再 来 一 次", "§4蜡烛还有 " + ctx.ri(20, 60) + " 根");
                        ctx.msg("§7没有人告诉你总共有多少根。");
                    });
                    ctx.later(620, () -> ctx.obliterate());
                })
                .build());

        TrollEvents.reg(TrollEvent.of("fest_easter_egg_truth", "彩蛋真相", TrollTier.OBLIVION)
                .desc("终极彩蛋的奖励是一句真话：这个世界只是为了让你找到这里")
                .weight(25)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.titleToAll("§4§l彩 蛋 已 找 到", "§7不过你可能不太想找到它");
                    ctx.glitch(GlitchIds.FAKE_CRASH, 300, 3);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.4f);
                    ctx.give(ctx.pick(TrollPools.MYSTERY_ITEMS), 1);
                    ctx.msg("§e彩蛋奖励已发放。§7你甚至有点感动。");
                    ctx.later(160, () -> {
                        ctx.stripAll();
                        ctx.msg("§4彩蛋内容：§7「你居然真的找到了。」");
                        ctx.msg("§8「所以你不需要奖励了。」");
                    });
                    ctx.later(340, () -> {
                        ctx.voidPit(5, 24);
                        ctx.meteorShower(24, 50);
                        ctx.effectRandom(1200, 3);
                        ctx.msg("§8它把世界关掉了。§7因为它的用途已经完成了。");
                    });
                    ctx.later(560, () -> {
                        ctx.buryAlive();
                        ctx.obliterate();
                    });
                })
                .build());
    }
}
