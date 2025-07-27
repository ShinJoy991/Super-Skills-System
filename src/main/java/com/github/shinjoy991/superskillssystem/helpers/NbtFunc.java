package com.github.shinjoy991.superskillssystem.helpers;

import net.minecraft.nbt.CompoundTag;

public class NbtFunc {
    public static CompoundTag copyNBTWithoutUUID(CompoundTag oldNbt) {
        CompoundTag newNbt = new CompoundTag();
        for (String key : oldNbt.getAllKeys()) {
            if (!key.equals("UUID")) { // Exclude UUID
                newNbt.put(key, oldNbt.get(key));
            }
        }
        return newNbt;
    }
}