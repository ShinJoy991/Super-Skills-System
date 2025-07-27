//package com.github.shinjoy991.sss.client;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.core.particles.DustParticleOptions;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.level.Level;
//import org.joml.Vector3f;
//
//@SuppressWarnings("resources")
//public class ClientParticlesHandler {
//
//    public static void mightyForceArrow(int entityId, Integer color) {
//        Minecraft minecraft = Minecraft.getInstance();
//        ClientLevel world = minecraft.level;
//        if (world != null) {
//            Entity entity = world.getEntity(entityId);
//            if (entity instanceof AbstractArrow arrow) {
//                addParticles(arrow, world, color);
//            }
//        }
//    }
//
//    private static void addParticles(AbstractArrow arrow, Level world, Integer color) {
//        double x = arrow.getX();
//        double y = arrow.getY();
//        double z = arrow.getZ();
//        int count = 10;
//        DustParticleOptions particleData = getParticleData(color);
//
//        for (int i = 0; i < count; i++) {
//            double offsetX = (world.random.nextDouble() - 0.5) * 0.1;
//            double offsetY = (world.random.nextDouble() - 0.5) * 0.1;
//            double offsetZ = (world.random.nextDouble() - 0.5) * 0.1;
//            world.addParticle(particleData, x + offsetX, y + offsetY, z + offsetZ, 0, 0, 0);
//        }
//    }
//
//    private static DustParticleOptions getParticleData(Integer color) {
//        return switch (color) {
//            case 1 -> new DustParticleOptions(new Vector3f(0.0F, 0.5F, 0.0F), 1.0F);
//            case 2 -> new DustParticleOptions(new Vector3f(0.0F, 1.0F, 0.0F), 1.0F);
//            case 3 -> new DustParticleOptions(new Vector3f(1.0F, 1.0F, 0.0F), 1.0F);
//            case 4 -> new DustParticleOptions(new Vector3f(1.0F, 0.5F, 0.0F), 1.0F);
//            case 5 -> new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 1.0F);
//            default -> new DustParticleOptions(new Vector3f(1.0F, 1.0F, 1.0F), 1.0F);
//        };
//    }
//}
