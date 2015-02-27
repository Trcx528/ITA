package com.trcx.itaBasic.Common.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by Trcx on 2/25/2015.
 */
public class ArmorHammer extends Item {

    public ArmorHammer(){
        setMaxStackSize(1);
        setTextureName("ITA:ArmorFormer");
        setContainerItem(this); // player gets to keep this when crafting
        GameRegistry.addShapedRecipe(new ItemStack(this), " i ", " si", "s  ", 's', new ItemStack(Items.stick),
                'i', new ItemStack(Items.iron_ingot));
    }
}
