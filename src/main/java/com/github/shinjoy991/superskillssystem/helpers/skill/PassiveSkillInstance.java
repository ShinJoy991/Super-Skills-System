package com.github.shinjoy991.superskillssystem.helpers.skill;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import org.checkerframework.checker.units.qual.C;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PassiveSkillInstance {
    private final PassiveSkill skill;
    private int level;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("+#.##;-#.##");

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

    public float getValue(SkillTags tag) {
        float baseValue = skill.getBase(tag);
        float bonusValue = skill.getBonus(tag);
        if (level == 1) {
            return baseValue;
        }
        else if (level == 20) {
            return baseValue + bonusValue * 20;
        }
        return baseValue + bonusValue * (level - 1);
    }

    public boolean hasTag(SkillTags tag) {
        return skill.tags.contains(tag);
    }

    public Component getInfo(boolean isSectMatch) {
//        System.out.println("Getting info for skill: " + skill.tags);
        MutableComponent result = Component.literal("");
        MutableComponent nameLine = Component.translatable("skill.name." + skill.name)
                .withStyle(ChatFormatting.BOLD)
                .withStyle(style -> style.withColor(isSectMatch
                        ? TextColor.fromLegacyFormat(ChatFormatting.DARK_RED)
                        : TextColor.fromRgb(0x996600)));
        result.append(nameLine).append("    ");

        // Description passive
        result.append(Component.translatable("skill.type.passive")).append("\n").withStyle(ChatFormatting.DARK_GRAY);
        for (SkillTags tag : skill.tags) {
            result.append(getInfoBaseOnTag(tag));
            result.append("\n");
        }
        return result;
    }

    private Component getInfoBaseOnTag(SkillTags tag) {
//        System.out.println("Getting info for tag: " + tag);
        float value = this.getValue(tag);
        // Format số (tự động thêm + và bỏ 0 thừa)
        String text = DECIMAL_FORMAT.format(value);
        if (tag.isPercentage()) {
            text += "%";
        }
        MutableComponent appendValue = Component.literal(text);
        if (value > 0) {
            appendValue.withStyle(ChatFormatting.DARK_GREEN);
        }
        return Component.translatable("skill.info." + tag.value()).append(appendValue);
    }

    public SectTypes getSectType() {
        return skill.sectType;
    }

    public PassiveSkill getSkill() {
        return skill;
    }


    public List<Component> getLoreInfo(boolean isSectMatch) {
        List<Component> lore = new ArrayList<>();
        lore.add(skill.sectType.translatableName().copy()
                        .withStyle(style -> style
                                .withItalic(false)
                                .withColor(isSectMatch
                                        ? TextColor.fromLegacyFormat(ChatFormatting.DARK_RED)
                                        : TextColor.fromRgb(0x996600)))
        );

        for (SkillTags tag : skill.tags) {
            lore.add(getLoreInfoBaseOnTag(tag).copy()
                            .withStyle(style -> style.withItalic(false)).withStyle(ChatFormatting.GRAY)
            );
        }
        return lore;
    }

    private Component getLoreInfoBaseOnTag(SkillTags tag) {
        float value = this.getValue(tag);
        String text = DECIMAL_FORMAT.format(value);
        if (tag.isPercentage()) {
            text += "%";
        }
        MutableComponent appendValue = Component.literal(text);
        if (value > 0) {
            appendValue.withStyle(ChatFormatting.DARK_GREEN);
        } else if (value < 0) {
            appendValue.withStyle(ChatFormatting.DARK_RED);
        }
        return Component.translatable("skill.info." + tag.value())
                .append(appendValue);
    }
}
