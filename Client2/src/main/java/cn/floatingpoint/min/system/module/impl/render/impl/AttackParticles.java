package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-11 21:30:33
 */
public class AttackParticles extends RenderModule {
    public static final OptionValue moreCritParticle = new OptionValue(false);
    public static final OptionValue moreSharpnessParticle = new OptionValue(false);

    public AttackParticles() {
        addValues(
                new Pair<>("MoreCritParticle", moreCritParticle),
                new Pair<>("MoreSharpnessParticle", moreSharpnessParticle)
        );
        setCanBeEnabled(false);
    }

    @Override
    public void onRender3D() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
