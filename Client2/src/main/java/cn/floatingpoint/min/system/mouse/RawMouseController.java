package cn.floatingpoint.min.system.mouse;

public class RawMouseController {
    private static int deltaX = 0, deltaY = 0;

    public static void setDeltaX(int x) {
        deltaX += x;
    }

    public static void setDeltaY(int y) {
        deltaY += y;
    }

    public static int getDeltaX() {
        int dx = deltaX;
        deltaX = 0;
        return dx;
    }

    public static int getDeltaY() {
        int dy = deltaY;
        deltaY = 0;
        return -dy;
    }
}
