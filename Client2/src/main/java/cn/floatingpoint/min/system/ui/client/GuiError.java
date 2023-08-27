package cn.floatingpoint.min.system.ui.client;

import cn.floatingpoint.min.management.Managers;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-27 16:40:22
 */
public class GuiError extends GuiScreen {
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 50, Managers.i18NManager.getTranslation("error.exit")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground(0);
        String first = Managers.i18NManager.getTranslation("error.first");
        mc.fontRenderer.drawString(first, (width - mc.fontRenderer.getStringWidth(first)) / 2, height / 2 - 50, -1);
        String second = Managers.i18NManager.getTranslation("error.second");
        mc.fontRenderer.drawString(second, (width - mc.fontRenderer.getStringWidth(second)) / 2, height / 2 - 40, -1);
        String third = Managers.i18NManager.getTranslation("error.third");
        mc.fontRenderer.drawString(third, (width - mc.fontRenderer.getStringWidth(third)) / 2, height / 2 - 30, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            mc.shutdown();
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }
}
