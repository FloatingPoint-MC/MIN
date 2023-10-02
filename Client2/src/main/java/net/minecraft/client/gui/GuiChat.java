package net.minecraft.client.gui;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.ui.components.DraggableGameView;
import cn.floatingpoint.min.utils.math.Vec2i;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.lwjglx.input.Keyboard;
import org.lwjglx.input.Mouse;

public class GuiChat extends GuiScreen implements ITabCompleter {
    private String historyBuffer = "";

    /**
     * keeps position of which chat message you will select when you press up, (does not increase for duplicated
     * messages sent immediately after each other)
     */
    private int sentHistoryCursor = -1;
    private TabCompleter tabCompleter;

    /**
     * Chat entry field
     */
    protected GuiTextField inputField;

    /**
     * is the text that appears when you press the chat key and the input box appears pre-filled
     */
    private String defaultInputFieldText = "";

    private static DraggableGameView clickedDraggable;

    private int prevMouseX;
    private int prevMouseY;

    public GuiChat() {
    }

    public GuiChat(String defaultText) {
        this.defaultInputFieldText = defaultText;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        this.inputField = new GuiTextField(0, this.fontRenderer, 4, this.height - 12, this.width - 4, 12);
        this.inputField.setMaxStringLength(256);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
        this.inputField.setCanLoseFocus(false);
        String buttonText = Managers.i18NManager.getTranslation("chat.channel") + ": " + Managers.i18NManager.getTranslation("chat.channel." + Managers.clientManager.channel.name().toLowerCase());
        this.buttonList.add(new GuiButton(666, 4, this.height - 34, 8 + Managers.fontManager.sourceHansSansCN_Regular_20.getStringWidth(buttonText), 20, buttonText));
        this.tabCompleter = new GuiChat.ChatTabCompleter(this.inputField);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        this.inputField.updateCursorCounter();
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.tabCompleter.resetRequested();

        if (keyCode == 15) {
            this.tabCompleter.complete();
        } else {
            this.tabCompleter.resetDidComplete();
        }

        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        } else if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 200) {
                this.getSentHistory(-1);
            } else if (keyCode == 208) {
                this.getSentHistory(1);
            } else if (keyCode == 201) {
                this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
            } else if (keyCode == 209) {
                this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
            } else {
                this.inputField.textboxKeyTyped(typedChar, keyCode);
            }
        } else {
            String s = this.inputField.getText().trim();

            if (!s.isEmpty()) {
                this.sendChatMessage(s);
            }

            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 666) {
            switch (Managers.clientManager.channel) {
                case WORLD:
                    Managers.clientManager.channel = Channel.MIN;
                    break;
                case MIN:
                    Managers.clientManager.channel = Channel.WORLD;
            }
            button.displayString = Managers.i18NManager.getTranslation("chat.channel") + ": " + Managers.i18NManager.getTranslation("chat.channel." + Managers.clientManager.channel.name().toLowerCase());
            button.width = Managers.fontManager.sourceHansSansCN_Regular_20.getStringWidth(button.displayString) + 8;
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        ScaledResolution scaledresolution = new ScaledResolution(mc);
        int dWheel = Mouse.getDWheel();
        if (dWheel != 0) {
            for (Map.Entry<DraggableGameView, Vec2i> entry : Managers.draggableGameViewManager.draggableMap.entrySet()) {
                DraggableGameView draggableGameView = entry.getKey();
                Vec2i position = entry.getValue();
                if (isHovered(scaledresolution.getScaledWidth() / 2 + position.x, position.y, scaledresolution.getScaledWidth() / 2 + position.x + draggableGameView.getWidth(), position.y + draggableGameView.getHeight(), mouseX, mouseY)) {
                    if (dWheel > 0) {
                        draggableGameView.multiplyScale();
                    } else {
                        draggableGameView.divideScale();
                    }
                    return;
                }
            }
        }

        int i = Mouse.getEventDWheel();
        if (i != 0) {
            if (i > 1) {
                i = 1;
            }

            if (i < -1) {
                i = -1;
            }

            if (!isShiftKeyDown()) {
                i *= 7;
            }

            this.mc.ingameGUI.getChatGUI().scroll(i);
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution scaledresolution = new ScaledResolution(mc);
        if (clickedDraggable == null && mouseButton == 0) {
            for (Map.Entry<DraggableGameView, Vec2i> entry : Managers.draggableGameViewManager.draggableMap.entrySet()) {
                DraggableGameView draggableGameView = entry.getKey();
                Vec2i position = entry.getValue();
                if (isHovered(scaledresolution.getScaledWidth() / 2 + position.x + draggableGameView.xOffset(), position.y + draggableGameView.yOffset(), scaledresolution.getScaledWidth() / 2 + position.x + draggableGameView.getWidth() + draggableGameView.xOffset(), position.y + draggableGameView.getHeight() + draggableGameView.yOffset(), mouseX, mouseY)) {
                    clickedDraggable = draggableGameView;
                    return;
                }
            }
        }
        if (mouseButton == 0) {
            ITextComponent itextcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

            if (itextcomponent != null && this.handleComponentClick(itextcomponent)) {
                return;
            }
        }

        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Sets the text of the chat
     */
    protected void setText(String newChatText, boolean shouldOverwrite) {
        if (shouldOverwrite) {
            this.inputField.setText(newChatText);
        } else {
            this.inputField.writeText(newChatText);
        }
    }

    /**
     * input is relative and is applied directly to the sentHistoryCursor so -1 is the previous message, 1 is the next
     * message from the current cursor position
     */
    public void getSentHistory(int msgPos) {
        int i = this.sentHistoryCursor + msgPos;
        int j = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        i = MathHelper.clamp(i, 0, j);

        if (i != this.sentHistoryCursor) {
            if (i == j) {
                this.sentHistoryCursor = j;
                this.inputField.setText(this.historyBuffer);
            } else {
                if (this.sentHistoryCursor == j) {
                    this.historyBuffer = this.inputField.getText();
                }

                this.inputField.setText(this.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
                this.sentHistoryCursor = i;
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        this.inputField.drawTextBox();
        if (clickedDraggable != null) {
            if (Mouse.isButtonDown(0)) {
                Vec2i prevPosition = Managers.draggableGameViewManager.draggableMap.get(clickedDraggable);
                ScaledResolution scaledResolution = new ScaledResolution(mc);
                int width = scaledResolution.getScaledWidth();
                int height = scaledResolution.getScaledHeight();
                int x = Math.max(prevPosition.x + mouseX - prevMouseX, -width / 2);
                int y = Math.max(prevPosition.y + mouseY - prevMouseY, 0);
                if (width / 2 + x + clickedDraggable.getWidth() + clickedDraggable.xOffset() > width) {
                    x = width - clickedDraggable.getWidth() - scaledResolution.getScaledWidth() / 2;
                }
                if (y + clickedDraggable.getHeight() + clickedDraggable.yOffset() > height) {
                    y = height - clickedDraggable.getHeight();
                }
                if (Managers.clientManager.adsorption) {
                    if (Math.abs(y - this.height / 2) < 10) {
                        drawRect(0, this.height / 2 - 2, this.width, this.height / 2 + 2, new Color(255, 255, 85).getRGB());
                        if (Math.abs(y - this.height / 2) < 5) {
                            y = this.height / 2;
                        } else {
                            prevMouseY = mouseY;
                        }
                    } else {
                        prevMouseY = mouseY;
                    }
                    if (Math.abs(x + scaledResolution.getScaledWidth() / 2 - this.width / 2) < 10) {
                        drawRect(this.width / 2 - 2, 0, this.width / 2 + 2, this.height, new Color(255, 255, 85).getRGB());
                        if (Math.abs(x + scaledResolution.getScaledWidth() / 2 - this.width / 2) < 5) {
                            x = this.width / 2 - scaledResolution.getScaledWidth() / 2;
                        } else {
                            prevMouseX = mouseX;
                        }
                    } else {
                        prevMouseX = mouseX;
                    }
                } else {
                    prevMouseX = mouseX;
                    prevMouseY = mouseY;
                }
                Managers.draggableGameViewManager.draggableMap.put(clickedDraggable, new Vec2i(x, y));
            } else {
                clickedDraggable = null;
            }
            return;
        }
        prevMouseX = mouseX;
        prevMouseY = mouseY;

        ITextComponent itextcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());

        if (itextcomponent != null && itextcomponent.getStyle().getHoverEvent() != null) {
            this.handleComponentHover(itextcomponent, mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Sets the list of tab completions, as long as they were previously requested.
     */
    public void setCompletions(@Nonnull String... newCompletions) {
        this.tabCompleter.setCompletions(newCompletions);
    }

    public static class ChatTabCompleter extends TabCompleter {
        private final Minecraft client = Minecraft.getMinecraft();

        public ChatTabCompleter(GuiTextField p_i46749_1_) {
            super(p_i46749_1_, false);
        }

        public void complete() {
            super.complete();

            if (this.completions.size() > 1) {
                StringBuilder stringbuilder = new StringBuilder();

                for (String s : this.completions) {
                    if (!stringbuilder.isEmpty()) {
                        stringbuilder.append(", ");
                    }

                    stringbuilder.append(s);
                }

                this.client.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(stringbuilder.toString()), 1);
            }
        }

        @Nullable
        public BlockPos getTargetBlockPos() {
            BlockPos blockpos = null;

            if (this.client.objectMouseOver != null && this.client.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                blockpos = this.client.objectMouseOver.getBlockPos();
            }

            return blockpos;
        }
    }

    public enum Channel {
        WORLD,
        MIN
    }
}
