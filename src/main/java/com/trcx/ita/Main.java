package com.trcx.ita;

/**
 * Created by Trcx on 2/24/2015.
 */

import com.trcx.ita.Common.Network.*;
import com.trcx.ita.Common.*;
import com.trcx.ita.Common.Item.Alloy;
import com.trcx.ita.Common.Item.ArmorHammer;
import com.trcx.ita.Common.Item.ITAArmor;
import com.trcx.ita.Common.Recipes.RecipeAlloy;
import com.trcx.ita.Common.Recipes.RecipeArmorDye;
import com.trcx.ita.Common.Recipes.RecipeITAAarmor;
import com.trcx.ita.Common.Traits.BaseTrait;
import com.trcx.ita.Common.Traits.PotionTrait;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

@Mod(modid = "ITA", version = Main.VERSION, name = "Infinitely Tweakable Armor")
public class Main
{

    public static final String VERSION = "0.0.5";
    private static int tickCounter = 0;

    public Main(){
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }



    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException{
        File configDir = new File(event.getModConfigurationDirectory(),"ITA");
        ITA.config.configDir = configDir;
        ITA.config.loadConfigs();

        ITA.net = NetworkRegistry.INSTANCE.newSimpleChannel("ITA");
        ITA.registerMessage(jsonConfigPacket.class, jsonConfigPacket.jsonConfigMessage.class);
        ITA.registerMessage(commonConfigPacket.class, commonConfigPacket.commonConfigMessage.class);

        ITA.Helmet = new ITAArmor(CONSTS.typeHELMET).setUnlocalizedName("ITAHelmet").setTextureName("ITA:Helmet");
        ITA.Chestplate = new ITAArmor(CONSTS.typeCHESTPLATE).setUnlocalizedName("ITAChestplate").setTextureName("ITA:Chestplate");
        ITA.Leggings = new ITAArmor(CONSTS.typeLEGGINGS).setUnlocalizedName("ITALeggings").setTextureName("ITA:Leggings");
        ITA.Boots = new ITAArmor(CONSTS.typeBOOTS).setUnlocalizedName("ITABoots").setTextureName("ITA:Boots");
        ITA.ArmorHammer = new ArmorHammer().setUnlocalizedName("ITAHammer").setTextureName("ITA:Hammer");
        ITA.Alloy = new Alloy().setUnlocalizedName("ITAAlloy").setTextureName("ITA:Alloy");

        GameRegistry.registerItem(ITA.Helmet,CONSTS.idHELMENT);
        GameRegistry.registerItem(ITA.Chestplate, CONSTS.idCHESTPLATE);
        GameRegistry.registerItem(ITA.Leggings, CONSTS.idLEGGINGS);
        GameRegistry.registerItem(ITA.Boots, CONSTS.idBOOTS);
        GameRegistry.registerItem(ITA.ArmorHammer, CONSTS.idARMORHAMMER);
        GameRegistry.registerItem(ITA.Alloy, CONSTS.idALLOY);

        GameRegistry.addRecipe(new RecipeITAAarmor());
        GameRegistry.addRecipe(new RecipeArmorDye());
        GameRegistry.addRecipe(new RecipeAlloy());
        RecipeSorter.register("ITA:armor",RecipeITAAarmor.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
        RecipeSorter.register("ITA:armorDye",RecipeArmorDye.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shaped");
        RecipeSorter.register("ITA:alloy", RecipeAlloy.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");

        new speedApplicator();

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    }

    @SideOnly(Side.SERVER)
    @SubscribeEvent
    public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event){
        if (ITA.syncConfigToPlayersOnLogin) {
            jsonConfigPacket.jsonConfigMessage msg = new jsonConfigPacket.jsonConfigMessage(ITA.jsonMaterial, CONSTS.packetMaterialId);
            ITA.net.sendTo(msg, (EntityPlayerMP) event.player);
            msg = new jsonConfigPacket.jsonConfigMessage(ITA.jsonPotionTraits, CONSTS.packetPotionTraitsId);
            ITA.net.sendTo(msg, (EntityPlayerMP) event.player);
            msg = new jsonConfigPacket.jsonConfigMessage(ITA.jsonProtectionTraits, CONSTS.packetProtectionTraitsId);
            ITA.net.sendTo(msg, (EntityPlayerMP) event.player);
            ITA.net.sendTo(new commonConfigPacket.commonConfigMessage(), (EntityPlayerMP) event.player);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void toolTipListener(ItemTooltipEvent event) {
        if (!ITA.shiftForToolTips || ((Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)))) {
            DecimalFormat df = new DecimalFormat("#.#");
            if ((event.itemStack.getItem() == ITA.Helmet || event.itemStack.getItem() == ITA.Chestplate ||
                    event.itemStack.getItem() == ITA.Leggings || event.itemStack.getItem() == ITA.Boots) && ITA.itaArmorToolTips) {
                new ITAArmorProperties(event.itemStack).getToolTip(event.toolTip);
            } else if (event.itemStack.getItem() instanceof ISpecialArmor && ITA.specialArmorToolTips) {
                ISpecialArmor iArmor = (ISpecialArmor) event.itemStack.getItem();
                ItemArmor armor = (ItemArmor) event.itemStack.getItem();
                event.toolTip.add(EnumChatFormatting.BLUE + "Shields: " +  df.format((float)iArmor.getArmorDisplay(event.entityPlayer, event.itemStack, 0) / 2));
                event.toolTip.add(EnumChatFormatting.AQUA + "Durability: "+ (event.itemStack.getMaxDamage() - event.itemStack.getItemDamage()) + "/" + event.itemStack.getMaxDamage());
                event.toolTip.add(EnumChatFormatting.GREEN + "Enchantability: " + armor.getItemEnchantability(event.itemStack));
            } else if (event.itemStack.getItem() instanceof ItemArmor && ITA.basicArmorToolTips) {
                ItemArmor armor = (ItemArmor) event.itemStack.getItem();
                event.toolTip.add(EnumChatFormatting.BLUE + "Shields: " + df.format((float)armor.getArmorMaterial().getDamageReductionAmount(armor.armorType) /2));
                event.toolTip.add(EnumChatFormatting.AQUA + "Durability: " + (event.itemStack.getMaxDamage() - event.itemStack.getItemDamage()) + "/" + event.itemStack.getMaxDamage());
                event.toolTip.add(EnumChatFormatting.GREEN + "Enchantability: " + armor.getItemEnchantability(event.itemStack));
            } else {
                if (ITA.materialToolTips) {
                    for (int id : OreDictionary.getOreIDs(event.itemStack)) {
                        if (ITA.Materials.containsKey(OreDictionary.getOreName(id))) {
                            ITA.Materials.get(OreDictionary.getOreName(id)).getToolTip(event.toolTip);
                        }
                    }
                    GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(event.itemStack.getItem());
                    if (uid != null) {
                        if (ITA.Materials.containsKey(uid.modId + ":" + uid.name)) {
                            MaterialProperty mat = ITA.Materials.get(uid.modId + ":" + uid.name);
                            if (mat.metadata == null || mat.metadata == event.itemStack.getItemDamage()){
                                ITA.Materials.get(uid.modId + ":" + uid.name).getToolTip(event.toolTip);
                            }
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
