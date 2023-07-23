package cn.floatingpoint.min.system.ui.loading;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.utils.math.FunctionUtil;
import cn.floatingpoint.min.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;

public class GuiLoading extends GuiScreen {
    private float animation;
    private int stage;

    @Override
    public void initGui() {
        animation = 1.0f;
        stage = 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, width, height, new Color(0, 0, 0).getRGB());
        GL11.glPushMatrix();
        int size = (int) (128 * animation);
        if (this.stage <= 1) {
            RenderUtil.drawImage(new ResourceLocation("min/logo.png"), (width - size) / 2, (height - size) / 2, size, size);
        } else {
            RenderUtil.drawImage(new ResourceLocation("min/square.png"), (width - size) / 2, (height - size) / 2, size, size);
        }
        GL11.glPopMatrix();
        int colorCode = 216;
        int alpha = 255;
        if (stage == 0) {
            colorCode = (int) (216 + (animation / 1.3f) * (255 - 216));
        } else if (stage == 1) {
            colorCode = 255;
            alpha *= animation / 1.3f;
        } else if (stage == 2) {
            alpha = 0;
        }
        Managers.fontManager.comfortaa_25.drawCenteredString("Min Client, Max Performance", width / 2, height / 2 + 86, new Color(colorCode, colorCode, colorCode, alpha).getRGB());
        if (stage == 0) {
            animation = FunctionUtil.decreasedSpeed(animation, 1.0f, 1.25f, 0.03f * (animation - 0.25f));
        } else if (stage == 1) {
            animation = FunctionUtil.increasedSpeed(animation, 1.1f, 0.0f, 0.1f * animation);
        } else if (stage == 2) {
            animation += 0.35f;
        }
        if (stage == 0 && 1.25f - animation <= 0.0005f) {
            stage = 1;
        } else if (stage == 1 && animation <= 0.0001f) {
            animation = 0.0f;
            stage = 2;
        } else if (stage == 2) {
            if (animation > 10.0f) {
                if (Managers.clientManager.firstStart) {
                    mc.displayGuiScreen(new GuiFirstStart());
                } else {
                    mc.displayGuiScreen(new GuiMainMenu(true));
                }
            }
        }
        animation = Math.max(animation, 0.0f);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }
}
