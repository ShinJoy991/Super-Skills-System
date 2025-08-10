package com.github.shinjoy991.superskillssystem.command;

import com.github.shinjoy991.superskillssystem.config.ReadConfig;
import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.SectTypes;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class AddSectTypeCommand {

    public AddSectTypeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("addsecttype")
                        .requires(source -> source.hasPermission(4))

                        .then(Commands.argument("sect_type", StringArgumentType.word())
                                .suggests(SECT_TYPE_SUGGESTIONS)
                                        .executes(this::addSectType))
        );
    }

    private int addSectType(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        ServerPlayer player = context.getSource().getPlayerOrException();
        if (player.hasPermissions(4)) {
            if (!player.level().isClientSide && player.level().getServer() != null) {
                String sectTypeString = StringArgumentType.getString(context, "sect_type");
                if (Objects.equals(sectTypeString, "remove")) {
                    AllPlayersInfo.get(player.getUUID()).setSectType(SectTypes.NONE);
                    MutableComponent message = Component.literal("[Super Skills System]")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                            .append(Component.literal("Sect type removed").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                    player.sendSystemMessage(message);
                    return 0;
                }
                try {
                    SectTypes sectType = SectTypes.fromName(sectTypeString);
                    AllPlayersInfo.get(player.getUUID()).setSectType(sectType);
                    MutableComponent message = Component.literal("[Super Skills System]")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                            .append(Component.literal("Sect type set to ").append(sectType.translatableName()).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                    player.sendSystemMessage(message);

                } catch (IllegalArgumentException e) {
                    MutableComponent message = Component.literal("[Super Skills System]")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                            .append(Component.literal("Sect type Error!!").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                    player.sendSystemMessage(message);
                }
            }
        }
        return 0;
    }

    public static final SuggestionProvider<CommandSourceStack> SECT_TYPE_SUGGESTIONS = (context, builder) -> {
        for (PassiveSkill skill : ReadConfig.passiveSkills) {
            builder.suggest(skill.sectType.name());
        }
        return builder.buildFuture();
    };
}