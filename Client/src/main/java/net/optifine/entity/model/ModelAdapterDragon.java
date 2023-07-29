package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.boss.EntityDragon;

public class ModelAdapterDragon extends ModelAdapter {
    public ModelAdapterDragon() {
        super(EntityDragon.class, "dragon", 0.5F);
    }

    public ModelBase makeModel() {
        return new ModelDragon();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelDragon)) {
            return null;
        } else {
            ModelDragon modeldragon = (ModelDragon) model;

            switch (modelPart) {
                case "head":
                    return modeldragon.head;
                case "spine":
                    return modeldragon.spine;
                case "jaw":
                    return modeldragon.jaw;
                case "body":
                    return modeldragon.body;
                case "rear_leg":
                    return modeldragon.rearLeg;
                case "front_leg":
                    return modeldragon.frontLeg;
                case "rear_leg_tip":
                    return modeldragon.rearLegTip;
                case "front_leg_tip":
                    return modeldragon.frontLegTip;
                case "rear_foot":
                    return modeldragon.rearFoot;
                case "front_foot":
                    return modeldragon.frontFoot;
                case "wing":
                    return modeldragon.wing;
                default:
                    return modelPart.equals("wing_tip") ? modeldragon.wingTip : null;
            }
        }
    }

    public String[] getModelRendererNames() {
        return new String[]{"head", "spine", "jaw", "body", "rear_leg", "front_leg", "rear_leg_tip", "front_leg_tip", "rear_foot", "front_foot", "wing", "wing_tip"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderDragon renderdragon = new RenderDragon(rendermanager);
        renderdragon.mainModel = modelBase;
        renderdragon.shadowSize = shadowSize;
        return renderdragon;
    }
}
