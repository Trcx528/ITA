package com.trcx.ita.Common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Trcx on 3/14/2015.
 */
public class SwapperInventory implements IInventory {

    private ItemStack[] contents = new ItemStack[4];
    private ItemStack containerItem;

    private static final String stringNBTINVENTORY = "Inventory";



    public SwapperInventory(ItemStack invItem){
        super();
        if (invItem != null){
            containerItem = invItem;
            if (invItem.hasTagCompound()) {
                readFromNBT(invItem.getTagCompound());
            } else {
                containerItem.stackTagCompound = new NBTTagCompound();
            }
        }
    }

    public void invChanged(){
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        containerItem.stackTagCompound = tag;
    }


    public void readFromNBT(NBTTagCompound tag){
        contents = new ItemStack[4];
        if (tag.hasKey(stringNBTINVENTORY)) {
            NBTTagCompound invTag = (NBTTagCompound)tag.getTag(stringNBTINVENTORY);
            for(Object sSlot: invTag.func_150296_c()){
                Integer slot = Integer.valueOf((String)sSlot);
                contents[slot] = ItemStack.loadItemStackFromNBT(invTag.getCompoundTag((String)sSlot));
            }
        }
    }

    public void writeToNBT(NBTTagCompound tag){
        NBTTagCompound invTag = new NBTTagCompound();
        for (Integer i=0; i!=4;i++ ) {
            ItemStack is = contents[i];
            if (is != null){
                NBTTagCompound stackNBT = new NBTTagCompound();
                is.writeToNBT(stackNBT);
                invTag.setTag(i.toString(), stackNBT);
            }
        }
        tag.setTag(stringNBTINVENTORY,invTag);
    }

    @Override
    public int getSizeInventory() {
        return contents.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return contents[slot];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        if (contents[par1] != null) {
            ItemStack itemstack;

            if (contents[par1].stackSize <= par2)
            {
                itemstack = contents[par1];
                contents[par1] = null;
                invChanged();
                return itemstack;
            }
            itemstack = contents[par1].splitStack(par2);
            if (contents[par1].stackSize == 0)
            {
                contents[par1] = null;
            }

            invChanged();
            return itemstack;
        }
        invChanged();
        return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return contents[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack is) {
        contents[slot] = is;
    }

    @Override
    public String getInventoryName() {
        return "ITA Swapper";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {}  //according to open mods, this function is basically useless

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return true; //any item can go in any slot
    }
}
