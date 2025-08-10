package com.github.shinjoy991.superskillssystem.event.server;

import com.github.shinjoy991.superskillssystem.Config;
import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.effect.SSSEffectInstance;
import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.SSSDamageSource;
import com.github.shinjoy991.superskillssystem.register.RegisterDamageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid = SSS.MODID)
public class LivingEvents {

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        DamageSource source = event.getSource();
        Entity causing = source.getEntity();
        Entity direct = source.getDirectEntity();
        if (source.is(RegisterDamageType.MELEE_PHYSICAL.key()) || source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK)) {
            // Đây là melee damage
            float accuracy = 0;
            float defPen = 0;
            float dmg_red_pen = 0;
            float resistance_pen = 0;
            float life_steal = 0;
            float mana_steal = 0;
            if (causing instanceof ServerPlayer player && direct == causing) {
                // Đây là người chơi đang đánh
                accuracy = AllPlayersInfo.get(player.getUUID()).getAccuracy();
                defPen = AllPlayersInfo.get(player.getUUID()).getDefPen();
                dmg_red_pen = AllPlayersInfo.get(player.getUUID()).getDmgRedPen();
                resistance_pen = AllPlayersInfo.get(player.getUUID()).getResistancePen();
                life_steal = AllPlayersInfo.get(player.getUUID()).getLifeSteal();
                mana_steal = AllPlayersInfo.get(player.getUUID()).getManaSteal();
            } else if (direct instanceof Entity) {
                // Đây là mob đang đánh
                accuracy = direct.getPersistentData().getFloat("accuracy") == 0 ? 10000 : direct.getPersistentData().getFloat("accuracy");
                defPen = direct.getPersistentData().getFloat("def_pen");
                dmg_red_pen = direct.getPersistentData().getFloat("dmg_red_pen");
                resistance_pen = direct.getPersistentData().getFloat("resistance_pen");
                life_steal = direct.getPersistentData().getFloat("life_steal");
                mana_steal = direct.getPersistentData().getFloat("mana_steal");
            }

            // victim stats
            float evasion = 0;
            float def = 0;
            float dmg_red = 0;
            float resistance = 0;
            float counter_chance = 0;
            int victimLevel = 1;
            LivingEntity victim = event.getEntity();
            if (victim instanceof ServerPlayer victimPlayer) {
                evasion = AllPlayersInfo.get(victimPlayer.getUUID()).getEvasion();
                def = AllPlayersInfo.get(victimPlayer.getUUID()).getDef();
                dmg_red = AllPlayersInfo.get(victimPlayer.getUUID()).getDmgRed();
                resistance = AllPlayersInfo.get(victimPlayer.getUUID()).getResistance();
                counter_chance = AllPlayersInfo.get(victimPlayer.getUUID()).getCounterChance();
                victimLevel = AllPlayersInfo.get(victimPlayer.getUUID()).getPrimeLevel();
            }
            else {
                CompoundTag data = victim.getPersistentData();
                def = data.getFloat("def");
                evasion = data.getFloat("evasion");
                dmg_red = data.getFloat("dmg_red");
                resistance = data.getFloat("resistance");
                counter_chance = data.getFloat("counter_chance");
            }
            // Tính toán
            int realEvasion = (int) Math.max(0, evasion - accuracy);
            int realDef = (int) Math.max(0, def - defPen);
            int realDmgRed = (int) Math.max(0, dmg_red - dmg_red_pen);
            int realResistance = (int) Math.min(Math.max(0, resistance - resistance_pen), 100); // Percent

            // Tinh toán sát thương
            float damage = event.getAmount();
            boolean isMissed = victim.level().random.nextInt(10000) < realEvasion;
            if (isMissed) {
                damage *= 0.5f;
            }
            float potentialReduction = realDef * victimLevel / 99f; // từ 0 → 1
            float totalReduction = (float) Math.min(potentialReduction, 0.8 * damage); // không vượt quá sát thương
            damage = damage - totalReduction;

            float damageRedPercent = realDmgRed / 100f;
            damage = damage * (1 - damageRedPercent); // Giảm sát thương theo phần trăm


            if (source instanceof SSSDamageSource sssSource) {
                ArrayList<SSSEffectInstance> effectsList = sssSource.getEffectList();
                for (SSSEffectInstance effectInstance : effectsList) {
                    if (victim.level().random.nextInt(100) < effectInstance.getChance()
                            && victim.level().random.nextInt(100) < (100 - realResistance)) { // Xác suất kích hoạt hiệu ứng
                        victim.addEffect(effectInstance);
                    }
                }
            }
            System.out.println("Phy Normal Damage: " + event.getAmount());
            System.out.println("Damage: " + damage + " | Evasion: " + realEvasion + " | Def: " + realDef + " | Dmg Red: " + realDmgRed + " | Resistance: " + realResistance);
            event.setAmount(damage);
            // Xử lý hiệu ứng cướp máu và mana
            if (causing instanceof LivingEntity realAttacker) {
                if (life_steal > 0) {
                    float lifeStealAmount = damage * (life_steal / 100f);
                    realAttacker.heal(lifeStealAmount);
                }
                if (mana_steal > 0) {
                    float manaStealAmount = damage * (mana_steal / 100f);
                    if (realAttacker instanceof Player player) {
                        AllPlayersInfo.get(player.getUUID()).addMana(manaStealAmount);
                    }
                }
                // Xử lý hiệu ứng phản đòn
                if (counter_chance > 0 && victim.level().random.nextInt(100) < counter_chance) {
                    float counterDamage = damage * 0.2f;
                    DamageSource counterSource = reverseDamageSource(source, victim);
                    realAttacker.hurt(counterSource, counterDamage);
                }
            }

        }
        else if (source.is(RegisterDamageType.RANGED_PHYSICAL.key()) || source.is(DamageTypes.ARROW)) {
            // Đây là ranged damage
            float accuracy = 0;
            float defPen = 0;
            float dmg_red_pen = 0;
            float resistance_pen = 0;
            float life_steal = 0;
            float mana_steal = 0;
            if (causing instanceof ServerPlayer player) {
                // Đây là người chơi đang đánh
                accuracy = AllPlayersInfo.get(player.getUUID()).getAccuracy();
                defPen = AllPlayersInfo.get(player.getUUID()).getDefPen();
                dmg_red_pen = AllPlayersInfo.get(player.getUUID()).getDmgRedPen();
                resistance_pen = AllPlayersInfo.get(player.getUUID()).getResistancePen();
                life_steal = AllPlayersInfo.get(player.getUUID()).getLifeSteal();
                mana_steal = AllPlayersInfo.get(player.getUUID()).getManaSteal();
            } else if (causing instanceof LivingEntity) {
                // Đây là mob đang đánh
                accuracy = causing.getPersistentData().getFloat("accuracy") == 0 ? 10000 : causing.getPersistentData().getFloat("accuracy");
                defPen = causing.getPersistentData().getFloat("def_pen");
                dmg_red_pen = causing.getPersistentData().getFloat("dmg_red_pen");
                resistance_pen = causing.getPersistentData().getFloat("resistance_pen");
                life_steal = causing.getPersistentData().getFloat("life_steal");
                mana_steal = causing.getPersistentData().getFloat("mana_steal");
            }

            // victim stats
            float evasion = 0;
            float def = 0;
            float dmg_red = 0;
            float resistance = 0;
            int victimLevel = 1;
            LivingEntity victim = event.getEntity();
            if (victim instanceof ServerPlayer victimPlayer) {
                evasion = AllPlayersInfo.get(victimPlayer.getUUID()).getEvasion();
                def = AllPlayersInfo.get(victimPlayer.getUUID()).getDef();
                dmg_red = AllPlayersInfo.get(victimPlayer.getUUID()).getDmgRed();
                resistance = AllPlayersInfo.get(victimPlayer.getUUID()).getResistance();
                victimLevel = AllPlayersInfo.get(victimPlayer.getUUID()).getPrimeLevel();
            }
            else {
                CompoundTag data = victim.getPersistentData();
                def = data.getFloat("def");
                evasion = data.getFloat("evasion");
                dmg_red = data.getFloat("dmg_red");
                resistance = data.getFloat("resistance");
            }
            // Tính toán
            int realEvasion = (int) Math.max(0, evasion - accuracy);
            int realDef = (int) Math.max(0, def - defPen);
            int realDmgRed = (int) Math.max(0, dmg_red - dmg_red_pen);
            int realResistance = (int) Math.min(Math.max(0, resistance - resistance_pen), 100); // Percent

            // Tinh toán sát thương
            float damage = event.getAmount();
            boolean isMissed = victim.level().random.nextInt(10000) < realEvasion;
            if (isMissed) {
                damage *= 0.5f;
            }
            float potentialReduction = realDef * victimLevel / 99f; // từ 0 → 1
            float totalReduction = (float) Math.min(potentialReduction, 0.8 * damage); // không vượt quá sát thương
            damage = damage - totalReduction;

            float damageRedPercent = realDmgRed / 100f;
            damage = damage * (1 - damageRedPercent); // Giảm sát thương theo phần trăm

            if (source instanceof SSSDamageSource sssSource) {
                ArrayList<SSSEffectInstance> effectsList;
                effectsList = sssSource.getEffectList();
                for (SSSEffectInstance effect : effectsList) {
                    if (victim.level().random.nextInt(100) < effect.getChance()
                            && victim.level().random.nextInt(100) < (100 - realResistance)) { // Xác suất kích hoạt hiệu ứng
                        victim.addEffect(effect);
                    }
                }
            }
            System.out.println("Normal Damage: " + event.getAmount());
            System.out.println("Damage: " + damage + " | Evasion: " + realEvasion + " | Def: " + realDef + " | Dmg Red: " + realDmgRed + " | Resistance: " + realResistance);
            event.setAmount(damage);
            // Xử lý hiệu ứng cướp máu và mana
            if (causing instanceof LivingEntity realAttacker) {
                if (life_steal > 0) {
                    float lifeStealAmount = damage * (life_steal / 100f);
                    realAttacker.heal(lifeStealAmount);
                }
                if (mana_steal > 0) {
                    float manaStealAmount = damage * (mana_steal / 100f);
                    if (realAttacker instanceof Player player) {
                        AllPlayersInfo.get(player.getUUID()).addMana(manaStealAmount);
                    }
                }
            }

        }

