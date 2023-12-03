package cn.floatingpoint.min.utils.math;

public class FunctionUtil {
    public static float increasedSpeed(float current, float start, float target, float speed) {
        float k = speed / (target - start);
        return current + (k * (current - start) + speed) * (start > target ? -1 : 1);
    }

    public static float decreasedSpeed(float current, float start, float target, float speed) {
        float k = speed / (start - target);
        return current + (k * (current - start) + speed) * (start > target ? -1 : 1);
    }

    public static int decreasedSpeed(int current, int start, int target, float speed) {
        float k = speed / (start - target);
        return current + ((int) (k * (current - start)) + (int) speed) * (start > target ? -1 : 1);
    }

    public static int smooth(int current, int target, float speed) {
        float add = (Math.abs(current - target)) * speed / 60.0f;
        float currentState = current;
        if (currentState < target) {
            if (currentState + add < target) {
                currentState += add;
            } else {
                currentState = target;
            }
        } else if (currentState - add > target) {
            currentState -= add;
        } else {
            currentState = target;
        }
        return (int) currentState;
    }
}
