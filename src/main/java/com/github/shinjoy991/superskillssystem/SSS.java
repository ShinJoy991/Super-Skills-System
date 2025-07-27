package com.github.shinjoy991.superskillssystem;

import com.github.shinjoy991.superskillssystem.entity.trading.ModVillagers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.github.shinjoy991.superskillssystem.network.ModNetworking;
import com.github.shinjoy991.superskillssystem.register.InitClientEvents;
import com.github.shinjoy991.superskillssystem.register.RegisterBlock;
import com.github.shinjoy991.superskillssystem.register.RegisterItem;
import com.github.shinjoy991.superskillssystem.register.RegisterMenu;

@Mod(SSS.MODID)
public class SSS {
    public static final String MODID = "sss";
    public static final Logger LOGGER = LogManager.getLogger();

    public SSS() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        RegisterBlock.BLOCKS.register(bus);
        RegisterItem.ITEMS.register(bus);
        RegisterItem.CREATIVE_MODE_TAB.register(bus);
        RegisterMenu.MENU_TYPES.register(bus);
        InitClientEvents.init();
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
//        ModLootModifiers.register(bus);
//        MinecraftForge.EVENT_BUS.register(RendererRegister.class);
        MinecraftForge.EVENT_BUS.register(this);
//        bus.addListener(this::onLoadConfig);
        ModVillagers.register(bus);
    }

//    private void onLoadConfig(final ModConfigEvent.Loading event) {
//        Config.onLoad(event);
    ////        RegisterEvent.registermodevents();
//    }

    private void setup(@NotNull FMLCommonSetupEvent event) {
        ModNetworking.registerPackets();
        LOGGER.info("Mushroom Edition PREINIT setting up...");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("sss hello from server starting");
    }

}
