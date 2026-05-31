package com.github.shinjoy991.superskillssystem;

import com.github.shinjoy991.superskillssystem.config.CreateJson;
import com.github.shinjoy991.superskillssystem.entity.trading.ModVillagers;
import com.github.shinjoy991.superskillssystem.entity.trading.SectVillager;
import com.github.shinjoy991.superskillssystem.gui.screen.PrimeEXPGrinderScreen;
import com.github.shinjoy991.superskillssystem.gui.screen.SectVillagerScreen;
import com.github.shinjoy991.superskillssystem.register.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.github.shinjoy991.superskillssystem.network.ModNetworking;

import static com.github.shinjoy991.superskillssystem.config.ReadConfig.readJsonValue;

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
        RegisterEntity.ENTITY_TYPES.register(bus);
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
//        InitClientEvents.init();
//        ModLootModifiers.register(bus);
//        MinecraftForge.EVENT_BUS.register(RendererRegister.class);
        MinecraftForge.EVENT_BUS.register(this);
//        bus.addListener(this::onLoadConfig);
        Config.loadCustomConfig();
        ModVillagers.register(bus);
        bus.addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(RegisterMenu.PRIME_EXP_GRINDER_MENU.get(), PrimeEXPGrinderScreen::new);
        MenuScreens.register(RegisterMenu.SECT_VILLAGER_MENU.get(), SectVillagerScreen::new);
    }


//    private void onLoadConfig(final ModConfigEvent.Loading event) {
//        Config.onLoad(event);
//    }

    private void setup(@NotNull FMLCommonSetupEvent event) {
        ModNetworking.registerPackets();
        CreateJson.CreateJsonConfigFile();
        readJsonValue(CreateJson.configFile);

        LOGGER.info("Mushroom Edition PREINIT setting up...");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("sss hello from server starting");
    }




    @Mod.EventBusSubscriber(
            modid = MODID,
            bus = Mod.EventBusSubscriber.Bus.MOD
    )
    public static class ServerRegisterHandler {
//        @SubscribeEvent
//        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
//            event.registerLayerDefinition(OakLogModel.LAYER_LOCATION, OakLogModel::createBodyLayer);
//            event.registerLayerDefinition(ItemEntityDiamondModel.LAYER_LOCATION, ItemEntityDiamondModel::createBodyLayer);
//            event.registerLayerDefinition(MushPunchEntityModel.LAYER_LOCATION, MushPunchEntityModel::createBodyLayer);
//            event.registerLayerDefinition(MooshroomKingModel.LAYER_LOCATION, MooshroomKingModel::createBodyLayer);
//        }

        @SubscribeEvent
        public static void onAttributeCreate(EntityAttributeCreationEvent event) {
            event.put(RegisterEntity.SECT_WARRIOR_VILLAGER.get(), SectVillager.createAttributes().build());
        }
    }

    @Mod.EventBusSubscriber(
            modid = MODID,
            bus = Mod.EventBusSubscriber.Bus.MOD,
            value = {Dist.CLIENT}
    )
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(RegisterEntity.SECT_WARRIOR_VILLAGER.get(), VillagerRenderer::new);
        }

//        @SubscribeEvent
//        public static void registerParticles(RegisterParticleProvidersEvent event) {
//            Minecraft.getInstance().particleEngine.register(RegisterParticle.BARRIER_PARTICLE.get(), BarrierParticle.Provider::new);
//            Minecraft.getInstance().particleEngine.register(RegisterParticle.GOD_CONFUSE_PARTICLE.get(), new GodConfuseParticle.Provider());
//        }
    }
}
