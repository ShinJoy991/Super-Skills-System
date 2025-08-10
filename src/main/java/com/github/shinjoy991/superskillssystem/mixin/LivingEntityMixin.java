package com.github.shinjoy991.superskillssystem.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

//    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot p_184582_1_);
//
//    @Inject(method = "tryAddSoulSpeed", at = @At("HEAD"))
//    private void magmaWalk(CallbackInfo ci) {
//        try {
//            int j = EnchantmentHelper.getItemEnchantmentLevel(RegisterEnch.MAGMA_WALKER.get(),
//                    this.getItemBySlot(EquipmentSlot.FEET));
//            if (j > 0) {
//                MagmaWalker.freezeNearby(((LivingEntity) (Object) this),
//                        (ServerLevel) ((LivingEntity) (Object) this).level(),
//                        ((LivingEntity) (Object) this).blockPosition(), j);
//            }
//        } catch (Exception ignored) {
//        }
//    }

}