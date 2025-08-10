//package com.github.shinjoy991.superskillssystem.register;
//
//import net.minecraft.core.particles.ParticleType;
//import net.minecraft.core.particles.SimpleParticleType;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;
//
//public class RegisterParticle {
//    public static final DeferredRegister<ParticleType<?>> PARTICLES;
//    public static final RegistryObject<SimpleParticleType> BARRIER_PARTICLE;
//    public static final RegistryObject<SimpleParticleType> GOD_CONFUSE_PARTICLE;
//
//    static {
//        PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "mushroom_edition");
//        BARRIER_PARTICLE = PARTICLES.register("barrier_particle", () -> new SimpleParticleType(true));
//        GOD_CONFUSE_PARTICLE = PARTICLES.register("god_confuse_particle", () -> new SimpleParticleType(true));
//    }
//}