package cn.floatingpoint.min.system.command;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.utils.client.ChatUtil;
import cn.floatingpoint.min.utils.client.CheatDetection;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-20 11:55:51
 */
public class CommandMin {
    public static boolean execute(String[] args) {
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("cheaters")) {
                if (args.length == 3) {
                    if (args[1].equalsIgnoreCase("remove")) {
                        UUID uuid = UUID.fromString(args[2]);
                        Managers.clientManager.cheaterUuids.put(uuid, new CheatDetection());
                        ChatUtil.printToChatWithPrefix(Managers.i18NManager.getTranslation("module.implement.CheaterDetector.delete"));
                        return true;
                    } else if (args[1].equalsIgnoreCase("add")) {
                        UUID uuid;
                        try {
                            uuid = UUID.fromString(args[2]);
                        } catch (IllegalArgumentException e) {
                            EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByName(args[2]);
                            if (player == null) return false;
                            uuid = player.getUniqueID();
                        }
                        CheatDetection cheatDetection = new CheatDetection();
                        cheatDetection.hacks = true;
                        Managers.clientManager.cheaterUuids.put(uuid, cheatDetection);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
