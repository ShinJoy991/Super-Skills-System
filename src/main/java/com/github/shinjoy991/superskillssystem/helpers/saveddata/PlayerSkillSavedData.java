package com.github.shinjoy991.superskillssystem.helpers.saveddata;

import com.github.shinjoy991.superskillssystem.helpers.skill.SkillData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSkillSavedData extends SavedData {

    private static final String DATA_NAME = "player_skill_data";
    private final Map<UUID, Map<String, SkillData>> data = new HashMap<>();

    public static PlayerSkillSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                PlayerSkillSavedData::load,
                PlayerSkillSavedData::new,
                DATA_NAME
        );
    }

    @Override
    public CompoundTag save(CompoundTag tag) {

        for (Map.Entry<UUID, Map<String, SkillData>> playerEntry : data.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            for (Map.Entry<String, SkillData> skillEntry : playerEntry.getValue().entrySet()) {
                CompoundTag skillTag = new CompoundTag();
                SkillData skillData = skillEntry.getValue();
                skillTag.putInt("level", skillData.getLevel());
                if (skillData.getId() != null) {
                    skillTag.putString("id", skillData.getId());
                }
                playerTag.put(skillEntry.getKey(), skillTag);
            }
            tag.put(playerEntry.getKey().toString(), playerTag);
        }

        return tag;
    }

    public static PlayerSkillSavedData load(CompoundTag tag) {
        PlayerSkillSavedData savedData = new PlayerSkillSavedData();
        for (String uuidString : tag.getAllKeys()) {
            UUID uuid = UUID.fromString(uuidString);
            CompoundTag playerTag = tag.getCompound(uuidString);
            Map<String, SkillData> skills = new HashMap<>();
            for (String skillName : playerTag.getAllKeys()) {
                CompoundTag skillTag = playerTag.getCompound(skillName);
                int level = skillTag.getInt("level");
                String id = skillTag.contains("id")
                        ? skillTag.getString("id")
                        : null;
                skills.put(skillName, new SkillData(level, id));
            }
            savedData.data.put(uuid, skills);
        }

        return savedData;
    }

    public void setSkill(UUID playerUUID, String skillName, int level, String id) {
        data.computeIfAbsent(playerUUID, k -> new HashMap<>())
                .put(skillName, new SkillData(level, id));

        setDirty();
    }

    public SkillData getSkill(UUID playerUUID, String skillName) {
        Map<String, SkillData> skills = data.get(playerUUID);
        if (skills == null) {
            return null;
        }
        return skills.get(skillName);
    }

    public int getSkillLevel(UUID playerUUID, String skillName) {
        SkillData skill = getSkill(playerUUID, skillName);
        return skill == null ? 0 : skill.getLevel();
    }

    public String getSkillId(UUID playerUUID, String skillName) {
        SkillData skill = getSkill(playerUUID, skillName);
        return skill == null ? null : skill.getId();
    }

    public boolean hasSkill(UUID playerUUID, String skillName) {
        Map<String, SkillData> skills = data.get(playerUUID);
        return skills != null && skills.containsKey(skillName);
    }
    public void clearSkills(UUID playerUUID) {
        data.remove(playerUUID);
        setDirty();
    }
    public Map<String, SkillData> getSkills(UUID playerUUID) {
        return data.getOrDefault(playerUUID, Collections.emptyMap());
    }

    public void clearPassiveSkills(UUID playerUUID) {
        Map<String, SkillData> skills = data.get(playerUUID);

        if (skills == null) {
            return;
        }

        skills.entrySet().removeIf(entry ->
                entry.getValue().getId() == null
        );

        if (skills.isEmpty()) {
            data.remove(playerUUID);
        }

        setDirty();
    }
    public void removeSkill(UUID playerUUID, String skillName) {
        Map<String, SkillData> skills = data.get(playerUUID);

        if (skills != null) {
            skills.remove(skillName);

            if (skills.isEmpty()) {
                data.remove(playerUUID);
            }

            setDirty();
        }
    }
}