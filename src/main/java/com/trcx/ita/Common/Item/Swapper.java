package com.trcx.ita.Common.Item;

import com.google.common.collect.Multimap;
import com.trcx.ita.Common.OpenMods.ItemInventory;
import com.trcx.ita.Main;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Set;

/**
 * Created by Trcx on 3/14/2015.
 */
public class Swapper extends Item {

    private static final String stringLASTTOOL = "LastTool";

    //private static IIcon transparentIcon;
    private static IIcon outlineIcon;

    private static final int slotSHOVEL = 0;
    private static final int slotPICK =1;
    private static final int slotAXE = 2;
    private static final int slotSword = 3;
    private static final int slotRightClick = 4;

    public static final int swapperSlots = 5;

    public Swapper(){
        super();
        setMaxStackSize(1);
        setMaxDamage(Integer.MAX_VALUE);
    }
//region inventory stuff
    private ItemStack getLastStack(ItemStack swapper){
        if (swapper.hasTagCompound()){
            int slot = swapper.stackTagCompound.getInteger(stringLASTTOOL);
            return getStack(slot, swapper);
        }
        return null;
    }

    private ItemStack getStack(int slot, ItemStack swapper){
        ItemInventory inv = new ItemInventory(swapper, swapperSlots);
        if (!swapper.hasTagCompound())
            swapper.stackTagCompound = new NBTTagCompound();
        swapper.stackTagCompound.setInteger(stringLASTTOOL, slot);
        return inv.getStackInSlot(slot);
    }
//endregion

//region Dig

    @Override
    public boolean onBlockStartBreak(ItemStack swapper, int X, int Y, int Z, EntityPlayer player) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().onBlockStartBreak(is,X,Y,Z,player);
        return super.onBlockStartBreak(swapper, X, Y, Z, player);
    }

    @Override
    public float getDigSpeed(ItemStack swapper, Block block, int metadata) {
        String effTool = block.getHarvestTool(metadata);
        ItemStack is;
        if (effTool != null) {
            if (effTool.equals("shovel")) {
                is = getStack(slotSHOVEL, swapper);
            } else if (effTool.equals("axe")) {
                is = getStack(slotAXE, swapper);
            } else {
                is = getStack(slotPICK, swapper);
            }
        } else {
            is = getStack(slotPICK, swapper);
        }
        if (is != null)
            return is.getItem().getDigSpeed(is, block, metadata);
        return super.getDigSpeed(swapper, block, metadata);
    }

    @Override
    public int getHarvestLevel(ItemStack swapper, String toolClass) {
        ItemStack is;
        if (toolClass.equals("shovel")){
            is = getStack(slotSHOVEL,swapper);
        } else if (toolClass.equals("axe")) {
            is = getStack(slotAXE, swapper);
        } else if (toolClass.equals("sword")) {
            is = getStack(slotSword, swapper);
        } else {
            is = getStack(slotPICK, swapper);
        }
        if (is != null)
            return is.getItem().getHarvestLevel(is, toolClass);
        return super.getHarvestLevel(swapper, toolClass);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack swapper, World world, Block block, int x, int y, int z, EntityLivingBase player) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().onBlockDestroyed(is,world, block,x,y,z,player);
        return super.onBlockDestroyed(swapper, world, block, x, y, z, player);
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().canHarvestBlock(block, is);
        return super.canHarvestBlock(block, swapper);
    }

//endregion

//region attack/hit
    @Override
    public boolean onEntitySwing(EntityLivingBase player, ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if(is != null)
            return is.getItem().onEntitySwing(player,is);
        return super.onEntitySwing(player, swapper);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack swapper, EntityPlayer player, Entity mob) {
        ItemStack is = getStack(slotSword,swapper);
        if(is != null)
            return is.getItem().onLeftClickEntity(is,player,mob);
        return super.onLeftClickEntity(swapper, player, mob);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack swapper, EntityPlayer player, EntityLivingBase mob) {
        ItemStack is = getStack(slotSword,swapper);
        if(is != null)
            return is.getItem().itemInteractionForEntity(is,player,mob);
        return super.itemInteractionForEntity(swapper, player, mob);
    }

    @Override
    public boolean hitEntity(ItemStack swapper, EntityLivingBase mob, EntityLivingBase player) {
        ItemStack is = getStack(slotSword,swapper);
        if (is != null)
            return is.getItem().hitEntity(is,mob,player);
        return super.hitEntity(swapper, mob, player);
    }

