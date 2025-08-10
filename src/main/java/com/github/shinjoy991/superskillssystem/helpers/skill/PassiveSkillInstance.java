package com.github.shinjoy991.superskillssystem.helpers.skill;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class PassiveSkillInstance {
    private final PassiveSkill skill;
    private int level;

    public PassiveSkillInstance(PassiveSkill skill, int level) {
        this.skill = skill;
        this.level = level;
    }

    public String getName() {
        return skill.name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int newLevel) {
        this.level = newLevel;
    }

    public void addLevel(int add) {
        this.level += add;
    }

    public List<SkillTags> getTags() {
        return skill.tags;
    }

    public float getBase(SkillTags tag) {
        return skill.base.getOrDefault(tag, 0f);
    }

    public float getBonus(SkillTags tag) {
        return skill.bonuses.getOrDefault(tag, 0f);
    }

    public float getValue(SkillTags tag) {
        float baseValue = getBase(tag);
        float bonusValue = getBonus(tag);
        return baseValue + bonusValue * level;
    }

    public boolean hasTag(SkillTags tag) {
        return skill.tags.contains(tag);
    }

    public Component getInfo(boolean isSectMatch) {
        MutableComponent result = Component.literal("");
        MutableComponent nameLine = Component.translatable("skill.name." + skill.name)
                .withStyle(isSectMatch ? ChatFormatting.GOLD : ChatFormatting.GRAY);
        result.append(nameLine).append("\n");

        // Dòng phụ màu đen
        result.append(Component.literal("").withStyle(ChatFormatting.BLACK));

        result.append(Component.translatable("skill.type.passive")).append("\n");
        for (SkillTags tag : skill.tags) {
            result.append(getInfoBaseOnTag(tag)).append("\n");
        }
        return result;
    }
    private Component getInfoBaseOnTag(SkillTags tag) {
        float value = getValue(tag);
        return switch (tag) {
            case ATK_FLAT -> Component.translatable("skill.info.atk", value);
            case DEF_FLAT -> Component.translatable("skill.info.defense", value);
            case SPEED -> Component.translatable("skill.info.speed", value);
            default -> Component.translatable("skill.info.unknown", tag.getTag());
        };
    }

    public SectTypes getSectType() {
        return skill.sectType;
    }

    public PassiveSkill getSkill() {
        return skill;
    }
}
