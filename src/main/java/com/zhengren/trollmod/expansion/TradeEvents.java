package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【村庄与交易】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。共 99 个事件，
 * 全部 id 以 {@code trade_} 开头，覆盖村民、职业、绿宝石、交易涨价、掠夺者、袭击、
 * 铁傀儡、雪傀儡、猫、钟、流浪商人、唤魔者、女巫等主题。</p>
 *
 * <p>纪律：本文件刻意不引用任何 Minecraft 原版类型，只用 {@code ctx.*}
 * 方法与 {@code TrollPools.*} / {@code GlitchIds.*} 常量，因此不受 Minecraft API 版本影响。</p>
 */
public final class TradeEvents {

    private TradeEvents() {
    }

    public static void register() {
        TrollEvents.category("村庄与交易");

        // ============================================================
        // 一、PRANK（小整）18 个
        // ============================================================

        TrollEvents.reg(TrollEvent.of("trade_emerald_polite", "礼貌的绿宝石", TrollTier.PRANK)
                .desc("村民热情地送你一颗绿宝石，并坚持要记账")
                .weight(120)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 1);
                    ctx.msg("§7村民热情地塞给你一颗绿宝石，然后小声补了一句：『记你账上。』");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.later(200, () -> {
                        ctx.takeRandomItem();
                        ctx.msg("§7村民回来收款了，本金加利息，一起拿走了。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_discount_lie", "今日特价", TrollTier.PRANK)
                .desc("宣布全场一折，但原价已经被默默乘了十倍")
                .weight(110)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.msg("§e村民：『今天全场一折！』§7（原价已经提前乘了十倍）");
                    ctx.actionBar("§a今日特价：全部交易 +900%");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_villager_hello", "村民的问候", TrollTier.PRANK)
                .desc("村民用一整段鼻音向你问好，翻译过来不太礼貌")
                .weight(100)
                .cooldownSeconds(30)
                .run(ctx -> {
                    ctx.fakeChat("村民", "哼——嗯——哼！（翻译：你好，冤大头。）");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_cat_purr", "猫的呼噜", TrollTier.PRANK)
                .desc("村里的猫蹭了蹭你的腿，顺便在裤脚上开了个洞")
                .weight(90)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 1.8f);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 12);
                    ctx.msg("§7一只猫蹭了蹭你的腿，顺便把你的裤脚抓了个洞。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_bell_toll", "钟自己响了", TrollTier.PRANK)
                .desc("村里的钟无故自鸣，钟不打算解释，你也不敢问")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 0.7f);
                    ctx.msg("§7村里的钟自己响了一下。没人知道为什么。");
                    ctx.later(60, () -> ctx.msg("§7钟又响了一下。这次听起来像是在叫你。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_joke", "绿宝石可食用", TrollTier.PRANK)
                .desc("伪造一条官方公告，宣称绿宝石已更新为可食用")
                .weight(100)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e【交易提示】绿宝石已更新为可食用。");
                    ctx.msg("§7请勿真的食用绿宝石，除非你想解锁隐藏成就。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_reroll_free", "免费重掷", TrollTier.PRANK)
                .desc("提示交易已重掷，其实什么都没变")
                .weight(80)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.actionBar("§a交易已重掷！");
                    ctx.msg("§7（你的交易列表没有任何变化。一条都没有。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_bad_price", "天价报价", TrollTier.PRANK)
                .desc("村民盯着你的东西沉默三秒，然后报了个离谱的价格")
                .weight(110)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.msg("§7村民看着你手里的东西，沉默了三秒，然后报了个天价。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_hello", "流浪商人的耐心", TrollTier.PRANK)
                .desc("流浪商人自称有全世界最稀有的商品：耐心")
                .weight(100)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.fakeChat("流浪商人", "我这里有全世界最稀有的东西——耐心。");
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_villager_laugh", "村民笑了", TrollTier.PRANK)
                .desc("村民当着你的面笑你，然后说是在笑你的背包")
                .weight(110)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.5f);
                    ctx.fakeChat("村民", "哈哈哈哈……对不起，我是在笑你的背包。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_shower_small", "小雨般的绿宝石", TrollTier.PRANK)
                .desc("天降绿宝石，落地时悄悄换了材质")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 30);
                    ctx.give(ctx.pick(TrollPools.JUNK), 2);
                    ctx.msg("§a天上掉下几颗绿宝石！§7（快看，它们落地时变成了别的东西。）");
                    ctx.later(100, () -> ctx.msg("§7绿宝石已到账——在你的梦里。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_iron_golem_wave", "铁傀儡挥手", TrollTier.PRANK)
                .desc("铁傀儡朝你挥了挥手，你希望那真的只是挥手")
                .weight(80)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.6f, 0.6f);
                    ctx.msg("§7铁傀儡朝你挥了挥手。你希望那是挥手。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_snow_golem_sneeze", "雪傀儡打喷嚏", TrollTier.PRANK)
                .desc("雪傀儡一个喷嚏，你的鞋里全是雪")
                .weight(80)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.9f, 1.3f);
                    ctx.msg("§7雪傀儡打了个喷嚏，你的鞋里全是雪。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_chicken_trade", "用鸡换东西", TrollTier.PRANK)
                .desc("村民想用一只活鸡跟你交易，鸡本人表示抗议")
                .weight(90)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS), ctx.x(), ctx.y() + 1, ctx.z());
                    ctx.msg("§7村民想用一只鸡跟你换点东西。鸡表示强烈抗议。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_map_lie", "藏宝图", TrollTier.PRANK)
                .desc("流浪商人卖你一张藏宝图，坐标指向他自己家")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.WEIRD_ITEMS), 1);
                    ctx.msg("§7流浪商人卖给你一张藏宝图，坐标指向他家门口。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_note_block_ad", "音符盒广告", TrollTier.PRANK)
                .desc("村庄广播开始循环播放自治委员会的赞助广告")
                .weight(90)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.9f);
                    ctx.actionBar("§e♪ 本村由村民自治委员会独家赞助 ♪");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_cat_gift", "猫的礼物", TrollTier.PRANK)
                .desc("猫叼来三样礼物放在你脚边，然后一脸期待")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), 3);
                    ctx.msg("§7猫叼来三样「礼物」放在你脚边，然后一脸期待地看着你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_pillager_joke", "掠夺者冷笑话", TrollTier.PRANK)
                .desc("掠夺者日报今天不打劫，改讲冷笑话")
                .weight(80)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeBroadcast("§c【掠夺者日报】今日不打劫，改讲冷笑话。");
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "§7你知道村民为什么不出门吗？因为他没有门。");
                    ctx.later(80, () -> ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "§7……你们怎么都不笑？"));
                })
                .build());

        // ============================================================
        // 二、ANNOYING（烦人）32 个
        // ============================================================

        TrollEvents.reg(TrollEvent.of("trade_price_up", "物价上涨", TrollTier.ANNOYING)
                .desc("村民以物价上涨为由，顺便让你长长记性")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.msg("§c村民：『最近物价上涨，你也涨涨记性。』");
                    ctx.actionBar("§c所有交易价格 +300%");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_tax", "呼吸税", TrollTier.ANNOYING)
                .desc("村民委员会开始征收呼吸税，先记账下次一起收")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.msg("§c村民委员会向你征收『呼吸税』：3 颗绿宝石。");
                    ctx.msg("§7（他们先记在账上了，说是下次一起收。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_haggle_fail", "讨价还价失败", TrollTier.ANNOYING)
                .desc("你试图砍价，村民用沉默击败了你，还附赠一个负面效果")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.msg("§7你试图讨价还价。村民用长达十秒的沉默击败了你。");
                    ctx.effectRandom(120, 0);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_villager_ignore", "村民不理你", TrollTier.ANNOYING)
                .desc("你点了他好几次，他假装在看货架")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.msg("§7你点了村民好几次，他一直在假装研究货架。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_bed_occupied", "床被占了", TrollTier.ANNOYING)
                .desc("你的床被一个村民睡了，他睡得很香")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.msg("§7你的床被一个村民睡了。他睡得很香，还打呼。");
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 0.9f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_workstation_moved", "工作站被搬走", TrollTier.ANNOYING)
                .desc("你的工作站被搬走了，你被挪到了村子另一头")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.msg("§7你的工作站被搬到了村子另一头。它说它想静静。");
                    ctx.scatter(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_profession_confusion", "职业错乱", TrollTier.ANNOYING)
                .desc("村民的职业栏在你眼前刷成乱码，他自己也懵了")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.msg("§7村民的职业栏在你眼前刷成了乱码。他自己也懵了。");
                    ctx.glitch(GlitchIds.SHAKY, 40, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_villager_shrug", "拒绝提供信息", TrollTier.ANNOYING)
                .desc("村民用一声鼻音回应了你的所有问题")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeChat("村民", "哼。（翻译：不知道，别问我。）");
                    ctx.actionBar("§7村民拒绝提供任何有用信息。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_iron_golem_stare", "铁傀儡凝视", TrollTier.ANNOYING)
                .desc("铁傀儡站在你旁边一动不动地看你，已经十分钟了")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.msg("§7铁傀儡站在你旁边，一动不动地看着你。已经十分钟了。");
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_golem_block", "铁傀儡挡路", TrollTier.ANNOYING)
                .desc("铁傀儡友好地把你请出了他的视线范围")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.scatter(4);
                    ctx.msg("§7铁傀儡很友好地把你「请」出了他的视线范围。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_snow_golem_snowball", "雪球远程攻击", TrollTier.ANNOYING)
                .desc("雪傀儡对你进行了精准的远程打击")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE), ctx.x(), ctx.y() + 2, ctx.z());
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 15);
                    ctx.msg("§7一只雪傀儡对你进行了远程攻击。命中率百分之百。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_cat_distract", "猫的干扰", TrollTier.ANNOYING)
                .desc("一只猫路过，你的视线就不属于你了")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.randomRotation();
                    ctx.msg("§7一只猫从你面前走过，你的视线不受控制地跟了过去。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_llama_spit", "羊驼口水", TrollTier.ANNOYING)
                .desc("流浪商人的羊驼朝你吐口水，商人假装没看见")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§7流浪商人的羊驼朝你吐了口水。商人假装没看见。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_price", "坐地起价", TrollTier.ANNOYING)
                .desc("流浪商人看了看你，把所有价格默默乘了三")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.msg("§7流浪商人看了看你，把所有价格默默乘了三。");
                    ctx.actionBar("§c流浪商人：『你看起来很有钱。』");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_bell_wrong", "误报袭击", TrollTier.ANNOYING)
                .desc("警钟大作，其实只是有人在搬钟")
                .weight(90)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.title("§e§l警钟", "§7（其实只是有人在搬钟）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_bell_ring_raid", "假袭击警报", TrollTier.ANNOYING)
                .desc("袭击警报是假的，但你的心跳是真的")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 1.1f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.msg("§7村里响起了袭击警报。警报是假的，但你的心跳是真的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_pillager_patrol_rumor", "巡逻队传闻", TrollTier.ANNOYING)
                .desc("伪造一条掠夺者巡逻队正在接近的警报")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.fakeBroadcast("§c【警报】掠夺者巡逻队正在接近……可能吧。");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_vindicator_visit", "上门推销员", TrollTier.ANNOYING)
                .desc("一位拿着斧头的推销员出现在你家门口")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.giveMobCustomName(
                            ctx.spawnAny(ctx.pick(TrollPools.HOSTILE), ctx.x() + 6, ctx.y() + 1, ctx.z()),
                            "上门推销员");
                    ctx.msg("§7一位「上门推销员」出现在你家门口，手里拿着斧头。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_illusionist_mirror", "幻术师镜像", TrollTier.ANNOYING)
                .desc("幻术师让你开始怀疑自己显示器坏了")
                .weight(90)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SHAKY, 60, 2);
                    ctx.msg("§7幻术师对你施展了法术，你的画面开始怀疑自己。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_witch_splash", "女巫的试用装", TrollTier.ANNOYING)
                .desc("路过的女巫朝你扔了瓶药水，说这是试用装")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.msg("§7一个路过的女巫朝你扔了瓶药水，说这是「试用装」。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_evoker_fang_tease", "尖牙虚晃", TrollTier.ANNOYING)
                .desc("地面裂开又合上，唤魔者说他只是在热身")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.msg("§7地面裂开又合上了。唤魔者说他只是在热身。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_zombie_villager_cure", "治疗方法错了", TrollTier.ANNOYING)
                .desc("你试图治疗僵尸村民，结果自己先感染了坏运气")
                .weight(90)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.effectRandom(120, 0);
                    ctx.msg("§7你试图治疗僵尸村民，结果自己先感染了坏运气。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_float", "会飞的绿宝石", TrollTier.ANNOYING)
                .desc("一颗绿宝石在你面前悬浮，然后飞走了")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 25);
                    ctx.msg("§7一颗绿宝石悬浮在你面前，然后飞走了。你追不上它。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_ledger_error", "账本错误", TrollTier.ANNOYING)
                .desc("村民说你还欠他 64 颗绿宝石，可你从没交易过")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.msg("§7村民翻了翻账本，说你还欠他 64 颗绿宝石。");
                    ctx.msg("§7（你从来没有和他交易过。一次都没有。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_receipt_lost", "收据风波", TrollTier.ANNOYING)
                .desc("村民索要收据，你把快捷栏翻乱了也没找到")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.msg("§7村民：『你的收据呢？』你把快捷栏翻了个底朝天。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_villager_gossip_you", "村民八卦你", TrollTier.ANNOYING)
                .desc("两位村民当着你面讨论你上次偷钟的事")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeChat("村民甲", "就是他，上次把村里的钟偷了。");
                    ctx.fakeChat("村民乙", "我看见了，他还假装路过。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_cat_steals_chair", "猫抢椅子", TrollTier.ANNOYING)
                .desc("你刚坐下，猫就把椅子占了还打了个哈欠")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.7f);
                    ctx.msg("§7你刚坐下，猫就把你的椅子占了，还打了个哈欠。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_golem_flower", "铁傀儡的礼物", TrollTier.ANNOYING)
                .desc("铁傀儡送你一朵花，然后一脚踩扁了它")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.msg("§7铁傀儡送你一朵花，然后当着你的面一脚踩扁了它。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_snow_golem_melt", "雪傀儡化了", TrollTier.ANNOYING)
                .desc("你的雪傀儡化了，临死前看了你一眼")
                .weight(90)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 30);
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 1.4f);
                    ctx.msg("§7你的雪傀儡化了。它临走前看了你一眼。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_disappear", "商人蒸发", TrollTier.ANNOYING)
                .desc("流浪商人当着你的面消失，连同他许诺的折扣")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.3f);
                    ctx.msg("§7流浪商人当着你的面消失了，连同他许诺的折扣一起。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_trade_lockout", "交易被冻结", TrollTier.ANNOYING)
                .desc("你的交易权限被冻结，理由是想买东西的意图过于明显")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.msg("§c你的交易权限已被暂时冻结。理由：看起来太想买东西。");
                    ctx.setCooldown(ctx.pick(TrollPools.JUNK), 300);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_hotbar_emerald", "绿宝石炫光", TrollTier.ANNOYING)
                .desc("绿宝石的反光晃了你的眼，快捷栏顺序全乱")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.msg("§7你的快捷栏被绿宝石的光晃了一下，顺序全乱了。");
                })
                .build());

        // ============================================================
        // 三、BRUTAL（抓狂）27 个
        // ============================================================

        TrollEvents.reg(TrollEvent.of("trade_emerald_drain", "找零", TrollTier.BRUTAL)
                .desc("村民从你背包里「找零」，没告诉你找了多少")
                .weight(110)
                .cooldownSeconds(150)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    if (ctx.takeRandomItem()) {
                        ctx.msg("§c村民从你背包里「找零」了，没告诉你具体找了多少。");
                    } else {
                        ctx.msg("§c村民想从你背包里找零，结果发现你什么都没有，场面很尴尬。");
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_inventory_tax", "交易税", TrollTier.BRUTAL)
                .desc("交易税以混乱的形式从背包扣除")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.shuffleInventory();
                    ctx.msg("§c交易税已从背包扣除（以混乱的形式）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_hotbar_fee", "手续费", TrollTier.BRUTAL)
                .desc("手续费从快捷栏扣除，扣的是顺序")
                .weight(100)
                .cooldownSeconds(120)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.msg("§c手续费已从快捷栏扣除，顺序是其中一部分。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_iron_golem_throw", "铁傀儡投掷", TrollTier.BRUTAL)
                .desc("铁傀儡认为你影响村容，于是把你扔了出去")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-1.5f, 1.5f), 1.6, ctx.rf(-1.5f, 1.5f));
                    ctx.damage(4.0f);
                    ctx.msg("§c铁傀儡认为你影响了村容，把你扔了出去。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_golem_slam", "铁傀儡拍肩", TrollTier.BRUTAL)
                .desc("铁傀儡拍了拍你的肩膀，用的是拳头")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.damage(6.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§c铁傀儡拍了拍你的肩膀。用的是拳头。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_spawn", "不祥之兆", TrollTier.BRUTAL)
                .desc("你身上多了个不祥之兆，和三个不祥的东西")
                .weight(120)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 12);
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.msg("§c你身上多了一个不祥之兆，和三个不祥的东西。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_pillager_ambush", "麦田伏击", TrollTier.BRUTAL)
                .desc("掠夺者从麦田里站了起来，原来那不是麦田")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.msg("§c掠夺者从麦田里站了起来。原来那不是麦田。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_vindicator_axe", "斧头试锋", TrollTier.BRUTAL)
                .desc("卫道士说他只是路过，顺便试了试斧头")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.damage("magic", 5.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§c卫道士说他只是路过，顺便试了试斧头的锋利度。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_witch_poison", "女巫新品", TrollTier.BRUTAL)
                .desc("女巫的新品种，副作用是「活着」")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.damage("magic", 3.0f);
                    ctx.msg("§c女巫说这是新品种，副作用是「活着」。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_evoker_vex", "唤魔者的小帮手", TrollTier.BRUTAL)
                .desc("唤魔者叫来两个小帮手，它们都很有意见")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 5);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.1f);
                    ctx.msg("§c唤魔者叫来了两个小帮手，它们都很有意见。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_ravager_charge", "劫掠兽冲撞", TrollTier.BRUTAL)
                .desc("劫掠兽从村口一路冲到你脸上，中间没有减速")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.damage(7.0f);
                    ctx.fling(ctx.rf(-2f, 2f), 1.2, ctx.rf(-2f, 2f));
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§c劫掠兽从村口一路冲到你脸上，中间没有减速。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_omen", "村庄通缉", TrollTier.BRUTAL)
                .desc("你被村庄正式通缉，罪名是「存在」")
                .weight(100)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.fakeBroadcast("§c【不祥之兆】你已经被村庄通缉，罪名是「存在」。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_bell_siege", "钟声围城", TrollTier.BRUTAL)
                .desc("钟声一响，袭击准时开始，村庄向你道谢")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.title("§4§l袭击开始", "§7村庄：我谢谢你");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_village_burning", "村民的热情", TrollTier.BRUTAL)
                .desc("村民热情地为你点了火把，位置选在你身上")
                .weight(100)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.setFire(6);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§c村民热情地为你点了火把，只是位置选在了你身上。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_tnt_trade", "TNT 以物易物", TrollTier.BRUTAL)
                .desc("村民想用 TNT 跟你换东西，直接把 TNT 扔过来了")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x() + ctx.rd(-3, 3), ctx.y() + 1, ctx.z() + ctx.rd(-3, 3), 60);
                    ctx.msg("§c村民想用 TNT 跟你换东西。他直接把 TNT 扔过来了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_lava_price", "岩浆付款", TrollTier.BRUTAL)
                .desc("村民说只收岩浆，然后直接把岩浆铺好了")
                .weight(80)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.msg("§c村民说付款方式只收岩浆。他直接把岩浆铺好了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_void_payment", "虚空找零", TrollTier.BRUTAL)
                .desc("为了找零，村民在你脚下开了个洞")
                .weight(80)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.voidPit(2, 6);
                    ctx.msg("§c为了支付，村民在你脚下开了个洞。他说这是「找零」。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_scatter_market", "被请出市场", TrollTier.BRUTAL)
                .desc("你被请出市场，方式是传到很远的地方")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.scatterFar(60);
                    ctx.msg("§c你被「请」出了市场，方式是把你传到市场之外很远的地方。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_curse", "绿宝石诅咒", TrollTier.BRUTAL)
                .desc("你捡起的绿宝石是个诅咒，它跟你回家了")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 2);
                    ctx.msg("§c你捡起的绿宝石是个诅咒。它现在跟你回家了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_golem_confiscate", "铁傀儡没收", TrollTier.BRUTAL)
                .desc("铁傀儡以安全隐患为由没收了你一件物品")
                .weight(110)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    if (ctx.takeRandomItem()) {
                        ctx.msg("§c铁傀儡以「安全隐患」为由没收了你的一件物品。");
                    } else {
                        ctx.msg("§c铁傀儡想没收你的东西，但你穷得让他无话可说。");
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_wave", "袭击波次", TrollTier.BRUTAL)
                .desc("袭击开始了，第一波是你，第二波还是你")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 9);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 11);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 13);
                    ctx.msg("§c袭击开始了。第一波是你，第二波还是你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_pillager_crossbow", "弩箭先到", TrollTier.BRUTAL)
                .desc("掠夺者的弩箭先到了，道歉后到")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE), ctx.x() + ctx.rd(-6, 6), ctx.y() + 3,
                            ctx.z() + ctx.rd(-6, 6));
                    ctx.damage("magic", 4.0f);
                    ctx.msg("§c掠夺者的弩箭先到了，然后是那句『抱歉，手滑』。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_trap", "特价商品", TrollTier.BRUTAL)
                .desc("流浪商人的特价商品是一颗点着了的 TNT")
                .weight(100)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x() + ctx.rd(-2, 2), ctx.y() + 1, ctx.z() + ctx.rd(-2, 2), 40);
                    ctx.msg("§c流浪商人的「特价商品」是一颗点了火的 TNT。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_cat_scratch", "撸猫的代价", TrollTier.BRUTAL)
                .desc("你摸了村里的猫，猫给你上了一课")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.damage(4.0f);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.6f);
                    ctx.msg("§c你摸了村里的猫。猫给你上了一课，学费是血。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_witch_swarm", "女巫茶话会", TrollTier.BRUTAL)
                .desc("三个女巫在村口开茶话会，你是茶")
                .weight(100)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10);
                    ctx.msg("§c三个女巫在村口开茶话会，你是茶。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_evoker_wololo", "唤魔者下场", TrollTier.BRUTAL)
                .desc("唤魔者决定亲自下场，这是你要求的")
                .weight(100)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 10);
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.msg("§c唤魔者决定亲自下场。这是你要求的，你亲口说的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_iron_golem_revolt", "铁傀儡决议", TrollTier.BRUTAL)
                .desc("铁傀儡开会一致通过了对你的处理方案")
                .weight(100)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                    ctx.damage(5.0f);
                    ctx.msg("§c铁傀儡们开了个会，一致通过了对你的处理方案。");
                })
                .build());

        // ============================================================
        // 四、NIGHTMARE（崩溃）15 个
        // ============================================================

        TrollEvents.reg(TrollEvent.of("trade_golem_execution", "村庄判决", TrollTier.NIGHTMARE)
                .desc("铁傀儡执行村庄法庭的判决，罪名是「太吵」")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.damage(12.0f);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.7f);
                    ctx.msg("§4铁傀儡执行了村庄法庭的判决：你太吵了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_apocalypse_wave", "袭击第七波", TrollTier.NIGHTMARE)
                .desc("这一波袭击没有终点，公告也是这么写的")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.fakeBroadcast("§4【袭击 · 第 7 波】本波次没有终点。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_ravager_stampede", "劫掠兽踩踏", TrollTier.NIGHTMARE)
                .desc("一群劫掠兽决定在你身上练冲刺")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.damage(10.0f);
                    ctx.fling(ctx.rf(-3f, 3f), 2.0, ctx.rf(-3f, 3f));
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                    ctx.msg("§4一群劫掠兽决定在你身上练习冲刺。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_evoker_fang_real", "尖牙成真", TrollTier.NIGHTMARE)
                .desc("这次尖牙是真的，上次只是预热")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.damage("magic", 12.0f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                    ctx.msg("§4这次尖牙是真的。唤魔者说上次只是预热。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_witch_curse_real", "女巫终身会员", TrollTier.NIGHTMARE)
                .desc("女巫给你办了终身会员，费用是你的生命条")
                .weight(90)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.damage("wither", 6.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    ctx.msg("§4女巫给你上了「终身会员」，费用从生命条里扣。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_village_massacre", "村庄不太平静", TrollTier.NIGHTMARE)
                .desc("村庄今早彻底不平静，全服都有目共睹")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 9);
                    ctx.broadcast("§4村庄今早不太平静。具体来说，是彻底不平静。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_all_gone", "绿宝石集体辞职", TrollTier.NIGHTMARE)
                .desc("你的绿宝石们集体辞职，还带走了几位同事")
                .weight(90)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.dropRandom();
                    ctx.dropRandom();
                    ctx.dropRandom();
                    ctx.msg("§4你的绿宝石们集体辞职，并顺手带走了几位同事。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_hoard_lost", "资产安全保管", TrollTier.NIGHTMARE)
                .desc("村庄银行系统故障，你的全部资产已被安全保管")
                .weight(80)
                .cooldownSeconds(360)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.clearInventory();
                    ctx.msg("§4村庄银行系统故障，你的全部资产已被「安全保管」。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_bell_doom", "丧钟", TrollTier.NIGHTMARE)
                .desc("丧钟为谁而鸣，答案一直是你")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.title("§4§l丧钟为谁而鸣", "§7为你，一直是你");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_doom", "限时优惠到货", TrollTier.NIGHTMARE)
                .desc("流浪商人的限时优惠到货了，货是两只精英怪")
                .weight(80)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                    ctx.msg("§4流浪商人的「限时优惠」到货了，货是两只精英怪。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_cat_nightmare", "猫的噩梦", TrollTier.NIGHTMARE)
                .desc("村里的猫同时转头看你，而且是同时")
                .weight(80)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.BLACKOUT, 80, 2);
                    ctx.randomHorrorSound();
                    ctx.msg("§4村里的猫全都转头看着你。它们同时转头。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_snow_golem_army", "雪傀儡军团", TrollTier.NIGHTMARE)
                .desc("雪傀儡军团成立，第一个决议是砸你")
                .weight(80)
                .cooldownSeconds(300)
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                ctx.x() + ctx.rd(-5, 5), ctx.y() + 1, ctx.z() + ctx.rd(-5, 5));
                    }
                    ctx.msg("§4雪傀儡军团成立了，它们的第一个决议是砸你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_iron_golem_colossus", "巨型铁傀儡", TrollTier.NIGHTMARE)
                .desc("铁傀儡升级了：变得非常大，且非常生气")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 5);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.msg("§4铁傀儡升级了。升级内容：变得非常大，且非常生气。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_endless", "无尽袭击", TrollTier.NIGHTMARE)
                .desc("本次袭击没有波次上限，公告已明确说明")
                .weight(70)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 10);
                    ctx.fakeBroadcast("§4【袭击】本次袭击没有波次上限。祝你好运。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_wither", "发黑的绿宝石", TrollTier.NIGHTMARE)
                .desc("你收到的绿宝石在发黑，它开始扣你的血")
                .weight(80)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.damage("wither", 5.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 2);
                    ctx.msg("§4你收到的绿宝石在发黑。它开始扣你的血了。");
                })
                .build());

        // ============================================================
        // 五、APOCALYPSE（地狱）7 个
        // ============================================================

        TrollEvents.reg(TrollEvent.of("trade_village_annihilation", "村庄除名", TrollTier.APOCALYPSE)
                .desc("陨石雨加地面爆炸，村庄被温柔地从地图上移除")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.meteorShower(8, 14.0);
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 6.0f);
                    ctx.broadcast("§4村庄已从地图上「温柔地」移除。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_apocalypse", "袭击末世", TrollTier.APOCALYPSE)
                .desc("雷暴、四只敌对生物和一只精英怪，袭击等级末日")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.thunderstorm(1200);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 10);
                    ctx.broadcast("§4袭击等级：末日。请村民自行逃命，玩家不算村民。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_evoker_apocalypse", "唤魔者搬家", TrollTier.APOCALYPSE)
                .desc("唤魔者决定把村子搬去另一个维度，包括你")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 10);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.broadcast("§4唤魔者决定把村子搬进另一个维度。包括你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_golem_apocalypse", "天空的判决", TrollTier.APOCALYPSE)
                .desc("铁傀儡向天空投诉了你，天空同意了")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.lightning();
                    ctx.lightningNear();
                    ctx.damage("lightning", 10.0f);
                    ctx.broadcast("§4铁傀儡向天空投诉了你，天空同意了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_market_crash", "市场崩盘", TrollTier.APOCALYPSE)
                .desc("经济崩盘：资产、地图和信心同时归零，还会假崩溃")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 100, 3);
                    ctx.clearInventory();
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 5.0f);
                    ctx.broadcast("§4经济系统崩盘：你的资产、地图和信心同时归零。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_singularity", "绿宝石奇点", TrollTier.APOCALYPSE)
                .desc("一颗绿宝石产生了奇点，把你吸了进去")
                .weight(30)
                .cooldownSeconds(720)
                .run(ctx -> {
                    ctx.kill();
                    ctx.broadcast("§4一颗绿宝石产生了奇点，把你吸了进去。村庄表示遗憾。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_apocalypse", "清仓大甩卖", TrollTier.APOCALYPSE)
                .desc("流浪商人的清仓大甩卖开始了，甩的是陨石")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.meteorShower(6, 12.0);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                    ctx.broadcast("§4流浪商人的「清仓大甩卖」开始了，甩的是陨石。");
                })
                .build());
    }
}
