package com.libGdx.test.cir;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/9/12 14:23
 */
public class FreeCir extends LibGdxTestMain {
    public static void main(String[] args) {
        FreeCir cir = new FreeCir();
        cir.start(cir);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        test(stage);
    }
    private Image image;
    public void test(Stage stage){
        TextureRegion region = new TextureRegion(new Texture("shuoming.png"));
        image = new Image(region){
            private float time = 0;
            private ShaderProgram shadowProgram = new ShaderProgram(
                    Gdx.files.internal("shader/cirSquare.vert"),
                    Gdx.files.internal("shader/cirSquare.glsl"));

            @Override
            public void act(float delta) {
                super.act(delta);
                time += delta;
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.flush();
                batch.setShader(shadowProgram);
                float u = region.getU();
                float u2 = region.getU2();
                float v = region.getV();
                float v2 = region.getV2();
                float uv = Math.abs((u2 - u) / 2.0f);
                float vv = Math.abs((v2 - v) / 2.0f);
                shadowProgram.setUniformf("uv",uv);
                shadowProgram.setUniformf("vv",vv);
                float i = 50.0f / region.getRegionHeight();
                shadowProgram.setUniformf("rato",image.getWidth()/image.getHeight());
                shadowProgram.setUniformf("ra",i);
                if (time > 0.4f){
                    time = 0.4f;
                }
                float v1 = time;
                shadowProgram.setUniformf("h",v1);
                super.draw(batch, parentAlpha);
                batch.flush();
                batch.setShader(null);
            }
        };
        stage.addActor(image);
    }
}
