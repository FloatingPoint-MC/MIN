package org.lwjglx.opengl;

/**
 * This is the input implementation interface. Mouse and Keyboard delegates to implementors of this interface. There is
 * one InputImplementation for each supported platform.
 * 
 * @author elias_naur
 */
public interface InputImplementation {

    int getWidth();

    int getHeight();
}
