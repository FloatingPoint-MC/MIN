package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.entity.passive.EntityWolf;

public class ModelAdapterWolf extends ModelAdapter
{
    public ModelAdapterWolf()
    {
        super(EntityWolf.class, "wolf", 0.5F);
    }

    public ModelBase makeModel()
    {
        return new ModelWolf();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelWolf))
        {
            return null;
        }
        else
        {
            ModelWolf modelwolf = (ModelWolf)model;

            switch (modelPart) {
                case "head":
                    return modelwolf.wolfHeadMain;
                case "body":
                    return modelwolf.wolfBody;
                case "leg1":
                    return modelwolf.wolfLeg1;
                case "leg2":
                    return modelwolf.wolfLeg2;
                case "leg3":
                    return modelwolf.wolfLeg3;
                case "leg4":
                    return modelwolf.wolfLeg4;
                case "tail":
                    return modelwolf.wolfTail;
                default:
                    return modelPart.equals("mane") ? modelwolf.wolfMane : null;
            }
        }
    }

    public String[] getModelRendererNames()
    {
        return new String[] {"head", "body", "leg1", "leg2", "leg3", "leg4", "tail", "mane"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderWolf renderwolf = new RenderWolf(rendermanager);
        renderwolf.mainModel = modelBase;
        renderwolf.shadowSize = shadowSize;
        return renderwolf;
    }
}
