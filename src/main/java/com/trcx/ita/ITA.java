package com.trcx.ita;

import com.trcx.ita.Common.MaterialProperty;
import com.trcx.ita.Common.Traits.BaseTrait;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Trcx on 2/25/2015.
 */


public class ITA {


    public static Map<String, MaterialProperty> Materials = new HashMap<String, MaterialProperty>();
    public static Map<String, BaseTrait> Traits = new HashMap<String, BaseTrait>();
    public static Float lastSpeedModifier = -9999F; // set to a weird value so that it'll refresh
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
}
