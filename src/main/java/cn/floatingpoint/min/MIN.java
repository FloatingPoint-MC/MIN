package cn.floatingpoint.min;

import cn.floatingpoint.min.management.Managers;

public class MIN {
    public static void init() {
        Managers.init();
    }

    public static void stop() {
        Managers.fileManager.saveConfig();
    }
}
