package kw.artpuzzle.group.levelgroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

public class EnterTwo extends Group {
   public EnterTwo(float width, float height){
      setSize(width,height);
      MiddleSpreed spreed = new MiddleSpreed(width,height);
      spreed.setStartTime(0.45f);
      spreed.setSpeed(1.03f);
      spreed.setDelay(0);
      addActor(spreed);
   }
}
