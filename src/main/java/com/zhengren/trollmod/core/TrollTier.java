package com.zhengren.trollmod.core;

/**
 * 事件残酷等级。数字越大越折磨人。
 *
 * <p>第 6 档 {@link #OBLIVION}（绝望）是 v3.0.0 新增的顶层：这一档不追求"有趣"，
 * 只追求把玩家彻底击溃。</p>
 */
public enum TrollTier {
    PRANK(1, "小整", "Prank"),
    ANNOYING(2, "烦人", "Annoying"),
    BRUTAL(3, "抓狂", "Brutal"),
    NIGHTMARE(4, "崩溃", "Nightmare"),
    APOCALYPSE(5, "地狱", "Apocalypse"),
    OBLIVION(6, "绝望", "Oblivion");

    private final int level;
    private final String cn;
    private final String en;

    TrollTier(int level, String cn, String en) {
        this.level = level;
        this.cn = cn;
        this.en = en;
    }

    public int level() {
        return level;
    }

    public String cn() {
        return cn;
    }

    public String en() {
        return en;
    }

    public static TrollTier byLevel(int level) {
        for (TrollTier t : values()) {
            if (t.level == level) {
                return t;
            }
        }
        return PRANK;
    }

    /** 用于聊天栏显示的彩色标签 */
    public String colored() {
        switch (this) {
            case PRANK:
                return "§a[" + cn + "]";
            case ANNOYING:
                return "§e[" + cn + "]";
            case BRUTAL:
                return "§6[" + cn + "]";
            case NIGHTMARE:
                return "§c[" + cn + "]";
            case APOCALYPSE:
                return "§4§l[" + cn + "]";
            default:
                // 绝望：黑底 + 乱码扰动，视觉上就不像好东西
                return "§0§l§k||§r§4§l[" + cn + "]§r§0§l§k||";
        }
    }
}
