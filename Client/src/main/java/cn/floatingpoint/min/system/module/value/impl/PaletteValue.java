package cn.floatingpoint.min.system.module.value.impl;

import cn.floatingpoint.min.system.module.value.Value;

import java.util.function.Supplier;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-19 13:51:04
 */
public class PaletteValue extends Value<Integer> {
    public PaletteValue(Integer value) {
        super(value, () -> true);
    }

    public PaletteValue(Integer value, Supplier<Boolean> displayable) {
        super(value, displayable);
    }
}
