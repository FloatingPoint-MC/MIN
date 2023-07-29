package cn.floatingpoint.min.system.module.impl.render;

import cn.floatingpoint.min.system.module.Category;
import cn.floatingpoint.min.system.module.Module;

public abstract class RenderModule extends Module {
    public RenderModule() {
        super(Category.Render);
    }

    public abstract void onRender3D();
}
