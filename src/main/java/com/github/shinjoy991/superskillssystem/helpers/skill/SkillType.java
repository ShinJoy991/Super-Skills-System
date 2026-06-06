//package com.github.shinjoy991.superskillssystem.helpers.skill;
//
//import net.minecraft.world.entity.LivingEntity;
//
//import java.util.function.BiFunction;
//
//public class SkillType {
//
//    private final BiFunction<LivingEntity, Integer, ActiveSkill> factory;
//
//    public SkillType(BiFunction<LivingEntity, Integer, ActiveSkill> factory) {
//        this.factory = factory;
//    }
//
//    public ActiveSkill create(LivingEntity caster, int level) {
//        return factory.apply(caster, level);
//    }
//}