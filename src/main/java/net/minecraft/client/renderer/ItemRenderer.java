package net.minecraft.client.renderer;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.render.impl.Animation;
import com.google.common.base.MoreObjects;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.optifine.Config;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.MapData;
import net.optifine.DynamicLights;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorForge;
import net.optifine.shaders.Shaders;

import java.util.Objects;

public class ItemRenderer {
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");

    /**
     * A reference to the Minecraft object.
     */
    private final Minecraft mc;
    private ItemStack itemStackMainHand = ItemStack.EMPTY;
    private ItemStack itemStackOffHand = ItemStack.EMPTY;
    private float equippedProgressMainHand;
    private float prevEquippedProgressMainHand;
    private float equippedProgressOffHand;
    private float prevEquippedProgressOffHand;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;

    public ItemRenderer(Minecraft mcIn) {
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }

    public void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform) {
        this.renderItemSide(entityIn, heldStack, transform, false);
    }

    public void renderItemSide(EntityLivingBase entitylivingbaseIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform, boolean leftHanded) {
        if (!heldStack.isEmpty()) {
            Item item = heldStack.getItem();
            Block block = Block.getBlockFromItem(item);
            GlStateManager.pushMatrix();
            boolean flag = this.itemRenderer.shouldRenderItemIn3D(heldStack) && block.getRenderLayer() == BlockRenderLayer.TRANSLUCENT;

            if (flag && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask)) {
                GlStateManager.depthMask(false);
            }

            this.itemRenderer.renderItem(heldStack, entitylivingbaseIn, transform, leftHanded);

            if (flag) {
                GlStateManager.depthMask(true);
            }

            GlStateManager.popMatrix();
        }
    }

    /**
     * Rotate the render around X and Y
     */
    private void rotateArroundXAndY(float angle, float angleY) {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(angleY, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void setLightmap() {
        AbstractClientPlayer abstractclientplayer = this.mc.player;
        int i = this.mc.world.getCombinedLight(new BlockPos(abstractclientplayer.posX, abstractclientplayer.posY + (double) abstractclientplayer.getEyeHeight(), abstractclientplayer.posZ), 0);

        if (Config.isDynamicLights()) {
            i = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), i);
        }

        float f = (float) (i & 65535);
        float f1 = (float) (i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
    }

    private void rotateArm(float p_187458_1_) {
        EntityPlayerSP entityplayersp = this.mc.player;
        float f = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * p_187458_1_;
        float f1 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * p_187458_1_;
        GlStateManager.rotate((entityplayersp.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((entityplayersp.rotationYaw - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
    }

    /**
     * Return the angle to render the Map
     */
    private float getMapAngleFromPitch(float pitch) {
        float f = 1.0F - pitch / 45.0F + 0.1F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = -MathHelper.cos(f * (float) Math.PI) * 0.5F + 0.5F;
        return f;
    }

    private void renderArms() {
        if (!this.mc.player.isInvisible()) {
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            this.renderArm(EnumHandSide.RIGHT);
            this.renderArm(EnumHandSide.LEFT);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
        }
    }

    private void renderArm(EnumHandSide p_187455_1_) {
        this.mc.getTextureManager().bindTexture(this.mc.player.getLocationSkin());
        Render<AbstractClientPlayer> render = this.renderManager.getEntityRenderObject(this.mc.player);
        RenderPlayer renderplayer = (RenderPlayer) render;
        assert renderplayer != null;
        GlStateManager.pushMatrix();
        float f = p_187455_1_ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -41.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(f * 0.3F, -1.1F, 0.45F);

        if (p_187455_1_ == EnumHandSide.RIGHT) {
            renderplayer.renderRightArm(this.mc.player);
        } else {
            renderplayer.renderLeftArm(this.mc.player);
        }

        GlStateManager.popMatrix();
    }

    private void renderMapFirstPersonSide(float p_187465_1_, EnumHandSide hand, float p_187465_3_, ItemStack stack) {
        float f = hand == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.translate(f * 0.125F, -0.125F, 0.0F);

        if (!this.mc.player.isInvisible()) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(f * 10.0F, 0.0F, 0.0F, 1.0F);
            this.renderArmFirstPerson(p_187465_1_, p_187465_3_, hand);
            GlStateManager.popMatrix();
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(f * 0.51F, -0.08F + p_187465_1_ * -1.2F, -0.75F);
        float f1 = MathHelper.sqrt(p_187465_3_);
        float f2 = MathHelper.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f5 = -0.3F * MathHelper.sin(p_187465_3_ * (float) Math.PI);
        GlStateManager.translate(f * f3, f4 - 0.3F * f2, f5);
        GlStateManager.rotate(f2 * -45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * f2 * -30.0F, 0.0F, 1.0F, 0.0F);
        this.renderMapFirstPerson(stack);
        GlStateManager.popMatrix();
    }

    private void renderMapFirstPerson(float p_187463_1_, float p_187463_2_, float p_187463_3_) {
        float f = MathHelper.sqrt(p_187463_3_);
        float f1 = -0.2F * MathHelper.sin(p_187463_3_ * (float) Math.PI);
        float f2 = -0.4F * MathHelper.sin(f * (float) Math.PI);
        GlStateManager.translate(0.0F, -f1 / 2.0F, f2);
        float f3 = this.getMapAngleFromPitch(p_187463_1_);
        GlStateManager.translate(0.0F, 0.04F + p_187463_2_ * -1.2F + f3 * -0.5F, -0.72F);
        GlStateManager.rotate(f3 * -85.0F, 1.0F, 0.0F, 0.0F);
        this.renderArms();
        float f4 = MathHelper.sin(f * (float) Math.PI);
        GlStateManager.rotate(f4 * 20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        this.renderMapFirstPerson(this.itemStackMainHand);
    }

    private void renderMapFirstPerson(ItemStack stack) {
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(0.38F, 0.38F, 0.38F);
        GlStateManager.disableLighting();
        this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.translate(-0.5F, -0.5F, 0.0F);
        GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        MapData mapdata = ReflectorForge.getMapData(Items.FILLED_MAP, stack, this.mc.world);

        if (mapdata != null) {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }

        GlStateManager.enableLighting();
    }

    private void renderArmFirstPerson(float p_187456_1_, float p_187456_2_, EnumHandSide p_187456_3_) {
        boolean flag = p_187456_3_ != EnumHandSide.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(p_187456_2_);
        float f2 = -0.3F * MathHelper.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(p_187456_2_ * (float) Math.PI);
        GlStateManager.translate(f * (f2 + 0.64000005F), f3 - 0.6F + p_187456_1_ * -0.6F, f4 - 0.71999997F);
        GlStateManager.rotate(f * 45.0F, 0.0F, 1.0F, 0.0F);
        float f5 = MathHelper.sin(p_187456_2_ * p_187456_2_ * (float) Math.PI);
        float f6 = MathHelper.sin(f1 * (float) Math.PI);
        GlStateManager.rotate(f * f6 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f * f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        AbstractClientPlayer abstractclientplayer = this.mc.player;
        this.mc.getTextureManager().bindTexture(abstractclientplayer.getLocationSkin());
        GlStateManager.translate(f * -1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(f * 120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(f * 5.6F, 0.0F, 0.0F);
        RenderPlayer renderplayer = (RenderPlayer) this.renderManager.<AbstractClientPlayer>getEntityRenderObject(abstractclientplayer);
        assert renderplayer != null;
        GlStateManager.disableCull();

        if (flag) {
            renderplayer.renderRightArm(abstractclientplayer);
        } else {
            renderplayer.renderLeftArm(abstractclientplayer);
        }

        GlStateManager.enableCull();
    }

    private void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack) {
        float f = (float) this.mc.player.getItemInUseCount() - p_187454_1_ + 1.0F;
        float f1 = f / (float) stack.getMaxItemUseDuration();

        if (f1 < 0.8F) {
            float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float) Math.PI) * 0.1F);
            GlStateManager.translate(0.0F, f2, 0.0F);
        }

        float f3 = 1.0F - (float) Math.pow(f1, 27.0D);
        int i = hand == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate(f3 * 0.6F * (float) i, f3 * -0.5F, f3 * 0.0F);
        GlStateManager.rotate((float) i * f3 * 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((float) i * f3 * 30.0F, 0.0F, 0.0F, 1.0F);
    }

    private void transformFirstPerson(EnumHandSide hand, float swingProgress) {
        int i = hand == EnumHandSide.RIGHT ? 1 : -1;
        float f = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        GlStateManager.rotate((float) i * (45.0F + f * -20.0F), 0.0F, 1.0F, 0.0F);
        float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
        GlStateManager.rotate((float) i * f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((float) i * -45.0F, 0.0F, 1.0F, 0.0F);
    }

    private void transformSideFirstPerson(EnumHandSide hand, float equippedProgress) {
        int i = hand == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float) i * 0.56F, -0.52F + equippedProgress * -0.6F, -0.72F);
    }

    private void transformFirstPersonItem(float side, float equipProgress, float swingProgress) {
        GlStateManager.translate(side * 0.56F, -0.52F - equipProgress, -0.71999997F);
        GlStateManager.rotate(-102.25F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(side * 20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(side * 80.0F, 0.0F, 0.0F, 1.0F);
        double f = Math.sin(swingProgress * swingProgress * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        GlStateManager.rotate((float) (f * -20.0F), 0, 1, 0);
        GlStateManager.rotate((float) (f1 * -80.0F), 0, 0, 1);
        GlStateManager.rotate((float) (f1 * -50.0F), 1, 0, 0);
        GlStateManager.scale(1.4f, 1.4f, 1.4f);
    }

    /**
     * Renders the active item in the player's hand when in first person mode.
     */
    public void renderItemInFirstPerson(float partialTicks) {
        AbstractClientPlayer abstractclientplayer = this.mc.player;
        float f = abstractclientplayer.getSwingProgress(partialTicks);
        EnumHand enumhand = MoreObjects.firstNonNull(abstractclientplayer.swingingHand, EnumHand.MAIN_HAND);
        float f1 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f2 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        boolean flag = true;
        boolean flag1 = true;

        if (abstractclientplayer.isHandActive()) {
            ItemStack itemstack = abstractclientplayer.getActiveItemStack();

            if (itemstack.getItem() instanceof ItemBow) {
                EnumHand enumhand1 = abstractclientplayer.getActiveHand();
                flag = enumhand1 == EnumHand.MAIN_HAND;
                flag1 = !flag;
            }
        }

        this.rotateArroundXAndY(f1, f2);
        this.setLightmap();
        this.rotateArm(partialTicks);
        GlStateManager.enableRescaleNormal();

        if (flag) {
            float f3 = enumhand == EnumHand.MAIN_HAND ? f : 0.0F;
            float f5 = 1.0F - (this.prevEquippedProgressMainHand + (this.equippedProgressMainHand - this.prevEquippedProgressMainHand) * partialTicks);

            if (!Reflector.ForgeHooksClient_renderSpecificFirstPersonHand.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_renderSpecificFirstPersonHand, EnumHand.MAIN_HAND, partialTicks, f1, f3, f5, this.itemStackMainHand)) {
                this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.MAIN_HAND, f3, this.itemStackMainHand, f5);
            }
        }

        if (flag1) {
            float f4 = enumhand == EnumHand.OFF_HAND ? f : 0.0F;
            float f6 = 1.0F - (this.prevEquippedProgressOffHand + (this.equippedProgressOffHand - this.prevEquippedProgressOffHand) * partialTicks);

            if (!Reflector.ForgeHooksClient_renderSpecificFirstPersonHand.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_renderSpecificFirstPersonHand, EnumHand.OFF_HAND, partialTicks, f1, f4, f6, this.itemStackOffHand)) {
                this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.OFF_HAND, f4, this.itemStackOffHand, f6);
            }
        }

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    public void renderItemInFirstPerson(AbstractClientPlayer player, float partialTicks, float rotationPitch, EnumHand hand, float swingProgress, ItemStack stack, float equippedProgress) {
        if (!Config.isShaders() || !Shaders.isSkipRenderHand(hand)) {
            boolean flag = hand == EnumHand.MAIN_HAND;
            EnumHandSide enumhandside = flag ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
            GlStateManager.pushMatrix();

            if (stack.isEmpty()) {
                if (flag && !player.isInvisible()) {
                    this.renderArmFirstPerson(equippedProgress, swingProgress, enumhandside);
                }
            } else {
                if (Animation.oldRodAnimation.getValue() && (stack.getItem() instanceof ItemCarrotOnAStick || stack.getItem() instanceof ItemFishingRod)) {
                    GlStateManager.translate(0.08F, -0.027F, -0.33F);
                    GlStateManager.scale(0.93F, 1.0F, 1.0F);
                }
                if (Animation.oldSwingAnimation.getValue() && swingProgress != 0.0F && !mc.player.isHandActive()) {
                    GlStateManager.scale(0.85F, 0.85F, 0.85F);
                    GlStateManager.translate(-0.06F, 0.003F, 0.05F);
                }
                if (stack.getItem() instanceof ItemMap) {
                    if (flag && this.itemStackOffHand.isEmpty()) {
                        this.renderMapFirstPerson(rotationPitch, equippedProgress, swingProgress);
                    } else {
                        this.renderMapFirstPersonSide(equippedProgress, enumhandside, swingProgress, stack);
                    }
                } else {
                    boolean flag1 = enumhandside == EnumHandSide.RIGHT;

                    if (player.isHandActive() && player.getItemInUseCount() > 0 && player.getActiveHand() == hand) {
                        int sideCode = flag1 ? 1 : -1;

                        switch (stack.getItemUseAction()) {
                            case NONE:
                                this.transformSideFirstPerson(enumhandside, equippedProgress);
                                break;

                            case EAT:
                            case DRINK:
                                if (!Animation.foodSwingMode.isCurrentMode("None") && swingProgress != 0.0f) {
                                    GlStateManager.translate(sideCode * 0.56, -0.52 + equippedProgress * -0.6, -0.72);
                                    GlStateManager.rotate(45.0F, 0, 0, 0);
                                    float f = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
                                    float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
                                    GlStateManager.rotate(f * 20.0F, 0, -1, 0);
                                    GlStateManager.rotate(f1 * 20.0F, 0, 0, -1);
                                    GlStateManager.rotate(f1 * 80.0F, -1, 0, 0);
                                    GlStateManager.translate(-0.8f, 0.2f, 0f);
                                    GlStateManager.rotate(30f, 0f, 1f, 0f);
                                    GlStateManager.rotate(-80f, 1f, 0f, 0f);
                                    GlStateManager.rotate(60f, 0f, 1f, 0f);
                                    GlStateManager.scale(1.4f, 1.4f, 1.4f);
                                    break;
                                }
                                this.transformEatFirstPerson(partialTicks, enumhandside, stack);
                                this.transformSideFirstPerson(enumhandside, equippedProgress);
                                if (!Animation.foodSwingMode.isCurrentMode("None")) {
                                    doSwingAnimation(swingProgress);
                                }
                                break;

                            case BLOCK:
                                this.transformFirstPersonItem(enumhandside == EnumHandSide.RIGHT ? 1.0F : -1.0F, equippedProgress, Animation.blockSwingMode.isCurrentMode("None") ? 0.0f : swingProgress);
                                break;

                            case BOW:
                                this.transformSideFirstPerson(enumhandside, equippedProgress);
                                if (!Animation.bowSwingMode.isCurrentMode("None")) {
                                    transformFirstPerson(enumhandside, swingProgress);
                                }
                                GlStateManager.translate((float) sideCode * -0.2785682F, 0.18344387F, 0.15731531F);
                                GlStateManager.rotate(-13.935F, 1.0F, 0.0F, 0.0F);
                                GlStateManager.rotate((float) sideCode * 35.3F, 0.0F, 1.0F, 0.0F);
                                GlStateManager.rotate((float) sideCode * -9.785F, 0.0F, 0.0F, 1.0F);
                                float f5 = (float) stack.getMaxItemUseDuration() - ((float) this.mc.player.getItemInUseCount() - partialTicks + 1.0F);
                                float f6 = f5 / 20.0F;
                                f6 = (f6 * f6 + f6 * 2.0F) / 3.0F;

                                if (f6 > 1.0F) {
                                    f6 = 1.0F;
                                }

                                if (f6 > 0.1F) {
                                    float f7 = MathHelper.sin((f5 - 0.1F) * 1.3F);
                                    float f3 = f6 - 0.1F;
                                    float f4 = f7 * f3;
                                    GlStateManager.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
                                }

                                GlStateManager.translate(f6 * 0.0F, f6 * 0.0F, f6 * 0.04F);
                                GlStateManager.scale(1.0F, 1.0F, 1.0F + f6 * 0.2F);
                                GlStateManager.rotate((float) sideCode * 45.0F, 0.0F, -1.0F, 0.0F);
                        }
                    } else {
                        float f = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
                        float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * ((float) Math.PI * 2F));
                        float f2 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
                        int i = flag1 ? 1 : -1;
                        GlStateManager.translate((float) i * f, f1, f2);
                        this.transformSideFirstPerson(enumhandside, equippedProgress);
                        this.transformFirstPerson(enumhandside, swingProgress);
                    }

                    this.renderItemSide(player, stack, flag1 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag1);
                }
            }

            GlStateManager.popMatrix();
        }
    }

    private void doSwingAnimation(float swingProgress) {
        float f = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
        GlStateManager.rotate(f * -20.0F, 0, 1, 0);
        GlStateManager.rotate(f1 * -20.0F, 0, 0, 1);
        GlStateManager.rotate(f1 * -80.0F, 1, 0, 0);
    }

    /**
     * Renders the overlays.
     */
    public void renderOverlays(float partialTicks) {
        GlStateManager.disableAlpha();

        if (this.mc.player.isEntityInsideOpaqueBlock()) {
            IBlockState iblockstate = this.mc.world.getBlockState(new BlockPos(this.mc.player));
            BlockPos blockpos = new BlockPos(this.mc.player);
            EntityPlayer entityplayer = this.mc.player;

            for (int i = 0; i < 8; ++i) {
                double d0 = entityplayer.posX + (double) (((float) (i % 2) - 0.5F) * entityplayer.width * 0.8F);
                double d1 = entityplayer.posY + (double) (((float) ((i >> 1) % 2) - 0.5F) * 0.1F);
                double d2 = entityplayer.posZ + (double) (((float) ((i >> 2) % 2) - 0.5F) * entityplayer.width * 0.8F);
                BlockPos blockpos1 = new BlockPos(d0, d1 + (double) entityplayer.getEyeHeight(), d2);
                IBlockState iblockstate1 = this.mc.world.getBlockState(blockpos1);

                if (iblockstate1.causesSuffocation()) {
                    iblockstate = iblockstate1;
                    blockpos = blockpos1;
                }
            }

            if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                Object object = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);

                if (!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, this.mc.player, partialTicks, object, iblockstate, blockpos)) {
                    this.renderSuffocationOverlay(this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
                }
            }
        }

        if (!this.mc.player.isSpectator()) {
            if (this.mc.player.isInsideOfMaterial(Material.WATER) && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, this.mc.player, partialTicks)) {
                this.renderWaterOverlayTexture();
            }

            if (this.mc.player.isBurning() && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, this.mc.player, partialTicks)) {
                this.renderFireInFirstPerson();
            }
        }

        GlStateManager.enableAlpha();
    }

    /**
     * Renders the given sprite over the player's view
     */
    private void renderSuffocationOverlay(TextureAtlasSprite sprite) {
        this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(0.1F, 0.1F, 0.1F, 0.5F);
        GlStateManager.pushMatrix();
        float f6 = sprite.getMinU();
        float f7 = sprite.getMaxU();
        float f8 = sprite.getMinV();
        float f9 = sprite.getMaxV();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-1.0D, -1.0D, -0.5D).tex(f7, f9).endVertex();
        bufferbuilder.pos(1.0D, -1.0D, -0.5D).tex(f6, f9).endVertex();
        bufferbuilder.pos(1.0D, 1.0D, -0.5D).tex(f6, f8).endVertex();
        bufferbuilder.pos(-1.0D, 1.0D, -0.5D).tex(f7, f8).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders a texture that warps around based on the direction the player is looking. Texture needs to be bound
     * before being called. Used for the water overlay.
     */
    private void renderWaterOverlayTexture() {
        if (!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
            this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            float f = this.mc.player.getBrightness();
            GlStateManager.color(f, f, f, 0.5F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.pushMatrix();
            float f7 = -this.mc.player.rotationYaw / 64.0F;
            float f8 = this.mc.player.rotationPitch / 64.0F;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(-1.0D, -1.0D, -0.5D).tex(4.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.pos(1.0D, -1.0D, -0.5D).tex(0.0F + f7, 4.0F + f8).endVertex();
            bufferbuilder.pos(1.0D, 1.0D, -0.5D).tex(0.0F + f7, 0.0F + f8).endVertex();
            bufferbuilder.pos(-1.0D, 1.0D, -0.5D).tex(4.0F + f7, 0.0F + f8).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
        }
    }

    /**
     * Renders the fire on the screen for first person mode. Arg: partialTickTime
     */
    private void renderFireInFirstPerson() {
        if (Managers.moduleManager.renderModules.get("FireFilter").isEnabled()) {
            if (Minecraft.getMinecraft().player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(12)))) {
                return;
            }
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        for (int i = 0; i < 2; ++i) {
            GlStateManager.pushMatrix();
            TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float f1 = textureatlassprite.getMinU();
            float f2 = textureatlassprite.getMaxU();
            float f3 = textureatlassprite.getMinV();
            float f4 = textureatlassprite.getMaxV();
            GlStateManager.translate((float) (-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            GlStateManager.rotate((float) (i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.setSprite(textureatlassprite);
            bufferbuilder.pos(-0.5D, -0.5D, -0.5D).tex(f2, f4).endVertex();
            bufferbuilder.pos(0.5D, -0.5D, -0.5D).tex(f1, f4).endVertex();
            bufferbuilder.pos(0.5D, 0.5D, -0.5D).tex(f1, f3).endVertex();
            bufferbuilder.pos(-0.5D, 0.5D, -0.5D).tex(f2, f3).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }

    public void updateEquippedItem() {
        this.prevEquippedProgressMainHand = this.equippedProgressMainHand;
        this.prevEquippedProgressOffHand = this.equippedProgressOffHand;
        EntityPlayerSP entityplayersp = this.mc.player;
        ItemStack itemstack = entityplayersp.getHeldItemMainhand();
        ItemStack itemstack1 = entityplayersp.getHeldItemOffhand();

        if (entityplayersp.isRowingBoat()) {
            this.equippedProgressMainHand = MathHelper.clamp(this.equippedProgressMainHand - 0.4F, 0.0F, 1.0F);
            this.equippedProgressOffHand = MathHelper.clamp(this.equippedProgressOffHand - 0.4F, 0.0F, 1.0F);
        } else {
            this.equippedProgressMainHand += MathHelper.clamp((Objects.equals(this.itemStackMainHand.getItem(), itemstack.getItem()) ? 1.0F : 0.0F) - this.equippedProgressMainHand, -0.4F, 0.4F);
            this.equippedProgressOffHand += MathHelper.clamp((Objects.equals(this.itemStackOffHand.getItem(), itemstack1.getItem()) ? 1.0F : 0.0F) - this.equippedProgressOffHand, -0.4F, 0.4F);
        }

        if (this.equippedProgressMainHand < 0.1F) {
            this.itemStackMainHand = itemstack;

            if (Config.isShaders()) {
                Shaders.setItemToRenderMain(this.itemStackMainHand);
            }
        }

        if (this.equippedProgressOffHand < 0.1F) {
            this.itemStackOffHand = itemstack1;

            if (Config.isShaders()) {
                Shaders.setItemToRenderOff(this.itemStackOffHand);
            }
        }
    }

    public void resetEquippedProgress(EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND) {
            this.equippedProgressMainHand = 0.0F;
        } else {
            this.equippedProgressOffHand = 0.0F;
        }
    }
}
