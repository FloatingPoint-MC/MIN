package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEvokerFangs;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderEvokerFangs;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityEvokerFangs;

public class ModelAdapterEvokerFangs extends ModelAdapter {
    public ModelAdapterEvokerFangs() {
        super(EntityEvokerFangs.class, "evocation_fangs", 0.0F);
    }

    public ModelBase makeModel() {
        return new ModelEvokerFangs();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelEvokerFangs)) {
            return null;
        } else {
            ModelEvokerFangs modelevokerfangs = (ModelEvokerFangs) model;

            if (modelPart.equals("base")) {
                return modelevokerfangs.base;
            } else if (modelPart.equals("upper_jaw")) {
                return modelevokerfangs.upperJaw;
            } else {
                return modelPart.equals("lower_jaw") ? modelevokerfangs.lowerJaw : null;
            }
        }
    }

    public String[] getModelRendererNames() {
        return new String[]{"base", "upper_jaw", "lower_jaw"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderEvokerFangs renderevokerfangs = new RenderEvokerFangs(rendermanager);
        renderevokerfangs.model = (ModelEvokerFangs) modelBase;
        renderevokerfangs.shadowSize = shadowSize;
        return renderevokerfangs;
    }
}
