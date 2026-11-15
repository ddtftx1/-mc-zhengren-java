package com.zhengren.trollmod.expansion;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollPools;
import com.zhengren.trollmod.core.TrollTier;

/**
 * 【挖掘与矿石】分类事件。
 *
 * <p>本文件是扩容批次的一部分（目标：事件总数 615 → 2000）。</p>
 *
 * <p>镐子会累，矿脉会跑，洞穴会呼吸。你挖的每一镐都在被人记账。
 * 本文件刻意不引用任何 Minecraft 类型，全部通过 {@code ctx.*} 与 {@code TrollPools.*} 组合实现。</p>
 */
public final class MiningEvents {

    private MiningEvents() {
    }

    public static void register() {
        TrollEvents.category("挖掘与矿石");

        // ================================================================
        // PRANK —— 小整，笑一下就好
        // ================================================================

        TrollEvents.reg(TrollEvent.of("mining_pick_opinion", "镐子点评握法", TrollTier.BRUTAL)
                .desc("你的镐子第一次开口，评价了你的握持姿势和人生选择")
                .weight(135)
                .cooldownSeconds(45)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.6f);
                    ctx.actionBar("§7镐子：§8手腕太僵，§e你这样挖不到钻石的§7。");
                    ctx.addRage(2);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_fake_diamond", "挖到钻石了（不是）", TrollTier.BRUTAL)
                .desc("石缝里闪过一丝蓝光，你凑近一看是块煤")
                .weight(130)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(1);
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.4f);
                    ctx.msg("§b那道光§7……是 §8矿洞灯反光§7。给你块煤压压惊。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cave_drip", "洞穴在滴水", TrollTier.PRANK)
                .desc("水滴声从你后脑勺方向传来，位置精确得不像自然现象")
                .weight(125)
                .cooldownSeconds(50)
                .run(ctx -> {
                    ctx.soundAt(ctx.pick(TrollPools.AMBIENT_CREEPY), ctx.x(), ctx.y() + 2, ctx.z() - 2, 0.9f, 1.2f);
                    ctx.msg("§8滴。§8滴。§7……滴在你脖子上。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_bat_dinner", "晚餐到了", TrollTier.ANNOYING)
                .desc("一只蝙蝠自称是你的晚餐，在你头顶盘旋并拒绝解释")
                .weight(120)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.giveMobCustomName(
                            ctx.spawnAny(ctx.pick(TrollPools.TINY_ANNOYING), ctx.x(), ctx.y() + 3, ctx.z()),
                            "§8你的晚餐");
                    ctx.msg("§7头顶那只生物表示：§8今天轮到我吃你§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cobble_poem", "圆石成诗", TrollTier.ANNOYING)
                .desc("你挖出的圆石在地上排成了一句不像好话的句子")
                .weight(120)
                .cooldownSeconds(70)
                .run(ctx -> {
                    ctx.fakeChat("圆石", "§7" + ctx.pick(TrollPools.TAUNTS));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.8f, 1.3f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_torch_miscount", "火把少了一根", TrollTier.BRUTAL)
                .desc("系统提示你火把数量减少了，但背包看起来一根没少")
                .weight(115)
                .cooldownSeconds(80)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.sys("火把库存：§e-1§7（理由：§8照明损耗§7）");
                    ctx.msg("§7你数了三遍，§8还是原来的数量§7，但你就是不放心。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_gravel_tickle", "沙砾挠脚", TrollTier.BRUTAL)
                .desc("脚下的沙砾轻轻塌了一点，像是有人在下面挠你")
                .weight(115)
                .cooldownSeconds(70)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[3]);
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.7f, 0.8f);
                    ctx.msg("§7沙子：§8不好意思，§7手滑。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_deepslate_hum", "深板岩在低鸣", TrollTier.BRUTAL)
                .desc("深板岩用低频震动表达了它对被挖掘这件事的不满")
                .weight(115)
                .cooldownSeconds(65)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 0.5f);
                    ctx.actionBar("§8深板岩：§7嗡————（翻译：别挖了）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_amethyst_chime", "紫水晶风铃", TrollTier.BRUTAL)
                .desc("紫水晶簇自己响了三声，音高正好卡在你最烦的频率上")
                .weight(115)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    for (int i = 0; i < 3; i++) {
                        ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.6f, 1.1f + i * 0.3f);
                    }
                    ctx.msg("§d水晶：§8叮、叮、叮§7。没别的意思，就是想响。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_rail_squeak", "铁轨吱呀", TrollTier.BRUTAL)
                .desc("废弃铁轨在你经过时发出非常配合气氛的响声")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    ctx.soundAt(ctx.pick(TrollPools.AMBIENT_CREEPY), ctx.x() + 2, ctx.y(), ctx.z(), 0.9f, 1.5f);
                    ctx.msg("§7铁轨：§8这段路，上次有人走过是 §e很多年前§8。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cart_horn", "矿车按喇叭", TrollTier.ANNOYING)
                .desc("停着的矿车冲你按了一声喇叭，没人知道喇叭装在哪")
                .weight(110)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 1.7f);
                    ctx.actionBar("§7矿车：§8哔——§7（无客运安排）");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_dripstone_tap", "石笋敲了一下", TrollTier.BRUTAL)
                .desc("石笋按节奏敲击地面，节奏和你心跳莫名同步")
                .weight(110)
                .cooldownSeconds(65)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    for (int i = 0; i < 4; i++) {
                        ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.5f, 1.8f);
                    }
                    ctx.msg("§7滴石：§8哒、哒、哒、哒§7——你已经在跟着数了。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_lapis_smudge", "青金石蹭脏手", TrollTier.BRUTAL)
                .desc("青金石粉末糊了你一手，洗不掉，但也不影响什么")
                .weight(110)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 12);
                    ctx.msg("§9你的手现在 §l很蓝§r§9，§8仅此而已§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_xp_puff", "一缕经验", TrollTier.ANNOYING)
                .desc("你得到了一小缕经验，小到系统都不好意思播报")
                .weight(110)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.addExperience(ctx.ri(1, 3));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.7f, 2.0f);
                    ctx.msg("§a经验 +一点点§7。§8别问具体多少，问就是不多。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_ore_face", "矿石长出了脸", TrollTier.BRUTAL)
                .desc("矿石表面上浮现出一张简单的脸，正对着你摇头")
                .weight(110)
                .cooldownSeconds(80)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.fakeChat("矿石", "§7你也配挖我？");
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 0.9f, 1.2f);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_headlamp_flicker", "头灯接触不良", TrollTier.BRUTAL)
                .desc("你的头灯闪了两下才亮，黑暗的那半秒非常漫长")
                .weight(115)
                .cooldownSeconds(70)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.glitch(GlitchIds.BLACKOUT, 8, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.7f, 1.6f);
                    ctx.msg("§e灯：§8没事，§7就是 §e松了§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_echo_hint", "洞穴回声提示", TrollTier.PRANK)
                .desc("洞穴深处传来一段回声，听起来像是在给你指路（错误的路）")
                .weight(110)
                .cooldownSeconds(75)
                .run(ctx -> {
                    ctx.sys("回声：§8往 §e左§8 走 §e200 格§8 有钻石§7。");
                    ctx.msg("§7顺便一提，§8左边是死路§7。");
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_pebble_helmet", "石子掉进头盔", TrollTier.ANNOYING)
                .desc("一颗小石子准确落进你的头盔，声音在你耳边放大了十倍")
                .weight(110)
                .cooldownSeconds(55)
                .run(ctx -> {
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.9f);
                    ctx.actionBar("§7当。");
                    ctx.addRage(2);
                })
                .build());

        // ================================================================
        // ANNOYING —— 烦人，会皱眉
        // ================================================================

        TrollEvents.reg(TrollEvent.of("mining_pick_dull", "镐子变钝了", TrollTier.ANNOYING)
                .desc("镐子突然失去手感，挖掘速度像被人踩住了刹车")
                .weight(135)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 180, 0);
                    ctx.damageRandomTool(20);
                    ctx.msg("§7镐子：§8我今天 §l不想努力§r§8 了。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_shovel_backfire", "铲子朝后铲", TrollTier.ANNOYING)
                .desc("铲子把力全用在了你身上，方向还很讲究地选了身后")
                .weight(125)
                .cooldownSeconds(80)
                .run(ctx -> {
                    ctx.fling(-ctx.rd(0.8, 1.8), 0.5, -ctx.rd(0.8, 1.8));
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§7铲子：§8这一铲，铲的是你自己。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_ore_vanish", "矿脉当场消失", TrollTier.BRUTAL)
                .desc("你刚要挖到矿，矿脉连带着周围一圈石块一起不见了")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    for (int i = 0; i < 5; i++) {
                        ctx.breakAt(ctx.randomNearbyPos(3));
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.1f);
                    ctx.msg("§7矿：§8我先走了§7，你慢慢挖石头。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_ore_switch", "钻石变深板岩", TrollTier.BRUTAL)
                .desc("你确认过三遍的钻石矿，挖下来变成了深板岩")
                .weight(130)
                .cooldownSeconds(95)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setFire(3);
                    ctx.setBlock(ctx.ri(-1, 1), 0, ctx.ri(-1, 1), ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.msg("§8矿石学第一课：§7看错了不算偷。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_fortune_zero", "时运归零", TrollTier.BRUTAL)
                .desc("你的时运附魔今天罢工，掉落数量回到最朴素的一")
                .weight(125)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.msg("§7时运：§8今天 §l时运 I§r§8，别嫌少。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_silk_fail", "精准采集差一点", TrollTier.BRUTAL)
                .desc("精准采集在最后一刻失效，方块碎成了一地没用的渣")
                .weight(125)
                .cooldownSeconds(95)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.give(ctx.pick(TrollPools.JUNK), 2);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.msg("§7精准采集：§8差 §e0.1%§8，很遗憾。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_gravel_slide", "沙砾滑了一段", TrollTier.BRUTAL)
                .desc("头顶的沙砾层缓缓下滑，把你下半身埋进沙子里")
                .weight(125)
                .cooldownSeconds(85)
                .run(ctx -> {
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[3]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 140, 0);
                    ctx.msg("§7沙砾：§8埋到腰就停，§7留你个头。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_creeper_swap", "苦力怕换了个位置", TrollTier.ANNOYING)
                .desc("你背后的苦力怕无声地换到了你正前方，礼貌地等你抬头")
                .weight(130)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 4);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 1.3f);
                    ctx.msg("§7它站在那儿 §e有一会儿了§7。");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_lava_drip", "头顶滴岩浆", TrollTier.NIGHTMARE)
                .desc("天花板上开始渗岩浆，一滴一滴，正好落在你脑袋上")
                .weight(125)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.setBlock(0, 2, 0, TrollPools.TROLL_BLOCKS[10]);
                    ctx.setFire(3);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§c一滴。§7然后是 §c第二滴§7。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cart_derail", "矿车出轨（物理）", TrollTier.NIGHTMARE)
                .desc("矿车在平直的铁轨上选择了自由，你跟着它一起自由")
                .weight(125)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.fling(ctx.rd(-1.5, 1.5), 1.0, ctx.rd(-1.5, 1.5));
                    ctx.damage("flyintowall", 2.0f);
                    ctx.msg("§7矿车：§8轨道是参考，不是限制。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_hopper_cart_steal", "漏斗矿车顺手牵羊", TrollTier.ANNOYING)
                .desc("路过的漏斗矿车从你背包里吸走了一件东西，头也不回")
                .weight(125)
                .cooldownSeconds(100)
                .run(ctx -> {
                    if (!ctx.invEmpty()) {
                        ctx.takeRandomItem();
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.7f);
                    ctx.msg("§7漏斗矿车：§8路过，§7顺便收个过路费。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_tnt_fizzle", "TNT 哑火了一下", TrollTier.NIGHTMARE)
                .desc("TNT 先哑火，等你看过去的时候才轻轻炸了一下")
                .weight(125)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.lavaPit(1);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.8f, 1.4f);
                    ctx.boomAtPlayer(1.2f);
                    ctx.msg("§7嘶——§8（沉默）§7——§c砰。");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_xp_thief", "经验被截胡", TrollTier.ANNOYING)
                .desc("矿石的经验飞出来绕了一圈，然后飞向了别的地方")
                .weight(120)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.damageRandomTool(5);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 0.9f, 1.4f);
                    ctx.msg("§a经验：§8我去那边看看§7（再也没回来）。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_deepslate_slow", "深板岩加倍坚硬", TrollTier.NIGHTMARE)
                .desc("深板岩临时提高了硬度标准，你的每一镐都像在挖墙")
                .weight(125)
                .cooldownSeconds(95)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 220, 0);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§8深板岩：§7硬度已上调，§8理由：§7不告诉你。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_stone_cough", "石头咳嗽了", TrollTier.ANNOYING)
                .desc("你敲下去的石头喷出一团粉尘，正中面门")
                .weight(120)
                .cooldownSeconds(85)
                .run(ctx -> {
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 140, 0);
                    ctx.msg("§7石头：§8咳、咳咳§7——§8不好意思，§7老毛病了。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cave_wind", "洞穴穿堂风", TrollTier.BRUTAL)
                .desc("一阵不知从哪来的风把你吹得横向漂移")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.fling(ctx.rd(-2.0, 2.0), 0.3, ctx.rd(-2.0, 2.0));
                    ctx.soundSelf(ctx.pick(TrollPools.AMBIENT_CREEPY), 1.0f, 0.8f);
                    ctx.msg("§7风向：§8从里面吹向外面§7，说明里面有什么。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_dripstone_fall", "钟乳石掉了", TrollTier.BRUTAL)
                .desc("头顶那根你最担心的钟乳石终于决定了它的归宿")
                .weight(125)
                .cooldownSeconds(95)
                .run(ctx -> {
                    ctx.spawnFallingBlock(TrollPools.TROLL_BLOCKS[3]);
                    ctx.damage("fall", 3.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.1f);
                    ctx.msg("§7它悬了 §e十分钟§7，就等你站过去。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_geode_trap", "紫水晶洞关门", TrollTier.BRUTAL)
                .desc("紫水晶洞在你进来之后合上了，四面都是漂亮的晶壁")
                .weight(120)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.cageWith(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 160, 0);
                    ctx.msg("§d晶洞：§8这里很漂亮吧？§7多待一会儿。");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_lapis_blue", "全身变蓝", TrollTier.NIGHTMARE)
                .desc("青金石粉尘顺着呼吸道进去了，你开始用一种很蓝的方式看世界")
                .weight(115)
                .cooldownSeconds(100)
                .run(ctx -> {
                    ctx.effectRandom(180, 0);
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 160, 0);
                    ctx.msg("§9你现在是 §l蓝色§r§9 的，§8大概两分钟。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_redstone_shock", "红石矿回你一手", TrollTier.NIGHTMARE)
                .desc("红石矿在你镐子碰上去的瞬间放了电，电路感十足")
                .weight(120)
                .cooldownSeconds(90)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(400, 2);
                    ctx.damage("magic", 2.5f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.TAUNT_PARTICLES), 15);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 1.8f);
                    ctx.msg("§c红石：§8来，§7握个手。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_emerald_dazzle", "绿宝石晃眼", TrollTier.NIGHTMARE)
                .desc("绿宝石的反光刺得你眼花，挖矿精度直线下降")
                .weight(115)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 0);
                    ctx.glitch(GlitchIds.FLASHBANG, 8, 1);
                    ctx.msg("§a真绿§7。§8绿到看不清路§7。");
                    ctx.addRage(7);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_gold_fever", "黄金热", TrollTier.NIGHTMARE)
                .desc("看见金矿之后你的手开始不听使唤，快捷栏自己跳舞")
                .weight(120)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.scrambleHotbar();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.1f);
                    ctx.msg("§6黄金热§7：§8你的手比你有主见§7。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_iron_rust", "铁器生锈", TrollTier.ANNOYING)
                .desc("你手里的铁工具以肉眼可见的速度长出了一层锈")
                .weight(120)
                .cooldownSeconds(95)
                .run(ctx -> {
                    ctx.damageRandomTool(35);
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 0.9f, 0.8f);
                    ctx.msg("§7铁器：§8矿洞湿气重§7，这很合理。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_coal_dust", "煤灰糊脸", TrollTier.NIGHTMARE)
                .desc("煤粉糊了你一脸，接下来十几秒你基本靠摸")
                .weight(120)
                .cooldownSeconds(95)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 150, 0);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 20);
                    ctx.msg("§8你现在是 §7黑色的§8，§7包括视野。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_copper_oxide", "铜矿蹭了一身绿", TrollTier.NIGHTMARE)
                .desc("铜矿的氧化层沾了你一身，味道非常「有年代感」")
                .weight(115)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    ctx.effectRandom(160, 0);
                    ctx.msg("§2铜绿§7已附着：§8建议不要舔§7。");
                    ctx.addRage(6);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cart_swap", "和矿车换了位置", TrollTier.NIGHTMARE)
                .desc("你和矿车互换了位置，现在它在你的位置，你在它的位置")
                .weight(115)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.breakRandomTool();
                    if (ctx.hasOthers()) {
                        ctx.swapWith(ctx.randomOtherPlayer());
                        ctx.msg("§7（其实是和 §e那位§7 换了，§8矿车够不着§7。）");
                    } else {
                        ctx.scatter(40);
                        ctx.msg("§7你和矿车换了位，§8代价是换到了四十格外§7。");
                    }
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_rail_switch", "铁轨岔路乱切", TrollTier.BRUTAL)
                .desc("铁轨的岔道在你到达前一秒全部改向了相反的方向")
                .weight(115)
                .cooldownSeconds(110)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.exhaust(12.0f);
                    for (int i = 0; i < 4; i++) {
                        ctx.setBlock(ctx.ri(-2, 2), 0, 1, ctx.pick(TrollPools.TROLL_BLOCKS));
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§7调度室：§8本次列车改道§7，改去哪不知道。");
                    ctx.addRage(8);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_ore_beacon", "矿石发信号", TrollTier.BRUTAL)
                .desc("你挖到的矿石发出了一束光，把你的位置广播给了附近所有生物")
                .weight(120)
                .cooldownSeconds(110)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 40);
                    for (int i = 0; i < 2; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 10);
                    }
                    ctx.msg("§e信号已发出§7，§8接收方：§7它们。");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_gravel_bait", "挖到的不是矿", TrollTier.BRUTAL)
                .desc("你精准定位的矿点被替换成了一整块沙砾，还附赠两块碎石")
                .weight(120)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.give(ctx.pick(TrollPools.JUNK), 2);
                    ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[3]);
                    ctx.msg("§7矿点已重新分配§8，分配对象：§7不是你。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_drop_magnet", "掉落物被吸走", TrollTier.BRUTAL)
                .desc("你刚挖出来的东西被一股看不见的力吸向了洞穴深处")
                .weight(120)
                .cooldownSeconds(110)
                .run(ctx -> {
                    if (!ctx.invEmpty()) {
                        ctx.dropRandom();
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 1.2f);
                    ctx.msg("§7物品：§8我跟着引力走了§7，勿念。");
                    ctx.addRage(9);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cave_scream", "洞穴深处有人尖叫", TrollTier.BRUTAL)
                .desc("一声惨叫从矿道尽头传来，紧接着是一片安静")
                .weight(120)
                .cooldownSeconds(100)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("magic", 1.5f);
                    ctx.randomHorrorSound();
                    ctx.title(ctx.pick(TrollPools.PANIC_TITLES), "§7来源：§8你前面§7。");
                    ctx.addRage(10);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_spawner_tease", "刷怪笼假动作", TrollTier.BRUTAL)
                .desc("刷怪笼开始旋转并发光，转了三圈之后表示今天不营业")
                .weight(120)
                .cooldownSeconds(120)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(240, 1);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 0.9f, 1.2f);
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 25);
                    ctx.msg("§8刷怪笼：§7试运行结束，§8下次一定。");
                    ctx.later(60, () -> ctx.msg("§8（它其实 §c在营业§8，只是没告诉你时间。）"));
                    ctx.addRage(11);
                })
                .build());

        // ================================================================
        // BRUTAL —— 抓狂，会骂人
        // ================================================================

        TrollEvents.reg(TrollEvent.of("mining_pick_snap", "镐子当场解体", TrollTier.BRUTAL)
                .desc("你的镐子在最有用的那一刻宣布退役，碎片掉了一地")
                .weight(55)
                .cooldownSeconds(200)
                .run(ctx -> {
                    ctx.breakRandomTool();
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.2f);
                    ctx.msg("§7镐子：§8我尽力了§7（并没有）。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_shovel_launch", "铲子把你铲出去", TrollTier.NIGHTMARE)
                .desc("铲子突然以你为支点完成了一次标准抛投动作")
                .weight(52)
                .cooldownSeconds(210)
                .run(ctx -> {
                    ctx.launch(2.6);
                    ctx.damage("fall", 3.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§7铲子：§8这一次，§7铲的是人。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_ore_trap_boom", "矿脉带陷阱", TrollTier.APOCALYPSE)
                .desc("这颗钻石下面压着一整圈 TNT，触发方式非常朴素")
                .weight(52)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    for (int i = 0; i < 5; i++) {
                        ctx.setBlock(ctx.ri(-3, 3), 0, ctx.ri(-3, 3), TrollPools.TROLL_BLOCKS[16]);
                    }
                    ctx.boomAtPlayer(2.8f);
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 70);
                    ctx.msg("§c恭喜挖到矿§7，也恭喜挖到 §c配套装置§7。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_lava_floor", "脚底换成岩浆池", TrollTier.APOCALYPSE)
                .desc("你脚下的矿道地板被就地改造成了岩浆，装修队已撤离")
                .weight(50)
                .cooldownSeconds(230)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.lavaPit(1);
                    ctx.setFire(8);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.7f);
                    ctx.msg("§c地热资源丰富§7，丰富到不讲道理。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_lava_bucket", "头顶一桶岩浆", TrollTier.APOCALYPSE)
                .desc("有人在你头顶非常精准地倒了一桶岩浆，倒完还道了歉")
                .weight(50)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.setBlock(0, 3, 0, TrollPools.TROLL_BLOCKS[10]);
                    ctx.setFire(10);
                    ctx.damage("lava", 4.0f);
                    ctx.fakeChat(ctx.pick(TrollPools.FAKE_NAMES), "抱歉啊，手滑了。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cave_in", "矿道塌方", TrollTier.NIGHTMARE)
                .desc("你的矿道完成了它的最后一次结构性调整")
                .weight(52)
                .cooldownSeconds(230)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[3]);
                    ctx.setBlock(0, 3, 0, TrollPools.TROLL_BLOCKS[3]);
                    ctx.damage(4.0f);
                    ctx.msg("§8塌方通知：§7本矿道已停止营业。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_gravel_burial", "沙砾活埋体验", TrollTier.APOCALYPSE)
                .desc("沙砾从四面八方涌来，你成了矿洞里的一件藏品")
                .weight(50)
                .cooldownSeconds(230)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.setExperience(0);
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[3]);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.msg("§8藏品编号 MINING-" + ctx.gameTicks() % 1000 + "§7，§8请勿触摸§7。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_void_shaft", "竖井直通虚空", TrollTier.APOCALYPSE)
                .desc("你脚下的矿石被挖穿，下面是一条没有底的人工竖井")
                .weight(48)
                .cooldownSeconds(240)
                .when(ctx -> ctx.isOnGround())
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.voidPit(1, 3);
                    ctx.damage("outofworld", 2.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.msg("§8施工方：§7这口井，§8我们没有挖到底§7。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_tnt_chain", "TNT 连锁掘进", TrollTier.APOCALYPSE)
                .desc("TNT 掘进法确实高效，只是爆破范围的计算方式比较奔放")
                .weight(50)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    for (int i = 0; i < 6; i++) {
                        ctx.spawnTnt(ctx.x() + ctx.ri(-4, 4), ctx.y() + ctx.ri(1, 3), ctx.z() + ctx.ri(-4, 4), 30);
                    }
                    ctx.boomAtPlayer(2.5f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.8f);
                    ctx.msg("§c掘进效率：§e极高§7。§c留存率：§e极低§7。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_creeper_ambush", "苦力怕包围圈", TrollTier.APOCALYPSE)
                .desc("矿道两端同时出现了几位非常安静的邻居")
                .weight(50)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileBehind(ctx.pick(TrollPools.HOSTILE));
                    }
                    ctx.sound(ctx.pick(TrollPools.HORROR), 1.0f, 1.0f);
                    ctx.title("§c§l嘶——", "§7不止一个方向");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cart_crash", "矿车脱轨撞击", TrollTier.APOCALYPSE)
                .desc("矿车在转弯处选择直线行驶，你选择跟着它直线飞行")
                .weight(48)
                .cooldownSeconds(230)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.dismount();
                    ctx.fling(ctx.rd(-2.0, 2.0), 1.6, ctx.rd(-2.0, 2.0));
                    ctx.damage("flyintowall", 4.0f);
                    ctx.msg("§7矿车：§8走直线最快§7，你要相信物理。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_hopper_cart_drain", "漏斗矿车清仓", TrollTier.NIGHTMARE)
                .desc("一节漏斗矿车贴着你开了过去，你的背包随之轻了不少")
                .weight(48)
                .cooldownSeconds(230)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.scatterFar(120);
                    for (int i = 0; i < 3; i++) {
                        ctx.takeRandomItem();
                    }
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 0.6f);
                    ctx.msg("§7漏斗矿车：§8清仓完毕§7，§8运费已抵扣§7。");
                    ctx.addRage(18);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_dripstone_impale", "石笋穿刺", TrollTier.APOCALYPSE)
                .desc("你落地的地方，正好有一根等待了千年的石笋")
                .weight(50)
                .cooldownSeconds(220)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.spawnFallingBlock(TrollPools.TROLL_BLOCKS[3]);
                    ctx.damage("fall", 6.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.9f);
                    ctx.msg("§7石笋：§8我等这一刻，§7等了 §e一千年§7。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_amethyst_storm", "紫水晶碎片风暴", TrollTier.NIGHTMARE)
                .desc("紫水晶簇集体爆裂，碎片像刀片一样四散")
                .weight(48)
                .cooldownSeconds(230)
                .run(ctx -> {
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 80);
                    ctx.damage(5.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 160, 1);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.6f);
                    ctx.msg("§d水晶风暴§7，§8掉率很高，§7掉的是你的血。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_xp_drain", "经验被抽干", TrollTier.NIGHTMARE)
                .desc("你攒了一整晚的经验，被一只看不见的手一次性取走")
                .weight(46)
                .cooldownSeconds(250)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.takeRandomItems(2);
                    ctx.setExperience(0);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.8f);
                    ctx.msg("§a经验余额：§c0§7。§8支出说明：§7未提供。");
                    ctx.addRage(19);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_deepslate_prison", "深板岩牢房", TrollTier.APOCALYPSE)
                .desc("深板岩从四壁长出来把你困住，牢房风格极其工业化")
                .weight(48)
                .cooldownSeconds(230)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.damage("outofworld", 8.0f);
                    ctx.cageWith(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.glitch(GlitchIds.HIDE_HUD, 80, 1);
                    ctx.msg("§8牢房设施：§7无窗、无灯、无解释。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_silverfish_swarm", "蠹虫开会", TrollTier.APOCALYPSE)
                .desc("你挖开的石头里藏着一整届蠹虫代表大会")
                .weight(48)
                .cooldownSeconds(230)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    for (int i = 0; i < 5; i++) {
                        ctx.giveMobCustomName(
                                ctx.spawnAny(ctx.pick(TrollPools.ANNOYING_MOBS),
                                        ctx.x() + ctx.ri(-3, 3), ctx.y(), ctx.z() + ctx.ri(-3, 3)),
                                "§8代表");
                    }
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.5f);
                    ctx.msg("§7会议议题：§8《关于咬你的若干决定》§7。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_enderman_steal", "末影人抢矿", TrollTier.NIGHTMARE)
                .desc("你刚挖出来的矿被一只末影人顺走，它还回头看了你一眼")
                .weight(46)
                .cooldownSeconds(240)
                .run(ctx -> {
                    if (!ctx.invEmpty()) {
                        ctx.takeRandomItem();
                    }
                    ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    ctx.soundSelf(ctx.pick(TrollPools.CREEPY), 1.0f, 0.9f);
                    ctx.msg("§5它拿走的是 §l最值钱的那件§r§5，§8它知道。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_fortune_curse", "时运倒着算", TrollTier.APOCALYPSE)
                .desc("你的时运附魔开始做减法，镐子也出现了裂纹")
                .weight(48)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.curseHeld();
                    ctx.damageRandomTool(40);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 0);
                    ctx.msg("§7时运：§8今天我算 §l负数§r§8，你忍着点。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_silk_reverse", "反向精准采集", TrollTier.APOCALYPSE)
                .desc("精准采集把方块完整地保留了下来，然后换成了另一块")
                .weight(46)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.setBlock(ctx.ri(-1, 1), 0, ctx.ri(-1, 1), ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.give(ctx.pick(TrollPools.JUNK), 1);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 1.3f);
                    ctx.msg("§7精准采集：§8方块保住了§7，§8保住的不是你那块§7。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cart_sky", "矿车送上天", TrollTier.APOCALYPSE)
                .desc("矿车沿着一截不存在的斜坡冲上了天，你只能跟着")
                .weight(46)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.effectRandom(600, 2);
                    ctx.lift(60);
                    ctx.fling(ctx.rd(-1.5, 1.5), 0.5, ctx.rd(-1.5, 1.5));
                    ctx.title("§c§l上升中", "§7回程票：无");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_rail_coaster", "过山车失控", TrollTier.BRUTAL)
                .desc("废弃铁轨突然变成了一段完整的过山车，问题是安全带也没装")
                .weight(46)
                .cooldownSeconds(250)
                .run(ctx -> {
                    ctx.lift(30);
                    ctx.launch(2.0);
                    ctx.glitch(GlitchIds.SHAKY, 100, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 200, 1);
                    ctx.msg("§7过山车：§8系不系安全带，§7结果都差不多。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_copper_lightning", "铜矿引雷", TrollTier.NIGHTMARE)
                .desc("你挖的铜矿成了完美的引雷装置，雷也这么认为")
                .weight(46)
                .cooldownSeconds(240)
                .run(ctx -> {
                    ctx.lightningNear();
                    ctx.lightning();
                    ctx.setFire(6);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 180, 1);
                    ctx.msg("§6导电性良好§7，§c实验结果：§7你冒烟了。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_emerald_greed", "绿宝石的馈赠", TrollTier.APOCALYPSE)
                .desc("绿宝石给了你一堆好东西，同时把位置广播给了整条矿道")
                .weight(46)
                .cooldownSeconds(240)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.give(ctx.pick(TrollPools.LOOT), 3);
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.msg("§a礼物收到了吗？§7§8它们也收到了§7。");
                    ctx.addRage(16);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_midas_touch", "点石成金（劣化版）", TrollTier.APOCALYPSE)
                .desc("你的手变成了金色，装备也跟着变成金色，属性一点没变")
                .weight(46)
                .cooldownSeconds(250)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    ctx.renameRandomItem("§6金色传说");
                    ctx.swapEquipmentRandomly();
                    ctx.soundSelf(ctx.pick(TrollPools.FUNNY), 1.0f, 1.0f);
                    ctx.msg("§6全部镀金§7，§8性能不变，价格也不变§7。");
                    ctx.addRage(15);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_bedrock_crack", "基岩裂了", TrollTier.APOCALYPSE)
                .desc("你脚下的基岩出现了一道裂缝，然后裂缝里传来非常不妙的声音")
                .weight(46)
                .cooldownSeconds(250)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.voidPit(1, 10);
                    ctx.setBlock(0, -1, 0, TrollPools.EVIL_BLOCKS[0]);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 60, 2);
                    ctx.boom(0.0, 0.0, 0.0, 1.5f);
                    ctx.msg("§8裂缝：§7下面还有东西，§4而且它在往上。");
                    ctx.addRage(17);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cave_blackout", "洞内全黑", TrollTier.NIGHTMARE)
                .desc("你的所有光源同时熄灭，周围多了一些有节奏的呼吸声")
                .weight(48)
                .cooldownSeconds(230)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.starve();
                    ctx.glitch(GlitchIds.BLACKOUT, 80, 2);
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 6);
                    }
                    ctx.title("§4§l灯灭了", "§7呼吸声不止一处");
                    ctx.addRage(18);
                })
                .build());

        // ================================================================
        // 终章 —— 绝望：矿脉永远差一格，深处一直有东西在等
        // ================================================================

        TrollEvents.reg(TrollEvent.of("mining_lava_tsunami", "地下岩浆海啸", TrollTier.OBLIVION)
                .desc("岩浆退下去过一次，那次是为了让你量清楚它下一次能涨多高")
                .weight(32)
                .cooldownSeconds(420)
                .run(ctx -> {
                    ctx.lavaPit(3);
                    ctx.setFire(15);
                    ctx.damage("lava", 6.0f);
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[10], 3);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.5f);
                    ctx.msg("§4岩浆：§7地下的东西，§8从来不归地面管§7。");
                    ctx.later(140, () -> {
                        // 假希望：退潮了
                        ctx.msg("§a岩浆退下去了。");
                        ctx.placeFloor(TrollPools.TROLL_BLOCKS[1], 3);
                    });
                    ctx.later(280, () -> {
                        ctx.lavaPit(6);
                        ctx.setFire(20);
                        ctx.damage("lava", 8.0f);
                        ctx.warn("§4退潮只是为了量出下一次的高度。§7现在它涨回来了。");
                    });
                    ctx.later(460, () -> {
                        ctx.stripAll();
                        ctx.meteorShower(24, 50);
                        ctx.obliterate();
                    });
                    ctx.addRage(25);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cave_collapse_full", "整条矿道报废", TrollTier.APOCALYPSE)
                .desc("从入口到尽头，整条矿道同一时间完成了塌方")
                .weight(30)
                .cooldownSeconds(440)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.buryAlive();
                    ctx.surroundWith(TrollPools.TROLL_BLOCKS[3]);
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[1], 4);
                    ctx.setBlock(0, 3, 0, TrollPools.TROLL_BLOCKS[3]);
                    ctx.damage(6.0f);
                    ctx.msg("§8矿道状态：§7已注销§8。");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_void_fall", "挖穿了世界底部", TrollTier.OBLIVION)
                .desc("你挖穿了世界底部，下面没有底，只有更下面")
                .weight(30)
                .cooldownSeconds(460)
                .run(ctx -> {
                    ctx.voidPit(2, 16);
                    ctx.teleport(ctx.x(), -60.0, ctx.z());
                    ctx.damage("outofworld", 3.0f);
                    ctx.glitch(GlitchIds.BLACKOUT, 60, 3);
                    ctx.msg("§8下方没有矿§7，§8下方没有底§7。");
                    ctx.later(160, () -> {
                        // 假希望：好像踩到了什么
                        ctx.msg("§a你好像踩到了什么。");
                        ctx.placeFloor(TrollPools.TROLL_BLOCKS[1], 1);
                    });
                    ctx.later(300, () -> {
                        ctx.voidPit(2, 20);
                        ctx.damage("outofworld", 5.0f);
                        ctx.teleport(ctx.x(), -120.0, ctx.z());
                        ctx.warn("§4那不是地面，是别的掉下来的人。");
                    });
                    ctx.later(460, () -> {
                        ctx.effectRandom(1200, 3);
                        ctx.obliterate();
                    });
                    ctx.addRage(30);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_tnt_cavern", "TNT 大厅", TrollTier.APOCALYPSE)
                .desc("你在一个装满 TNT 的大厅正中央，引信已经点着了")
                .weight(30)
                .cooldownSeconds(440)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.clearInventory();
                    for (int i = 0; i < 10; i++) {
                        ctx.spawnTnt(ctx.x() + ctx.ri(-5, 5), ctx.y() + ctx.ri(0, 3), ctx.z() + ctx.ri(-5, 5), 25);
                    }
                    ctx.boomAtPlayer(4.0f);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 80, 3);
                    ctx.title("§4§l点火", "§7引信长度：§8很短");
                    ctx.addRage(30);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_spawner_army", "刷怪笼集会", TrollTier.APOCALYPSE)
                .desc("你周围的刷怪笼同时开始工作，出席率百分之百")
                .weight(30)
                .cooldownSeconds(460)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    for (int i = 0; i < 7; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 9);
                    }
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.glitch(GlitchIds.SHAKY, 100, 2);
                    ctx.msg("§7会议主题：§8《关于你的一切》§7。");
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_obsidian_tomb", "黑曜石墓室", TrollTier.OBLIVION)
                .desc("墓室会先给你开一条缝，让你确认外面也一样黑")
                .weight(28)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[2]);
                    ctx.setHealth(6.0f);
                    ctx.setFire(15);
                    ctx.glitch(GlitchIds.HIDE_HUD, 120, 2);
                    ctx.msg("§8墓室装修标准：§7无出口、无解释、无退款。");
                    ctx.later(160, () -> {
                        // 假希望：墙上开了缝
                        ctx.setBlock(0, 1, 1, TrollPools.TROLL_BLOCKS[2]);
                        ctx.msg("§a墙上开了一条缝。");
                    });
                    ctx.later(300, () -> {
                        ctx.cageWith(TrollPools.EVIL_BLOCKS[0]);
                        ctx.setBlock(0, 1, 1, TrollPools.EVIL_BLOCKS[2]);
                        ctx.warn("§4缝外面也是黑曜石。§7它合上了。");
                    });
                    ctx.later(460, () -> {
                        ctx.voidPit(2, 16);
                        ctx.stripAll();
                        ctx.effectRandom(1200, 3);
                        ctx.obliterate();
                    });
                    ctx.addRage(29);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_dripstone_rain", "钟乳石雨", TrollTier.APOCALYPSE)
                .desc("洞顶所有钟乳石同时决定下来看看你")
                .weight(30)
                .cooldownSeconds(450)
                .run(ctx -> {
                    // [v3.0.0 残酷化] 本事件等级已上调，补足与等级相称的痛感
                    ctx.meteorShower(2, 12.0);
                    ctx.meteorShower(10, 22.0);
                    ctx.damage("fall", 5.0f);
                    ctx.sound(ctx.pick(TrollPools.IMPACT), 1.0f, 0.6f);
                    ctx.title("§4§l洞顶清仓", "§7全部下来了");
                    ctx.addRage(26);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_amethyst_resonance", "紫水晶共振", TrollTier.NIGHTMARE)
                .desc("紫水晶洞开始共振，你的骨头跟着一起振")
                .weight(28)
                .cooldownSeconds(460)
                .run(ctx -> {
                    ctx.glitch(GlitchIds.SHAKY, 120, 3);
                    ctx.glitch(GlitchIds.NAUSEA_SHADER, 120, 2);
                    ctx.damage("magic", 5.0f);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 260, 1);
                    ctx.msg("§d共振频率：§7正好是你的 §8不适频率§7。");
                    ctx.addRage(27);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_enderman_swarm", "末影人集体出差", TrollTier.NIGHTMARE)
                .desc("矿道里站满了末影人，它们都在等你先眨眼")
                .weight(28)
                .cooldownSeconds(480)
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.effect(ctx.pick(TrollPools.WEIRD_EFFECTS), 200, 1);
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 60, 2);
                    ctx.msg("§5不要看它们的眼睛§7……§8你已经看了§7。");
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_warden_knock", "有人敲了敲矿壁", TrollTier.NIGHTMARE)
                .desc("矿壁另一端传来三下敲击，接着是某种东西站起来的声音")
                .weight(28)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 12);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.randomHorrorSound();
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 60, 3);
                    ctx.title("§4§l咚。咚。咚。", "§7它在找路的出口");
                    ctx.addRage(32);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_ore_singularity", "矿石引力坍缩", TrollTier.OBLIVION)
                .desc("矿石朝你飞过来，然后停在离你一格的地方——永远差那一格")
                .weight(28)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.pullPlayersToMe(6);
                    ctx.scatter(30);
                    ctx.damage(6.0f);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 80, 3);
                    ctx.msg("§8奇点已形成§7，§8成分：§7你的矿、§8你的血。");
                    ctx.later(150, () -> {
                        // 假希望：一块矿到手了
                        ctx.give(ctx.pick(TrollPools.LOOT), 1);
                        ctx.msg("§a一块矿石朝你飞过来了！");
                    });
                    ctx.later(300, () -> {
                        ctx.takeRandomItems(6);
                        ctx.voidPit(2, 16);
                        ctx.meteorShower(6, 18.0);
                        ctx.warn("§4它停在离你一格的地方。§7那一格是留给你看的。");
                    });
                    ctx.later(460, () -> {
                        ctx.stripAll();
                        ctx.obliterate();
                    });
                    ctx.addRage(30);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_cart_deep_pit", "矿车直坠深井", TrollTier.APOCALYPSE)
                .desc("铁轨尽头是一口深井，矿车全速前进，刹车在今天之前就坏了")
                .weight(30)
                .cooldownSeconds(460)
                .run(ctx -> {
                    ctx.voidPit(2, 4);
                    ctx.teleport(ctx.x(), ctx.y() - 30, ctx.z());
                    ctx.damage("fall", 6.0f);
                    ctx.glitch(GlitchIds.BLACKOUT, 50, 3);
                    ctx.msg("§7矿车：§8到站了§7（站台在建造中）。");
                    ctx.addRage(29);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_xp_void", "经验被虚空收走", TrollTier.OBLIVION)
                .desc("经验会短暂地回来一次，只为让你看清它被拿走了多少")
                .weight(28)
                .cooldownSeconds(500)
                .run(ctx -> {
                    ctx.setExperience(0);
                    ctx.glitch(GlitchIds.FAKE_OOM, 60, 2);
                    ctx.soundSelf(ctx.pick(TrollPools.HORROR), 1.0f, 0.7f);
                    ctx.msg("§a经验余额清算完成§7，§8收款方：§7虚空。");
                    ctx.later(140, () -> {
                        // 假希望：退款了
                        ctx.addExperience(200);
                        ctx.msg("§a虚空发起了退款。");
                    });
                    ctx.later(280, () -> {
                        ctx.setExperience(0);
                        ctx.stripAll();
                        ctx.warn("§4退款金额：0。§7附带一份账单，金额是你原来的等级。");
                    });
                    ctx.later(440, () -> {
                        ctx.voidPit(2, 16);
                        ctx.meteorShower(24, 50);
                        ctx.buryAlive();
                        ctx.obliterate();
                    });
                    ctx.addRage(28);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_deepslate_crusher", "深板岩压路机", TrollTier.OBLIVION)
                .desc("压路机会停一下，确认你还在里面，然后继续")
                .weight(28)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.surroundWith(ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.setBlock(0, 2, 0, ctx.pick(TrollPools.NASTY_BLOCKS));
                    ctx.damage(7.0f);
                    ctx.freeze(80);
                    ctx.msg("§8压铸完成§7，§8成品编号：§7你。");
                    ctx.later(150, () -> {
                        // 假希望：机器停了
                        ctx.msg("§a机器的声音停了。");
                        ctx.clearEffects();
                    });
                    ctx.later(300, () -> {
                        ctx.buryAlive();
                        ctx.voidPit(2, 16);
                        ctx.damage(8.0f);
                        ctx.warn("§4停机是为了换模具。§7新的模具是刚才那个。");
                    });
                    ctx.later(460, () -> {
                        ctx.effectRandom(1200, 3);
                        ctx.obliterate();
                    });
                    ctx.addRage(30);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_sculk_spread", "幽匿在蔓延", TrollTier.OBLIVION)
                .desc("幽匿会绕开一次光，然后学会不再绕开")
                .weight(28)
                .cooldownSeconds(480)
                .run(ctx -> {
                    ctx.particleAtPlayer(ctx.pick(TrollPools.SPOOKY_PARTICLES), 60);
                    for (int i = 0; i < 4; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.HOSTILE), 8);
                    }
                    ctx.glitch(GlitchIds.SCREEN_TEAR, 80, 2);
                    ctx.effect(ctx.pick(TrollPools.BAD_EFFECTS), 300, 1);
                    ctx.msg("§3它比你快§7，§8而且它不需要火把§7。");
                    ctx.later(160, () -> {
                        // 假希望：光挡住了它
                        ctx.msg("§a它在一束光前停住了。");
                    });
                    ctx.later(320, () -> {
                        ctx.voidPit(2, 16);
                        ctx.scatterBlocks(ctx.pick(TrollPools.NASTY_BLOCKS), 8);
                        ctx.warn("§4它停那一下是在记位置。§7现在它知道光在哪里了。");
                    });
                    ctx.later(480, () -> {
                        ctx.stripAll();
                        ctx.meteorShower(24, 50);
                        ctx.obliterate();
                    });
                    ctx.addRage(30);
                })
                .build());

        // ================================================================
        // 终章 —— 绝望：没有最底层，只有下一层
        // ================================================================

        TrollEvents.reg(TrollEvent.of("mining_ore_meltdown", "矿脉熔毁", TrollTier.OBLIVION)
                .desc("裂缝会合上一点点，好让你相信这是最后一层")
                .weight(24)
                .cooldownSeconds(1300)
                .run(ctx -> {
                    ctx.lavaPit(4);
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[10], 4);
                    ctx.setFire(20);
                    ctx.damage("lava", 8.0f);
                    ctx.glitch(GlitchIds.FAKE_CORRUPT, 100, 3);
                    ctx.title("§4§l矿脉熔毁", "§8下面还有一层");
                    ctx.later(180, () -> {
                        // 假希望：裂口封上了
                        ctx.setBlock(0, -1, 0, TrollPools.TROLL_BLOCKS[0]);
                        ctx.msg("§a裂口似乎被封住了。");
                    });
                    ctx.later(340, () -> {
                        ctx.voidPit(4, 20);
                        ctx.lavaPit(6);
                        ctx.setFire(20);
                        ctx.warn("§4它不是盖子，是门。§7门后面还有门。");
                    });
                    ctx.later(520, () -> {
                        ctx.meteorShower(24, 50);
                        ctx.stripAll();
                        ctx.obliterate();
                    });
                    ctx.addRage(45);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_the_hole", "你挖出了一个无底洞", TrollTier.OBLIVION)
                .desc("无底洞会短暂地让你看见底，好让你摔得更久一点")
                .weight(22)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    ctx.voidPit(3, 6);
                    ctx.teleport(ctx.x(), -70.0, ctx.z());
                    ctx.glitch(GlitchIds.FAKE_CRASH, 100, 3);
                    ctx.msg("§4§l你挖穿了。§r§8下面是空白，再下面还是空白。");
                    ctx.later(160, () -> {
                        // 假希望：看见底了
                        ctx.msg("§a下面好像有底。");
                        ctx.placeFloor(TrollPools.TROLL_BLOCKS[0], 1);
                    });
                    ctx.later(320, () -> {
                        ctx.voidPit(3, 24);
                        ctx.warn("§4那不是底，是上一层的天花板。§7现在它也不见了。");
                    });
                    ctx.later(480, () -> {
                        ctx.stripAll();
                        ctx.meteorShower(24, 50);
                        ctx.obliterate();
                    });
                    ctx.addRage(50);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_tnt_mountain", "整座矿山的 TNT", TrollTier.OBLIVION)
                .desc("勘测报告会先告诉你『储量有限』，然后补上后半句『对你不限』")
                .weight(22)
                .cooldownSeconds(1500)
                .run(ctx -> {
                    for (int i = 0; i < 14; i++) {
                        ctx.setBlock(ctx.ri(-5, 5), ctx.ri(0, 4), ctx.ri(-5, 5), TrollPools.TROLL_BLOCKS[16]);
                    }
                    ctx.particle(ctx.pick(TrollPools.BOOM_PARTICLES), 200);
                    ctx.glitch(GlitchIds.SHAKE_BURST, 100, 3);
                    ctx.title("§4§l矿山已开采", "§8开采方式：全部一次");
                    ctx.later(160, () -> {
                        // 假希望：勘测有误
                        ctx.msg("§a勘测报告更正：储量有限。");
                    });
                    ctx.later(300, () -> {
                        ctx.boomDestructive(ctx.x(), ctx.y(), ctx.z(), 6.0f);
                        ctx.boomAtPlayer(4.0f);
                        ctx.stripAll();
                        ctx.warn("§4报告还有下半句：§7储量对你来说不限。");
                    });
                    ctx.later(460, () -> {
                        ctx.meteorShower(24, 50);
                        ctx.buryAlive();
                        ctx.obliterate();
                    });
                    ctx.addRage(48);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_warden_arena", "监守者竞技场", TrollTier.OBLIVION)
                .desc("竞技场会先宣布你获胜，然后把出口也判给你——在梦里")
                .weight(22)
                .cooldownSeconds(1600)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        ctx.spawnHostileNear(ctx.pick(TrollPools.BOSS), 10);
                    }
                    ctx.effectAll(ctx.pick(TrollPools.BAD_EFFECTS), 400, 2);
                    ctx.glitch(GlitchIds.HARD_BLACKOUT, 80, 3);
                    ctx.title("§4§l竞技开始", "§7规则：活着（不允许）");
                    ctx.later(200, () -> {
                        // 假希望：宣布胜利
                        ctx.title("§a§l胜 利", "§7出口已为你打开");
                        ctx.clearEffects();
                    });
                    ctx.later(360, () -> {
                        ctx.cageWith(TrollPools.EVIL_BLOCKS[2]);
                        ctx.buryAlive();
                        ctx.stripAll();
                        ctx.warn("§4出口在宣布的那一刻就已经封上了。§7胜利属于观众。");
                    });
                    ctx.later(540, () -> {
                        ctx.meteorShower(24, 50);
                        ctx.obliterate();
                    });
                    ctx.addRage(50);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_bedrock_collapse", "基岩崩落", TrollTier.APOCALYPSE)
                .desc("世界底部的基岩往下塌了一层，你跟着一起走")
                .weight(22)
                .cooldownSeconds(1600)
                .run(ctx -> {
                    ctx.cageWith(TrollPools.EVIL_BLOCKS[0]);
                    ctx.setHealth(4.0f);
                    ctx.setFire(20);
                    ctx.starve();
                    ctx.glitch(GlitchIds.HIDE_HUD, 200, 3);
                    ctx.title("§4§l底层失守", "§8基岩：我也累了");
                    ctx.addRage(52);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_lava_sea", "地下岩浆海", TrollTier.OBLIVION)
                .desc("岩浆海上会浮出一小块岸，等你游过去，它就沉了")
                .weight(22)
                .cooldownSeconds(1600)
                .run(ctx -> {
                    ctx.lavaPit(5);
                    ctx.placeFloor(TrollPools.TROLL_BLOCKS[10], 5);
                    ctx.setFire(20);
                    ctx.damage("lava", 10.0f);
                    ctx.thunderstorm(1200);
                    ctx.title("§4§l岩浆海", "§8本次勘探到此结束");
                    ctx.later(180, () -> {
                        // 假希望：前方有岸
                        ctx.msg("§a前方浮出一小块岸。");
                        ctx.placeFloor(TrollPools.TROLL_BLOCKS[1], 1);
                    });
                    ctx.later(340, () -> {
                        ctx.lavaPit(8);
                        ctx.setFire(20);
                        ctx.placeFloor(TrollPools.TROLL_BLOCKS[10], 2);
                        ctx.warn("§4岸沉了。§7它是礁石，而且它更喜欢你站着的时候。");
                    });
                    ctx.later(520, () -> {
                        ctx.timeOfDay(18000L);
                        ctx.stripAll();
                        ctx.meteorShower(24, 50);
                        ctx.obliterate();
                    });
                    ctx.addRage(52);
                })
                .build());

        TrollEvents.reg(TrollEvent.of("mining_last_ore", "最后一颗钻石", TrollTier.OBLIVION)
                .desc("钻石就在下一格，而你永远差这一格；矿物资产随后全部回收")
                .weight(20)
                .cooldownSeconds(1800)
                .once()
                .run(ctx -> {
                    ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.ORE_TEASE));
                    ctx.give(ctx.pick(TrollPools.LOOT), 1);
                    ctx.fakeBroadcast("§4检测到超额采矿，正在回收全部矿物资产。");
                    ctx.glitch(GlitchIds.FAKE_CRASH, 120, 3);
                    ctx.msg("§e你终于挖到钻石了。");
                    ctx.later(180, () -> {
                        // 假希望：以为能留下
                        ctx.msg("§a矿石已入包。");
                        ctx.clearInventory();
                    });
                    ctx.later(340, () -> {
                        ctx.dropEverything();
                        ctx.placeFloor(TrollPools.TROLL_BLOCKS[1], 3);
                        ctx.fakeJoinLeave(ctx.pick(TrollPools.FAKE_NAMES), false);
                        ctx.warn("§8……玩笑到此为止。§7§8钻石是真的没了。");
                    });
                    ctx.later(520, () -> {
                        ctx.setBlock(0, -1, 0, ctx.pick(TrollPools.ORE_TEASE));
                        ctx.msg("§4下一格又出现了一颗。§7它一直都在下一格。");
                        ctx.stripAll();
                        ctx.buryAlive();
                        ctx.effectRandom(1200, 3);
                        ctx.obliterate();
                    });
                    ctx.addRage(55);
                })
                .build());
    }
}
