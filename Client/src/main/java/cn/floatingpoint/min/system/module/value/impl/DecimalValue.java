package cn.floatingpoint.min.system.module.value.impl;

import cn.floatingpoint.min.system.module.value.Value;

import java.util.function.Supplier;

public class DecimalValue extends Value<Double> {
    private final Double minimum, maximum, increment;

    public DecimalValue(Double minimum, Double maximum, Double increment, Double value) {
        this(minimum, maximum, increment, value, () -> true);
    }

    public DecimalValue(Double minimum, Double maximum, Double increment, Double value, Supplier<Boolean> displayable) {
        super(value, displayable);
        this.minimum = minimum;
        this.maximum = maximum;
        this.increment = increment;
    }

    @Override
    public void setValue(Double value) {
        if (value > maximum) value = maximum;
        if (value < minimum) value = minimum;
        super.setValue(value);
    }

    public Double getMinimum() {
        return minimum;
    }

    public Double getMaximum() {
        return maximum;
    }

    public Double getIncrement() {
        return increment;
    }
}
