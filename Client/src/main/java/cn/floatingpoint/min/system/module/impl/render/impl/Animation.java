package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.ModeValue;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-19 17:38:38
 */
public class Animation extends RenderModule {
    public static OptionValue oldArmorAnimation = new OptionValue(false);
    public static OptionValue oldRodAnimation = new OptionValue(false);
    public static OptionValue oldSwingAnimation = new OptionValue(false);
    public static ModeValue foodSwingMode = new ModeValue(new String[]{"None", "Old", "AimBlock", "Whenever"}, "None");
    public static ModeValue bowSwingMode = new ModeValue(new String[]{"None", "Old", "AimBlock", "Whenever"}, "None");
    public static ModeValue blockSwingMode = new ModeValue(new String[]{"None", "Old", "AimBlock", "Whenever"}, "None");

    public Animation() {
        addValues(
                new Pair<>("OldArmorAnimation", oldArmorAnimation),
                new Pair<>("OldRodAnimation", oldRodAnimation),
                new Pair<>("OldSwingAnimation", oldSwingAnimation),
                new Pair<>("BlockSwingMode", blockSwingMode),
                new Pair<>("BowSwingMode", bowSwingMode),
                new Pair<>("FoodSwingMode", foodSwingMode));
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
