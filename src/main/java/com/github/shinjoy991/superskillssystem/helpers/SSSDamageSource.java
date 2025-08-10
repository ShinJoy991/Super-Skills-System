package com.github.shinjoy991.superskillssystem.helpers;

import com.github.shinjoy991.superskillssystem.effect.SSSEffectInstance;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class SSSDamageSource extends DamageSource {
    private final Holder<DamageType> fallBackType;
    private final ArrayList<SSSEffectInstance> effectList = new ArrayList<>();

    public SSSDamageSource(Holder<DamageType> type, @Nullable Entity direct, @Nullable Entity causing, @Nullable Vec3 location,
                           Holder<DamageType> fallBackType) {
        super(type, direct, causing, location);
        this.fallBackType = fallBackType;
    }

    @Override
    public boolean is(TagKey<DamageType> tag) {
        return super.is(tag) || fallBackType.is(tag);
    }

    @Override
    public boolean is(ResourceKey<DamageType> key) {
        return super.is(key) || fallBackType.is(key);
    }

    public Holder<DamageType> getFallBackType() {
        return fallBackType;
    }

    public SSSDamageSource withEffect(SSSEffectInstance effect) {
        this.effectList.add(effect);
        return this;
    }

    public SSSDamageSource withEffects(ArrayList<SSSEffectInstance> effects) {
        this.effectList.addAll(effects);
        return this;
    }

    public ArrayList<SSSEffectInstance> getEffectList() {
        return effectList;
    }

    public void clearEffects() {
        this.effectList.clear();
    }

    public void reduceChance(float chance) {
        for (SSSEffectInstance effect : effectList) {
            effect.reduceChance(chance);
        }
    }
}
