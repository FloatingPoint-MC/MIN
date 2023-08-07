package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.IntegerValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-07 16:17:24
 */
public class MotionBlur extends RenderModule {
    public static final IntegerValue multiplier = new IntegerValue(0, 10, 1, 2);

    public MotionBlur() {
        addValues(
                new Pair<>("Multiplier", multiplier)
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
