package cn.floatingpoint.min.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {
    private static final ShaderUtil roundedShader = new ShaderUtil();

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        GL11.glEnable(3042);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GL11.glDisable(3042);
    }

    public static void doGlScissor(int x, int y, int width, int height) {
        int scaleFactor = 1;
        int k = Minecraft.getMinecraft().gameSettings.guiScale;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && Minecraft.getMinecraft().displayWidth / (scaleFactor + 1) >= 320
                && Minecraft.getMinecraft().displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor(x * scaleFactor, Minecraft.getMinecraft().displayHeight - (y + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
    }

    public static void drawRoundedRect(int x, int y, int x2, int y2, int radius, int c) {
        int width = x2 - x;
        int height = y2 - y;
        Color color = new Color(c, true);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        roundedShader.init();

        setupRoundedRectUniforms(x, y, width, height, radius);
        roundedShader.setUniform2i("blur", 0);
        roundedShader.setUniform("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        ShaderUtil.drawQuads(x - 1, y - 1, width + 2, height + 2);
        roundedShader.unload();
        GlStateManager.disableBlend();
    }
    private static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        roundedShader.setUniform("location", x * sr.getScaleFactor(), (Minecraft.getMinecraft().displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        roundedShader.setUniform("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        roundedShader.setUniform("radius", radius * sr.getScaleFactor());
    }

    public static int reAlpha(int color, int alpha) {
        int red = color >> 16 & 255;
        int green = color >> 8 & 255;
        int blue = color & 255;
        return new Color(red, green, blue, alpha).getRGB();
    }

    public static void drawBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        posBoundingBoxSquare(boundingBox, tessellator, bufferBuilder);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        posBoundingBoxLeftHalf(boundingBox, bufferBuilder);
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        posBoundingBoxHalf(boundingBox, bufferBuilder);
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawBoundingBoxOutline(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION);
        posBoundingBoxHalf(boundingBox, bufferBuilder);
        tessellator.draw();
        bufferBuilder.begin(3, DefaultVertexFormats.POSITION);
        posBoundingBoxLeftHalf(boundingBox, bufferBuilder);
        tessellator.draw();
        bufferBuilder.begin(1, DefaultVertexFormats.POSITION);
        posBoundingBoxSquare(boundingBox, tessellator, bufferBuilder);
    }

    private static void posBoundingBoxSquare(AxisAlignedBB boundingBox, Tessellator tessellator, BufferBuilder bufferBuilder) {
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }

    private static void posBoundingBoxLeftHalf(AxisAlignedBB boundingBox, BufferBuilder bufferBuilder) {
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
    }

    private static void posBoundingBoxHalf(AxisAlignedBB boundingBox, BufferBuilder bufferBuilder) {
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        bufferBuilder.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        bufferBuilder.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
    }

    public static void drawGradientSideways(int left, int top, int right, int bottom, int color1, int color2) {
        float f = (float) (color1 >> 24 & 255) / 255.0f;
        float f1 = (float) (color1 >> 16 & 255) / 255.0f;
        float f2 = (float) (color1 >> 8 & 255) / 255.0f;
        float f3 = (float) (color1 & 255) / 255.0f;
        float f4 = (float) (color2 >> 24 & 255) / 255.0f;
        float f5 = (float) (color2 >> 16 & 255) / 255.0f;
        float f6 = (float) (color2 >> 8 & 255) / 255.0f;
        float f7 = (float) (color2 & 255) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
    }

    public static void drawGradientRect(int x, int y, int x1, int y1, int topColor, int bottomColor) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        float alpha = (float) (topColor >> 24 & 255) / 255.0f;
        float red = (float) (topColor >> 16 & 255) / 255.0f;
        float green = (float) (topColor >> 8 & 255) / 255.0f;
        float blue = (float) (topColor & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        alpha = (float) (bottomColor >> 24 & 255) / 255.0f;
        red = (float) (bottomColor >> 16 & 255) / 255.0f;
        green = (float) (bottomColor >> 8 & 255) / 255.0f;
        blue = (float) (bottomColor & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawFilledCircle(double x, double y, double r, int c, int id) {
        float f = (float) (c >> 24 & 0xff) / 255F;
        float f1 = (float) (c >> 16 & 0xff) / 255F;
        float f2 = (float) (c >> 8 & 0xff) / 255F;
        float f3 = (float) (c & 0xff) / 255F;
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glColor4f(f1, f2, f3, f);
        glBegin(GL_POLYGON);
        if (id == 1) {
            glVertex2d(x, y);
            for (int i = 0; i <= 90; i++) {
                double x2 = MathHelper.sin((float) (i * Math.PI / 180)) * r;
                double y2 = MathHelper.cos((float) (i * Math.PI / 180)) * r;
                glVertex2d(x - x2, y - y2);
            }
        } else if (id == 2) {
            glVertex2d(x, y);
            for (int i = 90; i <= 180; i++) {
                double x2 = MathHelper.sin((float) (i * Math.PI / 180)) * r;
                double y2 = MathHelper.cos((float) (i * Math.PI / 180)) * r;
                glVertex2d(x - x2, y - y2);
            }
        } else if (id == 3) {
            glVertex2d(x, y);
            for (int i = 270; i <= 360; i++) {
                double x2 = MathHelper.sin((float) (i * Math.PI / 180)) * r;
                double y2 = MathHelper.cos((float) (i * Math.PI / 180)) * r;
                glVertex2d(x - x2, y - y2);
            }
        } else if (id == 4) {
            glVertex2d(x, y);
            for (int i = 180; i <= 270; i++) {
                double x2 = MathHelper.sin((float) (i * Math.PI / 180)) * r;
                double y2 = MathHelper.cos((float) (i * Math.PI / 180)) * r;
                glVertex2d(x - x2, y - y2);
            }
        } else {
            for (int i = 0; i <= 360; i++) {
                double x2 = MathHelper.sin((float) (i * Math.PI / 180)) * r;
                double y2 = MathHelper.cos((float) (i * Math.PI / 180)) * r;
                glVertex2f((float) (x - x2), (float) (y - y2));
            }
        }
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }
}
