package com.trcx.ita.Client;

import com.trcx.ita.Common.ItemInventory;
import com.trcx.ita.Common.SwapperContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;

/**
 * Created by Trcx on 3/14/2015.
 */
public class SwapperGui extends GuiContainer {
    public SwapperGui (InventoryPlayer invPlayer, ItemInventory invSwapper) {
        //the container is instanciated and passed to the superclass for handling
        super(new SwapperContainer(invPlayer, invSwapper));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        //fontRenderer.drawString("Tiny", 8, 6, 4210752);
        //fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        //draw your Gui here, only thing you need to change is the path
        //int texture = mc.renderEngine.getTexture("/gui/trap.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //this.mc.renderEngine.bindTexture(texture);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }
}
