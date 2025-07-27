package com.github.shinjoy991.superskillssystem.helpers;

import com.github.shinjoy991.superskillssystem.helpers.saveddata.SavedDelayedTaskData.FallBackType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;


public class SavedTaskInfo {
    public final int delay;
    public final FallBackType fallbackType;
    public final Object[] fallbackParameters;

    public SavedTaskInfo(int delay, FallBackType fallbackType, Object[] fallbackParameters) {
        this.delay = delay;
        this.fallbackType = fallbackType;
        this.fallbackParameters = fallbackParameters;
    }


    public CompoundTag toTag() {
//        System.out.println("4 Totag Saving task with delay: " + delay + ", fallbackType: " + fallbackType);
        CompoundTag tag = new CompoundTag();
        tag.putInt("Delay", delay);
        tag.putString("FallbackType", fallbackType.name());
        ListTag list = new ListTag();
        for (Object param : fallbackParameters) {
            list.add(serializeParam(param));
        }
        tag.put("params", list);
        return tag;
    }


    public static SavedTaskInfo fromTag(CompoundTag tag) {
//        System.out.println("L1 Loading task with delay: " + tag.getInt("Delay") + ", level: " + tag.getString("level") + ", fallbackType: " + tag.getString("FallbackType"));
        int delay = tag.getInt("Delay");
//        ResourceKey<Level> levelKey = ResourceKey.create(
//                Registries.DIMENSION,
//                new ResourceLocation(tag.getString("level")));
//        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
//        ServerLevel level = server.getLevel(levelKey);
        String fallbackType = tag.getString("FallbackType");
        FallBackType fallBackType = FallBackType.valueOf(fallbackType);
        ListTag list = tag.getList("params", Tag.TAG_COMPOUND);
        Object[] params = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            params[i] = deserializeParam(fallBackType, list.getCompound(i));
        }
        return new SavedTaskInfo(delay, fallBackType, params);
    }


    private CompoundTag serializeParam(Object param) {
        CompoundTag tag = new CompoundTag();
        if (fallbackType == FallBackType.REMOVE_BLOCK && param instanceof HashMap) {
            @SuppressWarnings("unchecked")
            HashMap<BlockPos, BlockState> map = (HashMap<BlockPos, BlockState>) param;
            ListTag listTag = new ListTag();

            for (Map.Entry<BlockPos, BlockState> entry : map.entrySet()) {
                CompoundTag entryTag = new CompoundTag();

                // Serialize position
                entryTag.putLong("pos", entry.getKey().asLong());

                // Serialize block state
                BlockState state = entry.getValue();
                ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
                entryTag.putString("block", blockId.toString());

                listTag.add(entryTag);
            }

            tag.put("removeBlocks", listTag);
        }

        // Add more as needed
        return tag;
    }

    // Get each parameter by index
    private static Object deserializeParam(FallBackType fallBackType, CompoundTag tag) {
        if (fallBackType == FallBackType.REMOVE_BLOCK) {
            ListTag entries = tag.getList("removeBlocks", Tag.TAG_COMPOUND);
            if (entries.isEmpty()) {
                return new HashMap<BlockPos, BlockState>();
            }
            HashMap<BlockPos, BlockState> map = new HashMap<>();
            for (Tag entryTag : entries) {
                CompoundTag entry = (CompoundTag) entryTag;
                BlockPos pos = BlockPos.of(entry.getLong("pos"));
                ResourceLocation blockId = ResourceLocation.parse(entry.getString("block"));
                BlockState state = BuiltInRegistries.BLOCK.get(blockId).defaultBlockState();
                map.put(pos, state);
            }
            return map;
        } else {

        }
        return null;
    }

    public void activate(ServerLevel level) {
        switch (fallbackType) {
            case REMOVE_ENTITY:
//                if (fallbackParameters.length > 0 && fallbackParameters[0] instanceof Integer) {
//                    int entityId = (Integer) fallbackParameters[0];
//                    level.getEntity(entityId).ifPresent(entity -> entity.discard());
//                }
//                break;
            case REMOVE_BLOCK: {
//                System.out.println("L4 Reactivate, removing blocks in " + level.dimension().location() + " now ");
                if (fallbackParameters.length > 0 && fallbackParameters[0] instanceof HashMap) {
                    @SuppressWarnings("unchecked")
                    HashMap<BlockPos, BlockState> map = (HashMap<BlockPos, BlockState>) fallbackParameters[0];
                    for (Map.Entry<BlockPos, BlockState> entry : map.entrySet()) {
                        level.setBlockAndUpdate(entry.getKey(), entry.getValue().getBlock().defaultBlockState());
                    }
                }
                break;
            }
            default:
//                throw new IllegalArgumentException("Unknown fallback type: " + fallbackType);
        }
    }
}
