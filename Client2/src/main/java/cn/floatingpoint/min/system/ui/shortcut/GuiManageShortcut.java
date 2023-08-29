package cn.floatingpoint.min.system.ui.shortcut;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.shortcut.Shortcut;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-25 22:25:49
 */
public class GuiManageShortcut extends GuiScreen {
    private final GuiScreen prevScreen;
    public Shortcut selectedShortcut;
    private GuiSelectShortcut selectShortcut;
    protected GuiButton delete;
    protected GuiButton edit;

    public GuiManageShortcut(GuiScreen prevScreen) {
        this.prevScreen = prevScreen;
    }

    @Override
    public void initGui() {
        selectShortcut = new GuiSelectShortcut(this, this.width, this.height, 32, this.height - 64, 36);
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height - 52, 98, 20, Managers.i18NManager.getTranslation("shortcut.add")));
        edit = new GuiButton(1, width / 2 - 100, height - 30, 98, 20, Managers.i18NManager.getTranslation("shortcut.edit"));
        this.buttonList.add(edit);
        edit.enabled = false;
        delete = new GuiButton(2, width / 2 + 2, height - 52, 98, 20, Managers.i18NManager.getTranslation("shortcut.delete"));
        this.buttonList.add(delete);
        delete.enabled = false;
        this.buttonList.add(new GuiButton(3, width / 2 + 2, height - 30, 98, 20, Managers.i18NManager.getTranslation("shortcut.back")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        selectShortcut.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, Managers.i18NManager.getTranslation("shortcut"), this.width / 2, 20, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        selectShortcut.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            mc.displayGuiScreen(new GuiEditShortcut(null, this));
        } else if (button.id == 1) {
            mc.displayGuiScreen(new GuiEditShortcut(selectedShortcut, this));
        } else if (button.id == 2) {
            Managers.clientManager.shortcuts.remove(selectedShortcut);
            mc.displayGuiScreen(new GuiManageShortcut(prevScreen));
        } else if (button.id == 3) {
            mc.displayGuiScreen(prevScreen);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            mc.displayGuiScreen(prevScreen);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        selectShortcut.handleMouseInput();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        selectShortcut.mouseReleased(mouseX, mouseY, state);
    }
}
