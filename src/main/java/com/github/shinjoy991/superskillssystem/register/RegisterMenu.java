package com.github.shinjoy991.superskillssystem.register;


import com.github.shinjoy991.superskillssystem.SSS;
import com.github.shinjoy991.superskillssystem.gui.menu.PlayerInfoMenu;
import com.github.shinjoy991.superskillssystem.gui.menu.PrimeEXPGrinderMenu;
import com.github.shinjoy991.superskillssystem.gui.menu.SectVillagerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterMenu {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SSS.MODID);

    public static final RegistryObject<MenuType<SectVillagerMenu>> SECT_VILLAGER_MENU =
            MENU_TYPES.register("sect_villager_menu",
                    () -> IForgeMenuType.create(SectVillagerMenu::new));
    public static final RegistryObject<MenuType<PrimeEXPGrinderMenu>> PRIME_EXP_GRINDER_MENU = MENU_TYPES.register("prime_exp_grinder_menu",
            () -> IForgeMenuType.create(PrimeEXPGrinderMenu::new));
//    public static final RegistryObject<MenuType<PlayerInfoMenu>> PLAYER_INFO_MENU = MENU_TYPES.register("player_info_menu",
//            () -> IForgeMenuType.create(PlayerInfoMenu::new));

}