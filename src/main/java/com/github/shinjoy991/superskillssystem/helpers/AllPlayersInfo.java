package com.github.shinjoy991.superskillssystem.helpers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AllPlayersInfo {
    public static HashMap<UUID, PlayerInfo> allPlayersInfo = new HashMap<>();
    private static ServerLevel serverLevelData;

    public static void init() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        serverLevelData = server.overworld();
        List<ServerPlayer> allPlayers = server.getPlayerList().getPlayers();
        for (ServerPlayer player : allPlayers) {
            allPlayersInfo.put(player.getUUID(), new PlayerInfo(serverLevelData, player.getUUID()));
        }
    }

    public static PlayerInfo get(UUID uuid) {
        return allPlayersInfo.computeIfAbsent(uuid, id -> new PlayerInfo(serverLevelData, id));
    }

    public void add(UUID uuid, PlayerInfo info) {
        allPlayersInfo.put(uuid, info);
    }

    public boolean contains(UUID uuid) {
        return allPlayersInfo.containsKey(uuid);
    }

}
