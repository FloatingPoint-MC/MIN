package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRabbit;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderRabbit;
import net.minecraft.entity.passive.EntityRabbit;

public class ModelAdapterRabbit extends ModelAdapter{

    public ModelAdapterRabbit()
    {
        super(EntityRabbit.class, "rabbit", 0.3F);
    }

    public ModelBase makeModel()
    {
        return new ModelRabbit();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelRabbit))
        {
            return null;
        }
        else
        {
            ModelRabbit modelrabbit = (ModelRabbit)model;
            switch (modelPart) {
                case "left_foot":
                    return modelrabbit.rabbitLeftFoot;
                case "right_foot":
                    return modelrabbit.rabbitRightFoot;
                case "left_thigh":
                    return modelrabbit.rabbitLeftThigh;
                case "right_thigh":
                    return modelrabbit.rabbitRightThigh;
                case "body":
                    return modelrabbit.rabbitBody;
                case "left_arm":
                    return modelrabbit.rabbitLeftArm;
                case "right_arm":
                    return modelrabbit.rabbitRightArm;
                case "head":
                    return modelrabbit.rabbitHead;
                case "right_ear":
                    return modelrabbit.rabbitRightEar;
                case "left_ear":
                    return modelrabbit.rabbitLeftEar;
                case "tail":
                    return modelrabbit.rabbitTail;
                case "nose":
                    return modelrabbit.rabbitNose;
                default:
                    return null;
            }
        }
    }

    public String[] getModelRendererNames()
    {
        return new String[] {"left_foot", "right_foot", "left_thigh", "right_thigh", "body", "left_arm", "right_arm", "head", "right_ear", "left_ear", "tail", "nose"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderRabbit renderrabbit = new RenderRabbit(rendermanager);
        renderrabbit.mainModel = modelBase;
        renderrabbit.shadowSize = shadowSize;
        return renderrabbit;
    }
}
