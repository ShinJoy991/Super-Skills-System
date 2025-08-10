package com.github.shinjoy991.superskillssystem.helpers.saveddata;

import com.github.shinjoy991.superskillssystem.helpers.skill.SectTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerSectTypeSavedData extends SavedData {
    private static final String DATA_NAME = "player_sect_type_data";

    private final HashMap<UUID, SectTypes> data = new HashMap<>();

    public static PlayerSectTypeSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                PlayerSectTypeSavedData::load,
                PlayerSectTypeSavedData::new,
                DATA_NAME
        );
    }

    public PlayerSectTypeSavedData() {}

    public void set(UUID uuid, SectTypes sectType) {
        data.put(uuid, sectType);
        setDirty();
    }

    public SectTypes get(UUID uuid) {
        return data.getOrDefault(uuid, SectTypes.NONE);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag all = new CompoundTag();
        for (Map.Entry<UUID, SectTypes> entry : data.entrySet()) {
            all.putString(entry.getKey().toString(), entry.getValue().name());
        }
        tag.put("data", all);
        return tag;
    }

    public static PlayerSectTypeSavedData load(CompoundTag tag) {
        PlayerSectTypeSavedData savedData = new PlayerSectTypeSavedData();
        CompoundTag all = tag.getCompound("data");
        for (String key : all.getAllKeys()) {
            try {
                UUID uuid = UUID.fromString(key);
                SectTypes sect = SectTypes.valueOf(all.getString(key));
                savedData.data.put(uuid, sect);
            } catch (IllegalArgumentException | NullPointerException e) {
                // Nếu enum không hợp lệ hoặc uuid lỗi
                savedData.data.put(UUID.fromString(key), SectTypes.NONE);
            }
        }
        return savedData;
    }
}