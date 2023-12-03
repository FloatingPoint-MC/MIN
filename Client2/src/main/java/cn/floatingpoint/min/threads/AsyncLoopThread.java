package cn.floatingpoint.min.threads;

import cn.floatingpoint.min.runnable.Runnable;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Optional;

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
                    // HIGH Priority
                    Optional<Runnable> high = runnableSet.stream().filter(runnable -> runnable.priority() == 0).findAny();
                    if (high.isPresent()) {
                        Runnable runnable = high.get();
                        runnableSet.remove(runnable);
                        logger.info("Running high-priority task: #" + Integer.toHexString(runnable.hashCode()));
                        runnable.run();
                        continue;
                    }
                    Runnable runnable = runnableSet.stream().findAny().get();
                    runnableSet.remove(runnable);
                    logger.info("Running task: #" + Integer.toHexString(runnable.hashCode()));
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        logger.error("Unhandled exception(That's not your fault, contact the author for help): ", e);
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }
}
