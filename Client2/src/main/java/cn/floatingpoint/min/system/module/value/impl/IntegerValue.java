package cn.floatingpoint.min.system.module.value.impl;

import cn.floatingpoint.min.system.module.value.Value;

import java.util.function.Supplier;

public class IntegerValue extends Value<Integer> {
    private final Integer minimum, maximum, increment;

    public IntegerValue(Integer minimum, Integer maximum, Integer increment, Integer value) {
        this(minimum, maximum, increment, value, () -> true);
    }

    public IntegerValue(Integer minimum, Integer maximum, Integer increment, Integer value, Supplier<Boolean> displayable) {
        super(value, displayable);
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    @Override
    public void setValue(Integer value) {
        if (value > maximum) value = maximum;
        if (value < minimum) value = minimum;
        super.setValue(value);
    }

    public Integer getMinimum() {
        return minimum;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public Integer getIncrement() {
        return increment;
    }
}
