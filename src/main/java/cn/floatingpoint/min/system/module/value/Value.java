package cn.floatingpoint.min.system.module.value;

import java.util.function.Supplier;

public class Value<V> {
    private V value;
    private final Supplier<Boolean> displayable;

    public Value(V value, Supplier<Boolean> displayable) {
        this.value = value;
        this.displayable = displayable;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public boolean isDisplayable() {
        return displayable.get();
    }
}
