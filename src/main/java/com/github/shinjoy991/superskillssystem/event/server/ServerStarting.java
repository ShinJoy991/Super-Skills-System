package com.github.shinjoy991.superskillssystem.event.server;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.saveddata.SavedDelayedTaskData;
import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.network.client.PlayerDataClientInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;

import static com.github.shinjoy991.superskillssystem.helpers.DelayFunc.pendingTasks;
import static com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill.sterilizeGlobalPassiveSkillsList;

@Mod.EventBusSubscriber(
        modid = SSS.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ServerStarting {
    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            AllPlayersInfo.init();
            // ✅ Get the SavedData instance
            SavedDelayedTaskData data = SavedDelayedTaskData.get(serverLevel);
            pendingTasks.clear();
            // ✅ Call the method on the instance
            data.activateDelayedTasks(serverLevel);

        }
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        CompoundTag sendTag = AllPlayersInfo.get(player.getUUID()).saveToNBT();
        CompoundTag passiveSkillTag = sterilizeGlobalPassiveSkillsList();
        ModNetworking.INSTANCE.sendTo(new PlayerDataClientInit(sendTag, passiveSkillTag), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

}
