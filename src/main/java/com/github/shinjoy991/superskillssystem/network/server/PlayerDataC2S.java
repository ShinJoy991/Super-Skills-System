package com.github.shinjoy991.superskillssystem.network.server;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.network.client.PlayerDataCRespone;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import com.github.shinjoy991.superskillssystem.network.ModNetworking;

import java.util.function.Supplier;

public class PlayerDataC2S {

    public PlayerDataC2S() {
    }
    public static void encode(PlayerDataC2S packet, FriendlyByteBuf buffer) {

    }

    public static PlayerDataC2S decode(FriendlyByteBuf buffer) {
        return new PlayerDataC2S();
    }

    public static void handle(PlayerDataC2S packet,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player instanceof ServerPlayer) {
//                System.out.println("Received PlayerDataS2C packet on server side for player: " + player.getName().getString());
//                System.out.println(AllPlayersInfo.get(player.getUUID()).toString());
                CompoundTag sendTag = AllPlayersInfo.get(player.getUUID()).saveToNBT();
                ModNetworking.INSTANCE.sendTo(new PlayerDataCRespone(sendTag), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
        });
        context.setPacketHandled(true);
    }
}