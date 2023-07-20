package cn.floatingpoint.min.management.impl;

import cn.floatingpoint.min.management.Manager;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.Category;
import cn.floatingpoint.min.system.module.Module;
import cn.floatingpoint.min.system.module.impl.boost.BoostModule;
import cn.floatingpoint.min.system.module.impl.boost.impl.Sprint;
import cn.floatingpoint.min.system.module.impl.misc.MiscModule;
import cn.floatingpoint.min.system.module.impl.misc.impl.AutoText;
import cn.floatingpoint.min.system.module.impl.misc.impl.CheaterDetector;
import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.impl.render.impl.*;
import cn.floatingpoint.min.system.module.value.Value;
import cn.floatingpoint.min.system.module.value.impl.*;
import cn.floatingpoint.min.utils.math.TimeHelper;
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
        boostModules.put("Sprint", new Sprint());
        // Misc
        miscModules.put("AutoText", new AutoText());
        miscModules.put("CheaterDetector", new CheaterDetector());
        // Render
        renderModules.put("Animation", new Animation());
        renderModules.put("BlockOverlay", new BlockOverlay());
        renderModules.put("CleanView", new CleanView());
        renderModules.put("ClickGUI", new ClickGUI());
        renderModules.put("FullBright", new FullBright());
        renderModules.put("MinimizedBobbing", new MinimizedBobbing());
        renderModules.put("MoreParticles", new MoreParticles());
        renderModules.put("PotionDisplay", new PotionDisplay());
        renderModules.put("Scoreboard", new Scoreboard());

        modules.putAll(boostModules);
        modules.putAll(miscModules);
        modules.putAll(renderModules);

        String context = Managers.fileManager.readAsString("module/status.json");
        try {
            JSONObject jsonObject = new JSONObject(context);
            for (String key : jsonObject.keySet()) {
                JSONObject moduleData = jsonObject.getJSONObject(key);
                modules.get(key).setEnableOnStartUp(moduleData.getBoolean("Enabled"));
                modules.get(key).setKey(Keyboard.getKeyIndex(moduleData.getString("KeyBind").toUpperCase()));
            }
        } catch (Exception ignore) {
        }
        context = Managers.fileManager.readAsString("module/value.json");
        try {
            JSONObject jsonObject = new JSONObject(context);
            for (String key : jsonObject.keySet()) {
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
        } catch (Exception ignore) {
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
