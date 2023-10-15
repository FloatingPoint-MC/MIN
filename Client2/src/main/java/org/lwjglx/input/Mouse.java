package org.lwjglx.input;

import org.lwjgl.glfw.GLFW;
import org.lwjglx.LWJGLConfig;
import org.lwjglx.LWJGLException;
import org.lwjglx.opengl.Display;

public class Mouse {
    private static boolean grabbed = false;

    private static int lastX = 0;
    private static int lastY = 0;
    private static int latestX = 0;
    private static int latestY = 0;

    private static int x = 0;
    private static int y = 0;

    private static final EventQueue queue = new EventQueue(128);

    private static final int[] buttonEvents = new int[queue.getMaxEvents()];
    private static final boolean[] buttonEventStates = new boolean[queue.getMaxEvents()];
    private static final int[] xEvents = new int[queue.getMaxEvents()];
    private static final int[] yEvents = new int[queue.getMaxEvents()];
    private static final int[] wheelEvents = new int[queue.getMaxEvents()];

    private static final boolean clipPositionToDisplay = true;
    private static int ignoreNextDelta = 0;
    private static int ignoreNextMove = 0;
    private static int dWheel;

    public static void addMoveEvent(double mouseX, double mouseY) {
        if (ignoreNextMove > 0) {
            ignoreNextMove--;
            return;
        }
        latestX = (int) mouseX;
        latestY = Display.getHeight() - (int) mouseY;
        if (ignoreNextDelta > 0) {
            ignoreNextDelta--;
            x = latestX;
            y = latestY;
            lastX = latestX;
            lastY = latestY;
        }

        xEvents[queue.getNextPos()] = latestX;
        yEvents[queue.getNextPos()] = latestY;

        wheelEvents[queue.getNextPos()] = 0;

        buttonEvents[queue.getNextPos()] = -1;
        buttonEventStates[queue.getNextPos()] = false;

        queue.add();
    }

    public static void addButtonEvent(int button, boolean pressed) {

        xEvents[queue.getNextPos()] = latestX;
        yEvents[queue.getNextPos()] = latestY;

        wheelEvents[queue.getNextPos()] = 0;

        buttonEvents[queue.getNextPos()] = button;
        buttonEventStates[queue.getNextPos()] = pressed;

        queue.add();
    }

    static double fractionalWheelPosition = 0.0;
    // Used for our config screen for ease of access
    public static double totalScrollAmount = 0.0;

    public static void addWheelEvent(double dwheel) {
        if (LWJGLConfig.INPUT_INVERT_WHEEL) {
            dwheel = -dwheel;
        }
        dwheel *= LWJGLConfig.INPUT_SCROLL_SPEED;
        dWheel += (int) dwheel;
        final int lastWheel = (int) fractionalWheelPosition;
        fractionalWheelPosition += dwheel;
        totalScrollAmount += dwheel;
        final int newWheel = (int) fractionalWheelPosition;
        if (newWheel != lastWheel) {

            xEvents[queue.getNextPos()] = latestX;
            yEvents[queue.getNextPos()] = latestY;

            wheelEvents[queue.getNextPos()] = newWheel - lastWheel;

            buttonEvents[queue.getNextPos()] = -1;
            buttonEventStates[queue.getNextPos()] = false;

            queue.add();
        }
        fractionalWheelPosition = fractionalWheelPosition % 1;
    }

    public static void poll() {
        lastX = x;
        lastY = y;

        if (!grabbed && clipPositionToDisplay) {
            if (latestX < 0) latestX = 0;
            if (latestY < 0) latestY = 0;
            if (latestX > Display.getWidth() - 1) latestX = Display.getWidth() - 1;
            if (latestY > Display.getHeight() - 1) latestY = Display.getHeight() - 1;
        }

        x = latestX;
        y = latestY;
    }

    public static void create() throws LWJGLException {
    }

    public static boolean isCreated() {
        return Display.isCreated();
    }

    public static void setGrabbed(boolean grab) {
        if (grabbed == grab) {
            return;
        }
        grabbed = grab;
        GLFW.glfwSetInputMode(
                Display.getWindow(),
                GLFW.GLFW_CURSOR,
                grab ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
        if (!grab) {
            setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
        } else {
            ignoreNextMove++;
        }
        ignoreNextDelta++;
    }

    public static boolean isButtonDown(int button) {
        return GLFW.glfwGetMouseButton(Display.getWindow(), button) == GLFW.GLFW_PRESS;
    }

    public static boolean next() {
        return queue.next();
    }

    public static int getEventX() {
        return xEvents[queue.getCurrentPos()];
    }

    public static int getEventY() {
        return yEvents[queue.getCurrentPos()];
    }

    public static int getEventButton() {
        return buttonEvents[queue.getCurrentPos()];
    }

    public static boolean getEventButtonState() {
        return buttonEventStates[queue.getCurrentPos()];
    }

    public static int getEventDWheel() {
        return wheelEvents[queue.getCurrentPos()];
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static int getDX() {
        return (ignoreNextDelta > 0) ? 0 : (x - lastX);
    }

    public static int getDY() {
        return (ignoreNextDelta > 0) ? 0 : (y - lastY);
    }

    public static int getDWheel() {
        int result = dWheel;
        dWheel = 0;
        return result;
    }

    public static void setCursorPosition(int new_x, int new_y) {
        if (grabbed) {
            return;
        }
        GLFW.glfwSetCursorPos(Display.getWindow(), new_x, new_y);
        addMoveEvent(new_x, new_y);
    }

    public static void destroy() {
    }

    public static boolean isInsideWindow() {
        return Display.isVisible();
    }
}
