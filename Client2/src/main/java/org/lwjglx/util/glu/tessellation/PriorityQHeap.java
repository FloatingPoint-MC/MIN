package org.lwjglx.util.glu.tessellation;

class PriorityQHeap extends PriorityQ {

    PQnode[] nodes;
    PQhandleElem[] handles;
    int size, max;
    int freeList;
    boolean initialized;
    Leq leq;

    /* really __gl_pqHeapNewPriorityQ */
    PriorityQHeap(Leq leq) {
        size = 0;
        max = PriorityQ.INIT_SIZE;
        nodes = new PQnode[PriorityQ.INIT_SIZE + 1];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new PQnode();
        }
        handles = new PQhandleElem[PriorityQ.INIT_SIZE + 1];
        for (int i = 0; i < handles.length; i++) {
            handles[i] = new PQhandleElem();
        }
        initialized = false;
        freeList = 0;
        this.leq = leq;

        nodes[1].handle = 1; /* so that Minimum() returns NULL */
        handles[1].key = null;
    }

    /* really __gl_pqHeapDeletePriorityQ */
    void pqDeletePriorityQ() {
        handles = null;
        nodes = null;
    }

    void FloatDown(int curr) {
        PQnode[] n = nodes;
        PQhandleElem[] h = handles;
        int hCurr, hChild;
        int child;

        hCurr = n[curr].handle;
        for (;;) {
            child = curr << 1;
            if (child < size && LEQ(leq, h[n[child + 1].handle].key, h[n[child].handle].key)) {
                ++child;
            }

            assert (child <= max);

            hChild = n[child].handle;
            if (child > size || LEQ(leq, h[hCurr].key, h[hChild].key)) {
                n[curr].handle = hCurr;
                h[hCurr].node = curr;
                break;
            }
            n[curr].handle = hChild;
            h[hChild].node = curr;
            curr = child;
        }
    }

    void FloatUp(int curr) {
        PQnode[] n = nodes;
        PQhandleElem[] h = handles;
        int hCurr, hParent;
        int parent;

        hCurr = n[curr].handle;
        for (;;) {
            parent = curr >> 1;
            hParent = n[parent].handle;
            if (parent == 0 || LEQ(leq, h[hParent].key, h[hCurr].key)) {
                n[curr].handle = hCurr;
                h[hCurr].node = curr;
                break;
            }
            n[curr].handle = hParent;
            h[hParent].node = curr;
            curr = parent;
        }
    }

    /* really __gl_pqHeapInit */
    boolean pqInit() {
        int i;

        /* This method of building a heap is O(n), rather than O(n lg n). */

        for (i = size; i >= 1; --i) {
            FloatDown(i);
        }
        initialized = true;

        return true;
    }

    /* really __gl_pqHeapInsert */
    /* returns LONG_MAX iff out of memory */
    int pqInsert(Object keyNew) {
        int curr;
        int free;

        curr = ++size;
        if ((curr * 2) > max) {
            PQnode[] saveNodes = nodes;
            PQhandleElem[] saveHandles = handles;

            /* If the heap overflows, double its size. */
            max <<= 1;
            // pq->nodes = (PQnode *)memRealloc( pq->nodes, (size_t) ((pq->max + 1) * sizeof( pq->nodes[0]
            // )));
            PQnode[] pqNodes = new PQnode[max + 1];
            System.arraycopy(nodes, 0, pqNodes, 0, nodes.length);
            for (int i = nodes.length; i < pqNodes.length; i++) {
                pqNodes[i] = new PQnode();
            }
            nodes = pqNodes;
            if (nodes == null) {
                nodes = saveNodes; /* restore ptr to free upon return */
                return Integer.MAX_VALUE;
            }

            // pq->handles = (PQhandleElem *)memRealloc( pq->handles,(size_t)((pq->max + 1) * sizeof(
            // pq->handles[0] )));
            PQhandleElem[] pqHandles = new PQhandleElem[max + 1];
            System.arraycopy(handles, 0, pqHandles, 0, handles.length);
            for (int i = handles.length; i < pqHandles.length; i++) {
                pqHandles[i] = new PQhandleElem();
            }
            handles = pqHandles;
            if (handles == null) {
                handles = saveHandles; /* restore ptr to free upon return */
                return Integer.MAX_VALUE;
            }
        }

        if (freeList == 0) {
            free = curr;
        } else {
            free = freeList;
            freeList = handles[free].node;
        }

        nodes[curr].handle = free;
        handles[free].node = curr;
        handles[free].key = keyNew;

        if (initialized) {
            FloatUp(curr);
        }
        assert (free != Integer.MAX_VALUE);
        return free;
    }

    /* really __gl_pqHeapExtractMin */
    Object pqExtractMin() {
        PQnode[] n = nodes;
        PQhandleElem[] h = handles;
        int hMin = n[1].handle;
        Object min = h[hMin].key;

        if (size > 0) {
            n[1].handle = n[size].handle;
            h[n[1].handle].node = 1;

            h[hMin].key = null;
            h[hMin].node = freeList;
            freeList = hMin;

            if (--size > 0) {
                FloatDown(1);
            }
        }
        return min;
    }

    /* really __gl_pqHeapDelete */
    void pqDelete(int hCurr) {
        PQnode[] n = nodes;
        PQhandleElem[] h = handles;
        int curr;

        assert (hCurr >= 1 && hCurr <= max && h[hCurr].key != null);

        curr = h[hCurr].node;
        n[curr].handle = n[size].handle;
        h[n[curr].handle].node = curr;

        if (curr <= --size) {
            if (curr <= 1 || LEQ(leq, h[n[curr >> 1].handle].key, h[n[curr].handle].key)) {
                FloatDown(curr);
            } else {
                FloatUp(curr);
            }
        }
        h[hCurr].key = null;
        h[hCurr].node = freeList;
        freeList = hCurr;
    }

    Object pqMinimum() {
        return handles[nodes[1].handle].key;
    }

    boolean pqIsEmpty() {
        return size == 0;
    }
}
