package com.libGdx.test.beser;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/17 14:16
 */
public class RoadDrawU {
    /** 轨道宽度 */
    public static int roadWidth = 8;
    /** 轨道颜色 */
    public static Color roadColor = Color.BLUE;
    /** 轨道描边宽度 */
    public static int roadBorder = 5;
    /** 轨道描边颜色 */
    public static Color roadBorderColor = Color.RED;
    /// ** 纹理ID */
    // public static int texId;
    /** 轨道点平滑插值个数 */
    public static int pointSmoothCount = 1;

    public static int shadowLen = 30;
    public static int shadowOffX = 0, shadowOffY = 0;
    public static int bridgeShadowLen = 10;
    public static String roadPicPath = "";

    public int canvasWidth;
    public int canvasHeight;
    public float[] pointLenToRoad;// 点到轨道的距离
    public float[] shadowAlpha;// 阴影alpha值
    public int[] pointPosOnRoadPointIndex;
    public boolean[] pointBridge;
    public boolean isHasBridge;
    public int maxLen;

    public RoadDrawU(int w, int h) {
        canvasWidth = w;
        canvasHeight = h;
    }

    public Pixmap drawPath(Array<float[]> points) {
        //宽  高
        int pointCount = canvasWidth * canvasHeight;
        pointLenToRoad = new float[pointCount];
        pointPosOnRoadPointIndex = new int[pointCount];
        pointBridge = new boolean[pointCount];
        shadowAlpha = new float[pointCount];
        //rgb a
        byte[] pixmapData = new byte[pointCount * 4];

        ///////////////// 初始化数组
        maxLen = Math.max(roadWidth * 2, roadWidth + roadBorder);
        for (int i = 0; i < pointLenToRoad.length; i++) {
            pointLenToRoad[i] = maxLen;
        }
        /////////////////

        calcRoadPoints(points);
        calcPixData(pixmapData);

        // LogU.log(" >drawPath updatePoints done! :" + System.currentTimeMillis());

        Pixmap pix = new Pixmap(canvasWidth, canvasHeight, Format.RGBA8888);
        ByteBuffer bb = pix.getPixels();
        bb.put(pixmapData);
        ((Buffer) bb).flip();

        // long now = System.currentTimeMillis();
        // LogU.log(" >drawPath done! used[" + (now - time) + "] - now:" + now);

        points.clear();

        // texPix.dispose();
        return pix;
    }

    private void calcRoadPoints(Array<float[]> points) {
        /// 计算点到轨道的距离
        float[] point;
        float[] point2;
        float smoothStepX;
        float smoothStepY;
        for (int i = 0; i < points.size - 1; i++) {
            point = points.get(i);
            point2 = points.get(i + 1);

            smoothStepX = (point2[0] - point[0]) / pointSmoothCount;
            smoothStepY = (point2[1] - point[1]) / pointSmoothCount;

            for (int pi = 0; pi < pointSmoothCount; pi++) {
                calcBasePointSurround(point[0] + smoothStepX * pi, point[1] + smoothStepY * pi,
                        i * pointSmoothCount + pi);
            }
        }
        // 最后一个点
        point = points.get(points.size - 1);
        calcBasePointSurround(point[0], point[1], (points.size - 1) * pointSmoothCount);

        ////// shadows.
        for (int px = 0; px < canvasWidth; px++) {
            for (int py = 0; py < canvasHeight; py++) {
                int pIndex = px + py * canvasWidth;
                int movePIndex = px + (Math.min(canvasHeight - 1, py + shadowOffY)) * canvasWidth;
                float len = getShadowLenToEdge(pointLenToRoad[movePIndex]);
                float p = 1 - len / shadowLen;
                if (p < 0) {
                    p = 0;
                }
                float clen = getShadowLenToEdge(pointLenToRoad[pIndex]);
                float cp = 1 - clen / (shadowOffY + shadowLen);
                p *= cp * 0.85f;
                shadowAlpha[pIndex] = Interpolation.circleIn.apply(p);
            }
        }
        ////// BridgeShadow
        if (isHasBridge) {
            float shadowMax = bridgeShadowLen + shadowOffY;
            int checkLen = roadWidth + 1;
            for (int px = 0; px < canvasWidth; px++) {
                float shadowVal = 0;
                float skipUpper = 0;
                for (int py = canvasHeight - 1; py >= 0; py--) {
                    int pIndex = px + py * canvasWidth;
                    if (pointBridge[pIndex]) {
                        if (skipUpper <= 0) {
                            shadowVal = shadowMax;
                        } else {
                            skipUpper--;
                        }
                    } else {
                        if (shadowVal <= 0) {
                            skipUpper = bridgeShadowLen;
                        }
                    }
                    if (shadowVal > 0) {
                        if (pointLenToRoad[pIndex] < checkLen) {
                            float sa = Interpolation.circleIn.apply(shadowVal / shadowMax * 0.85f);
                            if (sa > shadowAlpha[pIndex]) {
                                shadowAlpha[pIndex] = sa;
                            }
                        }
                        shadowVal--;
                    }
                }
            }
        }
    }

