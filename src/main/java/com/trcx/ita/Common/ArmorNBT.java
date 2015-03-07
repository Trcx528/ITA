package com.trcx.ita.Common;

import com.trcx.ita.CONSTS;
import com.trcx.ita.ITA;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

/**
 * Created by Trcx on 2/26/2015.
 */
public class ArmorNBT {

    private static final String stringNBTMATERIAL = "itaMaterials";
    private static final String stringNBTARMORTYPE = "itaArmorType";
    private static final String stringNBTARMORCOLOR = "itaArmorColor";
    private static final String stringNBTARMORINVISIBLE = "itaArmorInvisible";
    private static final List<String> arrayNBTALLOYNAME = Arrays.asList("itaAlloy1","itaAlloy2","itaAlloy3","itaAlloy4","itaAlloy5","itaAlloy6","itaAlloy7","itaAlloy8");


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
                for (int i = 0; i < 8; i++) {
                    NBTTagCompound TNBT = is.stackTagCompound.getCompoundTag(stringNBTMATERIAL);
                    if (TNBT.hasKey(arrayNBTALLOYNAME.get(i))) {
                        NBTTagCompound nbt = (NBTTagCompound) TNBT.getTag(arrayNBTALLOYNAME.get(i));
                        AlloyNBT anbt = new AlloyNBT(nbt);
                        materials.put(MaterialProperty.forAlloy(anbt), 1);
                    }
                }
            }
            if (is.stackTagCompound.hasKey(stringNBTARMORTYPE))
                armorType = is.stackTagCompound.getInteger(stringNBTARMORTYPE);

            if (is.stackTagCompound.hasKey(stringNBTARMORCOLOR))
                color = is.stackTagCompound.getInteger(stringNBTARMORCOLOR);

            if (is.stackTagCompound.hasKey(stringNBTARMORINVISIBLE))
                invisible = is.stackTagCompound.getBoolean(stringNBTARMORINVISIBLE);
            for (String s: arrayNBTALLOYNAME){
                if (is.stackTagCompound.hasKey(s)){
                    MaterialProperty mat = MaterialProperty.forAlloy(new AlloyNBT(is.stackTagCompound.getCompoundTag(s)));
                    if (materials.containsKey(mat)){
                        materials.put(mat,materials.get(mat) + 1);
                    } else {
                        materials.put(mat, 1);
                    }
                }
            }
        }
    }

    public ArmorNBT(){}

    public static void writeToNBT(ArmorNBT anbt, ItemStack is){
        NBTTagCompound nbt = new NBTTagCompound();
        int i = 0;
        for (MaterialProperty mat: anbt.materials.keySet()){
            if (mat.oreDictionaryName.equals(CONSTS.stringMaterialAlloyName)) {
                nbt.setTag(arrayNBTALLOYNAME.get(i),mat.alloyNBT);
                i++;
            } else {
                nbt.setInteger(mat.oreDictionaryName, anbt.materials.get(mat));
            }
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
