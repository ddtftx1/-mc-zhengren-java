package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【控制与傀儡】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。</p>
 *
 * <p>主题：强制操作玩家（视角 / 移动 / 潜行 / 冲刺 / 骑乘 / 丢物品 / 切槽位）、
 * 夺舍感、假玩家替你行动、被操控的错觉、身体不听使唤、动作被劫持、输入失灵。</p>
 */
public final class PuppetEvents {

    private PuppetEvents() {
    }

    public static void register() {
        TrollEvents.category("控制与傀儡");

        // ==================== PRANK ====================

        TrollEvents.reg(TrollEvent.of("puppet_wave_hello", "替你挥手", TrollTier.ANNOYING)
                .desc("视角被轻轻拧向旁边，像有人在替你打招呼，然后系统说对方没回应")
                .weight(120)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.spin(ctx.rf(-40f, 40f));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.2f);
                    ctx.actionBar("§7你的手刚才动了一下，不是你动的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_nod_yes", "替你点头", TrollTier.BRUTAL)
                .desc("脑袋被按下去又抬起来，完成一次标准同意，你本人没有发言权")
                .weight(118)
                .cooldownSeconds(45)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.setRotation(ctx.rf(-180f, 180f), 60f);
                    ctx.later(20, () -> ctx.setRotation(ctx.rf(-180f, 180f), -20f));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.3f);
                    ctx.msg("§7记录在案：你同意了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_look_left", "强制回头", TrollTier.ANNOYING)
                .desc("后脑勺被拍了一下，视角自动转回去，那边当然什么都没有")
                .weight(116)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.setRotation(ctx.rf(-180f, 180f), ctx.rf(-10f, 10f));
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.1f);
                    ctx.actionBar("§8回头看看吧，反正也看不见东西。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_sneak_greeting", "强制潜行打招呼", TrollTier.ANNOYING)
                .desc("你被迫蹲下向空气问好，三秒后系统才把姿势还给你")
                .weight(112)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.setSneaking(true);
                    ctx.later(60, () -> ctx.setSneaking(false));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.0f);
                    ctx.msg("§7刚才那个姿势叫礼貌，不叫害怕。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_sprint_burst", "冲刺一小步", TrollTier.BRUTAL)
                .desc("腿突然自己动了两下，冲出去一小段，方向由脚决定")
                .weight(115)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.setSprinting(true);
                    ctx.fling(ctx.rf(-0.4f, 0.4f), 0.2, ctx.rf(-0.4f, 0.4f));
                    ctx.later(40, () -> ctx.setSprinting(false));
                    ctx.actionBar("§7你的腿刚才有自己的想法。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_slot_musical", "快捷栏点歌", TrollTier.BRUTAL)
                .desc("快捷栏被挨个点了一遍，像在弹琴，音效很配合，东西一件没少")
                .weight(110)
                .cooldownSeconds(50)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.randomSelectSlot();
                    ctx.randomSelectSlot();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.msg("§7曲目：《我到底拿了什么》");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_spin_dance", "原地转圈", TrollTier.ANNOYING)
                .desc("你的身体决定跳一支舞，转完一圈之后没人鼓掌")
                .weight(113)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.spin(360f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 18);
                    ctx.ok("精彩。请继续你的表演。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_mount_prank", "免费坐骑体验", TrollTier.BRUTAL)
                .desc("你被塞上一只不知道是什么的东西，体验三秒后系统礼貌地请你下来")
                .weight(108)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.mount(ctx.pick(TrollPools.RIDEABLE));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.later(60, () -> ctx.dismount());
                    ctx.msg("§7试乘结束，车费另计。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_hide_and_seek", "隐身捉迷藏", TrollTier.ANNOYING)
                .desc("你被藏了起来，但没人来找，五秒后系统自己结束了游戏")
                .weight(106)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.setInvisible(true);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.2f);
                    ctx.later(100, () -> {
                        ctx.setInvisible(false);
                        ctx.msg("§7捉迷藏结束，你是唯一参赛者。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_strings_tug", "提线被拽", TrollTier.PRANK)
                .desc("头顶好像有线被拉了一下，你整个人往上提了提，又落回来")
                .weight(104)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.lift(1.5);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.4f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 14);
                    ctx.actionBar("§7线在你身上，握线的人不在。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_fake_speech", "假玩家替你发言", TrollTier.ANNOYING)
                .desc("以你的名义伪造一句话发到聊天栏，内容相当丢人")
                .weight(109)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeChat(ctx.playerName(), ctx.pick(TrollPools.FAKE_LINES));
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "刚那句话真是你说的吗");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.1f);
                    ctx.warn("该发言已归档，无法撤回。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_pose_audit", "姿势审计", TrollTier.BRUTAL)
                .desc("系统抽查你的站姿，强制蹲下、站起、再蹲下，最后判定不合格")
                .weight(102)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.setSneaking(true);
                    ctx.later(40, () -> ctx.setSneaking(false));
                    ctx.later(80, () -> ctx.setSneaking(true));
                    ctx.later(120, () -> {
                        ctx.setSneaking(false);
                        ctx.sys("姿势审计结果：不合格，但没人管。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_rotation_lottery", "视角抽奖", TrollTier.BRUTAL)
                .desc("你获得了随机朝向一份，抽到什么看什么，不接受换货")
                .weight(111)
                .cooldownSeconds(45)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.randomRotation();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.3f);
                    ctx.actionBar("§7恭喜抽中朝向：" + ctx.ri(-180, 180) + " 度。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_hand_tremor", "手抖", TrollTier.ANNOYING)
                .desc("手腕莫名其妙抖了一下，视角跟着晃，像刚举完铁")
                .weight(107)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.spin(ctx.rf(-12f, 12f));
                    ctx.setRotation(ctx.rf(-180f, 180f), ctx.rf(-30f, 30f));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.7f, 1.5f);
                    ctx.msg("§7医生说这是紧张，模组说这是设计。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_ghost_step", "幽灵步伐", TrollTier.BRUTAL)
                .desc("脚自己往前挪了三格，还留下一点烟雾，像有人替你走了一步")
                .weight(105)
                .cooldownSeconds(55)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.scatter(3);
                    ctx.smokeBurst();
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.0f);
                    ctx.actionBar("§8刚才那一步的署名不是你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_salute", "强制敬礼", TrollTier.BRUTAL)
                .desc("你的头被按低到标准敬礼角度，维持一会儿，仪式感拉满")
                .weight(100)
                .cooldownSeconds(65)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.setRotation(ctx.rf(-180f, 180f), -60f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.later(70, () -> ctx.setRotation(ctx.rf(-180f, 180f), 0f));
                    ctx.msg("§7礼毕。你可以抬头了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_countdown_pose", "倒数姿势", TrollTier.PRANK)
                .desc("倒计时开始时你被按住蹲好，倒数结束时又莫名站直，全程没搞懂在数什么")
                .weight(98)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.setSneaking(true);
                    ctx.title("§e§l3", "§7请保持标准蹲姿");
                    ctx.later(60, () -> {
                        ctx.setSneaking(false);
                        ctx.title("§a§l0", "§7姿势合格，但没有奖品");
                    });
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_mirror_self", "镜中人", TrollTier.BRUTAL)
                .desc("你向左转，身体偏要向右，像在跟镜子里的自己闹别扭")
                .weight(103)
                .cooldownSeconds(50)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.spin(-ctx.rf(20f, 60f));
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.2f);
                    ctx.msg("§7镜中人先动的，别赖我。");
                })
                .build());

        // ==================== ANNOYING ====================

        TrollEvents.reg(TrollEvent.of("puppet_backwards_walk", "倒着走", TrollTier.NIGHTMARE)
                .desc("前进键被换成了后退，你的身体坚持朝反方向走一小段")
                .weight(130)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 100, 1);
                    ctx.setRotation(ctx.rf(-180f, 180f), ctx.rf(-10f, 10f));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.warn("方向已重新定义，定义者不是你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_slot_juggler", "快捷栏杂耍", TrollTier.NIGHTMARE)
                .desc("快捷栏被来回抛接，你连自己在拿什么都说不清")
                .weight(128)
                .cooldownSeconds(65)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.randomSelectSlot();
                    ctx.randomSelectSlot();
                    ctx.randomSelectSlot();
                    ctx.scrambleHotbar();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.msg("§7杂耍结束，请自行找回你的镐子。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_mouse_slave", "鼠标奴隶", TrollTier.NIGHTMARE)
                .desc("你的鼠标灵敏度被拉到极限，轻轻一动视角就飞出屏幕")
                .weight(124)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.glitch(GlitchIds.SENS_MAX, 120, 2);
                    ctx.spin(ctx.rf(-60f, 60f));
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.actionBar("§7现在你的手只负责添乱。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_sens_zero", "灵敏度归零", TrollTier.BRUTAL)
                .desc("鼠标被冻住了，你怎么拖视角都不动，只能靠身体转")
                .weight(122)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.glitch(GlitchIds.SENS_ZERO, 140, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.9f);
                    ctx.warn("你的鼠标刚刚被没收了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_forced_sneak", "强制潜行", TrollTier.NIGHTMARE)
                .desc("你被按着蹲了八秒，速度慢得像在思考人生")
                .weight(126)
                .cooldownSeconds(65)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.setSneaking(true);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.later(160, () -> ctx.setSneaking(false));
                    ctx.actionBar("§7蹲着挺好，至少目标小。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_forced_sprint", "强制冲刺", TrollTier.BRUTAL)
                .desc("你的腿被强行挂上冲刺，停不下来，撞到什么算什么")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.setSprinting(true);
                    ctx.fling(ctx.rf(-0.9f, 0.9f), 0.3, ctx.rf(-0.9f, 0.9f));
                    ctx.effectRandom(160, 0);
                    ctx.later(120, () -> ctx.setSprinting(false));
                    ctx.warn("刹车功能本次未安装。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_forced_dismount", "强制下马", TrollTier.BRUTAL)
                .desc("你正骑着东西，突然被一脚踹了下来，坐骑自己跑了")
                .weight(112)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.dismount();
                    ctx.fling(ctx.rf(-0.8f, 0.8f), 0.7, ctx.rf(-0.8f, 0.8f));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§7坐骑表示它另有安排。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_rotation_drift", "视角漂移", TrollTier.NIGHTMARE)
                .desc("你的视角开始自己缓慢偏移，你越是想瞄准，它偏得越狠")
                .weight(118)
                .cooldownSeconds(75)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.spin(ctx.rf(-90f, 90f));
                    ctx.later(60, () -> ctx.spin(ctx.rf(-60f, 60f)));
                    ctx.later(120, () -> ctx.spin(ctx.rf(-40f, 40f)));
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.1f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_slot_roulette", "槽位轮盘", TrollTier.NIGHTMARE)
                .desc("快捷栏被当成轮盘转了三圈，停下时你手里什么都没有")
                .weight(116)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.randomSelectSlot();
                    ctx.randomSelectSlot();
                    ctx.randomSelectSlot();
                    ctx.randomSelectSlot();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.warn("轮盘已停，结果：空手。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_hand_let_go", "手自己松了", TrollTier.NIGHTMARE)
                .desc("你的手指突然不听使唤，主手的东西就这么掉在了地上")
                .weight(125)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.dropHeld();
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.warn("刚才那一下，神经表示不是它干的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_random_release", "随机放手", TrollTier.NIGHTMARE)
                .desc("背包里随机一件东西被抽走并扔到地上，你只能看着它滚远")
                .weight(122)
                .cooldownSeconds(65)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.dropRandom();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.actionBar("§7你的手刚才做了一个决定，没通知你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_toss_gift", "拿到就丢", TrollTier.ANNOYING)
                .desc("系统好心送你一件东西，两秒后你的手又把它丢了出去")
                .weight(114)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 1);
                    ctx.ok("送你一件礼物，请拿好。");
                    ctx.later(40, () -> {
                        ctx.dropHeld();
                        ctx.msg("§7礼物已由你本人丢弃，概不负责。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_hotbar_lock", "快捷栏锁死", TrollTier.BRUTAL)
                .desc("快捷栏被钉死在当前格子，你拿着一把镐子想挖树")
                .weight(113)
                .cooldownSeconds(75)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 120, 2);
                    ctx.selectSlot(ctx.ri(0, 8));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.warn("本格槽位已被预定，退订请按 /zr off。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_hotbar_jump", "快捷栏乱跳", TrollTier.BRUTAL)
                .desc("你刚选好的东西自己跳走了，再选，再跳")
                .weight(115)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.glitch(GlitchIds.HOTBAR_JUMP, 140, 2);
                    ctx.randomSelectSlot();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_invert_pitch", "上下颠倒", TrollTier.NIGHTMARE)
                .desc("你的上下视角被反了过来，天空在下面，地面在更下面")
                .weight(111)
                .cooldownSeconds(75)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.glitch(GlitchIds.INVERT_PITCH, 120, 2);
                    ctx.spin(ctx.rf(-20f, 20f));
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.1f);
                    ctx.actionBar("§7抬头看看，脚下风景不错。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_invert_mouse", "鼠标反向", TrollTier.ANNOYING)
                .desc("鼠标 Y 轴被翻了个面，你想抬头结果低了头")
                .weight(109)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 120, 2);
                    ctx.setRotation(ctx.rf(-180f, 180f), ctx.rf(-40f, 40f));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_view_snap", "强制回首", TrollTier.BRUTAL)
                .desc("有人把你掰过去面向背后，一秒后又把你掰回来，什么都没留下")
                .weight(117)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    float yaw = ctx.rf(-180f, 180f);
                    ctx.setRotation(yaw + 180f, ctx.rf(-15f, 15f));
                    ctx.later(40, () -> ctx.setRotation(yaw, 0f));
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                    ctx.msg("§8刚才那边确实没人，现在也没有。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_walk_of_shame", "耻辱行走", TrollTier.ANNOYING)
                .desc("你被迫蹲着挪到六格开外，一路上所有人都在看你")
                .weight(108)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.setSneaking(true);
                    ctx.scatter(6);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.later(120, () -> ctx.setSneaking(false));
                    ctx.msg("§7这一段路，请你自己回想。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_gravity_glitch", "失重三秒", TrollTier.NIGHTMARE)
                .desc("重力被临时关闭，你在半空尴尬地飘了三秒")
                .weight(112)
                .cooldownSeconds(80)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.noGravity(60);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.3f);
                    ctx.actionBar("§7重力服务暂时下线，请自行漂浮。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_float_step", "飘浮一步", TrollTier.NIGHTMARE)
                .desc("你被轻轻提起来往前推了一步，落地时膝盖有点软")
                .weight(110)
                .cooldownSeconds(75)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.lift(2.0);
                    ctx.effectRandom(120, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.msg("§7刚才那一步是借你的，现在收回。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_teleport_twitch", "传送抽搐", TrollTier.NIGHTMARE)
                .desc("你被来回传送了两次，每次都只挪几格，像身体在打嗝")
                .weight(114)
                .cooldownSeconds(80)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("wither", 3.0f);
                    ctx.scatter(8);
                    ctx.scatter(8);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 24);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_arm_swap", "手臂调包", TrollTier.NIGHTMARE)
                .desc("你身上的两件装备自己换了位置，头盔穿到了脚上这种事真的会发生")
                .weight(106)
                .cooldownSeconds(80)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.swapEquipmentRandomly();
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.9f);
                    ctx.warn("请自行检查你的穿搭，我们不做售后。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_tool_sabotage", "工具自损", TrollTier.NIGHTMARE)
                .desc("你的手在没人按的情况下用力过猛，工具耐久掉了一截")
                .weight(104)
                .cooldownSeconds(75)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.damageRandomTool(ctx.ri(10, 35));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.1f);
                    ctx.actionBar("§7刚才那一下，力是你出的，锅是你的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_enchant_forced", "强制附魔", TrollTier.BRUTAL)
                .desc("你手里的东西被莫名其妙附了魔，颜色变了，效果很微妙")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.enchantHeldRandomly();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.3f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.msg("§7附魔台说这是它见过最随便的一次施法。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_curse_gift", "诅咒之手", TrollTier.NIGHTMARE)
                .desc("你的手自己给东西挂上了消失诅咒，还顺手改了个名字")
                .weight(102)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.curseHeld();
                    ctx.renameHeld(ctx.pick(TrollPools.CURSED_NAMES));
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.9f);
                    ctx.warn("这不是我干的，是你的手干的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_mount_stranger", "强制上陌生坐骑", TrollTier.NIGHTMARE)
                .desc("你被塞上一只完全不认识的生物，它看向你，你看向远方")
                .weight(107)
                .cooldownSeconds(85)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.mount(ctx.pick(TrollPools.RIDEABLE));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§7请勿与坐骑交流，它听不懂，也不想听。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_string_dance", "提线舞步", TrollTier.NIGHTMARE)
                .desc("蹲下、冲刺、转身三个动作被串成一套舞，编舞者没有审美")
                .weight(105)
                .cooldownSeconds(85)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.setSneaking(true);
                    ctx.setSprinting(true);
                    ctx.spin(ctx.rf(-160f, 160f));
                    ctx.later(100, () -> {
                        ctx.setSneaking(false);
                        ctx.setSprinting(false);
                    });
                    ctx.actionBar("§7本舞步共一节，学费不退。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_pose_freeze", "姿势冻结", TrollTier.BRUTAL)
                .desc("你的身体保持当前姿势被冻了四秒，连眨眼都没得商量")
                .weight(103)
                .cooldownSeconds(80)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.freeze(80);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                    ctx.actionBar("§7请保持微笑，虽然我们看不到。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_look_at_me", "看向我", TrollTier.BRUTAL)
                .desc("你的头被扳正指向正前方，系统声称那边有东西，实际上没有")
                .weight(101)
                .cooldownSeconds(75)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.setRotation(0f, 0f);
                    ctx.glitch(GlitchIds.FOV_ZOOM, 80, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.msg("§7正前方什么都没有，但你刚才很确定有。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_slot_tax", "切槽位还要收税", TrollTier.NIGHTMARE)
                .desc("换了个槽位，顺便被扣了一件物品当手续费")
                .weight(99)
                .cooldownSeconds(85)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.randomSelectSlot();
                    ctx.takeRandomItem();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.warn("切换槽位手续费：一件，已扣除。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_fake_control_notice", "假操控提示", TrollTier.ANNOYING)
                .desc("系统公告说你的账号已被接管，配一声提示音，实际只接管了你的心情")
                .weight(97)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e玩家 " + ctx.playerName() + " 的操作权限已临时移交模组。");
                    ctx.actionBar("§7移交范围：全部，除了责任。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_body_clock", "身体报时", TrollTier.BRUTAL)
                .desc("你的身体被当成闹钟，每隔两秒换一个姿势提醒你时间在走")
                .weight(96)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.setSneaking(true);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.0f);
                    ctx.later(40, () -> {
                        ctx.setSneaking(false);
                        ctx.setSprinting(true);
                    });
                    ctx.later(80, () -> {
                        ctx.setSprinting(false);
                        ctx.msg("§7报时结束，本闹钟不可关闭。");
                    });
                })
                .build());

        // ==================== BRUTAL ====================

        TrollEvents.reg(TrollEvent.of("puppet_marionette_walk", "提线木偶行走", TrollTier.APOCALYPSE)
                .desc("你被拽着走了一大段路，冲刺挂满、方向随机、中间还定住检查姿势")
                .weight(68)
                .cooldownSeconds(280)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.scatter(30);
                    ctx.setSprinting(true);
                    ctx.freeze(60);
                    ctx.setSneaking(true);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.2f, 0.9f);
                    ctx.warn("这一段路不是你想走的，但确实是你走的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_leap_of_faith", "被提起来跳", TrollTier.APOCALYPSE)
                .desc("你被拽到十二格高空再往前抛，落地方式完全由重力学决定")
                .weight(65)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.lift(12.0);
                    ctx.fling(ctx.rf(-1.0f, 1.0f), 0.5, ctx.rf(-1.0f, 1.0f));
                    ctx.effectRandom(160, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.8f);
                    ctx.title("§6§l自由落体", "§7你并不自由");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_mount_fling", "坐骑甩飞", TrollTier.APOCALYPSE)
                .desc("被塞上坐骑后被原地甩出去，落地时背后刚好站了一只东西")
                .weight(63)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.mount(ctx.pick(TrollPools.RIDEABLE));
                    ctx.fling(ctx.rf(-1.2f, 1.2f), 1.1, ctx.rf(-1.2f, 1.2f));
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.dismount();
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.2f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_pose_cage", "姿势定格", TrollTier.APOCALYPSE)
                .desc("你的姿势被冻住，四周同时长出围栏，围观者只有怪")
                .weight(62)
                .cooldownSeconds(310)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.freeze(180);
                    ctx.cageWith(ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.7f);
                    ctx.warn("请在定格状态下等待展览结束。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_body_swap", "换身体", TrollTier.APOCALYPSE)
                .desc("你和另一个人被强行调换位置，全服跟着一起头晕")
                .weight(60)
                .cooldownSeconds(300)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    var o = ctx.other();
                    if (o != null) {
                        ctx.swapWith(o);
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                        ctx.titleToAll("§5§l身体已重新分配", "§7谁是谁，请自行确认");
                        ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.2f, 0.9f);
                        ctx.msgToOther(o, "§7刚才那个位置本来是我的。");
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_drag_everyone", "全体被拖拽", TrollTier.NIGHTMARE)
                .desc("八十格内所有玩家被一股看不见的线拽到同一个点上")
                .weight(58)
                .cooldownSeconds(320)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.pullPlayersToMe(80.0);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.2f, 0.9f);
                    ctx.sendToAll("§c[操控] 全体玩家坐标已统一，请互相认识一下。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_input_freeze", "输入冻结", TrollTier.NIGHTMARE)
                .desc("输入被彻底冻结，画面照常，操作全无，你只能看着自己被摆布")
                .weight(64)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 140, 2);
                    ctx.freeze(160);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 0.9f);
                    ctx.warn("键盘鼠标已进入休假状态。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_input_failure", "输入失灵", TrollTier.APOCALYPSE)
                .desc("灵敏度归零加输入冻结双重打击，你连转头都得靠系统施舍")
                .weight(61)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.glitch(GlitchIds.SENS_ZERO, 160, 2);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 120, 2);
                    ctx.spin(ctx.rf(-120f, 120f));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_hands_open", "双手全松", TrollTier.APOCALYPSE)
                .desc("你的手指集体罢工，手里的东西掉在地上，背包里还连着洒出两件")
                .weight(59)
                .cooldownSeconds(320)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.dropHeld();
                    ctx.dropRandom();
                    ctx.dropRandom();
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.7f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 30);
                    ctx.warn("你的手刚才表决通过了这个决定。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_teleport_marathon", "传送马拉松", TrollTier.APOCALYPSE)
                .desc("连续三次随机传送，最后被丢到六十格外，脚还没来得及站直")
                .weight(57)
                .cooldownSeconds(320)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.scatter(25);
                    ctx.scatter(25);
                    ctx.scatterFar(60);
                    ctx.damage("fall", 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_blind_puppet", "盲眼傀儡", TrollTier.NIGHTMARE)
                .desc("视线被掐断，身体被冻住，背后还有呼吸声，你只能等它结束")
                .weight(62)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.BLACKOUT, 120, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.freeze(140);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.3f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_doll_stage", "傀儡舞台", TrollTier.NIGHTMARE)
                .desc("你被封进一个小舞台，姿势被定格，全服标题宣布演出开始")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.cageWith(ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.freeze(160);
                    ctx.titleToAll("§6§l傀儡舞台", "§7主演：一位不愿意透露姓名的玩家");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.2f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_army_of_one", "一人军团", TrollTier.BRUTAL)
                .desc("你被隐身了，三只怪却精准地朝你走来，仿佛它们能看见你")
                .weight(58)
                .cooldownSeconds(310)
                .run(ctx -> {
                    ctx.setInvisible(true);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 0.9f);
                    ctx.warn("隐身不负责隐藏气味、心跳和脚步声。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_ceiling_doll", "吊在天花板", TrollTier.NIGHTMARE)
                .desc("你被吊到二十五格高空并失去重力，低头是一片让人腿软的空")
                .weight(59)
                .cooldownSeconds(310)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.lift(25.0);
                    ctx.noGravity(120);
                    ctx.resetFallDistance();
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.2f, 0.7f);
                    ctx.title("§7§l高处不胜寒", "§7绳子握在别人手里");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_slot_gamble", "槽位赌博", TrollTier.BRUTAL)
                .desc("快捷栏被彻底洗牌，还顺手把你的工具干到报废边缘")
                .weight(56)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.breakRandomTool();
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 1.0f);
                    ctx.warn("本次赌博的筹码是你的工具。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_forced_tribute", "强制纳贡", TrollTier.APOCALYPSE)
                .desc("你的手主动交出了两件东西，其中一件被直接扔在地上")
                .weight(58)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.takeRandomItem();
                    ctx.takeRandomItem();
                    ctx.dropRandom();
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 0.9f);
                    ctx.warn("纳贡记录已盖章，盖章的是一只看不见的手。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_pose_of_regret", "后悔姿势", TrollTier.NIGHTMARE)
                .desc("你被摆成一个标准的后悔姿势并冻结，同时还掉了几点血")
                .weight(57)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.freeze(200);
                    ctx.setSneaking(true);
                    ctx.damage("magic", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.2f, 0.8f);
                    ctx.msg("§7这个姿势的意思是：早知道就不玩了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_string_snap", "线断了", TrollTier.APOCALYPSE)
                .desc("操控你的那根线突然崩断，你被甩出十几格，巨响告诉所有人出事了")
                .weight(55)
                .cooldownSeconds(320)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.fling(ctx.rf(-1.5f, 1.5f), 1.2, ctx.rf(-1.5f, 1.5f));
                    ctx.scatter(15);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.3f, 0.7f);
                    ctx.warn("线断了，但操控者还有备用的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_sprint_into_wall", "冲向墙壁", TrollTier.APOCALYPSE)
                .desc("你的腿被挂上冲刺并往前推，撞墙的账算在物理身上")
                .weight(56)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.setSprinting(true);
                    ctx.launch(0.4);
                    ctx.damage("flyintowall", 4.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.9f);
                    ctx.warn("墙没有问题，问题是你跑得太快。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_mount_parade", "坐骑游行", TrollTier.APOCALYPSE)
                .desc("你被强行安排上坐骑，全服收到游行通知，路线没人知道")
                .weight(54)
                .cooldownSeconds(320)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.mount(ctx.pick(TrollPools.RIDEABLE));
                    ctx.setSprinting(true);
                    ctx.sendToAll("§6[操控] 坐骑游行即将开始，路线：模组说了算。");
                    ctx.soundToAll(ctx.pick(TrollPools.FUNNY), 1.2f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_hotbar_hostage", "快捷栏人质", TrollTier.APOCALYPSE)
                .desc("快捷栏被锁死，同时一件物品被绑走当人质，赎回方式不存在")
                .weight(57)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 160, 2);
                    ctx.takeRandomItem();
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 1.0f);
                    ctx.warn("人质已转移，谈判通道已关闭。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_forced_stare", "强制凝视", TrollTier.APOCALYPSE)
                .desc("你的头被掰向前方并锁定，视野被压暗，有什么在暗处回望")
                .weight(59)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.setRotation(0f, 0f);
                    ctx.glitch(GlitchIds.SENS_MAX, 140, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 220, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.2f, 0.8f);
                    ctx.msg("§8不要移开视线。你也移不开。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_equipment_chaos", "装备大乱", TrollTier.APOCALYPSE)
                .desc("身上四件装备被连续调换三次，最后穿成什么样完全随机")
                .weight(55)
                .cooldownSeconds(310)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.swapEquipmentRandomly();
                    ctx.swapEquipmentRandomly();
                    ctx.swapEquipmentRandomly();
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 1.1f);
                    ctx.warn("请自行确认头盔的位置，它可能已经不在头上了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_march_to_void", "走向虚空", TrollTier.NIGHTMARE)
                .desc("你的身体朝远处走了很长一段，终点是一个刚挖好的坑")
                .weight(58)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.scatterFar(70);
                    ctx.voidPit(2, 8);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.2f, 0.8f);
                    ctx.warn("这个坑不是为你准备的，但你正好站在上面。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_puppet_of_puppet", "傀儡的傀儡", TrollTier.APOCALYPSE)
                .desc("你被塞上坐骑、按住蹲姿、挂上负面状态，背后还站了一只等着的东西")
                .weight(56)
                .cooldownSeconds(300)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.mount(ctx.pick(TrollPools.RIDEABLE));
                    ctx.setSneaking(true);
                    ctx.effectRandom(220, 0);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_string_suspension", "悬丝", TrollTier.APOCALYPSE)
                .desc("你被提上去十八格并冻结在半空，脚下是空气，头顶是线")
                .weight(57)
                .cooldownSeconds(320)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.lift(18.0);
                    ctx.freeze(100);
                    ctx.noGravity(60);
                    ctx.resetFallDistance();
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.2f, 0.7f);
                    ctx.title("§7§l悬丝之上", "§7线在这，剪刀不在这");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_hands_confiscated", "双手被夺", TrollTier.BRUTAL)
                .desc("主手的东西被夺走，背包里还连掉两件，手完全不属于你")
                .weight(58)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.dropHeld();
                    ctx.dropRandom();
                    ctx.dropRandom();
                    ctx.glitch(GlitchIds.HOTBAR_LOCK, 100, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.9f);
                })
                .build());

        // ==================== NIGHTMARE ====================

        TrollEvents.reg(TrollEvent.of("puppet_full_possession", "完全夺舍", TrollTier.APOCALYPSE)
                .desc("身体被彻底接管：输入反转、姿势冻结五秒，两只怪在旁边围观")
                .weight(36)
                .cooldownSeconds(480)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.freeze(300);
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 200, 3);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.titleToAll("§5§l夺舍完成", "§7现在由模组替你呼吸");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.4f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_body_not_mine", "身体不是我的", TrollTier.NIGHTMARE)
                .desc("输入完全冻结，你被隐身，然后有人替你挨了几下，痛觉却传给了你")
                .weight(34)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 160, 3);
                    ctx.setInvisible(true);
                    ctx.damage("magic", 4.0f);
                    ctx.damage("outofworld", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.3f, 0.7f);
                    ctx.warn("刚才动手的不是你，但流血的是你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_remote_control", "远程操控", TrollTier.OBLIVION)
                .desc("操控会松手一次，好让你亲手把自己走到更深的地方")
                .weight(33)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.scatterFar(150);
                    ctx.scatter(20);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 160, 3);
                    ctx.effectRandom(240, 1);
                    ctx.title("§5§l远程操控中", "§7方向盘不在你手里");
                    ctx.later(160, () -> {
                        // 假希望：控制权归还
                        ctx.title("§a§l控制权已归还", "§7你可以动了");
                        ctx.clearEffects();
                    });
                    ctx.later(320, () -> {
                        ctx.glitch(GlitchIds.FREEZE_INPUT, 140, 3);
                        ctx.voidPit(2, 16);
                        ctx.warn("§4归还只是为了让你确认：是你自己走到这里的。");
                    });
                    ctx.later(480, () -> {
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.effectRandom(1200, 3);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_hostage_swap", "人质交换", TrollTier.APOCALYPSE)
                .desc("你和另一个人被换位，对方被扔到远处，全服一起头晕目眩")
                .weight(32)
                .cooldownSeconds(480)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    var o = ctx.other();
                    if (o != null) {
                        ctx.swapWith(o);
                        ctx.teleportOtherTo(o, o.getX() + 40.0, o.getY() + 15.0, o.getZ() + 40.0);
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                        ctx.giveOther(o, ctx.pick(TrollPools.DANGEROUS), 1);
                        ctx.msgToOther(o, "§7你的位置被征用了，补偿是一份危险品。");
                        ctx.titleToAll("§5§l人质交换", "§7请双方自行确认身份");
                        ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.3f, 0.8f);
                    }
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_doll_army", "傀儡军团", TrollTier.NIGHTMARE)
                .desc("你隐身站着，四只怪却排着队走向你，像被同一根线牵着")
                .weight(35)
                .cooldownSeconds(460)
                .run(ctx -> {
                    ctx.setInvisible(true);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.titleToAll("§8§l傀儡军团入场", "§7它们只认线，不认人");
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.3f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_gravity_owner", "重力易主", TrollTier.APOCALYPSE)
                .desc("重力被人接管，你被抛到三十格高空并剧烈甩镜，落地时间未知")
                .weight(34)
                .cooldownSeconds(480)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.noGravity(200);
                    ctx.lift(30.0);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 100, 3);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.4f, 0.6f);
                    ctx.title("§5§l重力易主", "§7下落方向由新主人指定");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_drop_and_watch", "丢掉一切然后看着", TrollTier.OBLIVION)
                .desc("手把包倒空之后会还给你，让你亲自确认已经捡不回来了")
                .weight(33)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.dropEverything();
                    ctx.freeze(240);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.3f, 0.8f);
                    ctx.warn("请记住每件东西的位置，虽然你现在动不了。");
                    ctx.later(160, () -> {
                        // 假希望：解冻
                        ctx.msg("§a你的手松开了。");
                        ctx.clearEffects();
                    });
                    ctx.later(320, () -> {
                        ctx.takeRandomItems(10);
                        ctx.voidPit(2, 16);
                        ctx.warn("§4地上的东西已经被人捡走了。§7那个人也是你，就在刚才。");
                    });
                    ctx.later(480, () -> {
                        ctx.buryAlive();
                        ctx.effectRandom(1200, 3);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_input_slavery", "输入奴隶", TrollTier.APOCALYPSE)
                .desc("输入冻结、控制反转、强制潜行三件套，你的手脚彻底改姓")
                .weight(35)
                .cooldownSeconds(480)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 180, 3);
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 180, 3);
                    ctx.freeze(240);
                    ctx.setSneaking(true);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.3f, 0.7f);
                    ctx.warn("从这一刻起，你只是操作界面的旁观者。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_marionette_arena", "提线竞技场", TrollTier.APOCALYPSE)
                .desc("你被封进竞技场并定格，背后两只怪同时入场，观众席空无一人")
                .weight(34)
                .cooldownSeconds(480)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.cageWith(ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.freeze(220);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 3);
                    ctx.damage("magic", 3.0f);
                    ctx.titleToAll("§4§l提线竞技场", "§7规则：站着不动，直到不动");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.4f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_forced_march", "强制行军", TrollTier.APOCALYPSE)
                .desc("你被挂上冲刺扔到很远的地方，落地摔伤，身上还挂了个负面状态")
                .weight(32)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.scatterFar(180);
                    ctx.setSprinting(true);
                    ctx.effectRandom(260, 1);
                    ctx.damage("fall", 4.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.3f, 0.8f);
                    ctx.warn("行军路线已定，终点是个坏消息。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_eye_owner", "眼睛易主", TrollTier.OBLIVION)
                .desc("眼睛会还给你，代价是你必须一直记得刚才看见的东西")
                .weight(33)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SPIN, 160, 3);
                    ctx.glitch(GlitchIds.BLACKOUT, 90, 2);
                    ctx.randomRotation();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.3f, 0.7f);
                    ctx.later(160, () -> {
                        // 假希望：视线恢复
                        ctx.msg("§a你的视线回来了。");
                    });
                    ctx.later(320, () -> {
                        ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                        ctx.glitch(GlitchIds.RANDOM_SOUND, 140, 2);
                        ctx.warn("§4你还看得见。§7而且它知道你看见了。");
                    });
                    ctx.later(480, () -> {
                        ctx.stripAll();
                        ctx.voidPit(2, 16);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_puppet_king", "傀儡之王", TrollTier.OBLIVION)
                .desc("王会退场，线不会；它把线留在了所有人身上")
                .weight(35)
                .cooldownSeconds(460)
                .run(ctx -> {
                    ctx.pullPlayersToMe(100.0);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.freeze(160);
                    ctx.titleToAll("§6§l傀儡之王", "§7线从这一头，连到所有人");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.3f, 0.8f);
                    ctx.later(200, () -> {
                        // 假希望：王退场
                        ctx.titleToAll("§a§l王已退场", "§7线还在");
                    });
                    ctx.later(380, () -> {
                        ctx.freeze(300);
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                        ctx.warn("§4它不需要亲自在场。§7线是自动的。");
                    });
                    ctx.later(560, () -> {
                        ctx.meteorShower(24, 50);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_hand_of_god", "神之手", TrollTier.OBLIVION)
                .desc("手会接住你一次，让你相信它打算把你放下来")
                .weight(32)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.lift(40.0);
                    ctx.fling(ctx.rf(-1.0f, 1.0f), 0.6, ctx.rf(-1.0f, 1.0f));
                    ctx.title("§7§l神之手", "§7握着你的是谁，不重要");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.4f, 0.6f);
                    ctx.later(160, () -> {
                        // 假希望：被接住了
                        ctx.resetFallDistance();
                        ctx.msg("§a下坠停住了。");
                    });
                    ctx.later(320, () -> {
                        ctx.resetFallDistance();
                        ctx.effectRandom(1200, 3);
                        ctx.obliterate();
                        ctx.warn("§4接住是为了称重。§7现在它松手了。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_body_auction", "身体拍卖", TrollTier.OBLIVION)
                .desc("你可以把自己拍回来，代价是身上所有东西，而卖方也不认账")
                .weight(31)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.MASS_CHAT, 140, 2);
                    ctx.dropHeld();
                    ctx.scrambleHotbar();
                    ctx.warn("成交价：一件物品，买家不明，你不同意也没用。");
                    ctx.later(160, () -> {
                        // 假希望：允许赎回
                        ctx.msg("§a你被允许赎回自己。");
                    });
                    ctx.later(320, () -> {
                        ctx.stripAll();
                        ctx.takeRandomItems(8);
                        ctx.warn("§4拍卖行已收款。§7卖方跑了，标的物还挂在台上。");
                    });
                    ctx.later(480, () -> {
                        ctx.buryAlive();
                        ctx.effectRandom(1200, 3);
                        ctx.voidPit(2, 16);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_last_command", "最后一条指令", TrollTier.APOCALYPSE)
                .desc("你被冻住、双手被清空，还被弹了一个假死亡提示，指令只有两个字：站着")
                .weight(30)
                .cooldownSeconds(520)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.freeze(260);
                    ctx.dropHeld();
                    ctx.dropRandom();
                    ctx.fakeDeathScreen(ctx.pick(TrollPools.FAKE_DEATHS));
                    ctx.glitch(GlitchIds.FAKE_DEATH, 120, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.4f, 0.7f);
                })
                .build());

        // ==================== APOCALYPSE ====================

        TrollEvents.reg(TrollEvent.of("puppet_total_control", "完全控制", TrollTier.OBLIVION)
                .desc("操控者会短暂松手，让你确认自己已经不会自己动了")
                .weight(19)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FREEZE_INPUT, 200, 3);
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 200, 3);
                    ctx.freeze(300);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.titleToAll("§0§l完全控制", "§8你们的身体现在归我");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.6f);
                    ctx.later(220, () -> {
                        // 假希望：松手
                        ctx.glitch(GlitchIds.HARD_BLACKOUT, 140, 3);
                        ctx.titleToAll("§a§l松 手", "§7你试试看");
                    });
                    ctx.later(400, () -> {
                        ctx.glitch(GlitchIds.FREEZE_INPUT, 300, 3);
                        ctx.freeze(400);
                        ctx.warn("§4你刚才没有动。§7你已经习惯被控制了。");
                    });
                    ctx.later(600, () -> {
                        ctx.stripAll();
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_mass_possession", "群体夺舍", TrollTier.OBLIVION)
                .desc("夺舍会宣布结束，然后继续，因为没有人负责结束")
                .weight(18)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.pullPlayersToMe(120.0);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.glitch(GlitchIds.MASS_CHAT, 200, 3);
                    ctx.setSneaking(true);
                    ctx.titleToAll("§5§l群体夺舍", "§7一个都别想有自己的动作");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.6f);
                    ctx.later(220, () -> {
                        // 假希望：公布了结束时间
                        ctx.titleToAll("§a§l夺舍已结束", "§7感谢配合");
                    });
                    ctx.later(400, () -> {
                        ctx.glitch(GlitchIds.MASS_CHAT, 260, 3);
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                        ctx.warn("§4那条公告是自动回复。§7没有人负责结束这件事。");
                    });
                    ctx.later(600, () -> {
                        ctx.buryAlive();
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_apocalypse", "傀儡天启", TrollTier.OBLIVION)
                .desc("怪是被请进来的，而请柬上有你的签名——你被操控着签的")
                .weight(17)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 140, 3);
                    ctx.titleToAll("§4§l傀儡天启", "§4所有人的动作已由服务器代签");
                    ctx.later(200, () -> {
                        // 假希望：增援到了
                        ctx.msg("§a你听见了增援的脚步声。");
                    });
                    ctx.later(360, () -> {
                        ctx.cageWith(ctx.pick(TrollPools.EVIL_BLOCKS));
                        ctx.stripAll();
                        ctx.warn("§4那脚步声是你自己走过来的。§7门也是你开的。");
                    });
                    ctx.later(540, () -> {
                        ctx.meteorShower(24, 50);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_body_confiscation", "身体没收", TrollTier.OBLIVION)
                .desc("身体会还给你，附赠一份你不在场时它做过的事的记录——已删除")
                .weight(16)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.dropEverything();
                    ctx.freeze(300);
                    ctx.glitch(GlitchIds.FAKE_BAN, 160, 3);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.sendToAll("§4§l[操控] 身体已被没收，请勿申诉，申诉入口不存在。");
                    ctx.later(220, () -> {
                        // 假希望：归还身体
                        ctx.sendToAll("§a§l[操控] 身体已归还，感谢配合。");
                        ctx.clearEffects();
                    });
                    ctx.later(400, () -> {
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.warn("§4归还太晚了。§7它用你的手做完了一件事，记录已删除。");
                    });
                    ctx.later(560, () -> {
                        ctx.effectRandom(1200, 3);
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_marionette_world", "提线世界", TrollTier.APOCALYPSE)
                .desc("世界重载卡死、假崩溃、陨石十连、全服被定格，连世界本身都被提线了")
                .weight(19)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 200, 3);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 160, 3);
                    ctx.meteorShower(10, 30.0);
                    ctx.freeze(260);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.sendToAll("§4§l[系统] 世界已交由模组操控，请停止抵抗，抵抗也是动作。");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.6f, 0.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_final_command", "最终指令", TrollTier.OBLIVION)
                .desc("最终指令是『不许动』，而且没有写失效时间")
                .weight(17)
                .cooldownSeconds(900)
                .once()
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.pullPlayersToMe(100.0);
                    ctx.cageWith(ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.freeze(300);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 140, 3);
                    ctx.sendToAll("§4§l[指令] 最终指令已下发：不许动。");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.6f, 0.5f);
                    ctx.later(240, () -> {
                        // 假希望：指令到期
                        ctx.sendToAll("§a§l[指令] 指令已到期。");
                    });
                    ctx.later(420, () -> {
                        ctx.freeze(400);
                        ctx.voidPit(2, 16);
                        ctx.warn("§4到期条款写在附件里，附件不存在。§7所以它一直有效。");
                    });
                    ctx.later(600, () -> {
                        ctx.stripAll();
                        ctx.obliterate();
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("puppet_strings_cut", "提线崩断", TrollTier.OBLIVION)
                .desc("线断了，你自由了——自由落体的那种自由")
                .weight(15)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.noGravity(200);
                    ctx.voidPit(4, 14);
                    ctx.sendToAll("§4§l[操控] 线断了。接下来发生的事，和操控者无关。");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.6f, 0.5f);
                    ctx.later(200, () -> {
                        // 假希望：重获自由
                        ctx.sendToAll("§a§l[系统] 你自由了。");
                        ctx.lift(30.0);
                    });
                    ctx.later(380, () -> {
                        ctx.meteorShower(24, 50);
                        ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 600, 2);
                        ctx.warn("§4没人拉着你，也没人接着你。§7这就是自由的定义。");
                    });
                    ctx.later(560, () -> {
                        ctx.resetFallDistance();
                        ctx.obliterate();
                    });
                })
                .build());
    }
}
