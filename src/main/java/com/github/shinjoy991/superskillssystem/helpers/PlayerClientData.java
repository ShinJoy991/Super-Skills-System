package com.github.shinjoy991.superskillssystem.helpers;

import com.github.shinjoy991.superskillssystem.config.ReadConfig;
import com.github.shinjoy991.superskillssystem.helpers.skill.ActiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkillInstance;
import com.github.shinjoy991.superskillssystem.helpers.skill.SectTypes;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillTags;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillRegistry;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.server.UpdateWheelSlotC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
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
    public static double RangeDmg;
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
    public static List<ActiveSkill> activeSkills = new ArrayList<>();

    public static List<PassiveSkill> warriorGlobalPassiveSkills = new ArrayList<>();
    public static List<PassiveSkill> archerGlobalPassiveSkills = new ArrayList<>();
    // Thêm sect khác tương tự nếu cần

    public static ResourceLocation activeSkillSlot1;
    public static ResourceLocation activeSkillSlot2;
    public static ResourceLocation activeSkillSlot3;
    public static ResourceLocation activeSkillSlot4;

    public PlayerClientData (LocalPlayer player, CompoundTag tag, CompoundTag passiveSkillTag, CompoundTag skillSlotsTag) {
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

        updateGlobalSkills(passiveSkillTag);
        updateSkillSlots(skillSlotsTag);

        ListTag skillList = tag.getList("PassiveSkills", Tag.TAG_COMPOUND);
        for (Tag t : skillList) {
            CompoundTag skillTag = (CompoundTag) t;
            String name = skillTag.getString("SkillName");
            int level = skillTag.getInt("Level");
            PassiveSkill skill = findSkillInAllGlobalLists(name);
            if (skill != null) {
                passiveSkills.add(new PassiveSkillInstance(skill, level));
            }
        }

        syncActiveSkillList(tag);

        mana = tag.getInt(SterilizeTags.MANA.name());
        maxMana = tag.getInt(SterilizeTags.MAX_MANA.name());

    }

