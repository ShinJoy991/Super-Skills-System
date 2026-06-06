//package com.github.shinjoy991.superskillssystem.helpers.saveddata;
//
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.level.saveddata.SavedData;
//
//import java.util.HashMap;
//import java.util.UUID;
//
//public class PlayerDetailStatSavedData extends SavedData {
//    private static final String DATA_NAME = "player_attribute_stat_data";
//
//    private final HashMap<UUID, CompoundTag> data = new HashMap<>();
//
//    public static PlayerDetailStatSavedData get(ServerLevel level) {
//        return level.getDataStorage().computeIfAbsent(
//                PlayerDetailStatSavedData::load,
//                PlayerDetailStatSavedData::new,
//                DATA_NAME
//        );
//    }
//
//    public static PlayerDetailStatSavedData load(CompoundTag tag) {
//        PlayerDetailStatSavedData data = new PlayerDetailStatSavedData();
//        CompoundTag playersTag = tag.getCompound("Players");
//
//        for (String key : playersTag.getAllKeys()) {
//            try {
//                UUID uuid = UUID.fromString(key);
//                CompoundTag playerTag = playersTag.getCompound(key);
//                data.data.put(uuid, playerTag);
//            } catch (IllegalArgumentException ignored) {
//                // Skip invalid UUID
//            }
//        }
//
//        return data;
//    }
//
//    @Override
//    public CompoundTag save(CompoundTag tag) {
//        CompoundTag playersTag = new CompoundTag();
//        for (HashMap.Entry<UUID, CompoundTag> entry : data.entrySet()) {
//            playersTag.put(entry.getKey().toString(), entry.getValue());
//        }
//        tag.put("Players", playersTag);
//        return tag;
//    }
//
//    // ======= Generic helper =======
//    private float getAttribute(UUID uuid, String key) {
//        CompoundTag playerData = data.get(uuid);
//        if (playerData != null && playerData.contains(key)) {
//            return playerData.getFloat(key);
//        }
//        return 0;
//    }
//
//    private void addAttribute(UUID uuid, String key, float amount) {
//        CompoundTag playerData = data.computeIfAbsent(uuid, k -> new CompoundTag());
//        int current = playerData.getInt(key);
//        playerData.putFloat(key, current + amount);
//        setDirty();
//    }
//
//    public float WeaponDmgBonus(UUID uuid) {
//        return getAttribute(uuid, "WeaponDmgBonus");
//    }
//    public float getRangeDmg(UUID uuid) {
//        return getAttribute(uuid, "RangeDmg");
//    }
//    public float getMagicDmg(UUID uuid) {
//        return getAttribute(uuid, "MagicDmg");
//    }
//    public float getDef(UUID uuid) {
//        return getAttribute(uuid, "Def");
//    }
//    public float getDefPen(UUID uuid) {
//        return getAttribute(uuid, "DefPen");
//    }
//    public float getMagicDef(UUID uuid) {
//        return getAttribute(uuid, "MagicDef");
//    }
//    public float getMagicPen(UUID uuid) {
//        return getAttribute(uuid, "MagicPen");
//    }
//    public float getCritChance(UUID uuid) {
//        return getAttribute(uuid, "CritChance");
//    }
//    public float getCritDmg(UUID uuid) {
//        return getAttribute(uuid, "CritDmg");
//    }
//    public float getAttackSpeed(UUID uuid) {
//        return getAttribute(uuid, "AttackSpeed");
//    }
//    public float getSpeed(UUID uuid) {
//        return getAttribute(uuid, "Speed");
//    }
//    public float getDmgRed(UUID uuid) {
//        return getAttribute(uuid, "DmgRed");
//    }
//    public float getMagicResist(UUID uuid) {
//        return getAttribute(uuid, "MagicResist");
//    }
//    public float getEvasion(UUID uuid) {
//        return getAttribute(uuid, "Evasion");
//    }
//    public float getAccuracy(UUID uuid) {
//        return getAttribute(uuid, "Accuracy");
//    }
//    public float getCounterChance(UUID uuid) {
//        return getAttribute(uuid, "CounterChance");
//    }
//    public float getResistance(UUID uuid) {
//        return getAttribute(uuid, "Resistance");
//    }
//    public float getLifeSteal(UUID uuid) {
//        return getAttribute(uuid, "LifeSteal");
//    }
//    public float getManaSteal(UUID uuid) {
//        return getAttribute(uuid, "Mana Steal");
//    }
//    public float getHealRegen(UUID uuid) {
//        return getAttribute(uuid, "HealRegen");
//    }
//    public float getManaRegen(UUID uuid) {
//        return getAttribute(uuid, "ManaRegen");
//    }
//    public float getDmgRedPen(UUID uuid) {
//        return getAttribute(uuid, "DmgRedPen");
//    }
//    public float getMagicResistPen(UUID uuid) {
//        return getAttribute(uuid, "MagicResistPen");
//    }
//    public float getResistancePen(UUID uuid) {
//        return getAttribute(uuid, "ResistancePen");
//    }
//
//    public void addWeaponDmgBonus(UUID uuid, float amount) {
//        addAttribute(uuid, "WeaponDmgBonus", amount);
//    }
//    public void addRangeDmg(UUID uuid, float amount) {
//        addAttribute(uuid, "RangeDmg", amount);
//    }
//    public void addMagicDmg(UUID uuid, float amount) {
//        addAttribute(uuid, "MagicDmg", amount);
//    }
//    public void addDef(UUID uuid, float amount) {
//        addAttribute(uuid, "Def", amount);
//    }
//    public void addDefPen(UUID uuid, float amount) {
//        addAttribute(uuid, "DefPen", amount);
//    }
//    public void addMagicDef(UUID uuid, float amount) {
//        addAttribute(uuid, "MagicDef", amount);
//    }
//    public void addMagicPen(UUID uuid, float amount) {
//        addAttribute(uuid, "MagicPen", amount);
//    }
//    public void addCritChance(UUID uuid, float amount) {
//        addAttribute(uuid, "CritChance", amount);
//    }
//    public void addCritDmg(UUID uuid, float amount) {
//        addAttribute(uuid, "CritDmg", amount);
//    }
//    public void addAttackSpeed(UUID uuid, float amount) {
//        addAttribute(uuid, "AttackSpeed", amount);
//    }
//    public void addSpeed(UUID uuid, float amount) {
//        addAttribute(uuid, "Speed", amount);
//    }
//    public void addDmgRed(UUID uuid, float amount) {
//        addAttribute(uuid, "DmgRed", amount);
//    }
//    public void addMagicResist(UUID uuid, float amount) {
//        addAttribute(uuid, "MagicResist", amount);
//    }
//    public void addEvasion(UUID uuid, float amount) {
//        addAttribute(uuid, "Evasion", amount);
//    }
//    public void addAccuracy(UUID uuid, float amount) {
//        addAttribute(uuid, "Accuracy", amount);
//    }
//    public void addCounterChance(UUID uuid, float amount) {
//        addAttribute(uuid, "CounterChance", amount);
//    }
//    public void addResistance(UUID uuid, float amount) {
//        addAttribute(uuid, "Resistance", amount);
//    }
//    public void addLifeSteal(UUID uuid, float amount) {
//        addAttribute(uuid, "LifeSteal", amount);
//    }
//    public void addManaSteal(UUID uuid, float amount) {
//        addAttribute(uuid, "ManaSteal", amount);
//    }
//    public void addHealRegen(UUID uuid, float amount) {
//        addAttribute(uuid, "HealRegen", amount);
//    }
//    public void addManaRegen(UUID uuid, float amount) {
//        addAttribute(uuid, "ManaRegen", amount);
//    }
//    public void addDmgRedPen(UUID uuid, float amount) {
//        addAttribute(uuid, "DmgRedPen", amount);
//    }
//    public void addMagicResistPen(UUID uuid, float amount) {
//        addAttribute(uuid, "MagicResistPen", amount);
//    }
//    public void addResistancePen(UUID uuid, float amount) {
//        addAttribute(uuid, "ResistancePen", amount);
//    }
//
//}