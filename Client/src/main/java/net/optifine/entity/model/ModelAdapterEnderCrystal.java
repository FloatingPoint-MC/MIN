package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.optifine.Config;

public class ModelAdapterEnderCrystal extends ModelAdapter {
    public ModelAdapterEnderCrystal() {
        this("end_crystal");
    }

    protected ModelAdapterEnderCrystal(String name) {
        super(EntityEnderCrystal.class, name, 0.5F);
    }

    public ModelBase makeModel() {
        return new ModelEnderCrystal(true);
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelEnderCrystal)) {
            return null;
        } else {
            ModelEnderCrystal modelendercrystal = (ModelEnderCrystal) model;

            if (modelPart.equals("cube")) {
                return modelendercrystal.cube;
            } else if (modelPart.equals("glass")) {
                return modelendercrystal.glass;
            } else {
                return modelPart.equals("base") ? modelendercrystal.base : null;
            }
        }
    }

    public String[] getModelRendererNames() {
        return new String[]{"cube", "glass", "base"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        Render<EntityEnderCrystal> render = rendermanager.getEntityRenderMap().get(EntityEnderCrystal.class);

        if (!(render instanceof RenderEnderCrystal)) {
            Config.warn("Not an instance of RenderEnderCrystal: " + render);
            return null;
        } else {
            RenderEnderCrystal renderendercrystal = (RenderEnderCrystal) render;
            renderendercrystal.modelEnderCrystal = modelBase;
            renderendercrystal.shadowSize = shadowSize;
            return renderendercrystal;
        }
    }
}
