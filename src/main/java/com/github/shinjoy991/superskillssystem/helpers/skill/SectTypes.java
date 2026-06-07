package com.github.shinjoy991.superskillssystem.helpers.skill;

import net.minecraft.network.chat.MutableComponent;

import java.awt.*;

public enum SectTypes {
    NONE("none"),
    SECT_1("sect_1"),
    SECT_2("sect_2"),
    WARRIOR("warrior"),
    SECT_4("sect_4"),
    SECT_5("sect_5"),
    SECT_6("sect_6"),
    SECT_7("sect_7"),
    SECT_8("sect_8"),
    ARCHER("archer");
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
