package com.zhengren.trollmod.item;

import com.zhengren.trollmod.ZhengrenMod;
import com.zhengren.trollmod.core.TrollConfig;
import com.zhengren.trollmod.mindbreak.Msgs;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * 「精神污染」五件神器：从 mindbreak 移植并合并进整人大师，注册在 {@code zhengren} 命名空间。
 *
 * <p>五件物品全部注册为 {@code zhengren:absurd_stick}、{@code zhengren:curse_book}、
 * {@code zhengren:chaos_pearl}、{@code zhengren:regret_cake}、{@code zhengren:mystery_meat}，
 * 并加入原版物品栏分组（工具组 / 食物组）。</p>
 *
 * <p>物品行为统一受 {@link ZhengrenMod#CONFIG} 的总开关与「精神污染」开关约束；
 * {@link AbsurdStickItem} 直接驱动合并后的统一事件表（{@link com.zhengren.trollmod.core.TrollScheduler}），
 * 因此 {@code /zr} 的配置、等级、黑名单对它同样生效。</p>
 */
public final class ModItems {

    /** 五件神器共用的随机源（原 mindbreak 的 {@code ChaosManager.RANDOM}）。 */
    static final Random RANDOM = new Random();

    public static final Item ABSURD_STICK = register("absurd_stick", AbsurdStickItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.EPIC));

    public static final Item CURSE_BOOK = register("curse_book", CurseBookItem::new,
            new Item.Settings().maxCount(1).rarity(Rarity.RARE));

    public static final Item CHAOS_PEARL = register("chaos_pearl", ChaosPearlItem::new,
            new Item.Settings().maxCount(16).rarity(Rarity.RARE));

    public static final Item REGRET_CAKE = register("regret_cake", RegretCakeItem::new,
            new Item.Settings().food(new FoodComponent.Builder()
                    .hunger(2)
                    .saturationModifier(0.05F)
                    .alwaysEdible()
                    .build()));

    public static final Item MYSTERY_MEAT = register("mystery_meat", MysteryMeatItem::new,
            new Item.Settings().food(new FoodComponent.Builder()
                    .hunger(3)
                    .saturationModifier(0.1F)
                    .alwaysEdible()
                    .build()));

    private ModItems() {
    }

    /** 注册五件神器并把它们塞进原版物品栏分组（工具组：棒 / 书 / 珍珠；食物组：蛋糕 / 肉）。 */
    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(ABSURD_STICK);
            entries.add(CURSE_BOOK);
            entries.add(CHAOS_PEARL);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(REGRET_CAKE);
            entries.add(MYSTERY_MEAT);
        });
    }

    /** 五件神器的完整列表（`/zr gift` 等命令使用）。 */
    public static List<Item> all() {
        return List.of(ABSURD_STICK, CURSE_BOOK, CHAOS_PEARL, REGRET_CAKE, MYSTERY_MEAT);
    }

    /**
     * 1.20.1 注册方式：直接按 {@code Identifier} 注册（原 1.21 代码用的是 {@code RegistryKey} +
     * {@code Identifier.of}）。
     */
    private static Item register(String name, Function<Item.Settings, Item> factory, Item.Settings settings) {
        return Registry.register(Registries.ITEM, new Identifier(ZhengrenMod.MOD_ID, name), factory.apply(settings));
    }

    // ==================================================================
    // 共享工具：五件神器共用（原 mindbreak 的 ChaosUtil 等价物）
    // 只保留 6 个文件，所以这些小工具放在这里而不是新增 util 类。
    // ==================================================================

    /**
     * 「神器是否还能发疯」的统一闸门：总开关 {@link TrollConfig#enabled} 与
     * {@link TrollConfig#mindbreakEnabled} 任一关闭时给出提示并返回 {@code false}。
     */
    static boolean canTroll(ServerPlayerEntity player) {
        TrollConfig cfg = ZhengrenMod.CONFIG;
        if (cfg == null || !cfg.enabled || !cfg.mindbreakEnabled) {
            msg(player, "整活开关现在是关着的，神器暂时睡着了。");
            return false;
        }
        return true;
    }

    /** 给单个玩家发一条带「精神污染」前缀的提示。 */
    static void msg(ServerPlayerEntity player, String text) {
        player.sendMessage(Text.literal(Msgs.frame(text)), false);
    }

    /** 全服播报（带「精神污染」前缀）。 */
    static void broadcast(MinecraftServer server, String text) {
        server.getPlayerManager().broadcast(Text.literal(Msgs.frame(text)), false);
    }

    /** 在玩家位置播放音效。 */
    static void sound(ServerPlayerEntity player, SoundEvent sound, float volume, float pitch) {
        ServerWorld world = player.getServerWorld();
        world.playSound(null, player.getX(), player.getY(), player.getZ(), sound, SoundCategory.PLAYERS, volume, pitch);
    }

    /** 返回 [-range, range) 内的随机偏移（原 {@code ChaosUtil.offset}）。 */
    static double offset(double range) {
        return (RANDOM.nextDouble() * 2.0 - 1.0) * range;
    }
}
