package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.ModeValue;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.system.ui.components.DraggableGameView;
import cn.floatingpoint.min.utils.client.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.awt.*;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-22 17:38:49
 */
public class StatusDisplay extends RenderModule implements DraggableGameView {
    private final OptionValue fps = new OptionValue(true);
    private final OptionValue ping = new OptionValue(true);
    private final ModeValue alignment = new ModeValue(new String[]{"L", "C", "R"}, "C");
    private final OptionValue shadow = new OptionValue(false);
    private final OptionValue background = new OptionValue(true);
    private int height;
    private boolean drawable;
    private float scale = 1.0f;

    public StatusDisplay() {
        addValues(
                new Pair<>("FPS", fps),
                new Pair<>("Ping", ping),
                new Pair<>("Alignment", alignment),
                new Pair<>("Shadow", shadow),
                new Pair<>("Background", background)
        );

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onRender3D() {

    }

    @Override
    public void draw(int x, int y) {
        height = 0;
        drawable = false;
        if (!this.isEnabled()) {
            return;
        }
        if (fps.getValue()) {
            drawable = true;
            drawButton(x, y, "FPS:" + Minecraft.getDebugFPS());
            height += 12;
            if (background.getValue()) {
                height += 12;
            }
        }
        if (ping.getValue()) {
            drawable = true;
            if (mc.player.connection != null) {
                NetworkPlayerInfo info = mc.player.connection.getPlayerInfo(mc.player.getUniqueID());
                if (info != null) {
                    int ping = info.getResponseTime();
                    drawButton(x, height + y, "Ping: " + ping + "ms");
                    height += 20;
                }
            }
        } else {
            height -= 2;
            if (!background.getValue()) {
                height += 12;
            }
        }
    }

    @Override
    public boolean isDrawable() {
        return drawable;
    }

    private void drawButton(int x, int y, String text) {
        if (background.getValue()) {
            Gui.drawRect(x, y, x + 70, y + 22, new Color(40, 40, 40, 102).getRGB());
        }
        if (alignment.isCurrentMode("C")) {
            if (shadow.getValue()) {
                Managers.fontManager.sourceHansSansCN_Regular_18.drawCenteredStringWithShadow(text, x + 35, y + 6, new Color(216, 216, 216, 216).getRGB());
            } else {
                Managers.fontManager.sourceHansSansCN_Regular_18.drawCenteredString(text, x + 35, y + 6, new Color(216, 216, 216, 216).getRGB());
            }
        } else if (alignment.isCurrentMode("L")) {
            if (shadow.getValue()) {
                Managers.fontManager.sourceHansSansCN_Regular_18.drawStringWithShadow(text, x + 2, y + 6, new Color(216, 216, 216, 216).getRGB());
            } else {
                Managers.fontManager.sourceHansSansCN_Regular_18.drawString(text, x + 2, y + 6, new Color(216, 216, 216, 216).getRGB());
            }
        } else if (alignment.isCurrentMode("R")) {
            int textWidth = Managers.fontManager.sourceHansSansCN_Regular_18.getStringWidth(text);
            if (shadow.getValue()) {
                Managers.fontManager.sourceHansSansCN_Regular_18.drawStringWithShadow(text, x + 68 - textWidth, y + 6, new Color(216, 216, 216, 216).getRGB());
            } else {
                Managers.fontManager.sourceHansSansCN_Regular_18.drawString(text, x + 58 - textWidth, y + 6, new Color(216, 216, 216, 216).getRGB());
            }
        }
    }



    @Override
    public int getWidth() {
        return (int) (70 * scale);
    }

    @Override
    public int getHeight() {
        return (int) (height * scale);
    }

    @Override
    public void multiplyScale() {
        scale += 0.1f;
        if (scale > 2.0f) {
            scale = 2.0f;
        }
    }

    @Override
    public void divideScale() {
        scale -= 0.1f;
        if (scale < 0.1f) {
            scale = 0.1f;
        }
    }

    @Override
    public float scalePercent() {
        return scale;
    }

    @Override
    public String getIdentity() {
        return "StatusDisplay";
    }
}
