package org.lwjglx.util.glu.tessellation;

class GLUmesh {

    GLUvertex vHead = new GLUvertex(); /* dummy header for vertex list */
    GLUface fHead = new GLUface(); /* dummy header for face list */
    GLUhalfEdge eHead = new GLUhalfEdge(true); /* dummy header for edge list */
    GLUhalfEdge eHeadSym = new GLUhalfEdge(false); /* and its symmetric counterpart */
}
