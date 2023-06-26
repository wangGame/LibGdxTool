package kw.artpuzzle.group.levelgroup;

import com.badlogic.gdx.scenes.scene2d.Group;

public class EnterFour extends Group {
   public EnterFour(float width, float height){
      setSize(width,height);
//      EgeToMiddle spreed = new EgeToMiddle(width,height);
//      addActor(spreed);
//

      MiddleSpreed2 middleSpreed = new MiddleSpreed2(width,height);
      addActor(middleSpreed);

//      CirSpreed cirSpreed = new CirSpreed(width,height);
//      cirSpreed.setStartTime(0f);
//      cirSpreed.setDelay(1.8f);
//      addActor(cirSpreed);


   }
}
