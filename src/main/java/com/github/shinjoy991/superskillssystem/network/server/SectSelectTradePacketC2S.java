package com.github.shinjoy991.superskillssystem.network.server;

import com.github.shinjoy991.superskillssystem.gui.menu.SectVillagerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SectSelectTradePacketC2S {
    private final int item;

    public SectSelectTradePacketC2S(int item) {
        this.item = item;
    }

    public int getItem() {
        return item;
    }

    public static void encode(SectSelectTradePacketC2S packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.item);
    }

    public static SectSelectTradePacketC2S decode(FriendlyByteBuf buffer) {
        int item = buffer.readVarInt();
        return new SectSelectTradePacketC2S(item);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                int i = this.getItem();
                AbstractContainerMenu abstractcontainermenu = player.containerMenu;
                if (abstractcontainermenu instanceof SectVillagerMenu sectVillagerMenu) {
                    if (!sectVillagerMenu.stillValid(player)) {
                        return;
                    }

                    sectVillagerMenu.setSelectionHint(i);
                    sectVillagerMenu.tryMoveItems(i);
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
