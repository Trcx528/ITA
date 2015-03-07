package com.trcx.ita.Common.Item;

import com.trcx.ita.Common.AlloyNBT;
import com.trcx.ita.Common.MaterialProperty;
import com.trcx.ita.ITA;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Trcx on 3/6/2015.
 */
public class Alloy extends Item {
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List data, boolean p_77624_4_) {
        MaterialProperty mat = MaterialProperty.forAlloy(AlloyNBT.fromStack(is));
        if (mat != null) {
            DecimalFormat df = new DecimalFormat("#.#");
            data.add(EnumChatFormatting.BLUE + "Armor: " + df.format(mat.protection));
            data.add(EnumChatFormatting.AQUA + "Durability " + df.format(mat.durability));
            data.add(EnumChatFormatting.GREEN + "Enchantability: " + mat.enchantability);
            if (ITA.debug)
                data.add("Speed Modifier: " + mat.speed);
            data.add(EnumChatFormatting.LIGHT_PURPLE + "Speed: " + (mat.speed) * 100 + "%");
            if (mat.traits.keySet().size() > 0) {
                data.add(EnumChatFormatting.DARK_AQUA + "Traits: ");
                for (String trait : mat.traits.keySet()) {
                    data.add(" - " + trait + " " + mat.traits.get(trait).toString());
                }
            }
        }
    }
}