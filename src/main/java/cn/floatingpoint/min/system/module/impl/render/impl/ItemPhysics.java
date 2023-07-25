package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-20 17:38:07
 */
public class ItemPhysics extends RenderModule {
    public static final OptionValue twoD = new OptionValue(false);

    public ItemPhysics() {
        addValues(
                new Pair<>("2D", twoD)
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
