package com.github.shinjoy991.superskillssystem.event.server;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.saveddata.SavedDelayedTaskData;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.shinjoy991.superskillssystem.helpers.DelayFunc.pendingTasks;

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

}
