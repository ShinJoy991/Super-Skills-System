package com.github.shinjoy991.superskillssystem.command;

import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public class ManaCommand {

    public ManaCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("SSS")
                        .then(Commands.literal("Mana")
                                .requires(source -> source.hasPermission(4))
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(ctx -> execute(
                                                ctx.getSource(),
                                                IntegerArgumentType.getInteger(ctx, "amount")
                                        )))
                        )
        );
    }

    private int execute(CommandSourceStack source, int amount) throws CommandSyntaxException {

        ServerPlayer player = source.getPlayerOrException();

        AllPlayersInfo.get(player.getUUID()).addMana(amount);

        MutableComponent message = Component.literal("[Super Skill System]")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                .append(Component.literal(
                        (amount >= 0 ? " Added +" : " Added ")
                                + amount + " Mana"
                ).setStyle(Style.EMPTY.withColor(
                        amount >= 0 ? ChatFormatting.GREEN : ChatFormatting.RED
                )));

        player.sendSystemMessage(message);

        return 1;
    }
}