package cn.floatingpoint.min.system.module.value.impl;

import cn.floatingpoint.min.system.module.value.Value;

import java.util.function.Supplier;

public class ModeValue extends Value<String> {
    private final String[] modes;

    public ModeValue(String[] modes, String value) {
        this(modes, value, () -> true);
    }

    public ModeValue(String[] modes, String value, Supplier<Boolean> displayable) {
        super(value, displayable);
        this.modes = modes;
    }

    public String[] getModes() {
        return modes;
    }

    public void nextMode() {
        boolean found = false;
        for (String mode : modes) {
            if (found) {
                setValue(mode);
                return;
            }
            if (mode.equalsIgnoreCase(getValue())) {
                found = true;
            }
        }
        if (found) {
            setValue(modes[0]);
        }
    }

    public boolean isCurrentMode(String mode) {
        return this.getValue().equalsIgnoreCase(mode);
    }
}
