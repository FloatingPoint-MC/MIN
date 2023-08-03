package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.management.Manager;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.Category;
import cn.floatingpoint.min.system.module.Module;
import cn.floatingpoint.min.system.module.impl.boost.BoostModule;
import cn.floatingpoint.min.system.module.impl.boost.impl.FastLoad;
import cn.floatingpoint.min.system.module.impl.boost.impl.MemoryManager;
import cn.floatingpoint.min.system.module.impl.boost.impl.Sprint;
import cn.floatingpoint.min.system.module.impl.misc.MiscModule;
import cn.floatingpoint.min.system.module.impl.misc.impl.*;
import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.impl.render.impl.*;
import cn.floatingpoint.min.system.module.value.Value;
import cn.floatingpoint.min.system.module.value.impl.*;
import cn.floatingpoint.min.utils.math.TimeHelper;
import net.minecraft.client.Minecraft;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModuleManager implements Manager {
    public final LinkedHashMap<String, BoostModule> boostModules = new LinkedHashMap<>();
    public final LinkedHashMap<String, MiscModule> miscModules = new LinkedHashMap<>();
    public final LinkedHashMap<String, RenderModule> renderModules = new LinkedHashMap<>();
    public final LinkedHashMap<String, Module> modules = new LinkedHashMap<>();
    public final TimeHelper antiNoise = new TimeHelper();

    @Override
    public String getName() {
        return "Module Manager";
    }

    @Override
    public void init() {
        // Boost
        boostModules.put("FastLoad", new FastLoad());
        boostModules.put("MemoryManager", new MemoryManager());
        boostModules.put("Sprint", new Sprint());
        // Misc
        miscModules.put("AutoText", new AutoText());
        miscModules.put("CheaterDetector", new CheaterDetector());
        miscModules.put("CustomSkin", new CustomSkin());
        miscModules.put("RankDisplay", new RankDisplay());
        miscModules.put("TerminateBreakingBlock", new TerminateBreakingBlock());
        miscModules.put("WorldTimeChanger", new WorldTimeChange());
        // Render
        renderModules.put("Animation", new Animation());
        renderModules.put("ArmorDisplay", new ArmorDisplay());
        renderModules.put("AttackIndicator", new AttackIndicator());
        renderModules.put("BlockOverlay", new BlockOverlay());
        renderModules.put("BoundingBox", new BoundingBox());
        renderModules.put("ChatBar", new ChatBar());
        renderModules.put("CleanView", new CleanView());
        renderModules.put("ClickGUI", new ClickGUI());
        renderModules.put("FireFilter", new FireFilter());
        renderModules.put("FreeLook", new FreeLook());
        renderModules.put("FullBright", new FullBright());
        renderModules.put("FurtherCamera", new FurtherCamera());
        renderModules.put("ItemPhysics", new ItemPhysics());
        renderModules.put("KeyStrokes", new KeyStrokes());
        renderModules.put("MinimizedBobbing", new MinimizedBobbing());
        renderModules.put("NameProtect", new NameProtect());
        renderModules.put("NameTag", new NameTag());
        renderModules.put("NoHurtCam", new NoHurtCam());
        renderModules.put("Particles", new Particles());
        renderModules.put("PotionDisplay", new PotionDisplay());
        renderModules.put("Scoreboard", new Scoreboard());
        renderModules.put("SmoothZoom", new SmoothZoom());
        renderModules.put("Spinning", new Spinning());
        renderModules.put("StatusDisplay", new StatusDisplay());
        renderModules.put("TNTTimer", new TNTTimer());

        modules.putAll(boostModules);
        modules.putAll(miscModules);
        modules.putAll(renderModules);

        String context = Managers.fileManager.readAsString("module/status.json");
        try {
            JSONObject jsonObject = new JSONObject(context);
            for (String key : jsonObject.keySet()) {
                JSONObject moduleData = jsonObject.getJSONObject(key);
                if (modules.containsKey(key)) {
                    modules.get(key).setEnableOnStartUp(moduleData.getBoolean("Enabled"));
                    modules.get(key).setKey(Keyboard.getKeyIndex(moduleData.getString("KeyBind").toUpperCase()));
                }
            }
        } catch (Exception e) {
            if (Minecraft.DEBUG_MODE) {
                e.printStackTrace();
            }
        }
        context = Managers.fileManager.readAsString("module/value.json");
        try {
            JSONObject jsonObject = new JSONObject(context);
            for (String key : jsonObject.keySet()) {
                if (modules.containsKey(key)) {
                    for (Map.Entry<String, Value<?>> entry : modules.get(key).getValues().entrySet()) {
                        JSONObject valueData = jsonObject.getJSONObject(key);
                        String valueKey = entry.getKey();
                        Value<?> value = entry.getValue();
                        if (valueData.has(valueKey)) {
                            if (value instanceof DecimalValue) {
                                ((DecimalValue) value).setValue(valueData.getDouble(valueKey));
                            } else if (value instanceof IntegerValue) {
                                ((IntegerValue) value).setValue(valueData.getInt(valueKey));
                            } else if (value instanceof ModeValue) {
                                ((ModeValue) value).setValue(valueData.getString(valueKey));
                            } else if (value instanceof OptionValue) {
                                ((OptionValue) value).setValue(valueData.getBoolean(valueKey));
                            } else if (value instanceof TextValue) {
                                ((TextValue) value).setValue(valueData.getString(valueKey));
                            } else if (value instanceof PaletteValue) {
                                ((PaletteValue) value).setValue(valueData.getInt(valueKey));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (Minecraft.DEBUG_MODE) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, ? extends Module> getModulesByCategory(Category category) {
        switch (category) {
            case Boost:
                return boostModules;
            case Misc:
                return miscModules;
            case Render:
                return renderModules;
        }
        return null;
    }
}
