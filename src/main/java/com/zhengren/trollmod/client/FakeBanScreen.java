package com.zhengren.trollmod.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.Random;

/**
 * 假封禁界面：看起来像被服务器永久封禁了，还贴心地给了申诉码。
 */
public class FakeBanScreen extends Screen {

    private static final String[] REASONS = {
            "使用外挂（其实没有）",
            "恶意可爱",
            "破坏其他玩家的心态",
            "在被整的时候叫得太大",
            "疑似使用了「双手」进行游戏",
            "账号数据过于正常，疑似机器人",
            "对模组作者不敬",
            "在错误的时间打开了背包"
    };

    private final String reason = REASONS[new Random().nextInt(REASONS.length)];
    private final String appealCode = "ZR-" + Integer.toHexString(new Random().nextInt(0xFFFFFF)).toUpperCase()
            + "-" + Integer.toHexString(new Random().nextInt(0xFFFF)).toUpperCase();

    public FakeBanScreen() {
        super(Text.literal("你已被封禁"));
    }

    @Override
    protected void init() {
        int y = height - 52;
        addDrawableChild(ButtonWidget.builder(Text.literal("§c退出游戏"), b -> {
            if (client != null) {
                client.scheduleStop();
            }
        }).dimensions(width / 2 - 154, y, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("§7申诉（没用）"), b -> {
            if (client != null) {
                client.setScreen(null);
            }
            ClientGlitchManager.clear("fake_ban");
        }).dimensions(width / 2 + 4, y, 150, 20).build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(0, 0, width, height, 0xE0100000);
        int cx = width / 2;
        int y = Math.max(30, height / 2 - 80);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("§4§l你已被永久封禁"), cx, y, 0xFFFFFF);
        y += 30;
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("§7封禁理由："), cx, y, 0xAAAAAA);
        y += 14;
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("§f" + reason), cx, y, 0xFFFFFF);
        y += 24;
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal("§7封禁时长：§c永久 §8(解除时间：永远不会)"), cx, y, 0xFFFFFF);
        y += 14;
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal("§7申诉码：§e" + appealCode), cx, y, 0xFFFFFF);
        y += 14;
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal("§7申诉地址：§8https://example.invalid/appeal"), cx, y, 0x888888);
        y += 26;
        ctx.drawCenteredTextWithShadow(textRenderer,
                Text.literal("§8（别慌，你其实还在游戏里。真的。）"), cx, y, 0x777777);
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
