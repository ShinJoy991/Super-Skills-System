package com.github.shinjoy991.superskillssystem.mixin;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {

    @Redirect(
            method = "getArrow(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/projectile/AbstractArrow;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setCritArrow(Z)V")
    )
    private static void disableCrit(AbstractArrow arrow, boolean crit) {
        arrow.setCritArrow(false); // luôn tắt crit
    }

    @Inject(method = "shootProjectile",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z")
    )
    private static void injectCritTag(CallbackInfo ci, @Local Projectile projectile, @Local(argsOnly = true) LivingEntity living) {
        if (living instanceof ServerPlayer serverPlayer) {
            float perfection = AllPlayersInfo.get(serverPlayer.getUUID()).getPerfection();
            float critChance = AllPlayersInfo.get(serverPlayer.getUUID()).getCritChance();
            boolean isCrit = serverPlayer.level().random.nextInt(100) < critChance;
            if (isCrit) {
                projectile.getPersistentData().putBoolean("sss_crit", true);
            }
            projectile.getPersistentData().putDouble("sss_perfection", perfection);
        }
    }
}