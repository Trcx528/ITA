package com.trcx.ita;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trcx.ita.Common.MaterialProperty;
import com.trcx.ita.Common.Traits.BaseTrait;
import com.trcx.ita.Common.Traits.PotionTrait;
import com.trcx.ita.Common.Traits.ProtectionTrait;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Trcx on 2/25/2015.
 */


public class ITA {
    public static Map<String, MaterialProperty> Materials;
    public static Map<String, BaseTrait> Traits;
    public static Float lastSpeedModifier = -9999F; // set to a weird value so that it'll refresh
    public static Double fovCalculatorValue = 0D; // only used on client
    public static Double lastSpeedValue = 10000D;
    public static SimpleNetworkWrapper net;
    public static int nextPacketId = 0;

    //common config
    public static float alloyMultiplier;
    public static boolean debug;
    public static boolean craftingHammerRequired;
    public static float maxSpeedMultiplier;
    public static float maxProtectionPreType;
    public static boolean syncConfigToPlayersOnLogin;

    //client config
    public static boolean materialToolTips;
    public static boolean specialArmorToolTips;
    public static boolean basicArmorToolTips;
    public static boolean itaArmorToolTips;
    public static boolean shiftForToolTips;

    public static String jsonMaterial;
    public static String jsonPotionTraits;
    public static String jsonProtectionTraits;

    public static Item Helmet;
    public static Item Chestplate;
    public static Item Leggings;
    public static Item Boots;
    public static Item ArmorHammer;
    public static Item Alloy;

    public static void registerMessage(Class packet, Class message)
    {
        net.registerMessage(packet, message, nextPacketId, Side.CLIENT);
        net.registerMessage(packet, message, nextPacketId, Side.SERVER);
        nextPacketId++;
    }

    public static class config{
        public static File configDir;

        private static Configuration clientConfig;
        private static Configuration commonConfig;
        private static List<MaterialProperty> tempMaterials = new ArrayList<MaterialProperty>();
        private static List<PotionTrait> tempPotionTraits = new ArrayList<PotionTrait>();
        private static List<ProtectionTrait> tempProtectionTraits = new ArrayList<ProtectionTrait>();
        private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
        private static Gson gsonCompact = new GsonBuilder().create();

        private static Type typeOfMaterials = new TypeToken<List<MaterialProperty>>() {}.getType();
        private static Type typeOfPotionTraits = new TypeToken<List<PotionTrait>>() {}.getType();
        private static Type typeOfProtectionTraits = new TypeToken<List<ProtectionTrait>>() {}.getType();

