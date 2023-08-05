package cn.floatingpoint.min.system.module.impl.misc.impl;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.misc.MiscModule;
import cn.floatingpoint.min.system.module.value.impl.ModeValue;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.utils.client.Pair;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-22 15:07:57
 */
public class RankDisplay extends MiscModule {
    public static ModeValue game = new ModeValue(new String[]{"bw", "bw-xp", "sw", "kit"}, "bw") {
        @Override
        public void setValue(String value) {
            Managers.clientManager.ranks.clear();
            Managers.clientManager.cooldown.clear();
            super.setValue(value);
        }
    };
    public static final OptionValue self = new OptionValue(true);

    public RankDisplay() {
        addValues(
                new Pair<>("Game", game),
                new Pair<>("Self", self)
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
        for (EntityPlayer player : mc.world.playerEntities) {
            Managers.clientManager.getRank(player.getName());
        }
    }
}
