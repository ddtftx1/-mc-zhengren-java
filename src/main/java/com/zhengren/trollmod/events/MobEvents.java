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
        // PRANK —— 笑一下就过去
        // ============================================================

        TrollEvents.reg(TrollEvent.of("mob_hiss_behind", "身后的引信", TrollTier.PRANK)
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

        TrollEvents.reg(TrollEvent.of("mob_bat_surprise", "蝙蝠冲脸", TrollTier.PRANK)
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

        TrollEvents.reg(TrollEvent.of("mob_chicken_parade", "鸡群检阅", TrollTier.PRANK)
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

        TrollEvents.reg(TrollEvent.of("mob_judgement_beast", "审判官驾到", TrollTier.PRANK)
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

        TrollEvents.reg(TrollEvent.of("mob_silverfish_itch", "鞋子里的东西", TrollTier.PRANK)
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
        // ANNOYING —— 皱眉级别
        // ============================================================

        TrollEvents.reg(TrollEvent.of("mob_bee_escort", "蜜蜂护卫队", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("mob_choir_of_annoyance", "噪音合唱团", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("mob_wolf_stare", "被盯上的那个人", TrollTier.ANNOYING)
                .desc("刷三只生物围着玩家盯视，只起名不出声")
                .weight(100)
                .cooldownSeconds(60)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        var e = ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                ctx.x() + ctx.ri(-3, 3), ctx.y() + 1, ctx.z() + ctx.ri(-3, 3));
                        ctx.giveMobCustomName(e, "§7盯——");
                    }
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.msg("§7它们不说话，就看着你。你千万别先眨眼。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_zombie_welcome", "热情的新朋友", TrollTier.ANNOYING)
                .desc("在玩家背后刷一只锁定玩家的怪，并起一个热情到发毛的名字")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    var e = ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.giveMobCustomName(e, "§a想要抱抱的你朋友");
                    ctx.msg("§7有个新朋友想跟你贴贴，它已经在你背后站了三秒了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_face_to_face", "贴脸服务", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("mob_named_nightmare", "怪物名册更新", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("mob_junk_gift", "拾荒者的馈赠", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("mob_phantom_scout", "头顶的侦察兵", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("mob_armor_thief", "装备被「收下」了", TrollTier.ANNOYING)
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
        // BRUTAL —— 开始骂人
        // ============================================================

        TrollEvents.reg(TrollEvent.of("mob_zombie_siege", "僵尸围城", TrollTier.BRUTAL)
                .desc("一次性在玩家周围刷 5~9 只锁定玩家的敌对生物并上标题")
                .weight(70)
                .cooldownSeconds(180)
                .when(ctx -> ctx.nearbyMobCount(24) < 18)
                .run(ctx -> {
                    int n = ctx.ri(5, 9);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    }
                    ctx.title("§4§l围 城", "§7你被包围了，恭喜获奖");
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_circle_judgement", "围观群众", TrollTier.BRUTAL)
                .desc("用三角函数把 8 只怪精确摆成一个圈，然后开始评头论足")
                .weight(60)
                .cooldownSeconds(200)
                .when(ctx -> ctx.nearbyMobCount(20) < 14)
                .run(ctx -> {
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

        TrollEvents.reg(TrollEvent.of("mob_skyfall_horde", "天降大礼包", TrollTier.BRUTAL)
                .desc("在玩家上空 7~11 格刷 3~5 只怪，让它们自由落体到脸上")
                .weight(65)
                .cooldownSeconds(210)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    int n = ctx.ri(3, 5);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                ctx.x() + ctx.ri(-3, 3), ctx.y() + ctx.ri(7, 11), ctx.z() + ctx.ri(-3, 3));
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.warn("天上掉下来几个「大礼包」，建议用脸接。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_mob_pile", "送你去看热闹", TrollTier.BRUTAL)
                .desc("把玩家抬起 12 格并在空中刷满怪，一起自由落体进怪堆")
                .weight(50)
                .cooldownSeconds(240)
                .when(ctx -> ctx.health() > 10.0f && ctx.isOnGround())
                .run(ctx -> {
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

        TrollEvents.reg(TrollEvent.of("mob_skeleton_volley", "远程部队已就位", TrollTier.BRUTAL)
                .desc("刷一队敌对生物并在头顶丢 8 个投射物下来")
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

        TrollEvents.reg(TrollEvent.of("mob_pet_betrayal", "宠物叛变", TrollTier.BRUTAL)
                .desc("把玩家身边的生物改名成叛变宠物，并让玩家挨一下")
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

        TrollEvents.reg(TrollEvent.of("mob_rideable_chaos", "坐骑不载你，载你上天", TrollTier.BRUTAL)
                .desc("刷一批坐骑并把玩家甩出去")
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

        TrollEvents.reg(TrollEvent.of("mob_slime_rain", "史莱姆雨", TrollTier.BRUTAL)
                .desc("在头顶 6~12 格刷 6~10 只史莱姆，让地板变得不礼貌")
                .weight(60)
                .cooldownSeconds(200)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
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
        // NIGHTMARE —— 想砸键盘
        // ============================================================

        TrollEvents.reg(TrollEvent.of("mob_elite_hunter", "精英追杀者", TrollTier.NIGHTMARE)
                .desc("在背后刷一只精英怪并冠以编号「追杀者」，附赠凋灵出场音")
                .weight(30)
                .cooldownSeconds(300)
                .when(ctx -> ctx.nearbyMobCount(32) < 10)
                .run(ctx -> {
                    var e = ctx.spawnHostileBehind(ctx.pick(TrollPools.BOSS));
                    ctx.giveMobCustomName(e, "§4§l追杀者-§c" + ctx.ri(1, 99));
                    ctx.sound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0f, 1.4f);
                    ctx.title("§4§l它来了", "§7你的名字已经在名单上了");
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_warden_knock", "墙里有人呼吸", TrollTier.NIGHTMARE)
                .desc("在 10 格内刷一只监守者，配咆哮与黑暗效果")
                .weight(25)
                .cooldownSeconds(420)
                .when(ctx -> ctx.nearbyMobCount(40) < 12)
                .run(ctx -> {
                    var e = ctx.spawnAny(EntityType.WARDEN,
                            ctx.x() + ctx.ri(-10, 10), ctx.y(), ctx.z() + ctx.ri(-10, 10));
                    ctx.giveMobCustomName(e, "§1§l敲门的");
                    ctx.sound(SoundEvents.ENTITY_WARDEN_ROAR, 1.0f, 0.8f);
                    ctx.effect(StatusEffects.DARKNESS, 200, 0);
                    ctx.msg("§8有人在你的墙里呼吸，而且它数到了三。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_blaze_inferno", "三台移动烧烤架", TrollTier.NIGHTMARE)
                .desc("刷三只烈焰人包围玩家并点燃玩家")
                .weight(30)
                .cooldownSeconds(380)
                .when(ctx -> !ctx.isInWater() && !ctx.isInLava())
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(EntityType.BLAZE, 8);
                    }
                    ctx.setFire(4);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.warn("三台移动烧烤架认为你是今天的食材，而且是全熟那种。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_ghast_above", "头顶的哭声", TrollTier.NIGHTMARE)
                .desc("在头顶刷一只恶魂并丢 4 个投射物下来")
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

        TrollEvents.reg(TrollEvent.of("mob_invisible_stalker", "看不见的偷看者", TrollTier.NIGHTMARE)
                .desc("黑暗 + 甩视角 + 在背后刷一只名字被乱码覆盖的怪")
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

        TrollEvents.reg(TrollEvent.of("mob_obsidian_peepshow", "个人展出", TrollTier.NIGHTMARE)
                .desc("用黑曜石把玩家罩起来，再让 6 只怪在外面围观")
                .weight(25)
                .cooldownSeconds(480)
                .when(ctx -> ctx.isOnGround() && !ctx.isInLava() && !ctx.isInWater())
                .run(ctx -> {
                    ctx.cageWith(Blocks.OBSIDIAN);
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                ctx.x() + ctx.ri(-5, 5), ctx.y(), ctx.z() + ctx.ri(-5, 5));
                    }
                    ctx.title("§8§l个人展出", "§7观众已经买票入场了");
                    ctx.warn("恭喜，你被「保护」进了一个黑曜石展柜里，门外全是在看你的怪。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_giant_shadow", "远处的巨大剪影", TrollTier.NIGHTMARE)
                .desc("在 12 格外刷一只精英怪并起名「巨大的影子」，配黑暗")
                .weight(30)
                .cooldownSeconds(500)
                .when(ctx -> ctx.nearbyMobCount(40) < 10)
                .run(ctx -> {
                    var e = ctx.spawnAny(ctx.pick(TrollPools.BOSS),
                            ctx.x() + ctx.ri(-12, 12), ctx.y(), ctx.z() + ctx.ri(-12, 12));
                    ctx.giveMobCustomName(e, "§4巨大的影子");
                    ctx.effect(StatusEffects.DARKNESS, 120, 0);
                    ctx.sound(ctx.pick(TrollPools.HORROR), 0.8f, 0.6f);
                    ctx.msg("§8远处有个影子比树还高。它没动，只是看你。");
                })
                .build());

        // ============================================================
        // APOCALYPSE —— 直接退出游戏
        // ============================================================

        TrollEvents.reg(TrollEvent.of("mob_apocalypse_horde", "终极尸潮", TrollTier.APOCALYPSE)
                .desc("一次性刷 8~12 只敌对生物 + 2 只带名精英，并召唤雷暴（每局一次）")
                .weight(20)
                .cooldownSeconds(600)
                .once()
                .when(ctx -> ctx.nearbyMobCount(40) < 20)
                .run(ctx -> {
                    int n = ctx.ri(8, 12);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 9);
                    }
                    for (int i = 0; i < 2; i++) {
                        var e = ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 12);
                        ctx.giveMobCustomName(e, "§4§l尸潮指挥官");
                    }
                    ctx.thunderstorm(600);
                    ctx.lightningNear();
                    ctx.title("§4§l尸 潮", "§7这次真的没有下一波了");
                    ctx.broadcast("§8[§c服务器§8] §c" + ctx.playerName() + " §c已被尸潮锁定，祝他好运。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_wither_arrival", "凋零的问候", TrollTier.APOCALYPSE)
                .desc("在头顶刷一只凋灵并施加失明（每局一次）")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .when(ctx -> ctx.health() > 10.0f)
                .run(ctx -> {
                    var e = ctx.spawnAny(EntityType.WITHER,
                            ctx.x() + ctx.ri(-8, 8), ctx.y() + 6, ctx.z() + ctx.ri(-8, 8));
                    ctx.giveMobCustomName(e, "§8§l你的结局");
                    ctx.sound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0f, 0.7f);
                    ctx.effect(StatusEffects.BLINDNESS, 100, 0);
                    ctx.title("§0§l凋 零", "§7你还有三秒的思考时间");
                    ctx.msg("§7它来了，带着三颗头和一整个下午的耐心。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_tnt_skyfall", "空袭警报", TrollTier.APOCALYPSE)
                .desc("在头顶 18~26 格刷 4 个 TNT，让玩家自己决定往哪跑")
                .weight(22)
                .cooldownSeconds(600)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnAny(EntityType.TNT,
                                ctx.x() + ctx.ri(-4, 4), ctx.y() + ctx.ri(18, 26), ctx.z() + ctx.ri(-4, 4));
                    }
                    ctx.title("§c§l空 袭", "§7抬头，别低头");
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.warn("四枚 TNT 正在自由落体。你有大约五秒时间思考人生和跑位。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mob_void_stare", "送你上天看风景", TrollTier.APOCALYPSE)
                .desc("把玩家抬到 12 格高空并塞满幻翼与怪，附赠视角反转（每局一次）")
                .weight(20)
                .cooldownSeconds(700)
                .once()
                .when(ctx -> ctx.health() > 12.0f && ctx.isOnGround())
                .run(ctx -> {
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7抬头看，风景不错");
                    ctx.lift(12);
                    ctx.effect(StatusEffects.NAUSEA, 120, 1);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnAny(EntityType.PHANTOM,
                                ctx.x() + ctx.ri(-6, 6), ctx.y() + ctx.ri(2, 6), ctx.z() + ctx.ri(-6, 6));
                    }
                    for (int i = 0; i < 2; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                ctx.x() + ctx.ri(-4, 4), ctx.y() + 1, ctx.z() + ctx.ri(-4, 4));
                    }
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 1.1f);
                    ctx.glitch(GlitchIds.INVERT_PITCH, 120, 2);
                    ctx.warn("整人大师把你连人带尊严一起送上了天。落地前请想好遗言。");
                })
                .build());
    }
}
