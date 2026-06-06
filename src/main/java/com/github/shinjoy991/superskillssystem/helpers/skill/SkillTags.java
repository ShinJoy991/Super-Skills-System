package com.github.shinjoy991.superskillssystem.helpers.skill;

import java.util.EnumSet;

public enum SkillTags {
    // Tag gồm tag name (IN HOA) và tag value (thường là chữ thường, không có khoảng trắng)
    ATK_FLAT("atk_flat"), ATK_PERCENT("atk_percent"), // (Player ATK + weapon ATK) * perfect rate
    RANGE_FLAT("range_flat"), RANGE_PERCENT("range_percent"), // (Weapon dmg + pull power * RangeDmg) * perfect rate
    MAGIC_FLAT("magic_flat"), MAGIC_PERCENT("magic_percent"),
    WEAPON_DMG_PERCENT("weapon_dmg_percent"), // Weapon dmg = tooltip show / 1 (player hand dmg), etc. iron sword = 5

    DEF_PERCENT("def_percent"), DEF_FLAT("def_flat"), // Aside from armor,armor toughness, enchantments, effects.
    MAGIC_DEF_PERCENT("magic_def_percent"), MAGIC_DEF_FLAT("magic_def_flat"),

    HP_FLAT("hp_flat"), HP_PERCENT("hp_percent"), // add transient modifier to player max health
    MANA_FLAT("mana_flat"), MANA_PERCENT("mana_percent"),
    STR_FLAT("str_flat"), STR_PERCENT("str_percent"),
    VIT_FLAT("vit_flat"), VIT_PERCENT("vit_percent"),
    AGI_FLAT("agi_flat"), AGI_PERCENT("agi_percent"),
    INT_FLAT("int_flat"), INT_PERCENT("int_percent"),
    PER_FLAT("per_flat"), PER_PERCENT("per_percent"),

    ATTACK_SPEED("attack_speed"), // swing speed, pull bow, cast speed
    SPEED("speed"), // walk and run and swim speed
    CRIT_CHANCE("crit_chance"), // crit increase 30% dmg
    PERFECTION("perfection"), // Percentage damage change randomly, 1 pt = 1%
    COUNTER_CHANCE("counter_chance"), // Chance to counter attack, reflect and reduce damage

    DEF_PEN_PERCENT("def_pen_percent"), DEF_PEN_FLAT("def_pen_flat"),
    MAGIC_PEN_PERCENT("magic_pen_percent"), MAGIC_PEN_FLAT("magic_pen_flat"),

    LIFE_STEAL("life_steal"), MANA_STEAL("mana_steal"),
    HEAL_REGEN("heal_regen"), MANA_REGEN("mana_regen"),

    EVASION("evasion"), ACCURACY("accuracy"), // Chance
    DMG_REDUCTION("dmg_reduction"),
    RESISTANCE("resistance"), // Chance
    MAGIC_RESISTANCE("magic_resistance"),

    DMG_REDUCTION_PEN("dmg_reduction_pen"),
    RESISTANCE_PEN("resistance_pen"), // Chance
    MAGIC_RESISTANCE_PEN("magic_resistance_pen"),
   ;
    private final String tag;

    SkillTags(String tag) {
        this.tag = tag;
    }

    public String value() {
        return tag;
    }
    public static SkillTags fromName(String name) {
        return SkillTags.valueOf(name);
    }

    private static final EnumSet<SkillTags> PERCENTAGE_TAGS = EnumSet.of(
            ATK_PERCENT, RANGE_PERCENT, MAGIC_PERCENT, WEAPON_DMG_PERCENT,
            DEF_PERCENT, MAGIC_DEF_PERCENT, HP_PERCENT, MANA_PERCENT,
            STR_PERCENT, VIT_PERCENT, AGI_PERCENT, INT_PERCENT, PER_PERCENT,
            CRIT_CHANCE, PERFECTION, COUNTER_CHANCE,
            DEF_PEN_PERCENT, MAGIC_PEN_PERCENT,
            EVASION, ACCURACY,
            RESISTANCE, RESISTANCE_PEN
    );

    public boolean isPercentage() {
        return PERCENTAGE_TAGS.contains(this);
    }
}
