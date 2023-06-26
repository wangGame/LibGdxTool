package kw.artpuzzle.group;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

import kw.artpuzzle.shadermanager.ShaderFactory;
import kw.artpuzzle.shadermanager.ShaderManager;
import kw.artpuzzle.shadermanager.ShaderType;

public class ShaderImage extends Image {
    private ShaderProgram shadowProgram;
    private Texture texture;
    private float time = 0;
    private boolean startAnimation = false;
    public ShaderImage(TextureRegion region){
        super(region);
        shadowProgram = ShaderManager.getManager().getType(ShaderType.outSpreadMiddle);
        texture = Asset.getAsset().getTexture("XPvTc.png");
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        time+=delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (shadowProgram!=null && startAnimation) {
            batch.setShader(shadowProgram);
            int timeLocation = shadowProgram.getUniformLocation("time");
            int u_t = shadowProgram.getUniformLocation("u_texture2");
            int v_color1 = shadowProgram.getUniformLocation("v_color1");
            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
            texture.bind();
            shadowProgram.setUniformi(u_t,1);
            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
            shadowProgram.setUniformf(v_color1,1,1,1,1);
            shadowProgram.setUniformf(timeLocation,time*0.7F);
            super.draw(batch, parentAlpha);
            batch.setShader(null);
        }else {
            super.draw(batch, parentAlpha);
        }
    }

    public void setAnimation() {
        startAnimation = true;
        time = 0;
        addAction(Actions.delay(2,Actions.run(()->{
            startAnimation = false;
        })));
    }
}
