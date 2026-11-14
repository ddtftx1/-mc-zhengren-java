package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【建筑与红石】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。</p>
 *
 * <p>红石、活塞、门、梯子、照明、容器……你盖的一切都会在某个时刻决定造反。
 * 本文件刻意不引用任何 Minecraft 类型，全部通过 {@code ctx.*} 与 {@code TrollPools.*} 组合实现。</p>
 */
public final class BuildEvents {

    private BuildEvents() {
    }

    public static void register() {
        TrollEvents.category("建筑与红石");

        // ================================================================
        // PRANK —— 小整，笑一下就好
        // ================================================================

        TrollEvents.reg(TrollEvent.of("build_torch_opinion", "火把有意见", TrollTier.PRANK)
                .desc("火把开始对你的建筑审美发表评论，火焰配合着抖了三下")
                .weight(130)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.particle(TrollPools.TAUNT_PARTICLES[0], 10);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.6f);
                    ctx.actionBar("§7火把：§8这墙歪了 §e0.3 格§7。");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_sign_sass", "告示牌学会顶嘴", TrollTier.PRANK)
                .desc("你身边的告示牌自动改写内容，专挑你不想听的说")
                .weight(125)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeChat("告示牌", "§7本牌位长期招租，现任主人审美不合格。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.msg("§7你身后的告示牌好像§e自己动了一下§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_door_creak", "门在装鬼", TrollTier.PRANK)
                .desc("门自己开了一条缝，又自己关上，全程没人碰")
                .weight(125)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.soundAt(ctx.pick(TrollPools.CREEPY), ctx.x() + 1, ctx.y(), ctx.z(), 1.0f, 1.3f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 8);
                    ctx.msg("§8门：§7我就开一下，你别紧张。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_ladder_grease", "梯子抹了黄油", TrollTier.PRANK)
                .desc("爬梯子的手感突然变得异常顺滑，顺滑到有点可疑")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.actionBar("§e梯子呈 §l镜面抛光§r§e 状态。");
                    ctx.msg("§7保养记录：本梯子由 §8不明人士§7 上油。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_button_confetti", "按钮喷彩纸", TrollTier.PRANK)
                .desc("按一下按钮，喷出来的不是红石信号，是一团彩色碎屑")
                .weight(120)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.particle(TrollPools.BOOM_PARTICLES[0], 20);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.8f);
                    ctx.ok("按钮表示：§a信号发完了，效果自己体会§a。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_repeater_patience", "中继器多了一档", TrollTier.PRANK)
                .desc("你的中继器被人偷偷调慢了，理由是「让你多等一会儿，静一静心」")
                .weight(120)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.6f, 1.5f);
                    ctx.msg("§7中继器公示：§e新增档位 §l看你急不急§r§e。");
                    ctx.sys("电路延迟 +4 刻（也许更多，谁知道呢）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_observer_stare", "观察者盯着你", TrollTier.PRANK)
                .desc("墙上的观察者把脸转向你，并且持续输出「已检测到菜鸟」信号")
                .weight(120)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 1.2f);
                    ctx.actionBar("§8观察者：§7已锁定目标，输出 §c15§7。");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_lantern_flicker", "灯笼闪三下", TrollTier.PRANK)
                .desc("灯笼表演了一段接触不良，纯属吓你一跳")
                .weight(115)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.BLACKOUT, 6, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.7f, 1.7f);
                    ctx.msg("§e灯：§8我没事§7，就是§8想看你怕一下§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_carpet_drift", "地毯自己挪窝", TrollTier.PRANK)
                .desc("地毯悄悄平移了一格，你踩的时候脚感不太对")
                .weight(110)
                .cooldownSeconds(90)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.setBlock(1, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.7f, 1.5f);
                    ctx.msg("§7地毯声明：§8这是风水调整§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_scaffold_song", "脚手架在哼歌", TrollTier.PRANK)
                .desc("脚手架随你的脚步发出音阶，踩得越急它唱得越难听")
                .weight(115)
                .cooldownSeconds(60)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.6f, 0.8f + i * 0.25f);
                    }
                    ctx.actionBar("§7♫ 脚手架交响曲·第一乐章 ♫");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_stair_pixel", "台阶高了一像素", TrollTier.PRANK)
                .desc("台阶被抬高了看不出来的一点点，你上楼时微妙地绊了一下")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.5f, 1.9f);
                    ctx.msg("§7台阶：§8我长高了 §e0.5 像素§7，你不服你改我。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_glass_wiped", "玻璃被擦得吱吱响", TrollTier.PRANK)
                .desc("身边响起刺耳的擦玻璃声，玻璃却比刚才更花了")
                .weight(110)
                .cooldownSeconds(65)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 1.6f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 12);
                    ctx.msg("§7保洁已完成，视野清晰度：§c-15%§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_cauldron_bubble", "炼药锅自己冒泡", TrollTier.PRANK)
                .desc("空炼药锅咕嘟咕嘟，凑近看又什么都没有")
                .weight(105)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.1f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 15);
                    ctx.msg("§7锅里没有药水§8，但有 §e情绪§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_bed_rumple", "被子被谁睡皱了", TrollTier.PRANK)
                .desc("你的床凭空出现一个睡过的凹陷，重生点倒是还在")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "床挺软，就是枕头有点高。");
                    ctx.msg("§7你的床§8被使用过§7，重生点暂无变动。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_frame_tilt", "展示框歪了", TrollTier.PRANK)
                .desc("你挂的展示框集体歪了五度，物品跟着一起斜")
                .weight(105)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.spin(5.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.3f);
                    ctx.msg("§7展示框：§8这叫 §l艺术倾斜§r§8，你不懂。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_painting_swap", "画的内容换了", TrollTier.PRANK)
                .desc("画框里的画换成了更让你不舒服的那一张")
                .weight(105)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 0.9f);
                    ctx.msg("§7这幅画的名字是《§8刚才还不是这样§7》。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_trapdoor_wink", "活板门眨了眨眼", TrollTier.PRANK)
                .desc("活板门开合了一下，像在对你抛媚眼，怪吓人的")
                .weight(115)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.5f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 6);
                    ctx.actionBar("§7活板门：§8嘿。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_hopper_sigh", "漏斗叹了口气", TrollTier.PRANK)
                .desc("漏斗吸走一件东西，又吐回来，纯粹为了证明它活着")
                .weight(115)
                .cooldownSeconds(70)
                .run(ctx -> {
                    if (!ctx.invEmpty()) {
                        ctx.takeRandomItem();
                        ctx.later(20, () -> ctx.giveRandomJunk());
                        ctx.msg("§7漏斗：§8借一下，§7马上还。");
                    } else {
                        ctx.msg("§7漏斗对着空背包叹了口气。");
                    }
                })
                .build());

        // ================================================================
        // ANNOYING —— 烦人，会皱眉
        // ================================================================

        TrollEvents.reg(TrollEvent.of("build_piston_fling", "活塞送你一程", TrollTier.ANNOYING)
                .desc("脚下的活塞突然伸出，把你当方块一样推出去")
                .weight(135)
                .cooldownSeconds(70)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[8]);
                    ctx.launch(1.4);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§7活塞行程已满§8，乘客请自理落点§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_repeater_slowdown", "全线限速", TrollTier.ANNOYING)
                .desc("你所有的红石延迟统一加长，机器开始按慢动作运行")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.8f, 0.7f);
                    ctx.msg("§7红石管理局通知：§e本区域信号§l限速§r§e，恢复时间未定。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_comparator_liar", "比较器学会了撒谎", TrollTier.ANNOYING)
                .desc("比较器输出的信号强度纯属虚构，你调试一晚上也白搭")
                .weight(130)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.sys("比较器输出：§e" + ctx.ri(1, 15) + "§7（真值：§8拒绝透露§7）");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.1f);
                    ctx.msg("§7比较器：§8你猜我读了几个物品？§7。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_hopper_clog", "漏斗堵了", TrollTier.ANNOYING)
                .desc("漏斗里被塞满各种奇妙垃圾，转运速度归零")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.JUNK), 6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.msg("§7漏斗向你回吐了 §f一堆垃圾§7，声称是§8库存溢出§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_dispenser_junk", "发射器六亲不认", TrollTier.ANNOYING)
                .desc("发射器把里面的东西全朝着你的脸打出来")
                .weight(130)
                .cooldownSeconds(80)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 20);
                    ctx.msg("§7发射器：§8目标已锁定，§c弹药是垃圾§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_dropper_misfire", "投掷器投错方向", TrollTier.ANNOYING)
                .desc("投掷器把东西丢到了你找不到的地方，还贴心附上坐标（假的）")
                .weight(125)
                .cooldownSeconds(85)
                .run(ctx -> {
                    if (!ctx.invEmpty()) {
                        ctx.dropRandom();
                    }
                    ctx.sys("物品落点：§8x=" + (int) ctx.x() + " y=? z=?§7");
                    ctx.msg("§7投掷器表示§8方向感这东西不能强求§7。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_lever_reverse", "拉杆逻辑反了", TrollTier.ANNOYING)
                .desc("拉杆开变成关、关变成开，你盯着它看了十秒才反应过来")
                .weight(125)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 0.6f);
                    ctx.actionBar("§7拉杆状态：§e开=关，关=开§7。");
                    ctx.msg("§8恭喜，你的电路从今天起需要一个 §7NOT§8 门。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_plate_combo", "压力板三连", TrollTier.ANNOYING)
                .desc("踩一下压力板，触发三个互相矛盾的机关")
                .weight(125)
                .cooldownSeconds(75)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                    ctx.breakUnder();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§7压力板：§8一板三用，§7你只付了一脚。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_door_jammed", "门卡住了（大约两秒）", TrollTier.ANNOYING)
                .desc("门在你面前表演卡死，等你放弃走门它又好了")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.soundAt(ctx.pick(TrollPools.IMPACT), ctx.x() + 1, ctx.y(), ctx.z(), 1.0f, 0.5f);
                    ctx.msg("§7门：§8【系统维护中】§7。");
                    ctx.later(40, () -> ctx.ok("门维护完成，现在可以正常开了（大概）。"));
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_trapdoor_pit", "脚下的活板门", TrollTier.ANNOYING)
                .desc("站的地方变成活板门并且缓缓打开，重力开始接手")
                .weight(130)
                .cooldownSeconds(80)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.breakUnder();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§7活板门：§8请下楼，§7楼梯就是没有楼梯。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_ladder_missing_rung", "梯子少了一节", TrollTier.ANNOYING)
                .desc("爬到一半发现梯子中间被抽走一格，你只能原地挂机")
                .weight(120)
                .cooldownSeconds(85)
                .when(ctx -> !ctx.isOnGround())
                .run(ctx -> {
                    ctx.breakAt(ctx.pos());
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§7维修记录：§8某一节梯子去休假了§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_scaffold_sag", "脚手架塌了个角", TrollTier.ANNOYING)
                .desc("你脚下的脚手架往下沉了一格，还发出非常不妙的声音")
                .weight(120)
                .cooldownSeconds(85)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[3]);
                    ctx.msg("§7脚手架质检结果：§c不合格§7，但先用着。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_torch_blackout", "照明集体离职", TrollTier.ANNOYING)
                .desc("周围亮度骤降，你手里的火把也闹脾气")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.BLACKOUT, 30, 1);
                    ctx.clearEffects();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 140, 0);
                    ctx.msg("§8所有光源：§7今天的班就上到这里。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_lantern_dim", "灯笼变成小夜灯", TrollTier.ANNOYING)
                .desc("灯笼亮度被调成了暧昧的昏暗，照明范围肉眼可见地缩水")
                .weight(115)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 15);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 1.0f);
                    ctx.msg("§7照明参数：§8气氛优先，可视距离其次§7。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_sign_rewrite", "告示牌被改字", TrollTier.ANNOYING)
                .desc("你写的指路牌被改成了满分阴阳，路人也看不懂")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.fakeChat("告示牌", "§7" + ctx.pick(TrollPools.FAKE_LINES));
                    ctx.sys("内容已被 §8匿名建筑评论家§7 修改。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_bed_trampoline", "床改行蹦床", TrollTier.ANNOYING)
                .desc("想睡觉的时候被床弹起来三次，睡意全没了")
                .weight(120)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.launch(1.0);
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 120, 0);
                    ctx.msg("§7床：§8本床升级为§l娱乐设施§r§8，睡觉功能另行通知。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_crafting_scramble", "工作台配方乱序", TrollTier.ANNOYING)
                .desc("合成表在你眼皮底下被重新洗牌，摆对了也做不出来")
                .weight(125)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.dropHeld();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 0.9f, 1.4f);
                    ctx.msg("§7工作台公告：§e配方已更新（未经通知、无法回滚）。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_chest_swap", "箱子里的东西互换了", TrollTier.ANNOYING)
                .desc("两个箱子的内容物交换了，你放置的秩序荡然无存")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.shuffleInventory();
                    ctx.scrambleHotbar();
                    ctx.msg("§7整理爱好者提示：§8你的箱子内容已重新随机分布§7。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_shulker_recolor", "潜影盒换了个色", TrollTier.ANNOYING)
                .desc("潜影盒的颜色在你转身之后变了，里面的东西还在但很没安全感")
                .weight(115)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.SPOOKY_PARTICLES), 12);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.2f);
                    ctx.msg("§7潜影盒：§8换个心情，也换个色。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_lighting_strobe", "照明爆闪", TrollTier.ANNOYING)
                .desc("墙面灯光开始高频闪烁，眼睛先受不了的是你")
                .weight(115)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FLASHBANG, 10, 1);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                    ctx.msg("§e灯光师：§8灯没坏，是我在按开关。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_stairs_soap", "楼梯打了肥皂", TrollTier.ANNOYING)
                .desc("楼梯表面突然变得极滑，走一步出一身汗")
                .weight(120)
                .cooldownSeconds(85)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                    ctx.msg("§7楼梯：§8上楼下楼请走稳，摔了算你的。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_slab_shrink", "台阶缩水", TrollTier.ANNOYING)
                .desc("脚下的台阶变成了半砖，还差两厘米就能站住")
                .weight(115)
                .cooldownSeconds(90)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.breakUnder();
                    ctx.setBlock(1, -1, 0, ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.msg("§7工程变更通知：§8台阶厚度调整为 §e0.5§8，请重新学习走路§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_glass_fog", "玻璃起雾了", TrollTier.ANNOYING)
                .desc("玻璃上泛起一层雾，你把脸贴上去也看不清外面")
                .weight(115)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 160, 0);
                    ctx.msg("§7玻璃：§8雾是免费的，擦是收费的。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_fence_lift", "栅栏把你抬过去", TrollTier.ANNOYING)
                .desc("你想翻栅栏，栅栏说不用，我送你过去，然后你越过了自己的院子")
                .weight(115)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.lift(3);
                    ctx.fling(ctx.rd(-1.5, 1.5), 0.2, ctx.rd(-1.5, 1.5));
                    ctx.msg("§7栅栏：§8不用翻§7，我送。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_gate_slam", "栅门夹了一下", TrollTier.ANNOYING)
                .desc("栅门在你通过时快速开合，夹得你怀疑人生")
                .weight(115)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.damage(2.0f);
                    ctx.soundAt(ctx.pick(TrollPools.IMPACT), ctx.x(), ctx.y(), ctx.z() + 1, 1.0f, 0.6f);
                    ctx.msg("§7栅门：§8通过速度不达标，重来。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_rail_detour", "铁轨悄悄改道", TrollTier.ANNOYING)
                .desc("铁轨在你出发后改了走向，矿车直奔墙里")
                .weight(115)
                .cooldownSeconds(110)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.setBlock(ctx.ri(-2, 2), 0, 1, TrollPools.TROLL_BLOCKS[1]);
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§7铁路局：§8本次列车终点站为 §c墙§7。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_minecart_hijack", "矿车被远程接管", TrollTier.ANNOYING)
                .desc("矿车突然不受你控制，朝更刺激的方向驶去")
                .weight(115)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.fling(ctx.rd(-1.2, 1.2), 1.2, ctx.rd(-1.2, 1.2));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.8f);
                    ctx.msg("§7矿车：§8方向盘我拿走了§7，坐稳。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_button_echo", "按钮回声", TrollTier.ANNOYING)
                .desc("按一下按钮，机关响了十下，每一响都在别的地方")
                .weight(120)
                .cooldownSeconds(80)
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.soundAt(ctx.pick(TrollPools.IMPACT), ctx.x() + ctx.ri(-6, 6), ctx.y() + ctx.ri(0, 3),
                                ctx.z() + ctx.ri(-6, 6), 0.8f, ctx.rf(0.7f, 1.4f));
                    }
                    ctx.msg("§7按钮：§8触发一次，收费十响。");
                    ctx.addRage(5);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_dust_reverse", "红石粉方向反了", TrollTier.ANNOYING)
                .desc("红石线路的流向被调头，你的装置开始向后运行")
                .weight(125)
                .cooldownSeconds(95)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.TROLL_BLOCKS), 12);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 0.7f);
                    ctx.msg("§7红石粉：§8今天走 §l逆行§r§8 路线。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_observer_loop", "观察者陷入死循环", TrollTier.ANNOYING)
                .desc("两个观察者开始互相观察，世界多了一份持续不断的噪音")
                .weight(120)
                .cooldownSeconds(100)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.7f, 1.5f);
                    }
                    ctx.effectRandom(120, 0);
                    ctx.msg("§7观察者 A → 观察者 B → 观察者 A → §8永动机§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_hopper_lead", "漏斗开始带节奏", TrollTier.ANNOYING)
                .desc("漏斗运东西的速度变成了诡异的节拍，你的大脑被迫跟着数")
                .weight(115)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.9f);
                    ctx.msg("§7节奏提示：§8哒、哒、哒——你已经在数了§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_fake_tnt_ore", "假 TNT 矿", TrollTier.ANNOYING)
                .desc("你挖到的「TNT」其实是假的，但心跳是真的")
                .weight(115)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.1f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 15);
                    ctx.msg("§c§l嘶———— §r§7……§8是假的。§7你刚才是不是缩了一下？");
                    ctx.addRage(9);
                })
                .build());

        // ================================================================
        // BRUTAL —— 抓狂，会骂人
        // ================================================================

        TrollEvents.reg(TrollEvent.of("build_piston_launcher", "活塞发射台", TrollTier.BRUTAL)
                .desc("脚下升起一整排活塞，把你当炮弹朝着天上发出去")
                .weight(55)
                .cooldownSeconds(200)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[8]);
                    ctx.launch(3.2);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.title("§c§l发射", "§7轨道：垂直向上，终点：地面");
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_slime_launchpad", "粘液块弹射器", TrollTier.BRUTAL)
                .desc("脚底铺满粘液块，弹上去的瞬间你听见了自己的惨叫")
                .weight(55)
                .cooldownSeconds(190)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[8], 2);
                    ctx.launch(2.4);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.msg("§a弹射成功§7，落地失败的概率 §c98%§7。");
                    ctx.addRage(13);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_anvil_gift", "铁砧礼包", TrollTier.BRUTAL)
                .desc("头顶送来一份铁砧快递，签收方式是站着不动")
                .weight(52)
                .cooldownSeconds(210)
                .run(ctx -> {
                    ctx.spawnFallingBlock(TrollPools.TROLL_BLOCKS[3]);
                    ctx.damage("fall", 4.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.msg("§7快递员：§8放你头上了§7，自己接一下。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_gravel_burial", "沙砾活埋", TrollTier.BRUTAL)
                .desc("周围与头顶同时灌沙砾，考古学家的快乐你也能拥有")
                .weight(52)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[3]);
                    ctx.setBlock(0, 3, 0, TrollPools.TROLL_BLOCKS[3]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§8你已被登记为考古标本§7，编号 BUILD-" + ctx.gameTicks() % 1000 + "。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_obsidian_seal", "黑曜石密封装修", TrollTier.BRUTAL)
                .desc("黑曜石把你原地密封，连一条缝都算设计缺陷")
                .weight(50)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[2]);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.7f);
                    ctx.msg("§8装修完成：§7无窗、无门、无采光、无处可逃。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_doorless_room", "房间忘了留门", TrollTier.BRUTAL)
                .desc("你被安置进一间四壁齐全的样板房，设计师拒绝了加门需求")
                .weight(50)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.TROLL_BLOCKS[1]);
                    ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[16]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.msg("§7样板房参观须知：§8参观时长由本房决定§7。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_trapdoor_void", "活板门下面是空的", TrollTier.BRUTAL)
                .desc("活板门打开，下面是你亲手挖的深坑，深度由你决定")
                .weight(50)
                .cooldownSeconds(210)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(1, 3);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§7活板门：§8楼下请。");
                    ctx.addRage(14);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_scaffold_sky", "脚手架通天", TrollTier.BRUTAL)
                .desc("脚手架一路搭到云上，再在你爬到一半时撤走")
                .weight(48)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.lift(60);
                    ctx.scatterBlocks(ctx.pick(TrollPools.TROLL_BLOCKS), 20);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.title("§c§l施工已完成", "§7拆除同步进行中");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_ladder_fire", "梯子着火了", TrollTier.BRUTAL)
                .desc("你正要爬的梯子被人泼了易燃物，火势非常配合")
                .weight(48)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.setBlock(0, 0, 0, TrollPools.TROLL_BLOCKS[10]);
                    ctx.setFire(10);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§c梯子：§8今天走的是§l热情路线§r§c。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_rod_lightning", "避雷针反向工作", TrollTier.BRUTAL)
                .desc("你头上的避雷针开始吸引闪电，而不是躲避它")
                .weight(46)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.lightning();
                    ctx.setFire(6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.msg("§7避雷针：§8避雷，避的是别人家的雷§7。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_tnt_relay", "红石连锁 TNT", TrollTier.BRUTAL)
                .desc("一条红石线连着七处 TNT，信号的走向由它自己决定")
                .weight(46)
                .cooldownSeconds(250)
                .run(ctx -> {
                    for (int i = 0; i < 7; i++) {
                        ctx.setBlock(ctx.ri(-4, 4), ctx.ri(0, 2), ctx.ri(-4, 4), TrollPools.TROLL_BLOCKS[16]);
                    }
                    ctx.boomAtPlayer(2.8f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 80);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§c红石信号已发送§7，具体炸哪几处：§8随机§7。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_dispenser_volley", "发射器连发", TrollTier.BRUTAL)
                .desc("发射器把满满一箱箭全部朝你招呼，装填速度还很快")
                .weight(48)
                .cooldownSeconds(210)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.damage(2.5f);
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.4f);
                    ctx.title("§c§l箭雨", "§7发射器：库存清空中");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_dropper_lava", "投掷器倒岩浆", TrollTier.BRUTAL)
                .desc("投掷器朝你脚下投放了一份热饮，温度 1200℃")
                .weight(45)
                .cooldownSeconds(260)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.lavaPit(1);
                    ctx.setFire(8);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§c投掷器：§8热饮一杯，§7请趁热。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_hopper_thief", "漏斗偷背包", TrollTier.BRUTAL)
                .desc("你身上的漏斗开始主动工作，吸走了几件它喜欢的东西")
                .weight(50)
                .cooldownSeconds(200)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.takeRandomItem();
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§7漏斗：§8我替你保管§7，什么时候还？§8看心情。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_comparator_alarm", "比较器触发警报", TrollTier.BRUTAL)
                .desc("比较器检测到「有玩家过于安逸」，随即将警报等级拉到最高")
                .weight(48)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FLASHBANG, 20, 2);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10);
                    }
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7信号强度：§c满格§7。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_bed_bomb", "床变成了炸弹", TrollTier.BRUTAL)
                .desc("你只是想睡个觉，床却决定用一声巨响送你入眠")
                .weight(48)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.boomAtPlayer(2.6f);
                    ctx.setFire(6);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.msg("§c床：§8睡什么睡，§7起来嗨。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_crafting_trap", "工作台卡住了你", TrollTier.BRUTAL)
                .desc("工作台把你当场锁定，合成进度条永远停在 99%")
                .weight(46)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.freeze(80);
                    ctx.glitch(GlitchIds.CLOSE_SCREEN, 30, 1);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§7工作台：§8合成 §e99%§7，剩下 §e1%§7 看缘分。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_chest_mimic", "箱子咬人", TrollTier.BRUTAL)
                .desc("你伸手进箱子，箱子回敬了你一口，还顺走了点东西")
                .weight(48)
                .cooldownSeconds(210)
                .run(ctx -> {
                    ctx.damage(4.0f);
                    ctx.takeRandomItem();
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.msg("§8箱子：§7这是使用费。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_shulker_exile", "潜影盒把你装箱了", TrollTier.BRUTAL)
                .desc("潜影盒先把你收进去，再在很远的地方吐出来")
                .weight(44)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.BLACKOUT, 40, 2);
                    ctx.scatterFar(200);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§7潜影盒：§8运输已完成§7，运费从你背包里扣。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_glass_cage", "玻璃展示柜", TrollTier.BRUTAL)
                .desc("四面玻璃把你装起来供人观赏，问题是这里只有你一个观众")
                .weight(46)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.TROLL_BLOCKS[17]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.glitch(GlitchIds.FOV_ZOOM, 60, 1);
                    ctx.msg("§7展品说明：§8一只过度自信的建筑师§7。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_fence_cage", "栅栏围猎场", TrollTier.BRUTAL)
                .desc("栅栏把你围起来，同时给你安排了几位热情的新邻居")
                .weight(46)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[1]);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    }
                    ctx.title("§c§l乔迁之喜", "§7邻居已就位");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_stairs_skyfall", "楼梯直通天国", TrollTier.BRUTAL)
                .desc("你面前出现一条通向云层的大楼梯，只是没有回程")
                .weight(45)
                .cooldownSeconds(250)
                .run(ctx -> {
                    ctx.lift(80);
                    ctx.spin(180.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§7楼梯尽头：§8恭喜到达顶层，§7下一层是自由落体。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_slab_quake", "台阶地震", TrollTier.BRUTAL)
                .desc("脚下台阶开始震动，整片地板像被谁重新浇筑过")
                .weight(46)
                .cooldownSeconds(240)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[1], 3);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 180, 1);
                    ctx.glitch(GlitchIds.SHAKY, 80, 2);
                    ctx.msg("§7地质部门：§8本层台阶正在§l重新浇筑§r§8，请勿移动。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_lantern_flood", "灯笼海", TrollTier.BRUTAL)
                .desc("成片的灯笼把你埋了，亮度足够，呼吸空间不足")
                .weight(44)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[6]);
                    ctx.setBlock(0, 3, 0, TrollPools.TROLL_BLOCKS[6]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.msg("§e照明充足§7，空气不足，§8你自己选。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_rail_catapult", "铁轨弹射弯道", TrollTier.BRUTAL)
                .desc("铁轨末端的弯道把你甩向了很有想法的方向")
                .weight(45)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.fling(ctx.rd(-2.5, 2.5), 2.0, ctx.rd(-2.5, 2.5));
                    ctx.damage("flyintowall", 3.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§7列车广播：§8本次列车已不在轨道上§7，请自行欣赏风景。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_minecart_derail", "矿车脱轨", TrollTier.BRUTAL)
                .desc("矿车在最高速的时候选择自由，你选择跟它一起")
                .weight(45)
                .cooldownSeconds(250)
                .run(ctx -> {
                    ctx.dismount();
                    ctx.launch(1.8);
                    for (int i = 0; i < 2; i++) {
                        ctx.damage(2.0f);
                    }
                    ctx.msg("§7矿车：§8到站了§7（并不是站）。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_target_ricochet", "标靶方块回击", TrollTier.BRUTAL)
                .desc("你射出的东西被标靶原路打了回来，力道还加了三成")
                .weight(46)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.damage(3.5f);
                    ctx.fling(-ctx.rd(0.5, 1.5), 0.6, -ctx.rd(0.5, 1.5));
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§7标靶：§8来而不往非礼也。");
                    ctx.addRage(14);
                })
                .build());

        // ================================================================
        // NIGHTMARE —— 崩溃，会砸键盘
        // ================================================================

        TrollEvents.reg(TrollEvent.of("build_piston_crusher", "活塞压路机", TrollTier.NIGHTMARE)
                .desc("四面活塞同时开工，你被夹在中间体验压铸工艺")
                .weight(32)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[1]);
                    ctx.setBlock(0, 2, 0, TrollPools.TROLL_BLOCKS[1]);
                    ctx.damage(6.0f);
                    ctx.freeze(60);
                    ctx.glitch(GlitchIds.SHAKY, 100, 3);
                    ctx.msg("§4压铸完成§7，成品形状：§8你§7。");
                    ctx.addRage(24);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_anvil_rain", "铁砧暴雨", TrollTier.NIGHTMARE)
                .desc("天空开始下铁砧，密度足以让气象局辞职")
                .weight(30)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.meteorShower(8, 24.0);
                    ctx.damage("fall", 5.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.title("§4§l铁砧天气", "§7请勿在户外停留");
                    ctx.addRage(22);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_void_room", "虚空样板间", TrollTier.NIGHTMARE)
                .desc("你被搬进一间没有地板的样板间，脚下就是虚空")
                .weight(30)
                .cooldownSeconds(450)
                .run(ctx -> {
                    ctx.voidPit(2, 4);
                    ctx.teleport(ctx.x(), ctx.y() + 1, ctx.z());
                    ctx.damage("outofworld", 2.0f);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 40, 2);
                    ctx.msg("§8样板间亮点：§7无地板、无边界、无生还。");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_lava_floor", "岩浆地板翻新", TrollTier.NIGHTMARE)
                .desc("地板翻新工程使用了大量岩浆，成本由你承担")
                .weight(30)
                .cooldownSeconds(450)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.setFire(12);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§c装修队：§8地板已升级为§l自发热§r§c材质。");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_bedrock_box", "基岩收纳盒", TrollTier.NIGHTMARE)
                .desc("基岩把你装进收纳盒，说明书上写着「永久保存」")
                .weight(28)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[0]);
                    ctx.setHealth(6.0f);
                    ctx.setFire(15);
                    ctx.glitch(GlitchIds.HIDE_HUD, 120, 2);
                    ctx.msg("§4收纳完成§7，保质期：§8永久§7。");
                    ctx.addRage(30);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_tnt_carpet", "TNT 地毯", TrollTier.NIGHTMARE)
                .desc("你脚下的地毯换成了 TNT，踩下去才收到通知")
                .weight(30)
                .cooldownSeconds(420)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[16], 3);
                    ctx.boomAtPlayer(3.2f);
                    ctx.boomAtPlayer(2.0f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 140);
                    ctx.title("§4§l地毯到货", "§7货号：TNT");
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_observer_swarm", "观察者大军", TrollTier.NIGHTMARE)
                .desc("上百只观察者的视线同时落在你身上，信号强到能把人压扁")
                .weight(28)
                .cooldownSeconds(450)
                .run(ctx -> {
                    for (int i = 0; i < 8; i++) {
                        ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS), ctx.x() + ctx.ri(-6, 6),
                                ctx.y() + ctx.ri(0, 3), ctx.z() + ctx.ri(-6, 6));
                    }
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 80, 2);
                    ctx.msg("§7观察结果：§8你被观察了 §l很多§r§8 次。");
                    ctx.addRage(24);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_redstone_screech", "红石啸叫", TrollTier.NIGHTMARE)
                .desc("红石线路进入共振，啸叫声直冲后脑勺")
                .weight(28)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.randomHorrorSound();
                    ctx.glitch(GlitchIds.SENS_MAX, 60, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.title("§4§l信号共振", "§7频率：正好让你难受");
                    ctx.addRage(25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_lightning_grid", "避雷针阵列", TrollTier.NIGHTMARE)
                .desc("你周围布满了避雷针，接下来发生的事完全符合物理规律")
                .weight(28)
                .cooldownSeconds(480)
                .run(ctx -> {
                    for (int i = 0; i < 4; i++) {
                        ctx.lightningAtPlayer(ctx.player());
                    }
                    ctx.setFire(8);
                    ctx.glitch(GlitchIds.FLASHBANG, 30, 3);
                    ctx.msg("§4阵列启动§7，本次实验的对照组：§8你§7。");
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_hopper_void", "漏斗通向虚空", TrollTier.NIGHTMARE)
                .desc("你的漏斗被接进了虚空，物品一件件消失在看不见的地方")
                .weight(30)
                .cooldownSeconds(420)
                .run(ctx -> {
                    if (!ctx.invEmpty()) {
                        ctx.takeRandomItem();
                        ctx.takeRandomItem();
                    }
                    ctx.msg("§8物品去向：§7漏斗 → 虚空 → §8未知§7。");
                    ctx.later(60, () -> ctx.msg("§8虚空回话：§7收到了，味道一般。"));
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_shulker_prison", "潜影盒监狱", TrollTier.NIGHTMARE)
                .desc("潜影盒把你收进去关了三十秒，出来时天都黑了")
                .weight(28)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 60, 3);
                    ctx.freeze(100);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.later(100, () -> {
                        ctx.timeOfDay(18000L);
                        ctx.msg("§8盒盖打开了§7，外面天黑了，§8你没关错门。");
                    });
                    ctx.msg("§7你被 §l收纳§r§7 进了潜影盒。");
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_glass_maze", "玻璃迷宫", TrollTier.NIGHTMARE)
                .desc("四周升起玻璃墙，你能看见外面，但外面并不想看见你")
                .weight(28)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[17]);
                    ctx.cageWith(TrollPools.TROLL_BLOCKS[17]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.glitch(GlitchIds.FOV_WILD, 100, 2);
                    ctx.msg("§7迷宫出口：§8理论上存在§7。");
                    ctx.addRage(24);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_ladder_down", "梯子通向地底", TrollTier.NIGHTMARE)
                .desc("你顺着梯子往下爬，爬了很久才发现梯子没有底")
                .weight(28)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.teleport(ctx.x(), -60.0, ctx.z());
                    ctx.damage("outofworld", 3.0f);
                    ctx.glitch(GlitchIds.BLACKOUT, 60, 3);
                    ctx.msg("§8梯子：§7还剩 §e∞§7 节。");
                    ctx.addRage(30);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_door_nowhere", "门通向墙里", TrollTier.NIGHTMARE)
                .desc("你推开门，门后是实心石头，而身后那扇门不见了")
                .weight(28)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[0]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.glitch(GlitchIds.FAKE_SAVING, 80, 2);
                    ctx.fakeAchievement("§8迷路了", "在门后找到了一面墙");
                    ctx.msg("§4门后：§7是墙。§4身后：§7没有门。");
                    ctx.addRage(29);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_scaffold_collapse", "高空脚手架崩塌", TrollTier.NIGHTMARE)
                .desc("你在八十格高的脚手架上，而固定螺栓刚刚集体离职")
                .weight(28)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.lift(80);
                    ctx.scatterBlocks(TrollPools.TROLL_BLOCKS[3], 24);
                    ctx.launch(-0.5);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 80, 3);
                    ctx.title("§4§l结构失效", "§7请自行降落");
                    ctx.addRage(28);
                })
                .build());

        // ================================================================
        // APOCALYPSE —— 地狱，会退游戏
        // ================================================================

        TrollEvents.reg(TrollEvent.of("build_permission_revoked", "建筑权限被回收", TrollTier.APOCALYPSE)
                .desc("服务器通知你已在建筑白名单之外，随后你的作品开始逐块消失")
                .weight(24)
                .cooldownSeconds(1200)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 120, 3);
                    ctx.fakeBroadcast("§4你已被移出建筑白名单，作品将逐块回收。");
                    ctx.clearInventory();
                    ctx.placeFloor(TrollPools.EVIL_BLOCKS[0], 3);
                    ctx.setBlock(0, 3, 0, TrollPools.EVIL_BLOCKS[2]);
                    ctx.msg("§4§l权限已回收§r§7，包括你脚下这块地。");
                    ctx.addRage(42);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_tnt_cathedral", "TNT 大教堂", TrollTier.APOCALYPSE)
                .desc("一座由 TNT 砌成的教堂在你头顶落成，随即开始它的第一次弥撒")
                .weight(22)
                .cooldownSeconds(1400)
                .run(ctx -> {
                    for (int i = 0; i < 12; i++) {
                        ctx.setBlock(ctx.ri(-4, 4), ctx.ri(1, 5), ctx.ri(-4, 4), TrollPools.TROLL_BLOCKS[16]);
                    }
                    ctx.boomAtPlayer(4.5f);
                    ctx.boomAtPlayer(3.0f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 180);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 80, 3);
                    ctx.title("§4§l落成典礼", "§7礼成，人也差不多了");
                    ctx.addRage(45);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_bedrock_tomb", "基岩墓穴", TrollTier.APOCALYPSE)
                .desc("基岩把你封进永久产权墓穴，通风设计得非常人性化（没有）")
                .weight(22)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[0]);
                    ctx.setHealth(4.0f);
                    ctx.setFire(20);
                    ctx.starve();
                    ctx.glitch(GlitchIds.HIDE_HUD, 200, 3);
                    ctx.title("§4§l墓穴交付", "§8产权：永久");
                    ctx.msg("§4没有窗，没有门，没有你。");
                    ctx.addRage(50);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_redstone_singularity", "红石奇点", TrollTier.APOCALYPSE)
                .desc("你所有的红石同时满功率运行，世界在信号里开始抖动")
                .weight(22)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_OOM, 100, 3);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    for (int i = 0; i < 8; i++) {
                        ctx.setBlock(ctx.ri(-5, 5), ctx.ri(1, 5), ctx.ri(-5, 5), TrollPools.TROLL_BLOCKS[16]);
                    }
                    ctx.thunderstorm(1200);
                    ctx.timeOfDay(18000L);
                    ctx.msg("§4所有信号同时到齐§7，§8包括最后一个。");
                    ctx.addRage(48);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_anvil_judgment", "铁砧审判", TrollTier.APOCALYPSE)
                .desc("天空开了一场铁砧审判，你是唯一的被告，判决书正在下落")
                .weight(22)
                .cooldownSeconds(1600)
                .run(ctx -> {
                    ctx.meteorShower(16, 30.0);
                    ctx.spawnTnt(ctx.x(), ctx.y() + 20, ctx.z(), 40);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 120, 3);
                    ctx.title("§4§l判决如下", "§7有罪，立即执行");
                    ctx.addRage(50);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_lava_cathedral", "岩浆教堂", TrollTier.APOCALYPSE)
                .desc("你脚下的地板被献祭给了岩浆，整座建筑开始向地心迁移")
                .weight(22)
                .cooldownSeconds(1600)
                .run(ctx -> {
                    ctx.lavaPit(3);
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[10], 3);
                    ctx.setFire(20);
                    ctx.damage("lava", 6.0f);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 12);
                    ctx.title("§4§l献祭完成", "§8地板：谢谢惠顾");
                    ctx.addRage(52);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("build_blueprint_erase", "蓝图被一块橡皮擦过", TrollTier.APOCALYPSE)
                .desc("有人拿橡皮擦从你的蓝图中间擦了过去，你的背包和作品一起没了")
                .weight(20)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 120, 3);
                    ctx.fakeBroadcast("§4检测到建筑违规，正在回收全部资产。");
                    ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), false);
                    ctx.dropEverything();
                    ctx.clearInventory();
                    ctx.placeFloor(TrollPools.EVIL_BLOCKS[2], 3);
                    ctx.msg("§8……图纸还能重画。§7背包和作品是真的没了。");
                    ctx.addRage(55);
                })
                .build());
    }
}
