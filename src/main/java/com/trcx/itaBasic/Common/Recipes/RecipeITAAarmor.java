package com.trcx.itaBasic.Common.Recipes;

import com.trcx.itaBasic.Common.ArmorNBT;
import com.trcx.itaBasic.Common.CONSTS;
import com.trcx.itaBasic.Common.Item.ArmorHammer;
import com.trcx.itaBasic.Common.MaterialProperty;
import com.trcx.itaBasic.ITABasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Trcx on 2/25/2015.
 */
public class RecipeITAAarmor implements IRecipe {

    private ItemStack getOutput(InventoryCrafting inv, Boolean shouldCreate){
        ArrayList<Integer> slotsUsed = new ArrayList<Integer>();
        int hammerCount = 0;
        boolean[] materialSlots = new boolean[9];
        boolean t = true;
        boolean f = false;
        ArmorNBT anbt = new ArmorNBT();
        //Map<MaterialProperty, Integer> armorMaterials = new HashMap<MaterialProperty,Integer>();
        int armorType = 5; //bad armor value

        for (int i = 0; i < 9; i++) {
            if (inv.getStackInSlot(i) != null) {
                if (inv.getStackInSlot(i).getItem() == ITABasic.ArmorHammer) {
                    hammerCount++;
                } else {
                    int[] ids = OreDictionary.getOreIDs(inv.getStackInSlot(i));
                    for (int id : ids) {
                        String oreDictName = OreDictionary.getOreName(id);
                        if (ITABasic.Materials.containsKey(oreDictName)) {
                            materialSlots[i] = true;
                            MaterialProperty mat = ITABasic.Materials.get(oreDictName);
                            if (anbt.materials.containsKey(mat)) {
                                Integer qty = anbt.materials.get(mat);
                                anbt.materials.remove(mat);
                                anbt.materials.put(mat, qty + 1);
                            } else {
                                anbt.materials.put(mat, 1);
                            }
                        }
                    }
                }
            }
        }

        if (hammerCount != 1){
            return null;
        }

        if (isTheSame(materialSlots, new boolean[]{t, t, t, t, f, t, f, f, f}, 9) ||
                isTheSame(materialSlots, new boolean[]{f, f, f, t, t, t, t, f, t}, 9)){ //helmet
            armorType = CONSTS.typeHELMET;
        } else if (isTheSame(materialSlots, new boolean[]{t, f, t, t, t, t, t, t, t},9)){ // chestplate
            armorType = CONSTS.typeCHESTPLATE;
        } else if (isTheSame(materialSlots, new boolean[]{t,t,t,t,f,t,t,f,t},9)){ // leggings
            armorType = CONSTS.typeLEGGINGS;
        } else if (isTheSame(materialSlots,new boolean[]{t,f,t,t,f,t,f,f,f}, 9) ||
                isTheSame(materialSlots, new boolean[]{f,f,f,t,f,t,t,f,t},9)){ //boots
            armorType = CONSTS.typeBOOTS;
        }

        for (MaterialProperty mat: anbt.materials.keySet()){
            if (mat.invalidTypes.contains(armorType)){
                return null;
            }
        }

        if (armorType == 5){
            return null;
        }

        ItemStack returnArmor;
        if (armorType == CONSTS.typeHELMET){
            returnArmor = new ItemStack(ITABasic.Helmet);
        } else if (armorType == CONSTS.typeCHESTPLATE){
            returnArmor = new ItemStack(ITABasic.Chestplate);
        } else if (armorType == CONSTS.typeLEGGINGS){
            returnArmor = new ItemStack(ITABasic.Leggings);
        } else {
            returnArmor = new ItemStack(ITABasic.Boots);
        }

        ArmorNBT.writeToNBT(anbt, returnArmor);
        if (shouldCreate)
            returnArmor.stackSize = 1;

        return returnArmor.copy();
    }

    private static boolean isTheSame(boolean[] first, boolean[] second, int size){
        for (int i = 0; i < size; i++){
            if (first[i] != second[i]){
                return false;
            }
        }
        return true;
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
