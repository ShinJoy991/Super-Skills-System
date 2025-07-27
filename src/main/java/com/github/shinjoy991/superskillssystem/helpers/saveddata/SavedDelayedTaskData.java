package com.github.shinjoy991.superskillssystem.helpers.saveddata;

import com.github.shinjoy991.superskillssystem.helpers.SavedTaskInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.Map;

// Separate per world
public class SavedDelayedTaskData extends SavedData {
    private static final String DATA_NAME = "saved_delayed_tasks";
    public final Map<Integer, SavedTaskInfo> activeTasks = new HashMap<>();

    public static SavedDelayedTaskData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(SavedDelayedTaskData::load, SavedDelayedTaskData::new, DATA_NAME);
    }

    public static SavedDelayedTaskData load(CompoundTag tag) {
        SavedDelayedTaskData data = new SavedDelayedTaskData();
        ListTag taskList = tag.getList("tasks", Tag.TAG_COMPOUND);
        for (Tag taskTag : taskList) {
            CompoundTag t = (CompoundTag) taskTag;
            int id = t.getInt("id");
            SavedTaskInfo info = SavedTaskInfo.fromTag(t.getCompound("Info"));
            data.activeTasks.put(id, info);
        }
//        System.out.println("L2 Loaded " + data.activeTasks.size() + " delayed tasks from world data.");
        return data;
    }

    // Called when server saves the world
    @Override
    public CompoundTag save(CompoundTag tag) {
//        System.out.println("3 Saving " + activeTasks.size() + " delayed tasks to world data.");
        // Serialize each task into a tag
        ListTag taskList = new ListTag();
        for (var entry : activeTasks.entrySet()) {
            CompoundTag taskTag = new CompoundTag();
            taskTag.putInt("id", entry.getKey());
            taskTag.put("Info", entry.getValue().toTag());
            taskList.add(taskTag);
        }
        tag.put("tasks", taskList);
        return tag;
    }

    public void registerTask(int id, SavedTaskInfo info) {
//        System.out.println("2 Registering task with ID: " + id);
        activeTasks.put(id, info);
        setDirty();
    }

    public void removeTask(int id) {
//        System.out.println("L5 Removing task with ID: " + id);
        activeTasks.remove(id);
        setDirty();
    }
    public int generateId() {
        int id = 1;
        while (activeTasks.containsKey(id)) {
            id++;
        }
//        System.out.println("1 Generated new task ID: " + id);
        return id;
    }


    public Map<Integer, SavedTaskInfo> getTasks() {
        return activeTasks;
    }

    public void activateDelayedTasks(ServerLevel serverLevel) {
        for (Map.Entry<Integer, SavedTaskInfo> entry : activeTasks.entrySet()) {
//            System.out.println("L3 Activating delayed task with ID: " + entry.getKey());
            SavedTaskInfo info = entry.getValue();
            info.activate(serverLevel);
            this.removeTask(entry.getKey());
        }
    }

    public enum FallBackType {
        REMOVE_ENTITY, REMOVE_BLOCK;
    }
}


