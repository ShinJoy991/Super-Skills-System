package com.github.shinjoy991.superskillssystem.helpers.skill;

public class SkillData {
    private int level;
    private String id;

    public SkillData(int level, String id) {
        this.level = level;
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}