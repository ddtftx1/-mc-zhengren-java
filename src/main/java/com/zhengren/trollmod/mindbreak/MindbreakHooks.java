package com.zhengren.trollmod.mindbreak;

import com.zhengren.trollmod.ZhengrenMod;
import com.zhengren.trollmod.core.TrollConfig;
import com.zhengren.trollmod.core.TrollContext;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollScheduler;
import com.zhengren.trollmod.core.TrollTier;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * 「精神污染」交互回调（冻结接口，实现由 hooks-porter 完成）。
 *
 * <p>契约：把 mindbreak 原有的 8 个玩家交互回调（挖方块被拒绝、方块变身、
 * 床拒绝你、打怪被背刺、聊天被曲解、死亡遗言、加入/重生问候）注册进 Fabric 事件总线。</p>
 *
 * <p>所有回调必须受 {@link com.zhengren.trollmod.core.TrollConfig#enabled} 与
 * {@code mindbreakEnabled} / {@code mindbreakHooks} 开关约束，并尊重
 * {@code affectCreative}（创造模式玩家跳过）。</p>
 *
 * <p><b>注意</b>：与原版 mindbreak（1.21.1）不同，本模组编译目标是 1.20.1 的
 * fabric-api 0.92.12，其中的 {@code ServerPlayerEvents} 并<b>没有</b> {@code JOIN}
 * 事件，因此「加入问候」改用等价的 {@link ServerPlayConnectionEvents#JOIN}
 * （networking API，回调里通过 {@code handler.player} 取玩家）。语义一致。</p>
 *
 * <p>本类<b>不</b>注册任何 tick 轮询：随机调度统一归
 * {@link TrollScheduler}，这里只做「玩家干了什么」的即时反应。</p>
 */
public final class MindbreakHooks {

    private static final Random RANDOM = new Random();

    /**
     * 仅用于构造 {@link TrollContext} 的工具事件（不注册进统一事件表，不参与随机调度）。
     * 这样回调可以复用整人大师的统一工具方法（msg / broadcast / sound / setBlockAt / damage），
     * 而不是另造一套。
     */
    private static final TrollEvent HOOK_CONTEXT = TrollEvent
            .of("mindbreak:hook", "精神污染交互回调", TrollTier.ANNOYING)
            .desc("MindbreakHooks 内部工具上下文，不参与随机调度")
            .build();

    private MindbreakHooks() {
    }

    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register(MindbreakHooks::onBeforeBreak);
        PlayerBlockBreakEvents.AFTER.register(MindbreakHooks::onAfterBreak);
        UseBlockCallback.EVENT.register(MindbreakHooks::onUseBlock);
        AttackEntityCallback.EVENT.register(MindbreakHooks::onAttackEntity);
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(MindbreakHooks::onChatMessage);
        ServerLivingEntityEvents.AFTER_DEATH.register(MindbreakHooks::onDeath);
        // 1.20.1 无 ServerPlayerEvents.JOIN，等价物是 ServerPlayConnectionEvents.JOIN
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> onJoin(handler.player));
        ServerPlayerEvents.AFTER_RESPAWN.register(MindbreakHooks::onRespawn);
    }

    // ==================================================================
    // 统一开关：总开关 + 精神污染参与 + 交互回调 + 创造模式语义
    // ==================================================================

    private static TrollConfig cfg() {
        return ZhengrenMod.CONFIG;
    }

    /** 所有交互回调共用的开关判断。{@code affectCreative == false} 时跳过创造模式玩家。 */
    private static boolean hooksOn(PlayerEntity player) {
        TrollConfig cfg = cfg();
        if (cfg == null || !cfg.enabled || !cfg.mindbreakEnabled || !cfg.mindbreakHooks) {
            return false;
        }
        return cfg.affectCreative || !player.isCreative();
    }

    /** 复用整人大师工具的统一上下文（不新建任何状态）。 */
    private static TrollContext ctx(ServerPlayerEntity player) {
        return new TrollContext(player.getServer(), player, cfg(),
                TrollScheduler.state(player), RANDOM, HOOK_CONTEXT);
    }

    // ==================================================================
    // 1. 挖方块有概率被方块拒绝
    // ==================================================================

    private static boolean onBeforeBreak(World world, PlayerEntity player, BlockPos pos, BlockState state,
                                         @Nullable BlockEntity blockEntity) {
        if (world.isClient() || !(player instanceof ServerPlayerEntity serverPlayer) || !hooksOn(player)) {
            return true;
        }
        if (RANDOM.nextInt(100) < 5) {
            TrollContext ctx = ctx(serverPlayer);
            ctx.sound(SoundEvents.ENTITY_VILLAGER_NO, 1.0F, 0.8F);
            ctx.msg(Msgs.frame("这个" + state.getBlock().getName().getString() + "太紧张了，趁你不注意逃走了。"));
            return false;
        }
        return true;
    }

    // ==================================================================
    // 2. 挖掉的方块有概率「长」成一个奇怪的东西
    // ==================================================================

    private static void onAfterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state,
                                     @Nullable BlockEntity blockEntity) {
        if (world.isClient() || !(player instanceof ServerPlayerEntity serverPlayer) || !hooksOn(player)) {
            return;
        }
        if (RANDOM.nextInt(100) < 4) {
            Block[] weird = {Blocks.PUMPKIN, Blocks.MELON, Blocks.CAKE, Blocks.JUKEBOX, Blocks.SPONGE,
                    Blocks.SLIME_BLOCK};
            Block block = weird[RANDOM.nextInt(weird.length)];
            TrollContext ctx = ctx(serverPlayer);
            ctx.setBlockAt(pos, block);
            ctx.msg(Msgs.frame("你挖掉的方块又长回来了，但它现在是一个" + block.getName().getString() + "。"));
        }
    }

    // ==================================================================
    // 3. 右键：床拒绝你，方块半路变身
    // ==================================================================

    private static ActionResult onUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        if (world.isClient() || !(player instanceof ServerPlayerEntity serverPlayer) || !hooksOn(player)) {
            return ActionResult.PASS;
        }
        if (hand == Hand.MAIN_HAND && world.getBlockState(hitResult.getBlockPos()).isIn(BlockTags.BEDS)
                && RANDOM.nextInt(100) < 15) {
            serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 50, 1));
            TrollContext ctx = ctx(serverPlayer);
            ctx.sound(SoundEvents.ENTITY_VILLAGER_NO, 1.0F, 0.7F);
            ctx.msg(Msgs.frame("床：不，你不想睡。"));
            return ActionResult.FAIL;
        }

        ItemStack held = player.getStackInHand(hand);
        if (held.getItem() instanceof BlockItem && RANDOM.nextInt(100) < 8) {
            Item replacement = pickReplacementBlock();
            player.setStackInHand(hand, new ItemStack(replacement));
            TrollContext ctx = ctx(serverPlayer);
            ctx.sound(SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, 1.0F, 0.5F);
            ctx.msg(Msgs.frame("你的方块在最后一刻变成了" + replacement.getName().getString() + "，它说它更喜欢这样。"));
        }
        return ActionResult.PASS;
    }

    private static Item pickReplacementBlock() {
        Item[] pool = {Items.MELON, Items.CAKE, Items.PUMPKIN, Items.SLIME_BLOCK, Items.SPONGE,
                Items.JUKEBOX, Items.DIAMOND_BLOCK, Items.TNT};
        return pool[RANDOM.nextInt(pool.length)];
    }

    // ==================================================================
    // 4. 打怪有概率被自己的武器背刺，怪物有概率嘴臭
    // ==================================================================

    private static ActionResult onAttackEntity(PlayerEntity player, World world, Hand hand, Entity entity,
                                               @Nullable EntityHitResult hitResult) {
        if (world.isClient() || !(player instanceof ServerPlayerEntity serverPlayer) || !hooksOn(player)) {
            return ActionResult.PASS;
        }
        TrollContext ctx = ctx(serverPlayer);
        if (RANDOM.nextInt(100) < 4) {
            ctx.damage("magic", 2.0F);
            ctx.sound(SoundEvents.ENTITY_PLAYER_HURT, 1.0F, 1.0F);
            ctx.msg(Msgs.frame("你的武器突然对你产生了兴趣。"));
        } else if (RANDOM.nextInt(100) < 7) {
            ctx.broadcast(Msgs.frame("【" + entity.getName().getString() + "】" + Msgs.pick(RANDOM, Msgs.TAUNTS)));
        } else if (entity instanceof LivingEntity living && RANDOM.nextInt(100) < 5) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 200, 2));
            ctx.msg(Msgs.frame("你把它打兴奋了。它现在的速度是 3。"));
        }
        return ActionResult.PASS;
    }

    // ==================================================================
    // 5. 聊天被曲解（额外受 chatMangle 约束）
    // ==================================================================

    private static boolean onChatMessage(SignedMessage message, ServerPlayerEntity sender,
                                         MessageType.Parameters params) {
        TrollConfig cfg = cfg();
        if (cfg == null || !cfg.enabled || !cfg.mindbreakEnabled || !cfg.mindbreakHooks || !cfg.chatMangle) {
            return true;
        }
        if (!cfg.affectCreative && sender.isCreative()) {
            return true;
        }
        if (RANDOM.nextInt(100) >= 18) {
            return true;
        }
        String mangled = Msgs.mangle(message.getContent().getString());
        sender.getServer().getPlayerManager().broadcast(
                Text.literal("<" + sender.getName().getString() + "> " + mangled), false);
        return false;
    }

    // ==================================================================
    // 6. 死了之后有概率留下遗言（全村通报）
    // ==================================================================

    private static void onDeath(LivingEntity entity, DamageSource source) {
        if (!(entity instanceof ServerPlayerEntity serverPlayer) || !hooksOn(serverPlayer)) {
            return;
        }
        if (RANDOM.nextInt(100) < 60) {
            ctx(serverPlayer).broadcast(Msgs.frame("【遗言】" + serverPlayer.getName().getString() + " "
                    + Msgs.pick(RANDOM, Msgs.DEATH)));
        }
    }

    // ==================================================================
    // 7. 加入问候（ServerPlayConnectionEvents.JOIN 代替不存在的 ServerPlayerEvents.JOIN）
    // ==================================================================

    private static void onJoin(ServerPlayerEntity player) {
        if (!hooksOn(player)) {
            return;
        }
        if (RANDOM.nextInt(100) < 70) {
            ctx(player).msg(Msgs.frame("欢迎回来，" + player.getName().getString()
                    + "。本模组承诺：不会让你无聊，只会让你崩溃。"));
        }
    }

    // ==================================================================
    // 8. 重生问候
    // ==================================================================

    private static void onRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        if (!hooksOn(newPlayer)) {
            return;
        }
        ctx(newPlayer).msg(Msgs.frame("你回来了？它们记得你。"));
    }
}
