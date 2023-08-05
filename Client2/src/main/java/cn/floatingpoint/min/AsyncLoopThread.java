package cn.floatingpoint.min;

import net.minecraft.client.Minecraft;

import java.util.HashSet;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-22 15:18:57
 */
public class AsyncLoopThread extends Thread {
    public final static HashSet<Runnable> runnableSet = new HashSet<>();

    @Override
    @SuppressWarnings("all")
    public void run() {
        while (Minecraft.getMinecraft().running) {
            try {
                synchronized (runnableSet) {
                    if (runnableSet.isEmpty()) {
                        Thread.sleep(1000L);
                        continue;
                    }
                    Runnable runnable = runnableSet.stream().findAny().get();
                    runnableSet.remove(runnable);
                    runnable.run();
                }
            } catch (Exception ignored) {
            }
        }
    }
}
