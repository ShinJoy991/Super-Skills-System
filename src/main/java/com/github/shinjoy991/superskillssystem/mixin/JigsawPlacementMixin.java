package com.github.shinjoy991.superskillssystem.mixin;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(StructureTemplatePool.class)
public class JigsawPlacementMixin {

    @Shadow
    private Holder<StructureTemplatePool> fallback;

    private static final List<String> VILLAGE_FALLBACKS = List.of(
            "village/plains/terminators",
            "village/savanna/terminators",
            "village/desert/terminators",
            "village/snowy/terminators",
            "village/taiga/terminators"
    );

    @Inject(
            method = "getShuffledTemplates",
            at = @At("RETURN"),
            cancellable = true
    )
    private void onGetShuffledTemplates(
            RandomSource random,
            CallbackInfoReturnable<List<StructurePoolElement>> cir
    ) {
        ResourceLocation fallbackLoc = this.fallback.unwrapKey()
                .map(k -> k.location())
                .orElse(null);

        if (fallbackLoc == null) return;

        boolean isVillageHousePool = VILLAGE_FALLBACKS.stream()
                .anyMatch(path -> fallbackLoc.getPath().equals(path));

        if (!isVillageHousePool) return;

        // Lấy list hiện tại từ return value
        List<StructurePoolElement> list = new java.util.ArrayList<>(cir.getReturnValue());

        StructurePoolElement sectHouseElement = StructurePoolElement
                .single("sss:sect_house")
                .apply(StructureTemplatePool.Projection.RIGID);

        list.add(sectHouseElement);
        list.add(sectHouseElement);

        cir.setReturnValue(list);

//        System.out.println("[SSS DEBUG] getShuffledTemplates injected, final size: " + list.size());
    }
}