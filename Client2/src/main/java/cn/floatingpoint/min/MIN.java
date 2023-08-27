package cn.floatingpoint.min;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.ui.clickgui.GuiError;
import net.minecraft.client.Minecraft;

public class MIN {
    public static final String VERSION = "2.3";
    private static final AsyncLoopThread asyncLoopThread = new AsyncLoopThread();

    public static void init() {
        Managers.init();
        asyncLoopThread.setName("Asynchronous Loop Thread");
        asyncLoopThread.setDaemon(true);
        asyncLoopThread.start();
    }

    public static void stop() {
        Managers.fileManager.saveConfig();
    }

    public static void runAsync(Runnable runnable) {
        AsyncLoopThread.runnableSet.add(runnable);
    }

    public static void checkIfAsyncThreadAlive() {
        if (!asyncLoopThread.isAlive() || asyncLoopThread.isInterrupted() || asyncLoopThread.getState().equals(Thread.State.TERMINATED)) {
            Minecraft.getMinecraft().world.sendQuittingDisconnectingPacket();
            Minecraft.getMinecraft().loadWorld(null);
            Minecraft.getMinecraft().displayGuiScreen(new GuiError());
        }
    }
}
