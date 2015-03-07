package com.trcx.ita.Common;

import com.trcx.ita.Main;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
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
                    GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(player.getHeldItem().getItem());
                    sender.addChatMessage(new ChatComponentText(uid.modId + ":" + uid.name));
                    int[] ids = OreDictionary.getOreIDs(player.getHeldItem());
                    for (int id : ids) {
                        sender.addChatMessage(new ChatComponentText(OreDictionary.getOreName(id)));
                    }
                } else if (parameters[0].equals("reload")) {
                    Main.loadConfigs();
                    sender.addChatMessage(new ChatComponentText("Configs reloaded"));
                }
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
        if (parameters.length <= 1){
            List<String> retVal = new ArrayList<String>();
            if (parameters[0].startsWith("r"))
                retVal.add("reload");
            if (parameters[0].startsWith("d"))
                retVal.add("dictionary");
            return retVal;
        }
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
