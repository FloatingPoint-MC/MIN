package cn.floatingpoint.min.system.module.impl.boost;

import cn.floatingpoint.min.system.module.Category;
import cn.floatingpoint.min.system.module.Module;

public abstract class BoostModule extends Module {
    public BoostModule() {
        super(Category.Boost);
    }

    public abstract void tick();
}
