package com.trcx.itaBasic.Common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Trcx on 2/24/2015.
 */
public class MaterialProperty {
    public String oreDictionaryName;
    public String friendlyName;
    public String color;
    public double protection;
    public int enchantability;
    public double weight;
    public int durability;
    public Map<String, Integer> traits = new HashMap<String, Integer>();

    public MaterialProperty(){}

    public MaterialProperty(String friendlyName, String color, double protection, int enchantability, double weight, int durability) {
        this.friendlyName = friendlyName;
        this.color = color;
        this.protection = protection;
        this.enchantability = enchantability;
        this.weight = weight;
        this.durability = durability;
    }

    public void getFriendlyNameFromOreDictionaryName (String oreDictionaryName){

    }
}
