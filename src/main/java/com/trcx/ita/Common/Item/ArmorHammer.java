package com.trcx.ita.Common.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

/**
 * Created by Trcx on 2/25/2015.
 */
public class ArmorHammer extends Item {

    public ArmorHammer() {
        setMaxStackSize(1);
        setContainerItem(this); // player gets to keep this when crafting
        GameRegistry.addShapedRecipe(new ItemStack(this), " i ", " si", "s  ", 's', new ItemStack(Items.stick),
                'i', new ItemStack(Items.iron_ingot));
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack is) {
        return false;
    }
}
