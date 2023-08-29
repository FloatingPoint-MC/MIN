package cn.floatingpoint.min.system.irc;

import cn.floatingpoint.min.MIN;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.runnable.Runnable;
import cn.floatingpoint.min.utils.client.ChatUtil;
import cn.floatingpoint.min.utils.client.WebUtil;
import net.minecraft.util.text.TextComponentString;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-19 16:27:11
 */
public class IRCMessageGrabber {
    public static AtomicBoolean enabled = new AtomicBoolean();
    private static int startLoc;

    public static void grabMessage() {
        if (enabled.get()) {
            MIN.runAsync(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = WebUtil.getJSONFromPost("https://minserver.vlouboos.repl.co/irc/current");
                        if (jsonObject.getInt("Code") == 0) {
                            if (startLoc - 1 == jsonObject.getInt("Current")) return;
                        }
                        JSONObject messages = WebUtil.getJSONFromPost("https://minserver.vlouboos.repl.co/irc/get?index=" + startLoc);
                        if (messages.getInt("Code") == 0) {
                            List<Integer> prevMessages = new ArrayList<>();
                            List<Integer> currentMessages = new ArrayList<>();
                            messages.remove("Code");
                            for (String key : messages.keySet()) {
                                if (key.contains("prev")) {
                                    prevMessages.add(Integer.parseInt(key.substring(4)));
                                } else {
                                    currentMessages.add(Integer.parseInt(key));
                                }
                            }
                            prevMessages.sort(Comparator.comparingInt(Integer::intValue));
                            currentMessages.sort(Comparator.comparingInt(Integer::intValue));
                            for (int prevIndex : prevMessages) {
                                String originMessage = messages.getString("prev" + prevIndex);
                                printMessage(originMessage);
                            }
                            int lastIndex = 0;
                            for (int index : currentMessages) {
                                String originMessage = messages.getString(String.valueOf(index));
                                printMessage(originMessage);
                                lastIndex = index;
                            }
                            startLoc = lastIndex + 1;
                        }
                    } catch (URISyntaxException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public byte priority() {
                    return 0;
                }
            });
        }
    }

    private static void printMessage(String originMessage) {
        if (originMessage.contains("\2476[DEVELOPER]") || originMessage.contains("\2474[ADMIN]") || originMessage.contains("\2474[SERVER]")) {
            String identity = originMessage.split("]")[0].split("\\[")[1];
            originMessage = originMessage.replaceFirst(identity, Managers.i18NManager.getTranslation("irc." + identity));
        }
        TextComponentString text = new TextComponentString("\247b[MIN-IRC]" + originMessage);
        ChatUtil.printToChat(text);
    }

    public static void reset() {
        MIN.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = WebUtil.getJSONFromPost("https://minserver.vlouboos.repl.co/irc/current");
                    if (jsonObject.has("Code")) {
                        if (jsonObject.getInt("Code") == 0) {
                            startLoc = jsonObject.getInt("Current") + 1;
                            if (!enabled.get()) {
                                ChatUtil.printToChatWithPrefix("IRC已经联通!");
                            }
                            enabled.set(true);
                        }
                    }
                } catch (URISyntaxException | IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public byte priority() {
                return 0;
            }
        });
    }
}
