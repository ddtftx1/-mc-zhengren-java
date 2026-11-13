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
        // PRANK —— 小整，笑一下就好
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("player_fake_creeper", "§a身后的嘶嘶声", TrollTier.PRANK)
                .desc("贴脸播放苦力怕引信声，然后什么都不发生")
                .weight(160)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.soundSelf(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 1.0f);
                    ctx.actionBar("§7嘶————————");
                    ctx.msg("§7（你僵住了 40 tick）");
                    ctx.msg("§f……§o什么都没有发生。你的反应已经被记录在案。");
                    ctx.addRage(1);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_bad_burp", "消化不良", TrollTier.PRANK)
                .desc("打个嗝，顺便嘲一下玩家的伙食")
                .weight(150)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, ctx.rf(0.8f, 1.3f));
                    ctx.spin(ctx.rf(-12f, 12f));
                    ctx.msg("§7你打了一个嗝，味道非常熟悉——就是你刚才舍不得吃的那口金苹果。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_fake_levelup", "假升级", TrollTier.PRANK)
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

        TrollEvents.reg(TrollEvent.of("player_slippery_start", "脚底一滑", TrollTier.PRANK)
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
        // ANNOYING —— 烦人，会皱眉
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("player_nausea_spin", "晕头转向", TrollTier.ANNOYING)
                .desc("长时间反胃，并把视角随机拧一下")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.effect(StatusEffects.NAUSEA, 20 * 15, 0);
                    ctx.spin(ctx.rf(-45f, 45f));
                    ctx.msg("§7你的视野开始打转，胃里像养了一条末影龙。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_blindness_flash", "闪光弹", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("player_lead_boots", "铅制靴子", TrollTier.ANNOYING)
                .desc("缓慢 + 挖掘疲劳，走两步喘三下")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.effect(StatusEffects.SLOWNESS, 20 * 20, 1);
                    ctx.effect(StatusEffects.MINING_FATIGUE, 20 * 20, 1);
                    ctx.msg("§7你的靴子不知何时被换成了两块铅。恭喜，走路也是健身。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_noodle_arms", "面条手臂", TrollTier.ANNOYING)
                .desc("虚弱 + 挖掘疲劳，挖一块石头挖到天亮")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.effect(StatusEffects.WEAKNESS, 20 * 18, 1);
                    ctx.effect(StatusEffects.MINING_FATIGUE, 20 * 18, 0);
                    ctx.msg("§7你的胳膊变成了两条煮烂的面条。加油，慢慢挖，我们不急。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_darkness_crawl", "伸手不见五指", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("player_hunger_drain", "胃变成黑洞", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("player_unluck_curse", "今天不宜开箱", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("player_potion_roulette", "药水轮盘赌", TrollTier.ANNOYING)
                .desc("随机塞两三个负面效果，偶尔搭一个奇怪的")
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

        TrollEvents.reg(TrollEvent.of("player_frostbite", "冻伤", TrollTier.ANNOYING)
                .desc("冻伤伤害 + 缓慢，配一点寒气粒子")
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

        TrollEvents.reg(TrollEvent.of("player_jumpy_sugar", "过期跳跳糖", TrollTier.ANNOYING)
                .desc("跳跃提升拉满，落地全靠运气")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.effect(StatusEffects.JUMP_BOOST, 20 * 30, 4);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.msg("§e你吃了过期的跳跳糖。刹车是什么？不知道。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_night_ghost", "夜视幽灵", TrollTier.ANNOYING)
                .desc("夜视 + 隐形，你能看见别人，别人看不见你")
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

        TrollEvents.reg(TrollEvent.of("player_xp_wipe", "等级归零", TrollTier.ANNOYING)
                .desc("把经验等级直接清零")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.setExperience(0);
                    ctx.soundSelf(SoundEvents.BLOCK_ANVIL_LAND, 1.0f, 0.6f);
                    ctx.msg("§7你的等级被重置为 §c0§7。理由：§o你不够努力。");
                    ctx.addRage(4);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_xp_exchange", "经验兑换活动", TrollTier.ANNOYING)
                .desc("假装用经验换钻石，然后真的把经验清零")
                .weight(95)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.fakeBroadcast("§a经验兑换活动开启：§f每 1 级经验可兑换 3 颗钻石！");
                    ctx.setExperience(0);
                    ctx.msg("§7你的经验已经提交兑换，服务器正在处理……");
                    ctx.sys("处理完毕：获得 §c0 §7颗钻石，手续费 §c100%§7。感谢参与。");
                    ctx.addRage(5);
                })
                .build());

        // ==================================================================
        // BRUTAL —— 抓狂，会骂人
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("player_balloon_ride", "32 个气球", TrollTier.BRUTAL)
                .desc("飘浮上升，再给缓降让你安全但羞耻地落地")
                .weight(70)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.effect(StatusEffects.LEVITATION, 20 * 8, 0);
                    ctx.effect(StatusEffects.SLOW_FALLING, 20 * 20, 0);
                    ctx.soundSelf(SoundEvents.ENTITY_GHAST_SCREAM, 1.0f, 1.2f);
                    ctx.msg("§7有人往你身上绑了 §f32 个气球§7。祝旅途愉快，记得挥手。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_hot_skin", "自燃体质", TrollTier.BRUTAL)
                .desc("点燃玩家并补一点火焰伤害")
                .weight(70)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.isInWater())
                .run(ctx -> {
                    ctx.setFire(8);
                    ctx.damage("fire", 3.0f);
                    ctx.soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.8f, 1.4f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 30);
                    ctx.warn("§c你的皮肤检测到『自燃』功能已上线。退款入口：不存在。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_sky_launcher", "一飞冲天", TrollTier.BRUTAL)
                .desc("把玩家从地面弹向高空（血量足够时才触发，保证摔不死）")
                .weight(55)
                .cooldownSeconds(240)
                .when(ctx -> ctx.isOnGround() && ctx.health() > 14.0f)
                .run(ctx -> {
                    ctx.launch(1.45);
                    ctx.actionBar("§e海拔正在上升……");
                    ctx.msg("§7再见，凡人。§o（落地时请务必感谢重力，它一直很敬业。）");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_ice_rink", "临时溜冰场", TrollTier.BRUTAL)
                .desc("脚下铺一圈冰，再给个加速让它更难刹车")
                .weight(60)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.placeFloor(Blocks.ICE, 2);
                    ctx.effect(StatusEffects.SPEED, 20 * 15, 1);
                    ctx.msg("§b方圆 5 格已被改造成溜冰场。请优雅地、体面地摔出去。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_face_north", "颈椎终结者", TrollTier.BRUTAL)
                .desc("旋转视角后强行扭向正北方并按住看地面")
                .weight(65)
                .cooldownSeconds(180)
                .run(ctx -> {
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

        TrollEvents.reg(TrollEvent.of("player_health_tax", "生命税", TrollTier.BRUTAL)
                .desc("直接扣除 3 颗心，并给出荒谬的收费理由")
                .weight(50)
                .cooldownSeconds(240)
                .when(ctx -> ctx.health() > 10.0f)
                .run(ctx -> {
                    ctx.damage("magic", 6.0f);
                    ctx.playHurtSound();
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 30);
                    ctx.warn("§c『生命税』已自动扣除：3 颗心。发票请自行去找模组作者索要。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_buried_alive", "活埋", TrollTier.BRUTAL)
                .desc("把玩家往下按两层，再用泥土裹起来")
                .weight(50)
                .cooldownSeconds(300)
                .when(ctx -> ctx.isOnGround() && ctx.health() > 14.0f)
                .run(ctx -> {
                    var brick = ctx.pick(TrollPools.TROLL_BLOCKS);
                    ctx.teleport(ctx.x(), ctx.y() - 2.0, ctx.z());
                    ctx.surroundWith(brick);
                    ctx.soundSelf(SoundEvents.BLOCK_ANVIL_LAND, 0.9f, 0.6f);
                    ctx.msg("§7地板下才是你的归宿。慢慢挖，别憋死——或者也别太慢。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_drown_crown", "头顶那格下雨了", TrollTier.BRUTAL)
                .desc("在玩家头顶放一格水并清空氧气")
                .weight(55)
                .cooldownSeconds(240)
                .when(ctx -> !ctx.isInWater() && ctx.health() > 10.0f)
                .run(ctx -> {
                    ctx.setBlock(0, 2, 0, Blocks.WATER);
                    ctx.setAir(0);
                    ctx.damage("drown", 2.0f);
                    ctx.msg("§b你的头顶突然开始下暴雨——只下你头顶这一格，专属定制。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_suffocate_cap", "泥土帽子", TrollTier.BRUTAL)
                .desc("在玩家头的位置塞一个方块，体验窒息")
                .weight(55)
                .cooldownSeconds(240)
                .when(ctx -> ctx.health() > 12.0f)
                .run(ctx -> {
                    var cap = ctx.pick(TrollPools.TROLL_BLOCKS);
                    ctx.setBlock(0, 2, 0, cap);
                    ctx.soundSelf(SoundEvents.BLOCK_ANVIL_LAND, 0.8f, 0.7f);
                    ctx.msg("§7有人给你戴了顶新帽子。材质：§8泥土系§7。透气性：§c0§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_wither_touch", "凋灵的一摸", TrollTier.BRUTAL)
                .desc("凋灵效果 + 凋零伤害，血量够才不会当场去世")
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
        // NIGHTMARE —— 崩溃，会砸键盘
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("player_lightning_hug", "天罚", TrollTier.NIGHTMARE)
                .desc("原地一道闪电劈脸，附赠点燃和标题")
                .weight(35)
                .cooldownSeconds(420)
                .when(ctx -> ctx.health() > 13.0f)
                .run(ctx -> {
                    ctx.title("§4§l天罚", "§7你被点名了");
                    ctx.lightning();
                    ctx.setFire(4);
                    ctx.soundSelf(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 60);
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_void_peek", "云端一游", TrollTier.NIGHTMARE)
                .desc("把玩家抬到 90 格高空，再给足缓降让他慢慢飘回来")
                .weight(35)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.lift(90);
                    ctx.effect(StatusEffects.SLOW_FALLING, 20 * 30, 0);
                    ctx.effect(StatusEffects.DARKNESS, 20 * 8, 0);
                    ctx.fakeBroadcast("§c有玩家正在离开世界边界，正在回收……");
                    ctx.msg("§7欢迎来到云端。往下看，那是你暂时回不去的家。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_heal_then_break", "先治疗后收费", TrollTier.NIGHTMARE)
                .desc("把血回满，再当场打残一半以上")
                .weight(30)
                .cooldownSeconds(480)
                .when(ctx -> ctx.health() > 14.0f)
                .run(ctx -> {
                    ctx.heal(20.0f);
                    ctx.sys("已为你恢复全部生命值。请查收。");
                    ctx.damage("magic", 12.0f);
                    ctx.playHurtSound();
                    ctx.warn("§4惊喜！恢复服务已收回 6 颗心作为手续费，概不退还。");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_health_lock", "焊死的血条", TrollTier.NIGHTMARE)
                .desc("血量锁死在半颗心并封掉自然回血，配黑屏标题")
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
                    ctx.msg("§4你的血条被焊死在 0.5 颗心。想回血？先爬着去找吃的。");
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_explosion_hug", "脚下的地生气了", TrollTier.NIGHTMARE)
                .desc("脚下不破坏地形的爆炸，把人掀起来")
                .weight(30)
                .cooldownSeconds(480)
                .when(ctx -> ctx.health() > 16.0f)
                .run(ctx -> {
                    ctx.boomAtPlayer(1.5f);
                    ctx.fling(ctx.rd(-0.6, 0.6), 0.9, ctx.rd(-0.6, 0.6));
                    ctx.soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 80);
                    ctx.warn("§c友情提示：你脚下那块地，刚刚决定当一次 TNT。（地形无损，人有所损）");
                    ctx.addRage(11);
                })
                .build());

        // ==================================================================
        // APOCALYPSE —— 地狱，会退游戏（直接击杀只在这里出现）
        // ==================================================================

        TrollEvents.reg(TrollEvent.of("player_last_heart", "温柔地请离", TrollTier.APOCALYPSE)
                .desc("直接击杀玩家，并全服公告。整局只触发一次")
                .weight(18)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.title("§4§l整人大师", "§7本次服务：免费送你回家");
                    ctx.broadcast("§8[§c整人大师§8] §f" + ctx.playerName() + " §7已被系统温柔地请离了这个世界。");
                    ctx.randomHorrorSound();
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 60);
                    ctx.addRage(20);
                    ctx.kill();
                })
                .build());

        TrollEvents.reg(TrollEvent.of("player_final_straw", "最后一根稻草", TrollTier.APOCALYPSE)
                .desc("迟缓 + 点燃 + 闪电 + 背后两只怪，最后直接击杀。整局只触发一次")
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
