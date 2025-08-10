package com.github.shinjoy991.superskillssystem.network.client;

import com.github.shinjoy991.superskillssystem.gui.menu.SectVillagerMenu;
import com.github.shinjoy991.superskillssystem.helpers.skill.SectTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SectVillagerOffersS2C {
    private final int containerId;
    private final MerchantOffers offers;
    private final SectTypes sectTypes;

    public SectVillagerOffersS2C(int asInt, MerchantOffers merchantoffers, SectTypes sectTypes) {
        this.containerId = asInt;
        this.offers = merchantoffers;
        this.sectTypes = sectTypes;
    }
    public SectVillagerOffersS2C(FriendlyByteBuf buf) {
        this.containerId = buf.readVarInt();
        this.offers = MerchantOffers.createFromStream(buf);
        this.sectTypes = buf.readEnum(SectTypes.class); // Đọc enum
    }
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.containerId);
        this.offers.writeToStream(buffer);
        buffer.writeEnum(this.sectTypes); // Ghi enum
    }
    public int getContainerId() {
        return this.containerId;
    }

    public MerchantOffers getOffers() {
        return this.offers;
    }

    public static void handle(SectVillagerOffersS2C packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist.isClient()) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
                    AbstractContainerMenu abstractcontainermenu = player.containerMenu;
                    if (packet.containerId == abstractcontainermenu.containerId && abstractcontainermenu instanceof SectVillagerMenu sectMenu) {
                        sectMenu.setOffers(new MerchantOffers(packet.offers.createTag()));
                        sectMenu.setSectType(packet.sectTypes);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}