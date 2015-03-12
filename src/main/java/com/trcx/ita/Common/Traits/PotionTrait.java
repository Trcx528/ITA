package com.trcx.ita.Common.Traits;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.*;

/**
 * Created by Trcx on 3/3/2015.
 */
public class PotionTrait extends BaseTrait {

    public Integer potionID;
    public String potionName;
    public int duration = 0;
    public int potency = 0;
    public List<Integer> dimensionBlacklist = new ArrayList<Integer>();
    public int minWeightForAlwaysActive = -1;
    public int randActivationFrequency = -1;
    public String weightFrequencyImpact = "none";
    public String weightDurationImpact = "none";

    private static Map<String,Integer> potionIndex;
    private static Random random = new Random();

    public void tick(double weight, int tickCount, EntityPlayer player) {
        if ((tickCount % 20) == 1) {
            if (!dimensionBlacklist.contains(player.worldObj.provider.dimensionId)) {
                Double calculatedDuration = duration * getCalculatedWeight(weight, weightDurationImpact);
                PotionEffect effect = null;
                if (potionName != null && potionIndex.containsKey(potionName) && Potion.potionTypes[potionIndex.get(potionName)] != null) {
                    effect = new PotionEffect(potionIndex.get(potionName), calculatedDuration.intValue(), potency, true);
                } else if (potionID != null) {
                    regenPotionMappings();
                    effect = new PotionEffect(potionID, calculatedDuration.intValue(), potency, true);
                }
                if (effect != null) {
                    if (weight >= minWeightForAlwaysActive) {
                        player.addPotionEffect(effect);
                    } else if (randActivationFrequency > 0) {
                        double calcWeight = getCalculatedWeight(weight, weightFrequencyImpact);
                        if (random.nextInt(3600) <= Math.floor(calcWeight * randActivationFrequency)) { // weight of 1 should trigger once every hour
                            player.addPotionEffect(effect);
                        }
                    }
                }
            }
        }
    }

    private void regenPotionMappings(){
        potionIndex = new HashMap<String, Integer>();
        for (int i=0; i<Potion.potionTypes.length; i++) {
            if (Potion.potionTypes[i] != null) {
                Potion pe = Potion.potionTypes[i];
                potionIndex.put(pe.getName(), pe.getId());
            }
        }
    }

    public void updateTrait(){
        regenPotionMappings();
        dimensionBlacklist = new ArrayList<Integer>();
        if (potionID != null){
            if (potionIndex.containsValue(potionID)){
                for (String key: potionIndex.keySet()){
                    if (potionIndex.get(key).equals(potionID)){
                        potionName = key;
                        potionID = null;
                    }
                }
            }
        }
    }

    public PotionTrait(String name){
        super(name);
    }
}
