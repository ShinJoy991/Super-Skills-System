package com.github.shinjoy991.superskillssystem.command;

import com.github.shinjoy991.superskillssystem.config.ReadConfig;
import com.github.shinjoy991.superskillssystem.helpers.AllPlayersInfo;
import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AddPassiveSkillCommand {

    public AddPassiveSkillCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("addpassiveskill")
                        .requires(source -> source.hasPermission(4)) // permission level
                        .then(Commands.argument("skill_name", StringArgumentType.word())
                                .suggests(SKILL_NAME_SUGGESTIONS)
                                .then(Commands.argument("level", IntegerArgumentType.integer(-20))
                                        .executes(this::addPassiveSkill)))
        );
    }

    private int addPassiveSkill(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {

        ServerPlayer player = context.getSource().getPlayerOrException();
        if (player.hasPermissions(4)) {
            if (!player.level().isClientSide && player.level().getServer() != null) {
                String skillId = StringArgumentType.getString(context, "skill_name");
                int level = IntegerArgumentType.getInteger(context, "level");
                boolean success = AllPlayersInfo.get(player.getUUID()).addPassiveSkill(skillId, level);
                if (success) {
                    MutableComponent message = Component.literal("[Super Skills System]")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                            .append(Component.literal(" Added 1 Skill1").setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
                    player.sendSystemMessage(message);

                } else {
                    MutableComponent message = Component.literal("[Super Skills System]")
                            .setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD))
                            .append(Component.literal("Error!!").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                    player.sendSystemMessage(message);
                }
            }
        }
        return 0;
    }

    public static final SuggestionProvider<CommandSourceStack> SKILL_NAME_SUGGESTIONS = (context, builder) -> {
        for (PassiveSkill skill : ReadConfig.passiveSkills) {
            builder.suggest(skill.name);
        }
        return builder.buildFuture();
    };
}