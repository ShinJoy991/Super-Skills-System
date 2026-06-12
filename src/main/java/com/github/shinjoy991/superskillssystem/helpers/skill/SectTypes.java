package com.github.shinjoy991.superskillssystem.helpers.skill;

import net.minecraft.network.chat.MutableComponent;

import java.awt.*;

public enum SectTypes {
    NONE("none"),
    WARRIOR("warrior"), // Axe, Hammer, Spear
    ARCHER("archer"), // Bow, Crossbow
    MAGE("mage"), // Staff, Wand
    SWORDSMAN("swordsman"), // Sword
    MEDIC("medic"), // Book
    STRIKER("striker"), // Fist, Claw
    TRICKSTER("trickster"), // Potion, shadow moving // Sự Vật
    GUARDIAN("guardian"), // shield, armor
    HUNTER("hunter"), // Gun // tăng thuoc tinh
    SUMMONER("summoner"), // Summoning // Dark magic, Necromancy
    ENGINEER("engineer"), // Gadget, Futuristic weapon //number
    ASSASSIN("assassin"); // Dagger, Poison
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
