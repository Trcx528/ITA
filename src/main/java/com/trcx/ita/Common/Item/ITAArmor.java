package com.trcx.ita.Common.Item;

import com.trcx.ita.Client.ArmorRenderer;
import com.trcx.ita.Common.ArmorNBT;
import com.trcx.ita.Common.ITAArmorProperties;
import com.trcx.ita.Common.Traits.BaseTrait;
import com.trcx.ita.Common.Traits.ProtectionTrait;
import com.trcx.ita.ITA;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

/**
 * Created by Trcx on 2/25/2015.
 */
public class ITAArmor extends ItemArmor implements ISpecialArmor {

    private ArmorRenderer model = null;

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        ITAArmorProperties props = new ITAArmorProperties(itemStack);

        if (props.isInvisible){
          return null;
        } else {
            if (this.model == null) {
                this.model = new ArmorRenderer();
            }
            this.model.bipedHead.showModel = (props.armorType == 0);
            this.model.bipedHeadwear.showModel = (props.armorType == 0);
            this.model.bipedBody.showModel = ((props.armorType == 1) || (props.armorType == 2));
            this.model.bipedLeftArm.showModel = (props.armorType == 1);
            this.model.bipedRightArm.showModel = (props.armorType == 1);
            this.model.bipedLeftLeg.showModel = (props.armorType == 2 || props.armorType == 3);
            this.model.bipedRightLeg.showModel = (props.armorType == 2 || props.armorType == 3);
            this.model.isSneak = entityLiving.isSneaking();

            this.model.isRiding = entityLiving.isRiding();
            this.model.isChild = entityLiving.isChild();

            this.model.aimedBow = false;
            this.model.heldItemRight = (entityLiving.getHeldItem() != null ? 1 : 0);
            this.model.setColor(props.getColor());
            return this.model;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
        if (new ArmorNBT(stack).invisible){
            return "ITA:textures/models/armor/invisble.png";
        } else if (this.armorType == 2) {
            return "ITA:textures/models/armor/basicarmor_2.png";
        } else {
            return "ITA:textures/models/armor/basicarmor_1.png";
        }
    }

    public ITAArmor(int armorTypeIndex) {
        super(ArmorMaterial.DIAMOND, 0, armorTypeIndex);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return new ITAArmorProperties(stack).maxDurability;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        ArmorProperties ap= new ArmorProperties(0, 0, 500);
        ITAArmorProperties bap = new ITAArmorProperties(armor);

        if (!source.isUnblockable()) {
            ap.AbsorbRatio = bap.armorProtectionValue;
        }

        for (BaseTrait trait: bap.traits.keySet()){
            if (trait instanceof ProtectionTrait){
                if (((ProtectionTrait) trait).getDamageSourceType() == source){
                    ap.AbsorbRatio += ((ProtectionTrait) trait).getDamageReductionPercent(bap.traits.get(trait));
                }
            }
        }

        ap.AbsorbRatio = ap.AbsorbRatio / 100;
        if (ITA.debug) {
            System.out.println("Props: Max: " + ap.AbsorbMax + " Ratio: " + ap.AbsorbRatio + "(" + damage + ")" + " Unblockable: " + source.isUnblockable());
        }
        return ap;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return new ITAArmorProperties(stack).enchantability;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return new ITAArmorProperties(armor).armorDisplayValue;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        stack.damageItem(damage, entity);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasColor(ItemStack is) {return  true;}

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon(this.getIconString());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColor(ItemStack is) {
        return new ITAArmorProperties(is).getColor();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean requiresMultipleRenderPasses() {
        return false;
    }

    @Override
    public boolean isRepairable() {
        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack armor, ItemStack material) {
        return material.getItem() == Items.diamond;
    }
}
