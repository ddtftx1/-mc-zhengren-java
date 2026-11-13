package com.zhengren.trollmod.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

/**
 * 纯黑屏（含随机噪点文字），持续到 glitch 结束。
 */
public class BlackoutScreen extends Screen {

    private int ticks;

    public BlackoutScreen() {
        super(Text.literal(" "));
    }

    @Override
    public void tick() {
        ticks++;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(0, 0, width, height, 0xFF000000);
        if (ticks % 20 < 6) {
            ctx.drawTextWithShadow(textRenderer, Text.literal("§8..."), width / 2, height / 2, 0x333333);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return true;
    }
}
