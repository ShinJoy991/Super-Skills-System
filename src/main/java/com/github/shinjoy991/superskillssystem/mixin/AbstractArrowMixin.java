package com.github.shinjoy991.superskillssystem.mixin;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin {

    @ModifyVariable(method = "onHitEntity", at = @At(value = "STORE", ordinal = 0))

    private int modifyDamage(int i) {
        AbstractArrow arrow = (AbstractArrow) (Object) this;
        Entity owner = arrow.getOwner();
        if (!(owner instanceof ServerPlayer serverPlayer)) {
            return i;
        }
        float rangeDmg = AllPlayersInfo.get(serverPlayer.getUUID()).getRangeDmg();

        CompoundTag tag = arrow.getPersistentData();
        if (tag.contains("sss_pullstr") && tag.contains("sss_perfection")) {
            float pull = tag.getFloat("sss_pullstr");
            float perfection = tag.getFloat("sss_perfection");
            float bonusDamage = pull * rangeDmg;
            return (int) ((i + bonusDamage) * (0.8F + serverPlayer.level().random.nextFloat() * 0.4F + perfection));
        }
        return i;
    }
}
