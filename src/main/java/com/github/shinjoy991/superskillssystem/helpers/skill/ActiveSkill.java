package com.github.shinjoy991.superskillssystem.helpers.skill;

import com.github.shinjoy991.superskillssystem.activeskills.meleephysical.ActSkillThrust;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public abstract class ActiveSkill {

    protected final ResourceLocation id;
    protected final LivingEntity caster;
    protected final String name;
    protected final SectTypes sectType;
    protected final int level;

    protected int manaCost;
    protected int cooldown;
    protected float baseDamage;
    protected ResourceKey<DamageType> damageType = DamageTypes.GENERIC; // default damage type

    protected LivingEntity mainTarget;
    protected List<LivingEntity> otherTargets;
    protected Vec3 targetLocation;

    protected final boolean isBuff;
    protected final boolean mobCanUse = false;

    public ActiveSkill(
            ResourceLocation id,
            String name,
            LivingEntity caster,
            SectTypes sectType,
            int level, int manaCost, boolean isBuff) {

        this.id = id;
        this.name = name;
        this.caster = caster;
        this.sectType = sectType;
        this.level = level;
        this.manaCost = manaCost;
        this.isBuff = isBuff;
    }

    public abstract void activate();

    public ResourceLocation getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SectTypes getSectType() {
        return sectType;
    }

    public int getLevel() {
        return level;
    }

    public int getManaCost() {
        return manaCost;
    }

    public boolean isBuff() {
        return isBuff;
    }

    public int getCooldown() {
        return cooldown;
    }


    public Component getInfo(boolean isSectMatch) {
        MutableComponent result = Component.literal("");
        MutableComponent nameLine = Component.translatable("skill.name." + name)
                .withStyle(ChatFormatting.BOLD)
                .withStyle(style -> style.withColor(isSectMatch
                        ? TextColor.fromLegacyFormat(ChatFormatting.DARK_RED)
                        : TextColor.fromRgb(0x996600)));

        result.append(nameLine).append("    ");
        result.append(Component.translatable("skill.type.active")).append("\n").withStyle(ChatFormatting.DARK_GRAY);
        result.append(Component.translatable("skill.level").append(": " + level).append("    "));
        result.append(Component.translatable("skill.mana_cost").append(": " + manaCost).append("    "));
        result.append(Component.translatable("skill.cooldown").append(": " + cooldown).append("\n"));
        result.append(getDamageTypeName()).append("    ");
        result.append(Component.translatable("skill.description." + name));
//        System.out.println("result info: " + result.getString());
        return result;
    }


    public ResourceKey<DamageType> getDamageType() {
        return damageType;
    }

    public Component getDamageTypeName() {
        ResourceLocation id = damageType.location();
        return Component.translatable(
                "damage_type." + id.getNamespace() + "." + id.getPath()
        );
    }

}