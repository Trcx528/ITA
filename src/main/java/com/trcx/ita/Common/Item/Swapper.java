package com.trcx.ita.Common.Item;

import com.trcx.ita.Main;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by Trcx on 3/14/2015.
 */
public class Swapper extends Item {

    public Swapper(){
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            if (!world.isRemote){
                player.openGui(Main.instance,0,world,0,0,0);
                //player.openGui();
            }
            //open gui to change tools
        } else {
            //use the rightClickTool
        }
        return is;
    }
}