//endregion

//region right click

    @Override
    public void onUsingTick(ItemStack swapper, EntityPlayer player, int count) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            is.getItem().onUsingTick(is,player,count);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack swapper, World world, EntityPlayer player, int par4) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            is.getItem().onPlayerStoppedUsing(is,world,player,par4);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack swapper) {
        ItemStack is = getStack(slotRightClick, swapper);
        if (is != null)
            return is.getItem().getItemUseAction(is);
        return super.getItemUseAction(swapper);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack swapper) {
        ItemStack is = getStack(slotRightClick,swapper);
        if (is != null)
            return is.getItem().getMaxItemUseDuration(is);
        return super.getMaxItemUseDuration(swapper);
    }

    @Override
    public ItemStack onEaten(ItemStack swapper, World world, EntityPlayer player) {
        ItemStack is = getStack(slotRightClick,swapper);
        if (is != null)
            return is.getItem().onEaten(is,world,player);
        return super.onEaten(swapper, world, player);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack swapper, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            if (!world.isRemote)
                player.openGui(Main.instance, 0, world, 0, 0, 0);
        } else {
            ItemStack is = getStack(slotRightClick,swapper);
            if (is!=null)
                is.getItem().onItemRightClick(is,world,player);
        }
        return swapper;
    }

    @Override
    public boolean onItemUseFirst(ItemStack swapper, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        ItemStack is = getStack(slotRightClick, swapper);
        if (is !=null){
            Item item = is.getItem();
            if (item instanceof ItemBlock){
                Block placeBlock = ((ItemBlock)item).field_150939_a;
                Block clickBlock = world.getBlock(x,y,z);
                if (clickBlock == Blocks.snow_layer) side =1;
                else if (!clickBlock.isReplaceable(world,x,y,z)){
                    ForgeDirection fSide = ForgeDirection.getOrientation(side);
                    x += fSide.offsetX;
                    y += fSide.offsetY;
                    z += fSide.offsetZ;
                }
                return !world.canPlaceEntityOnSide(placeBlock, x ,y,z,false,side,null,is);
            } else {
                return item.onItemUse(is,player,world,x,y,z,side,hitX,hitY,hitZ);
            }
        }
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack swapper, EntityPlayer player, World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ) {
        ItemStack is = getStack(slotRightClick, swapper);
        if (is !=null){
            Item item = is.getItem();
            if (item instanceof ItemBlock){
                boolean ret = ((ItemBlock)item).onItemUse(is,player,world,x,y,z,side,clickX,clickY,clickZ);
                ItemInventory inv = new ItemInventory(swapper,swapperSlots);
                if (is.stackSize == 0){
                    inv.setInventorySlotContents(slotRightClick,null);
                }
                inv.markDirty();
                return ret;
            } else {
                return item.onItemUse(is,player,world,x,y,z,side,clickX,clickY,clickZ);
            }
        }
        return false;
    }

    //endregion

//region damage stuff
    @Override
    public double getDurabilityForDisplay(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getDurabilityForDisplay(is);
        return super.getDurabilityForDisplay(swapper);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    @Override
    public boolean showDurabilityBar(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if(is != null)
            return is.getItem().showDurabilityBar(is);
        return false;
    }

    @Override
    public int getDisplayDamage(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getDisplayDamage(is);
        return super.getDisplayDamage(swapper);
    }

    @Override
    public int getDamage(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getDamage(is);
        return super.getDamage(swapper);
    }



    @Override
    public int getMaxDamage(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getMaxDamage(is);
        return super.getMaxDamage(swapper);
    }

    @Override
    public boolean isDamaged(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().isDamaged(is);
        return super.isDamaged(swapper);
    }

    @Override
    public void setDamage(ItemStack swapper, int damage) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            is.getItem().setDamage(is, damage);
    }

