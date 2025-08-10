package com.github.shinjoy991.superskillssystem.register;

import com.github.shinjoy991.superskillssystem.entity.trading.SectVillager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.github.shinjoy991.superskillssystem.SSS.MODID;


public class RegisterEntity {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES;
    public static final RegistryObject<EntityType<SectVillager>> SECT_WARRIOR_VILLAGER;

    static {
        ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
        SECT_WARRIOR_VILLAGER = ENTITY_TYPES.register("sect_warrior_villager", () -> Builder.of(SectVillager::new, MobCategory.MISC).sized(0.6F, 1.95F).clientTrackingRange(10).build("sect_warrior_villager"));
    }
}
