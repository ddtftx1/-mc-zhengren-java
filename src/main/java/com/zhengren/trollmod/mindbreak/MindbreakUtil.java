package com.zhengren.trollmod.mindbreak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.zhengren.trollmod.core.TrollContext;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * 「精神污染」事件的工具集合，从 mindbreak 的 {@code ChaosUtil} 移植而来。
 *
 * <p>只保留统一事件 API（{@link TrollContext}）没有覆盖的部分：文案前缀、随机偏移、
 * 主背包槽位、附近生物、命名物品。调度、随机、音效、粒子、传送、给物品等一律走
 * {@code ctx.*}，因此本类不持有任何 tick 循环、随机引擎或第二套事件列表。</p>
 */
public final class MindbreakUtil {

    /** 主背包 36 格（0..35，不含护甲与副手），与原 mindbreak 的遍历范围一致。 */
    public static final int MAIN_SLOTS = 36;

    private MindbreakUtil() {
    }

    /** 【精神污染】前缀 + 只发给该玩家。 */
    public static void msg(TrollContext ctx, String text) {
        ctx.msg(Msgs.frame(text));
    }

    /** 【精神污染】前缀 + 全服广播。 */
    public static void broadcast(TrollContext ctx, String text) {
        ctx.broadcast(Msgs.frame(text));
    }

    /** [-range, range] 的随机偏移，对应原 {@code ChaosUtil.offset}。 */
    public static double offset(TrollContext ctx, double range) {
        return (ctx.random().nextDouble() * 2.0 - 1.0) * range;
    }

    /** 带颜色与自定义名的物品（1.20.1 用 {@code setCustomName}）。 */
    public static ItemStack named(Item item, String name, Formatting formatting) {
        ItemStack stack = new ItemStack(item);
        stack.setCustomName(Text.literal(name).formatted(formatting));
        return stack;
    }

    /** 主背包里所有非空槽位，对应原 {@code ChaosEvents.filledSlots}。 */
    public static List<Integer> filledSlots(PlayerInventory inventory) {
        List<Integer> slots = new ArrayList<>();
        for (int slot = 0; slot < MAIN_SLOTS; slot++) {
            if (!inventory.getStack(slot).isEmpty()) {
                slots.add(slot);
            }
        }
        return slots;
    }

    /** 洗牌主背包 36 格（不动护甲与副手），对应原 {@code inventory_shuffle}。 */
    public static void shuffleMain(PlayerInventory inventory, Random random) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int slot = 0; slot < MAIN_SLOTS; slot++) {
            stacks.add(inventory.getStack(slot).copy());
        }
        Collections.shuffle(stacks, random);
        for (int slot = 0; slot < MAIN_SLOTS; slot++) {
            inventory.setStack(slot, stacks.get(slot));
        }
    }

    /** 周围 16 格内所有活着的非玩家生物，对应原 {@code ChaosEvents.nearbyMobs}。 */
    public static List<LivingEntity> nearbyMobs(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        return world.getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(16.0),
                entity -> !(entity instanceof PlayerEntity) && entity.isAlive());
    }

    /**
     * 同时设置朝向与位置，对应原 {@code ChaosUtil.teleportRot}。
     * 先转向再传送，让客户端真正收到新的 yaw/pitch。
     */
    public static void teleportRot(TrollContext ctx, double x, double y, double z, float yaw, float pitch) {
        ctx.setRotation(yaw, pitch);
        ctx.teleport(x, y, z);
    }
}
