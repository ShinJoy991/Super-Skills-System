package com.github.shinjoy991.superskillssystem.helpers.saveddata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManaSavedData extends SavedData {
    private static final String DATA_NAME = "player_mana_data";

    private final HashMap<UUID, Integer> data = new HashMap<>();

    public static PlayerManaSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                PlayerManaSavedData::load,
                PlayerManaSavedData::new,
                DATA_NAME
        );
    }

    public void set(UUID uuid, int mana) {
        data.put(uuid, mana);
        setDirty();
    }

    public Integer get(UUID uuid) {
        return data.getOrDefault(uuid, 0);
    }
    public void setMana(UUID uuid, int mana) {
        data.put(uuid, mana);
        setDirty();
    }

    public void addMana(UUID uuid, int amount) {
        int current = data.getOrDefault(uuid, 0);
        data.put(uuid, current + amount);
        setDirty();
    }

    public int getMana(UUID uuid) {
        return data.getOrDefault(uuid, 0);
    }
    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag all = new CompoundTag();
        for (Map.Entry<UUID, Integer> entry : data.entrySet()) {
            all.putInt(entry.getKey().toString(), entry.getValue());
        }
        tag.put("data", all);
        return tag;
    }

    public static PlayerManaSavedData load(CompoundTag tag) {
        PlayerManaSavedData data = new PlayerManaSavedData();
        CompoundTag all = tag.getCompound("data");
        for (String key : all.getAllKeys()) {
            int mana = all.getInt(key);
            data.data.put(UUID.fromString(key), mana);
        }
        return data;
    }
}