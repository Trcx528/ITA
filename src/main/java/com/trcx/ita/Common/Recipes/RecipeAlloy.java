package com.trcx.ita.Common.Recipes;

import com.trcx.ita.Common.AlloyNBT;
import com.trcx.ita.Common.MaterialProperty;
import com.trcx.ita.ITA;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Trcx on 3/6/2015.
 */
public class RecipeAlloy implements IRecipe {
    public ItemStack getOutput(InventoryCrafting inv, Boolean shouldCraft) {
        Map<MaterialProperty, Integer> materials = new HashMap<MaterialProperty, Integer>();
        for (int i=0; i <9; i ++) {
            ItemStack is = inv.getStackInSlot(i);
            if (is == null)
                return null;
            if (i == 0 || i ==2 || i ==6 || i == 8) {
                Boolean found = false;
                int[] ids = OreDictionary.getOreIDs(is);
                for (int id: ids) {
                    if(ITA.Materials.containsKey(OreDictionary.getOreName(id))) {
                        found = true;
                        MaterialProperty mat = ITA.Materials.get(OreDictionary.getOreName(id));
                        if (materials.containsKey(mat)) {
                            materials.put(mat, materials.get(mat) + 1);
                        } else {
                            materials.put(mat, 1);
                        }
                    }
                }

                GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(is.getItem());
                if (uid != null) {
                    if (ITA.Materials.containsKey(uid.modId + ":" + uid.name)) {
                        MaterialProperty mat = ITA.Materials.get(uid.modId + ":" + uid.name);
                        found = true;
                        if (materials.containsKey(mat)) {
                            materials.put(mat, materials.get(mat) + 1);
                        } else {
                            materials.put(mat, 1);
                        }
                    } else if (ITA.Materials.containsKey(uid.modId +":" + uid.name + is.getItemDamage())){
                        MaterialProperty mat = ITA.Materials.get(uid.modId + ":" + uid.name + is.getItemDamage());

                        found = true;
                        if (materials.containsKey(mat)) {
                            materials.put(mat, materials.get(mat) + 1);
                        } else {
                            materials.put(mat, 1);
                        }
                    }
                }
                if (!found)
                    return null;
            } else if (i == 1 || i == 7) {
                if (is.getItem() != Item.getItemFromBlock(Blocks.obsidian))
                    return null;
            } else if ( i == 3 || i == 5) {
                if (is.getItem() != Items.glowstone_dust)
                    return null;
            } else if (i == 4) {
                if (is.getItem() != Items.lava_bucket)
                    return null;
            } else {
                return null;
            }
        }

        ItemStack is = new ItemStack(ITA.Alloy);
        AlloyNBT anbt = new AlloyNBT();
        anbt.materials = materials;

        AlloyNBT.writeToNBT(anbt, is);

        if (shouldCraft)
            is.stackSize = 1;

        return  is;
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
