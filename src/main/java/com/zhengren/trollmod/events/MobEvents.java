package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;

/**
 * 【生物与惊吓】分类事件。
 * 负责整活：背后刷怪、怪围一圈、怪从天而降、精英追杀者、幻翼空袭、史莱姆雨、
 * 鸡群检阅、蝙蝠冲脸、怪物改名、怪物「穿走」玩家装备、把玩家丢进怪堆……
 *
 * <p>所有整活都通过 {@code ctx} + {@code TrollPools} 完成，不直接碰 World / Entity 子类。</p>
 */
public final class MobEvents {

    private MobEvents() {
    }

    public static void register() {
        TrollEvents.category("生物与惊吓");

        // ============================================================
        // 序章 —— 声音先到：你听见了，但它还没出现
        // ============================================================

        TrollEvents.reg(TrollEvent.of("mob_hiss_behind", "身后的引信", TrollTier.ANNOYING)
                .desc("在玩家背后播放苦力怕引信声，然后……什么都没有")
                .weight(160)
                .cooldownSeconds(35)
                .run(ctx -> {
                    ctx.sound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.2f, 1.0f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.msg("§7嘶——");
                    ctx.actionBar("§8你确定那只是音效吗？");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_bat_surprise", "蝙蝠冲脸", TrollTier.BRUTAL)
                .desc("在玩家脸前刷一小群蝙蝠并播放阴森音效")
                .weight(150)
                .cooldownSeconds(40)
                .run(ctx -> {
                    int n = ctx.ri(6, 9);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(EntityType.BAT, ctx.x() + ctx.ri(-2, 2), ctx.y() + ctx.ri(1, 3), ctx.z() + ctx.ri(-2, 2));
                    }
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.5f);
                    ctx.msg("§7一群蝙蝠从你裤裆底下飞过去了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_chicken_parade", "鸡群检阅", TrollTier.BRUTAL)
                .desc("刷一堆带编号的鸡，宣布玩家已被鸡群收编")
                .weight(140)
                .cooldownSeconds(45)
                .run(ctx -> {
                    int n = ctx.ri(6, 10);
                    for (int i = 0; i < n; i++) {
                        var e = ctx.spawnAny(EntityType.CHICKEN, ctx.x() + ctx.ri(-4, 4), ctx.y() + 1, ctx.z() + ctx.ri(-4, 4));
                        ctx.giveMobCustomName(e, "§e鸡你太美 #" + (i + 1));
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.msg("§7鸡群已认定你为同族。恭喜，你以后要下蛋了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_judgement_beast", "审判官驾到", TrollTier.ANNOYING)
                .desc("刷一只被临时任命为审判官的动物，它审视你并当场宣布结论")
                .weight(120)
                .cooldownSeconds(45)
                .run(ctx -> {
                    var judge = ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                            ctx.x() + ctx.ri(-2, 2), ctx.y() + 1, ctx.z() + ctx.ri(-2, 2));
                    ctx.giveMobCustomName(judge, "§7审判官：本庭第一席");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 1.6f);
                    ctx.msg("§7它看了你一眼，然后当庭宣判：操作不合格。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_silverfish_itch", "鞋子里的东西", TrollTier.ANNOYING)
                .desc("在玩家背后刷几只蠹虫，配一句让人挠腿的台词")
                .weight(110)
                .cooldownSeconds(40)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    int n = ctx.ri(2, 4);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileBehind(EntityType.SILVERFISH);
                    }
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.7f, 1.8f);
                    ctx.msg("§7有东西钻进了你的鞋子里。别脱，会尴尬。");
                })
                .build());

        // ============================================================
        // 加压 —— 它们开始注意你：围住、盯住、改名
        // ============================================================

        TrollEvents.reg(TrollEvent.of("mob_bee_escort", "蜜蜂护卫队", TrollTier.BRUTAL)
                .desc("刷一整队编号蜜蜂贴身护航，附带绝望提示")
                .weight(130)
                .cooldownSeconds(50)
                .run(ctx -> {
                    int n = ctx.ri(5, 8);
                    for (int i = 0; i < n; i++) {
                        var e = ctx.spawnAny(EntityType.BEE, ctx.x() + ctx.ri(-3, 3), ctx.y() + ctx.ri(1, 3), ctx.z() + ctx.ri(-3, 3));
                        ctx.giveMobCustomName(e, "§e护卫 " + (i + 1) + " 号");
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.warn("蜜蜂护卫队已就位。它们一致认为你很甜。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_choir_of_annoyance", "噪音合唱团", TrollTier.BRUTAL)
                .desc("刷一队被命名为声部的烦人生物，并伪造一条鹦鹉聊天")
                .weight(110)
                .cooldownSeconds(55)
                .run(ctx -> {
                    int n = ctx.ri(4, 6);
                    for (int i = 0; i < n; i++) {
                        var e = ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                ctx.x() + ctx.ri(-3, 3), ctx.y() + ctx.ri(1, 2), ctx.z() + ctx.ri(-3, 3));
                        ctx.giveMobCustomName(e, "§e合唱团第 " + (i + 1) + " 声部");
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.fakeChat("合唱团指挥", "§7曲目：《你今晚别想睡》");
                    ctx.msg("§7它们开口了，而且没有一个在调上。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_wolf_stare", "它们不打你，只看着", TrollTier.NIGHTMARE)
                .desc("三只生物把你围住，一言不发地盯着——凋零已经在替你计时")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    for (int i = 0; i < 3; i++) {
                        var e = ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                ctx.x() + ctx.ri(-3, 3), ctx.y() + 1, ctx.z() + ctx.ri(-3, 3));
                        ctx.giveMobCustomName(e, "§7盯——");
                    }
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.msg("§7它们不说话，就看着你。你千万别先眨眼。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_zombie_welcome", "它记得你的名字", TrollTier.NIGHTMARE)
                .desc("背后出现一只锁定你的东西，名字却热情得发毛；两格物品已经不在你包里了")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    var e = ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.giveMobCustomName(e, "§a想要抱抱的你朋友");
                    ctx.msg("§7有个新朋友想跟你贴贴，它已经在你背后站了三秒了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_face_to_face", "贴脸服务", TrollTier.BRUTAL)
                .desc("在玩家正前方不到一格的地方刷一只怪，零距离对视")
                .weight(115)
                .cooldownSeconds(70)
                .run(ctx -> {
                    var yaw = Math.toRadians(ctx.player().getYaw());
                    var e = ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                            ctx.x() - Math.sin(yaw) * 0.8, ctx.y(), ctx.z() + Math.cos(yaw) * 0.8);
                    ctx.giveMobCustomName(e, "§c贴脸服务专员");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 0.9f, 1.2f);
                    ctx.actionBar("§8你们的距离是 0.8 格，请保持微笑。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_named_nightmare", "怪物名册更新", TrollTier.BRUTAL)
                .desc("刷三只怪并赋予细思极恐的自定义名字")
                .weight(90)
                .cooldownSeconds(70)
                .when(ctx -> ctx.nearbyMobCount(24) < 16)
                .run(ctx -> {
                    String[] names = {
                            "§c它记得你",
                            "§4你的名字在它嘴里",
                            "§8刚才那个声音",
                            "§c别回头看的那只",
                            "§4住在你床底下的"
                    };
                    for (int i = 0; i < 3; i++) {
                        var e = ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                        ctx.giveMobCustomName(e, ctx.pick(names));
                    }
                    ctx.sound(ctx.pick(TrollPools.HORROR), 0.6f, 1.2f);
                    ctx.sys("怪物名册已更新：3 条新记录，收件人是你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_junk_gift", "拾荒者的馈赠", TrollTier.BRUTAL)
                .desc("刷一只自称拾荒者的怪，硬往背包里塞腐烂食物和垃圾")
                .weight(100)
                .cooldownSeconds(50)
                .run(ctx -> {
                    var e = ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.giveMobCustomName(e, "§6拾荒者：送礼上门");
                    ctx.give(Items.ROTTEN_FLESH, ctx.ri(3, 9));
                    ctx.giveRandomJunk();
                    ctx.msg("§7它坚持要把自己的收藏送给你，不收就追着你塞。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_phantom_scout", "头顶的侦察兵", TrollTier.BRUTAL)
                .desc("在玩家上方刷一小队幻翼，配一句完全不合时宜的台词")
                .weight(85)
                .cooldownSeconds(90)
                .when(ctx -> !ctx.isInWater() && ctx.health() > 4.0f)
                .run(ctx -> {
                    int n = ctx.ri(2, 4);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(EntityType.PHANTOM, ctx.x() + ctx.ri(-5, 5), ctx.y() + ctx.ri(8, 14), ctx.z() + ctx.ri(-5, 5));
                    }
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 1.0f);
                    ctx.actionBar("§8「今晚的月亮真好看。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_armor_thief", "装备被「收下」了", TrollTier.BRUTAL)
                .desc("打乱玩家装备槽并丢出手持物，同时刷一只宣称穿走装备的怪")
                .weight(95)
                .cooldownSeconds(120)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.swapEquipmentRandomly();
                    ctx.dropHeld();
                    var e = ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.giveMobCustomName(e, "§6穿着你装备的那位");
                    ctx.warn("它说你穿得没它好看，所以装备归它了。别问，问就是合身。");
                })
                .build());

        // ============================================================
        // 合围 —— 数量即恐惧：一圈、一队、一群
        // ============================================================

        TrollEvents.reg(TrollEvent.of("mob_zombie_siege", "收网", TrollTier.NIGHTMARE)
                .desc("工具先断一件，然后五到九只锁定你的东西从四面合拢——标题只是通知你结果")
                .weight(70)
                .cooldownSeconds(180)
                .when(ctx -> ctx.nearbyMobCount(24) < 18)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    int n = ctx.ri(5, 9);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    }
                    ctx.title("§4§l围 城", "§7你被包围了，恭喜获奖");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_circle_judgement", "审判席", TrollTier.NIGHTMARE)
                .desc("八只怪精确围成一圈，负面效果先落在你身上——接下来它们要讨论的就是你")
                .weight(60)
                .cooldownSeconds(200)
                .when(ctx -> ctx.nearbyMobCount(20) < 14)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    int n = 8;
                    for (int i = 0; i < n; i++) {
                        double a = Math.PI * 2.0 * i / n;
                        var e = ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                ctx.x() + Math.cos(a) * 4.5, ctx.y(), ctx.z() + Math.sin(a) * 4.5);
                        ctx.giveMobCustomName(e, "§c围观群众 " + (i + 1) + " 号");
                    }
                    ctx.msg("§7它们围成一圈，开始对你的操作水平进行学术讨论。");
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_skyfall_horde", "从上面来", TrollTier.NIGHTMARE)
                .desc("头顶七到十一格同时开闸，饥饿清零——你只能用脸接住它们")
                .weight(65)
                .cooldownSeconds(210)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    int n = ctx.ri(3, 5);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                ctx.x() + ctx.ri(-3, 3), ctx.y() + ctx.ri(7, 11), ctx.z() + ctx.ri(-3, 3));
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.warn("天上掉下来几个「大礼包」，建议用脸接。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_mob_pile", "人堆", TrollTier.APOCALYPSE)
                .desc("抬到十二格空中，下面已经站满了等你的东西——落地那一下，它们会同时抬头")
                .weight(50)
                .cooldownSeconds(240)
                .when(ctx -> ctx.health() > 10.0f && ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7给你找了个热闹的地方");
                    ctx.lift(12);
                    for (int i = 0; i < 8; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                ctx.x() + ctx.ri(-4, 4), ctx.y(), ctx.z() + ctx.ri(-4, 4));
                    }
                    ctx.fling(ctx.rd(-0.3, 0.3), -0.5, ctx.rd(-0.3, 0.3));
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.warn("整人大师认为你该社交了。落地时请注意脚下有没有人。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_skeleton_volley", "弹幕", TrollTier.NIGHTMARE)
                .desc("地面出现敌人，头顶同时落下八个投射物——没有哪个方向是安全的")
                .weight(55)
                .cooldownSeconds(200)
                .run(ctx -> {
                    int n = ctx.ri(3, 5);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10);
                    }
                    for (int i = 0; i < 8; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE),
                                ctx.x() + ctx.ri(-6, 6), ctx.y() + ctx.ri(6, 10), ctx.z() + ctx.ri(-6, 6));
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.warn("空袭警报拉响了，建议原地不动（别信）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_pet_betrayal", "它换了主人", TrollTier.NIGHTMARE)
                .desc("你养过的东西被改名成叛徒，然后亲手给你一下——聊天记录里只留下它的理由")
                .weight(60)
                .cooldownSeconds(240)
                .run(ctx -> {
                    for (int i = 0; i < 2; i++) {
                        var e = ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                ctx.x() + ctx.ri(-3, 3), ctx.y() + 1, ctx.z() + ctx.ri(-3, 3));
                        ctx.giveMobCustomName(e, "§c你养的那只（已叛变）");
                    }
                    ctx.damage(2.0f);
                    ctx.fakeChat("你养的那只", "§c我换主人了，你上顿没喂我。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_rideable_chaos", "被甩下来", TrollTier.NIGHTMARE)
                .desc("三只坐骑出现在周围，然后一致决定把你抛向半空")
                .weight(45)
                .cooldownSeconds(180)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.RIDEABLE),
                                ctx.x() + ctx.ri(-4, 4), ctx.y() + 1, ctx.z() + ctx.ri(-4, 4));
                    }
                    ctx.fling(ctx.rd(-1.5, 1.5), ctx.rd(0.4, 0.9), ctx.rd(-1.5, 1.5));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§7坐骑们一致表示：你太重了，自己飞吧。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_slime_rain", "黏液覆盖", TrollTier.APOCALYPSE)
                .desc("六到十只史莱姆从头顶砸下，负面效果随之而来——脚下每一块地都变得不可信")
                .weight(60)
                .cooldownSeconds(200)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    int n = ctx.ri(6, 10);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(EntityType.SLIME,
                                ctx.x() + ctx.ri(-6, 6), ctx.y() + ctx.ri(6, 12), ctx.z() + ctx.ri(-6, 6));
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                    ctx.msg("§7天降黏液。你脚下这片地板从此不再可信。");
                })
                .build());

