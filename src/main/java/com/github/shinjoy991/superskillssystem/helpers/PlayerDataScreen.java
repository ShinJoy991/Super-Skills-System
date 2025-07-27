package com.github.shinjoy991.superskillssystem.helpers;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class PlayerDataScreen {

    public final String name;
    public final UUID uuid;
    public Integer primeExp;
    public Integer primeLevel;
    public Integer expInCurrentLevel;
    public Integer expToNextLevel;
    public Integer totalAttPoint;
    public Integer usedAttPoint;
    public Integer availableAttPoint;
    public Integer StrPoint;
    public Integer VitPoint;
    public Integer AgiPoint;
    public Integer IntPoint;
    public Integer PerPoint;
    public Integer TotalStr;
    public Integer TotalVit;
    public Integer TotalAgi;
    public Integer TotalInt;
    public Integer TotalPer;

    public PlayerDataScreen(CompoundTag tag) {
        this.name = tag.getString("Name");
        this.uuid = tag.getUUID("UUID");
        this.primeExp = tag.getInt("PrimeExp");
        this.primeLevel = tag.getInt("PrimeLevel");
        this.expInCurrentLevel = tag.getInt("ExpInCurrentLevel");
        this.expToNextLevel = tag.getInt("ExpToNextLevel");
        this.totalAttPoint = this.primeLevel * 3;
        this.usedAttPoint = tag.getInt("UsedAttPoint");
        this.availableAttPoint = Math.max(0, this.totalAttPoint - this.usedAttPoint);
        this.StrPoint = tag.getInt("StrPoint");
        this.VitPoint = tag.getInt("VitPoint");
        this.AgiPoint = tag.getInt("AgiPoint");
        this.IntPoint = tag.getInt("IntPoint");
        this.PerPoint = tag.getInt("PerPoint");
        this.TotalStr = tag.getInt("TotalStr");
        this.TotalVit = tag.getInt("TotalVit");
        this.TotalAgi = tag.getInt("TotalAgi");
        this.TotalInt = tag.getInt("TotalInt");
        this.TotalPer = tag.getInt("TotalPer");
    }
}