        public static void loadFromCachedJSON(String id){
            try {
                if (id.equals(CONSTS.packetMaterialId)) {
                    tempMaterials = gson.fromJson(jsonMaterial, typeOfMaterials);
                    ITA.Materials = new HashMap<String, MaterialProperty>();
                    for (MaterialProperty prop : tempMaterials) {
                        prop.onLoadFromFileUpdate();
                        ITA.Materials.put(prop.identifier, prop);
                    }
                    ITA.Traits = new HashMap<String, BaseTrait>();
                } else if (id.equals(CONSTS.packetPotionTraitsId)) {
                    tempPotionTraits = gson.fromJson(jsonPotionTraits, typeOfPotionTraits);
                    for (PotionTrait trait : tempPotionTraits) {
                        ITA.Traits.put(trait.name, trait);
                    }
                } else if (id.equals(CONSTS.packetProtectionTraitsId)) {
                    tempProtectionTraits = gson.fromJson(jsonProtectionTraits, typeOfProtectionTraits);
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
                    jsonMaterial = new String(Files.readAllBytes(Paths.get(materialsFile.getPath())));
                    tempMaterials = gson.fromJson(jsonMaterial, typeOfMaterials);
                } else {
                    RegisterArmorMaterial(2.50, 9, 1.0, 36, "#D8D8D8", "ingotIron");
                    tempMaterials.get(tempMaterials.size() - 1).comment = "Vanilla values";
                    RegisterArmorMaterial(1.83, 25, 0.8, 17, "#EAEE57", "ingotGold");
                    tempMaterials.get(tempMaterials.size() - 1).comment = "Vanilla values (except for speed)";
                    RegisterArmorMaterial(3.33, 10, 1, 80, "#8CF4E2", "gemDiamond");
                    tempMaterials.get(tempMaterials.size() - 1).comment = "Vanilla values";

                    RegisterArmorMaterial(0, 0, 1.0, 5, "#7FCC19", "dyeLime");
                    tempMaterials.get(tempMaterials.size() - 1).comment = "For testing only";
                    tempMaterials.get(tempMaterials.size() - 1).traits.put("Night Vision", 1);
                    tempMaterials.get(tempMaterials.size() - 1).invalidTypes.add(CONSTS.typeBOOTS);
                    tempMaterials.get(tempMaterials.size() - 1).invalidTypes.add(CONSTS.typeCHESTPLATE);
                    tempMaterials.get(tempMaterials.size() - 1).invalidTypes.add(CONSTS.typeLEGGINGS);

                    RegisterArmorMaterial(0, 0, 1.0, 5, "#E5E533", "dyeYellow");
                    tempMaterials.get(tempMaterials.size() - 1).comment = "For testing only";
                    tempMaterials.get(tempMaterials.size() - 1).traits.put("Fall Protection", 1);
                    tempMaterials.get(tempMaterials.size() - 1).invalidTypes.add(CONSTS.typeHELMET);
                    tempMaterials.get(tempMaterials.size() - 1).invalidTypes.add(CONSTS.typeCHESTPLATE);
                    tempMaterials.get(tempMaterials.size() - 1).invalidTypes.add(CONSTS.typeLEGGINGS);

                    RegisterArmorMaterial(1.50, 30, 1.0, 10, "#E0D495", "ingotNickel");
                    RegisterArmorMaterial(2.00, 23, 0.9, 16, "#A5BCC3", "ingotSilver");
                    RegisterArmorMaterial(3.60, 3, 0.0, 5, "#ABABCF", "ingotLead");
                    RegisterArmorMaterial(2.70, 10, 1.0, 28, "#FFB262", "ingotCopper");
                    RegisterArmorMaterial(2.90, 10, 1.0, 23, "#CFD7D7", "ingotTin");
                    RegisterArmorMaterial(3.00, 11, 1.0, 30, "#FC5D2D", "ingotBronze");
                    RegisterArmorMaterial(3.80, 10, 1.0, 40, "#898989", "ingotSteel");
                    RegisterArmorMaterial(3.50, 18, 1.3, 9, "#FFFF0F", "ingotRefinedGlowstone");
                    RegisterArmorMaterial(4.10, 40, 1.0, 120, "#1E0059", "ingotRefinedObsidian");
                    RegisterArmorMaterial(4.10, 15, 0.0, 70, "#A97DE0", "ingotManyullyn"); //TODO undo weight nerf (testing)
                    RegisterArmorMaterial(4.00, 20, 1.8, 50, "#F48A00", "ingotArdite");
                    RegisterArmorMaterial(4.00, 25, 0.9, 60, "#2376DD", "ingotCobalt");
                    RegisterArmorMaterial(0.30, 3, 1.0, 5, "#CD8B7D", "ingotMeatRaw");
                    RegisterArmorMaterial(0.50, 5, 1.0, 8, "#6E3F23", "ingotMeat");
                    RegisterArmorMaterial(2.70, 30, 1.0, 23, "#F0D467", "ingotAluminiumBrass");
                    RegisterArmorMaterial(3.00, 10, 0.9, 35, "#E39BD3", "ingotAlumite");
                    RegisterArmorMaterial(2.40, 12, 1.0, 25, "#C5C5C5", "ingotAluminum");
                    RegisterArmorMaterial(3.50, 35, 1.0, 45, "#F0F589", "ingotElectrumFlux");
                    tempMaterials.get(tempMaterials.size() - 1).comment = "Balanced against redstone arsenal";
                    RegisterArmorMaterial(3.00, 12, 1.0, 40, "#FF9F1A", "ingotSignalum");
                    RegisterArmorMaterial(2.80, 14, 1.0, 35, "#DDE2E0", "ingotInvar");
                    RegisterArmorMaterial(4.50, 20, 0.9, 50, "#107272", "ingotEnderium"); // TODO add teleport?
                    RegisterArmorMaterial(0.90, 20, 2.0, 20, "#D5BF6D", "ingotLumium");
                    RegisterArmorMaterial(2.80, 35, 0.7, 15, "#FDF488", "ingotElectrum");
                    RegisterArmorMaterial(0.50, 5, 1.0, 80, "#8F60D4", "ingotObsidian");
                    RegisterArmorMaterial(2.00, 50, 0.9, 10, "#88D8FF", "ingotMithril");
                    RegisterArmorMaterial(2.50, 23, 1.0, 10, "#C7EEFF", "ingotPlatinum");
                    RegisterArmorMaterial(2.60, 25, 1.0, 30, "#7A6AAE", "ingotThaumium");
                    RegisterArmorMaterial(1.30, 9, 1.05, 40, "#F0A8A4", "ingotPigIron");
                    RegisterArmorMaterial(4.00, 25, 1.0, 50, "#200D36", "ingotVoid");
                    RegisterArmorMaterial(0.00, 25, 1.0, 500, "#737372", "ingotUnstable");
                    tempMaterials.get(tempMaterials.size() - 1).comment = "the irony, unstable ingots form the most stable(durable) armor";
                    RegisterArmorMaterial(1.00, 5, 0.7, 21, "#D9DB5C", "ingotYellorium");
                    RegisterArmorMaterial(2.00, 7, 0.8, 18, "#4642D6", "ingotBlutonium");
                    RegisterArmorMaterial(0.70, 4, 0.6, 19, "#5CAFDB", "ingotCyanite");
                    RegisterArmorMaterial(2.30, 10, 1.0, 1, "#8095A9", "ingotOsmium");
                    RegisterArmorMaterial(3.20, 28, 0.7, 60, "#FACAFC", "ingotElvenElementium");
                    RegisterArmorMaterial(3.50, 25, 1.0, 70, "#90E764", "ingotTerrasteel"); // TODO Health Increase
                    RegisterArmorMaterial(2.70, 18, 1.0, 45, "#CAEAFD", "ingotManasteel");
                }

                ITA.Materials = new HashMap<String, MaterialProperty>();
                for (MaterialProperty prop : tempMaterials) {
                    prop.onLoadFromFileUpdate();
                    ITA.Materials.put(prop.identifier, prop);
                }

                jsonMaterial = gson.toJson(tempMaterials, typeOfMaterials);
                Files.write(Paths.get(materialsFile.getPath()), jsonMaterial.getBytes());
                jsonMaterial = gsonCompact.toJson(tempMaterials, typeOfMaterials);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private static void RegisterArmorMaterial(double protection,Integer enchantability,double speedModifier,Integer maxDurability,String hexColor,String oreDictName){
            MaterialProperty newMat = new MaterialProperty();
            newMat.protection = protection;
            newMat.enchantability = enchantability;
            newMat.speed = speedModifier;
            newMat.durability = maxDurability;
            newMat.hexColor = hexColor;
            newMat.identifier = oreDictName;
            newMat.friendlyName = null;
            tempMaterials.add(newMat);
        }
        private static void loadPotionTraits(File potionTraitFile){
            try{
                if (potionTraitFile.exists()) {
                    jsonPotionTraits = new String(Files.readAllBytes(Paths.get(potionTraitFile.getPath())));
                    tempPotionTraits = gson.fromJson(jsonPotionTraits, typeOfPotionTraits);
                } else {
                    PotionTrait trait = new PotionTrait("Night Vision");
                    trait.potionID = Potion.nightVision.id;
                    trait.weightDurationImpact = "*1";
                    trait.weightFrequencyImpact = "^2";
                    trait.randActivationFrequency = 5;
                    trait.duration = 600;
                    trait.minWeightForAlwaysActive = 5;
                    tempPotionTraits.add(trait);
                }

                for (PotionTrait trait : tempPotionTraits) {
                    ITA.Traits.put(trait.name, trait);
                }
                jsonPotionTraits = gson.toJson(tempPotionTraits, typeOfPotionTraits);
                Files.write(Paths.get(potionTraitFile.getPath()), jsonPotionTraits.getBytes());
                jsonPotionTraits = gsonCompact.toJson(tempPotionTraits, typeOfPotionTraits);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        private static void loadProtectionTraits(File protectionTraitFile){
            try {
                if (protectionTraitFile.exists()) {
                    jsonProtectionTraits = new String(Files.readAllBytes(Paths.get(protectionTraitFile.getPath())));
                    tempProtectionTraits = gson.fromJson(jsonProtectionTraits, typeOfProtectionTraits);
                } else {
                    ProtectionTrait sampleProtection = new ProtectionTrait("Fall Protection");
                    sampleProtection.damageSourceType = "fall";
                    sampleProtection.protectionPerWeight = 5;
                    sampleProtection.weightImpact = "^2";
                    tempProtectionTraits.add(sampleProtection);
                }

                for (ProtectionTrait trait : tempProtectionTraits) {
                    ITA.Traits.put(trait.name, trait);
                }
                jsonProtectionTraits = gson.toJson(tempProtectionTraits, typeOfProtectionTraits);
                Files.write(Paths.get(protectionTraitFile.getPath()), jsonProtectionTraits.getBytes());
                jsonProtectionTraits = gsonCompact.toJson(tempProtectionTraits, typeOfProtectionTraits);
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
}
