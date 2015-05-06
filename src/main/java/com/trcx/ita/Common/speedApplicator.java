package com.trcx.ita.Common;

import com.trcx.ita.CONSTS;
import com.trcx.ita.ITA;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;


/**
 * Created by Trcx on 3/4/2015.
 */
public class speedApplicator {

    private final float stepHeight = 1.0000528F;

    public speedApplicator() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    public float getSpeedModifier(EntityPlayer player) {
        float speedModifier = 1F;
        for (int i = 0; i < 4; i++) {
            ItemStack is = player.getCurrentArmor(i);
            if (is != null) {
                if (is.getItem() == ITA.Helmet || is.getItem() == ITA.Chestplate ||
                        is.getItem() == ITA.Leggings || is.getItem() == ITA.Boots) {
                    Double armorSpeedMod = new ITAArmorProperties(is).speedModifier;
                    speedModifier *= Math.max(armorSpeedMod, 0.0);
                }
            }
        }
        speedModifier = Math.max(speedModifier, 0);
        speedModifier = Math.min(speedModifier, ITA.maxSpeedMultiplier);
        return speedModifier;
    }

    @SubscribeEvent
    public void doSpeedApplication(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (event.player.getCurrentArmor(0) != null) {
                if (event.player.getCurrentArmor(0).getItem() == ITA.Boots) {
                    event.player.stepHeight = stepHeight;
                } else if (event.player.stepHeight == stepHeight) {
                    event.player.stepHeight = 0.5F;
                }
            } else if (event.player.stepHeight == stepHeight) {
                event.player.stepHeight = 0.5F;
            }
            EntityPlayer p = event.player;
            float speedModifier = getSpeedModifier(event.player);
            AttributeModifier modifier = p.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(CONSTS.speedAttribute);
            if (ITA.lastSpeedModifier != speedModifier || modifier == null ||
                    Double.compare(p.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue(), ITA.lastSpeedValue) != 0) {
                ITA.lastSpeedModifier = speedModifier;
                if (ITA.debug)
                    System.out.println("Speed Modifier: " + speedModifier);
                    System.out.println(event.player.getDisplayName() + ": " + event.player.toString());
                if (modifier != null)
                    p.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(modifier);

                double x = p.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
                modifier = new AttributeModifier(CONSTS.speedAttribute, "ITA Speed Modifier", -x + Math.max(x * speedModifier, 0.005), 0);
                p.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(modifier);

                ITA.lastSpeedValue = p.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
            }
        }
    }
}