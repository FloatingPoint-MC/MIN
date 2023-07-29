package cn.floatingpoint.min.system.module.value.impl;

import cn.floatingpoint.min.system.module.value.Value;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

import java.util.function.Supplier;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-18 11:44:36
 */
public class TextValue extends Value<String> {

    public TextValue(String value) {
        this(value, () -> true);
    }

    public TextValue(String value, Supplier<Boolean> displayable) {
        super(value, displayable);
    }

    public void deleteWords() {
        if (!this.getValue().isEmpty()) {
            int current = this.getValue().lastIndexOf(" ");
            this.setValue(this.getValue().substring(0, current));
        }
    }

    public void updateText(char typedChar, int keyCode) {
        if (keyCode == 14) {
            if (GuiScreen.isCtrlKeyDown()) {
                this.deleteWords();
            } else {
                this.delete();
            }
        }
        if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
            this.writeText(Character.toString(typedChar));
        }
    }

    private void writeText(String string) {
        this.setValue(this.getValue() + string);
    }

    private void delete() {
        if (!this.getValue().isEmpty()) {
            this.setValue(this.getValue().substring(0, this.getValue().length() - 1));
        }
    }
}
