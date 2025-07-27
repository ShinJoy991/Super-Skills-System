package com.github.shinjoy991.superskillssystem.helpers.saveddata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class PrimeExpSavedData extends SavedData {
    private static final String DATA_NAME = "prime_exp_saved_data";

    private static final int TARGET_LEVEL = 100;         // Cấp mục tiêu (ví dụ: 100)
    private static final int TARGET_TOTAL_EXP = 100_00; // Tổng EXP cần để đạt cấp đó
    private static final double BASE_EXP;
    static {
        // Tính BASE_EXP để sao cho calculateExpForLevel(100) ≈ 1_000_0
        double sum = 0;
        for (int i = 1; i <= TARGET_LEVEL; i++) {
            sum += i * Math.log(i + 1);  // giữ hàm tăng EXP
        }
        BASE_EXP = TARGET_TOTAL_EXP / sum;
    }

    private final Map<UUID, Integer> data = new HashMap<>();

    public void setExp(UUID playerUUID, int exp) {
        data.put(playerUUID, exp);
        setDirty(); // Đánh dấu dữ liệu thay đổi để Minecraft tự save
    }

    public int getExp(UUID playerUUID) {
        return data.getOrDefault(playerUUID, 0);
    }

    public boolean has(UUID playerUUID) {
        return data.containsKey(playerUUID);
    }

    public void addExp(UUID playerUUID, int amount) {
        int current = data.getOrDefault(playerUUID, 0);
        data.put(playerUUID,Math.max(0, current + amount));
        setDirty();
    }


    // Ghi dữ liệu ra NBT
    @Override
    public CompoundTag save(CompoundTag tag) {
        CompoundTag allData = new CompoundTag();
        for (Map.Entry<UUID, Integer> entry : data.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putUUID("uuid", entry.getKey());
            entryTag.putInt("exp", entry.getValue());
            allData.put(entry.getKey().toString(), entryTag);
        }
        tag.put("primeExpData", allData);
        return tag;
    }

    // Đọc dữ liệu từ NBT
    public static PrimeExpSavedData load(CompoundTag tag) {
        PrimeExpSavedData savedData = new PrimeExpSavedData();
        CompoundTag allData = tag.getCompound("primeExpData");

        for (String key : allData.getAllKeys()) {
            CompoundTag entryTag = allData.getCompound(key);
            UUID uuid = entryTag.getUUID("uuid");
            int exp = entryTag.getInt("exp");
            savedData.data.put(uuid, exp);
        }

        return savedData;
    }

    // Truy cập từ ServerLevel
    public static PrimeExpSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                PrimeExpSavedData::load,
                PrimeExpSavedData::new,
                DATA_NAME
        );
    }






}