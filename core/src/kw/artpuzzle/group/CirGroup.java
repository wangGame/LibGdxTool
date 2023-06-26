package kw.artpuzzle.group;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.utils.basier.BseInterpolation;

public class CirGroup extends Group {
    private ShaderProgram program;
    private float time = 0;
    private TextureRegion region;
    private Image image;
    private BseInterpolation bseInterpolation;
    private float delay;
    public CirGroup(TextureRegion actor){
        program = new ShaderProgram(//shader/levelitem/cir.glsl
                Gdx.files.internal("shader/levelitem/cir.vert"),
                Gdx.files.internal("shader/levelitem/cir.glsl"));
        this.region = actor;
        this.bseInterpolation = new BseInterpolation(0.0f,0.0f,0.75f,1.0f);
        image = new Image(actor);
        addActor(image);
    }

    public void setV(float v) {
        this.v = v;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    private float delayCount = 0;
    private float v;
    @Override
    public void act(float delta) {
        super.act(delta);
//        v += delta * 1.52f;
        delayCount += delta;
        if (delayCount < delay){
            return;
        }
        v += delta;
        if (v>=1.0f){
            v = 1.0f;
        }
        time = bseInterpolation.apply(v);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setShader(program);
        program.setUniformf("u2",region.getU2());
        program.setUniformf("u",region.getU());
        program.setUniformf("v",region.getV());
        program.setUniformf("v2",region.getV2());

        program.setUniformf("time",time*0.44f);
        super.draw(batch, parentAlpha);
        batch.setShader(null);
    }
}