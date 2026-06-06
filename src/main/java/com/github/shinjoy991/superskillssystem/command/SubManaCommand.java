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

public class SubManaCommand {

    public SubManaCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("SSS")
                        .then(Commands.literal("SubMana")
                                .requires(commandSource -> commandSource.hasPermission(4))
                                .executes((command) -> CustomCommand1a(command.getSource()))
                        ));
    }

    private int CustomCommand1a(CommandSourceStack source) throws CommandSyntaxException {

        ServerPlayer player = source.getPlayerOrException();
        if (player.hasPermissions(4)) {
            if (!player.level().isClientSide && player.level().getServer() != null) {
                if (true) {
                    // Reset the player's prime exp to 0
                    AllPlayersInfo.get(player.getUUID()).addMana(-1);
                    MutableComponent message = Component.literal("[Super Skills System]")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                            .append(Component.literal(" Added -1 Mana").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
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