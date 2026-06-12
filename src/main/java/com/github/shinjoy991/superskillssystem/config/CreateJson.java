package com.github.shinjoy991.superskillssystem.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.github.shinjoy991.superskillssystem.SSS.LOGGER;
import static com.github.shinjoy991.superskillssystem.helpers.skill.SkillTags.*;

public class CreateJson {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    public static Path configPath = FMLPaths.CONFIGDIR.get().resolve("superskillssystem");
    public static Path configFile = configPath.resolve("skills_define.json");

    public static void CreateJsonConfigFile() {
        if (!Files.exists(configFile)) {
            try {
                Files.createDirectories(configPath);
            } catch (IOException e) {
                LOGGER.error("[Super Skill System] Failed to create directory: {}", configPath, e);
                return;
            }
        }
        else {
            LOGGER.info("Config file already exists, skipping creation: {}", configFile);
            return;
        }

        Map<String, Object> jsonData = new LinkedHashMap<>();
        List<String> comments = new ArrayList<>();
        comments.add("This is config section for Super Skill System mod");
        comments.add("Pretty easy, change it to match your desire, go to mod's page for more information");

        jsonData.put("__comment", comments);

        // Warrior skills
        jsonData.put("overlord_sutra", createData(
                "warrior",
                Arrays.asList(ATK_FLAT.value(), ATK_PERCENT.value()),
                Map.of(ATK_FLAT.value(), 2, ATK_PERCENT.value(), 5),
                Map.of(ATK_FLAT.value(), 0.2, ATK_PERCENT.value(), 0.25)
        ));

        jsonData.put("war_technique", createData(
                "warrior",
                Arrays.asList(ATK_PERCENT.value(), RANGE_PERCENT.value()),
                Map.of(ATK_PERCENT.value(), 5, RANGE_PERCENT.value(), 5),
                Map.of(ATK_PERCENT.value(), 0.25, RANGE_PERCENT.value(), 0.25)
        ));

        jsonData.put("warrior_refinement", createData(
                "warrior",
                Arrays.asList(STR_PERCENT.value(), DEF_FLAT.value(), HP_FLAT.value()),
                Map.of(ATK_FLAT.value(), 1, DEF_FLAT.value(), 1, HP_FLAT.value(), 3),
                Map.of(ATK_FLAT.value(), 0.1, DEF_FLAT.value(), 0.1, HP_FLAT.value(), 0.15)
                ));

        jsonData.put("striking_technique", createData(
                "warrior",
                Arrays.asList(CRIT_CHANCE.value(), ATTACK_SPEED.value()),
                Map.of(CRIT_CHANCE.value(), 5, ATTACK_SPEED.value(), 0.05),
                Map.of(CRIT_CHANCE.value(), 1, ATTACK_SPEED.value(), 0.0075)
        ));

        jsonData.put("augmentation_tempering", createData(
                "warrior",
                Arrays.asList(HP_FLAT.value(), STR_FLAT.value()),
                Map.of(HP_FLAT.value(), 3, STR_FLAT.value(), 5),
                Map.of(HP_FLAT.value(), 0.15, STR_FLAT.value(), 1.25)
        ));

        jsonData.put("military_strategy", createData(
                "warrior",
                Arrays.asList(DEF_PEN_FLAT.value(), STR_PERCENT.value()),
                Map.of(DEF_PEN_FLAT.value(), 2, STR_PERCENT.value(), 2),
                Map.of(DEF_PEN_FLAT.value(), 0.1, STR_PERCENT.value(), 0.5)
        ));

        jsonData.put("combat_training", createData(
                "warrior",
                Arrays.asList(COUNTER_CHANCE.value(), EVASION.value()),
                Map.of(COUNTER_CHANCE.value(), 5, EVASION.value(), 5),
                Map.of(COUNTER_CHANCE.value(), 0.25, EVASION.value(), 0.75)
        ));

        jsonData.put("iron_will", createData(
                "warrior",
                Arrays.asList(RESISTANCE.value(), DMG_REDUCTION.value()),
                Map.of(RESISTANCE.value(), 2, DMG_REDUCTION.value(), 2),
                Map.of(RESISTANCE.value(), 0.5, DMG_REDUCTION.value(), 0.25)
        ));

        jsonData.put("siege_technique", createData(
                "warrior",
                Arrays.asList(RESISTANCE_PEN.value(), MAGIC_RESISTANCE_PEN.value()),
                Map.of(RESISTANCE_PEN.value(), 5, MAGIC_RESISTANCE_PEN.value(), 5),
                Map.of(RESISTANCE_PEN.value(), 0.5, MAGIC_RESISTANCE_PEN.value(), 0.25)
        ));

        jsonData.put("vanguard_assault", createData(
                "warrior",
                Arrays.asList(ATTACK_SPEED.value(), DEF_PEN_PERCENT.value(), AGI_PERCENT.value()),
                Map.of(ATTACK_SPEED.value(), 0.05, DEF_PEN_PERCENT.value(), 3, AGI_PERCENT.value(), 1),
                Map.of(ATTACK_SPEED.value(), 0.0075, DEF_PEN_PERCENT.value(), 0.25, AGI_PERCENT.value(), 0.3)
        ));

        jsonData.put("battlefield_adaptation", createData(
                "warrior",
                Arrays.asList(PER_FLAT.value(), EVASION.value()),
                Map.of(PER_FLAT.value(), 5, EVASION.value(), 3),
                Map.of(PER_FLAT.value(), 1, EVASION.value(), 0.35)
        ));

        jsonData.put("defensive_tactics", createData(
                "warrior",
                Arrays.asList(DEF_PERCENT.value(), DMG_REDUCTION.value()),
                Map.of(DEF_PERCENT.value(), 5, DMG_REDUCTION.value(), 3),
                Map.of(DEF_PERCENT.value(), 0.5, DMG_REDUCTION.value(), 0.5)
        ));

        jsonData.put("offensive_tactics", createData(
                "warrior",
                Arrays.asList(RANGE_FLAT.value(), ACCURACY.value()),
                Map.of(RANGE_FLAT.value(), 2, ACCURACY.value(), 5),
                Map.of(RANGE_FLAT.value(), 0.2, ACCURACY.value(), 0.75)
        ));

        jsonData.put("fearless_charge", createData(
                "warrior",
                Arrays.asList(SPEED.value(), VIT_FLAT.value()),
                Map.of(SPEED.value(), 0.005, VIT_FLAT.value(), 1),
                Map.of(SPEED.value(), 0.0002, VIT_FLAT.value(), 0.15)
        ));

        jsonData.put("tyrant_body_divine_technique", createData(
                "warrior",
                Arrays.asList(HP_FLAT.value(), HP_PERCENT.value()),
                Map.of(HP_FLAT.value(), 4, HP_PERCENT.value(), 4),
                Map.of(HP_FLAT.value(), 0.2, HP_PERCENT.value(), 0.2)
        ));

        jsonData.put("weapon_master", createData(
                "warrior",
                Arrays.asList(WEAPON_DMG_PERCENT.value()),
                Map.of(WEAPON_DMG_PERCENT.value(), 5),
                Map.of(WEAPON_DMG_PERCENT.value(), 0.5)
        ));

        // Archer skills
        jsonData.put("draconic_swiftness", createData(
                "archer",
                Arrays.asList(SPEED.value()),
                Map.of(SPEED.value(), 0.1f),
                Map.of(SPEED.value(), 0.02f)
        ));

        jsonData.put("qi_outburst", createData(
                "archer",
                Arrays.asList(CRIT_CHANCE.value(), RANGE_FLAT.value()),
                Map.of(CRIT_CHANCE.value(), 10, RANGE_FLAT.value(), 10),
                Map.of(CRIT_CHANCE.value(), 2, RANGE_FLAT.value(), 2)
        ));


        jsonData.put("hawkeye", createData(
                "archer",
                Arrays.asList(ACCURACY.value()),
                Map.of(ACCURACY.value(), 10),
                Map.of(ACCURACY.value(), 3)
        ));

        jsonData.put("swift_draw", createData(
                "archer",
                Arrays.asList(ATTACK_SPEED.value()),
                Map.of(ATTACK_SPEED.value(), 0.1f),
                Map.of(ATTACK_SPEED.value(), 0.02f)
        ));

        jsonData.put("wind_step", createData(
                "archer",
                Arrays.asList(SPEED.value()),
                Map.of(SPEED.value(), 0.1f),
                Map.of(SPEED.value(), 0.02f)
        ));

        // Mage skills
        jsonData.put("mystic_talisman", createData(
                "mage",
                Arrays.asList(MANA_FLAT.value(), INT_FLAT.value()),
                Map.of(MANA_FLAT.value(), 20, INT_FLAT.value(), 5),
                Map.of(MANA_FLAT.value(), 5, INT_FLAT.value(), 2)
        ));

        jsonData.put("tome_of_wisdom", createData(
                "mage",
                Arrays.asList(MANA_PERCENT.value(), INT_PERCENT.value()),
                Map.of(MANA_PERCENT.value(), 10, INT_PERCENT.value(), 10),
                Map.of(MANA_PERCENT.value(), 2, INT_PERCENT.value(), 2)
        ));

        jsonData.put("tome_of_covert_lore", createData(
                "mage",
                Arrays.asList(MANA_PERCENT.value(), PER_FLAT.value()),
                Map.of(MANA_PERCENT.value(), 10, PER_FLAT.value(), 5),
                Map.of(MANA_PERCENT.value(), 2, PER_FLAT.value(), 2)
        ));

        // SWOORDSMAN skills


        // Medic skills
        jsonData.put("vital_essence_manipulation", createData(
                "medic",
                Arrays.asList(HP_PERCENT.value(), MANA_PERCENT.value()),
                Map.of(HP_PERCENT.value(), 10, MANA_PERCENT.value(), 10),
                Map.of(HP_PERCENT.value(), 2, MANA_PERCENT.value(), 2)
        ));


        try (FileWriter writer = new FileWriter(configFile.toFile())) {
            GSON.toJson(jsonData, writer);
        } catch (IOException exception) {
            LOGGER.error("[Super Skill System] Failed to write config file: {}", configFile, exception);
        }
    }


    private static JsonObject createData(
            String sect,
            List<String> tagsList,
            Map<String, Number> baseMap,
            Map<String, Number> bonusMap
    ) {
        JsonObject root = new JsonObject();
        root.addProperty("sect", sect);

        JsonArray tags = new JsonArray();
        for (String tag : tagsList) {
            tags.add(tag);
        }
        root.add("tags", tags);

        JsonObject base = new JsonObject();
        for (Map.Entry<String, Number> entry : baseMap.entrySet()) {
            base.addProperty(entry.getKey(), entry.getValue());
        }
        root.add("base", base);

        JsonObject bonus = new JsonObject();
        for (Map.Entry<String, Number> entry : bonusMap.entrySet()) {
            bonus.addProperty(entry.getKey(), entry.getValue());
        }
        root.add("bonus", bonus);

        return root;
    }

}