package com.github.shinjoy991.superskillssystem.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(ItemStack.class)
public class ItemStackMixin {
//
//    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true)
//    private void modifyAttackDamage(EquipmentSlot slot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
//        ItemStack stack = (ItemStack)(Object)this;
//
//        if (slot == EquipmentSlot.MAINHAND && stack.getItem() instanceof SwordItem) {
//            Multimap<Attribute, AttributeModifier> original = cir.getReturnValue();
//            Multimap<Attribute, AttributeModifier> modified = HashMultimap.create(original);
//
//            Collection<AttributeModifier> modifiers = original.get(Attributes.ATTACK_DAMAGE);
//            modified.removeAll(Attributes.ATTACK_DAMAGE);
//
//            for (AttributeModifier mod : modifiers) {
//                AttributeModifier doubled = new AttributeModifier(
//                        mod.getId(), mod.getName(), mod.getAmount() * 2 + 1,
//                        mod.getOperation());
//                modified.put(Attributes.ATTACK_DAMAGE, doubled);
//            }
//
//            cir.setReturnValue(modified);
//        }
//    }
}