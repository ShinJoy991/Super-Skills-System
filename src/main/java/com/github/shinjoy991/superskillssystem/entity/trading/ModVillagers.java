package com.github.shinjoy991.superskillssystem.entity.trading;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.register.RegisterBlock;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, SSS.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, SSS.MODID);

    public static final RegistryObject<PoiType> SOUND_POI = POI_TYPES.register("sound_poi",
            () -> new PoiType(ImmutableSet.copyOf(RegisterBlock.CRUSTEDMAGMA.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final RegistryObject<VillagerProfession> SOUND_MASTER =
            VILLAGER_PROFESSIONS.register("soundmaster", () -> new VillagerProfession("soundmaster",
                    holder -> holder.get() == SOUND_POI.get(), holder -> holder.get() == SOUND_POI.get(),
                    ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_ARMORER));

    public static final RegistryObject<VillagerProfession> SECT_MASTER =
            VILLAGER_PROFESSIONS.register("sect_master",
                    () -> new VillagerProfession(
                            "sect_master",
                            poi -> false, // Không yêu cầu POI
                            poi -> false, // Không tìm kiếm POI
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_CELEBRATE // Hoặc âm thanh khác
                    )
            );

    public static final RegistryObject<VillagerProfession> SECT_WARRIOR =
            VILLAGER_PROFESSIONS.register("sect_warrior",
                    () -> new VillagerProfession(
                            "sect_warrior",
                            poi -> false, // Không yêu cầu POI
                            poi -> false, // Không tìm kiếm POI
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_CELEBRATE // Hoặc âm thanh khác
                    )
            );

    public static final RegistryObject<VillagerProfession> SECT_ARCHER =
            VILLAGER_PROFESSIONS.register("sect_archer",
                    () -> new VillagerProfession(
                            "sect_archer",
                            poi -> false,
                            poi -> false,
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_FLETCHER
                    )
            );

    public static final RegistryObject<VillagerProfession> SECT_MAGE =
            VILLAGER_PROFESSIONS.register("sect_mage",
                    () -> new VillagerProfession(
                            "sect_mage",
                            poi -> false,
                            poi -> false,
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_CELEBRATE
                    )
            );

    public static final RegistryObject<VillagerProfession> SECT_SWORDSMAN =
            VILLAGER_PROFESSIONS.register("sect_swordsman",
                    () -> new VillagerProfession(
                            "sect_swordsman",
                            poi -> false,
                            poi -> false,
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_ARMORER
                    )
            );

    public static final RegistryObject<VillagerProfession> SECT_MEDIC =
            VILLAGER_PROFESSIONS.register("sect_medic",
                    () -> new VillagerProfession(
                            "sect_medic",
                            poi -> false,
                            poi -> false,
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_CARTOGRAPHER
                    )
            );

    public static final RegistryObject<VillagerProfession> SECT_STRIKER =
            VILLAGER_PROFESSIONS.register("sect_striker",
                    () -> new VillagerProfession(
                            "sect_striker",
                            poi -> false,
                            poi -> false,
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_CELEBRATE
                    )
            );

    public static final RegistryObject<VillagerProfession> SECT_TRICKSTER =
            VILLAGER_PROFESSIONS.register("sect_trickster",
                    () -> new VillagerProfession(
                            "sect_trickster",
                            poi -> false,
                            poi -> false,
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_CARTOGRAPHER
                    )
            );

    public static final RegistryObject<VillagerProfession> SECT_GUARDIAN =
            VILLAGER_PROFESSIONS.register("sect_guardian",
                    () -> new VillagerProfession(
                            "sect_guardian",
                            poi -> false,
                            poi -> false,
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_ARMORER
                    )
            );

    public static final RegistryObject<VillagerProfession> SECT_HUNTER =
            VILLAGER_PROFESSIONS.register("sect_hunter",
                    () -> new VillagerProfession(
                            "sect_hunter",
                            poi -> false,
                            poi -> false,
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_FLETCHER
                    )
            );

    public static final RegistryObject<VillagerProfession> SECT_SUMMONER =
            VILLAGER_PROFESSIONS.register("sect_summoner",
                    () -> new VillagerProfession(
                            "sect_summoner",
                            poi -> false,
                            poi -> false,
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_CELEBRATE
                    )
            );

    public static final RegistryObject<VillagerProfession> SECT_ENGINEER =
            VILLAGER_PROFESSIONS.register("sect_engineer",
                    () -> new VillagerProfession(
                            "sect_engineer",
                            poi -> false,
                            poi -> false,
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_WORK_ARMORER
                    )
            );

    public static final RegistryObject<VillagerProfession> SECT_ASSASSIN =
            VILLAGER_PROFESSIONS.register("sect_assassin",
                    () -> new VillagerProfession(
                            "sect_assassin",
                            poi -> false,
                            poi -> false,
                            ImmutableSet.of(),
                            ImmutableSet.of(),
                            SoundEvents.VILLAGER_CELEBRATE
                    )
            );

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}