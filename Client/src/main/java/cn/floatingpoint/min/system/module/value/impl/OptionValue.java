package cn.floatingpoint.min.system.module.value.impl;

import cn.floatingpoint.min.system.module.value.Value;

import java.util.function.Supplier;

public class OptionValue extends Value<Boolean> {
    public OptionValue(Boolean value) {
        super(value, () -> true);
    }

    public OptionValue(Boolean value, Supplier<Boolean> displayable) {
        super(value, displayable);
    }

    @Override
    public Boolean getValue() {
        return super.getValue() && isDisplayable();
    }
}
