package com.zhengren.trollmod.item;

import com.zhengren.trollmod.ZhengrenMod;
import com.zhengren.trollmod.core.TrollConfig;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollScheduler;
import com.zhengren.trollmod.mindbreak.Msgs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

/**
 * 离谱棒：右键随机整一个活。用完不消耗，因为后悔没有终点。
 *
 * <p>移植要点：不再保留 mindbreak 自己那套随机逻辑，而是从整人大师的
 * <b>统一事件表</b>里抽一个事件（{@link TrollScheduler#randomEventFiltered}）并照常结算
 * （{@link TrollScheduler#fire}），这样 {@code /zr} 的等级上限、黑名单、强度缩放
 * 以及事件自身的冷却 / 条件判断全部生效，还可能抽中整人大师的原生事件。</p>
 */
public class AbsurdStickItem extends Item {
    public AbsurdStickItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (!world.isClient() && user instanceof ServerPlayerEntity serverPlayer) {
            MinecraftServer server = serverPlayer.getServer();
            if (server != null && ModItems.canTroll(serverPlayer)) {
                ModItems.sound(serverPlayer, SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.8F, 1.8F);

                TrollConfig cfg = ZhengrenMod.CONFIG;
                TrollEvent event = TrollScheduler.randomEventFiltered(cfg);
                if (event == null) {
                    // 事件表被配置清空 / 等级上限太低：退回一条嘴臭，别让右键毫无反馈。
                    ModItems.msg(serverPlayer, Msgs.pick(ModItems.RANDOM, Msgs.CURSE));
                } else {
                    TrollScheduler.fire(server, serverPlayer, cfg, TrollScheduler.state(serverPlayer), event);
                }
            }
        }
        return TypedActionResult.success(stack);
    }
}
