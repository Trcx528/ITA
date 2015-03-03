package com.trcx.ita.Common.Recipes;

import com.trcx.ita.Common.ArmorNBT;
import com.trcx.ita.Common.ColorHelper;
import com.trcx.ita.ITA;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Trcx on 3/3/2015.
 */
public class RecipeArmorDye implements IRecipe {

    private static Map<String, String> dyeColorMap;

    public RecipeArmorDye(){
        dyeColorMap = new HashMap<String, String>();
        dyeColorMap.put("dyeBlack", "#191919");
        dyeColorMap.put("dyeRed", "#993333");
        dyeColorMap.put("dyeGreen", "#667F33");
        dyeColorMap.put("dyeBrown", "#664C33");
        dyeColorMap.put("dyeBlue", "#334CB2");
        dyeColorMap.put("dyePurple", "#7F3FB2");
        dyeColorMap.put("dyeCyan", "#4C7F99");
        dyeColorMap.put("dyeLightGray", "#999999");
        dyeColorMap.put("dyeGray", "#4C4C4C");
        dyeColorMap.put("dyePink", "#F27FA5");
        dyeColorMap.put("dyeLime", "#7FCC19");
        dyeColorMap.put("dyeYellow", "#E5E533");
        dyeColorMap.put("dyeLightBlue", "#6699D8");
        dyeColorMap.put("dyeMagenta", "#B24CD8");
        dyeColorMap.put("dyeOrange", "#D87F33");
        dyeColorMap.put("dyeWhite", "#FFFFFF");
    }

    private ItemStack getOutput(InventoryCrafting inv, boolean shouldCraft){
        Map <String, Integer> dyeMap = new HashMap<String, Integer>();
        int glassCount = 0;
        ItemStack armor = null;

        for (int i = 0; i < 9 ; i++) {
            ItemStack is = inv.getStackInSlot(i);
            if (is != null) {
                if (is.getItem() == ITA.Helmet || is.getItem() == ITA.Chestplate || is.getItem() == ITA.Leggings ||
                        is.getItem() == ITA.Boots) {
                    armor = is;
                } else {
                    int[] ids = OreDictionary.getOreIDs(is);
                    for (int id : ids) {
                        String oreName = OreDictionary.getOreName(id);
                        if (dyeColorMap.keySet().contains(oreName)) {
                            if (dyeMap.keySet().contains(dyeColorMap.get(oreName))) {
                                dyeMap.put(dyeColorMap.get(oreName), dyeMap.get(dyeColorMap.get(oreName)) +1);
                            } else {
                                dyeMap.put(dyeColorMap.get(oreName), 1);
                            }
                        } else if (oreName.equals("blockGlass")) {
                            glassCount++;
                        }
                    }
                }
            }
        }

        if (armor == null)
            return null;

        ArmorNBT anbt = new ArmorNBT(armor);

        ItemStack returnArmor = armor.copy();
        if (shouldCraft)
            returnArmor.stackSize = 1;

        if (glassCount > 0){
            if (glassCount == 8) {
                anbt.invisible = true;
                ArmorNBT.writeToNBT(anbt, returnArmor);
                return returnArmor;
            }
            return null;
        }

        if (dyeMap.size() > 0) {
            Integer newColor = ColorHelper.getAvgColorInt(dyeMap);
            anbt.color = newColor;
            anbt.invisible = false;
            ArmorNBT.writeToNBT(anbt, returnArmor);
            return returnArmor;
        } else {
            return null;
        }
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {return getOutput(inv,false) != null;}

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return getOutput(inv,true);
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}
