package com.trcx.ita.Common.Traits;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

import java.util.Random;

/**
 * Created by Trcx on 3/3/2015.
 */
public class PotionTrait extends BaseTrait {

    public int potionID;
    public int duration = 0;
    public int potency = 0;
    public int minWeightForAlwaysActive = -1;
    public int randActivationFrequency = -1;
    public String weightFrequencyImpact = "none";
    public String weightDurationImpact = "none";

    private static Random random = new Random();

    public void tick(double weight, int tickCount, EntityPlayer player) {
        if ((tickCount % 20) == 1) {
            Double calculatedDuration = duration * getCalculatedWeight(weight, weightDurationImpact);
            PotionEffect effect = new PotionEffect(potionID, calculatedDuration.intValue(), potency);
            if (weight >= minWeightForAlwaysActive) {
                player.addPotionEffect(effect);
            } else if (randActivationFrequency > 0) {
                double calcWeight = getCalculatedWeight(weight, weightFrequencyImpact);
                if (random.nextInt(3600) <= Math.floor(calcWeight * randActivationFrequency)){ // weight of 1 should trigger once every hour
                    player.addPotionEffect(effect);
                }
            }
        }
    }

    public PotionTrait(String name){
        super(name);
    }
}
