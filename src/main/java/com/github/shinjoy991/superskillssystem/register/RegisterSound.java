//package com.github.shinjoy991.superskillssystem.register;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;
//
//public class RegisterSound {
//    public static final DeferredRegister<SoundEvent> SOUND_EVENTS;
//    private static final ResourceLocation EXPLOSION_GOD_RES;
//    public static final RegistryObject<SoundEvent> EXPLOSION_GOD;
//
//    static {
//        SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, "mushroom_edition");
//        EXPLOSION_GOD_RES = new ResourceLocation("mushroom_edition", "explosion_god");
//        EXPLOSION_GOD = SOUND_EVENTS.register("explosion_god", () -> SoundEvent.createVariableRangeEvent(EXPLOSION_GOD_RES));
//    }
//}
