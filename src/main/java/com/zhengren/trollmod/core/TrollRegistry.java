package com.zhengren.trollmod.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局事件注册表。
 */
public final class TrollRegistry {

    private static final Map<String, TrollEvent> BY_ID = new LinkedHashMap<>();
    private static final List<TrollEvent> ALL = new ArrayList<>();
    private static final List<String> DUPLICATES = new ArrayList<>();

    private TrollRegistry() {
    }

    public static void register(String category, TrollEvent event) {
        if (BY_ID.containsKey(event.id())) {
            DUPLICATES.add(event.id());
            return;
        }
        event.setCategory(category);
        BY_ID.put(event.id(), event);
        ALL.add(event);
    }

    public static List<TrollEvent> all() {
        return Collections.unmodifiableList(ALL);
    }

    public static TrollEvent get(String id) {
        return BY_ID.get(id);
    }

    public static boolean has(String id) {
        return BY_ID.containsKey(id);
    }

    public static List<String> duplicates() {
        return Collections.unmodifiableList(DUPLICATES);
    }

    public static int size() {
        return ALL.size();
    }

    public static Map<String, List<TrollEvent>> byCategory() {
        Map<String, List<TrollEvent>> map = new LinkedHashMap<>();
        for (TrollEvent e : ALL) {
            map.computeIfAbsent(e.category(), k -> new ArrayList<>()).add(e);
        }
        return map;
    }

    /** 按等级筛选（level &lt;= maxLevel） */
    public static List<TrollEvent> upTo(int maxLevel) {
        List<TrollEvent> out = new ArrayList<>();
        for (TrollEvent e : ALL) {
            if (e.tier().level() <= maxLevel) {
                out.add(e);
            }
        }
        return out;
    }

    public static int[] countPerTier() {
        int[] c = new int[TrollTier.values().length + 1];
        for (TrollEvent e : ALL) {
            c[e.tier().level()]++;
        }
        return c;
    }
}
