package com.github.shinjoy991.superskillssystem.activeskills.meleephysical;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.helpers.skill.ActiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.SectTypes;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillRegistry;
import com.github.shinjoy991.superskillssystem.register.RegisterDamageType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = SSS.MODID)
public class ActSkillJump extends ActiveSkill {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(SSS.MODID, "jump");
    public static void register() {
        SkillRegistry.register(ID, ActSkillJump.class);
    }
    private float jumpPower = 1.0f;

    public ActSkillJump(LivingEntity caster, int level) {
        super(ID, "act_skill_jump", caster, SectTypes.ARCHER, level, 1, false);
        this.manaCost = getManaCost(level);
        this.jumpPower = getJumpPower(level);

        this.displayPowerType = true;
        this.power = this.jumpPower;
    }

    public int getManaCost(int level) {
        return level * 2;
    }
    public int getCooldown(int level) {return 80;}
    public float getJumpPower(int level) {
        return 0.5f + 0.1f * level;
    }

    @Override
    public void activate() {
        Vec3 upVecto = new Vec3(0, 1, 0);
        Vec3 jumpDirection = upVecto.scale(jumpPower);
        caster.setDeltaMovement(jumpDirection);
        caster.hurtMarked = true;
        caster.fallDistance = 0.0F;
    }
}