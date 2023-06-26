package kw.artpuzzle.group;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.SnapshotArray;
import com.kw.gdx.utils.basier.BseInterpolation;

public class ProcessUtils extends Group{
   private Group progress;
   public int index;

   public ProcessUtils(int num,Image prefab){
      int width = 224 + (num - 1) * 30;
      progress = new Group(){{
         SpriteDrawable drawable = (SpriteDrawable) prefab.getDrawable();
         for (int i = 1; i <= num; i++) {
            ProGroup image = new ProGroup(drawable);
            addActor(image);
            image.setName(i + "");
            image.initSize(4,4);
         }
      }};
      progress.setHeight(4);
      progress.setWidth(width);
      addActor(progress);
      setSize(width,4);
   }


   public void setPoint(int index) {
      {
         this.index = index;
         //初始化
         for (Actor child : progress.getChildren()) {
            ProGroup child1 = (ProGroup) child;
            child.setWidth(4);
            child1.setBigPoint(true);
         }

         Actor actor = progress.findActor(index + "");
         actor.setWidth(224);
         ((ProGroup) actor).setBigPoint(false);

         float offSetX = 0;
         float fix = 0;
         for (Actor child : progress.getChildren()) {
            child.setX(offSetX);
            offSetX = offSetX + child.getWidth() + 16;
            ((ProGroup)(child)).setFix(fix);
            fix += 16 + 4;
         }
      }
      {
         SnapshotArray<Actor> children = progress.getChildren();
         for (int i = index+1; i <= children.size; i++) {
            ProGroup actor = progress.findActor(i + "");
            actor.setBigPointColor(Color.valueOf("3d0a0a1E"));
            actor.setProcessVisible(false);
            actor.setProcessBgVisible(false);
         }
      }
   }

//   public void enterAnimation() {
//      float time = 0;
//      for (Actor child : progress.getChildren()) {
//         float width = child.getWidth();
//         child.getColor().a = 0;
//         child.addAction(Actions.delay(time,Actions.fadeIn(0.2f)));
//         child.setWidth(4);
//         child.addAction(Actions.delay(time,Actions.sizeTo(width,child.getHeight(),0.5f)));
//         if (width>15){
//            time += 0.5F;
//         }else {
//            time += 0.1f;
//         }
//      }
//   }
   boolean flag;
//   public void qieHuanEnd(int end){
//      ProGroup actor = progress.findActor(end+ "");
//      actor.setWidth(200);
//      float x = actor.getX(Align.center);
//      float y = actor.getY(Align.center);
//      flag = false;
//      actor.setHideProcess();
//      actor.addAction(
//                 Actions.parallel(
//                         Actions.sequence(Actions.sizeTo(4, 4, 0.4f),
//                                 Actions.run(()->{
//                                    flag = true;
//                                    actor.setBigPoint(true);
//                                    actor.setHideProcess();
//                                    qieHuanStart(end+1);
//                                 })),
//                         new Action() {
//                            @Override
//                            public boolean act(float delta) {
//                               actor.setPosition(x+7,y,Align.center);
//                               int size = progress.getChildren().size;
//                               for (int i = end+1; i <= size; i++) {
//                                  Actor actorTemp = progress.findActor(i + "");
//                                  actorTemp.setX(actor.getX(Align.right)+24*(i-end),Align.center);
//                               }
//                               for (int i = 1; i < end; i++) {
//                                  Actor actorTemp = progress.findActor(i + "");
//                                  actorTemp.setX(actor.getX(Align.left)-24 * (end - i),Align.center);
//                               }
//                               if (flag)return true;
//                               return false;
//                            }
//                         }
//
//              ));
//   }



