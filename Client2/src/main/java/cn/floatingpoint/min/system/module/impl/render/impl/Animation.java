package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.IntegerValue;
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
    public static OptionValue oldBackwardAnimation = new OptionValue(false);
    public static OptionValue oldHeartAnimation = new OptionValue(false);
    //public static OptionValue sneakAnimation = new OptionValue(false);
    public static IntegerValue blockX = new IntegerValue(-200, 200, 1, 0);
    public static IntegerValue blockY = new IntegerValue(-200, 200, 1, 0);
    public static ModeValue blockSwingMode = new ModeValue(new String[]{"None", "Old", "AimBlock", "Whenever"}, "None");
    public static ModeValue bowSwingMode = new ModeValue(new String[]{"None", "Old", "AimBlock", "Whenever"}, "None");
    public static ModeValue foodSwingMode = new ModeValue(new String[]{"None", "Old", "AimBlock", "Whenever"}, "None");
    public static ModeValue foodSwingAnimationMode = new ModeValue(new String[]{"MC", "MIN"}, "MC", () -> !foodSwingMode.isCurrentMode("None"));

    public Animation() {
        addValues(
                new Pair<>("OldArmorAnimation", oldArmorAnimation),
                new Pair<>("OldBackwardAnimation", oldBackwardAnimation),
                new Pair<>("OldRodAnimation", oldRodAnimation),
                new Pair<>("OldSwingAnimation", oldSwingAnimation),
                new Pair<>("OldHeartAnimation", oldHeartAnimation),
                //new Pair<>("SneakAnimation", sneakAnimation),
                new Pair<>("BlockX", blockX),
                new Pair<>("BlockY", blockY),
                new Pair<>("BlockSwingMode", blockSwingMode),
                new Pair<>("BowSwingMode", bowSwingMode),
                new Pair<>("FoodSwingMode", foodSwingMode),
                new Pair<>("FoodSwingAnimationMode", foodSwingAnimationMode)
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
