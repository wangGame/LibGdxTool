package kw.artpuzzle.group.levelgroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

import kw.artpuzzle.theme.GameTheme;

public class CirSpreed extends Group {
    private float  delay = 0;
    private float startTime;
    private float time = 0;
    private float speed;
    public CirSpreed(float width, float height){
        setSize(width,height);
        Texture texture = Asset.getAsset().getTexture("noise.png");
        ShaderProgram shadowProgram = new ShaderProgram(
                Gdx.files.internal("shader/enterGameshader/cir.vert"),
                Gdx.files.internal("shader/enterGameshader/cir.frag"));
        Image group = new Image(texture){

            @Override
            public void act(float delta) {
                super.act(delta);
                delay -= delta;
                if (delay <=0) {
                    time += delta * 0.43f * speed;
                }
            }


            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (shadowProgram!=null) {
                    batch.setShader(shadowProgram);
                    shadowProgram.setUniformf("rataio",height / width);
                    shadowProgram.setUniformf("time",time);
                    super.draw(batch, parentAlpha);
                    batch.setShader(null);
                }else {
                    super.draw(batch, parentAlpha);
                }
            }
        };
        group.setSize(width,height);
        group.setPosition(0,0);
        group.setColor(GameTheme.enterGameColor);
        addActor(group);
//        group.addAction(Actions.delay(3.1f, Actions.fadeOut(1.6f)));
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
        this.time = startTime;
    }
}
