package cn.floatingpoint.min.system.module.impl.misc.impl;

import cn.floatingpoint.min.system.module.impl.misc.MiscModule;
import cn.floatingpoint.min.system.module.value.impl.IntegerValue;
import cn.floatingpoint.min.system.module.value.impl.ModeValue;
import cn.floatingpoint.min.system.module.value.impl.TextValue;
import cn.floatingpoint.min.utils.client.Pair;
import cn.floatingpoint.min.utils.math.TimeHelper;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-19 22:33:54
 */
public class AutoText extends MiscModule {
    public static final ModeValue whenToSend = new ModeValue(new String[]{"Win", "End"}, "Win");
    private final IntegerValue delay = new IntegerValue(0, 5000, 1000, 1000);
    private final TextValue text = new TextValue("GG");
    private final TimeHelper timer = new TimeHelper();
    public static boolean timeToSendGG = false;
    private boolean send;

    public AutoText() {
        addValues(
                new Pair<>("WhenToSend", whenToSend),
                new Pair<>("Delay", delay),
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
            timer.reset();
            send = true;
            timeToSendGG = false;
        }
        if (send && timer.isDelayComplete(delay.getValue())) {
            mc.player.sendChatMessage(text.getValue());
            send = false;
        }
    }
}
