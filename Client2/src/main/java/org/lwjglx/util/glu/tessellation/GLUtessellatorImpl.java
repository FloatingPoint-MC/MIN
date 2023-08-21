package org.lwjglx.util.glu.tessellation;

import static org.lwjglx.util.glu.GLU.*;

import org.lwjglx.util.glu.GLUtessellator;
import org.lwjglx.util.glu.GLUtessellatorCallback;
import org.lwjglx.util.glu.GLUtessellatorCallbackAdapter;

import java.util.concurrent.atomic.AtomicReference;

public class GLUtessellatorImpl implements GLUtessellator {

    public static final int TESS_MAX_CACHE = 100;

    private int state; /* what begin/end calls have we seen? */

    private GLUhalfEdge lastEdge; /* lastEdge->Org is the most recent vertex */
    GLUmesh mesh; /*
                   * stores the input contours, and eventually the tessellation itself
                   */

    /*** state needed for projecting onto the sweep plane ***/
    double[] normal = new double[3]; /* user-specified normal (if provided) */

    double[] sUnit = new double[3]; /* unit vector in s-direction (debugging) */
    double[] tUnit = new double[3]; /* unit vector in t-direction (debugging) */

    int windingRule; /* rule for determining polygon interior */
    boolean fatalError; /* fatal error: needed combine callback */

    Dict dict; /* edge dictionary for sweep line */
    PriorityQ pq; /* priority queue of vertex events */
    GLUvertex event; /* current sweep event being processed */

    /*** state needed for rendering callbacks (see render.c) ***/
    boolean flagBoundary; /* mark boundary edges (use EdgeFlag) */

    boolean boundaryOnly; /* Extract contours, not triangles */
    GLUface lonelyTriList;
    /* list of triangles which could not be rendered as strips or fans */

    /*** state needed to cache single-contour polygons for renderCache() */
    private boolean flushCacheOnNextVertex; /* empty cache on next vertex() call */

    int cacheCount; /* number of cached vertices */
    CachedVertex[] cache = new CachedVertex[TESS_MAX_CACHE]; /* the vertex data */

    /*** rendering callbacks that also pass polygon data ***/
    private Object polygonData; /* client data for current polygon */

    private final AtomicReference<GLUtessellatorCallback> callBegin = new AtomicReference<>();
    private final GLUtessellatorCallback callEdgeFlag;
    private final GLUtessellatorCallback callVertex;
    private final GLUtessellatorCallback callEnd;
    // private GLUtessellatorCallback callMesh;
    private final GLUtessellatorCallback callError;
    private final GLUtessellatorCallback callCombine;

    private final GLUtessellatorCallback callBeginData;
    private final GLUtessellatorCallback callEdgeFlagData;
    private final GLUtessellatorCallback callVertexData;
    private final GLUtessellatorCallback callEndData;
    // private GLUtessellatorCallback callMeshData;
    private final GLUtessellatorCallback callErrorData;
    private final GLUtessellatorCallback callCombineData;
    // private static final int GLU_TESS_MESH = 100112; /* void (*)(GLUmesh *mesh) */
    private static final GLUtessellatorCallback NULL_CB = new GLUtessellatorCallbackAdapter();

    // #define MAX_FAST_ALLOC (MAX(sizeof(EdgePair), \
    // MAX(sizeof(GLUvertex),sizeof(GLUface))))

    public GLUtessellatorImpl() {
        state = TessState.T_DORMANT;

        normal[0] = 0;
        normal[1] = 0;
        normal[2] = 0;

        windingRule = GLU_TESS_WINDING_ODD;
        flagBoundary = false;
        boundaryOnly = false;

        callBegin.set(NULL_CB);
        callEdgeFlag = NULL_CB;
        callVertex = NULL_CB;
        callEnd = NULL_CB;
        callError = NULL_CB;
        callCombine = NULL_CB;
        // callMesh = NULL_CB;

        callBeginData = NULL_CB;
        callEdgeFlagData = NULL_CB;
        callVertexData = NULL_CB;
        callEndData = NULL_CB;
        callErrorData = NULL_CB;
        callCombineData = NULL_CB;

        polygonData = null;

        for (int i = 0; i < cache.length; i++) {
            cache[i] = new CachedVertex();
        }
    }

