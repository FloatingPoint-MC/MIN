package guichaguri.betterfps.gui.data;

import guichaguri.betterfps.BetterFpsConfig;
import guichaguri.betterfps.BetterFpsConfig.AlgorithmType;
import guichaguri.betterfps.BetterFpsHelper;
import guichaguri.betterfps.gui.GuiConfigOption;

import java.util.List;

/**
 * @author Guilherme Chaguri
 */
public class OptionManager {

    public static void addButtons(List<GuiConfigOption<?>> buttons) {
        BetterFpsConfig config = BetterFpsHelper.getConfig();

        Boolean[] boolMap = new Boolean[]{true, false};

        String[] enabledNames = new String[]{
                "On",
                "Off"
        };
        String[] fancyFast = new String[]{
                "Fancy",
                "Fast"
        };

        // Algorithm
        GuiConfigOption<AlgorithmType> algorithm = new GuiConfigOption<>(0, "Better FPS Options");
        algorithm.add(AlgorithmType.VANILLA, "Vanilla");
        algorithm.add(AlgorithmType.RIVENS, "Rivens");
        algorithm.add(AlgorithmType.LIBGDX, "LibGDX");
        algorithm.add(AlgorithmType.RIVENS_FULL, "Rivens-Full");
        algorithm.add(AlgorithmType.RIVENS_HALF, "Rivens-Half");
        algorithm.add(AlgorithmType.TAYLORS, "Taylors");
        algorithm.add(AlgorithmType.JAVA, "Java");
        algorithm.setWide(true);
        algorithm.setDefaults(AlgorithmType.VANILLA, AlgorithmType.RIVENS_HALF, config.algorithm);
        algorithm.setDescription(
                "Algorithm",
                "\247eChange algorithm."
        );
        algorithm.setShiftClick(new AlgorithmAction(algorithm));
        buttons.add(algorithm);

        // Fog
        GuiConfigOption<Boolean> fog = new GuiConfigOption<>(3, "Fog");
        fog.set(boolMap, fancyFast);
        fog.setDefaults(true, true, config.fog);
        fog.setDescription("Enable Fog.");
        buttons.add(fog);

        // Beacon Beam
        GuiConfigOption<Boolean> beam = new GuiConfigOption<>(4, "BeaconBeam");
        beam.set(boolMap, fancyFast);
        beam.setDefaults(true, true, config.beaconBeam);
        beam.setDescription("Enable Beacon Beam.");
        buttons.add(beam);

        // Hopper Improvement
        GuiConfigOption<Boolean> hopper = new GuiConfigOption<>(5, "Fast Hopper");
        hopper.set(boolMap, enabledNames);
        hopper.setDescription("Enable Fast Hopper.");
        buttons.add(hopper);

        // Beacon Improvement
        GuiConfigOption<Boolean> beacon;
        beacon = new GuiConfigOption<>(6, "Fast Beacon");
        beacon.set(boolMap, enabledNames);
        beacon.setDefaults(false, true, config.fastBeacon);
        beacon.setDescription("Enable Fast Beacon");
        buttons.add(beacon);
    }

    public static boolean store(List<GuiConfigOption<?>> buttons) {
        BetterFpsConfig config = BetterFpsHelper.getConfig();

        config.algorithm = getButtonValue(buttons, 0, AlgorithmType.VANILLA);
        config.updateChecker = getButtonValue(buttons, 1);
        config.preallocateMemory = getButtonValue(buttons, 2);
        config.fog = getButtonValue(buttons, 3);
        config.beaconBeam = getButtonValue(buttons, 4);
        config.fastBeacon = getButtonValue(buttons, 6);
        return false;
    }

    private static boolean getButtonValue(List<GuiConfigOption<?>> buttons, int id) {
        return getButtonValue(buttons, id, true);
    }

    private static <T> T getButtonValue(List<GuiConfigOption<?>> buttons, int id, T def) {
        for (GuiConfigOption<?> button : buttons) {
            if (button.id == id) {
                return (T) button.getValue();
            }
        }
        return def;
    }

}
