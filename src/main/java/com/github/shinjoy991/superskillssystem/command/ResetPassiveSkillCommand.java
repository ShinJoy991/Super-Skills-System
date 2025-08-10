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
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ResetPassiveSkillCommand {

    public ResetPassiveSkillCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ResetPassiveSkill")
                .requires(commandSource -> commandSource.hasPermission(4)).executes((command) -> CustomCommand1a(command.getSource())));
    }

    private int CustomCommand1a(CommandSourceStack source) throws CommandSyntaxException {

        ServerPlayer player = source.getPlayerOrException();
        if (player.hasPermissions(4)) {
            if (!player.level().isClientSide && player.level().getServer() != null) {
                  AllPlayersInfo.get(player.getUUID()).resetPassiveSkills();
//                System.out.println("atk dmg: " + AllPlayersInfo.get(player.getUUID()).getAtkDmg() + " Str: " + AllPlayersInfo.get(player.getUUID()).getStrPoint() + " total:" + AllPlayersInfo.get(player.getUUID()).getTotalStr());
//                 System.out.println("atl: "+ (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    MutableComponent message = Component.literal("[Super Skills System]")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                            .append(Component.literal("Reset passive skills").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                    player.sendSystemMessage(message);


            }
        }
        return 0;
    }
}