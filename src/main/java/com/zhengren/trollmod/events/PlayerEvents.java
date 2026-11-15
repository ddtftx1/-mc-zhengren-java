package com.zhengren.trollmod.events;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundEvents;

/**
 * 【玩家状态】分类事件。
 * 负责整活：药水效果、血量、饥饿、经验、视角、位移、着火、溺水、窒息、脱水、坠落与终极击杀。
 *
 * <p>全部只用 {@code ctx.*} + {@code TrollPools.*}，外加契约白名单内的
 * {@code StatusEffects} / {@code Blocks} / {@code SoundEvents} / {@code GlitchIds}。</p>
 */
public final class PlayerEvents {

    private PlayerEvents() {
    }

    public static void register() {
        TrollEvents.category("玩家状态");

        // ==================================================================
        // 序章 —— 身体的小差错：声响、嗝、假升级
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("player_fake_creeper", "§a身后的嘶嘶声", TrollTier.BRUTAL)
                .desc("贴脸播放苦力怕引信声，然后什么都不发生")
                .weight(160)
                .cooldownSeconds(45)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.soundSelf(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
                    ctx.actionBar("§7嘶————————");
                    ctx.msg("§7（你僵住了 40 tick）");
                    ctx.msg("§f……§o什么都没有发生。你的反应已经被记录在案。");
                    ctx.addRage(1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_bad_burp", "消化不良", TrollTier.ANNOYING)
                .desc("打个嗝，顺便嘲一下玩家的伙食")
                .weight(150)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, ctx.rf(0.8f, 1.3f));
                    ctx.spin(ctx.rf(-12f, 12f));
                    ctx.msg("§7你打了一个嗝，味道非常熟悉——就是你刚才舍不得吃的那口金苹果。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_fake_levelup", "假升级", TrollTier.ANNOYING)
                .desc("播放升级特效和标题，但经验一点不给")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.playLevelUp();
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 30);
                    ctx.title("§a§l等级提升！", "§7Lv.??? → Lv.!!!");
                    ctx.msg("§7恭喜你升到了 §cLv.???§7！……§o骗你的，经验一点没给，还倒欠 3 级。");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_slippery_start", "脚底一滑", TrollTier.BRUTAL)
                .desc("给玩家一个莫名其妙的小趔趄")
                .weight(120)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, ctx.rf(1.0f, 1.4f));
                    ctx.fling(ctx.rd(-0.18, 0.18), 0.35, ctx.rd(-0.18, 0.18));
                    ctx.msg("§7你踩到了一坨……说不清是什么的东西。总之很滑。");
                })
                .build());

        // ==================================================================
        // 加压 —— 状态开始背叛你：晕、盲、饿、霉
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("player_nausea_spin", "晕头转向", TrollTier.BRUTAL)
                .desc("长时间反胃，并把视角随机拧一下")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.effect(StatusEffects.NAUSEA, 20 * 15, 0);
                    ctx.spin(ctx.rf(-45f, 45f));
                    ctx.msg("§7你的视野开始打转，胃里像养了一条末影龙。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_blindness_flash", "闪光弹", TrollTier.BRUTAL)
                .desc("闪白 + 失明，附赠一声巨响")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FLASHBANG, 12, 1);
                    ctx.soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                    ctx.effect(StatusEffects.BLINDNESS, 20 * 6, 0);
                    ctx.warn("§c有人往你脸上扔了个闪光弹。是谁？不知道。反正不是我们。");
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_lead_boots", "灌了铅的腿", TrollTier.NIGHTMARE)
                .desc("缓慢与挖掘疲劳同时上身——你连转身都像在搬自己")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.effect(StatusEffects.SLOWNESS, 20 * 20, 1);
                    ctx.effect(StatusEffects.MINING_FATIGUE, 20 * 20, 1);
                    ctx.msg("§7你的靴子不知何时被换成了两块铅。恭喜，走路也是健身。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_noodle_arms", "面条手臂", TrollTier.BRUTAL)
                .desc("虚弱 + 挖掘疲劳，挖一块石头挖到天亮")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.effect(StatusEffects.WEAKNESS, 20 * 18, 1);
                    ctx.effect(StatusEffects.MINING_FATIGUE, 20 * 18, 0);
                    ctx.msg("§7你的胳膊变成了两条煮烂的面条。加油，慢慢挖，我们不急。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_darkness_crawl", "伸手不见五指", TrollTier.BRUTAL)
                .desc("挂上黑暗效果并配一声阴森响动")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.effect(StatusEffects.DARKNESS, 20 * 12, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.msg("§8光照等级：0。你的勇气等级：也是 0。");
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_hunger_drain", "胃变成黑洞", TrollTier.BRUTAL)
                .desc("饥饿效果并清空饥饿值")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.effect(StatusEffects.HUNGER, 20 * 25, 2);
                    ctx.starve();
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.warn("§c你的胃突然变成了黑洞，连回忆都被消化了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_unluck_curse", "今天不宜开箱", TrollTier.BRUTAL)
                .desc("霉运拉满，顺便让你发光被怪看见")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.effect(StatusEffects.UNLUCK, 20 * 60, 2);
                    ctx.effect(StatusEffects.GLOWING, 20 * 20, 0);
                    ctx.msg("§7模组给你上了个 buff：§c『今天不宜开箱、不宜附魔、不宜活着』§7。");
                    ctx.addRage(3);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_glowing_beacon", "稀有精英怪", TrollTier.ANNOYING)
                .desc("给玩家发光，并全服公告『精英怪刷新了』")
                .weight(90)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.effect(StatusEffects.GLOWING, 20 * 45, 0);
                    ctx.broadcast("§8[§c服务器§8] §e一只 §c§l稀有精英怪 §e在附近刷新了，坐标已公开。");
                    ctx.msg("§7你就是那只『稀有精英怪』。别问，问就是系统认定。");
                    ctx.addRage(4);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_potion_roulette", "轮盘", TrollTier.NIGHTMARE)
                .desc("两三种负面效果随机落下，偶尔再加一个奇怪的——剂量由它决定，止损由你承担")
                .weight(100)
                .cooldownSeconds(90)
                .when(ctx -> ctx.health() > 14.0f)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 20 * ctx.ri(4, 9), ctx.ri(0, 1));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 20 * ctx.ri(4, 9), 0);
                    if (ctx.chance(0.35)) {
                        ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 20 * 15, ctx.ri(0, 1));
                    }
                    ctx.msg("§7整人大师牌药水：口味随机、效果随机、负责到底？§c不负责。");
                    ctx.addRage(4);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_frostbite", "结霜的骨头", TrollTier.NIGHTMARE)
                .desc("冻伤伤害与缓慢一起到来，寒气粒子贴着你——冷是会往里走的")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.damage("freeze", 2.0f);
                    ctx.effect(StatusEffects.SLOWNESS, 20 * 12, 0);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.msg("§b西伯利亚的冷空气专程来看你，还带了份冻伤当伴手礼。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_poison_vial", "沼泽特调", TrollTier.ANNOYING)
                .desc("中毒，但保证留你一口气")
                .weight(110)
                .cooldownSeconds(90)
                .when(ctx -> ctx.health() > 8.0f)
                .run(ctx -> {
                    ctx.effect(StatusEffects.POISON, 20 * 10, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.msg("§2有人往你饮料里加了一勺『沼泽特调』。配方保密，味道感人。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_jumpy_sugar", "过期跳跳糖", TrollTier.BRUTAL)
                .desc("跳跃提升拉满，落地全靠运气")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.effect(StatusEffects.JUMP_BOOST, 20 * 30, 4);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.msg("§e你吃了过期的跳跳糖。刹车是什么？不知道。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_night_ghost", "看不见的活人", TrollTier.NIGHTMARE)
                .desc("你能看见一切，别人看不见你——但怪物能感觉到你")
                .weight(90)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.effect(StatusEffects.NIGHT_VISION, 20 * 30, 0);
                    ctx.effect(StatusEffects.INVISIBILITY, 20 * 20, 0);
                    ctx.msg("§7你现在是隐形的了——除了怪物，谁都能感觉到你的存在。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_hunger_yoyo", "饥饿溜溜球", TrollTier.ANNOYING)
                .desc("先把饥饿拉满，再立刻清空")
                .weight(90)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.feed(20);
                    ctx.saturation(5.0f);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.ok("[系统] §a检测到你快饿死了，免费补给已发放。");
                    ctx.starve();
                    ctx.exhaust(6.0f);
                    ctx.warn("补给收回。刚才是幻觉，你的背包里从来就没有过食物。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_xp_wipe", "努力归零", TrollTier.NIGHTMARE)
                .desc("随机负面效果先落下，等级随后被清成 0——理由只有四个字：你不够努力")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.setExperience(0);
                    ctx.soundSelf(SoundEvents.BLOCK_ANVIL_LAND, 1.0f, 0.6f);
                    ctx.msg("§7你的等级被重置为 §c0§7。理由：§o你不够努力。");
                    ctx.addRage(4);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_xp_exchange", "经验兑换活动", TrollTier.BRUTAL)
                .desc("假装用经验换钻石，然后真的把经验清零")
                .weight(95)
                .cooldownSeconds(150)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.fakeBroadcast("§a经验兑换活动开启：§f每 1 级经验可兑换 3 颗钻石！");
                    ctx.setExperience(0);
                    ctx.msg("§7你的经验已经提交兑换，服务器正在处理……");
                    ctx.sys("处理完毕：获得 §c0 §7颗钻石，手续费 §c100%§7。感谢参与。");
                    ctx.addRage(5);
                })
                .build());

        // ==================================================================
        // 剥夺 —— 控制权在流失：视角、位置、等级
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("player_balloon_ride", "升空", TrollTier.APOCALYPSE)
                .desc("飘浮把你带离地面，缓降保证你落地时还醒着——羞耻是完整的")
                .weight(70)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.effect(StatusEffects.LEVITATION, 20 * 8, 0);
                    ctx.effect(StatusEffects.SLOW_FALLING, 20 * 20, 0);
                    ctx.soundSelf(SoundEvents.ENTITY_GHAST_SCREAM, 1.0f, 1.2f);
                    ctx.msg("§7有人往你身上绑了 §f32 个气球§7。祝旅途愉快，记得挥手。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_hot_skin", "自燃", TrollTier.APOCALYPSE)
                .desc("陨石雨落下，皮肤被点燃——退款入口不存在")
                .weight(70)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.isInWater())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.setFire(8);
                    ctx.damage("fire", 3.0f);
                    ctx.soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.8f, 1.4f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 30);
                    ctx.warn("§c你的皮肤检测到『自燃』功能已上线。退款入口：不存在。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_sky_launcher", "弹向高空", TrollTier.APOCALYPSE)
                .desc("从地面直接弹向高空，剩下的交给重力——它一直很敬业")
                .weight(55)
                .cooldownSeconds(240)
                .when(ctx -> ctx.isOnGround() && ctx.health() > 14.0f)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.launch(1.45);
                    ctx.actionBar("§e海拔正在上升……");
                    ctx.msg("§7再见，凡人。§o（落地时请务必感谢重力，它一直很敬业。）");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_ice_rink", "刹车失灵", TrollTier.NIGHTMARE)
                .desc("脚下铺满冰，速度加成让刹车成为传说——你只能选择撞哪一边")
                .weight(60)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.placeFloor(Blocks.ICE, 2);
                    ctx.effect(StatusEffects.SPEED, 20 * 15, 1);
                    ctx.msg("§b方圆 5 格已被改造成溜冰场。请优雅地、体面地摔出去。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_face_north", "被扳向北方", TrollTier.APOCALYPSE)
                .desc("视角被拧转后锁死在正北、按下看向地面，等级清零——你的身体不再听你的")
                .weight(65)
                .cooldownSeconds(180)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.glitch(GlitchIds.SPIN, 40, 2);
                    ctx.spin(180f);
                    ctx.setRotation(0f, 89f);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.9f);
                    ctx.msg("§7你的脖子被强行拧向正北方，还被按着看地面。颈椎：§c告辞。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_spotlight_hunt", "全服最亮的靶子", TrollTier.BRUTAL)
                .desc("发光标记 + 在背后刷两只敌对生物")
                .weight(50)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.effect(StatusEffects.GLOWING, 20 * 40, 0);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.fakeBroadcast("§c已将一名玩家标记为『猎物』，全服可见。");
                    ctx.msg("§c你现在是全服最亮的仔，也是最显眼的靶子。回头看看？");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_health_tax", "扣除通知", TrollTier.APOCALYPSE)
                .desc("生命被直接扣走一大截，附赠一张不存在的发票——收费方不解释")
                .weight(50)
                .cooldownSeconds(240)
                .when(ctx -> ctx.health() > 10.0f)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.damage("magic", 6.0f);
                    ctx.playHurtSound();
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 30);
                    ctx.warn("§c『生命税』已自动扣除：3 颗心。发票请自行去找模组作者索要。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_buried_alive", "地板下面", TrollTier.NIGHTMARE)
                .desc("被按进地下两层，再用方块裹死，工具同步报废——慢慢挖，或者别挖")
                .weight(50)
                .cooldownSeconds(300)
                .when(ctx -> ctx.isOnGround() && ctx.health() > 14.0f)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    var brick = ctx.pick(TrollPools.TROLL_BLOCKS);
                    ctx.teleport(ctx.x(), ctx.y() - 2.0, ctx.z());
                    ctx.surroundWith(brick);
                    ctx.soundSelf(SoundEvents.BLOCK_ANVIL_LAND, 0.9f, 0.6f);
                    ctx.msg("§7地板下才是你的归宿。慢慢挖，别憋死——或者也别太慢。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_drown_crown", "专属暴雨", TrollTier.APOCALYPSE)
                .desc("头顶被放上一格水，氧气清零、背包清空——这场雨只为你一个人下")
                .weight(55)
                .cooldownSeconds(240)
                .when(ctx -> !ctx.isInWater() && ctx.health() > 10.0f)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.setBlock(0, 2, 0, Blocks.WATER);
                    ctx.setAir(0);
                    ctx.damage("drown", 2.0f);
                    ctx.msg("§b你的头顶突然开始下暴雨——只下你头顶这一格，专属定制。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_suffocate_cap", "不透气的帽子", TrollTier.NIGHTMARE)
                .desc("头部位置被塞进方块，工具同时报废——有人给你戴了顶没有孔的帽子")
                .weight(55)
                .cooldownSeconds(240)
                .when(ctx -> ctx.health() > 12.0f)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    var cap = ctx.pick(TrollPools.TROLL_BLOCKS);
                    ctx.setBlock(0, 2, 0, cap);
                    ctx.soundSelf(SoundEvents.BLOCK_ANVIL_LAND, 0.8f, 0.7f);
                    ctx.msg("§7有人给你戴了顶新帽子。材质：§8泥土系§7。透气性：§c0§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_wither_touch", "隔着次元的手", TrollTier.NIGHTMARE)
                .desc("凋灵效果与凋零伤害一起留下——那只手来自另一个维度，不会松开")
                .weight(45)
                .cooldownSeconds(300)
                .when(ctx -> ctx.health() > 14.0f)
                .run(ctx -> {
                    ctx.effect(StatusEffects.WITHER, 20 * 8, 1);
                    ctx.damage("wither", 2.0f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 0.8f);
                    ctx.warn("§8一只凋灵骷髅隔着次元拍了拍你的肩膀，还留下了一点礼物。");
                    ctx.addRage(7);
                })
                .build());

        // ==================================================================
        // 折磨 —— 活着本身变得很累
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("player_lightning_hug", "被点名", TrollTier.OBLIVION)
                .desc("它先在广播里念出你的名字，再让雷落下。就算你活过这一下，引擎也已经替它关掉了怜悯。")
                .weight(35)
                .cooldownSeconds(420)
                .when(ctx -> ctx.health() > 13.0f)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e[天候系统] 雷击目标已确认：§f" + ctx.playerName() + "§e。");
                    ctx.title("§4§l被点名了", "§7你有三秒");
                    ctx.later(60, () -> {
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.effectRandom(1200, 3);
                        ctx.meteorShower(4, 16.0);
                        ctx.lightning();
                        ctx.setFire(6);
                        ctx.soundSelf(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
                        ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 60);
                        ctx.glitch(GlitchIds.FLASHBANG, 25, 2);
                        ctx.msg("§0雷声里，你听见有人在念你的名字。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_void_peek", "云端", TrollTier.APOCALYPSE)
                .desc("被抬到九十格高空，缓降让你慢慢飘回地面——往下看，家还在，只是很远")
                .weight(35)
                .cooldownSeconds(420)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.lift(90);
                    ctx.effect(StatusEffects.SLOW_FALLING, 20 * 30, 0);
                    ctx.effect(StatusEffects.DARKNESS, 20 * 8, 0);
                    ctx.fakeBroadcast("§c有玩家正在离开世界边界，正在回收……");
                    ctx.msg("§7欢迎来到云端。往下看，那是你暂时回不去的家。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_heal_then_break", "先治疗后收费", TrollTier.OBLIVION)
                .desc("它把血给你补满，让你有一瞬间真的以为自己被放过了——三秒后，连同这段安心一起收回去。")
                .weight(30)
                .cooldownSeconds(480)
                .when(ctx -> ctx.health() > 14.0f)
                .run(ctx -> {
                    ctx.heal(20.0f);
                    ctx.sys("已为你恢复全部生命值。请查收。");
                    ctx.ok("§a你被治好了。");
                    ctx.later(60, () -> {
                        ctx.msg("§7现在结算。");
                        ctx.buryAlive();
                        ctx.effectRandom(1200, 3);
                        ctx.voidPit(2, 16);
                        ctx.meteorShower(4, 16.0);
                        ctx.obliterate();
                        ctx.warn("§4恢复服务手续费：全部。概不退还。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_health_lock", "0.5", TrollTier.OBLIVION)
                .desc("它把你的血条钉在最后半颗心，再封死回血的路——从这一刻起你活着，但每一秒都在倒计时。")
                .weight(30)
                .cooldownSeconds(600)
                .when(ctx -> ctx.health() > 4.0f)
                .run(ctx -> {
                    ctx.setHealth(1.0f);
                    ctx.starve();
                    ctx.effect(StatusEffects.HUNGER, 20 * 60, 2);
                    ctx.effect(StatusEffects.SLOWNESS, 20 * 30, 1);
                    ctx.effect(StatusEffects.WEAKNESS, 20 * 30, 1);
                    ctx.glitch(GlitchIds.BLACKOUT, 30, 1);
                    ctx.title("§4§l0.5", "§c生命值已锁定");
                    ctx.msg("§4你的血条被焊死在 0.5 颗心。");
                    ctx.later(200, () -> {
                        ctx.msg("§8你听见有人在倒计时。§7从十开始，但数得很慢。");
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 60, 2);
                        ctx.effectRandom(300, 2);
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_explosion_hug", "脚下的地生气了", TrollTier.OBLIVION)
                .desc("先夺走你的一切，再让脚下炸开。爆炸不毁地形，只会把你剩下那点东西一起带走。")
                .weight(30)
                .cooldownSeconds(480)
                .when(ctx -> ctx.health() > 16.0f)
                .run(ctx -> {
                    ctx.effectRandom(1200, 3);
                    ctx.voidPit(2, 16);
                    ctx.stripAll();
                    ctx.boomAtPlayer(1.5f);
                    ctx.fling(ctx.rd(-0.6, 0.6), 0.9, ctx.rd(-0.6, 0.6));
                    ctx.soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 80);
                    ctx.msg("§8爆炸之后，地上什么都没少——少的只有你。");
                    ctx.later(60, () -> ctx.obliterate());
                })
                .build());

        // ==================================================================
        // 判决 —— 血条与尊严一起被结清
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("player_last_heart", "温柔地请离", TrollTier.OBLIVION)
                .desc("它先说这是免费服务，再把你活埋、抹除、并公告全服。整局只触发一次，死后也不会有第二局。")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.fakeBroadcast("§a[整人大师] 免费返乡服务已为你开通。");
                    ctx.ok("§a你被选中了，费用全免。");
                    ctx.later(80, () -> {
                        ctx.buryAlive();
                        ctx.effectRandom(1200, 3);
                        ctx.voidPit(2, 16);
                        ctx.title("§4§l整人大师", "§7本次服务：免费送你回家");
                        ctx.broadcast("§8[§c整人大师§8] §f" + ctx.playerName() + " §7已被系统温柔地请离了这个世界。");
                        ctx.randomHorrorSound();
                        ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 60);
                        ctx.obliterate();
                        ctx.msg("§0§o（引擎已关闭怜悯：这一次，重生点也不会替你保留什么。）");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_final_straw", "稻草", TrollTier.APOCALYPSE)
                .desc("先治满你的血，再慢速、失明、点燃、雷击、两面夹击，最后亲手结束你。整局只来一次")
                .weight(12)
                .cooldownSeconds(1200)
                .once()
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.title("§4§l最后一根稻草", "§c你确定还要继续玩下去？");
                    ctx.heal(20.0f);
                    ctx.effect(StatusEffects.SLOWNESS, 20 * 10, 3);
                    ctx.effect(StatusEffects.BLINDNESS, 20 * 6, 0);
                    ctx.setFire(6);
                    ctx.lightning();
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.broadcast("§8[§c整人大师§8] §4压死骆驼的，从来不是最后一根稻草，而是这个模组。");
                    ctx.addRage(25);
                    ctx.kill();
                })
                .build());
    }
}
