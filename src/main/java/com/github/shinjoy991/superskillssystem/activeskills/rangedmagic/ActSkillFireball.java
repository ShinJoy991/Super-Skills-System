package com.github.shinjoy991.superskillssystem.activeskills.rangedmagic;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.helpers.skill.ActiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.SectTypes;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillRegistry;
import com.github.shinjoy991.superskillssystem.register.RegisterDamageType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SSS.MODID)
public class ActSkillFireball extends ActiveSkill {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(SSS.MODID, "fireball");

    public static void register() {
        SkillRegistry.register(ID, ActSkillFireball.class);
    }

    public ActSkillFireball(LivingEntity caster, int level) {
        super(
                ID,
                "act_skill_fireball",
                caster,
                SectTypes.MAGE,
                level,
                1,
                false
        );
        this.manaCost = getManaCost(level);
        this.damageType = RegisterDamageType.MAGIC.key();
        this.baseDamage = getBaseDamage(level);
    }

    public int getManaCost(int level) {
        return level * 5;
    }

    public int getCooldown(int level) {
        return 80;
    }

    public float getBaseDamage(int level) {
        return 10.0F;
    }

    @Override
    public void activate() {
        if (!(caster.level() instanceof ServerLevel)) {
            return;
        }

        Vec3 look = caster.getLookAngle().normalize();

        LargeFireball fireball = new LargeFireball(
                caster.level(),
                caster,
                look.x,
                look.y,
                look.z,
                level
        );

        fireball.setPos(
                caster.getX() + look.x * 2.0D,
                caster.getEyeY() - 0.2D,
                caster.getZ() + look.z * 2.0D
        );

        caster.level().addFreshEntity(fireball);
    }
}