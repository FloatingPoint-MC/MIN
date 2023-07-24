package net.optifine.entity.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityHorse;

public class ModelAdapterHorse extends ModelAdapter {
    public ModelAdapterHorse() {
        super(EntityHorse.class, "horse", 0.75F);
    }

    protected ModelAdapterHorse(Class entityClass, String name, float shadowSize) {
        super(entityClass, name, shadowSize);
    }

    public ModelBase makeModel() {
        return new ModelHorse();
    }

    public ModelRenderer getModelRenderer(ModelBase model, String modelPart) {
        if (!(model instanceof ModelHorse)) {
            return null;
        } else {
            ModelHorse modelhorse = (ModelHorse) model;

            switch (modelPart) {
                case "head":
                    return modelhorse.head;
                case "upper_mouth":
                    return modelhorse.upperMouth;
                case "lower_mouth":
                    return modelhorse.lowerMouth;
                case "horse_left_ear":
                    return modelhorse.horseLeftEar;
                case "horse_right_ear":
                    return modelhorse.horseRightEar;
                case "mule_left_ear":
                    return modelhorse.muleLeftEar;
                case "mule_right_ear":
                    return modelhorse.muleRightEar;
                case "neck":
                    return modelhorse.neck;
                case "horse_face_ropes":
                    return modelhorse.horseFaceRopes;
                case "mane":
                    return modelhorse.mane;
                case "body":
                    return modelhorse.body;
                case "tail_base":
                    return modelhorse.tailBase;
                case "tail_middle":
                    return modelhorse.tailMiddle;
                case "tail_tip":
                    return modelhorse.tailTip;
                case "back_left_leg":
                    return modelhorse.backLeftLeg;
                case "back_left_shin":
                    return modelhorse.backLeftShin;
                case "back_left_hoof":
                    return modelhorse.backLeftHoof;
                case "back_right_leg":
                    return modelhorse.backRightLeg;
                case "back_right_shin":
                    return modelhorse.backRightShin;
                case "back_right_hoof":
                    return modelhorse.backRightHoof;
                case "front_left_leg":
                    return modelhorse.frontLeftLeg;
                case "front_left_shin":
                    return modelhorse.frontLeftShin;
                case "front_left_hoof":
                    return modelhorse.frontLeftHoof;
                case "front_right_leg":
                    return modelhorse.frontRightLeg;
                case "front_right_shin":
                    return modelhorse.frontRightShin;
                case "front_right_hoof":
                    return modelhorse.frontRightHoof;
                case "mule_left_chest":
                    return modelhorse.muleLeftChest;
                case "mule_right_chest":
                    return modelhorse.muleRightChest;
                case "horse_saddle_bottom":
                    return modelhorse.horseSaddleBottom;
                case "horse_saddle_front":
                    return modelhorse.horseSaddleFront;
                case "horse_saddle_back":
                    return modelhorse.horseSaddleBack;
                case "horse_left_saddle_rope":
                    return modelhorse.horseLeftSaddleRope;
                case "horse_left_saddle_metal":
                    return modelhorse.horseLeftSaddleMetal;
                case "horse_right_saddle_rope":
                    return modelhorse.horseRightSaddleRope;
                case "horse_right_saddle_metal":
                    return modelhorse.horseRightSaddleMetal;
                case "horse_left_face_metal":
                    return modelhorse.horseLeftFaceMetal;
                case "horse_right_face_metal":
                    return modelhorse.horseRightFaceMetal;
                case "horse_left_rein":
                    return modelhorse.horseLeftRein;
                case "horse_right_rein":
                    return modelhorse.horseRightRein;
                default:
                    return null;
            }
        }
    }

    public String[] getModelRendererNames() {
        return new String[]{"head", "upper_mouth", "lower_mouth", "horse_left_ear", "horse_right_ear", "mule_left_ear", "mule_right_ear", "neck", "horse_face_ropes", "mane", "body", "tail_base", "tail_middle", "tail_tip", "back_left_leg", "back_left_shin", "back_left_hoof", "back_right_leg", "back_right_shin", "back_right_hoof", "front_left_leg", "front_left_shin", "front_left_hoof", "front_right_leg", "front_right_shin", "front_right_hoof", "mule_left_chest", "mule_right_chest", "horse_saddle_bottom", "horse_saddle_front", "horse_saddle_back", "horse_left_saddle_rope", "horse_left_saddle_metal", "horse_right_saddle_rope", "horse_right_saddle_metal", "horse_left_face_metal", "horse_right_face_metal", "horse_left_rein", "horse_right_rein"};
    }

    public IEntityRenderer makeEntityRender(ModelBase modelBase, float shadowSize) {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        RenderHorse renderhorse = new RenderHorse(rendermanager);
        renderhorse.mainModel = modelBase;
        renderhorse.shadowSize = shadowSize;
        return renderhorse;
    }
}
