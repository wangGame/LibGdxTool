package kw.artpuzzle.group;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import org.w3c.dom.ls.LSOutput;

public class Process extends Group {
   private Image bg;
   private Image pro;
   public Process(Drawable drawable){
      bg = new Image(drawable);
      pro = new Image(drawable);
      addActor(bg);
      addActor(pro);
   }

   @Override
   public void setSize(float width, float height) {
      super.setSize(width, height);
   }

   public void setProcess(float pro){

   }
}
