package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.ui.clickgui.ClickUI;

public class ClickGUI extends RenderModule {
    public ClickGUI() {
        if (this.getKey() == 0) {
            this.setKey(54);
        }
        setCanBeEnabled(false);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void requestToggle() {
        mc.displayGuiScreen(new ClickUI());
    }

    @Override
    public void onRender3D() {

    }
}
