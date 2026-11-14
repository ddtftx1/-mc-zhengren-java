package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【物理与重力】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。共 99 个事件，
 * 全部 id 以 {@code phys_} 开头，覆盖重力异常、失重、弹射、抛飞、摔落伤害、
 * 碰撞、弹跳、漂浮、活塞推动、水流冲击、下落方块、TNT 冲击波、粒子物理、惯性。</p>
 *
 * <p>纪律：本文件刻意不引用任何 Minecraft 原版类型，只用 {@code ctx.*} 方法
 * 与 {@code TrollPools.*} / {@code GlitchIds.*} 常量，因此不受游戏版本 API 差异影响。</p>
 */
public final class PhysicsEvents {

    private PhysicsEvents() {
    }

    public static void register() {
        TrollEvents.category("物理与重力");

        // ============================================================
        // 一、PRANK（小整）18 个
        // ============================================================

        TrollEvents.reg(TrollEvent.of("phys_float_moment", "短暂失重", TrollTier.PRANK)
                .desc("让重力暂停一秒，你怀疑了一下自己是不是真的在飘")
                .weight(120)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.noGravity(20);
                    ctx.msg("§7你的脚底突然感觉不到地面了。就一秒。");
                    ctx.actionBar("§7重力：暂时维护中");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_leaf_sway", "像树叶一样", TrollTier.PRANK)
                .desc("视角轻轻晃了一下，像被风吹的叶子")
                .weight(110)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.spin(ctx.rf(-10f, 10f));
                    ctx.msg("§7一阵风吹过，你跟着晃了晃。风说不是它。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_tiny_hop", "轻轻一跳", TrollTier.PRANK)
                .desc("脚下像被弹簧顶了一下，非常轻")
                .weight(120)
                .cooldownSeconds(30)
                .run(ctx -> {
                    ctx.launch(0.3);
                    ctx.msg("§7你莫名其妙地小跳了一下。别问，问就是物理。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_particle_gust", "粒子阵风", TrollTier.PRANK)
                .desc("一阵可见的气流从你身边刮过，只有视觉危害")
                .weight(110)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 30);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 1.4f);
                    ctx.msg("§7一股气流擦着你过去了，发型没乱，心乱了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_inertia_note", "惯性说明", TrollTier.PRANK)
                .desc("屏幕角落弹出惯性的加载提示，永远加载不完")
                .weight(100)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.actionBar("§7惯性：正在加载… 3%");
                    ctx.msg("§7系统正在为你计算惯性，预计完成时间：下辈子。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_gravity_notice", "重力维护公告", TrollTier.PRANK)
                .desc("伪造一条服务器公告，宣称重力正在例行维护")
                .weight(100)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e【物理引擎】重力将于 10 秒后例行维护，请提前扶好。");
                    ctx.msg("§7（维护人员只有一位，且他请假了。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_feather_fall", "羽毛下坠", TrollTier.PRANK)
                .desc("短暂的下落减速，像一片羽毛")
                .weight(100)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 80, 0);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 18);
                    ctx.msg("§7你下落的样子突然变得很优雅。只有样子。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_squeaky_step", "脚步异响", TrollTier.PRANK)
                .desc("你的每一步都开始发出弹簧的吱呀声")
                .weight(100)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.7f);
                    ctx.msg("§7你的鞋底不知何时装了弹簧，走路自带音效。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_bubble_rise", "气泡上浮", TrollTier.PRANK)
                .desc("你像气泡一样被轻轻托起半格")
                .weight(90)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.lift(0.5);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.msg("§7你脚下冒出一串气泡，把你轻轻托了一下。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_tiny_bounce", "小弹跳", TrollTier.PRANK)
                .desc("落地时多弹了一下，没人看见，但你感觉到了")
                .weight(100)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.launch(0.5);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.9f, 1.2f);
                    ctx.msg("§7你的膝盖多弹了一下。就一下。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_wind_vane", "风向标", TrollTier.PRANK)
                .desc("视角被风吹着转了小半圈")
                .weight(90)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.spin(15f);
                    ctx.msg("§7你的身体短暂地变成了一个风向标。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_gravity_blink", "重力眨眼", TrollTier.PRANK)
                .desc("重力关了一秒又打开，你差点没发现")
                .weight(110)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.noGravity(20);
                    ctx.msg("§7重力刚才眨了下眼。它以为你没看见。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_paper_plane", "纸飞机", TrollTier.PRANK)
                .desc("一架纸飞机从你头顶飞过，方向精准对着你脸")
                .weight(90)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.spawnAny(ctx.pick(TrollPools.PROJECTILE), ctx.x() + ctx.rd(-4, 4), ctx.y() + 3,
                            ctx.z() + ctx.rd(-4, 4));
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 1.5f);
                    ctx.msg("§7一架纸飞机从你头顶飞过。它是有预谋的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_sand_puff", "脚下一撮沙", TrollTier.PRANK)
                .desc("脚下突然扬起一小撮沙尘，暗示你落地姿势很丑")
                .weight(90)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 22);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.7f, 0.9f);
                    ctx.msg("§7你脚下扬起一撮沙。它说你的落地姿势可以再练练。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_anvil_distant", "远处的铁砧", TrollTier.PRANK)
                .desc("远处传来铁砧落地声，和你的膝盖无关")
                .weight(90)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.7f, 0.8f);
                    ctx.msg("§7远处有什么重物落地了。你下意识护住了膝盖。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_momentum_tip", "惯性小贴士", TrollTier.PRANK)
                .desc("系统贴心地告诉你怎么刹车，但没告诉你怎么停")
                .weight(90)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.actionBar("§7惯性小贴士：想要停下，请先减速。");
                    ctx.msg("§7（至于怎么减速，系统说那是你自己的事。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_light_step", "轻功入门", TrollTier.PRANK)
                .desc("你被轻轻抬了一下，像要开始轻功却卡在第一章")
                .weight(100)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.launch(0.4);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 10);
                    ctx.msg("§7你尝试了一下轻功。第一章，第一页，原地起跳。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_lecture", "物理讲座", TrollTier.PRANK)
                .desc("某人假装讲师，给你讲了一段没用的自由落体")
                .weight(80)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES),
                            "§7自由落体的精髓在于：你落下去，然后你自由了。");
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 0.8f, 1.1f);
                })
                .build());

        // ============================================================
        // 二、ANNOYING（烦人）32 个
        // ============================================================

        TrollEvents.reg(TrollEvent.of("phys_gravity_reverse", "重力反转", TrollTier.ANNOYING)
                .desc("短暂漂浮，脚找不到地面，脑袋找不到方向")
                .weight(120)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.noGravity(60);
                    ctx.msg("§7重力反转了。你开始怀念地面。");
                    ctx.actionBar("§c重力方向：上");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_launch_light", "弹射起步", TrollTier.ANNOYING)
                .desc("脚下突然弹了一下，够不上受伤但足够丢脸")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.launch(0.9);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.8f, 1.3f);
                    ctx.msg("§7地面决定送你一程。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_random_shove", "随机推一把", TrollTier.ANNOYING)
                .desc("一股看不见的力量从随机方向推了你一下")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-1.0f, 1.0f), 0.25, ctx.rf(-1.0f, 1.0f));
                    ctx.msg("§7有人推了你一把。周围没人。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_lift_small", "被抬升", TrollTier.ANNOYING)
                .desc("原地升高三格，没有任何解释")
                .weight(110)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.lift(3);
                    ctx.msg("§7你被抬高了。抬你的人没留下名字。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_scatter_short", "随机短传", TrollTier.ANNOYING)
                .desc("被丢到附近某个随机位置")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.scatter(6);
                    ctx.msg("§7空间抖了一下，你换了块地站着。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_spin_wild", "视角乱转", TrollTier.ANNOYING)
                .desc("视野被随机甩到一个毫无意义的方向")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.randomRotation();
                    ctx.msg("§7你的脖子和你的意志短暂地分居了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_hop_annoy", "停不下来的跳", TrollTier.ANNOYING)
                .desc("隔一秒又弹一下，你以为结束了其实没有")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.launch(0.8);
                    ctx.msg("§7你弹起来了。这次应该结束了。");
                    ctx.later(30, () -> {
                        ctx.launch(0.6);
                        ctx.msg("§7没有结束。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_bounce_pad", "隐形弹床", TrollTier.ANNOYING)
                .desc("你脚下被塞了一张看不见的弹床")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.launch(1.2);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.msg("§7你踩到了弹床。弹床不承认。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_ice_slip", "地面打滑", TrollTier.ANNOYING)
                .desc("脚下被换成一块很滑的东西，滑倒纯属你自己的问题")
                .weight(100)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.placeFloor(ctx.pick(TrollPools.NASTY_BLOCKS), 1);
                    ctx.msg("§7你脚下的地面突然变得不太可靠。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_piston_shove", "活塞推动", TrollTier.ANNOYING)
                .desc("被一只看不见的活塞顶了出去")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-1.2f, 1.2f), 0.3, ctx.rf(-1.2f, 1.2f));
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.9f, 1.1f);
                    ctx.msg("§7一只看不见的活塞把你推开了。它很有礼貌地没有解释。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_water_current", "水流冲击", TrollTier.ANNOYING)
                .desc("一股看不见的水流横着冲了你一程")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-1.4f, 1.4f), 0.2, ctx.rf(-1.4f, 1.4f));
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.msg("§7一股水流的冲击感从侧面袭来。你身上是干的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_inertia_drift", "惯性漂移", TrollTier.ANNOYING)
                .desc("你停下了，但身体又滑出去一段")
                .weight(100)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-1.0f, 1.0f), 0.1, ctx.rf(-1.0f, 1.0f));
                    ctx.msg("§7你已经停下了。你的惯性不同意。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_drop_test", "下落测试", TrollTier.ANNOYING)
                .desc("被抬到六格高做自由落体实验，实验对象是你")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.lift(6);
                    ctx.msg("§7有人拿你做了个自由落体实验。实验组和对照组都是你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_ceiling_bonk", "顶头", TrollTier.ANNOYING)
                .desc("原地升起两格，正好撞到不该撞的东西")
                .weight(100)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.lift(2);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.9f, 1.5f);
                    ctx.msg("§7你的头顶和天花板进行了友好会晤。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_moon_gravity", "月球重力", TrollTier.ANNOYING)
                .desc("重力被调低，走路像在月球上，回不来了也别怪谁")
                .weight(100)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.noGravity(80);
                    ctx.launch(0.5);
                    ctx.msg("§7欢迎来到月球。重力正在打折。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_trampoline", "弹簧地板", TrollTier.ANNOYING)
                .desc("地板突然有了弹性，你被弹得比预期高")
                .weight(110)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.launch(1.4);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 1.6f);
                    ctx.msg("§7地板：我只是想帮你一把。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_magnet_push", "磁力推挤", TrollTier.ANNOYING)
                .desc("像被同极磁铁顶开一样被推开")
                .weight(100)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-1.0f, 1.0f), 0.35, ctx.rf(-1.0f, 1.0f));
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 0.8f, 1.2f);
                    ctx.msg("§7你被一股看不见的斥力顶开了。你和它可能同极。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_vacuum_pull", "真空吸力", TrollTier.ANNOYING)
                .desc("半径内的其他玩家被吸到你身边，然后一起尴尬")
                .weight(90)
                .cooldownSeconds(120)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    ctx.pullPlayersToMe(8.0);
                    ctx.sound(ctx.pick(TrollPools.CREEPY), 1.0f, 1.4f);
                    ctx.msg("§7附近的人被吸到了你身边。现在大家都不知道该怎么解释。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_gravity_stutter", "重力抽风", TrollTier.ANNOYING)
                .desc("重力一开一关，你的胃先撑不住了")
                .weight(100)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.noGravity(30);
                    ctx.msg("§7重力关闭。");
                    ctx.later(40, () -> {
                        ctx.noGravity(30);
                        ctx.msg("§7重力又关闭了一次。它正在抽风。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_fall_tease", "掉落虚惊", TrollTier.ANNOYING)
                .desc("被抬到八格高，然后什么都不发生——你会自己下去的")
                .weight(100)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.lift(8);
                    ctx.msg("§7你被抬到了八格高。系统说你自由活动。");
                    ctx.later(40, () -> ctx.msg("§7（下落时请自行负责落地姿势。）"));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_balloon_up", "气球上浮", TrollTier.ANNOYING)
                .desc("被几只气球拽着慢慢升高，没人告诉你怎么下来")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.lift(4);
                    ctx.particle(ctx.pick(TrollPools.TAUNT_PARTICLES), 25);
                    ctx.msg("§7几只气球把你带上去了。它们没有和你商量过降落方案。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_anchor_drag", "锚拖拽", TrollTier.ANNOYING)
                .desc("像被锚住一样被往下一拽，然后又放开")
                .weight(100)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.fling(0.0, -0.6, 0.0);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.8f, 0.7f);
                    ctx.msg("§7有什么东西往下拽了你一下。它松手得很快。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_push_from_behind", "背后一推", TrollTier.ANNOYING)
                .desc("背后传来一股力道，你非常确定身后没人")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-1.0f, 1.0f), 0.2, ctx.rf(-1.0f, 1.0f));
                    ctx.msg("§7你背后有人推了你。你回头了。你没看见人。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_slime_bounce", "黏液弹跳", TrollTier.ANNOYING)
                .desc("脚下像踩到了黏液，弹起来还带着音效")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.launch(1.0);
                    ctx.sound(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§7你踩到了黏液。黏液的反弹力比你想象的大。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_momentum_loss", "惯性丢失", TrollTier.ANNOYING)
                .desc("身体突然变重，走两步像在推山")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.freeze(40);
                    ctx.msg("§7你的惯性被系统借走了，暂时不还。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_gravity_hiccup", "重力打嗝", TrollTier.ANNOYING)
                .desc("重力打了一个嗝，你被顺带抛了一下")
                .weight(100)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.noGravity(10);
                    ctx.later(20, () -> {
                        ctx.launch(0.6);
                        ctx.msg("§7重力打了个嗝，你被顺带抛了一下。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_low_ceiling", "天花板变低", TrollTier.ANNOYING)
                .desc("头顶掉下一块方块，正好卡在你上方")
                .weight(100)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.spawnFallingBlock(ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.9f, 0.8f);
                    ctx.msg("§7天花板决定离你近一点。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_tremor", "地面震颤", TrollTier.ANNOYING)
                .desc("脚下一阵震动，站得不太稳，但什么都没塌")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.msg("§7地面震了一下。震完什么都没发生，更吓人。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_water_jet", "水柱冲击", TrollTier.ANNOYING)
                .desc("一股水柱从下往上把你顶起来")
                .weight(100)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.launch(1.1);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 20);
                    ctx.msg("§7一股水柱从地面喷出来，把你顶了起来。地上是干的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_wind_gust", "强阵风", TrollTier.ANNOYING)
                .desc("一阵强风把所有人往同一个方向吹")
                .weight(100)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.soundToAll(ctx.pick(TrollPools.IMPACT), 0.8f, 1.2f);
                    ctx.fling(ctx.rf(-1.2f, 1.2f), 0.3, ctx.rf(-1.2f, 1.2f));
                    ctx.msg("§7一阵强风过境。风只针对你，别人没感觉。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_gravity_rotate", "重力换向", TrollTier.ANNOYING)
                .desc("重力的方向被拧了一下，你斜着就飘了出去")
                .weight(100)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.setRotation(ctx.rf(-180f, 180f), ctx.rf(-40f, 40f));
                    ctx.fling(ctx.rf(-1.0f, 1.0f), 0.4, ctx.rf(-1.0f, 1.0f));
                    ctx.msg("§7重力的方向被拧了 45 度。你顺着新方向飘了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_block_drop", "方块砸脚", TrollTier.ANNOYING)
                .desc("一块方块从头顶落下来，正好在你脚边碎开")
                .weight(100)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.spawnFallingBlock(ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§7一块方块落在你脚边。它瞄准的是你的脚。");
                })
                .build());

        // ============================================================
        // 三、BRUTAL（抓狂）27 个
        // ============================================================

        TrollEvents.reg(TrollEvent.of("phys_launch_hard", "强力弹射", TrollTier.BRUTAL)
                .desc("被弹到相当高的地方，落地的事自己想办法")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.launch(1.8);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§c地面用尽全力把你送上了天。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_fling_far", "抛飞", TrollTier.BRUTAL)
                .desc("被朝随机方向抛出去，附带一身伤")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-3f, 3f), 1.5, ctx.rf(-3f, 3f));
                    ctx.damage(3.0f);
                    ctx.msg("§c你被抛了出去。抛你的人力气很大，且不讲道理。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_lift_high", "高空抬升", TrollTier.BRUTAL)
                .desc("被抬到二十五格高，重力已经准备好接手")
                .weight(110)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.lift(25);
                    ctx.msg("§c你被抬到了二十五格。接下来是自由落体环节。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_scatter_far", "远距抛送", TrollTier.BRUTAL)
                .desc("被丢到很远的随机位置，坐标不提供")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.scatterFar(50);
                    ctx.msg("§c你被物理引擎请出了当前区域，坐标保密。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_launch_down", "向下弹射", TrollTier.BRUTAL)
                .desc("弹射方向被调反了，你被砸进地里")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.fling(0.0, -2.0, 0.0);
                    ctx.damage("fall", 4.0f);
                    ctx.msg("§c弹射方向装反了，你被砸向了地面。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_tnt_shockwave", "TNT 冲击波", TrollTier.BRUTAL)
                .desc("旁边炸开一颗 TNT，冲击波把你掀飞")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x() + ctx.rd(-3, 3), ctx.y() + 1, ctx.z() + ctx.rd(-3, 3), 50);
                    ctx.fling(ctx.rf(-2f, 2f), 1.0, ctx.rf(-2f, 2f));
                    ctx.msg("§c一颗 TNT 在你旁边炸开。冲击波先到，抱歉后到。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_boom_push", "爆炸吹飞", TrollTier.BRUTAL)
                .desc("一场不毁地形的爆炸，只负责把你吹走")
                .weight(100)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.boomAtPlayer(3.0f);
                    ctx.fling(ctx.rf(-2.5f, 2.5f), 1.2, ctx.rf(-2.5f, 2.5f));
                    ctx.damage(3.0f);
                    ctx.msg("§c爆炸了。地形没事，你有事。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_fall_damage", "摔落实验", TrollTier.BRUTAL)
                .desc("把你抬到十四格高，检验一下摔落伤害公式")
                .weight(110)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.lift(14);
                    ctx.msg("§c摔落伤害公式需要更多数据，样本是你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_anvil_drop", "铁砧下落", TrollTier.BRUTAL)
                .desc("头顶掉下一块重物，物理定律准时上班")
                .weight(110)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.spawnFallingBlock(ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.damage(5.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§c头顶的东西下来了。物理定律从不迟到。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_meteor_small", "小陨石", TrollTier.BRUTAL)
                .desc("三颗小型陨石落在你附近，纯属巧合")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.meteorShower(3, 8.0);
                    ctx.damage(3.0f);
                    ctx.msg("§c三颗陨石选中了你这片区域。概率学表示这不怪它。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_lightning_near", "近距落雷", TrollTier.BRUTAL)
                .desc("雷落在你旁边，只差一点点")
                .weight(100)
                .cooldownSeconds(170)
                .run(ctx -> {
                    ctx.lightningNear();
                    ctx.damage("lightning", 4.0f);
                    ctx.msg("§c雷落在你旁边。它说下次会更准。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_collision_wall", "高速撞墙", TrollTier.BRUTAL)
                .desc("被加速甩向墙的方向，撞击伤害由你承担")
                .weight(100)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-3f, 3f), 0.5, ctx.rf(-3f, 3f));
                    ctx.damage("flyintowall", 5.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§c你和墙面进行了一次高速会面。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_piston_launch", "活塞发射", TrollTier.BRUTAL)
                .desc("被一只巨型活塞正面弹射出去")
                .weight(100)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.launch(2.0);
                    ctx.damage(2.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§c活塞表示它只是正常工作，是你站错了位置。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_water_hammer", "水流重击", TrollTier.BRUTAL)
                .desc("高压水流把你冲飞，附赠冲击伤害")
                .weight(100)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-2.5f, 2.5f), 1.0, ctx.rf(-2.5f, 2.5f));
                    ctx.damage(4.0f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 25);
                    ctx.msg("§c高压水流命中。你飞了，水花也很配合。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_gravity_crush", "重力碾压", TrollTier.BRUTAL)
                .desc("重力骤然加重，把你按在原地动弹不得")
                .weight(100)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.freeze(60);
                    ctx.damage(4.0f);
                    ctx.msg("§c重力突然加重。你的膝盖提出了抗议。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_void_drop", "脚下开洞", TrollTier.BRUTAL)
                .desc("脚下的地面被挖开，物理负责把你送下去")
                .weight(90)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.voidPit(2, 4);
                    ctx.msg("§c你脚下的地面没了。重力接过了后续工作。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_lava_splash", "岩浆飞溅", TrollTier.BRUTAL)
                .desc("脚下铺开一层岩浆，温度属于物理范畴")
                .weight(90)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.lavaPit(1);
                    ctx.setFire(3);
                    ctx.msg("§c你脚下多了一层岩浆。热力学暂时不打算帮你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_bounce_hard", "硬核弹跳", TrollTier.BRUTAL)
                .desc("被弹得很高，落地的账稍后一起算")
                .weight(100)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.launch(2.2);
                    ctx.damage(2.0f);
                    ctx.msg("§c你被弹到了很高的地方。下落的账单已经开好了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_slingshot", "弹弓", TrollTier.BRUTAL)
                .desc("像弹弓一样被甩出去很远")
                .weight(100)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-4f, 4f), 1.8, ctx.rf(-4f, 4f));
                    ctx.damage(3.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.4f);
                    ctx.msg("§c你被当成了弹弓的弹药。目标是随机选的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_player_collision", "玩家对撞", TrollTier.BRUTAL)
                .desc("另一名玩家被拉到你身边，然后你们互相撞了一下")
                .weight(90)
                .cooldownSeconds(180)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    ctx.teleportOtherToMe(ctx.other());
                    ctx.damageOther(ctx.other(), 3.0f);
                    ctx.damage(3.0f);
                    ctx.msg("§c有人被传到你面前。你们相撞了，双方各承担一半责任。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_falling_floor", "地板塌陷", TrollTier.BRUTAL)
                .desc("脚下的地板整片塌掉，物理引擎鼓掌")
                .weight(90)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.voidPit(3, 3);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.8f);
                    ctx.msg("§c地板塌了。它塌得很专业，一点预兆都没有。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_ceiling_crush", "天花板砸落", TrollTier.BRUTAL)
                .desc("头顶掉下重物并造成实伤，物理老师很欣慰")
                .weight(100)
                .cooldownSeconds(170)
                .run(ctx -> {
                    ctx.spawnFallingBlock(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.damage(6.0f);
                    ctx.msg("§c天花板部分脱落，正好落在你头上。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_inertia_slam", "惯性猛冲", TrollTier.BRUTAL)
                .desc("惯性突然恢复并加倍，你冲出去撞了点什么")
                .weight(100)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-3f, 3f), 0.8, ctx.rf(-3f, 3f));
                    ctx.damage(5.0f);
                    ctx.msg("§c你积攒的惯性一次性到账了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_wind_blast", "风爆", TrollTier.BRUTAL)
                .desc("一阵爆炸性气流把你掀翻，只有风，没有火")
                .weight(100)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.boom(ctx.x(), ctx.y() + 1, ctx.z(), 2.0f);
                    ctx.fling(ctx.rf(-2.5f, 2.5f), 1.4, ctx.rf(-2.5f, 2.5f));
                    ctx.damage(3.0f);
                    ctx.msg("§c一股压缩空气炸开，把你掀翻在地。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_gravity_surge", "重力激增", TrollTier.BRUTAL)
                .desc("重力瞬间翻倍，你被按进地里")
                .weight(100)
                .cooldownSeconds(170)
                .run(ctx -> {
                    ctx.freeze(80);
                    ctx.damage(5.0f);
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.msg("§c重力翻倍了。你现在理解蚂蚁的感受了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_tnt_chain", "TNT 连锁", TrollTier.BRUTAL)
                .desc("三颗 TNT 依次引爆，冲击波叠加")
                .weight(100)
                .cooldownSeconds(190)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnTnt(ctx.x() + ctx.rd(-4, 4), ctx.y() + 1, ctx.z() + ctx.rd(-4, 4), 40 + i * 15);
                    }
                    ctx.damage(4.0f);
                    ctx.msg("§c三颗 TNT 排好了队。它们轮流来。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_lightning_strike", "落雷", TrollTier.BRUTAL)
                .desc("雷直接劈在你身上，物理和运气同时缺席")
                .weight(100)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.lightning();
                    ctx.damage("lightning", 6.0f);
                    ctx.setFire(2);
                    ctx.msg("§c雷劈中了你。概率学今晚不打算解释。");
                })
                .build());

        // ============================================================
        // 四、NIGHTMARE（崩溃）15 个
        // ============================================================

        TrollEvents.reg(TrollEvent.of("phys_stratosphere", "平流层弹射", TrollTier.NIGHTMARE)
                .desc("被弹到极高处，落回地面的过程由重力全程负责")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.launch(3.0);
                    ctx.lift(60);
                    ctx.msg("§4你被弹进了平流层。返回舱就是你自己。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_fling_void", "抛向远方", TrollTier.NIGHTMARE)
                .desc("被巨力朝随机方向抛出极远距离")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-8f, 8f), 3.0, ctx.rf(-8f, 8f));
                    ctx.damage(6.0f);
                    ctx.msg("§4你被抛了出去。抛射角度经过精心设计：没有设计。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_meteor_storm", "陨石风暴", TrollTier.NIGHTMARE)
                .desc("八颗陨石围绕你落下，局部天灾")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.meteorShower(8, 16.0);
                    ctx.damage(6.0f);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.8f);
                    ctx.broadcast("§4你所在的位置生成了一片局部天灾。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_tnt_barrage", "TNT 覆盖", TrollTier.NIGHTMARE)
                .desc("五颗 TNT 覆盖式投放，爆炸范围重叠")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnTnt(ctx.x() + ctx.rd(-5, 5), ctx.y() + ctx.rd(2, 5), ctx.z() + ctx.rd(-5, 5),
                                30 + i * 10);
                    }
                    ctx.damage(6.0f);
                    ctx.msg("§4爆炸范围经过计算，结论是：全覆盖。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_lightning_storm", "雷暴连击", TrollTier.NIGHTMARE)
                .desc("雷暴之中连挨两道雷，天气参与了针对")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.thunderstorm(1200);
                    ctx.lightning();
                    ctx.lightningNear();
                    ctx.damage("lightning", 8.0f);
                    ctx.msg("§4天气亲自下场了，而且它站在你的对立面。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_fall_lethal", "致命坠落", TrollTier.NIGHTMARE)
                .desc("被抬到八十格高，剩下的交给重力")
                .weight(80)
                .cooldownSeconds(340)
                .run(ctx -> {
                    ctx.lift(80);
                    ctx.msg("§4你被抬到了八十格。接下来的几十秒由你独自面对。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_boom_destructive", "破坏性爆炸", TrollTier.NIGHTMARE)
                .desc("两场会改地形的爆炸，物理后果非常具体")
                .weight(80)
                .cooldownSeconds(340)
                .run(ctx -> {
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 5.0f);
                    ctx.boomDestructive(ctx.x() + ctx.rd(-4, 4), ctx.y(), ctx.z() + ctx.rd(-4, 4), 4.0f);
                    ctx.damage(8.0f);
                    ctx.msg("§4两场爆炸。地形记住了你，你也会记住地形。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_void_abyss", "无底深坑", TrollTier.NIGHTMARE)
                .desc("脚下被挖出一个很深的坑，深度有点过分")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.voidPit(3, 12);
                    ctx.damage("fall", 6.0f);
                    ctx.msg("§4你脚下出现了一个深坑。它深得很有诚意。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_lava_floor", "岩浆地板", TrollTier.NIGHTMARE)
                .desc("脚下三格全部换成岩浆，站着就是失误")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.lavaPit(3);
                    ctx.setFire(8);
                    ctx.damage("lava", 6.0f);
                    ctx.msg("§4你脚下的地板被换成了岩浆。这是一次整体改造。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_gravity_collapse", "重力崩溃", TrollTier.NIGHTMARE)
                .desc("重力先把你压扁，再把你放上天，顺序很折磨")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.freeze(200);
                    ctx.damage(10.0f);
                    ctx.noGravity(100);
                    ctx.msg("§4重力崩溃了。它先压你，再放你，再假装无事发生。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_collision_apocalypse", "高速对撞", TrollTier.NIGHTMARE)
                .desc("被远远抛出去，撞击伤害由你全额承担")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.scatterFar(80);
                    ctx.damage("flyintowall", 10.0f);
                    ctx.msg("§4你以很高的速度去了很远的地方，并且在终点撞了些什么。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_tnt_underfoot", "脚下 TNT", TrollTier.NIGHTMARE)
                .desc("一颗引信极短的 TNT 就踩在你脚下")
                .weight(90)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x(), ctx.y(), ctx.z(), 20);
                    ctx.damage(8.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§4你脚下有东西在响。留给你反应的时间是 1 秒。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_anvil_rain", "铁砧雨", TrollTier.NIGHTMARE)
                .desc("八块重物从头顶连续落下，覆盖你周围")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    for (int i = 0; i < 8; i++) {
                        ctx.spawnFallingBlock(ctx.pick(TrollPools.TROLL_BLOCKS));
                    }
                    ctx.damage(7.0f);
                    ctx.sound(ctx.pick(TrollPools.ULTRA), 1.0f, 0.9f);
                    ctx.msg("§4头顶开始下重物。这是天气，也不是天气。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_shockwave_slam", "冲击波震击", TrollTier.NIGHTMARE)
                .desc("一场近距离爆炸直接把你震到重伤")
                .weight(80)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.boomAtPlayer(5.0f);
                    ctx.damage(12.0f);
                    ctx.fling(ctx.rf(-4f, 4f), 1.5, ctx.rf(-4f, 4f));
                    ctx.msg("§4冲击波从你脚下扩散出去。你的骨头是第一批观众。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_spin_launch", "旋转弹射", TrollTier.NIGHTMARE)
                .desc("边转边被弹飞，落地时已经分不清东南西北")
                .weight(80)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.randomRotation();
                    ctx.launch(2.5);
                    ctx.damage(6.0f);
                    ctx.msg("§4你被旋转着弹了出去。方向感在起点就丢了。");
                })
                .build());

        // ============================================================
        // 五、APOCALYPSE（地狱）7 个
        // ============================================================

        TrollEvents.reg(TrollEvent.of("phys_gravity_zero", "零重力末日", TrollTier.APOCALYPSE)
                .desc("重力被长期关闭，天上下着雷，你在中间飘")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.noGravity(600);
                    ctx.launch(2.0);
                    ctx.lightning();
                    ctx.broadcast("§4重力已全域下线。所有人开始飘，没人负责降落。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_orbital_launch", "轨道弹射", TrollTier.APOCALYPSE)
                .desc("被一口气弹到两百格高，坐标接近外太空")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.lift(200);
                    ctx.launch(4.0);
                    ctx.broadcast("§4有人被弹出了大气层。弹射者拒绝透露姓名。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_meteor_apocalypse", "陨石天罚", TrollTier.APOCALYPSE)
                .desc("十二颗陨石加一场破坏性爆炸，地图需要重画")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.meteorShower(12, 20.0);
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 7.0f);
                    ctx.damage(10.0f);
                    ctx.broadcast("§4陨石雨已抵达。本区域的地形现在属于可协商范围。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_tnt_apocalypse", "TNT 末世", TrollTier.APOCALYPSE)
                .desc("六颗 TNT 加两场破坏性爆炸，物理规则暂时失效")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnTnt(ctx.x() + ctx.rd(-6, 6), ctx.y() + ctx.rd(1, 4), ctx.z() + ctx.rd(-6, 6),
                                30 + i * 8);
                    }
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 6.0f);
                    ctx.boomDestructive(ctx.x() + ctx.rd(-5, 5), ctx.y(), ctx.z() + ctx.rd(-5, 5), 5.0f);
                    ctx.broadcast("§4TNT 覆盖率 100%。请把这里当作一个刚刚诞生的陨石坑。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_lightning_apocalypse", "雷神之怒", TrollTier.APOCALYPSE)
                .desc("雷暴加三道落雷，伤害拉满，附带着火")
                .weight(40)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.thunderstorm(2400);
                    ctx.lightning();
                    ctx.lightningNear();
                    ctx.lightningNear();
                    ctx.damage("lightning", 15.0f);
                    ctx.setFire(6);
                    ctx.broadcast("§4天空决定一次性解决你。它准备了三道雷。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_collapse_apocalypse", "世界塌陷", TrollTier.APOCALYPSE)
                .desc("深坑、岩浆与破坏性爆炸同时发生，地形被彻底改写")
                .weight(35)
                .cooldownSeconds(600)
                .run(ctx -> {
                    ctx.voidPit(4, 20);
                    ctx.lavaPit(4);
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 8.0f);
                    ctx.damage(12.0f);
                    ctx.broadcast("§4脚下的世界正在被重写。作者没有留下注释。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("phys_singularity", "物理奇点", TrollTier.APOCALYPSE)
                .desc("物理引擎崩成一个奇点，画面假崩溃，人也一起没了")
                .weight(30)
                .cooldownSeconds(720)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 100, 3);
                    ctx.lightning();
                    ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 7.0f);
                    ctx.kill();
                    ctx.broadcast("§4物理引擎崩溃成了一个奇点。它把你也一起带走了。");
                })
                .build());
    }
}
