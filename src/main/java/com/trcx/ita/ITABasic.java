package com.trcx.ita;

import com.trcx.ita.Common.MaterialProperty;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Trcx on 2/25/2015.
 */


public class ITABasic {
    public static Map<String, MaterialProperty> Materials = new HashMap<String, MaterialProperty>();

    public static boolean debug;

    public static boolean materialToolTips;
    public static boolean specialArmorToolTips;
    public static boolean basicArmorToolTips;
    public static boolean itaArmorToolTips;
    public static boolean shiftForToolTips;

    public static Item Helmet;
    public static Item Chestplate;
    public static Item Leggings;
    public static Item Boots;
    public static Item ArmorHammer;

}
