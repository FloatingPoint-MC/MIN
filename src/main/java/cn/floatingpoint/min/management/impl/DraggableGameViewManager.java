package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.management.Manager;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.ui.components.DraggableGameView;
import cn.floatingpoint.min.utils.math.Vec2i;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-18 13:24:16
 */
public class DraggableGameViewManager implements Manager {
    public final HashMap<DraggableGameView, Vec2i> draggableMap = new HashMap<>();

    @Override
    public String getName() {
        return "Draggable Game View Manager";
    }

    @Override
    public void init() {
        try {
            JSONObject jsonObject = new JSONObject(Managers.fileManager.readAsString("draggable.json"));
            for (String key : jsonObject.keySet()) {
                DraggableGameView draggableGameView = DraggableGameView.getDraggable(key);
                if (draggableGameView == null) {
                    continue;
                }
                JSONObject positionMap = jsonObject.getJSONObject(key);
                draggableMap.put(draggableGameView, new Vec2i(positionMap.getInt("x"), positionMap.getInt("y")));
            }
        } catch (Exception ignore) {}
        draggableMap.putIfAbsent(DraggableGameView.getDraggable("ArmorDisplay"), new Vec2i(0, 0));
        draggableMap.putIfAbsent(DraggableGameView.getDraggable("KeyStrokes"), new Vec2i(0, 0));
        draggableMap.putIfAbsent(DraggableGameView.getDraggable("MemoryManager"), new Vec2i(0, 0));
        draggableMap.putIfAbsent(DraggableGameView.getDraggable("PotionDisplay"), new Vec2i(0, 0));
        draggableMap.putIfAbsent(DraggableGameView.getDraggable("Scoreboard"), new Vec2i(0, 0));
        draggableMap.putIfAbsent(DraggableGameView.getDraggable("Sprint"), new Vec2i(0, 0));
        draggableMap.putIfAbsent(DraggableGameView.getDraggable("StatusDisplay"), new Vec2i(-400, 200));
    }
}
