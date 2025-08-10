package com.github.shinjoy991.superskillssystem.helpers;

import java.util.List;

public enum SterilizeTags {
    PRIME_EXP("prime_exp"),
    SECT("sect"),
    STR_POINT("str_point"),
    VIT_POINT("vit_point"),
    AGI_POINT("agi_point"),
    INT_POINT("int_point"),
    PER_POINT("per_point"),
    TOTAL_STR("total_str"),
    TOTAL_VIT("total_vit"),
    TOTAL_AGI("total_agi"),
    TOTAL_INT("total_int"),
    TOTAL_PER("total_per"),
    USED_ATT_POINT("used_att_point"),
    WEAPON_DMG("weapon_dmg"),
    WEAPON_DMG_BONUS("weapon_dmg_bonus"),
    PERFECTION("perfection"),
    ATK_DMG("atk_dmg"),
    RANGE_DMG("range_dmg"),
    MAGIC_DMG("magic_dmg"),
    DEF("def"),
    DEF_PEN("def_pen"),
    MAGIC_DEF("magic_def"),
    MAGIC_PEN("magic_pen"),
    CRIT_CHANCE("crit_chance"),
    ATTACK_SPEED("attack_speed"),
    SPEED("speed"),
    DMG_RED("dmg_red"),
    MAGIC_RESIST("magic_resist"),
    EVASION("evasion"),
    ACCURACY("accuracy"),
    COUNTER_CHANCE("counter_chance"),
    RESISTANCE("resistance"),
    LIFE_STEAL("life_steal"),
    MANA_STEAL("mana_steal"),
    HEAL_REGEN("heal_regen"),
    MANA_REGEN("mana_regen"),
    DMG_RED_PEN("dmg_red_pen"),
    MAGIC_RESIST_PEN("magic_resist_pen"),
    RESISTANCE_PEN("resistance_pen"),

    MANA("mana"), MAX_MANA("max_mana"), BONUS_DMG("bonus_dmg"),;


    SterilizeTags(String tag) {
    }

    public List<SterilizeTags> getAllTags() {
        return List.of(SterilizeTags.values());
    }
}
