package com.zhengren.trollmod.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.Random;

/**
 * 假崩溃界面：一个看起来很真实的 Minecraft 崩溃报告。
 * 点「退出游戏」会真的退出（够整人），点「我还能抢救一下」才会关掉。
 */
public class FakeCrashScreen extends Screen {

    private static final Random RNG = new Random();
    private final String crashId = Long.toHexString(System.nanoTime()).toUpperCase();
    private int ticks;

    public FakeCrashScreen() {
        super(Text.literal("游戏崩溃了"));
    }

    @Override
    protected void init() {
        int y = height - 52;
        addDrawableChild(ButtonWidget.builder(Text.literal("§c退出游戏"), b -> {
            if (client != null) {
                client.scheduleStop();
            }
        }).dimensions(width / 2 - 154, y, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("§7我还能抢救一下"), b -> {
            if (client != null) {
                client.setScreen(null);
            }
            ClientGlitchManager.clear("fakecrash");
        }).dimensions(width / 2 + 4, y, 150, 20).build());
    }

    @Override
    public void tick() {
        ticks++;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx);
        ctx.fill(0, 0, width, height, 0xD0000000);
        int x = 12;
        int y = 20;
        ctx.drawTextWithShadow(textRenderer, Text.literal("§c§lMinecraft 已崩溃"), x, y, 0xFFFFFF);
        y += 14;
        ctx.drawTextWithShadow(textRenderer,
                Text.literal("§7---- Minecraft Crash Report ----"), x, y, 0xAAAAAA);
        y += 12;
        ctx.drawTextWithShadow(textRenderer,
                Text.literal("§7// 别问我，我也不知道为什么"), x, y, 0xAAAAAA);
        y += 12;
        ctx.drawTextWithShadow(textRenderer,
                Text.literal("§7时间: " + java.time.LocalDateTime.now()), x, y, 0xAAAAAA);
        y += 12;
        ctx.drawTextWithShadow(textRenderer,
                Text.literal("§7描述: §fRendering screen §c" + randomClass()), x, y, 0xAAAAAA);
        y += 18;
        for (String line : stackTrace()) {
            ctx.drawTextWithShadow(textRenderer, Text.literal("§7" + line), x, y, 0xAAAAAA);
            y += 10;
            if (y > height - 70) {
                break;
            }
        }
        y = Math.max(y, height - 66);
        ctx.drawTextWithShadow(textRenderer,
                Text.literal("§8崩溃编号: " + crashId + "  §7(已运行 " + ticks + " tick)"), x, y, 0x888888);
        super.render(ctx, mouseX, mouseY, delta);
    }

    private String randomClass() {
        String[] names = {
                "net.minecraft.client.gui.hud.InGameHud",
                "net.minecraft.entity.player.PlayerInventory",
                "net.minecraft.client.render.WorldRenderer",
                "net.minecraft.server.MinecraftServer"
        };
        return names[RNG.nextInt(names.length)];
    }

    private String[] stackTrace() {
        return new String[]{
                "java.lang.NullPointerException: Cannot invoke \"Object.toString()\"",
                "\tat net.minecraft.client.MinecraftClient.tick(MinecraftClient.java:" + (1000 + RNG.nextInt(9000)) + ")",
                "\tat net.minecraft.client.gui.hud.InGameHud.render(InGameHud.java:" + (200 + RNG.nextInt(800)) + ")",
                "\tat com.zhengren.trollmod.client.FakeCrashScreen.render(FakeCrashScreen.java:1)",
                "\tat net.minecraft.client.render.GameRenderer.render(GameRenderer.java:" + (300 + RNG.nextInt(700)) + ")",
                "\t... 27 more"
        };
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
