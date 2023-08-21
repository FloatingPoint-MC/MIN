package org.lwjglx.util.glu.tessellation;

class Geom {

    private Geom() {}

    /*
     * Given three vertices u,v,w such that VertLeq(u,v) && VertLeq(v,w), evaluates the t-coord of the edge uw at the
     * s-coord of the vertex v. Returns v->t - (uw)(v->s), ie. the signed distance from uw to v. If uw is vertical (and
     * thus passes thru v), the result is zero. The calculation is extremely accurate and stable, even when v is very
     * close to u or w. In particular if we set v->t = 0 and let r be the negated result (this evaluates (uw)(v->s)),
     * then r is guaranteed to satisfy MIN(u->t,w->t) <= r <= MAX(u->t,w->t).
     */
    static double EdgeEval(GLUvertex u, GLUvertex v, GLUvertex w) {
        double gapL, gapR;

        assert (VertLeq(u, v) && VertLeq(v, w));

        gapL = v.s - u.s;
        gapR = w.s - v.s;

        if (gapL + gapR > 0) {
            if (gapL < gapR) {
                return (v.t - u.t) + (u.t - w.t) * (gapL / (gapL + gapR));
            } else {
                return (v.t - w.t) + (w.t - u.t) * (gapR / (gapL + gapR));
            }
        }
        /* vertical line */
        return 0;
    }

    static double EdgeSign(GLUvertex u, GLUvertex v, GLUvertex w) {
        double gapL, gapR;

        assert (VertLeq(u, v) && VertLeq(v, w));

        gapL = v.s - u.s;
        gapR = w.s - v.s;

        if (gapL + gapR > 0) {
            return (v.t - w.t) * gapL + (v.t - u.t) * gapR;
        }
        /* vertical line */
        return 0;
    }

    /***********************************************************************
     * Define versions of EdgeSign, EdgeEval with s and t transposed.
     */
    static double TransEval(GLUvertex u, GLUvertex v, GLUvertex w) {
        /*
         * Given three vertices u,v,w such that TransLeq(u,v) && TransLeq(v,w), evaluates the t-coord of the edge uw at
         * the s-coord of the vertex v. Returns v->s - (uw)(v->t), ie. the signed distance from uw to v. If uw is
         * vertical (and thus passes thru v), the result is zero. The calculation is extremely accurate and stable, even
         * when v is very close to u or w. In particular if we set v->s = 0 and let r be the negated result (this
         * evaluates (uw)(v->t)), then r is guaranteed to satisfy MIN(u->s,w->s) <= r <= MAX(u->s,w->s).
         */
        double gapL, gapR;

        assert (TransLeq(u, v) && TransLeq(v, w));

        gapL = v.t - u.t;
        gapR = w.t - v.t;

        if (gapL + gapR > 0) {
            if (gapL < gapR) {
                return (v.s - u.s) + (u.s - w.s) * (gapL / (gapL + gapR));
            } else {
                return (v.s - w.s) + (w.s - u.s) * (gapR / (gapL + gapR));
            }
        }
        /* vertical line */
        return 0;
    }

    static double TransSign(GLUvertex u, GLUvertex v, GLUvertex w) {
        /*
         * Returns a number whose sign matches TransEval(u,v,w) but which is cheaper to evaluate. Returns > 0, == 0 , or
         * < 0 as v is above, on, or below the edge uw.
         */
        double gapL, gapR;

        assert (TransLeq(u, v) && TransLeq(v, w));

        gapL = v.t - u.t;
        gapR = w.t - v.t;

        if (gapL + gapR > 0) {
            return (v.s - w.s) * gapL + (v.s - u.s) * gapR;
        }
        /* vertical line */
        return 0;
    }

    /*
     * Given parameters a,x,b,y returns the value (b*x+a*y)/(a+b), or (x+y)/2 if a==b==0. It requires that a,b >= 0, and
     * enforces this in the rare case that one argument is slightly negative. The implementation is extremely stable
     * numerically. In particular it guarantees that the result r satisfies MIN(x,y) <= r <= MAX(x,y), and the results
     * are very accurate even when a and b differ greatly in magnitude.
     */
    static double Interpolate(double a, double x, double b, double y) {
        a = (a < 0) ? 0 : a;
        b = (b < 0) ? 0 : b;
        if (a <= b) {
            if (b == 0) {
                return (x + y) / 2.0;
            } else {
                return (x + (y - x) * (a / (a + b)));
            }
        } else {
            return (y + (x - y) * (b / (a + b)));
        }
    }

