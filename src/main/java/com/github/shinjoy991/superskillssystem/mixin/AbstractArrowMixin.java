package com.github.shinjoy991.superskillssystem.mixin;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

    @ModifyVariable(method = "onHitEntity", at = @At(value = "STORE", ordinal = 0))

    // only arrow from player
    private int modifyDamage(int i) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        Entity owner = arrow.getOwner();
        if (!(owner instanceof ServerPlayer serverPlayer)) {
            System.out.println("not player shooting");
            return i;
        }
        float rangeDmg = AllPlayersInfo.get(serverPlayer.getUUID()).getRangeDmg();
        float CritChance = AllPlayersInfo.get(serverPlayer.getUUID()).getCritChance();
        boolean isCrit = serverPlayer.level().random.nextInt(100) < CritChance;
        CompoundTag tag = arrow.getPersistentData();

        if (tag.contains("sss_pullstr") && tag.contains("sss_perfection")) {
            float pull = tag.getFloat("sss_pullstr");
            float perfection = tag.getFloat("sss_perfection");
            float baseDamage = i / 10f + pull * rangeDmg;
//            System.out.println("Base damage: " + baseDamage);
            float fluctuationDamage = baseDamage * (1F - perfection / 100F + serverPlayer.level().random.nextFloat() * (perfection / 50F));
            System.out.println("Perfecttion: " + perfection + "fluc damage: " + (int) Math.ceil(fluctuationDamage));
            if (isCrit) {
                fluctuationDamage *= 1.3F; // tăng 30% damage nếu crit
            }
            return (int) Math.ceil(fluctuationDamage);
        }
        return i;
    }


    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;isCritArrow()Z",
                    shift = At.Shift.AFTER
            )
    )
    private void addParticles(CallbackInfo ci) {
        // after if (this.isCritArrow())
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        if (!(arrow.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        if (!arrow.getPersistentData().contains("sss_crit")) {
            return;
        }
        Vec3 vec3 = arrow.getDeltaMovement();
        double d5 = vec3.x;
        double d6 = vec3.y;
        double d1 = vec3.z;
        for (int i = 0; i < 4; ++i) {
            serverLevel.sendParticles(
                    ParticleTypes.CRIT,
                    arrow.getX() + d5 * (double) i / 4.0D,
                    arrow.getY() + d6 * (double) i / 4.0D,
                    arrow.getZ() + d1 * (double) i / 4.0D,
                    0,
                    -d5,
                    -d6 + 0.2D,
                    -d1,
                    1.0D
            );

        }
    }
}
