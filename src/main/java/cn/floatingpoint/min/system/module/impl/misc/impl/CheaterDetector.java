package cn.floatingpoint.min.system.module.impl.misc.impl;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.misc.MiscModule;
import cn.floatingpoint.min.system.module.value.impl.IntegerValue;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.system.module.value.impl.TextValue;
import cn.floatingpoint.min.utils.client.ChatUtil;
import cn.floatingpoint.min.utils.client.CheatDetection;
import cn.floatingpoint.min.utils.client.Pair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import java.util.*;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-18 11:43:08
 */
public class CheaterDetector extends MiscModule {
    public static final OptionValue printVLToChat = new OptionValue(false);
    public static final OptionValue verbose = new OptionValue(false, printVLToChat::getValue);
    public static final OptionValue autoTaunt = new OptionValue(false);
    public static final TextValue autoTauntPrefix = new TextValue("@", autoTaunt::getValue);
    private final IntegerValue delay = new IntegerValue(0, 5000, 1000, 1000, autoTaunt::getValue);
    public static final OptionValue autoMark = new OptionValue(false);
    public static final OptionValue reachCheck = new OptionValue(true);
    public static final IntegerValue reachMaxVL = new IntegerValue(1, 30, 1, 15, reachCheck::getValue);
    private final OptionValue sprintCheck = new OptionValue(true);
    private final IntegerValue sprintMaxVL = new IntegerValue(1, 30, 1, 1, sprintCheck::getValue);
    private final OptionValue noSlowCheck = new OptionValue(true);
    private final IntegerValue noSlowMaxVL = new IntegerValue(1, 30, 1, 10, noSlowCheck::getValue);
    private static final HashMap<String, Long> tauntMap = new HashMap<>();

    public CheaterDetector() {
        addValues(
                new Pair<>("PrintVLToChat", printVLToChat),
                new Pair<>("Verbose", verbose),
                new Pair<>("AutoTaunt", autoTaunt),
                new Pair<>("AutoTauntPrefix", autoTauntPrefix),
                new Pair<>("Delay", delay),
                new Pair<>("AutoMark", autoMark),
                new Pair<>("ReachCheck", reachCheck),
                new Pair<>("ReachMaxVL", reachMaxVL),
                new Pair<>("SprintCheck", sprintCheck),
                new Pair<>("SprintMaxVL", sprintMaxVL),
                new Pair<>("NoSlowCheck", noSlowCheck),
                new Pair<>("NoSlowMaxVL", noSlowMaxVL)
        );
    }

    public static void taunt(EntityPlayer player) {
        tauntMap.put(player.getName(), System.currentTimeMillis());
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
            if (player != null && player != mc.player) {
                Managers.clientManager.cheaterUuids.putIfAbsent(player.getUniqueID(), new CheatDetection());
                CheatDetection detection = Managers.clientManager.cheaterUuids.get(player.getUniqueID());
                if (detection.hacks) continue;
                if (sprintCheck.getValue()) {
                    if (player.getMoveSpeed() >= player.getPredictSpeed() * 0.93 && (player.moveForward < 0.0F || player.moveForward == 0.0F && player.moveStrafing != 0.0F)) {
                        detection.sprintPercentage += 20;
                        if (detection.sprintPercentage >= 100) {
                            detection.sprint++;
                            if (detection.sprint == sprintMaxVL.getValue()) {
                                markCheating(player, detection);
                            }
                            detection.sprintPercentage = 0;
                            if (printVLToChat.getValue()) {
                                ChatUtil.printToChatWithPrefix(Managers.i18NManager.getTranslation("module.implement.CheaterDetector.vlNotice")
                                        .replace("{0}", player.getName())
                                        .replace("{1}", Managers.i18NManager.getTranslation("module.implement.CheaterDetector.SprintCheck"))
                                        .replace("{2}", String.valueOf(detection.sprint)));
                            }
                        } else if (verbose.getValue()) {
                            ChatUtil.printToChatWithPrefix(Managers.i18NManager.getTranslation("module.implement.CheaterDetector.verboseNotice")
                                    .replace("{0}", player.getName())
                                    .replace("{1}", Managers.i18NManager.getTranslation("module.implement.CheaterDetector.SprintCheck"))
                                    .replace("{2}", String.valueOf(detection.sprintPercentage)));
                        }
                    }
                }
                if (noSlowCheck.getValue()) {
                    if (player.isHandActive() && player.onGround && player.hurtResistantTime == 0 && !player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(1))) && player.getMoveSpeed() >= player.getPredictSpeed() * 0.9 && !player.getActiveItemStack().getDisplayName().toLowerCase().contains("bow")) {
                        detection.noSlowPercentage += 20;
                        if (detection.noSlowPercentage >= 100) {
                            detection.noSlow++;
                            if (detection.noSlow == noSlowMaxVL.getValue()) {
                                markCheating(player, detection);
                            }
                            detection.noSlowPercentage = 0;
                            if (printVLToChat.getValue()) {
                                ChatUtil.printToChatWithPrefix(Managers.i18NManager.getTranslation("module.implement.CheaterDetector.vlNotice")
                                        .replace("{0}", player.getName())
                                        .replace("{1}", Managers.i18NManager.getTranslation("module.implement.CheaterDetector.NoSlowCheck"))
                                        .replace("{2}", String.valueOf(detection.noSlow)));
                            }
                        } else if (verbose.getValue()) {
                            ChatUtil.printToChatWithPrefix(Managers.i18NManager.getTranslation("module.implement.CheaterDetector.verboseNotice")
                                    .replace("{0}", player.getName())
                                    .replace("{1}", Managers.i18NManager.getTranslation("module.implement.CheaterDetector.NoSlowCheck"))
                                    .replace("{2}", String.valueOf(detection.noSlowPercentage)));
                        }
                    }
                }
            }
        }
        HashSet<String> sent = new HashSet<>();
        tauntMap.forEach((s, aLong) -> {
            if (System.currentTimeMillis() - aLong >= delay.getValue()) {
                ArrayList<String> messages = Managers.clientManager.sarcasticMessages;
                mc.player.sendChatMessage(messages.get(new Random().nextInt(messages.size())).replace("{0}", s));
                sent.add(s);
            }
        });
        sent.forEach(tauntMap::remove);
    }

    public static void markCheating(EntityPlayer player, CheatDetection detection) {
        detection.hacks = true;
        ChatUtil.printToChatWithPrefix(Managers.i18NManager.getTranslation("module.implement.CheaterDetector.confirmHack")
                .replace("{0}", player.getName()));
        ITextComponent textComponent = new TextComponentString(Managers.i18NManager.getTranslation("module.implement.CheaterDetector.confirmDelete"));
        textComponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(Managers.i18NManager.getTranslation("module.implement.CheaterDetector.clickDelete"))));
        textComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/min cheaters remove " + player.getUniqueID()));
        ChatUtil.printToChat(textComponent);
    }
}
