package com.trcx.ita.Common;

import com.trcx.ita.Common.CONSTS;
import com.trcx.ita.ITA;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.MinecraftForge;


/**
 * Created by Trcx on 3/4/2015.
 */
public class speedApplicator {

    public speedApplicator() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public float getSpeedModifier(EntityPlayer player) {
        float speedModifier = 0F;
        for (int i = 0; i < 4; i++) {
            ItemStack is = player.getCurrentArmor(i);
            if (is != null) {
                if (is.getItem() == ITA.Helmet || is.getItem() == ITA.Chestplate ||
                        is.getItem() == ITA.Leggings || is.getItem() == ITA.Boots) {
                    Double armorSpeedMod = new ITAArmorProperties(is).speedModifier;
                    if (speedModifier < 0) {
                        speedModifier += armorSpeedMod / 4;
                    } else {
                        speedModifier += (armorSpeedMod / 4) + 0.25F;
                    }
                } else {
                    speedModifier += 0.25F;
                }
            } else {
                speedModifier += 0.25F;
            }
        }
        if (speedModifier < 1)
            speedModifier += -1F;

        if (speedModifier > 1)
            speedModifier += 1F;

        return Math.max(speedModifier, -1);
    }

    @SideOnly(Side.CLIENT)
    private float getFov(EntityPlayer player) {
        float f = 1.0F;

        if (player.capabilities.isFlying) {
            f *= 1.1F;
        }

        f = (float) ((double) f * ((ITA.fovCalculatorValue / (double) player.capabilities.getWalkSpeed() + 1.0D) / 2.0D));

        if (player.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
            f = 1.0F;
        }

        if (player.isUsingItem() && player.getItemInUse().getItem() == Items.bow) {
            int i = player.getItemInUseDuration();
            float f1 = (float) i / 20.0F;

            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 *= f1;
            }

            f *= 1.0F - f1 * 0.15F;
        }
        return f;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void fovOverride(FOVUpdateEvent event) {
        event.newfov = getFov(event.entity);
    }

    @SubscribeEvent
    public void doSpeedApplication(TickEvent.PlayerTickEvent event) {
        float speedModifier = getSpeedModifier(event.player);
        if (event.phase == TickEvent.Phase.START) {
            if (ITA.lastSpeedModifier != speedModifier) {
                EntityPlayer p = event.player;
                AttributeModifier modifier = p.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(CONSTS.speedAttribute);
                if (modifier != null)
                    p.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(modifier);

                ITA.lastSpeedModifier = speedModifier;
                ITA.fovCalculatorValue = p.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();

                if (speedModifier != 1F) {
                    modifier = new AttributeModifier(CONSTS.speedAttribute, "ITA Speed Modifier", speedModifier, 1);
                    p.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(modifier);
                }
            }
        }
    }
}
