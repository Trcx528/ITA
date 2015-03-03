package com.trcx.ita.Common;

import com.trcx.ita.ITABasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Trcx on 2/26/2015.
 */
public class ITAArmorProperties {
    public int armorDisplayValue;
    public double armorProtectionValue = 0;
    public double speedModifier = 0;
    public int enchantability = 0;
    public Color color;
    public int maxDurability = 0;
    private Map<String, Integer> ColorMap = new HashMap<String, Integer>();

    public ITAArmorProperties(ItemStack is) {
        ArmorNBT anbt = new ArmorNBT(is);
        Integer totalQty = 0;
        for (MaterialProperty mat : anbt.materials.keySet()) {
            Integer qty = anbt.materials.get(mat);
            totalQty += qty;
            armorProtectionValue += mat.protection * qty;
            speedModifier += mat.speed * qty;
            enchantability += mat.enchantability * qty;
            maxDurability += mat.durability * qty;
            ColorMap.put(mat.hexColor, qty);
        }

        if (totalQty != 0) {
            double typeArmorFactor = 0;
            double typeDurabilityFactor = 0;
            switch (anbt.armorType) {
                case (0): //helmet
                    typeArmorFactor = 0.758;
                    typeDurabilityFactor = 1.6;
                    break;
                case (1): //chestplate
                    typeArmorFactor = 1.315;
                    typeDurabilityFactor = 1.1;
                    break;
                case (2): //leggings
                    typeArmorFactor = 0.919;
                    typeDurabilityFactor = 1.0;
                    break;
                case (3): //boots
                    typeArmorFactor = 0.811;
                    typeDurabilityFactor = 1.1;
                    break;
            }

            armorProtectionValue = typeArmorFactor * armorProtectionValue;
            armorDisplayValue = Math.round((float) armorProtectionValue / 4);
            maxDurability *= typeDurabilityFactor;
            enchantability /= totalQty;
            speedModifier /= totalQty;
            color = ColorHelper.getAvgColor(ColorMap);
        }
    }

    public void getToolTip(List<String> data){
        if (!ITABasic.debug) {
            data.add(EnumChatFormatting.BLUE + "Protection: " + this.armorDisplayValue);
        } else {
            data.add(EnumChatFormatting.BLUE + "Armor Display: " + this.armorDisplayValue);
            data.add(EnumChatFormatting.BLUE + "Protection %: " + this.armorProtectionValue);
        }
        data.add(EnumChatFormatting.AQUA + "Max Durability: " + this.maxDurability);
        data.add(EnumChatFormatting.GREEN + "Enchantability: " + this.enchantability);
        data.add(EnumChatFormatting.LIGHT_PURPLE + "Weight: " + ((this.speedModifier * -1) + 1));
    }
}