package com.github.shinjoy991.superskillssystem.worldgen.structure;

import com.github.shinjoy991.superskillssystem.SSS;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = SSS.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SectVillageStructures {

    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY =
            ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.fromNamespaceAndPath("minecraft", "empty"));

    // Map: pool path -> danh sách [nbt name, weight]
    private static final Map<String, List<Pair<String, Integer>>> VILLAGE_HOUSES = Map.of(
            "village/plains/houses", List.of(
                    Pair.of("sss:village/sect_house_plains_1", 50)
            ),
            "village/snowy/houses", List.of(
                    Pair.of("sss:village/sect_house_snowy_1", 50)
            ),
            "village/savanna/houses", List.of(
                    Pair.of("sss:village/sect_house_savanna_1", 50)
            ),
            "village/taiga/houses", List.of(
                    Pair.of("sss:village/sect_house_taiga_1", 30)
            ),
            "village/desert/houses", List.of(
                    Pair.of("sss:village/sect_house_desert_1", 50)
            )
    );

    @SubscribeEvent
    public static void addVillageHouse(ServerAboutToStartEvent event) {
        Registry<StructureTemplatePool> poolRegistry =
                event.getServer().registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
        Registry<StructureProcessorList> processorRegistry =
                event.getServer().registryAccess().registryOrThrow(Registries.PROCESSOR_LIST);

        for (Map.Entry<String, List<Pair<String, Integer>>> entry : VILLAGE_HOUSES.entrySet()) {
            ResourceLocation poolRL = ResourceLocation.fromNamespaceAndPath("minecraft", entry.getKey());
            for (Pair<String, Integer> house : entry.getValue()) {
                addToPool(poolRegistry, processorRegistry, poolRL, house.getFirst(), house.getSecond());
            }
        }
    }

    private static void addToPool(Registry<StructureTemplatePool> poolReg,
                                  Registry<StructureProcessorList> procReg,
                                  ResourceLocation poolRL,
                                  String nbtRL, int weight) {
        Holder<StructureProcessorList> emptyProc = procReg.getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY);
        StructureTemplatePool pool = poolReg.get(poolRL);
        if (pool == null) return;

        SinglePoolElement piece = SinglePoolElement.legacy(nbtRL, emptyProc)
                .apply(StructureTemplatePool.Projection.RIGID);

        for (int i = 0; i < weight; i++) {
            pool.templates.add(piece);
        }

        List<Pair<StructurePoolElement, Integer>> list = new ArrayList<>(pool.rawTemplates);
        list.add(new Pair<>(piece, weight));
        pool.rawTemplates = list;
    }
}