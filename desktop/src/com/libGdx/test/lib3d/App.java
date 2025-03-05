package com.libGdx.test.lib3d;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.group.ModelActor;
import com.kw.gdx.group.ModelActorManager;
import com.kw.gdx.animation3d.RotationAction;
import com.libGdx.test.base.LibGdxTestMain;
import com.libGdx.test.model.ModelUtils;

public class App extends LibGdxTestMain{
    private float time = 1;
    private boolean touchx = false;
    private Vector2 touchV2 = new Vector2();
    private boolean fx = true;
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        rotation();
//        scaleAction();
        rotationAction();
//        scaleActor();
//        scaleActor();
    }

    private void scaleActor() {
        Group group = new Group();



//        actor.setPosition(new Vector3(0,0,-700));
//        actor.setOrigin(Align.center);
//        addActor(group);
//        group.setPosition(400,400);
//        actor.rotation(20,30,20);


//        group.addAction(Actions.rotateTo(100,4));
//        group.addAction(Actions.sequence(
//                Actions.moveToAligned(500,1000,Align.center,4),
//                Actions.scaleTo(3,3,4)
//                ));
//
//        group.addAction(Actions.forever(
//                Actions.rotateBy(19,0.1f)
//        ));
    }

    private void rotationAction() {

        ModelInstance instance = ModelUtils.createInstance(0);
        ModelActor actor = new ModelActor(instance);
        actor.setPosition(new Vector3(0,0,-100));
        ModelActorManager modelActorManager = new ModelActorManager(actor);
        addActor(modelActorManager);
        modelActorManager.setPosition(400,500,Align.center);
        modelActorManager.setRotation(10);
        modelActorManager.setDebug(true);
//        actor.addAction(Actions.forever(Actions.rotateBy(10,1)));

        RotationAction rotationAction = new RotationAction();
        rotationAction.setEndX(100);
        rotationAction.setEndY(100);
        rotationAction.setEndZ(100);
        rotationAction.setDuration(1);
        modelActorManager.setOrigin(Align.center);


        RotationAction action = new RotationAction();
        action.setDuration(1f);
        action.setEndX(23.37f);
        action.setEndY(-17.74F);
        action.setEndZ(-164.67F);

        RotationAction action1 = new RotationAction();
        action1.setDuration(1f);
        action1.setEndX(0);
        action1.setEndY(0F);
        action1.setEndZ(-180F);

        modelActorManager.addAction(Actions.sequence(
                Actions.scaleTo(2,2,0.4f),
                Actions.scaleTo(0.2f,0.2f,0.4f)
                ));

//        modelActorManager.addAction(Actions.rotateTo(150,0.3f));
        modelActorManager.addAction(Actions.sequence(
                action, Actions.delay(0.3f),action1
        ));
//


//        modelActorManager.addAction();



    }


    private void scaleAction() {
//        ModelInstance instance = ModelUtils.createInstance(0);
//        actor = new ModelActor(instance);
//        actor.setSize(100,100);
//        actor.setDebug(true);
//        addActor(actor);
//        actor.setPosition(new Vector3(300,300,-100));
//        actor.setOrigin(Align.center);
//        actor.addAction(Actions.sequence(
//                Actions.scaleTo(1.4f,1.4f, 5)
//        ));

    }

/*

    private void moveAction() {
        ModelInstance instance = ModelUtils.createInstance();
        actor = new ModelActor(instance);
        actor.setSize(100,100);
        actor.setDebug(true);
        addActor(actor);
        actor.setPosition(new Vector3(300,300,-100));

        actor.addAction(Actions.sequence(
                Actions.moveToAligned(500,500, Align.center,4f, Interpolation.bounce)
        ));

    }
*/

    private void rotation() {
//        ModelInstance instance = ModelUtils.createInstance(0);
//        actor = new ModelActor(instance){
//            @Override
//            public void act(float delta) {
//                super.act(delta);
//
//                    time -= delta;
//                    if (time>0) {
//                        Vector3 lrot = actor.get_lrot();
//                        Vector2 touchV3 = touchV2;
//                        float deltaX = touchV3.x *time;
//                        float deltaY = touchV3.y *time;
//                        if (fx) {
//                            actor.rotation(lrot.x - deltaY, lrot.y - deltaX, 0);
//                        }else {
//                            actor.rotation(lrot.x - deltaY, lrot.y + deltaX, 0);
//                        }
//                    }
//
//            }
//        };
//        actor.setSize(200,200);
//        addActor(actor);
//        actor.setScale(new Vector3(0.5f,0.5f,0.5f));
//        actor.setPosition(new Vector3(100,100,-100));
//        actor.addListener(new ActorGestureListener(){
//
//
//            @Override
//            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchDown(event, x, y, pointer, button);
//                touchx = true;
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchUp(event, x, y, pointer, button);
//
//
//            }
//
//            @Override
//            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
//                super.pan(event, x, y, deltaX, deltaY);
//                Vector3 lrot = actor.get_lrot();
//                System.out.println(lrot);
//                touchV2.set(deltaX,deltaY);
//                time = 1;
//                if (Math.abs(lrot.x % 360 )>=90&&Math.abs(lrot.x % 360 )<=270) {
//                    fx = true;
//                    actor.rotation(lrot.x - deltaY, lrot.y - deltaX, 0);
//                }else {
//                    fx = false;
//                    actor.rotation(lrot.x - deltaY, lrot.y + deltaX, 0);
//                }
//                tileGameObject.rotate(new Vector3(1,0,0),deltaX);

//                public void rotate(float yaw, float pitch, float roll) {
//                    rotate(Vector3.Y, yaw);
//                    rotate(Vector3.X, pitch);
//                    rotate(Vector3.Z, roll);
//                }

//            }
//        });
    }
}