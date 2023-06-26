package kw.artpuzzle.group;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.kw.gdx.resource.annotation.ScreenResource;
import com.kw.gdx.sound.AudioProcess;
import com.kw.gdx.sound.AudioType;
import com.kw.gdx.utils.ads.BannerView;
import com.kw.gdx.utils.basier.BseInterpolation;
import com.kw.gdx.view.dialog.DialogManager;
import com.kw.gdx.view.dialog.base.BaseDialog;

import kw.artpuzzle.ArtPuzzle;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.signlistener.SignListener;

@ScreenResource("cocos/levelBottomView.json")
public class BottomPanel extends BaseDialog {
   private SignListener listener;
   private Group panel;

   public BottomPanel(SignListener signListener){
      this.listener = signListener;
   }

   @Override
   public void show() {
       super.show();

//       panel.addAction(Actions.moveToAligned(baseX,20 - v/2.0f + BannerView.pxToDp(50) + 20,Align.bottom,0.2f));
       dialogGroup.findActor("empty_click").addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y) {
               super.clicked(event, x, y);
               dialogManager.closeDialog(BottomPanel.this);
               AudioProcess.playSound(AudioType.clickA);
           }
       });

       findActor("lbv_view_click").addListener(new OrdinaryButtonListener(){
           @Override
           public void clicked(InputEvent event, float x, float y) {
               super.clicked(event, x, y);
               closeDialog(LevelConfig.VIEW);
           }
       });
       findActor("lbv_restart_click").addListener(new OrdinaryButtonListener(){
           @Override
           public void clicked(InputEvent event, float x, float y) {
               super.clicked(event, x, y);
               closeDialog(LevelConfig.RESET);
           }
       });
       findActor("lbv_restart_cancel").setOrigin(Align.center);
       findActor("lbv_restart_cancel").addListener(new OrdinaryButtonListener(){
           @Override
           public void clicked(InputEvent event, float x, float y) {
               super.clicked(event, x, y);
               dialogManager.closeDialog(BottomPanel.this);
           }
       });
//
       float v1 = Constant.GAMEWIDTH/720;
       float v2 = Constant.GAMEHIGHT / 1280.0f;
       dialogGroup.setOrigin(Align.bottom);
       dialogGroup.setScale(Math.min(v1,v2));
   }

   public void enterAnimation(){
       panel = findActor("panel");
       panel.setY(0,Align.top);
       float baseX = panel.getX(Align.center);
       float v = (Constant.GAMEHIGHT - 1280.0f) / 2.0f;
       panel.addAction(
               Actions.sequence(
                       Actions.moveToAligned(baseX,20 - v + 11.87f,
                               Align.bottom,0.2667f,new BseInterpolation(0,0,0.688f,1.0f)),
                       Actions.moveToAligned(baseX,20 - v ,
                               Align.bottom,0.1667f,new BseInterpolation(0.25f,0,1,1))
                       ));
   }

    private void closeDialog(int type) {
        dialogManager.closeDialog(BottomPanel.this);
        listener.sign(type);
    }

    @Override
    public void close() {
        float baseX = panel.getX(Align.center);
        panel.addAction(Actions.sequence(
                Actions.moveToAligned(baseX,0,Align.top,0.26667f,
                        new BseInterpolation(0.25f,0,1.0f,1.0f)),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        BottomPanel.this.remove();
                    }
                })
        ));
    }
}