        // ============================================================
        // 猎杀 —— 你才是被观察的对象
        // ============================================================

        TrollEvents.reg(TrollEvent.of("mob_elite_hunter", "名单上的编号", TrollTier.OBLIVION)
                .desc("它先让你听见凋灵的出场音，再报出你的编号。被追杀这件事，从名字被念出来的那一刻就已经成立了。")
                .weight(30)
                .cooldownSeconds(300)
                .when(ctx -> ctx.nearbyMobCount(32) < 10)
                .run(ctx -> {
                    ctx.effectRandom(1200, 3);
                    ctx.sound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0f, 1.4f);
                    ctx.title("§4§l名单已更新", "§7编号正在分配");
                    ctx.later(70, () -> {
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.meteorShower(4, 16.0);
                        var e = ctx.spawnHostileBehind(ctx.pick(TrollPools.BOSS));
                        ctx.giveMobCustomName(e, "§4§l追杀者-§c" + ctx.ri(1, 99));
                        ctx.msg("§8编号念完了。现在它知道该去哪里找你。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_warden_knock", "它数到了三", TrollTier.APOCALYPSE)
                .desc("十格内出现监守者，附赠失明与一次虚空伤害——墙里的呼吸声越来越近")
                .weight(25)
                .cooldownSeconds(420)
                .when(ctx -> ctx.nearbyMobCount(40) < 12)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    var e = ctx.spawnAny(EntityType.WARDEN,
                            ctx.x() + ctx.ri(-10, 10), ctx.y(), ctx.z() + ctx.ri(-10, 10));
                    ctx.giveMobCustomName(e, "§1§l敲门的");
                    ctx.sound(SoundEvents.ENTITY_WARDEN_ROAR, 1.0f, 0.8f);
                    ctx.effect(StatusEffects.DARKNESS, 200, 0);
                    ctx.msg("§8有人在你的墙里呼吸，而且它数到了三。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_blaze_inferno", "三台烤架", TrollTier.APOCALYPSE)
                .desc("三只烈焰人包住你并点燃——陨石雨同时落下，退路也一起燃烧")
                .weight(30)
                .cooldownSeconds(380)
                .when(ctx -> !ctx.isInWater() && !ctx.isInLava())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(EntityType.BLAZE, 8);
                    }
                    ctx.setFire(4);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.warn("三台移动烧烤架认为你是今天的食材，而且是全熟那种。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_ghast_above", "哭声在上方", TrollTier.NIGHTMARE)
                .desc("恶魂悬在头顶，四发投射物随后落下——那个哭声，也许一直是你的")
                .weight(28)
                .cooldownSeconds(400)
                .run(ctx -> {
                    var e = ctx.spawnAny(EntityType.GHAST,
                            ctx.x() + ctx.ri(-6, 6), ctx.y() + ctx.ri(10, 16), ctx.z() + ctx.ri(-6, 6));
                    ctx.giveMobCustomName(e, "§c哭丧的");
                    ctx.sound(SoundEvents.ENTITY_GHAST_SCREAM, 1.0f, 0.9f);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE),
                                ctx.x() + ctx.ri(-5, 5), ctx.y() + ctx.ri(8, 12), ctx.z() + ctx.ri(-5, 5));
                    }
                    ctx.msg("§7头顶有人在哭。别找了，哭的可能是你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_invisible_stalker", "你看不见它", TrollTier.NIGHTMARE)
                .desc("黑暗、视角被拧、背后出现一只名字被乱码吃掉的东西——它在等你转身")
                .weight(35)
                .cooldownSeconds(360)
                .run(ctx -> {
                    ctx.effect(StatusEffects.DARKNESS, 120, 0);
                    ctx.spin(ctx.rf(-180f, 180f));
                    var e = ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.giveMobCustomName(e, "§8§k偷看者§r§8 1 号");
                    ctx.glitch(GlitchIds.SHAKY, 60, 2);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.2f, 0.7f);
                    ctx.msg("§7你确定刚才那个位置，之前没有东西吗？");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_obsidian_peepshow", "展柜", TrollTier.APOCALYPSE)
                .desc("黑曜石把你封进一个没有门的展柜，六只怪买票入场——你是展品")
                .weight(25)
                .cooldownSeconds(480)
                .when(ctx -> ctx.isOnGround() && !ctx.isInLava() && !ctx.isInWater())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.cageWith(Blocks.OBSIDIAN);
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                ctx.x() + ctx.ri(-5, 5), ctx.y(), ctx.z() + ctx.ri(-5, 5));
                    }
                    ctx.title("§8§l个人展出", "§7观众已经买票入场了");
                    ctx.warn("恭喜，你被「保护」进了一个黑曜石展柜里，门外全是在看你的怪。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_giant_shadow", "它一直没有动", TrollTier.OBLIVION)
                .desc("远处的影子比树高。它不追你、不叫、不攻击，只是站着，看你自己把退路走完。")
                .weight(30)
                .cooldownSeconds(500)
                .when(ctx -> ctx.nearbyMobCount(40) < 10)
                .run(ctx -> {
                    ctx.effect(StatusEffects.DARKNESS, 120, 0);
                    var e = ctx.spawnAny(ctx.pick(TrollPools.BOSS),
                            ctx.x() + ctx.ri(-12, 12), ctx.y(), ctx.z() + ctx.ri(-12, 12));
                    ctx.giveMobCustomName(e, "§4巨大的影子");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 0.8f, 0.6f);
                    ctx.msg("§8远处有个影子比树还高。它没动。");
                    ctx.later(120, () -> {
                        ctx.voidPit(2, 16);
                        ctx.buryAlive();
                        ctx.effectRandom(1200, 3);
                        ctx.obliterate();
                        ctx.msg("§0它还是没动。动的是地面。");
                    });
                })
                .build());

        // ============================================================
        // 降临 —— 传说级的东西踏进了你的世界
        // ============================================================

        TrollEvents.reg(TrollEvent.of("mob_apocalypse_horde", "尸潮不会结束", TrollTier.OBLIVION)
                .desc("它先告诉你这是最后一波，再把最后一波无限延长——围住你的东西会一轮接一轮，直到你不再数它们。")
                .weight(20)
                .cooldownSeconds(600)
                .once()
                .when(ctx -> ctx.nearbyMobCount(40) < 20)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e[世界事件] 最后一波尸潮即将刷新，结束后恢复和平。");
                    ctx.later(60, () -> {
                        ctx.voidPit(2, 16);
                        ctx.stripAll();
                        ctx.obliterate();
                        ctx.meteorShower(4, 16.0);
                        ctx.thunderstorm(1200);
                        for (int wave = 0; wave < 3; wave++) {
                            final int delay = 60 + wave * 60;
                            ctx.later(delay, () -> {
                                for (int i = 0; i < 4; i++) {
                                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 9);
                                }
                            });
                        }
                        var boss = ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 12);
                        ctx.giveMobCustomName(boss, "§4§l尸潮指挥官");
                        ctx.lightningNear();
                        ctx.title("§4§l最后一波", "§7（这是第三「最后一波」）");
                        ctx.broadcast("§8[§c服务器§8] §c" + ctx.playerName() + " §c已被尸潮锁定，祝他好运。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_wither_arrival", "名字叫结局", TrollTier.APOCALYPSE)
                .desc("头顶出现凋灵，失明随之落下——它留给你三秒，用来想清楚自己叫什么")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .when(ctx -> ctx.health() > 10.0f)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    var e = ctx.spawnAny(EntityType.WITHER,
                            ctx.x() + ctx.ri(-8, 8), ctx.y() + 6, ctx.z() + ctx.ri(-8, 8));
                    ctx.giveMobCustomName(e, "§8§l你的结局");
                    ctx.sound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0f, 0.7f);
                    ctx.effect(StatusEffects.BLINDNESS, 100, 0);
                    ctx.title("§0§l凋 零", "§7你还有三秒的思考时间");
                    ctx.msg("§7它来了，带着三颗头和一整个下午的耐心。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_tnt_skyfall", "抬头看", TrollTier.OBLIVION)
                .desc("先用一条广播让你抬头，再在头顶十八格挂上引信。你有五秒决定跑去哪——而它早就知道你会往哪跑。")
                .weight(22)
                .cooldownSeconds(600)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.fakeBroadcast("§c[空袭预警] 请保持原地不动，救援即将抵达。");
                    ctx.ok("§a救援即将抵达。");
                    ctx.later(80, () -> {
                        ctx.msg("§7救援到了。");
                        ctx.buryAlive();
                        ctx.effectRandom(1200, 3);
                        ctx.voidPit(2, 16);
                        ctx.meteorShower(4, 16.0);
                        for (int i = 0; i < 4; i++) {
                            ctx.spawnAny(EntityType.TNT,
                                    ctx.x() + ctx.ri(-4, 4), ctx.y() + ctx.ri(18, 26), ctx.z() + ctx.ri(-4, 4));
                        }
                        ctx.title("§c§l空 袭", "§7它们落下的位置，正好是你跑的方向");
                        ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                        ctx.warn("§4四枚引信在你头顶。逃吧，随便往哪。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_void_stare", "被送上去看清一切", TrollTier.OBLIVION)
                .desc("它先夺走你的装备，再把你抬到十二格高空；视野被反转，你能看见每一只正在靠近的东西，却动不了。")
                .weight(20)
                .cooldownSeconds(700)
                .once()
                .when(ctx -> ctx.health() > 12.0f && ctx.isOnGround())
                .run(ctx -> {
                    ctx.stripAll();
                    ctx.glitch(GlitchIds.INVERT_PITCH, 120, 2);
                    ctx.lift(12);
                    ctx.effect(StatusEffects.NAUSEA, 120, 1);
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7你能看清下面每一张脸");
                    ctx.later(80, () -> {
                        ctx.meteorShower(4, 16.0);
                        ctx.buryAlive();
                        ctx.effectRandom(1200, 3);
                        for (int i = 0; i < 4; i++) {
                            ctx.spawnAny(EntityType.PHANTOM,
                                    ctx.x() + ctx.ri(-6, 6), ctx.y() + ctx.ri(2, 6), ctx.z() + ctx.ri(-6, 6));
                        }
                        for (int i = 0; i < 3; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                        }
                        ctx.warn("§4落地之前，它们会先认识你。");
                    });
                })
                .build());
    }
}
