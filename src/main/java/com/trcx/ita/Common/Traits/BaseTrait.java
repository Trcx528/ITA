package com.trcx.ita.Common.Traits;

import java.util.Random;

/**
 * Created by Trcx on 3/3/2015.
 */
public abstract class BaseTrait {
    public String name;

    public BaseTrait(String name){
        this.name = name;
    }

    protected static double getCalculatedWeight(double initalWeight, String impactType){
        if (impactType.equals("none"))
            return initalWeight;
        Double num = Double.parseDouble(impactType.substring(1));
        if (impactType.startsWith("*"))
            return initalWeight * num;
        if (impactType.startsWith("^")){
            return Math.pow(initalWeight, num);
        }
        return 0;
    }
}
