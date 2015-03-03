package com.trcx.ita.Common.Traits;

import net.minecraft.util.DamageSource;

/**
 * Created by Trcx on 3/3/2015.
 */
public class ProtectionTrait extends BaseTrait {

    String DamageSource;
    Integer protectionPerWeight;
    String weightImpact;

    public ProtectionTrait(String name){
        super(name);
    }
}
