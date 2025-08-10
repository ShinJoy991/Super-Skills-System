package com.github.shinjoy991.superskillssystem.helpers;

import com.github.shinjoy991.superskillssystem.config.ReadConfig;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkillInstance;
import com.github.shinjoy991.superskillssystem.helpers.skill.SectTypes;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillTags;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

import static com.github.shinjoy991.superskillssystem.helpers.Calculation.*;

@OnlyIn(Dist.CLIENT)
public class PlayerClientData {

    private static LocalPlayer player;
    public static String name = "";
    public final UUID uuid;
    public static SectTypes sect;
    public static float mana;
    public static float maxMana;
    public static Integer primeExp;
    public static Integer primeLevel;
    public static Integer expInCurrentLevel;
    public static Integer expToNextLevel;
    public static Integer totalAttPoint;
    public static Integer usedAttPoint;
    public static Integer availableAttPoint;
    public static Integer StrPoint;
    public static Integer VitPoint;
    public static Integer AgiPoint;
    public static Integer IntPoint;
    public static Integer PerPoint;
    public static Integer TotalStr;
    public static Integer TotalVit;
    public static Integer TotalAgi;
    public static Integer TotalInt;
    public static Integer TotalPer;
    // Detail Screen
    public static double bonusDmg;
    public static double WeaponDmg;
    public static double WeaponDmgBonus;
    public static double Perfection;
    public static double AtkDmg;
    public static double RangeDmg; // Total = base arrow dmg * speed (block per tick)
    public static double MagicDmg;

    public static double Def;
    public static double DefPen;
    public static double MagicDef;
    public static double MagicPen;

    public static double CritChance;
    public static double AttackSpeed;
    public static double Speed;

    public static double DmgRed; // All Damage Reduction
    public static double MagicResist; // Magic Damage Immunity
    public static double Evasion; // Chance to evade physical attacks
    public static double Accuracy; // Chance to hit a target with physical attacks

    public static double CounterChance; // Chance to counter physical attacks
    public static double Resistance; // Resist bad effects (poison, burn, freeze, etc.)

    public static double LifeSteal; // Chance to steal life from the target
    public static double ManaSteal; // Chance to steal mana from the target
    public static double HealRegen; // Health regeneration per second
    public static double ManaRegen; // Mana regeneration per second

    public static double DmgRedPen; // Damage Reduction Penetration
    public static double MagicResistPen; // Magic Resistance Penetration
    public static double ResistancePen; // Resistance Penetration

    public static List<PassiveSkillInstance> passiveSkills = new ArrayList<>();
    public static List<PassiveSkillInstance> activeSkills = new ArrayList<>();

    public static List<PassiveSkill> warriorGlobalPassiveSkills = new ArrayList<>();


