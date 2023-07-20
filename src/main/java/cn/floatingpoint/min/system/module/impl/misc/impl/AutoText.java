package cn.floatingpoint.min.system.module.impl.misc.impl;

import cn.floatingpoint.min.system.module.impl.misc.MiscModule;
import cn.floatingpoint.min.system.module.value.impl.ModeValue;
import cn.floatingpoint.min.system.module.value.impl.TextValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-19 22:33:54
 */
public class AutoText extends MiscModule {
    public static final ModeValue whenToSend = new ModeValue(new String[]{"Win", "End"}, "Win");
    private final TextValue text = new TextValue("GG");
    public static boolean timeToSendGG = false;

    public AutoText() {
        addValues(
                new Pair<>("WhenToSend", whenToSend),
                new Pair<>("Text", text)
        );
    }

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
