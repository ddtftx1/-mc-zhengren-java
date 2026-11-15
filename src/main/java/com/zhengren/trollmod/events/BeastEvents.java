package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;

/**
 * 【怪物军团】分类事件。
 * 负责整活：怪潮围城、精英小队、动物大迁徙、虫群、天降军团、怪物围观、军团合唱、
 * 末影人搬家、僵尸骑鸡、骷髅骑蜘蛛、苦力怕叠罗汉、刷怪笼效应、把玩家丢进怪堆……
 *
 * <p>全部通过 {@code ctx} + {@code TrollPools} 组合实现，不直接使用 World / Entity 子类。</p>
 */
public final class BeastEvents {

    private BeastEvents() {
    }

    public static void register() {
        TrollEvents.category("怪物军团");

        // ============================================================
        // PRANK —— 笑一下就过去
        // ============================================================

        TrollEvents.reg(TrollEvent.of("beast_animal_parade", "动物大迁徙", TrollTier.BRUTAL)
                .desc("一口气刷 12~20 只农场动物从玩家身边路过")
                .weight(150)
                .cooldownSeconds(50)
                .when(ctx -> ctx.nearbyMobCount(24) < 30)
                .run(ctx -> {
                    int n = ctx.ri(12, 20);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                ctx.x() + ctx.ri(-12, 12), ctx.y() + 1, ctx.z() + ctx.ri(-12, 12));
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.msg("§7整片草原搬家了，路过时顺手把你列为观察对象。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_chicken_cavalry", "鸡骑兵连", TrollTier.BRUTAL)
                .desc("强制骑上鸡，并刷几只自称骑兵的僵尸当部下")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.mount(EntityType.CHICKEN);
                    int n = ctx.ri(2, 4);
                    for (int i = 0; i < n; i++) {
                        var e = ctx.spawnHostileNear(EntityType.ZOMBIE, 8);
                        ctx.giveMobCustomName(e, "§c僵尸骑兵（坐骑落在半路了）");
                    }
                    ctx.msg("§7你被空降任命为鸡骑士长，部下当场表示不服。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_tiny_swarm", "小东西入侵", TrollTier.BRUTAL)
                .desc("刷 8~14 只极小极烦的生物（蝙蝠/蠹虫/蝌蚪那一挂）糊在脸上")
                .weight(140)
                .cooldownSeconds(45)
                .run(ctx -> {
                    int n = ctx.ri(8, 14);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING),
                                ctx.x() + ctx.ri(-3, 3), ctx.y() + ctx.ri(0, 3), ctx.z() + ctx.ri(-3, 3));
                    }
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.8f, 1.7f);
                    ctx.msg("§7小东西们一致决定：以后住在你身上。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_bat_storm", "蝙蝠风暴", TrollTier.BRUTAL)
                .desc("头顶刷 10~16 只蝙蝠，形成一场小型生物风暴")
                .weight(130)
                .cooldownSeconds(50)
                .run(ctx -> {
                    int n = ctx.ri(10, 16);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(EntityType.BAT,
                                ctx.x() + ctx.ri(-4, 4), ctx.y() + ctx.ri(1, 5), ctx.z() + ctx.ri(-4, 4));
                    }
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.2f, 1.5f);
                    ctx.msg("§7一整个蝙蝠窝从天上掉下来，正好落在你头上。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_pet_visit", "等人认领的生物", TrollTier.BRUTAL)
                .desc("刷三只被编号的烦人生物围着玩家坐下")
                .weight(120)
                .cooldownSeconds(55)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        var e = ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                ctx.x() + ctx.ri(-2, 2), ctx.y() + 1, ctx.z() + ctx.ri(-2, 2));
                        ctx.giveMobCustomName(e, "§e等人认领 " + (i + 1) + " 号");
                    }
                    ctx.msg("§7它们围着你坐下，默认你就是主人了。恭喜领养。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_animal_rain", "动物降水", TrollTier.ANNOYING)
                .desc("从 8~14 格高空下 6~10 只动物")
                .weight(125)
                .cooldownSeconds(60)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    int n = ctx.ri(6, 10);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                ctx.x() + ctx.ri(-6, 6), ctx.y() + ctx.ri(8, 14), ctx.z() + ctx.ri(-6, 6));
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.warn("气象台提醒：本地区有动物降水，出门请注意头顶。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_cow_stampede", "牛群冲刺", TrollTier.BRUTAL)
                .desc("刷十只被编号的动物冲过来，并把玩家顶飞一下")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    for (int i = 0; i < 10; i++) {
                        var e = ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS),
                                ctx.x() + ctx.ri(-8, 8), ctx.y() + 1, ctx.z() + ctx.ri(-8, 8));
                        ctx.giveMobCustomName(e, "§7冲刺中 " + (i + 1) + " 号");
                    }
                    ctx.fling(ctx.rd(-0.8, 0.8), 0.35, ctx.rd(-0.8, 0.8));
                    ctx.msg("§7牛群表示：你站的这个位置非常挡道。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_cat_supervisors", "喵星监工组", TrollTier.BRUTAL)
                .desc("刷三只被任命为监工的动物，盯着玩家干活")
                .weight(115)
                .cooldownSeconds(55)
                .run(ctx -> {
                    String[] titles = {"§7总监工", "§7包工头", "§7实习监工"};
                    for (int i = 0; i < titles.length; i++) {
                        var e = ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                ctx.x() + ctx.ri(-3, 3), ctx.y() + 1, ctx.z() + ctx.ri(-3, 3));
                        ctx.giveMobCustomName(e, titles[i]);
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.6f);
                    ctx.msg("§7它们不是来陪你的，是来验收工程质量的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_slime_mount", "黏液代步", TrollTier.ANNOYING)
                .desc("强制把玩家骑到一只史莱姆背上")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.mount(EntityType.SLIME);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§7交通工具已到，弹性极佳，刹车欠奉。");
                })
                .build());

        // ============================================================
        // ANNOYING —— 皱眉级别
        // ============================================================

        TrollEvents.reg(TrollEvent.of("beast_monster_choir", "军团合唱", TrollTier.BRUTAL)
                .desc("四只怪被编成声部，同时以不同音高开嗓")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        var e = ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                        ctx.giveMobCustomName(e, "§e第 " + (i + 1) + " 声部");
                    }
                    for (int i = 0; i < 4; i++) {
                        ctx.sound(ctx.pick(TrollPools.HORROR), 0.8f, 1.0f + i * 0.2f);
                    }
                    ctx.warn("它们开嗓了，曲目是《你今晚别想睡》。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_silent_watchers", "沉默的观众席", TrollTier.NIGHTMARE)
                .desc("她们不说话，只是站成一圈看着你。沉默比动手更像判决")
                .weight(110)
                .cooldownSeconds(70)
                .when(ctx -> ctx.nearbyMobCount(20) < 12)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    int n = 6;
                    for (int i = 0; i < n; i++) {
                        double a = Math.PI * 2.0 * i / n;
                        var e = ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                ctx.x() + Math.cos(a) * 3.5, ctx.y(), ctx.z() + Math.sin(a) * 3.5);
                        ctx.giveMobCustomName(e, "§7观众席");
                    }
                    ctx.msg("§7它们不打你，只是站着看你——这比打你难受多了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_unseen_stalkers", "看不见的追踪者", TrollTier.BRUTAL)
                .desc("黑暗 + 三只名字被打码的怪贴上来")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.effect(StatusEffects.DARKNESS, 100, 0);
                    for (int i = 0; i < 3; i++) {
                        var e = ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                        ctx.giveMobCustomName(e, "§8§k追踪者§r§8 " + (i + 1) + " 号");
                    }
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.msg("§7你看不见它们，但它们绝对看得见你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_item_thief", "顺手牵羊的那位", TrollTier.NIGHTMARE)
                .desc("它们贴着你的后背站好，等你转身。它们知道你会转")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    var e = ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.giveMobCustomName(e, "§6顺手牵羊的那位");
                    boolean got = ctx.takeRandomItem();
                    ctx.msg(got
                            ? "§7它从你包里挑走了最「值钱」的那件。你希望不是钻石。"
                            : "§7它翻了半天，发现你比它还穷，只好失望地走了。");
                    ctx.fakeChat("小偷", "§7下次记得带点值钱的出来。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_door_blockers", "门口被堵死了", TrollTier.BRUTAL)
                .desc("贴身刷 6 只怪，宣称要把门口排成两排")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    }
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.9f, 0.9f);
                    ctx.msg("§7它们在你门口排成两排，谁也别想出去（包括你自己）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_name_roster", "军团名册", TrollTier.BRUTAL)
                .desc("刷三只怪并赋予与玩家住所有关的细思极恐名字")
                .weight(100)
                .cooldownSeconds(80)
                .when(ctx -> ctx.nearbyMobCount(24) < 16)
                .run(ctx -> {
                    String[] names = {
                            "§c今晚睡你床底那只",
                            "§4知道你家在哪只",
                            "§8一直在门口等你的",
                            "§c你上一任房东"
                    };
                    for (int i = 0; i < 3; i++) {
                        var e = ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                        ctx.giveMobCustomName(e, ctx.pick(names));
                    }
                    ctx.sys("已新增 3 条怪物记录，备注栏写着你的名字。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_enderman_moving", "末影人搬家", TrollTier.ANNOYING)
                .desc("刷 3~5 只末影人，宣称它们搬到玩家旁边定居")
                .weight(90)
                .cooldownSeconds(100)
                .when(ctx -> !ctx.isInWater())
                .run(ctx -> {
                    int n = ctx.ri(3, 5);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(EntityType.ENDERMAN,
                                ctx.x() + ctx.ri(-6, 6), ctx.y(), ctx.z() + ctx.ri(-6, 6));
                    }
                    ctx.sound(SoundEvents.ENTITY_ENDERMAN_SCREAM, 0.9f, 1.2f);
                    ctx.msg("§7末影人全家搬到了你旁边。房东是你，房租没谈拢。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_slime_party", "黏液派对", TrollTier.BRUTAL)
                .desc("刷 5~8 只史莱姆，让地板变得有弹性")
                .weight(105)
                .cooldownSeconds(70)
                .run(ctx -> {
                    int n = ctx.ri(5, 8);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileNear(EntityType.SLIME, 6);
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§7地板开始有弹性了。你的膝盖准备好了吗？");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_bee_airforce", "蜜蜂空军", TrollTier.BRUTAL)
                .desc("刷 8~12 只蜜蜂组成空军编队")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    int n = ctx.ri(8, 12);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(EntityType.BEE,
                                ctx.x() + ctx.ri(-4, 4), ctx.y() + ctx.ri(1, 4), ctx.z() + ctx.ri(-4, 4));
                    }
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.warn("空军已升空。目标：你手里的花，以及你的脸。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_armor_theft", "装备「借」走了", TrollTier.BRUTAL)
                .desc("打乱装备槽并丢出手持物，刷一只宣称穿走装备的怪")
                .weight(95)
                .cooldownSeconds(150)
                .when(ctx -> !ctx.invEmpty())
                .run(ctx -> {
                    ctx.swapEquipmentRandomly();
                    ctx.dropHeld();
                    var e = ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.giveMobCustomName(e, "§6穿着你装备的那位");
                    ctx.warn("它把你的装备「借」走了，还留了一句谢谢。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_silverfish_walls", "墙里有虫", TrollTier.BRUTAL)
                .desc("在背后刷 4~7 只蠹虫，模拟墙体被掏空的声音")
                .weight(105)
                .cooldownSeconds(80)
                .run(ctx -> {
                    int n = ctx.ri(4, 7);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileBehind(EntityType.SILVERFISH);
                    }
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.8f, 1.6f);
                    ctx.msg("§7你家的承重墙刚刚打了个喷嚏。");
                })
                .build());

        // ============================================================
        // BRUTAL —— 开始骂人
        // ============================================================

        TrollEvents.reg(TrollEvent.of("beast_horde_siege", "怪潮围城", TrollTier.NIGHTMARE)
                .desc("它们从四面合拢，把「家」这个字从你脑子里一点一点挤出去")
                .weight(70)
                .cooldownSeconds(200)
                .when(ctx -> ctx.nearbyMobCount(24) < 18)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    int n = ctx.ri(7, 11);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.title("§4§l怪 潮", "§7围城开始，城门就是你");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_skyfall_legion", "空降军团", TrollTier.NIGHTMARE)
                .desc("它们不是掉下来的，是被放下来的，落点围着你排成一个圆")
                .weight(65)
                .cooldownSeconds(210)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    int n = ctx.ri(4, 6);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                ctx.x() + ctx.ri(-4, 4), ctx.y() + ctx.ri(8, 12), ctx.z() + ctx.ri(-4, 4));
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.warn("空降兵已就位，落点由你决定（你决定不了）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_pit_transport", "传送到怪堆中心", TrollTier.APOCALYPSE)
                .desc("它把你送到了一个陌生的地方，那里已经站满了等你的人")
                .weight(55)
                .cooldownSeconds(240)
                .when(ctx -> ctx.health() > 10.0f)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.scatter(25);
                    for (int i = 0; i < 8; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    }
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7欢迎来到中心位置");
                    ctx.warn("整人大师给你选了个热闹的地址，不退不换。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_legion_formation", "军团编队", TrollTier.APOCALYPSE)
                .desc("方阵排开，逐一点名。被点到的那个人，就是你")
                .weight(60)
                .cooldownSeconds(220)
                .when(ctx -> ctx.nearbyMobCount(20) < 14)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    String[] ranks = {"§c第一排", "§c第二排", "§4第三排"};
                    for (int r = 0; r < ranks.length; r++) {
                        for (int i = -2; i <= 2; i++) {
                            var e = ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                    ctx.x() + i * 1.5, ctx.y(), ctx.z() + 4 + r * 2);
                            ctx.giveMobCustomName(e, ranks[r] + " 第 " + (i + 3) + " 位");
                        }
                    }
                    ctx.title("§4§l点 名", "§7被点到的人请出列");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_creeper_totem", "叠罗汉", TrollTier.NIGHTMARE)
                .desc("三只苦力怕垒成品字形，引信同步，而你刚好站在正下方")
                .weight(60)
                .cooldownSeconds(200)
                .when(ctx -> ctx.nearbyMobCount(16) < 12)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    for (int i = 0; i < 3; i++) {
                        var e = ctx.spawnAny(EntityType.CREEPER, ctx.x(), ctx.y() + i, ctx.z() + 3);
                        ctx.giveMobCustomName(e, "§a叠罗汉第 " + (i + 1) + " 层");
                    }
                    ctx.sound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
                    ctx.sound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 1.25f);
                    ctx.warn("三只苦力怕正在搭人梯，你最好别站在下面看。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_spider_jockey", "骷髅骑蜘蛛", TrollTier.NIGHTMARE)
                .desc("它们被拼在一起从来不是为了骑乘，是为了让你分不清该先打哪一个")
                .weight(60)
                .cooldownSeconds(220)
                .run(ctx -> {
                    for (int i = 0; i < 2; i++) {
                        double ox = ctx.x() + ctx.ri(-5, 5);
                        double oz = ctx.z() + ctx.ri(-5, 5);
                        ctx.giveMobCustomName(ctx.spawnAny(EntityType.SPIDER, ox, ctx.y(), oz),
                                "§8坐骑：蜘蛛");
                        ctx.giveMobCustomName(ctx.spawnAny(EntityType.SKELETON, ox, ctx.y() + 1, oz),
                                "§7骑手：骷髅（站位版）");
                    }
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.9f);
                    ctx.msg("§7两组骑兵已经组建完毕，缰绳忘了发。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_phantom_dive", "幻翼俯冲", TrollTier.NIGHTMARE)
                .desc("它们不睡觉，是因为你在下面。每一次俯冲，都只为确认你还在")
                .weight(60)
                .cooldownSeconds(200)
                .when(ctx -> !ctx.isInWater())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    int n = ctx.ri(3, 5);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(EntityType.PHANTOM,
                                ctx.x() + ctx.ri(-5, 5), ctx.y() + ctx.ri(6, 10), ctx.z() + ctx.ri(-5, 5));
                    }
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 1.1f);
                    ctx.msg("§7它们俯冲下来，只为确认你是不是真的那么菜。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_proj_barrage", "投射物覆盖射击", TrollTier.APOCALYPSE)
                .desc("刷一队远程怪，再从头顶丢 10 个投射物")
                .weight(55)
                .cooldownSeconds(200)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    int n = ctx.ri(3, 5);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 9);
                    }
                    for (int i = 0; i < 10; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE),
                                ctx.x() + ctx.ri(-7, 7), ctx.y() + ctx.ri(6, 11), ctx.z() + ctx.ri(-7, 7));
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.warn("远程部队表示：你跑动的时候比较好瞄准。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_pet_betrayal", "宠物集体叛变", TrollTier.BRUTAL)
                .desc("把三只生物改名成叛变宠物，并让玩家挨一下")
                .weight(60)
                .cooldownSeconds(240)
                .run(ctx -> {
                    String[] names = {
                            "§c你养的第一只",
                            "§c你养的第二只",
                            "§c第三只你其实忘了喂"
                    };
                    for (int i = 0; i < names.length; i++) {
                        var e = ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                ctx.x() + ctx.ri(-3, 3), ctx.y() + 1, ctx.z() + ctx.ri(-3, 3));
                        ctx.giveMobCustomName(e, names[i]);
                    }
                    ctx.damage(2.0f);
                    ctx.fakeChat("你的宠物", "§c我们开会决定了：你出局。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_spawner_effect", "刷怪笼效应", TrollTier.NIGHTMARE)
                .desc("出货声每两秒一次。等你反应过来，那节奏是在数你，不是数怪")
                .weight(55)
                .cooldownSeconds(240)
                .when(ctx -> ctx.nearbyMobCount(24) < 20)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    int n = ctx.ri(8, 12);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10);
                    }
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.warn("每两秒响一次的那种动静，别找了——刷怪笼在你脑子里。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_nether_legion", "下界军团", TrollTier.APOCALYPSE)
                .desc("刷一队敌对生物并把玩家点着三秒")
                .weight(60)
                .cooldownSeconds(220)
                .when(ctx -> !ctx.isInWater() && !ctx.isInLava())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    int n = ctx.ri(5, 8);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.setFire(3);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.warn("一支带着硫磺味的军团到场了，附赠你身上三秒的火。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_raid_squad", "劫掠小队路过", TrollTier.APOCALYPSE)
                .desc("他们不是来抢东西的。他们只是需要一个理由，站在你面前，看着你")
                .weight(58)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    int n = ctx.ri(5, 7);
                    for (int i = 0; i < n; i++) {
                        var e = ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 9);
                        ctx.giveMobCustomName(e, "§c劫掠队员家属");
                    }
                    ctx.title("§6§l袭击开始", "§7目标：你（和你的那一间房）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_mount_roulette", "坐骑盲盒", TrollTier.APOCALYPSE)
                .desc("坐骑由它挑。它挑的标准只有一个：哪一种能让你死得最不像意外")
                .weight(45)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.mount(ctx.pick(TrollPools.RIDEABLE));
                    ctx.fling(ctx.rd(-1.2, 1.2), 0.6, ctx.rd(-1.2, 1.2));
                    ctx.msg("§7盲盒已开。抽到什么全看命，模组不负责售后。");
                })
                .build());

        // ============================================================
        // NIGHTMARE —— 想砸键盘
        // ============================================================

        TrollEvents.reg(TrollEvent.of("beast_elite_squad", "精英小队", TrollTier.APOCALYPSE)
                .desc("从精英池连刷三只 BOSS 级生物并编号")
                .weight(25)
                .cooldownSeconds(400)
                .when(ctx -> ctx.nearbyMobCount(32) < 12)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    for (int i = 0; i < 3; i++) {
                        var e = ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 12);
                        ctx.giveMobCustomName(e, "§4§l精英队员 " + (i + 1) + " 号");
                    }
                    ctx.sound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0f, 1.3f);
                    ctx.title("§4§l精英小队", "§7他们说你欠他们一句道歉");
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_ravager_walk", "劫掠兽散步", TrollTier.APOCALYPSE)
                .desc("刷两只劫掠兽并起名成正在散步的邻居")
                .weight(30)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    for (int i = 0; i < 2; i++) {
                        var e = ctx.spawnHostileNear(EntityType.RAVAGER, 10);
                        ctx.giveMobCustomName(e, "§4散步中的劫掠兽");
                    }
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.warn("两只劫掠兽出门散步，而你是路上那滩东西。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_warden_patrol", "监守者在巡逻", TrollTier.OBLIVION)
                .desc("它不是在找你。它只是沿着你的脚印，一遍一遍地巡逻，直到你不得不动")
                .weight(25)
                .cooldownSeconds(480)
                .when(ctx -> ctx.nearbyMobCount(40) < 12)
                .run(ctx -> {
                    ctx.sound(SoundEvents.ENTITY_WARDEN_ROAR, 1.0f, 0.7f);
                    ctx.effect(StatusEffects.DARKNESS, 200, 1);
                    ctx.msg("§8它没有追你。§7它在你的房子外面，走了一圈，又走了一圈。");
                    ctx.later(80, () -> {
                        var e = ctx.spawnAny(EntityType.WARDEN,
                                ctx.x() + ctx.ri(-14, 14), ctx.y(), ctx.z() + ctx.ri(-14, 14));
                        ctx.giveMobCustomName(e, "§1§l巡逻中的");
                        ctx.warn("§1它这次停在你门口了。§8（它认得你的脚步声。）");
                    });
                    ctx.later(160, () -> {
                        ctx.effectRandom(1200, 3);
                        ctx.buryAlive();
                        ctx.obliterate();
                    });
                    ctx.addRage(9.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_enderman_swarm", "末影人包围网", TrollTier.NIGHTMARE)
                .desc("刷 4~6 只锁定玩家的末影人")
                .weight(35)
                .cooldownSeconds(360)
                .when(ctx -> ctx.nearbyMobCount(24) < 12)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    int n = ctx.ri(4, 6);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileNear(EntityType.ENDERMAN, 9);
                    }
                    ctx.sound(SoundEvents.ENTITY_ENDERMAN_SCREAM, 1.0f, 0.9f);
                    ctx.warn("你刚刚不小心看了其中一只一眼。现在它们全都看过你了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_blaze_firestorm", "烈焰人火刑", TrollTier.APOCALYPSE)
                .desc("刷三只烈焰人、四个投射物并点燃玩家五秒")
                .weight(30)
                .cooldownSeconds(380)
                .when(ctx -> !ctx.isInWater() && !ctx.isInLava())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(EntityType.BLAZE, 8);
                    }
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE),
                                ctx.x() + ctx.ri(-5, 5), ctx.y() + ctx.ri(6, 10), ctx.z() + ctx.ri(-5, 5));
                    }
                    ctx.setFire(5);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.warn("火刑已经排好队，法官、行刑人和柴火都是同一批人。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_ghast_flotilla", "恶魂舰队", TrollTier.APOCALYPSE)
                .desc("两艘会哭的船在你头顶巡航。它们的礼炮，是用来确认你坐标的")
                .weight(28)
                .cooldownSeconds(400)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    for (int i = 0; i < 2; i++) {
                        var e = ctx.spawnAny(EntityType.GHAST,
                                ctx.x() + ctx.ri(-8, 8), ctx.y() + ctx.ri(12, 18), ctx.z() + ctx.ri(-8, 8));
                        ctx.giveMobCustomName(e, "§c舰队旗舰 " + (i + 1));
                    }
                    ctx.sound(SoundEvents.ENTITY_GHAST_SCREAM, 1.0f, 0.9f);
                    ctx.warn("两艘「空中哭丧舰」正在你头顶巡航，火球是它们的礼炮。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_shell_encircle", "贝壳包围圈", TrollTier.APOCALYPSE)
                .desc("贴身刷 6 只怪并给玩家挂上减速")
                .weight(30)
                .cooldownSeconds(360)
                .when(ctx -> ctx.nearbyMobCount(20) < 14)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    }
                    ctx.effect(StatusEffects.SLOWNESS, 200, 1);
                    ctx.msg("§7六只怪把你围成一个圈，还贴心地给你上了「别想跑」减速。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_creeper_guard", "苦力怕仪仗队", TrollTier.APOCALYPSE)
                .desc("把 6 只苦力怕排成一圈并同时播放引信声")
                .weight(32)
                .cooldownSeconds(360)
                .when(ctx -> ctx.nearbyMobCount(20) < 12)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    int n = 6;
                    for (int i = 0; i < n; i++) {
                        double a = Math.PI * 2.0 * i / n;
                        ctx.spawnAny(EntityType.CREEPER,
                                ctx.x() + Math.cos(a) * 3.0, ctx.y(), ctx.z() + Math.sin(a) * 3.0);
                    }
                    ctx.sound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
                    ctx.title("§2§l嘘——", "§7六根引信，一个你");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_giant_awakening", "远处站起来的影子", TrollTier.OBLIVION)
                .desc("远处有个东西站起来了。它不需要跑——它只要走，而你已经没有路了")
                .weight(25)
                .cooldownSeconds(500)
                .when(ctx -> ctx.nearbyMobCount(40) < 10)
                .run(ctx -> {
                    ctx.later(70, () -> {
                        var e = ctx.spawnAny(ctx.pick(TrollPools.BOSS),
                                ctx.x() + ctx.ri(-14, 14), ctx.y(), ctx.z() + ctx.ri(-14, 14));
                        ctx.giveMobCustomName(e, "§4§l巨大的那个");
                        ctx.sound(ctx.pick(TrollPools.HORROR), 0.8f, 0.5f);
                        ctx.title("§4§l它站起来了", "§7从现在开始，别回头看");
                    });
                    ctx.later(150, () -> {
                        ctx.effect(StatusEffects.DARKNESS, 160, 0);
                        ctx.voidPit(2, 8);
                        ctx.meteorShower(4, 16.0);
                        ctx.stripAll();
                        ctx.obliterate();
                    });
                    ctx.addRage(9.0);
                })
                .build());

        // ============================================================
        // APOCALYPSE —— 直接退出游戏
        // ============================================================

        TrollEvents.reg(TrollEvent.of("beast_legion_apocalypse", "军团末日", TrollTier.OBLIVION)
                .desc("它不再隐藏了。整支军团同时出现，并且开始一个一个喊出你们的名字（每局一次）")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .when(ctx -> ctx.nearbyMobCount(40) < 20)
                .run(ctx -> {
                    int n = ctx.ri(10, 14);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10);
                    }
                    for (int i = 0; i < 3; i++) {
                        var e = ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 14);
                        ctx.giveMobCustomName(e, "§4§l军团指挥官");
                    }
                    ctx.thunderstorm(800);
                    ctx.effectAll(StatusEffects.DARKNESS, 200, 0);
                    ctx.titleToAll("§4§l军 团", "§7投降按钮已经拆掉了");
                    ctx.sendToAll("§8[§c服务器§8] §c" + ctx.playerName() + " §c的末日已排期，请勿迟到。");
                    ctx.later(100, () -> {
                        ctx.lightningNear();
                        ctx.title("§4§l点名", "§c" + ctx.playerName() + "§7，出列。");
                        ctx.obliterate();
                    });
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_wither_twin", "凋灵与它的朋友", TrollTier.OBLIVION)
                .desc("主犯先到，从犯随后。它们说好一起等你把这场劫难走完（每局一次）")
                .weight(22)
                .cooldownSeconds(900)
                .once()
                .when(ctx -> ctx.health() > 10.0f)
                .run(ctx -> {
                    ctx.later(70, () -> {
                        var w = ctx.spawnAny(EntityType.WITHER,
                                ctx.x() + ctx.ri(-10, 10), ctx.y() + 7, ctx.z() + ctx.ri(-10, 10));
                        ctx.giveMobCustomName(w, "§8§l主犯");
                        ctx.sound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0f, 0.7f);
                        ctx.effect(StatusEffects.WITHER, 120, 0);
                        ctx.title("§0§l凋 零", "§7你惹到的不止一个");
                    });
                    ctx.later(150, () -> {
                        var mate = ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 14);
                        ctx.giveMobCustomName(mate, "§4§l从犯");
                        ctx.stripAll();
                        ctx.obliterate();
                    });
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_tnt_stampede", "爆炸军团冲锋", TrollTier.APOCALYPSE)
                .desc("六枚引信同时点燃，四只怪负责确保你留在原地，把整场看完")
                .weight(22)
                .cooldownSeconds(700)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnAny(EntityType.TNT,
                                ctx.x() + ctx.ri(-5, 5), ctx.y() + ctx.ri(14, 22), ctx.z() + ctx.ri(-5, 5));
                    }
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    }
                    ctx.title("§c§l冲 锋", "§7跑吧，别回头看爆炸");
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 1.0f);
                    ctx.warn("六枚 TNT 加四只怪，他们分工明确——你负责死。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_endless_night", "无尽长夜", TrollTier.OBLIVION)
                .desc("太阳不会再升起。你和它们都清楚这一点，所以它们并不着急（每局一次）")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.timeOfDay(18000L);
                    ctx.thunderstorm(1600);
                    ctx.effectAll(StatusEffects.DARKNESS, 300, 0);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 100, 2);
                    int n = ctx.ri(8, 12);
                    for (int i = 0; i < n; i++) {
                        ctx.spawnAny(EntityType.PHANTOM,
                                ctx.x() + ctx.ri(-8, 8), ctx.y() + ctx.ri(10, 18), ctx.z() + ctx.ri(-8, 8));
                    }
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 9);
                    }
                    ctx.title("§0§l长 夜", "§7天空没有一点要亮的意思");
                    ctx.msg("§8（你已经不记得太阳是什么时候落下去的了。）");
                    ctx.later(200, () -> {
                        ctx.timeOfDay(18000L);
                        ctx.buryAlive();
                        ctx.obliterate();
                        ctx.msg("§4天亮了。§8（亮的是你的复活点。）");
                    });
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("beast_player_hunt", "全员追杀", TrollTier.OBLIVION)
                .desc("他们不急着杀你。他们想先看看你能跑多久——答案是：跑不了多久")
                .weight(20)
                .cooldownSeconds(750)
                .when(ctx -> ctx.health() > 12.0f && ctx.isOnGround())
                .run(ctx -> {
                    ctx.title("§4§l猎 杀", "§7规则：你跑，它们追");
                    ctx.lift(14);
                    for (int i = 0; i < 10; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.HOSTILE),
                                ctx.x() + ctx.ri(-5, 5), ctx.y() + 1, ctx.z() + ctx.ri(-5, 5));
                    }
                    for (int i = 0; i < 2; i++) {
                        var e = ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 12);
                        ctx.giveMobCustomName(e, "§4§l猎手");
                    }
                    ctx.fling(0.0, -0.6, 0.0);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.9f);
                    ctx.later(90, () -> {
                        ctx.effectRandom(600, 2);
                        ctx.takeRandomItems(3);
                        ctx.warn("§c第一轮结束。§7它们捡走了你掉的东西，很有耐心。");
                    });
                    ctx.later(180, () -> {
                        ctx.warn("§4狩猎结束。§8（真正的猎人从不急着收网。）");
                        ctx.obliterate();
                    });
                    ctx.addRage(8);
                })
                .build());
    }
}
