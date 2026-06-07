package com.github.shinjoy991.superskillssystem.helpers.skill;

import com.github.shinjoy991.superskillssystem.config.ReadConfig;
import com.github.shinjoy991.superskillssystem.helpers.PlayerClientData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.Map;

public class PassiveSkill {
    public String name;
    public List<SkillTags> tags;
    public Map<SkillTags, Float> base;
    public Map<SkillTags, Float> bonuses;
    public SectTypes sectType;

    public PassiveSkill(String name, SectTypes sectType, List<SkillTags> tags, Map<SkillTags, Float> base, Map<SkillTags, Float> bonuses) {
        this.name = name;
        this.sectType = sectType;
        this.tags = tags;
        this.base = base;
        this.bonuses = bonuses;
        System.out.println("Loaded passive skill: " + name + " tags is " + tags);
    }

    public static List<PassiveSkill> getGlobalPassiveSkillsListBySect(SectTypes sectType) {
        return ReadConfig.passiveSkills.stream()
                .filter(skill -> skill.sectType == sectType)
                .toList();
    }
    public float getBase(SkillTags tag) {
        return base.getOrDefault(tag, 0f);
    }

    public float getBonus(SkillTags tag) {
        return bonuses.getOrDefault(tag, 0f);
    }
    public static List<PassiveSkill> getGlobalPassiveSkillsList() {
        return ReadConfig.passiveSkills;
    }
    public static CompoundTag sterilizeGlobalPassiveSkillsList() {
        CompoundTag tag = new CompoundTag();
        ListTag warriorList = new ListTag();

        for (PassiveSkill skill : ReadConfig.passiveSkills) {
            if (skill.sectType == SectTypes.WARRIOR) {
                CompoundTag warriorSkillTag = new CompoundTag();
                warriorSkillTag.putString("name", skill.name);
                warriorSkillTag.putString("sect", skill.sectType.name());

                CompoundTag baseTag = new CompoundTag();
                for (Map.Entry<SkillTags, Float> entry : skill.base.entrySet()) {
                    baseTag.putFloat(entry.getKey().name(), entry.getValue());
                }
                warriorSkillTag.put("base", baseTag);

                CompoundTag bonusesTag = new CompoundTag();
                for (Map.Entry<SkillTags, Float> entry : skill.bonuses.entrySet()) {
                    bonusesTag.putFloat(entry.getKey().name(), entry.getValue());
                }
                warriorSkillTag.put("bonuses", bonusesTag);

                // Sterilize tags list
                ListTag tagsList = new ListTag();
                for (SkillTags skillTag : skill.tags) {
                    tagsList.add(StringTag.valueOf(skillTag.name()));
                }
                warriorSkillTag.put("tags", tagsList);

                warriorList.add(warriorSkillTag);
            }
        }

        tag.put("warrior", warriorList);
        return tag;
    }

    public MutableComponent getTranslatableName() {
        return Component.translatable("skill.name." + this.name);
    }

}
