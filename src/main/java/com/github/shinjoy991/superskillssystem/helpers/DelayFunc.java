package com.github.shinjoy991.superskillssystem.helpers;

import com.github.shinjoy991.superskillssystem.helpers.saveddata.SavedDelayedTaskData;
import com.github.shinjoy991.superskillssystem.helpers.skill.ActiveSkill;
import com.google.common.collect.ArrayListMultimap;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DelayFunc {
    public static int tick = 0;
    public static final ArrayListMultimap<Integer, Runnable> pendingTasks = ArrayListMultimap.create();
    public static void delayedTask(int ticksToWait, Runnable run) {
        pendingTasks.put(ticksToWait + tick, run);
    }

    public static void saveDelayedTask(
            int ticksToWait,
            SavedDelayedTaskData.FallBackType fallbackType,
            ServerLevel level,
            Object[] fallbackParameters,
            Runnable mainRunnable
    ) {
        // Save to memory
        SavedTaskInfo info = new SavedTaskInfo(ticksToWait, fallbackType, fallbackParameters);
        SavedDelayedTaskData data = SavedDelayedTaskData.get(level);
        int id = data.generateId();
        data.registerTask(id, info);
        // Schedule the main task
        pendingTasks.put(ticksToWait + tick, () -> {
            try {
                mainRunnable.run();
            } finally {
                SavedDelayedTaskData savedData = SavedDelayedTaskData.get(level);
                savedData.removeTask(id);
            }
        });
    }


    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for(Runnable task : pendingTasks.get(tick)) {
                task.run();
            }
        } else if (event.phase == TickEvent.Phase.END) {
            pendingTasks.removeAll(tick);

            ++tick;
        }
    }
}
