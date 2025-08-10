package com.github.shinjoy991.superskillssystem.config;

import com.github.shinjoy991.superskillssystem.helpers.skill.PassiveSkill;
import com.github.shinjoy991.superskillssystem.helpers.skill.SectTypes;
import com.github.shinjoy991.superskillssystem.helpers.skill.SkillTags;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.shinjoy991.superskillssystem.SSS.LOGGER;

public class ReadConfig {
    public static JsonObject jsonObject;
    public static List<PassiveSkill> passiveSkills = new ArrayList<>();
//    private static int errordelay = 300;

    public static void readJsonValue(Path configFile) {
        try {
            String jsonString = Files.readString(configFile, StandardCharsets.UTF_8);
            jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String skillKey = entry.getKey();
                if ("__comment".equals(skillKey)) {
                    continue;
                }

                JsonObject skillObj = entry.getValue().getAsJsonObject();
//                String name = skillObj.get("name").getAsString();
                SectTypes sectType = SectTypes.NONE; // hoặc giá trị mặc định tùy bạn
                if (skillObj.has("sect")) {
                    try {
                        sectType = SectTypes.fromName(skillObj.get("sect").getAsString());
                    } catch (IllegalArgumentException ignored) {

                    }
                }

                List<SkillTags> tags = new ArrayList<>();
                for (JsonElement tagElement : skillObj.getAsJsonArray("tags")) {
                    String tagName = tagElement.getAsString();
                    try {
                        SkillTags tag = SkillTags.fromName(tagName.toUpperCase());
                        tags.add(tag);
                    } catch (IllegalArgumentException e) {
                        LOGGER.warn("[Super Skills System] Unknown skill tag '{}', skipping.", tagName);
                        continue;
                    }
                }
                Map<SkillTags, Float> base = new HashMap<>();
                JsonObject baseObj = skillObj.getAsJsonObject("base");
                for (Map.Entry<String, JsonElement> baseEntry : baseObj.entrySet()) {
                    try {
                        SkillTags tag = SkillTags.valueOf(baseEntry.getKey().toUpperCase());
                        base.put(tag, baseEntry.getValue().getAsFloat());
                    } catch (IllegalArgumentException e) {
                        LOGGER.warn("[Super Skills System] Unknown base tag '{}', skipping.", baseEntry.getKey());
                        continue;
                    }
                }

                Map<SkillTags, Float> bonuses = new HashMap<>();
                JsonObject bonusObj = skillObj.getAsJsonObject("bonus");
                for (Map.Entry<String, JsonElement> bonusEntry : bonusObj.entrySet()) {
                    try {
                        SkillTags tag = SkillTags.valueOf(bonusEntry.getKey().toUpperCase());
                        bonuses.put(tag, bonusEntry.getValue().getAsFloat());
                    } catch (IllegalArgumentException e) {
                        LOGGER.warn("[Super Skills System] Unknown bonus tag '{}', skipping.", bonusEntry.getKey());
                        continue;
                    }
                }
                passiveSkills.add(new PassiveSkill(skillKey, sectType, tags, base, bonuses));
            }

        } catch (IOException e) {
            LOGGER.error("[Super Skills System] Error reading config JSON file: {}", e.toString());
        } catch (Exception e) {
            LOGGER.error("[Super Skills System] Invalid JSON format: {}", e.toString());
        }
    }

//    public static Object getConfig(String key, String subKey, int getInt) {
//        try {
//            JsonElement element = jsonObject.getAsJsonObject(key).get(subKey);
//            if (getInt != 1) {
//                return element.getAsString();
//            }
//            return element.getAsInt();
//        } catch (Exception e) {
//            if (errordelay > 300) {
//                errordelay = 0;
//                LOGGER.error(e);
//                LOGGER.error("[Super Skills System] error {} in {}", subKey, key);
//            } else
//                errordelay++;
//            if (getInt != 1) {
//                return "null";
//            }
//            return -1;
//        }
//    }

    public static int reloadConfig() {
        try {
            String jsonString = new String(Files.readAllBytes(CreateJson.configFile),
                    StandardCharsets.UTF_8);
            jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();

            return 0;
        } catch (IOException e) {
            LOGGER.error("[Super Skills System] Config reload error " + e);
            return 1;
        }
    }

}