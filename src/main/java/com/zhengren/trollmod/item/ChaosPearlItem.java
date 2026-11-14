package com.zhengren.trollmod.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

/**
 * 混乱珍珠：随机把你扔到 100 格开外的某个地方。方向感不提供。
 *
 * <p>移植要点：1.20.1 的 {@code serverPlayer.teleport(ServerWorld, x, y, z, yaw, pitch)}
 * 直接可用；落点高度取 {@code MOTION_BLOCKING_NO_LEAVES} 地表。</p>
 */
public class ChaosPearlItem extends Item {
    public ChaosPearlItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient() && user instanceof ServerPlayerEntity serverPlayer) {
            if (ModItems.canTroll(serverPlayer)) {
                ServerWorld serverWorld = serverPlayer.getServerWorld();
                double startX = serverPlayer.getX();
                double startZ = serverPlayer.getZ();
                double x = startX + ModItems.offset(96.0);
                double z = startZ + ModItems.offset(96.0);
                int y = serverWorld.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (int) x, (int) z);
                serverPlayer.teleport(serverWorld, x, y + 1, z, serverPlayer.getYaw(), serverPlayer.getPitch());
                ModItems.sound(serverPlayer, SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.2F);

                int distance = (int) Math.sqrt((x - startX) * (x - startX) + (z - startZ) * (z - startZ));
                ModItems.msg(serverPlayer, "你被扔到了大约 " + distance + " 格开外。方向感？不需要。");
                stack.decrement(1);
            }
        }
        return TypedActionResult.success(stack);
    }
}
