package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.management.Manager;
import cn.floatingpoint.min.management.Managers;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.UUID;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-18 11:26:03
 */
public class ClientManager implements Manager {
    public HashSet<UUID> clientMateUuids;
    public float titleSize, titleX, titleY;

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
        try {
            String context = Managers.fileManager.readAsString("config.json");
            JSONObject jsonObject = new JSONObject(context);
            if (!jsonObject.has("Config-Version")) {
                return;
            }
            if (jsonObject.getInt("Config-Version") != FileManager.VERSION) {
                return;
            }
            Managers.i18NManager.setSelectedLanguage(jsonObject.getString("Language"));
            titleSize = jsonObject.getFloat("Title-Size");
        } catch (Exception ignore) {}
    }
}
