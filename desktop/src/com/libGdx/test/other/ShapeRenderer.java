package com.libGdx.test.other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public class ShapeRenderer implements Disposable {
    public enum ShapeType {
        Point(GL20.GL_POINTS), Line(GL20.GL_LINES), Filled(GL20.GL_TRIANGLES);

        private final int glType;

        ShapeType (int glType) {
            this.glType = glType;
        }

        public int getGlType () {
            return glType;
        }
    }

    private final ImmediateModeRenderer renderer;
    private boolean matrixDirty;
    private final Matrix4 projectionMatrix = new Matrix4();
    private final Matrix4 transformMatrix = new Matrix4();
    private final Matrix4 combinedMatrix = new Matrix4();
    private final Color color = new Color(1, 1, 1, 1);
    private ShapeRenderer.ShapeType shapeType;

    public ShapeRenderer () {
        this(5000);
    }

    public ShapeRenderer (int maxVertices) {
        renderer = new ImmediateModeRenderer2001(maxVertices, false, true, 0);
        projectionMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        matrixDirty = true;
    }

    public void setColor (Color color) {
        this.color.set(color);
    }

    public Color getColor () {
        return color;
    }

    /** Sets the projection matrix to be used for rendering. Usually this will be set to {@link Camera#combined}.
     * @param matrix */
    public void setProjectionMatrix (Matrix4 matrix) {
        projectionMatrix.set(matrix);
        matrixDirty = true;
    }

    public void setTransformMatrix (Matrix4 matrix) {
        transformMatrix.set(matrix);
        matrixDirty = true;
    }

    public void begin () {
        begin(ShapeRenderer.ShapeType.Line);
    }

    public void begin (ShapeRenderer.ShapeType type) {
        if (shapeType != null) throw new IllegalStateException("Call end() before beginning a new shape batch.");
        shapeType = type;
        if (matrixDirty) {
            combinedMatrix.set(projectionMatrix);
            Matrix4.mul(combinedMatrix.val, transformMatrix.val);
            matrixDirty = false;
        }
        renderer.begin(combinedMatrix, shapeType.getGlType());
    }

    public void set (ShapeRenderer.ShapeType type) {
        if (shapeType == type) return;
        if (shapeType == null) throw new IllegalStateException("begin must be called first.");
        end();
        begin(type);
    }

    /** Draws a rectangle in the x/y plane using {@link ShapeRenderer.ShapeType#Line} or {@link ShapeRenderer.ShapeType#Filled}. */
    public void rect (float x, float y, float width, float height) {
        check(ShapeRenderer.ShapeType.Line, ShapeRenderer.ShapeType.Filled, 8);
        float colorBits = color.toFloatBits();
        if (shapeType == ShapeRenderer.ShapeType.Line) {
            renderer.color(colorBits);
            renderer.vertex(x, y, 0);
            renderer.color(colorBits);
            renderer.vertex(x + width, y, 0);

            renderer.color(colorBits);
            renderer.vertex(x + width, y, 0);
            renderer.color(colorBits);
            renderer.vertex(x + width, y + height, 0);

            renderer.color(colorBits);
            renderer.vertex(x + width, y + height, 0);
            renderer.color(colorBits);
            renderer.vertex(x, y + height, 0);

            renderer.color(colorBits);
            renderer.vertex(x, y + height, 0);
            renderer.color(colorBits);
            renderer.vertex(x, y, 0);
        } else {
            renderer.color(colorBits);
            renderer.vertex(x, y, 0);
            renderer.color(colorBits);
            renderer.vertex(x + width, y, 0);
            renderer.color(colorBits);
            renderer.vertex(x + width, y + height, 0);

            renderer.color(colorBits);
            renderer.vertex(x + width, y + height, 0);
            renderer.color(colorBits);
            renderer.vertex(x, y + height, 0);
            renderer.color(colorBits);
            renderer.vertex(x, y, 0);
        }
    }

    /** @param other May be null. */
    private void check (ShapeRenderer.ShapeType preferred, ShapeRenderer.ShapeType other, int newVertices) {
        if (shapeType == null) throw new IllegalStateException("begin must be called first.");
        if (shapeType != preferred && shapeType != other) {
            // Shape type is not valid.
            end();
            begin(preferred);
        } else if (matrixDirty) {
            // Matrix has been changed.
            ShapeRenderer.ShapeType type = shapeType;
            end();
            begin(type);
        } else if (renderer.getMaxVertices() - renderer.getNumVertices() < newVertices) {
            // Not enough space.
            ShapeRenderer.ShapeType type = shapeType;
            end();
            begin(type);
        }
    }

    public void end () {
        renderer.end();
        shapeType = null;
    }

    public void dispose () {
        renderer.dispose();
    }
}
