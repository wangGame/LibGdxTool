package kw.artpuzzle.group.levelgroup;

import com.badlogic.gdx.scenes.scene2d.Group;

public class EnterThree extends Group {
   public EnterThree(float width, float height){
      setSize(width,height);
      EgeToMiddle spreed = new EgeToMiddle(width,height);
      addActor(spreed);
      spreed.setSpeed(1.4f);
   }
}
