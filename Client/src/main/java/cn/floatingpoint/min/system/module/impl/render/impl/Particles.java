package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.IntegerValue;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.utils.client.Pair;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-30 16:46:54
 */
public class Particles extends RenderModule {
    public final static OptionValue waterWake = new OptionValue(true);
    public final static IntegerValue waterWakeAmplifier = new IntegerValue(1, 10, 1, 1, waterWake::getValue);
    public final static OptionValue crit = new OptionValue(true);
    public final static IntegerValue critAmplifier = new IntegerValue(1, 10, 1, 1, crit::getValue);
    public final static OptionValue sharpness = new OptionValue(true);
    public final static IntegerValue sharpnessAmplifier = new IntegerValue(1, 10, 1, 1, sharpness::getValue);
    public final static OptionValue enchantTable = new OptionValue(true);
    public final static IntegerValue enchantTableAmplifier = new IntegerValue(1, 10, 1, 1, enchantTable::getValue);
    public final static OptionValue lava = new OptionValue(true);
    public final static IntegerValue lavaAmplifier = new IntegerValue(1, 10, 1, 1, lava::getValue);
    public final static OptionValue itemCrack = new OptionValue(true);
    public final static IntegerValue itemCrackAmplifier = new IntegerValue(1, 10, 1, 1, itemCrack::getValue);
    public final static OptionValue blockCrack = new OptionValue(true);
    public final static IntegerValue blockCrackAmplifier = new IntegerValue(1, 10, 1, 1, blockCrack::getValue);
    public final static OptionValue blockDust = new OptionValue(true);
    public final static IntegerValue blockDustAmplifier = new IntegerValue(1, 10, 1, 1, blockDust::getValue);
    public final static OptionValue waterDrop = new OptionValue(true);
    public final static IntegerValue waterDropAmplifier = new IntegerValue(1, 10, 1, 1, waterDrop::getValue);

    public Particles() {
        addValues(
                new Pair<>("WW", waterWake),
                new Pair<>("WWA", waterWakeAmplifier),
                new Pair<>("C", crit),
                new Pair<>("CA", critAmplifier),
                new Pair<>("S", sharpness),
                new Pair<>("SA", sharpnessAmplifier),
                new Pair<>("ET", enchantTable),
                new Pair<>("ETA", enchantTableAmplifier),
                new Pair<>("L", lava),
                new Pair<>("LA", lavaAmplifier),
                new Pair<>("IC", itemCrack),
                new Pair<>("ICA", itemCrackAmplifier),
                new Pair<>("BC", blockCrack),
                new Pair<>("BCA", blockCrackAmplifier),
                new Pair<>("BD", blockDust),
                new Pair<>("BDA", blockDustAmplifier),
                new Pair<>("WD", waterDrop),
                new Pair<>("WDA", waterDropAmplifier)
        );
        setCanBeEnabled(false);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onRender3D() {

    }
}
