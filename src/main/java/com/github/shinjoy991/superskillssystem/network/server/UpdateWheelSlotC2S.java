package com.github.shinjoy991.superskillssystem.network.server;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.PlayerInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateWheelSlotC2S {

    private final CompoundTag receiveTag;

    public UpdateWheelSlotC2S(CompoundTag receiveTag) {
        this.receiveTag = receiveTag;
    }
    public static void encode(UpdateWheelSlotC2S packet, FriendlyByteBuf buffer) {
        buffer.writeNbt(packet.receiveTag);
    }

    public static UpdateWheelSlotC2S decode(FriendlyByteBuf buffer) {
        CompoundTag tag = buffer.readNbt();
        return new UpdateWheelSlotC2S(tag);
    }

    public static void handle(UpdateWheelSlotC2S packet,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
//            System.out.println("Received UpdateWheelSlotC2S packet");
            ServerPlayer player = context.getSender();
            if (player != null) {
                AllPlayersInfo.get(player.getUUID()).setWheelSlots(packet.receiveTag);
//                System.out.println("Updated wheel slots for player with tag: " + packet.receiveTag);
            }
        });
        context.setPacketHandled(true);
    }
}