//    public static void updateGlobalSkills(CompoundTag tag) {
//        // Passive skills
//        List<PassiveSkill> clientPassiveSkillList = new ArrayList<>();
//
//        if (tag.contains("warrior", Tag.TAG_LIST)) {
//            ListTag warriorList = tag.getList("warrior", Tag.TAG_COMPOUND);
//
//            for (Tag t : warriorList) {
//                CompoundTag warriorSkillTag = (CompoundTag) t;
//
//                String name = warriorSkillTag.getString("name");
//                SectTypes sect = SectTypes.valueOf(warriorSkillTag.getString("sect"));
//
//                // Đọc base
//                Map<SkillTags, Float> base = new HashMap<>();
//                CompoundTag baseTag = warriorSkillTag.getCompound("base");
//                for (String key : baseTag.getAllKeys()) {
//                    base.put(SkillTags.valueOf(key), baseTag.getFloat(key));
//                }
//
//                // Đọc bonuses
//                Map<SkillTags, Float> bonuses = new HashMap<>();
//                CompoundTag bonusesTag = warriorSkillTag.getCompound("bonuses");
//                for (String key : bonusesTag.getAllKeys()) {
//                    bonuses.put(SkillTags.valueOf(key), bonusesTag.getFloat(key));
//                }
//
//                // Đọc tags
//                List<SkillTags> tags = new ArrayList<>();
//                if (warriorSkillTag.contains("tags", Tag.TAG_LIST)) {
//                    ListTag tagsList = warriorSkillTag.getList("tags", Tag.TAG_STRING);
//                    for (Tag tagEntry : tagsList) {
//                        tags.add(SkillTags.valueOf(tagEntry.getAsString()));
//                    }
//                }
//
//                PassiveSkill skill = new PassiveSkill(name, sect, tags, base, bonuses);
//                clientPassiveSkillList.add(skill);
//            }
//        }
//        warriorGlobalPassiveSkills = clientPassiveSkillList;
//        System.out.println("Warrior global skills updated: " + warriorGlobalPassiveSkills.size() + " skills loaded.");
//    }

    public static void updateGlobalSkills(CompoundTag tag) {
        List<PassiveSkill> clientPassiveSkillList = new ArrayList<>();

        for (SectTypes sectType : SectTypes.values()) {
            if (sectType == SectTypes.NONE) continue;
            String key = sectType.name().toLowerCase();
            if (!tag.contains(key, Tag.TAG_LIST)) continue;

            ListTag sectList = tag.getList(key, Tag.TAG_COMPOUND);
            List<PassiveSkill> sectSkillList = new ArrayList<>();

            for (Tag t : sectList) {
                CompoundTag skillTag = (CompoundTag) t;
                String name = skillTag.getString("name");
                SectTypes sect = SectTypes.valueOf(skillTag.getString("sect"));

                Map<SkillTags, Float> base = new HashMap<>();
                CompoundTag baseTag = skillTag.getCompound("base");
                for (String k : baseTag.getAllKeys()) {
                    base.put(SkillTags.valueOf(k), baseTag.getFloat(k));
                }

                Map<SkillTags, Float> bonuses = new HashMap<>();
                CompoundTag bonusesTag = skillTag.getCompound("bonuses");
                for (String k : bonusesTag.getAllKeys()) {
                    bonuses.put(SkillTags.valueOf(k), bonusesTag.getFloat(k));
                }

                List<SkillTags> tags = new ArrayList<>();
                if (skillTag.contains("tags", Tag.TAG_LIST)) {
                    ListTag tagsList = skillTag.getList("tags", Tag.TAG_STRING);
                    for (Tag tagEntry : tagsList) {
                        tags.add(SkillTags.valueOf(tagEntry.getAsString()));
                    }
                }

                PassiveSkill skill = new PassiveSkill(name, sect, tags, base, bonuses);
                clientPassiveSkillList.add(skill);
                sectSkillList.add(skill);
            }

            // Gán vào list riêng từng sect để SectVillagerMenu dùng
            if (sectType == SectTypes.WARRIOR) {
                warriorGlobalPassiveSkills = sectSkillList;
                System.out.println("Warrior global skills updated: " + warriorGlobalPassiveSkills.size());
            } else if (sectType == SectTypes.ARCHER) {
                archerGlobalPassiveSkills = sectSkillList;
                System.out.println("Archer global skills updated: " + archerGlobalPassiveSkills.size());
            }
        }
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
//                        System.out.println("AtkDmg updated in client: " + AtkDmg);
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

        // TODO: Only update specific skills instead of clearing all and re-adding
        if (tag.contains("PassiveSkills", Tag.TAG_LIST)) {
//            player.sendSystemMessage(Component.literal("Updating passive skills..."+passiveSkills.size() + " first skill name" + (passiveSkills.isEmpty() ? "" : passiveSkills.get(0).getName())));
            ListTag skillList = tag.getList("PassiveSkills", Tag.TAG_COMPOUND);
//            player.sendSystemMessage(Component.literal("Tag Extract: " + skillList));
            passiveSkills.clear(); // Luôn xóa

            for (Tag t : skillList) {
                CompoundTag skillTag = (CompoundTag) t;
                String name = skillTag.getString("SkillName");
                int level = skillTag.getInt("Level");
//                player.sendSystemMessage(Component.literal("Skill read from tag: " + name + " level " + level));
//                PassiveSkill skill = warriorGlobalPassiveSkills.stream()
//                        .filter(s -> s.name.equals(name))
//                        .findFirst()
//                        .orElse(null);
//                if (skill != null) {
                PassiveSkill skill = findSkillInAllGlobalLists(name);
                if (skill != null) {
//                    player.sendSystemMessage(Component.literal("Skill found in config: " + skill.name));
                    passiveSkills.add(new PassiveSkillInstance(skill, level));
                }
            }
            passiveSkills.sort(Comparator.comparing(s -> s.getName().toLowerCase()));
//            player.sendSystemMessage(Component.literal("Passive skills updated, total " + passiveSkills.size() + " skills. First skill: " + (passiveSkills.isEmpty() ? "None" : passiveSkills.get(0).getName())));
        }
        syncActiveSkillList(tag);
        System.out.println("Client Data updated");
    }

    public static void syncActiveSkillList(CompoundTag tag) {
        if (!tag.contains("ActiveSkills", Tag.TAG_LIST)) {
            return;
        }
        // TODO: Only update specific skills instead of clearing all and re-adding
        // todo only sync skill name, level, id, and some basic info, not instance
        activeSkills.clear();
        ListTag skillList = tag.getList("ActiveSkills", Tag.TAG_COMPOUND);
        if (skillList.isEmpty()) {return;}
        for (Tag t : skillList) {
            if (!(t instanceof CompoundTag skillTag)) {
                continue;
            }
            ResourceLocation skillId = resolveActiveSkillId(skillTag);
            Class<? extends ActiveSkill> skillClass = skillId == null ? null : SkillRegistry.get(skillId);
            if (skillClass == null) {
                continue;
            }
            int level = skillTag.contains("Level") ? skillTag.getInt("Level") : 1;
            ActiveSkill skill = createActiveSkill(skillClass, level);
            if (skill != null) {
                activeSkills.add(skill);
            }
        }
        activeSkills.sort(Comparator.comparing(s -> s.getName().toLowerCase()));
        System.out.println("Active skill list synced, total " + activeSkills.size() + " skills.");
    }

    private static ActiveSkill createActiveSkill(Class<? extends ActiveSkill> clazz, int level) {
        LocalPlayer clientPlayer = player != null ? player : Minecraft.getInstance().player;
        if (clientPlayer == null) {
            return null;
        }

        try {
            Constructor<? extends ActiveSkill> ctor = clazz.getConstructor(LivingEntity.class, int.class);
            return ctor.newInstance(clientPlayer, level);
        } catch (Exception e) {
            return null;
        }
    }

    private static ResourceLocation resolveActiveSkillId(CompoundTag skillTag) {
        String[] candidates = {
                skillTag.getString("SkillId"),
                skillTag.getString("skillId"),
                skillTag.getString("ClassName"),
                skillTag.getString("className"),
                skillTag.getString("SkillName"),
                skillTag.getString("skillName")
        };

        for (String candidate : candidates) {
            ResourceLocation id = resolveActiveSkillId(candidate);
            if (id != null) {
                return id;
            }
        }

        return null;
    }

    private static ResourceLocation resolveActiveSkillId(String skillName) {
        if (skillName == null || skillName.isBlank()) {
            return null;
        }

        try {
            ResourceLocation parsed = ResourceLocation.parse(skillName);
            if (SkillRegistry.get(parsed) != null) {
                return parsed;
            }
        } catch (Exception ignored) {
        }

        for (Map.Entry<ResourceLocation, Class<? extends ActiveSkill>> entry : SkillRegistry.SKILLS.entrySet()) {
            Class<? extends ActiveSkill> clazz = entry.getValue();
            String simpleName = clazz.getSimpleName();
            String className = clazz.getName();
            if (simpleName.equalsIgnoreCase(skillName) || className.equals(skillName) || className.endsWith("." + skillName)) {
                return entry.getKey();
            }

            try {
                Field idField = clazz.getDeclaredField("ID");
                idField.setAccessible(true);
                Object value = idField.get(null);
                if (value instanceof ResourceLocation rl) {
                    if (rl.toString().equalsIgnoreCase(skillName) || rl.getPath().equalsIgnoreCase(skillName)) {
                        return entry.getKey();
                    }
                }
            } catch (Exception ignored) {
            }

            ActiveSkill skill = createActiveSkill(clazz, 1);
            if (skill != null) {
                if (skill.getName().equalsIgnoreCase(skillName)
                        || skill.getId().toString().equalsIgnoreCase(skillName)
                        || skill.getId().getPath().equalsIgnoreCase(skillName)) {
                    return entry.getKey();
                }
            }
        }

        return null;
    }

    private static ResourceLocation readResourceLocation(CompoundTag tag, String key) {
        String value = tag.getString(key);
        if (value.isBlank()) {
            return null;
        }
        try {
            return ResourceLocation.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    // Align with hovered sector logic in SkillWheelScreen to cast skill
    public static ResourceLocation getWheelSkillId(int hovered) {
        switch (hovered) {
            case 1 -> {
                return activeSkillSlot1;
            }
            case 2 -> {
                return activeSkillSlot2;
            }
            case 3 -> {
                return activeSkillSlot3;
            }
            case 4 -> {
                return activeSkillSlot4;
            }
            default -> {
                return null;
            }
        }

    }
    public static void setWheelSlot(int slot, ResourceLocation skillId) {
        switch (slot) {
            case 1 -> activeSkillSlot1 = skillId;
            case 2 -> activeSkillSlot2 = skillId;
            case 3 -> activeSkillSlot3 = skillId;
            case 4 -> activeSkillSlot4 = skillId;
        }
        CompoundTag tag = getWheelSlotTag();
        ModNetworking.INSTANCE.sendToServer(new UpdateWheelSlotC2S(tag));
    }
    // Get tag to send to server
    public static CompoundTag getWheelSlotTag() {
        CompoundTag tag = new CompoundTag();
        if (activeSkillSlot1 != null) {
            tag.putString("ActiveSkillSlot1", activeSkillSlot1.toString());
        } else {
            tag.putString("ActiveSkillSlot1", "");
        }
        if (activeSkillSlot2 != null) {
            tag.putString("ActiveSkillSlot2", activeSkillSlot2.toString());
        } else {
            tag.putString("ActiveSkillSlot2", "");
        }
        if (activeSkillSlot3 != null) {
            tag.putString("ActiveSkillSlot3", activeSkillSlot3.toString());
        } else {
            tag.putString("ActiveSkillSlot3", "");
        }
        if (activeSkillSlot4 != null) {
            tag.putString("ActiveSkillSlot4", activeSkillSlot4.toString());
        } else {
            tag.putString("ActiveSkillSlot4", "");
        }
        return tag;
    }


    // Update skill slots from server data
    public static void updateSkillSlots(CompoundTag skillSlotsTag) {
        if (skillSlotsTag.contains("ActiveSkillSlot1")) {
            activeSkillSlot1 = readResourceLocation(skillSlotsTag, "ActiveSkillSlot1");
        }
        if (skillSlotsTag.contains("ActiveSkillSlot2")) {
            activeSkillSlot2 = readResourceLocation(skillSlotsTag, "ActiveSkillSlot2");
        }
        if (skillSlotsTag.contains("ActiveSkillSlot3")) {
            activeSkillSlot3 = readResourceLocation(skillSlotsTag, "ActiveSkillSlot3");
        }
        if (skillSlotsTag.contains("ActiveSkillSlot4")) {
            activeSkillSlot4 = readResourceLocation(skillSlotsTag, "ActiveSkillSlot4");
        }
    }

    public static PassiveSkill findSkillInAllGlobalLists(String name) {
        PassiveSkill skill = warriorGlobalPassiveSkills.stream()
                .filter(s -> s.name.equals(name)).findFirst().orElse(null);
        if (skill != null) return skill;
        skill = archerGlobalPassiveSkills.stream()
                .filter(s -> s.name.equals(name)).findFirst().orElse(null);
        return skill;
    }
}
