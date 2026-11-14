package com.zhengren.trollmod.core;

import com.zhengren.trollmod.events.AudioEvents;
import com.zhengren.trollmod.events.BeastEvents;
import com.zhengren.trollmod.events.ChaosEvents;
import com.zhengren.trollmod.events.DoomEvents;
import com.zhengren.trollmod.events.GlitchEvents;
import com.zhengren.trollmod.events.ItemEvents;
import com.zhengren.trollmod.events.LootEvents;
import com.zhengren.trollmod.events.MindEvents;
import com.zhengren.trollmod.events.MobEvents;
import com.zhengren.trollmod.events.NightmareEvents;
import com.zhengren.trollmod.events.PlayerEvents;
import com.zhengren.trollmod.events.SocialEvents;
import com.zhengren.trollmod.events.TrapEvents;
import com.zhengren.trollmod.events.WorldEvents;

/**
 * 事件汇总入口。每个分类文件负责注册自己的一批事件。
 * 事件作者只需要调用 {@code reg(...)}，分类会自动打上。
 */
public final class TrollEvents {

    /**
     * 合并进来的「精神污染」（原 mindbreak 模组）事件统一使用的分类名。
     * 调度器用它识别扩展事件，从而按 {@code mindbreakIntensity} 缩放权重。
     */
    public static final String CATEGORY_MINDBREAK = "精神污染";

    private static String currentCategory = "通用";

    private TrollEvents() {
    }

    /** 切换当前分类（在每个分类文件开头调用一次） */
    public static void category(String name) {
        currentCategory = name;
    }

    /** 注册一个事件 */
    public static void reg(TrollEvent event) {
        TrollRegistry.register(currentCategory, event);
    }

    /** 注册一个事件（显式指定分类） */
    public static void reg(String category, TrollEvent event) {
        TrollRegistry.register(category, event);
    }

    public static void bootstrap() {
        // 第一批：230 个事件
        WorldEvents.register();
        ItemEvents.register();
        MobEvents.register();
        PlayerEvents.register();
        AudioEvents.register();
        GlitchEvents.register();
        ChaosEvents.register();
        // 第二批：扩展包
        TrapEvents.register();
        LootEvents.register();
        BeastEvents.register();
        MindEvents.register();
        SocialEvents.register();
        NightmareEvents.register();
        DoomEvents.register();
        // 第三批：合并进来的「精神污染」（原 mindbreak 模组）
        com.zhengren.trollmod.mindbreak.MindbreakEvents.register();
    }
}
