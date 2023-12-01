package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.MIN;
import cn.floatingpoint.min.management.Manager;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.misc.impl.RankDisplay;
import cn.floatingpoint.min.system.shortcut.Shortcut;
import cn.floatingpoint.min.utils.client.Rank;
import cn.floatingpoint.min.utils.client.WebUtil;
import net.minecraft.client.gui.GuiChat;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjglx.input.Keyboard;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-18 11:26:03
 */
public class ClientManager implements Manager {
    public HashMap<UUID, Integer> clientMateUuids;
    public HashMap<String, Rank> ranks = new HashMap<>();
    public float titleSize, titleX, titleY;
    public HashSet<String> cooldown = new HashSet<>();
    public HashSet<Shortcut> shortcuts;
    public boolean firstStart;
    public boolean lock;
    public boolean adsorption;
    public boolean vexGui;
    public GuiChat.Channel channel;

    @Override
    public String getName() {
        return "Client Manager";
    }

    @Override
    public void init() {
        clientMateUuids = new HashMap<>();
        ranks = new HashMap<>();
        titleSize = 1.0f;
        titleX = 0.0f;
        titleY = 0.0f;
        cooldown = new HashSet<>();
        shortcuts = new HashSet<>();
        firstStart = false;
        vexGui = false;
        adsorption = false;
        channel = GuiChat.Channel.WORLD;
        try {
            String context = Managers.fileManager.readAsString("config.json");
            JSONObject jsonObject = new JSONObject(context);
            if (!jsonObject.has("Config-Version")) {
                return;
            }
            int version = jsonObject.getInt("Config-Version");
            if (version != FileManager.VERSION) {
                if (version < 200) {
                    return;
                } else if (version == 202) {
                    adsorption = jsonObject.getBoolean("Adsorption");
                } else if (version == 203 || version == 204) {
                    adsorption = jsonObject.getBoolean("Adsorption");
                    channel = GuiChat.Channel.valueOf(jsonObject.getString("Chat-Channel").toUpperCase());
                }
            } else {
                adsorption = jsonObject.getBoolean("Adsorption");
                channel = GuiChat.Channel.valueOf(jsonObject.getString("Chat-Channel").toUpperCase());
                for (Object object : jsonObject.getJSONArray("Shortcuts")) {
                    if (object instanceof JSONObject json) {
                        ArrayList<Shortcut.Action> actions = new ArrayList<>();
                        for (Object o : json.getJSONArray("Actions")) {
                            if (o instanceof JSONObject action) {
                                actions.add(new Shortcut.Action(
                                        Shortcut.Action.Type.valueOf(action.getString("Type")),
                                        action.getString("Context")
                                ));
                            }
                        }
                        shortcuts.add(new Shortcut(json.getString("Name"), Keyboard.getKeyIndex(json.getString("KeyBind").toUpperCase()), actions));
                    }
                }
            }
            Managers.i18NManager.setSelectedLanguage(jsonObject.getString("Language"));
            titleSize = jsonObject.getFloat("Title-Size");
            titleX = jsonObject.getFloat("Title-X");
            titleY = jsonObject.getFloat("Title-Y");
        } catch (Exception e) {
            firstStart = true;
        }
    }

    @SuppressWarnings("all")
    public void getRank(String id) {
        if (id.contains("\247")) {
            lock = true;
            return;
        }
        if (lock) {
            return;
        }
        if (!ranks.containsKey(id) && !cooldown.contains(id)) {
            cooldown.add(id);
            MIN.runAsync(() -> {
                try {
                    JSONObject json = null;
                    if (RankDisplay.game.isCurrentMode("bw")) {
                        json = WebUtil.getJSON("http://mc-api.16163.com/search/bedwars.html?uid=" + id);
                        int rank = json.has("rank") ? json.getInt("rank") : -1;
                        ranks.put(id, new Rank(rank, json.getDouble("killDead")));
                    } else if (RankDisplay.game.isCurrentMode("sw")) {
                        json = WebUtil.getJSON("http://mc-api.16163.com/search/skywars.html?uid=" + id);
                        int rank = json.has("rank") ? json.getInt("rank") : -1;
                        ranks.put(id, new Rank(rank, json.getDouble("killDead")));
                    } else if (RankDisplay.game.isCurrentMode("kit")) {
                        json = WebUtil.getJSON("http://mc-api.16163.com/search/kitbattle.html?uid=" + id);
                        int rank = json.has("rank") ? json.getInt("rank") : -1;
                        ranks.put(id, new Rank(rank, json.getDouble("killDead")));
                    }
                    cooldown.remove(id);
                } catch (IOException | URISyntaxException | JSONException ignore) {
                }
            });
        }
    }

    public int isClientMate(UUID uuid) {
        return clientMateUuids.getOrDefault(uuid, -1);
    }
}
