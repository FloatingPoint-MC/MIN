package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-28 14:56:04
 */
public class AttackIndicator extends RenderModule {
    public static final OptionValue showInThirdPerson = new OptionValue(false);

    public AttackIndicator() {
        addValues(
                new Pair<>("ShowInThirdPerson", showInThirdPerson)
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
