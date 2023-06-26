package kw.artpuzzle.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.kw.gdx.BaseGame;
import com.kw.gdx.constant.Configuration;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.screen.BaseScreen;
import com.kw.gdx.utils.log.NLog;
import com.kw.gdx.utils.log.StringUtils;
import com.kw.gdx.zip.PackZip;

import kw.artpuzzle.constant.GameStaticInstance;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.csv.InitCsvData;
import kw.artpuzzle.csv.LevelOrder;
import kw.artpuzzle.downLoad.DownLoadLevelTempUtils;
import kw.artpuzzle.pref.ArtPuzzlePreferece;

abstract class UserBaseScreen extends BaseScreen {
    private Long outTime = 0L;
    protected Group loadingImage;



   public UserBaseScreen(BaseGame game) {
      super(game);
   }


  public abstract boolean loadingAnimation();

   @Override
   public void pause() {
      super.pause();
      outTime = System.currentTimeMillis();
       LevelConfig.enterGame = false;
   }

   protected void removeLoading(){

   }

   protected boolean resumeLoading;

   @Override
   public void resume() {
      super.resume();
      if (outTime == 0L)return;

      if (System.currentTimeMillis() - outTime>LevelConfig.leaveTime) {
          LevelConfig.enterGame = true;
          resumeLoading = false;
          resumeLoading = loadingAnimation();
          NLog.e("ads + loading");
          if (this instanceof GameScreen){
              GameStaticInstance.gameListener.hideBanner();
          }
          stage.addAction(Actions.delay(1,Actions.run(()->{
              if (resumeLoading) {
                  removeLoading();
              }
              if (this instanceof GameScreen) {
                  if (LevelConfig.bannerStatus) {
                      if(LevelConfig.ENTERGAME != LevelConfig.VIEW) {
                          GameStaticInstance.gameListener.showBanner();
                      }
                  }
              }
              if(LevelConfig.ENTERGAME != LevelConfig.VIEW) {
                  if(LevelConfig.countTime>LevelConfig.interstitial) {
                      GameStaticInstance.gameListener.showInterstitial(2000L);
                  }
              }
          })));
      }
   }

    @Override
    public void render(float delta) {
        super.render(delta);
        LevelConfig.countTime += delta;
    }
}
