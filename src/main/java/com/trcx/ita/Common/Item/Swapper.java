package com.trcx.ita.Common.Item;

import com.google.common.collect.Multimap;
import com.trcx.ita.Common.OpenMods.ItemInventory;
import com.trcx.ita.Main;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Trcx on 3/14/2015.
 */
public class Swapper extends Item {

    private static final String stringLASTTOOL = "LastTool";

    private static final int slotSHOVEL = 0;
    private static final int slotPICK =1;
    private static final int slotAXE = 2;
    private static final int slotRightClick = 3;

    @Override
    public boolean isDamageable() {
        return true;
    }

    public Swapper(){
        setMaxStackSize(1);
        setMaxDamage(5000);
    }

    @Override
    public int getHarvestLevel(ItemStack swapper, String toolClass) {
        ItemStack is = null;
        if (toolClass.equals("shovel")){
            is = getStack(slotSHOVEL,swapper);
        } else if (toolClass.equals("axe")) {
            is = getStack(slotAXE,swapper);
        }
        if (is==null)
            is = getStack(slotPICK, swapper);
        if (is != null)
            return is.getItem().getHarvestLevel(is, toolClass);
        return super.getHarvestLevel(swapper, toolClass);
    }

    private ItemStack getLastStack(ItemStack swapper){
        if (swapper.hasTagCompound()){
            int slot = swapper.stackTagCompound.getInteger(stringLASTTOOL);
            return getStack(slot, swapper);
        }
        return null;
    }

    private ItemStack getStack(int slot, ItemStack swapper){
        ItemInventory inv = new ItemInventory(swapper, 4);
        if (!swapper.hasTagCompound())
            swapper.stackTagCompound = new NBTTagCompound();
        swapper.stackTagCompound.setInteger(stringLASTTOOL, slot);
        return  inv.getStackInSlot(slot);
    }

    @Override
    public boolean onItemUse(ItemStack swapper, EntityPlayer player, World world, int parm4, int parm5, int parm6, int parm7, float parm8, float parm9, float parm10) {
        ItemStack is = getStack(slotRightClick, swapper);
        if (is != null)
            return is.getItem().onItemUse(is, player, world, parm4, parm5, parm6, parm7, parm8, parm9, parm10);
        return false;
    }

    @Override
    public IIcon getIconIndex(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getIconIndex(is);
        return super.getIconIndex(swapper);
    }

    @Override
    public float getDigSpeed(ItemStack swapper, Block block, int metadata) {
        String effectiveTool = block.getHarvestTool(metadata);
        ItemStack is = null;
        if (effectiveTool != null) {
            if (effectiveTool.equals("shovel")) {
                is = getStack(slotSHOVEL, swapper);
            } else if (effectiveTool.equals("axe")) {
                is = getStack(slotAXE, swapper);
            }
            if (is==null)
                is = getStack(slotPICK, swapper);
            if (is != null)
                return is.getItem().getDigSpeed(is, block, metadata);
        }
        is = getStack(slotPICK, swapper);
        if (is != null)
            return is.getItem().getDigSpeed(is, block, metadata);
        return super.getDigSpeed(swapper, block, metadata);
    }

    @Override
    public IIcon getIcon(ItemStack swapper, int pass) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getIcon(is, pass);
        return super.getIcon(swapper, pass);
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getDisplayDamage(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if(is!=null)
            return is.getItem().getDisplayDamage(is);
        return 0;
    }

    @Override
    public boolean showDurabilityBar(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().showDurabilityBar(is);
        return false;
    }

