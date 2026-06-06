package com.github.shinjoy991.superskillssystem.helpers.saveddata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.UUID;

public class PlayerAttPointSavedData extends SavedData {
    // For Integer only
    // TotalAttPoint, UsedAttPoint, StrPoint, VitPoint, AgiPoint, IntPoint, PerPoint
    // activeSkillSlot1, activeSkillSlot2, activeSkillSlot3, activeSkillSlot4
    private static final String DATA_NAME = "player_attribute_point_data";

    private final HashMap<UUID, CompoundTag> data = new HashMap<>();

    public static PlayerAttPointSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                PlayerAttPointSavedData::load,
                PlayerAttPointSavedData::new,
                DATA_NAME
        );
    }

    public static PlayerAttPointSavedData load(CompoundTag tag) {
        PlayerAttPointSavedData data = new PlayerAttPointSavedData();
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

    // ======= Tổng và đã dùng =======
    public int getUsedAttPoints(UUID uuid) {
        return getAttribute(uuid, "UsedAttPoints");
    }

    public void addUsedAttPoints(UUID uuid, int amount) {
        addAttribute(uuid, "UsedAttPoints", amount);

    }
    public void setUsedAttPoints(UUID uuid, int amount) {
        CompoundTag playerData = data.computeIfAbsent(uuid, k -> new CompoundTag());
        playerData.putInt("UsedAttPoints", amount);
        setDirty();
    }
    // ======= STR =======
    public int getStrPoint(UUID uuid) {
        return getAttribute(uuid, "StrPoint");
    }

    public void addStrPoint(UUID uuid, int amount) {
        addAttribute(uuid, "StrPoint", amount);
    }

    // ======= VIT =======
    public int getVitPoint(UUID uuid) {
        return getAttribute(uuid, "VitPoint");
    }

    public void addVitPoint(UUID uuid, int amount) {
//        System.out.println("Adding Vit Point SavedData: " + amount);
        addAttribute(uuid, "VitPoint", amount);
    }

    // ======= AGI =======
    public int getAgiPoint(UUID uuid) {
        return getAttribute(uuid, "AgiPoint");
    }

    public void addAgiPoint(UUID uuid, int amount) {
        addAttribute(uuid, "AgiPoint", amount);
    }

    // ======= INT =======
    public int getIntPoint(UUID uuid) {
        return getAttribute(uuid, "IntPoint");
    }

    public void addIntPoint(UUID uuid, int amount) {
        addAttribute(uuid, "IntPoint", amount);
    }

    // ======= PER =======
    public int getPerPoint(UUID uuid) {
        return getAttribute(uuid, "PerPoint");
    }

    public void addPerPoint(UUID uuid, int amount) {
        addAttribute(uuid, "PerPoint", amount);
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
//        System.out.println("Adding in saved data " + key + ": " + amount + " (current: " + current + ")");
        playerData.putInt(key, current + amount);
        setDirty();
    }

    public void setStrPoint(UUID uuid, int i) {
        CompoundTag playerData = data.computeIfAbsent(uuid, k -> new CompoundTag());
        playerData.putInt("StrPoint", i);
        setDirty();
    }

    public void setVitPoint(UUID uuid, int i) {
        CompoundTag playerData = data.computeIfAbsent(uuid, k -> new CompoundTag());
        playerData.putInt("VitPoint", i);
        setDirty();
    }

    public void setAgiPoint(UUID uuid, int i) {
        CompoundTag playerData = data.computeIfAbsent(uuid, k -> new CompoundTag());
        playerData.putInt("AgiPoint", i);
        setDirty();
    }

    public void setIntPoint(UUID uuid, int i) {
        CompoundTag playerData = data.computeIfAbsent(uuid, k -> new CompoundTag());
        playerData.putInt("IntPoint", i);
        setDirty();
    }

    public void setPerPoint(UUID uuid, int i) {
        CompoundTag playerData = data.computeIfAbsent(uuid, k -> new CompoundTag());
        playerData.putInt("PerPoint", i);
        setDirty();
    }

    public String getActiveSkillSlot(UUID uuid, int slot) {
        CompoundTag playerData = data.get(uuid);
        if (playerData != null) {
            return playerData.getString("ActiveSkillSlot" + slot);
        }
        return "";
    }
    public void setActiveSkillSlot(UUID uuid, int slot, String skillId) {
        CompoundTag playerData = data.computeIfAbsent(uuid, k -> new CompoundTag());
        playerData.putString("ActiveSkillSlot" + slot, skillId);
        setDirty();
    }
}