    public PlayerClientData (LocalPlayer player, CompoundTag tag) {
        PlayerClientData.player = player;
        name = player.getName().getString();
        this.uuid = player.getUUID();
        bonusDmg = player.getAttributeValue(Attributes.ATTACK_DAMAGE) - 1;
        sect = SectTypes.valueOf(tag.getString(SterilizeTags.SECT.name()));
        primeExp = tag.getInt(SterilizeTags.PRIME_EXP.name());
        primeLevel = calLevelByExp(primeExp);
        expInCurrentLevel = calCurrentLevelExp(primeExp);
        expToNextLevel = calExpForLevel(primeLevel + 1);
        totalAttPoint = primeLevel * 3;
        usedAttPoint = tag.getInt(SterilizeTags.USED_ATT_POINT.name());
        availableAttPoint = Math.max(0, totalAttPoint - usedAttPoint);
        StrPoint = tag.getInt(SterilizeTags.STR_POINT.name());
        VitPoint = tag.getInt(SterilizeTags.VIT_POINT.name());
        AgiPoint = tag.getInt(SterilizeTags.AGI_POINT.name());
        IntPoint = tag.getInt(SterilizeTags.INT_POINT.name());
        PerPoint = tag.getInt(SterilizeTags.PER_POINT.name());
        TotalStr = tag.getInt(SterilizeTags.TOTAL_STR.name());
        TotalVit = tag.getInt(SterilizeTags.TOTAL_VIT.name());
        TotalAgi = tag.getInt(SterilizeTags.TOTAL_AGI.name());
        TotalInt = tag.getInt(SterilizeTags.TOTAL_INT.name());
        TotalPer = tag.getInt(SterilizeTags.TOTAL_PER.name());
        WeaponDmg = tag.getFloat(SterilizeTags.WEAPON_DMG.name());
        WeaponDmgBonus = tag.getFloat(SterilizeTags.WEAPON_DMG_BONUS.name());
        Perfection = tag.getFloat(SterilizeTags.PERFECTION.name());
        AtkDmg = tag.getFloat(SterilizeTags.ATK_DMG.name());
        RangeDmg = tag.getFloat(SterilizeTags.RANGE_DMG.name());
        MagicDmg = tag.getFloat(SterilizeTags.MAGIC_DMG.name());
        Def = tag.getFloat(SterilizeTags.DEF.name());
        DefPen = tag.getFloat(SterilizeTags.DEF_PEN.name());
        MagicDef = tag.getFloat(SterilizeTags.MAGIC_DEF.name());
        MagicPen = tag.getFloat(SterilizeTags.MAGIC_PEN.name());
        CritChance = tag.getFloat(SterilizeTags.CRIT_CHANCE.name());
        AttackSpeed = tag.getFloat(SterilizeTags.ATTACK_SPEED.name());
        Speed = tag.getFloat(SterilizeTags.SPEED.name()); // Can get through LocalPlayer
        DmgRed = tag.getFloat(SterilizeTags.DMG_RED.name());
        MagicResist = tag.getFloat(SterilizeTags.MAGIC_RESIST.name());
        Evasion = tag.getFloat(SterilizeTags.EVASION.name());
        Accuracy = tag.getFloat(SterilizeTags.ACCURACY.name());
        CounterChance = tag.getFloat(SterilizeTags.COUNTER_CHANCE.name());
        Resistance = tag.getFloat(SterilizeTags.RESISTANCE.name());
        LifeSteal = tag.getFloat(SterilizeTags.LIFE_STEAL.name());
        ManaSteal = tag.getFloat(SterilizeTags.MANA_STEAL.name());
        HealRegen = tag.getFloat(SterilizeTags.HEAL_REGEN.name());
        ManaRegen = tag.getFloat(SterilizeTags.MANA_REGEN.name());
        DmgRedPen = tag.getFloat(SterilizeTags.DMG_RED_PEN.name());
        MagicResistPen = tag.getFloat(SterilizeTags.MAGIC_RESIST_PEN.name());
        ResistancePen = tag.getFloat(SterilizeTags.RESISTANCE_PEN.name());

        ListTag skillList = tag.getList("PassiveSkills", Tag.TAG_COMPOUND);

        for (Tag t : skillList) {
            CompoundTag skillTag = (CompoundTag) t;
            String name = skillTag.getString("SkillName");
            int level = skillTag.getInt("Level");
            PassiveSkill skill = ReadConfig.passiveSkills.stream()
                    .filter(s -> s.name.equals(name))
                    .findFirst()
                    .orElse(null);
            if (skill != null) {
                passiveSkills.add(new PassiveSkillInstance(skill, level));
            }
        }

        mana = tag.getInt(SterilizeTags.MANA.name());
        maxMana = tag.getInt(SterilizeTags.MAX_MANA.name());

    }

    public static void updateGlobalSkills(CompoundTag tag) {
        List<PassiveSkill> warriorSkills = new ArrayList<>();

        if (tag.contains("warrior", Tag.TAG_LIST)) {
            ListTag warriorList = tag.getList("warrior", Tag.TAG_COMPOUND);

            for (Tag t : warriorList) {
                CompoundTag warriorSkillTag = (CompoundTag) t;

                String name = warriorSkillTag.getString("name");
                SectTypes sect = SectTypes.valueOf(warriorSkillTag.getString("sect"));

                // Đọc base
                Map<SkillTags, Float> base = new HashMap<>();
                CompoundTag baseTag = warriorSkillTag.getCompound("base");
                for (String key : baseTag.getAllKeys()) {
                    base.put(SkillTags.valueOf(key), baseTag.getFloat(key));
                }

                // Đọc bonuses
                Map<SkillTags, Float> bonuses = new HashMap<>();
                CompoundTag bonusesTag = warriorSkillTag.getCompound("bonuses");
                for (String key : bonusesTag.getAllKeys()) {
                    bonuses.put(SkillTags.valueOf(key), bonusesTag.getFloat(key));
                }

                PassiveSkill skill = new PassiveSkill(name, sect, new ArrayList<>(), base, bonuses);
                warriorSkills.add(skill);
            }
        }
        warriorGlobalPassiveSkills = warriorSkills;
        System.out.println("Warrior global skills updated: " + warriorGlobalPassiveSkills.size() + " skills loaded.");
    }

