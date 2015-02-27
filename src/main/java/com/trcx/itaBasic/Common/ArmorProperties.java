package com.trcx.itaBasic.Common;

import net.minecraft.item.ItemStack;

import java.awt.*;

/**
 * Created by Trcx on 2/26/2015.
 */
public class ArmorProperties {
    public int armorDisplayValue;
    public double armorProtectionValue;
    public double speedModifier;
    public int enchantability;
    public String hexColor;
    public Color color;
    public int durability;

    public ArmorProperties (ItemStack is){
        ArmorNBT anbt = new ArmorNBT(is);
        Integer totalQty = 0;
        for (MaterialProperty mat: anbt.materials.keySet()){
            Integer qty = anbt.materials.get(mat);
            totalQty += qty;
            armorProtectionValue = mat.protection * qty;
            speedModifier = mat.
        }
    }


}
