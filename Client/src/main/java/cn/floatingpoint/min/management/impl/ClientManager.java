package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.MIN;
import cn.floatingpoint.min.management.Manager;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.misc.impl.RankDisplay;
import cn.floatingpoint.min.utils.client.CheatDetection;
import cn.floatingpoint.min.utils.client.WebUtil;
import org.json.JSONException;
import org.json.JSONObject;

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
    public HashMap<UUID, Boolean> clientMateUuids;
    public HashMap<String, Integer> ranks = new HashMap<>();
    public HashMap<UUID, CheatDetection> cheaterUuids;
    public float titleSize, titleX, titleY;
    public ArrayList<String> sarcasticMessages;
    public HashSet<String> cooldown = new HashSet<>();
    public boolean firstStart;

    @Override
    public String getName() {
        return "Client Manager";
    }

    @Override
    public void init() {
        clientMateUuids = new HashMap<>();
        cheaterUuids = new HashMap<>();
        sarcasticMessages = new ArrayList<>();
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
                if (version == 1 || version == 2 || version == 3) {

                } else {
                    return;
                }
            }
            Managers.i18NManager.setSelectedLanguage(jsonObject.getString("Language"));
            titleSize = jsonObject.getFloat("Title-Size");
            titleX = jsonObject.getFloat("Title-X");
            titleY = jsonObject.getFloat("Title-Y");
        } catch (Exception e) {
            firstStart = true;
        }
        sarcasticMessages = Managers.fileManager.readAsList("SarcasticMessages.txt");
        if (sarcasticMessages.isEmpty()) {
            sarcasticMessages.add("{0}，上帝是公平的，给了你丑的外表，一定也会给你低的智商，所以让你开，以免让你显得不协调。");
            sarcasticMessages.add("{0}，像你这种头脑简单，四肢发达的人，活着就是耻辱，开更是显示你的低智商。");
            sarcasticMessages.add("好好赚钱吧，{0}，没有钱，你拿什么呵护你的亲情，联络你的友情？靠开吗？别闹了，大家都挺忙的。");
            sarcasticMessages.add("听我一句劝，{0}，脑子空不要紧，就是别开，开就进水。");
            sarcasticMessages.add("{0}，你是受精卵被开水烫过之后出生的吗？开都开不明白。");
            sarcasticMessages.add("真羡慕{0}的皮肤，特别是脸，保养的这么厚，是纪狗特权吧。");
            sarcasticMessages.add("我从不骂人，我骂的都不是人，是{0}，是纪狗。");
            sarcasticMessages.add("{0}，别人笑起来很好看，但你不一样，你开是看起来很好笑。");
            sarcasticMessages.add("{0}，怎样办呀，我眼睛的度数好像又增加了，完全看不见你的智商了啊，你的外纪有办法治疗我吗？");
            sarcasticMessages.add("{0}，不要在老子面前表演你那不要脸的外纪，没本事。");
            sarcasticMessages.add("{0}，人贱一辈子，猪贱一刀子，活着浪费空气，死了浪费土地，开浪费人民币。");
            sarcasticMessages.add("{0}，也不知道你为什么总是不用你脖子上顶着的那玩意儿思考，你说你活着除了衬托世界有多么美好之外你还能干什么？开？");
            sarcasticMessages.add("{0}，我真的不愿意用脚趾头鄙视你。但兄弟，是你逼我这么做的，你的外纪太拉跨了。");
            sarcasticMessages.add("虽然{0}可能开了那种高档的外纪，但我还能隐约看到它卑贱的低智商。");
            sarcasticMessages.add("{0}从小都是孤身一人，受的打击太大了，开都不会开了。");
            sarcasticMessages.add("{0}，你应该还有点自知之明吧，你不要开好吗？你一开就把你的Low技术与低智商暴露了。");
            sarcasticMessages.add("{0}，飞起来的不必须是天使，也可能是鸟人。");
        }
    }

    public boolean isClientMate(UUID uniqueID) {
        return false;
    }

    @SuppressWarnings("all")
    public void getRank(String id) {
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
}
