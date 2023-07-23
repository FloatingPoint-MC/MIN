package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.utils.client.Pair;
import org.lwjgl.opengl.Display;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-22 17:41:55
 */
public class FreeLook extends RenderModule {
    private final OptionValue invertY = new OptionValue(false);
    public static boolean perspectiveToggled;
    private static float cameraYaw;
    private static float cameraPitch;
    private static int previousPerspective;

    public FreeLook() {
        addValues(
                new Pair<>("InvertY", invertY)
        );
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onRender3D() {
        if (!perspectiveToggled) {
            if (mc.gameSettings.keyBindFreeLook.isKeyDown()) {
                perspectiveToggled = true;
                cameraYaw = mc.player.rotationYaw;
                cameraPitch = mc.player.rotationPitch;
                previousPerspective = mc.gameSettings.thirdPersonView;
                mc.gameSettings.thirdPersonView = 1;
            }
        } else if (!mc.gameSettings.keyBindFreeLook.isKeyDown()) {
            perspectiveToggled = false;
            mc.gameSettings.thirdPersonView = previousPerspective;
        }
    }

    public float getCameraYaw() {
        if (perspectiveToggled) {
            return cameraYaw;
        } else {
            assert mc.getRenderViewEntity() != null;
            return mc.getRenderViewEntity().rotationYaw;
        }
    }

    public float getCameraPitch() {
        if (perspectiveToggled) {
            return cameraPitch;
        } else {
            assert mc.getRenderViewEntity() != null;
            return mc.getRenderViewEntity().rotationPitch;
        }
    }

    public float getCameraPrevYaw() {
        if (perspectiveToggled) {
            return cameraYaw;
        } else {
            assert mc.getRenderViewEntity() != null;
            return mc.getRenderViewEntity().prevRotationYaw;
        }
    }

    public float getCameraPrevPitch() {
        if (perspectiveToggled) {
            return cameraPitch;
        } else {
            assert mc.getRenderViewEntity() != null;
            return mc.getRenderViewEntity().prevRotationPitch;
        }
    }

    public boolean overrideMouse() {
        if (mc.inGameHasFocus && Display.isActive()) {
            if (!perspectiveToggled) {
                return true;
            }
            mc.mouseHelper.mouseXYChange();
            float f1 = mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f2 = f1 * f1 * f1 * 8.0f;
            float f3 = mc.mouseHelper.deltaX * f2;
            float f4 = mc.mouseHelper.deltaY * f2;
            if (invertY.getValue()) {
                f4 *= -1.0F;
            }
            cameraYaw += f3 * 0.15f;
            cameraPitch += f4 * 0.15f;
            if (cameraPitch > 90.0f) {
                cameraPitch = 90.0f;
            }
            if (cameraPitch < -90.0f) {
                cameraPitch = -90.0f;
            }
        }
        return false;
    }
}
