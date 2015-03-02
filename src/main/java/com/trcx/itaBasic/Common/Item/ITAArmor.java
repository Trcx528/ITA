package com.trcx.itaBasic.Common.Item;

import com.trcx.itaBasic.Common.ITAArmorProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;

import java.util.List;

/**
 * Created by Trcx on 2/25/2015.
 */
public class ITAArmor extends ItemArmor implements ISpecialArmor {

    public ITAArmor(int armorTypeIndex) {
        super(ArmorMaterial.IRON, 0, armorTypeIndex);
    }

    @Override
    public void onCreated(ItemStack armor, World world, EntityPlayer player) {
        super.onCreated(armor, world, player);
        setMaxDamage(new ITAArmorProperties(armor).maxDurability);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return new ITAArmorProperties(stack).maxDurability;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        ArmorProperties ap= new ArmorProperties(0, 0, armor.getMaxDamage() +1  - armor.getItemDamage());
        ITAArmorProperties bap = new ITAArmorProperties(armor);
        if (!source.isUnblockable()) {
            ap.AbsorbRatio = bap.armorProtectionValue;
            ap.AbsorbRatio /= 100;
        }

        //System.out.println("Props: Max: " + ap.AbsorbMax + " Ratio: " + ap.AbsorbRatio  + "(" + damage +")" + " Unblockable: " + source.isUnblockable());
        return ap;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return new ITAArmorProperties(stack).enchantability;
    }

    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List data, boolean p_77624_4_) {
        ITAArmorProperties props = new ITAArmorProperties(is);
        data.add(EnumChatFormatting.GREEN + "Armor Display: " + props.armorDisplayValue);
        data.add("Protection %: " + props.armorProtectionValue);
        data.add("Enchantability: " + props.enchantability);
        data.add("Max Durability: " + props.maxDurability);
        data.add("Speed Modifier: " + props.speedModifier);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return new ITAArmorProperties(armor).armorDisplayValue;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        stack.damageItem(damage, entity);
    }
}
