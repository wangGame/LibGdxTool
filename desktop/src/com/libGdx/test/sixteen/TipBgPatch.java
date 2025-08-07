package com.libGdx.test.sixteen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class TipBgPatch {
    public static final int TOP_LEFT = 0;
    public static final int TOP_LEFT_CENTER = 1;
    public static final int TOP_CENTER = 2;
    public static final int TOP_RIGHT_CENTER = 3;
    public static final int TOP_RIGHT = 4;
    public static final int MIDDLE_LEFT = 5;
    public static final int MIDDLE_LEFT_CENTER = 6;
    public static final int MIDDLE_CENTER = 7;
    public static final int MIDDLE_RIGHT_CENTER = 8;
    public static final int MIDDLE_RIGHT = 9;
    public static final int BOTTOM_LEFT = 10;
    public static final int BOTTOM_LEFT_CENTER = 11;
    public static final int BOTTOM_CENTER = 12;
    public static final int BOTTOM_RIGHT_CENTER = 13;
    public static final int BOTTOM_RIGHT = 14;

    static private final Color tmpDrawColor = new Color();

    private Texture texture;
    private int bottomLeft = -1,bottomLeftCenter =-1, bottomCenter = -1,bottomRightCenter = -1, bottomRight = -1;
    private int middleLeft = -1, middleLeftCenter = -1,middleCenter = -1, middleRightCenter = -1, middleRight = -1;
    private int topLeft = -1, topLeftCenter,topCenter = -1,topRightCenter = -1, topRight = -1;
    private float leftWidth, rightWidth, leftMiddleWidth,leftMiddleHeight, rightMiddleWidth,rightMiddleHeight, middleWidth, middleHeight, topHeight, bottomHeight;
    private float[] vertices = new float[15 * 4 * 5];
    private int idx;
    private final Color color = new Color(Color.WHITE);
    private TextureRegion region;


    public TipBgPatch(TextureRegion region, int left, int leftCenter, int rightCenter , int right, int top, int bottom) {
        updateData(region,left,leftCenter,rightCenter,right,top,bottom);
    }

    public void updateData(int left, int leftCenter, int rightCenter , int right, int top, int bottom){
        updateData(region,left,leftCenter,rightCenter,right,top,bottom);
    }

    public void updateData(TextureRegion region, int left, int leftCenter, int rightCenter , int right, int top, int bottom){
        if (region == null) throw new IllegalArgumentException("region cannot be null.");
        this.region = region;
        final int middleHeight = region.getRegionHeight() - top - bottom;
        final int regionWidth = region.getRegionWidth();
        final int regionHeight = region.getRegionHeight();
        TextureRegion[] patches = new TextureRegion[15];
        patches[TOP_LEFT] = new TextureRegion(region, 0, 0, left, top);
        patches[TOP_LEFT_CENTER] = new TextureRegion(region, left, 0, leftCenter, top);
        patches[TOP_RIGHT] = new TextureRegion(region,regionWidth-right,0,right,top);
        patches[TOP_RIGHT_CENTER] = new TextureRegion(region,regionWidth-right - rightCenter,0,rightCenter,top);
        patches[TOP_CENTER] = new TextureRegion(region,leftCenter+left,0,regionWidth - left - right - leftCenter - rightCenter,top);
        patches[MIDDLE_LEFT] = new TextureRegion(region, 0, top, left, middleHeight);
        patches[MIDDLE_LEFT_CENTER] = new TextureRegion(region, left, top, leftCenter, middleHeight);
        patches[MIDDLE_RIGHT] = new TextureRegion(region,regionWidth-right,top,right,middleHeight);
        patches[MIDDLE_RIGHT_CENTER] = new TextureRegion(region,regionWidth-right - rightCenter,top,rightCenter,middleHeight);
        patches[MIDDLE_CENTER] = new TextureRegion(region,leftCenter+left,top,regionWidth - left - right - leftCenter - rightCenter,middleHeight);
        patches[BOTTOM_LEFT] = new TextureRegion(region, 0,regionHeight-bottom , left, bottom);
        patches[BOTTOM_LEFT_CENTER] = new TextureRegion(region, left, regionHeight-bottom, leftCenter, bottom);
        patches[BOTTOM_RIGHT] = new TextureRegion(region,regionWidth-right,regionHeight-bottom,right,bottom);
        patches[BOTTOM_RIGHT_CENTER] = new TextureRegion(region,regionWidth-right - rightCenter,regionHeight-bottom,rightCenter,bottom);
        patches[BOTTOM_CENTER] = new TextureRegion(region,leftCenter+left,regionHeight-bottom,regionWidth - left - right - leftCenter - rightCenter,bottom);
        load(patches);
    }

    private void load(TextureRegion[] patches) {
        final float color = Color.WHITE.toFloatBits(); // placeholder color, overwritten at draw time

        if (patches[BOTTOM_LEFT] != null) {
            bottomLeft = add(patches[BOTTOM_LEFT], color, false, false);
            leftWidth = patches[BOTTOM_LEFT].getRegionWidth();
            bottomHeight = patches[BOTTOM_LEFT].getRegionHeight();
        }
        if(patches[BOTTOM_LEFT_CENTER]!=null){
            bottomLeftCenter = add(patches[BOTTOM_LEFT_CENTER], color, false, false);
            leftMiddleWidth = patches[BOTTOM_LEFT_CENTER].getRegionWidth();
            bottomHeight= patches[BOTTOM_LEFT_CENTER].getRegionHeight();
        }
        if (patches[BOTTOM_CENTER] != null) {
            bottomCenter = add(patches[BOTTOM_CENTER], color, true, false);
            middleWidth = Math.max(middleWidth, patches[BOTTOM_CENTER].getRegionWidth());
            bottomHeight = Math.max(bottomHeight, patches[BOTTOM_CENTER].getRegionHeight());
        }

        if (patches[BOTTOM_RIGHT_CENTER] != null) {
            bottomRightCenter = add(patches[BOTTOM_RIGHT_CENTER], color, false, false);
            rightMiddleWidth = Math.max(rightMiddleWidth, patches[BOTTOM_RIGHT_CENTER].getRegionWidth());
            bottomHeight = Math.max(bottomHeight, patches[BOTTOM_RIGHT_CENTER].getRegionHeight());
        }

        if (patches[BOTTOM_RIGHT] != null) {
            bottomRight = add(patches[BOTTOM_RIGHT], color, false, false);
            rightWidth = Math.max(rightWidth, patches[BOTTOM_RIGHT].getRegionWidth());
            bottomHeight = Math.max(bottomHeight, patches[BOTTOM_RIGHT].getRegionHeight());
        }
        if (patches[MIDDLE_LEFT] != null) {
            middleLeft = add(patches[MIDDLE_LEFT], color, false, true);
            leftWidth = Math.max(leftWidth, patches[MIDDLE_LEFT].getRegionWidth());
            middleHeight = Math.max(middleHeight, patches[MIDDLE_LEFT].getRegionHeight());
        }

        if (patches[MIDDLE_LEFT_CENTER] != null) {
            middleLeftCenter = add(patches[MIDDLE_LEFT_CENTER], color, true, true);
            leftMiddleWidth = Math.max(leftMiddleWidth, patches[MIDDLE_LEFT_CENTER].getRegionWidth());
            leftMiddleHeight = Math.max(middleHeight, patches[MIDDLE_LEFT_CENTER].getRegionHeight());
        }

        if (patches[MIDDLE_CENTER] != null) {
            middleCenter = add(patches[MIDDLE_CENTER], color, true, true);
            middleWidth = Math.max(middleWidth, patches[MIDDLE_CENTER].getRegionWidth());
            middleHeight = Math.max(middleHeight, patches[MIDDLE_CENTER].getRegionHeight());
        }


        if (patches[MIDDLE_RIGHT_CENTER] != null) {
            middleRightCenter = add(patches[MIDDLE_RIGHT_CENTER], color, true, true);
            rightMiddleWidth = Math.max(rightMiddleHeight, patches[MIDDLE_RIGHT_CENTER].getRegionWidth());
            rightMiddleHeight = Math.max(middleHeight, patches[MIDDLE_RIGHT_CENTER].getRegionHeight());
        }
        if (patches[MIDDLE_RIGHT] != null) {
            middleRight = add(patches[MIDDLE_RIGHT], color, false, true);
            rightWidth = Math.max(rightWidth, patches[MIDDLE_RIGHT].getRegionWidth());
            middleHeight = Math.max(middleHeight, patches[MIDDLE_RIGHT].getRegionHeight());
        }
        if (patches[TOP_LEFT] != null) {
            topLeft = add(patches[TOP_LEFT], color, false, false);
            leftWidth = Math.max(leftWidth, patches[TOP_LEFT].getRegionWidth());
            topHeight = Math.max(topHeight, patches[TOP_LEFT].getRegionHeight());
        }

        if (patches[TOP_LEFT_CENTER] != null) {
            topLeftCenter = add(patches[TOP_LEFT_CENTER], color, true, false);
            leftMiddleWidth = Math.max(leftMiddleWidth, patches[TOP_LEFT_CENTER].getRegionWidth());
            topHeight = Math.max(topHeight, patches[TOP_LEFT_CENTER].getRegionHeight());
        }
        if (patches[TOP_CENTER] != null) {
            topCenter = add(patches[TOP_CENTER], color, true, false);
            middleWidth = Math.max(middleWidth, patches[TOP_CENTER].getRegionWidth());
            topHeight = Math.max(topHeight, patches[TOP_CENTER].getRegionHeight());
        }

        if (patches[TOP_RIGHT_CENTER] != null) {
            topRightCenter = add(patches[TOP_RIGHT_CENTER], color, true, false);
            rightMiddleWidth = Math.max(rightMiddleWidth, patches[TOP_RIGHT_CENTER].getRegionWidth());
            topHeight = Math.max(topHeight, patches[TOP_RIGHT_CENTER].getRegionHeight());
        }


        if (patches[TOP_RIGHT] != null) {
            topRight = add(patches[TOP_RIGHT], color, false, false);
            rightWidth = Math.max(rightWidth, patches[TOP_RIGHT].getRegionWidth());
            topHeight = Math.max(topHeight, patches[TOP_RIGHT].getRegionHeight());
        }
        if (idx < vertices.length) {
            float[] newVertices = new float[idx];
            System.arraycopy(vertices, 0, newVertices, 0, idx);
            vertices = newVertices;
        }
    }

    private int add(TextureRegion region, float color, boolean isStretchW, boolean isStretchH) {
        if (texture == null)
            texture = region.getTexture();
        else if (texture != region.getTexture()) //
            throw new IllegalArgumentException("All regions must be from the same texture.");

        float u = region.getU();
        float v = region.getV2();
        float u2 = region.getU2();
        float v2 = region.getV();

        // Add half pixel offsets on stretchable dimensions to avoid color bleeding when GL_LINEAR
        // filtering is used for the texture. This nudges the texture coordinate to the center
        // of the texel where the neighboring pixel has 0% contribution in linear blending mode.
        if (texture.getMagFilter() == Texture.TextureFilter.Linear || texture.getMinFilter() == Texture.TextureFilter.Linear) {
            if (isStretchW) {
                float halfTexelWidth = 0.5f * 1.0f / texture.getWidth();
                u += halfTexelWidth;
                u2 -= halfTexelWidth;
            }
            if (isStretchH) {
                float halfTexelHeight = 0.5f * 1.0f / texture.getHeight();
                v -= halfTexelHeight;
                v2 += halfTexelHeight;
            }
        }

        final float[] vertices = this.vertices;

        vertices[idx + 2] = color;
        vertices[idx + 3] = u;
        vertices[idx + 4] = v;

        vertices[idx + 7] = color;
        vertices[idx + 8] = u;
        vertices[idx + 9] = v2;

        vertices[idx + 12] = color;
        vertices[idx + 13] = u2;
        vertices[idx + 14] = v2;

        vertices[idx + 17] = color;
        vertices[idx + 18] = u2;
        vertices[idx + 19] = v;
        idx += 20;

        return idx - 20;
    }

    /**
     * Set the coordinates and color of a ninth of the patch.
     */
    private void set(int idx, float x, float y, float width, float height, float color) {
        final float fx2 = x + width;
        final float fy2 = y + height;
        final float[] vertices = this.vertices;
        vertices[idx] = x;
        vertices[idx + 1] = y;
        vertices[idx + 2] = color;

        vertices[idx + 5] = x;
        vertices[idx + 6] = fy2;
        vertices[idx + 7] = color;

        vertices[idx + 10] = fx2;
        vertices[idx + 11] = fy2;
        vertices[idx + 12] = color;

        vertices[idx + 15] = fx2;
        vertices[idx + 16] = y;
        vertices[idx + 17] = color;
    }

    private void prepareVertices(Batch batch, float x, float y,float leftCenterWidth, float width, float height) {
        final float leftCenterColumnX = x + leftWidth;
        final float rightCenterColumnX = leftCenterColumnX+leftCenterWidth+middleWidth;
        final float rightCenterWidth = width -(rightCenterColumnX - x) - rightWidth;
        final float centerColumnX = x + leftWidth + leftCenterWidth;
        final float rightColumnX = x + width - rightWidth;
        final float middleRowY = y + bottomHeight;
        final float topRowY = y + height - topHeight;
        final float c = tmpDrawColor.set(color).mul(batch.getColor()).toFloatBits();

        if (bottomLeft != -1) set(bottomLeft, x, y, leftWidth, middleRowY - y, c);
        if (bottomLeftCenter!=-1)set(bottomLeftCenter,leftCenterColumnX,y,leftCenterWidth,middleRowY-y,c);
        if (bottomCenter != -1) set(bottomCenter, centerColumnX, y, middleWidth, middleRowY - y, c);
        if (bottomRight != -1) set(bottomRightCenter, rightCenterColumnX, y, rightCenterWidth, middleRowY - y, c);
        if (bottomRight != -1) set(bottomRight, rightColumnX, y, x + width - rightColumnX, middleRowY - y, c);
        if (middleLeft != -1) set(middleLeft, x, middleRowY, leftWidth, topRowY - middleRowY, c);
        if (middleLeftCenter != -1) set(middleLeftCenter, leftCenterColumnX, middleRowY, leftCenterWidth, topRowY - middleRowY, c);
        if (middleCenter != -1) set(middleCenter, centerColumnX, middleRowY, middleWidth, topRowY - middleRowY, c);
        if (middleRightCenter != -1) set(middleRightCenter, rightCenterColumnX, middleRowY, rightCenterWidth, topRowY - middleRowY, c);
        if (middleRight != -1) set(middleRight, rightColumnX, middleRowY, x + width - rightColumnX, topRowY - middleRowY, c);
        if (topLeft != -1) set(topLeft, x, topRowY, leftWidth, y + height - topRowY, c);
        if (topLeftCenter!= -1) set(topLeftCenter, leftCenterColumnX, topRowY, leftCenterWidth, y + height - topRowY, c);
        if (topCenter != -1) set(topCenter, centerColumnX, topRowY, middleWidth, y + height - topRowY, c);
        if (topRightCenter != -1) set(topRightCenter, rightCenterColumnX, topRowY, rightCenterWidth, y + height - topRowY, c);
        if (topRight != -1) set(topRight, rightColumnX, topRowY, x + width - rightColumnX, y + height - topRowY, c);
    }

    public void draw(Batch batch, float x, float y,float leftCenterWidth, float width, float height) {
        prepareVertices(batch, x, y,leftCenterWidth, width, height);
        batch.draw(texture, vertices, 0, idx);
    }

    public void draw(Batch batch, float x, float y, float originX, float originY, float leftCenterWidth,float width, float height, float scaleX,
                     float scaleY, float rotation) {
        prepareVertices(batch, x, y, leftCenterWidth,width, height);
        float worldOriginX = x + originX, worldOriginY = y + originY;
        int n = this.idx;
        float[] vertices = this.vertices;
        if (rotation != 0) {
            for (int i = 0; i < n; i += 5) {
                float vx = (vertices[i] - worldOriginX) * scaleX, vy = (vertices[i + 1] - worldOriginY) * scaleY;
                float cos = MathUtils.cosDeg(rotation), sin = MathUtils.sinDeg(rotation);
                vertices[i] = cos * vx - sin * vy + worldOriginX;
                vertices[i + 1] = sin * vx + cos * vy + worldOriginY;
            }
        } else if (scaleX != 1 || scaleY != 1) {
            for (int i = 0; i < n; i += 5) {
                vertices[i] = (vertices[i] - worldOriginX) * scaleX + worldOriginX;
                vertices[i + 1] = (vertices[i + 1] - worldOriginY) * scaleY + worldOriginY;
            }
        }
        batch.draw(texture, vertices, 0, n);
    }

    /**
     * Copy given color. The color will be blended with the batch color, then combined with the texture colors at
     * {@link com.badlogic.gdx.graphics.g2d.NinePatch#draw(Batch, float, float, float, float) draw} time. Default is {@link Color#WHITE}.
     */
    public void setColor(Color color) {
        this.color.set(color);
    }

    public Color getColor() {
        return color;
    }

    public float getLeftWidth() {
        return leftWidth;
    }

    /**
     * Set the draw-time width of the three left edge patches
     */
    public void setLeftWidth(float leftWidth) {
        this.leftWidth = leftWidth;
    }

    public float getRightWidth() {
        return rightWidth;
    }

    /**
     * Set the draw-time width of the three right edge patches
     */
    public void setRightWidth(float rightWidth) {
        this.rightWidth = rightWidth;
    }

    public float getTopHeight() {
        return topHeight;
    }

    /**
     * Set the draw-time height of the three top edge patches
     */
    public void setTopHeight(float topHeight) {
        this.topHeight = topHeight;
    }

    public float getBottomHeight() {
        return bottomHeight;
    }

    /**
     * Set the draw-time height of the three bottom edge patches
     */
    public void setBottomHeight(float bottomHeight) {
        this.bottomHeight = bottomHeight;
    }

    public float getMiddleWidth() {
        return middleWidth;
    }

    /**
     * Set the width of the middle column of the patch. At render time, this is implicitly the requested render-width of the
     * entire nine patch, minus the left and right width. This value is only used for computing the {@link #getTotalWidth() default
     * total width}.
     */
    public void setMiddleWidth(float middleWidth) {
        this.middleWidth = middleWidth;
    }

    public float getMiddleHeight() {
        return middleHeight;
    }

    /**
     * Set the height of the middle row of the patch. At render time, this is implicitly the requested render-height of the entire
     * nine patch, minus the top and bottom height. This value is only used for computing the {@link #getTotalHeight() default
     * total height}.
     */
    public void setMiddleHeight(float middleHeight) {
        this.middleHeight = middleHeight;
    }

    public float getTotalWidth() {
        return leftWidth + middleWidth + rightWidth;
    }

    public float getTotalHeight() {
        return topHeight + middleHeight + bottomHeight;
    }

    /**
     * Multiplies the top/left/bottom/right sizes and padding by the specified amount.
     */
    public void scale(float scaleX, float scaleY) {
        leftWidth *= scaleX;
        rightWidth *= scaleX;
        topHeight *= scaleY;
        bottomHeight *= scaleY;
        middleWidth *= scaleX;
        middleHeight *= scaleY;
    }

    public Texture getTexture() {
        return texture;
    }

}
