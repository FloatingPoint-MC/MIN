package guichaguri.betterfps.gui;

import org.lwjgl.input.Keyboard;

/**
 * @author Guilherme Chaguri
 */
public class GuiConfigOption<T> extends GuiCycleButton<T> {

    private String description;
    private boolean wide = false;
    private T defaultVanilla, defaultBetterFps;
    private Runnable shiftClick;

    public GuiConfigOption(int id, String title) {
        super(id, title);
    }

    public boolean isWide() {
        return wide;
    }

    public void setWide(boolean wide) {
        this.wide = wide;
    }

    public void setShiftClick(Runnable shiftClick) {
        this.shiftClick = shiftClick;
    }

    public void setDefaults(T defaultVanilla, T defaultBetterFps, T originalValue) {
        this.defaultVanilla = defaultVanilla;
        this.defaultBetterFps = defaultBetterFps;

        setValue(originalValue);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String... lines) {
        StringBuilder builder = new StringBuilder();

        for (String line : lines) {
            builder.append(line);
            builder.append('\n');
        }

        builder.append('\n');
        builder.append("\2477");
        if (defaultVanilla == defaultBetterFps) {
            builder.append(toDisplayName(defaultBetterFps));
        } else {
            builder.append(toDisplayName(defaultVanilla));
            builder.append('\n');
            builder.append(toDisplayName(defaultBetterFps));
        }

        description = builder.toString();
    }

    @Override
    public void add(T value, String displayName) {
        super.add(value, displayName);
    }

    @Override
    public void actionPerformed() {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            if (shiftClick != null) {
                shiftClick.run();
                return;
            }
        }

        super.actionPerformed();
    }

}
