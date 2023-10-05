package cn.floatingpoint.min.threads;

import cn.floatingpoint.min.system.mouse.RawMouseController;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Mouse;
import net.minecraft.client.Minecraft;

public class MouseHandlerThread extends Thread {
    private final Minecraft mc = Minecraft.getMinecraft();
    private Mouse mouse = null;

    @SuppressWarnings("all")
    @Override
    public void run() {
        while (mc.running) {
            Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
            if (mouse == null) {
                for (Controller controller : controllers) {
                    if (controller.getType() == Controller.Type.MOUSE) {
                        controller.poll();
                        if (((Mouse) controller).getX().getPollData() != 0.0 || ((Mouse) controller).getY().getPollData() != 0.0) {
                            mouse = (Mouse) controller;
                        }
                    }
                }
            }
            if (mouse != null) {
                mouse.poll();
                int deltaX = (int) mouse.getX().getPollData();
                int deltaY = (int) mouse.getY().getPollData();
                if (mc.currentScreen == null) {
                    RawMouseController.setDeltaX(deltaX);
                    RawMouseController.setDeltaY(deltaY);
                }
            }
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
