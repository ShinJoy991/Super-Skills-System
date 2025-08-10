package com.github.shinjoy991.superskillssystem.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class SSSEffectInstance extends MobEffectInstance {
    private float chance;

    public SSSEffectInstance(MobEffect effect, int duration, int amplifier, float chance) {
        super(effect, duration, amplifier);
        this.chance = chance;
    }

    public float getChance() {
        return chance;
    }

    public void reduceChance(float chance) {
        if (this.chance > chance) {
            this.chance = Math.max(0, this.chance - chance);
        } else {
            this.chance = 0;
        }
    }
}
