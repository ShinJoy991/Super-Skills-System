package com.github.shinjoy991.superskillssystem.register;


import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.gui.screen.PlayerInfoScreen;
import com.github.shinjoy991.superskillssystem.gui.screen.PrimeEXPGrinderScreen;
import com.github.shinjoy991.superskillssystem.gui.screen.SectVillagerScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = SSS.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InitClientEvents {

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(InitClientEvents::onClientSetup);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(RegisterMenu.PRIME_EXP_GRINDER_MENU.get(), PrimeEXPGrinderScreen::new);
      //  MenuScreens.register(RegisterMenu.PLAYER_INFO_MENU.get(), PlayerInfoScreen::new);
        MenuScreens.register(RegisterMenu.SECT_VILLAGER_MENU.get(), SectVillagerScreen::new);

    }


}