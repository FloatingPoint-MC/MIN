package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWitch;
import net.minecraft.entity.monster.EntityWitch;

public class ModelAdapterWitch extends ModelAdapter
{
    public ModelAdapterWitch()
    {
        super(EntityWitch.class, "witch", 0.5F);
    }

    public ModelBase makeModel()
    {
        return new ModelWitch(0.0F);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelWitch))
        {
            return null;
        }
        else
        {
            ModelWitch modelwitch = (ModelWitch)model;

            switch (modelPart) {
                case "mole":
                    return modelwitch.mole;
                case "hat":
                    return modelwitch.witchHat;
                case "head":
                    return modelwitch.villagerHead;
                case "body":
                    return modelwitch.villagerBody;
                case "arms":
                    return modelwitch.villagerArms;
                case "left_leg":
                    return modelwitch.leftVillagerLeg;
                case "right_leg":
                    return modelwitch.rightVillagerLeg;
                default:
                    return modelPart.equals("nose") ? modelwitch.villagerNose : null;
            }
        }
    }

    public String[] getModelRendererNames()
    {
        return new String[] {"mole", "head", "body", "arms", "right_leg", "left_leg", "nose"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderWitch renderwitch = new RenderWitch(rendermanager);
        renderwitch.mainModel = modelBase;
        renderwitch.shadowSize = shadowSize;
        return renderwitch;
    }
}
