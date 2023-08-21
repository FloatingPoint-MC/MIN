package org.lwjglx.util.glu.tessellation;

class GLUvertex {

    public GLUvertex next; /* next vertex (never NULL) */
    public GLUvertex prev; /* previous vertex (never NULL) */
    public GLUhalfEdge anEdge; /* a half-edge with this origin */
    public Object data; /* client's data */

    /* Internal data (keep hidden) */
    public double[] coords = new double[3]; /* vertex location in 3D */
    public double s, t; /* projection onto the sweep plane */
    public int pqHandle; /* to allow deletion from priority queue */
}
