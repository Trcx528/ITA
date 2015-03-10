package com.trcx.ita.Common.Network;

import com.trcx.ita.CONSTS;
import com.trcx.ita.Common.Config;
import com.trcx.ita.ITA;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/**
 * Created by Trcx on 3/7/2015.
 */
public class jsonConfigPacket implements IMessageHandler<jsonConfigPacket.jsonConfigMessage, IMessage> {

    @Override
    public IMessage onMessage(jsonConfigMessage message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            if (message.configName.equals(CONSTS.packetMaterialId)){
                ITA.jsonMaterial = message.json;
            } else if (message.configName.equals(CONSTS.packetProtectionTraitsId)){
                ITA.jsonProtectionTraits = message.json;
            } else if (message.configName.equals(CONSTS.packetPotionTraitsId)){
                ITA.jsonPotionTraits = message.json;
            }
            System.out.println(message.configName);
            System.out.println(message.json);
            Config.loadFromCachedJSON(message.configName);
            return null;
        }
        return null;
    }

    public static class jsonConfigMessage implements IMessage{

        public String json;
        public String configName;

        public jsonConfigMessage(){}

        public jsonConfigMessage(String json, String configName){
            this.json = json;
            this.configName = configName;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            json = new String(buf.readBytes(buf.readInt()).array());
            configName = new String(buf.readBytes(buf.readInt()).array());
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(json.getBytes().length);
            buf.writeBytes(json.getBytes());
            buf.writeInt(configName.getBytes().length);
            buf.writeBytes(configName.getBytes());
        }
    }
}
