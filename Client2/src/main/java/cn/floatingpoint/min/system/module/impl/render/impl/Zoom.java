package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.DecimalValue;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-22 17:41:10
 */
public class Zoom extends RenderModule {
    public final static OptionValue smoothZoom = new OptionValue(false);
    public final static DecimalValue speed = new DecimalValue(0.1, 10.0, 0.1, 4.0, smoothZoom::getValue);
    public final static OptionValue filmViewToggle = new OptionValue(true);

    public Zoom() {
        addValues(
                new Pair<>("SmoothZoom", smoothZoom),
                new Pair<>("Speed", speed),
                new Pair<>("FilmViewToggle", filmViewToggle)
        );
        this.setCanBeEnabled(false);
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
