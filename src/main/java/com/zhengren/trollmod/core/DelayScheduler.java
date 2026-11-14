package com.zhengren.trollmod.core;

import com.zhengren.trollmod.ZhengrenMod;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 延迟任务调度器。合并自 mindbreak 的 ChaosScheduler：
 * 用来实现"偷你的东西 10 秒后还给你"这类跨 tick 的阴间操作。
 *
 * <p>整人大师原有的 571 个事件都是瞬时执行的，这里补上了延时能力，
 * 合并后的所有事件（含精神污染事件）都可以通过 {@link TrollContext#later(int, Runnable)} 使用。</p>
 */
public final class DelayScheduler {

    private static final List<Scheduled> TASKS = new ArrayList<>();

    private DelayScheduler() {
    }

    private static final class Scheduled {
        final int due;
        final Runnable action;

        Scheduled(int due, Runnable action) {
            this.due = due;
            this.action = action;
        }
    }

    /** 注册到服务端 tick 事件（由主入口调用一次）。 */
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(DelayScheduler::tick);
        // 服务器停止时清空待执行任务，避免换存档 / 重开服后残留旧任务去操作已失效的玩家对象
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPED
                .register(server -> clear());
    }

    /** 清空所有待执行任务。 */
    public static void clear() {
        TASKS.clear();
    }

    public static void schedule(MinecraftServer server, int delayTicks, Runnable action) {
        if (server == null || action == null) {
            return;
        }
        TASKS.add(new Scheduled(server.getTicks() + Math.max(1, delayTicks), action));
    }

    public static void tick(MinecraftServer server) {
        if (TASKS.isEmpty()) {
            return;
        }
        List<Scheduled> due = new ArrayList<>();
        Iterator<Scheduled> iterator = TASKS.iterator();
        while (iterator.hasNext()) {
            Scheduled task = iterator.next();
            if (server.getTicks() >= task.due) {
                iterator.remove();
                due.add(task);
            }
        }
        for (Scheduled task : due) {
            try {
                task.action.run();
            } catch (Exception exception) {
                ZhengrenMod.LOGGER.warn("[整人大师] 延迟任务执行失败：{}", exception.toString());
            }
        }
    }

    /** 尚未执行的延时任务数量（调试 / 验收用）。 */
    public static int pending() {
        return TASKS.size();
    }
}
