package com.github.shinjoy991.superskillssystem.command;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public class AddPrimeLevelCommand {

    public AddPrimeLevelCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("SSS")
                        .then(Commands.literal("AddPrimeLevel")
                                .requires(commandSource -> commandSource.hasPermission(4))
                                .then(Commands.argument("level", IntegerArgumentType.integer(1, 100))
                                        .executes(this::CustomCommand1aCommandContext)))
        );
    }

    private int CustomCommand1aCommandContext(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        ServerPlayer player = context.getSource().getPlayerOrException();
        if (player.hasPermissions(4)) {
            if (!player.level().isClientSide && player.level().getServer() != null) {
                int level = IntegerArgumentType.getInteger(context, "level");
                AllPlayersInfo.get(player.getUUID()).addPrimeLevel(level);
                MutableComponent message = Component.literal("[Super Skills System]")
                        .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                        .append(Component.literal(" Added " + level + " level").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                player.sendSystemMessage(message);
            }
        }
        return 0;
    }
}