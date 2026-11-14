package com.zhengren.trollmod;

import com.zhengren.trollmod.command.MindbreakCommand;
import com.zhengren.trollmod.command.TrollCommand;
import com.zhengren.trollmod.core.DelayScheduler;
import com.zhengren.trollmod.core.TrollConfig;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollRegistry;
import com.zhengren.trollmod.core.TrollScheduler;
import com.zhengren.trollmod.item.ModItems;
import com.zhengren.trollmod.mindbreak.MindbreakEvents;
import com.zhengren.trollmod.mindbreak.MindbreakHooks;
import com.zhengren.trollmod.net.GlitchPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 整人大师 —— 主入口。
 *
 * <p>本模组是「整人大师 (zhengren)」与「精神污染 (mindbreak)」的合并体：
 * 两套整活内容共用同一个事件注册表、同一个调度器、同一份配置和同一套命令，
 * 因此可以互相触发、统一开关、统一调强度。</p>
 */
public class ZhengrenMod implements ModInitializer {

    public static final String MOD_ID = "zhengren";
    public static final Logger LOGGER = LoggerFactory.getLogger("zhengren");

    public static TrollConfig CONFIG;

    @Override
    public void onInitialize() {
        CONFIG = TrollConfig.load();

        try {
            GlitchPayload.register();
        } catch (Throwable t) {
            LOGGER.warn("[整人大师] 客户端整活网络包注册失败（不影响服务端事件）: {}", t.toString());
        }

        // 「精神污染」的五件神器（合并进来的内容）
        ModItems.register();

        // 事件表：14 个原生分类 + 合并进来的 44 个精神污染事件
        TrollEvents.bootstrap();
        // 延时任务调度器（精神污染带来的新能力，所有事件共用）
        DelayScheduler.register();
        // 交互型回调：挖方块 / 右键 / 攻击 / 聊天 / 死亡 / 进出游戏
        MindbreakHooks.register();

        TrollScheduler.register();
        TrollCommand.register();
        // 原 mindbreak 命令的兼容入口（/mindbreak、/mb）
        MindbreakCommand.register();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> CONFIG = TrollConfig.load());

        int[] tiers = TrollRegistry.countPerTier();
        LOGGER.info("[整人大师] 加载完成：共 {} 个整人事件（小整{} / 烦人{} / 抓狂{} / 崩溃{} / 地狱{}），"
                        + "其中「精神污染」{} 个",
                TrollRegistry.size(), tiers[1], tiers[2], tiers[3], tiers[4], tiers[5],
                MindbreakEvents.size());
        if (!TrollRegistry.duplicates().isEmpty()) {
            LOGGER.warn("[整人大师] 发现 {} 个重复事件 ID: {}", TrollRegistry.duplicates().size(),
                    TrollRegistry.duplicates());
        }
    }
}
