package com.zhengren.trollmod.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhengren.trollmod.ZhengrenMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 配置文件：config/zhengren.json
 */
public class TrollConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    /** 总开关 */
    public boolean enabled = true;
    /** 允许出现的最高残酷等级 1-5 */
    public int maxTier = 5;
    /** 两次事件之间的最小间隔（秒） */
    public int minIntervalSeconds = 20;
    /** 两次事件之间的最大间隔（秒） */
    public int maxIntervalSeconds = 75;
    /** 随着游戏时间推移越来越狠 */
    public boolean escalation = true;
    /** 是否在聊天栏提示刚刚发生了什么 */
    public boolean announce = true;
    /** 是否允许"大规模地形改造"类事件（boomDestructive / meteorShower 的 TNT 模式 / lavaPit）。
     *  注意：小型爆炸、`ctx.spawnTnt`、`voidPit`、`cageWith` 等仍会造成局部方块变化——这是本模组的整人本质，无法完全关闭。 */
    public boolean allowWorldDamage = false;
    /** 是否影响创造模式玩家 */
    public boolean affectCreative = true;
    /** 是否允许客户端画面类整活（假崩溃界面等） */
    public boolean clientGlitches = true;
    /** 是否允许踢出类事件（假掉线） */
    public boolean fakeKick = true;
    /** 每分钟最多触发的事件数 */
    public int maxEventsPerMinute = 14;
    /** 客户端整活强度倍率 0.1 - 5.0 */
    public double glitchPower = 1.0;
    /** 黑名单事件 ID */
    public List<String> disabled = new ArrayList<>();
    /** 白名单玩家名（为空表示所有玩家都整） */
    public List<String> targets = new ArrayList<>();

    // ==================================================================
    // 「精神污染」合并开关（原 mindbreak 模组的功能）
    // ==================================================================

    /** 是否让「精神污染」事件参与随机调度（关闭后仍可用 /zr fire 手动触发） */
    public boolean mindbreakEnabled = true;
    /** 精神污染强度 1-3：1 温柔 / 2 正常 / 3 不做人，影响精神污染事件的抽取权重 */
    public int mindbreakIntensity = 2;
    /** 是否启用交互型整活回调（挖方块被拒、方块变身、床拒绝你、打怪背刺……） */
    public boolean mindbreakHooks = true;
    /** 是否启用聊天内容扭曲 */
    public boolean chatMangle = true;

    private transient Set<String> disabledSet = new LinkedHashSet<>();
    private transient Set<String> targetSet = new LinkedHashSet<>();
    private transient long[] recentFires = new long[0];
    private transient int fireIndex = 0;
    private transient int fireCount = 0;

    public static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("zhengren.json");
    }

    public static TrollConfig load() {
        Path p = configPath();
        TrollConfig cfg = null;
        if (Files.exists(p)) {
            try {
                String json = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
                cfg = GSON.fromJson(json, TrollConfig.class);
            } catch (Exception e) {
                ZhengrenMod.LOGGER.warn("[整人大师] 配置文件读取失败，将使用默认值: {}", e.toString());
            }
        }
        if (cfg == null) {
            cfg = new TrollConfig();
        }
        cfg.reindex();
        cfg.save();
        return cfg;
    }

    public void save() {
        try {
            Path p = configPath();
            Files.createDirectories(p.getParent());
            Files.write(p, GSON.toJson(this).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            ZhengrenMod.LOGGER.warn("[整人大师] 配置保存失败: {}", e.toString());
        }
    }

    public void reindex() {
        if (disabled == null) {
            disabled = new ArrayList<>();
        }
        if (targets == null) {
            targets = new ArrayList<>();
        }
        disabledSet = new LinkedHashSet<>(disabled);
        targetSet = new LinkedHashSet<>(targets);
        recentFires = new long[Math.max(1, maxEventsPerMinute)];
        if (maxTier < 1) {
            maxTier = 1;
        }
        if (maxTier > 5) {
            maxTier = 5;
        }
        if (minIntervalSeconds < 1) {
            minIntervalSeconds = 1;
        }
        if (maxIntervalSeconds < minIntervalSeconds) {
            maxIntervalSeconds = minIntervalSeconds;
        }
        if (maxEventsPerMinute < 1) {
            maxEventsPerMinute = 1;
        }
        if (glitchPower < 0.1) {
            glitchPower = 0.1;
        }
        if (glitchPower > 5.0) {
            glitchPower = 5.0;
        }
        if (mindbreakIntensity < 1) {
            mindbreakIntensity = 1;
        }
        if (mindbreakIntensity > 3) {
            mindbreakIntensity = 3;
        }
    }

    /**
     * 「精神污染」事件的权重倍率，由 {@link #mindbreakIntensity} 推导。
     * 用于 {@link TrollScheduler#pick} 的实时加权，因此改强度不需要重载事件表。
     */
    public double mindbreakWeightScale() {
        return switch (mindbreakIntensity) {
            case 1 -> 0.5;
            case 3 -> 2.0;
            default -> 1.0;
        };
    }

    public boolean isDisabled(String id) {
        return disabledSet.contains(id);
    }

    public boolean isTarget(String name) {
        return targetSet.isEmpty() || targetSet.contains(name);
    }

    /** 频率闸门：返回 true 表示现在允许再触发一个事件。
     *  维护最近 maxEventsPerMinute 次触发的 tick，若在 1200 tick（1 分钟）内已用满则拒绝。 */
    public synchronized boolean allowFire(long now) {
        int window = Math.max(1, maxEventsPerMinute);
        if (recentFires.length != window) {
            recentFires = new long[window];
            fireIndex = 0;
            fireCount = 0;
        }
        long oldest = recentFires[fireIndex];
        if (fireCount >= window && now - oldest < 1200L) {
            return false;
        }
        recentFires[fireIndex] = now;
        fireIndex = (fireIndex + 1) % window;
        if (fireCount < window) {
            fireCount++;
        }
        return true;
    }

    public Set<String> disabledIds() {
        return disabledSet;
    }
}
