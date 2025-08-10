package com.github.shinjoy991.superskillssystem.helpers.skill;

public enum BaseSkills {
    OVERLORD_SUTRA("overlord_sutra"),
    TYRANT_BODY_DIVINE_TECHNIQUE("tyrant_body_divine_technique"),
    SKILL3("skill3_name");

    private final String skillName;

    BaseSkills(String skillName) {
        this.skillName = skillName;
    }

    public String string() {
        return skillName;
    }

    @Override
    public String toString() {
        return skillName;
    }
}
