package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.system.ui.components.DraggableGameView;
import cn.floatingpoint.min.utils.client.Pair;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-22 16:56:25
 */
public class KeyStrokes extends RenderModule implements DraggableGameView {
    private final OptionValue shadow = new OptionValue(false);
    private final OptionValue showMoveKeys = new OptionValue(true);
    private final OptionValue showJumpKey = new OptionValue(true);
    private final OptionValue showSneakKey = new OptionValue(true);
    private final OptionValue showMouseButton = new OptionValue(true) {
        @Override
        public void setValue(Boolean value) {
            leftCounter.clear();
            rightCounter.clear();
            super.setValue(value);
        }
    };
    public final static HashSet<Long> leftCounter = new HashSet<>();
    public final static HashSet<Long> rightCounter = new HashSet<>();
    private final HashMap<String, Integer> colors = new HashMap<>();
    private int height;

    public KeyStrokes() {
        addValues(
                new Pair<>("Shadow", shadow),
                new Pair<>("ShowMoveKeys", showMoveKeys),
                new Pair<>("ShowJumpKey", showJumpKey),
                new Pair<>("ShowSneakKey", showSneakKey),
                new Pair<>("ShowMouseButton", showMouseButton)
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
        if (showMoveKeys.getValue()) {
            drawButton("Forward", mc.gameSettings.keyBindForward.getKeyCode(), x + 24, y, 22, Keyboard.getKeyName(mc.gameSettings.keyBindForward.getKeyCode()));
            drawButton("Left", mc.gameSettings.keyBindLeft.getKeyCode(), x, y + 24, 22, Keyboard.getKeyName(mc.gameSettings.keyBindLeft.getKeyCode()));
            drawButton("Back", mc.gameSettings.keyBindBack.getKeyCode(), x + 24, y + 24, 22, Keyboard.getKeyName(mc.gameSettings.keyBindBack.getKeyCode()));
            drawButton("Right", mc.gameSettings.keyBindRight.getKeyCode(), x + 48, y + 24, 22, Keyboard.getKeyName(mc.gameSettings.keyBindRight.getKeyCode()));
            height = 48;
        }
        if (showJumpKey.getValue()) {
            drawButton("Jump", mc.gameSettings.keyBindJump.getKeyCode(), x, height + y, 70, "\247m------");
            height += 24;
        }
        if (showSneakKey.getValue()) {
            drawButton("Sneak", mc.gameSettings.keyBindSneak.getKeyCode(), x, height + y, 70, "Sneak");
            height += 24;
        }
        if (showMouseButton.getValue()) {
            leftCounter.removeIf(time -> System.currentTimeMillis() - time >= 1000L);
            rightCounter.removeIf(time -> System.currentTimeMillis() - time >= 1000L);
            drawButton("LMB", mc.gameSettings.keyBindAttack.getKeyCode(), x, height + y, 34, leftCounter.size() == 0 ? "LMB" : leftCounter.size() + "CPS");
            drawButton("RMB", mc.gameSettings.keyBindUseItem.getKeyCode(), x + 36, height + y, 34, rightCounter.size() == 0 ? "RMB" : rightCounter.size() + "CPS");
            height += 22;
        } else {
            height -= 2;
        }
        return true;
    }

    private void drawButton(String identity, int keyCode, int x, int y, int width, String text) {
        colors.putIfAbsent(identity, 0);
        int current = colors.get(identity);
        if (pressed(keyCode)) {
            colors.put(identity, 39);
        } else {
            if (current > 0) {
                colors.put(identity, current -= 1);
            }
        }
        Gui.drawRect(x, y, x + width, y + 22, new Color(40 + current * 2, 40 + current * 2, 40 + current * 2, 102 + current).getRGB());
        if (shadow.getValue()) {
            Managers.fontManager.sourceHansSansCN_Regular_18.drawCenteredStringWithShadow(text, x + width / 2, y + 6, new Color(216, 216, 216, 216 + current).getRGB());
        } else {
            Managers.fontManager.sourceHansSansCN_Regular_18.drawCenteredString(text, x + width / 2, y + 6, new Color(216, 216, 216, 216 + current).getRGB());
        }
    }

    private boolean pressed(int keyCode) {
        if (keyCode < -50) {
            return Mouse.isButtonDown(keyCode + 100);
        }
        return Keyboard.isKeyDown(keyCode);
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
        return "KeyStrokes";
    }
}
