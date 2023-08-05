package cn.floatingpoint.min.system.ui.components;

import cn.floatingpoint.min.management.Managers;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public abstract class ClickableButton {
    private int x;
    private int y;
    private int width;
    private int height;
    private String text;

    public ClickableButton(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void drawScreen() {
        GlStateManager.disableBlend();
        int startX = x - width / 2;
        int endX = x + width / 2;
        int startY = y - height / 2;
        int endY = y + height / 2;
        Gui.drawRect(startX, startY, endX, endY, new Color(0, 0, 0, 102).getRGB());
        Gui.drawRect(startX, startY, startX + 0.5D, endY, new Color(44, 44, 44, 102).getRGB());
        Gui.drawRect(startX + 0.5D, endY - 0.5D, endX - 0.5D, endY, new Color(44, 44, 44, 102).getRGB());
        Gui.drawRect(endX - 0.5D, startY, endX, endY, new Color(44, 44, 44, 102).getRGB());
        Gui.drawRect(startX + 0.5D, startY, endX - 0.5D, startY + 0.5, new Color(44, 44, 44, 102).getRGB());
        Managers.fontManager.sourceHansSansCN_Regular_18.drawCenteredString(text, x, startY + (height == 20 ? 6 : 2), new Color(216, 216, 216).getRGB());
        GlStateManager.enableBlend();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            int startX = x - width / 2;
            int endX = x + width / 2;
            int startY = y - height / 2;
            int endY = y + height / 2;
            if (Gui.isHovered(startX, startY, endX, endY, mouseX, mouseY)) {
                clicked();
            }
        }
    }

    public abstract void clicked();
}
