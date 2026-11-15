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
        // 一、PRANK（小整）
        // ============================================================

        TrollEvents.reg(TrollEvent.of("trade_emerald_polite", "礼貌的绿宝石", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("trade_villager_hello", "村民的问候", TrollTier.BRUTAL)
                .desc("村民用一整段鼻音向你问好，翻译过来不太礼貌")
                .weight(100)
                .cooldownSeconds(30)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.fakeChat("村民", "哼——嗯——哼！（翻译：你好，冤大头。）");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_cat_purr", "猫的呼噜", TrollTier.ANNOYING)
                .desc("村里的猫蹭了蹭你的腿，顺便在裤脚上开了个洞")
                .weight(90)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 1.8f);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 12);
                    ctx.msg("§7一只猫蹭了蹭你的腿，顺便把你的裤脚抓了个洞。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_bell_toll", "钟自己响了", TrollTier.ANNOYING)
                .desc("村里的钟无故自鸣，钟不打算解释，你也不敢问")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 0.7f);
                    ctx.msg("§7村里的钟自己响了一下。没人知道为什么。");
                    ctx.later(60, () -> ctx.msg("§7钟又响了一下。这次听起来像是在叫你。"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_joke", "绿宝石可食用", TrollTier.BRUTAL)
                .desc("伪造一条官方公告，宣称绿宝石已更新为可食用")
                .weight(100)
                .cooldownSeconds(45)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.fakeBroadcast("§e【交易提示】绿宝石已更新为可食用。");
                    ctx.msg("§7请勿真的食用绿宝石，除非你想解锁隐藏成就。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_reroll_free", "免费重掷", TrollTier.BRUTAL)
                .desc("提示交易已重掷，其实什么都没变")
                .weight(80)
                .cooldownSeconds(45)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.actionBar("§a交易已重掷！");
                    ctx.msg("§7（你的交易列表没有任何变化。一条都没有。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_bad_price", "天价报价", TrollTier.ANNOYING)
                .desc("村民盯着你的东西沉默三秒，然后报了个离谱的价格")
                .weight(110)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.msg("§7村民看着你手里的东西，沉默了三秒，然后报了个天价。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_hello", "流浪商人的耐心", TrollTier.BRUTAL)
                .desc("流浪商人自称有全世界最稀有的商品：耐心")
                .weight(100)
                .cooldownSeconds(50)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.fakeChat("流浪商人", "我这里有全世界最稀有的东西——耐心。");
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_villager_laugh", "村民笑了", TrollTier.BRUTAL)
                .desc("村民当着你的面笑你，然后说是在笑你的背包")
                .weight(110)
                .cooldownSeconds(40)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.5f);
                    ctx.fakeChat("村民", "哈哈哈哈……对不起，我是在笑你的背包。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_shower_small", "小雨般的绿宝石", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("trade_iron_golem_wave", "铁傀儡挥手", TrollTier.BRUTAL)
                .desc("铁傀儡朝你挥了挥手，你希望那真的只是挥手")
                .weight(80)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.6f, 0.6f);
                    ctx.msg("§7铁傀儡朝你挥了挥手。你希望那是挥手。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_snow_golem_sneeze", "雪傀儡打喷嚏", TrollTier.BRUTAL)
                .desc("雪傀儡一个喷嚏，你的鞋里全是雪")
                .weight(80)
                .cooldownSeconds(50)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.9f, 1.3f);
                    ctx.msg("§7雪傀儡打了个喷嚏，你的鞋里全是雪。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_chicken_trade", "用鸡换东西", TrollTier.ANNOYING)
                .desc("村民想用一只活鸡跟你交易，鸡本人表示抗议")
                .weight(90)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS), ctx.x(), ctx.y() + 1, ctx.z());
                    ctx.msg("§7村民想用一只鸡跟你换点东西。鸡表示强烈抗议。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_map_lie", "藏宝图", TrollTier.BRUTAL)
                .desc("流浪商人卖你一张藏宝图，坐标指向他自己家")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.give(ctx.pick(TrollPools.WEIRD_ITEMS), 1);
                    ctx.msg("§7流浪商人卖给你一张藏宝图，坐标指向他家门口。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_note_block_ad", "音符盒广告", TrollTier.ANNOYING)
                .desc("村庄广播开始循环播放自治委员会的赞助广告")
                .weight(90)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.9f);
                    ctx.actionBar("§e♪ 本村由村民自治委员会独家赞助 ♪");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_cat_gift", "猫的礼物", TrollTier.ANNOYING)
                .desc("猫叼来三样礼物放在你脚边，然后一脸期待")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), 3);
                    ctx.msg("§7猫叼来三样「礼物」放在你脚边，然后一脸期待地看着你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_pillager_joke", "掠夺者冷笑话", TrollTier.ANNOYING)
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
        // 二、ANNOYING（烦人）
        // ============================================================

        TrollEvents.reg(TrollEvent.of("trade_price_up", "物价上涨", TrollTier.BRUTAL)
                .desc("村民以物价上涨为由，顺便让你长长记性")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
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

        TrollEvents.reg(TrollEvent.of("trade_haggle_fail", "讨价还价失败", TrollTier.BRUTAL)
                .desc("你试图砍价，村民用沉默击败了你，还附赠一个负面效果")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.msg("§7你试图讨价还价。村民用长达十秒的沉默击败了你。");
                    ctx.effectRandom(120, 0);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_villager_ignore", "无人回应", TrollTier.NIGHTMARE)
                .desc("你喊了七次，没有一个村民回头；饥饿替你回答了这个问题")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.msg("§7你点了村民好几次，他一直在假装研究货架。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_bed_occupied", "床不再是你的", TrollTier.NIGHTMARE)
                .desc("村民睡上了你的床，你被丢到一百二十格外，理由是没有理由")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.msg("§7你的床被一个村民睡了。他睡得很香，还打呼。");
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 0.9f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_workstation_moved", "家当搬空", TrollTier.NIGHTMARE)
                .desc("工作站消失，你带着一身凋零被挪到村外，村民说这叫重新分配")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.msg("§7你的工作站被搬到了村子另一头。它说它想静静。");
                    ctx.scatter(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_profession_confusion", "职业错乱", TrollTier.BRUTAL)
                .desc("村民的职业栏在你眼前刷成乱码，他自己也懵了")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.msg("§7村民的职业栏在你眼前刷成了乱码。他自己也懵了。");
                    ctx.glitch(GlitchIds.SHAKY, 40, 1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_villager_shrug", "集体失语", TrollTier.NIGHTMARE)
                .desc("所有村民同时装聋，凋零从沉默里渗出来，顺着你的血条往上爬")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.fakeChat("村民", "哼。（翻译：不知道，别问我。）");
                    ctx.actionBar("§7村民拒绝提供任何有用信息。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_iron_golem_stare", "铁傀儡的注视", TrollTier.NIGHTMARE)
                .desc("它站在你旁边一动不动地看你，脚下已经替你铺好了岩浆")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.msg("§7铁傀儡站在你旁边，一动不动地看着你。已经十分钟了。");
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_golem_block", "被清出视线", TrollTier.NIGHTMARE)
                .desc("铁傀儡把你连同凋零一起请出村子，动作很轻，后果很重")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.scatter(4);
                    ctx.msg("§7铁傀儡很友好地把你「请」出了他的视线范围。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_snow_golem_snowball", "雪球远程攻击", TrollTier.BRUTAL)
                .desc("雪傀儡对你进行了精准的远程打击")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE), ctx.x(), ctx.y() + 2, ctx.z());
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 15);
                    ctx.msg("§7一只雪傀儡对你进行了远程攻击。命中率百分之百。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_cat_distract", "猫的干扰", TrollTier.BRUTAL)
                .desc("一只猫路过，你的视线就不属于你了")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.randomRotation();
                    ctx.msg("§7一只猫从你面前走过，你的视线不受控制地跟了过去。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_llama_spit", "羊驼口水", TrollTier.BRUTAL)
                .desc("流浪商人的羊驼朝你吐口水，商人假装没看见")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§7流浪商人的羊驼朝你吐了口水。商人假装没看见。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_price", "无价之市", TrollTier.NIGHTMARE)
                .desc("流浪商人把价格抬到你付不起的高度，再给你挂上一整套负面效果")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.msg("§7流浪商人看了看你，把所有价格默默乘了三。");
                    ctx.actionBar("§c流浪商人：『你看起来很有钱。』");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_bell_wrong", "警钟为假", TrollTier.NIGHTMARE)
                .desc("钟声大作，村民四散，只有你的饥饿值是真的被清空了")
                .weight(90)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.title("§e§l警钟", "§7（其实只是有人在搬钟）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_bell_ring_raid", "等不到的袭击", TrollTier.NIGHTMARE)
                .desc("你握着武器等了一整晚，只等到一整套负面效果准时到账")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.sound(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 1.1f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.msg("§7村里响起了袭击警报。警报是假的，但你的心跳是真的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_pillager_patrol_rumor", "永不抵达的巡逻队", TrollTier.NIGHTMARE)
                .desc("警报是真的，巡逻队是假的，饥饿是真的，而且不会走")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.fakeBroadcast("§c【警报】掠夺者巡逻队正在接近……可能吧。");
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_vindicator_visit", "上门讨债人", TrollTier.NIGHTMARE)
                .desc("拿着斧头的人站在你门口，凋零先替他说完了开场白")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.giveMobCustomName(
                            ctx.spawnAny(ctx.pick(TrollPools.HOSTILE), ctx.x() + 6, ctx.y() + 1, ctx.z()),
                            "上门推销员");
                    ctx.msg("§7一位「上门推销员」出现在你家门口，手里拿着斧头。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_illusionist_mirror", "幻术师镜像", TrollTier.BRUTAL)
                .desc("幻术师让你开始怀疑自己显示器坏了")
                .weight(90)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.glitch(GlitchIds.SHAKY, 60, 2);
                    ctx.msg("§7幻术师对你施展了法术，你的画面开始怀疑自己。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_witch_splash", "女巫的试用装", TrollTier.BRUTAL)
                .desc("路过的女巫朝你扔了瓶药水，说这是试用装")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.msg("§7一个路过的女巫朝你扔了瓶药水，说这是「试用装」。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_evoker_fang_tease", "尖牙预演", TrollTier.NIGHTMARE)
                .desc("地面裂开又合上，你的工具在缝里碎了，唤魔者说这只是彩排")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.msg("§7地面裂开又合上了。唤魔者说他只是在热身。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_zombie_villager_cure", "治疗方法错了", TrollTier.BRUTAL)
                .desc("你试图治疗僵尸村民，结果自己先感染了坏运气")
                .weight(90)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.effectRandom(120, 0);
                    ctx.msg("§7你试图治疗僵尸村民，结果自己先感染了坏运气。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_float", "看得见拿不到", TrollTier.NIGHTMARE)
                .desc("绿宝石在你眼前飘走，饥饿把你按在原地看完了全程")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
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

        TrollEvents.reg(TrollEvent.of("trade_receipt_lost", "收据风波", TrollTier.BRUTAL)
                .desc("村民索要收据，你把快捷栏翻乱了也没找到")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.scrambleHotbar();
                    ctx.msg("§7村民：『你的收据呢？』你把快捷栏翻了个底朝天。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_villager_gossip_you", "全村都在说你", TrollTier.NIGHTMARE)
                .desc("你被议论着扔到一百二十格外，没有一个人替你说一句话")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.fakeChat("村民甲", "就是他，上次把村里的钟偷了。");
                    ctx.fakeChat("村民乙", "我看见了，他还假装路过。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_cat_steals_chair", "连猫都不让位", TrollTier.NIGHTMARE)
                .desc("猫占了你的位置，你的工具替你付了这笔座位费")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.7f);
                    ctx.msg("§7你刚坐下，猫就把你的椅子占了，还打了个哈欠。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_golem_flower", "踩碎的花", TrollTier.NIGHTMARE)
                .desc("铁傀儡送你一朵花，再从你背包里收走两件回礼，然后当着你的面踩碎")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
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

        TrollEvents.reg(TrollEvent.of("trade_wandering_disappear", "人间蒸发", TrollTier.NIGHTMARE)
                .desc("流浪商人当着你的面消失，同时带走了你身上的一部分，余额不补")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.3f);
                    ctx.msg("§7流浪商人当着你的面消失了，连同他许诺的折扣一起。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_trade_lockout", "永久冻结", TrollTier.NIGHTMARE)
                .desc("交易权限被冻结，脚下被铺上岩浆，理由是你太想买东西了")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.msg("§c你的交易权限已被暂时冻结。理由：看起来太想买东西。");
                    ctx.setCooldown(ctx.pick(TrollPools.JUNK), 300);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_hotbar_emerald", "眩光", TrollTier.NIGHTMARE)
                .desc("绿宝石的反光刺进眼睛，快捷栏和生命条在同一秒乱掉")
                .weight(90)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.scrambleHotbar();
                    ctx.msg("§7你的快捷栏被绿宝石的光晃了一下，顺序全乱了。");
                })
                .build());

        // ============================================================
        // 三、BRUTAL（抓狂）
        // ============================================================

        TrollEvents.reg(TrollEvent.of("trade_emerald_drain", "全部找零", TrollTier.APOCALYPSE)
                .desc("村民清空了你的背包，说这是找零；找了多少，他不打算说")
                .weight(110)
                .cooldownSeconds(150)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    if (ctx.takeRandomItem()) {
                        ctx.msg("§c村民从你背包里「找零」了，没告诉你具体找了多少。");
                    } else {
                        ctx.msg("§c村民想从你背包里找零，结果发现你什么都没有，场面很尴尬。");
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_inventory_tax", "交易税·全额", TrollTier.APOCALYPSE)
                .desc("你用全部经验和整包物品的顺序，支付了这笔名为交易税的账单")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.shuffleInventory();
                    ctx.msg("§c交易税已从背包扣除（以混乱的形式）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_hotbar_fee", "手续费", TrollTier.APOCALYPSE)
                .desc("手续费是整包清空，再用混乱给你找回一点零头")
                .weight(100)
                .cooldownSeconds(120)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.scrambleHotbar();
                    ctx.msg("§c手续费已从快捷栏扣除，顺序是其中一部分。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_iron_golem_throw", "投掷练习", TrollTier.NIGHTMARE)
                .desc("铁傀儡把你当成练手材料，伤害在你落地那一刻才到账")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-1.5f, 1.5f), 1.6, ctx.rf(-1.5f, 1.5f));
                    ctx.damage(4.0f);
                    ctx.msg("§c铁傀儡认为你影响了村容，把你扔了出去。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_golem_slam", "铁拳拍肩", TrollTier.NIGHTMARE)
                .desc("它拍了拍你的肩，用的是拳头，附赠一整套负面效果")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.damage(6.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§c铁傀儡拍了拍你的肩膀。用的是拳头。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_spawn", "不祥之兆成真", TrollTier.NIGHTMARE)
                .desc("你身上多了一个不祥之兆，和三个正在赶来的东西，它们熟知你的坐标")
                .weight(120)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 12);
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.msg("§c你身上多了一个不祥之兆，和三个不祥的东西。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_pillager_ambush", "麦田里的伏兵", TrollTier.APOCALYPSE)
                .desc("掠夺者从麦田里站起来，你脚下的十格同时消失")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.msg("§c掠夺者从麦田里站了起来。原来那不是麦田。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_vindicator_axe", "斧刃试锋", TrollTier.APOCALYPSE)
                .desc("卫道士用你的身体测试锋利度，再用黑曜石把你封起来慢慢评价")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.damage("magic", 5.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§c卫道士说他只是路过，顺便试了试斧头的锋利度。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_witch_poison", "女巫的试用装", TrollTier.NIGHTMARE)
                .desc("新品种的副作用是持续掉血，女巫管这个叫「活着」")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.damage("magic", 3.0f);
                    ctx.msg("§c女巫说这是新品种，副作用是「活着」。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_evoker_vex", "大小帮手", TrollTier.APOCALYPSE)
                .desc("唤魔者叫来的帮手不止两个，你身上还挂着一整套负面效果")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.TINY_ANNOYING), 5);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.1f);
                    ctx.msg("§c唤魔者叫来了两个小帮手，它们都很有意见。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_ravager_charge", "正面冲撞", TrollTier.APOCALYPSE)
                .desc("劫掠兽一路冲到你脸上，虚空伤害和撞击伤害在同一秒结算")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.damage(7.0f);
                    ctx.fling(ctx.rf(-2f, 2f), 1.2, ctx.rf(-2f, 2f));
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§c劫掠兽从村口一路冲到你脸上，中间没有减速。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_omen", "村庄通缉令", TrollTier.APOCALYPSE)
                .desc("罪名是「存在」，惩罚是经验清零加一整套负面，执行人是全村")
                .weight(100)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.fakeBroadcast("§c【不祥之兆】你已经被村庄通缉，罪名是「存在」。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_bell_siege", "钟声围城", TrollTier.APOCALYPSE)
                .desc("钟声一响，黑曜石封顶，袭击开始，村庄向你道谢")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.title("§4§l袭击开始", "§7村庄：我谢谢你");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_village_burning", "村民的火把", TrollTier.NIGHTMARE)
                .desc("他们热情地为你点了火把，位置选在你身上，还顺手把你传走")
                .weight(100)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.setFire(6);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§c村民热情地为你点了火把，只是位置选在了你身上。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_tnt_trade", "以物易物", TrollTier.NIGHTMARE)
                .desc("村民用 TNT 跟你交换：先收走两件东西，再点燃引信")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.spawnTnt(ctx.x() + ctx.rd(-3, 3), ctx.y() + 1, ctx.z() + ctx.rd(-3, 3), 60);
                    ctx.msg("§c村民想用 TNT 跟你换东西。他直接把 TNT 扔过来了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_lava_price", "岩浆结算", TrollTier.NIGHTMARE)
                .desc("只收岩浆，先扣两件物品，再把岩浆整片铺在你脚下")
                .weight(80)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.lavaPit(2);
                    ctx.msg("§c村民说付款方式只收岩浆。他直接把岩浆铺好了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_void_payment", "虚空找零", TrollTier.APOCALYPSE)
                .desc("为了找零，村民在你脚下开了洞，天上下起了陨石")
                .weight(80)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.voidPit(2, 6);
                    ctx.msg("§c为了支付，村民在你脚下开了个洞。他说这是「找零」。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_scatter_market", "逐出市场", TrollTier.APOCALYPSE)
                .desc("你被黑曜石封住，再连人带棺材一起扔到六十格之外")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.scatterFar(60);
                    ctx.msg("§c你被「请」出了市场，方式是把你传到市场之外很远的地方。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_curse", "绿宝石的利息", TrollTier.APOCALYPSE)
                .desc("诅咒随绿宝石住进你身体，虚空伤害开始按日计息")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 2);
                    ctx.msg("§c你捡起的绿宝石是个诅咒。它现在跟你回家了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_golem_confiscate", "强制没收", TrollTier.NIGHTMARE)
                .desc("铁傀儡以安全隐患为由没收你的物品，并把你扔到很远的地方重新考虑")
                .weight(110)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    if (ctx.takeRandomItem()) {
                        ctx.msg("§c铁傀儡以「安全隐患」为由没收了你的一件物品。");
                    } else {
                        ctx.msg("§c铁傀儡想没收你的东西，但你穷得让他无话可说。");
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_wave", "波次：你", TrollTier.APOCALYPSE)
                .desc("第一波是你，第二波还是你；天上有陨石，地上有三只东西")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 9);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 11);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 13);
                    ctx.msg("§c袭击开始了。第一波是你，第二波还是你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_pillager_crossbow", "弩箭先到", TrollTier.APOCALYPSE)
                .desc("弩箭先射穿你，黑曜石随后封口，那句道歉永远不会到")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE), ctx.x() + ctx.rd(-6, 6), ctx.y() + 3,
                            ctx.z() + ctx.rd(-6, 6));
                    ctx.damage("magic", 4.0f);
                    ctx.msg("§c掠夺者的弩箭先到了，然后是那句『抱歉，手滑』。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_trap", "特价商品", TrollTier.APOCALYPSE)
                .desc("流浪商人的特价是 TNT，你已经被清空的背包是赠品")
                .weight(100)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.spawnTnt(ctx.x() + ctx.rd(-2, 2), ctx.y() + 1, ctx.z() + ctx.rd(-2, 2), 40);
                    ctx.msg("§c流浪商人的「特价商品」是一颗点了火的 TNT。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_cat_scratch", "撸猫账单", TrollTier.APOCALYPSE)
                .desc("猫抓了你，天上落下两颗陨石替你结账")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
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

        TrollEvents.reg(TrollEvent.of("trade_evoker_wololo", "唤魔者亲自下场", TrollTier.APOCALYPSE)
                .desc("虚空伤害先到，精英怪随后到，这是他亲口答应你的事")
                .weight(100)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 10);
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.msg("§c唤魔者决定亲自下场。这是你要求的，你亲口说的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_iron_golem_revolt", "铁傀儡决议", TrollTier.BRUTAL)
                .desc("铁傀儡开会一致通过了对你的处理方案：留你一命，方便下次再来")
                .weight(100)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                    ctx.damage(2.0f);
                    ctx.takeRandomItems(1);
                    ctx.msg("§6铁傀儡们开了个会，一致通过了对你的处理方案：留你一命，方便下次。");
                })
                .build());

        // ============================================================
        // 四、NIGHTMARE / APOCALYPSE / OBLIVION（残酷区）
        // ============================================================

        TrollEvents.reg(TrollEvent.of("trade_golem_execution", "村庄判决", TrollTier.OBLIVION)
                .desc("全村投票一致通过处决你，罪名是「存在感过强」，缓刑时间以秒计")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.title("§4§l村庄法庭", "§7全体村民一致通过：有罪");
                    ctx.msg("§7铁傀儡走上来了。判决书上写着你的名字，墨迹还没干。");
                    ctx.msg("§7负责宣读的村民清了清嗓子：§c罪名——存在感过强§7。");
                    ctx.actionBar("§4判决：立即执行");
                    ctx.later(40, () -> {
                        ctx.buryAlive();
                        ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.6f);
                        ctx.msg("§4铁傀儡没有给你辩解的机会。它说那不在它的工作范围内。");
                    });
                    ctx.later(90, () -> {
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 100, 3);
                        ctx.obliterate();
                        ctx.broadcast("§4村里贴出公告：那个多出来的村民，已经被处理掉了。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_apocalypse_wave", "袭击第七波", TrollTier.OBLIVION)
                .desc("公告先说袭击结束诱你放松，再更正说从没结束，然后兑现后半句")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e【袭击】第 7 波已结束，感谢你的配合。");
                    ctx.actionBar("§a袭击结束");
                    ctx.later(60, () -> {
                        ctx.fakeBroadcast("§4【更正】第 7 波没有结束。刚才那条是诱饵。");
                        ctx.freeze(120);
                        ctx.msg("§4你刚才放松的那一下，已经被记录在案。");
                    });
                    ctx.later(130, () -> {
                        for (int i = 0; i < 4; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                        }
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 10);
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 600, 1);
                        ctx.broadcast("§4袭击没有波次上限。公告从头到尾没有骗过你。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_ravager_stampede", "劫掠兽踩踏", TrollTier.OBLIVION)
                .desc("村民先赔你一袋绿宝石，再让劫掠兽连袋子带你的东西一起叼走")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 8);
                    ctx.msg("§a村民凑了一袋绿宝石，说是赔偿金。§7你居然信了。");
                    ctx.later(50, () -> {
                        ctx.takeRandomItems(9);
                        ctx.damage(3.0f);
                        ctx.fling(ctx.rf(-3f, 3f), 2.0, ctx.rf(-3f, 3f));
                        ctx.glitch(GlitchIds.SHAKY, 80, 2);
                        ctx.msg("§4劫掠兽把袋子叼走了，连同袋子里本来就属于你的东西。");
                        ctx.msg("§4它们不是来赔偿的，是来记账的。");
                    });
                    ctx.later(110, () -> {
                        ctx.lightningNear();
                        ctx.damage(3.0f);
                        ctx.broadcast("§4村口的蹄印一直延伸到你家门口，然后停住了。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_evoker_fang_real", "尖牙成真", TrollTier.APOCALYPSE)
                .desc("上次只是预热，这次你脚下的坑和尖牙一起到账")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.damage("magic", 12.0f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                    ctx.msg("§4这次尖牙是真的。唤魔者说上次只是预热。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_witch_curse_real", "女巫终身会员", TrollTier.OBLIVION)
                .desc("先给你一瓶解药，再把副作用按缓释方式记到你的生命条上")
                .weight(90)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.heal(6.0f);
                    ctx.msg("§a女巫递给你一瓶解药，说这次免费。§7你喝了。");
                    ctx.later(60, () -> {
                        ctx.damage("wither", 3.0f);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                        ctx.msg("§4解药是缓释型的。副作用从这一秒开始计费。");
                    });
                    ctx.later(150, () -> {
                        ctx.glitch(GlitchIds.NAUSEA_SHADER, 160, 3);
                        ctx.obliterate();
                        ctx.broadcast("§4女巫更新了会员条款：「终身」指的是你的，不是她的。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_village_massacre", "村庄翻脸", TrollTier.OBLIVION)
                .desc("村民委员会宣布你不再是本村村民，并顺手把你脚下的地也划走")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.broadcast("§7村民委员会宣布：从今天起，你不再是本村村民。");
                    ctx.msg("§7他们在你家门口竖了块牌子，正面写着§c「谢绝入内」§7。");
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 400, 1);
                    ctx.later(60, () -> {
                        ctx.stripAll();
                        for (int i = 0; i < 3; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                        }
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 9);
                        ctx.msg("§4牌子背面还有一行小字：§c「包括你的房子」§4。");
                    });
                    ctx.later(130, () -> {
                        ctx.buryAlive();
                        ctx.meteorShower(3, 14.0);
                        ctx.broadcast("§4村庄通过了新的土地法：你脚下的地，现在是他们的。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_all_gone", "资产清零", TrollTier.APOCALYPSE)
                .desc("绿宝石集体辞职，经验归零，几位同事被顺手牵走")
                .weight(90)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.dropRandom();
                    ctx.dropRandom();
                    ctx.dropRandom();
                    ctx.msg("§4你的绿宝石们集体辞职，并顺手带走了几位同事。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_hoard_lost", "资产保管", TrollTier.APOCALYPSE)
                .desc("银行系统故障，背包清空，虚空伤害作为手续费一并扣除")
                .weight(80)
                .cooldownSeconds(360)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.clearInventory();
                    ctx.msg("§4村庄银行系统故障，你的全部资产已被「安全保管」。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_bell_doom", "丧钟", TrollTier.OBLIVION)
                .desc("钟只响一次，但足够把你抬起来、转过去、扔出去，再替你把账结清")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.title("§4§l丧钟", "§7钟声只响一次，但你会记得很久");
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.msg("§7村里的钟自己响了。所有村民同时停下手里的事，转头看你。");
                    ctx.later(50, () -> {
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 120, 3);
                        ctx.setRotation(ctx.rf(-180f, 180f), ctx.rf(-60f, 60f));
                        ctx.scatterFar(100);
                        ctx.msg("§4钟声把你抬了起来，扔到村外。钟说你超重了。");
                    });
                    ctx.later(120, () -> {
                        ctx.freeze(200);
                        ctx.damage("wither", 3.0f);
                        ctx.obliterate();
                        ctx.broadcast("§4钟没有再响。它不需要第二下。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_doom", "限时优惠到货", TrollTier.OBLIVION)
                .desc("先塞给你四件好东西，再让你在一个从没来过的地方醒来，背包空着")
                .weight(80)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.msg("§a流浪商人喊道：清仓甩卖，先到先得！§7他朝你招了招手。");
                    ctx.give(ctx.pick(TrollPools.LOOT), 4);
                    ctx.later(50, () -> {
                        ctx.takeRandomItems(9);
                        ctx.scatterFar(140);
                        ctx.glitch(GlitchIds.WORLD_RELOAD, 100, 2);
                        ctx.msg("§4等你回过神，货没了，人没了，你站在一个从没到过的地方。");
                    });
                    ctx.later(120, () -> {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                        ctx.buryAlive();
                        ctx.broadcast("§4流浪商人留下的最后一句话：这里也清仓。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_cat_nightmare", "猫的噩梦", TrollTier.APOCALYPSE)
                .desc("村里的猫同时转头看你，黑屏和负面效果同时降临")
                .weight(80)
                .cooldownSeconds(280)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.glitch(GlitchIds.BLACKOUT, 80, 2);
                    ctx.randomHorrorSound();
                    ctx.msg("§4村里的猫全都转头看着你。它们同时转头。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_snow_golem_army", "雪傀儡军团", TrollTier.APOCALYPSE)
                .desc("军团成立，经验清零，第一个决议是把你围在中间")
                .weight(80)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                ctx.x() + ctx.rd(-5, 5), ctx.y() + 1, ctx.z() + ctx.rd(-5, 5));
                    }
                    ctx.msg("§4雪傀儡军团成立了，它们的第一个决议是砸你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_iron_golem_colossus", "巨型铁傀儡", TrollTier.NIGHTMARE)
                .desc("它变得非常大、非常生气，还提前清空了你的饥饿值当见面礼")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 5);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.5f);
                    ctx.msg("§4铁傀儡升级了。升级内容：变得非常大，且非常生气。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_endless", "没有尽头的袭击", TrollTier.NIGHTMARE)
                .desc("公告说本次袭击没有波次上限，然后它真的没有")
                .weight(70)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 10);
                    ctx.fakeBroadcast("§4【袭击】本次袭击没有波次上限。祝你好运。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_wither", "发黑的绿宝石", TrollTier.OBLIVION)
                .desc("一捧绿宝石在你手心里慢慢变黑，同时替你把手心和血条一起结清")
                .weight(80)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 6);
                    ctx.msg("§a你捡到一捧绿宝石。它们在你手心里还是亮的。");
                    ctx.later(60, () -> {
                        ctx.msg("§4绿宝石开始发黑。你的手心也是。");
                        ctx.damage("wither", 3.0f);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 500, 2);
                        ctx.takeRandomItems(6);
                    });
                    ctx.later(140, () -> {
                        ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 60);
                        ctx.obliterate();
                        ctx.broadcast("§4据村民说，那批绿宝石是上一个玩家留下的。他也没能还回来。");
                    });
                })
                .build());

        // ============================================================
        // 五、OBLIVION（绝望）
        // ============================================================

        TrollEvents.reg(TrollEvent.of("trade_village_annihilation", "村庄除名", TrollTier.OBLIVION)
                .desc("公告说本村十秒后除名，村民搬走的是你家的东西，第一个被划掉的是你")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.broadcast("§e【通知】本村将于 10 秒后从地图上除名。");
                    ctx.msg("§7村民开始搬家。他们搬走的，都是你家的东西。");
                    ctx.later(40, () -> {
                        ctx.stripAll();
                        ctx.meteorShower(6, 16.0);
                        ctx.glitch(GlitchIds.SCREEN_TEAR, 120, 3);
                        ctx.msg("§4除名流程开始了。名单上第一个被划掉的，是你。");
                    });
                    ctx.later(110, () -> {
                        ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 6.0f);
                        ctx.obliterate();
                        ctx.broadcast("§4地图更新完成：本村不存在。你也不在。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_raid_apocalypse", "袭击末世", TrollTier.OBLIVION)
                .desc("公告提供撤离通道，五秒后更正说通道就是袭击起点，全程都有全服伴奏")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.thunderstorm(2400);
                    ctx.fakeBroadcast("§e【袭击】本波次提供撤离通道，请前往村中心集合。");
                    ctx.later(50, () -> {
                        ctx.fakeBroadcast("§4【更正】村中心就是袭击起点。谢谢配合。");
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 120, 3);
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 800, 2);
                    });
                    ctx.later(120, () -> {
                        for (int i = 0; i < 4; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                        }
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 10);
                        ctx.obliterate();
                        ctx.broadcast("§4撤离通道关闭。它从来就没有打开过。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_evoker_apocalypse", "唤魔者搬家", TrollTier.OBLIVION)
                .desc("唤魔者把整座村子搬去另一个维度，你的新住址由他指定，行李不带")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.title("§4§l搬迁通知", "§7你的新住址：由唤魔者指定");
                    ctx.later(60, () -> {
                        ctx.scatterFar(160);
                        ctx.glitch(GlitchIds.WORLD_RELOAD, 140, 3);
                        ctx.msg("§4画面重载了一次。你被留在了新地址上。");
                    });
                    ctx.later(140, () -> {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                        ctx.buryAlive();
                        ctx.obliterate();
                        ctx.broadcast("§4唤魔者搬走了整个村子。你的那份行李，他没带。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_golem_apocalypse", "天空的判决", TrollTier.OBLIVION)
                .desc("铁傀儡抬头看了一眼天，天点了头，然后两次雷击把判决执行完毕")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.msg("§7铁傀儡抬头看了一眼天。天点了头。");
                    ctx.later(50, () -> {
                        ctx.lightning();
                        ctx.lightningNear();
                        ctx.damage("lightning", 3.0f);
                        ctx.setFire(4);
                        ctx.glitch(GlitchIds.FLASHBANG, 40, 3);
                        ctx.msg("§4第一道雷是通知，第二道还在路上。");
                    });
                    ctx.later(120, () -> {
                        ctx.lightning();
                        ctx.obliterate();
                        ctx.broadcast("§4天空的判决书只有一行：§c维持原判，立即执行§4。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_market_crash", "市场崩盘", TrollTier.APOCALYPSE)
                .desc("资产、地图和信心同时归零，画面还假装崩溃了一次给你看")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 100, 3);
                    ctx.clearInventory();
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 5.0f);
                    ctx.broadcast("§4经济系统崩盘：你的资产、地图和信心同时归零。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_emerald_singularity", "绿宝石奇点", TrollTier.OBLIVION)
                .desc("奇点先给你颁奖，奖品是你的一切，用来喂养它自己")
                .weight(30)
                .cooldownSeconds(720)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 10);
                    ctx.msg("§a一颗绿宝石在你手里开始发亮，像是在给你颁奖。");
                    ctx.later(50, () -> {
                        ctx.takeRandomItems(12);
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 100, 3);
                        ctx.freeze(120);
                        ctx.msg("§4奖项是：你的一切，用来喂养这颗奇点。");
                    });
                    ctx.later(130, () -> {
                        ctx.obliterate();
                        ctx.broadcast("§4绿宝石奇点闭合了。它打了个嗝，然后什么都没剩下。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("trade_wandering_apocalypse", "清仓大甩卖", TrollTier.OBLIVION)
                .desc("甩的是陨石，买单的是你，流浪商人从不打折，从不告诉你原价")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e【限时】流浪商人清仓甩卖，全场一折，仅此一次。");
                    ctx.give(ctx.pick(TrollPools.LOOT), 5);
                    ctx.later(50, () -> {
                        ctx.takeRandomItems(9);
                        ctx.meteorShower(6, 16.0);
                        ctx.msg("§4甩卖开始了。甩的是陨石，买单的是你。");
                    });
                    ctx.later(120, () -> {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                        ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 5.0f);
                        ctx.obliterate();
                        ctx.broadcast("§4流浪商人收摊了。他从不打折，只是从不告诉你原价。");
                    });
                })
                .build());
    }
}
