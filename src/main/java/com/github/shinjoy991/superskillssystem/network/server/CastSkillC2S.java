package com.github.shinjoy991.superskillssystem.network.server;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.HelperFunction;
import com.github.shinjoy991.superskillssystem.helpers.PlayerInfo;
import com.github.shinjoy991.superskillssystem.helpers.saveddata.PlayerSkillSavedData;
import com.github.shinjoy991.superskillssystem.helpers.skill.ActiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillData;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class CastSkillC2S {

    public final ResourceLocation skillId;

    public CastSkillC2S(ResourceLocation skillId) {
        this.skillId = skillId;
    }

    public static void encode(CastSkillC2S packet, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(packet.skillId);
    }

    public static CastSkillC2S decode(FriendlyByteBuf buffer) {
        return new CastSkillC2S(buffer.readResourceLocation());
    }

    public static void handle(CastSkillC2S packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;

            // get skill class from registry
            Class<? extends ActiveSkill> clazz = SkillRegistry.get(packet.skillId);
            if (clazz == null) return;


            // ── Check if player really has the skill ─────────────────────────────
            ServerLevel serverLevel = (ServerLevel) player.level();
            PlayerSkillSavedData skillData = PlayerSkillSavedData.get(serverLevel);
            SkillData savedSkill = skillData.getSkill(player.getUUID(), packet.skillId.toString());
            if (savedSkill == null) {
                System.out.println("[SSS] Player " + player.getName().getString()
                        + " tried to cast not loaded skill: " + packet.skillId);
                return;
            }
            if (savedSkill.getLevel() <= 0) {
                System.out.println("[SSS] Player " + player.getName().getString()
                        + " tried to cast not learned skill: " + packet.skillId);
                return;
            }
            int skillLevel = savedSkill.getLevel();

            // get constructor
            Constructor<? extends ActiveSkill> ctor;
            try {
                ctor = clazz.getConstructor(LivingEntity.class, int.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return;
            }

            // create skill instance
            ActiveSkill skill;
            try {
                skill = ctor.newInstance(player, skillLevel);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            // ── Check cooldown ────────────────────────────────────────────────────
            if (HelperFunction.isOnCooldown(player.getUUID(), packet.skillId)) {
                long remaining = HelperFunction.getRemainingCooldown(player.getUUID(), packet.skillId);
                float remainSec = remaining / 20.0f;
                System.out.println("[SSS] On cooldown: " + remainSec + "s left");
                // send active message to player
                HelperFunction.sendActiveMessage(player, "Skill on cooldown: " + remainSec + "s left", 0xFFFF00);
                return;
            }

            // ── Check mana ────────────────────────────────────────────────────────
            PlayerInfo playerInfo = AllPlayersInfo.get(player.getUUID());
            if (playerInfo == null) return;
            int manaCost = skill.getManaCost();
            if (playerInfo.getMana() < manaCost) {
                System.out.println("[SSS] Not enough mana: need " + manaCost + ", has " + playerInfo.getMana());
                // send active message to player
                 HelperFunction.sendActiveMessage(player, "Not enough mana!", 0xFF0000);
                return;
            }

            // ── All checks passed — deduct mana, apply cooldown, activate ─────────
            playerInfo.addMana(-manaCost);
            HelperFunction.applyCooldown(player.getUUID(), packet.skillId, skill.getCooldown());
            skill.activate();
        });
        context.setPacketHandled(true);
    }
}