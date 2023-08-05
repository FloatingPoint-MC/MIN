/*
* WARNING - DON'T COPY THIS UNLESS YOU CREDIT US!
* This font renderer is made by FPSMaster Team.
*
* Stupidly fast font loading.
*/
package cn.floatingpoint.min.system.ui.font;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.render.impl.NameProtect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.io.InputStream;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class FontRenderer extends net.minecraft.client.gui.FontRenderer {
    public int FONT_HEIGHT = 8;
    private StringCache stringCache;
    public int[] colorCode = new int[32];

    public FontRenderer(ResourceLocation resourceLocation, int size) {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        Font font;
        try {
            InputStream is = Objects.requireNonNull(Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation)).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            font = new Font("default", Font.PLAIN, size);
        }
        ResourceLocation res = new ResourceLocation("textures/font/ascii.png");
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;

            if (i == 6) {
                k += 85;
            }

            if (Minecraft.getMinecraft().gameSettings.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            this.colorCode[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }

        if (res.getPath().equalsIgnoreCase("textures/font/ascii.png") && this.getStringCache() == null) {
            this.setStringCache(new StringCache(colorCode));
            this.getStringCache().setDefaultFont(font, size);
        }
    }

    public StringCache getStringCache() {
        return stringCache;
    }

    public void setStringCache(StringCache value) {
        stringCache = value;
    }


    /**
     * Draws the specified string with a shadow.
     */
    public int drawStringWithShadow(String text, int x, int y, int color) {
        return this.drawString(text, x, y, color, true);
    }

    /**
     * Draws the specified string.
     */
    public int drawString(String text, int x, int y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    /**
     * Draws the specified string.
     */
    public int drawString(String text, int x, int y, int color, boolean dropShadow) {
        int i;
        GlStateManager.enableBlend();
        if (dropShadow) {
            getStringCache().renderString(text, x + 1.0F, y + 1.0F, new Color(50, 50, 50, 200).getRGB(), true);
            i = Math.max(0, getStringCache().renderString(text, x, y, color, false));
        } else {
            i = getStringCache().renderString(text, x, y, color, false);
        }
        GlStateManager.disableBlend();


        return i;
    }

    public int getStringWidth(String text) {
        if (Minecraft.getMinecraft().player != null && Managers.moduleManager.renderModules.get("NameProtect").isEnabled()) {
            text = text.replace(Minecraft.getMinecraft().player.getName(), NameProtect.name.getValue());
        }
        return getStringCache().getStringWidth(text);
    }

    public void drawCenteredString(String text, int x, int y, int color) {
        getStringCache().renderString(text, x - getStringCache().getStringWidth(text) / 2f, y, color, false);
    }

    public void drawCenteredStringWithShadow(String text, int x, int y, int color) {
        this.drawString(text, x - getStringCache().getStringWidth(text) / 2, y, color, true);
    }

    public float getHeight() {
        return 8;
    }

    @SuppressWarnings("all")
    public String trimStringToWidth(String text, int width) {
        return getStringCache().trimStringToWidth(text, width, false);
    }

    @SuppressWarnings("all")
    public String trimStringToWidth(String text, int width, boolean reverse) {
        return getStringCache().trimStringToWidth(text, width, reverse);
    }
}
