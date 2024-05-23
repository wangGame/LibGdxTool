package com.libGdx.test.moni;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.libGdx.test.base.LibGdxTestMain;

import org.lwjgl.openal.AL;

public class Appp extends LibGdxTestMain {
    public static void main(String[] args) {
        Appp appp = new Appp();
        appp.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        stage.setDebugAll(true);
        CalUtils.mainTest(
                300,400,700,800,100,100,300,100,400,200
        );
        Actor actor = new Actor();
        addActor(actor);
        actor.setSize(1000,1000);


        Actor actor1 = new Actor();
        addActor(actor1);
        actor1.setSize(100,100);
        actor1.setPosition(300,400, Align.center);


        Actor actor2 = new Actor();
        addActor(actor2);
        actor2.setSize(100,100);
        actor2.setPosition(450,550, Align.center);

        Group group = new Group();
        group.setSize(400,200);
        addActor(group);

        Actor actor3 = new Actor();
        group.addActor(actor3);
        actor3.setSize(100,100);
        actor3.setPosition(100,100, Align.center);

        Actor actor4 = new Actor();
        group.addActor(actor4);
        actor4.setSize(100,100);
        actor4.setPosition(300,100, Align.center);

        group.setPosition(370,470,Align.center);
        group.setOrigin(Align.center);
        group.setRotation(45);

    }
}
