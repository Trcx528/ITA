package com.trcx.ita.Common.Recipes;

import com.trcx.ita.CONSTS;
import com.trcx.ita.Common.AlloyNBT;
import com.trcx.ita.Common.ArmorNBT;
import com.trcx.ita.Common.MaterialProperty;
import com.trcx.ita.ITA;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by Trcx on 2/25/2015.
 */
public class RecipeITAAarmor implements IRecipe {

    private ItemStack getOutput(InventoryCrafting inv, Boolean shouldCreate){
        int hammerCount = ITA.craftingHammerRequired ? 0 : 1;
        boolean[] materialSlots = new boolean[9];
        boolean t = true;
        boolean f = false;
        ArmorNBT anbt = new ArmorNBT();
        int armorType = 5; //bad armor value

        for (int i = 0; i < 9; i++) {
            if (inv.getStackInSlot(i) != null) {
                if (inv.getStackInSlot(i).getItem() == ITA.ArmorHammer) {
                    hammerCount++;
                } else {
                    int[] ids = OreDictionary.getOreIDs(inv.getStackInSlot(i));
                    for (int id : ids) {
                        String oreDictName = OreDictionary.getOreName(id);
                        if (ITA.Materials.containsKey(oreDictName)) {
                            materialSlots[i] = true;
                            MaterialProperty mat = ITA.Materials.get(oreDictName);
                            if (anbt.materials.containsKey(mat)) {
                                Integer qty = anbt.materials.get(mat);
                                anbt.materials.remove(mat);
                                anbt.materials.put(mat, qty + 1);
                            } else {
                                anbt.materials.put(mat, 1);
                            }
                        }
                    }
                    GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(inv.getStackInSlot(i).getItem());
                    if (uid != null) {
                        if (ITA.Materials.containsKey(uid.modId + ":" + uid.name)) {
                            MaterialProperty mat = ITA.Materials.get(uid.modId + ":" + uid.name);
                            materialSlots[i] = true;
                            if (anbt.materials.containsKey(mat)) {
                                anbt.materials.put(mat, anbt.materials.get(mat) + 1);
                            } else {
                                anbt.materials.put(mat, 1);
                            }
                        } else if (ITA.Materials.containsKey(uid.modId + ":" + uid.name + inv.getStackInSlot(i).getItemDamage())) {
                            MaterialProperty mat = ITA.Materials.get(uid.modId + ":" + uid.name + inv.getStackInSlot(i).getItemDamage());
                            materialSlots[i] = true;
                            if (anbt.materials.containsKey(mat)) {
                                anbt.materials.put(mat, anbt.materials.get(mat) + 1);
                            } else {
                                anbt.materials.put(mat, 1);
                            }
                        }
                    }
                    if (inv.getStackInSlot(i).getItem() == ITA.Alloy) {
                        materialSlots[i] = true;
                        MaterialProperty alloy = MaterialProperty.forAlloy(AlloyNBT.fromStack(inv.getStackInSlot(i)));
                        if (anbt.materials.containsKey(alloy)) {
                            anbt.materials.put(alloy, anbt.materials.get(alloy) + 1);
                        } else {
                            anbt.materials.put(alloy, 1);
                        }
                    }
                }

            }
        }

        if (hammerCount != 1 && ITA.craftingHammerRequired){
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
            returnArmor = new ItemStack(ITA.Helmet);
        } else if (armorType == CONSTS.typeCHESTPLATE){
            returnArmor = new ItemStack(ITA.Chestplate);
        } else if (armorType == CONSTS.typeLEGGINGS){
            returnArmor = new ItemStack(ITA.Leggings);
        } else {
            returnArmor = new ItemStack(ITA.Boots);
        }

        anbt.armorType = armorType;
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
