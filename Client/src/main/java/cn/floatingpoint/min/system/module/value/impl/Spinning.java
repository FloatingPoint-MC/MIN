package cn.floatingpoint.min.system.module.value.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.utils.client.Pair;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-26 18:42:15
 */
public class Spinning extends RenderModule {
    private static final OptionValue everyone = new OptionValue(false);
    public static final IntegerValue speed = new IntegerValue(1, 360, 1, 10);
    public static final ModeValue direction = new ModeValue(new String[]{"P", "N"}, "P");
    public static float current = 0.0f;
    private static Spinning instance;

    public Spinning() {
        instance = this;
        addValues(
                new Pair<>("Everyone", everyone),
                new Pair<>("Speed", speed),
                new Pair<>("Direction", direction)
        );
    }

    public static float getCurrentPitch(float pitch) {
        return instance.isEnabled() ? 90.0F : pitch;
    }

    @Override
    public void onRender3D() {
        if (everyone.getValue()) {
            for (EntityPlayer player : mc.world.playerEntities) {
                player.renderYawOffset = player.rotationYaw + current;
            }
        } else {
            mc.player.renderYawOffset = mc.player.rotationYaw + current;
        }
    }

    public static float getCurrent(EntityLivingBase player) {
        return instance.isEnabled() && player instanceof EntityPlayer && (player instanceof EntityPlayerSP || everyone.getValue()) ? current : 0;
    }

    public static boolean other(EntityPlayer entityPlayer) {
        return instance.isEnabled() && (entityPlayer instanceof EntityPlayerSP || everyone.getValue());
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
