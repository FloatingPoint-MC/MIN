package cn.floatingpoint.min.system.module;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.value.Value;
import cn.floatingpoint.min.utils.client.Pair;
import net.minecraft.client.Minecraft;

import java.util.LinkedHashMap;

import static net.minecraft.init.SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_OFF;
import static net.minecraft.init.SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_ON;

public abstract class Module {
    private final Category category;
    private int key;
    private boolean enabled;
    private boolean canBeEnabled;
    private boolean enableOnStartUp;
    private final LinkedHashMap<String, Value<?>> values;
    protected final Minecraft mc = Minecraft.getMinecraft();

    public Module(Category category) {
        this.category = category;
        values = new LinkedHashMap<>();
        canBeEnabled = true;
        enabled = false;
        enableOnStartUp = false;
    }

    public Category getCategory() {
        return category;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public abstract void onEnable();

    public abstract void onDisable();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (canBeEnabled) {
            if (enabled) {
                this.enabled = true;
                this.onEnable();
                if (mc.world != null && Managers.moduleManager.antiNoise.isDelayComplete(100L)) {
                    mc.player.playSound(BLOCK_WOOD_PRESSPLATE_CLICK_ON, 0.5F, 1.0F);
                    Managers.moduleManager.antiNoise.reset();
                }
            } else {
                this.onDisable();
                this.enabled = false;
                if (mc.world != null && Managers.moduleManager.antiNoise.isDelayComplete(100L)) {
                    mc.player.playSound(BLOCK_WOOD_PRESSPLATE_CLICK_OFF, 0.5F, 0.8F);
                    Managers.moduleManager.antiNoise.reset();
                }
            }
        } else {
            requestToggle();
        }
    }

    public void requestToggle() {}

    public void toggle() {
        this.setEnabled(!this.enabled);
    }

    @SafeVarargs
    protected final void addValues(Pair<String, Value<?>>... values) {
        for (Pair<String, Value<?>> valuePair : values) {
            this.values.put(valuePair.getKey(), valuePair.getValue());
        }
    }

    public LinkedHashMap<String, Value<?>> getValues() {
        return values;
    }

    public boolean canBeEnabled() {
        return canBeEnabled;
    }

    public void setCanBeEnabled(boolean canBeEnabled) {
        this.canBeEnabled = canBeEnabled;
    }

    public boolean shouldEnableOnStartUp() {
        return enableOnStartUp;
    }

    public void setEnableOnStartUp(boolean enabled) {
        enableOnStartUp = enabled;
    }

    public void enableOnStartUp() {
        this.enabled = true;
        this.onEnable();
    }
}
