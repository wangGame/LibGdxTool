package kw.artpuzzle.listener;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kw.gdx.listener.ButtonListener;
import com.kw.gdx.sound.AudioProcess;

public class LevelItemListener extends LevelItemClickListener {
   private String audioName;

   public LevelItemListener(Actor target) {
      super(target);
   }

   public LevelItemListener(float scale) {
      super(scale);
   }

   public LevelItemListener(String audioName) {
      this.audioName = audioName;
   }

   public LevelItemListener() {
   }

   public void clickEffect() {
      super.clickEffect();
      if (this.audioName == null) {
         AudioProcess.playSound("audio/artpuzzle_click.ogg");
      } else if (!"".equals(this.audioName)) {
         AudioProcess.playSound(this.audioName);
      }

   }

   public void touchDownEffect() {
   }

   protected void releaseEffect() {
      super.releaseEffect();
   }
}