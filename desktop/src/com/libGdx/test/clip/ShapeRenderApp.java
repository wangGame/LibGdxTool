package com.libGdx.test.clip;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.actor.PolygonClipGroup;
import com.kw.gdx.actor.ShaperRenerInteface;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.stencil.Cir;

public class ShapeRenderApp extends LibGdxTestMain {
    public static void main(String[] args) {
        ShapeRenderApp app = new ShapeRenderApp();
        app.start();
    }



    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        PolygonClipGroup clipGroup = new PolygonClipGroup(new ShaperRenerInteface() {
            @Override
            public void draw(Batch batch,float a) {

            }

            @Override
            public void setProjectionMatrix(Matrix4 projectionMatrix) {

            }

            @Override
            public void setTransformMatrix(Matrix4 transformMatrix) {

            }

            @Override
            public void begin(ShapeRenderer.ShapeType filled) {

            }

            @Override
            public void setColor(Color color) {

            }

            @Override
            public void end() {

            }
        });
        clipGroup.setSize(500,500);
        addActor(clipGroup);
        Image image = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        clipGroup.addActor(image);

    }
}
