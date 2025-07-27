package com.github.shinjoy991.superskillssystem.network.client.handler;


import com.github.shinjoy991.superskillssystem.gui.screen.PlayerInfoScreen;
import com.github.shinjoy991.superskillssystem.helpers.PlayerDataScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;

public class PlayerDataResponePacketHandler {

    public static void ShowDataScreen(CompoundTag tag) {
        PlayerDataScreen dataScreen = new PlayerDataScreen(tag);
        Minecraft.getInstance().setScreen(
                new PlayerInfoScreen(dataScreen));
    }
}
