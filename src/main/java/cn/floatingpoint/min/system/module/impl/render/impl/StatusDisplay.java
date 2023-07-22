package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.render.RenderModule;
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
    private int height;

    public StatusDisplay() {
        addValues(
                new Pair<>("FPS", fps),
                new Pair<>("Ping", ping)
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
    public boolean draw(int x, int y) {
        height = 0;
        if (!this.isEnabled()) {
            return false;
        }
        if (fps.getValue()) {
            drawButton(x, y, "FPS:" + Minecraft.getDebugFPS());
            height += 24;
        }
        if (ping.getValue()) {
            if (mc.player.connection != null) {
                NetworkPlayerInfo info = mc.player.connection.getPlayerInfo(mc.player.getUniqueID());
                if (info != null) {
                    int ping = info.getResponseTime();
                    drawButton(x, height + y, "Ping: " + ping + "ms");
                    height += 22;
                }
            }
        } else {
            height -= 2;
        }
        return true;
    }

    private void drawButton(int x, int y, String text) {
        Gui.drawRect(x, y, x + 70, y + 22, new Color(40, 40, 40, 102).getRGB());
        Managers.fontManager.sourceHansSansCN_Regular_18.drawCenteredString(text, x + 35, y + 6, new Color(216, 216, 216, 216).getRGB());
    }

    @Override
    public int getWidth() {
        return 70;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String getIdentity() {
        return "StatusDisplay";
    }
}
