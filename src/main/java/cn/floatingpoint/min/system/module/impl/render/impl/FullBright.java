package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;

public class FullBright extends RenderModule {
    private float oldGamma;

    @Override
    public void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 15.0f;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
    }

    @Override
    public void onRender3D() {

    }
}
