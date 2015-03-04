package com.trcx.ita.Common.Traits;

import net.minecraft.util.DamageSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Trcx on 3/3/2015.
 */
public class ProtectionTrait extends BaseTrait {

    public String damageSourceType;
    public Integer protectionPerWeight;
    public String weightImpact;

    private static Map<String, DamageSource> damageSourceMap = new HashMap<String, DamageSource>();

    public ProtectionTrait(String name){
        super(name);
    }


    public int getDamageReductionPercent(double initialWeight){
        Double retVal = getCalculatedWeight(initialWeight, weightImpact);
        return protectionPerWeight * retVal.intValue();
    }

    public DamageSource getDamageSourceType(){
        if (damageSourceMap.size() == 0){
            damageSourceMap.put("anvil", DamageSource.anvil);
            damageSourceMap.put("cactus", DamageSource.cactus);
            damageSourceMap.put("drown", DamageSource.drown);
            damageSourceMap.put("fall", DamageSource.fall);
            damageSourceMap.put("fallingBlock", DamageSource.fallingBlock);
            damageSourceMap.put("generic", DamageSource.generic);
            damageSourceMap.put("inFire", DamageSource.inFire);
            damageSourceMap.put("inWall", DamageSource.inWall);
            damageSourceMap.put("lava", DamageSource.lava);
            damageSourceMap.put("magic", DamageSource.magic);
            damageSourceMap.put("onFire", DamageSource.onFire);
            damageSourceMap.put("outOfWorld", DamageSource.outOfWorld);
            damageSourceMap.put("starve", DamageSource.starve);
            damageSourceMap.put("wither", DamageSource.wither);
        }
        if (damageSourceMap.containsKey(damageSourceType))
            return damageSourceMap.get(damageSourceType);
        return null;
    }
}
