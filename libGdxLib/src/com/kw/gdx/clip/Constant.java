package com.kw.gdx.clip;

import static com.esotericsoftware.spine.utils.SpineUtils.cosDeg;
import static com.esotericsoftware.spine.utils.SpineUtils.sinDeg;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/21 16:57
 */
public class Constant {
    public static float a;
    public static float b;
    public static float c;
    public static float d;
    public static float worldX;
    public static float worldY;
    static {
        float rotation = 0;
        float shearY = 0;
        float scaleX = 1.0f;
        float scaleY = 1.0f;
        float shearX = 0;
        float xxx = 0;
        float yyy = 0;
        float xx = 0;
        float yy = 0;
        float rotationY = rotation + 90 + shearY, sx = scaleX, sy = scaleY;
        a = cosDeg(rotation + shearX) * scaleX * sx;
        b = cosDeg(rotationY) * scaleY * sx;
        c = sinDeg(rotation + shearX) * scaleX * sy;
        d = sinDeg(rotationY) * scaleY * sy;
        worldX = xxx * sx + xx;
        worldY = yyy * sy + yy;
    }
}
