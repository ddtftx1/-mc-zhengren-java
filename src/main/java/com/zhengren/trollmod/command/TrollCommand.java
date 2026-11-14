package com.zhengren.trollmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.zhengren.trollmod.ZhengrenMod;
import com.zhengren.trollmod.core.PlayerTrollState;
import com.zhengren.trollmod.core.TrollConfig;
import com.zhengren.trollmod.core.TrollEvent;
import com.zhengren.trollmod.core.TrollRegistry;
import com.zhengren.trollmod.core.TrollScheduler;
import com.zhengren.trollmod.core.TrollTier;
import com.zhengren.trollmod.item.ModItems;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * /zhengren （别名 /zr、/troll）指令。
 */
public final class TrollCommand {

    private TrollCommand() {
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, env) -> {
            build(dispatcher, "zhengren");
            build(dispatcher, "zr");
            build(dispatcher, "troll");
        });
    }

    private static void build(CommandDispatcher<ServerCommandSource> dispatcher, String name) {
        dispatcher.register(CommandManager.literal(name)
                .executes(ctx -> help(ctx.getSource()))
                .then(CommandManager.literal("on").executes(ctx -> {
                    TrollConfig c = cfg();
                    c.enabled = true;
                    c.save();
                    feedback(ctx.getSource(), "§a整人模式已开启 §7(共 " + TrollRegistry.size() + " 个事件待命)");
                    return 1;
                }))
                .then(CommandManager.literal("off").executes(ctx -> {
                    TrollConfig c = cfg();
                    c.enabled = false;
                    c.save();
                    feedback(ctx.getSource(), "§e整人模式已关闭。");
                    return 1;
                }))
                .then(CommandManager.literal("reload").executes(ctx -> {
                    ZhengrenMod.CONFIG = TrollConfig.load();
                    feedback(ctx.getSource(), "§a配置已重载。");
                    return 1;
                }))
                .then(CommandManager.literal("count").executes(ctx -> {
                    int[] t = TrollRegistry.countPerTier();
                    feedback(ctx.getSource(), "§6事件总数：§f" + TrollRegistry.size()
                            + " §7| §a小整 " + t[1] + " §e烦人 " + t[2] + " §6抓狂 " + t[3]
                            + " §c崩溃 " + t[4] + " §4地狱 " + t[5]);
                    return 1;
                }))
                .then(CommandManager.literal("stats").executes(ctx -> {
                    ServerPlayerEntity p = ctx.getSource().getPlayer();
                    if (p == null) {
                        feedback(ctx.getSource(), "§c该指令需要玩家执行。");
                        return 0;
                    }
                    PlayerTrollState st = TrollScheduler.state(p);
                    feedback(ctx.getSource(), "§6崩溃指数：§f" + (int) st.rage
                            + " §7| 累计被整：§f" + st.firedCount + " §7次 | 上次：§f"
                            + (st.lastEventId == null ? "无" : st.lastEventId));
                    return 1;
                }))
                .then(CommandManager.literal("rage").executes(ctx -> {
                    ServerPlayerEntity p = ctx.getSource().getPlayer();
                    if (p == null) {
                        return 0;
                    }
                    PlayerTrollState st = TrollScheduler.state(p);
                    feedback(ctx.getSource(), "§6崩溃指数：§f" + (int) st.rage);
                    return 1;
                }))
                .then(CommandManager.literal("chaos")
                        .executes(ctx -> chaos(ctx.getSource(), 30))
                        .then(CommandManager.argument("seconds", IntegerArgumentType.integer(1, 3600))
                                .executes(ctx -> chaos(ctx.getSource(),
                                        IntegerArgumentType.getInteger(ctx, "seconds")))))
                .then(CommandManager.literal("calm").executes(ctx -> {
                    TrollScheduler.stopChaos();
                    feedback(ctx.getSource(), "§b狂暴模式已停止。");
                    return 1;
                }))
                .then(CommandManager.literal("mode")
                        .then(CommandManager.argument("level", IntegerArgumentType.integer(1, 5))
                                .executes(ctx -> {
                                    TrollConfig c = cfg();
                                    c.maxTier = IntegerArgumentType.getInteger(ctx, "level");
                                    c.save();
                                    feedback(ctx.getSource(), "§6最高残酷等级：" + TrollTier.byLevel(c.maxTier).colored());
                                    return 1;
                                })))
                // 合并入口：等价于 /mindbreak intensity <1-3>
                .then(CommandManager.literal("intensity")
                        .then(CommandManager.argument("level", IntegerArgumentType.integer(1, 3))
                                .executes(ctx -> {
                                    TrollConfig c = cfg();
                                    c.mindbreakIntensity = IntegerArgumentType.getInteger(ctx, "level");
                                    c.reindex();
                                    c.save();
                                    feedback(ctx.getSource(), "§6精神污染强度：" + c.mindbreakIntensity
                                            + " §7(等价于 /mindbreak intensity)");
                                    return 1;
                                })))
                // 合并入口：等价于 /mindbreak gift
                .then(CommandManager.literal("gift").executes(ctx -> gift(ctx.getSource())))
                .then(CommandManager.literal("interval")
                        .then(CommandManager.argument("min", IntegerArgumentType.integer(1, 3600))
                                .then(CommandManager.argument("max", IntegerArgumentType.integer(1, 3600))
                                        .executes(ctx -> {
                                            TrollConfig c = cfg();
                                            c.minIntervalSeconds = IntegerArgumentType.getInteger(ctx, "min");
                                            c.maxIntervalSeconds = IntegerArgumentType.getInteger(ctx, "max");
                                            c.reindex();
                                            c.save();
                                            feedback(ctx.getSource(), "§6事件间隔：" + c.minIntervalSeconds + " ~ "
                                                    + c.maxIntervalSeconds + " 秒");
                                            return 1;
                                        }))))
                .then(CommandManager.literal("damage")
                        .then(CommandManager.argument("value", com.mojang.brigadier.arguments.BoolArgumentType.bool())
                                .executes(ctx -> {
                                    TrollConfig c = cfg();
                                    c.allowWorldDamage = com.mojang.brigadier.arguments.BoolArgumentType
                                            .getBool(ctx, "value");
                                    c.save();
                                    feedback(ctx.getSource(), "§6允许破坏地形：" + c.allowWorldDamage);
                                    return 1;
                                })))
                .then(CommandManager.literal("list")
                        .executes(ctx -> list(ctx.getSource(), 1))
                        .then(CommandManager.argument("page", IntegerArgumentType.integer(1, 99))
                                .executes(ctx -> list(ctx.getSource(),
                                        IntegerArgumentType.getInteger(ctx, "page")))))
                .then(CommandManager.literal("info")
                        .then(CommandManager.argument("id", StringArgumentType.word())
                                .executes(ctx -> info(ctx.getSource(), StringArgumentType.getString(ctx, "id")))))
                .then(CommandManager.literal("fire")
                        .then(CommandManager.argument("id", StringArgumentType.word())
                                .executes(ctx -> fireSelf(ctx.getSource(), StringArgumentType.getString(ctx, "id")))
                                .then(CommandManager.argument("target", EntityArgumentType.player())
                                        .executes(ctx -> fireOther(ctx.getSource(),
                                                StringArgumentType.getString(ctx, "id"),
                                                EntityArgumentType.getPlayer(ctx, "target"))))))
                .then(CommandManager.literal("random")
                        .executes(ctx -> random(ctx.getSource(), ctx.getSource().getPlayer()))
                        .then(CommandManager.argument("target", EntityArgumentType.player())
                                .executes(ctx -> random(ctx.getSource(),
                                        EntityArgumentType.getPlayer(ctx, "target")))))
        );
    }

    private static TrollConfig cfg() {
        if (ZhengrenMod.CONFIG == null) {
            ZhengrenMod.CONFIG = TrollConfig.load();
        }
        return ZhengrenMod.CONFIG;
    }

    private static void feedback(ServerCommandSource src, String text) {
        src.sendFeedback(() -> Text.literal(text), false);
    }

    private static int help(ServerCommandSource src) {
        feedback(src, "§8===== §c整人大师 §8=====");
        feedback(src, "§7/zr on §f| §7off §f| §7count §f| §7stats §f| §7rage");
        feedback(src, "§7/zr mode <1-5> §f| §7interval <秒> <秒> §f| §7damage <true|false>");
        feedback(src, "§7/zr intensity <1-3> §f| §7gift §f| §7chaos [秒] §f| §7calm §f| §7list [页] §f| §7info <id>");
        feedback(src, "§7/zr fire <id> [玩家] §f| §7random [玩家]");
        feedback(src, "§8已合并精神污染：§7/zr list 可看到全部事件 §8(§7/mindbreak§8、§7/mb§8 亦可用)");
        feedback(src, "§7当前：" + (cfg().enabled ? "§a开启" : "§c关闭")
                + " §7最高等级：" + TrollTier.byLevel(cfg().maxTier).colored()
                + " §7间隔：" + cfg().minIntervalSeconds + "-" + cfg().maxIntervalSeconds + "s");
        return 1;
    }

    /** 合并入口：等价于 /mindbreak gift，发全套精神污染神器。 */
    private static int gift(ServerCommandSource src) {
        ServerPlayerEntity p = src.getPlayer();
        if (p == null) {
            feedback(src, "§c该指令需要玩家执行。");
            return 0;
        }
        List<Item> items = ModItems.all();
        for (Item item : items) {
            ItemStack stack = new ItemStack(item);
            if (!p.giveItemStack(stack)) {
                p.dropItem(stack, false);
            }
        }
        feedback(src, "§6精神污染五件套已发放 §7(共 " + items.size() + " 件，等价于 /mindbreak gift)");
        return 1;
    }

    private static int chaos(ServerCommandSource src, int seconds) {
        ServerPlayerEntity p = src.getPlayer();
        long now = src.getServer().getTicks();
        TrollScheduler.startChaos(now, seconds);
        feedback(src, "§4§l狂暴模式启动！§r §c接下来 " + seconds + " 秒，每 1 秒整你一次。");
        if (p != null) {
            p.sendMessage(Text.literal("§4§l准备好了吗？"), false);
        }
        return 1;
    }

    private static int fireSelf(ServerCommandSource src, String id) {
        ServerPlayerEntity p = src.getPlayer();
        if (p == null) {
            feedback(src, "§c控制台请指定玩家：/zr fire <id> <玩家>");
            return 0;
        }
        return doFire(src, id, p);
    }

    private static int fireOther(ServerCommandSource src, String id, ServerPlayerEntity target) {
        return doFire(src, id, target);
    }

    private static int doFire(ServerCommandSource src, String id, ServerPlayerEntity target) {
        if (!TrollRegistry.has(id)) {
            feedback(src, "§c找不到事件：§f" + id + " §7(用 /zr list 查看)");
            return 0;
        }
        boolean ok = TrollScheduler.fireById(src.getServer(), target, id);
        feedback(src, ok ? "§a已对 " + target.getName().getString() + " 触发 §f" + id : "§c触发失败。");
        return ok ? 1 : 0;
    }

    private static int random(ServerCommandSource src, ServerPlayerEntity target) {
        if (target == null) {
            feedback(src, "§c需要指定玩家。");
            return 0;
        }
        TrollEvent e = TrollScheduler.randomEventFiltered(cfg());
        if (e == null) {
            feedback(src, "§c没有可用事件。");
            return 0;
        }
        TrollScheduler.fire(src.getServer(), target, cfg(), TrollScheduler.state(target), e);
        feedback(src, "§a随机事件：§f" + e.id());
        return 1;
    }

    private static int info(ServerCommandSource src, String id) {
        TrollEvent e = TrollRegistry.get(id);
        if (e == null) {
            feedback(src, "§c找不到事件：§f" + id);
            return 0;
        }
        feedback(src, "§8--- §f" + e.id() + " §8---");
        feedback(src, "§7名称：" + e.tier().colored() + " §f" + e.display());
        feedback(src, "§7分类：" + e.category() + " §7权重：" + e.weight()
                + " §7冷却：" + (e.cooldownTicks() / 20) + "s");
        if (!e.description().isEmpty()) {
            feedback(src, "§7说明：" + e.description());
        }
        return 1;
    }

    private static int list(ServerCommandSource src, int page) {
        Map<String, List<TrollEvent>> map = TrollRegistry.byCategory();
        List<TrollEvent> flat = new ArrayList<>(TrollRegistry.all());
        int perPage = 30;
        int pages = Math.max(1, (flat.size() + perPage - 1) / perPage);
        int p = Math.max(1, Math.min(page, pages));
        feedback(src, "§8===== 事件列表 §7(第 " + p + "/" + pages + " 页，共 " + flat.size() + " 个) §8=====");
        StringBuilder sb = new StringBuilder();
        for (int i = (p - 1) * perPage; i < Math.min(flat.size(), p * perPage); i++) {
            TrollEvent e = flat.get(i);
            sb.append(e.tier().colored()).append(" §f").append(e.id()).append("§7, ");
        }
        for (String line : split(sb.toString(), 240)) {
            feedback(src, line);
        }
        feedback(src, "§7分类：" + String.join(" / ", map.keySet()));
        return 1;
    }

    private static List<String> split(String text, int max) {
        List<String> out = new ArrayList<>();
        int i = 0;
        while (i < text.length()) {
            int end = Math.min(text.length(), i + max);
            out.add(text.substring(i, end));
            i = end;
        }
        if (out.isEmpty()) {
            out.add("§7(空)");
        }
        return out;
    }
}
