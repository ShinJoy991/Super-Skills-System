package com.github.shinjoy991.superskillssystem.mixin;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject(method = "releaseUsing",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z")
    )
    private void injectPullStrength(CallbackInfo ci, @Local float f, @Local AbstractArrow arrow, @Local Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            float perfection = AllPlayersInfo.get(serverPlayer.getUUID()).getPerfection();
            arrow.getPersistentData().putDouble("sss_perfection", perfection);
            arrow.getPersistentData().putFloat("sss_pullstr", f);
        }
    }

}