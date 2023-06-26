package kw.artpuzzle.group.levelgroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

import kw.artpuzzle.theme.GameTheme;

public class EgeToMiddle extends Group {
   private float time = 0;
   private float startTime;
   private float delay;
   private float speed = 1.0f;
   public EgeToMiddle(float width, float height) {
      setSize(width, height);
      Texture texture = Asset.getAsset().getTexture("noise.png");
      ShaderProgram shadowProgram = new ShaderProgram(
              Gdx.files.internal("shader/enterGameshader/tomiddle.vert"),
              Gdx.files.internal("shader/enterGameshader/tomiddle.frag"));
      Image group = new Image(texture){

         @Override
         public void act(float delta) {
            super.act(delta);
            delay -= delta;
            if (delay<=0) {
               time += delta * speed * 0.3;
            }
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
      group.setColor(GameTheme.enterGameColor);
      addActor(group);
   }

   public void setStartTime(float startTime) {
      this.startTime = startTime;
      this.time = startTime;
   }

   public void setDelay(float delay) {
      this.delay = delay;
   }

   public void setSpeed(float speed) {
      this.speed = speed;
   }
}

