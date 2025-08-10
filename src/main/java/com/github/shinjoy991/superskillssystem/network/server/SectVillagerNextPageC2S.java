package com.github.shinjoy991.superskillssystem.network.server;

import com.github.shinjoy991.superskillssystem.gui.menu.SectVillagerMenu;
import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.PlayerInfo;
import com.github.shinjoy991.superskillssystem.helpers.SterilizeTags;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.client.InfoChangeUpdateS2C;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SectVillagerNextPageC2S {

    private final boolean goToNext;

    public SectVillagerNextPageC2S(boolean goToNext) {
        this.goToNext = goToNext;
    }

    // ENCODE: ghi dữ liệu vào buffer
    public static void encode(SectVillagerNextPageC2S packet, FriendlyByteBuf buffer) {
        buffer.writeBoolean(packet.goToNext);
    }

    // DECODE: đọc dữ liệu từ buffer
    public static SectVillagerNextPageC2S decode(FriendlyByteBuf buffer) {
        boolean goNext = buffer.readBoolean();
        return new SectVillagerNextPageC2S(goNext);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                if (this.goToNext) {
                    if (player.containerMenu instanceof SectVillagerMenu menu) {
                        // Ví dụ: tăng category lên 1
                        int newCategory = (menu.getSelectedCategory() + 1) % menu.getMaxCategories();
                        menu.setSelectedCategory(newCategory);

                        // Cập nhật cho client biết danh sách mới
                        menu.broadcastChanges();
                    }
                }
                else {
                    if (player.containerMenu instanceof SectVillagerMenu menu) {
                        // Ví dụ: giảm category xuống 1
                        int newCategory = (menu.getSelectedCategory() - 1 + menu.getMaxCategories()) % menu.getMaxCategories();
                        // Đảm bảo không âm
                        if (newCategory < 0) {
                            newCategory = 0;
                        }
                        menu.setSelectedCategory(newCategory);

                        // Cập nhật cho client biết danh sách mới
                        menu.broadcastChanges();
                    }
                }
            }
        });
        context.get().setPacketHandled(true);
    }
}
