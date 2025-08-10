package com.github.shinjoy991.superskillssystem.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.github.shinjoy991.superskillssystem.SSS.LOGGER;

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

        jsonData.put("overlord_sutra", createData1());
        jsonData.put("tyrant_body_divine_technique", createData2());

        try (FileWriter writer = new FileWriter(configFile.toFile())) {
            GSON.toJson(jsonData, writer);
        } catch (IOException exception) {
            LOGGER.error("[Super Skills System] Failed to write config file: {}", configFile, exception);
        }
    }


    private static JsonObject createData1() {
        JsonObject root = new JsonObject();

//        root.addProperty("name", "skill.overlord_sutra");
        root.addProperty("sect", "warrior");

        JsonArray tags = new JsonArray();
        tags.add("MANA_PERCENT");
        tags.add("def");
        root.add("tags", tags);

        JsonObject base = new JsonObject();
        base.addProperty("MANA_PERCENT", 10);
        base.addProperty("def", 1);
        root.add("base", base);

        JsonObject bonuses = new JsonObject();
        bonuses.addProperty("MANA_PERCENT", 1);
        bonuses.addProperty("DEF", 1);
        root.add("bonus", bonuses);

        return root;
    }

    private static JsonObject createData2() {
        JsonObject root = new JsonObject();

//        root.addProperty("name", "tyrant_body_divine_technique");
        root.addProperty("sect", "warrior");

        JsonArray tags = new JsonArray();
        tags.add("hp");
        tags.add("def");
        root.add("tags", tags);

        JsonObject base = new JsonObject();
        base.addProperty("hp", 1);
        base.addProperty("def", 2);
        root.add("base", base);

        JsonObject bonuses = new JsonObject();
        bonuses.addProperty("hp", 1);
        bonuses.addProperty("def", 2);
        root.add("bonus", bonuses);

        return root;
    }

}