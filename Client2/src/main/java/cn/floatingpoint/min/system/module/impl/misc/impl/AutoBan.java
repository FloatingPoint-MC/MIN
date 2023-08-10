package cn.floatingpoint.min.system.module.impl.misc.impl;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.misc.MiscModule;
import cn.floatingpoint.min.utils.client.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-10 00:16:06
 */
public class AutoBan extends MiscModule {
    private boolean shouldExecute = true;

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        if (Managers.clientManager.lock) {
            if (shouldExecute) {
                shouldExecute = false;
                ChatUtil.printToChatWithPrefix("您当前在大厅，请进入游戏取得封禁。");
            }
            return;
        } else {
            shouldExecute = true;
        }
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player != mc.player) {
                if (mc.player.getDistance(player) >= 3.0f) {
                    mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                    for (int i = 0; i < 3; i++) {
                        mc.player.connection.sendPacket(new CPacketUseEntity(player));
                    }
                }
            }
        }
    }
}
