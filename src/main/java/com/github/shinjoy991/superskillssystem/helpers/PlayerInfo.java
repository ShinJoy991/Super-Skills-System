package com.github.shinjoy991.superskillssystem.helpers;

import com.github.shinjoy991.superskillssystem.helpers.saveddata.PlayerAttPointSavedData;
import com.github.shinjoy991.superskillssystem.helpers.saveddata.PlayerAttStatSavedData;
import com.github.shinjoy991.superskillssystem.helpers.saveddata.PrimeExpSavedData;
import com.sun.jna.platform.unix.solaris.LibKstat;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

import static com.github.shinjoy991.superskillssystem.helpers.Calculation.*;

public class PlayerInfo {

    private final ServerLevel serverLevelData;
    private final String name;
    private final UUID uuid;
    private Integer primeExp;
    private Integer primeLevel;
    private Integer expInCurrentLevel;
    private Integer expToNextLevel;
    private Integer totalAttPoint;
    private Integer usedAttPoint;
    private Integer availableAttPoint;
    private Integer StrPoint;
    private Integer VitPoint;
    private Integer AgiPoint;
    private Integer IntPoint;
    private Integer PerPoint;
    private Integer TotalStr;
    private Integer TotalVit;
    private Integer TotalAgi;
    private Integer TotalInt;
    private Integer TotalPer;
//    private Integer ConfigMul;

