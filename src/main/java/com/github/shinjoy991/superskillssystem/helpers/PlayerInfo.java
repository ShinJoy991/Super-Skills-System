package com.github.shinjoy991.superskillssystem.helpers;

import com.github.shinjoy991.superskillssystem.Config;
import com.github.shinjoy991.superskillssystem.config.ReadConfig;
import com.github.shinjoy991.superskillssystem.helpers.saveddata.*;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkillInstance;
import com.github.shinjoy991.superskillssystem.helpers.skill.SectTypes;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillTags;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.client.InfoChangeUpdateS2C;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;

import static com.github.shinjoy991.superskillssystem.helpers.Calculation.*;
import static com.mojang.text2speech.Narrator.LOGGER;

public class PlayerInfo {

    private final ServerLevel serverLevelData;
    private final ServerPlayer finalPlayer;
    private float perfection;
    private String name;
    private UUID uuid;
    private SectTypes sect;
    //    private Integer ConfigMul;
    // Player attributes
    private int mana;
    private int maxMana;
    private Integer primeExp;
    private Integer primeLevel;
    private Integer expInCurrentLevel;
    private Integer expToNextLevel;
    private Integer totalAttPoint;
    private Integer usedAttPoint;
    private Integer availableAttPoint;
    private Integer StrPoint;
    private Integer VitPoint;
    private Integer AgiPoint;
    private Integer IntPoint;
    private Integer PerPoint;
    private Integer TotalStr;
    private Integer TotalVit;
    private Integer TotalAgi;
    private Integer TotalInt;
    private Integer TotalPer;
    // Detail Screen
    private float weaponPercentDmgBonus;
    private float AtkDmg;
    private float RangeDmg; // Total = base arrow dmg * speed (block per tick)
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

        // Load passive skills
        this.passiveSkills = playerSkillData.getPassiveSkills(uuid);
        this.skillBonusMap = this.getSkillBonusMap();
        this.equipmentBonusMap = this.getEquipmentBonusMap();

