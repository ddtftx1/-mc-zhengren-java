package com.zhengren.trollmod.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * 后悔蛋糕：吃完会后悔，但吃之前你不会相信。
 *
 * <p>移植要点：沿用原实现的「右键即生效 + 再交给 {@code super.use} 走进食流程」，
 * 效果与文案保持不变；仅补上统一开关闸门。</p>
 */
public class RegretCakeItem extends Item {
    public RegretCakeItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient() && user instanceof ServerPlayerEntity serverPlayer && ModItems.canTroll(serverPlayer)) {
            serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
            serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0));
            ModItems.msg(serverPlayer, "你尝到了后悔的味道。");
        }
        return super.use(world, user, hand);
    }
}
