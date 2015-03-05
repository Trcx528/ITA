package com.trcx.ita.Common;

import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Trcx on 2/24/2015.
 */
public class MaterialProperty {
    public String oreDictionaryName;
    public String friendlyName;
    public String hexColor;
    public double protection;
    public int enchantability;
    //private double weight;
    public double speed;
    public int durability;
    public String comment;
    public List<Integer> invalidTypes = new ArrayList<Integer>();

    public Map<String, Integer> traits = new HashMap<String, Integer>();

    public MaterialProperty(){}

    public void getToolTip(List<String> data){
        data.add(EnumChatFormatting.BLUE + "Armor: " + this.protection);
        data.add(EnumChatFormatting.AQUA + "Durability " + this.durability);
        data.add(EnumChatFormatting.GREEN + "Enchantability: " + this.enchantability);
        if (this.speed > 1 ) {
            data.add(EnumChatFormatting.LIGHT_PURPLE + "Speed: " + (this.speed + 1) * 100 + "%");
        } else if (this.speed < 1){
            data.add(EnumChatFormatting.LIGHT_PURPLE + "Speed: " + (1 + this.speed) * 100 + "%");
        } else {
            data.add(EnumChatFormatting.LIGHT_PURPLE + "Speed: 100%");
        }
        if (traits.keySet().size() > 0) {
            data.add(EnumChatFormatting.DARK_AQUA + "Traits: ");
            for (String trait : this.traits.keySet()) {
                data.add(trait + " " + traits.get(trait).toString());
            }
        }
    }

    public void onLoadFromFileUpdate(){
        if (this.friendlyName == null) {
            String[] chunks = this.oreDictionaryName.split("(?=[A-Z])");
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
