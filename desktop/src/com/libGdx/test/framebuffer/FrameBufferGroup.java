package com.libGdx.test.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Renders a ScrollPane into a texture while keeping Scene2D's normal stage,
 * parent transform and scissor coordinate systems intact.
 */
public class FrameBufferGroup extends Group {

    private final ScrollPane scrollPane;
    private final TextureRegion region = new TextureRegion();
    private final Matrix4 oldProjection = new Matrix4();
    private final Matrix4 oldTransform = new Matrix4();
    private final Vector2 point = new Vector2();
    private final Vector2 min = new Vector2();
    private final Vector2 max = new Vector2();

    private FrameBuffer frameBuffer;
    private Image drawContent;
    private int bufferWidth;
    private int bufferHeight;
    private boolean needUpdate = true;

    public FrameBufferGroup(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
        setSize(scrollPane.getWidth(), scrollPane.getHeight());
        addActor(scrollPane);

        // A valid region is needed because callers create the Image before the
        // actor is attached to a Stage. It is updated to the real screen crop
        // during the first draw.
        ensureFrameBuffer();
        region.setRegion(frameBuffer.getColorBufferTexture());
        region.flip(false, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (getStage() == null || drawContent == null) return;

        syncScrollPaneHitArea();
        validateLayout();
        ensureFrameBuffer();
        renderToFrameBuffer(batch, parentAlpha);
        updateTextureRegion();
        needUpdate = false;
    }

    private void renderToFrameBuffer(Batch batch, float parentAlpha) {
        batch.flush();
        oldProjection.set(batch.getProjectionMatrix());
        oldTransform.set(batch.getTransformMatrix());

        Viewport viewport = getStage().getViewport();

        frameBuffer.begin();
        Gdx.gl.glViewport(0, 0, bufferWidth, bufferHeight);
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Keep the matrices supplied by Scene2D. At this point they already
        // contain every enclosing Group's position, origin, scale and rotation.
        batch.setProjectionMatrix(oldProjection);
        batch.setTransformMatrix(oldTransform);

        // Stage.calculateScissors produces back-buffer coordinates, so the FBO
        // deliberately has the same dimensions as the back buffer.
        Gdx.gl.glViewport(
                viewport.getScreenX(),
                viewport.getScreenY(),
                viewport.getScreenWidth(),
                viewport.getScreenHeight()
        );
        scrollPane.draw(batch, parentAlpha);
        batch.flush();
        frameBuffer.end();

        batch.setProjectionMatrix(oldProjection);
        batch.setTransformMatrix(oldTransform);
        viewport.apply(false);
    }

    private void updateTextureRegion() {
        Viewport viewport = getStage().getViewport();

        projectCorner(viewport, 0f, 0f, min);
        projectCorner(viewport, drawContent.getWidth(), drawContent.getHeight(), max);

        float left = Math.min(min.x, max.x);
        float right = Math.max(min.x, max.x);
        float bottom = Math.min(min.y, max.y);
        float top = Math.max(min.y, max.y);

        left = clamp(left, 0f, bufferWidth);
        right = clamp(right, 0f, bufferWidth);
        bottom = clamp(bottom, 0f, bufferHeight);
        top = clamp(top, 0f, bufferHeight);

        // Direct UV assignment avoids TextureRegion's top-left pixel convention.
        // FBO textures are vertically inverted, hence top is used as v and
        // bottom as v2.
        region.setRegion(
                left / bufferWidth,
                top / bufferHeight,
                right / bufferWidth,
                bottom / bufferHeight
        );
    }

    private void projectCorner(Viewport viewport, float x, float y, Vector2 out) {
        point.set(x, y);
        drawContent.localToStageCoordinates(point);
        viewport.project(point);
        out.set(point);
    }

    private void syncScrollPaneHitArea() {
        point.set(drawContent.getWidth() * 0.5f, drawContent.getHeight() * 0.5f);
        drawContent.localToStageCoordinates(point);
        stageToLocalCoordinates(point);

        scrollPane.setOrigin(Align.center);
        scrollPane.setPosition(point.x, point.y, Align.center);
        scrollPane.setScale(
                drawContent.getWidth() / scrollPane.getWidth(),
                drawContent.getHeight() / scrollPane.getHeight()
        );
    }

    private void validateLayout() {
        scrollPane.validate();
        Actor widget = scrollPane.getWidget();
        if (widget instanceof Layout) ((Layout) widget).validate();
    }

    private void ensureFrameBuffer() {
        int width = Math.max(1, Gdx.graphics.getBackBufferWidth());
        int height = Math.max(1, Gdx.graphics.getBackBufferHeight());
        if (frameBuffer != null && width == bufferWidth && height == bufferHeight) return;

        if (frameBuffer != null) frameBuffer.dispose();
        bufferWidth = width;
        bufferHeight = height;
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public TextureRegion getBufferTexture(float globalScale) {
        return region;
    }

    public void setDrawContent(Image drawContent) {
        this.drawContent = drawContent;
        needUpdate = true;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void dispose() {
        if (frameBuffer != null) {
            frameBuffer.dispose();
            frameBuffer = null;
        }
    }
}
