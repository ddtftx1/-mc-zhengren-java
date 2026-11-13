package com.zhengren.trollmod;

import com.zhengren.trollmod.command.TrollCommand;
import com.zhengren.trollmod.core.TrollConfig;
import com.zhengren.trollmod.core.TrollEvents;
import com.zhengren.trollmod.core.TrollRegistry;
import com.zhengren.trollmod.core.TrollScheduler;
import com.zhengren.trollmod.net.GlitchPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 整人大师 —— 主入口。
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

        TrollEvents.bootstrap();
        TrollScheduler.register();
        TrollCommand.register();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> CONFIG = TrollConfig.load());

        int[] tiers = TrollRegistry.countPerTier();
        LOGGER.info("[整人大师] 加载完成：共 {} 个整人事件（小整{} / 烦人{} / 抓狂{} / 崩溃{} / 地狱{}）",
                TrollRegistry.size(), tiers[1], tiers[2], tiers[3], tiers[4], tiers[5]);
        if (!TrollRegistry.duplicates().isEmpty()) {
            LOGGER.warn("[整人大师] 发现 {} 个重复事件 ID: {}", TrollRegistry.duplicates().size(),
                    TrollRegistry.duplicates());
        }
    }
}
