package com.github.shinjoy991.superskillssystem.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.github.shinjoy991.superskillssystem.helpers.PlayerClientData.updateClientData;

public class InfoChangeUpdateS2C {
    private final CompoundTag tag;

    public InfoChangeUpdateS2C(CompoundTag tag) {
        this.tag = tag;
    }

    public InfoChangeUpdateS2C(FriendlyByteBuf buffer) {
        this.tag = buffer.readNbt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.tag);
    }

    public static void handle(InfoChangeUpdateS2C packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist.isClient()) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
//                    player.sendSystemMessage(Component.literal("Received info update packet: " + packet.tag));
                    updateClientData(packet.tag);
                }
            }
        });
        context.setPacketHandled(true);
    }
}