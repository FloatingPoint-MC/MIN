package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderArmorStand;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityArmorStand;
import net.optifine.Config;

public class ModelAdapterArmorStand extends ModelAdapterBiped
{
    public ModelAdapterArmorStand()
    {
        super(EntityArmorStand.class, "armor_stand", 0.0F);
    }

    public ModelBase makeModel()
    {
        return new ModelArmorStand();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart)
    {
        if (!(model instanceof ModelArmorStand))
        {
            return null;
        }
        else
        {
            ModelArmorStand modelarmorstand = (ModelArmorStand)model;

            if (modelPart.equals("right"))
            {
                return modelarmorstand.standRightSide;
            }
            else if (modelPart.equals("left"))
            {
                return modelarmorstand.standLeftSide;
            }
            else if (modelPart.equals("waist"))
            {
                return modelarmorstand.standWaist;
            }
            else
            {
                return modelPart.equals("base") ? modelarmorstand.standBase : super.getModelRenderer(modelarmorstand, modelPart);
            }
        }
    }

    public String[] getModelRendererNames()
    {
        String[] astring = super.getModelRendererNames();
        astring = (String[])Config.addObjectsToArray(astring, new String[] {"right", "left", "waist", "base"});
        return astring;
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderArmorStand renderarmorstand = new RenderArmorStand(rendermanager);
        renderarmorstand.mainModel = modelBase;
        renderarmorstand.shadowSize = shadowSize;
        return renderarmorstand;
    }
}
