package cn.floatingpoint.min.system.ui.components;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.Module;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-18 11:51:10
 */
public interface DraggableGameView {

    void draw(int x, int y);

    boolean isDrawable();

    int getWidth();

    int getHeight();

    void multiplyScale();

    void divideScale();

    float scalePercent();

    default int xOffset() {
        return 0;
    }

    default int yOffset() {
        return 0;
    }

    static DraggableGameView getDraggable(String name) {
        Module module = Managers.moduleManager.modules.get(name);
        if (module instanceof DraggableGameView) {
            return (DraggableGameView) module;
        }
        return null;
    }

    String getIdentity();
}
