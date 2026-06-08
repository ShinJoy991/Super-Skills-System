package com.github.shinjoy991.superskillssystem.helpers;

import com.github.shinjoy991.superskillssystem.helpers.skill.SkillTags;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HelperFunction {

    private static final UUID SPD_UUID = UUID.fromString("c8edfd12-8e53-4c57-85b4-4c3d1af1bca1");
    private static final UUID ATK_SPD_UUID = UUID.fromString("7f8a9d42-3d2e-4c9e-8f12-123456789abc");

    public static void refreshPlayerAttributes(ServerPlayer player, SkillTags skillTags, float bonus) {
        switch (skillTags)
        {
            case SPEED -> {
                AttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
                if (attributeInstance != null) {
                    attributeInstance.removeModifier(SPD_UUID);
                    attributeInstance.addTransientModifier(
                            new AttributeModifier(
                                    SPD_UUID,
                                    "Speed Boost Bonus",
                                    bonus,
                                    AttributeModifier.Operation.ADDITION
                            )
                    );
                }
            }
            case ATTACK_SPEED -> {
                AttributeInstance attributeInstance = player.getAttribute(Attributes.ATTACK_SPEED);
                if (attributeInstance != null) {
                    attributeInstance.removeModifier(ATK_SPD_UUID);
                    AttributeModifier modifier = new AttributeModifier(ATK_SPD_UUID,
                            "Attack Speed Bonus", bonus, AttributeModifier.Operation.ADDITION);
                    attributeInstance.addTransientModifier(modifier);
                }

            }
        }
    }

    // ── Cooldown helpers ──────────────────────────────────────────────────────

    // Field
    private static final Map<UUID, Map<ResourceLocation, Long>> cooldownMap = new HashMap<>();

    public static boolean isOnCooldown(UUID playerUUID, ResourceLocation skillId) {
        Map<ResourceLocation, Long> map = cooldownMap.get(playerUUID);
        if (map == null) return false;
        return map.containsKey(skillId);
    }

    public static long getRemainingCooldown(UUID playerUUID, ResourceLocation skillId) {
        Map<ResourceLocation, Long> map = cooldownMap.get(playerUUID);
        if (map == null) return 0;
        Long expiry = map.get(skillId);
        return expiry == null ? 0 : Math.max(0, expiry - DelayFunc.tick);
    }

    /** Gọi sau khi skill cast thành công — tự clear sau cooldownTicks */
    public static void applyCooldown(UUID playerUUID, ResourceLocation skillId, int cooldownTicks) {
        cooldownMap
                .computeIfAbsent(playerUUID, k -> new HashMap<>())
                .put(skillId, (long) DelayFunc.tick + cooldownTicks);

        DelayFunc.delayedTask(cooldownTicks, () ->  {
            Map<ResourceLocation, Long> map = cooldownMap.get(playerUUID);
            if (map != null) {
                map.remove(skillId);
                if (map.isEmpty()) cooldownMap.remove(playerUUID);
            }
        });
    }

    public static void sendActiveMessage(ServerPlayer player, Component message, int color) {
        player.displayClientMessage(
                message.copy().withStyle(style -> style.withColor(TextColor.fromRgb(color))),
                true
        );
    }
    public static void sendActiveMessage(ServerPlayer player, Component message) {
        player.displayClientMessage(message, true);
    }
}
