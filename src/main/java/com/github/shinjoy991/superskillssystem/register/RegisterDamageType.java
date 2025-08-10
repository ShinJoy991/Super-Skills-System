package com.github.shinjoy991.superskillssystem.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.github.shinjoy991.superskillssystem.SSS.MODID;

public class RegisterDamageType {
    public static final DeferredRegister<DamageType> DAMAGE_TYPES =
            DeferredRegister.create(Registries.DAMAGE_TYPE, MODID);

    public record DamageTypeEntry(RegistryObject<DamageType> object, ResourceKey<DamageType> key) {
    }

    private static DamageTypeEntry register(String name, float exhaustion) {
        RegistryObject<DamageType> object = DAMAGE_TYPES.register(name, () -> new DamageType(name, exhaustion));
        ResourceKey<DamageType> key = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, name));
        return new DamageTypeEntry(object, key);
    }

    // Đăng ký các DamageType tại đây
    public static final DamageTypeEntry MELEE_PHYSICAL = register("melee_physical", 0.1f);
    public static final DamageTypeEntry RANGED_PHYSICAL = register("ranged_physical", 0.1f);
//    public static final DamageTypeEntry HYBRID = register("hybrid_damage", 0.1f);

    public static void register(IEventBus bus) {
        DAMAGE_TYPES.register(bus);
    }
}
