package com.trcx.ita.Common;

import com.trcx.ita.Client.SwapperGui;
import com.trcx.ita.ITA;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Trcx on 3/14/2015.
 */
public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() == ITA.Swapper){
            return new SwapperContainer(player.inventory, new SwapperInventory(player.getHeldItem()));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() == ITA.Swapper){
            return new SwapperGui(player.inventory, new SwapperInventory(player.getHeldItem()));
        }
        return null;
    }
}
