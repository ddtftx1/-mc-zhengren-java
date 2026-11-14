package com.zhengren.trollmod.item;

import com.zhengren.trollmod.mindbreak.Msgs;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * 嘴臭之书：对全世界输出一句不带脏字但很伤人的话。
 *
 * <p>移植要点：文案改用 {@link Msgs}，广播走合并后的聊天通道
 * （带「精神污染」前缀），难受效果保留原样。</p>
 */
public class CurseBookItem extends Item {
    public CurseBookItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient() && user instanceof ServerPlayerEntity serverPlayer) {
            MinecraftServer server = serverPlayer.getServer();
            if (server != null && ModItems.canTroll(serverPlayer)) {
                // 原实现取玩家列表里最后一个「别人」当靶子；没有别人就嘴臭全世界。
                ServerPlayerEntity target = null;
                for (ServerPlayerEntity other : server.getPlayerManager().getPlayerList()) {
                    if (other != serverPlayer) {
                        target = other;
                    }
                }
                String targetName = target == null ? "全世界" : target.getName().getString();
                ModItems.broadcast(server, serverPlayer.getName().getString() + " 对 " + targetName + " 说："
                        + Msgs.pick(ModItems.RANDOM, Msgs.CURSE));
                serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0));
            }
        }
        return TypedActionResult.success(stack);
    }
}
