package org.lwjglx.util.glu.tessellation;

abstract class PriorityQ {

    public static final int INIT_SIZE = 32;

    public static class PQnode {

        int handle;
    }

    public static class PQhandleElem {

        Object key;
        int node;
    }

    public interface Leq {

        boolean leq(Object key1, Object key2);
    }

    // #ifdef FOR_TRITE_TEST_PROGRAM
    // private static boolean LEQ(PriorityQCommon.Leq leq, Object x,Object y) {
    // return pq.leq.leq(x,y);
    // }
    // #else
    /* Violates modularity, but a little faster */
    // #include "geom.h"
    public static boolean LEQ(Leq leq, Object x, Object y) {
        return Geom.VertLeq((GLUvertex) x, (GLUvertex) y);
    }

    static PriorityQ pqNewPriorityQ(Leq leq) {
        return new PriorityQSort(leq);
    }

    abstract void pqDeletePriorityQ();

    abstract boolean pqInit();

    abstract int pqInsert(Object keyNew);

    abstract Object pqExtractMin();

    abstract void pqDelete(int hCurr);

    abstract Object pqMinimum();

    abstract boolean pqIsEmpty();
    // #endif
}