        this.sect = sectTypeData.get(uuid);
        this.primeExp = primeExpData.getExp(uuid);
        this.primeLevel = calLevelByExp(this.primeExp);
        this.expInCurrentLevel = calCurrentLevelExp(this.primeExp);
        this.expToNextLevel = calExpForLevel(this.primeLevel + 1);

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
        this.Resistance = skillBonusMap.getOrDefault(SkillTags.RESISTANCE, 0f)
                + equipmentBonusMap.getOrDefault(SkillTags.RESISTANCE, 0f);
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
        this.RangeDmg = this.TotalAgi * 5 + this.TotalStr * 2;
    }
    public float getRangeDmg() {
        return (float) (this.RangeDmg * Config.SCALE_DMG);
    }
    private void updateAtkDmg() {
        float player_atk = 1;
        float atk = this.TotalStr * 0.1f + this.TotalAgi * 0.025f;
        float calAtk = atk * (1 + 0.01f *
                (skillBonusMap.getOrDefault(SkillTags.ATK_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.ATK_PERCENT, 0f)))
                + skillBonusMap.getOrDefault(SkillTags.ATK_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.ATK_FLAT, 0f);
//        ServerPlayer serverPlayer = (ServerPlayer) serverLevelData.getPlayerByUUID(this.uuid);
//        System.out.println("calAtk: " + calAtk + " atk: " + atk + " player_atk: " + player_atk +" att: "+ ((float) serverPlayer.getAttributeValue(Attributes.ATTACK_DAMAGE)));

        this.AtkDmg = player_atk + calAtk;
    }
    public float getAtkDmg() {
        return (float) (this.AtkDmg * Config.SCALE_DMG);
    }

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
        return primeExp;
    }
    public void addPrimeExp(int amount) {
        this.primeExp += amount;
        this.primeLevel = Calculation.calLevelByExp(this.primeExp);
        this.expInCurrentLevel = Calculation.calCurrentLevelExp(this.primeExp);
        this.expToNextLevel = Calculation.calExpForLevel(this.primeLevel + 1);
        this.totalAttPoint = this.primeLevel * 3;
        if (amount < 0) {
            this.usedAttPoint = 0;
            this.StrPoint = 0;
            this.VitPoint = 0;
            this.AgiPoint = 0;
            this.IntPoint = 0;
            this.PerPoint = 0;
        }
        this.availableAttPoint = Math.max(0, this.totalAttPoint - this.usedAttPoint);

        PrimeExpSavedData data = PrimeExpSavedData.get(serverLevelData);
        data.addExp(uuid, amount);
    }
    public void setPrimeExp(int i) {
        this.primeExp = i;
        this.primeLevel = Calculation.calLevelByExp(this.primeExp);
        this.expInCurrentLevel = Calculation.calCurrentLevelExp(this.primeExp);
        this.expToNextLevel = Calculation.calExpForLevel(this.primeLevel + 1);
        this.totalAttPoint = this.primeLevel * 3;
        this.usedAttPoint = 0;
        this.availableAttPoint = Math.max(0, this.totalAttPoint - this.usedAttPoint);
        this.StrPoint = 0;
        this.VitPoint = 0;
        this.AgiPoint = 0;
        this.IntPoint = 0;
        this.PerPoint = 0;

        PrimeExpSavedData data = PrimeExpSavedData.get(serverLevelData);
        data.setExp(uuid, i);
    }
    public int getPrimeLevel() {
        return primeLevel;
    }
    public int getExpInCurrentLevel() {
        return expInCurrentLevel;
    }
    public int getExpToNextLevel() {
        return expToNextLevel;
    }

    public int getTotalAttPoint() {
        return totalAttPoint;
    }
    public int getUsedAttPoint() {
        return usedAttPoint;
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
        return this.StrPoint;
    }
    public void addStrPoint(int amount) {
        StrPoint += amount;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addStrPoint(uuid, amount);
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
        return this.TotalStr;
    }

    public int getVitPoint() {
        return this.VitPoint;
    }
    public void addVitPoint(int amount) {
        VitPoint += amount;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addVitPoint(uuid, amount);
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
        return this.TotalVit;
    }

    public int getAgiPoint() {
        return this.AgiPoint;
    }
    public void addAgiPoint(int amount) {
        AgiPoint += amount;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addAgiPoint(uuid, amount);
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
        return this.TotalAgi;
    }

    public int getIntPoint() {
        return this.IntPoint;
    }
    public void addIntPoint(int amount) {
        IntPoint += amount;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addIntPoint(uuid, amount);
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
        return this.TotalInt;
    }

    public int getPerPoint() {
        return this.PerPoint;
    }
    public void addPerPoint(int amount) {
        PerPoint += amount;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addPerPoint(uuid, amount);
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
        return this.TotalPer;
    }

    public float getMana() {
        return this.mana;
    }
    public void addMana(float amount) {
        int mana = Math.round(amount);
        if (this.mana + mana > this.maxMana) {
            mana = (int) Math.max(0, this.maxMana - this.mana);
        }
        this.mana += mana;

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
        tag.putInt(SterilizeTags.MANA.name(), this.mana);
        tag.putInt(SterilizeTags.MAX_MANA.name(), this.maxMana);
        tag.putInt(SterilizeTags.PRIME_EXP.name(), this.primeExp);
        tag.putInt(SterilizeTags.USED_ATT_POINT.name(), this.usedAttPoint);
        tag.putInt(SterilizeTags.STR_POINT.name(), this.StrPoint);
        tag.putInt(SterilizeTags.VIT_POINT.name(), this.VitPoint);
        tag.putInt(SterilizeTags.AGI_POINT.name(), this.AgiPoint);
        tag.putInt(SterilizeTags.INT_POINT.name(), this.IntPoint);
        tag.putInt(SterilizeTags.PER_POINT.name(), this.PerPoint);
        tag.putInt(SterilizeTags.TOTAL_STR.name(), this.TotalStr);
        tag.putInt(SterilizeTags.TOTAL_VIT.name(), this.TotalVit);
        tag.putInt(SterilizeTags.TOTAL_AGI.name(), this.TotalAgi);
        tag.putInt(SterilizeTags.TOTAL_STR.name(), this.TotalInt);
        tag.putInt(SterilizeTags.TOTAL_PER.name(), this.TotalPer);

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
        tag.putFloat(SterilizeTags.WEAPON_DMG_BONUS.name(), (float) (this.weaponPercentDmgBonus * Config.SCALE_DMG));
        tag.putDouble(SterilizeTags.BONUS_DMG.name(), player.getAttributeValue(Attributes.ATTACK_DAMAGE) - 1 - weaponDmg);
        tag.putFloat(SterilizeTags.ATK_DMG.name(), (float) (this.AtkDmg * Config.SCALE_DMG));
        tag.putFloat(SterilizeTags.RANGE_DMG.name(), (float) (this.RangeDmg * Config.SCALE_DMG));
        tag.putFloat(SterilizeTags.MAGIC_DMG.name(), (float) (this.MagicDmg * Config.SCALE_DMG));
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

        return tag;
    }

