package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.ModeValue;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-29 15:46:37
 */
public class ChatBar extends RenderModule {
    public static final ModeValue font = new ModeValue(new String[]{"Minecraft", "SourceSans"}, "Minecraft");
    public static final OptionValue shadow = new OptionValue(true);
    public static final OptionValue background = new OptionValue(true);

    public ChatBar() {
        addValues(
                new Pair<>("Font", font),
                new Pair<>("Shadow", shadow),
                new Pair<>("Background", background)
        );
        setCanBeEnabled(false);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onRender3D() {

    }
}
