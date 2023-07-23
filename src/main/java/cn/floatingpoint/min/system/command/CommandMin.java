package cn.floatingpoint.min.system.command;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.ui.hyt.party.VexViewButton;
import cn.floatingpoint.min.system.ui.hyt.party.GuiHandleInvitation;
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
                        UUID uuid;
                        try {
                            uuid = UUID.fromString(args[2]);
                        } catch (IllegalArgumentException e) {
                            EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByName(args[2]);
                            if (player == null) return false;
                            uuid = player.getUniqueID();
                        }
                        Managers.clientManager.cheaterUuids.put(uuid, new CheatDetection());
                        EntityPlayer player = Minecraft.getMinecraft().world.getPlayerEntityByUUID(uuid);
                        if (player != null) {
                            ChatUtil.printToChatWithPrefix(Managers.i18NManager.getTranslation("module.implement.CheaterDetector.delete")
                                    .replace("{0}", player.getName()));
                        }
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
            } else if (args[0].equalsIgnoreCase("hyt")) {
                if (args.length != 1) {
                    if (args[1].equalsIgnoreCase("party")) {
                        if (args.length != 2) {
                            if (args[2].equalsIgnoreCase("handle")) {
                                if (args.length == 5) {
                                    try {
                                        String accept = args[3];
                                        String deny = args[4];
                                        Minecraft.getMinecraft().displayGuiScreen(new GuiHandleInvitation(new VexViewButton("同意", accept), new VexViewButton("拒绝", deny)));
                                    } catch (Exception ignore) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
