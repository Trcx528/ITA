package com.trcx.ita.Client;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

/**
 * Created by Trcx on 3/3/2015.
 */
public class ArmorRenderer extends ModelBiped {
    private float red;
    private float green;
    private float blue;

    public void setColor(int color){
        red = (float)(color >> 16 & 255) / 255.0F;
        green = (float)(color >> 8 & 255) / 255.0F;
        blue = (float)(color & 255) / 255.0F;
    }

    @Override
    public void render(Entity player, float parm2, float parm3, float parm4, float parm5, float parm6, float parm7){
        this.setRotationAngles(parm2, parm3, parm4, parm5, parm6, parm7, player);
        if (!player.isEntityInvulnerable()) {
            GL11.glColor4f(red, green, blue, 0.2f);
        }
        this.bipedHead.render(parm7);
        this.bipedBody.render(parm7);
        this.bipedRightArm.render(parm7);
        this.bipedLeftArm.render(parm7);
        this.bipedRightLeg.render(parm7);
        this.bipedLeftLeg.render(parm7);
        this.bipedHeadwear.render(parm7);
    }

}
