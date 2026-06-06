package com.github.shinjoy991.superskillssystem.network.server;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.PlayerInfo;
import com.github.shinjoy991.superskillssystem.helpers.SterilizeTags;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.client.InfoChangeUpdateS2C;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static com.github.shinjoy991.superskillssystem.event.server.PlayerEvents.refreshPlayerHealth;

public class AttPointChangeRequestC2S {


    public static final int RESET_ALL = 999;
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
                if (type == RESET_ALL) {
                    if (playerInfo.getUsedAttPoint() <= 0) {
                        return;
                    }
                    // check diamond
                    boolean foundDiamond = false;

                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack stack = player.getInventory().getItem(i);

                        if (stack.is(Items.DIAMOND)) {
                            stack.shrink(1);
//                            player.getInventory().setChanged();
//                            player.containerMenu.broadcastChanges();
                            foundDiamond = true;
                            break;
                        }
                    }

                    if (!foundDiamond) {
                        return;
                    }

                    playerInfo.setStrPoint(0);
                    playerInfo.setVitPoint(0);
                    playerInfo.setAgiPoint(0);
                    playerInfo.setIntPoint(0);
                    playerInfo.setPerPoint(0);

                    playerInfo.setUsedAttPoint(0);

                    CompoundTag sendTag =
                            AllPlayersInfo.get(player.getUUID()).saveToNBT();
                    refreshPlayerHealth(player);
                    ModNetworking.INSTANCE.sendTo(
                            new InfoChangeUpdateS2C(sendTag),
                            player.connection.connection,
                            NetworkDirection.PLAY_TO_CLIENT
                    );

                    return;
                }
                    if (amount == 0) return;
                    if (type < 0 || type > 4) {
                        // Invalid type, do nothing or send an error message
                        return;
                    }
                    int verifiedAmount = amount;
