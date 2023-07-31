package cn.floatingpoint.min.system.module.impl.render.impl;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.module.impl.render.RenderModule;
import cn.floatingpoint.min.system.module.value.impl.ModeValue;
import cn.floatingpoint.min.system.module.value.impl.OptionValue;
import cn.floatingpoint.min.system.ui.components.DraggableGameView;
import cn.floatingpoint.min.utils.client.Pair;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @projectName: MIN
 * @author: vlouboos
 * @date: 2023-07-19 20:49:37
 */
public class Scoreboard extends RenderModule implements DraggableGameView {
    private final ModeValue font = new ModeValue(new String[]{"Minecraft", "SourceSans"}, "Minecraft");
    private final OptionValue shadow = new OptionValue(false);
    private final OptionValue redNumber = new OptionValue(true);
    private final OptionValue background = new OptionValue(true);
    public static ScoreObjective scoreObjective;
    private int width, height;

    public Scoreboard() {
        addValues(
                new Pair<>("Font", font),
                new Pair<>("Shadow", shadow),
                new Pair<>("RedNumber", redNumber),
                new Pair<>("Background", background)
        );
        setCanBeEnabled(false);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onRender3D() {

    }

    @Override
    public boolean draw(int x, int y) {
        if (scoreObjective == null) {
            width = 0;
            height = 0;
            return false;
        }
        FontRenderer fontRenderer = font.isCurrentMode("Minecraft") ? mc.fontRenderer : font.isCurrentMode("SourceSans") ? Managers.fontManager.sourceHansSansCN_Regular_18 : null;
        assert fontRenderer != null;
        net.minecraft.scoreboard.Scoreboard scoreboard = scoreObjective.getScoreboard();
        List<Score> list = scoreboard.getSortedScores(scoreObjective).stream().filter(p_apply_1_ -> !p_apply_1_.getPlayerName().startsWith("#")).collect(Collectors.toList());
        ArrayList<Score> scores = new ArrayList<>();
        Score score = new Score(scoreboard, scoreObjective, "  \247fMIN官方KOOK: \247b14221788  ");
        score.setScorePoints(0);
        scores.add(score);

        if (list.size() > 15) {
            scores.addAll(Lists.newArrayList(Iterables.skip(list, list.size() - 15)));
        } else {
            scores.addAll(list);
        }

        int i = fontRenderer.getStringWidth(scoreObjective.getDisplayName());

        for (Score score1 : scores) {
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score1.getPlayerName()) + ": " + TextFormatting.RED + score1.getScorePoints();
            i = Math.max(i, fontRenderer.getStringWidth(s));
        }

        int l1 = x - i - 3;
        int j;

        for (j = 0; j < scores.size(); j++) {
            int k = y - j * fontRenderer.FONT_HEIGHT;
            int l = x - 3 + 2;
            if (j == scores.size() - 1) {
                width = l + 2 - l1;
                height = y - k;
            }
        }

        l1 += width;
        j = 0;
        for (Score score1 : scores) {
            ++j;
            ScorePlayerTeam playersTeam = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(playersTeam, score1.getPlayerName());
            String s2 = TextFormatting.RED + String.valueOf(score1.getScorePoints());
            int k = y - j * fontRenderer.FONT_HEIGHT + height;
            int l = x - 3 + 2 + width;
            if (background.getValue()) {
                Gui.drawRect(l1 - 2, k, l, k + fontRenderer.FONT_HEIGHT, 1342177280);
            }
            if (shadow.getValue()) {
                fontRenderer.drawStringWithShadow(s1, l1, k, new Color(553648127).getRGB());
            } else {
                fontRenderer.drawString(s1, l1, k, new Color(553648127).getRGB());
            }
            if (redNumber.getValue()) {
                if (shadow.getValue()) {
                    fontRenderer.drawStringWithShadow(s2, l - fontRenderer.getStringWidth(s2), k, new Color(553648127).getRGB());
                } else {
                    fontRenderer.drawString(s2, l - fontRenderer.getStringWidth(s2), k, new Color(553648127).getRGB());
                }
            }

            if (j == scores.size()) {
                String s3 = scoreObjective.getDisplayName();
                if (background.getValue()) {
                    Gui.drawRect(l1 - 2, k - fontRenderer.FONT_HEIGHT - 1, l, k - 1, 1610612736);
                    Gui.drawRect(l1 - 2, k - 1, l, k, 1342177280);
                }
                if (shadow.getValue()) {
                    fontRenderer.drawStringWithShadow(s3, l1 + i / 2 - fontRenderer.getStringWidth(s3) / 2, k - fontRenderer.FONT_HEIGHT, new Color(553648127).getRGB());
                } else {
                    fontRenderer.drawString(s3, l1 + i / 2 - fontRenderer.getStringWidth(s3) / 2, k - fontRenderer.FONT_HEIGHT, new Color(553648127).getRGB());
                }
            }
        }
        scoreObjective = null;
        return true;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String getIdentity() {
        return "Scoreboard";
    }
}
