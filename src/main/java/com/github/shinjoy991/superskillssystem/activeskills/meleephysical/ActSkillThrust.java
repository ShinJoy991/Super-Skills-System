package com.github.shinjoy991.superskillssystem.activeskills.meleephysical;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.helpers.skill.ActiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.SectTypes;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillRegistry;
import com.github.shinjoy991.superskillssystem.register.RegisterDamageType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
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
public class ActSkillThrust extends ActiveSkill {

    private static final List<DashData> ACTIVE_DASHES = new ArrayList<>();
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(SSS.MODID, "thrust");
    private static final double HIT_RADIUS = 2.0D;
    public static void register() {
        SkillRegistry.register(ID, ActSkillThrust.class);
    }

    public ActSkillThrust(LivingEntity caster, int level) {
        super(ID, "act_skill_thrust", caster, SectTypes.WARRIOR, level, 1, false);
        this.manaCost = getManaCost(level);
        this.damageType = RegisterDamageType.MELEE_PHYSICAL.key();
        this.baseDamage = getBaseDamage(level);
    }

    public int getManaCost(int level) {
        return level * 2;
    }
    public int getCooldown(int level) {return 80;}
    public float getBaseDamage(int level) {return 5;}

    @Override
    public void activate() {
        Vec3 look = caster.getLookAngle().normalize();
        
        // Dash bằng vận tốc thật thay vì push
        Vec3 thrustDirection = look.scale(8.8d);
        caster.setDeltaMovement(thrustDirection);
        caster.hurtMarked = true; // Đánh dấu để client update vị trí ngay lập tức
        caster.fallDistance = 0.0F; // Reset fall distance để tránh damage rơi

        // Thêm dash vào list để track - damage sẽ được apply dần dần mỗi tick
        ACTIVE_DASHES.add(new DashData(caster, baseDamage, HIT_RADIUS));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide()) {return;}

        Iterator<DashData> it = ACTIVE_DASHES.iterator();
        while (it.hasNext()) {
            DashData dash = it.next();
            dash.tick();
            dash.damageEntitiesAlong(); // Damage entities along the way mỗi tick
            if (dash.finished()) {
                it.remove();
            }
        }
    }

    /**
     * Inner class để track dash data
     */
    private static class DashData {
        private final LivingEntity caster;
        private final float baseDamage;
        private final double hitRadius;
        private int ticksActive = 0;
        private static final int MAX_DURATION = 20;

        public DashData(LivingEntity caster, float baseDamage, double hitRadius) {
            this.caster = caster;
            this.baseDamage = baseDamage;
            this.hitRadius = hitRadius;
        }

        public void tick() {
            ticksActive++;
        }

        public void damageEntitiesAlong() {
            if (caster.isAlive()) {
                Vec3 pos = caster.position();
                AABB scanArea = new AABB(pos, pos).inflate(hitRadius);
                List<LivingEntity> entities = caster.level().getEntitiesOfClass(LivingEntity.class, scanArea);

                for (LivingEntity target : entities) {
                    if (target == caster) continue;
                    // Minecraft sẽ tự handle invulnerability time
                    target.hurt(caster.damageSources().mobAttack(caster), baseDamage);
                }
            }
        }

        public boolean finished() {
            return ticksActive >= MAX_DURATION || !caster.isAlive();
        }
    }
}