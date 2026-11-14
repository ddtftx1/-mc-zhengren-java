package com.zhengren.trollmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.zhengren.trollmod.ZhengrenMod;
import com.zhengren.trollmod.core.TrollConfig;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollRegistry;
import com.zhengren.trollmod.core.TrollScheduler;
import com.zhengren.trollmod.item.ModItems;
import com.zhengren.trollmod.mindbreak.MindbreakEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

/**
 * 「精神污染」命令（冻结接口，实现由 command-porter 完成）。
 *
 * <p>契约：注册 {@code /mindbreak} 及其别名 {@code /mb}，子命令保持与原 mindbreak 模组兼容：
 * {@code toggle / intensity &lt;1-3&gt; / trigger &lt;id&gt; / list / gift / chaos / stats / help}。
 * 这些子命令内部必须委托给整人大师的统一系统（{@link com.zhengren.trollmod.core.TrollRegistry}、
 * {@link com.zhengren.trollmod.core.TrollScheduler}、{@link com.zhengren.trollmod.core.TrollConfig}），
 * 不得再维护第二套状态。</p>
 *
 * <p>因此 {@code toggle} 改的是 {@link TrollConfig#mindbreakEnabled}（与 {@code /zr on|off}
 * 共用同一份 {@code ZhengrenMod.CONFIG}），{@code trigger} 走 {@link TrollScheduler#fireById}，
 * 事件列表来自 {@link MindbreakEvents#ids()}。</p>
 */
public final class MindbreakCommand {

