package org.lwjglx.util.glu;

/**
 * The <b>GLUtessellatorCallbackAdapter</b> provides a default implementation of {@link GLUtessellatorCallback
 * GLUtessellatorCallback} with empty callback methods. This class can be extended to provide user defined callback
 * methods.
 *
 * @author Eric Veach, July 1994
 * @author Java Port: Pepijn Van Eechhoudt, July 2003
 * @author Java Port: Nathan Parker Burg, August 2003
 */
public class GLUtessellatorCallbackAdapter implements GLUtessellatorCallback {

    public void begin(int type) {}

    public void edgeFlag(boolean boundaryEdge) {}

    public void vertex(Object vertexData) {}

    public void end() {}

    // public void mesh(com.sun.opengl.impl.tessellator.GLUmesh mesh) {}
    public void error(int errnum) {}

    public void combine(double[] coords, Object[] data, float[] weight, Object[] outData) {}

    public void beginData(int type, Object polygonData) {}

    public void edgeFlagData(boolean boundaryEdge, Object polygonData) {}

    public void vertexData(Object vertexData, Object polygonData) {}

    public void endData(Object polygonData) {}

    public void errorData(int errnum, Object polygonData) {}

    public void combineData(double[] coords, Object[] data, float[] weight, Object[] outData, Object polygonData) {}
}
