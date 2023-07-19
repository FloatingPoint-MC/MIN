package cn.floatingpoint.min.system.module.impl.misc.impl;

import cn.floatingpoint.min.system.module.impl.misc.MiscModule;
import cn.floatingpoint.min.system.module.value.impl.TextValue;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-19 22:33:54
 */
public class AutoText extends MiscModule {
    private final TextValue text = new TextValue("GG");
    public static boolean timeToSendGG = false;

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        if (timeToSendGG) {
            mc.player.sendChatMessage(text.getValue());
            timeToSendGG = false;
        }
    }
}
