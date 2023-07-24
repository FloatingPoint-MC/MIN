package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;

public class ModelAdapterBoat extends ModelAdapter {
    public ModelAdapterBoat() {
        super(EntityBoat.class, "boat", 0.5F);
    }

    public ModelBase makeModel() {
        return new ModelBoat();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelBoat)) {
            return null;
        } else {
            ModelBoat modelboat = (ModelBoat) model;

            switch (modelPart) {
                case "bottom":
                    return modelboat.boatSides[0];
                case "back":
                    return modelboat.boatSides[1];
                case "front":
                    return modelboat.boatSides[2];
                case "right":
                    return modelboat.boatSides[3];
                case "left":
                    return modelboat.boatSides[4];
                case "paddle_left":
                    return modelboat.paddles[0];
                case "paddle_right":
                    return modelboat.paddles[1];
                default:
                    return modelPart.equals("bottom_no_water") ? modelboat.noWater : null;
            }
        }
    }

    public String[] getModelRendererNames() {
        return new String[]{"bottom", "back", "front", "right", "left", "paddle_left", "paddle_right", "bottom_no_water"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderBoat renderboat = new RenderBoat(rendermanager);
        renderboat.modelBoat = modelBase;
        renderboat.shadowSize = shadowSize;
        return renderboat;
    }
}
