package com.libGdx.test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.libGdx.test.base.LibGdxTestMain;

public class Sq extends LibGdxTestMain {
    private ThreeStage threeStage;

    public static void main(String[] args) {
        Sq sq = new Sq();
        sq.start();
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        threeStage = new ThreeStage();




        Actor actor = new Actor();
        actor.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        actor.addListener(new OrdinaryButtonListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);

            }
        });

        addActor(actor);
    }
    float rx = 0;
    float ry = 0;
    @Override
    public void render() {
        Gdx.gl.glClearColor(0.455f, 0.725f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        super.render();

        if (threeStage!=null) {
            rx += 1f;
            ry += 0.5f;

            threeStage.rotate(rx, ry);
            threeStage.render();
        }
    }
}
