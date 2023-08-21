package org.lwjglx.util.glu.tessellation;

class GLUface {

    public GLUface next; /* next face (never NULL) */
    public GLUface prev; /* previous face (never NULL) */
    public GLUhalfEdge anEdge; /* a half edge with this left face */
    public Object data; /* room for client's data */

    /* Internal data (keep hidden) */
    public GLUface trail; /* "stack" for conversion to strips */
    public boolean marked; /* flag for conversion to strips */
    public boolean inside; /* this face is in the polygon interior */
}
