package kw.artpuzzle.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

import kw.artpuzzle.theme.GameTheme;

public class TestGroup extends Group {
    private float time;

    public TestGroup(float width,float height) {
        setSize(width,height);
        Texture texture = Asset.getAsset().getTexture("test11.png");
        ShaderProgram shadowProgram = new ShaderProgram(
                Gdx.files.internal("shader/blackenter/middlespreed.vert"),
                Gdx.files.internal("shader/blackenter/tomiddle.frag"));
        Image group = new Image(Asset.getAsset().getTexture("noise.png")) {

            @Override
            public void act(float delta) {
                super.act(delta);
                time += delta * 0.65f;
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (shadowProgram != null) {
                    batch.setShader(shadowProgram);
                    int timeLocation = shadowProgram.getUniformLocation("time");
                    int u_t = shadowProgram.getUniformLocation("u_texture2");
                    Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
                    texture.bind();
                    shadowProgram.setUniformi(u_t,1);
                    shadowProgram.setUniformf(timeLocation, time);
                    Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
                    super.draw(batch, parentAlpha);
                    batch.setShader(null);
                } else {
                    super.draw(batch, parentAlpha);
                }
            }
        };

        setSize(texture.getWidth(),texture.getHeight());
        group.setSize(getWidth(), getHeight());
        group.setPosition(0, 0);

        addActor(group);
    }
}
