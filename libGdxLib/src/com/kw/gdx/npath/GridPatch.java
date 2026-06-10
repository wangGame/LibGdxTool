package com.kw.gdx.npath;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.asset.Asset;

/**
 * 可以直接切，然后拼起来
 *
 * 思路完成，不写了
 */
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GridPatch {

    private static final int VERTEX_SIZE = 5;
    private static final int QUAD_SIZE = 4 * VERTEX_SIZE;

    private static final Color tmpDrawColor = new Color();

    private final Texture texture;

    private final int cols;
    private final int rows;

    /**
     * 原图中每一列的宽度，单位像素。
     * 从左到右。
     */
    private final float[] srcColWidths;

    /**
     * 原图中每一行的高度，单位像素。
     * 注意：从上到下。
     */
    private final float[] srcRowHeights;

    /**
     * 每一列是否横向拉伸。
     */
    private final boolean[] stretchCols;

    /**
     * 每一行是否纵向拉伸。
     * 注意：从上到下。
     */
    private final boolean[] stretchRows;

    private final float[] drawColWidths;
    private final float[] drawRowHeights;

    private final float[] vertices;

    private final Color color = new Color(Color.WHITE);

    public GridPatch(
            Texture texture,
            int[] colWidths,
            int[] rowHeightsTopToBottom,
            boolean[] stretchCols,
            boolean[] stretchRows
    ) {
        this(new TextureRegion(texture), colWidths, rowHeightsTopToBottom, stretchCols, stretchRows);
    }

    public GridPatch(
            Texture texture,
            int[] colWidths,
            int[] rowHeightsTopToBottom
    ) {
        this(
                new TextureRegion(texture),
                colWidths,
                rowHeightsTopToBottom,
                createDefaultStretchFlags(colWidths.length),
                createDefaultStretchFlags(rowHeightsTopToBottom.length)
        );
    }

    public GridPatch(
            TextureRegion region,
            int[] colWidths,
            int[] rowHeightsTopToBottom,
            boolean[] stretchCols,
            boolean[] stretchRows
    ) {
        if (region == null) {
            throw new IllegalArgumentException("region cannot be null.");
        }

        if (colWidths == null || colWidths.length == 0) {
            throw new IllegalArgumentException("colWidths cannot be empty.");
        }

        if (rowHeightsTopToBottom == null || rowHeightsTopToBottom.length == 0) {
            throw new IllegalArgumentException("rowHeightsTopToBottom cannot be empty.");
        }

        if (stretchCols == null || stretchCols.length != colWidths.length) {
            throw new IllegalArgumentException("stretchCols length must equal colWidths length.");
        }

        if (stretchRows == null || stretchRows.length != rowHeightsTopToBottom.length) {
            throw new IllegalArgumentException("stretchRows length must equal rowHeightsTopToBottom length.");
        }

        int totalW = sum(colWidths);
        int totalH = sum(rowHeightsTopToBottom);

        if (totalW != region.getRegionWidth()) {
            throw new GdxRuntimeException(
                    "Column widths sum must equal region width. sum="
                            + totalW + ", regionWidth=" + region.getRegionWidth()
            );
        }

        if (totalH != region.getRegionHeight()) {
            throw new GdxRuntimeException(
                    "Row heights sum must equal region height. sum="
                            + totalH + ", regionHeight=" + region.getRegionHeight()
            );
        }

        this.texture = region.getTexture();

        this.cols = colWidths.length;
        this.rows = rowHeightsTopToBottom.length;

        this.srcColWidths = toFloatArray(colWidths);
        this.srcRowHeights = toFloatArray(rowHeightsTopToBottom);

        this.stretchCols = stretchCols.clone();
        this.stretchRows = stretchRows.clone();

        this.drawColWidths = new float[cols];
        this.drawRowHeights = new float[rows];

        this.vertices = new float[cols * rows * QUAD_SIZE];

        buildUV(region, colWidths, rowHeightsTopToBottom);
    }

    /**
     * 兼容普通九宫格。
     */
    public static GridPatch fromNinePatch(Texture texture, int left, int right, int top, int bottom) {
        int w = texture.getWidth();
        int h = texture.getHeight();

        int centerW = w - left - right;
        int centerH = h - top - bottom;

        if (centerW <= 0 || centerH <= 0) {
            throw new GdxRuntimeException("Invalid nine patch split.");
        }

        return new GridPatch(
                texture,
                new int[]{left, centerW, right},
                new int[]{top, centerH, bottom},
                new boolean[]{false, true, false},
                new boolean[]{false, true, false}
        );
    }

    private void buildUV(TextureRegion sourceRegion, int[] colWidths, int[] rowHeightsTopToBottom) {
        int sourceY = 0;

        for (int row = 0; row < rows; row++) {
            int sourceX = 0;
            int rowHeight = rowHeightsTopToBottom[row];

            for (int col = 0; col < cols; col++) {
                int colWidth = colWidths[col];

                TextureRegion cellRegion = new TextureRegion(
                        sourceRegion,
                        sourceX,
                        sourceY,
                        colWidth,
                        rowHeight
                );

                int cellIndex = row * cols + col;
                setUV(
                        cellIndex,
                        cellRegion,
                        stretchCols[col],
                        stretchRows[row]
                );

                sourceX += colWidth;
            }

            sourceY += rowHeight;
        }
    }

    private void setUV(int cellIndex, TextureRegion region, boolean isStretchW, boolean isStretchH) {
        float u = region.getU();
        float v = region.getV2();
        float u2 = region.getU2();
        float v2 = region.getV();

        if (texture.getMagFilter() == TextureFilter.Linear
                || texture.getMinFilter() == TextureFilter.Linear) {

            if (isStretchW) {
                float halfTexelWidth = 0.5f / texture.getWidth();
                u += halfTexelWidth;
                u2 -= halfTexelWidth;
            }

            if (isStretchH) {
                float halfTexelHeight = 0.5f / texture.getHeight();
                v -= halfTexelHeight;
                v2 += halfTexelHeight;
            }
        }

        int i = cellIndex * QUAD_SIZE;

        vertices[i + 3] = u;
        vertices[i + 4] = v;

        vertices[i + 8] = u;
        vertices[i + 9] = v2;

        vertices[i + 13] = u2;
        vertices[i + 14] = v2;

        vertices[i + 18] = u2;
        vertices[i + 19] = v;
    }

    public void draw(Batch batch, float x, float y, float width, float height) {
        prepareVertices(batch, x, y, width, height);
        batch.draw(texture, vertices, 0, vertices.length);
    }

    public void draw(
            Batch batch,
            float x,
            float y,
            float originX,
            float originY,
            float width,
            float height,
            float scaleX,
            float scaleY,
            float rotation
    ) {
        prepareVertices(batch, x, y, width, height);

        float worldOriginX = x + originX;
        float worldOriginY = y + originY;

        if (rotation != 0f) {
            float cos = MathUtils.cosDeg(rotation);
            float sin = MathUtils.sinDeg(rotation);

            for (int i = 0; i < vertices.length; i += VERTEX_SIZE) {
                float vx = (vertices[i] - worldOriginX) * scaleX;
                float vy = (vertices[i + 1] - worldOriginY) * scaleY;

                vertices[i] = cos * vx - sin * vy + worldOriginX;
                vertices[i + 1] = sin * vx + cos * vy + worldOriginY;
            }

        } else if (scaleX != 1f || scaleY != 1f) {
            for (int i = 0; i < vertices.length; i += VERTEX_SIZE) {
                vertices[i] = (vertices[i] - worldOriginX) * scaleX + worldOriginX;
                vertices[i + 1] = (vertices[i + 1] - worldOriginY) * scaleY + worldOriginY;
            }
        }

        batch.draw(texture, vertices, 0, vertices.length);
    }

    private void prepareVertices(Batch batch, float x, float y, float width, float height) {
        computeDrawSizes(width, srcColWidths, stretchCols, drawColWidths);
        computeDrawSizes(height, srcRowHeights, stretchRows, drawRowHeights);

        float packedColor = tmpDrawColor
                .set(color)
                .mul(batch.getColor())
                .toFloatBits();

        float currentTopY = y + height;

        for (int row = 0; row < rows; row++) {
            float cellHeight = drawRowHeights[row];
            currentTopY -= cellHeight;

            float currentX = x;

            for (int col = 0; col < cols; col++) {
                float cellWidth = drawColWidths[col];

                int cellIndex = row * cols + col;
                setPositionAndColor(
                        cellIndex,
                        currentX,
                        currentTopY,
                        cellWidth,
                        cellHeight,
                        packedColor
                );

                currentX += cellWidth;
            }
        }
    }

    private void setPositionAndColor(
            int cellIndex,
            float x,
            float y,
            float width,
            float height,
            float packedColor
    ) {
        float x2 = x + width;
        float y2 = y + height;

        int i = cellIndex * QUAD_SIZE;

        vertices[i] = x;
        vertices[i + 1] = y;
        vertices[i + 2] = packedColor;

        vertices[i + 5] = x;
        vertices[i + 6] = y2;
        vertices[i + 7] = packedColor;

        vertices[i + 10] = x2;
        vertices[i + 11] = y2;
        vertices[i + 12] = packedColor;

        vertices[i + 15] = x2;
        vertices[i + 16] = y;
        vertices[i + 17] = packedColor;
    }

    private static void computeDrawSizes(
            float targetSize,
            float[] sourceSizes,
            boolean[] stretchFlags,
            float[] outSizes
    ) {
        float fixedSize = 0f;
        float stretchSize = 0f;
        float totalSize = 0f;

        for (int i = 0; i < sourceSizes.length; i++) {
            totalSize += sourceSizes[i];

            if (stretchFlags[i]) {
                stretchSize += sourceSizes[i];
            } else {
                fixedSize += sourceSizes[i];
            }
        }

        if (targetSize <= 0f || totalSize <= 0f) {
            for (int i = 0; i < outSizes.length; i++) {
                outSizes[i] = 0f;
            }
            return;
        }

        if (stretchSize > 0f && targetSize >= fixedSize) {
            float remainingSize = targetSize - fixedSize;

            for (int i = 0; i < sourceSizes.length; i++) {
                if (stretchFlags[i]) {
                    outSizes[i] = remainingSize * sourceSizes[i] / stretchSize;
                } else {
                    outSizes[i] = sourceSizes[i];
                }
            }
        } else {
            float scale = targetSize / totalSize;

            for (int i = 0; i < sourceSizes.length; i++) {
                outSizes[i] = sourceSizes[i] * scale;
            }
        }
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public Color getColor() {
        return color;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getTotalWidth() {
        float total = 0f;
        for (float value : srcColWidths) {
            total += value;
        }
        return total;
    }

    public float getTotalHeight() {
        float total = 0f;
        for (float value : srcRowHeights) {
            total += value;
        }
        return total;
    }

    private static boolean[] createDefaultStretchFlags(int count) {
        boolean[] result = new boolean[count];

        if (count == 1) {
            result[0] = true;
            return result;
        }

        for (int i = 0; i < count; i++) {
            result[i] = i != 0 && i != count - 1;
        }

        return result;
    }

    private static int sum(int[] values) {
        int result = 0;

        for (int value : values) {
            if (value < 0) {
                throw new IllegalArgumentException("size cannot be negative.");
            }
            result += value;
        }

        return result;
    }

    private static float[] toFloatArray(int[] values) {
        float[] result = new float[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }

        return result;
    }
}