package com.github.shinjoy991.superskillssystem.helpers.skill;

import net.minecraft.network.chat.MutableComponent;

import java.awt.*;

public enum SectTypes {
    NONE("none"),
    WARRIOR("warrior"),
    ARCHER("archer"),
    MAGE("mage");
    private final String name;

    SectTypes(String name) {
        this.name = name;
    }

    public MutableComponent translatableName() {
        return net.minecraft.network.chat.Component.translatable("sect_type." + name);
    }

    public static SectTypes fromName(String name) {
        for (SectTypes type : values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return NONE; // hoặc throw exception nếu cần
    }

}