    static void EdgeIntersect(GLUvertex o1, GLUvertex d1, GLUvertex o2, GLUvertex d2, GLUvertex v)
    /*
     * Given edges (o1,d1) and (o2,d2), compute their point of intersection. The computed point is guaranteed to lie in
     * the intersection of the bounding rectangles defined by each edge.
     */ {
        double z1, z2;

        /*
         * This is certainly not the most efficient way to find the intersection of two line segments, but it is very
         * numerically stable. Strategy: find the two middle vertices in the VertLeq ordering, and interpolate the
         * intersection s-value from these. Then repeat using the TransLeq ordering to find the intersection t-value.
         */

        if (!VertLeq(o1, d1)) {
            GLUvertex temp = o1;
            o1 = d1;
            d1 = temp;
        }
        if (!VertLeq(o2, d2)) {
            GLUvertex temp = o2;
            o2 = d2;
            d2 = temp;
        }
        if (!VertLeq(o1, o2)) {
            GLUvertex temp = o1;
            o1 = o2;
            o2 = temp;
            temp = d1;
            d1 = d2;
            d2 = temp;
        }

        if (!VertLeq(o2, d1)) {
            /* Technically, no intersection -- do our best */
            v.s = (o2.s + d1.s) / 2.0;
        } else if (VertLeq(d1, d2)) {
            /* Interpolate between o2 and d1 */
            z1 = EdgeEval(o1, o2, d1);
            z2 = EdgeEval(o2, d1, d2);
            if (z1 + z2 < 0) {
                z1 = -z1;
                z2 = -z2;
            }
            v.s = Interpolate(z1, o2.s, z2, d1.s);
        } else {
            /* Interpolate between o2 and d2 */
            z1 = EdgeSign(o1, o2, d1);
            z2 = -EdgeSign(o1, d2, d1);
            if (z1 + z2 < 0) {
                z1 = -z1;
                z2 = -z2;
            }
            v.s = Interpolate(z1, o2.s, z2, d2.s);
        }

        /* Now repeat the process for t */

        if (!TransLeq(o1, d1)) {
            GLUvertex temp = o1;
            o1 = d1;
            d1 = temp;
        }
        if (!TransLeq(o2, d2)) {
            GLUvertex temp = o2;
            o2 = d2;
            d2 = temp;
        }
        if (!TransLeq(o1, o2)) {
            GLUvertex temp = o2;
            o2 = o1;
            o1 = temp;
            temp = d2;
            d2 = d1;
            d1 = temp;
        }

        if (!TransLeq(o2, d1)) {
            /* Technically, no intersection -- do our best */
            v.t = (o2.t + d1.t) / 2.0;
        } else if (TransLeq(d1, d2)) {
            /* Interpolate between o2 and d1 */
            z1 = TransEval(o1, o2, d1);
            z2 = TransEval(o2, d1, d2);
            if (z1 + z2 < 0) {
                z1 = -z1;
                z2 = -z2;
            }
            v.t = Interpolate(z1, o2.t, z2, d1.t);
        } else {
            /* Interpolate between o2 and d2 */
            z1 = TransSign(o1, o2, d1);
            z2 = -TransSign(o1, d2, d1);
            if (z1 + z2 < 0) {
                z1 = -z1;
                z2 = -z2;
            }
            v.t = Interpolate(z1, o2.t, z2, d2.t);
        }
    }

    static boolean VertEq(GLUvertex u, GLUvertex v) {
        return u.s == v.s && u.t == v.t;
    }

    static boolean VertLeq(GLUvertex u, GLUvertex v) {
        return u.s < v.s || (u.s == v.s && u.t <= v.t);
    }

    /* Versions of VertLeq, EdgeSign, EdgeEval with s and t transposed. */

    static boolean TransLeq(GLUvertex u, GLUvertex v) {
        return u.t < v.t || (u.t == v.t && u.s <= v.s);
    }

    static boolean EdgeGoesLeft(GLUhalfEdge e) {
        return VertLeq(e.Sym.Org, e.Org);
    }

    static boolean EdgeGoesRight(GLUhalfEdge e) {
        return VertLeq(e.Org, e.Sym.Org);
    }

    static double VertL1dist(GLUvertex u, GLUvertex v) {
        return Math.abs(u.s - v.s) + Math.abs(u.t - v.t);
    }
}
