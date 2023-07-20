package cn.floatingpoint.min.system.module.impl.boost.impl;

import cn.floatingpoint.min.system.module.impl.boost.BoostModule;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.system.module.value.impl.TextValue;
import cn.floatingpoint.min.system.ui.components.DraggableGameView;
import cn.floatingpoint.min.utils.client.Pair;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-18 11:43:21
 */
public class Sprint extends BoostModule implements DraggableGameView {
    private final OptionValue showText = new OptionValue(true);
    private final TextValue text = new TextValue("[Sprint Enabled]", showText::getValue);
    private final int defaultColor;

    public Sprint() {
        addValues(new Pair<>("ShowText", showText), new Pair<>("Text", text));
        defaultColor = new Color(200, 200, 200).getRGB();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode()));
    }

    @Override
    public void tick() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    }

    @Override
    public boolean draw(int x, int y) {
        if (this.isEnabled() && showText.getValue()) {
            mc.fontRenderer.drawStringWithShadow(text.getValue(), x, y + 1, defaultColor);
            return true;
        }
        return false;
    }

    @Override
    public int getWidth() {
        return showText.getValue() ? mc.fontRenderer.getStringWidth(text.getValue()) : 0;
    }

    @Override
    public int getHeight() {
        return mc.fontRenderer.FONT_HEIGHT + 1;
    }

    @Override
    public String getIdentity() {
        return "Sprint";
    }
}
