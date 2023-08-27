package cn.floatingpoint.min.system.ui.shortcut;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.shortcut.Shortcut;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-27 17:20:38
 */
public class GuiEditShortcut extends GuiScreen {
    private final GuiManageShortcut parent;
    private final Shortcut shortcut;
    private GuiSelectAction selectAction;
    private GuiTextField name;
    private int key;

    public GuiEditShortcut(Shortcut shortcut, GuiManageShortcut parent) {
        this.parent = parent;
        this.shortcut = shortcut;
    }

    @Override
    public void initGui() {
        selectAction = new GuiSelectAction(null, this.width, this.height, this.height / 2, this.height - 64, 12);
        name = new GuiTextField(0, mc.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        if (shortcut != null) {
            name.setText(shortcut.name());
            key = shortcut.key();
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 52, 98, 20, Managers.i18NManager.getTranslation("shortcut.save")));
        } else {
            name.setFocused(true);
            key = 0;
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 52, 98, 20, Managers.i18NManager.getTranslation("shortcut.add")));
            this.buttonList.get(0).enabled = false;
        }
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height - 52, 98, 20, Managers.i18NManager.getTranslation("shortcut.back")));
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
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        selectAction.drawScreen(mouseX, mouseY, partialTicks);
        mc.fontRenderer.drawString(Managers.i18NManager.getTranslation("shortcut.name"), this.width / 2 - 100, 40, -1);
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
            mc.displayGuiScreen(parent);
        } else {
            name.textboxKeyTyped(typedChar, keyCode);
            this.buttonList.get(0).enabled = !name.getText().isEmpty();
        }
    }

    @Override
    public void updateScreen() {
        name.updateCursorCounter();
    }
}
