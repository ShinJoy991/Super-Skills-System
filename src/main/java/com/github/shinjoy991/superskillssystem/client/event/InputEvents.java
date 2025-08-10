package com.github.shinjoy991.superskillssystem.client.event;

import com.github.shinjoy991.superskillssystem.client.init.ClientKeyMapping;
import com.github.shinjoy991.superskillssystem.gui.screen.PlayerInfoScreen;
import com.github.shinjoy991.superskillssystem.network.client.PlayerDataClientInit;
import com.github.shinjoy991.superskillssystem.network.server.RequestUpdateInfoC2S;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.server.PlayerDataC2S;
import net.minecraftforge.network.NetworkDirection;

@Mod.EventBusSubscriber(modid = SSS.MODID, value = Dist.CLIENT)
public class InputEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (ClientKeyMapping.PLAYER_INFO_KEY.consumeClick()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                ModNetworking.INSTANCE.sendToServer(new RequestUpdateInfoC2S());
                Minecraft.getInstance().setScreen(
                        new PlayerInfoScreen());
            }
        }
    }

    @Mod.EventBusSubscriber(modid = SSS.MODID, value = Dist.CLIENT, bus =
            Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void clientSetup(RegisterKeyMappingsEvent event) {
            event.register(ClientKeyMapping.PLAYER_INFO_KEY);
        }
    }
}