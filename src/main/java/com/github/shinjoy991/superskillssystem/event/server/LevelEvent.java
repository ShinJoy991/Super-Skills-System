package com.github.shinjoy991.superskillssystem.event.server;

import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.entity.trading.SectVillager;
import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.saveddata.SavedDelayedTaskData;
import com.github.shinjoy991.superskillssystem.register.RegisterEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.github.shinjoy991.superskillssystem.helpers.DelayFunc.pendingTasks;

@Mod.EventBusSubscriber(
        modid = SSS.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class LevelEvent {
    @SubscribeEvent
    public static void onLevelLoad(net.minecraftforge.event.level.LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            AllPlayersInfo.init();
            // Get the SavedData instance
            SavedDelayedTaskData data = SavedDelayedTaskData.get(serverLevel);
            pendingTasks.clear();
            // Call the method on the instance
            data.activateDelayedTasks(serverLevel);

        }
    }


    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {

        if (!(event.getEntity() instanceof Villager villager)) return;
        if (villager instanceof SectVillager) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        MobSpawnType spawnType = event.getSpawnType();
        // Chỉ xử lý villager sinh tự nhiên
        if (spawnType != MobSpawnType.STRUCTURE && spawnType != MobSpawnType.BREEDING && spawnType != MobSpawnType.NATURAL) {
            return;
        }
        // 5% cơ hội
        if (level.random.nextFloat() > 0.05f)
            return;
        SectVillager sectVillager = RegisterEntity.SECT_VILLAGER.get().create(level);
        if (sectVillager == null) return;
        sectVillager.moveTo(
                villager.getX(),
                villager.getY(),
                villager.getZ(),
                villager.getYRot(),
                villager.getXRot()
        );
        level.getServer().execute(() -> {
            if (!villager.isRemoved()) {
                villager.discard();
                level.addFreshEntity(sectVillager);
            }
        });
    }
}
