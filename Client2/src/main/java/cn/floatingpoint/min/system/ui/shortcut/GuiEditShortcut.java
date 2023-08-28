package cn.floatingpoint.min.system.ui.shortcut;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.shortcut.Shortcut;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextFormatting;
import org.lwjglx.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-27 17:20:38
 */
public class GuiEditShortcut extends GuiScreen {
    protected Shortcut shortcut;
    private final GuiManageShortcut parent;
    private GuiSelectAction selectAction;
    private GuiTextField name;
    private int key;
    private boolean listening;
    private final String title;
    protected Shortcut.Action selectedAction;
    public GuiButton edit, delete;

    public GuiEditShortcut(Shortcut shortcut, GuiManageShortcut parent) {
        this.parent = parent;
        this.shortcut = shortcut;
        if (shortcut == null) {
            title = Managers.i18NManager.getTranslation("shortcut.create");
        } else {
            title = Managers.i18NManager.getTranslation("shortcut.change");
        }
    }

    @Override
    public void initGui() {
        name = new GuiTextField(0, mc.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        if (shortcut != null) {
            name.setText(shortcut.name());
            key = shortcut.key();
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 52, 98, 20, Managers.i18NManager.getTranslation("shortcut.save")));
        } else {
            shortcut = new Shortcut("", 0, new ArrayList<>());
            name.setFocused(true);
            key = 0;
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 52, 98, 20, Managers.i18NManager.getTranslation("shortcut.add")));
            this.buttonList.get(0).enabled = false;
        }
        selectAction = new GuiSelectAction(shortcut, this, this.width, this.height, this.height / 2, this.height - 64, 12);
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height - 52, 98, 20, Managers.i18NManager.getTranslation("shortcut.back")));
        this.buttonList.add(new GuiButton(3, mc.fontRenderer.getStringWidth(Managers.i18NManager.getTranslation("shortcut.key")) + 8, this.height / 2 - 36, 98, 20, Keyboard.getKeyName(key).replace("NONE", "None")));
        this.buttonList.add(new GuiButton(4, this.width - 152, this.height / 2 - 17, 48, 16, Managers.i18NManager.getTranslation("shortcut.add")));
        edit = new GuiButton(5, this.width - 102, this.height / 2 - 17, 48, 16, Managers.i18NManager.getTranslation("shortcut.edit"));
        this.buttonList.add(edit);
        edit.enabled = false;
        delete = new GuiButton(6, this.width - 52, this.height / 2 - 17, 48, 16, Managers.i18NManager.getTranslation("shortcut.delete"));
        this.buttonList.add(delete);
        delete.enabled = false;

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                if (shortcut != null) {
                    Managers.clientManager.shortcuts.remove(shortcut);
                }
                Managers.clientManager.shortcuts.add(new Shortcut(name.getText(), key, selectAction.getActions()));
                mc.displayGuiScreen(parent);
            } else if (button.id == 2) {
                mc.displayGuiScreen(parent);
            } else if (button.id == 3) {
                button.displayString = TextFormatting.WHITE + "> " + TextFormatting.YELLOW + Managers.i18NManager.getTranslation("shortcut.listening") + TextFormatting.WHITE + " <";
                listening = true;
            } else if (button.id == 4) {
                mc.displayGuiScreen(new GuiEditAction(null, this));
            } else if (button.id == 5) {
                mc.displayGuiScreen(new GuiEditAction(selectedAction, this));
            } else if (button.id == 6) {
                shortcut.actions().remove(selectedAction);
                mc.displayGuiScreen(new GuiEditShortcut(shortcut, parent));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        selectAction.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, title, this.width / 2, 20, -1);
        mc.fontRenderer.drawString(Managers.i18NManager.getTranslation("shortcut.name"), this.width / 2 - 100, 46, -1);
        mc.fontRenderer.drawString(Managers.i18NManager.getTranslation("shortcut.key"), 4, this.height / 2 - 30, -1);
        mc.fontRenderer.drawString(Managers.i18NManager.getTranslation("shortcut.action"), 4, this.height / 2 - 12, -1);
        name.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        name.mouseClicked(mouseX, mouseY, mouseButton);
        selectAction.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        selectAction.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        selectAction.handleMouseInput();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            if (listening) {
                key = 0;
                shortcut = new Shortcut(shortcut.name(), key, shortcut.actions());
                listening = false;
                this.buttonList.get(2).displayString = "None";
                return;
            }
            mc.displayGuiScreen(parent);
        } else {
            if (listening) {
                key = keyCode;
                listening = false;
                shortcut = new Shortcut(shortcut.name(), key, shortcut.actions());
                this.buttonList.get(2).displayString = Keyboard.getKeyName(key).replace("NONE", "None");
                return;
            }
            name.textboxKeyTyped(typedChar, keyCode);
            shortcut = new Shortcut(name.getText(), shortcut.key(), shortcut.actions());
            this.buttonList.get(0).enabled = !name.getText().isEmpty();
        }
    }

    @Override
    public void updateScreen() {
        name.updateCursorCounter();
    }
}
