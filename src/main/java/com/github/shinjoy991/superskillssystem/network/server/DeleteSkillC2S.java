package com.github.shinjoy991.superskillssystem.network.server;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.PlayerInfo;
import com.github.shinjoy991.superskillssystem.helpers.saveddata.PlayerSkillSavedData;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkillInstance;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.client.InfoChangeUpdateS2C;
import com.github.shinjoy991.superskillssystem.network.client.PlayerDataClientInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill.sterilizeGlobalPassiveSkillsList;

public class DeleteSkillC2S {

    private final String skillName;
    private int type;

    public DeleteSkillC2S(String skillName, int type) {
        this.skillName = skillName;
        this.type = type;
    }
    public static void encode(DeleteSkillC2S packet, FriendlyByteBuf buffer) {
        buffer.writeUtf(packet.skillName);
        buffer.writeInt(packet.type);
    }

    public static DeleteSkillC2S decode(FriendlyByteBuf buffer) {
        String skillName = buffer.readUtf();
        int type = buffer.readInt();
        return new DeleteSkillC2S(skillName, type);
    }

    public static void handle(DeleteSkillC2S packet,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
//            System.out.println("Received DeleteSkillC2S packet for skill: " + packet.skillName);
            ServerPlayer player = context.getSender();
            if (player != null) {
                PlayerInfo playerInfo = AllPlayersInfo.get(player.getUUID());
//                System.out.println("server receive request: " + packet.skillName);
                boolean success;
                if (packet.type == 0) {
                    System.out.println("Deleting passive skill server");
                    success = playerInfo.removePassiveSkill(packet.skillName);
                } else {
                    System.out.println("Deleting active skill server");
                    success = playerInfo.removeActiveSkill(packet.skillName);
                }
//                System.out.println("Adding passive skill success server");
                if (success) {
                    System.out.println("server response: remove skill success");
//                    CompoundTag sendTag = AllPlayersInfo.get(player.getUUID()).saveToNBT();
//                    ModNetworking.INSTANCE.sendTo(new InfoChangeUpdateS2C(sendTag), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                }
            }
        });
        context.setPacketHandled(true);
    }
}