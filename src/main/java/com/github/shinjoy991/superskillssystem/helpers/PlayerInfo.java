package com.github.shinjoy991.superskillssystem.helpers;

import com.github.shinjoy991.superskillssystem.Config;
import com.github.shinjoy991.superskillssystem.config.ReadConfig;
import com.github.shinjoy991.superskillssystem.helpers.saveddata.*;
import com.github.shinjoy991.superskillssystem.helpers.skill.*;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.client.InfoChangeUpdateS2C;
import com.github.shinjoy991.superskillssystem.network.client.PlayerDataClientInit;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;

import static com.github.shinjoy991.superskillssystem.event.server.PlayerEvents.refreshPlayerHealth;
import static com.github.shinjoy991.superskillssystem.helpers.Calculation.*;
import static com.github.shinjoy991.superskillssystem.helpers.HelperFunction.refreshPlayerAttributes;
import static com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill.sterilizeGlobalPassiveSkillsList;
import static com.mojang.text2speech.Narrator.LOGGER;

public class PlayerInfo {

    private final ServerLevel serverLevelData;
    private ServerPlayer finalPlayer;
    private float perfection;
    private String name;
    private UUID uuid;
    private SectTypes sect;
    //    private Integer ConfigMul;
    // Player attributes
    private float mana;
    private float maxMana;
    private float primeExp;
    private float primeLevel;
    private float expInCurrentLevel;
    private float expToNextLevel;
    private float totalAttPoint;
    private float usedAttPoint;
    private float availableAttPoint;
    private float StrPoint;
    private float VitPoint;
    private float AgiPoint;
    private float IntPoint;
    private float PerPoint;
    private float TotalStr;
    private float TotalVit;
    private float TotalAgi;
    private float TotalInt;
    private float TotalPer;
    // Detail Screen
    private float weaponPercentDmgBonus;
    private float AtkDmg;
    private float RangeDmg;
    private float MagicDmg;
    private float Def;
    private float DefPen;
    private float MagicDef;
    private float MagicPen;
    private float CritChance;
    private float AttackSpeed;
    private float Speed;
    private float DmgRed; // All Damage Reduction
    private float MagicResist; // Magic Damage Immunity
    private float Evasion; // Chance to evade physical attacks
    private float Accuracy; // Chance to hit a target with physical attacks
    private float CounterChance; // Chance to counter physical attacks
    private float Resistance; // Resist bad effects (poison, burn, freeze, etc.)
    private float LifeSteal; // Chance to steal life from the target
    private float ManaSteal; // Chance to steal mana from the target
    private float HealRegen; // Health regeneration per second
    private float ManaRegen; // Mana regeneration per second
    private float DmgRedPen;
    private float MagicResistPen;
    private float ResistancePen;

    private List<PassiveSkillInstance> passiveSkills = new ArrayList<>();
    private HashMap<SkillTags, Float> skillBonusMap = new HashMap<>();
    private HashMap<SkillTags, Float> equipmentBonusMap = new HashMap<>();
    // Active skill storage (server-side persisted IDs and levels)
    // Map<skillId, level>
    private Map<String, Integer> activeSkills = new LinkedHashMap<>();

    private String activeSkillSlot1 = "";
    private String activeSkillSlot2 = "";
    private String activeSkillSlot3 = "";
    private String activeSkillSlot4 = "";

    public PlayerInfo(ServerLevel serverLevelData, UUID uuid) {
        this.serverLevelData = serverLevelData;
        this.finalPlayer = findPlayerByUUID(uuid);

        if (finalPlayer == null) {
//            System.out.println("Player with UUID " + uuid + " not found on server.");
            return;
        }
        PrimeExpSavedData primeExpData = PrimeExpSavedData.get(serverLevelData);
        PlayerAttPointSavedData attPointData = PlayerAttPointSavedData.get(serverLevelData);
        PlayerManaSavedData manaData = PlayerManaSavedData.get(serverLevelData);
//        PlayerDetailStatSavedData attStatData = PlayerDetailStatSavedData.get(serverLevelData);
        PlayerSkillSavedData playerSkillData = PlayerSkillSavedData.get(serverLevelData);
        PlayerSectTypeSavedData sectTypeData = PlayerSectTypeSavedData.get(serverLevelData);

        this.name = finalPlayer.getName().getString();
        this.uuid = uuid;

        // Load skills
        Map<String, SkillData> skills = playerSkillData.getSkills(uuid);
        for (Map.Entry<String, SkillData> entry : skills.entrySet()) {

            SkillData skillData = entry.getValue();

            if (skillData.getId() != null) {
                activeSkills.put(skillData.getId(), skillData.getLevel());
                continue;
            }

            PassiveSkill matched = ReadConfig.passiveSkills.stream()
                    .filter(s -> s.name.equals(entry.getKey()))
                    .findFirst()
                    .orElse(null);

            if (matched != null) {
                passiveSkills.add(
                        new PassiveSkillInstance(
                                matched,
                                skillData.getLevel()
                        )
                );
            }
        }

        this.skillBonusMap = this.getSkillBonusMap();
        this.equipmentBonusMap = this.getEquipmentBonusMap();

        this.sect = sectTypeData.get(uuid);
        this.primeExp = primeExpData.getExp(uuid);
        this.primeLevel = calLevelByExp((int) this.primeExp);
        this.expInCurrentLevel = calCurrentLevelExp((int) this.primeExp);
        this.expToNextLevel = calExpForLevel((int) (this.primeLevel + 1));

        this.totalAttPoint = primeLevel * 3;
        this.usedAttPoint = attPointData.getUsedAttPoints(uuid);
        this.availableAttPoint = Math.max(0, this.totalAttPoint - this.usedAttPoint);

        this.StrPoint = attPointData.getStrPoint(uuid);
        this.VitPoint = attPointData.getVitPoint(uuid);
        this.AgiPoint = attPointData.getAgiPoint(uuid);
        this.IntPoint = attPointData.getIntPoint(uuid);
        this.PerPoint = attPointData.getPerPoint(uuid);
        updateTotalStr();
        updateTotalVit();
        updateTotalAgi();
        initTotalInt();
        initTotalPer();
        // Calculate mana and maxMana
        this.mana = manaData.getMana(uuid);
        this.updateMaxMana();

        this.activeSkillSlot1 = attPointData.getActiveSkillSlot(uuid, 1);
        this.activeSkillSlot2 = attPointData.getActiveSkillSlot(uuid, 2);
        this.activeSkillSlot3 = attPointData.getActiveSkillSlot(uuid, 3);
        this.activeSkillSlot4 = attPointData.getActiveSkillSlot(uuid, 4);

        this.updatePerfectionBonus();
        this.updateAtkDmg();
        this.updateRangeDmg();
        this.updateMagicDmg();
        this.updateDef();
        this.updateDefPen();
        this.updateMagDef();
        this.updateMagResist();
        this.updateAttackSpeed();
        this.updateDmgRed();
        this.updateEvasion();
        this.updateAccuracy();
        this.updateHealRegen();
        this.updateSpeed();
        this.updateCritChance();
        this.updateCounterChance();
        this.updateResistance();
        this.updateLifeSteal();
        this.updateManaSteal();
        this.updateManaRegen();
        this.updateDmgRedPen();
        this.updateMagicPen();
        this.updateResistancePen();
        this.updateWeaponPercentDmgBonus();
        this.updateMagicResistPen();
    }


