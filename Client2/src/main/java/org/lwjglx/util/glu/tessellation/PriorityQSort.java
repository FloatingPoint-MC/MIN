package org.lwjglx.util.glu.tessellation;

class PriorityQSort extends PriorityQ {

    PriorityQHeap heap;
    Object[] keys;

    // JAVA: 'order' contains indices into the keys array.
    // This simulates the indirect pointers used in the original C code
    // (from Frank Suykens, Luciad.com).
    int[] order;
    int size, max;
    boolean initialized;
    Leq leq;

    PriorityQSort(Leq leq) {
        heap = new PriorityQHeap(leq);

        keys = new Object[PriorityQ.INIT_SIZE];

        size = 0;
        max = PriorityQ.INIT_SIZE;
        initialized = false;
        this.leq = leq;
    }

    /* really __gl_pqSortDeletePriorityQ */
    void pqDeletePriorityQ() {
        if (heap != null) heap.pqDeletePriorityQ();
        order = null;
        keys = null;
    }

    private static boolean LT(Leq leq, Object x, Object y) {
        return (!PriorityQHeap.LEQ(leq, y, x));
    }

    private static boolean GT(Leq leq, Object x, Object y) {
        return (!PriorityQHeap.LEQ(leq, x, y));
    }

    private static void Swap(int[] array, int a, int b) {
        if (true) {
            int tmp = array[a];
            array[a] = array[b];
            array[b] = tmp;
        }
    }

    private static class Stack {

        int p, r;
    }

    /* really __gl_pqSortInit */
    boolean pqInit() {
        int p, r, i, j;
        int piv;
        Stack[] stack = new Stack[50];
        for (int k = 0; k < stack.length; k++) {
            stack[k] = new Stack();
        }
        int top = 0;

        int seed = 2016473283;

        /*
         * Create an array of indirect pointers to the keys, so that we the handles we have returned are still valid.
         */
        order = new int[size + 1];
        /* the previous line is a patch to compensate for the fact that IBM */
        /* machines return a null on a malloc of zero bytes (unlike SGI), */
        /* so we have to put in this defense to guard against a memory */
        /* fault four lines down. from fossum@austin.ibm.com. */
        p = 0;
        r = size - 1;
        for (piv = 0, i = p; i <= r; ++piv, ++i) {
            // indirect pointers: keep an index into the keys array, not a direct pointer to its contents
            order[i] = piv;
        }

        /*
         * Sort the indirect pointers in descending order, using randomized Quicksort
         */
        stack[top].p = p;
        stack[top].r = r;
        ++top;
        while (--top >= 0) {
            p = stack[top].p;
            r = stack[top].r;
            while (r > p + 10) {
                seed = Math.abs(seed * 1539415821 + 1);
                i = p + seed % (r - p + 1);
                piv = order[i];
                order[i] = order[p];
                order[p] = piv;
                i = p - 1;
                j = r + 1;
                do {
                    do {
                        ++i;
                    } while (GT(leq, keys[order[i]], keys[piv]));
                    do {
                        --j;
                    } while (LT(leq, keys[order[j]], keys[piv]));
                    Swap(order, i, j);
                } while (i < j);
                Swap(order, i, j); /* Undo last swap */
                if (i - p < r - j) {
                    stack[top].p = j + 1;
                    stack[top].r = r;
                    ++top;
                    r = i - 1;
                } else {
                    stack[top].p = p;
                    stack[top].r = i - 1;
                    ++top;
                    p = j + 1;
                }
            }
            /* Insertion sort small lists */
            for (i = p + 1; i <= r; ++i) {
                piv = order[i];
                for (j = i; j > p && LT(leq, keys[order[j - 1]], keys[piv]); --j) {
                    order[j] = order[j - 1];
                }
                order[j] = piv;
            }
        }
        max = size;
        initialized = true;
        heap.pqInit(); /* always succeeds */

        /*
         * #ifndef NDEBUG p = order; r = p + size - 1; for (i = p; i < r; ++i) { Assertion.doAssert(LEQ( * * (i + 1),
         * **i )); } #endif
         */

        return true;
    }

    /* really __gl_pqSortInsert */
    /* returns LONG_MAX iff out of memory */
    int pqInsert(Object keyNew) {
        int curr;

        if (initialized) {
            return heap.pqInsert(keyNew);
        }
        curr = size;
        if (++size >= max) {
            Object[] saveKey = keys;

            /* If the heap overflows, double its size. */
            max <<= 1;
            // pq->keys = (PQHeapKey *)memRealloc( pq->keys,(size_t)(pq->max * sizeof( pq->keys[0] )));
            Object[] pqKeys = new Object[max];
            System.arraycopy(keys, 0, pqKeys, 0, keys.length);
            keys = pqKeys;
            if (keys == null) {
                keys = saveKey; /* restore ptr to free upon return */
                return Integer.MAX_VALUE;
            }
        }
        assert curr != Integer.MAX_VALUE;
        keys[curr] = keyNew;

        /* Negative handles index the sorted array. */
        return -(curr + 1);
    }

    /* really __gl_pqSortExtractMin */
    Object pqExtractMin() {
        Object sortMin, heapMin;

        if (size == 0) {
            return heap.pqExtractMin();
        }
        sortMin = keys[order[size - 1]];
        if (!heap.pqIsEmpty()) {
            heapMin = heap.pqMinimum();
            if (LEQ(leq, heapMin, sortMin)) {
                return heap.pqExtractMin();
            }
        }
        do {
            --size;
        } while (size > 0 && keys[order[size - 1]] == null);
        return sortMin;
    }

    /* really __gl_pqSortMinimum */
    Object pqMinimum() {
        Object sortMin, heapMin;

        if (size == 0) {
            return heap.pqMinimum();
        }
        sortMin = keys[order[size - 1]];
        if (!heap.pqIsEmpty()) {
            heapMin = heap.pqMinimum();
            if (PriorityQHeap.LEQ(leq, heapMin, sortMin)) {
                return heapMin;
            }
        }
        return sortMin;
    }

    /* really __gl_pqSortIsEmpty */
    boolean pqIsEmpty() {
        return (size == 0) && heap.pqIsEmpty();
    }

    /* really __gl_pqSortDelete */
    void pqDelete(int curr) {
        if (curr >= 0) {
            heap.pqDelete(curr);
            return;
        }
        curr = -(curr + 1);
        assert curr < max && keys[curr] != null;

        keys[curr] = null;
        while (size > 0 && keys[order[size - 1]] == null) {
            --size;
        }
    }
}
