package com.github.shinjoy991.superskillssystem.network.client;

import com.github.shinjoy991.superskillssystem.helpers.PlayerDataScreen;
import com.github.shinjoy991.superskillssystem.network.client.handler.PlayerDataResponePacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerDataCRespone {
    private final CompoundTag tag;

    public PlayerDataCRespone(CompoundTag tag) {
        this.tag = tag;
    }

    public PlayerDataCRespone(FriendlyByteBuf buffer) {
        this.tag = buffer.readNbt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.tag);
    }

    public static void handle(PlayerDataCRespone packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist.isClient()) {
//                System.out.println("Received PlayerDataCRespone packet on client side.");
                PlayerDataResponePacketHandler.ShowDataScreen(packet.tag);
            }
        });
        context.setPacketHandled(true);
    }
}