    private void updateMagicResistPen() {
        this.MagicResistPen = skillBonusMap.getOrDefault(SkillTags.MAGIC_RESISTANCE, 0f)
                + equipmentBonusMap.getOrDefault(SkillTags.MAGIC_RESISTANCE_PEN, 0f);
    }

    private void updateResistancePen() {
        this.ResistancePen = skillBonusMap.getOrDefault(SkillTags.RESISTANCE_PEN, 0f)
                + equipmentBonusMap.getOrDefault(SkillTags.RESISTANCE_PEN, 0f);
    }

    private void updateDmgRedPen() {
        this.DmgRedPen = skillBonusMap.getOrDefault(SkillTags.DMG_REDUCTION_PEN, 0f)
                + equipmentBonusMap.getOrDefault(SkillTags.DMG_REDUCTION_PEN, 0f);
    }

    private void updateManaSteal() {
        this.ManaSteal = skillBonusMap.getOrDefault(SkillTags.MANA_STEAL, 0f)
                + equipmentBonusMap.getOrDefault(SkillTags.MANA_STEAL, 0f);
    }

    private void updateLifeSteal() {
        this.LifeSteal = skillBonusMap.getOrDefault(SkillTags.LIFE_STEAL, 0f)
                + equipmentBonusMap.getOrDefault(SkillTags.LIFE_STEAL, 0f);
    }

    private void updateResistance() {
        float resistance = this.TotalVit * 0.1f + this.TotalPer * 0.05f;
        resistance += skillBonusMap.getOrDefault(SkillTags.RESISTANCE, 0f) + equipmentBonusMap.getOrDefault(SkillTags.RESISTANCE, 0f);
        this.Resistance = resistance;
    }
    private void updateCounterChance() {
        this.CounterChance = skillBonusMap.getOrDefault(SkillTags.COUNTER_CHANCE, 0f)
                + equipmentBonusMap.getOrDefault(SkillTags.COUNTER_CHANCE, 0f);
    }

    private void updateCritChance() {
        this.CritChance = skillBonusMap.getOrDefault(SkillTags.CRIT_CHANCE, 0f)
                + equipmentBonusMap.getOrDefault(SkillTags.CRIT_CHANCE, 0f);
    }

    private void updateWeaponPercentDmgBonus() {
        float cal = skillBonusMap.getOrDefault(SkillTags.WEAPON_DMG_PERCENT, 0f)
                + equipmentBonusMap.getOrDefault(SkillTags.WEAPON_DMG_PERCENT, 0f);
        this.weaponPercentDmgBonus = cal;
    }
    public float getWeaponPercentDmgBonus() {
        return this.weaponPercentDmgBonus;
    }

    public void updateRangeDmg() {
        float rangeDmg = this.TotalAgi * 0.1f + this.TotalStr * 0.025f;
        this.RangeDmg = rangeDmg * (1 + 0.01f *
                (skillBonusMap.getOrDefault(SkillTags.RANGE_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.RANGE_PERCENT, 0f)))
                + skillBonusMap.getOrDefault(SkillTags.RANGE_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.RANGE_FLAT, 0f);

    }
    public float getRangeDmg() {
        return (float) (this.RangeDmg * Config.SCALE_DMG);
    }
    private void updateAtkDmg() {
        float player_atk = 1;
        float atk = player_atk + this.TotalStr * 0.1f + this.TotalAgi * 0.025f;
        float calAtk = atk * (1 + 0.01f *
                (skillBonusMap.getOrDefault(SkillTags.ATK_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.ATK_PERCENT, 0f)))
                + skillBonusMap.getOrDefault(SkillTags.ATK_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.ATK_FLAT, 0f);
        ServerPlayer serverPlayer = (ServerPlayer) serverLevelData.getPlayerByUUID(this.uuid);
//        System.out.println("calAtk: " + calAtk + " atk: " + atk + " player_atk: " + player_atk +" att: "+ ((float) serverPlayer.getAttributeValue(Attributes.ATTACK_DAMAGE)));

        this.AtkDmg = calAtk;
    }
    public float getAtkDmg() {
        return (float) (this.AtkDmg * Config.SCALE_DMG);
    }

    // Active skill slot setters/getters
//    public void setActiveSkillSlot(int slot, ResourceLocation rl) {
//        String s = rl == null ? null : rl.toString();
//        switch (slot) {
//            case 1 -> this.activeSkillSlot1 = s;
//            case 2 -> this.activeSkillSlot2 = s;
//            case 3 -> this.activeSkillSlot3 = s;
//            case 4 -> this.activeSkillSlot4 = s;
//        }
//    }
//
//    public String getActiveSkillSlotString(int slot) {
//        return switch (slot) {
//            case 1 -> this.activeSkillSlot1;
//            case 2 -> this.activeSkillSlot2;
//            case 3 -> this.activeSkillSlot3;
//            case 4 -> this.activeSkillSlot4;
//            default -> null;
//        };
//    }

