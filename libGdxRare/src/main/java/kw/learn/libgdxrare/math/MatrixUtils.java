package kw.learn.libgdxrare.math;

public class MatrixUtils {
    public static final int M00 = 0;
    public static final int M01 = 1;
    public static final int M02 = 2;
    public static final int M03 = 3;
    public static final int M10 = 4;
    public static final int M11 = 5;
    public static final int M12 = 6;
    public static final int M13 = 7;
    public static final int M20 = 8;
    public static final int M21 = 9;
    public static final int M22 = 10;
    public static final int M23 = 11;
    public static final int M30 = 12;
    public static final int M31 = 13;
    public static final int M32 = 14;
    public static final int M33 = 15;

    /**
     * 1    0   0   0
     * 0    1   0   0
     * 0    0   1   0
     * 0    0   0   1
     * @return
     */
    public float[] createMatrixIdt(){
        float [] q = new float[16];
        for (int i = 0; i < q.length; i++) {
            q[i] = 0;
        }
        q[M00] = 1;
        q[M11] = 1;
        q[M22] = 1;
        q[M33] = 1;
        return q;
    }

    public void setMatrix4(float[] matr,
                           float n11, float n12, float n13, float n14,
                           float n21, float n22, float n23, float n24,
                           float n31, float n32, float n33, float n34,
                           float n41, float n42, float n43, float n44) {
        matr[M00] = n11;
        matr[M01] = n12;
        matr[M02] = n13;
        matr[M03] = n14;
        matr[M10] = n21;
        matr[M11] = n22;
        matr[M12] = n23;
        matr[M13] = n24;
        matr[M20] = n31;
        matr[M21] = n32;
        matr[M22] = n33;
        matr[M23] = n34;
        matr[M30] = n41;
        matr[M31] = n42;
        matr[M32] = n43;
        matr[M33] = n44;
    }

    /**
     * a    e   d   0
     * f    g   h   0
     * m    b   t   0
     * 0    0   0   1
     * @return
     */
    void setMatrix3(float[] matr, float a, float e, float d, float f, float g, float h, float m, float b, float t) {
        matr[M00] = a;
        matr[M01] = e;
        matr[M02] = d;
        matr[M03] = 0;
        matr[M10] = f;
        matr[M11] = g;
        matr[M12] = h;
        matr[M13] = 0;
        matr[M20] = m;
        matr[M21] = b;
        matr[M22] = t;
        matr[M23] = 0;
        matr[M30] = 0;
        matr[M31] = 0;
        matr[M32] = 0;
        matr[M33] = 1;
    }

    void setMatrixTo(float[] mart1, float[] mart2) {
        mart1[M00] = mart2[M00];
        mart1[M01] = mart2[M01];
        mart1[M02] = mart2[M02];
        mart1[M03] = mart2[M03];
        mart1[M10] = mart2[M10];
        mart1[M11] = mart2[M11];
        mart1[M12] = mart2[M12];
        mart1[M13] = mart2[M13];
        mart1[M20] = mart2[M20];
        mart1[M21] = mart2[M21];
        mart1[M22] = mart2[M22];
        mart1[M23] = mart2[M23];
        mart1[M30] = mart2[M30];
        mart1[M31] = mart2[M31];
        mart1[M32] = mart2[M32];
        mart1[M33] = mart2[M33];
    }

    /**
     * x
     * @param ae
     * @param be
     */
    public void matrixMultiR(float[] ae, float[] be) {
        float a11 = ae[M00], a12 = ae[M01], a13 = ae[M02], a14 = ae[M03];
        float a21 = ae[M10], a22 = ae[M11], a23 = ae[M12], a24 = ae[M13];
        float a31 = ae[M20], a32 = ae[M21], a33 = ae[M22], a34 = ae[M23];
        float a41 = ae[M30], a42 = ae[M31], a43 = ae[M32], a44 = ae[M33];

        float b11 = be[M00], b12 = be[M01], b13 = be[M02], b14 = be[M03];
        float b21 = be[M10], b22 = be[M11], b23 = be[M12], b24 = be[M13];
        float b31 = be[M20], b32 = be[M21], b33 = be[M22], b34 = be[M23];
        float b41 = be[M30], b42 = be[M31], b43 = be[M32], b44 = be[M33];


        ae[M00] = a11 * b11 + a12 * b21 + a13 * b31 + a14 * b41;
        ae[M01] = a11 * b12 + a12 * b22 + a13 * b32 + a14 * b42;
        ae[M02] = a11 * b13 + a12 * b23 + a13 * b33 + a14 * b43;
        ae[M03] = a11 * b14 + a12 * b24 + a13 * b34 + a14 * b44;

        ae[M10] = a21 * b11 + a22 * b21 + a23 * b31 + a24 * b41;
        ae[M11] = a21 * b12 + a22 * b22 + a23 * b32 + a24 * b42;
        ae[M12] = a21 * b13 + a22 * b23 + a23 * b33 + a24 * b43;
        ae[M13] = a21 * b14 + a22 * b24 + a23 * b34 + a24 * b44;

        ae[M20] = a31 * b11 + a32 * b21 + a33 * b31 + a34 * b41;
        ae[M21] = a31 * b12 + a32 * b22 + a33 * b32 + a34 * b42;
        ae[M22] = a31 * b13 + a32 * b23 + a33 * b33 + a34 * b43;
        ae[M23] = a31 * b14 + a32 * b24 + a33 * b34 + a34 * b44;

        ae[M30] = a41 * b11 + a42 * b21 + a43 * b31 + a44 * b41;
        ae[M31] = a41 * b12 + a42 * b22 + a43 * b32 + a44 * b42;
        ae[M32] = a41 * b13 + a42 * b23 + a43 * b33 + a44 * b43;
        ae[M33] = a41 * b14 + a42 * b24 + a43 * b34 + a44 * b44;
    }

    void setMoveMatrix(float []matr,float x,float y,float z) {
        setMatrix4(matr,
                1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                x, y, z, 1);
    }
}
