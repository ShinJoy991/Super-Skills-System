package com.github.shinjoy991.superskillssystem.helpers;

import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkillInstance;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillTags;

import java.util.List;

public class Calculation {
    private static final int TARGET_LEVEL = 100;         // Cấp mục tiêu (ví dụ: 100)
    private static final int TARGET_TOTAL_EXP = 100_00; // Tổng EXP cần để đạt cấp đó
    private static final double BASE_EXP;

    static {
        // Tính BASE_EXP để sao cho calculateExpForLevel(100) ≈ 1_000_000
        double sum = 0;
        for (int i = 1; i <= TARGET_LEVEL; i++) {
            sum += i * Math.log(i + 1);  // giữ hàm tăng EXP
        }
        BASE_EXP = TARGET_TOTAL_EXP / sum;
    }

    /** Tổng EXP tích lũy để đạt tới một cấp nhất định */
    public static int calTotalExpForLevel(int level) {
        if (level <= 0) return 0;
        double totalExp = 0;
        for (int i = 1; i <= level; i++) {
            totalExp += BASE_EXP * i * Math.log(i + 1);
        }
        return (int) totalExp;
    }
    /** EXP cần để đạt cấp tiếp theo */
    public static int calExpForLevel(int level) {
        if (level <= 0) return 0;
        return calTotalExpForLevel(level) - calTotalExpForLevel(level - 1);
    }
    /** Tính cấp hiện tại dựa trên tổng EXP */
    public static int calLevelByExp(int exp) {
        if (exp < 0) return 0;

        int level = 0;
        while (true) {
            int nextLevelExp = calTotalExpForLevel(level + 1);
            if (exp < nextLevelExp) break;
            level++;
        }
        return level;
    }

    /** EXP hiện có trong cấp hiện tại */
    public static int calCurrentLevelExp(int totalExp) {
        int currentLevel = calLevelByExp(totalExp);
        int expForCurrentLevel = calTotalExpForLevel(currentLevel);
//        System.out.println("Current Level: " + currentLevel + ", Total EXP: " + totalExp + ", EXP for Current Level: " + expForCurrentLevel);
        return totalExp - expForCurrentLevel;
    }

    public static Integer calTotalStr(PlayerInfo info) {
        if (info == null) return 0;
        return info.getStrPoint() * 2;
    }
    public static Integer calTotalStr(int info) {
        return info * 2; // vi dụ, mỗi điểm StrPoint tăng 2 Str, tạm thời cho local
    }

    public static Integer calTotalVit(PlayerInfo info) {
        if (info == null) return 0;
        return info.getVitPoint() * 2;
    }
    public static Integer calTotalVit(int info) {
        return info * 2; // vi dụ, mỗi điểm VitPoint tăng 2 Vit, tạm thời cho local
    }

    public static Integer calTotalAgi(PlayerInfo info) {
        if (info == null) return 0;
        return info.getAgiPoint() * 2;
    }
    public static Integer calTotalAgi(int info) {
        return info * 2; // vi dụ, mỗi điểm AgiPoint tăng 2 Agi, tạm thời cho local
    }
    public static Integer calTotalInt(PlayerInfo info) {
        if (info == null) return 0;
        return info.getIntPoint() * 2;
    }
    public static Integer calTotalInt(int info) {
        return info * 2; // vi dụ, mỗi điểm IntPoint tăng 2 Int, tạm thời cho local
    }
    public static Integer calTotalPer(PlayerInfo info) {
        if (info == null) return 0;
        return info.getPerPoint() * 2; // Giả sử mỗi điểm PerPoint tăng 2 Per
    }
    public static Integer calTotalPer(int info) {
        return info * 2; // vi dụ, mỗi điểm PerPoint tăng 2 Per, tạm thời cho local
    }

    public static Integer calMaxMana(Integer intPoint, Integer perPoint, double manaPercentBonus, double manaFlatBonus) {
        int baseMana = intPoint * 10 + perPoint * 5;
        return (int) (baseMana * (1 + manaPercentBonus / 100) + manaFlatBonus);
    }

    public static double getBonus(SkillTags skillTag, List<PassiveSkillInstance> passiveSkills) {
        double bonus = 0;
        for (PassiveSkillInstance skillInstance : passiveSkills) {
            if (skillInstance.getLevel() > 0 && skillInstance.getTags().contains(skillTag)) {
                bonus += skillInstance.getValue(skillTag);
            }
        }
        return bonus;
    }
}

