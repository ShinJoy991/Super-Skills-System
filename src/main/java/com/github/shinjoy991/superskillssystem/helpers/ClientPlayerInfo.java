//package com.github.shinjoy991.superskillssystem.helpers;
//
//import com.github.shinjoy991.superskillssystem.config.ReadConfig;
//import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill;
//import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkillInstance;
//import com.github.shinjoy991.superskillssystem.helpers.skill.SkillTag;
//import net.minecraft.client.player.LocalPlayer;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.nbt.Tag;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//import java.util.*;
//
//import static com.github.shinjoy991.superskillssystem.helpers.Calculation.*;
//
//@OnlyIn(Dist.CLIENT)
//public class ClientPlayerInfo {
//
//    public final String name;
//    public final UUID uuid;
//
//    private List<PassiveSkillInstance> passiveSkills = new ArrayList<>();
////    public List<PassiveSkillInstance> activeSkills = new ArrayList<>();
//    private HashMap<SkillTag, Float> skillBonusMap = new HashMap<>();
//    private HashMap<SkillTag, Float> equipmentBonusMap = new HashMap<>();
//
//    public float mana;
//    public float maxMana;
//    public Integer primeExp;
//    public Integer primeLevel;
//    public Integer expInCurrentLevel;
//    public Integer expToNextLevel;
//    public Integer totalAttPoint;
//    public Integer usedAttPoint;
//    public Integer availableAttPoint;
//    public Integer StrPoint;
//    public Integer VitPoint;
//    public Integer AgiPoint;
//    public Integer IntPoint;
//    public Integer PerPoint;
//    public Integer TotalStr;
//    public Integer TotalVit;
//    public Integer TotalAgi;
//    public Integer TotalInt;
//    public Integer TotalPer;
//    // Detail Screen
//    public float weaponDmg;
//    public float weaponDmgBonus;
//    public float AtkDmg;
//    public float RangeDmg; // Total = base arrow dmg * speed (block per tick)
//    public float MagicDmg;
//
//    public float Armor;
//    public float ArmorPen;
//    public float MagicArmor;
//    public float MagicPen;
//
//    public float CritChance;
//    public float CritDmg;
//    public float AttackSpeed;
//    public float Speed;
//
//    public float DmgRed; // All Damage Reduction
//    public float MagicResist; // Magic Damage Immunity
//    public float Evasion; // Chance to evade physical attacks
//    public float Accuracy; // Chance to hit a target with physical attacks
//
//    public float CounterChance; // Chance to counter physical attacks
//    public float Resistance; // Resist bad effects (poison, burn, freeze, etc.)
//
//    public float LifeSteal; // Chance to steal life from the target
//    public float ManaSteal; // Chance to steal mana from the target
//    public float HealRegen; // Health regeneration per second
//    public float ManaRegen; // Mana regeneration per second
//
//    public float DmgRedPen; // Damage Reduction Penetration
//    public float MagicResistPen; // Magic Resistance Penetration
//    public float ResistancePen; // Resistance Penetration
//
//
//    public ClientPlayerInfo(LocalPlayer player, CompoundTag tag) {
//        this.name = player.getName().getString();
//        this.uuid = player.getUUID();
//
//        ListTag skillList = tag.getList("PassiveSkills", Tag.TAG_COMPOUND);
//        for (Tag t : skillList) {
//            CompoundTag skillTag = (CompoundTag) t;
//            String name = skillTag.getString("SkillName");
//            int level = skillTag.getInt("Level");
//            PassiveSkill skill = ReadConfig.passiveSkills.stream()
//                    .filter(s -> s.name.equals(name))
//                    .findFirst()
//                    .orElse(null);
//            if (skill != null) {
//                passiveSkills.add(new PassiveSkillInstance(skill, level));
//            }
//        }
//        this.skillBonusMap = this.getSkillBonusMap();
//
//        this.primeExp = tag.getInt("PrimeExp");
//        this.primeLevel = calLevelByExp(this.primeExp);
//        this.expInCurrentLevel = calCurrentLevelExp(this.primeExp);
//        this.expToNextLevel = calExpForLevel(this.primeLevel + 1);
//        this.totalAttPoint = this.primeLevel * 3;
//        this.usedAttPoint = tag.getInt("UsedAttPoint");
//        this.availableAttPoint = Math.max(0, this.totalAttPoint - this.usedAttPoint);
//
//        this.StrPoint = tag.getInt("StrPoint");
//        this.VitPoint = tag.getInt("VitPoint");
//        this.AgiPoint = tag.getInt("AgiPoint");
//        this.IntPoint = tag.getInt("IntPoint");
//        this.PerPoint = tag.getInt("PerPoint");
//        this.updateTotalStr();
//        this.updateTotalVit();
//        this.updateTotalAgi();
//        this.initTotalInt();
//        this.initTotalPer();
//
//        this.mana = tag.getFloat("Mana");
//        this.updateMaxMana();
//        // Detail Screen
//        this.weaponDmg = tag.getFloat("WeaponDmg");
//        this.weaponDmgBonus = tag.getFloat("WeaponDmgBonus");
//        this.AtkDmg = tag.getFloat("AtkDmg");
//        this.RangeDmg = tag.getFloat("RangeDmg"); // Total = base arrow dmg * speed (block per tick)
//        this.MagicDmg = tag.getFloat("MagicDmg");
//
//        this.Armor = tag.getFloat("Armor");
//        this.ArmorPen = tag.getFloat("ArmorPen");
//        this.MagicArmor = tag.getFloat("MagicArmor");
//        this.MagicPen = tag.getFloat("MagicPen");
//
//        this.CritChance = tag.getFloat("CritChance");
//        this.CritDmg = tag.getFloat("CritDmg");
//        this.AttackSpeed = tag.getFloat("AttackSpeed");
//        this.Speed = tag.getFloat("Speed");
//
//        this.DmgRed = tag.getFloat("DmgRed"); // All Damage Reduction
//        this.MagicResist = tag.getFloat("MagicResist"); // Magic Damage Immun
//    }
//
//    private HashMap<SkillTag, Float> getSkillBonusMap() {
//        for (SkillTag tag : SkillTag.values()) {
//            this.skillBonusMap.put(tag, 0f);
//        }
//        HashMap<SkillTag, Float> bonusMap = new HashMap<>();
//        for (PassiveSkillInstance skill : this.passiveSkills) {
//            if (skill.getTags() != null) {
//                for (SkillTag tag : skill.getTags()) {
//                    bonusMap.merge(tag, skill.getValue(tag), Float::sum);
//                }
//            }
//        }
//        return bonusMap;
//    }
//
//
//    private void updateTotalStr() {
//        float calStr =  this.StrPoint * (1 + 0.01f *
//                (skillBonusMap.getOrDefault(SkillTag.STR_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.STR_PERCENT, 0f)))
//                + skillBonusMap.getOrDefault(SkillTag.STR_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.STR_FLAT, 0f);
//        this.TotalStr = Math.round(calStr);
//    }
//    private void updateTotalVit() {
//        float calVit =  this.VitPoint * (1 + 0.01f *
//                (skillBonusMap.getOrDefault(SkillTag.VIT_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.VIT_PERCENT, 0f)))
//                + skillBonusMap.getOrDefault(SkillTag.VIT_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.VIT_FLAT, 0f);
//        this.VitPoint = Math.round(calVit);
//    }
//    private void updateTotalAgi() {
//        float calAgi =  this.AgiPoint * (1 + 0.01f *
//                (skillBonusMap.getOrDefault(SkillTag.AGI_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.AGI_PERCENT, 0f)))
//                + skillBonusMap.getOrDefault(SkillTag.AGI_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.AGI_FLAT, 0f);
//        this.TotalAgi = Math.round(calAgi);
//    }
//    private void updateTotalInt() {
//        float calInt =  this.IntPoint * (1 + 0.01f *
//                (skillBonusMap.getOrDefault(SkillTag.INT_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.INT_PERCENT, 0f)))
//                + skillBonusMap.getOrDefault(SkillTag.INT_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.INT_FLAT, 0f);
//        this.IntPoint = Math.round(calInt);
//        this.updateMaxMana();
//    }
//    private void updateTotalPer() {
//        float calPer =  this.PerPoint * (1 + 0.01f *
//                (skillBonusMap.getOrDefault(SkillTag.PER_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.PER_PERCENT, 0f)))
//                + skillBonusMap.getOrDefault(SkillTag.PER_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.PER_FLAT, 0f);
//        this.TotalPer = Math.round(calPer);
//        this.updateMaxMana();
//    }
//    private void initTotalInt() {
//        float calInt =  this.IntPoint * (1 + 0.01f *
//                (skillBonusMap.getOrDefault(SkillTag.INT_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.INT_PERCENT, 0f)))
//                + skillBonusMap.getOrDefault(SkillTag.INT_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.INT_FLAT, 0f);
//        this.IntPoint = Math.round(calInt);
//    }
//    private void initTotalPer() {
//        float calPer =  this.PerPoint * (1 + 0.01f *
//                (skillBonusMap.getOrDefault(SkillTag.PER_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.PER_PERCENT, 0f)))
//                + skillBonusMap.getOrDefault(SkillTag.PER_FLAT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.PER_FLAT, 0f);
//        this.TotalPer = Math.round(calPer);
//    }
//    private void updateMaxMana() {
//        float calMaxMana =  (this.TotalInt * 5 + this.TotalPer * 3) * (1 + 0.01f
//                * (skillBonusMap.getOrDefault(SkillTag.MANA_PERCENT, 0f) + equipmentBonusMap.getOrDefault(SkillTag.MANA_PERCENT, 0f)))
//                + skillBonusMap.getOrDefault(SkillTag.MANA_FLAT, 0f)
//                + equipmentBonusMap.getOrDefault(SkillTag.MANA_FLAT, 0f);
//        this.maxMana = Math.round(calMaxMana);
//    }
//
//}
