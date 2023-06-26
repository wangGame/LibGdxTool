package kw.artpuzzle.group.levelgroup;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

import kw.artpuzzle.shadermanager.ShaderManager;
import kw.artpuzzle.shadermanager.ShaderType;

public class BgEnterAni1 extends Group {
    private Texture texture;
    private ShaderProgram shadowProgram;
    public BgEnterAni1(float width, float height){
        texture = Asset.getAsset().getTexture("noise.png");
        shadowProgram = ShaderManager.getManager().getType(ShaderType.MiddleSpread);
        Image group = new Image(texture){
            private float time = 0;
            @Override
            public void act(float delta) {
                super.act(delta);
                time+=delta * 0.33f;
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                if (shadowProgram!=null) {
                    batch.setShader(shadowProgram);
                    int timeLocation = shadowProgram.getUniformLocation("time");
                    shadowProgram.setUniformf(timeLocation,time);
                    super.draw(batch, parentAlpha);
                    batch.setShader(null);
                }else {
                    super.draw(batch, parentAlpha);
                }
            }
        };
        group.setSize(width,height);
        group.setPosition(0,0);
        group.setColor(240.0F/255, 217.0F/255, 187.0F/255,1);
        addActor(group);
    }
}
