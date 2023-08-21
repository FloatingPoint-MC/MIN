package org.lwjglx.util.glu.tessellation;

import org.lwjglx.util.glu.GLU;

class Normal {

    private Normal() {}

    static boolean SLANTED_SWEEP;
    static double S_UNIT_X; /* Pre-normalized */
    static double S_UNIT_Y;
    private static final boolean TRUE_PROJECT = false;

    static {
        if (SLANTED_SWEEP) {
            /*
             * The "feature merging" is not intended to be complete. There are special cases where edges are nearly
             * parallel to the sweep line which are not implemented. The algorithm should still behave robustly (ie.
             * produce a reasonable tesselation) in the presence of such edges, however it may miss features which could
             * have been merged. We could minimize this effect by choosing the sweep line direction to be something
             * unusual (ie. not parallel to one of the coordinate axes).
             */
            S_UNIT_X = 0.50941539564955385; /* Pre-normalized */
            S_UNIT_Y = 0.86052074622010633;
        } else {
            S_UNIT_X = 1.0;
            S_UNIT_Y = 0.0;
        }
    }

    private static double Dot(double[] u, double[] v) {
        return (u[0] * v[0] + u[1] * v[1] + u[2] * v[2]);
    }

    static void Normalize(double[] v) {
        double len = v[0] * v[0] + v[1] * v[1] + v[2] * v[2];

        assert (len > 0);
        len = Math.sqrt(len);
        v[0] /= len;
        v[1] /= len;
        v[2] /= len;
    }

    static int LongAxis(double[] v) {
        int i = 0;

        if (Math.abs(v[1]) > Math.abs(v[0])) {
            i = 1;
        }
        if (Math.abs(v[2]) > Math.abs(v[i])) {
            i = 2;
        }
        return i;
    }

    static void ComputeNormal(GLUtessellatorImpl tess, double[] norm) {
        GLUvertex v, v1, v2;
        double c, tLen2, maxLen2;
        double[] maxVal, minVal, d1, d2, tNorm;
        GLUvertex[] maxVert, minVert;
        GLUvertex vHead = tess.mesh.vHead;
        int i;

        maxVal = new double[3];
        minVal = new double[3];
        minVert = new GLUvertex[3];
        maxVert = new GLUvertex[3];
        d1 = new double[3];
        d2 = new double[3];
        tNorm = new double[3];

        maxVal[0] = maxVal[1] = maxVal[2] = -2 * GLU.TESS_MAX_COORD;
        minVal[0] = minVal[1] = minVal[2] = 2 * GLU.TESS_MAX_COORD;

        for (v = vHead.next; v != vHead; v = v.next) {
            for (i = 0; i < 3; ++i) {
                c = v.coords[i];
                if (c < minVal[i]) {
                    minVal[i] = c;
                    minVert[i] = v;
                }
                if (c > maxVal[i]) {
                    maxVal[i] = c;
                    maxVert[i] = v;
                }
            }
        }

        /*
         * Find two vertices separated by at least 1/sqrt(3) of the maximum distance between any two vertices
         */
        i = 0;
        if (maxVal[1] - minVal[1] > maxVal[0] - minVal[0]) {
            i = 1;
        }
        if (maxVal[2] - minVal[2] > maxVal[i] - minVal[i]) {
            i = 2;
        }
        if (minVal[i] >= maxVal[i]) {
            /* All vertices are the same -- normal doesn't matter */
            norm[0] = 0;
            norm[1] = 0;
            norm[2] = 1;
            return;
        }

        /*
         * Look for a third vertex which forms the triangle with maximum area (Length of normal == twice the triangle
         * area)
         */
        maxLen2 = 0;
        v1 = minVert[i];
        v2 = maxVert[i];
        d1[0] = v1.coords[0] - v2.coords[0];
        d1[1] = v1.coords[1] - v2.coords[1];
        d1[2] = v1.coords[2] - v2.coords[2];
        for (v = vHead.next; v != vHead; v = v.next) {
            d2[0] = v.coords[0] - v2.coords[0];
            d2[1] = v.coords[1] - v2.coords[1];
            d2[2] = v.coords[2] - v2.coords[2];
            tNorm[0] = d1[1] * d2[2] - d1[2] * d2[1];
            tNorm[1] = d1[2] * d2[0] - d1[0] * d2[2];
            tNorm[2] = d1[0] * d2[1] - d1[1] * d2[0];
            tLen2 = tNorm[0] * tNorm[0] + tNorm[1] * tNorm[1] + tNorm[2] * tNorm[2];
            if (tLen2 > maxLen2) {
                maxLen2 = tLen2;
                norm[0] = tNorm[0];
                norm[1] = tNorm[1];
                norm[2] = tNorm[2];
            }
        }

        if (maxLen2 <= 0) {
            /* All points lie on a single line -- any decent normal will do */
            norm[0] = norm[1] = norm[2] = 0;
            norm[LongAxis(d1)] = 1;
        }
    }

