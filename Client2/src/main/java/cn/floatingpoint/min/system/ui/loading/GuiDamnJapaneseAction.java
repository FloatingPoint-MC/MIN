package cn.floatingpoint.min.system.ui.loading;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-08-24 17:11:09
 */
public class GuiDamnJapaneseAction extends GuiScreen {
    private final GuiScreen nextScreen;
    private int animation;

    public GuiDamnJapaneseAction(GuiScreen nextScreen) {
        this.nextScreen = nextScreen;
    }

    @Override
    public void initGui() {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        String date = format.format(new Date());
        if (!date.equals("8-24") && !date.equals("08-24")) {
            mc.displayGuiScreen(nextScreen);
        }
        animation = -15;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawImage(new ResourceLocation("min/nuke_pollution/" + Math.max(0, animation / 2) + ".png"), 0, 0, width, height);
        int red = new Color(192, 0, 0).getRGB();
        Managers.fontManager.sourceHansSansCN_Regular_34.drawCenteredString("This is an article about Japan started release nuclear wastewater.", width / 2, height / 2 - 140, red);
        Managers.fontManager.sourceHansSansCN_Regular_34.drawCenteredString("这是一篇关于日本开始排放核废水的文章。", width / 2, height / 2 - 124, red);
        int white = new Color(216, 216, 216).getRGB();
        Managers.fontManager.sourceHansSansCN_Regular_26.drawCenteredString("Japan announced that it will start release nuclear wastewater", width / 2, height / 2 - 70, white);
        Managers.fontManager.sourceHansSansCN_Regular_26.drawCenteredString("日本已经在2023年8月24日宣布它将", width / 2, height / 2 - 56, white);
        Managers.fontManager.sourceHansSansCN_Regular_26.drawCenteredString("into the Pacific Ocean on Aug 24 2023.", width / 2, height / 2 - 42, white);
        Managers.fontManager.sourceHansSansCN_Regular_26.drawCenteredString("排放核废水到太平洋中", width / 2, height / 2 - 28, white);
        Managers.fontManager.sourceHansSansCN_Regular_26.drawCenteredString("We consider this as a dangerous actions which does", width / 2, height / 2 - 4, white);
        Managers.fontManager.sourceHansSansCN_Regular_26.drawCenteredString("我们认为这是一个对于人类", width / 2, height / 2 + 10, white);
        Managers.fontManager.sourceHansSansCN_Regular_26.drawCenteredString("giant harm to human beings.", width / 2, height / 2 + 24, white);
        Managers.fontManager.sourceHansSansCN_Regular_26.drawCenteredString("产生巨大危害的行为。", width / 2, height / 2 + 38, white);
        Managers.fontManager.sourceHansSansCN_Regular_26.drawCenteredString("Japan is joking about human life and is irresponsible for safety by doing so.", width / 2, height / 2 + 62, white);
        Managers.fontManager.sourceHansSansCN_Regular_26.drawCenteredString("日本这么做是在拿人类的生命开玩笑，对安全不负责任。", width / 2, height / 2 + 76, white);
        Managers.fontManager.sourceHansSansCN_Regular_26.drawCenteredString("We don't want this day to become World Ocean Disaster Day, we must stop this fucking behavior.", width / 2, height / 2 + 90, white);
        Managers.fontManager.sourceHansSansCN_Regular_26.drawCenteredString("我们不希望这一天变成世界海洋灾难日，必须阻止这一肮脏的行为。", width / 2, height / 2 + 104, white);
        int black = new Color(0, 0, 0, 126).getRGB();
        RenderUtil.drawRoundedRect(width / 2 - 250, height / 2 + 144, width / 2 - 10, height / 2 + 174, 5, black);
        Managers.fontManager.sourceHansSansCN_Regular_22.drawCenteredString("I don't think this actions is wrong and exit", width / 2 - 130, height / 2 + 148, white);
        Managers.fontManager.sourceHansSansCN_Regular_22.drawCenteredString("我不认为该决定有问题并退出", width / 2 - 130, height / 2 + 162, white);
        RenderUtil.drawRoundedRect(width / 2 + 10, height / 2 + 144, width / 2 + 250, height / 2 + 174, 5, black);
        Managers.fontManager.sourceHansSansCN_Regular_22.drawCenteredString("Resolutely resist fucking irresponsible actions", width / 2 + 130, height / 2 + 148, white);
        Managers.fontManager.sourceHansSansCN_Regular_22.drawCenteredString("坚决抵制该不负责任的行为", width / 2 + 130, height / 2 + 162, white);
        if (animation < 0) {
            drawRect(0, 0, width, height, new Color(0, 0, 0, -animation * 17).getRGB());
        }
    }

    @Override
    public void updateScreen() {
        if (animation < 236) {
            animation++;
        }
        if (!mc.isFullScreen()) {
            mc.gameSettings.guiScale = 2;
            mc.toggleFullscreen();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            if (isHovered(width / 2 - 250, height / 2 + 144, width / 2 - 10, height / 2 + 174, mouseX, mouseY)) {
                mc.shutdown();
            } else if (isHovered(width / 2 + 10, height / 2 + 144, width / 2 + 250, height / 2 + 174, mouseX, mouseY)) {
                mc.displayGuiScreen(nextScreen);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

    }
}
