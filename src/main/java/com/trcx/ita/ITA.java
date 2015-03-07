package com.trcx.ita;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.trcx.ita.Common.MaterialProperty;
import com.trcx.ita.Common.Traits.BaseTrait;
import com.trcx.ita.Common.Traits.PotionTrait;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;

import java.util.HashMap;
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