//endregion

//region icons/rendering
    @Override
    public FontRenderer getFontRenderer(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getFontRenderer(is);
        return super.getFontRenderer(swapper);
    }

    @Override
    public int getColorFromItemStack(ItemStack swapper, int par2) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getColorFromItemStack(swapper,par2);
        return super.getColorFromItemStack(swapper, par2);
    }

    @Override
    public IIcon getIcon(ItemStack swapper, int renderPass) {
        ItemStack is = getLastStack(swapper);
        IIcon ret;
        if (is != null) {
            if (is.getItem().requiresMultipleRenderPasses()) {
                ret = is.getItem().getIcon(is, renderPass);
            } else {
                ret= is.getItem().getIconIndex(is);
            }
        } else {
            ret = outlineIcon;
        }
        return ret;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        super.registerIcons(register);
        //transparentIcon = register.registerIcon("ITA:Transparent.png");
        outlineIcon = register.registerIcon("ITA:Swapper");
    }

    @Override
    public IIcon getIcon(ItemStack swapper, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        ItemStack is = getLastStack(swapper);
        IIcon ret;
        if (is != null){
            if (is.getItem().requiresMultipleRenderPasses()) {
                ret = is.getItem().getIcon(is, renderPass, player, usingItem, useRemaining);
            } else if (renderPass == 3) {
                ret = null;
            }else {
                ret = is.getItem().getIconIndex(is);
            }
        }else {
            ret = outlineIcon;
        }
        return ret;
    }

    @Override
    public IIcon getIconIndex(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is!=null)
            return is.getItem().getIconIndex(is);
        return outlineIcon;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int metadata) {
        return 16;
    }
//endregion

//region misc

    @Override
    public void addInformation(ItemStack swapper, EntityPlayer player, List data, boolean val) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            is.getItem().addInformation(is,player,data,val);
    }

    @Override
    public void onUpdate(ItemStack swapper, World world, Entity player, int par4, boolean par5) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            is.getItem().onUpdate(is,world,player,par4,par5);
    }

    @Override
    public String getPotionEffect(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getPotionEffect(is);
        return super.getPotionEffect(swapper);
    }

    @Override
    public Multimap getAttributeModifiers(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getAttributeModifiers(swapper);
        return super.getAttributeModifiers(swapper);
    }

    @Override
    public boolean isItemTool(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().isItemTool(is);
        return super.isItemTool(swapper);
    }

    @Override
    public EnumRarity getRarity(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getRarity(is);
        return super.getRarity(swapper);
    }

    @Override
    public boolean hasCustomEntity(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().hasCustomEntity(is);
        return super.hasCustomEntity(swapper);
    }

    @Override
    public String getItemStackDisplayName(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return "Swapper: " + is.getItem().getItemStackDisplayName(is);
        return super.getItemStackDisplayName(swapper);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean hasEffect(ItemStack swapper, int pass) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().hasEffect(is,pass);
        return super.hasEffect(swapper, pass);
    }

    @Override
    public float getSmeltingExperience(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getSmeltingExperience(is);
        return super.getSmeltingExperience(swapper);
    }

    @Override
    public float func_150893_a(ItemStack swapper, Block block) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().func_150893_a(is,block);
        return super.func_150893_a(swapper, block);
    }

    @Override
    public boolean hasEffect(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().hasEffect(is);
        return super.hasEffect(swapper);
    }

    @Override
    public Set<String> getToolClasses(ItemStack swapper) {
        ItemStack is = getLastStack(swapper);
        if (is != null)
            return is.getItem().getToolClasses(is);
        return super.getToolClasses(swapper);
    }
    //endregion
}
