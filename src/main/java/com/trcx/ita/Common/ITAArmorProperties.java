package com.trcx.ita.Common;

import com.trcx.ita.Common.Traits.BaseTrait;
import com.trcx.ita.ITA;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

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
    public int maxDurability = 0;
    public boolean isInvisible = false;
    public int armorType = 5;

    public Map<BaseTrait, Integer> traits = new HashMap<BaseTrait, Integer>();

    private int nbtColor;
    private Map<String, Integer> ColorMap = new HashMap<String, Integer>();
    private ItemStack armorStack;

    public ITAArmorProperties(ItemStack is) {
        armorStack = is;
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
            for (String traitName : mat.traits.keySet()){
                if (ITA.Traits.containsKey(traitName)){
                    BaseTrait trait = ITA.Traits.get(traitName);
                    if (traits.containsKey(trait)){
                        traits.put(trait,traits.get(trait) + (mat.traits.get(traitName) * qty));
                    } else {
                        traits.put(trait, mat.traits.get(traitName) * qty);
                    }
                }
            }
        }

        this.armorType = anbt.armorType;

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

            isInvisible = anbt.invisible;
            nbtColor = anbt.color;
        }
    }

    public int getColor(){
        if (nbtColor != 0)
            return nbtColor;
        return ColorHelper.getAvgColorInt(ColorMap);
    }

    public void getToolTip(List<String> data){
        if (!ITA.debug) {
            data.add(EnumChatFormatting.BLUE + "Protection: " + this.armorDisplayValue);
        } else {
            data.add(EnumChatFormatting.BLUE + "Armor Display: " + this.armorDisplayValue);
            data.add(EnumChatFormatting.BLUE + "Protection %: " + this.armorProtectionValue);
        }
        data.add(EnumChatFormatting.AQUA + "Durability: " + (this.maxDurability - this.armorStack.getItemDamage()) + "/" + this.maxDurability);
        data.add(EnumChatFormatting.GREEN + "Enchantability: " + this.enchantability);

        data.add(EnumChatFormatting.LIGHT_PURPLE + "Speed: " + this.speedModifier * 100 + "%");

        if (isInvisible){
            data.add(EnumChatFormatting.GRAY + "Invisible");
        }
        if (traits.size() > 0){
            data.add(EnumChatFormatting.DARK_GRAY+ "Traits: ");
            for (BaseTrait trait : traits.keySet()){
                data.add(EnumChatFormatting.DARK_PURPLE + trait.name + ": " + traits.get(trait));
            }
        }
    }
}
