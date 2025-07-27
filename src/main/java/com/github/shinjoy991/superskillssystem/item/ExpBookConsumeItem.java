package com.github.shinjoy991.superskillssystem.item;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.saveddata.PrimeExpSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ExpBookConsumeItem extends Item {
    public ExpBookConsumeItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide())
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            ItemStack stack = serverPlayer.getItemInHand(hand);
            stack.shrink(1);
            ServerLevel serverLevel = (ServerLevel) level;
            serverLevel.playSound(null, serverPlayer.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, level.random.nextFloat() * 2.0F);
            // Add 30 prime exp to the player
            AllPlayersInfo.get(player.getUUID()).addPrimeExp(30);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
