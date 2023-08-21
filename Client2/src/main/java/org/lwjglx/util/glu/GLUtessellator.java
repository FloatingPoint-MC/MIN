package org.lwjglx.util.glu;

public interface GLUtessellator {

    void gluTessBeginPolygon(Object data);

    void gluTessBeginContour();

    void gluTessEndContour();

    void gluTessEndPolygon();
}
