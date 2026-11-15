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

        TrollEvents.reg(TrollEvent.of("chaos_false_alarm", "假警报", TrollTier.BRUTAL)
                .desc("音效、标题、假聊天、烟雾四套一起上，然后什么都没有发生——这才是最气人的")
                .weight(120)
                .cooldownSeconds(45)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7（其实什么都没有）", 0, 30, 12);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), ctx.pick(TrollPools.FAKE_LINES));
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 30);
                    ctx.msg("§8[提示] 警报解除。你刚才那一跳，我们截图了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_mystery_gift", "神秘礼物", TrollTier.ANNOYING)
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

        TrollEvents.reg(TrollEvent.of("chaos_triple_combo", "三连击", TrollTier.NIGHTMARE)
                .desc("音效、传送、洗背包老三样一起打包，外加一次原地转圈——你连自己为什么在这儿都想不起来")
                .weight(140)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.6f, 0.6f);
                    ctx.teleport(ctx.x() + ctx.ri(-20, 20), ctx.y(), ctx.z() + ctx.ri(-20, 20));
                    ctx.shuffleInventory();
                    ctx.spin(360.0f);
                    ctx.glitch(GlitchIds.SHAKY, 60, 1);
                    ctx.msg("§8第一下拿走你的方向，第二下拿走你的东西，第三下拿走你的判断。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_hotbar_chaos", "快捷栏抽奖", TrollTier.NIGHTMARE)
                .desc("洗乱快捷栏、锁死槽位、强制随机切格：你有武器，但你的手指永远慢一步")
                .weight(150)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 200, 2);
                    ctx.glitch(GlitchIds.HOTBAR_JUMP, 200, 2);
                    ctx.randomSelectSlot();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§8它开始打你的时候，你的手正忙着找那把剑。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_potion_roulette", "药水轮盘", TrollTier.NIGHTMARE)
                .desc("先清空你全部增益，再灌三瓶随机负面，最后放一段笑声当背景音乐")
                .weight(130)
                .cooldownSeconds(70)
                .when(ctx -> !ctx.isInLava())
                .run(ctx -> {
                    ctx.clearEffects();
                    ctx.effectRandom(300, 1);
                    ctx.effectRandom(300, 1);
                    ctx.effectRandom(300, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.2f, 0.7f);
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7三瓶都中了，没有一瓶是好东西");
                    ctx.addRage(9);
                })
                .build());

        // ==================== BRUTAL ====================

        TrollEvents.reg(TrollEvent.of("chaos_mob_ambush", "背后有货", TrollTier.NIGHTMARE)
                .desc("背后两只、身边两只、失明一次、惨叫一声——它们分好了工，你只需要站在原地")
                .weight(70)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.6f, 0.6f);
                    ctx.glitch(GlitchIds.BLACKOUT, 50);
                    ctx.addRage(11);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_lightning_judgment", "天谴", TrollTier.APOCALYPSE)
                .desc("雷暴、午夜、三连闪电加上怪潮：天气、方块与刷怪系统一起宣判，你连上诉的窗口都没有")
                .weight(60)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.isInWater())
                .run(ctx -> {
                    ctx.thunderstorm(1200);
                    ctx.timeOfDay(18000L);
                    ctx.lightning();
                    ctx.lightningNear();
                    ctx.lightningNear();
                    ctx.setFire(6);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    }
                    ctx.title("§4§l天谴", "§8本次判决不接受任何申诉");
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_gift_and_boom", "先给你，再拿走", TrollTier.APOCALYPSE)
                .desc("真的发钻石、真的有领奖音效、真的让你高兴了三秒——然后把礼物、背包和你一起炸了")
                .weight(65)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 3);
                    ctx.playLevelUp();
                    ctx.ok("恭喜！你中了本服今日的唯一一份大奖。");
                    ctx.later(70, () -> {
                        ctx.takeRandomItems(3);
                        ctx.boomAtPlayer(2.5f);
                        ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 70);
                        ctx.warn("大奖的兑奖方式：当场回收，连本带利。");
                        ctx.msg("§8那三秒的开心是真的。这也是它唯一没骗你的地方。");
                        ctx.addRage(15);
                    });
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_teleport_mugging", "定点收割", TrollTier.NIGHTMARE)
                .desc("有队友就把你送到队友的刀口上，没有就把你扔到两百格外，顺手再拿走一件东西")
                .weight(70)
                .cooldownSeconds(260)
                .run(ctx -> {
                    if (ctx.hasOthers()) {
                        ctx.teleportToPlayer(ctx.other());
                        ctx.msg("§c你被送到了最近的人面前。它希望你们之间发生点什么。");
                    } else {
                        ctx.scatterFar(200);
                        ctx.msg("§8两百格外，没有人和你说话。");
                    }
                    ctx.takeRandomItems(2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 0.7f);
                    ctx.addRage(11);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_inventory_purge", "清仓", TrollTier.APOCALYPSE)
                .desc("清空背包、塞满垃圾、身边刷怪、广播说你搬家了——四套系统同时动手，你负责站在原地被搬空")
                .weight(40)
                .cooldownSeconds(600)
                .once()
                .run(ctx -> {
                    ctx.clearInventory();
                    ctx.giveRandomJunk();
                    ctx.giveRandomJunk();
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    }
                    ctx.fakeBroadcast("§e[搬迁通知] " + ctx.playerName() + " 的全部家当已完成转移，感谢配合。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_lava_baptism", "岩浆洗礼", TrollTier.APOCALYPSE)
                .desc("脚下铺开岩浆、全身点燃，再配一场岩浆粒子——洗礼只有一次，也没有第二次机会")
                .weight(55)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.isInLava())
                .run(ctx -> {
                    ctx.lift(3);
                    ctx.lavaPit(2);
                    ctx.setFire(10);
                    ctx.fling(0.0, -0.4, 0.0);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 70);
                    ctx.title("§c§l洗礼开始", "§8受洗者：你。施洗者：地板。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_ten_combo", "十连整人", TrollTier.APOCALYPSE)
                .desc("音效、传送、洗包、丢手、随机药水、画面抖动、贴脸刷怪、粒子、着火——十步一次结算，中间没有停顿")
                .weight(45)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.6f, 0.6f);
                    ctx.scatter(50);
                    ctx.shuffleInventory();
                    ctx.dropHeld();
                    ctx.effectRandom(300, 1);
                    ctx.glitch(GlitchIds.SHAKY, 120, 2);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 60);
                    ctx.setFire(5);
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7十步走完，你还在原地");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_scramble_swarm", "手忙脚乱", TrollTier.APOCALYPSE)
                .desc("洗乱快捷栏、丢掉手里的东西、刷一群怪、锁死槽位：你有还手的能力，只是永远差半秒")
                .weight(60)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.dropHeld();
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    }
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 260, 2);
                    ctx.damage("magic", 2.0f);
                    ctx.msg("§8它们不着急。它们知道你按不出那一下。");
                    ctx.addRage(15);
                })
                .build());

        // ==================== NIGHTMARE ====================

        TrollEvents.reg(TrollEvent.of("chaos_bedrock_cage", "基岩牢笼", TrollTier.APOCALYPSE)
                .desc("用基岩把你原地封死，塞两只怪进来，再灌凋零与失明——唯一的出口写着你的重生点")
                .weight(35)
                .cooldownSeconds(480)
                .when(ctx -> !ctx.isInLava() && ctx.isOnGround())
                .run(ctx -> {
                    ctx.cageWith(ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.setBlock(0, 3, 0, ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 1);
                    ctx.glitch(GlitchIds.HIDE_HUD, 200, 1);
                    ctx.title("§4§l牢笼已合拢", "§8你可以在里面慢慢想清楚");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_fake_crash_siege", "崩溃围城", TrollTier.APOCALYPSE)
                .desc("先给你假崩溃界面再撤掉 HUD，趁你手忙脚乱时刷满怪、放雷暴、贴脸惨叫")
                .weight(32)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 80, 2);
                    ctx.glitch(GlitchIds.HIDE_HUD, 300, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.6f, 0.6f);
                    ctx.thunderstorm(900);
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    }
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7界面没了，它们还在");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_fake_death_blackout", "假死黑屏", TrollTier.OBLIVION)
                .desc("扣血、假死亡界面、黑屏、刷屏假死因、假聊天，五套系统演一出你已经死了的戏；演完之后，戏变成真的")
                .weight(34)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.damage("magic", 2.0f);
                    ctx.glitch(GlitchIds.FAKE_DEATH, 90, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 90, 3);
                    ctx.playHurtSound();
                    ctx.broadcast("§7☠ §c" + ctx.playerName() + " §7" + ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "刚看到播报了……你还好吗？");
                    ctx.later(100, () -> {
                        ctx.fakeChat("§8系统", "死亡已被记录。正在处理遗体……");
                        ctx.msg("§7（你还活着。但系统坚持认为你死了。）");
                    });
                    ctx.later(190, () -> {
                        ctx.fakeChat("§8系统", "遗体处理完成。你没有在名单上。");
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 2);
                        ctx.buryAlive();
                        ctx.addRage(14);
                    });
                    ctx.addRage(12);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_sky_abyss", "天空监狱", TrollTier.OBLIVION)
                .desc("清空全部增益、抬到三百格高空、清掉缓降、反转视角，再广播一句祝你落地平安——而地面离你很远")
                .weight(30)
                .cooldownSeconds(480)
                .when(ctx -> !ctx.isFalling())
                .run(ctx -> {
                    ctx.clearEffects();
                    ctx.teleport(ctx.x(), 300.0, ctx.z());
                    ctx.lift(6);
                    ctx.randomRotation();
                    ctx.glitch(GlitchIds.INVERT_PITCH, 120, 2);
                    ctx.fakeBroadcast("§e[塔台] 检测到乘客 §b" + ctx.playerName() + "§e 已登机，祝落地平安。");
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "等等，你背上没有缓降……");
                    ctx.msg("§8往下看是地面。往上看是刚才那个把你送上来的人。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_flashbang_horde", "闪光怪潮", TrollTier.NIGHTMARE)
                .desc("闪白巨响、剧烈甩镜、爆炸特效、四只怪加虚弱——你恢复视力的那一刻，它们已经站好位了")
                .weight(33)
                .cooldownSeconds(460)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FLASHBANG, 30, 3);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 60, 3);
                    ctx.explosionFx();
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    }
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_control_sabotage", "操作台被黑", TrollTier.APOCALYPSE)
                .desc("灵敏度归零、鼠标反转、视野抖动、眩晕、远距离传送加刷怪——六套系统一起瘫痪你的手")
                .weight(30)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SENS_ZERO, 300, 2);
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 300, 2);
                    ctx.glitch(GlitchIds.SHAKY, 300, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.scatterFar(120);
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    }
                    ctx.title("§8§l控制权已移交", "§7接收方不打算还给你");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_blood_moon", "血月当空", TrollTier.OBLIVION)
                .desc("强制午夜、雷暴、五只怪、恐怖音效和血月广播同时上线；月亮红起来的时候，你已经不在名单上了")
                .weight(35)
                .cooldownSeconds(450)
                .run(ctx -> {
                    ctx.timeOfDay(18000L);
                    ctx.thunderstorm(1400);
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.7f, 0.55f);
                    ctx.broadcast("§4血月升起。今晚没有任何人被允许活到天亮。");
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 160, 2);
                    ctx.title("§4§l血月", "§8抬头看一眼，然后低头跑");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_scorched_earth", "焦土", TrollTier.OBLIVION)
                .desc("破坏性爆炸、遍地火焰、雷暴与怪潮一起铺开；它不打算占这块地，只想让你也待不下去")
                .weight(28)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 2.5f);
                    ctx.setFire(12);
                    ctx.flameBurst();
                    ctx.thunderstorm(1200);
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    }
                    ctx.fakeBroadcast("§4[警报] 本区域已被标记为不可居住。请自行安排后事。");
                    ctx.addRage(19);
                })
                .build());

        // ==================== APOCALYPSE ====================

        TrollEvents.reg(TrollEvent.of("chaos_ultimate_combo", "终极连击", TrollTier.OBLIVION)
                .desc("假崩溃、隐藏 HUD、假标题界面、洗背包、四只贴脸怪、雷暴一次全给；中间还会小小地「恢复」一下，好让你看清代价")
                .weight(20)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 80, 3);
                    ctx.glitch(GlitchIds.HIDE_HUD, 260, 3);
                    ctx.later(80, () -> {
                        ctx.glitch(GlitchIds.MENU_TEASE, 70, 2);
                        ctx.takeRandomItems(3);
                        ctx.msg("§7（界面回来了一点。东西没有。）");
                    });
                    ctx.later(170, () -> {
                        ctx.thunderstorm(1200);
                        for (int i = 0; i < 4; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                        }
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 2);
                        ctx.buryAlive();
                        ctx.title("§4§l终极连击", "§8结算完毕，谢绝找零");
                        ctx.addRage(20);
                    });
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_apocalypse_now", "末日降临", TrollTier.APOCALYPSE)
                .desc("午夜雷暴、首领级怪群、黑暗与凋零、连环爆炸、末日标题——全场最狠的一道天罚，没有第二个解释")
                .weight(15)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.timeOfDay(18000L);
                    ctx.thunderstorm(1600);
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                    }
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    }
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 380, 2);
                    ctx.boomAtPlayer(2.5f);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 80, 3);
                    ctx.title("§4§l末日降临", "§8本次没有幸存者名单");
                    ctx.addRage(22);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_doom_arena", "死亡竞技场", TrollTier.OBLIVION)
                .desc("蛛网封场、怪潮入场、虚弱失明凋零三件套、血量压到两格，然后广播宣布比赛开始——没有裁判，也没有规则")
                .weight(22)
                .cooldownSeconds(720)
                .run(ctx -> {
                    ctx.surroundWith(ctx.pick(TrollPools.TROLL_BLOCKS));
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    }
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 360, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 360, 1);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 360, 1);
                    ctx.setHealth(4.0f);
                    ctx.glitch(GlitchIds.HIDE_HUD, 200, 2);
                    ctx.broadcast("§4[竞技场] 第 " + ctx.ri(1, 999) + " 场比赛开始。参赛者一名，观众零名。");
                    ctx.title("§4§l比赛开始", "§8唯一规则：不要死。很遗憾，这条也不保证。");
                    ctx.addRage(21);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_everything_broken", "万物崩坏", TrollTier.OBLIVION)
                .desc("清增益、灌四瓶负面、洗背包、丢装备、远距离传送、刷怪、眩晕画面——九套系统一次性崩给你看，修不回来")
                .weight(18)
                .cooldownSeconds(600)
                .once()
                .run(ctx -> {
                    ctx.clearEffects();
                    ctx.effectRandom(360, 2);
                    ctx.effectRandom(360, 2);
                    ctx.effectRandom(360, 1);
                    ctx.effectRandom(360, 1);
                    ctx.shuffleInventory();
                    ctx.dropRandom();
                    ctx.dropRandom();
                    ctx.scatterFar(180);
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    }
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 200, 2);
                    ctx.title("§4§l万物崩坏", "§8九套系统同时失效，包括你");
                    ctx.addRage(22);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("chaos_last_stand", "最后三秒", TrollTier.OBLIVION)
                .desc("假存档损坏、屏闪甩镜、假玩家加入围观、怪潮合围，再用广播念一段属于你的悼词——你还能听见，这就是最残忍的地方")
                .weight(12)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 80, 3);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 60, 3);
                    ctx.randomRotation();
                    ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), true);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "都来看，他好像撑不住了。");
                    ctx.later(70, () -> {
                        for (int i = 0; i < 6; i++) {
                            ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                        }
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 320, 2);
                        ctx.broadcast("§7「他来过。他挖过。他最后什么都没带走。」");
                        ctx.title("§4§l最后三秒", "§8第一秒给你，第二秒给他，第三秒没有人的");
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 80, 3);
                        ctx.stripAll();
                        ctx.addRage(22);
                    });
                    ctx.addRage(18);
                })
                .build());
    }
}
