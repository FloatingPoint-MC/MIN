package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.passive.EntityOcelot;

public class ModelAdapterOcelot extends ModelAdapter {

    public ModelAdapterOcelot() {
        super(EntityOcelot.class, "ocelot", 0.4F);
    }

    public ModelBase makeModel() {
        return new ModelOcelot();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelOcelot)) {
            return null;
        } else {
            ModelOcelot modelocelot = (ModelOcelot) model;
            switch (modelPart) {
                case "back_left_leg":
                    return modelocelot.ocelotBackLeftLeg;
                case "back_right_leg":
                    return modelocelot.ocelotBackRightLeg;
                case "front_left_leg":
                    return modelocelot.ocelotFrontLeftLeg;
                case "front_right_leg":
                    return modelocelot.ocelotFrontRightLeg;
                case "tail":
                    return modelocelot.ocelotTail;
                case "tail2":
                    return modelocelot.ocelotTail2;
                case "head":
                    return modelocelot.ocelotHead;
                case "body":
                    return modelocelot.ocelotBody;
                default:
                    return null;
            }
        }
    }

    public String[] getModelRendererNames() {
        return new String[]{"back_left_leg", "back_right_leg", "front_left_leg", "front_right_leg", "tail", "tail2", "head", "body"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderOcelot renderocelot = new RenderOcelot(rendermanager);
        renderocelot.mainModel = modelBase;
        renderocelot.shadowSize = shadowSize;
        return renderocelot;
    }
}
