package cn.floatingpoint.min.system.ui.loading;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-23 21:52:32
 */
public class GuiFirstStart extends GuiScreen {
    private int alpha;
    private int stage;

    @Override
    public void initGui() {
        alpha = 255;
        stage = -1;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, width, height, new Color(0, 0, 0).getRGB());
        int white = new Color(216, 216, 216).getRGB();
        String text = Managers.i18NManager.getTranslation("clickgui.language") + ": ";
        Managers.fontManager.sourceHansSansCN_Regular_20.drawString(text, width - 26 - Managers.fontManager.sourceHansSansCN_Regular_20.getStringWidth(text), 9, white);
        Gui.drawRect(width - 24, 2, width - 23, 24, white);
        Gui.drawRect(width - 24, 2, width - 3, 3, white);
        Gui.drawRect(width - 3, 2, width - 2, 24, white);
        Gui.drawRect(width - 24, 23, width - 2, 24, white);
        if (Managers.i18NManager.getSelectedLanguage().equalsIgnoreCase("English")) {
            Managers.fontManager.sourceHansSansCN_Regular_18.drawString("ENG", width - 22, 9, white);
        } else {
            Managers.fontManager.sourceHansSansCN_Regular_18.drawString("中", width - 17, 9, white);
        }
        String welcomeText = Managers.i18NManager.getTranslation("start.welcome");
        Managers.fontManager.sourceHansSansCN_Regular_30.drawCenteredString(welcomeText, width / 2, height / 2 - 30, white);
        String continueText = Managers.i18NManager.getTranslation("start.continue");
        RenderUtil.drawRoundedRect(width / 2 - 100, height / 2 - 10, width / 2 + 100, height / 2 + 10, 3, white);
        Managers.fontManager.sourceHansSansCN_Regular_30.drawCenteredString(continueText, width / 2, height / 2 - 2, new Color(40, 40, 40).getRGB());
        Managers.fontManager.sourceHansSansCN_Regular_30.drawCenteredString("\247n" + Managers.i18NManager.getTranslation("start.skip"), width / 2, height / 2 + 40, white);
        if (stage == -1) {
            if (alpha > 0) {
                Gui.drawRect(0, 0, width, height, new Color(0, 0, 0, alpha).getRGB());
                alpha -= 3;
            } else {
                stage = 0;
            }
        } else if (stage == -2 || stage == 1) {
            if (alpha <= 255) {
                alpha += (stage + 3) * 3;
                alpha = Math.min(255, alpha);
                Gui.drawRect(0, 0, width, height, new Color(0, 0, 0, alpha).getRGB());
                if (alpha == 255) {
                    if (stage == -2) {
                        mc.displayGuiScreen(new GuiDamnJapaneseAction(mc.mainMenu));
                    } else if (stage == 1) {
                        mc.displayGuiScreen(new GuiTutorial());
                    }
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (stage >= 0) {
            if (isHovered(width - 24, 2, width - 2, 24, mouseX, mouseY)) {
                Managers.i18NManager.nextLanguage();
            } else if (isHovered(width / 2 - 100, height / 2 - 10, width / 2 + 100, height / 2 + 10, mouseX, mouseY)) {
                stage = 1;
            }
        }
        String text = Managers.i18NManager.getTranslation("start.skip");
        int width = Managers.fontManager.sourceHansSansCN_Regular_30.getStringWidth(text);
        if (isHovered((this.width - width) / 2, height / 2 + 40, (this.width + width) / 2, height / 2 + 50, mouseX, mouseY)) {
            stage = -2;
        }
    }
}
