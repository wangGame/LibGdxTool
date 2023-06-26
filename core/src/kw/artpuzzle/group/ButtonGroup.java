package kw.artpuzzle.group;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;

public class ButtonGroup extends Group {
   public ButtonGroup(){
      Image buttonBg = new Image(Asset.getAsset().getTexture(""));
      addActor(buttonBg);
      setSize(buttonBg.getWidth(),buttonBg.getHeight());
      Label label = new Label("",new Label.LabelStyle(){
         {

         }
      });
      label.setAlignment(Align.center);
      label.setText("CONTINUE");
      addActor(label);
      label.setPosition(getWidth()/2,getHeight()/2,Align.center);
   }
}
