//package com.github.shinjoy991.sss.register;
//
//import com.github.shinjoy991.balanced_enchantments.enchantments.*;
//import com.github.shinjoy991.balanced_enchantments.enchantments.curses.CurseOfProvocation;
//import com.github.shinjoy991.balanced_enchantments.events.VolleyBowEvents;
//import net.minecraftforge.common.MinecraftForge;
//
//import static com.github.shinjoy991.balanced_enchantments.config.ReadConfig.getConfig;
//
//public class RegisterEvent {
//    public static void registermodevents() {
//        boolean allowVolley = getConfig("volley", "enable", 0).toString().equalsIgnoreCase("true");
//        boolean allowSuperCharged =
//                getConfig("supercharged", "enable", 0).toString().equalsIgnoreCase("true");
//        boolean allowHeroLanding =
//                getConfig("herolanding", "enable", 0).toString().equalsIgnoreCase(
//                        "true");
//        boolean allowStatusProtection =
//                getConfig("statusprotection", "enable", 0).toString().equalsIgnoreCase(
//                        "true");
//        boolean allowHuntingInstinct =
//                getConfig("huntinginstinct", "enable", 0).toString().equalsIgnoreCase(
//                        "true");
//        boolean allowTrueSharpness =
//                getConfig("truesharpness", "enable", 0).toString().equalsIgnoreCase(
//                        "true");
//        boolean allowInvisibleShield =
//                getConfig("invisibleshield", "enable", 0).toString().equalsIgnoreCase(
//                        "true");
//        boolean allowWeaponArt =
//                getConfig("weaponart", "enable", 0).toString().equalsIgnoreCase(
//                        "true");
//        boolean allowMightyForce =
//                getConfig("mightyforce", "enable", 0).toString().equalsIgnoreCase(
//                        "true");
//        boolean allowMiningFocus =
//                getConfig("miningfocus", "enable", 0).toString().equalsIgnoreCase(
//                        "true");
//        boolean allowAutoMachine =
//                getConfig("automachine", "enable", 0).toString().equalsIgnoreCase(
//                        "true");
//        boolean allowLinking =
//                getConfig("linking", "enable", 0).toString().equalsIgnoreCase(
//                        "true");
//        boolean allowCurseofProvocation =
//                getConfig("curseofprovocation", "enable", 0).toString().equalsIgnoreCase(
//                        "true");
//        if (allowVolley)
//            MinecraftForge.EVENT_BUS.register(VolleyBowEvents.class);
//        if (allowSuperCharged)
//            MinecraftForge.EVENT_BUS.register(SuperCharged.class);
//        if (allowHeroLanding)
//            MinecraftForge.EVENT_BUS.register(HeroLanding.class);
//        if (allowStatusProtection)
//            MinecraftForge.EVENT_BUS.register(StatusProtection.class);
//        if (allowHuntingInstinct)
//            MinecraftForge.EVENT_BUS.register(HuntingInstinct.class);
//        if (allowTrueSharpness)
//            MinecraftForge.EVENT_BUS.register(TrueSharpness.class);
//        if (allowInvisibleShield)
//            MinecraftForge.EVENT_BUS.register(InvisibleShield.class);
//        if (allowWeaponArt)
//            MinecraftForge.EVENT_BUS.register(WeaponArt.class);
//        if (allowMightyForce)
//            MinecraftForge.EVENT_BUS.register(MightyForce.class);
//        if (allowMiningFocus)
//            MinecraftForge.EVENT_BUS.register(MiningFocus.class);
//        if (allowAutoMachine)
//            MinecraftForge.EVENT_BUS.register(AutoMachine.class);
//        if (allowLinking)
//            MinecraftForge.EVENT_BUS.register(Linking.class);
//        if (allowCurseofProvocation)
//            MinecraftForge.EVENT_BUS.register(CurseOfProvocation.class);
//    }
//}