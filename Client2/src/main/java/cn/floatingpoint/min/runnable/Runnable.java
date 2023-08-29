package cn.floatingpoint.min.runnable;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-29 18:44:54
 */
public interface Runnable {
    void run();

    default byte priority() {
        return 1;
    }
}
