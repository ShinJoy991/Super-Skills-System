package com.github.shinjoy991.superskillssystem.command;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public class AddPassiveSkill1Command {

    public AddPassiveSkill1Command(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("AddPassiveSkill1")
                .requires(commandSource -> commandSource.hasPermission(4)).executes((command) -> CustomCommand1a(command.getSource())));
    }

    private int CustomCommand1a(CommandSourceStack source) throws CommandSyntaxException {

        ServerPlayer player = source.getPlayerOrException();
        if (player.hasPermissions(4)) {
            if (!player.level().isClientSide && player.level().getServer() != null) {
                if (true) {
                    // Reset the player's prime exp to 0
                    AllPlayersInfo.get(player.getUUID()).addPassiveSkill("skill1", 1);
                    MutableComponent message = Component.literal("[Super Skills System]")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                            .append(Component.literal(" Added 1 Skill1").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                    player.sendSystemMessage(message);

                } else {
                    MutableComponent message = Component.literal("[Super Skills System]")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                            .append(Component.literal(" Reload Error!!").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                    player.sendSystemMessage(message);
                }
            }
        }
        return 0;
    }
}