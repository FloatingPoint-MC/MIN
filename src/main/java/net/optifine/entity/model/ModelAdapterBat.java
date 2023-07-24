package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBat;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderBat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityBat;

public class ModelAdapterBat extends ModelAdapter {
    public ModelAdapterBat() {
        super(EntityBat.class, "bat", 0.25F);
    }

    public ModelBase makeModel() {
        return new ModelBat();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelBat)) {
            return null;
        } else {
            ModelBat modelbat = (ModelBat) model;

            switch (modelPart) {
                case "head":
                    return modelbat.batHead;
                case "body":
                    return modelbat.batBody;
                case "right_wing":
                    return modelbat.batRightWing;
                case "left_wing":
                    return modelbat.batLeftWing;
                case "outer_right_wing":
                    return modelbat.batOuterRightWing;
                default:
                    return modelPart.equals("outer_left_wing") ? modelbat.batOuterLeftWing : null;
            }
        }
    }

    public String[] getModelRendererNames() {
        return new String[]{"head", "body", "right_wing", "left_wing", "outer_right_wing", "outer_left_wing"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderBat renderbat = new RenderBat(rendermanager);
        renderbat.mainModel = modelBase;
        renderbat.shadowSize = shadowSize;
        return renderbat;
    }
}
