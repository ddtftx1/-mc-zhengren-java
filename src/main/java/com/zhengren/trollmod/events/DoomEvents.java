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
 * 【末日与毁灭】分类事件（第二批终极连招）。
 * 全场最狠的一包：陨石雨、虚空流放、岩浆浴场、假封禁、世界终章、全员末日。
 * 每条事件都同时拉动至少三套系统：天象/时间 + 地形 + 刷怪 + 客户端画面 + 药水 + 音效。
 */
public final class DoomEvents {

    private DoomEvents() {
    }

    public static void register() {
        TrollEvents.category("末日与毁灭");

        // ==================== PRANK ====================

        TrollEvents.reg(TrollEvent.of("doom_false_prophecy", "末日预言", TrollTier.PRANK)
                .desc("广播、标题、阴森音效、假聊天、恐怖粒子一起放，最后告诉你预言时间：永远不会")
                .weight(120)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.fakeBroadcast(ctx.pick(TrollPools.FAKE_BROADCASTS));
                    ctx.title("§4§l末日将至", "§7预计抵达时间：永远不到");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 0.8f);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "预言是真的，我从不骗人");
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.msg("§8[预言] 本次预言由模组赞助，不承担任何责任。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_diamond_miracle", "钻石神迹", TrollTier.PRANK)
                .desc("发三颗钻石、弹假成就、撒爱心粒子、放升级音效——纯粹为了让你高兴三秒")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.give(Items.DIAMOND, 3);
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_ACHIEVEMENTS), "挖到了三颗闪亮的东西");
                    ctx.particleAtPlayer(ParticleTypes.HEART, 20);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.msg("§7（我们很快就会为这次的慷慨后悔）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_tip_of_the_day", "末日小贴士", TrollTier.PRANK)
                .desc("假加载提示、假自动保存、气人音效、系统嘲讽，三套界面系统一起骚扰你")
                .weight(100)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.actionBar(ctx.pick(TrollPools.FAKE_TIPS));
                    ctx.fakeSaveNotice();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.3f);
                    ctx.sys(ctx.pick(TrollPools.TAUNTS));
                })
                .build());

        // ==================== ANNOYING ====================

        TrollEvents.reg(TrollEvent.of("doom_weather_mood", "天气心情", TrollTier.ANNOYING)
                .desc("随机天气 + 时间跳跃 + 阴森音效 + 粒子，天空归模组管，不归你管")
                .weight(130)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.setWeatherRandom();
                    ctx.timeSkip(ctx.ri(-6000, 6000));
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 0.9f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.actionBar("§7天气已由模组重新分配，不接受申诉。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_ash_rain", "灰烬之雨", TrollTier.ANNOYING)
                .desc("满屏白灰、随机药水、阴森音效、假聊天，让你以为世界正在烧成灰")
                .weight(120)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.particleAtPlayer(ParticleTypes.WHITE_ASH, 80);
                    ctx.effectRandom(200, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.1f);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "天上下灰了，你还好吗");
                    ctx.actionBar("§8空气里全是灰，你的肺表示强烈抗议。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_quest_board", "假任务板", TrollTier.ANNOYING)
                .desc("弹一个根本不存在的任务成就，塞点垃圾当奖励，再补一句不可取消")
                .weight(120)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_QUESTS), "目标：活着");
                    ctx.giveRandomJunk();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.actionBar("§e新任务已接取（不可取消，也不可完成）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_low_gravity", "低重力演练", TrollTier.ANNOYING)
                .desc("失重药水 + 弹射 + 传送音效 + 粒子 + 标题，让你体验当一颗土豆的感觉")
                .weight(110)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.noGravity(120);
                    ctx.launch(0.7);
                    ctx.setSprinting(false);
                    ctx.soundSelf(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.4f);
                    ctx.particleAtPlayer(ParticleTypes.PORTAL, 30);
                    ctx.title("§b§l重力已离线", "§7落地时间未知");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_save_scare", "假保存惊吓", TrollTier.ANNOYING)
                .desc("假保存界面 + 客户端假保存提示 + 音效 + 聊天，让你以为进度真的存了")
                .weight(130)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.glitch(GlitchIds.FAKE_SAVING, 60, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§7世界数据已保存（并没有）。");
                })
                .build());

        // ==================== BRUTAL ====================

        TrollEvents.reg(TrollEvent.of("doom_meteor_warning", "陨石预警", TrollTier.BRUTAL)
                .desc("预警标题 + 巨响 + 陨石雨 + 雷暴 + 爆炸粒子，警告发完了，陨石也到了")
                .weight(65)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.title("§4§l陨石接近中", "§c抬头，或者不抬，都一样");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.2f, 0.8f);
                    ctx.meteorShower(3, 14.0);
                    ctx.thunderstorm(600);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 30);
                    ctx.warn("规避建议：跑。往哪跑都行。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_sinkhole_trap", "地陷陷阱", TrollTier.BRUTAL)
                .desc("脚下挖空六格、背后贴脸刷怪、缓慢药水、爆炸音效，地基这种东西本来就是可选项")
                .weight(60)
                .cooldownSeconds(300)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(2, 6);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.effect(StatusEffects.SLOWNESS, 200, 1);
                    ctx.soundSelf(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.2f, 0.7f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 40);
                    ctx.warn("你家地基已被回收，感谢配合城市建设。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_lava_bath", "岩浆浴场", TrollTier.BRUTAL)
                .desc("脚下铺岩浆 + 抬高三格 + 着火 + 缓慢 + 岩浆粒子，一条龙洗浴服务")
                .weight(55)
                .cooldownSeconds(320)
                .when(ctx -> !ctx.isInLava())
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.lift(3.0);
                    ctx.setFire(4);
                    ctx.effect(StatusEffects.SLOWNESS, 120, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 1.0f);
                    ctx.particleAtPlayer(ParticleTypes.LAVA, 40);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_tnt_gift", "礼物是TNT", TrollTier.BRUTAL)
                .desc("先送你五个 TNT 当礼物，再在你脚边点着两枚，配点火音效和画面抖动")
                .weight(60)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.give(Items.TNT, 5);
                    ctx.ok("服务器送你 5 个 TNT，请签收（附赠两份已点燃的）。");
                    ctx.spawnTnt(ctx.x() + ctx.ri(-3, 3), ctx.y() + 1.0, ctx.z() + ctx.ri(-3, 3), 40);
                    ctx.spawnTnt(ctx.x() + ctx.ri(-3, 3), ctx.y() + 1.0, ctx.z() + ctx.ri(-3, 3), 60);
                    ctx.soundSelf(SoundEvents.ENTITY_TNT_PRIMED, 1.2f, 1.0f);
                    ctx.glitch(GlitchIds.SHAKY, 60, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_junk_rain", "杂物天降", TrollTier.BRUTAL)
                .desc("头顶连掉三块恶心方块、铁砧巨响、缓慢药水和一句安慰，物理伤害与心理伤害齐飞")
                .weight(58)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.spawnFallingBlock(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.spawnFallingBlock(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.spawnFallingBlock(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.soundSelf(SoundEvents.BLOCK_ANVIL_LAND, 1.2f, 0.8f);
                    ctx.effect(StatusEffects.SLOWNESS, 160, 1);
                    ctx.title("§4§l天降杂物", "§7都是给你的，别客气");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_haunted_mines", "幽灵矿脉", TrollTier.BRUTAL)
                .desc("周围撒一堆假矿、假玩家在旁边起哄、阴森音效加失明，挖不挖都是坑")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.ORE_TEASE), 10);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "你旁边那堆钻石是我放的");
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "挖吧，挖到算你厉害");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.1f, 0.9f);
                    ctx.effect(StatusEffects.BLINDNESS, 100, 0);
                    ctx.msg("§7（它们看起来是真的，摸起来也是真的，可惜不值钱）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_freeze_field", "冰封战场", TrollTier.BRUTAL)
                .desc("冻结定身 + 三向刷怪 + 环境恐怖音 + 白灰粒子，你只能站着挨打")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.freeze(200);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.2f, 0.8f);
                    ctx.particleAtPlayer(ParticleTypes.WHITE_ASH, 60);
                    ctx.actionBar("§b你的关节已被模组接管。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_mount_escape", "强制上马", TrollTier.BRUTAL)
                .desc("强制骑上一只不知道是什么的东西，然后把你甩出去，末尾再安排一只追兵")
                .weight(55)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.mount(ctx.pick(TrollPools.RIDEABLE));
                    ctx.setSprinting(true);
                    ctx.fling(0.0, 1.1, 0.0);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.2f, 0.9f);
                    ctx.actionBar("§e你的坐骑不归你管，你的方向也不归你管。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_pit_and_punish", "陷坑惩罚", TrollTier.BRUTAL)
                .desc("挖坑、前后夹击刷怪、缓慢药水、巨响加恐怖粒子，掉下去只是流程第一步")
                .weight(62)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.voidPit(1, 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.effect(StatusEffects.SLOWNESS, 200, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.8f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_trap_garden", "陷阱花园", TrollTier.BRUTAL)
                .desc("周围撒满陷阱方块、随机药水、假广播说环境美化完成，走路都能走出事故")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.TRAP_BLOCKS), 14);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                    ctx.effectRandom(300, 0);
                    ctx.title("§6§l你家装修好了", "§7免费，不用谢");
                    ctx.fakeBroadcast("§e检测到玩家周边环境美化工程已完成。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_cursed_loot", "诅咒战利品", TrollTier.BRUTAL)
                .desc("塞你一件神秘物品、随手改个羞辱名字、挂上消失诅咒，再补一段骷髅粒子")
                .weight(58)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.MYSTERY_ITEMS), 1);
                    ctx.renameRandomItem(ctx.pick(TrollPools.CURSED_NAMES));
                    ctx.curseHeld();
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.1f, 0.9f);
                    ctx.particleAtPlayer(ParticleTypes.SOUL, 30);
                    ctx.warn("好东西都带点代价，这不是常识吗。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_endless_night", "无尽长夜", TrollTier.BRUTAL)
                .desc("直接拨到午夜、雷暴拉满、三向刷怪、黑暗药水、巨响标题——太阳今天不排班")
                .weight(65)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.timeOfDay(18000L);
                    ctx.thunderstorm(2400);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.effect(StatusEffects.DARKNESS, 300, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.2f, 0.7f);
                    ctx.title("§8§l太阳已下架", "§7今晚没有月亮，也没有明天");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_siege_line", "围城线", TrollTier.BRUTAL)
                .desc("四向刷怪、虚弱药水、全服巨响加全服公告，包围圈形成后不接受投降")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.effect(StatusEffects.WEAKNESS, 300, 0);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.0f, 0.9f);
                    ctx.sendToAll("§c[末日] 包围圈已形成，请勿走动，走了也没用。");
                })
                .build());

        // ==================== NIGHTMARE ====================

        TrollEvents.reg(TrollEvent.of("doom_meteor_storm", "陨石风暴", TrollTier.NIGHTMARE)
                .desc("八连陨石 + 雷暴 + 全服标题 + 全服巨响 + 画面撕裂，坏消息由全服共享")
                .weight(35)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.meteorShower(8, 24.0);
                    ctx.thunderstorm(1200);
                    ctx.titleToAll("§4§l陨石风暴", "§c本次天灾为全服共享套餐");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.4f, 0.7f);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 120, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_void_exile", "虚空流放", TrollTier.NIGHTMARE)
                .desc("脚下挖空十二格、浮空药水、两向刷怪、硬黑屏和结局级巨响，你被流放了")
                .weight(32)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.voidPit(3, 12);
                    ctx.effect(StatusEffects.LEVITATION, 60, 1);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 100, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.4f, 0.6f);
                    ctx.warn("你脚下那块地我们已经回收了，连同地基一起。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_lava_arena", "熔岩竞技场", TrollTier.NIGHTMARE)
                .desc("脚下铺满岩浆、缓慢加凋零、背后贴脸刷怪，全服标题宣布第一轮选手是你")
                .weight(30)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.lavaPit(3);
                    ctx.effect(StatusEffects.SLOWNESS, 200, 1);
                    ctx.effect(StatusEffects.WITHER, 160, 0);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.titleToAll("§c§l熔岩竞技场开赛", "§7第一轮选手：你");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.3f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_obsidian_tomb", "黑曜石棺材", TrollTier.NIGHTMARE)
                .desc("黑曜石原地封棺、失明、塞两只怪进去、血量压到四颗心，出口只有重生点")
                .weight(34)
                .cooldownSeconds(480)
                .when(ctx -> ctx.isOnGround() && !ctx.isInLava())
                .run(ctx -> {
                    ctx.cageWith(Blocks.OBSIDIAN);
                    ctx.effect(StatusEffects.BLINDNESS, 200, 0);
                    ctx.spawnAny(EntityType.ZOMBIE, ctx.x(), ctx.y(), ctx.z());
                    ctx.spawnAny(EntityType.SKELETON, ctx.x(), ctx.y(), ctx.z());
                    ctx.setHealth(Math.min(ctx.health(), 8.0f));
                    ctx.soundSelf(SoundEvents.BLOCK_ANVIL_LAND, 1.2f, 0.6f);
                    ctx.warn("棺材用黑曜石打的，算是对你审美的一点尊重。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_fake_ban_wave", "假封禁潮", TrollTier.NIGHTMARE)
                .desc("假封禁界面 + 假病毒警告 + 海量假聊天 + 假广播封禁理由 + 结局级巨响")
                .weight(33)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_BAN, 140, 3);
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 120, 2);
                    ctx.glitch(GlitchIds.MASS_CHAT, 100, 2);
                    ctx.fakeBroadcast("§c封禁理由：" + ctx.pick(TrollPools.FAKE_EXCUSES));
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.3f, 0.7f);
                    ctx.warn("申诉通道已关闭，因为它从来没有存在过。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_world_reload_loop", "假重载循环", TrollTier.NIGHTMARE)
                .desc("假世界重载 + 假更新卡 99% + 硬黑屏 + 假管理员发言 + 假保存，一条龙劝退")
                .weight(32)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 160, 3);
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 120, 2);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 80, 2);
                    ctx.fakeChat("服务器管理员", ctx.pick(TrollPools.FAKE_OPERATOR_LINES));
                    ctx.fakeSaveNotice();
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.2f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_input_slavery", "输入奴隶", TrollTier.NIGHTMARE)
                .desc("前后左右反转 + 输入冻结 + 灵敏度拉满 + 强制潜行，再顺手放两只怪进来")
                .weight(30)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 160, 3);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 100, 2);
                    ctx.glitch(GlitchIds.SENS_MAX, 120, 2);
                    ctx.freeze(240);
                    ctx.setSneaking(true);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 0.7f);
                    ctx.warn("你的手现在归模组管，方向感也是。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_sky_hammer", "天锤", TrollTier.NIGHTMARE)
                .desc("高空陨石 + 双闪雷电 + 雷声 + 剧烈甩镜 + 天罚标题，这不是比喻")
                .weight(31)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.meteorShower(5, 32.0);
                    ctx.lightningAt(ctx.x() + ctx.ri(-5, 5), ctx.y(), ctx.z() + ctx.ri(-5, 5));
                    ctx.lightningAt(ctx.x() + ctx.ri(-5, 5), ctx.y(), ctx.z() + ctx.ri(-5, 5));
                    ctx.soundSelf(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 1.4f, 1.0f);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 60, 3);
                    ctx.title("§4§l天锤落下", "§7请保持站立，方便瞄准");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_grave_collapse", "墓地塌陷", TrollTier.NIGHTMARE)
                .desc("挖空脚下八格、三向刷怪、眩晕药水、恐怖粒子加巨响，墓地自己先塌了")
                .weight(33)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.voidPit(4, 8);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.effect(StatusEffects.NAUSEA, 300, 2);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 60);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.3f, 0.6f);
                    ctx.fakeBroadcast("§8本地块的承载能力已到期，请自行处理坠落。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_flood_of_beasts", "兽潮", TrollTier.NIGHTMARE)
                .desc("六向刷怪 + 把附近玩家全拉过来 + 全服失明 + 全服标题和巨响，一个都别想划水")
                .weight(35)
                .cooldownSeconds(460)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.pullPlayersToMe(64.0);
                    ctx.effectAll(StatusEffects.BLINDNESS, 120, 0);
                    ctx.titleToAll("§4§l兽潮", "§7它们只对活物感兴趣");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.3f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_tnt_chain", "连锁爆破", TrollTier.NIGHTMARE)
                .desc("三枚延时 TNT 布成三角 + 原地无伤爆炸 + 缓慢药水 + 剧烈甩镜 + 结局级巨响")
                .weight(34)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x() + 4.0, ctx.y() + 1.0, ctx.z(), 30);
                    ctx.spawnTnt(ctx.x() - 4.0, ctx.y() + 1.0, ctx.z(), 45);
                    ctx.spawnTnt(ctx.x(), ctx.y() + 1.0, ctx.z() + 4.0, 60);
                    ctx.boom(ctx.x(), ctx.y() + 1, ctx.z(), 3.0f);
                    ctx.effect(StatusEffects.SLOWNESS, 160, 1);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 80, 3);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.4f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_swap_doom", "换位末日", TrollTier.NIGHTMARE)
                .desc("和另一名玩家互换位置，把人扔到三十格外，再全服上眩晕、塞危险品、泼脏水")
                .weight(30)
                .cooldownSeconds(480)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    var o = ctx.other();
                    if (o != null) {
                        ctx.swapWith(o);
                        ctx.teleportOtherTo(o, ctx.x() + 30.0, ctx.y() + 12.0, ctx.z() + 30.0);
                        ctx.giveOther(o, ctx.pick(TrollPools.DANGEROUS), 1);
                        ctx.msgToOther(o, "§7刚才那一下不是我干的，真的。");
                        ctx.effectAll(StatusEffects.NAUSEA, 200, 1);
                        ctx.titleToAll("§5§l位置已重新分配", "§7谁站哪儿由模组说了算");
                        ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.2f, 0.9f);
                    }
                })
                .build());

        // ==================== APOCALYPSE ====================

        TrollEvents.reg(TrollEvent.of("doom_meteor_apocalypse", "陨石天启", TrollTier.APOCALYPSE)
                .desc("二十连陨石 + 雷暴 + 午夜 + 全服凋零 + 全服标题 + 假崩溃，天灾由全服平摊")
                .weight(18)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.meteorShower(20, 40.0);
                    ctx.thunderstorm(4800);
                    ctx.timeOfDay(18000L);
                    ctx.effectAll(StatusEffects.WITHER, 200, 1);
                    ctx.titleToAll("§4§l陨石天启", "§4全服共享，一个都跑不掉");
                    ctx.glitch(GlitchIds.FAKE_CRASH, 140, 3);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_world_end", "世界终章", TrollTier.APOCALYPSE)
                .desc("十二连陨石 + 挖空十六格 + 岩浆铺地 + 假世界重载 + 假存档损坏 + 全服悼词")
                .weight(15)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.meteorShower(12, 30.0);
                    ctx.voidPit(4, 16);
                    ctx.lavaPit(3);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 200, 3);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 160, 3);
                    ctx.sendToAll("§4§l[末日] 世界数据正在被重写，请不要保存，反正也保存不了。");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_judgment_day", "审判日", TrollTier.APOCALYPSE)
                .desc("全服凋零加黑暗、雷劈其他玩家、地面近处落雷、硬黑屏，所有人的行为都被记录了")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.effectAll(StatusEffects.WITHER, 300, 1);
                    ctx.effectAll(StatusEffects.DARKNESS, 300, 0);
                    var o = ctx.other();
                    if (o != null) {
                        ctx.lightningAtPlayer(o);
                        ctx.damageOther(o, 4.0f);
                    }
                    ctx.lightningNear();
                    ctx.titleToAll("§4§l审判日", "§7你的所有行为都已被记录（并没有）");
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 160, 3);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_no_escape", "无处可逃", TrollTier.APOCALYPSE)
                .desc("基岩封场、脚下铺岩浆、冻结定身、凋零加身、血量压到一格半，出口只剩重生点")
                .weight(16)
                .cooldownSeconds(900)
                .once()
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.cageWith(Blocks.BEDROCK);
                    ctx.lavaPit(1);
                    ctx.freeze(300);
                    ctx.effect(StatusEffects.WITHER, 200, 1);
                    ctx.spawnAny(EntityType.ZOMBIE, ctx.x(), ctx.y(), ctx.z());
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 140, 3);
                    ctx.setHealth(3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.5f, 0.5f);
                    ctx.warn("这间屋子没有门，只有一条出路叫死亡。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_singularity", "奇点坍缩", TrollTier.APOCALYPSE)
                .desc("挖空二十格、失重加浮空、眩晕画面、满屏传送门粒子，你已经不在原来的坐标上")
                .weight(20)
                .cooldownSeconds(720)
                .run(ctx -> {
                    ctx.voidPit(5, 20);
                    ctx.noGravity(200);
                    ctx.effect(StatusEffects.LEVITATION, 120, 1);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 200, 3);
                    ctx.particleAtPlayer(ParticleTypes.PORTAL, 100);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.5f, 0.5f);
                    ctx.title("§5§l奇点坍缩", "§7欢迎来到没有地面的世界");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_mass_extinction", "万物灭绝", TrollTier.APOCALYPSE)
                .desc("十六连陨石 + 八向怪潮 + 全服凋零 + 海量假聊天刷屏 + 全服灭绝公告")
                .weight(14)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.meteorShower(16, 26.0);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.effectAll(StatusEffects.WITHER, 240, 1);
                    ctx.glitch(GlitchIds.MASS_CHAT, 200, 3);
                    ctx.titleToAll("§4§l万物灭绝", "§8本次没有幸存者名单");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.6f, 0.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_hell_on_earth", "人间地狱", TrollTier.APOCALYPSE)
                .desc("脚下岩浆 + 雷暴 + 午夜 + 四向怪潮 + 凋零 + 假内存溢出，把世界调成地狱难度")
                .weight(19)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.thunderstorm(3600);
                    ctx.timeOfDay(18000L);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.effect(StatusEffects.WITHER, 200, 1);
                    ctx.glitch(GlitchIds.FAKE_OOM, 120, 2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_countdown", "末日倒计时", TrollTier.APOCALYPSE)
                .desc("全服倒计时标题 + 假死亡提示 + 假更新卡 99% + 六连陨石，数到零就开砸")
                .weight(21)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.titleToAll("§4§l3", "§c最后三秒，请保持体面");
                    ctx.fakeDeathScreen(ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.fakeSaveNotice();
                    ctx.glitch(GlitchIds.FAKE_UPDATE, 140, 3);
                    ctx.meteorShower(6, 28.0);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.6f);
                    ctx.sendToAll("§4[末日] 倒计时结束后请自行处理后果。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_apocalypse_protocol", "末日协议", TrollTier.APOCALYPSE)
                .desc("把一百二十格内的玩家全拉过来、全服失明、脚下铺岩浆、五向怪潮、假病毒警告")
                .weight(17)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.pullPlayersToMe(120.0);
                    ctx.effectAll(StatusEffects.BLINDNESS, 160, 1);
                    ctx.lavaPit(2);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.titleToAll("§4§l末日协议已启动", "§7所有玩家合并进同一个坏消息");
                    ctx.glitch(GlitchIds.FAKE_VIRUS, 120, 2);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_extinction_event", "灭绝事件", TrollTier.APOCALYPSE)
                .desc("十连陨石 + 监守者入场 + 全服双重负面 + 假封禁界面 + 存活率公告：0%")
                .weight(16)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.meteorShower(10, 35.0);
                    ctx.spawnHostileNear(EntityType.WARDEN, 6);
                    ctx.effectAll(StatusEffects.WITHER, 200, 2);
                    ctx.effectAll(StatusEffects.SLOWNESS, 300, 2);
                    ctx.glitch(GlitchIds.FAKE_BAN, 140, 3);
                    ctx.sendToAll("§4[灭绝事件] 本次事件的存活率已统计完毕：0%。");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.6f, 0.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_final_save", "最终保存", TrollTier.APOCALYPSE)
                .desc("假保存 + 假世界重载 + 假崩溃 + 硬黑屏 + 八连陨石，并告诉你玩家数据已归档")
                .weight(13)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.fakeSaveNotice();
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 200, 3);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 160, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 120, 3);
                    ctx.sendToAll("§4§l[系统] 世界已保存。玩家数据：已归档进模组的收藏夹。");
                    ctx.meteorShower(8, 24.0);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.6f, 0.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("doom_last_light", "最后一缕光", TrollTier.APOCALYPSE)
                .desc("午夜、全服黑暗、挖空十四格、硬黑屏、环境恐怖音，然后是标题——然后没有然后")
                .weight(22)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.timeOfDay(18000L);
                    ctx.effectAll(StatusEffects.DARKNESS, 400, 0);
                    ctx.voidPit(3, 14);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 140, 3);
                    ctx.soundToAll(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.4f, 0.6f);
                    ctx.titleToAll("§8§l最后一缕光", "§7然后就没有然后了");
                })
                .build());
    }
}