//    public CompoundTag nbtToClient(List<SkillTags> skillTags) {
//        CompoundTag tag = new CompoundTag();
//        for (SkillTags skillTag : skillTags) {
//            switch (skillTag) {
//                case STR_POINT -> tag.putInt(SkillTags.STR_POINT.name(), this.StrPoint);
//                case VIT_POINT -> tag.putInt(SkillTags.VIT_POINT.name(), this.VitPoint);
//                case AGI_POINT -> tag.putInt(SkillTags.AGI_POINT.name(), this.AgiPoint);
//                case INT_POINT -> tag.putInt(SkillTags.INT_POINT.name(), this.IntPoint);
//                case PER_POINT -> tag.putInt(SkillTags.PER_POINT.name(), this.PerPoint);
//
//            }
//        }
//        return tag;
//    }


    public void setStrPoint(int i) {
        this.StrPoint = i;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.setStrPoint(uuid, i);
    }


    public void setVitPoint(int i) {
        this.VitPoint = i;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.setVitPoint(uuid, i);
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

        PlayerSkillSavedData data = PlayerSkillSavedData.get(serverLevelData);
        int levelOld = data.getPassiveSkillLevel(uuid, skillName);
        int newLevel = levelOld + addLevel;

        // Cập nhật bonus: trừ skill cũ và cộng skill mới (nếu có)
        for (int sign : new int[]{-1, 1}) {
            int level = (sign == -1) ? levelOld : newLevel;
            if (level <= 0) continue;

            PassiveSkillInstance instance = new PassiveSkillInstance(matched, level);
            for (SkillTags tag : instance.getTags()) {
                float value = instance.getValue(tag) * sign;
                skillBonusMap.merge(tag, value, Float::sum);
            }
        }

        boolean success = data.addPassiveSkillLevel(uuid, skillName, addLevel);
        if (success) {
            this.passiveSkills = data.getPassiveSkills(uuid);
        }

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
        float def = this.TotalVit * 2 + this.TotalStr * 0.5f;
        def *= (1 + 0.01f * (skillBonusMap.getOrDefault(SkillTags.DEF_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.DEF_PERCENT, 0f)));
        def += skillBonusMap.getOrDefault(SkillTags.DEF_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.DEF_FLAT, 0f);
        this.Def = def;
    }

    public float getDef() {
        return this.Def;
    }

    public void updateDefPen() {
        float defPen = this.TotalStr * 0.5f + this.TotalAgi * 0.2f;
        defPen *= (1 + 0.01f * (skillBonusMap.getOrDefault(SkillTags.DEF_PEN_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.DEF_PEN_PERCENT, 0f)));
        defPen += skillBonusMap.getOrDefault(SkillTags.DEF_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.DEF_FLAT, 0f);
        this.DefPen = defPen;
    }

    public float getDefPen() {
        return this.DefPen;
    }

    public void updateAttackSpeed() {
        float attackSpeed = 1.0f + this.TotalAgi * 0.01f + this.TotalStr * 0.005f;
        attackSpeed += skillBonusMap.getOrDefault(SkillTags.ATK_SPD, 0f) + equipmentBonusMap.getOrDefault(SkillTags.ATK_SPD, 0f);
        this.AttackSpeed =  attackSpeed;
    }

    public float getAttackSpeed() {
        return this.AttackSpeed;
    }

    public void updateDmgRed() {
        float dmgRed = this.TotalVit * 0.5f + this.TotalStr * 0.2f;
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
        float magDef = this.TotalInt * 2 + this.TotalPer * 0.5f;
        magDef *= (1 + 0.01f * (skillBonusMap.getOrDefault(SkillTags.MAGIC_DEF_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.MAGIC_DEF_PERCENT, 0f)));
        magDef += skillBonusMap.getOrDefault(SkillTags.MAGIC_DEF_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTags.MAGIC_DEF_FLAT, 0f);
        this.MagicDef = magDef;
    }

    public float getMagDef() {
        return this.MagicDef;
    }

    public void updateMagResist() {
        float magResist = this.TotalInt * 0.5f + this.TotalPer * 0.2f;
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
        float speed = 0.1f + this.TotalAgi * 0.01f + this.TotalPer * 0.005f;
        speed += skillBonusMap.getOrDefault(SkillTags.SPEED, 0f) + equipmentBonusMap.getOrDefault(SkillTags.SPEED, 0f);
        this.Speed = speed;
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

    public void update(List<SkillTags> skillTags) {
        EnumSet<SkillTags> tags = EnumSet.copyOf(skillTags);

        if (tags.contains(SkillTags.STR_FLAT) || tags.contains(SkillTags.STR_PERCENT)) {
            updateTotalStr();
            updateAtkDmg();
            updateDef();
            updateDefPen();
            updateAttackSpeed();
            updateDmgRed();
            updateAccuracy();
        }

        if (tags.contains(SkillTags.VIT_FLAT) || tags.contains(SkillTags.VIT_PERCENT)) {
            updateTotalVit();
            updateDef();
            updateDmgRed();
            updateMagDef();
            updateMagResist();
            updateHealRegen();
            updateResistance();
        }
        if (tags.contains(SkillTags.AGI_FLAT) || tags.contains(SkillTags.AGI_PERCENT)) {
            updateTotalAgi();
            updateAtkDmg();
            updateRangeDmg();
            updateAttackSpeed();
            updateEvasion();
            updateSpeed();
            updateAccuracy();
        }
        if (tags.contains(SkillTags.INT_FLAT) || tags.contains(SkillTags.INT_PERCENT)) {
            updateTotalInt();
            updateMagicDmg();
            updateMagDef();
            updateMagResist();
            updateMagicPen();
            updateResistance();
        }
        if (tags.contains(SkillTags.PER_FLAT) || tags.contains(SkillTags.PER_PERCENT)) {
            updateTotalPer();
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
    }

    public void setSectType(SectTypes sectType) {
        this.sect = sectType;
        PlayerSectTypeSavedData sectTypeData = PlayerSectTypeSavedData.get(serverLevelData);
        sectTypeData.set(uuid, sectType);
    }

    public float getPerfection() {
        return this.perfection;
    }

    public double getHealthBonus() {
        double healthBonus = this.TotalVit * 2 + this.TotalStr * 0.5f;
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
}
