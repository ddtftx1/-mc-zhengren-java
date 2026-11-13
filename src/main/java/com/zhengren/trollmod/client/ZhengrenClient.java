package com.zhengren.trollmod.client;

import com.zhengren.trollmod.net.GlitchPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * 客户端入口：接收整活包并把画面搞乱。
 */
public class ZhengrenClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(GlitchPayload.TYPE,
                (packet, player, sender) -> {
                    net.minecraft.client.MinecraftClient client = net.minecraft.client.MinecraftClient.getInstance();
                    client.execute(() -> ClientGlitchManager.trigger(packet.glitch(),
                            packet.durationTicks(), packet.power()));
                });

        ClientTickEvents.END_CLIENT_TICK.register(ClientGlitchManager::tick);

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ClientGlitchManager.reset(client));
    }
}
