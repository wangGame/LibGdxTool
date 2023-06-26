package kw.artpuzzle.group;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;

public class ProGroup extends Group {
   private Image process;
   private Image processBg;
   private Image pointIdea;

   public ProGroup(SpriteDrawable drawable){
      TextureRegion region = new TextureRegion(drawable.getSprite());
      process = new Image(new NinePatch(region,2,2,0,0));
      processBg = new Image(new NinePatch(region,2,2,0,0));
      pointIdea = new Image(Asset.getAsset().getTexture("cocos/gameasset/progress_kuai1.png"));
      addActor(pointIdea);
      addActor(processBg);
      addActor(process);
      processBg.setSize(4,4);
      processBg.setColor(Color.valueOf("3d0a0a1e"));
      process.setColor(Color.valueOf("cc6256"));
      pointIdea.setColor(Color.valueOf("cc6256"));
      pointIdea.setVisible(false);
   }

   @Override
   protected void sizeChanged() {
      super.sizeChanged();
      processBg.setWidth(getWidth());
      pointIdea.setX(processBg.getX(Align.left));
   }

   public void upPosition(){
      setX(fix);
   }

   @Override
   public void setColor(Color color) {
      super.setColor(color);
      pointIdea.setColor(color);
   }

   public void setProcess(float width,boolean isAni){
      float height = process.getHeight();
      if (isAni) {
         process.addAction(Actions.sizeTo(width * processBg.getWidth(), height, 0.2f));
      }else {
         process.setWidth(width * processBg.getWidth());
      }
      if (width==0){
         process.setVisible(false);
      }else {
         process.setVisible(true);
      }
   }

   public void setProcess(float width){
      setProcess(width,true);
   }



   public void initSize(int i, int i1) {
      setSize(i,i1);
      process.setSize(i,i1);
      processBg.setSize(i,i1);
      process.setPosition(getWidth()/2,getHeight()/2,Align.center);
      processBg.setPosition(getWidth()/2,getHeight()/2,Align.center);
      pointIdea.setPosition(getWidth()/2,getHeight()/2,Align.center);
   }

   public void setHideProcess() {
      process.setVisible(false);
   }

   public void setBigPoint(boolean b) {
      pointIdea.setVisible(b);
   }

   public void setBigPointColor(Color color){
      pointIdea.setColor(color);
   }

   public void setBigPointVisible(boolean b){
      pointIdea.setVisible(b);
   }

   public void setProcessColor(Color color){
      process.setColor(color);
   }

   public void setProcessVisible(boolean isv){
      process.setVisible(isv);
   }

   public void setProcessBgVisible(boolean isv){
      processBg.setVisible(isv);
   }

   public void setProcessBgColor() {
      processBg.setColor(Color.valueOf("3d0a0a1E"));
   }

   public Actor getIdeaActor() {
      return pointIdea;
   }

   private float fix = 0;
   public void setFix(float fix) {
      this.fix = fix;
   }

   @Override
   public void act(float delta) {
      super.act(delta);
   }
}
