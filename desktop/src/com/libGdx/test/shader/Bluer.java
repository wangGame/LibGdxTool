package com.libGdx.test.shader;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.arrow.B;
import com.libGdx.test.base.LibGdxTestMain;

public class Bluer extends LibGdxTestMain {
    private ShaderProgram program;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        program = new ShaderProgram(
                Gdx.files.internal("assets/shader/blur/hui.vert"),
                Gdx.files.internal("assets/shader/blur/hui.frag")
        );
        Texture texture = Asset.getAsset().getTexture("assets/0_1_41_512.jpg");
        Image image = new Image(texture){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.flush();
                batch.setShader(program);
                program.setUniformf(
                        "u_texSize",
                        texture.getWidth(),
                        texture.getHeight()
                );
                super.draw(batch, parentAlpha);
                batch.setShader(null);
            }
        };
        addActor(image);

    }

    public static void main(String[] args) {
        Bluer bluer = new Bluer();
        bluer.start();
    }
}