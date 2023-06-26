package kw.artpuzzle.group.levelgroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

import kw.artpuzzle.theme.GameTheme;

public class MiddleSpreed extends Group {
   private float delay;
   private float dd;
   private float time;
   private float count;


   public MiddleSpreed(float width,float height) {
      setSize(width,height);

      Texture texture = Asset.getAsset().getTexture("noise.png");
      ShaderProgram shadowProgram = new ShaderProgram(
              Gdx.files.internal("shader/enterGameshader/middlespreed.vert"),
              Gdx.files.internal("shader/enterGameshader/middlespreed.frag"));
      Image group = new Image(texture) {

         @Override
         public void act(float delta) {
            super.act(delta);
            dd += delta;
            if (dd > delay){
               time +=delta * 0.4f * speed;
            }
         }

         @Override
         public void draw(Batch batch, float parentAlpha) {
            if (shadowProgram != null) {
               batch.setShader(shadowProgram);
               int timeLocation = shadowProgram.getUniformLocation("time");
               shadowProgram.setUniformf(timeLocation, time);
               super.draw(batch, parentAlpha);
               batch.setShader(null);
            } else {
               super.draw(batch, parentAlpha);
            }
         }
      };
      group.setSize(width, height);
      group.setPosition(0, 0);
      group.setColor(GameTheme.enterGameColor);
      addActor(group);
   }

   public void setDelay(float v) {
      this.delay = v;
   }

    public void setStartTime(float startTime) {

      this.time = startTime;
    }

    private float speed = 1.0f;
   public void setSpeed(float v) {
      this.speed = v;
   }
}
