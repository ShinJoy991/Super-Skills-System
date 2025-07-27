package com.github.shinjoy991.superskillssystem.helpers.saveddata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.UUID;

public class PlayerAttStatSavedData extends SavedData {
    private static final String DATA_NAME = "player_attribute_stat_data";

    private final HashMap<UUID, CompoundTag> data = new HashMap<>();

    public static PlayerAttStatSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                PlayerAttStatSavedData::load,
                PlayerAttStatSavedData::new,
                DATA_NAME
        );
    }

    public static PlayerAttStatSavedData load(CompoundTag tag) {
        PlayerAttStatSavedData data = new PlayerAttStatSavedData();
        CompoundTag playersTag = tag.getCompound("Players");

        for (String key : playersTag.getAllKeys()) {
            try {
                UUID uuid = UUID.fromString(key);
                CompoundTag playerTag = playersTag.getCompound(key);
                data.data.put(uuid, playerTag);
            } catch (IllegalArgumentException ignored) {
                // Skip invalid UUID
            }
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag playersTag = new CompoundTag();
        for (HashMap.Entry<UUID, CompoundTag> entry : data.entrySet()) {
            playersTag.put(entry.getKey().toString(), entry.getValue());
        }
        tag.put("Players", playersTag);
        return tag;
    }

    public int getPhyAtkDmg(UUID uuid) {
        return getAttribute(uuid, "PhyAtkDmg");
    }
    public void addPhyAtkDmg(UUID uuid, int amount) {
        addAttribute(uuid, "PhyAtkDmg", amount);
    }
    public int getMagAtkDmg(UUID uuid) {
        return getAttribute(uuid, "MagAtkDmg");
    }
    public void addMagAtkDmg(UUID uuid, int amount) {
        addAttribute(uuid, "MagAtkDmg", amount);
    }
    public int getPhyDef(UUID uuid) {
        return getAttribute(uuid, "PhyDef");
    }
    public void addPhyDef(UUID uuid, int amount) {
        addAttribute(uuid, "PhyDef", amount);
    }
    public int getMagDef(UUID uuid) {
        return getAttribute(uuid, "MagDef");
    }
    public void addMagDef(UUID uuid, int amount) {
        addAttribute(uuid, "MagDef", amount);
    }


    // ======= Generic helper =======
    private int getAttribute(UUID uuid, String key) {
        CompoundTag playerData = data.get(uuid);
        if (playerData != null && playerData.contains(key)) {
            return playerData.getInt(key);
        }
        return 0;
    }

    private void addAttribute(UUID uuid, String key, int amount) {
        CompoundTag playerData = data.computeIfAbsent(uuid, k -> new CompoundTag());
        int current = playerData.getInt(key);
        playerData.putInt(key, current + amount);
        setDirty();
    }

}