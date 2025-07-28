package com.github.shinjoy991.superskillssystem.mixin;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BowItem.class)
public class BowItemMixin {

    /**
     * Prevents the bow from marking an arrow as critical by redirecting the call to setCritArrow(true)
     */
    @Redirect(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setCritArrow(Z)V"
            )
    )
    private void disableArrowCrit(AbstractArrow instance, boolean crit) {
        // Do nothing if true, allow false to go through (to clear crit flags when reused)
        if (!crit) {
            instance.setCritArrow(false);
        }
    }
}