    @Override
    public int getMaxDamage(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getMaxDamage(is);
        return super.getMaxDamage(swapper);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack swapper, int X, int Y, int Z, EntityPlayer player) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().onBlockStartBreak(is, X, Y, Z, player);
        return super.onBlockStartBreak(swapper, X, Y, Z, player);
    }

    @Override
    public boolean isDamaged(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().isDamaged(is);
        return false;
    }

    @Override
    public int getDamage(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getDamage(is);
        return 0;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack swapper, World world, Block block, int parm4, int parm5, int parm6, EntityLivingBase entity) {
        ItemStack is = getLastStack(swapper);
        if (is!=null)
            return is.getItem().onBlockDestroyed(is, world, block, parm4, parm5, parm6, entity);
        return super.onBlockDestroyed(swapper, world, block, parm4, parm5, parm6, entity);
    }

    @Override
    public float getSmeltingExperience(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is!=null)
            return is.getItem().getSmeltingExperience(is);
        return super.getSmeltingExperience(swapper);
    }

    @Override
    public boolean onItemUseFirst(ItemStack swapper, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        ItemStack is = getLastStack(swapper);
        if (is!=null)
            return is.getItem().onItemUseFirst(is, player, world, x, y, z, side, hitX, hitY, hitZ);
        return super.onItemUseFirst(swapper, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    public boolean isItemTool(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is!=null)
            return is.getItem().isItemTool(is);
        return super.isItemTool(swapper);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getItemUseAction(is);
        return super.getItemUseAction(swapper);
    }

    @Override
    public float func_150893_a(ItemStack swapper, Block block) {
        ItemStack is = getLastStack(swapper);
        if(is!=null)
            return is.getItem().func_150893_a(is,block);
        return super.func_150893_a(swapper, block);
    }

    @Override
    public void setDamage(ItemStack swapper, int damage) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            is.getItem().setDamage(is, damage);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack swapper, EntityPlayer player, Entity entity) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().onLeftClickEntity(is, player, entity);
        return super.onLeftClickEntity(swapper, player, entity);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE; // never despawn
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().canHarvestBlock(block, is);
        return super.canHarvestBlock(block, swapper);
    }

    @Override
    public Multimap getAttributeModifiers(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getAttributeModifiers(swapper);
        return super.getAttributeModifiers(swapper);
    }

    @Override
    public String getPotionEffect(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getPotionEffect(is);
        return super.getPotionEffect(swapper);
    }

    @Override
    public boolean hasEffect(ItemStack swapper, int pass) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().hasEffect(is);
        return super.hasEffect(swapper, pass);
    }

    @Override
    public IIcon getIcon(ItemStack swapper, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getIcon(is,renderPass, player,usingItem, useRemaining);
        return super.getIcon(swapper, renderPass, player, usingItem, useRemaining);
    }

    @Override
    public void onUsingTick(ItemStack swapper, EntityPlayer player, int count) {
        ItemStack is = getLastStack(swapper);
        if (is!=null) {
            is.getItem().onUsingTick(is, player, count);
        } else {
            super.onUsingTick(swapper, player, count);
        }
    }

    @Override
    public void onUpdate(ItemStack swapper, World world, Entity player, int parm4, boolean parm5) {
        ItemStack is = getLastStack(swapper);
        if (is!=null) {
            is.getItem().onUpdate(is,world,player,parm4,parm5);
        } else {
            super.onUpdate(swapper, world, player, parm4, parm5);
        }
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack swapper, EntityPlayer player) {
        ItemStack is = getLastStack(swapper);
        if (is!=null)
            return is.getItem().onDroppedByPlayer(is,player);
        return super.onDroppedByPlayer(swapper, player);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack swapper, World world, EntityPlayer player, int parm4) {
        ItemStack is = getLastStack(swapper);
        if (is!=null){
            is.getItem().onPlayerStoppedUsing(is,world,player,parm4);
        }else {
            super.onPlayerStoppedUsing(swapper, world, player, parm4);
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is!=null)
            return is.getItem().getMaxItemUseDuration(is);
        return super.getMaxItemUseDuration(swapper);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack swapper, EntityPlayer player, EntityLivingBase mob) {
        ItemStack is = getLastStack(swapper);
        if (is!=null)
            return is.getItem().itemInteractionForEntity(is,player,mob);
        return super.itemInteractionForEntity(swapper, player, mob);
    }

    @Override
    public ItemStack onEaten(ItemStack swapper, World world, EntityPlayer player) {
        ItemStack is = getStack(slotRightClick, swapper);
        if (is!=null)
            return is.getItem().onEaten(is,world,player);
        return super.onEaten(swapper, world, player);
    }

    @Override
    public boolean hitEntity(ItemStack swapper, EntityLivingBase mob, EntityLivingBase player) {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack swapper, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            if (!world.isRemote){
                player.openGui(Main.instance,0,world,0,0,0);
            }
        } else {
            ItemStack is = getStack(slotRightClick, swapper);
            if (is != null)
                is.getItem().onItemRightClick(is,world,player);
        }
        return swapper;
    }
}
