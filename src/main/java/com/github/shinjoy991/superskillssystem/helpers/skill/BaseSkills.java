package com.github.shinjoy991.superskillssystem.helpers.skill;

public enum SkillTag {
    ATK("attack"),
    DEFENSE("defense"),
    SPEED("speed"),
    HP("health"),
    MANA_FLAT("mana_flat"),
    MANA_PERCENT("mana_percent"),
    CRIT_CHANCE("crit_chance"),
    CRIT_DAMAGE("crit_damage"),
    LIFESTEAL("lifesteal"),
    REGENERATION("regeneration"),
    ARMOR_PENETRATION("armor_penetration");

    private final String tag;

    SkillTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
    public static SkillTag fromName(String name) {
        return SkillTag.valueOf(name.toUpperCase());
    }
}
