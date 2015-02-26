package com.trcx.itaBasic.Common.Recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by Trcx on 2/25/2015.
 */
public class RecipeITAAarmor implements IRecipe {

    private ItemStack getOutput(InventoryCrafting inv, Boolean shouldCreate){
        ArrayList<Integer> slotsUsed = new ArrayList<Integer>();



        return null;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        return getOutput(inv,false) != null;
    }

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
