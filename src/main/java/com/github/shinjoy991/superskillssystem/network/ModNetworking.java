package com.github.shinjoy991.superskillssystem.network;

import com.github.shinjoy991.superskillssystem.network.client.InfoChangeUpdateS2C;
import com.github.shinjoy991.superskillssystem.network.client.SectVillagerOffersS2C;
import com.github.shinjoy991.superskillssystem.network.server.*;
import com.github.shinjoy991.superskillssystem.network.client.PlayerDataClientInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import com.github.shinjoy991.superskillssystem.SSS;


public class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(SSS.MODID, "main"), () -> "1", "1"::equals, "1"::equals);
    // encode, decode = C2S
    // toByte = S2C
    public static void registerPackets() {
        INSTANCE.registerMessage(0, PlayerDataC2S.class, PlayerDataC2S::encode, PlayerDataC2S::decode, PlayerDataC2S::handle);
        INSTANCE.registerMessage(1, PlayerDataClientInit.class, PlayerDataClientInit::toBytes, PlayerDataClientInit::new, PlayerDataClientInit::handle);
        INSTANCE.registerMessage(2, AttPointChangeRequestC2S.class, AttPointChangeRequestC2S::encode, AttPointChangeRequestC2S::decode, AttPointChangeRequestC2S::handle);
        INSTANCE.registerMessage(3, InfoChangeUpdateS2C.class, InfoChangeUpdateS2C::toBytes, InfoChangeUpdateS2C::new, InfoChangeUpdateS2C::handle);
        INSTANCE.registerMessage(4, RequestUpdateInfoC2S.class, RequestUpdateInfoC2S::encode, RequestUpdateInfoC2S::decode, RequestUpdateInfoC2S::handle);
        INSTANCE.registerMessage(5, SectVillagerNextPageC2S.class, SectVillagerNextPageC2S::encode, SectVillagerNextPageC2S::decode, SectVillagerNextPageC2S::handle);
        INSTANCE.registerMessage(6, SectVillagerOffersS2C.class, SectVillagerOffersS2C::toBytes, SectVillagerOffersS2C::new, SectVillagerOffersS2C::handle);
        INSTANCE.registerMessage(7, SectSelectTradePacketC2S.class, SectSelectTradePacketC2S::encode, SectSelectTradePacketC2S::decode, SectSelectTradePacketC2S::handle);



//        INSTANCE.registerMessage(3, ShootActionPacket.class, ShootActionPacket::encode, ShootActionPacket::decode, ShootActionPacket::handle);
//        INSTANCE.registerMessage(4, RightClickBlockGodModPacket.class, RightClickBlockGodModPacket::encode, RightClickBlockGodModPacket::decode, RightClickBlockGodModPacket::handle);
//        INSTANCE.registerMessage(5, LeftClickEmptyGodPacket.class, LeftClickEmptyGodPacket::encode, LeftClickEmptyGodPacket::decode, LeftClickEmptyGodPacket::handle);
//        INSTANCE.registerMessage(6, RightClickMushPunchPacket.class, RightClickMushPunchPacket::encode, RightClickMushPunchPacket::decode, RightClickMushPunchPacket::handle);
//        INSTANCE.registerMessage(7, ResponseFovModifierPacket.class, ResponseFovModifierPacket::toBytes, ResponseFovModifierPacket::new, ResponseFovModifierPacket::handle);
//        INSTANCE.registerMessage(8, ExplosionGodPacket.class, ExplosionGodPacket::toBytes, ExplosionGodPacket::new, ExplosionGodPacket::handle);
//        INSTANCE.registerMessage(9, GodConfuseParticlePacket.class, GodConfuseParticlePacket::toBytes, GodConfuseParticlePacket::new, GodConfuseParticlePacket::handle);
//        INSTANCE.registerMessage(10, MushPunchParticlePacket.class, MushPunchParticlePacket::toBytes, MushPunchParticlePacket::new, MushPunchParticlePacket::handle);
    }

    public static void sendToServer(Object message) {
        INSTANCE.sendToServer(message);
    }

    public static void sendToPlayer(ServerPlayer player, Object packet) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
