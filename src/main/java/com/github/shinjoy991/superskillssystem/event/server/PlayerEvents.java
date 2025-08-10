package com.github.shinjoy991.superskillssystem.event.server;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.UUID;

@EventBusSubscriber
public class PlayerEvents {

    public static final UUID EXTRA_HEALTH_UUID = UUID.fromString("a0123456-789b-4def-1234-567890abcdef");

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();

        double extraHealth = AllPlayersInfo.get(player.getUUID()).getHealthBonus(); // giá trị bạn muốn tăng

        removeExtraHealth(player);

        AttributeInstance attr = player.getAttribute(Attributes.MAX_HEALTH);
        if (attr != null) {
            AttributeModifier modifier = new AttributeModifier(EXTRA_HEALTH_UUID, "ExtraHealth", extraHealth, AttributeModifier.Operation.ADDITION);
            attr.addTransientModifier(modifier); // Transient: không lưu NBT, không clone khi respawn/dimension
        }

        // Hồi đầy máu nếu muốn
        player.setHealth((float) player.getMaxHealth());
    }


    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        double extraHealth = AllPlayersInfo.get(oldPlayer.getUUID()).getHealthBonus();
        AttributeInstance attr = newPlayer.getAttribute(Attributes.MAX_HEALTH);
        if (attr != null) {
            AttributeModifier modifier = new AttributeModifier(
                    EXTRA_HEALTH_UUID, "ExtraHealth", extraHealth, AttributeModifier.Operation.ADDITION
            );
            attr.addTransientModifier(modifier);
        }
    }

    private static void removeExtraHealth(ServerPlayer player) {
        AttributeInstance attr = player.getAttribute(Attributes.MAX_HEALTH);
        if (attr != null) {
            attr.removeModifier(EXTRA_HEALTH_UUID);
        }

        // Nếu bạn muốn reset máu hiện tại nếu vượt quá giới hạn mới
        player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));
    }

}
