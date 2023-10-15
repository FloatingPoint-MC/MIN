package net.minecraft.util;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.mouse.RawMouseController;
import net.minecraft.client.Minecraft;
import org.lwjglx.input.Mouse;
import org.lwjglx.opengl.Display;

public class MouseHelper {
    /**
     * Mouse delta X this frame
     */
    public int deltaX;

    /**
     * Mouse delta Y this frame
     */
    public int deltaY;

    /**
     * Grabs the mouse cursor it doesn't move and isn't seen.
     */
    public void grabMouseCursor() {
        Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
        Mouse.setGrabbed(true);
        this.deltaX = 0;
        this.deltaY = 0;
    }

    /**
     * Ungrabs the mouse cursor so it can be moved and set it to the center of the screen
     */
    public void ungrabMouseCursor() {
        Mouse.setGrabbed(false);
        Minecraft.getMinecraft().addScheduledTask(() -> Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2));
    }

    public void mouseXYChange() {
        int deltaX = RawMouseController.getDeltaX();
        int deltaY = RawMouseController.getDeltaY();
        if (Managers.moduleManager.boostModules.get("RawMouseInput").isEnabled()) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        } else {
            this.deltaX = Mouse.getDX();
            this.deltaY = Mouse.getDY();
        }
    }
}
