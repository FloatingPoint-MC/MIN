package net.minecraft.client.settings;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.IntHashMap;
import org.lwjglx.input.Keyboard;

public class KeyBinding implements Comparable<KeyBinding> {
    private static final Map<String, KeyBinding> KEY_BIND_ARRAY = Maps.newHashMap();
    private static final IntHashMap<KeyBinding> HASH = new IntHashMap<>();
    private static final Set<String> KEY_BIND_SET = Sets.newHashSet();
    private static final Map<String, Integer> CATEGORY_ORDER = Maps.newHashMap();
    private final String keyDescription;
    private final int keyCodeDefault;
    private final String keyCategory;
    private int keyCode;

    /**
     * Is the key held down?
     */
    private boolean pressed;
    private int pressTime;

    public static void onTick(int keyCode) {
        if (keyCode != 0) {
            KeyBinding keybinding = HASH.lookup(keyCode);

            if (keybinding != null) {
                ++keybinding.pressTime;
            }
        }
    }

    public static void setKeyBindState(int keyCode, boolean pressed) {
        if (keyCode != 0) {
            KeyBinding keybinding = HASH.lookup(keyCode);

            if (keybinding != null) {
                keybinding.pressed = pressed;
            }
        }
    }

    /**
     * Completely recalculates whether any key binds are held, from scratch.
     */
    public static void updateKeyBindState() {
        for (KeyBinding keybinding : KEY_BIND_ARRAY.values()) {
            try {
                setKeyBindState(keybinding.keyCode, keybinding.keyCode < 256 && Keyboard.isKeyDown(keybinding.keyCode));
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
    }

    public static void unPressAllKeys() {
        for (KeyBinding keybinding : KEY_BIND_ARRAY.values()) {
            keybinding.unpressKey();
        }
    }

    public static void resetKeyBindingArrayAndHash() {
        HASH.clearMap();

        for (KeyBinding keybinding : KEY_BIND_ARRAY.values()) {
            HASH.addKey(keybinding.keyCode, keybinding);
        }
    }

    public static Set<String> getKeyBinds() {
        return KEY_BIND_SET;
    }

    public KeyBinding(String description, int keyCode, String category) {
        this.keyDescription = description;
        this.keyCode = keyCode;
        this.keyCodeDefault = keyCode;
        this.keyCategory = category;
        KEY_BIND_ARRAY.put(description, this);
        HASH.addKey(keyCode, this);
        KEY_BIND_SET.add(category);
    }

    /**
     * Returns true if the key is pressed (used for continuous querying). Should be used in tickers.
     */
    public boolean isKeyDown() {
        return this.pressed;
    }

    public String getKeyCategory() {
        return this.keyCategory;
    }

    /**
     * Returns true on the initial key press. For continuous querying use {@link #isKeyDown()}. Should be used in key
     * events.
     */
    public boolean isPressed() {
        if (this.pressTime == 0) {
            return false;
        } else {
            --this.pressTime;
            return true;
        }
    }

    public void clearKeyPressed() {
        pressTime = 0;
    }

    private void unpressKey() {
        this.pressTime = 0;
        this.pressed = false;
    }

    public String getKeyDescription() {
        return this.keyDescription;
    }

    public int getKeyCodeDefault() {
        return this.keyCodeDefault;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public int compareTo(KeyBinding p_compareTo_1_) {
        return this.keyCategory.equals(p_compareTo_1_.keyCategory) ? I18n.format(this.keyDescription).compareTo(I18n.format(p_compareTo_1_.keyDescription)) : CATEGORY_ORDER.get(this.keyCategory).compareTo(CATEGORY_ORDER.get(p_compareTo_1_.keyCategory));
    }

    public static Supplier<String> getDisplayString(String key) {
        KeyBinding keybinding = KEY_BIND_ARRAY.get(key);
        return keybinding == null ? () ->
                key : () ->
                GameSettings.getKeyDisplayString(keybinding.getKeyCode());
    }

    static {
        CATEGORY_ORDER.put("key.categories.movement", 1);
        CATEGORY_ORDER.put("key.categories.gameplay", 2);
        CATEGORY_ORDER.put("key.categories.inventory", 3);
        CATEGORY_ORDER.put("key.categories.creative", 4);
        CATEGORY_ORDER.put("key.categories.multiplayer", 5);
        CATEGORY_ORDER.put("key.categories.ui", 6);
        CATEGORY_ORDER.put("key.categories.misc", 7);
        CATEGORY_ORDER.put("MIN Client", 8);
    }
}
