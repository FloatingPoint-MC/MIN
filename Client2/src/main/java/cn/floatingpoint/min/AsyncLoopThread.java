package cn.floatingpoint.min;

import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-22 15:18:57
 */
public class AsyncLoopThread extends Thread {
    public final static HashSet<Runnable> runnableSet = new HashSet<>();
    private final Logger logger = LogManager.getLogger();

    @Override
    @SuppressWarnings("all")
    public void run() {
        while (Minecraft.getMinecraft().running) {
            try {
                synchronized (runnableSet) {
                    if (runnableSet.isEmpty()) {
                        logger.info("There is nothing left to be run asyncly, thread sleeped.");
                        Thread.sleep(1000L);
                        continue;
                    }
                    Runnable runnable = runnableSet.stream().findAny().get();
                    runnableSet.remove(runnable);
                    logger.info("Running task: #" + Integer.toHexString(runnable.hashCode()));
                    runnable.run();
                }
            } catch (Exception ignored) {
            }
        }
    }
}
