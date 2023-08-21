package org.lwjglx.util.glu.tessellation;

class GLUhalfEdge {

    public GLUhalfEdge next; /* doubly-linked list (prev==Sym->next) */
    public GLUhalfEdge Sym; /* same edge, opposite direction */
    public GLUhalfEdge Onext; /* next edge CCW around origin */
    public GLUhalfEdge Lnext; /* next edge CCW around left face */
    public GLUvertex Org; /* origin vertex (Overtex too long) */
    public GLUface Lface; /* left face */

    /* Internal data (keep hidden) */
    public ActiveRegion activeRegion; /* a region with this upper edge (sweep.c) */
    public int winding; /* change in winding number when crossing */
    public boolean first;

    GLUhalfEdge(boolean first) {
        this.first = first;
    }
}
