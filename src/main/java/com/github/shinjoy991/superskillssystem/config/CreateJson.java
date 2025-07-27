//package com.github.shinjoy991.sss.config;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
//import net.minecraftforge.fml.loading.FMLPaths;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import static com.github.shinjoy991.balanced_enchantments.BalancedEnchantments.LOGGER;
//
//public class CreateJson {
//    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
//    public static Path configPath = FMLPaths.CONFIGDIR.get().resolve("balancedenchantments");
//    public static Path configFile = configPath.resolve("balanced_enchantments_config.json");
//
//    public static void CreateJsonConfigFile() {
//        if (!Files.exists(configPath)) {
//            try {
//                Files.createDirectories(configPath);
//            } catch (IOException e) {
//                LOGGER.error("[Balanced Enchantments] Failed to create directory: {}", configPath, e);
//                return;
//            }
//        }
//
//        if (Files.exists(configFile)) {
//            LOGGER.info("[Balanced Enchantments] Config file already exists, checking for missing sections: {}", configFile);
//            updateConfigFile(configFile);
//            return;
//        }
//
//        Map<String, Object> jsonData = new LinkedHashMap<>();
//        List<String> comments = new ArrayList<>();
//        comments.add("This is config section for Balanced Enchantments mod");
//        comments.add("Pretty easy, change it to match your desire, go to mod's page for more information");
//        comments.add("Use integer numbers and true/false to prevent errors, time normally in tick");
//        jsonData.put("__comment", comments);
//
//        jsonData.put("blockmasksoul", createData1());
//        jsonData.put("volley", createData2());
//        jsonData.put("magmawalker", createData3());
//        jsonData.put("supercharged", createData4());
//        jsonData.put("curseofdurability", createData5());
//        jsonData.put("herolanding", createData6());
//        jsonData.put("cmoon", createData7());
//        jsonData.put("statusprotection", createData8());
//        jsonData.put("iceaspect", createData9());
//        jsonData.put("huntinginstinct", createData10());
//        jsonData.put("curseofunstable", createData11());
//        jsonData.put("truesharpness", createData12());
//        jsonData.put("invisibleshield", createData13());
//        jsonData.put("weaponart", createData14());
//        jsonData.put("mightyforce", createData15());
//        jsonData.put("miningfocus", createData16());
//        jsonData.put("automachine", createData17());
//        jsonData.put("linking", createData18());
//        jsonData.put("curseofprovocation", createData19());
//
//        try (FileWriter writer = new FileWriter(configFile.toFile())) {
//            GSON.toJson(jsonData, writer);
//        } catch (IOException exception) {
//            LOGGER.error("[Balanced Enchantments] Failed to write config file: {}", configFile, exception);
//        }
//    }
//
//    private static void updateConfigFile(Path configFile) {
//        try {
//            String jsonString = new String(Files.readAllBytes(configFile), StandardCharsets.UTF_8);
//            JsonObject jsonObject = GSON.fromJson(jsonString, JsonObject.class);
//
//            addMissingSection(jsonObject, "blockmasksoul", createData1());
//            addMissingSection(jsonObject, "volley", createData2());
//            addMissingSection(jsonObject, "magmawalker", createData3());
//            addMissingSection(jsonObject, "supercharged", createData4());
//            addMissingSection(jsonObject, "curseofdurability", createData5());
//            addMissingSection(jsonObject, "herolanding", createData6());
//            addMissingSection(jsonObject, "cmoon", createData7());
//            addMissingSection(jsonObject, "statusprotection", createData8());
//            addMissingSection(jsonObject, "iceaspect", createData9());
//            addMissingSection(jsonObject, "huntinginstinct", createData10());
//            addMissingSection(jsonObject, "curseofunstable", createData11());
//            addMissingSection(jsonObject, "truesharpness", createData12());
//            addMissingSection(jsonObject, "invisibleshield", createData13());
//            addMissingSection(jsonObject, "weaponart", createData14());
//            addMissingSection(jsonObject, "mightyforce", createData15());
//            addMissingSection(jsonObject, "miningfocus", createData16());
//            addMissingSection(jsonObject, "automachine", createData17());
//            addMissingSection(jsonObject, "linking", createData18());
//            addMissingSection(jsonObject, "curseofprovocation", createData19());
//
//            try (FileWriter writer = new FileWriter(configFile.toFile())) {
//                GSON.toJson(jsonObject, writer);
//            }
//        } catch (IOException e) {
//            LOGGER.error("[Balanced Enchantments] Error updating Json config file: {}", configFile, e);
//        }
//    }
//
//    private static void addMissingSection(JsonObject jsonObject, String key, JsonObject data) {
//        if (!jsonObject.has(key)) {
//            jsonObject.add(key, data);
//        }
//    }
//
//    private static JsonObject createData1() {
//        JsonObject data1 = new JsonObject();
//        data1.addProperty("enable", "true");
//        data1.addProperty("maxlevel", "2");
//        data1.addProperty("onlyhoe", "false");
//        data1.addProperty("tickduration", "40");
//        data1.addProperty("chance", "25");
//        return data1;
//    }
//
//    private static JsonObject createData2() {
//        JsonObject data2 = new JsonObject();
//        data2.addProperty("enable", "true");
//        data2.addProperty("maxlevel", "3");
//        data2.addProperty("anglebetween", "10");
//        data2.addProperty("removesubarrow", "true");
//        data2.addProperty("matchinfinity", "true");
//        return data2;
//    }
//
//    private static JsonObject createData3() {
//        JsonObject data3 = new JsonObject();
//        data3.addProperty("enable", "true");
//        data3.addProperty("maxlevel", "3");
//        data3.addProperty("radiusperlvl", "1");
//        data3.addProperty("minduration", "20");
//        data3.addProperty("maxduration", "100");
//        return data3;
//    }
//
//    private static JsonObject createData4() {
//        JsonObject data4 = new JsonObject();
//        data4.addProperty("enable", "true");
//        data4.addProperty("maxlevel", "4");
//        data4.addProperty("dmgaddperlvl", "4");
//        data4.addProperty("weaknesslvl", "1");
//        data4.addProperty("weaknessduration", "60");
//        data4.addProperty("cooldown", "60");
//        return data4;
//    }
//
//    private static JsonObject createData5() {
//        JsonObject data5 = new JsonObject();
//        data5.addProperty("enable", "true");
//        data5.addProperty("minrandom", "-10");
//        data5.addProperty("maxrandom", "10");
//        return data5;
//    }
//
//    private static JsonObject createData6() {
//        JsonObject data6 = new JsonObject();
//        data6.addProperty("enable", "true");
//        data6.addProperty("maxlevel", "3");
//        data6.addProperty("heightaddperlvl", "3");
//        return data6;
//    }
//
//    private static JsonObject createData7() {
//        JsonObject data7 = new JsonObject();
//        data7.addProperty("enable", "true");
//        data7.addProperty("maxlevel", "2");
//        data7.addProperty("power", "2");
//        data7.addProperty("affecthealthbelow", "40");
//        return data7;
//    }
//
//    private static JsonObject createData8() {
//        JsonObject data8 = new JsonObject();
//        data8.addProperty("enable", "true");
//        data8.addProperty("maxlevel", "4");
//        data8.addProperty("initialcooldown", "2400");
//        data8.addProperty("cooldownreduceperlvl", "140");
//        data8.addProperty("initialchance", "45");
//        data8.addProperty("chanceincreaseperlvl", "3");
//        data8.addProperty("allstatuslevel", "10");
//        return data8;
//    }
//
//    private static JsonObject createData9() {
//        JsonObject data9 = new JsonObject();
//        data9.addProperty("enable", "true");
//        data9.addProperty("maxlevel", "2");
//        data9.addProperty("chance", "75");
//        data9.addProperty("freezedurationperlvl", "80");
//        return data9;
//    }
//
//    private static JsonObject createData10() {
//        JsonObject data10 = new JsonObject();
//        data10.addProperty("enable", "true");
//        data10.addProperty("chance", "75");
//        data10.addProperty("slownesslvl", "0");
//        data10.addProperty("strengthlvl", "0");
//        data10.addProperty("speedlvl", "1");
//        return data10;
//    }
//
//    private static JsonObject createData11() {
//        JsonObject data11 = new JsonObject();
//        data11.addProperty("enable", "true");
//        data11.addProperty("chance", "5");
//        return data11;
//    }
//
//    private static JsonObject createData12() {
//        JsonObject data12 = new JsonObject();
//        data12.addProperty("enable", "true");
//        data12.addProperty("adddmg", "5");
//        data12.addProperty("durabilityreduce", "10");
//        return data12;
//    }
//
//    private static JsonObject createData13() {
//        JsonObject data13 = new JsonObject();
//        data13.addProperty("enable", "true");
//        data13.addProperty("maxlevel", "4");
//        data13.addProperty("recovertick", "200");
//        data13.addProperty("percentreductionperlvl", "20");
//        return data13;
//    }
//
//    private static JsonObject createData14() {
//        JsonObject data14 = new JsonObject();
//        data14.addProperty("enable", "true");
//        data14.addProperty("maxlevel", "4");
//        data14.addProperty("percentreductionperlvl", "7");
//        return data14;
//    }
//
//    private static JsonObject createData15() {
//        JsonObject data15 = new JsonObject();
//        data15.addProperty("enable", "true");
//        data15.addProperty("stackdeletetick", "80");
//        data15.addProperty("percentdmgincrease", "20");
//        return data15;
//    }
//
//    private static JsonObject createData16() {
//        JsonObject data16 = new JsonObject();
//        data16.addProperty("enable", "true");
//        data16.addProperty("maxlevel", "3");
//        data16.addProperty("addtickperlvl", "160");
//        data16.addProperty("hastelvl", "1");
//        data16.addProperty("cooldown", "800");
//        return data16;
//    }
//
//    private static JsonObject createData17() {
//        JsonObject data17 = new JsonObject();
//        data17.addProperty("enable", "true");
//        data17.addProperty("maxlevel", "4");
//        data17.addProperty("addlengthperlvl", "2");
//        data17.addProperty("initiallength", "3");
//        return data17;
//    }
//
//    private static JsonObject createData18() {
//        JsonObject data18 = new JsonObject();
//        data18.addProperty("enable", "true");
//        data18.addProperty("maxlevel", "4");
//        data18.addProperty("addblockperlvl", "2");
//        return data18;
//    }
//
//    private static JsonObject createData19() {
//        JsonObject data19 = new JsonObject();
//        data19.addProperty("enable", "true");
//        data19.addProperty("radius", "2");
//        return data19;
//    }
//}