package kw.artpuzzle.group.levelgroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

public class EnterOne extends Group {
   public EnterOne(float width,float height){
      setSize(width,height);
      MiddleSpreed middleSpreed = new MiddleSpreed(width,height);
      middleSpreed.setStartTime(0.6f);
      addActor(middleSpreed);
      EgeToMiddle spreed = new EgeToMiddle(width,height);
      addActor(spreed);
      spreed.setStartTime(0.4f);
//      CirSpreed cirSpreed = new CirSpreed(width,height);
//      cirSpreed.setDelay(0.7f);
////      addActor(cirSpreed);
//      cirSpreed.setY(150);
   }
}
