package com.github.shinjoy991.superskillssystem.client.renderer;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.entity.trading.SectVillager;
import com.github.shinjoy991.superskillssystem.helpers.skill.SectTypes;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SectVillagerRenderer extends VillagerRenderer {

    // Texture mặc định (vanilla) - dùng khi NONE hoặc chưa có skin riêng
    private static final ResourceLocation DEFAULT_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/villager/villager.png");

    // Texture riêng cho từng sect - đặt vào assets/sss/textures/entity/villager/
    private static final ResourceLocation WARRIOR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/entity/villager/profession/sect_warrior.png");

    private static final ResourceLocation ARCHER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/entity/villager/profession/sect_archer.png");

    private static final ResourceLocation MAGE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/entity/villager/profession/sect_mage.png");

    private static final ResourceLocation SWORDSMAN_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/entity/villager/profession/sect_swordsman.png");

    private static final ResourceLocation MEDIC_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/entity/villager/profession/sect_medic.png");

    private static final ResourceLocation STRIKER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/entity/villager/profession/sect_striker.png");

    private static final ResourceLocation TRICKSTER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/entity/villager/profession/sect_trickster.png");

    private static final ResourceLocation GUARDIAN_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/entity/villager/profession/sect_guardian.png");

    private static final ResourceLocation HUNTER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/entity/villager/profession/sect_hunter.png");

    private static final ResourceLocation SUMMONER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/entity/villager/profession/sect_summoner.png");

    private static final ResourceLocation ENGINEER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/entity/villager/profession/sect_engineer.png");

    private static final ResourceLocation ASSASSIN_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "textures/entity/villager/profession/sect_assassin.png");

    public SectVillagerRenderer(EntityRendererProvider.Context context) {
        super(context);

    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(Villager villager) {
        if (villager instanceof SectVillager sectVillager) {
            SectTypes sect = sectVillager.getSectType();
            switch (sect) {
                case WARRIOR -> {
                    return WARRIOR_TEXTURE;
                }
                case ARCHER -> {
                    return ARCHER_TEXTURE;
                }
                case MAGE -> {
                    return MAGE_TEXTURE;
                }
                case SWORDSMAN -> {
                    return SWORDSMAN_TEXTURE;
                }
                case MEDIC -> {
                    return MEDIC_TEXTURE;
                }
                case STRIKER -> {
                    return STRIKER_TEXTURE;
                }
                case TRICKSTER -> {
                    return TRICKSTER_TEXTURE;
                }
                case GUARDIAN -> {
                    return GUARDIAN_TEXTURE;
                }
                case HUNTER -> {
                    return HUNTER_TEXTURE;
                }
                case SUMMONER -> {
                    return SUMMONER_TEXTURE;
                }
                case ENGINEER -> {
                    return ENGINEER_TEXTURE;
                }
                case ASSASSIN -> {
                    return ASSASSIN_TEXTURE;
                }
                case NONE -> {
                    return DEFAULT_TEXTURE;
                }
                default -> {
                    return DEFAULT_TEXTURE;
                }
            }
        }
        return DEFAULT_TEXTURE;
    }
}