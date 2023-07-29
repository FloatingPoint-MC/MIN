package cn.floatingpoint.min.system.ui.loading;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.Category;
import cn.floatingpoint.min.utils.math.FunctionUtil;
import cn.floatingpoint.min.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-23 22:27:26
 */
public class GuiTutorial extends GuiScreen {
    private int background;
    private int clickUiAnimationLeft;
    private int clickUiAnimationRight;
    private int clickUiAnimationAlpha;
    private int endAlpha;
    private int stage;

    @Override
    public void initGui() {
        stage = 0;
        background = 0;
        clickUiAnimationLeft = 0;
        clickUiAnimationRight = width;
        endAlpha = 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (stage == 0) {
            if (background < 255) {
                background += 5;
            }
            Gui.drawRect(0, 0, width, height, new Color(background, background, background).getRGB());
            if (background == 255) {
                int backgroundColor = new Color(40, 40, 40, 102).getRGB();
                int textColor = new Color(216, 216, 216).getRGB();
                int categoryColor = new Color(40, 40, 40, 56).getRGB();
                clickUiAnimationLeft = FunctionUtil.decreasedSpeed(clickUiAnimationLeft, 0, width / 2 - 100, 40.0f);
                clickUiAnimationRight = FunctionUtil.decreasedSpeed(clickUiAnimationRight, width, width / 2 - 220, 80.0f);
                RenderUtil.drawRoundedRect(clickUiAnimationRight, height / 2 - 160, clickUiAnimationRight + 440, height / 2 + 160, 3, backgroundColor);
                RenderUtil.drawRoundedRect(clickUiAnimationLeft - 120, height / 2 - 160, clickUiAnimationLeft, height / 2 + 160, 3, backgroundColor);
                int y = height / 2 - 10;
                for (Category category : Category.values()) {
                    RenderUtil.drawRoundedRect(clickUiAnimationLeft - 100, y, clickUiAnimationLeft - 20, y + 20, 2, categoryColor);
                    Managers.fontManager.sourceHansSansCN_Regular_30.drawCenteredString(Managers.i18NManager.getTranslation("module.category." + category.name()), clickUiAnimationLeft - 60, y + 8, textColor);
                    y += 30;
                }
                Managers.fontManager.sourceHansSansCN_Regular_30.drawStringWithShadow("MIN Client,", clickUiAnimationRight + 130, height / 2 - 150, textColor);
                Managers.fontManager.sourceHansSansCN_Regular_34.drawStringWithShadow("MAX Performance.", clickUiAnimationRight + 150, height / 2 - 136, textColor);
                if (clickUiAnimationAlpha < 255) {
                    clickUiAnimationAlpha += 5;
                }
                RenderUtil.drawImage(new ResourceLocation("min/logo.png"), clickUiAnimationLeft - 110, height / 2 - 140, 100, 100);
                RenderUtil.drawImage(new ResourceLocation("min/uis/setting.png"), clickUiAnimationLeft - 118, height / 2 + 142, 16, 16);
                int black = new Color(40, 40, 40, clickUiAnimationAlpha).getRGB();
                int white = new Color(216, 216, 216, clickUiAnimationAlpha).getRGB();
                Gui.drawRect(width / 2 - 260, height / 2 + 151, width / 2 - 218, height / 2 + 152, black);
                Gui.drawRect(width / 2 - 218, height / 2 + 142, width / 2 - 217, height / 2 + 158, white);
                Gui.drawRect(width / 2 - 218, height / 2 + 142, width / 2 - 202, height / 2 + 143, white);
                Gui.drawRect(width / 2 - 203, height / 2 + 142, width / 2 - 202, height / 2 + 158, white);
                Gui.drawRect(width / 2 - 218, height / 2 + 157, width / 2 - 202, height / 2 + 158, white);
                String firstTip = Managers.i18NManager.getTranslation("start.clickUiSetting");
                int tipWidth = Managers.fontManager.sourceHansSansCN_Regular_18.getStringWidth(firstTip);
                Gui.drawRect(width / 2 - 264 - tipWidth, height / 2 + 142, width / 2 - 260, height / 2 + 162, black);
                Managers.fontManager.sourceHansSansCN_Regular_18.drawString(firstTip, width / 2 - 262 - tipWidth, height / 2 + 148, white);
                Managers.fontManager.sourceHansSansCN_Regular_30.drawCenteredStringWithShadow(Managers.i18NManager.getTranslation("start.press"), width / 2, height / 2 + 174, black);
            }
        } else if (stage == 1) {
            if (endAlpha < 255) {
                endAlpha += 5;
            }
            Gui.drawRect(0, 0, width, height, new Color(background, background, background, endAlpha).getRGB());
            if (endAlpha == 255) {
                if (background > 0) {
                    background -= 5;
                }
                if (background == 0) {
                    mc.displayGuiScreen(new GuiMainMenu(true));
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        stage = 1;
    }
}
