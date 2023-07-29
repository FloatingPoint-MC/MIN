package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.IntegerValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-20 11:37:49
 */
public class MoreParticles extends RenderModule {
    public static final IntegerValue critParticles = new IntegerValue(0, 10, 1, 5);
    public static final IntegerValue sharpParticles = new IntegerValue(0, 10, 1, 5);
    public static final IntegerValue bloodParticles = new IntegerValue(0, 10, 1, 5);

    public MoreParticles() {
        addValues(
                new Pair<>("CritParticles", critParticles),
                new Pair<>("SharpParticles", sharpParticles),
                new Pair<>("BloodParticles", bloodParticles)
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
