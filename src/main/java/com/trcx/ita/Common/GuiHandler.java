package com.trcx.ita.Common;

import com.trcx.ita.Client.SwapperGui;
import com.trcx.ita.Common.Item.Swapper;
import com.trcx.ita.Common.OpenMods.PlayerItemInventory;
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
            return new SwapperContainer(player.inventory, new PlayerItemInventory(player, Swapper.swapperSlots));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() == ITA.Swapper){
            return new SwapperGui(player.inventory, new PlayerItemInventory(player,Swapper.swapperSlots));
        }
        return null;
    }
}
