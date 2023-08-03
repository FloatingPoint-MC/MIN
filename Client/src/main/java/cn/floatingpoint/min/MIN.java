package cn.floatingpoint.min;

import cn.floatingpoint.min.management.Managers;

public class MIN {
    public static final String VERSION = "1.2.1";
    private static final AsyncLoopThread asyncLoopThread = new AsyncLoopThread();

    public static void init() {
        Managers.init();
        asyncLoopThread.start();
        new DaemonThread();
    }

    public static void stop() {
        Managers.fileManager.saveConfig();
    }

    public static void runAsync(Runnable runnable) {
        AsyncLoopThread.runnableSet.add(runnable);
    }
}
