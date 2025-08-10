package com.github.shinjoy991.superskillssystem.mixin;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.PlayerInfo;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collection;

@Mixin(Player.class)
public class PlayerMixin {
    /**
     * Forcefully disables the critical hit condition (`flag2`) by overriding it before ForgeHooks.getCriticalHit is called.
     */
    @ModifyVariable(
            method = "attack(Lnet/minecraft/world/entity/Entity;)V",
            at = @At(value = "STORE", ordinal = 2), // flag2 is the 3rd boolean stored
            ordinal = 2
    )
    private boolean modifyCritChance(boolean original) {
        Player self = (Player) (Object) this;

        // Tăng tỉ lệ chí mạng lên 50%
        if (original) {
            return self.level().random.nextFloat() < 0.5f;
        }
        return false;
    }

    @ModifyVariable(
            method = "attack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D"),
            index = 2
    )
    // only player melee attack
    private float injectAttackDamageBonus(float original) {
        Player self = (Player) (Object) this;

        if (self.level().isClientSide) {
            return original; // Không thay đổi gì nếu là client, có thể sai nếu sau đó dùng giá trị này để render
        }
        if (original < 2) {
            return original; // Không thay đổi gì nếu giá trị gốc quá nhỏ
        }
        PlayerInfo info = AllPlayersInfo.get(self.getUUID());
        float atk = info.getAtkDmg();
        // get damage of item of player
        ItemStack weapon = self.getMainHandItem();
        Multimap<Attribute, AttributeModifier> modifiers = weapon.getAttributeModifiers(EquipmentSlot.MAINHAND);
        double weaponDmgAddByPercent = 0;
        Collection<AttributeModifier> dmgModifiers = modifiers.get(Attributes.ATTACK_DAMAGE);
        for (AttributeModifier modifier : dmgModifiers) {
            if (modifier.getOperation() == AttributeModifier.Operation.ADDITION) {
                weaponDmgAddByPercent += modifier.getAmount(); // cộng dồn nếu có nhiều modifier ADDITION
            }
        }
    // Áp dụng bonus:
        weaponDmgAddByPercent *= (0.01f * info.getWeaponPercentDmgBonus());
//        System.out.println("PlayerMixin.injectAttackDamageBonus: atk = " + atk + ", weaponDmgAddByPercent = " + weaponDmgAddByPercent + ", original = " + original);
        return (float) (atk + weaponDmgAddByPercent + original);
//        return (float) ((atk + weaponDmgAddByPercent) * (0.8F + self.level().random.nextFloat() * 0.4F + info.getPerfection()));
//            return original; // Trả về giá trị gốc nếu không có thay đổi nào
    }

}