package com.trcx.ita.Common;

import com.trcx.ita.CONSTS;
import com.trcx.ita.ITA;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Trcx on 2/24/2015.
 */
public class MaterialProperty {
    public String identifier;
    public String friendlyName;
    public String hexColor;
    public double protection = 0D;
    public int enchantability = 0;
    //private double weight;
    public double speed = 0;
    public int durability = 0;
    public String comment;
    public NBTTagCompound alloyNBT;
    public List<Integer> invalidTypes = new ArrayList<Integer>();

    public Map<String, Integer> traits = new HashMap<String, Integer>();

    public MaterialProperty(){}

    public static MaterialProperty forAlloy(AlloyNBT aNBT) {
        MaterialProperty retMat = new MaterialProperty();
        retMat.alloyNBT = aNBT.getNBT();
        retMat.friendlyName = "Alloy";
        retMat.identifier = CONSTS.stringMaterialAlloyName;
        Map<String, Integer> colorMap = new HashMap<String, Integer>();
        for (MaterialProperty mat : aNBT.materials.keySet()) {
            colorMap.put(mat.hexColor, aNBT.materials.get(mat));
            retMat.protection += mat.protection * aNBT.materials.get(mat) * ITA.alloyMultiplier;
            retMat.enchantability += mat.enchantability * aNBT.materials.get(mat);
            retMat.speed += mat.speed * aNBT.materials.get(mat);
            retMat.durability += mat.durability * aNBT.materials.get(mat) * ITA.alloyMultiplier;
            for (String trait : mat.traits.keySet()) {
                if (retMat.traits.containsKey(trait)) {
                    retMat.traits.put(trait, retMat.traits.get(trait) + (mat.traits.get(trait) / 4));
                } else {
                    retMat.traits.put(trait, mat.traits.get(trait) / 4);
                }
            }
        }
        retMat.hexColor = ColorHelper.hexFromInt(ColorHelper.getAvgColorInt(colorMap));
        retMat.enchantability /= 4;
        retMat.speed /= 4;
        return retMat;
    }

    public void getToolTip(List<String> data){
        data.add(EnumChatFormatting.BLUE + "Armor: " + this.protection);
        data.add(EnumChatFormatting.AQUA + "Durability " + this.durability);
        data.add(EnumChatFormatting.GREEN + "Enchantability: " + this.enchantability);
        if (ITA.debug)
            data.add("Speed Modifier: " + speed);
        data.add(EnumChatFormatting.LIGHT_PURPLE + "Speed: " + (this.speed) * 100 + "%");
        if (traits.keySet().size() > 0) {
            data.add(EnumChatFormatting.DARK_AQUA + "Traits: ");
            for (String trait : this.traits.keySet()) {
                data.add(" - " + trait + " " + traits.get(trait).toString());
            }
        }
    }

    public void onLoadFromFileUpdate(){
        if (this.friendlyName == null) {
            String[] chunks = this.identifier.split("(?=[A-Z])");
            for (int i = 0; i < chunks.length; i++) {
                if (i != 0) {
                    if (i == 1) {
                        this.friendlyName = chunks[i];
                    } else {
                        this.friendlyName = this.friendlyName + " " + chunks[i];
                    }
                }
            }
        }
    }
}
