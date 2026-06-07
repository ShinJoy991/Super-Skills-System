package com.github.shinjoy991.superskillssystem.command;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import com.github.shinjoy991.superskillssystem.SSS;

@Mod.EventBusSubscriber(modid = SSS.MODID)
public class CommandRegister {

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {

        new ReloadCommand(event.getDispatcher());
        new ResetPlayerLevel(event.getDispatcher());
        new AddPrimeLevelCommand(event.getDispatcher());
        new ManaCommand(event.getDispatcher());
        new AddPassiveSkillCommand(event.getDispatcher());
        new ResetPassiveSkillCommand(event.getDispatcher());
        new AddSectTypeCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }
}