    private float getShadowLenToEdge(float len) {
        if (len > roadWidth) {
            len = 0;
        } else {
            len = roadWidth - len;
        }
        return len;
    }

    private void calcBasePointSurround(float roadPointX, float roadPointY, int pointIndex) {
        int w = maxLen;
        int xMin = (int) Math.max(0, roadPointX - w);
        int xMax = (int) Math.min(canvasWidth, roadPointX + w);
        int yMin = (int) Math.max(0, roadPointY - w);
        int yMax = (int) Math.min(canvasHeight, roadPointY + w);

        float len = w * w;

        float l;
        int pIndex;
        for (int px = xMin; px < xMax; px++) {
            for (int py = yMin; py < yMax; py++) {
                pIndex = px + py * canvasWidth;
                l = Vector2.len2(px - roadPointX, py - roadPointY);
                if (l > len) {
                    continue;
                }
                l = (float) Math.sqrt(l);
                if (pointLenToRoad[pIndex] > l) {
                    pointLenToRoad[pIndex] = l;
                }
                if (l < roadWidth + bridgeShadowLen) {
                    if (pointPosOnRoadPointIndex[pIndex] == 0) {
                        pointPosOnRoadPointIndex[pIndex] = pointIndex;
                    } else {
                        if (pointIndex - pointPosOnRoadPointIndex[pIndex] > maxLen + 1) {
                            pointBridge[pIndex] = true;
                            isHasBridge = true;
                        }
                    }
                }
            }
        }
    }

    private void calcPixData(byte[] pixmapData) {
        Color c = new Color();
        int widthP1 = roadWidth + 1;
        int widthP2 = roadWidth + roadBorder;
        for (int y = 0; y < canvasHeight; y++) {
            for (int x = 0; x < canvasWidth; x++) {
                int p = x + y * canvasWidth;
                int dIndex = p * 4;

                float len = pointLenToRoad[p];
                boolean isInShadowArea = true;
                if (len < roadWidth) {
                    c.set(roadColor);
                } else if (len < widthP1) {
                    setColorLinear(c, roadBorderColor, roadColor, widthP1 - len);
                    setColorLinear(c, c, Color.BLACK, (widthP1 - len) * 0.2f);

                    isInShadowArea = false;
                } else if (len < widthP2) {
                    c.set(roadBorderColor);
                    c.a = (widthP2 - len) / roadBorder;
                    isInShadowArea = false;
                }
                else{
                    pixmapData[dIndex] = 0;
                    pixmapData[dIndex + 1] = 0;
                    pixmapData[dIndex + 2] = 0;
                    pixmapData[dIndex + 3] = 0;
                    continue;
                }

                float alpha = c.a;
                if (isInShadowArea) {
                    if (shadowAlpha[p] > 0) {
//                        c.set(Color.WHITE);
                        setColorLinear(c, c, Color.RED, Interpolation.sineOut.apply(shadowAlpha[p]));
                    }
                }
//                RoadDrawCommonU.setDataColor(pixmapData, dIndex, c, alpha);
                setDataColor(pixmapData, dIndex, c, alpha);
            }
        }
    }

    public static void setDataColor(byte[] data, int index, Color c, float alpha) {
        data[index] = (byte) (c.r * 255);
        data[index + 1] = (byte) (c.g * 255);
        data[index + 2] = (byte) (c.b * 255);
        data[index + 3] = (byte) (alpha * 255);
    }

    public static void setColorLinear(Color ct, Color ca, Color cb, float p) {
        ct.r = ca.r + (cb.r - ca.r) * p;
        ct.g = ca.g + (cb.g - ca.g) * p;
        ct.b = ca.b + (cb.b - ca.b) * p;
        ct.a = ca.a + (cb.a - ca.a) * p;
    }


    // Color texC = new Color();
    //
    // private void updateTexColor(int x, int y) {
    // float scale = 1;
    // int tx = (int) ((x / scale) % texPix.getWidth());
    // int ty = texPix.getHeight() - 1 - (int) ((y / scale) % texPix.getHeight());
    // texC.set(texPix.getPixel(tx, ty));
    // }
}
