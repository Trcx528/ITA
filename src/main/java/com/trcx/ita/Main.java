package com.trcx.ita;

/**
 * Created by Trcx on 2/24/2015.
 */

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trcx.ita.Common.*;
import com.trcx.ita.Common.CONSTS;
import com.trcx.ita.Common.Item.ArmorHammer;
import com.trcx.ita.Common.Item.ITAArmor;
import com.trcx.ita.Common.Recipes.RecipeArmorDye;
import com.trcx.ita.Common.Recipes.RecipeITAAarmor;
import com.trcx.ita.Common.Traits.BaseTrait;
import com.trcx.ita.Common.Traits.PotionTrait;
import com.trcx.ita.Common.Traits.ProtectionTrait;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import org.lwjgl.input.Keyboard;
import scala.Int;

import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Mod(modid = "ITA", version = Main.VERSION, name = "Infinitely Tweakable Armor")
public class Main
{

    public static final String VERSION = "0.0.1";
    public static Configuration clientConfig;

    private static File configDir;

    private static List<MaterialProperty> tempMaterials = new ArrayList<MaterialProperty>();
    private static List<PotionTrait> tempPotionTraits = new ArrayList<PotionTrait>();
    private static List<ProtectionTrait> tempProtectionTraits = new ArrayList<ProtectionTrait>();

    private static int tickCounter = 0;

