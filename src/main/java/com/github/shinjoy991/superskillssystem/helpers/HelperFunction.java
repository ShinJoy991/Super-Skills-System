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

//    public static void removeAttributeBonusFromEquipment(ServerPlayer player, ItemStack armor) {
//        CompoundTag tag = armor.getTag();
//        if (tag == null || !tag.contains("GEM_INLAY_COUNT"))
//            return;
//        for (int i = 1; i <= 3; i++) {
//            String type = tag.getString("GEM_INLAY_" + i);
//            try {
//                switch (type) {
//                    case "ATK_1" -> {
//                        AttributeInstance attributeInstance = player.getAttribute(Attributes.ATTACK_DAMAGE);
//                        if (attributeInstance != null && tag.hasUUID("GemATKUUID_" + i)) {
//                            attributeInstance.removeModifier(tag.getUUID("GemATKUUID_" + i));
//                        }
//                    }
//                    case "DEF_1" -> {
//                        AttributeInstance attributeInstance = player.getAttribute(Attributes.ARMOR);
//                        if (attributeInstance != null && tag.hasUUID("GemDEFUUID_" + i)) {
//                            attributeInstance.removeModifier(tag.getUUID("GemDEFUUID_" + i));
//                        }
//                    }
//                    case "HP_1" -> {
//                        AttributeInstance attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
//                        if (attributeInstance != null && tag.hasUUID("GemHPUUID_" + i)) {
//                            attributeInstance.removeModifier(tag.getUUID("GemHPUUID_" + i));
//                            player.setHealth(player.getHealth());
//                        }
//                    }
//                    case "SPD_1" -> {
//                        AttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
//                        if (attributeInstance != null && tag.hasUUID("GemSPDUUID_" + i)) {
//                            attributeInstance.removeModifier(tag.getUUID("GemSPDUUID_" + i));
//                        }
//                    }
//                }
//            } catch (Exception ignored) {
//            }
//        }
//    }

//    private static void applyAttributeBonusFromEquipment(Player player, CompoundTag tag) {
//        try {
//            int gemCount = tag.getInt("GEM_INLAY_COUNT");
//            for (int i = 1; i <= gemCount; i++) {
//                String gemName = tag.getString("GEM_INLAY_" + i);
//                switch (gemName) {
//                    case "ATK_1" -> {
//                        AttributeInstance attributeInstance = player.getAttribute(Attributes.ATTACK_DAMAGE);
//                        if (attributeInstance != null) {
//                            UUID uuid = UUID.randomUUID();
//                            AttributeModifier modifier = new AttributeModifier(uuid, "Gem ATK Bonus", Config.GEM_ATK_POWER, AttributeModifier.Operation.ADDITION);
//                            attributeInstance.addTransientModifier(modifier);
//                            tag.putUUID("GemATKUUID_" + i, uuid);
//                        }
//                    }
//                    case "DEF_1" -> {
//                        AttributeInstance attributeInstance = player.getAttribute(Attributes.ARMOR);
//                        if (attributeInstance != null) {
//                            UUID uuid = UUID.randomUUID();
//                            AttributeModifier modifier = new AttributeModifier(uuid, "Gem DEF Bonus", Config.GEM_DEF_POWER, AttributeModifier.Operation.ADDITION);
//                            attributeInstance.addTransientModifier(modifier);
//                            tag.putUUID("GemDEFUUID_" + i, uuid);
//                        }
//                    }
//
//                    case "HP_1" -> {
//                        AttributeInstance attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
//                        if (attributeInstance != null) {
//                            UUID uuid = UUID.randomUUID();
//                            AttributeModifier modifier = new AttributeModifier(uuid, "Gem HP Bonus", Config.GEM_HP_POWER, AttributeModifier.Operation.ADDITION);
//                            attributeInstance.addTransientModifier(modifier);
//                            tag.putUUID("GemHPUUID_" + i, uuid);
//                            player.setHealth(player.getHealth());
//                        }
//                    }
//                    case "SPD_1" -> {
//                        AttributeInstance attributeInstance = player.getAttribute(Attributes.MOVEMENT_SPEED);
//                        if (attributeInstance != null) {
//                            UUID uuid = UUID.randomUUID();
//                            AttributeModifier modifier = new AttributeModifier(uuid, "Gem SPD Bonus", Config.GEM_SPD_POWER, AttributeModifier.Operation.ADDITION);
//                            attributeInstance.addTransientModifier(modifier);
//                            tag.putUUID("GemSPDUUID_" + i, uuid);
//                        }
//                    }
//                }
//            }
//        } catch (Exception ignored) {
//        }
//    }

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

    public static void sendActiveMessage(ServerPlayer player, String message, int color) {
        player.sendSystemMessage(
                Component.literal(message).withStyle(style -> style.withColor(TextColor.fromRgb(color)))
        );
    }
}
