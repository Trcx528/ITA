package com.trcx.ita.Client;

import com.trcx.ita.Common.CommonProxy;
import com.trcx.ita.ITA;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * Created by Trcx on 3/16/2015.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void registerRenderers() {
        MinecraftForgeClient.registerItemRenderer(ITA.Swapper,new SwapperRenderer());
    }
}
