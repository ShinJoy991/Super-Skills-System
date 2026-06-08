package com.github.shinjoy991.superskillssystem.network.server;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.HelperFunction;
import com.github.shinjoy991.superskillssystem.helpers.PlayerInfo;
import com.github.shinjoy991.superskillssystem.helpers.saveddata.PlayerSkillSavedData;
import com.github.shinjoy991.superskillssystem.helpers.skill.ActiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillData;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
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
                System.out.println("[Super Skill System] Player " + player.getName().getString()
                        + " tried to cast not loaded skill: " + packet.skillId);
                return;
            }
            if (savedSkill.getLevel() <= 0) {
                System.out.println("[Super Skill System] Player " + player.getName().getString()
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
            skill.start(player);
        });
        context.setPacketHandled(true);
    }
}