    public PlayerInfo(ServerLevel serverLevelData, UUID uuid) {
        this.serverLevelData = serverLevelData;
        ServerPlayer player = findPlayerByUUID(uuid);

        PrimeExpSavedData primeExpData = PrimeExpSavedData.get(serverLevelData);
        PlayerAttPointSavedData attPointData = PlayerAttPointSavedData.get(serverLevelData);
        PlayerAttStatSavedData attStatData = PlayerAttStatSavedData.get(serverLevelData);
        this.name = (player != null) ? player.getName().getString() : "Unknown";
        this.uuid = uuid;

        this.primeExp = primeExpData.getExp(uuid);
        this.primeLevel = calLevelByExp(this.primeExp);
        this.expInCurrentLevel = calCurrentLevelExp(this.primeExp);
        this.expToNextLevel = calExpForLevel(this.primeLevel + 1);

        this.totalAttPoint = primeLevel * 3;
        this.usedAttPoint = attPointData.getUsedAttPoints(uuid);
        this.availableAttPoint = Math.max(0, this.totalAttPoint - this.usedAttPoint);

        this.StrPoint = attPointData.getStrPoint(uuid);
        this.VitPoint = attPointData.getVitPoint(uuid);
        this.AgiPoint = attPointData.getAgiPoint(uuid);
        this.IntPoint = attPointData.getIntPoint(uuid);
        this.PerPoint = attPointData.getPerPoint(uuid);

        this.TotalStr = calTotalPer(this);
        this.TotalVit = calTotalVit(this);
        this.TotalAgi = calTotalAgi(this);
        this.TotalInt = calTotalInt(this);
        this.TotalPer = calTotalPer(this);
    }
    public static ServerPlayer findPlayerByUUID(UUID uuid) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        for (ServerLevel level : server.getAllLevels()) {
            ServerPlayer player = (ServerPlayer) level.getPlayerByUUID(uuid);
            if (player != null) return player;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getPrimeExp() {
        return primeExp;
    }
    public void addPrimeExp(int amount) {
        this.primeExp += amount;
        this.primeLevel = Calculation.calLevelByExp(this.primeExp);
        this.expInCurrentLevel = Calculation.calCurrentLevelExp(this.primeExp);
        this.expToNextLevel = Calculation.calExpForLevel(this.primeLevel + 1);
        this.totalAttPoint = this.primeLevel * 3;
        this.availableAttPoint = Math.max(0, this.totalAttPoint - this.usedAttPoint);

        PrimeExpSavedData data = PrimeExpSavedData.get(serverLevelData);
        data.addExp(uuid, amount);
        data.setDirty();
    }
    public void setPrimeExp(int i) {
        this.primeExp = i;
        this.primeLevel = Calculation.calLevelByExp(this.primeExp);
        this.expInCurrentLevel = Calculation.calCurrentLevelExp(this.primeExp);
        this.expToNextLevel = Calculation.calExpForLevel(this.primeLevel + 1);

        PrimeExpSavedData data = PrimeExpSavedData.get(serverLevelData);
        data.setExp(uuid, i);
        data.setDirty();
    }
    public int getPrimeLevel() {
        return primeLevel;
    }
    public int getExpInCurrentLevel() {
        return expInCurrentLevel;
    }
    public int getExpToNextLevel() {
        return expToNextLevel;
    }
    public int getTotalAttPoint() {
        return totalAttPoint;
    }
    public void addAttPoint(int amount) {
        this.totalAttPoint += amount;
        this.availableAttPoint += amount;

        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addAttPoints(uuid, amount);
        data.setDirty();
    }

    public int getUsedAttPoint() {
        return usedAttPoint;
    }
    public void addUsedAttPoint(int amount) {
        if (amount == 0) return;
        usedAttPoint += amount;
        availableAttPoint = totalAttPoint - usedAttPoint;
//        System.out.println("Adding Used Att Point: " + amount + " Available: " + availableAttPoint);
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addUsedAttPoints(uuid, amount);
        data.setDirty();
    }
    public void setUsedAttPoint(int amount) {
        this.usedAttPoint = amount;
        this.availableAttPoint = totalAttPoint - usedAttPoint;

        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.setUsedAttPoints(uuid, amount);
        data.setDirty();
    }

    public int getStrPoint() {
        return StrPoint;
    }
    public void addStrPoint(int amount) {
//        int realAmount = amount;
//        if (amount < 0) {
//            int actualReduction = Math.min(-amount, StrPoint);
//            realAmount = -actualReduction;
//            StrPoint -= actualReduction;
//        }
//        else {
            StrPoint += amount;
//        }

        TotalStr = Calculation.calTotalStr(this);

        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addStrPoint(uuid, amount);
        data.setDirty();
    }
    public int getVitPoint() {
        return VitPoint;
    }
    public void addVitPoint(int amount) {
        VitPoint += amount;
        TotalVit = Calculation.calTotalStr(this);
//        System.out.println("Adding Vit Point2: " + amount + " Total Vit: " + TotalVit);
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addVitPoint(uuid, amount);
        data.setDirty();
    }
    public int getAgiPoint() {
        return AgiPoint;
    }
    public void addAgiPoint(int amount) {
        AgiPoint += amount;
        TotalAgi = Calculation.calTotalAgi(this);

        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addAgiPoint(uuid, amount);
        data.setDirty();
    }
    public int getIntPoint() {
        return IntPoint;
    }
    public void addIntPoint(int amount) {
        IntPoint += amount;
        TotalInt = Calculation.calTotalInt(this);

        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addIntPoint(uuid, amount);
        data.setDirty();
    }
    public int getPerPoint() {
        return PerPoint;
    }
    public void addPerPoint(int amount) {
        PerPoint += amount;
        TotalPer = Calculation.calTotalPer(this);

        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.addPerPoint(uuid, amount);
        data.setDirty();
    }
    public int getTotalStr() {
        return TotalStr;
    }
    public int getTotalVit() {
        return TotalVit;
    }
    public int getTotalAgi() {
        return TotalAgi;
    }
    public int getTotalInt() {
        return TotalInt;
    }
    public int getTotalPer() {
        return TotalPer;
    }



    // Ghi vào tag
    public CompoundTag saveToNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Name", this.name);
        tag.putUUID("UUID", uuid);
        tag.putInt("PrimeExp", this.primeExp);
        tag.putInt("PrimeLevel", this.primeLevel);
        tag.putInt("ExpInCurrentLevel", this.expInCurrentLevel);
        tag.putInt("ExpToNextLevel", this.expToNextLevel);
//        tag.putInt("TotalAttPoint", this.totalAttPoint); // No display no need to save
        tag.putInt("UsedAttPoint", this.usedAttPoint);
//        tag.putInt("AvailableAttPoint", this.availableAttPoint);
        tag.putInt("StrPoint", this.StrPoint);
        tag.putInt("VitPoint", this.VitPoint);
        tag.putInt("AgiPoint", this.AgiPoint);
        tag.putInt("IntPoint", this.IntPoint);
        tag.putInt("PerPoint", this.PerPoint);
        tag.putInt("TotalStr", this.TotalStr);
        tag.putInt("TotalVit", this.TotalVit);
        tag.putInt("TotalAgi", this.TotalAgi);
        tag.putInt("TotalInt", this.TotalInt);
        tag.putInt("TotalPer", this.TotalPer);

        return tag;
    }

    public void setStrPoint(int i) {
        this.StrPoint = i;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.setStrPoint(uuid, i);
        data.setDirty();
    }


    public void setVitPoint(int i) {
        this.VitPoint = i;
        PlayerAttPointSavedData data = PlayerAttPointSavedData.get(serverLevelData);
        data.setVitPoint(uuid, i);
        data.setDirty();
    }
}
