package com.github.shinjoy991.superskillssystem.network.server;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.client.InfoChangeUpdateS2C;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestUpdateInfoC2S {

    public RequestUpdateInfoC2S() {
    }

    public static void encode(RequestUpdateInfoC2S packet, FriendlyByteBuf buffer) {

    }

    public static RequestUpdateInfoC2S decode(FriendlyByteBuf buffer) {
        return new RequestUpdateInfoC2S();
    }

    public static void handle(RequestUpdateInfoC2S packet,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player instanceof ServerPlayer) {
                CompoundTag sendTag = AllPlayersInfo.get(player.getUUID()).saveToNBT();
//                System.out.println("Atk in server: " + sendTag.getFloat("atk_dmg"));
                ModNetworking.INSTANCE.sendTo(new InfoChangeUpdateS2C(sendTag), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
        });
        context.setPacketHandled(true);
    }
}
