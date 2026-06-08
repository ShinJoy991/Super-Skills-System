package com.github.shinjoy991.superskillssystem.activeskills;

import com.github.shinjoy991.superskillssystem.activeskills.meleephysical.ActSkillJump;
import com.github.shinjoy991.superskillssystem.activeskills.meleephysical.ActSkillThrust;
import com.github.shinjoy991.superskillssystem.activeskills.rangedmagic.ActSkillFireball;

public class ModSkills {

    public static void register() {
        ActSkillThrust.register();
        ActSkillJump.register();
        ActSkillFireball.register();
    }
}