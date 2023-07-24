package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelParrot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderParrot;
import net.minecraft.entity.passive.EntityParrot;

public class ModelAdapterParrot extends ModelAdapter {
    public ModelAdapterParrot() {
        super(EntityParrot.class, "parrot", 0.3F);
    }

    public ModelBase makeModel() {
        return new ModelParrot();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelParrot)) {
            return null;
        } else {
            ModelParrot modelparrot = (ModelParrot) model;

            switch (modelPart) {
                case "body":
                    return modelparrot.body;
                case "tail":
                    return modelparrot.tail;
                case "left_wing":
                    return modelparrot.wingLeft;
                case "right_wing":
                    return modelparrot.wingRight;
                case "head":
                    return modelparrot.head;
                case "left_leg":
                    return modelparrot.legLeft;
                default:
                    return modelPart.equals("right_leg") ? modelparrot.legRight : null;
            }
        }
    }

    public String[] getModelRendererNames() {
        return new String[]{"body", "tail", "left_wing", "right_wing", "head", "left_leg", "right_leg"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderParrot renderparrot = new RenderParrot(rendermanager);
        renderparrot.mainModel = modelBase;
        renderparrot.shadowSize = shadowSize;
        return renderparrot;
    }
}
