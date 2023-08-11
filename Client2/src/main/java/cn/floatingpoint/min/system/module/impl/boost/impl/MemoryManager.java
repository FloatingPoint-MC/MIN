package cn.floatingpoint.min.system.module.impl.boost.impl;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.boost.BoostModule;
import cn.floatingpoint.min.system.module.value.impl.IntegerValue;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.system.ui.components.DraggableGameView;
import cn.floatingpoint.min.utils.client.Pair;
import cn.floatingpoint.min.utils.math.TimeHelper;
import net.minecraft.client.gui.Gui;

import java.awt.*;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-20 17:45:43
 */
public class MemoryManager extends BoostModule implements DraggableGameView {
    private final OptionValue display = new OptionValue(false);
    private final OptionValue autoRelease = new OptionValue(true);
    private final IntegerValue limit = new IntegerValue(10, 100, 10, 60, autoRelease::getValue);
    private final TimeHelper timer = new TimeHelper();
    private long maxMemory;
    private long usedMemory;
    private float percentage;
    private int width;
    private boolean drawable;
    private float scale = 1.0f;

    public MemoryManager() {
        addValues(
                new Pair<>("Display", display),
                new Pair<>("AutoRelease", autoRelease),
                new Pair<>("Limit", limit)
        );
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        this.maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        this.usedMemory = totalMemory - freeMemory;
        if (autoRelease.getValue()) {
            this.percentage = this.usedMemory * 100.0f / this.maxMemory;
            if (this.timer.isDelayComplete(1000) && this.limit.getValue().floatValue() <= this.percentage) {
                Runtime.getRuntime().gc();
                this.timer.reset();
            }
        }
    }

    @Override
    public void draw(int x, int y) {
        if (!this.isEnabled() || !display.getValue()) {
            width = 0;
            drawable = false;
            return;
        }
        drawable = true;
        int textColor = new Color(216, 216, 216, 216).getRGB();
        String text = "Max:" + maxMemory / 1024 / 1024 + "MB Used:" + usedMemory / 1024 / 1024 + "MB(" + (int) percentage + "%)";
        int textWidth = Managers.fontManager.sourceHansSansCN_Regular_18.getStringWidth(text);
        width = textWidth + 1;
        Gui.drawRect(x, y, x + width, y + 3, new Color(0, 0, 0, 120).getRGB());
        Gui.drawRect(x, y, x + width * this.percentage / 100, y + 3, textColor);
        Managers.fontManager.sourceHansSansCN_Regular_18.drawStringWithShadow(text, x, y + 6, textColor);
    }

    @Override
    public boolean isDrawable() {
        return drawable;
    }



    @Override
    public int getWidth() {
        return (int) (width * scale);
    }

    @Override
    public int getHeight() {
        return (int) (16 * scale);
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
        return "MemoryManager";
    }
}
