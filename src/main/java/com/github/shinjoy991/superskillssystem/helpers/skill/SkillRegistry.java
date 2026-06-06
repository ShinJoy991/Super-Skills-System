package com.github.shinjoy991.superskillssystem.helpers.skill;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class SkillRegistry {

    public static final Map<ResourceLocation, Class<? extends ActiveSkill>> SKILLS = new HashMap<>();

    public static void register(
            ResourceLocation id,
            Class<? extends ActiveSkill> clazz) {

        SKILLS.put(id, clazz);
    }

    public static Class<? extends ActiveSkill> get(
            ResourceLocation id) {

        return SKILLS.get(id);
    }

    public static ResourceLocation getId(Class<? extends ActiveSkill> clazz) {
        for (Map.Entry<ResourceLocation, Class<? extends ActiveSkill>> entry : SKILLS.entrySet()) {
            if (entry.getValue().equals(clazz)) {
                return entry.getKey();
            }
        }
        return null;
    }
}