    public static ServerPlayer findPlayerByUUID(UUID uuid) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        for (ServerLevel level : server.getAllLevels()) {
            ServerPlayer player = (ServerPlayer) level.getPlayerByUUID(uuid);
            if (player != null) return player;
        }
        return null;
    }

    public String getName() {
        return name;
    }
    public int getPrimeExp() {
        return (int) primeExp;
    }

    public void addPrimeExp(int amount) {
        // Cộng (hoặc trừ nếu amount âm) vào tổng EXP tích lũy
        int newExp = Math.max(0, (int) this.primeExp + amount);
        this.primeExp = newExp;
        updateStats(newExp);

        // Nếu exp bị trừ về 0 (hoặc âm), reset stat phân phối
        if (newExp == 0) {
            resetStatPoints();
        }
    }
    private void resetStatPoints() {
        this.usedAttPoint = 0;
        this.StrPoint = 0;
        this.VitPoint = 0;
        this.AgiPoint = 0;
        this.IntPoint = 0;
        this.PerPoint = 0;

        PlayerAttPointSavedData att = PlayerAttPointSavedData.get(serverLevelData);
        att.setUsedAttPoints(uuid, 0);
        att.setStrPoint(uuid, 0);
        att.setVitPoint(uuid, 0);
        att.setAgiPoint(uuid, 0);
        att.setIntPoint(uuid, 0);
        att.setPerPoint(uuid, 0);
    }
    public void addPrimeLevel(int levelAmount) {
        if (levelAmount == 0) return;

        // Tính level mục tiêu, clamp trong khoảng [0, 99]
        int targetLevel = Math.max(0, Math.min(100, (int) this.primeLevel + levelAmount));

        // Gán thẳng bằng Tổng EXP tích lũy của level mục tiêu
        int newExp = Calculation.calTotalExpForLevel(targetLevel);
        this.primeExp = newExp;
        updateStats(newExp);

        // Nếu level về 0, reset stat phân phối
        if (targetLevel == 0) {
            resetStatPoints();
        }
    }

    // Hàm cập nhật trạng thái chung để tái sử dụng
    private void updateStats(int currentTotalExp) {
        this.primeLevel = Calculation.calLevelByExp(currentTotalExp);
        this.expInCurrentLevel = Calculation.calCurrentLevelExp(currentTotalExp);

        // EXP cần để lên cấp tiếp theo = Tổng EXP cấp kế - Tổng EXP hiện tại
        int nextLevelExpTotal = Calculation.calTotalExpForLevel((int) this.primeLevel + 1);
        this.expToNextLevel = Math.max(0, nextLevelExpTotal - currentTotalExp);

        this.totalAttPoint = (int) this.primeLevel * 3;
        this.availableAttPoint = Math.max(0, this.totalAttPoint - this.usedAttPoint);

        syncPrimeExpToSavedAndClient();
    }
    public void setPrimeExp(int i) {
        // 1. update runtime
        int newExp = Math.max(0, i);
        this.primeExp = newExp;
        this.primeLevel = calLevelByExp(newExp);
        this.expInCurrentLevel = calCurrentLevelExp(newExp);

        // EXP còn thiếu để lên cấp tiếp theo = Tổng EXP cấp kế - tổng EXP hiện tại
        int nextLevelExpTotal = Calculation.calTotalExpForLevel((int) this.primeLevel + 1);
        this.expToNextLevel = Math.max(0, nextLevelExpTotal - newExp);

        this.totalAttPoint = (int) this.primeLevel * 3;
        this.usedAttPoint = 0;
        this.availableAttPoint = Math.max(0, this.totalAttPoint - this.usedAttPoint);

        // 2. sync exp vào savedData
        syncPrimeExpToSavedAndClient();

        // 3. reset và sync stat phân phối
        resetStatPoints();
    }
    private void syncPrimeExpToSavedAndClient() {
        PrimeExpSavedData data = PrimeExpSavedData.get(serverLevelData);
        data.setExp(uuid, (int) this.primeExp);

        CompoundTag sendTag = new CompoundTag();
        sendTag.putInt(SterilizeTags.PRIME_EXP.name(), this.getPrimeExp());
        ModNetworking.INSTANCE.sendTo(new InfoChangeUpdateS2C(sendTag), finalPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
    public int getPrimeLevel() {
        return (int) primeLevel;
    }
    public int getExpInCurrentLevel() {
        return (int) expInCurrentLevel;
    }
    public int getExpToNextLevel() {
        return (int) expToNextLevel;
    }

    public int getTotalAttPoint() {
        return (int) totalAttPoint;
    }
    public int getUsedAttPoint() {
        return (int) usedAttPoint;
    }
    public void addUsedAttPoint(int amount) {
        usedAttPoint += amount;
        availableAttPoint = totalAttPoint - usedAttPoint;
//        System.out.println("Adding Used Att Point: " + amount + " Available: " + availableAttPoint);
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addUsedAttPoints(uuid, amount);
    }
    public void setUsedAttPoint(int amount) {
        this.usedAttPoint = amount;
        this.availableAttPoint = totalAttPoint - usedAttPoint;

        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.setUsedAttPoints(uuid, amount);
    }

    public int getStrPoint() {
        return (int) this.StrPoint;
    }
    public void setStrPoint(int i) {
        this.StrPoint = i;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.setStrPoint(uuid, i);
        updateStrPoint();
    }
    public void addStrPoint(int amount) {
        StrPoint += amount;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addStrPoint(uuid, amount);
        updateStrPoint();
    }

    private void updateStrPoint() {
        this.updateTotalStr();
        updateAtkDmg();
        updateDef();
        updateDefPen();
        updateAttackSpeed();
        updateDmgRed();
        updateAccuracy();
    }

    private void updateTotalStr() {
        float calStr = this.StrPoint * (1 + 0.01f *
                (skillBonusMap.getOrDefault(SkillTags.STR_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.STR_PERCENT, 0f)))
                + skillBonusMap.getOrDefault(SkillTags.STR_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.STR_FLAT, 0f);
        this.TotalStr = Math.round(calStr);
    }
    public int getTotalStr() {
        return (int) this.TotalStr;
    }

    public int getVitPoint() {
        return (int) this.VitPoint;
    }
    public void setVitPoint(int i) {
        this.VitPoint = i;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.setVitPoint(uuid, i);
        updateVitPoint();

    }
    public void addVitPoint(int amount) {
        VitPoint += amount;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addVitPoint(uuid, amount);
        updateVitPoint();
    }

    private void updateVitPoint() {
        this.updateTotalVit();
        updateDef();
        updateDmgRed();
        updateMagDef();
        updateMagResist();
        updateHealRegen();
        updateResistance();
    }

    private void updateTotalVit() {
        float calVit =  this.VitPoint * (1 + 0.01f *
                (skillBonusMap.getOrDefault(SkillTags.VIT_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.VIT_PERCENT, 0f)))
                + skillBonusMap.getOrDefault(SkillTags.VIT_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.VIT_FLAT, 0f);
        this.TotalVit = Math.round(calVit);
    }
    public int getTotalVit() {
        return (int) this.TotalVit;
    }

    public int getAgiPoint() {
        return (int) this.AgiPoint;
    }
    public void setAgiPoint(int i) {
        this.AgiPoint = i;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.setAgiPoint(uuid, i);
        updateAgiPoint();
    }
    public void addAgiPoint(int amount) {
        AgiPoint += amount;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addAgiPoint(uuid, amount);
        updateAgiPoint();
    }

    private void updateAgiPoint() {
        this.updateTotalAgi();
        updateAtkDmg();
        updateRangeDmg();
        updateAttackSpeed();
        updateEvasion();
        updateSpeed();
        updateAccuracy();
    }

    private void updateTotalAgi() {
        float calAgi =  this.AgiPoint * (1 + 0.01f *
                (skillBonusMap.getOrDefault(SkillTags.AGI_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.AGI_PERCENT, 0f)))
                + skillBonusMap.getOrDefault(SkillTags.AGI_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.AGI_FLAT, 0f);
        this.TotalAgi = Math.round(calAgi);
    }
    public int getTotalAgi() {
        return (int) this.TotalAgi;
    }

    public int getIntPoint() {
        return (int) this.IntPoint;
    }
    public void setIntPoint(int i) {
        this.IntPoint = i;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.setIntPoint(uuid, i);
        updateIntPoint();
    }

    public void addIntPoint(int amount) {
        IntPoint += amount;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addIntPoint(uuid, amount);
        updateIntPoint();
    }

    private void updateIntPoint() {
        this.updateTotalInt();
        updateMagicDmg();
        updateMagDef();
        updateMagResist();
        updateMagicPen();
        updateResistance();
    }

    private void updateTotalInt() {
        float calInt =  this.IntPoint * (1 + 0.01f *
                (skillBonusMap.getOrDefault(SkillTags.INT_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.INT_PERCENT, 0f)))
                + skillBonusMap.getOrDefault(SkillTags.INT_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.INT_FLAT, 0f);
        this.TotalInt = Math.round(calInt);
        this.updateMaxMana();
    }
    private void initTotalInt() {
        float calInt =  this.IntPoint * (1 + 0.01f *
                (skillBonusMap.getOrDefault(SkillTags.INT_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.INT_PERCENT, 0f)))
                + skillBonusMap.getOrDefault(SkillTags.INT_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.INT_FLAT, 0f);
        this.TotalInt = Math.round(calInt);
    }
    public int getTotalInt() {
        return (int) this.TotalInt;
    }

    public int getPerPoint() {
        return (int) this.PerPoint;
    }
    public void setPerPoint(int i) {
        this.PerPoint = i;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.setPerPoint(uuid, i);
        updatePerPoint();
    }
    public void addPerPoint(int amount) {
        PerPoint += amount;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addPerPoint(uuid, amount);
        updatePerPoint();
    }

    private void updatePerPoint() {
        this.updateTotalPer();
        updateMagicDmg();
        updateMagDef();
        updateMagResist();
        updateDef();
        updateSpeed();
        updateDmgRed();
        updateResistance();
        updateEvasion();
        updateManaRegen();
    }

    private void updateTotalPer() {
        float calPer =  this.PerPoint * (1 + 0.01f *
                (skillBonusMap.getOrDefault(SkillTags.PER_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.PER_PERCENT, 0f)))
                + skillBonusMap.getOrDefault(SkillTags.PER_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.PER_FLAT, 0f);
        this.TotalPer = Math.round(calPer);
        this.updateMaxMana();
    }
    private void initTotalPer() {
        float calPer =  this.PerPoint * (1 + 0.01f *
                (skillBonusMap.getOrDefault(SkillTags.PER_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.PER_PERCENT, 0f)))
                + skillBonusMap.getOrDefault(SkillTags.PER_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.PER_FLAT, 0f);
        this.TotalPer = Math.round(calPer);
    }
    public int getTotalPer() {
        return (int) this.TotalPer;
    }

    public float getMana() {
        return this.mana;
    }
    public void addMana(float amount) {
        int mana = Math.round(amount);
        if (this.mana + mana > this.maxMana) {
            this.mana = this.maxMana;
        } else if (this.mana + mana < 0) {
            this.mana = 0;
        } else {
            this.mana += mana;
        }
        PlayerManaSavedData data = PlayerManaSavedData.get(serverLevelData);
        data.addMana(uuid, mana);
    }
    public void setMana(float amount) {
        int mana = Math.round(amount);
        this.mana = mana;
        PlayerManaSavedData data = PlayerManaSavedData.get(serverLevelData);
        data.setMana(uuid, mana);
        data.setDirty();
    }
    private void updateMaxMana() {
        float calMaxMana =  (this.TotalInt * 2 + this.TotalPer) * (1 + 0.01f
                * (skillBonusMap.getOrDefault(SkillTags.MANA_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.MANA_PERCENT, 0f)))
                + skillBonusMap.getOrDefault(SkillTags.MANA_FLAT, 0f)
                + equipmentBonusMap.getOrDefault(SkillTags.MANA_FLAT, 0f);
        this.maxMana = Math.round(calMaxMana);
    }



    // Ghi vào tag
    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag skillList = new ListTag();

        for (PassiveSkillInstance instance : passiveSkills) {
            CompoundTag skillTag = new CompoundTag();
            skillTag.putString("SkillName", instance.getName());
            skillTag.putInt("Level", instance.getLevel());
            skillList.add(skillTag);
        }

        tag.put("PassiveSkills", skillList);
        tag.putString(SterilizeTags.SECT.name(), this.sect.name());
        tag.putFloat(SterilizeTags.MANA.name(), this.mana);
        tag.putFloat(SterilizeTags.MAX_MANA.name(), this.maxMana);
        tag.putFloat(SterilizeTags.PRIME_EXP.name(), this.primeExp);
        tag.putFloat(SterilizeTags.USED_ATT_POINT.name(), this.usedAttPoint);
        tag.putFloat(SterilizeTags.STR_POINT.name(), this.StrPoint);
        tag.putFloat(SterilizeTags.VIT_POINT.name(), this.VitPoint);
        tag.putFloat(SterilizeTags.AGI_POINT.name(), this.AgiPoint);
        tag.putFloat(SterilizeTags.INT_POINT.name(), this.IntPoint);
        tag.putFloat(SterilizeTags.PER_POINT.name(), this.PerPoint);
        tag.putFloat(SterilizeTags.TOTAL_STR.name(), this.TotalStr);
        tag.putFloat(SterilizeTags.TOTAL_VIT.name(), this.TotalVit);
        tag.putFloat(SterilizeTags.TOTAL_AGI.name(), this.TotalAgi);
        tag.putFloat(SterilizeTags.TOTAL_INT.name(), this.TotalInt);
        tag.putFloat(SterilizeTags.TOTAL_PER.name(), this.TotalPer);

        ServerPlayer player = findPlayerByUUID(uuid);
        double weaponDmg = 0.0;
        ItemStack stack = player.getMainHandItem();
        if (!stack.isEmpty()) {
            Multimap<Attribute, AttributeModifier> modifiers = stack.getAttributeModifiers(EquipmentSlot.MAINHAND);
            for (AttributeModifier modifier : modifiers.get(Attributes.ATTACK_DAMAGE)) {
                weaponDmg += modifier.getAmount();
            }
        }
        tag.putDouble(SterilizeTags.WEAPON_DMG.name(),weaponDmg);
        tag.putFloat(SterilizeTags.WEAPON_DMG_BONUS.name(), getWeaponPercentDmgBonus());
        tag.putDouble(SterilizeTags.BONUS_DMG.name(), player.getAttributeValue(Attributes.ATTACK_DAMAGE) - 1 - weaponDmg);
        tag.putFloat(SterilizeTags.ATK_DMG.name(), getAtkDmg());
//        System.out.println("Saving ATK DMG: " + (float) (this.AtkDmg * Config.SCALE_DMG));
        tag.putFloat(SterilizeTags.RANGE_DMG.name(), getRangeDmg());
        tag.putFloat(SterilizeTags.MAGIC_DMG.name(), getMagicDmg());
        tag.putFloat(SterilizeTags.DEF.name(), this.Def);
        tag.putFloat(SterilizeTags.DEF_PEN.name(), this.DefPen);
        tag.putFloat(SterilizeTags.MAGIC_DEF.name(), this.MagicDef);
        tag.putFloat(SterilizeTags.MAGIC_PEN.name(), this.MagicPen);
        tag.putFloat(SterilizeTags.CRIT_CHANCE.name(), this.CritChance);
        tag.putFloat(SterilizeTags.PERFECTION.name(), this.perfection);
        tag.putFloat(SterilizeTags.ATTACK_SPEED.name(), this.AttackSpeed);
        tag.putFloat(SterilizeTags.SPEED.name(), this.Speed);
        tag.putFloat(SterilizeTags.DMG_RED.name(), this.DmgRed);
        tag.putFloat(SterilizeTags.MAGIC_RESIST.name(), this.MagicResist);
        tag.putFloat(SterilizeTags.EVASION.name(), this.Evasion);
        tag.putFloat(SterilizeTags.ACCURACY.name(), this.Accuracy);
        tag.putFloat(SterilizeTags.COUNTER_CHANCE.name(), this.CounterChance);
        tag.putFloat(SterilizeTags.RESISTANCE.name(), this.Resistance);
        tag.putFloat(SterilizeTags.LIFE_STEAL.name(), this.LifeSteal);
        tag.putFloat(SterilizeTags.MANA_STEAL.name(), this.ManaSteal);
        tag.putFloat(SterilizeTags.HEAL_REGEN.name(), this.HealRegen);
        tag.putFloat(SterilizeTags.MANA_REGEN.name(), this.ManaRegen);
        tag.putFloat(SterilizeTags.DMG_RED_PEN.name(), this.DmgRedPen);
        tag.putFloat(SterilizeTags.MAGIC_RESIST_PEN.name(), this.MagicResistPen);
        tag.putFloat(SterilizeTags.RESISTANCE_PEN.name(), this.ResistancePen);
        // Save active skills (IDs + level) for client and persistence
        ListTag activeList = new ListTag();
        for (Map.Entry<String, Integer> entry : activeSkills.entrySet()) {
            CompoundTag skillTag = new CompoundTag();
            skillTag.putString("SkillId", entry.getKey());
            skillTag.putInt("Level", entry.getValue());
            activeList.add(skillTag);
        }
        tag.put("ActiveSkills", activeList);

        return tag;
    }

    public boolean addPassiveSkill(String skillName, int addLevel) {
        PassiveSkill matched = ReadConfig.passiveSkills.stream()
                .filter(s -> s.name.equals(skillName))
                .findFirst()
                .orElse(null);

        if (matched == null) {
            LOGGER.error("Player {} trying to assign {} level to passive skill {} not found in config", this.name, addLevel, skillName);
            return false;
        }
        System.out.println("Adding passive skill: " + skillName + " with level: " + addLevel);
        PlayerSkillSavedData data = PlayerSkillSavedData.get(serverLevelData);
        SkillData oldData = data.getSkill(uuid, skillName);

        int levelOld = oldData == null ? 0 : oldData.getLevel();
        int newLevel = Math.max(0, levelOld + addLevel);

        if (newLevel <= 0) {
//            System.out.println("Delete passive skill PlayerInfo");
            data.removeSkill(uuid, skillName);
        } else {
            data.setSkill(uuid, skillName, Math.min(newLevel, 20), null);
        }

        List<SkillTags> updateTags;
        PassiveSkillInstance instance0 = new PassiveSkillInstance(matched, 1);
        updateTags = instance0.getTags();

//        System.out.println("Adding passive skill to saveddata successedd");

            // Cập nhật bonus: trừ skill cũ và cộng skill mới (nếu có)
            for (int sign : new int[]{-1, 1}) {
//                System.out.println("loop with sign: " + sign);
                int level = (sign == -1) ? levelOld : newLevel; // add -100 nen level <0
                if (level <= 0) continue;
//                System.out.println("loop next with sign: " + sign);
                PassiveSkillInstance instance = new PassiveSkillInstance(matched, level);
                for (SkillTags tag : instance.getTags()) {
                    float value = instance.getValue(tag) * sign;
                    skillBonusMap.merge(tag, value, Float::sum);
                }
            }

            this.passiveSkills.removeIf(p -> p.getName().equals(skillName));

            if (newLevel > 0) {
                this.passiveSkills.add(new PassiveSkillInstance(matched, Math.min(newLevel, 20)));
            }
            refreshPlayerHealth(finalPlayer);
            this.update(updateTags); // lag
            CompoundTag sendTag = this.saveToNBT();
            ModNetworking.INSTANCE.sendTo(
                    new InfoChangeUpdateS2C(sendTag), finalPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
//            System.out.println("Adding passive skill success3");

//        System.out.println("Adding passive skill success4");
        return true;
    }
    public boolean removePassiveSkill(String skillName) {
        return addPassiveSkill(skillName, -100);
    }

    public boolean removeActiveSkill(String skillId) {
        return addActiveSkill(skillId, -100);
    }
    public boolean addActiveSkill(String skillId, int addLevel) {

        PlayerSkillSavedData data = PlayerSkillSavedData.get(serverLevelData);
        SkillData oldData = data.getSkill(uuid, skillId);
        int levelOld = oldData == null ? 0 : oldData.getLevel();
        int newLevel = Math.max(0, levelOld + addLevel);

        boolean success = !(levelOld == 0 && addLevel < 0);
        if (newLevel <= 0) {

            data.removeSkill(uuid, skillId);
            activeSkills.remove(skillId);

        } else {
            int finalLevel = Math.min(newLevel, 20);
            data.setSkill(uuid, skillId, finalLevel, skillId);
            activeSkills.put(skillId, finalLevel);
        }
        CompoundTag sendTag = this.saveToNBT();
        ModNetworking.INSTANCE.sendTo(
                new InfoChangeUpdateS2C(sendTag), finalPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        return success;
    }

    public PassiveSkillInstance getPassiveSkills(String skillName) {
        if (this.passiveSkills.isEmpty()) return null;
        for (PassiveSkillInstance skill : this.passiveSkills) {
            if (skill.getName().equals(skillName)) {
                return skill;
            }
        }
        return null;
    }
    public void resetPassiveSkills() {
        PlayerSkillSavedData data = PlayerSkillSavedData.get(serverLevelData);
        data.clearPassiveSkills(uuid);
        this.passiveSkills.clear();

        CompoundTag tag = new CompoundTag();
        ListTag skillList = new ListTag();

        for (PassiveSkillInstance instance : passiveSkills) {
            CompoundTag skillTag = new CompoundTag();
            skillTag.putString("SkillName", instance.getName());
            skillTag.putInt("Level", instance.getLevel());
            skillList.add(skillTag);
        }

        tag.put("PassiveSkills", skillList);
        ModNetworking.INSTANCE.sendTo(new InfoChangeUpdateS2C(tag), finalPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public List<PassiveSkillInstance> getPassiveSkillsInstances() {
        return this.passiveSkills;
    }

    public Map<String, Integer> getActiveSkillsMap() {
        return new HashMap<>(this.activeSkills);
    }

    public SkillData getSkillData(String skillName) {
        return PlayerSkillSavedData.get(serverLevelData).getSkill(uuid, skillName);
    }
    private void updatePerfectionBonus() {
        float perfection = 0f;
        for (Map.Entry<SkillTags, Float> tag : skillBonusMap.entrySet()) {
            if (tag.getKey() == SkillTags.PERFECTION) {
                perfection += tag.getValue();
                break;
            }
        }
        for (Map.Entry<SkillTags, Float> tag : equipmentBonusMap.entrySet()) {
            if (tag.getKey() == SkillTags.PERFECTION) {
                perfection += tag.getValue();
                break;
            }
        }
        this.perfection = perfection;
    }

    private HashMap<SkillTags, Float> getSkillBonusMap() {
        for (SkillTags tag : SkillTags.values()) {
            skillBonusMap.put(tag, 0f);
        }
        HashMap<SkillTags, Float> bonusMap = new HashMap<>();
        for (PassiveSkillInstance skill : this.passiveSkills) {
            if (skill.getTags() != null) {
                for (SkillTags tag : skill.getTags()) {
                    bonusMap.merge(tag, skill.getValue(tag), Float::sum);
                }
            }
        }
        return bonusMap;
    }

    private HashMap<SkillTags, Float> getEquipmentBonusMap() {
        HashMap<SkillTags, Float> bonusMap = new HashMap<>();
        for (SkillTags tag : SkillTags.values()) {
            bonusMap.put(tag, 0f);
        }

        Player thisPlayer =  serverLevelData.getPlayerByUUID(this.uuid);
            if (thisPlayer == null) {
                return bonusMap;
            }
            if (!(thisPlayer instanceof ServerPlayer serverPlayer)) {
                return bonusMap;
            }
            for (ItemStack armor : serverPlayer.getInventory().armor) {
                if (!armor.isEmpty() && armor.hasTag()) {
                    CompoundTag tag = armor.getTag();
                    if (tag != null && tag.contains("SSSSkillBonus", Tag.TAG_COMPOUND)) {
                        CompoundTag skillTag = tag.getCompound("SSSSkillBonus");

                        for (SkillTags skill : SkillTags.values()) {
                            if (skillTag.contains(skill.name(), Tag.TAG_FLOAT)) {
                                float value = skillTag.getFloat(skill.name());
                                bonusMap.merge(skill, value, Float::sum);
                            }
                        }
                    }
                }
            }


        return bonusMap;
    }

    public void updateDef() {
        float def = this.TotalVit * 0.2f + this.TotalStr * 0.05f;
        def *= (1 + 0.01f * (skillBonusMap.getOrDefault(SkillTags.DEF_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.DEF_PERCENT, 0f)));
        def += skillBonusMap.getOrDefault(SkillTags.DEF_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.DEF_FLAT, 0f);
        this.Def = def;
    }

    public float getDef() {
        return this.Def;
    }

    public void updateDefPen() {
        float defPen = this.TotalStr * 0.05f + this.TotalAgi * 0.2f;
        defPen *= (1 + 0.01f * (skillBonusMap.getOrDefault(SkillTags.DEF_PEN_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.DEF_PEN_PERCENT, 0f)));
        defPen += skillBonusMap.getOrDefault(SkillTags.DEF_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.DEF_FLAT, 0f);
        this.DefPen = defPen;
    }

    public float getDefPen() {
        return this.DefPen;
    }

    public void updateAttackSpeed() {
        float attackSpeed = this.TotalAgi * 0.01f + this.TotalStr * 0.005f;
        attackSpeed += skillBonusMap.getOrDefault(SkillTags.ATTACK_SPEED, 0f) + equipmentBonusMap.getOrDefault(SkillTags.ATTACK_SPEED, 0f);
        this.AttackSpeed =  attackSpeed;

        if (finalPlayer != null
                && !finalPlayer.hasDisconnected()
                && !finalPlayer.isRemoved()) {
            refreshPlayerAttributes(this.finalPlayer, SkillTags.ATTACK_SPEED, this.AttackSpeed);
        }
    }

    public float getAttackSpeed() {
        return this.AttackSpeed;
    }

    public void updateDmgRed() {
        float dmgRed = this.TotalVit * 0.2f + this.TotalStr * 0.15f;
        dmgRed += skillBonusMap.getOrDefault(SkillTags.DMG_REDUCTION, 0f) + equipmentBonusMap.getOrDefault(SkillTags.DMG_REDUCTION, 0f);
        this.DmgRed = dmgRed;
    }

    public float getDmgRed() {
        return this.DmgRed;
    }

    public void updateAccuracy() {
        float accuracy = this.TotalAgi * 0.5f + this.TotalPer * 0.2f;
        accuracy += skillBonusMap.getOrDefault(SkillTags.ACCURACY, 0f) + equipmentBonusMap.getOrDefault(SkillTags.ACCURACY, 0f);
        this.Accuracy = accuracy;
    }

    public float getAccuracy() {
        return this.Accuracy;
    }

    public void updateMagDef() {
        float magDef = this.TotalInt * 2 + this.TotalPer * 0.5f + this.TotalVit * 0.1f;
        magDef *= (1 + 0.01f * (skillBonusMap.getOrDefault(SkillTags.MAGIC_DEF_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.MAGIC_DEF_PERCENT, 0f)));
        magDef += skillBonusMap.getOrDefault(SkillTags.MAGIC_DEF_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.MAGIC_DEF_FLAT, 0f);
        this.MagicDef = magDef;
    }

    public float getMagDef() {
        return this.MagicDef;
    }

    public void updateMagResist() {
        float magResist = this.TotalInt * 0.5f + this.TotalPer * 0.2f + this.TotalVit * 0.1f;
        magResist += skillBonusMap.getOrDefault(SkillTags.MAGIC_RESISTANCE, 0f) + equipmentBonusMap.getOrDefault(SkillTags.MAGIC_RESISTANCE, 0f);
        this.MagicResist = magResist;
    }
    public float getMagResist() {
        return this.MagicResist;
    }

    public float getResistance() {
        return this.Resistance;
    }

    public void updateHealRegen() {
        float healRegen = this.TotalVit * 0.5f + this.TotalInt * 0.2f;
        healRegen += skillBonusMap.getOrDefault(SkillTags.HEAL_REGEN, 0f) + equipmentBonusMap.getOrDefault(SkillTags.HEAL_REGEN, 0f);
        this.HealRegen = healRegen;
    }
    public float getHealRegen() {
        return this.HealRegen;
    }


    public void updateSpeed() {
        float speed = this.TotalAgi * 0.01f + this.TotalPer * 0.005f;
        speed += skillBonusMap.getOrDefault(SkillTags.SPEED, 0f) + equipmentBonusMap.getOrDefault(SkillTags.SPEED, 0f);
        this.Speed = speed;
        System.out.println("Update speed PlayerInfo " + " Speed: " + this.Speed);
        if (finalPlayer != null
                && !finalPlayer.hasDisconnected()
                && !finalPlayer.isRemoved()) {
            refreshPlayerAttributes(this.finalPlayer, SkillTags.SPEED, this.Speed);
        }
    }
    public float getSpeed() {
        return this.Speed;
    }

    public void updateEvasion() {
        float evasion = this.TotalAgi * 0.5f + this.TotalPer * 0.2f;
        evasion += skillBonusMap.getOrDefault(SkillTags.EVASION, 0f) + equipmentBonusMap.getOrDefault(SkillTags.EVASION, 0f);
        this.Evasion = evasion;
    }
    public float getEvasion() {
        return this.Evasion;
    }

    public float getMagicDmg() {
        return (float) (this.MagicDmg * Config.SCALE_DMG);
    }
    public void updateMagicDmg() {
        float magicDmg = this.TotalInt * 5 + this.TotalPer * 2;
        magicDmg *= (1 + 0.01f * (skillBonusMap.getOrDefault(SkillTags.MAGIC_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.MAGIC_PERCENT, 0f)));
        magicDmg += skillBonusMap.getOrDefault(SkillTags.MAGIC_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.MAGIC_FLAT, 0f);
        this.MagicDmg = magicDmg;
    }

    public float getMagicPen() {
        return this.MagicPen;
    }
    public void updateMagicPen() {
        float magicPen = this.TotalInt * 0.5f + this.TotalPer * 0.2f;
        magicPen *= (1 + 0.01f * (skillBonusMap.getOrDefault(SkillTags.MAGIC_PEN_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.MAGIC_PEN_PERCENT, 0f)));
        magicPen += skillBonusMap.getOrDefault(SkillTags.MAGIC_PEN_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.MAGIC_PEN_FLAT, 0f);
        this.MagicPen = magicPen;
    }
    public float getCritChance() {
        return this.CritChance;
    }
    public float getManaRegen() {
        return this.ManaRegen;
    }
    public void updateManaRegen() {
        float manaRegen = this.TotalInt * 0.5f + this.TotalPer * 0.2f;
        manaRegen += skillBonusMap.getOrDefault(SkillTags.MANA_REGEN, 0f) + equipmentBonusMap.getOrDefault(SkillTags.MANA_REGEN, 0f);
        this.ManaRegen = manaRegen;
    }

    public float getMaxMana() {
        return this.maxMana;
    }

    private void update(List<SkillTags> skillTags) {
        if (skillTags == null || skillTags.isEmpty()) {
            return;
        }
        for (SkillTags tag : skillTags) {
            switch (tag) {
                case STR_FLAT, STR_PERCENT -> {
                    updateStrPoint();
                }
                case VIT_FLAT, VIT_PERCENT -> {
                    updateVitPoint();
                }
                case AGI_FLAT, AGI_PERCENT -> {
                    updateAgiPoint();
                }
                case INT_FLAT, INT_PERCENT -> {
                    updateIntPoint();
                }
                case PER_FLAT, PER_PERCENT -> {
                    updatePerPoint();
                }
                case ATK_FLAT, ATK_PERCENT -> {
                    updateAtkDmg();
                }
                case RANGE_FLAT, RANGE_PERCENT -> {
                    updateRangeDmg();
                }
                case MAGIC_FLAT, MAGIC_PERCENT -> {
                    updateMagicDmg();
                }
                case WEAPON_DMG_PERCENT -> {
                    updateWeaponPercentDmgBonus();
                }
                case DEF_FLAT, DEF_PERCENT -> {
                    updateDef();
                }
                case MAGIC_DEF_FLAT, MAGIC_DEF_PERCENT -> {
                    updateMagDef();
                }
                case MANA_FLAT, MANA_PERCENT -> {
                    updateMaxMana();
                }
                case ATTACK_SPEED -> {
                    updateAttackSpeed();
                }
                case SPEED -> {
                    updateSpeed();
                }
                case CRIT_CHANCE -> {
                    updateCritChance();
                }
                case PERFECTION -> {
                    updatePerfectionBonus();
                }
                case COUNTER_CHANCE -> {
                    updateCounterChance();
                }
                case DEF_PEN_PERCENT, DEF_PEN_FLAT -> {
                    updateDefPen();
                }
                case MAGIC_PEN_PERCENT, MAGIC_PEN_FLAT -> {
                    updateMagicPen();
                }
                case LIFE_STEAL -> {
                    updateLifeSteal();
                }
                case MANA_STEAL -> {
                    updateManaSteal();
                }
                case HEAL_REGEN -> {
                    updateHealRegen();
                }
                case MANA_REGEN -> {
                    updateManaRegen();
                }
                case EVASION -> {
                    updateEvasion();
                }
                case ACCURACY -> {
                    updateAccuracy();
                }
                case DMG_REDUCTION -> {
                    updateDmgRed();
                }
                case RESISTANCE -> {
                    updateResistance();
                }
                case MAGIC_RESISTANCE -> {
                    updateMagResist();
                }
                case DMG_REDUCTION_PEN -> {
                    updateDmgRedPen();
                }
                case RESISTANCE_PEN -> {
                    updateResistancePen();
                }
                case MAGIC_RESISTANCE_PEN -> {
                    updateMagicResistPen();
                }
                default -> {
                }
            }
        }
    }
    public void setSectType(SectTypes sectType) {
        this.sect = sectType;
        PlayerSectTypeSavedData sectTypeData = PlayerSectTypeSavedData.get(serverLevelData);
        sectTypeData.set(uuid, sectType);
        // Send update to client
        CompoundTag tag = new CompoundTag();
        tag.putString(SterilizeTags.SECT.name(), this.sect.name());
        ModNetworking.INSTANCE.sendTo(new InfoChangeUpdateS2C(tag), finalPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public float getPerfection() {
        return this.perfection;
    }

    public double getHealthBonus() {
        double healthBonus = this.TotalVit * 0.33f + this.TotalStr * 0.1f;
        healthBonus *= (1 + 0.01f * (skillBonusMap.getOrDefault(SkillTags.HP_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.HP_PERCENT, 0f)));
        healthBonus += skillBonusMap.getOrDefault(SkillTags.HP_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.HP_FLAT, 0f);
        return healthBonus;
    }

    public float getDmgRedPen() {
        return this.DmgRedPen;
    }

    public float getResistancePen() {
        return this.ResistancePen;
    }

    public float getLifeSteal() {
        return this.LifeSteal;
    }

    public float getManaSteal() {
        return this.ManaSteal;
    }

    public float getCounterChance() {
        return this.CounterChance;
    }

    public void setPlayer(ServerPlayer player) {
        this.finalPlayer = player;
    }

    public void setWheelSlots(CompoundTag tag) {
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);

        for (int i = 1; i <= 4; i++) {
            String key = "ActiveSkillSlot" + i;
            String value = (tag.contains(key, Tag.TAG_STRING))
                    ? tag.getString(key)
                    : "";
            switch (i) {
                case 1 -> activeSkillSlot1 = value;
                case 2 -> activeSkillSlot2 = value;
                case 3 -> activeSkillSlot3 = value;
                case 4 -> activeSkillSlot4 = value;
            }
            data.setActiveSkillSlot(uuid, i, value);
        }
    }

    public CompoundTag getSkillSlotTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("ActiveSkillSlot1", activeSkillSlot1);
        tag.putString("ActiveSkillSlot2", activeSkillSlot2);
        tag.putString("ActiveSkillSlot3", activeSkillSlot3);
        tag.putString("ActiveSkillSlot4", activeSkillSlot4);
        return tag;
    }
}
