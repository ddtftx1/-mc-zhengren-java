package com.zhengren.trollmod.net;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * 服务端 -&gt; 客户端 的整活指令包。
 * 只有装了本模组的客户端才收得到；收不到也不影响服务端其它整人事件。
 */
public record GlitchPayload(String glitch, int durationTicks, int power) implements FabricPacket {

    public static final PacketType<GlitchPayload> TYPE = PacketType.create(
            new Identifier("zhengren", "glitch"), GlitchPayload::read);

    private static GlitchPayload read(PacketByteBuf buf) {
        return new GlitchPayload(buf.readString(64), buf.readVarInt(), buf.readVarInt());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(glitch, 64);
        buf.writeVarInt(durationTicks);
        buf.writeVarInt(power);
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    public static void register() {
        // Fabric API 0.92.x（MC 1.20.1）没有 PacketTypeRegistry / PayloadTypeRegistry：
        // S2C 通道由客户端 ClientPlayNetworking.registerGlobalReceiver(TYPE, handler) 完成注册，
        // 服务端用 ServerPlayNetworking.send(player, FabricPacket) 直接发送，无需显式注册类型。
    }

    public static void send(ServerPlayerEntity player, String glitch, int durationTicks, int power) {
        try {
            ServerPlayNetworking.send(player, new GlitchPayload(glitch, durationTicks, power));
        } catch (Throwable ignored) {
        }
    }
}
