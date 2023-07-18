package cn.floatingpoint.min.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

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
}
