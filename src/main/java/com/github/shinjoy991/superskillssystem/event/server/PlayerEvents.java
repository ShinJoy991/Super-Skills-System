package com.github.shinjoy991.superskillssystem.event.server;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.PlayerInfo;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.client.PlayerDataClientInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.NetworkDirection;

import java.util.UUID;

import static com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill.sterilizeGlobalPassiveSkillsList;

@EventBusSubscriber
public class PlayerEvents {

    public static final UUID EXTRA_HEALTH_UUID = UUID.fromString("a0523956-789b-4def-1934-569390abcdef");

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        PlayerInfo info = AllPlayersInfo.get(player.getUUID());
        info.setPlayer(player);
        refreshPlayerHealth(player);
        info.updateSpeed();

        ModNetworking.INSTANCE.sendTo(
                new PlayerDataClientInit(info.saveToNBT(), sterilizeGlobalPassiveSkillsList(), info.getSkillSlotTag()),
                player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

//    @SubscribeEvent
//    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
//        System.out.println("Player logged out: " + event.getEntity().getName().getString() + " " +event.getEntity().level().isClientSide);
//        // Only if client player
//        if (!event.getEntity().level().isClientSide) return;
//        // get active wheel slots
//        CompoundTag tagToServer = PlayerClientData.getWheelSlotTag();
//        if (tagToServer.isEmpty()) return;
//        System.out.println("Sending wheel slot data to server on logout with tag: " + tagToServer);
//        ModNetworking.INSTANCE.sendToServer(new UpdateWheelSlotC2S(tagToServer));
//    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        if (!(event.getEntity() instanceof ServerPlayer newPlayer))
            return;
        PlayerInfo info = AllPlayersInfo.get(newPlayer.getUUID());
        info.setPlayer(newPlayer);
        refreshPlayerHealth(newPlayer);
        info.updateSpeed();
    }

    @SubscribeEvent
    public static void onPlayerChangeEquip(LivingEquipmentChangeEvent event) {
//        if (!(event.getEntity() instanceof ServerPlayer player)) {
//            return;
//        }
//        System.out.println("Player changed equipment");
    }

    public static void refreshPlayerHealth(ServerPlayer player) {
        double extraHealth =
                AllPlayersInfo.get(player.getUUID()).getHealthBonus();

        AttributeInstance attr =
                player.getAttribute(Attributes.MAX_HEALTH);

        if (attr != null) {
            attr.removeModifier(EXTRA_HEALTH_UUID);

            attr.addTransientModifier(
                    new AttributeModifier(
                            EXTRA_HEALTH_UUID,
                            "ExtraHealth",
                            extraHealth,
                            AttributeModifier.Operation.ADDITION
                    )
            );
        }
        player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));
    }

    private static void removeExtraHealth(ServerPlayer player) {
        AttributeInstance attr = player.getAttribute(Attributes.MAX_HEALTH);
        if (attr != null) {
            attr.removeModifier(EXTRA_HEALTH_UUID);
        }

        // Nếu bạn muốn reset máu hiện tại nếu vượt quá giới hạn mới
        player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer player)) return;
        float healRegen = AllPlayersInfo.get(player.getUUID()).getHealRegen();
        // heal every 2 seconds (40 ticks)
        int randomTick = 2 * (16 + player.getRandom().nextInt(9)); // 1s random between 16~24 ticks

        if (player.tickCount % randomTick == 0) {
            if (healRegen > 0 && player.getHealth() < player.getMaxHealth()) {
                player.heal(healRegen * 2);
            }
        }
    }
}