    public static void updateClientData(CompoundTag tag) {
        for (SterilizeTags sterilizeTag : SterilizeTags.values()) {
            if (tag.contains(sterilizeTag.name())) {
                switch (sterilizeTag) {
                    case PRIME_EXP -> {
                        primeExp = tag.getInt(sterilizeTag.name());
                        primeLevel = calLevelByExp(primeExp);
                        expInCurrentLevel = calCurrentLevelExp(primeExp);
                        expToNextLevel = calExpForLevel(primeLevel + 1);
                        totalAttPoint = primeLevel * 3;
                        availableAttPoint = Math.max(0, totalAttPoint - usedAttPoint);
                    }
                    case BONUS_DMG -> bonusDmg = tag.getDouble(sterilizeTag.name());
                    case MANA -> mana = tag.getFloat(sterilizeTag.name());
                    case MAX_MANA -> maxMana = tag.getFloat(sterilizeTag.name());
                    case SECT -> sect = SectTypes.valueOf(tag.getString(sterilizeTag.name()));
                    case STR_POINT -> StrPoint = tag.getInt(sterilizeTag.name());
                    case VIT_POINT -> VitPoint = tag.getInt(sterilizeTag.name());
                    case AGI_POINT -> AgiPoint = tag.getInt(sterilizeTag.name());
                    case INT_POINT -> IntPoint = tag.getInt(sterilizeTag.name());
                    case PER_POINT -> PerPoint = tag.getInt(sterilizeTag.name());
                    case TOTAL_STR -> TotalStr = tag.getInt(sterilizeTag.name());
                    case TOTAL_VIT -> TotalVit = tag.getInt(sterilizeTag.name());
                    case TOTAL_AGI -> TotalAgi = tag.getInt(sterilizeTag.name());
                    case TOTAL_INT -> TotalInt = tag.getInt(sterilizeTag.name());
                    case TOTAL_PER -> TotalPer = tag.getInt(sterilizeTag.name());
                    case USED_ATT_POINT -> {
                        usedAttPoint = tag.getInt(sterilizeTag.name());
                        availableAttPoint = Math.max(0, totalAttPoint - usedAttPoint);
                    }
                    case WEAPON_DMG -> WeaponDmg = tag.getFloat(sterilizeTag.name());
                    case WEAPON_DMG_BONUS -> WeaponDmgBonus = tag.getFloat(sterilizeTag.name());
                    case ATK_DMG -> {
                        AtkDmg = tag.getFloat(sterilizeTag.name());
//                        System.out.println("AtkDmg updated: " + AtkDmg);
                    }
                    case RANGE_DMG -> RangeDmg = tag.getFloat(sterilizeTag.name());
                    case MAGIC_DMG -> MagicDmg = tag.getFloat(sterilizeTag.name());
                    case PERFECTION -> Perfection = tag.getFloat(sterilizeTag.name());
                    case DEF -> Def = tag.getFloat(sterilizeTag.name());
                    case DEF_PEN -> DefPen = tag.getFloat(sterilizeTag.name());
                    case MAGIC_DEF -> MagicDef = tag.getFloat(sterilizeTag.name());
                    case MAGIC_PEN -> MagicPen = tag.getFloat(sterilizeTag.name());
                    case CRIT_CHANCE -> CritChance = tag.getFloat(sterilizeTag.name());
                    case ATTACK_SPEED -> AttackSpeed = tag.getFloat(sterilizeTag.name());
                    case SPEED -> Speed = tag.getFloat(sterilizeTag.name()); // Can get through LocalPlayer
                    case DMG_RED -> DmgRed = tag.getFloat(sterilizeTag.name());
                    case MAGIC_RESIST -> MagicResist = tag.getFloat(sterilizeTag.name());
                    case EVASION -> Evasion = tag.getFloat(sterilizeTag.name());
                    case ACCURACY -> Accuracy = tag.getFloat(sterilizeTag.name());
                    case COUNTER_CHANCE -> CounterChance = tag.getFloat(sterilizeTag.name());
                    case RESISTANCE -> Resistance = tag.getFloat(sterilizeTag.name());
                    case LIFE_STEAL -> LifeSteal = tag.getFloat(sterilizeTag.name());
                    case MANA_STEAL -> ManaSteal = tag.getFloat(sterilizeTag.name());
                    case HEAL_REGEN -> HealRegen = tag.getFloat(sterilizeTag.name());
                    case MANA_REGEN -> ManaRegen = tag.getFloat(sterilizeTag.name());
                    case DMG_RED_PEN -> DmgRedPen = tag.getFloat(sterilizeTag.name());
                    case MAGIC_RESIST_PEN -> MagicResistPen = tag.getFloat(sterilizeTag.name());
                    case RESISTANCE_PEN -> ResistancePen = tag.getFloat(sterilizeTag.name());
                }
            }
        }
        if (tag.contains("PassiveSkills", Tag.TAG_LIST)) {
            ListTag skillList = tag.getList("PassiveSkills", Tag.TAG_COMPOUND);
            passiveSkills.clear(); // ✔ Luôn xóa

            for (Tag t : skillList) {
                CompoundTag skillTag = (CompoundTag) t;
                String name = skillTag.getString("SkillName");
                int level = skillTag.getInt("Level");
                PassiveSkill skill = ReadConfig.passiveSkills.stream()
                        .filter(s -> s.name.equals(name))
                        .findFirst()
                        .orElse(null);
                if (skill != null) {
                    passiveSkills.add(new PassiveSkillInstance(skill, level));
                }
            }
        }
//        System.out.println("BonusDmg updated: " + bonusDmg);
    }
}
