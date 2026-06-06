package com.github.shinjoy991.superskillssystem;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

//@Mod.EventBusSubscriber(modid = SSS.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final Map<String, ForgeConfigSpec.IntValue> configValues = new HashMap<>();
    private static final Map<String, ForgeConfigSpec.DoubleValue> configDoubleValues = new HashMap<>();


    static {
        configDoubleValues.put("SCALE_DMG", BUILDER.comment("Scale damage dealt by players")
                .defineInRange("scale_dmg", 1f, 0.0001f, 10000f));
        configValues.put("MOB_MAX_CRIT", BUILDER.comment("Normal mob max Critical Chance")
                .defineInRange("mob_max_crit", 20, 0, 100));
        configValues.put("MOB_MIN_CRIT", BUILDER.comment("Normal mob min Critical Chance")
                .defineInRange("mob_min_crit", 0, 0, 100));
        configValues.put("MOB_MAX_EVASION", BUILDER.comment("Normal mob max evasion")
                .defineInRange("mob_max_evasion", 20, 0, 100));
        configValues.put("MOB_MIN_EVASION", BUILDER.comment("Normal mob min evasion")
                .defineInRange("mob_min_evasion", 0, 0, 100));
        configValues.put("MOB_MAX_DMG_REDUCTION", BUILDER.comment("Normal mob max damage reduction")
                .defineInRange("mob_max_dmg_reduction", 20, 0, 100));
        configValues.put("MOB_MIN_DMG_REDUCTION", BUILDER.comment("Normal mob min damage reduction")
                .defineInRange("mob_min_dmg_reduction", 0, 0, 100));
        configValues.put("MOB_MAX_RESISTANCE", BUILDER.comment("Normal mob max resistance")
                .defineInRange("mob_max_resistance", 20, 0, 100));
        configValues.put("MOB_MIN_RESISTANCE", BUILDER.comment("Normal mob min resistance")
                .defineInRange("mob_min_resistance", 0, 0, 100));
        configDoubleValues.put("SECT_PRICE_MULTIPLIER", BUILDER.comment("Price multiplier for sect master trades")
                .defineInRange("sect_price_multiplier", 0.077f, 0.01f, 100f));

        SPEC = BUILDER.build();
    }

    static final ForgeConfigSpec SPEC;

    public static double SCALE_DMG;
    public static int MOB_MAX_CRIT;
    public static int MOB_MIN_CRIT;
    public static int MOB_MAX_EVASION;
    public static int MOB_MIN_EVASION;
    public static int MOB_MAX_DMG_REDUCTION;
    public static int MOB_MIN_DMG_REDUCTION;
    public static int MOB_MAX_RESISTANCE;
    public static int MOB_MIN_RESISTANCE;

    public static double SECT_PRICE_MULTIPLIER;

//    @SubscribeEvent
//    static void onLoad(final ModConfigEvent event) {
//        MOB_MAX_EVASION = configValues.get("MOB_MAX_EVASION").get();
//        MOB_MIN_EVASION = configValues.get("MOB_MIN_EVASION").get();
//        MOB_MAX_DMG_REDUCTION = configValues.get("MOB_MAX_DMG_REDUCTION").get();
//        MOB_MIN_DMG_REDUCTION = configValues.get("MOB_MIN_DMG_REDUCTION").get();
//        MOB_MAX_RESISTANCE = configValues.get("MOB_MAX_RESISTANCE").get();
//        MOB_MIN_RESISTANCE = configValues.get("MOB_MIN_RESISTANCE").get();
//
//    }


    public static void loadCustomConfig() {
        try {
            Path configDir = FMLPaths.CONFIGDIR.get().resolve("superskillssystem");
            Files.createDirectories(configDir); // tạo thư mục nếu chưa có

            Path file = configDir.resolve("sss-common.toml");

            CommentedFileConfig configData = CommentedFileConfig.builder(file)
                    .autosave()
                    .writingMode(WritingMode.REPLACE)
                    .sync()
                    .build();

            configData.load();
            SPEC.setConfig(configData);

            // Load giá trị sau khi set config
            SCALE_DMG = configDoubleValues.get("SCALE_DMG").get();
            MOB_MAX_CRIT = configValues.get("MOB_MAX_CRIT").get();
            MOB_MIN_CRIT = configValues.get("MOB_MIN_CRIT").get();
            MOB_MAX_EVASION = configValues.get("MOB_MAX_EVASION").get();
            MOB_MIN_EVASION = configValues.get("MOB_MIN_EVASION").get();
            MOB_MAX_DMG_REDUCTION = configValues.get("MOB_MAX_DMG_REDUCTION").get();
            MOB_MIN_DMG_REDUCTION = configValues.get("MOB_MIN_DMG_REDUCTION").get();
            MOB_MAX_RESISTANCE = configValues.get("MOB_MAX_RESISTANCE").get();
            MOB_MIN_RESISTANCE = configValues.get("MOB_MIN_RESISTANCE").get();
            SECT_PRICE_MULTIPLIER = configDoubleValues.get("SECT_PRICE_MULTIPLIER").get();

        } catch (IOException e) {
            SSS.LOGGER.error("Failed to load custom config file", e);
        }
    }
}
