package com.github.shinjoy991.superskillssystem.network.server;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.PlayerInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ActiveSkillSlotsC2S {

    public final String slot1;
    public final String slot2;
    public final String slot3;
    public final String slot4;

    public ActiveSkillSlotsC2S(String s1, String s2, String s3, String s4) {
        this.slot1 = s1;
        this.slot2 = s2;
        this.slot3 = s3;
        this.slot4 = s4;
    }

    public static void encode(ActiveSkillSlotsC2S packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.slot1 == null ? "" : packet.slot1);
        buffer.writeUtf(packet.slot2 == null ? "" : packet.slot2);
        buffer.writeUtf(packet.slot3 == null ? "" : packet.slot3);
        buffer.writeUtf(packet.slot4 == null ? "" : packet.slot4);
    }

    public static ActiveSkillSlotsC2S decode(FriendlyByteBuf buffer) {
        String s1 = buffer.readUtf();
        String s2 = buffer.readUtf();
        String s3 = buffer.readUtf();
        String s4 = buffer.readUtf();
        return new ActiveSkillSlotsC2S(s1.isEmpty() ? null : s1, s2.isEmpty() ? null : s2, s3.isEmpty() ? null : s3, s4.isEmpty() ? null : s4);
    }

    public static void handle(ActiveSkillSlotsC2S packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

//            PlayerInfo info = AllPlayersInfo.get(player.getUUID());
//            // Update in-memory
//            try {
//                info.setActiveSkillSlot(1, packet.slot1 == null ? null : ResourceLocation.tryParse(packet.slot1));
//                info.setActiveSkillSlot(2, packet.slot2 == null ? null : ResourceLocation.tryParse(packet.slot2));
//                info.setActiveSkillSlot(3, packet.slot3 == null ? null : ResourceLocation.tryParse(packet.slot3));
//                info.setActiveSkillSlot(4, packet.slot4 == null ? null : ResourceLocation.tryParse(packet.slot4));
//            } catch (Exception e) {
//                player.sendSystemMessage(Component.literal("Failed to update active skill slots"));
//            }
//
//            // Persist to saved data
//            net.minecraft.server.MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
//            net.minecraft.server.level.ServerLevel level = server.overworld();
//            PlayerActiveSkillSavedData saved = PlayerActiveSkillSavedData.get(level);
//            UUID id = player.getUUID();
//            // Build compound tag for persistence
//            net.minecraft.nbt.CompoundTag t = saved.getPlayerData(id);
//            if (t == null) t = new net.minecraft.nbt.CompoundTag();
//            if (packet.slot1 != null) t.putString("ActiveSkillSlot1", packet.slot1); else t.remove("ActiveSkillSlot1");
//            if (packet.slot2 != null) t.putString("ActiveSkillSlot2", packet.slot2); else t.remove("ActiveSkillSlot2");
//            if (packet.slot3 != null) t.putString("ActiveSkillSlot3", packet.slot3); else t.remove("ActiveSkillSlot3");
//            if (packet.slot4 != null) t.putString("ActiveSkillSlot4", packet.slot4); else t.remove("ActiveSkillSlot4");
//            saved.setPlayerData(id, t);

        });
        context.setPacketHandled(true);
    }

}



