package com.trcx.ita.Common;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trcx.ita.CONSTS;
import com.trcx.ita.Common.Traits.BaseTrait;
import com.trcx.ita.Common.Traits.PotionTrait;
import com.trcx.ita.Common.Traits.ProtectionTrait;
import com.trcx.ita.ITA;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Config{
    public static File configDir;

    private static Configuration clientConfig;
    private static Configuration commonConfig;
    private static List<MaterialProperty> tempMaterials = new ArrayList<MaterialProperty>();
    private static List<PotionTrait> tempPotionTraits = new ArrayList<PotionTrait>();
    private static List<ProtectionTrait> tempProtectionTraits = new ArrayList<ProtectionTrait>();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static Gson gsonCompact = new GsonBuilder().create();
    private static String defConfigPath = "/assets/ita/config/";

    private static Type typeOfMaterials = new TypeToken<List<MaterialProperty>>() {}.getType();
    private static Type typeOfPotionTraits = new TypeToken<List<PotionTrait>>() {}.getType();
    private static Type typeOfProtectionTraits = new TypeToken<List<ProtectionTrait>>() {}.getType();

    public static void loadFromCachedJSON(String id){
        try {
            if (id.equals(CONSTS.packetMaterialId)) {
                tempMaterials = gson.fromJson(ITA.jsonMaterial, typeOfMaterials);
                ITA.Materials = new HashMap<String, MaterialProperty>();
                for (MaterialProperty prop : tempMaterials) {
                    if (prop.metadata == null) {
                        ITA.Materials.put(prop.identifier, prop);
                    } else {
                        ITA.Materials.put(prop.identifier + prop.metadata, prop);
                    }
                }
                ITA.Traits = new HashMap<String, BaseTrait>();
            } else if (id.equals(CONSTS.packetPotionTraitsId)) {
                tempPotionTraits = gson.fromJson(ITA.jsonPotionTraits, typeOfPotionTraits);
                for (PotionTrait trait : tempPotionTraits) {
                    ITA.Traits.put(trait.name, trait);
                }
            } else if (id.equals(CONSTS.packetProtectionTraitsId)) {
                tempProtectionTraits = gson.fromJson(ITA.jsonProtectionTraits, typeOfProtectionTraits);
                for (ProtectionTrait trait : tempProtectionTraits) {
                    ITA.Traits.put(trait.name, trait);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void loadMaterialConfig(File materialsFile) {
        try {
            if (materialsFile.exists()) {
                ITA.jsonMaterial = new String(Files.readAllBytes(Paths.get(materialsFile.getPath())));
            } else {
                InputStream is = Config.class.getResourceAsStream(defConfigPath+materialsFile.getName());
                ITA.jsonMaterial = org.apache.commons.io.IOUtils.toString(is, "UTF-8");
            }

            tempMaterials = gson.fromJson(ITA.jsonMaterial, typeOfMaterials);

            ITA.Materials = new HashMap<String, MaterialProperty>();
            for (MaterialProperty prop : tempMaterials) {
                prop.onLoadFromFileUpdate();
                if (prop.metadata == null){
                    ITA.Materials.put(prop.identifier, prop);
                } else {
                    ITA.Materials.put(prop.identifier + prop.metadata, prop);
                }
            }

            ITA.jsonMaterial = gson.toJson(tempMaterials, typeOfMaterials);
            Files.write(Paths.get(materialsFile.getPath()), ITA.jsonMaterial.getBytes());
            ITA.jsonMaterial = gsonCompact.toJson(tempMaterials, typeOfMaterials);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadPotionTraits(File potionTraitFile){
        try{
            if (potionTraitFile.exists()) {
                ITA.jsonPotionTraits = new String(Files.readAllBytes(Paths.get(potionTraitFile.getPath())));
            } else {
                InputStream is = Config.class.getResourceAsStream(defConfigPath+potionTraitFile.getName());
                ITA.jsonPotionTraits = org.apache.commons.io.IOUtils.toString(is,"UTF-8");
            }
            tempPotionTraits = gson.fromJson(ITA.jsonPotionTraits, typeOfPotionTraits);

            for (PotionTrait trait : tempPotionTraits) {
                trait.updateTrait();
                ITA.Traits.put(trait.name, trait);
            }
            ITA.jsonPotionTraits = gson.toJson(tempPotionTraits, typeOfPotionTraits);
            Files.write(Paths.get(potionTraitFile.getPath()), ITA.jsonPotionTraits.getBytes());
            ITA.jsonPotionTraits = gsonCompact.toJson(tempPotionTraits, typeOfPotionTraits);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void loadProtectionTraits(File protectionTraitFile){
        try {
            if (protectionTraitFile.exists()) {
                ITA.jsonProtectionTraits = new String(Files.readAllBytes(Paths.get(protectionTraitFile.getPath())));
            } else {
                InputStream is = Config.class.getResourceAsStream(defConfigPath+protectionTraitFile.getName());
                ITA.jsonProtectionTraits = org.apache.commons.io.IOUtils.toString(is,"UTF-8");
            }

            tempProtectionTraits = gson.fromJson(ITA.jsonProtectionTraits, typeOfProtectionTraits);
            for (ProtectionTrait trait : tempProtectionTraits) {
                ITA.Traits.put(trait.name, trait);
            }
            ITA.jsonProtectionTraits = gson.toJson(tempProtectionTraits, typeOfProtectionTraits);
            Files.write(Paths.get(protectionTraitFile.getPath()), ITA.jsonProtectionTraits.getBytes());
            ITA.jsonProtectionTraits = gsonCompact.toJson(tempProtectionTraits, typeOfProtectionTraits);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadConfigs() {
        if (!configDir.exists()) {
            configDir.mkdir();
        }
        ITA.Traits = new HashMap<String, BaseTrait>();

        loadMaterialConfig(new File(configDir, "ArmorMaterials.json"));
        loadPotionTraits(new File(configDir, "PotionTraits.json"));
        loadProtectionTraits(new File(configDir, "ProtectionTraits.json"));

        File clientConfigFile = new File(configDir, "Client.cfg");
        File commonConfigFile = new File(configDir, "Common.cfg");

        clientConfig = new Configuration(clientConfigFile);
        commonConfig = new Configuration(commonConfigFile);
        clientConfig.load();
        commonConfig.load();


        ITA.alloyMultiplier = commonConfig.getFloat("Alloy Multiplier", "Balance",0.375F, 0.0F, 10.0F, "");
        ITA.debug = commonConfig.getBoolean("debug mode", "Debug", false, "");
        ITA.maxProtectionPreType = commonConfig.getFloat("Max Protection (Pre Type) Factor", "Balance", 50.0F, 0.0F, 100.0F, "");
        ITA.maxSpeedMultiplier = commonConfig.getFloat("Max Speed Multiplier", "Balance", 16F, 0F, 100F, "");
        ITA.craftingHammerRequired = commonConfig.getBoolean("Require Crafting Hammer","Balance", true, "Might cause recipe conflicts if disabled");
        ITA.syncConfigToPlayersOnLogin = commonConfig.getBoolean("Sync Server Config To Players On Login", "General", true, "Recommended to leave enable");

        ITA.shiftForToolTips = clientConfig.getBoolean("Shift For Tooltips", "Tooltips", false, "");
        ITA.materialToolTips = clientConfig.getBoolean("Material Tooltips", "Tooltips", true, "");
        ITA.basicArmorToolTips = clientConfig.getBoolean("Basic Armor Tooltips", "Tooltips", true, "");
        ITA.specialArmorToolTips = clientConfig.getBoolean("Special Armor Tooltips", "Tooltips", true, "");
        ITA.itaArmorToolTips = clientConfig.getBoolean("ITA Armor Tooltips", "Tooltips", true, "");

        commonConfig.save();
        clientConfig.save();
    }
}