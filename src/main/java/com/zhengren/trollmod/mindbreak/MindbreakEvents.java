package com.zhengren.trollmod.mindbreak;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zhengren.trollmod.core.GlitchIds;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollScheduler;
import com.zhengren.trollmod.core.TrollTier;
import com.zhengren.trollmod.item.ModItems;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;

/**
 * 「精神污染」事件注册入口（冻结接口，实现由 events-porter 完成）。
 *
 * <p>契约：把 mindbreak 原有的 43 个整活事件全部转换成整人大师的
 * {@link com.zhengren.trollmod.core.TrollEvent}，注册进
 * {@link com.zhengren.trollmod.core.TrollRegistry}，分类名固定为 {@link #CATEGORY}。</p>
 *
 * <p>注册后这些事件自动获得整人大师的全套能力：权重抽取、冷却、等级过滤、
 * 黑名单、`/zr list`、`/zr fire`、`/zr info`、announce 公告。</p>
 *
 * <p>实现说明：本类共注册 <b>44</b> 个事件——原 mindbreak 的 43 个 id 原样保留，
 * 外加 1 个桥接事件 {@code relic_gift}（赠送整人大师的五件神器之一）。
 * 所有事件都用 {@code ctx.*} 统一 API 实现；延时动作用 {@code ctx.later(...)}
 * （统一延时调度器），不再自建 tick 循环、随机引擎或第二套事件列表。</p>
 */
public final class MindbreakEvents {

    /** 统一分类名，必须与实现在 TrollEvents.reg 时使用的字符串完全一致。 */
    public static final String CATEGORY = com.zhengren.trollmod.core.TrollEvents.CATEGORY_MINDBREAK;

    private MindbreakEvents() {
    }

