package com.github.shinjoy991.superskillssystem.network.client;

import com.github.shinjoy991.superskillssystem.helpers.PlayerClientData;
import com.github.shinjoy991.superskillssystem.helpers.SterilizeTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerDataClientInit {
    private final CompoundTag tag;
    private final CompoundTag passiveSkillTag;
    private final CompoundTag skillSlotsTag;


    public PlayerDataClientInit(CompoundTag tag, CompoundTag skillTag, CompoundTag skillSlotsTag) {
        this.tag = tag;
        this.passiveSkillTag = skillTag;
        this.skillSlotsTag = skillSlotsTag;
    }

    public PlayerDataClientInit(FriendlyByteBuf buffer) {
        this.tag = buffer.readNbt();
        this.passiveSkillTag = buffer.readNbt();
        this.skillSlotsTag = buffer.readNbt();
    }

    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.tag);
        buffer.writeNbt(this.passiveSkillTag);
        buffer.writeNbt(this.skillSlotsTag);
    }

    public static void handle(PlayerDataClientInit packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist.isClient()) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
                    new PlayerClientData(player, packet.tag, packet.passiveSkillTag, packet.skillSlotsTag);
                    System.out.println("Player data initialized on client");
                }
            }
        });
        context.setPacketHandled(true);
    }
}


