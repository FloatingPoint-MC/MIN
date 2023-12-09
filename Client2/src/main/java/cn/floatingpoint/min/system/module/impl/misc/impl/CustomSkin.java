package cn.floatingpoint.min.system.module.impl.misc.impl;

import cn.floatingpoint.min.MIN;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.misc.MiscModule;
import cn.floatingpoint.min.system.module.value.impl.TextValue;
import cn.floatingpoint.min.utils.client.Pair;
import cn.floatingpoint.min.utils.client.PlayerUtil;
import cn.floatingpoint.min.utils.client.WebUtil;
import cn.floatingpoint.min.utils.math.TimeHelper;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.json.JSONObject;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-27 14:37:32
 */
public class CustomSkin extends MiscModule {
    private final TextValue username = new TextValue("Steve");
    private final TimeHelper timer = new TimeHelper();
    private String cacheUsername = "";

    public CustomSkin() {
        addValues(
                new Pair<>("Username", username)
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
        if (mc.player != null && mc.player.connection != null) {
            NetworkPlayerInfo info = mc.player.connection.getPlayerInfo(mc.player.getUniqueID());
            if (info != null) {
                if (timer.isDelayComplete(3000L)) {
                    if (!cacheUsername.equals(username.getValue())) {
                        info.setPlayerTexturesLoaded(false);
                        cacheUsername = username.getValue();
                    }
                }
                if (info.isPlayerTexturesNotLoaded()) {
                    info.setPlayerTexturesLoaded(true);
                    MIN.runAsync(() -> {
                        try {
                            JSONObject json = WebUtil.getJSON("https://api.mojang.com/users/profiles/minecraft/" + cacheUsername);
                            if (json.has("id")) {
                                String raw = json.getString("id");
                                GameProfile gameProfile = new GameProfile(PlayerUtil.formUUID(raw), json.getString("name"));
                                gameProfile = mc.getSessionService().fillProfileProperties(gameProfile, false);
                                mc.getSkinManager().loadProfileTextures(gameProfile, (typeIn, location, profileTexture) -> {
                                    switch (typeIn) {
                                        case SKIN:
                                            info.playerTextures.put(MinecraftProfileTexture.Type.SKIN, location);
                                            info.skinType = profileTexture.getMetadata("model");

                                            if (info.skinType == null) {
                                                info.skinType = "default";
                                            }

                                            break;

                                        case CAPE:
                                            info.playerTextures.put(MinecraftProfileTexture.Type.CAPE, location);
                                            break;

                                        case ELYTRA:
                                            info.playerTextures.put(MinecraftProfileTexture.Type.ELYTRA, location);
                                    }
                                }, false);
                            }
                            json = WebUtil.getJSONFromPost("https://minserver.vlouboos.repl.co/online/get?username=" + mc.player.getName());
                            int id = json.getInt("code");
                            if (id == 0) {
                                String raw = json.getString("uuid");
                                Managers.clientManager.clientMateUuids.put(PlayerUtil.formUUID(raw), id);
                            }
                        } catch (Exception ignored) {
                        }
                    });
                }
            }
        }
    }
}