   public void qieHuanEnd1(int end) {
      this.index = end+1;
      {

         int index = 1;
         for (int i = end; i> 1 ; i--) {
            ProGroup actor = progress.findActor((i-1) + "");
            if (actor!=null) {
               flag = false;
               actor.setHideProcess();
               float ax = actor.getX();
               float ay = actor.getY();
               actor.addAction(
                       Actions.sequence(
                               Actions.delay(0.25667f+0.0534f*index),
                               Actions.moveTo(ax-9.38f,ay,0.13667f,new BseInterpolation(0,0,0.75f,1.0f)),
                               Actions.moveTo(ax-2,ay,0.13667f,new BseInterpolation(0.25f,0,1,1))));
            }
            index++;
         }
      }
      {
         ProGroup actor = progress.findActor(end + "");
         actor.setWidth(200);
         actor.setBigPointVisible(false);
         flag = false;
         actor.setHideProcess();
         actor.setProcessBgColor();
         float ax = actor.getX();
         float ay = actor.getY();
         actor.setOrigin(Align.left);
         actor.addAction(Actions.parallel(
                 Actions.moveTo(ax - 1.93f,ay,0.2667f),
                 Actions.sequence(
                         Actions.sizeTo(4,4*1.75f,0.2667f),
                         Actions.run(()->{
                            actor.setBigPoint(true);
                            actor.setBigPointColor(Color.valueOf("#cc6256"));
                            Gdx.app.postRunnable(new Runnable() {
                               @Override
                               public void run() {
                                  actor.upPosition();
                               }
                            });
                         }))
                 ));


         Actor ideaActor = actor.getIdeaActor();
         float ideaActorX = ideaActor.getX();
         float ideaActorY = ideaActor.getY();
         ideaActor.setOrigin(Align.center);
         float tranScaltime = 0.13667f;
         ideaActor.addAction(
                 Actions.parallel(
                         Actions.sequence(
                                 Actions.delay(0.25667f),
                                 Actions.scaleTo(1.35f,1.35f,0.13667f,new BseInterpolation(0,0,0.75f,1.0f)),
                                 Actions.scaleTo(1f,1f,tranScaltime,new BseInterpolation(0.25f,0,1,1))
                                 ),

                          Actions.sequence(
                          Actions.delay(0.25667f),
                          Actions.moveTo(ideaActorX-9.38f,ideaActorY,0.13667f,new BseInterpolation(0,0,0.75f,1.0f)),
                          Actions.moveTo(ideaActorY-2,ideaActorY,tranScaltime,new BseInterpolation(0.25f,0,1,1)))));

//         actor.addAction(Actions.sequence(
//                 Actions.delay(0.26667f),
//                 Actions.moveTo(ax - 8.23f,ay,0.333f,new BseInterpolation(0,0,1.0f,0.75f)),
//                 Actions.moveTo(ax , ay,0.06666f,new BseInterpolation(0,0,1.0f,0.25f))));
//         actor.addAction(Actions.sequence(
//                 Actions.delay(0.26667f),
//                 Actions.scaleTo(ax - 8.23f,ay,0.333f,new BseInterpolation(0,0,1.0f,0.75f)),
//                 Actions.moveTo(ax , ay,0.06666f,new BseInterpolation(0,0,1.0f,0.25f))));

//         actor.addAction(
//                Actions.sequence(
//                        Actions.parallel(
//                                Actions.sizeTo(4, 4, 3.4f),
//                                Actions.sequence(Actions.delay(0f),
//                                        Actions.parallel(
//                                                Actions.sequence(Actions.scaleTo(1.0f,3.0f,3f),
//                                                                Actions.scaleTo(1.0f,1.0f,1.0f)),
//                                                   Actions.sequence(
//                                                           Actions.moveTo(ax-20,ay,0.5f),
//                                                           Actions.moveTo(ax,ay,0.5f)
//                                                   )
//                                                )
//                        ),
//                        Actions.run(new Runnable() {
//                           @Override
//                           public void run() {
//                              actor.setBigPoint(true);
//                           }
//                        }))));
      }
      {
         ProGroup actorTemp = progress.findActor((end + 1)+ "");
         if (actorTemp!=null) {
            flag = false;
            actorTemp.setHideProcess();
            float ax = actorTemp.getX(Align.right);
//            actor.setBigPoint(false);
            actorTemp.setScale(1.0f,2.0f);
            actorTemp.setProcessBgVisible(true);
            actorTemp.getIdeaActor().addAction(
                    Actions.sequence(
                        Actions.scaleTo(0.6f,0.6f,0.1f),
                            Actions.visible(false),
                            Actions.scaleTo(1f,1f)
                            )
            );
            actorTemp.addAction(
                    Actions.parallel(
                            Actions.sequence(
                                     Actions.scaleTo(1.0f,1.1f,0),
                                     Actions.delay(0.0667f),
                                    Actions.scaleTo(1,1,0.33f)
                                    ),
                            Actions.sequence(
                                    Actions.delay(0.0667f),
                                    Actions.sizeTo(224, 4, 0.333f,
                                            new BseInterpolation(0.126f,0,1,0.99f)),
                                    Actions.run(new Runnable() {
                                       @Override
                                       public void run() {
                                          flag = true;
                                       }
                                    })

                            ),
                            new Action() {
                               @Override
                               public boolean act(float delta) {
                                  actor.setX(ax,Align.right);
                                  if (flag){
                                     actorTemp.upPosition();
                                     return true;
                                  }return false;
                               }
                            }));
         }
      }

   }


   public void qieHuanStart(int end) {
      this.index = end;
      Actor actor = progress.findActor(end + "");
      float x = actor.getX(Align.center);
      float y = actor.getY(Align.center);
      ((ProGroup)actor).setBigPoint(false);
      ((ProGroup) actor).setHideProcess();
      actor.addAction(
              Actions.parallel(
                      Actions.sequence(Actions.sizeTo(224, 4, 0.4f),
                              Actions.run(() -> {
                                 flag = true;
                              })),
                      new Action() {
                         @Override
                         public boolean act(float delta) {
                            actor.setPosition(x+5, y, Align.center);
                            int size = progress.getChildren().size;
                            for (int i = end + 1; i <= size; i++) {
                               Actor actorTemp = progress.findActor(i + "");
                               actorTemp.setX(actor.getX(Align.right) + 24 * (i - end), Align.center);
                            }
                            for (int i = 1; i < end; i++) {
                               Actor actorTemp = progress.findActor(i + "");
                               actorTemp.setX(actor.getX(Align.left) - 24 * (end - i), Align.center);
                            }
                            if (flag) return true;
                            return false;
                         }
                      }
              ));
   }

   public void setProcess(float v) {
      setProcess(v,true);
   }

   public void setProcess(float v,boolean isAni){
      ProGroup actor = findActor(index + "");
      if (isAni){
         actor.setProcess(v,true);
      }else {
         actor.setProcess(v,false);
      }
   }

   @Override
   public void act(float delta) {
      super.act(delta);
   }
}
