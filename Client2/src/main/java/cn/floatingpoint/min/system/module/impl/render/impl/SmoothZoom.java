package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.DecimalValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-22 17:41:10
 */
public class SmoothZoom extends RenderModule {
    public final static DecimalValue speed = new DecimalValue(0.1, 5.0, 0.1, 2.0);

    public SmoothZoom() {
        addValues(
                new Pair<>("Speed", speed)
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
