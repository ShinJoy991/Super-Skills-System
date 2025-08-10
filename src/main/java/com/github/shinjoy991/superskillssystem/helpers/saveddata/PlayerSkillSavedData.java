package com.github.shinjoy991.superskillssystem.helpers.saveddata;

import com.github.shinjoy991.superskillssystem.config.ReadConfig;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkillInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class PlayerSkillSavedData extends SavedData {
    private static final String DATA_NAME = "player_skill_data";

    private final Map<UUID, Map<String, Integer>> data = new HashMap<>();

    public static PlayerSkillSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                PlayerSkillSavedData::load,
                PlayerSkillSavedData::new,
                DATA_NAME
        );
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        for (Map.Entry<UUID, Map<String, Integer>> entry : data.entrySet()) {
            CompoundTag skillTag = new CompoundTag();
            for (Map.Entry<String, Integer> skillEntry : entry.getValue().entrySet()) {
                skillTag.putInt(skillEntry.getKey(), skillEntry.getValue());
            }
            tag.put(entry.getKey().toString(), skillTag);
        }
        return tag;
    }

    public static PlayerSkillSavedData load(CompoundTag tag) {
        PlayerSkillSavedData savedData = new PlayerSkillSavedData();

        for (String uuidString : tag.getAllKeys()) {
            UUID uuid = UUID.fromString(uuidString);
            CompoundTag skillTag = tag.getCompound(uuidString);
            Map<String, Integer> skills = new HashMap<>();

            for (String skillName : skillTag.getAllKeys()) {
                skills.put(skillName, skillTag.getInt(skillName));
            }

            savedData.data.put(uuid, skills);
        }
        return savedData;
    }

    public List<PassiveSkillInstance> getPassiveSkills(UUID playerUUID) {
        List<PassiveSkillInstance> result = new ArrayList<>();
        Map<String, Integer> playerSkills = data.get(playerUUID);

        if (playerSkills == null) return result;

        for (Map.Entry<String, Integer> entry : playerSkills.entrySet()) {
            String skillName = entry.getKey();
            int level = entry.getValue();

            PassiveSkill matched = ReadConfig.passiveSkills.stream()
                    .filter(s -> s.name.equals(skillName))
                    .findFirst()
                    .orElse(null);

            if (matched != null) {
                result.add(new PassiveSkillInstance(matched, level));
            }
        }

        return result;
    }

    public boolean addPassiveSkillLevel(UUID playerUUID, String skillName, int level) {
        int currentLevel = getPassiveSkillLevel(playerUUID, skillName);
        if (currentLevel == 0 && level < 0) {
            return false;
        }
        if (currentLevel + level < 0) {
            removePassiveSkill(playerUUID, skillName);
        }
        else setPassiveSkill(playerUUID, skillName, Math.min(currentLevel + level, 20));
        return true;
    }

    public void setPassiveSkill(UUID playerUUID, String skillName, int level) {
        data.computeIfAbsent(playerUUID, k -> new HashMap<>()).put(skillName, level);
        setDirty();
    }
    public void removePassiveSkill(UUID playerUUID, String skillName) {
        Map<String, Integer> playerSkills = data.get(playerUUID);
        if (playerSkills != null) {
            playerSkills.remove(skillName);
            if (playerSkills.isEmpty()) {
                data.remove(playerUUID);
            }
            setDirty();
        }
    }
    public boolean hasPassiveSkill(UUID playerUUID, String skillName) {
        Map<String, Integer> playerSkills = data.get(playerUUID);
        return playerSkills != null && playerSkills.containsKey(skillName);
    }
    public int getPassiveSkillLevel(UUID playerUUID, String skillName) {
        Map<String, Integer> playerSkills = data.get(playerUUID);
        return playerSkills != null ? playerSkills.getOrDefault(skillName, 0) : 0;
    }
    public void clearPassiveSkills(UUID playerUUID) {
        data.remove(playerUUID);
        setDirty();
    }

}
