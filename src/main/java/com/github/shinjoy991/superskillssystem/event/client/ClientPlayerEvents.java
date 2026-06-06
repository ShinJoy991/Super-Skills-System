//package com.github.shinjoy991.superskillssystem.event.client;
//
//import com.github.shinjoy991.superskillssystem.SSS;
//import com.github.shinjoy991.superskillssystem.helpers.PlayerClientData;
//import com.github.shinjoy991.superskillssystem.network.ModNetworking;
//import com.github.shinjoy991.superskillssystem.network.server.UpdateWheelSlotC2S;
//import net.minecraft.client.Minecraft;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
//import net.minecraftforge.client.event.ScreenEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = SSS.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
//public class ClientPlayerEvents {
//    @Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
//    public class ClientEvents {
//        @SubscribeEvent
//        public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
//            if (event.getPlayer() == null) return;
//
//            CompoundTag tag = PlayerClientData.getWheelSlotTag();
//
//            ModNetworking.INSTANCE.sendToServer(new UpdateWheelSlotC2S(tag));
//        }
//    }
//    @SubscribeEvent
//    public static void onClientLogout(ClientPlayerNetworkEvent.LoggingOut event) {
//        System.out.println("Client player logging out");
//        CompoundTag tag = PlayerClientData.getWheelSlotTag();
//        if (tag.isEmpty()) return;
//        Minecraft.getInstance().execute(() -> {
//            if (Minecraft.getInstance().getConnection() != null) {
//                ModNetworking.INSTANCE.sendToServer(new UpdateWheelSlotC2S(tag));
//            }
//        });
//    }
//    @SubscribeEvent
//    public static void onScreenChange(ScreenEvent.Closing event) {
//
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.player == null) return;
//        if (mc.getConnection() == null) return;
//
//        // chỉ gửi khi đang rời world/server screen
//        if (!(event.getScreen() instanceof net.minecraft.client.gui.screens.ConnectScreen)) return;
//
//        CompoundTag tag = PlayerClientData.getWheelSlotTag();
//        if (tag.isEmpty()) return;
//
//        ModNetworking.INSTANCE.sendToServer(new UpdateWheelSlotC2S(tag));
//
//        System.out.println("Sent ONCE via screen closing");
//    }
//}
