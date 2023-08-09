package cn.floatingpoint.min.system.module.impl.misc.impl;

import cn.floatingpoint.min.system.module.impl.misc.MiscModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-10 00:16:06
 */
public class AutoBan extends MiscModule {
    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player != mc.player) {
                if (mc.player.getDistance(player) >= 3.0f) {
                    for (int i = 0; i < 20; i++) {
                        mc.player.connection.sendPacket(new CPacketUseEntity(player));
                    }
                }
            }
        }
    }
}
