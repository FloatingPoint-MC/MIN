package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.system.ui.components.DraggableGameView;
import cn.floatingpoint.min.utils.client.Pair;
import cn.floatingpoint.min.utils.math.FunctionUtil;
import net.minecraft.client.gui.Gui;
import org.lwjglx.input.Keyboard;
import org.lwjglx.input.Mouse;

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
    private boolean drawable;
    private float scale = 1.0f;

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
    public void draw(int x, int y) {
        height = 0;
        drawable = false;
        if (!this.isEnabled()) {
            return;
        }
        if (showMoveKeys.getValue()) {
            drawable = true;
            drawButton("Forward", mc.gameSettings.keyBindForward.getKeyCode(), x + 24, y, 22, Keyboard.getKeyName(mc.gameSettings.keyBindForward.getKeyCode()));
            drawButton("Left", mc.gameSettings.keyBindLeft.getKeyCode(), x, y + 24, 22, Keyboard.getKeyName(mc.gameSettings.keyBindLeft.getKeyCode()));
            drawButton("Back", mc.gameSettings.keyBindBack.getKeyCode(), x + 24, y + 24, 22, Keyboard.getKeyName(mc.gameSettings.keyBindBack.getKeyCode()));
            drawButton("Right", mc.gameSettings.keyBindRight.getKeyCode(), x + 48, y + 24, 22, Keyboard.getKeyName(mc.gameSettings.keyBindRight.getKeyCode()));
            height = 48;
        }
        if (showJumpKey.getValue()) {
            drawable = true;
            drawButton("Jump", mc.gameSettings.keyBindJump.getKeyCode(), x, height + y, 70, "\247m------");
            height += 24;
        }
        if (showSneakKey.getValue()) {
            drawable = true;
            drawButton("Sneak", mc.gameSettings.keyBindSneak.getKeyCode(), x, height + y, 70, "Sneak");
            height += 24;
        }
        if (showMouseButton.getValue()) {
            drawable = true;
            leftCounter.removeIf(time -> System.currentTimeMillis() - time >= 1000L);
            rightCounter.removeIf(time -> System.currentTimeMillis() - time >= 1000L);
            drawButton("LMB", mc.gameSettings.keyBindAttack.getKeyCode(), x, height + y, 34, leftCounter.isEmpty() ? "LMB" : leftCounter.size() + "CPS");
            drawButton("RMB", mc.gameSettings.keyBindUseItem.getKeyCode(), x + 36, height + y, 34, rightCounter.isEmpty() ? "RMB" : rightCounter.size() + "CPS");
            height += 22;
        } else {
            height -= 2;
        }
    }

    @Override
    public boolean isDrawable() {
        return drawable;
    }

    private void drawButton(String identity, int keyCode, int x, int y, int width, String text) {
        colors.putIfAbsent(identity, 0);
        int current = colors.get(identity);
        if (pressed(keyCode)) {
            colors.put(identity, 39);
        } else {
            if (current > 0) {
                colors.put(identity, FunctionUtil.decreasedSpeed(current, 39, 0, 2));
            }
        }
        Gui.drawRect(x, y, x + width, y + 22, new Color(40 + current * 2, 40 + current * 2, 40 + current * 2, 102 + current).getRGB());
        if (shadow.getValue()) {
            Managers.fontManager.sourceHansSansCN_Regular_18.drawCenteredStringWithShadow(text, x + width / 2, y + 7, new Color(216, 216, 216, 216 + current).getRGB());
        } else {
            Managers.fontManager.sourceHansSansCN_Regular_18.drawCenteredString(text, x + width / 2, y + 7, new Color(216, 216, 216, 216 + current).getRGB());
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
        return "KeyStrokes";
    }
}
