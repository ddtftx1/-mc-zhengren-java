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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;

/**
 * 【混沌与地狱】分类事件。
 * 全模组最狠的一批：每条事件都至少同时开火三套整人系统——
 * 音效 + 位移 + 背包 + 刷怪 + 药水 + 方块 + 天气 + 客户端画面。
 * 单放是事故，组合起来才是艺术。
 */
public final class ChaosEvents {

    private ChaosEvents() {
    }

    public static void register() {
        TrollEvents.category("混沌与地狱");

        // ==================== PRANK ====================

        TrollEvents.reg(TrollEvent.of("chaos_false_alarm", "假警报", TrollTier.PRANK)
                .desc("音效、标题、假聊天、烟雾四套一起上，然后什么都没有发生——这才是最气人的")
                .weight(120)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7（其实什么都没有）", 0, 30, 12);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), ctx.pick(TrollPools.FAKE_LINES));
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 30);
                    ctx.msg("§8[提示] 警报解除。你刚才那一跳，我们截图了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_mystery_gift", "神秘礼物", TrollTier.PRANK)
                .desc("给你塞一堆垃圾，配上领奖音效和爱心粒子，再让全服广播你中奖了")
                .weight(130)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), 16);
                    ctx.giveRandomJunk();
                    ctx.soundSelf(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0f, 1.4f);
                    ctx.particleAtPlayer(ParticleTypes.HEART, 25);
                    ctx.fakeBroadcast("§e恭喜 " + ctx.playerName() + " 抽中服务器豪华大礼包！");
                    ctx.msg("§7礼包内容：腐肉、毒土豆，以及我们的祝福。");
                })
                .build());

        // ==================== ANNOYING ====================

        TrollEvents.reg(TrollEvent.of("chaos_triple_combo", "三连击", TrollTier.ANNOYING)
                .desc("音效 + 传送 + 洗背包，老三样一次打包，附赠一次原地转圈")
                .weight(140)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.actionBar("§e[连击 1/3] §7你被踹了一脚");
                    ctx.scatter(18);
                    ctx.actionBar("§e[连击 2/3] §7你的背包被摇了摇");
                    ctx.shuffleInventory();
                    ctx.randomRotation();
                    ctx.actionBar("§c[连击 3/3] §7完成。别谢。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_hotbar_chaos", "快捷栏抽奖", TrollTier.ANNOYING)
                .desc("洗乱快捷栏、锁死槽位、强制随机切格，三套系统同时折磨你的手指")
                .weight(150)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.randomSelectSlot();
                    ctx.glitch(GlitchIds.HOTBAR_JUMP, 80, 2);
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 60);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.actionBar("§6你的快捷栏正在重新抽奖……");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_potion_roulette", "药水轮盘", TrollTier.ANNOYING)
                .desc("先清空你所有的增益，再灌三瓶随机负面，最后放一段女巫笑声当背景音乐")
                .weight(130)
                .cooldownSeconds(70)
                .when(ctx -> !ctx.isInLava())
                .run(ctx -> {
                    ctx.clearEffects();
                    ctx.effectRandom(600, 1);
                    ctx.effectRandom(400, 1);
                    ctx.effectRandom(300, 0);
                    ctx.particleAtPlayer(ParticleTypes.WITCH, 40);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.9f);
                    ctx.msg("§5你失去的一切，都是我们精心挑选的。");
                })
                .build());

        // ==================== BRUTAL ====================

        TrollEvents.reg(TrollEvent.of("chaos_mob_ambush", "背后有货", TrollTier.BRUTAL)
                .desc("背后贴脸刷怪两只、身边刷两只、再来个失明和一声惨叫，五套系统齐活")
                .weight(70)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.soundSelf(SoundEvents.ENTITY_WITHER_SPAWN, 1.0f, 0.7f);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.effect(StatusEffects.BLINDNESS, 80, 0);
                    ctx.particleAtPlayer(ParticleTypes.LARGE_SMOKE, 50);
                    ctx.title("§4§l别回头", "§7后面已经排好队了");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_lightning_judgment", "天谴", TrollTier.BRUTAL)
                .desc("雷暴 + 午夜 + 三连闪电 + 怪潮，天气系统、方块系统和刷怪系统一起上")
                .weight(60)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.isInWater())
                .run(ctx -> {
                    ctx.thunderstorm(2400);
                    ctx.timeOfDay(18000L);
                    ctx.sound(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 1.4f, 1.0f);
                    ctx.lightningNear();
                    ctx.lightningAt(ctx.x() + ctx.ri(-4, 4), ctx.y(), ctx.z() + ctx.ri(-4, 4));
                    ctx.lightningAt(ctx.x() + ctx.ri(-8, 8), ctx.y(), ctx.z() + ctx.ri(-8, 8));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.title("§4§l天罚", "§c你最近是不是干了什么亏心事");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_gift_and_boom", "天上掉馅饼", TrollTier.BRUTAL)
                .desc("先发钻石和战利品、放领奖音效，再原地炸一发，让你体验什么叫到手的才是最好的")
                .weight(65)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 1);
                    ctx.give(Items.DIAMOND, 5);
                    ctx.ok("服务器补偿已到账，请查收。");
                    ctx.soundSelf(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
                    ctx.particleAtPlayer(ParticleTypes.HEART, 30);
                    ctx.boomAtPlayer(2.5f);
                    ctx.soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.2f, 0.8f);
                    ctx.msg("§c补偿发放失败：物资已在爆炸中损毁。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_teleport_mugging", "定点收割", TrollTier.BRUTAL)
                .desc("有队友就传送到队友那儿借刀杀人，没有就扔到两百格外，顺手再抢你一件东西")
                .weight(70)
                .cooldownSeconds(260)
                .run(ctx -> {
                    var other = ctx.randomOtherPlayer();
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    if (other != null) {
                        ctx.teleportToPlayer(other);
                        ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "谁把我传走了？");
                    } else {
                        ctx.scatterFar(180);
                    }
                    ctx.takeRandomItem();
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.particleAtPlayer(ParticleTypes.PORTAL, 40);
                    ctx.actionBar("§c你被随机分配了一个新地址。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_inventory_purge", "清仓大甩卖", TrollTier.BRUTAL)
                .desc("清空背包、塞满垃圾、身边刷怪、广播说你搬家了——四套系统一起动手")
                .weight(40)
                .cooldownSeconds(600)
                .once()
                .run(ctx -> {
                    ctx.clearInventory();
                    ctx.fillWith(Items.ROTTEN_FLESH, 12);
                    ctx.fillWith(Items.STICK, 12);
                    ctx.soundSelf(SoundEvents.BLOCK_CHEST_CLOSE, 1.0f, 0.6f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.fakeBroadcast("§e玩家 " + ctx.playerName() + " 已完成资产清算，感谢配合。");
                    ctx.warn("你的东西去旅行了，没带你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_lava_baptism", "岩浆洗礼", TrollTier.BRUTAL)
                .desc("抬高三格、脚下放岩浆、全身着火，再配上岩浆粒子，一套流程走完不留痕")
                .weight(55)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.isInLava())
                .run(ctx -> {
                    ctx.lift(4.0);
                    ctx.setBlock(0, -1, 0, Blocks.LAVA);
                    ctx.setFire(6);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 1.0f);
                    ctx.particleAtPlayer(ParticleTypes.LAVA, 40);
                    ctx.warn("温泉已就位，请入池。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_ten_combo", "十连整人", TrollTier.BRUTAL)
                .desc("音效、传送、洗包、丢手、随机药水、画面抖动、贴脸刷怪、粒子、着火，十个步骤一次结算")
                .weight(45)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.9f);
                    ctx.actionBar("§4[连击 1/10] §7热身结束");
                    ctx.scatter(32);
                    ctx.shuffleInventory();
                    ctx.dropHeld();
                    ctx.effectRandom(200, 1);
                    ctx.glitch(GlitchIds.SHAKY, 60, 2);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.particleAtPlayer(ParticleTypes.EXPLOSION, 12);
                    ctx.setFire(4);
                    ctx.addRage(15.0);
                    ctx.actionBar("§4[连击 10/10] §c恭喜通关，奖励是没有奖励");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_scramble_swarm", "手忙脚乱", TrollTier.BRUTAL)
                .desc("洗乱快捷栏、丢掉手里的东西、刷一群怪、再锁死你的槽位，让你彻底没法还手")
                .weight(60)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.dropHeld();
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 100, 2);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.soundSelf(SoundEvents.ENTITY_ITEM_BREAK, 1.0f, 1.0f);
                    ctx.warn("现在是检验你徒手格斗水平的时候了。");
                })
                .build());

        // ==================== NIGHTMARE ====================

        TrollEvents.reg(TrollEvent.of("chaos_bedrock_cage", "基岩牢笼", TrollTier.NIGHTMARE)
                .desc("用基岩把你原地封死，塞两只怪进来，再灌凋零和失明——唯一的出口是你的重生点")
                .weight(35)
                .cooldownSeconds(480)
                .when(ctx -> !ctx.isInLava() && ctx.isOnGround())
                .run(ctx -> {
                    ctx.cageWith(Blocks.BEDROCK);
                    ctx.effect(StatusEffects.BLINDNESS, 200, 0);
                    ctx.effect(StatusEffects.WITHER, 160, 1);
                    ctx.effect(StatusEffects.WEAKNESS, 200, 1);
                    ctx.spawnAny(EntityType.ZOMBIE, ctx.x(), ctx.y(), ctx.z());
                    ctx.spawnAny(EntityType.SKELETON, ctx.x(), ctx.y(), ctx.z());
                    ctx.setHealth(Math.min(ctx.health(), 6.0f));
                    ctx.soundSelf(SoundEvents.BLOCK_ANVIL_LAND, 1.2f, 0.6f);
                    ctx.particleAtPlayer(ParticleTypes.SOUL, 50);
                    ctx.warn("牢笼已经焊死。想出去？交一条命就行。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_fake_crash_siege", "崩溃围城", TrollTier.NIGHTMARE)
                .desc("先给你假崩溃界面再隐藏 HUD，趁你手忙脚乱时刷满怪、放雷暴、贴脸惨叫")
                .weight(32)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 100, 2);
                    ctx.glitch(GlitchIds.HIDE_HUD, 400, 1);
                    ctx.thunderstorm(1200);
                    ctx.soundSelf(SoundEvents.ENTITY_GHAST_SCREAM, 1.2f, 0.7f);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.title("§4§l游戏出现异常", "§8请稍候……其实不用等了");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_fake_death_blackout", "假死黑屏", TrollTier.NIGHTMARE)
                .desc("扣血、假死亡界面、黑屏、刷屏假死因、假聊天，五套系统演一出你已经死了的戏")
                .weight(34)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.setHealth(Math.max(1.0f, ctx.health() - 6.0f));
                    ctx.playHurtSound();
                    ctx.glitch(GlitchIds.FAKE_DEATH, 120, 2);
                    ctx.glitch(GlitchIds.BLACKOUT, 70, 2);
                    ctx.glitch(GlitchIds.CHAT_SPAM, 60, 1);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES),
                            "§7" + ctx.playerName() + " " + ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.particle(ParticleTypes.DAMAGE_INDICATOR, 30);
                    ctx.warn("死亡原因登记中……请勿关闭游戏（关了也没用）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_sky_abyss", "天空监狱", TrollTier.NIGHTMARE)
                .desc("清空你所有增益、抬到三百格高空、清掉缓降、反转视角，再广播一句祝你落地平安")
                .weight(30)
                .cooldownSeconds(480)
                .when(ctx -> !ctx.isFalling())
                .run(ctx -> {
                    ctx.clearEffects();
                    ctx.removeEffect(StatusEffects.SLOW_FALLING);
                    ctx.lift(150.0);
                    ctx.fling(0.0, -0.6, 0.0);
                    ctx.glitch(GlitchIds.FOV_WILD, 120, 2);
                    ctx.glitch(GlitchIds.INVERT_PITCH, 100, 1);
                    ctx.soundSelf(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.2f, 1.4f);
                    ctx.fakeBroadcast("§e玩家 " + ctx.playerName() + " 已升级为空中飞人。");
                    ctx.warn("落地姿势建议：不要低头看。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_flashbang_horde", "闪光怪潮", TrollTier.NIGHTMARE)
                .desc("闪白巨响、剧烈甩镜、爆炸特效、四只怪和虚弱药水，五套系统同时糊脸")
                .weight(33)
                .cooldownSeconds(460)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FLASHBANG, 40, 3);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 60, 3);
                    ctx.explosionFx();
                    ctx.soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.4f, 0.7f);
                    ctx.effect(StatusEffects.WEAKNESS, 300, 1);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.actionBar("§c闪光结束，怪潮开始。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_control_sabotage", "操作台被黑", TrollTier.NIGHTMARE)
                .desc("灵敏度归零、鼠标反转、视野抖动、眩晕药水、远距离传送加刷怪，六套系统一起瘫痪你的手")
                .weight(30)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SENS_ZERO, 120, 2);
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 140, 2);
                    ctx.glitch(GlitchIds.FOV_ZOOM, 80, 1);
                    ctx.effect(StatusEffects.NAUSEA, 300, 2);
                    ctx.effect(StatusEffects.SLOWNESS, 200, 1);
                    ctx.scatterFar(90);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 0.6f);
                    ctx.warn("你的操作已由本模组代为接管，接管方式是乱来。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_blood_moon", "血月当空", TrollTier.NIGHTMARE)
                .desc("强制午夜、雷暴、五只怪、恐怖音效和血月广播，天气刷怪时钟三系统联动")
                .weight(35)
                .cooldownSeconds(450)
                .run(ctx -> {
                    ctx.timeOfDay(18000L);
                    ctx.thunderstorm(3600);
                    ctx.sound(SoundEvents.ENTITY_WITHER_SPAWN, 1.4f, 0.6f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 60);
                    ctx.fakeBroadcast("§4今晚月色很好，适合送人头。");
                    ctx.title("§4§l血月", "§7今晚没有人能早睡");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_scorched_earth", "焦土政策", TrollTier.NIGHTMARE)
                .desc("破坏性爆炸（受配置限制）+ 着火 + 雷暴 + 怪潮 + 广播，主打一个寸草不生")
                .weight(28)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.boomDestructive(ctx.x() + ctx.ri(-4, 4), ctx.y(), ctx.z() + ctx.ri(-4, 4), 3.0f);
                    ctx.boom(ctx.x(), ctx.y() + 1, ctx.z(), 2.0f);
                    ctx.setFire(5);
                    ctx.thunderstorm(1800);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.4f, 0.6f);
                    ctx.fakeBroadcast("§c本地块已被划入拆迁范围，补偿方案：没有。");
                })
                .build());

        // ==================== APOCALYPSE ====================

        TrollEvents.reg(TrollEvent.of("chaos_ultimate_combo", "终极连击", TrollTier.APOCALYPSE)
                .desc("假崩溃 + 隐藏 HUD + 假标题界面 + 洗背包 + 四只贴脸怪 + 雷暴，一次全给你")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 140, 3);
                    ctx.glitch(GlitchIds.HIDE_HUD, 500, 2);
                    ctx.glitch(GlitchIds.MENU_TEASE, 120, 2);
                    ctx.shuffleInventory();
                    ctx.thunderstorm(2400);
                    ctx.soundSelf(SoundEvents.ENTITY_WITHER_SPAWN, 1.4f, 0.6f);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.particleAtPlayer(ParticleTypes.EXPLOSION_EMITTER, 3);
                    ctx.addRage(25.0);
                    ctx.broadcast("§4§l[整人大师] 终极连击已对 " + ctx.playerName() + " 生效。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_apocalypse_now", "末日降临", TrollTier.APOCALYPSE)
                .desc("午夜雷暴 + 监守者与劫掠兽 + 黑暗凋零 + 爆炸 + 末日标题，全场最狠的一道天罚")
                .weight(15)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.timeOfDay(18000L);
                    ctx.thunderstorm(4800);
                    ctx.sound(SoundEvents.ENTITY_WARDEN_ROAR, 1.5f, 0.7f);
                    ctx.spawnHostileNear(EntityType.WARDEN, 6);
                    ctx.spawnHostileNear(EntityType.RAVAGER, 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.effect(StatusEffects.DARKNESS, 400, 0);
                    ctx.effect(StatusEffects.WITHER, 200, 1);
                    ctx.boom(ctx.x(), ctx.y() + 1, ctx.z(), 3.0f);
                    ctx.glitch(GlitchIds.FAKE_OOM, 100, 2);
                    ctx.title("§4§l末日", "§4你已经没有第二次机会了");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_doom_arena", "死亡竞技场", TrollTier.APOCALYPSE)
                .desc("蛛网封场、怪潮入场、虚弱失明凋零三件套、血量压到两格，再广播比赛开始")
                .weight(22)
                .cooldownSeconds(720)
                .run(ctx -> {
                    ctx.cageWith(Blocks.COBWEB);
                    ctx.effect(StatusEffects.WEAKNESS, 400, 2);
                    ctx.effect(StatusEffects.BLINDNESS, 200, 0);
                    ctx.effect(StatusEffects.WITHER, 200, 1);
                    ctx.spawnAny(EntityType.ZOMBIE, ctx.x(), ctx.y(), ctx.z());
                    ctx.spawnAny(EntityType.SPIDER, ctx.x(), ctx.y(), ctx.z());
                    ctx.spawnAny(EntityType.SKELETON, ctx.x(), ctx.y(), ctx.z());
                    ctx.setHealth(2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.4f, 0.7f);
                    ctx.particleAtPlayer(ParticleTypes.DRAGON_BREATH, 50);
                    ctx.fakeBroadcast("§4竞技场已就绪，观众席座无虚席。");
                    ctx.warn("本场没有暂停键，也没有投降键。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_everything_broken", "万物崩坏", TrollTier.APOCALYPSE)
                .desc("清增益、灌四瓶负面、洗背包、丢装备、远距离传送、刷怪、眩晕画面——九套系统一次性崩给你看")
                .weight(18)
                .cooldownSeconds(600)
                .once()
                .run(ctx -> {
                    ctx.clearEffects();
                    ctx.effect(StatusEffects.NAUSEA, 600, 2);
                    ctx.effect(StatusEffects.BLINDNESS, 200, 1);
                    ctx.effect(StatusEffects.WITHER, 200, 1);
                    ctx.effect(StatusEffects.LEVITATION, 100, 1);
                    ctx.shuffleInventory();
                    ctx.dropRandom();
                    ctx.scatterFar(150);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 200, 3);
                    ctx.glitch(GlitchIds.SHAKY, 160, 2);
                    ctx.soundSelf(SoundEvents.ENTITY_ENDER_DRAGON_GROWL, 1.5f, 0.6f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 80);
                    ctx.addRage(30.0);
                    ctx.warn("系统提示：本模组已放弃对你进行人道主义处理。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_last_stand", "最后三秒", TrollTier.APOCALYPSE)
                .desc("假存档损坏、屏闪甩镜、假玩家加入围观、怪潮合围，再用广播念一段悼词")
                .weight(12)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 120, 3);
                    ctx.glitch(GlitchIds.FLASHBANG, 30, 3);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 50, 3);
                    int watchers = ctx.allPlayers().size() - 1;
                    ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), true);
                    ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), false);
                    if (watchers > 0) {
                        ctx.fakeBroadcast("§c" + watchers + " 名玩家正在围观你的最后时刻。");
                    }
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.soundSelf(SoundEvents.ENTITY_WITHER_SPAWN, 1.5f, 0.5f);
                    ctx.title("§4§l最后一秒", "§8存档已在另一个宇宙继续");
                    ctx.broadcast("§4§l[整人大师] 全体默哀三秒，为 " + ctx.playerName() + " 送行。");
                })
                .build());
    }
}
