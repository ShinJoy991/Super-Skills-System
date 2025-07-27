//package com.github.shinjoy991.superskillssystem.helpers;
//
//import com.github.shinjoy991.superskillssystem.helpers.saveddata.PlayerAttPointSavedData;
//import com.github.shinjoy991.superskillssystem.helpers.saveddata.PrimeExpSavedData;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraftforge.server.ServerLifecycleHooks;
//
//import java.util.UUID;
//
//import static com.github.shinjoy991.superskillssystem.helpers.Calculation.*;
//
//public class PlayerInfoScreenData {
//    private final String name;
//    private final UUID uuid;
//    private final Integer primeExp;
//    private final Integer primeLevel;
//    private final Integer expInCurrentLevel;
//    private final Integer expToNextLevel;
//    private final Integer totalAttPoint;
//    private final Integer usedAttPoint;
//    private final Integer availableAttPoint;
//    private final Integer StrPoint;
//    private final Integer VitPoint;
//    private final Integer AgiPoint;
//    private final Integer IntPoint;
//    private final Integer PerPoint;
//    private final Integer TotalStr;
//    private final Integer TotalVit;
//    private final Integer TotalAgi;
//    private final Integer TotalInt;
//    private final Integer TotalPer;
//    private final Integer ConfigMul;
//
//    public PlayerInfoScreenData(UUID uuid) {
//        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
//        ServerPlayer player = server.getPlayerList().getPlayer(uuid);
//        if (player != null) {
//            ServerLevel level = player.serverLevel();
//            PrimeExpSavedData primeExpData = PrimeExpSavedData.get(level);
//            PlayerAttPointSavedData attPointData = PlayerAttPointSavedData.get(level);
//            this.name = player.getName().getString();
//            this.uuid = player.getUUID();
//            this.primeExp = primeExpData.getExp(uuid);
//            this.primeLevel = calLevelByExp(this.primeExp);
//            this.expInCurrentLevel = calCurrentLevelExp(this.primeExp);
//            this.expToNextLevel = calExpForLevel(this.primeLevel + 1);
//            this.totalAttPoint = attPointData.getTotalAttPoints(uuid);
//            this.usedAttPoint = attPointData.getUsedAttPoints(uuid);
//            this.availableAttPoint = attPointData.getAvailableAttPoints(uuid);
//            this.StrPoint = attPointData.getAttribute(uuid, "StrPoint");
//
//        }
//        else {
//            // return null;
//        }
//
//    }
//}
