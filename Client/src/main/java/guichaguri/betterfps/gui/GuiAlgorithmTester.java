package guichaguri.betterfps.gui;

import guichaguri.betterfps.BetterFpsHelper;
import guichaguri.betterfps.math.JavaMath;
import guichaguri.betterfps.math.LibGDXMath;
import guichaguri.betterfps.math.RivensFullMath;
import guichaguri.betterfps.math.RivensHalfMath;
import guichaguri.betterfps.math.RivensMath;
import guichaguri.betterfps.math.TaylorMath;
import net.minecraft.util.math.MathHelper;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

/**
 * @author Guilherme Chaguri
 */
@SuppressWarnings("all")
public class GuiAlgorithmTester extends JFrame implements ActionListener {

    public static void main(String[] args) {
        warmupClasses();
        Map<String, Long> data = benchmark(1000000000, 5000);

        String bestAlgorithm = null;
        long bestTime = Long.MAX_VALUE;

        for (String key : data.keySet()) {
            long time = data.get(key);
            if (time < bestTime) {
                bestAlgorithm = key;
                bestTime = time;
            }
        }

        System.out.println(bestAlgorithm);
        System.exit(0);
    }

    private static void warmupClasses() {
        // Initialize the classes, so the loading time will not count in the startBenchmark
        JavaMath.sin(0);
        //MathHelper.sin(0);
        LibGDXMath.sin(0);
        RivensMath.sin(0);
        RivensFullMath.sin(0);
        RivensHalfMath.sin(0);
        TaylorMath.sin(0);
    }

    private static Map<String, Long> benchmark(int maxLoops, int maxTime) {
        long javaMath = 0;
        long vanilla = 0;
        long libgdx = 0;
        long rivens = 0;
        long rivensFull = 0;
        long rivensHalf = 0;
        long taylors = 0;

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < maxLoops; i++) {
            float angle = (float) Math.toRadians(i % 360);
            long time;

            time = System.nanoTime();
            JavaMath.sin(angle);
            JavaMath.cos(angle);
            javaMath += System.nanoTime() - time;

            //time = System.nanoTime();
            //MathHelper.sin(angle);
            //MathHelper.cos(angle);
            //vanilla += System.nanoTime() - time;

            time = System.nanoTime();
            LibGDXMath.sin(angle);
            LibGDXMath.cos(angle);
            libgdx += System.nanoTime() - time;

            time = System.nanoTime();
            RivensMath.sin(angle);
            RivensMath.cos(angle);
            rivens += System.nanoTime() - time;

            time = System.nanoTime();
            RivensFullMath.sin(angle);
            RivensFullMath.cos(angle);
            rivensFull += System.nanoTime() - time;

            time = System.nanoTime();
            RivensHalfMath.sin(angle);
            RivensHalfMath.cos(angle);
            rivensHalf += System.nanoTime() - time;

            time = System.nanoTime();
            TaylorMath.sin(angle);
            TaylorMath.cos(angle);
            taylors += System.nanoTime() - time;

            if (System.currentTimeMillis() - startTime > maxTime) {
                break;
            }
        }

        HashMap<String, Long> results = new HashMap<String, Long>();
        results.put("java", javaMath);
        results.put("vanilla", vanilla);
        results.put("rivens", rivens);
        results.put("rivens-full", rivensFull);
        results.put("rivens-half", rivensHalf);
        results.put("libgdx", libgdx);
        results.put("taylors", taylors);
        return results;
    }

    private final String EXIT = "exit";
    private final String TEST_AGAIN = "test_again";

    private final JLabel status;
    private final JPanel data;
    private final JButton again;
    private final JButton exit;

    private String bestAlgorithm = "rivens-half";

    public GuiAlgorithmTester() {
        setTitle("Algorithm Tester");
        setResizable(false);
        setMinimumSize(new Dimension(300, 200));

        JPanel content = new JPanel();
        content.setLayout(new GridBagLayout());
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;

        status = new JLabel();
        status.setHorizontalAlignment(JLabel.CENTER);
        status.setFont(status.getFont().deriveFont(16F));
        content.add(status, c);

        c.gridy++;
        content.add(Box.createVerticalStrut(15), c);

        c.gridy++;
        data = new JPanel();
        data.setLayout(new GridLayout(0, 3));
        data.setVisible(false);
        content.add(data, c);

        c.gridy++;
        content.add(Box.createVerticalStrut(15), c);

        c.gridy++;
        again = new JButton("Test Again");
        again.setActionCommand(TEST_AGAIN);
        again.addActionListener(this);
        again.setVisible(false);
        content.add(again, c);

        c.gridy++;
        exit = new JButton();
        exit.setActionCommand(EXIT);
        exit.addActionListener(this);
        exit.setVisible(false);
        content.add(exit, c);

        add(content);
        pack();
        setSize(getPreferredSize());
        setLocationRelativeTo(null);

        startBenchmark();
    }

    private void showResults(Map<String, Long> results) {
        BetterFpsHelper.LOG.info("Done! Showing the results");
        data.removeAll();

        long bestTime = Long.MAX_VALUE;

        for (String key : results.keySet()) {
            data.add(new JLabel(key));

            long d = results.get(key);
            if (d < bestTime) {
                bestAlgorithm = key;
                bestTime = d;
            }

            JLabel nanoseconds = new JLabel(d + " ns");
            nanoseconds.setHorizontalAlignment(JLabel.RIGHT);
            data.add(nanoseconds);

            JLabel miliseconds = new JLabel(TimeUnit.NANOSECONDS.toMillis(d) + " ms");
            miliseconds.setHorizontalAlignment(JLabel.RIGHT);
            data.add(miliseconds);
        }

        exit.setText("Exit");

        data.setVisible(true);
        again.setVisible(true);
        exit.setVisible(true);
        status.setText("Best Algorithm: " + bestAlgorithm);

        pack();
        setSize(getPreferredSize());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void startBenchmark() {
        Thread t = new Thread(() -> {
            status.setText("Testing...");
            data.setVisible(false);
            again.setVisible(false);
            exit.setVisible(false);

            BetterFpsHelper.LOG.info("Benchmarking algorithms...");
            warmupClasses();
            showResults(benchmark(1000000000, 10000));
        });
        t.start();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String action = event.getActionCommand();
        if (action.equals(TEST_AGAIN)) {
            startBenchmark();
        } else if (action.equals(EXIT)) {
            setVisible(false);
            System.exit(0);
        }
    }
}
