package cn.floatingpoint.min;

import java.util.HashSet;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-22 15:18:57
 */
public class AsyncLoopThread extends Thread {
    public final static HashSet<Runnable> runnableSet = new HashSet<>();

    @Override
    public void run() {
        while (true) {
            if (runnableSet.isEmpty()) {
                try {
                    Thread.sleep(1000L);
                    continue;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            Runnable runnable = runnableSet.stream().findAny().get();
            runnableSet.remove(runnable);
            runnable.run();
        }
    }
}