    static void CheckOrientation(GLUtessellatorImpl tess) {
        double area;
        GLUface f, fHead = tess.mesh.fHead;
        GLUvertex v, vHead = tess.mesh.vHead;
        GLUhalfEdge e;

        /*
         * When we compute the normal automatically, we choose the orientation so that the the sum of the signed areas
         * of all contours is non-negative.
         */
        area = 0;
        for (f = fHead.next; f != fHead; f = f.next) {
            e = f.anEdge;
            if (e.winding <= 0) continue;
            do {
                area += (e.Org.s - e.Sym.Org.s) * (e.Org.t + e.Sym.Org.t);
                e = e.Lnext;
            } while (e != f.anEdge);
        }
        if (area < 0) {
            /* Reverse the orientation by flipping all the t-coordinates */
            for (v = vHead.next; v != vHead; v = v.next) {
                v.t = -v.t;
            }
            tess.tUnit[0] = -tess.tUnit[0];
            tess.tUnit[1] = -tess.tUnit[1];
            tess.tUnit[2] = -tess.tUnit[2];
        }
    }

    /*
     * Determine the polygon normal and project vertices onto the plane of the polygon.
     */
    public static void __gl_projectPolygon(GLUtessellatorImpl tess) {
        GLUvertex v, vHead = tess.mesh.vHead;
        double w;
        double[] norm = new double[3];
        double[] sUnit, tUnit;
        int i;
        boolean computedNormal = false;

        norm[0] = tess.normal[0];
        norm[1] = tess.normal[1];
        norm[2] = tess.normal[2];
        if (norm[0] == 0 && norm[1] == 0 && norm[2] == 0) {
            ComputeNormal(tess, norm);
            computedNormal = true;
        }
        sUnit = tess.sUnit;
        tUnit = tess.tUnit;
        i = LongAxis(norm);

        if (TRUE_PROJECT) {
            /*
             * Choose the initial sUnit vector to be approximately perpendicular to the normal.
             */
            Normalize(norm);

            sUnit[i] = 0;
            sUnit[(i + 1) % 3] = S_UNIT_X;
            sUnit[(i + 2) % 3] = S_UNIT_Y;

            /* Now make it exactly perpendicular */
            w = Dot(sUnit, norm);
            sUnit[0] -= w * norm[0];
            sUnit[1] -= w * norm[1];
            sUnit[2] -= w * norm[2];
            Normalize(sUnit);

            /* Choose tUnit so that (sUnit,tUnit,norm) form a right-handed frame */
            tUnit[0] = norm[1] * sUnit[2] - norm[2] * sUnit[1];
            tUnit[1] = norm[2] * sUnit[0] - norm[0] * sUnit[2];
            tUnit[2] = norm[0] * sUnit[1] - norm[1] * sUnit[0];
            Normalize(tUnit);
        } else {
            /* Project perpendicular to a coordinate axis -- better numerically */
            sUnit[i] = 0;
            sUnit[(i + 1) % 3] = S_UNIT_X;
            sUnit[(i + 2) % 3] = S_UNIT_Y;

            tUnit[i] = 0;
            tUnit[(i + 1) % 3] = (norm[i] > 0) ? -S_UNIT_Y : S_UNIT_Y;
            tUnit[(i + 2) % 3] = (norm[i] > 0) ? S_UNIT_X : -S_UNIT_X;
        }

        /* Project the vertices onto the sweep plane */
        for (v = vHead.next; v != vHead; v = v.next) {
            v.s = Dot(v.coords, sUnit);
            v.t = Dot(v.coords, tUnit);
        }
        if (computedNormal) {
            CheckOrientation(tess);
        }
    }
}
