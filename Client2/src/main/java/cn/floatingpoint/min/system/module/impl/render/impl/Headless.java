package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.ModeValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-17 20:37:41
 */
public class Headless extends RenderModule {
    public static ModeValue who = new ModeValue(new String[]{"Y", "O", "A"}, "Y");

    public Headless() {
        addValues(
                new Pair<>("Who", who)
        );
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