    private void makeDormant() {
        /* Return the tessellator to its original dormant state. */

        if (mesh != null) {
            Mesh.__gl_meshDeleteMesh(mesh);
        }
        state = TessState.T_DORMANT;
        lastEdge = null;
        mesh = null;
    }

    private void requireState(int newState) {
        if (state != newState) gotoState(newState);
    }

    private void gotoState(int newState) {
        while (state != newState) {
            /*
             * We change the current state one level at a time, to get to the desired state.
             */
            if (state < newState) {
                if (state == TessState.T_DORMANT) {
                    callErrorOrErrorData(GLU_TESS_MISSING_BEGIN_POLYGON);
                    gluTessBeginPolygon(null);
                } else if (state == TessState.T_IN_POLYGON) {
                    callErrorOrErrorData(GLU_TESS_MISSING_BEGIN_CONTOUR);
                    gluTessBeginContour();
                }
            } else {
                if (state == TessState.T_IN_CONTOUR) {
                    callErrorOrErrorData(GLU_TESS_MISSING_END_CONTOUR);
                    gluTessEndContour();
                } else if (state == TessState.T_IN_POLYGON) {
                    callErrorOrErrorData(GLU_TESS_MISSING_END_POLYGON);
                    /* gluTessEndPolygon( tess ) is too much work! */
                    makeDormant();
                }
            }
        }
    }

    private boolean addVertex(double[] coords, Object vertexData) {
        GLUhalfEdge e;

        e = lastEdge;
        if (e == null) {
            /* Make a self-loop (one vertex, one edge). */

            e = Mesh.__gl_meshMakeEdge(mesh);
            if (e == null) return true;
            if (!Mesh.__gl_meshSplice(e, e.Sym)) return true;
        } else {
            /*
             * Create a new vertex and edge which immediately follow e in the ordering around the left face.
             */
            if (Mesh.__gl_meshSplitEdge(e) == null) return true;
            e = e.Lnext;
        }

        /* The new vertex is now e.Org. */
        e.Org.data = vertexData;
        e.Org.coords[0] = coords[0];
        e.Org.coords[1] = coords[1];
        e.Org.coords[2] = coords[2];

        /*
         * The winding of an edge says how the winding number changes as we cross from the edge''s right face to its
         * left face. We add the vertices in such an order that a CCW contour will add +1 to the winding number of the
         * region inside the contour.
         */
        e.winding = 1;
        e.Sym.winding = -1;

        lastEdge = e;

        return false;
    }

    private void cacheVertex(double[] coords, Object vertexData) {
        if (cache[cacheCount] == null) {
            cache[cacheCount] = new CachedVertex();
        }

        CachedVertex v = cache[cacheCount];

        v.data = vertexData;
        v.coords[0] = coords[0];
        v.coords[1] = coords[1];
        v.coords[2] = coords[2];
        ++cacheCount;
    }

    private boolean flushCache() {
        CachedVertex[] v = cache;

        mesh = Mesh.__gl_meshNewMesh();
        if (mesh == null) return true;

        for (int i = 0; i < cacheCount; i++) {
            CachedVertex vertex = v[i];
            if (addVertex(vertex.coords, vertex.data)) return true;
        }
        cacheCount = 0;
        flushCacheOnNextVertex = false;

        return false;
    }

    public void gluTessBeginPolygon(Object data) {
        requireState(TessState.T_DORMANT);

        state = TessState.T_IN_POLYGON;
        cacheCount = 0;
        flushCacheOnNextVertex = false;
        mesh = null;

        polygonData = data;
    }

    public void gluTessBeginContour() {
        requireState(TessState.T_IN_POLYGON);

        state = TessState.T_IN_CONTOUR;
        lastEdge = null;
        if (cacheCount > 0) {
            /*
             * Just set a flag so we don't get confused by empty contours -- these can be generated accidentally with
             * the obsolete NextContour() interface.
             */
            flushCacheOnNextVertex = true;
        }
    }

    public void gluTessEndContour() {
        requireState(TessState.T_IN_CONTOUR);
        state = TessState.T_IN_POLYGON;
    }

