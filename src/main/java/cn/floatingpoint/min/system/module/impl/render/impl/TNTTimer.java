package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashSet;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-22 17:40:39
 */
public class TNTTimer extends RenderModule {
    private final HashSet<EntityTNTPrimed> tntSet = new HashSet<>();

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onRender3D() {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableTexture2D();
        float partialTicks = mc.getRenderPartialTicks();
        float scale = 0.065f;
        for (EntityTNTPrimed tnt : tntSet) {
            if (tnt.isDead) {
                tntSet.remove(tnt);
                continue;
            }
            double x = tnt.lastTickPosX + (tnt.posX - tnt.lastTickPosX) * (double) partialTicks - mc.getRenderManager().getRenderPosX();
            double y = tnt.lastTickPosY + (tnt.posY - tnt.lastTickPosY) * (double) partialTicks - mc.getRenderManager().getRenderPosY();
            double z = tnt.lastTickPosZ + (tnt.posZ - tnt.lastTickPosZ) * (double) partialTicks - mc.getRenderManager().getRenderPosX();
            GlStateManager.translate(((float) x), ((float) y + tnt.height + 0.5f - (tnt.height / 2.0f)), ((float) z));
            GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
            GlStateManager.rotate((-mc.getRenderManager().playerViewY), 0.0f, 1.0f, 0.0f);
            GlStateManager.scale((-(scale /= 2.0f)), (-scale), (-scale));
            double xLeft = -10.0;
            double xRight = 10.0;
            double yUp = -20.0;
            double yDown = -10.0;
            drawRect((float) xLeft, (float) yUp, (float) xRight, (float) yDown, new Color(0, 0, 0, 80).getRGB());
            drawTime(tnt);
        }
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private static void drawTime(EntityTNTPrimed entity) {
        GlStateManager.disableDepth();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String text = decimalFormat.format((entity.getFuse() / 20.0F));
        Managers.fontManager.sourceHansSansCN_Regular_18.drawString(text, -Managers.fontManager.sourceHansSansCN_Regular_18.getStringWidth(text) + 5, -20, new Color(216, 216, 216).getRGB());
        GlStateManager.enableDepth();
    }

    public static void drawRect(float g, float h, float i, float j, int col1) {
        float f = (float) (col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float) (col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float) (col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float) (col1 & 0xFF) / 255.0f;
        GlStateManager.blendFunc(770, 771);
        GL11.glEnable(2848);
        GlStateManager.pushMatrix();
        GlStateManager.color(f1, f2, f3, f);
        GlStateManager.glBegin(7);
        GL11.glVertex2d(i, h);
        GL11.glVertex2d(g, h);
        GL11.glVertex2d(g, j);
        GL11.glVertex2d(i, j);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
    }
}
