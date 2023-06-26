package kw.artpuzzle.group.levelgroup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Configuration;

import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.shadermanager.ShaderManager;
import kw.artpuzzle.shadermanager.ShaderType;

public class BgEnterAni {
    private Group enterGroup;
    public BgEnterAni(float width, float height){
        if (LevelConfig.enterAni == 0) {
            enterGroup = new EnterOne(width,height);
        }else if (LevelConfig.enterAni == 1){
            enterGroup = new EnterTwo(width,height);
        }else if (LevelConfig.enterAni == 2){
            enterGroup = new EnterThree(width,height);
        }else if (LevelConfig.enterAni == 3){
            enterGroup = new EnterFour(width,height);
        }else if (LevelConfig.enterAni == 4){
            enterGroup = new EnterFive(width,height);
        }


        if (Configuration.device_state == Configuration.DeviceState.poor){
                enterGroup = new EnterFive(width,height);
        }


//        else if (LevelConfig.enterAni == 6){
//            enterGroup = new EnterOne(width,height);
//        }else if (LevelConfig.enterAni == 7){
//            enterGroup = new EnterOne(width,height);
//        }else if (LevelConfig.enterAni == 8){
//            enterGroup = new EnterOne(width,height);
//        }
    }

    public Group getEnterGroup() {
        return enterGroup;
    }
}