    public Main(){
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public static void loadConfigs(){
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            if (!configDir.exists()) {
                configDir.mkdir();
            }


            File materialsFile = new File(configDir, "ArmorMaterials.json");
            File potionTraitFile = new File(configDir, "PotionTraits.json");
            File protectionTraitFile = new File(configDir, "ProtectionTraits.json");
            File clientConfigFile = new File(configDir, "Client.cfg");

            Type typeOfMaterials = new TypeToken<List<MaterialProperty>>() {}.getType();
            Type typeOfPotionTraits = new TypeToken<List<PotionTrait>>() {}.getType();
            Type typeOfProtectionTraits = new TypeToken<List<ProtectionTrait>>() {}.getType();

            //Type typeOfMaterials = new TypeToken<Map<String, MaterialProperty>>() { }.getType();
            if (materialsFile.exists()) {
                String json = new String(Files.readAllBytes(Paths.get(materialsFile.getPath())));
                tempMaterials = gson.fromJson(json, typeOfMaterials);
            } else {
                RegisterArmorMaterial(2.50, 9, 1.0, 30, "#D8D8D8", "ingotIron");
                tempMaterials.get(tempMaterials.size() - 1).comment = "Vanilla values";
                RegisterArmorMaterial(1.83, 25, 0.8, 14, "#EAEE57", "ingotGold");
                tempMaterials.get(tempMaterials.size() - 1).comment = "Vanilla values (except for speed)";
                RegisterArmorMaterial(3.33, 10, 0, 66, "#8CF4E2", "gemDiamond");
                tempMaterials.get(tempMaterials.size() - 1).comment = "Vanilla values";

                RegisterArmorMaterial(0, 0, 1.0, 5, "#7FCC19", "dyeLime");
                tempMaterials.get(tempMaterials.size() - 1).traits.put("Night Vision", 1);
                tempMaterials.get(tempMaterials.size() - 1).invalidTypes.add(CONSTS.typeBOOTS);
                tempMaterials.get(tempMaterials.size() - 1).invalidTypes.add(CONSTS.typeCHESTPLATE);
                tempMaterials.get(tempMaterials.size() - 1).invalidTypes.add(CONSTS.typeLEGGINGS);

                RegisterArmorMaterial(0, 0, 1.0, 5, "#E5E533", "dyeYellow");
                tempMaterials.get(tempMaterials.size() - 1).traits.put("Fall Protection", 1);
                tempMaterials.get(tempMaterials.size() - 1).invalidTypes.add(CONSTS.typeHELMET);
                tempMaterials.get(tempMaterials.size() - 1).invalidTypes.add(CONSTS.typeCHESTPLATE);
                tempMaterials.get(tempMaterials.size() - 1).invalidTypes.add(CONSTS.typeLEGGINGS);

                RegisterArmorMaterial(1.50, 30,1.0, 10, "#E0D495", "ingotNickel");
                RegisterArmorMaterial(2.00, 23,0.9, 16, "#A5BCC3", "ingotSilver");
                RegisterArmorMaterial(3.60,  3,0.0, 5, "#ABABCF", "ingotLead");
                RegisterArmorMaterial(2.70, 10,1.0, 28, "#FFB262", "ingotCopper");
                RegisterArmorMaterial(2.90, 10,1.0, 23, "#CFD7D7", "ingotTin");
                RegisterArmorMaterial(3.00, 11,1.0, 30, "#FC5D2D", "ingotBronze");
                RegisterArmorMaterial(3.80, 10,1.0, 40, "#898989", "ingotSteel");
                RegisterArmorMaterial(3.50, 18,1.3, 9, "#FFFF0F", "ingotRefinedGlowstone");
                RegisterArmorMaterial(4.10, 40,1.0, 50, "#1E0059", "ingotRefinedObsidian");
                RegisterArmorMaterial(4.10, 15,0.0, 70, "#A97DE0", "ingotManyullyn"); //TODO undo weight nerf (testing)
                RegisterArmorMaterial(4.00, 20,1.8, 50, "#F48A00", "ingotArdite");
                RegisterArmorMaterial(4.00, 25,0.9, 60, "#2376DD", "ingotCobalt");
                RegisterArmorMaterial(0.30,  3,1.0, 5, "#CD8B7D", "ingotMeatRaw");
                RegisterArmorMaterial(0.50,  5,1.0, 8, "#6E3F23", "ingotMeat");
                RegisterArmorMaterial(2.70, 30,1.0, 23, "#F0D467", "ingotAluminiumBrass");
                RegisterArmorMaterial(3.00, 10,0.9, 35, "#E39BD3", "ingotAlumite");
                RegisterArmorMaterial(2.40, 12,1.0, 25, "#C5C5C5", "ingotAluminum");
                RegisterArmorMaterial(3.50, 35,1.0, 45, "#F0F589", "ingotElectrumFlux");
                tempMaterials.get(tempMaterials.size() - 1).comment = "Balanced against redstone arsenal";
                RegisterArmorMaterial(3.00, 12,1.0, 40, "#FF9F1A", "ingotSignalum");
                RegisterArmorMaterial(2.80, 14,1.0, 35, "#DDE2E0", "ingotInvar");
                RegisterArmorMaterial(4.50, 20,0.9, 50, "#107272", "ingotEnderium"); // TODO add teleport?
                RegisterArmorMaterial(0.90, 20,2.0, 20, "#D5BF6D", "ingotLumium");
                RegisterArmorMaterial(2.80, 35,0.7, 15, "#FDF488", "ingotElectrum");
                RegisterArmorMaterial(0.50,  5,1.0, 80, "#8F60D4", "ingotObsidian");
                RegisterArmorMaterial(2.00, 50,0.9, 10, "#88D8FF", "ingotMithril");
                RegisterArmorMaterial(2.50, 23,1.0, 10, "#C7EEFF", "ingotPlatinum");
                RegisterArmorMaterial(2.60, 25,1.0, 30, "#7A6AAE", "ingotThaumium");
                RegisterArmorMaterial(1.30, 9,1.05, 40, "#F0A8A4", "ingotPigIron");
                RegisterArmorMaterial(4.00, 25,1.0, 50, "#200D36", "ingotVoid");
                RegisterArmorMaterial(1.00, 25,1.0, 500, "#737372", "ingotUnstable");
                tempMaterials.get(tempMaterials.size() - 1).comment = "the irony, unstable ingots form the most stable(durable) armor";
                RegisterArmorMaterial(1.00,  5,0.7, 21, "#D9DB5C", "ingotYellorium");
                RegisterArmorMaterial(2.00,  7,0.8, 18, "#4642D6", "ingotBlutonium");
                RegisterArmorMaterial(0.70,  4,0.6, 19, "#5CAFDB", "ingotCyanite");
                RegisterArmorMaterial(2.30, 10,1.0, 1, "#8095A9", "ingotOsmium");
                RegisterArmorMaterial(3.20, 28,0.7, 60, "#FACAFC", "ingotElvenElementium");
                RegisterArmorMaterial(3.50, 25,1.0, 70, "#90E764", "ingotTerrasteel"); // TODO Health Increase
                RegisterArmorMaterial(2.70, 18,1.0, 45, "#CAEAFD", "ingotManasteel");
            }

            ITA.Materials = new HashMap<String, MaterialProperty>();
            for (MaterialProperty prop : tempMaterials) {
                prop.onLoadFromFileUpdate();
                ITA.Materials.put(prop.oreDictionaryName, prop);
            }

            String json = gson.toJson(tempMaterials, typeOfMaterials);
            Files.write(Paths.get(materialsFile.getPath()), json.getBytes());

            if (potionTraitFile.exists()) {
                json = new String(Files.readAllBytes(Paths.get(potionTraitFile.getPath())));
                tempPotionTraits = gson.fromJson(json, typeOfPotionTraits);
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

            ITA.Traits = new HashMap<String, BaseTrait>();
            for (PotionTrait trait: tempPotionTraits){
                ITA.Traits.put(trait.name, trait);
            }
            json = gson.toJson(tempPotionTraits, typeOfPotionTraits);
            Files.write(Paths.get(potionTraitFile.getPath()), json.getBytes());

            if (protectionTraitFile.exists()) {
                json = new String(Files.readAllBytes(Paths.get(protectionTraitFile.getPath())));
                tempProtectionTraits = gson.fromJson(json, typeOfProtectionTraits);
            } else {
                ProtectionTrait sampleProtection = new ProtectionTrait("Fall Protection");
                sampleProtection.damageSourceType = "fall";
                sampleProtection.protectionPerWeight = 5;
                sampleProtection.weightImpact = "^2";
                tempProtectionTraits.add(sampleProtection);
            }

            for (ProtectionTrait trait: tempProtectionTraits){
                ITA.Traits.put(trait.name, trait);
            }
            json = gson.toJson(tempProtectionTraits, typeOfProtectionTraits);
            Files.write(Paths.get(protectionTraitFile.getPath()), json.getBytes());


            clientConfig = new Configuration(clientConfigFile);
            clientConfig.load();

            ITA.debug = clientConfig.getBoolean("debug mode", "Debug", false, "");

            ITA.shiftForToolTips = clientConfig.getBoolean("Shift For Tooltips", "Tooltips", false, "");
            ITA.materialToolTips = clientConfig.getBoolean("Material Tooltips", "Tooltips", true, "");
            ITA.basicArmorToolTips = clientConfig.getBoolean("Basic Armor Tooltips", "Tooltips", true, "");
            ITA.specialArmorToolTips = clientConfig.getBoolean("Special Armor Tooltips", "Tooltips", true, "");
            ITA.itaArmorToolTips = clientConfig.getBoolean("ITA Armor Tooltips", "Tooltips", true, "");

            clientConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException{
        configDir = new File(event.getModConfigurationDirectory(),"ITA");
        loadConfigs();

        ITA.Helmet = new ITAArmor(CONSTS.typeHELMET).setUnlocalizedName("ITAHelmet").setTextureName("ITA:Helmet");
        ITA.Chestplate = new ITAArmor(CONSTS.typeCHESTPLATE).setUnlocalizedName("ITAChestplate").setTextureName("ITA:Chestplate");
        ITA.Leggings = new ITAArmor(CONSTS.typeLEGGINGS).setUnlocalizedName("ITALeggings").setTextureName("ITA:Leggings");
        ITA.Boots = new ITAArmor(CONSTS.typeBOOTS).setUnlocalizedName("ITABoots").setTextureName("ITA:Boots");
        ITA.ArmorHammer = new ArmorHammer().setUnlocalizedName("ITAHammer").setTextureName("ITA:Hammer");

        GameRegistry.registerItem(ITA.Helmet,CONSTS.idHELMENT);
        GameRegistry.registerItem(ITA.Chestplate, CONSTS.idCHESTPLATE);
        GameRegistry.registerItem(ITA.Leggings, CONSTS.idLEGGINGS);
        GameRegistry.registerItem(ITA.Boots, CONSTS.idBOOTS);
        GameRegistry.registerItem(ITA.ArmorHammer, CONSTS.idARMORHAMMER);

        GameRegistry.addRecipe(new RecipeITAAarmor());
        GameRegistry.addRecipe(new RecipeArmorDye());
        RecipeSorter.register("ITA:armor",RecipeITAAarmor.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
        RecipeSorter.register("ITA:armorDye",RecipeArmorDye.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");

        new speedApplicator();

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void toolTipListener(ItemTooltipEvent event) {
        if (!ITA.shiftForToolTips || ((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)))) {
            if ((event.itemStack.getItem() == ITA.Helmet || event.itemStack.getItem() == ITA.Chestplate ||
                    event.itemStack.getItem() == ITA.Leggings || event.itemStack.getItem() == ITA.Boots) && ITA.itaArmorToolTips) {
                new ITAArmorProperties(event.itemStack).getToolTip(event.toolTip);
            } else if (event.itemStack.getItem() instanceof ISpecialArmor && ITA.specialArmorToolTips) {
                ISpecialArmor iarmor = (ISpecialArmor) event.itemStack.getItem();
                ItemArmor armor = (ItemArmor) event.itemStack.getItem();
                event.toolTip.add(EnumChatFormatting.BLUE + "Protection: " + iarmor.getArmorDisplay(event.entityPlayer, event.itemStack, 0));
                event.toolTip.add(EnumChatFormatting.AQUA + "Durability: "+ (event.itemStack.getMaxDamage() - event.itemStack.getItemDamage()) + "/" + event.itemStack.getMaxDamage());
                event.toolTip.add(EnumChatFormatting.GREEN + "Enchantability: " + armor.getItemEnchantability(event.itemStack));
            } else if (event.itemStack.getItem() instanceof ItemArmor && ITA.basicArmorToolTips) {
                ItemArmor armor = (ItemArmor) event.itemStack.getItem();
                event.toolTip.add(EnumChatFormatting.BLUE + "Protection: " + armor.getArmorMaterial().getDamageReductionAmount(armor.armorType));
                event.toolTip.add(EnumChatFormatting.AQUA + "Durability: " + (event.itemStack.getMaxDamage() - event.itemStack.getItemDamage()) + "/" + event.itemStack.getMaxDamage());
                event.toolTip.add(EnumChatFormatting.GREEN + "Enchantability: " + armor.getItemEnchantability(event.itemStack));
            } else {
                if (ITA.materialToolTips) {
                    for (int id : OreDictionary.getOreIDs(event.itemStack)) {
                        if (ITA.Materials.containsKey(OreDictionary.getOreName(id))) {
                            ITA.Materials.get(OreDictionary.getOreName(id)).getToolTip(event.toolTip);
                        }
                    }
                }
            }
        }
    }

    @Mod.EventHandler
    public void serverInit(FMLServerStartingEvent event){
        event.registerServerCommand(new ITACommand());
    }



    private static void RegisterArmorMaterial(double protection,Integer enchantability,double speedModifier,Integer maxDurability,String hexColor,String oreDictName){
        MaterialProperty newMat = new MaterialProperty();
        newMat.protection = protection;
        newMat.enchantability = enchantability;
        newMat.speed = speedModifier;
        newMat.durability = maxDurability;
        newMat.hexColor = hexColor;
        newMat.oreDictionaryName = oreDictName;
        newMat.friendlyName = null;
        tempMaterials.add(newMat);
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event){
        EntityPlayer player = event.player;
        Map<PotionTrait, Integer> traits = new HashMap<PotionTrait, Integer>();
        for(int i=0; i < 4; i++){
            ItemStack is = player.getCurrentArmor(i);
            if (is != null) {
                if (is.getItem() == ITA.Helmet || is.getItem() == ITA.Chestplate ||
                        is.getItem() == ITA.Leggings || is.getItem() == ITA.Boots) {
                    ITAArmorProperties props = new ITAArmorProperties(is);
                    for (BaseTrait trait : props.traits.keySet()) {
                        if (trait instanceof PotionTrait) {
                            if (traits.containsKey(trait)) {
                                traits.put((PotionTrait) trait, traits.get(trait) + props.traits.get(trait));
                            } else {
                                traits.put((PotionTrait) trait, props.traits.get(trait));
                            }
                        }
                    }
                }
            }
        }
        for (PotionTrait trait: traits.keySet()){
            trait.tick(traits.get(trait),tickCounter, player);
        }
    }

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event){
        if (event.phase == TickEvent.Phase.END) {
            if (tickCounter >= 9999){
                tickCounter = 0;
            } else {
                tickCounter ++;
            }
        }
    }
}
