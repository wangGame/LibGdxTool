package com.kw.gdx.npath;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;

public class GridPatchDrawable extends BaseDrawable implements TransformDrawable {

    private GridPatch patch;

    /**
     * 创建一个未初始化的 GridPatchDrawable。
     * 使用前必须先 setPatch。
     */
    public GridPatchDrawable() {
    }

    public GridPatchDrawable(GridPatch patch) {
        this.patch = patch;
    }

    public GridPatchDrawable(GridPatchDrawable drawable) {
        super(drawable);
        this.patch = drawable.patch;
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        patch.draw(batch, x, y, width, height);
    }

    @Override
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
        patch.draw(
                batch,
                x,
                y,
                originX,
                originY,
                width,
                height,
                scaleX,
                scaleY,
                rotation
        );
    }

    public GridPatch getPatch() {
        return patch;
    }

}