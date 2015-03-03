package com.trcx.ita.Common;

import com.trcx.ita.ITA;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trcx on 3/2/2015.
 */
public class ITACommand implements ICommand {
    @Override
    public String getCommandName() {
        return "ITA";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/ITA <sub command>";
    }

    @Override
    public List getCommandAliases() {
        List<String> retval = new ArrayList<String>();
        retval.add("ita");
        return retval;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parameters) {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)sender;
            if (parameters.length > 0) { // do more with this later
                if (parameters[0].equals("dictionary")) {
                    int[] ids = OreDictionary.getOreIDs(player.getHeldItem());
                    for (int id : ids) {
                        sender.addChatMessage(new ChatComponentText(OreDictionary.getOreName(id)));
                    }
                }
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return ITA.debug;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
