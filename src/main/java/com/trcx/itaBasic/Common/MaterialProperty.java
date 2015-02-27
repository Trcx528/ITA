package com.trcx.itaBasic.Common;

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
    public double weight;
    public int durability;
    public List<Integer> invalidTypes;

    public Map<String, Integer> traits = new HashMap<String, Integer>();

    public MaterialProperty(){}

    public MaterialProperty(String friendlyName, String hexColor, double protection, int enchantability, double weight, int durability) {
        this.friendlyName = friendlyName;
        this.hexColor = hexColor;
        this.protection = protection;
        this.enchantability = enchantability;
        this.weight = weight;
        this.durability = durability;
    }

    public void onLoadFromFileUpdate(String oreDictionaryName){
        this.oreDictionaryName = oreDictionaryName;
        this.invalidTypes = new ArrayList<Integer>();
    }

    public void setFriendlyNameFromOreDictionaryName (){
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
