package com.zhengren.trollmod.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * 假掉线界面：看起来像被服务器踢了，其实什么都没发生。
 */
public class FakeKickScreen extends Screen {

    private static final String[] REASONS = {
            "你在错误的时间呼吸了",
            "服务器觉得你太菜了",
            "检测到客户端使用了「手」",
            "你的键盘敲得太响了",
            "管理员今天心情不好",
            "服务器需要一个人来背锅",
            "你已被列入「疑似太强」名单",
            "你的运气值低于服务器最低标准"
    };

    private final String reason = REASONS[new java.util.Random().nextInt(REASONS.length)];

    public FakeKickScreen() {
        super(Text.literal("连接中断"));
    }

    @Override
    protected void init() {
        int y = height / 2 + 40;
        addDrawableChild(ButtonWidget.builder(Text.literal("§7重新连接"), b -> {
            if (client != null) {
                client.setScreen(null);
            }
            ClientGlitchManager.clear("fakekick");
        }).dimensions(width / 2 - 154, y, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("§c回到标题界面"), b -> {
            if (client != null) {
                client.setScreen(new TitleScreen());
            }
        }).dimensions(width / 2 + 4, y, 150, 20).build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx);
        ctx.fill(0, 0, width, height, 0xC0000000);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("§c§l连接已中断"), width / 2, height / 2 - 60, 0xFFFFFF);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("§7你被踢出了游戏"), width / 2, height / 2 - 40, 0xAAAAAA);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("§f原因：§e" + reason), width / 2, height / 2 - 20, 0xFFFFFF);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("§8（其实你还在游戏里，别慌）"), width / 2, height / 2 + 10, 0x777777);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
