package com.libGdx.test.arrow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class AppShader extends LibGdxTestMain {
    public static void main(String[] args) {
        AppShader appShader = new AppShader();
        appShader.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Image image = new Image(Asset.getAsset().getTexture("assets/0_1_41_512.jpg")){
            ShaderProgram program = new ShaderProgram(
                    Gdx.files.internal("assets/shader/xx/grayScale.vert"),
                    Gdx.files.internal("assets/shader/xx/grayScale.glsl"));
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.flush();
                batch.setShader(program);
                super.draw(batch, parentAlpha);
                batch.flush();
                batch.setShader(null);
            }
        };
        addActor(image);
    }
}
