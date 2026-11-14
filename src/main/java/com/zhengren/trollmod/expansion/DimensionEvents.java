package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【下界与末地】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。</p>
 *
 * <p>主题：传送门、地狱岩、灵魂沙、萤石、烈焰人、恶魂、猪灵、疣猪兽、岩浆、
 * 末地石、末影人、末影龙、潜影贝、紫颂果、末地城、折跃门、紫珀块、虚空。</p>
 */
public final class DimensionEvents {

    private DimensionEvents() {
    }

    public static void register() {
        TrollEvents.category("下界与末地");

        // ==================== PRANK ====================

        TrollEvents.reg(TrollEvent.of("dim_portal_rumor", "传送门传闻", TrollTier.PRANK)
                .desc("假广播加一句黑曜石门框点评，配上传送门音效和紫色粒子，纯吓唬不干别的")
                .weight(120)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.fakeBroadcast("§e检测到附近存在未授权的维度通道。");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.2f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 16);
                    ctx.msg("§7门框搭得不错，就是不知道终点在哪。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_glowstone_bribe", "萤石贿赂", TrollTier.PRANK)
                .desc("塞你一点亮晶晶的东西，弹个假成就，然后放一句很快会后悔的预告")
                .weight(115)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.LOOT), 2);
                    ctx.fakeAchievement(ctx.pick(TrollPools.FAKE_ACHIEVEMENTS), "在黑暗中捡到了光");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.ok("下界特产，请收好（保质期三秒）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_nether_survival_tip", "下界生存小贴士", TrollTier.PRANK)
                .desc("物品栏顶上滚一条基本没用的提示，再配一声系统嘲讽，字字正确但毫无帮助")
                .weight(110)
                .cooldownSeconds(40)
                .run(ctx -> {
                    ctx.actionBar(ctx.pick(TrollPools.FAKE_TIPS));
                    ctx.sys("下界生存第一条：不要相信下界。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.1f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_soul_whisper", "灵魂低语", TrollTier.PRANK)
                .desc("灵魂粒子绕着你转，环境音在耳边叹气，再伪造一句亡者的聊天")
                .weight(112)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), ctx.pick(TrollPools.FAKE_LINES));
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 0.7f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 24);
                    ctx.msg("§8好像有人在你看不见的地方排队。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_piglin_smalltalk", "猪灵搭话", TrollTier.PRANK)
                .desc("伪造猪灵的聊天，问你金锭带够没有，然后什么都不做，纯社交压力")
                .weight(108)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.fakeChat("猪灵", "金锭？金子？金块？");
                    ctx.fakeChat("猪灵", "没有金子就滚出我的视线。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 0.8f);
                    ctx.actionBar("§7交易规则第一页：你身上没有金子。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_enderman_stare", "末影人对视", TrollTier.PRANK)
                .desc("背后响起末影人的注视音，视角被轻轻拧一下，然后——什么都没有")
                .weight(118)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 1.3f);
                    ctx.spin(ctx.rf(-25f, 25f));
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 18);
                    ctx.msg("§7记住：不要看它的眼睛。看了也没事，这次。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_chorus_snack", "紫颂果零食", TrollTier.PRANK)
                .desc("送你一颗来历不明的东西当零食，附赠爱心粒子和一句别管它长什么样的忠告")
                .weight(105)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.WEIRD_ITEMS), 1);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 18);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.5f);
                    ctx.msg("§7紫颂果的味道取决于你上次吃了什么，祝你好运。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_lava_viewpoint", "岩浆观景台", TrollTier.PRANK)
                .desc("来个标题和一片岩浆粒子，营造出你在泡温泉的错觉，实际连热度都没有")
                .weight(100)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.title("§6§l岩浆观景台", "§7脚下很烫，但你不会有事");
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 30);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.8f, 0.9f);
                    ctx.msg("§7本观景台不提供饮用水，也不提供后悔药。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_block_photography", "方块摄影大赛", TrollTier.PRANK)
                .desc("给你颁一个根本没参加过的建筑奖项，顺手把周围装饰一下")
                .weight(98)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.fakeAchievement("最佳地狱建筑奖", "评审团是十四只猪灵");
                    ctx.scatterBlocks(ctx.pick(TrollPools.TROLL_BLOCKS), 4);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.ok("奖杯在路上，装饰先到了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_fake_end_sky", "假末地天空", TrollTier.PRANK)
                .desc("把一颗颗末地烛粒子撒满夜空，再放个标题，让你以为自己被传送了")
                .weight(106)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.title("§5§l维度切换中", "§7其实你哪儿都没去");
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 40);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.0f);
                    ctx.msg("§7末地石的味道像粉笔灰，你很快就会知道。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_strider_hitchhike", "炽足兽搭车", TrollTier.PRANK)
                .desc("凭空出现一只骑着岩浆走的东西，冲你叫一声就消失，礼貌但你不需要")
                .weight(102)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.spawnAny(ctx.pick(TrollPools.FARM_ANIMALS), ctx.x() + 2.0, ctx.y(), ctx.z());
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.actionBar("§6炽足兽：顺路吗？不顺也不带。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_purpur_decoration", "紫珀装饰", TrollTier.PRANK)
                .desc("说你家的装修风格过于朴素，随手撒几块紫色方块，审美攻击型整活")
                .weight(96)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.TROLL_BLOCKS), 6);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "你这装修风格我给零分。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.3f);
                    ctx.msg("§d紫色是末地城的官方色，免费送你几块。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_fake_end_join", "假末地玩家加入", TrollTier.PRANK)
                .desc("伪造一条「某个熟人在末地」的进出提示，再补一句他带了礼物（他没有）")
                .weight(94)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), true);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "我从末地城回来了，带了一堆潜影壳。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.0f);
                    ctx.msg("§7（这位玩家并不存在，礼物也不存在。）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_obsidian_counting", "黑曜石清点", TrollTier.PRANK)
                .desc("系统郑重通知你黑曜石库存正在被审计，配一声敲击音，实际什么都没动")
                .weight(92)
                .cooldownSeconds(85)
                .run(ctx -> {
                    ctx.sys("正在清点你的黑曜石库存……");
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.7f, 1.6f);
                    ctx.actionBar("§7清点结果：够用，但不够救我。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_ghast_lullaby", "恶魂摇篮曲", TrollTier.PRANK)
                .desc("远处传来恶魂的哭声，像在唱歌，配一层薄雾粒子，氛围拉满但不掉血")
                .weight(104)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 0.6f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 28);
                    ctx.title("§8§l它在唱歌", "§7歌词大意是「你跑不掉」");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_pearl_tease", "末影珍珠诱惑", TrollTier.PRANK)
                .desc("送你一颗可以瞬移的小东西，然后提醒你它可能会把你送进墙里")
                .weight(100)
                .cooldownSeconds(65)
                .run(ctx -> {
                    ctx.give(ctx.pick(TrollPools.WEIRD_ITEMS), 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.msg("§5末影珍珠：用之前请确认墙的另一边是空气。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_nether_radio", "下界电台", TrollTier.PRANK)
                .desc("伪造一个正在播报的下界电台，放一段跑调音效，然后表示信号已丢失")
                .weight(88)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.fakeBroadcast("§6[下界电台] 正在播放：岩浆的咆哮（循环版）");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.actionBar("§7信号丢失。主播已被恶魂带走。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_void_philosophy", "虚空哲学", TrollTier.PRANK)
                .desc("在聊天栏跟你讨论虚空的本质，不造成任何伤害，只造成精神内耗")
                .weight(90)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.msg("§8虚空不是没有东西，虚空是只剩你。");
                    ctx.sys(ctx.pick(TrollPools.TAUNTS));
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.7f, 0.8f);
                })
                .build());

        // ==================== ANNOYING ====================

        TrollEvents.reg(TrollEvent.of("dim_portal_backfire", "传送门回火", TrollTier.ANNOYING)
                .desc("打火石崩了一下，你身上冒火一秒，音效和粒子都齐全，代价很小")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.setFire(1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.4f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 20);
                    ctx.warn("点火顺序错了，先烧的是你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_gold_armor_rust", "金装生锈", TrollTier.ANNOYING)
                .desc("猪灵看了一眼你的金装备，它就掉了耐久还多了个羞辱名字")
                .weight(128)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.damageRandomTool(ctx.ri(8, 25));
                    ctx.renameRandomItem(ctx.pick(TrollPools.CURSED_NAMES));
                    ctx.fakeChat("猪灵", "金子在我这，才是金子。");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_soul_sand_march", "灵魂沙行军", TrollTier.ANNOYING)
                .desc("脚底像陷进灵魂沙，移动被拖慢一截，灵魂粒子从鞋底往上冒")
                .weight(126)
                .cooldownSeconds(65)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 26);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 0.7f);
                    ctx.actionBar("§8每一脚都踩在别人的叹息上。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_glowstone_flicker", "萤石闪烁", TrollTier.ANNOYING)
                .desc("头顶的光源开始抽风，画面短暂变黑，音效跟着一闪一闪")
                .weight(124)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.BLACKOUT, 40, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 1.7f);
                    ctx.msg("§7萤石快不行了，建议你也别太乐观。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_blaze_sneeze", "烈焰人喷嚏", TrollTier.ANNOYING)
                .desc("附近刷出一点下界的东西，冲你打了个喷嚏，留下火星和一肚子火气")
                .weight(122)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.setFire(1);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_piglin_tax", "猪灵收税", TrollTier.ANNOYING)
                .desc("猪灵宣布你欠它一笔过路费，直接从你背包里抽走一件东西")
                .weight(125)
                .cooldownSeconds(70)
                .run(ctx -> {
                    if (ctx.takeRandomItem()) {
                        ctx.fakeChat("猪灵", "这是过路费，收据没有。");
                    } else {
                        ctx.fakeChat("猪灵", "你身上居然什么都没有，穷得让人心疼。");
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                    ctx.warn("下界没有免费通道，只有更贵的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_hoglin_shove", "疣猪兽撞人", TrollTier.ANNOYING)
                .desc("一头看不见的疣猪兽从侧面把你拱飞，还留下一声很不礼貌的哼")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-0.7f, 0.7f), 0.6, ctx.rf(-0.7f, 0.7f));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.actionBar("§6疣猪兽：路是你站错地方了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_lava_drip", "岩浆滴落", TrollTier.ANNOYING)
                .desc("天花板往下滴岩浆，烫你两点血，配上嘶嘶声和几颗火星")
                .weight(118)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.damage("fire", 2.0f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 14);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 1.3f);
                    ctx.warn("下界的顶棚从来不是不漏的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_enderman_block_theft", "末影人偷方块", TrollTier.ANNOYING)
                .desc("你脚下的地基被末影人搬走了，声音很轻，动作很快，账算你头上")
                .weight(121)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.breakUnder();
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 1.4f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 22);
                    ctx.msg("§5它只偷方块，顺便偷走你的安全感。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_shulker_misfire", "潜影贝误射", TrollTier.ANNOYING)
                .desc("凭空挨了一发潜影弹，浮起来又落下去，全程找不到发射者")
                .weight(116)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 100, 0);
                    ctx.damage("magic", 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.1f);
                    ctx.actionBar("§d弹道来自一个你够不到的地方。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_chorus_scramble", "紫颂乱传送", TrollTier.ANNOYING)
                .desc("像吃了没熟的紫颂果，被随机丢到附近，落地还带一声蘑菇音")
                .weight(119)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.scatter(10);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.3f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 30);
                    ctx.msg("§d紫颂果的正确吃法是烤过，而不是生啃。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_end_stone_dust", "末地石粉尘", TrollTier.ANNOYING)
                .desc("一阵惨白粉尘扑面而来，视野模糊，喉咙发干，随机挂个负面状态")
                .weight(114)
                .cooldownSeconds(65)
                .run(ctx -> {
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 50);
                    ctx.effectRandom(180, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 0.9f, 1.1f);
                    ctx.actionBar("§f末地石的粉尘，吸一口少活三秒。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_purpur_maze", "紫珀迷宫", TrollTier.ANNOYING)
                .desc("周围被随手种下一圈紫色障碍，出口还在，只是你得先绕一圈")
                .weight(110)
                .cooldownSeconds(85)
                .run(ctx -> {
                    ctx.scatterBlocks(ctx.pick(TrollPools.TROLL_BLOCKS), 8);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.msg("§d末地城的城市规划就这水平，凑合走。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_gateway_spin", "折跃门眩晕", TrollTier.ANNOYING)
                .desc("视角被折跃门拧了半圈，还附带一层眩晕画面，方向感当场离职")
                .weight(112)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.spin(ctx.rf(-180f, 180f));
                    ctx.randomRotation();
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 60, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_nether_fog", "下界迷雾", TrollTier.ANNOYING)
                .desc("视野被硬生生拉到最近，雾粒子贴脸，走路全靠运气")
                .weight(115)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FOV_ZOOM, 100, 1);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 45);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 0.8f);
                    ctx.actionBar("§8能见度：取决于你有多想活着。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_blaze_rod_swap", "烈焰棒调包", TrollTier.ANNOYING)
                .desc("快捷栏被烈焰人重新排过序，你伸手拿到的永远不是想要的那件")
                .weight(108)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.2f);
                    ctx.msg("§6烈焰人表示：手速也是实力的一部分。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_ghast_balloon", "恶魂气球", TrollTier.ANNOYING)
                .desc("你被一只路过的东西当成气球抓走了，升上去六格再慢慢落下")
                .weight(111)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.lift(6.0);
                    ctx.resetFallDistance();
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                    ctx.title("§7§l高空体验", "§7恶魂的临时航线");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_strider_slip", "炽足兽打滑", TrollTier.ANNOYING)
                .desc("脚下一滑，速度被抽走，姿势也变得很难看，好在地上没有岩浆")
                .weight(106)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 1);
                    ctx.fling(ctx.rf(-0.4f, 0.4f), 0.3, ctx.rf(-0.4f, 0.4f));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_soul_fire_camp", "灵魂火堆", TrollTier.ANNOYING)
                .desc("你身上着了蓝色的火，三秒后火自己灭了，还附赠一句「服务已结束」")
                .weight(105)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.setFire(3);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 30);
                    ctx.later(80, () -> {
                        ctx.extinguish();
                        ctx.msg("§3灵魂火已熄灭，本次焚烧由模组友情赞助。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_end_portal_hum", "末地门嗡鸣", TrollTier.ANNOYING)
                .desc("耳边响起末地传送门的低频嗡鸣，画面闪黑一下，让人膝盖发软")
                .weight(109)
                .cooldownSeconds(65)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.6f);
                    ctx.glitch(GlitchIds.BLACKOUT, 30, 1);
                    ctx.actionBar("§5嗡——这个声音会跟着你回家。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_golden_boots_melt", "金靴融化", TrollTier.ANNOYING)
                .desc("你的鞋底开始冒烟，耐久掉了一截，脚上还带着火")
                .weight(103)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.damageRandomTool(ctx.ri(10, 30));
                    ctx.setFire(2);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 18);
                    ctx.warn("金子熔点低，这不是常识吗。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_piglin_barter", "猪灵以物易物", TrollTier.ANNOYING)
                .desc("猪灵收走你一件东西，还给你一件更没用的，交易完成概不退换")
                .weight(113)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.giveRandomJunk();
                    ctx.fakeChat("猪灵", "交易公平，只是公平的定义由我决定。");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_magma_cream_hands", "岩浆膏黏手", TrollTier.ANNOYING)
                .desc("手上糊了一层岩浆膏，快捷栏滑来滑去，拿什么都拿不稳")
                .weight(107)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.scrambleHotbar();
                    ctx.randomSelectSlot();
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.8f);
                    ctx.actionBar("§6岩浆膏：黏住的不只是手。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_endermite_infestation", "末影螨寄生", TrollTier.ANNOYING)
                .desc("脚边钻出几只末影螨乱窜，又咬又吵，打它们还得弯腰")
                .weight(104)
                .cooldownSeconds(85)
                .run(ctx -> {
                    ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING), ctx.x() + 1.0, ctx.y(), ctx.z() + 1.0);
                    ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING), ctx.x() - 1.0, ctx.y(), ctx.z() - 1.0);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.5f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_void_gaze", "虚空凝视", TrollTier.ANNOYING)
                .desc("你感觉到脚下有什么在看上来，手掌大的标题跳出来，脑袋跟着一沉")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§8它一直在那里，只是你以前没低头");
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 0.6f);
                    ctx.addRage(6.0);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_obsidian_wall", "黑曜石矮墙", TrollTier.ANNOYING)
                .desc("你面前突然多出一段黑曜石矮墙，翻得过去，但很烦人")
                .weight(101)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.setBlock(1, 0, 0, ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.setBlock(1, 1, 0, ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.setBlock(0, 0, 1, ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§5黑曜石：又硬又慢，还很贵。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_nether_warp", "下界坐标错乱", TrollTier.ANNOYING)
                .desc("在下界走路本该是八倍，现在你连自己朝哪边走都不确定了")
                .weight(105)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.spin(ctx.rf(-120f, 120f));
                    ctx.randomRotation();
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.1f);
                    ctx.actionBar("§7坐标比例：1 比 8，方向感：0 比 8。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_blaze_heat", "烈焰烘烤", TrollTier.ANNOYING)
                .desc("烈焰人对着你吹了口气，先着火，再被一阵风吹灭，纯折磨")
                .weight(106)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.setFire(2);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 22);
                    ctx.later(60, () -> {
                        ctx.extinguish();
                        ctx.msg("§7火灭得比你的耐心还快。");
                    });
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_end_sky_flip", "末地天翻地覆", TrollTier.ANNOYING)
                .desc("上下视角被反过来，鼠标怎么动都别扭，好在几秒后自动恢复")
                .weight(102)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.INVERT_PITCH, 100, 1);
                    ctx.spin(ctx.rf(-60f, 60f));
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.2f);
                    ctx.actionBar("§5末地的天空没有上下，只有你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_shulker_box_mix", "潜影盒混装", TrollTier.ANNOYING)
                .desc("有人把你的背包当成潜影盒摇了一遍，东西还在，顺序全没了")
                .weight(108)
                .cooldownSeconds(85)
                .run(ctx -> {
                    ctx.shuffleInventory();
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.0f);
                    ctx.msg("§d潜影盒的正确用法是装东西，不是摇匀。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_chorus_heartburn", "紫颂果反胃", TrollTier.ANNOYING)
                .desc("一颗没熟的紫颂果让你胃里翻江倒海，画面晃得比脚步还厉害")
                .weight(107)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.exhaust(4.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.warn("紫颂果的味道：像末地石泡水。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_glowstone_glare", "萤石刺眼", TrollTier.ANNOYING)
                .desc("一颗萤石在你眼前炸开，屏幕闪白，耳朵嗡嗡响")
                .weight(109)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.FLASHBANG, 30, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.6f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 35);
                    ctx.msg("§e萤石很亮，亮到你不想再看见它。");
                })
                .build());

        // ==================== BRUTAL ====================

        TrollEvents.reg(TrollEvent.of("dim_portal_ambush", "传送门伏击", TrollTier.BRUTAL)
                .desc("你刚从门里出来就被传开，两只下界常客在终点等着，接风仪式非常热情")
                .weight(68)
                .cooldownSeconds(280)
                .run(ctx -> {
                    ctx.scatter(18);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.9f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 35);
                    ctx.warn("欢迎光临下界，出口在另一个方向。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_lava_floor", "岩浆地板", TrollTier.BRUTAL)
                .desc("脚下的地板换成了岩浆，你被抬高三格看风景，风景很烫")
                .weight(64)
                .cooldownSeconds(300)
                .when(ctx -> !ctx.isInLava())
                .run(ctx -> {
                    ctx.lavaPit(1);
                    ctx.lift(3.0);
                    ctx.setFire(3);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.8f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 30);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_blaze_volley", "烈焰齐射", TrollTier.BRUTAL)
                .desc("三个方向的火球同时问候你，身上着火，耳边爆炸，地上还留了坑")
                .weight(66)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.setFire(4);
                    ctx.boomAtPlayer(2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.2f, 0.9f);
                    ctx.warn("烈焰人的礼貌：先喊一声，再烧你。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_ghast_bombardment", "恶魂轰炸", TrollTier.BRUTAL)
                .desc("三发看不见的炮弹落在你周围，地面震动，天空传来很满意的哭声")
                .weight(65)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.boom(ctx.x() + ctx.ri(-5, 5), ctx.y(), ctx.z() + ctx.ri(-5, 5), 2.5f);
                    ctx.boom(ctx.x() + ctx.ri(-5, 5), ctx.y(), ctx.z() + ctx.ri(-5, 5), 2.5f);
                    ctx.boom(ctx.x() + ctx.ri(-5, 5), ctx.y(), ctx.z() + ctx.ri(-5, 5), 2.5f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.2f, 0.7f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 45);
                    ctx.title("§c§l空中炮击", "§7恶魂正在校准，你正在跑");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_piglin_raid", "猪灵突袭", TrollTier.BRUTAL)
                .desc("三只猪灵同时开抢，背包掉了一件，身上还挂了负面状态")
                .weight(63)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.takeRandomItem();
                    ctx.effectRandom(300, 0);
                    ctx.sendToAll("§6[下界] 猪灵巡逻队已抵达本区块，请自觉交出金锭。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_hoglin_charge", "疣猪兽冲锋", TrollTier.BRUTAL)
                .desc("被一头疣猪兽正面撞飞，落地之前背后又刷出一只，二连击服务")
                .weight(62)
                .cooldownSeconds(290)
                .run(ctx -> {
                    ctx.fling(ctx.rf(-1.2f, 1.2f), 1.0, ctx.rf(-1.2f, 1.2f));
                    ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    ctx.damage("flyintowall", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.7f);
                    ctx.warn("疣猪兽不认识你，但它很确定你挡路。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_enderman_swarm", "末影人包围", TrollTier.BRUTAL)
                .desc("三只末影人在你和世界之间站成一圈，你抬头一次，它们就全部转头")
                .weight(64)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.3f, 1.1f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 60);
                    ctx.actionBar("§5你刚才是不是眨眼了？");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_shulker_barrage", "潜影贝弹幕", TrollTier.BRUTAL)
                .desc("三发潜影弹连环命中，你浮起来又摔下去，摔下去又浮起来")
                .weight(60)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.effectRandom(200, 1);
                    ctx.damage("magic", 4.0f);
                    ctx.lift(5.0);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 1.0f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                    ctx.warn("潜影贝的弹道不受物理约束，你的下落也是。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_chorus_far_hop", "紫颂远跳", TrollTier.BRUTAL)
                .desc("一口气被丢到一百多格外，落地位置由一颗果子决定，你不参与决策")
                .weight(58)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.scatterFar(120);
                    ctx.damage("fall", 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 1.3f);
                    ctx.title("§d§l紫颂跳跃", "§7目的地：随机，且不可退票");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_void_rift", "虚空裂隙", TrollTier.BRUTAL)
                .desc("脚下被挖开一个八格深的洞，洞口冒着传送门粒子，掉下去只是开始")
                .weight(61)
                .cooldownSeconds(300)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.voidPit(2, 8);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.2f, 0.8f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 50);
                    ctx.warn("虚空不欠你一条命，只是先记着。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_purpur_cell", "紫珀牢房", TrollTier.BRUTAL)
                .desc("末地风格的单人牢房，四面封死、头顶封顶，装饰相当考究")
                .weight(59)
                .cooldownSeconds(320)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.cageWith(ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.effectRandom(240, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.7f);
                    ctx.msg("§d紫珀块砌的牢房，隔音效果意外地好。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_gateway_exile", "折跃流放", TrollTier.BRUTAL)
                .desc("折跃门把你扔到不知道哪里的坐标，还顺手挂了个负面状态")
                .weight(60)
                .cooldownSeconds(320)
                .run(ctx -> {
                    ctx.scatterFar(180);
                    ctx.effectRandom(300, 0);
                    ctx.scatter(15);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.2f, 0.9f);
                    ctx.title("§5§l折跃完成", "§7坐标：已删除");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_nether_ceiling", "下界封顶", TrollTier.BRUTAL)
                .desc("周围瞬间长出成片的地狱岩装饰，空气一下子不够用了")
                .weight(57)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.surroundWith(ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.effectRandom(200, 0);
                    ctx.setFire(2);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.8f);
                    ctx.warn("下界的天花板不是给人留的。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_soul_sand_grip", "灵魂沙之握", TrollTier.BRUTAL)
                .desc("几十只手从脚下冒出来抓住你，动不了也打不动，只能等它们松手")
                .weight(62)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.freeze(160);
                    ctx.effectRandom(240, 1);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 70);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.2f, 0.6f);
                    ctx.actionBar("§8它们在数你还有几口气。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_magma_eruption", "岩浆喷发", TrollTier.BRUTAL)
                .desc("地面裂开铺满岩浆，爆炸把你掀起来，火星和疼痛一起到账")
                .weight(63)
                .cooldownSeconds(310)
                .when(ctx -> !ctx.isInLava())
                .run(ctx -> {
                    ctx.lavaPit(2);
                    ctx.boomAtPlayer(2.5f);
                    ctx.setFire(4);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.2f, 0.8f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.BOOM_PARTICLES), 50);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_end_city_fake_loot", "末地城假宝箱", TrollTier.BRUTAL)
                .desc("末地城风格的奖励发放：一堆垃圾、一句假情报和一个消失诅咒")
                .weight(56)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.giveRandomJunk();
                    ctx.giveRandomJunk();
                    ctx.curseHeld();
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "顶层箱子我全拿了，剩下的是你的。");
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.1f, 1.0f);
                    ctx.warn("末地城的规矩：先到先得，后到得诅咒。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_blaze_rod_snap", "烈焰棒折断", TrollTier.BRUTAL)
                .desc("你手里那件工具被烈焰烤到几乎报废，身上也跟着烧起来")
                .weight(58)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.breakRandomTool();
                    ctx.setFire(4);
                    ctx.damage("fire", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 1.1f);
                    ctx.warn("烈焰棒的正确用途是燃料，不是武器。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_ghast_tear_tax", "恶魂之泪税", TrollTier.BRUTAL)
                .desc("恶魂哭了一场，账单记在你头上：一件物品加三点血")
                .weight(58)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.damage("fire", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.2f, 0.7f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 40);
                    ctx.msg("§7恶魂的眼泪很贵，贵到你必须付。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_endermite_burrow", "末影螨钻心", TrollTier.BRUTAL)
                .desc("一群末影螨从地里钻出来啃你，想跑却发现腿被冻住了")
                .weight(59)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING), ctx.x() + 1.0, ctx.y(), ctx.z());
                    ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING), ctx.x() - 1.0, ctx.y(), ctx.z());
                    ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING), ctx.x(), ctx.y(), ctx.z() + 1.0);
                    ctx.freeze(120);
                    ctx.damage("magic", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 1.4f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_obsidian_trap", "黑曜石陷阱", TrollTier.BRUTAL)
                .desc("你被黑曜石从脚封到头顶，里头还挂了个负面状态，挖矿得从内部开始")
                .weight(60)
                .cooldownSeconds(320)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.cageWith(ctx.pick(TrollPools.TROLL_BLOCKS));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.6f);
                    ctx.msg("§5黑曜石棺材升级版，这次有窗（并没有）。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_void_whisper", "虚空低语", TrollTier.BRUTAL)
                .desc("虚空凑到你耳边说了句话，画面一黑，身上直接掉了几点血")
                .weight(57)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.damage("outofworld", 3.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.2f, 0.6f);
                    ctx.glitch(GlitchIds.BLACKOUT, 60, 2);
                    ctx.title("§8§l它说：下来坐坐", "§7座位的名字刻着你");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_piglin_debt", "猪灵讨债", TrollTier.BRUTAL)
                .desc("猪灵拿出账本，说你欠了两件东西，利息是几点血")
                .weight(55)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.takeRandomItem();
                    ctx.takeRandomItem();
                    ctx.damage("magic", 3.0f);
                    ctx.fakeChat("猪灵", "账本在这里，你的签名是脚印。");
                    ctx.sendToAll("§6[下界] 债务关系已确认，拒绝协商。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_strider_lava_bath", "炽足兽泡澡", TrollTier.BRUTAL)
                .desc("被强行安排和一只炽足兽共享岩浆浴，它很享受，你很烫")
                .weight(54)
                .cooldownSeconds(320)
                .when(ctx -> !ctx.isInLava())
                .run(ctx -> {
                    ctx.mount(ctx.pick(TrollPools.RIDEABLE));
                    ctx.lavaPit(2);
                    ctx.setFire(5);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.2f, 0.8f);
                    ctx.warn("炽足兽觉得温度刚好，你觉得不至于。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_end_void_drop", "末地高空抛落", TrollTier.BRUTAL)
                .desc("你被提到二十格高空，脚下什么都没有，落地时间由重力决定")
                .weight(59)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.lift(20.0);
                    ctx.effectRandom(120, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.2f, 1.0f);
                    ctx.title("§d§l末地坠落测试", "§7本次测试没有安全网");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_shulker_teleport_jail", "潜影贝传送", TrollTier.BRUTAL)
                .desc("被潜影贝咬了一口后原地消失，出现在十几格外，还飘着下不来")
                .weight(58)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.teleport(ctx.x() + ctx.ri(-16, 16), ctx.y() + 6.0, ctx.z() + ctx.ri(-16, 16));
                    ctx.effectRandom(160, 0);
                    ctx.damage("magic", 2.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_chorus_mutation", "紫颂变异", TrollTier.BRUTAL)
                .desc("紫颂果的效果在你身上失控，两个负面状态同时发作，骨头都在响")
                .weight(56)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.effectRandom(240, 1);
                    ctx.effectRandom(240, 1);
                    ctx.damage("magic", 3.0f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 60);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.2f, 0.9f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_nether_roof_toss", "下界顶棚抛尸", TrollTier.BRUTAL)
                .desc("你被一口气抛向上方三十格，头顶是基岩，脚下是深渊")
                .weight(57)
                .cooldownSeconds(300)
                .run(ctx -> {
                    ctx.lift(30.0);
                    ctx.fling(ctx.rf(-0.6f, 0.6f), 0.4, ctx.rf(-0.6f, 0.6f));
                    ctx.setFire(3);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.2f, 0.7f);
                    ctx.warn("下界顶棚是基岩，你只是短暂地摸到了它。");
                })
                .build());

        // ==================== NIGHTMARE ====================

        TrollEvents.reg(TrollEvent.of("dim_lava_sea", "岩浆之海", TrollTier.NIGHTMARE)
                .desc("脚下三格全部换成岩浆，两只下界常客在岸边等你上岸，岸也是岩浆")
                .weight(36)
                .cooldownSeconds(480)
                .when(ctx -> !ctx.isInLava())
                .run(ctx -> {
                    ctx.lavaPit(3);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.titleToAll("§6§l岩浆之海已生成", "§7本次没有岸");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.3f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_blaze_legion", "烈焰军团", TrollTier.NIGHTMARE)
                .desc("烈焰人对面列队，火球加陨石一起砸，全服共享同一场高温")
                .weight(34)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.setFire(5);
                    ctx.meteorShower(4, 20.0);
                    ctx.title("§6§l烈焰军团", "§7编制完整，指挥缺失");
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.4f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_ghast_choir", "恶魂合唱", TrollTier.NIGHTMARE)
                .desc("三只恶魂同时开嗓，雷暴伴奏，炮弹当节拍，全服听众均无法退场")
                .weight(33)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                    ctx.thunderstorm(1800);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.titleToAll("§8§l恶魂合唱团", "§7曲目：你不会活到安可");
                    ctx.soundToAll(ctx.pick(TrollPools.HORROR), 1.4f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_enderman_tide", "末影人之潮", TrollTier.NIGHTMARE)
                .desc("成群的黑色高个子从各个方向走来，全服失明，谁眨眼谁负责")
                .weight(35)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 90);
                    ctx.titleToAll("§5§l它们都看着你", "§7所以请你也看着它们");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_dragon_shadow", "末影龙阴影", TrollTier.NIGHTMARE)
                .desc("天上掠过一片巨大的影子，八连陨石落地，全服抬头都会看见同一片黑暗")
                .weight(32)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.meteorShower(8, 26.0);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.5f, 0.6f);
                    ctx.damage("magic", 4.0f);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 80, 3);
                    ctx.titleToAll("§5§l它回来了", "§7末影龙只是路过，顺便拆了地面");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_shulker_prison", "潜影贝监狱", TrollTier.NIGHTMARE)
                .desc("被紫珀块封死在原地，潜影弹从缝里飞进来，浮空和窒息交替进行")
                .weight(34)
                .cooldownSeconds(500)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.cageWith(ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.effectRandom(300, 1);
                    ctx.damage("magic", 4.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.3f, 1.0f);
                    ctx.warn("这间牢房的设计理念是：没有理念。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_void_banish", "虚空放逐", TrollTier.NIGHTMARE)
                .desc("脚下挖空十二格、失重浮空、硬黑屏一起上，虚空第一次这么热情")
                .weight(33)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.voidPit(3, 12);
                    ctx.noGravity(140);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.damage("outofworld", 4.0f);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 100, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.4f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_chorus_dimension_hop", "紫颂维度跳跃", TrollTier.NIGHTMARE)
                .desc("画面假装重载世界，你被丢到很远的地方，背包顺序也一起重排了")
                .weight(31)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.scatterFar(200);
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 120, 2);
                    ctx.shuffleInventory();
                    ctx.effectRandom(240, 0);
                    ctx.title("§d§l维度重定向", "§7目标坐标：模组说了算");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_nether_inferno", "下界炼狱", TrollTier.NIGHTMARE)
                .desc("岩浆铺地、雷暴压顶、三只怪入场，全服标题宣布炼狱正式营业")
                .weight(35)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.lavaPit(3);
                    ctx.thunderstorm(2400);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.titleToAll("§4§l下界炼狱", "§7营业中，恕不接待");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.4f, 0.7f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_gateway_collapse", "折跃门崩塌", TrollTier.NIGHTMARE)
                .desc("脚下的门炸开，陨石和深坑同时出现，画面撕裂得像真的坏了")
                .weight(32)
                .cooldownSeconds(520)
                .run(ctx -> {
                    ctx.voidPit(3, 10);
                    ctx.meteorShower(5, 22.0);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 100, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.4f, 0.7f);
                    ctx.warn("折跃门本来就是借来的，现在它收回去了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_piglin_slaughterhouse", "猪灵屠场", TrollTier.NIGHTMARE)
                .desc("五只猪灵与疣猪兽混合编队包围你，全服挂上虚弱，没有任何谈判窗口")
                .weight(34)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 240, 0);
                    ctx.sendToAll("§6[下界] 屠场已开张，取号机坏了。");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.3f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_wither_visit", "凋灵来访", TrollTier.NIGHTMARE)
                .desc("天边飘来一个不该来做客的东西，凋零加身，雷声跟着它走")
                .weight(32)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                    ctx.damage("wither", 4.0f);
                    ctx.thunderstorm(1200);
                    ctx.lightningNear();
                    ctx.titleToAll("§8§l有客人到了", "§7它不敲门，也不需要门");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.4f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_end_city_siege", "末地城围攻", TrollTier.NIGHTMARE)
                .desc("四只怪封住出口，潜影贝在上方扫射，冻结定身后你只剩听天由命")
                .weight(33)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 5);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 7);
                    ctx.freeze(200);
                    ctx.damage("magic", 4.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.3f, 0.8f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_soul_river", "灵魂之河", TrollTier.NIGHTMARE)
                .desc("灵魂粒子汇成一条河把你冲走，全身僵硬，眼前一黑，河里还有人在拽你")
                .weight(31)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.freeze(220);
                    ctx.damage("magic", 4.0f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 100);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 90, 2);
                    ctx.title("§3§l灵魂之河", "§7下水免费，上岸收费");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_purpur_execution", "紫珀处刑", TrollTier.NIGHTMARE)
                .desc("被紫珀块封死，四种伤害轮流上，唯一的好消息是没有旁观者")
                .weight(30)
                .cooldownSeconds(520)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.cageWith(ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.damage("magic", 3.0f);
                    ctx.damage("wither", 3.0f);
                    ctx.damageRandomTool(ctx.ri(20, 60));
                    ctx.soundSelf(ctx.pick(TrollPools.ULTRA), 1.4f, 0.7f);
                    ctx.warn("处刑记录已归档，归档员是一只潜影贝。");
                })
                .build());

        // ==================== APOCALYPSE ====================

        TrollEvents.reg(TrollEvent.of("dim_nether_apocalypse", "下界天启", TrollTier.APOCALYPSE)
                .desc("岩浆铺满、十二连陨石、两只精英入场、全服凋零，最后弹一个假崩溃界面")
                .weight(19)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.lavaPit(3);
                    ctx.meteorShower(12, 34.0);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 7);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 240, 1);
                    ctx.titleToAll("§4§l下界天启", "§4全服共享，不接受退票");
                    ctx.glitch(GlitchIds.FAKE_CRASH, 140, 3);
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_end_apocalypse", "末地终焉", TrollTier.APOCALYPSE)
                .desc("挖空二十格、失重、十连陨石、假崩溃与假存档损坏，末地决定不再营业")
                .weight(18)
                .cooldownSeconds(1200)
                .once()
                .run(ctx -> {
                    ctx.voidPit(5, 20);
                    ctx.noGravity(200);
                    ctx.meteorShower(10, 38.0);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 160, 3);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 140, 3);
                    ctx.kill();
                    ctx.sendToAll("§4§l[末地] 本维度已停止服务，请自重。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_dragon_awakening", "末影龙苏醒", TrollTier.APOCALYPSE)
                .desc("全服巨龙咆哮、陨石齐落、三只精英空降，雷暴把整片天空染成黑色")
                .weight(20)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.meteorShower(10, 30.0);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 7);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 8);
                    ctx.thunderstorm(3600);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.titleToAll("§5§l末影龙已苏醒", "§7它记得你上次怎么赢的");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.6f, 0.6f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_void_consumes_all", "虚空吞噬一切", TrollTier.APOCALYPSE)
                .desc("把全服拉进同一个坑、挖空十六格、全员失重凋零，虚空这次不挑食")
                .weight(17)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.pullPlayersToMe(120.0);
                    ctx.voidPit(4, 16);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 0);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 160, 3);
                    ctx.titleToAll("§0§l虚空吞噬一切", "§8本次没有幸存者名单");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.5f, 0.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_dimension_collapse", "维度崩塌", TrollTier.APOCALYPSE)
                .desc("世界重载卡死、假崩溃、硬黑屏、八连陨石加全服公告：维度正在被删除")
                .weight(16)
                .cooldownSeconds(900)
                .once()
                .run(ctx -> {
                    ctx.glitch(GlitchIds.WORLD_RELOAD, 200, 3);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 160, 3);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 120, 3);
                    ctx.meteorShower(8, 28.0);
                    ctx.voidPit(3, 14);
                    ctx.sendToAll("§4§l[系统] 维度数据正在被删除，请勿保存，反正也保存不了。");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.6f, 0.5f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_lava_world", "熔岩世界", TrollTier.APOCALYPSE)
                .desc("脚下全成岩浆、雷暴与午夜同时降临、两只精英压场，最后来一句假内存溢出")
                .weight(19)
                .cooldownSeconds(900)
                .run(ctx -> {
                    ctx.lavaPit(3);
                    ctx.timeOfDay(18000L);
                    ctx.thunderstorm(4800);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 6);
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 7);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.glitch(GlitchIds.FAKE_OOM, 120, 2);
                    ctx.titleToAll("§6§l熔岩世界", "§7温度：不可描述");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("dim_last_portal", "最后一道传送门", TrollTier.APOCALYPSE)
                .desc("把全服封进紫珀与黑曜石的盒子，脚下浇满岩浆，门上写着「此路不通」")
                .weight(15)
                .cooldownSeconds(1200)
                .once()
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    ctx.cageWith(ctx.pick(TrollPools.EVIL_BLOCKS));
                    ctx.lavaPit(1);
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.dropEverything();
                    ctx.glitch(GlitchIds.FAKE_BAN, 160, 3);
                    ctx.sendToAll("§4§l[末地] 最后一道门已关闭，出口改为自愿放弃。");
                    ctx.soundToAll(ctx.pick(TrollPools.ULTRA), 1.6f, 0.5f);
                })
                .build());
    }
}
