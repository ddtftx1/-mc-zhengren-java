package com.zhengren.trollmod.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * 假「正在更新 / 正在重新加载」界面：进度永远卡在 99%，并反复提示「请勿关闭游戏」。
 */
public class FakeUpdatingScreen extends Screen {

    private final String line1;
    private final String line2;
    private int ticks;

    public FakeUpdatingScreen(String line1, String line2) {
        super(Text.literal(line1));
        this.line1 = line1;
        this.line2 = line2;
    }

    @Override
    protected void init() {
        int y = height - 52;
        addDrawableChild(ButtonWidget.builder(Text.literal("§c强制退出"), b -> {
            if (client != null) {
                client.scheduleStop();
            }
        }).dimensions(width / 2 - 154, y, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("§7继续等待（推荐）"), b -> {
            if (client != null) {
                client.setScreen(null);
            }
        }).dimensions(width / 2 + 4, y, 150, 20).build());
    }

    @Override
    public void tick() {
        ticks++;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(0, 0, width, height, 0xF00A0A12);
        int cx = width / 2;
        int y = height / 2 - 50;
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("§f§l" + line1), cx, y, 0xFFFFFF);
        y += 22;
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("§7" + line2), cx, y, 0xAAAAAA);

        // 假进度条：永远 99%
        int barW = Math.min(320, width - 80);
        int barX = cx - barW / 2;
        int barY = y + 26;
        ctx.fill(barX - 1, barY - 1, barX + barW + 1, barY + 11, 0xFF888888);
        ctx.fill(barX, barY, barX + barW, barY + 10, 0xFF202020);
        int fill = (int) (barW * 0.99);
        ctx.fill(barX, barY, barX + fill, barY + 10, 0xFF3FBF5F);
        ctx.drawTextWithShadow(textRenderer, Text.literal("§f99%"), barX + barW + 4, barY + 1, 0xFFFFFF);

        int y2 = barY + 26;
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal("§e请勿关闭游戏 §7(" + (ticks / 20) + "s)"), cx, y2, 0xFFFFFF);
        y2 += 14;
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal("§8剩余时间：约 " + (3 + (ticks / 100)) + " 分钟"), cx, y2, 0x888888);
        y2 += 22;
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal("§8正在校验文件校验和校验"), cx, y2, 0x666666);
        super.render(ctx, mouseX, mouseY, delta);
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
