package com.zhengren.trollmod.core;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 一个整人事件。用 {@link #of(String, String, TrollTier)} 构建。
 *
 * <pre>
 * reg(TrollEvent.of("id", "显示名", TrollTier.ANNOYING)
 *         .desc("说明")
 *         .weight(120)
 *         .cooldown(600)
 *         .when(ctx -&gt; ctx.player().isOnGround())
 *         .run(ctx -&gt; { ctx.msg("嘿嘿"); })
 *         .build());
 * </pre>
 */
public final class TrollEvent {

    private final String id;
    private final String display;
    private final TrollTier tier;
    private final String description;
    private final int weight;
    private final int cooldownTicks;
    private final boolean oncePerPlayer;
    private final Predicate<TrollContext> condition;
    private final Consumer<TrollContext> action;
    private String category = "通用";

    private TrollEvent(Builder b) {
        this.id = b.id;
        this.display = b.display;
        this.tier = b.tier;
        this.description = b.description;
        this.weight = b.weight;
        this.cooldownTicks = b.cooldownTicks;
        this.oncePerPlayer = b.oncePerPlayer;
        this.condition = b.condition;
        this.action = b.action;
    }

    public static Builder of(String id, String display, TrollTier tier) {
        return new Builder(id, display, tier);
    }

    public String id() {
        return id;
    }

    public String display() {
        return display;
    }

    public TrollTier tier() {
        return tier;
    }

    public String description() {
        return description;
    }

    public int weight() {
        return weight;
    }

    public int cooldownTicks() {
        return cooldownTicks;
    }

    public boolean oncePerPlayer() {
        return oncePerPlayer;
    }

    public String category() {
        return category;
    }

    void setCategory(String category) {
        this.category = category;
    }

    public boolean canRun(TrollContext ctx) {
        try {
            return condition == null || condition.test(ctx);
        } catch (Throwable t) {
            return false;
        }
    }

    public void run(TrollContext ctx) {
        action.accept(ctx);
    }

    @Override
    public String toString() {
        return id + "(" + display + "," + tier.cn() + ")";
    }

    public static final class Builder {
        private final String id;
        private final String display;
        private final TrollTier tier;
        private String description = "";
        private int weight = 100;
        private int cooldownTicks = 900;
        private boolean oncePerPlayer = false;
        private Predicate<TrollContext> condition;
        private Consumer<TrollContext> action = ctx -> { };

        private Builder(String id, String display, TrollTier tier) {
            this.id = id;
            this.display = display;
            this.tier = tier;
        }

        public Builder desc(String d) {
            this.description = d;
            return this;
        }

        public Builder weight(int w) {
            this.weight = Math.max(1, w);
            return this;
        }

        /** 冷却时间（秒） */
        public Builder cooldownSeconds(int seconds) {
            this.cooldownTicks = Math.max(20, seconds * 20);
            return this;
        }

        /** 冷却时间（tick） */
        public Builder cooldown(int ticks) {
            this.cooldownTicks = Math.max(20, ticks);
            return this;
        }

        public Builder once() {
            this.oncePerPlayer = true;
            return this;
        }

        public Builder when(Predicate<TrollContext> p) {
            this.condition = p;
            return this;
        }

        public Builder run(Consumer<TrollContext> c) {
            this.action = c;
            return this;
        }

        public TrollEvent build() {
            return new TrollEvent(this);
        }
    }
}
