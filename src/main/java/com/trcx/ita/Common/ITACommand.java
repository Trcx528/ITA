package com.trcx.ita.Common;

import com.trcx.ita.CONSTS;
import com.trcx.ita.Common.Network.commonConfigPacket;
import com.trcx.ita.Common.Network.jsonConfigPacket;
import com.trcx.ita.ITA;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trcx on 3/2/2015.
 */
public class ITACommand extends CommandBase {
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
                    ITA.config.loadConfigs();
                    sender.addChatMessage(new ChatComponentText("Configs reloaded"));
                } else if (parameters[0].equals("sync")){
                    jsonConfigPacket.jsonConfigMessage msg = new jsonConfigPacket.jsonConfigMessage(ITA.jsonMaterial, CONSTS.packetMaterialId);
                    ITA.net.sendToAll(msg);
                    msg = new jsonConfigPacket.jsonConfigMessage(ITA.jsonPotionTraits, CONSTS.packetPotionTraitsId);
                    ITA.net.sendToAll(msg);
                    msg = new jsonConfigPacket.jsonConfigMessage(ITA.jsonProtectionTraits, CONSTS.packetProtectionTraitsId);
                    ITA.net.sendToAll(msg);
                    ITA.net.sendToAll(new commonConfigPacket.commonConfigMessage());
                }
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
        List<String> retVal = new ArrayList<String>();
        if (parameters.length <= 1){
            if (parameters[0].startsWith("r"))
                retVal.add("reload");
            if (parameters[0].startsWith("d"))
                retVal.add("dictionary");
            if (parameters[0].startsWith("s"))
                retVal.add("sync");
        } else {
            retVal.add("reload");
            retVal.add("dictionary");
            retVal.add("sync");
        }
        return retVal;
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
