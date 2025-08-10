//package com.github.shinjoy991.superskillssystem.register;
//
//import net.minecraft.world.effect.MobEffect;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.item.alchemy.Potion;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;
//
//public class RegisterPotion {
//    public static final DeferredRegister<Potion> POTIONS;
//    public static final RegistryObject<Potion> SKY_TASTE;
//
//    static {
//        POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, "mushroom_edition");
//        SKY_TASTE = POTIONS.register("sky_taste", () -> new Potion(new MobEffectInstance[]{new MobEffectInstance((MobEffect)RegisterEffect.SKY_TASTE_EFFECT.get(), 300)}));
//    }
//}
