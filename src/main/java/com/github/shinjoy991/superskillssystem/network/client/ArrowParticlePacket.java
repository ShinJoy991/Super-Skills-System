//package com.github.shinjoy991.sss.client;
//
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraftforge.fml.loading.FMLEnvironment;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.function.Supplier;
//
//public class ArrowParticlePacket {
//    private final int entityId;
//    private final Integer color;
//
//    public ArrowParticlePacket(int entityId, Integer color) {
//        this.entityId = entityId;
//        this.color = color;
//    }
//
//    public ArrowParticlePacket(FriendlyByteBuf buf) {
//        this.entityId = buf.readInt();
//        this.color = buf.readInt();
//    }
//
//    public void toBytes(FriendlyByteBuf buf) {
//        buf.writeInt(entityId);
//        buf.writeInt(color);
//    }
//
//    public void handle(Supplier<NetworkEvent.Context> ctx) {
//        NetworkEvent.Context context = ctx.get();
//        ctx.get().enqueueWork(() -> {
//            if (FMLEnvironment.dist.isClient()) {
//                ClientParticlesHandler.mightyForceArrow(entityId, color);
//            }
//        });
//        context.setPacketHandled(true);
//    }
//}
