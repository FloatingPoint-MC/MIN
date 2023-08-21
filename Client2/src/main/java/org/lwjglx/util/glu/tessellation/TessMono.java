package org.lwjglx.util.glu.tessellation;

class TessMono {

    static boolean __gl_meshTessellateMonoRegion(GLUface face) {
        GLUhalfEdge up, lo;

        /*
         * All edges are oriented CCW around the boundary of the region. First, find the half-edge whose origin vertex
         * is rightmost. Since the sweep goes from left to right, face->anEdge should be close to the edge we want.
         */
        up = face.anEdge;
        assert (up.Lnext != up && up.Lnext.Lnext != up);

        while (Geom.VertLeq(up.Sym.Org, up.Org)) {
            up = up.Onext.Sym;
        }
        while (Geom.VertLeq(up.Org, up.Sym.Org)) {
            up = up.Lnext;
        }
        lo = up.Onext.Sym;

        while (up.Lnext != lo) {
            if (Geom.VertLeq(up.Sym.Org, lo.Org)) {
                /*
                 * up.Sym.Org is on the left. It is safe to form triangles from lo.Org. The EdgeGoesLeft test guarantees
                 * progress even when some triangles are CW, given that the upper and lower chains are truly monotone.
                 */
                while (lo.Lnext != up
                        && (Geom.EdgeGoesLeft(lo.Lnext) || Geom.EdgeSign(lo.Org, lo.Sym.Org, lo.Lnext.Sym.Org) <= 0)) {
                    GLUhalfEdge tempHalfEdge = Mesh.__gl_meshConnect(lo.Lnext, lo);
                    if (tempHalfEdge == null) return false;
                    lo = tempHalfEdge.Sym;
                }
                lo = lo.Onext.Sym;
            } else {
                /* lo.Org is on the left. We can make CCW triangles from up.Sym.Org. */
                while (lo.Lnext != up && (Geom.EdgeGoesRight(up.Onext.Sym)
                        || Geom.EdgeSign(up.Sym.Org, up.Org, up.Onext.Sym.Org) >= 0)) {
                    GLUhalfEdge tempHalfEdge = Mesh.__gl_meshConnect(up, up.Onext.Sym);
                    if (tempHalfEdge == null) return false;
                    up = tempHalfEdge.Sym;
                }
                up = up.Lnext;
            }
        }

        /*
         * Now lo.Org == up.Sym.Org == the leftmost vertex. The remaining region can be tessellated in a fan from this
         * leftmost vertex.
         */
        assert (lo.Lnext != up);
        while (lo.Lnext.Lnext != up) {
            GLUhalfEdge tempHalfEdge = Mesh.__gl_meshConnect(lo.Lnext, lo);
            if (tempHalfEdge == null) return false;
            lo = tempHalfEdge.Sym;
        }

        return true;
    }

    /*
     * __gl_meshTessellateInterior( mesh ) tessellates each region of the mesh which is marked "inside" the polygon.
     * Each such region must be monotone.
     */
    public static boolean __gl_meshTessellateInterior(GLUmesh mesh) {
        GLUface f, next;

        /* LINTED */
        for (f = mesh.fHead.next; f != mesh.fHead; f = next) {
            /* Make sure we don''t try to tessellate the new triangles. */
            next = f.next;
            if (f.inside) {
                if (!__gl_meshTessellateMonoRegion(f)) return false;
            }
        }

        return true;
    }

    // private static final int MARKED_FOR_DELETION = 0x7fffffff;

    /*
     * __gl_meshSetWindingNumber( mesh, value, keepOnlyBoundary ) resets the winding numbers on all edges so that
     * regions marked "inside" the polygon have a winding number of "value", and regions outside have a winding number
     * of 0. If keepOnlyBoundary is TRUE, it also deletes all edges which do not separate an interior region from an
     * exterior one.
     */
    public static boolean __gl_meshSetWindingNumber(GLUmesh mesh, int value, boolean keepOnlyBoundary) {
        GLUhalfEdge e, eNext;

        for (e = mesh.eHead.next; e != mesh.eHead; e = eNext) {
            eNext = e.next;
            if (e.Sym.Lface.inside != e.Lface.inside) {

                /* This is a boundary edge (one side is interior, one is exterior). */
                e.winding = (e.Lface.inside) ? value : -value;
            } else {

                /* Both regions are interior, or both are exterior. */
                if (!keepOnlyBoundary) {
                    e.winding = 0;
                } else {
                    if (!Mesh.__gl_meshDelete(e)) return false;
                }
            }
        }
        return true;
    }
}
