package cn.floatingpoint.min;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-03 15:25:13
 */
public class DaemonThread extends Thread {
    public DaemonThread() {
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        try {
            sleep(2147483647L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
