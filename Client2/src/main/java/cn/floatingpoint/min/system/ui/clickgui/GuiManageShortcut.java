package cn.floatingpoint.min.system.ui.clickgui;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.shortcut.Shortcut;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-25 22:25:49
 */
public class GuiManageShortcut extends GuiScreen {
    private final GuiScreen prevScreen;
    private GuiSelectShortcut selectShortcut;

    public GuiManageShortcut(GuiScreen prevScreen) {
        this.prevScreen = prevScreen;
    }

    @Override
    public void initGui() {
        selectShortcut = new GuiSelectShortcut(this.width, this.height, 32, this.height - 64, 36);
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        selectShortcut.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            mc.displayGuiScreen(prevScreen);
        }
    }
}
