package org.lwjglx.util.glu.tessellation;

class ActiveRegion {

    GLUhalfEdge eUp; /* upper edge, directed right to left */
    DictNode nodeUp; /* dictionary node corresponding to eUp */
    int windingNumber; /*
                        * used to determine which regions are inside the polygon
                        */
    boolean inside; /* is this region inside the polygon? */
    boolean sentinel; /* marks fake edges at t = +/-infinity */
    boolean dirty; /*
                    * marks regions where the upper or lower edge has changed, but we haven't checked whether they
                    * intersect yet
                    */
    boolean fixUpperEdge; /*
                           * marks temporary edges introduced when we process a "right vertex" (one without any edges
                           * leaving to the right)
                           */
}
