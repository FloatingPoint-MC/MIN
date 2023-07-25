package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

import java.util.stream.Collectors;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-20 21:13:32
 */
public class BoundingBox extends RenderModule {
    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onRender3D() {
        if (!Shaders.isShadowPass) {
            boolean wasTexture2DEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
            boolean wasLightingEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
            boolean wasCullEnabled = GL11.glIsEnabled(GL11.GL_CULL_FACE);
            boolean wasBlendEnabled = GL11.glIsEnabled(GL11.GL_BLEND);
            GlStateManager.depthMask(false);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            for (Entity entity : mc.world.loadedEntityList.stream().filter(e -> e != mc.player && e instanceof EntityPlayer && !e.isInvisible()).collect(Collectors.toList())) {
                double d_0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) mc.getRenderPartialTicks();
                double d_1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) mc.getRenderPartialTicks();
                double d_2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) mc.getRenderPartialTicks();
                double x = d_0 - mc.getRenderManager().getRenderPosX(), y = d_1 - mc.getRenderManager().getRenderPosY(), z = d_2 - mc.getRenderManager().getRenderPosZ();
                AxisAlignedBB boundingBox = entity.getEntityBoundingBox();
                RenderGlobal.drawBoundingBox(boundingBox.minX - entity.posX + x, boundingBox.minY - entity.posY + y, boundingBox.minZ - entity.posZ + z, boundingBox.maxX - entity.posX + x, boundingBox.maxY - entity.posY + y, boundingBox.maxZ - entity.posZ + z, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            if (wasBlendEnabled) {
                GlStateManager.enableBlend();
            }
            if (wasCullEnabled) {
                GlStateManager.enableCull();
            }
            if (wasLightingEnabled) {
                GlStateManager.enableLighting();
            }
            if (wasTexture2DEnabled) {
                GlStateManager.enableTexture2D();
            }
            GlStateManager.depthMask(true);
        }
    }
}
