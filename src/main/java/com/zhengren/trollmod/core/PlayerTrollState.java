package com.zhengren.trollmod.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 单个玩家的整人状态。
 */
public class PlayerTrollState {

    public final UUID uuid;
    public final String name;

    /** 崩溃指数：越高越狠、事件越密 */
    public double rage = 0.0;
    /** 下次允许触发事件的 tick */
    public long nextEventTick = 0;
    /** 累计被整次数 */
    public int firedCount = 0;
    /** 上次触发的事件 id */
    public String lastEventId = null;
    /** 进入游戏时的 tick */
    public long joinedTick = 0;
    public long lastFireTick = 0;

    public final Map<String, Long> lastRun = new HashMap<>();
    public final Set<String> firedOnce = new HashSet<>();

    public PlayerTrollState(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public boolean onCooldown(String eventId, long now) {
        Long last = lastRun.get(eventId);
        return last != null;
    }

    public void markRun(String eventId, long now) {
        lastRun.put(eventId, now);
        lastEventId = eventId;
        lastFireTick = now;
        firedCount++;
    }
}
