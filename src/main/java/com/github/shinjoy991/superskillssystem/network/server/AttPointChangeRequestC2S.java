package com.github.shinjoy991.superskillssystem.network.server;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.PlayerInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AttPointChangeRequestC2S {

    private final int type;
    private final int amount;

    public AttPointChangeRequestC2S(int type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    // ENCODE: ghi dữ liệu vào buffer
    public static void encode(AttPointChangeRequestC2S packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.type);
        buffer.writeInt(packet.amount);
    }

    // DECODE: đọc dữ liệu từ buffer
    public static AttPointChangeRequestC2S decode(FriendlyByteBuf buffer) {
        int type = buffer.readInt();
        int amount = buffer.readInt();
        return new AttPointChangeRequestC2S(type, amount);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                PlayerInfo playerInfo = AllPlayersInfo.get(player.getUUID());
                    if (amount == 0) return;
                    if (type < 0 || type > 4) {
                        // Invalid type, do nothing or send an error message
                        return;
                    }
                    int verifiedAmount = amount;
                    if (amount > 0) {
                        // Kiểm tra xem có đủ điểm để cộng không
                        if (playerInfo.getTotalAttPoint() - playerInfo.getUsedAttPoint() < amount) {
                            verifiedAmount = playerInfo.getTotalAttPoint() - playerInfo.getUsedAttPoint();
                        }
                    } else { // amount < 0
                        // Kiểm tra xem có đủ điểm để trừ không
                        int pointType = 0;
                        switch (type) {
                            case 0 -> pointType = playerInfo.getStrPoint();
                            case 1 -> pointType = playerInfo.getVitPoint();
                            case 2 -> pointType = playerInfo.getAgiPoint();
                            case 3 -> pointType = playerInfo.getIntPoint();
                            case 4 -> pointType = playerInfo.getPerPoint();
                        }
                        if (pointType < -amount) {
                            verifiedAmount = -pointType; // Giới hạn số điểm trừ không vượt quá điểm hiện tại
                        }
                    }
                    int actualAmount = verifiedAmount;
                        if (type == 0) { // Str
                            playerInfo.addStrPoint(actualAmount);
                        } else if (type == 1) { // Vit
                            playerInfo.addVitPoint(actualAmount);
                        } else if (type == 2) { // Agi
                            playerInfo.addAgiPoint(actualAmount);
                        } else if (type == 3) { // Int
                            playerInfo.addIntPoint(actualAmount);
                        } else if (type == 4) { // Per
                            playerInfo.addPerPoint(actualAmount);
                        }
                        playerInfo.addUsedAttPoint(actualAmount);
            }
        });
        context.get().setPacketHandled(true);
    }
}
