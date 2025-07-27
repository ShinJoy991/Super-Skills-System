package com.github.shinjoy991.superskillssystem.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;

public class CrustedMagma extends Block {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public CrustedMagma() {
        super(Properties.of().mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops().lightLevel((state) -> 3)
                .randomTicks().strength(0.5F)
                .isValidSpawn((p_187421_, p_187422_, p_187423_, p_187424_) -> p_187424_.fireImmune())
                .hasPostProcess((p_61036_, p_61037_, p_61038_) -> true)
                .emissiveRendering((p_61036_, p_61037_, p_61038_) -> true)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    public void turnIntoLava(Level world, BlockPos pos) {
        world.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
        world.neighborChanged(pos, Blocks.LAVA, pos);
    }

    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos,
            RandomSource random) {
        this.tick(state, worldIn, pos, random);
        BlockPos blockpos = pos.above();
        if (worldIn.getFluidState(pos).is(FluidTags.WATER)) {
            worldIn.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F,
                    2.6F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.8F);
            worldIn.sendParticles(ParticleTypes.LARGE_SMOKE, (double) blockpos.getX() + 0.5D,
                    (double) blockpos.getY() + 0.25D, (double) blockpos.getZ() + 0.5D, 8, 0.5D,
                    0.25D, 0.5D, 0.0D);
        }
    }

    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        int age = state.getValue(AGE);
        if (random.nextInt(3) == 0 || fewerNeigboursThan(world, pos, 4)) {
            if (world.getMaxLocalRawBrightness(pos) > 11 - age - state.getLightBlock(world, pos) && slightlyMelt(state, world, pos)) {
                for (Direction direction : Direction.values()) {
                    BlockPos newPos = pos.relative(direction);
                    BlockState newState = world.getBlockState(newPos);
                    if (newState.is(this) && !slightlyMelt(newState, world, newPos)) {
                        world.scheduleTick(newPos, this,
                                Mth.nextInt(random, 20, 40));
                    }
                }
                return;
            }
        }
        world.scheduleTick(pos, this, Mth.nextInt(random, 20, 40));
    }

    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block,
            BlockPos fromPos, boolean isMoving) {
        if (world instanceof ServerLevel && block.defaultBlockState().is(this) && fewerNeigboursThan((ServerLevel) world, pos, 2)) {
            turnIntoLava(world, pos);
        }
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
    }

    private boolean fewerNeigboursThan(ServerLevel world, BlockPos pos, int limit) {
        int count = 0;

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            if (world.getBlockState(neighborPos).is(this) && ++count >= limit) {
                return false;
            }
        }
        return true;
    }

    private boolean slightlyMelt(BlockState state, Level worldIn, BlockPos pos) {
        int i = state.getValue(AGE);
        if (i < 3) {
            worldIn.setBlock(pos, state.setValue(AGE, i + 1), 2);
            return false;
        } else {
            this.turnIntoLava(worldIn, pos);
            return true;
        }
    }

    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isSteppingCarefully() && !entity.fireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
            entity.hurt(level.damageSources().hotFloor(), 1.0F);
        }
        super.stepOn(level, pos, state, entity);
    }
}