//                    System.out.println("Server received request");
                    if (amount > 0) {
                        // Kiểm tra xem có đủ điểm để cộng không
//                        System.out.println("A1");
                        if (playerInfo.getTotalAttPoint() - playerInfo.getUsedAttPoint() < amount) {
//                            System.out.println("A2");
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
                    switch (type) {
                        case 0 -> {
                            CompoundTag sendTag = new CompoundTag();
                            sendTag.putInt(SterilizeTags.STR_POINT.name(), playerInfo.getStrPoint());
                            sendTag.putInt(SterilizeTags.TOTAL_STR.name(), playerInfo.getTotalStr());
                            sendTag.putInt(SterilizeTags.USED_ATT_POINT.name(), playerInfo.getUsedAttPoint());
                            sendTag.putFloat(SterilizeTags.ATK_DMG.name(), playerInfo.getAtkDmg());
                            sendTag.putFloat(SterilizeTags.DEF.name(), playerInfo.getDef());
                            sendTag.putFloat(SterilizeTags.DEF_PEN.name(), playerInfo.getDefPen());
                            sendTag.putFloat(SterilizeTags.ATTACK_SPEED.name(), playerInfo.getAttackSpeed());
                            sendTag.putFloat(SterilizeTags.DMG_RED.name(), playerInfo.getDmgRed());
                            sendTag.putFloat(SterilizeTags.ACCURACY.name(), playerInfo.getAccuracy());
                            ModNetworking.INSTANCE.sendTo(new InfoChangeUpdateS2C(sendTag), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                        }
                        case 1 -> {
                            CompoundTag sendTag = new CompoundTag();
                            sendTag.putInt(SterilizeTags.VIT_POINT.name(), playerInfo.getVitPoint());
                            sendTag.putInt(SterilizeTags.TOTAL_VIT.name(), playerInfo.getTotalVit());
                            sendTag.putInt(SterilizeTags.USED_ATT_POINT.name(), playerInfo.getUsedAttPoint());
                            sendTag.putFloat(SterilizeTags.DEF.name(), playerInfo.getDef());
                            sendTag.putFloat(SterilizeTags.MAGIC_DEF.name(), playerInfo.getMagDef());
                            sendTag.putFloat(SterilizeTags.MAGIC_RESIST.name(), playerInfo.getMagResist());
                            sendTag.putFloat(SterilizeTags.DMG_RED.name(), playerInfo.getDmgRed());
                            sendTag.putFloat(SterilizeTags.RESISTANCE.name(), playerInfo.getResistance());
                            sendTag.putFloat(SterilizeTags.HEAL_REGEN.name(), playerInfo.getHealRegen());
                            ModNetworking.INSTANCE.sendTo(new InfoChangeUpdateS2C(sendTag), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                        }
                        case 2 -> {
                            CompoundTag sendTag = new CompoundTag();
                            sendTag.putInt(SterilizeTags.AGI_POINT.name(), playerInfo.getAgiPoint());
                            sendTag.putInt(SterilizeTags.TOTAL_AGI.name(), playerInfo.getTotalAgi());
                            sendTag.putInt(SterilizeTags.USED_ATT_POINT.name(), playerInfo.getUsedAttPoint());
                            sendTag.putFloat(SterilizeTags.ATK_DMG.name(), playerInfo.getAtkDmg());
                            sendTag.putFloat(SterilizeTags.RANGE_DMG.name(), playerInfo.getRangeDmg());
                            sendTag.putFloat(SterilizeTags.ATTACK_SPEED.name(), playerInfo.getAttackSpeed());
                            sendTag.putFloat(SterilizeTags.ACCURACY.name(), playerInfo.getAccuracy());
                            sendTag.putFloat(SterilizeTags.SPEED.name(), playerInfo.getSpeed());
                            sendTag.putFloat(SterilizeTags.EVASION.name(), playerInfo.getEvasion());
                            ModNetworking.INSTANCE.sendTo(new InfoChangeUpdateS2C(sendTag), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                        }
                        case 3 -> {
                            CompoundTag sendTag = new CompoundTag();
                            sendTag.putInt(SterilizeTags.INT_POINT.name(), playerInfo.getIntPoint());
                            sendTag.putInt(SterilizeTags.TOTAL_INT.name(), playerInfo.getTotalInt());
                            sendTag.putInt(SterilizeTags.USED_ATT_POINT.name(), playerInfo.getUsedAttPoint());
                            sendTag.putFloat(SterilizeTags.MAGIC_DMG.name(), playerInfo.getMagicDmg());
                            sendTag.putFloat(SterilizeTags.MAGIC_DEF.name(), playerInfo.getMagDef());
                            sendTag.putFloat(SterilizeTags.MAGIC_PEN.name(), playerInfo.getMagicPen());
                            sendTag.putFloat(SterilizeTags.MAGIC_RESIST.name(), playerInfo.getMagResist());
                            sendTag.putFloat(SterilizeTags.RESISTANCE.name(), playerInfo.getResistance());
                            sendTag.putFloat(SterilizeTags.MAX_MANA.name(), playerInfo.getMaxMana());
                            ModNetworking.INSTANCE.sendTo(new InfoChangeUpdateS2C(sendTag), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                        }
                        case 4 -> {
                            CompoundTag sendTag = new CompoundTag();
                            sendTag.putInt(SterilizeTags.PER_POINT.name(), playerInfo.getPerPoint());
                            sendTag.putInt(SterilizeTags.TOTAL_PER.name(), playerInfo.getTotalPer());
                            sendTag.putInt(SterilizeTags.USED_ATT_POINT.name(), playerInfo.getUsedAttPoint());
                            sendTag.putFloat(SterilizeTags.MAGIC_DMG.name(), playerInfo.getMagicDmg());
                            sendTag.putFloat(SterilizeTags.DEF.name(), playerInfo.getDef());
                            sendTag.putFloat(SterilizeTags.MAGIC_DEF.name(), playerInfo.getMagDef());
                            sendTag.putFloat(SterilizeTags.MAGIC_RESIST.name(), playerInfo.getMagResist());
                            sendTag.putFloat(SterilizeTags.SPEED.name(), playerInfo.getSpeed());
                            sendTag.putFloat(SterilizeTags.DMG_RED.name(), playerInfo.getDmgRed());
                            sendTag.putFloat(SterilizeTags.RESISTANCE.name(), playerInfo.getResistance());
                            sendTag.putFloat(SterilizeTags.EVASION.name(), playerInfo.getEvasion());
                            sendTag.putFloat(SterilizeTags.MANA_REGEN.name(), playerInfo.getManaRegen());
                            sendTag.putFloat(SterilizeTags.MAX_MANA.name(), playerInfo.getMaxMana());
                            ModNetworking.INSTANCE.sendTo(new InfoChangeUpdateS2C(sendTag), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                        }

                    }
            }
        });
        context.get().setPacketHandled(true);
    }
}
