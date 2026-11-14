package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【移动与传送】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。</p>
 *
 * <p>与原生事件文件一致：<b>刻意不引用任何 Minecraft 类型</b>，全部通过 {@code ctx.*}
 * 与 {@code TrollPools.*} 组合实现，因此不会因为 MC API 版本变动而编译失败。</p>
 *
 * <p>主题范围：走路跑步跳跃、潜行、冲刺、游泳、船与矿车、骑马、鞘翅滑翔、
 * 末影珍珠、传送、坐标漂移、摔落、卡墙、掉洞、走错方向。</p>
 */
public final class MovementEvents {

    private MovementEvents() {
    }

    public static void register() {
        TrollEvents.category("移动与传送");

        // ================================================================
        // PRANK —— 小整：脚还没出事，脑子先出事
        // ================================================================

        TrollEvents.reg(TrollEvent.of("move_sprint_critique", "冲刺被点评", TrollTier.PRANK)
                .desc("你的跑步姿势被一位看不见的教练当场点评")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.msg("§7教练（看不见）：§8「摆臂，摆臂！你这是逃命还是散步？」");
                    ctx.actionBar("§7跑姿评分：§eB-§7（及格但不优雅）");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.3f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_jump_voiceover", "跳跃自带配音", TrollTier.PRANK)
                .desc("每次起跳都配一声很有精神的音效，来源不明")
                .weight(120)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.7f);
                    ctx.actionBar("§7「§e哼！§7」——配音：未署名");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_sneak_squeak", "潜行的吱吱声", TrollTier.PRANK)
                .desc("潜行时脚下发出清脆的吱吱声，隐蔽性归零")
                .weight(120)
                .cooldownSeconds(70)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.9f);
                    ctx.msg("§7你潜行得很认真，声音也很大。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_walk_rewind", "走路倒带", TrollTier.PRANK)
                .desc("系统提示你的移动轨迹正在被回放，并要求你确认")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "刚才那段路，你走回头了");
                    ctx.sys("回放进度：§e§o倒带中……§7（并没有）");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.7f, 1.1f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_boat_horn", "船载喇叭", TrollTier.PRANK)
                .desc("你的船开始鸣笛，虽然它只是一条木船")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§7你的船：§e呜————§7。");
                    ctx.actionBar("§7请给老弱病残让个座。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_minecart_ticket", "矿车检票", TrollTier.PRANK)
                .desc("矿车要求你出示车票，态度非常官方")
                .weight(110)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.fakeBroadcast("§6[矿车公司] 请出示车票，无票乘客将被礼貌地请下车。");
                    ctx.msg("§7你摸了摸口袋，只有石头。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_horse_sneeze", "马打了个喷嚏", TrollTier.PRANK)
                .desc("坐骑突然的喷嚏让它和你都愣了一下")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.msg("§7你的坐骑：§8「阿嚏——」");
                    ctx.spin(ctx.rf(-8f, 8f));
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_elytra_boarding", "鞘翅登机广播", TrollTier.PRANK)
                .desc("起飞前播报一段标准登机通知")
                .weight(100)
                .cooldownSeconds(180)
                .when(ctx -> !ctx.isOnGround())
                .run(ctx -> {
                    ctx.msg("§b[登机广播]§7 请系好鞘翅，收起你的恐惧。");
                    ctx.actionBar("§7预计到达：§e看你怎么飞§7。");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_pearl_refund", "珍珠退款提示", TrollTier.PRANK)
                .desc("末影珍珠表示本次传送不予退款，附赠一句客服话术")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.sys("§b[客服]§7 本次传送已完成，距离误差属于正常现象。");
                    ctx.msg("§8（误差：正常现象。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_wrong_way_sign", "方向指示", TrollTier.PRANK)
                .desc("系统贴心地提醒你，你正在往反方向走")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.actionBar("§e⚠ 检测到方向不一致：您似乎更想回出生点。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.4f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_speed_odometer", "速度播报", TrollTier.PRANK)
                .desc("你的移动速度被实时播报，单位很离谱")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.actionBar("§7当前速度：§e" + ctx.ri(3, 9) + " 格/秒§7（换算成蜗牛：§a"
                            + ctx.ri(20, 60) + " 只§7）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_bunny_hop_praise", "连跳被表扬", TrollTier.PRANK)
                .desc("系统为你连续跳跃颁发了一张电子奖状")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.fakeAchievement("§e弹跳爱好者", "连续跳跃次数达到令人费解的水平");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_footstep_echo", "脚步回声", TrollTier.PRANK)
                .desc("你的脚步声多了一层回声，且比你慢半拍")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.soundAt(ctx.pick(TrollPools.CREEPY), ctx.x(), ctx.y(), ctx.z() - 3, 0.8f, 0.8f);
                    ctx.msg("§8……哒。");
                    ctx.msg("§7（你刚才那步，好像有两个人在走。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_swim_style_score", "泳姿评分", TrollTier.PRANK)
                .desc("游泳被裁判打分，分数不太理想")
                .weight(100)
                .cooldownSeconds(140)
                .when(ctx -> ctx.isInWater())
                .run(ctx -> {
                    ctx.actionBar("§b泳姿评分：§e" + ctx.rd(3.0, 9.5) + "§7 / 10（水花太大）");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_stair_counter", "台阶计数器", TrollTier.PRANK)
                .desc("你走过的每一步都被记成了台阶数，数字还在涨")
                .weight(90)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.sys("步数统计：§e" + ctx.ri(1000, 9999)
                            + "§7。其中 §c" + ctx.ri(900, 9900) + "§7 步属于原地打转。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_door_butler", "门童问好", TrollTier.PRANK)
                .desc("每扇门经过时都会有一位门童向你问好")
                .weight(100)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "欢迎光临，里边请，别踩门槛。");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.6f, 1.4f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_coord_readout", "坐标播报", TrollTier.PRANK)
                .desc("你的精确坐标被广播了出来，还带了一点误差")
                .weight(100)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.sys("当前位置：§e" + (int) ctx.x() + "§7, §e" + (int) ctx.y()
                            + "§7, §e" + (int) ctx.z() + "§7（误差 ±" + ctx.ri(1, 40) + " 格）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_taxi_hail", "叫一辆车", TrollTier.PRANK)
                .desc("天上给你派来一辆坐骑，司机不说话")
                .weight(90)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.giveMobCustomName(
                            ctx.spawnAny(ctx.pick(TrollPools.RIDEABLE),
                                    ctx.x() + ctx.ri(-3, 3), ctx.y(), ctx.z() + ctx.ri(-3, 3)),
                            "§7出租车");
                    ctx.msg("§e您的车到了，请上车。§8（车费另议）");
                })
                .build());

        // ================================================================
        // ANNOYING —— 烦人：方向、阻尼、速度，样样跟你作对
        // ================================================================

        TrollEvents.reg(TrollEvent.of("move_slight_drift", "坐标轻微漂移", TrollTier.ANNOYING)
                .desc("你站的地方悄悄往旁边挪了半格，证据不足")
                .weight(140)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.teleport(ctx.x() + ctx.rd(-2.0, 2.0), ctx.y(), ctx.z() + ctx.rd(-2.0, 2.0));
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.6f, 1.2f);
                    ctx.actionBar("§7你的站位被系统温柔地调整了一下。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_sprint_slow", "冲刺变慢", TrollTier.ANNOYING)
                .desc("按住冲刺键，速度却像在趟泥")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.msg("§7你的冲刺：§8「今天想走慢一点。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_jump_short", "跳不高", TrollTier.ANNOYING)
                .desc("跳跃高度被悄悄扣了一截，一格半都费劲")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 180, 0);
                    ctx.actionBar("§7跳跃蓄力：§c不足§7。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.7f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_sneak_slip", "潜行打滑", TrollTier.ANNOYING)
                .desc("潜行时脚下一滑，你往前滑出去一小段")
                .weight(120)
                .cooldownSeconds(100)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.fling(ctx.rd(-0.8, 0.8), 0.2, ctx.rd(-0.8, 0.8));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.7f, 1.4f);
                    ctx.msg("§7潜行失败：§8「你发出了声音，还往前滑了。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_foot_sticky", "脚底发黏", TrollTier.ANNOYING)
                .desc("地面像涂了糖浆，起步特别费劲")
                .weight(120)
                .cooldownSeconds(110)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 1);
                    ctx.msg("§7地面对你的鞋底表示：§8「再聊会儿。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_ladder_hesitate", "爬梯子犹豫", TrollTier.ANNOYING)
                .desc("梯子今天不太想让你上去，先顿两秒")
                .weight(110)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.freeze(30);
                    ctx.actionBar("§7梯子：§8「等等，我先想想。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_boat_spin", "船原地打转", TrollTier.ANNOYING)
                .desc("船桨自己开始划圆，你分不清船头和船尾")
                .weight(120)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.spin(ctx.rf(120f, 320f));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.8f, 1.1f);
                    ctx.msg("§7船：§8「转圈也是航行的一种。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_minecart_shake", "矿车颠簸", TrollTier.ANNOYING)
                .desc("铁轨突然变得坑坑洼洼，矿车开始摇晃")
                .weight(110)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.spin(ctx.rf(-30f, 30f));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 0.9f);
                    ctx.actionBar("§7矿车：§8「这段路，我修过的。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_horse_stubborn", "马不听使唤", TrollTier.ANNOYING)
                .desc("坐骑决定今天自己选路线")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.setRotation(ctx.rf(-180f, 180f), ctx.rf(-20f, 20f));
                    ctx.msg("§7你的坐骑：§8「往这边，那边风景不好。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_elytra_drag", "鞘翅阻力", TrollTier.ANNOYING)
                .desc("空中阻力突然翻倍，滑翔变成了漂移")
                .weight(110)
                .cooldownSeconds(120)
                .when(ctx -> !ctx.isOnGround())
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.actionBar("§7空气：§8「今天比较稠。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_pearl_misfire", "珍珠偏一点", TrollTier.ANNOYING)
                .desc("末影珍珠落地位置比预期偏了一小段")
                .weight(120)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.teleport(ctx.x() + ctx.rd(-3.0, 3.0), ctx.y(), ctx.z() + ctx.rd(-3.0, 3.0));
                    ctx.sys("传送误差：§e" + ctx.ri(1, 5) + "§7 格。属正常磨损。");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_direction_flip", "左右互换", TrollTier.ANNOYING)
                .desc("左右方向悄悄对调，你的手比脑子先迷路")
                .weight(110)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.INVERT_MOUSE, 80);
                    ctx.msg("§7控制设置：§8「左和右，本来也没那么重要。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_speed_mismatch", "速度不同步", TrollTier.ANNOYING)
                .desc("画面和脚步错开半拍，走起来像在看延迟直播")
                .weight(110)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SHAKY, 70);
                    ctx.actionBar("§7延迟：§e" + ctx.ri(60, 400) + " ms§7（体感）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_ice_slip", "冰上打滑", TrollTier.ANNOYING)
                .desc("脚下瞬间铺满冰，你开始表演花样滑行")
                .weight(120)
                .cooldownSeconds(100)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[6], 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                    ctx.msg("§b临时溜冰场已铺设完毕。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_swim_upstream", "逆流而上", TrollTier.ANNOYING)
                .desc("水里的流向被改成了正对你的方向")
                .weight(110)
                .cooldownSeconds(120)
                .when(ctx -> ctx.isInWater())
                .run(ctx -> {
                    ctx.fling(ctx.rd(-1.5, 1.5), 0.0, ctx.rd(-1.5, 1.5));
                    ctx.actionBar("§3水流：§8「你确定是这个方向？」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_water_push", "水里有人推你", TrollTier.ANNOYING)
                .desc("水下传来一股持续的推力，来源是个谜")
                .weight(110)
                .cooldownSeconds(120)
                .when(ctx -> ctx.isInWater())
                .run(ctx -> {
                    ctx.fling(ctx.rd(-1.0, 1.0), -0.4, ctx.rd(-1.0, 1.0));
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.7f, 0.8f);
                    ctx.msg("§3水下有东西碰了你一下。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_fall_slow", "下落变慢", TrollTier.ANNOYING)
                .desc("摔下去的过程被拉长，让恐惧有个完整的酝酿期")
                .weight(110)
                .cooldownSeconds(120)
                .when(ctx -> ctx.isFalling())
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 100, 0);
                    ctx.glitch(GlitchIds.FOV_ZOOM, 60);
                    ctx.actionBar("§7下落速度：§e电影慢镜头§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_landing_stumble", "落地踉跄", TrollTier.ANNOYING)
                .desc("落地瞬间被绊了一下，往前扑了两格")
                .weight(120)
                .cooldownSeconds(90)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.fling(ctx.rd(-1.0, 1.0), 0.3, ctx.rd(-1.0, 1.0));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.8f, 1.3f);
                    ctx.msg("§7落地姿势：§8「很有想法。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_sand_sink", "沙子下陷", TrollTier.ANNOYING)
                .desc("脚下换成沙砾，你站着也往下沉")
                .weight(100)
                .cooldownSeconds(130)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[3]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                    ctx.msg("§7地面：§8「我在下沉，你也一起来。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_cobweb_touch", "沾了一身蛛网", TrollTier.ANNOYING)
                .desc("你路过的地方多了一团蛛网，腿立刻变重")
                .weight(110)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.setBlock(0, 1, 0, TrollPools.TROLL_BLOCKS[17]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 140, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 1.1f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_door_slam", "门自己关上", TrollTier.ANNOYING)
                .desc("你刚走过去，身后的门就砰地合上了")
                .weight(110)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[1]);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§7砰。§8你想了想，决定绕路。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_trapdoor_snap", "活板门弹起", TrollTier.ANNOYING)
                .desc("脚边的活板门突然弹起来敲了你的膝盖")
                .weight(110)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.placeUnder(TrollPools.TRAP_BLOCKS[0]);
                    ctx.damage("cactus", 1.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.5f);
                    ctx.msg("§7膝盖：§8「谢谢。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_hotbar_jostle", "边走边跳栏", TrollTier.ANNOYING)
                .desc("每走一步，快捷栏就跳一格")
                .weight(110)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.randomSelectSlot();
                    ctx.actionBar("§7快捷栏：§8「跟着节奏来。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_pack_shuffle", "背包跟着颠", TrollTier.ANNOYING)
                .desc("剧烈运动导致你的背包完成了一次自动整理")
                .weight(100)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.shuffleInventory();
                    ctx.msg("§7背包：§8「换了个顺序，惊喜吧。」");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_camera_lag", "视角滞后", TrollTier.ANNOYING)
                .desc("镜头比你的头慢半拍，转头像拖着一条尾巴")
                .weight(110)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SPIN, 60);
                    ctx.actionBar("§7视角同步率：§e" + ctx.ri(30, 80) + "%");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_mouse_heavy", "鼠标变沉", TrollTier.ANNOYING)
                .desc("准星移动手感突然变得又黏又重")
                .weight(100)
                .cooldownSeconds(150)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SENS_ZERO, 60);
                    ctx.msg("§7灵敏度：§8「帮你调低了一点，不客气。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_step_noise_tax", "脚步噪音税", TrollTier.ANNOYING)
                .desc("你走路的声音太大，招来了两位热心邻居")
                .weight(110)
                .cooldownSeconds(110)
                .run(ctx -> {
                    for (int i = 0; i < 2; i++) {
                        ctx.giveMobCustomName(
                                ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING),
                                        ctx.x() + ctx.ri(-4, 4), ctx.y(), ctx.z() + ctx.ri(-4, 4)),
                                "§8热心邻居");
                    }
                    ctx.msg("§7邻居：§8「你脚步很响，我们来看看。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_path_detour", "自动绕路", TrollTier.ANNOYING)
                .desc("导航系统坚持带你走一条更远的路")
                .weight(100)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.scatter(6);
                    ctx.sys("路径规划已更新：§e更远，但『风景更好』§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_rail_misalign", "铁轨错位", TrollTier.ANNOYING)
                .desc("脚下的铁轨被挪了半格，矿车路线全乱")
                .weight(100)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[1]);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 1.2f);
                    ctx.msg("§7铁轨：§8「稍微歪一点，更有性格。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_boat_leak", "船漏水了", TrollTier.ANNOYING)
                .desc("船底开始渗水，你只能一边划一边舀")
                .weight(100)
                .cooldownSeconds(130)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.8f, 0.9f);
                    ctx.msg("§3你的船：§8「别慌，只是有点漏。」");
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_minecart_bump", "矿车过减速带", TrollTier.ANNOYING)
                .desc("轨道上凭空出现一道减速带，你被颠了一下")
                .weight(100)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.launch(0.5);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.0f);
                    ctx.actionBar("§7前方：§e减速带§7（谁装的？）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_saddle_loose", "马鞍松了", TrollTier.ANNOYING)
                .desc("马鞍的带子松了一格，你开始频繁调整坐姿")
                .weight(100)
                .cooldownSeconds(140)
                .run(ctx -> {
                    ctx.spin(ctx.rf(-15f, 15f));
                    ctx.freeze(20);
                    ctx.msg("§7你的坐骑：§8「坐稳点，我要抖一下。」");
                })
                .build());

        // ================================================================
        // BRUTAL —— 抓狂：位置、方向、血量，总有一个要丢
        // ================================================================

        TrollEvents.reg(TrollEvent.of("move_scatter_random", "随机传送", TrollTier.BRUTAL)
                .desc("你被随机扔到附近某个地方，坐标保密")
                .weight(120)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.scatter(40);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 20);
                    ctx.msg("§c你被扔到了别处。§7具体哪儿，自己看地图。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_scatter_far", "远距离抛送", TrollTier.BRUTAL)
                .desc("距离拉到几百格，回家路费请自理")
                .weight(110)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.scatterFar(600);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 30);
                    ctx.warn("§c传送完成。§7距离：§e很远§7。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_swap_partner", "和同伴换位", TrollTier.BRUTAL)
                .desc("你和另一位玩家的位置被强行交换")
                .weight(110)
                .cooldownSeconds(200)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    ctx.swapWith(ctx.randomOtherPlayer());
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§7你眨了眨眼，面前的人换了一张脸。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_launch_sky", "原地弹射", TrollTier.BRUTAL)
                .desc("脚底像装了弹簧，你被直接送上高空")
                .weight(120)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.launch(2.2);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.msg("§7地面迅速远离了你。§8珍惜这几秒吧。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_lift_sudden", "瞬间升高", TrollTier.BRUTAL)
                .desc("你被抬高了二十格，脚下什么也没有")
                .weight(110)
                .cooldownSeconds(190)
                .run(ctx -> {
                    ctx.lift(20);
                    ctx.glitch(GlitchIds.FOV_WILD, 40);
                    ctx.warn("§c脚下：§7空气。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_fling_side", "横着甩出去", TrollTier.BRUTAL)
                .desc("一股巨力从侧面把你甩飞")
                .weight(120)
                .cooldownSeconds(170)
                .run(ctx -> {
                    ctx.fling(ctx.rd(-6.0, 6.0), 1.0, ctx.rd(-6.0, 6.0));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§7你飞了出去。方向：§e随机§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_fall_hard", "结结实实摔一跤", TrollTier.BRUTAL)
                .desc("落地结算一次加倍的摔倒伤害")
                .weight(120)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.damage("fall", 6.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§c地面：§7「久等了。」");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_hidden_pit", "脚下的深坑", TrollTier.BRUTAL)
                .desc("你站立的地方被挖成了一个深坑")
                .weight(110)
                .cooldownSeconds(220)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(1, 14);
                    ctx.damage("fall", 4.0f);
                    ctx.warn("§c地面消失了。往下看看。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_caged", "被关起来", TrollTier.BRUTAL)
                .desc("四周瞬间升起一圈结实的墙，你成了笼中物")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[0]);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§7有人给你建了个单间。§8租金面议。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_wall_stuck", "卡在墙里", TrollTier.BRUTAL)
                .desc("你被塞进方块中间，动弹不得")
                .weight(110)
                .cooldownSeconds(210)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[1]);
                    ctx.freeze(70);
                    ctx.damage("flyintowall", 3.0f);
                    ctx.msg("§c你卡住了。呼吸有点困难。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_ceiling_slam", "头顶突然封顶", TrollTier.BRUTAL)
                .desc("起跳的瞬间头顶多了一整块天花板，你原地撞了回去")
                .weight(110)
                .cooldownSeconds(190)
                .run(ctx -> {
                    ctx.setBlock(0, 3, 0, TrollPools.EVIL_BLOCKS[0]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 1);
                    ctx.damage("flyintowall", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§c咚。§7天花板：§8「此路不通。」");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_floor_gone", "地板撤了", TrollTier.BRUTAL)
                .desc("脚下的方块就地辞职，你负责自由落体")
                .weight(120)
                .cooldownSeconds(160)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.breakUnder();
                    ctx.breakUnder();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§7地板：§8「交接完毕，下一个。」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_pearl_into_lava", "珍珠送你去泡温泉", TrollTier.BRUTAL)
                .desc("末影珍珠非常贴心地把你送到了岩浆旁边")
                .weight(100)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.setFire(4);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.warn("§c珍珠落地，空气很热。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_boat_eject", "被甩出船外", TrollTier.BRUTAL)
                .desc("船突然急刹，你以抛物线的方式离开座位")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.dismount();
                    ctx.fling(ctx.rd(-4.0, 4.0), 1.2, ctx.rd(-4.0, 4.0));
                    ctx.msg("§7船：§8「到站了，请下车。」");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_minecart_launch", "矿车弹射", TrollTier.BRUTAL)
                .desc("矿车冲上了一个不存在的坡道")
                .weight(110)
                .cooldownSeconds(190)
                .run(ctx -> {
                    ctx.dismount();
                    ctx.launch(2.0);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.4f);
                    ctx.msg("§7矿车：§8「终点站，天上。」");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_horse_buck", "马把人甩下去", TrollTier.BRUTAL)
                .desc("坐骑一个急停，你被留在了原地")
                .weight(110)
                .cooldownSeconds(180)
                .run(ctx -> {
                    ctx.dismount();
                    ctx.damage("fall", 3.0f);
                    ctx.fling(ctx.rd(-3.0, 3.0), 0.8, ctx.rd(-3.0, 3.0));
                    ctx.msg("§7你的坐骑回头看了你一眼，然后走了。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_elytra_stall", "鞘翅失速", TrollTier.BRUTAL)
                .desc("滑翔途中突然失去升力，开始螺旋下坠")
                .weight(110)
                .cooldownSeconds(220)
                .when(ctx -> !ctx.isOnGround())
                .run(ctx -> {
                    ctx.spin(ctx.rf(180f, 540f));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 140, 1);
                    ctx.damage("fall", 4.0f);
                    ctx.warn("§c升力：§7已离职。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_gravity_loss", "重力失灵", TrollTier.BRUTAL)
                .desc("你飘了起来，想去哪全看运气")
                .weight(110)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.noGravity(100);
                    ctx.glitch(GlitchIds.FOV_WILD, 40);
                    ctx.msg("§7重力：§8「今天请假。」");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_spin_dizzy", "强制旋转", TrollTier.BRUTAL)
                .desc("视角被强行拧了好几圈，方向感彻底报废")
                .weight(120)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.spin(ctx.rf(400f, 900f));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 140, 1);
                    ctx.msg("§7北在哪边？§8这个问题现在很难。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_no_control", "方向失控", TrollTier.BRUTAL)
                .desc("你的朝向被随机重置，起步就撞树")
                .weight(120)
                .cooldownSeconds(160)
                .run(ctx -> {
                    ctx.randomRotation();
                    ctx.glitch(GlitchIds.INVERT_CONTROLS, 60);
                    ctx.msg("§7你转了个身，理由是：§8「随机」§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_ice_trap", "冰下是陷阱", TrollTier.BRUTAL)
                .desc("脚下的冰面下面是空的，你直接穿了进去")
                .weight(100)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.placeUnder(TrollPools.TRAP_BLOCKS[0]);
                    ctx.voidPit(1, 10);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 120, 1);
                    ctx.warn("§b冰面：§7「承重测试，你输了。」");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_deep_drop", "从高处被放下", TrollTier.BRUTAL)
                .desc("你被抬到高处，然后获准自行下降")
                .weight(110)
                .cooldownSeconds(220)
                .run(ctx -> {
                    ctx.teleport(ctx.x(), ctx.y() + 24, ctx.z());
                    ctx.glitch(GlitchIds.FOV_ZOOM, 50);
                    ctx.msg("§7脚下：§e很远§7。倒计时：§e现在§7。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_water_dunk", "被摁进水里", TrollTier.BRUTAL)
                .desc("有人把你的头按向水面，呼吸条开始倒计时")
                .weight(110)
                .cooldownSeconds(190)
                .run(ctx -> {
                    ctx.setAir(40);
                    ctx.fling(ctx.rd(-0.6, 0.6), -1.2, ctx.rd(-0.6, 0.6));
                    ctx.msg("§3手：§8「憋着。」");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_far_relocate", "遥远的落点", TrollTier.BRUTAL)
                .desc("你被送往一千格外，且没有回程票")
                .weight(100)
                .cooldownSeconds(260)
                .run(ctx -> {
                    ctx.scatterFar(1000);
                    ctx.glitch(GlitchIds.BLACKOUT, 30);
                    ctx.warn("§c传送完成。§7这里没有路标。");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_trapdoor_pit", "活板门下的洞", TrollTier.BRUTAL)
                .desc("你踩的活板门下面是一条直通地底的通道")
                .weight(100)
                .cooldownSeconds(230)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.breakUnder();
                    ctx.voidPit(1, 12);
                    ctx.damage("fall", 5.0f);
                    ctx.msg("§7活板门：§8「请。」");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_tnt_step", "脚下踩到东西", TrollTier.BRUTAL)
                .desc("你刚刚踩过的地方开始发出滴答声")
                .weight(100)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.spawnTnt(ctx.x(), ctx.y(), ctx.z(), 30);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.warn("§c滴答。滴答。§7别回头，跑。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_cactus_run", "仙人掌跑道", TrollTier.BRUTAL)
                .desc("你的前进路线上铺满了带刺的欢迎毯")
                .weight(100)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.scatterBlocks(TrollPools.TROLL_BLOCKS[11], 10);
                    ctx.damage("cactus", 3.0f);
                    ctx.msg("§a路两旁：§7仙人掌。整整两排。");
                    ctx.addRage(8);
                })
                .build());

        // ================================================================
        // NIGHTMARE —— 崩溃：位置与性命只能保一个
        // ================================================================

        TrollEvents.reg(TrollEvent.of("move_scatter_chain", "连环传送", TrollTier.NIGHTMARE)
                .desc("你被连续抛送多次，落点一次比一次离谱")
                .weight(90)
                .cooldownSeconds(380)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.later(20 * (i + 1), () -> ctx.scatterFar(500));
                    }
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 60);
                    ctx.title("§4§l转场中", "§7请勿离开座位");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_swap_all", "全员换位", TrollTier.NIGHTMARE)
                .desc("所有人在同一瞬间互相换了位置，秩序清零")
                .weight(80)
                .cooldownSeconds(420)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    ctx.swapWith(ctx.randomOtherPlayer());
                    ctx.scatterOther(ctx.randomOtherPlayer(), 40);
                    ctx.pullPlayersToMe(20);
                    ctx.glitch(GlitchIds.SHAKY, 80);
                    ctx.titleToAll("§4§l集体搬家", "§7请自行找回归属感");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_sky_drop", "从云上扔下来", TrollTier.NIGHTMARE)
                .desc("你出现在高空，而且重力已经先到一步")
                .weight(90)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.teleport(ctx.x(), ctx.y() + 60, ctx.z());
                    ctx.noGravity(1);
                    ctx.glitch(GlitchIds.FOV_ZOOM, 60);
                    ctx.warn("§4高度：§c致命§4。选择权：§7无。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_void_edge", "虚空边缘", TrollTier.NIGHTMARE)
                .desc("脚边裂开一个直通虚空的深坑")
                .weight(80)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.voidPit(3, 40);
                    ctx.damage("outofworld", 4.0f);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 40);
                    ctx.title("§0§l往下", "§7下面没有底");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_bound_long", "长时间定身", TrollTier.NIGHTMARE)
                .desc("你的双腿被彻底锁死，只能眼睁睁等它过去")
                .weight(90)
                .cooldownSeconds(380)
                .run(ctx -> {
                    ctx.freeze(200);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.msg("§4腿：§7「我们今天不参与。」");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_elytra_crash", "鞘翅坠毁", TrollTier.NIGHTMARE)
                .desc("鞘翅在空中突然失效，你以自由落体结束这次旅行")
                .weight(90)
                .cooldownSeconds(400)
                .when(ctx -> !ctx.isOnGround())
                .run(ctx -> {
                    ctx.damageRandomTool(90);
                    ctx.damage("fall", 10.0f);
                    ctx.glitch(GlitchIds.FLASHBANG, 20);
                    ctx.warn("§4鞘翅：§7「剩下的路，你自己走。」");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_horse_abyss", "骑马冲进深渊", TrollTier.NIGHTMARE)
                .desc("你和坐骑一起踏进了看不到底的地方")
                .weight(80)
                .cooldownSeconds(430)
                .run(ctx -> {
                    ctx.voidPit(2, 30);
                    ctx.dismount();
                    ctx.damage("fall", 8.0f);
                    ctx.msg("§4马：§7「我先走一步。」");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_minecart_hell", "矿车直通岩浆", TrollTier.NIGHTMARE)
                .desc("你乘坐的矿车终点站是一池岩浆")
                .weight(80)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.dismount();
                    ctx.lavaPit(3);
                    ctx.setFire(6);
                    ctx.damage("lava", 5.0f);
                    ctx.title("§c§l终点站", "§7请带好随身物品");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_pearl_loop", "珍珠连跳", TrollTier.NIGHTMARE)
                .desc("你的落点被反复重排，每次都是新的惊吓")
                .weight(80)
                .cooldownSeconds(430)
                .run(ctx -> {
                    ctx.later(15, () -> ctx.scatterFar(300));
                    ctx.later(40, () -> ctx.scatterFar(300));
                    ctx.later(70, () -> ctx.scatterFar(300));
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 50);
                    ctx.warn("§4落点：§7每次都不太一样。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_stone_prison", "石牢", TrollTier.NIGHTMARE)
                .desc("你被整体浇进石头里，只留一条呼吸缝")
                .weight(80)
                .cooldownSeconds(440)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[2]);
                    ctx.freeze(140);
                    ctx.damage("flyintowall", 5.0f);
                    ctx.glitch(GlitchIds.BLACKOUT, 50);
                    ctx.msg("§4石头：§7「先住下吧。」");
                    ctx.addRage(19);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_path_lightning", "路径引雷", TrollTier.NIGHTMARE)
                .desc("你走过的路线被逐格标上了雷击")
                .weight(90)
                .cooldownSeconds(380)
                .run(ctx -> {
                    ctx.lightning();
                    ctx.lightningNear();
                    ctx.damage("lightning", 5.0f);
                    ctx.setFire(4);
                    ctx.warn("§e天：§7「你走的那条路，我看见了。」");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_gravity_long", "长时间失重", TrollTier.NIGHTMARE)
                .desc("重力被撤走很久，你只能靠撞墙改变方向")
                .weight(90)
                .cooldownSeconds(400)
                .run(ctx -> {
                    ctx.noGravity(300);
                    ctx.glitch(GlitchIds.FOV_WILD, 60);
                    ctx.msg("§4重力：§7「长时间请假，勿念。」");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_fall_damage_tax", "摔落伤害加倍", TrollTier.NIGHTMARE)
                .desc("落地结算时，系统多收了一份手续费")
                .weight(90)
                .cooldownSeconds(390)
                .run(ctx -> {
                    ctx.damage("fall", 8.0f);
                    ctx.damage("fall", 6.0f);
                    ctx.playHurtSound();
                    ctx.warn("§4摔落伤害：§7已含服务费。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_teleport_maze", "传送迷宫", TrollTier.NIGHTMARE)
                .desc("每次你走几步就会被送走，出口不存在")
                .weight(80)
                .cooldownSeconds(430)
                .run(ctx -> {
                    ctx.later(30, () -> ctx.scatter(30));
                    ctx.later(70, () -> ctx.scatter(30));
                    ctx.later(110, () -> ctx.scatterFar(400));
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 60);
                    ctx.title("§5§l迷宫中", "§7本次不设出口");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_boat_abyss", "船开进虚空", TrollTier.NIGHTMARE)
                .desc("你的小船载着你驶向了一个没有底的洞")
                .weight(80)
                .cooldownSeconds(440)
                .run(ctx -> {
                    ctx.voidPit(3, 36);
                    ctx.dismount();
                    ctx.damage("outofworld", 5.0f);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 40);
                    ctx.msg("§4船：§7「我只能送到这儿了。」");
                    ctx.addRage(19);
                })
                .build());

        // ================================================================
        // APOCALYPSE —— 地狱：这一趟有去无回
        // ================================================================

        TrollEvents.reg(TrollEvent.of("move_apocalypse_void", "虚空吞噬", TrollTier.APOCALYPSE)
                .desc("你脚下的世界被替换成了虚空，装备和地面一起消失")
                .weight(50)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.clearInventory();
                    ctx.voidPit(4, 60);
                    ctx.damage("outofworld", 12.0f);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 70);
                    ctx.title("§0§l虚空已签收", "§7收件人：你");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_apocalypse_sky_throw", "天罚坠落", TrollTier.APOCALYPSE)
                .desc("你被提到两百格高空，然后开始了漫长的下降")
                .weight(50)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.teleport(ctx.x(), ctx.y() + 200, ctx.z());
                    ctx.glitch(GlitchIds.FOV_ZOOM, 90);
                    ctx.titleToAll("§4§l自由落体", "§7观看者请在安全距离外");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_apocalypse_lava_pool", "岩浆泳池", TrollTier.APOCALYPSE)
                .desc("整片地面被换成了滚烫的泳池，深度管够")
                .weight(50)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.lavaPit(6);
                    ctx.setFire(10);
                    ctx.damage("lava", 8.0f);
                    ctx.glitch(GlitchIds.FLASHBANG, 30);
                    ctx.title("§6§l温泉开业", "§7水温：§c致命§6");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_apocalypse_loop", "无尽传送", TrollTier.APOCALYPSE)
                .desc("你陷入了一场没有尽头的传送循环，方向感彻底报废")
                .weight(40)
                .cooldownSeconds(1000)
                .once()
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.later(20 * (i + 1), () -> ctx.scatterFar(800));
                    }
                    ctx.glitch(GlitchIds.RANDOM_SOUND, 100);
                    ctx.glitch(GlitchIds.SHAKY, 100);
                    ctx.title("§5§l循环中", "§7第 §eN§5 次：N 已丢失");
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_apocalypse_fatal_fall", "必死坠落", TrollTier.APOCALYPSE)
                .desc("急坠与地面结算同时发生，没有中间商")
                .weight(40)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.lift(120);
                    ctx.later(60, () -> {
                        ctx.resetFallDistance();
                        ctx.damage("fall", 40.0f);
                        ctx.glitch(GlitchIds.FAKE_DEATH, 60);
                    });
                    ctx.fakeDeathScreen("落地方式：垂直");
                    ctx.addRage(30);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_apocalypse_no_ground", "世界失去地面", TrollTier.APOCALYPSE)
                .desc("你周围的立足之处被成片抹去，只剩天与虚空")
                .weight(40)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.breakUnder();
                    ctx.scatterBlocks(TrollPools.TRAP_BLOCKS[0], 24);
                    ctx.boomDestructive(ctx.x() + 2, ctx.y() - 1, ctx.z() + 2, 5.0f);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 80);
                    ctx.title("§8§l大地退场", "§7请自行寻找落脚点");
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("move_apocalypse_fake_crash", "移动导致崩溃", TrollTier.APOCALYPSE)
                .desc("这次位移在客户端上表现为一次完整崩溃，纯视觉")
                .weight(40)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FAKE_CRASH, 110);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 110);
                    ctx.scatterFar(120);
                    ctx.fakeSaveNotice();
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_ACHIEVEMENTS), "走了一条不该走的路");
                    ctx.addRage(30);
                })
                .build());
    }
}
