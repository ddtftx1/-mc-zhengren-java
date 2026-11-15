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
    /** 允许出现的最高残酷等级 1-6（6 = 绝望） */
    public int maxTier = 6;
    /** 两次事件之间的最小间隔（秒） */
    public int minIntervalSeconds = 8;
    /** 两次事件之间的最大间隔（秒） */
    public int maxIntervalSeconds = 30;
    /** 随着游戏时间推移越来越狠 */
    public boolean escalation = true;
    /** 是否在聊天栏提示刚刚发生了什么 */
    public boolean announce = true;
    /** 是否允许"大规模地形改造"类事件（boomDestructive / meteorShower 的 TNT 模式 / lavaPit）。
     *  v3.0.0 起默认开启——本模组不打算给玩家留全尸。 */
    public boolean allowWorldDamage = true;
    /** 是否影响创造模式玩家 */
    public boolean affectCreative = true;
    /** 是否允许客户端画面类整活（假崩溃界面等） */
    public boolean clientGlitches = true;
    /** 是否允许踢出类事件（假掉线） */
    public boolean fakeKick = true;
    /** 每分钟最多触发的事件数 */
    public int maxEventsPerMinute = 40;
    /** 客户端整活强度倍率 0.1 - 5.0 */
    public double glitchPower = 1.0;

    // ==================================================================
    // v3.0.0「残酷化」总控
    // ==================================================================

    /** 残酷度 1-5：全局放大伤害、效果时长、爆炸威力、物品损失与位移强度。
     *  默认 5（最高）——要整就整到底。 */
    public int brutality = 5;
    /** 温和事件是否附带"残酷惩罚"：等级 ≤ 烦人 的事件执行完后，额外追加一次重击。
     *  这让 2000 个事件里那些"只是嘴上说说"的事件也真的会掉血、掉东西、被埋。 */
    public boolean punishmentRider = true;
    /** 惩罚附加的触发概率（0-100）。默认 100 = 每次温和事件都追加。 */
    public int punishmentChance = 100;
    /** 连击概率（0-100）：事件结束后有概率立刻再触发一个随机事件。 */
    public int chainChance = 45;
    /** 连击最多嵌套层数（防止无限递归）。 */
    public int maxChainDepth = 3;
    /** 玩家死亡后是否继续追击：重生时立即补一次重击。 */
    public boolean noMercy = true;
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

    /**
     * 配置文件版本（v3.0.0 新增）。
     *
     * <p>用于把**旧版本的默认值**迁移到新版本的默认值。判断依据是"当前值是否恰好等于旧默认值"——
     * 因此用户手动改过的设置不会被覆盖，而从未动过的设置会自动跟上新版本的残酷默认值。</p>
     *
     * <p>初始值必须是 {@code 0}（未标记）而不是 {@code 3}：Gson 对没有该字段的旧 JSON 会保留
     * 字段初始值，若初始值就是 3，迁移将永远不会触发。</p>
     */
    public int configVersion = 0;

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
        } else {
            cfg.migrateIfNeeded();
        }
        cfg.reindex();
        cfg.save();
        return cfg;
    }

    /**
     * 旧配置迁移。只在 {@code configVersion < 3} 时执行一次。
     *
     * <p>两条原则：</p>
     * <ol>
     *   <li><b>不覆盖用户的自定义</b>：只有"当前值仍等于 v2 旧默认值"的项才改写。</li>
     *   <li><b>破坏性开关更保守</b>：{@code allowWorldDamage} 的 v2 默认值是 {@code false}，
     *       与"用户刻意关掉"无法区分。因此只有在**整份配置看起来完全没被动过**时才会打开它，
     *       否则原样保留——否则会毁掉服主精心保护的建筑。</li>
     * </ol>
     *
     * <p>{@code maxTier} 必须迁移：v2 的默认/上限是 5，而 v3 新增了第 6 档「绝望」，
     * 不迁移的话那 258 个事件永远不会被抽到。</p>
     */
    private void migrateIfNeeded() {
        if (configVersion >= 3) {
            return;
        }
        boolean changed = false;
        boolean destroyedFlagChanged = false;

        // 整份配置是否完全是 v2 的默认样子
        boolean looksUntouched = maxTier == 5
                && minIntervalSeconds == 20 && maxIntervalSeconds == 75
                && maxEventsPerMinute == 14
                && !allowWorldDamage
                && (disabled == null || disabled.isEmpty())
                && (targets == null || targets.isEmpty());

        // maxTier：旧默认/旧上限 5 → 新的 6（否则「绝望」档不可达）
        if (maxTier == 5) {
            maxTier = 6;
            changed = true;
        }
        // 节奏：旧默认 20~75 秒 / 14 次每分钟 → 新默认 8~30 秒 / 40 次
        if (minIntervalSeconds == 20 && maxIntervalSeconds == 75) {
            minIntervalSeconds = 8;
            maxIntervalSeconds = 30;
            changed = true;
        }
        if (maxEventsPerMinute == 14) {
            maxEventsPerMinute = 40;
            changed = true;
        }
        // 地形破坏：只在整份配置未被改动过时才跟随新默认值
        if (looksUntouched && !allowWorldDamage) {
            allowWorldDamage = true;
            changed = true;
            destroyedFlagChanged = true;
        }

        configVersion = 3;
        if (changed) {
            ZhengrenMod.LOGGER.info("[整人大师] 已把旧版默认配置迁移到 v3 残酷默认值"
                            + "（maxTier=6 / 间隔 8-30s / 每分钟 40{}）",
                    destroyedFlagChanged ? " / 允许破坏地形" : "；地形破坏开关保持原样");
        } else if (!allowWorldDamage) {
            ZhengrenMod.LOGGER.info("[整人大师] 检测到自定义配置，已保留你的设置"
                    + "（地形破坏仍为关闭；想体验完整残酷化可改 allowWorldDamage 或 /zr damage true）");
        }
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
        if (maxTier > 6) {
            maxTier = 6;
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
        if (brutality < 1) {
            brutality = 1;
        }
        if (brutality > 5) {
            brutality = 5;
        }
        if (punishmentChance < 0) {
            punishmentChance = 0;
        }
        if (punishmentChance > 100) {
            punishmentChance = 100;
        }
        if (chainChance < 0) {
            chainChance = 0;
        }
        if (chainChance > 100) {
            chainChance = 100;
        }
        if (maxChainDepth < 0) {
            maxChainDepth = 0;
        }
        if (maxChainDepth > 10) {
            maxChainDepth = 10;
        }
    }

    /**
     * 残酷度倍率。用于放大伤害、效果时长、爆炸威力等一切"痛感"数值。
     *
     * <p>1→0.5×（还留一丝人性）／2→1.0×（原始强度）／3→1.8×／4→3.0×／5→5.0×</p>
     */
    public double brutalityScale() {
        return switch (brutality) {
            case 1 -> 0.5;
            case 2 -> 1.0;
            case 3 -> 1.8;
            case 4 -> 3.0;
            default -> 5.0;
        };
    }

    /** 附加效果的等级加成：残酷度越高，给的药水效果等级越高。 */
    public int brutalityAmplifierBonus() {
        return switch (brutality) {
            case 1 -> 0;
            case 2 -> 0;
            case 3 -> 1;
            case 4 -> 2;
            default -> 3;
        };
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
