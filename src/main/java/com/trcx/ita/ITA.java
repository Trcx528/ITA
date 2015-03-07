package com.trcx.ita;

import com.trcx.ita.Common.MaterialProperty;
import com.trcx.ita.Common.Traits.BaseTrait;
import net.minecraft.item.Item;

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

    public static double alloyMultiplier = 0.375;
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
    public static Item Alloy;

}