//        else if (source.is(RegisterDamageType.MAGIC.key()) || source.is(DamageTypes.INDIRECT_MAGIC)) {
//            // Đây là magic damage
//            float magResPen = 0;
//            float magPen = 0;
//            float dmg_red_pen = 0;
//            float resistance_pen = 0;
//            float life_steal = 0;
//            float mana_steal = 0;
//            if (causing instanceof ServerPlayer player) {
//                // Đây là người chơi đang đánh
//                magResPen = AllPlayersInfo.get(player.getUUID()).getAccuracy();
//                magPen = AllPlayersInfo.get(player.getUUID()).getDefPen();
//                dmg_red_pen = AllPlayersInfo.get(player.getUUID()).getDmgRedPen();
//                resistance_pen = AllPlayersInfo.get(player.getUUID()).getResistancePen();
//                life_steal = AllPlayersInfo.get(player.getUUID()).getLifeSteal();
//                mana_steal = AllPlayersInfo.get(player.getUUID()).getManaSteal();
//            } else if (direct instanceof Entity) {
//                // Đây là mob đang đánh
//                magResPen = direct.getPersistentData().getFloat("accuracy") == 0 ? 10000 : direct.getPersistentData().getFloat("accuracy");
//                magPen = direct.getPersistentData().getFloat("def_pen");
//                dmg_red_pen = direct.getPersistentData().getFloat("dmg_red_pen");
//                resistance_pen = direct.getPersistentData().getFloat("resistance_pen");
//                life_steal = direct.getPersistentData().getFloat("life_steal");
//                mana_steal = direct.getPersistentData().getFloat("mana_steal");
//            }
//
//            // victim stats
//            float evasion = 0;
//            float def = 0;
//            float dmg_red = 0;
//            float resistance = 0;
//            float counter_chance = 0;
//            int victimLevel = 1;
//            LivingEntity victim = event.getEntity();
//            if (victim instanceof ServerPlayer victimPlayer) {
//                evasion = AllPlayersInfo.get(victimPlayer.getUUID()).getEvasion();
//                def = AllPlayersInfo.get(victimPlayer.getUUID()).getDef();
//                dmg_red = AllPlayersInfo.get(victimPlayer.getUUID()).getDmgRed();
//                resistance = AllPlayersInfo.get(victimPlayer.getUUID()).getResistance();
//                counter_chance = AllPlayersInfo.get(victimPlayer.getUUID()).getCounterChance();
//                victimLevel = AllPlayersInfo.get(victimPlayer.getUUID()).getPrimeLevel();
//            }
//            else {
//                def = victim.getPersistentData().getFloat("def");
//                evasion = victim.getPersistentData().getFloat("evasion") == 0 ?
//                        victim.level().random.nextInt(Config.MOB_MAX_EVASION - Config.MOB_MIN_EVASION) + Config.MOB_MIN_EVASION : victim.getPersistentData().getFloat("evasion");
//                dmg_red = victim.getPersistentData().getFloat("dmg_red") == 0 ?
//                        victim.level().random.nextInt(Config.MOB_MAX_DMG_REDUCTION - Config.MOB_MIN_DMG_REDUCTION) + Config.MOB_MIN_DMG_REDUCTION : victim.getPersistentData().getFloat("dmg_red");
//                resistance = victim.getPersistentData().getFloat("resistance") == 0 ?
//                        victim.level().random.nextInt(Config.MOB_MAX_RESISTANCE - Config.MOB_MIN_RESISTANCE) + Config.MOB_MIN_RESISTANCE : victim.getPersistentData().getFloat("resistance");
//                counter_chance = victim.getPersistentData().getFloat("counter_chance");
//            }
//            // Tính toán
//            int realEvasion = (int) Math.max(0, evasion - magResPen);
//            int realDef = (int) Math.max(0, def - magPen);
//            int realDmgRed = (int) Math.max(0, dmg_red - dmg_red_pen);
//            int realResistance = (int) Math.min(Math.max(0, resistance - resistance_pen), 100); // Percent
//
//            // Tinh toán sát thương
//            float damage = event.getAmount();
//            boolean isMissed = victim.level().random.nextInt(10000) < realEvasion;
//            if (isMissed) {
//                damage *= 0.5f;
//            }
//            float potentialReduction = realDef * victimLevel / 99f; // từ 0 → 1
//            float totalReduction = (float) Math.min(potentialReduction, 0.8 * damage); // không vượt quá sát thương
//            damage = damage - totalReduction;
//
//            float damageRedPercent = realDmgRed / 100f;
//            damage = damage * (1 - damageRedPercent); // Giảm sát thương theo phần trăm
//
//
//            if (source instanceof SSSDamageSource sssSource) {
//                ArrayList<SSSEffectInstance> effectsList;
//                effectsList = sssSource.getEffectList();
//                for (SSSEffectInstance effect : effectsList) {
//                    if (victim.level().random.nextInt(100) < effect.getChance()
//                            && victim.level().random.nextInt(100) < (100 - realResistance)) { // Xác suất kích hoạt hiệu ứng
//                        applyEffect(effect.getEffect(), victim);
//                    }
//                }
//            }
//
//            event.setAmount(damage);
//            // Xử lý hiệu ứng cướp máu và mana
//            if (causing instanceof LivingEntity realAttacker) {
//                if (life_steal > 0) {
//                    float lifeStealAmount = damage * (life_steal / 100f);
//                    realAttacker.heal(lifeStealAmount);
//                }
//                if (mana_steal > 0) {
//                    float manaStealAmount = damage * (mana_steal / 100f);
//                    if (realAttacker instanceof Player player) {
//                        AllPlayersInfo.get(player.getUUID()).addMana(manaStealAmount);
//                    }
//                }
//                // Xử lý hiệu ứng phản đòn
//                if (counter_chance > 0 && victim.level().random.nextInt(100) < counter_chance) {
//                    float counterDamage = damage * 0.2f;
//                    DamageSource counterSource = reverseDamageSource(source, victim);
//                    realAttacker.hurt(counterSource, counterDamage);
//                }
//            }
//        } else if (source.is(RegisterDamageType.HYBRID.key())) {
//            // Đây là indirect magic damage
//        } else {
//            // Mặc định là physical damage
//
//        }



    }

    public static DamageSource reverseDamageSource(DamageSource original, Entity newAttacker) {

        if (original instanceof SSSDamageSource sssSource) {
            // Tạo lại với attacker mới nhưng giữ nguyên mọi thứ khác
            SSSDamageSource reversed = new SSSDamageSource (
                    sssSource.typeHolder(), original.getDirectEntity(), newAttacker, original.sourcePositionRaw(),
                    sssSource.getFallBackType()
            );
            ArrayList<SSSEffectInstance> effects = sssSource.getEffectList();
            reversed.withEffects(effects);
            reversed.reduceChance(0.3f); // Giảm xác suất hiệu ứng nếu cần
            return reversed;
        }
        else {
            return new DamageSource(
                    original.typeHolder(), original.getDirectEntity(), newAttacker, original.sourcePositionRaw()
            );
        }
    }

    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        LivingEntity entity = event.getEntity();
        if (entity != null) {

            CompoundTag data = entity.getPersistentData();

            if (!data.contains("evasion")) {
                data.putFloat("evasion", randomBetween(Config.MOB_MIN_EVASION, Config.MOB_MAX_EVASION, entity));
            }
            if (!data.contains("dmg_red")) {
                data.putFloat("dmg_red", randomBetween(Config.MOB_MIN_DMG_REDUCTION, Config.MOB_MAX_DMG_REDUCTION, entity));
            }
            if (!data.contains("resistance")) {
                data.putFloat("resistance", randomBetween(Config.MOB_MIN_RESISTANCE, Config.MOB_MAX_RESISTANCE, entity));
            }
        }
    }

    private static float randomBetween(int min, int max, LivingEntity entity) {
        return min + entity.getRandom().nextInt(Math.max(1, max - min + 1));
    }
}
