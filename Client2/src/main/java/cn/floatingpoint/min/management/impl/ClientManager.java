package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.MIN;
import cn.floatingpoint.min.management.Manager;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.misc.impl.RankDisplay;
import cn.floatingpoint.min.utils.client.WebUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-18 11:26:03
 */
public class ClientManager implements Manager {
    public HashSet<UUID> clientMateUuids;
    public HashMap<String, Integer> ranks = new HashMap<>();
    public float titleSize, titleX, titleY;
    public HashSet<String> cooldown = new HashSet<>();
    public boolean firstStart;
    public boolean lock;

    @Override
    public String getName() {
        return "Client Manager";
    }

    @Override
    public void init() {
        clientMateUuids = new HashSet<>();
        titleSize = 1.0f;
        titleX = 0.0f;
        titleY = 0.0f;
        firstStart = false;
        try {
            String context = Managers.fileManager.readAsString("config.json");
            JSONObject jsonObject = new JSONObject(context);
            if (!jsonObject.has("Config-Version")) {
                return;
            }
            int version = jsonObject.getInt("Config-Version");
            if (version != FileManager.VERSION) {
                return;
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
                    } else if (RankDisplay.game.isCurrentMode("bw-xp")) {
                        json = WebUtil.getJSON("http://mc-api.16163.com/search/bedwarsxp.html?uid=" + id);
                    } else if (RankDisplay.game.isCurrentMode("sw")) {
                        json = WebUtil.getJSON("http://mc-api.16163.com/search/skywars.html?uid=" + id);
                    } else if (RankDisplay.game.isCurrentMode("kit")) {
                        json = WebUtil.getJSON("http://mc-api.16163.com/search/kitbattle.html?uid=" + id);
                    }
                    ranks.put(id, json.getInt("rank"));
                    cooldown.remove(id);
                } catch (IOException | URISyntaxException | JSONException ignore) {
                }
            });
        }
    }

    public boolean isClientMate(UUID uuid) {
        for (UUID id : clientMateUuids) {
            if (id.equals(uuid)) return true;
        }
        return false;
    }
}