    /** 注册原 mindbreak 的 43 个「精神污染」事件 + 1 个桥接事件 relic_gift。 */
    public static void register() {

        // ==================== 嘴替类 ====================

        TrollEvents.reg(CATEGORY, TrollEvent.of("chat_spam", "嘴替上身", TrollTier.ANNOYING)
                .desc("随机一句阴阳怪气只发给你，精神污染的入门款")
                .weight(20)
                .cooldownSeconds(30)
                .run(ctx -> {
                    MindbreakUtil.msg(ctx, Msgs.pick(ctx.random(), Msgs.AMBIENT));
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("fake_achievement", "假成就", TrollTier.ANNOYING)
                .desc("融合整人大师独有 fakeAchievement：成就弹窗 + 升级音效 + 全服广播你『精神状态稳定』")
                .weight(8)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.fakeAchievement("精神状态稳定", "在崩溃边缘保持微笑");
                    MindbreakUtil.broadcast(ctx, "🏆 " + ctx.playerName() + " 获得了成就：精神状态稳定！");
                    ctx.sound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.6F);
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("fake_death", "假死亡", TrollTier.BRUTAL)
                .desc("融合整人大师独有 title/fakeDeathScreen：先全服报丧，3 秒后再宣布是玩笑")
                .weight(4)
                .cooldownSeconds(120)
                .run(ctx -> {
                    String name = ctx.playerName();
                    MindbreakUtil.broadcast(ctx, "☠ " + name + " 死了！");
                    ctx.fakeDeathScreen("§7……开个玩笑。");
                    ctx.later(60, () -> MindbreakUtil.broadcast(ctx, "……开个玩笑，" + name + " 还活着。惊不惊喜？"));
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("system_glitch", "系统故障", TrollTier.ANNOYING)
                .desc("融合整人大师独有 glitch + title：画面抖动 + 假系统标题，40 秒后宣布你的价值为 0")
                .weight(6)
                .cooldownSeconds(90)
                .run(ctx -> {
                    MindbreakUtil.broadcast(ctx, "系统正在重新计算你的价值……计算结果：0。");
                    ctx.glitch(GlitchIds.SHAKY, 60, 2);
                    ctx.title("§c系统错误", "§7正在重新计算你的价值……", 5, 40, 10);
                    ctx.later(40, () -> MindbreakUtil.broadcast(ctx, "计算完成。请继续，反正也没人在意。"));
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("villager_gossip", "村民八卦", TrollTier.ANNOYING)
                .desc("全服播报村民正在背后议论你，配一声鄙夷的『呵』")
                .weight(6)
                .cooldownSeconds(60)
                .run(ctx -> {
                    MindbreakUtil.broadcast(ctx, "村民们在背后议论你：『就是他，上次把村子的床全拆了。』");
                    ctx.sound(SoundEvents.ENTITY_VILLAGER_NO, 1.0F, 0.9F);
                })
                .build());

        // ==================== 背包类 ====================

        TrollEvents.reg(CATEGORY, TrollEvent.of("inventory_shuffle", "背包大洗牌", TrollTier.BRUTAL)
                .desc("主背包 36 格全部随机重排，找东西？祝你好运")
                .weight(10)
                .cooldownSeconds(120)
                .run(ctx -> {
                    MindbreakUtil.shuffleMain(ctx.inv(), ctx.random());
                    ctx.sound(SoundEvents.ENTITY_ITEM_PICKUP, 1.0F, 0.5F);
                    MindbreakUtil.msg(ctx, "你的背包被一阵妖风重新整理了。找东西？祝你好运。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("item_steal", "顺手牵羊", TrollTier.BRUTAL)
                .desc("偷走你一件东西，10~20 秒后再还回来，还要说声谢谢")
                .weight(7)
                .cooldownSeconds(150)
                .when(ctx -> !MindbreakUtil.filledSlots(ctx.inv()).isEmpty())
                .run(ctx -> {
                    PlayerInventory inventory = ctx.inv();
                    List<Integer> filled = MindbreakUtil.filledSlots(inventory);
                    if (filled.isEmpty()) {
                        return;
                    }
                    int slot = ctx.pick(filled);
                    ItemStack stolen = inventory.getStack(slot).copy();
                    if (stolen.isEmpty()) {
                        return;
                    }
                    String name = stolen.getName().getString();
                    inventory.setStack(slot, ItemStack.EMPTY);
                    MindbreakUtil.msg(ctx, "一个看不见的贼偷走了你的 " + name + "！");
                    ctx.later(200 + ctx.ri(200), () -> {
                        ctx.give(stolen);
                        MindbreakUtil.msg(ctx, "那个贼把 " + name + " 还回来了，还说了句『谢谢』。");
                    });
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("inventory_curse", "物品诅咒", TrollTier.ANNOYING)
                .desc("随机给你一件东西改名『被诅咒的xx』，深紫色那种")
                .weight(5)
                .cooldownSeconds(90)
                .when(ctx -> !MindbreakUtil.filledSlots(ctx.inv()).isEmpty())
                .run(ctx -> {
                    PlayerInventory inventory = ctx.inv();
                    List<Integer> filled = MindbreakUtil.filledSlots(inventory);
                    if (filled.isEmpty()) {
                        return;
                    }
                    int slot = ctx.pick(filled);
                    ItemStack stack = inventory.getStack(slot);
                    if (stack.isEmpty()) {
                        return;
                    }
                    String oldName = stack.getName().getString();
                    stack.setCustomName(net.minecraft.text.Text.literal("被诅咒的" + oldName)
                            .formatted(Formatting.DARK_PURPLE));
                    MindbreakUtil.msg(ctx, "你的 " + oldName + " 被诅咒了，它现在叫『被诅咒的" + oldName + "』。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("hotbar_swap", "手不听话", TrollTier.ANNOYING)
                .desc("把当前手持格和另一个快捷栏格子对调，你的手自己做的")
                .weight(6)
                .cooldownSeconds(60)
                .run(ctx -> {
                    PlayerInventory inventory = ctx.inv();
                    int selected = inventory.selectedSlot;
                    int other = ctx.ri(9);
                    if (selected == other) {
                        other = (other + 1) % 9;
                    }
                    ItemStack first = inventory.getStack(selected).copy();
                    ItemStack second = inventory.getStack(other).copy();
                    inventory.setStack(selected, second);
                    inventory.setStack(other, first);
                    MindbreakUtil.msg(ctx, "你的手不听话了，它换了个东西拿。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("drop_item", "手滑", TrollTier.ANNOYING)
                .desc("把主手物品直接丢出去，手说它不想拿这个")
                .weight(4)
                .cooldownSeconds(60)
                .when(ctx -> !ctx.held().isEmpty())
                .run(ctx -> {
                    ctx.dropHeld();
                    MindbreakUtil.msg(ctx, "你的手突然松开了。它说它不想拿这个。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("gift", "神秘礼物", TrollTier.PRANK)
                .desc("神秘人塞给你七种垃圾之一，名字都很讲究")
                .weight(9)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ItemStack gift = switch (ctx.ri(7)) {
                        case 0 -> MindbreakUtil.named(Items.ROTTEN_FLESH, "神秘礼物（已过期）", Formatting.DARK_GREEN);
                        case 1 -> MindbreakUtil.named(Items.POISONOUS_POTATO, "限量版土豆", Formatting.GREEN);
                        case 2 -> MindbreakUtil.named(Items.COBBLESTONE, "传家宝", Formatting.GRAY);
                        case 3 -> MindbreakUtil.named(Items.STICK, "神器（自称）", Formatting.GOLD);
                        case 4 -> MindbreakUtil.named(Items.DIRT, "极品泥土", Formatting.DARK_RED);
                        case 5 -> MindbreakUtil.named(Items.BONE, "某人的遗物", Formatting.WHITE);
                        default -> MindbreakUtil.named(Items.PUFFERFISH, "河豚（友情提示：别吃）", Formatting.YELLOW);
                    };
                    ctx.give(gift);
                    MindbreakUtil.msg(ctx, "一个神秘人往你手里塞了样东西，然后消失了。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("fake_diamond", "假钻石", TrollTier.PRANK)
                .desc("恭喜挖到钻石！请千万不要细看")
                .weight(8)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.give(MindbreakUtil.named(Items.DIRT, "钻石（大概）", Formatting.GRAY));
                    MindbreakUtil.msg(ctx, "恭喜你挖到了钻石！请千万不要细看。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("xp_blessing", "经验祝福", TrollTier.PRANK)
                .desc("塞给你 1~30 点经验，知识并不会增加")
                .weight(6)
                .cooldownSeconds(60)
                .run(ctx -> {
                    int xp = 1 + ctx.ri(30);
                    ctx.addExperience(xp);
                    ctx.sound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                    MindbreakUtil.msg(ctx, "你获得了 " + xp + " 点经验，但知识并没有增加。");
                })
                .build());

        // ==================== 身体类 ====================

        TrollEvents.reg(CATEGORY, TrollEvent.of("hunger_drain", "物理空虚", TrollTier.ANNOYING)
                .desc("突然消耗 6 点饱食度，你什么都没干")
                .weight(8)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.exhaust(6.0F);
                    MindbreakUtil.msg(ctx, "你突然感到一阵空虚（物理意义上的）。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("force_feed", "凭空饱腹", TrollTier.PRANK)
                .desc("饥饿值直接拉满，建议不要细想吃了什么")
                .weight(4)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.feed(20);
                    ctx.saturation(5.0F);
                    MindbreakUtil.msg(ctx, "你突然饱了，但你什么都没吃。建议不要细想。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("levitation", "你飘了", TrollTier.ANNOYING)
                .desc("3 秒飘浮。字面意思")
                .weight(8)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.effect(StatusEffects.LEVITATION, 60, 0);
                    MindbreakUtil.msg(ctx, "你飘了。（字面意思）");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("effect_roulette", "药水轮盘", TrollTier.ANNOYING)
                .desc("从加速/缓慢/跳跃/发光/反胃里随机抽一个，10 秒体验卡")
                .weight(7)
                .cooldownSeconds(90)
                .run(ctx -> {
                    net.minecraft.entity.effect.StatusEffect effect = switch (ctx.ri(5)) {
                        case 0 -> StatusEffects.SPEED;
                        case 1 -> StatusEffects.SLOWNESS;
                        case 2 -> StatusEffects.JUMP_BOOST;
                        case 3 -> StatusEffects.GLOWING;
                        default -> StatusEffects.NAUSEA;
                    };
                    ctx.effect(effect, 200, ctx.ri(2));
                    MindbreakUtil.msg(ctx, "你的身体决定自己做主。祝你好运。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("self_hurt", "自我伤害", TrollTier.BRUTAL)
                .desc("用魔法伤害打你 1.5 颗心：你被自己的存在伤害了")
                .weight(5)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.damage("magic", 3.0F);
                    ctx.sound(SoundEvents.ENTITY_PLAYER_HURT, 1.0F, 1.0F);
                    MindbreakUtil.msg(ctx, "你被自己的存在伤害了。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("mob_buff", "怪物兴奋剂", TrollTier.BRUTAL)
                .desc("周围 16 格内所有生物获得速度 II + 力量，原因不明")
                .weight(6)
                .cooldownSeconds(90)
                .when(ctx -> !MindbreakUtil.nearbyMobs(ctx.player()).isEmpty())
                .run(ctx -> {
                    List<LivingEntity> mobs = MindbreakUtil.nearbyMobs(ctx.player());
                    if (mobs.isEmpty()) {
                        return;
                    }
                    for (LivingEntity mob : mobs) {
                        mob.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 300, 1));
                        mob.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 300, 0));
                    }
                    MindbreakUtil.msg(ctx, "附近的生物突然都变得很兴奋，原因不明。");
                })
                .build());

        // ==================== 位移类 ====================

        TrollEvents.reg(CATEGORY, TrollEvent.of("small_teleport", "空间卡顿", TrollTier.ANNOYING)
                .desc("横坐标各偏移 16 格以内，直接扔到地表")
                .weight(8)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ServerWorld world = ctx.world();
                    double tx = ctx.x() + MindbreakUtil.offset(ctx, 16.0);
                    double tz = ctx.z() + MindbreakUtil.offset(ctx, 16.0);
                    int ty = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) tx, (int) tz);
                    ctx.teleport(tx, ty + 1, tz);
                    ctx.sound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.2F);
                    MindbreakUtil.msg(ctx, "空间刚才卡了一下，把你挤到了别处。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("spin", "方向感没收", TrollTier.ANNOYING)
                .desc("视角被强制旋转 90°~270°，附带末影人传送音")
                .weight(5)
                .cooldownSeconds(45)
                .run(ctx -> {
                    float yaw = ctx.player().getYaw() + 90.0F + ctx.random().nextFloat() * 180.0F;
                    MindbreakUtil.teleportRot(ctx, ctx.x(), ctx.y(), ctx.z(), yaw, ctx.player().getPitch());
                    ctx.sound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.5F);
                    MindbreakUtil.msg(ctx, "你的方向感被系统没收了。");
                })
                .build());

        // ==================== 生物类 ====================

        TrollEvents.reg(CATEGORY, TrollEvent.of("chicken_invasion", "鸡你太美", TrollTier.ANNOYING)
                .desc("召唤 5 只顶着『鸡你太美N』名字的鸡，永久驻留")
                .weight(7)
                .cooldownSeconds(90)
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ChickenEntity chicken = ctx.spawn(EntityType.CHICKEN,
                                ctx.x() + MindbreakUtil.offset(ctx, 4.0),
                                ctx.y() + 1.0,
                                ctx.z() + MindbreakUtil.offset(ctx, 4.0));
                        if (chicken == null) {
                            break;
                        }
                        ctx.giveMobCustomName(chicken, "鸡你太美" + (i + 1));
                        chicken.setPersistent();
                    }
                    MindbreakUtil.broadcast(ctx, "有人在 " + ctx.playerName() + " 附近召唤了鸡群，原因不明。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("pig_parade", "猪队友方阵", TrollTier.ANNOYING)
                .desc("3 只『猪队友N号』出现在你身边，它们不打算帮忙")
                .weight(5)
                .cooldownSeconds(90)
                .run(ctx -> {
                    for (int i = 0; i < 3; i++) {
                        PigEntity pig = ctx.spawn(EntityType.PIG,
                                ctx.x() + MindbreakUtil.offset(ctx, 5.0),
                                ctx.y() + 1.0,
                                ctx.z() + MindbreakUtil.offset(ctx, 5.0));
                        if (pig == null) {
                            break;
                        }
                        ctx.giveMobCustomName(pig, "猪队友" + (i + 1) + "号");
                        pig.setPersistent();
                    }
                    MindbreakUtil.broadcast(ctx, "一群猪队友出现在 " + ctx.playerName() + " 身边。它们不打算帮忙。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("mob_taunt", "怪物嘲讽", TrollTier.ANNOYING)
                .desc("随机挑一只附近生物，用它的口吻在全服嘲笑你")
                .weight(9)
                .cooldownSeconds(60)
                .run(ctx -> {
                    List<LivingEntity> mobs = MindbreakUtil.nearbyMobs(ctx.player());
                    if (mobs.isEmpty()) {
                        MindbreakUtil.msg(ctx, "你觉得好像有人在背后说你坏话，但周围一个生物都没有。");
                        return;
                    }
                    LivingEntity mob = ctx.pick(mobs);
                    MindbreakUtil.broadcast(ctx, "【" + mob.getName().getString() + "】对 " + ctx.playerName()
                            + " 说：" + Msgs.pick(ctx.random(), Msgs.TAUNTS));
                    ctx.sound(SoundEvents.ENTITY_VILLAGER_NO, 1.0F, 0.8F);
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("creeper_apology", "苦力怕道歉", TrollTier.PRANK)
                .desc("点火音响起，然后苦力怕说它走错了")
                .weight(5)
                .cooldownSeconds(45)
                .run(ctx -> {
                    ctx.sound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 1.0F);
                    MindbreakUtil.msg(ctx, "苦力怕：对不起，走错了。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("enderman_visitor", "社恐末影人", TrollTier.ANNOYING)
                .desc("刷一只关闭 AI 的『末影人（社恐）』，它不想说话也不想动")
                .weight(4)
                .cooldownSeconds(120)
                .run(ctx -> {
                    EndermanEntity enderman = ctx.spawn(EntityType.ENDERMAN,
                            ctx.x() + MindbreakUtil.offset(ctx, 12.0),
                            ctx.y() + 1.0,
                            ctx.z() + MindbreakUtil.offset(ctx, 12.0));
                    if (enderman == null) {
                        return;
                    }
                    ctx.giveMobCustomName(enderman, "末影人（社恐）");
                    enderman.setAiDisabled(true);
                    enderman.setPersistent();
                    ctx.sound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 0.8F);
                    MindbreakUtil.msg(ctx, "一只社恐末影人出现在附近。它不想说话，也不想动。");
                })
                .build());

        // ==================== 环境类 ====================

        TrollEvents.reg(CATEGORY, TrollEvent.of("tnt_stalker", "TNT 尾随", TrollTier.NIGHTMARE)
                .desc("在你身后 4 格刷一个引信 4 秒的 TNT，建议跑")
                .weight(4)
                .cooldownSeconds(150)
                .run(ctx -> {
                    double angle = ctx.random().nextDouble() * Math.PI * 2.0;
                    double tx = ctx.x() + Math.cos(angle) * 4.0;
                    double tz = ctx.z() + Math.sin(angle) * 4.0;
                    ctx.spawnTnt(tx, ctx.y() + 1.0, tz, 80);
                    ctx.sound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 1.0F);
                    MindbreakUtil.msg(ctx, "你背后……好像有什么东西。建议跑。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("diamond_rain", "钻石雨", TrollTier.PRANK)
                .desc("头顶掉落 5 件『钻石』，其中大约一半是真的")
                .weight(5)
                .cooldownSeconds(60)
                .run(ctx -> {
                    for (int i = 0; i < 5; i++) {
                        ItemStack stack = ctx.random().nextBoolean()
                                ? new ItemStack(Items.DIAMOND)
                                : MindbreakUtil.named(Items.DIRT, "假钻石", Formatting.GRAY);
                        ItemEntity item = new ItemEntity(ctx.world(),
                                ctx.x() + MindbreakUtil.offset(ctx, 3.0),
                                ctx.y() + 6.0,
                                ctx.z() + MindbreakUtil.offset(ctx, 3.0),
                                stack);
                        item.setVelocity(MindbreakUtil.offset(ctx, 0.2), 0.2, MindbreakUtil.offset(ctx, 0.2));
                        ctx.world().spawnEntity(item);
                    }
                    ctx.sound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.7F, 1.5F);
                    MindbreakUtil.msg(ctx, "天上下钻石了！其中大约一半是真的，另一半是土。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("weather_chaos", "天气抽奖", TrollTier.BRUTAL)
                .desc("随机切换成雷暴或普通降雨，具体下什么由服务器决定")
                .weight(4)
                .cooldownSeconds(120)
                .run(ctx -> {
                    boolean thunder = ctx.random().nextBoolean();
                    ctx.setRaining(true, thunder, thunder ? 6000 : 12000);
                    MindbreakUtil.broadcast(ctx, "天气预报：今天有 100% 的概率下点什么，具体下什么由服务器决定。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("time_chaos", "时间裂开", TrollTier.ANNOYING)
                .desc("把世界时间随机拨到一天中的任意时刻")
                .weight(4)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ctx.timeOfDay(ctx.random().nextInt(24000));
                    MindbreakUtil.broadcast(ctx, "时间表示：它裂开了。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("spooky_sound", "不该听的声音", TrollTier.ANNOYING)
                .desc("从 6 种恐怖音效里随机贴脸播放一个")
                .weight(8)
                .cooldownSeconds(45)
                .run(ctx -> {
                    switch (ctx.ri(6)) {
                        case 0 -> ctx.sound(SoundEvents.ENTITY_GHAST_SCREAM, 1.0F, 0.7F);
                        case 1 -> ctx.sound(SoundEvents.ENTITY_WITHER_SPAWN, 0.8F, 0.6F);
                        case 2 -> ctx.sound(SoundEvents.ENTITY_ENDERMAN_SCREAM, 1.0F, 0.9F);
                        case 3 -> ctx.sound(SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 0.8F);
                        case 4 -> ctx.sound(SoundEvents.BLOCK_ANVIL_LAND, 1.0F, 0.6F);
                        default -> ctx.sound(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 0.8F, 1.0F);
                    }
                    MindbreakUtil.msg(ctx, "你听到了不该听到的声音。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("lightning_tease", "差一点", TrollTier.ANNOYING)
                .desc("一声雷响，然后只有一句『差一点』")
                .weight(4)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.sound(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0F, 1.2F);
                    MindbreakUtil.msg(ctx, "差一点。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("shadow_follow", "影子跟随", TrollTier.ANNOYING)
                .desc("末影人凝视音 + 24 个传送门粒子，别回头，真的")
                .weight(4)
                .cooldownSeconds(60)
                .run(ctx -> {
                    ctx.sound(SoundEvents.ENTITY_ENDERMAN_STARE, 0.8F, 0.7F);
                    ctx.particle(ParticleTypes.PORTAL, 24);
                    MindbreakUtil.msg(ctx, "你总觉得有什么东西跟着你……别回头，真的。");
                })
                .build());

        // ==================== 丧心病狂类 ====================

        TrollEvents.reg(CATEGORY, TrollEvent.of("total_chaos", "总混沌", TrollTier.APOCALYPSE)
                .desc("融合整人大师独有 meteorShower + glitch，并直接调用统一调度器再触发两个随机事件")
                .weight(2)
                .cooldownSeconds(300)
                .run(ctx -> {
                    MindbreakUtil.broadcast(ctx, "【系统】检测到你过于快乐，已为你安排一点挫折。");
                    ctx.meteorShower(6, 12.0);
                    ctx.glitch(GlitchIds.FAKE_CRASH, 80, 2);
                    for (int i = 0; i < 2; i++) {
                        TrollEvent extra = TrollScheduler.pick(ctx.server(), ctx.player(), ctx.cfg(),
                                ctx.state, ctx.gameTicks());
                        if (extra != null && !"total_chaos".equals(extra.id())) {
                            TrollScheduler.fire(ctx.server(), ctx.player(), ctx.cfg(), ctx.state, extra);
                        }
                    }
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("chat_takeover", "聊天接管", TrollTier.BRUTAL)
                .desc("用你的名字在 0/1.5/3 秒后各发一句『我没说话』，最后承认是本人")
                .weight(3)
                .cooldownSeconds(180)
                .run(ctx -> {
                    String name = ctx.playerName();
                    ctx.later(0, () -> MindbreakUtil.broadcast(ctx, "<" + name + "> 我没说话。"));
                    ctx.later(30, () -> MindbreakUtil.broadcast(ctx, "<" + name + "> 我真的没说话。"));
                    ctx.later(60, () -> MindbreakUtil.broadcast(ctx, "<" + name + "> 好吧是我说的。"));
                })
                .build());

        // ==================== 新增：更不做人的 ====================

        TrollEvents.reg(CATEGORY, TrollEvent.of("neck_crack", "脖子自己转", TrollTier.ANNOYING)
                .desc("俯仰角随机甩到 -90°~90°，偏航再偏 30°，配骨骼错位音效")
                .weight(6)
                .cooldownSeconds(45)
                .run(ctx -> {
                    float pitch = -90.0F + ctx.random().nextFloat() * 180.0F;
                    float yaw = (float) (ctx.player().getYaw() + MindbreakUtil.offset(ctx, 30.0));
                    MindbreakUtil.teleportRot(ctx, ctx.x(), ctx.y(), ctx.z(), yaw, pitch);
                    ctx.sound(SoundEvents.ENTITY_SKELETON_HURT, 0.8F, 1.4F);
                    MindbreakUtil.msg(ctx, "你的脖子刚刚自己转了一下。它现在很满意。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("anvil_prank", "小心头顶", TrollTier.BRUTAL)
                .desc("融合整人大师独有 spawnFallingBlock：头顶 6 格掉铁砧，且不破坏地形")
                .weight(4)
                .cooldownSeconds(120)
                .run(ctx -> {
                    ctx.spawnFallingBlock(Blocks.ANVIL);
                    ctx.sound(SoundEvents.BLOCK_ANVIL_LAND, 1.0F, 0.8F);
                    MindbreakUtil.msg(ctx, "小心头顶。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("fake_ore", "假矿脉", TrollTier.ANNOYING)
                .desc("把附近 4 块石头/深板岩/圆石变成钻石矿，3 秒后悄悄还原")
                .weight(6)
                .cooldownSeconds(90)
                .run(ctx -> {
                    ServerWorld world = ctx.world();
                    Map<BlockPos, BlockState> replaced = new LinkedHashMap<>();
                    BlockPos center = ctx.pos();
                    for (int i = 0; i < 4; i++) {
                        BlockPos p = center.add(ctx.ri(-4, 4), ctx.ri(-1, 1), ctx.ri(-4, 4));
                        BlockState state = world.getBlockState(p);
                        if (state.isOf(Blocks.STONE) || state.isOf(Blocks.DEEPSLATE) || state.isOf(Blocks.COBBLESTONE)) {
                            replaced.put(p, state);
                        }
                    }
                    if (replaced.isEmpty()) {
                        return;
                    }
                    for (BlockPos p : replaced.keySet()) {
                        world.setBlockState(p, ctx.random().nextBoolean()
                                ? Blocks.DIAMOND_ORE.getDefaultState()
                                : Blocks.DEEPSLATE_DIAMOND_ORE.getDefaultState());
                    }
                    ctx.sound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 1.0F, 0.6F);
                    MindbreakUtil.msg(ctx, "你发现了钻石矿！……可惜它们只存在 3 秒。");
                    ctx.later(60, () -> {
                        for (Map.Entry<BlockPos, BlockState> entry : replaced.entrySet()) {
                            world.setBlockState(entry.getKey(), entry.getValue());
                        }
                    });
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("ice_floor", "脚下结冰", TrollTier.ANNOYING)
                .desc("把脚下的方块换成冰，滑倒纯属你自己的问题")
                .weight(5)
                .cooldownSeconds(60)
                .when(ctx -> !ctx.world().getBlockState(ctx.pos().down()).isAir())
                .run(ctx -> {
                    ctx.setBlockAt(ctx.pos().down(), Blocks.ICE);
                    ctx.sound(SoundEvents.BLOCK_GLASS_PLACE, 1.0F, 1.6F);
                    MindbreakUtil.msg(ctx, "你脚下的地面突然变得很滑。这和你没关系，别多想。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("fake_player_chat", "假玩家发言", TrollTier.ANNOYING)
                .desc("Notch、Herobrine、村民老王或一只鸡，替你说点刺激的")
                .weight(6)
                .cooldownSeconds(60)
                .run(ctx -> {
                    String[] names = {"Notch", "Herobrine", "Steve", "Alex", "村民老王", "一只鸡"};
                    MindbreakUtil.broadcast(ctx, "<" + ctx.pick(names) + "> "
                            + Msgs.pick(ctx.random(), Msgs.FAKE_CHAT));
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("fake_friend_join", "假好友上线", TrollTier.PRANK)
                .desc("随机一个假好友加入游戏，5 秒后再退出")
                .weight(4)
                .cooldownSeconds(90)
                .run(ctx -> {
                    String[] names = {"Steve", "Alex", "Herobrine", "Notch"};
                    String name = ctx.pick(names);
                    MindbreakUtil.broadcast(ctx, name + " 加入了游戏");
                    ctx.later(100, () -> MindbreakUtil.broadcast(ctx, name + " 退出了游戏"));
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("swap_players", "位置互换", TrollTier.BRUTAL)
                .desc("和另一名同世界玩家交换位置，没有原因，也不需要原因")
                .weight(4)
                .cooldownSeconds(120)
                .when(ctx -> ctx.hasOthers())
                .run(ctx -> {
                    ServerPlayerEntity other = ctx.other();
                    if (other == null || other.getServerWorld() != ctx.world()) {
                        MindbreakUtil.broadcast(ctx, "两位玩家的位置本该被交换，但他们不在同一个世界。算了。");
                        return;
                    }
                    ctx.swapWith(other);
                    MindbreakUtil.broadcast(ctx, "两位玩家的位置刚刚被交换了，没有原因，也不需要原因。");
                })
                .build());

        TrollEvents.reg(CATEGORY, TrollEvent.of("totem_tease", "图腾不想上班", TrollTier.PRANK)
                .desc("翻遍你 41 个格子找不死图腾，然后告诉你它今天请假了")
                .weight(5)
                .cooldownSeconds(90)
                .run(ctx -> {
                    PlayerInventory inventory = ctx.inv();
                    boolean hasTotem = false;
                    for (int slot = 0; slot < inventory.size(); slot++) {
                        if (inventory.getStack(slot).isOf(Items.TOTEM_OF_UNDYING)) {
                            hasTotem = true;
                            break;
                        }
                    }
                    if (hasTotem) {
                        MindbreakUtil.msg(ctx, "你的不死图腾刚刚抖了一下。它说它今天不想上班。");
                    } else {
                        MindbreakUtil.msg(ctx, "你没有不死图腾。现在你知道为什么了。");
                    }
                })
                .build());

        // ==================== 桥接事件：整人大师神器 ====================

        TrollEvents.reg(CATEGORY, TrollEvent.of("relic_gift", "神器馈赠", TrollTier.PRANK)
                .desc("桥接事件：融合整人大师物品系统，随机赠送五件神器之一（ABSURD_STICK / CURSE_BOOK / "
                        + "CHAOS_PEARL / REGRET_CAKE / MYSTERY_MEAT）")
                .weight(6)
                .cooldownSeconds(300)
                .run(ctx -> {
                    Item[] relics = {
                            ModItems.ABSURD_STICK,
                            ModItems.CURSE_BOOK,
                            ModItems.CHAOS_PEARL,
                            ModItems.REGRET_CAKE,
                            ModItems.MYSTERY_MEAT
                    };
                    ItemStack relic = new ItemStack(ctx.pick(relics));
                    ctx.give(relic);
                    ctx.sound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.8F, 1.2F);
                    ctx.particle(ParticleTypes.END_ROD, 30);
                    MindbreakUtil.msg(ctx, "你捡到了一件不属于这个世界的神器：" + relic.getName().getString());
                })
                .build());
    }

    /**
     * 当前已注册的「精神污染」事件 id 列表（按注册顺序）。
     * 默认实现从事件注册表按分类反查，实现方无需重写。
     */
    public static List<String> ids() {
        List<String> out = new ArrayList<>();
        for (com.zhengren.trollmod.core.TrollEvent e
                : com.zhengren.trollmod.core.TrollRegistry.byCategory()
                        .getOrDefault(CATEGORY, List.of())) {
            out.add(e.id());
        }
        return out;
    }

    /** 当前已注册的「精神污染」事件数量。 */
    public static int size() {
        return ids().size();
    }
}
