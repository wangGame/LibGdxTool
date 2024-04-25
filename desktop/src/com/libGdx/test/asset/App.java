package com.libGdx.test.asset;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.resource.cocosload.CocosResource;
import com.libGdx.test.base.LibGdxTestMain;

public class App extends LibGdxTestMain {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Group group = CocosResource.loadFile("cocos/GameScreen.json");
        addActor(group);
        setGroupVisible(group);
    }

    private void setGroupVisible(Actor group) {
        group.setVisible(true);
        if (group instanceof Group){
            for (Actor child : ((Group)group).getChildren()) {
                setGroupVisible(child);
            }
        }
    }
}
