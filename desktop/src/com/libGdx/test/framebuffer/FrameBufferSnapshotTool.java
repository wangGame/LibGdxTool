package com.libGdx.test.framebuffer;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;

public class FrameBufferSnapshotTool {

    private final SpriteBatch batch;
    private final Matrix4 projectionMatrix;

    public FrameBufferSnapshotTool() {
        batch = new SpriteBatch();
        projectionMatrix = new Matrix4();
    }

    /**
     * 把 Group 内容绘制到 FrameBuffer，并返回 Snapshot。
     *
     * 注意：
     * 1. 返回的 Texture 由 FrameBuffer 持有。
     * 2. 使用完 Snapshot 后，需要调用 snapshot.dispose()。
     * 3. 不要在外部 batch.begin() 和 batch.end() 之间调用这个方法。
     */
    public Snapshot drawGroupToTexture(Group group, int width, int height) {
        return drawGroupToTexture(group, width, height, true);
    }

    /**
     * @param resetGroupTransform true 表示忽略 group 自己的 x/y/scale/rotation，只绘制它内部的 children。
     *                            一般日历页、卡牌页推荐 true。
     */
    public Snapshot drawGroupToTexture(Group group, int width, int height, boolean resetGroupTransform) {
        FrameBuffer frameBuffer = new FrameBuffer(
                Pixmap.Format.RGBA8888,
                width,
                height,
                false
        );

        drawToFrameBuffer(group, frameBuffer, width, height, resetGroupTransform);

        return new Snapshot(frameBuffer, width, height);
    }

    /**
     * 如果你想复用同一个 FrameBuffer，避免频繁 new，可以用这个。
     */
    public void drawToFrameBuffer(
            Group group,
            FrameBuffer frameBuffer,
            int width,
            int height,
            boolean resetGroupTransform
    ) {
        // 如果是 Table、WidgetGroup 之类，先让它完成布局
        validateLayout(group);

        // 保存 group 自己的变换
        float oldX = group.getX();
        float oldY = group.getY();
        float oldScaleX = group.getScaleX();
        float oldScaleY = group.getScaleY();
        float oldRotation = group.getRotation();
        float oldOriginX = group.getOriginX();
        float oldOriginY = group.getOriginY();

        if (resetGroupTransform) {
            group.setPosition(0, 0);
            group.setScale(1f, 1f);
            group.setRotation(0f);
            group.setOrigin(0f, 0f);
        }

        frameBuffer.begin();

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        projectionMatrix.setToOrtho2D(0, 0, width, height);

        batch.setProjectionMatrix(projectionMatrix);
        batch.setTransformMatrix(new Matrix4());
        batch.setColor(Color.WHITE);

        batch.begin();

        /**
         * parentAlpha = 1
         *
         * Group 会继续递归绘制自己的 children。
         */
        group.draw(batch, 1f);

        batch.end();

        frameBuffer.end();

        // 恢复 group 自己的变换
        if (resetGroupTransform) {
            group.setPosition(oldX, oldY);
            group.setScale(oldScaleX, oldScaleY);
            group.setRotation(oldRotation);
            group.setOrigin(oldOriginX, oldOriginY);
        }
    }

    private void validateLayout(Group group) {
        if (group instanceof Layout) {
            ((Layout) group).validate();
        }

        for (int i = 0; i < group.getChildren().size; i++) {
            if (group.getChildren().get(i) instanceof Layout) {
                ((Layout) group.getChildren().get(i)).validate();
            }
        }
    }

    public void dispose() {
        batch.dispose();
    }

    public static class Snapshot {

        private final FrameBuffer frameBuffer;
        private final Texture texture;
        private final com.badlogic.gdx.graphics.g2d.TextureRegion region;

        private final int width;
        private final int height;

        private boolean disposed = false;

        private Snapshot(FrameBuffer frameBuffer, int width, int height) {
            this.frameBuffer = frameBuffer;
            this.texture = frameBuffer.getColorBufferTexture();
            this.width = width;
            this.height = height;

            /**
             * FrameBuffer 得到的纹理通常是上下反的。
             * 所以这里直接返回一个已经 flip 过的 region。
             */
            this.region = new com.badlogic.gdx.graphics.g2d.TextureRegion(texture);
            this.region.flip(false, true);
        }

        public Texture getTexture() {
            return texture;
        }

        public com.badlogic.gdx.graphics.g2d.TextureRegion getRegion() {
            return region;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public void dispose() {
            if (disposed) return;
            disposed = true;
            frameBuffer.dispose();
        }
    }
}