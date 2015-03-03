package com.trcx.ita.Common;

import com.trcx.ita.ITA;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Trcx on 2/26/2015.
 */
public class ArmorNBT {

    private static final String stringNBTMATERIAL = "itaMaterials";
    private static final String stringNBTARMORTYPE = "itaArmorType";
    private static final String stringNBTARMORCOLOR = "itaArmorColor";
    private static final String stringNBTARMORINVISIBLE = "itaArmorInvisible";

    public Map<MaterialProperty, Integer> materials = new HashMap<MaterialProperty, Integer>();
    public int armorType = 5;
    public int color = 0;
    public boolean invisible = false;

    public ArmorNBT(ItemStack is){
        if (is.hasTagCompound()) {
            if (is.stackTagCompound.hasKey(stringNBTMATERIAL)) {
                NBTTagCompound tag = is.stackTagCompound.getCompoundTag(stringNBTMATERIAL);
                Set<String> keys = tag.func_150296_c();
                for (String key : keys) {
                    if (ITA.Materials.containsKey(key)) {
                        materials.put(ITA.Materials.get(key), tag.getInteger(key));
                    }
                }
            }
            if (is.stackTagCompound.hasKey(stringNBTARMORTYPE))
                armorType = is.stackTagCompound.getInteger(stringNBTARMORTYPE);

            if (is.stackTagCompound.hasKey(stringNBTARMORCOLOR))
                color = is.stackTagCompound.getInteger(stringNBTARMORCOLOR);

            if (is.stackTagCompound.hasKey(stringNBTARMORINVISIBLE))
                invisible = is.stackTagCompound.getBoolean(stringNBTARMORINVISIBLE);
        }
    }

    public ArmorNBT(){}

    public static void writeToNBT(ArmorNBT anbt, ItemStack is){
        NBTTagCompound nbt = new NBTTagCompound();
        for (MaterialProperty mat: anbt.materials.keySet()){
            nbt.setInteger(mat.oreDictionaryName, anbt.materials.get(mat));
        }
        if (!is.hasTagCompound()){
            is.stackTagCompound = new NBTTagCompound();
        }
        is.stackTagCompound.setTag(stringNBTMATERIAL, nbt);
        is.stackTagCompound.setInteger(stringNBTARMORTYPE, anbt.armorType);
        if (anbt.invisible)
            is.stackTagCompound.setBoolean(stringNBTARMORINVISIBLE, true);
        if (anbt.color != 0)
            is.stackTagCompound.setInteger(stringNBTARMORCOLOR, anbt.color);
    }
}
