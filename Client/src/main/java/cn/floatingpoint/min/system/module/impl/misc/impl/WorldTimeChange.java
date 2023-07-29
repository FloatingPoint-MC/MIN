package cn.floatingpoint.min.system.module.impl.misc.impl;

import cn.floatingpoint.min.system.module.impl.misc.MiscModule;
import cn.floatingpoint.min.system.module.value.impl.IntegerValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-20 20:13:58
 */
public class WorldTimeChange extends MiscModule {
    public static IntegerValue time = new IntegerValue(0, 23999, 1, 18000);

    public WorldTimeChange() {
        addValues(
                new Pair<>("Time", time)
        );
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {

    }
}
