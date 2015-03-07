package com.trcx.ita.Common.Network;

import com.trcx.ita.ITA;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/**
 * Created by Trcx on 3/7/2015.
 */
public class commonConfigPacket implements IMessageHandler<commonConfigPacket.commonConfigMessage, IMessage>{

    @Override
    public IMessage onMessage(commonConfigMessage message, MessageContext ctx) {
        if (ctx.side.isClient()){
            ITA.alloyMultiplier = message.alloyMultiplier;
            ITA.craftingHammerRequired = message.craftingHammerRequired;
            ITA.maxSpeedMultiplier = message.maxSpeedMultiplier;
            ITA.maxProtectionPreType = message.maxProtectionPreType;
        }
        return null;
    }

    public static class commonConfigMessage implements IMessage{
        public float alloyMultiplier;
        public boolean craftingHammerRequired;
        public float maxSpeedMultiplier;
        public float maxProtectionPreType;

        public commonConfigMessage () {}

        @Override
        public void fromBytes(ByteBuf buf) {
            alloyMultiplier = buf.readFloat();
            craftingHammerRequired = buf.readBoolean();
            maxSpeedMultiplier = buf.readFloat();
            maxProtectionPreType = buf.readFloat();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeFloat(ITA.alloyMultiplier);
            buf.writeBoolean(ITA.craftingHammerRequired);
            buf.writeFloat(ITA.maxSpeedMultiplier);
            buf.writeFloat(ITA.maxProtectionPreType);
        }
    }
}
