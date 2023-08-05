package org.lwjglx.input;

public interface Controller {

    int getAxisCount();

    String getAxisName(int arg0);

    float getAxisValue(int arg0);

    int getButtonCount();

    String getButtonName(int arg0);

    float getDeadZone(int arg0);

    int getIndex();

    String getName();

    float getPovX();

    float getPovY();

    float getRXAxisDeadZone();

    float getRXAxisValue();

    float getRYAxisDeadZone();

    float getRYAxisValue();

    float getRZAxisDeadZone();

    float getRZAxisValue();

    int getRumblerCount();

    String getRumblerName(int arg0);

    float getXAxisDeadZone();

    float getXAxisValue();

    float getYAxisDeadZone();

    float getYAxisValue();

    float getZAxisDeadZone();

    float getZAxisValue();

    boolean isButtonPressed(int arg0);

    void poll();

    void setDeadZone(int arg0, float arg1);

    void setRXAxisDeadZone(float arg0);

    void setRYAxisDeadZone(float arg0);

    void setRZAxisDeadZone(float arg0);

    void setRumblerStrength(int arg0, float arg1);

    void setXAxisDeadZone(float arg0);

    void setYAxisDeadZone(float arg0);

    void setZAxisDeadZone(float arg0);
}