    private MindbreakCommand() {
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) -> {
            build(dispatcher, "mindbreak");
            build(dispatcher, "mb");
        });
    }

    private static void build(CommandDispatcher<ServerCommandSource> dispatcher, String name) {
        dispatcher.register(CommandManager.literal(name)
                .executes(ctx -> help(ctx.getSource()))
                .then(CommandManager.literal("help").executes(ctx -> help(ctx.getSource())))
                .then(CommandManager.literal("toggle").executes(ctx -> {
                    TrollConfig c = cfg();
                    // 只切换精神污染参与随机调度的开关，不动 CONFIG.enabled 总开关
                    c.mindbreakEnabled = !c.mindbreakEnabled;
                    c.save();
                    boolean now = c.mindbreakEnabled;
                    feedback(ctx.getSource(), "【精神污染】整活已" + (now ? "开启" : "关闭") + "。"
                            + (now ? "祝你好运。" : "你以为这就结束了？")
                            + " §7(共用开关：/zr on|off 控制总开关)");
                    return 1;
                }))
                .then(CommandManager.literal("intensity")
                        .then(CommandManager.argument("level", IntegerArgumentType.integer(1, 3))
                                .executes(ctx -> {
                                    int level = IntegerArgumentType.getInteger(ctx, "level");
                                    TrollConfig c = cfg();
                                    c.mindbreakIntensity = level;
                                    c.reindex();
                                    c.save();
                                    feedback(ctx.getSource(), "【精神污染】整活强度已设为 " + level + "："
                                            + intensityComment(level));
                                    return 1;
                                })))
                .then(CommandManager.literal("trigger")
                        .then(CommandManager.argument("event", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    MindbreakEvents.ids().forEach(builder::suggest);
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {
                                    String id = StringArgumentType.getString(ctx, "event");
                                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                                    if (player == null) {
                                        error(ctx.getSource(), "这个命令必须由玩家执行。");
                                        return 0;
                                    }
                                    boolean happened = TrollScheduler.fireById(
                                            ctx.getSource().getServer(), player, id);
                                    if (happened) {
                                        feedback(ctx.getSource(), "【精神污染】已触发事件：" + id);
                                        return 1;
                                    }
                                    error(ctx.getSource(), "事件不存在，或者它这次不想搞你：" + id);
                                    return 0;
                                })))
                .then(CommandManager.literal("list").executes(ctx -> {
                    List<String> ids = MindbreakEvents.ids();
                    feedback(ctx.getSource(), "【精神污染】共 " + ids.size() + " 个事件：" + String.join(", ", ids));
                    return 1;
                }))
                .then(CommandManager.literal("gift").executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                    if (player == null) {
                        error(ctx.getSource(), "这个命令必须由玩家执行。");
                        return 0;
                    }
                    List<Item> items = ModItems.all();
                    for (Item item : items) {
                        ItemStack stack = new ItemStack(item);
                        if (!player.giveItemStack(stack)) {
                            player.dropItem(stack, false);
                        }
                    }
                    feedback(ctx.getSource(), "【精神污染】" + items.size()
                            + " 件神器已发放。使用后果自负，其实作者也不负责。");
                    return 1;
                }))
                .then(CommandManager.literal("chaos").executes(ctx -> {
                    ServerPlayerEntity player = ctx.getSource().getPlayer();
                    if (player == null) {
                        error(ctx.getSource(), "这个命令必须由玩家执行。");
                        return 0;
                    }
                    TrollConfig c = cfg();
                    feedback(ctx.getSource(), "【精神污染】五连发，请站稳。");
                    int fired = 0;
                    for (int i = 0; i < 5; i++) {
                        TrollEvent event = TrollScheduler.randomEventFiltered(c);
                        if (event == null) {
                            break;
                        }
                        if (TrollScheduler.fire(ctx.getSource().getServer(), player, c,
                                TrollScheduler.state(player), event)) {
                            fired++;
                        }
                    }
                    if (fired == 0) {
                        error(ctx.getSource(), "没有可用事件（检查 /zr on 与等级设置）。");
                        return 0;
                    }
                    return 1;
                }))
                .then(CommandManager.literal("stats").executes(ctx -> {
                    TrollConfig c = cfg();
                    feedback(ctx.getSource(), "【精神污染】状态：" + (c.mindbreakEnabled ? "开着" : "关着")
                            + "，强度 " + c.mindbreakIntensity
                            + "，精神污染事件 " + MindbreakEvents.size() + " 个"
                            + "，事件总数 " + TrollRegistry.size() + " 个"
                            + " §7(交互回调 " + (c.mindbreakHooks ? "开" : "关")
                            + " / 聊天扭曲 " + (c.chatMangle ? "开" : "关")
                            + " / 总开关 " + (c.enabled ? "开" : "关") + ")");
                    return 1;
                })));
    }

    private static TrollConfig cfg() {
        if (ZhengrenMod.CONFIG == null) {
            ZhengrenMod.CONFIG = TrollConfig.load();
        }
        return ZhengrenMod.CONFIG;
    }

    private static String intensityComment(int level) {
        return switch (level) {
            case 1 -> "温柔模式，只是偶尔搞你一下。";
            case 3 -> "不做人模式，出事了别找我。";
            default -> "正常模式，建议保持警惕。";
        };
    }

    private static void feedback(ServerCommandSource src, String text) {
        src.sendFeedback(() -> Text.literal(text), false);
    }

    private static void error(ServerCommandSource src, String text) {
        src.sendError(Text.literal(text));
    }

    private static int help(ServerCommandSource src) {
        feedback(src, "【精神污染 Mindbreak】命令列表：");
        feedback(src, "/mindbreak toggle            开启或关闭精神污染事件（共用 /zr 配置）");
        feedback(src, "/mindbreak intensity <1-3>   设置强度（等价于 /zr intensity）");
        feedback(src, "/mindbreak trigger <事件id>  立刻触发一个事件");
        feedback(src, "/mindbreak list              列出全部精神污染事件");
        feedback(src, "/mindbreak gift              领取五件神器（等价于 /zr gift）");
        feedback(src, "/mindbreak chaos             从统一事件表随机五连发");
        feedback(src, "/mindbreak stats             查看状态");
        feedback(src, "提示：/mb 是 /mindbreak 的别名；总开关请用 /zr on|off。");
        feedback(src, "这个模组不会保护你的存档，也不会保护你的精神状态。");
        return 1;
    }
}
