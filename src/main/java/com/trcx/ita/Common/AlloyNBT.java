package com.trcx.ita.Common;

import com.trcx.ita.ITA;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Trcx on 3/6/2015.
 */
public class AlloyNBT {
    public Map<MaterialProperty, Integer> materials = new HashMap<MaterialProperty, Integer>();

    private static final String stringNBTALLOYMATERIALS = "itaMaterials";

    public AlloyNBT (NBTTagCompound nbt){
        if (nbt.hasKey(stringNBTALLOYMATERIALS)){
            Set<String> keys = nbt.getCompoundTag(stringNBTALLOYMATERIALS).func_150296_c();
            for (String key: keys){
                if (ITA.Materials.containsKey(key)){
                    if (this.materials.containsKey(ITA.Materials.get(key))){
                        this.materials.put(ITA.Materials.get(key), this.materials.get(key) + nbt.getCompoundTag(stringNBTALLOYMATERIALS).getInteger(key));
                    } else {
                        this.materials.put(ITA.Materials.get(key), nbt.getCompoundTag(stringNBTALLOYMATERIALS).getInteger(key));
                    }
                }
            }
        }
    }


    public static AlloyNBT fromStack(ItemStack is){
        if (is.hasTagCompound()){
            return new AlloyNBT(is.stackTagCompound);
        }
        return null;
    }

    public AlloyNBT(){}

    public static void writeToNBT(AlloyNBT anbt, ItemStack is){
        if (!is.hasTagCompound())
            is.stackTagCompound = new NBTTagCompound();
        is.stackTagCompound = anbt.getNBT();
    }

    public NBTTagCompound getNBT(){
        NBTTagCompound retVal = new NBTTagCompound();
        for (MaterialProperty mat: this.materials.keySet()){
            retVal.setInteger(mat.identifier, this.materials.get(mat));
        }
        NBTTagCompound rev = new NBTTagCompound();
        rev.setTag(stringNBTALLOYMATERIALS, retVal);
        return rev;
    }
}
