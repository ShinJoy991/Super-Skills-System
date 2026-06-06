package com.github.shinjoy991.superskillssystem.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NotifyC2S {

    public NotifyC2S() {

    }

    public static void encode(NotifyC2S packet, FriendlyByteBuf buffer) {
    }

    public static NotifyC2S decode(FriendlyByteBuf buffer) {
        return new NotifyC2S();
    }

    public static void handle(NotifyC2S packet,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            System.out.println("Received Notify packet");
            ServerPlayer player = context.getSender();
            if (player != null) {
                System.out.println("Player Notify from: " + player.getName().getString());
            }
        });
        context.setPacketHandled(true);
    }
}