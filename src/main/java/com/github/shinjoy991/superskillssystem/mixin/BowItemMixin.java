package com.github.shinjoy991.superskillssystem.mixin;

import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    /**
     * Disables setting any arrow as critical.
     * Intercepts any call to `this.critical = true` and makes it always false.
     */
    @Redirect(
            method = "setCritArrow(Z)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;critical:Z", opcode = 181) // PUTFIELD
    )
    private void disableArrowCrit(AbstractArrow instance, boolean value) {
        instance.critical = false; // Force crit to false regardless of input
    }
}