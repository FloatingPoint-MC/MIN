package cn.floatingpoint.min.system.module.impl.misc;

import cn.floatingpoint.min.system.module.Category;
import cn.floatingpoint.min.system.module.Module;

public abstract class MiscModule extends Module {
    public MiscModule() {
        super(Category.Misc);
    }

    public abstract void tick();
}