    public void gluTessEndPolygon() {
        GLUmesh mesh;

        try {
            requireState(TessState.T_IN_POLYGON);
            state = TessState.T_DORMANT;

            if (this.mesh == null) {
                if (!flagBoundary /* && callMesh == NULL_CB */) {

                    /*
                     * Try some special code to make the easy cases go quickly (eg. convex polygons). This code does NOT
                     * handle multiple contours, intersections, edge flags, and of course it does not generate an
                     * explicit mesh either.
                     */
                    if (Render.__gl_renderCache(this)) {
                        polygonData = null;
                        return;
                    }
                }
                if (flushCache()) throw new RuntimeException(); /* could've used a label */
            }

            /*
             * Determine the polygon normal and project vertices onto the plane of the polygon.
             */
            Normal.__gl_projectPolygon(this);

            /*
             * __gl_computeInterior( tess ) computes the planar arrangement specified by the given contours, and further
             * subdivides this arrangement into regions. Each region is marked "inside" if it belongs to the polygon,
             * according to the rule given by windingRule. Each interior region is guaranteed be monotone.
             */
            if (!Sweep.__gl_computeInterior(this)) {
                throw new RuntimeException(); /* could've used a label */
            }

            mesh = this.mesh;
            if (!fatalError) {
                boolean rc = true;

                /*
                 * If the user wants only the boundary contours, we throw away all edges except those which separate the
                 * interior from the exterior. Otherwise we tessellate all the regions marked "inside".
                 */
                if (boundaryOnly) {
                    rc = TessMono.__gl_meshSetWindingNumber(mesh, 1, true);
                } else {
                    rc = TessMono.__gl_meshTessellateInterior(mesh);
                }
                if (!rc) throw new RuntimeException(); /* could've used a label */

                Mesh.__gl_meshCheckMesh(mesh);

                if (callBegin != NULL_CB || callEnd != NULL_CB
                        || callVertex != NULL_CB
                        || callEdgeFlag != NULL_CB
                        || callBeginData != NULL_CB
                        || callEndData != NULL_CB
                        || callVertexData != NULL_CB
                        || callEdgeFlagData != NULL_CB) {
                    if (boundaryOnly) {
                        Render.__gl_renderBoundary(this, mesh); /* output boundary contours */
                    } else {
                        Render.__gl_renderMesh(this, mesh); /* output strips and fans */
                    }
                }
                // if (callMesh != NULL_CB) {
                //
                /// * Throw away the exterior faces, so that all faces are interior.
                // * This way the user doesn't have to check the "inside" flag,
                // * and we don't need to even reveal its existence. It also leaves
                // * the freedom for an implementation to not generate the exterior
                // * faces in the first place.
                // */
                // TessMono.__gl_meshDiscardExterior(mesh);
                // callMesh.mesh(mesh); /* user wants the mesh itself */
                // mesh = null;
                // polygonData = null;
                // return;
                // }
            }
            Mesh.__gl_meshDeleteMesh(mesh);
            polygonData = null;
        } catch (Exception e) {
            e.printStackTrace();
            callErrorOrErrorData(GLU_OUT_OF_MEMORY);
        }
    }

    /*******************************************************/

    /* Obsolete calls -- for backward compatibility */

    public void gluBeginPolygon() {
        gluTessBeginPolygon(null);
        gluTessBeginContour();
    }

    void callBeginOrBeginData(int a) {
        if (callBeginData != NULL_CB) callBeginData.beginData(a, polygonData);
        else callBegin.get().begin(a);
    }

    void callVertexOrVertexData(Object a) {
        if (callVertexData != NULL_CB) callVertexData.vertexData(a, polygonData);
        else callVertex.vertex(a);
    }

    void callEdgeFlagOrEdgeFlagData(boolean a) {
        if (callEdgeFlagData != NULL_CB) callEdgeFlagData.edgeFlagData(a, polygonData);
        else callEdgeFlag.edgeFlag(a);
    }

    void callEndOrEndData() {
        if (callEndData != NULL_CB) callEndData.endData(polygonData);
        else callEnd.end();
    }

    void callCombineOrCombineData(double[] coords, Object[] vertexData, float[] weights, Object[] outData) {
        if (callCombineData != NULL_CB) callCombineData.combineData(coords, vertexData, weights, outData, polygonData);
        else callCombine.combine(coords, vertexData, weights, outData);
    }

    void callErrorOrErrorData(int a) {
        if (callErrorData != NULL_CB) callErrorData.errorData(a, polygonData);
        else callError.error(a);
    }
}
