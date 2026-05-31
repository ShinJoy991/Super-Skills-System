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
import static com.github.shinjoy991.superskillssystem.helpers.skill.SkillTags.ATK_PERCENT;
import static com.github.shinjoy991.superskillssystem.helpers.skill.SkillTags.RANGE_PERCENT;

public class CreateJson {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    public static Path configPath = FMLPaths.CONFIGDIR.get().resolve("superskillssystem");
    public static Path configFile = configPath.resolve("skills_define.json");

    public static void CreateJsonConfigFile() {
        if (!Files.exists(configFile)) {
            try {
                Files.createDirectories(configPath);
            } catch (IOException e) {
                LOGGER.error("[Super Skills System] Failed to create directory: {}", configPath, e);
                return;
            }
        }
        else {
            LOGGER.info("Config file already exists, skipping creation: {}", configFile);
            return;
        }

        Map<String, Object> jsonData = new LinkedHashMap<>();
        List<String> comments = new ArrayList<>();
        comments.add("This is config section for Super Skills System mod");
        comments.add("Pretty easy, change it to match your desire, go to mod's page for more information");

        jsonData.put("__comment", comments);


        jsonData.put("overlord_sutra", createData(
                "warrior",
                Arrays.asList("MANA_PERCENT", "def"),
                Map.of("MANA_PERCENT", 10, "def", 1),
                Map.of("MANA_PERCENT", 1, "DEF", 1)
        ));

        jsonData.put("tyrant_body_divine_technique", createData(
                "warrior",
                Arrays.asList("hp", "def"),
                Map.of("hp", 1, "def", 2),
                Map.of("hp", 1, "def", 2)
        ));

        jsonData.put("war_technique", createData(
                "warrior",
                Arrays.asList(ATK_PERCENT.value(), RANGE_PERCENT.value()),
                Map.of(ATK_PERCENT.value(), 10, RANGE_PERCENT.value(), 10),
                Map.of(ATK_PERCENT.value(), 2, RANGE_PERCENT.value(), 2)
        ));






        try (FileWriter writer = new FileWriter(configFile.toFile())) {
            GSON.toJson(jsonData, writer);
        } catch (IOException exception) {
            LOGGER.error("[Super Skills System] Failed to write config file: {}", configFile, exception);
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