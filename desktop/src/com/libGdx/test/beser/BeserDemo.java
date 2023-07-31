package com.libGdx.test.beser;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.IntAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.animation.effect.EffectTool;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.GameInfo;
import com.libGdx.test.base.LibGdxTestMain;


/**
 * @Auther jian xian si qi
 * @Date 2023/7/17 13:58
 */
@GameInfo(width = 720,height = 1280,batch = Constant.COUPOLYGONBATCH,viewportType = Constant.EXTENDVIEWPORT)
public class BeserDemo extends LibGdxTestMain {
    private long lastTime ;
    public static void main(String[] args) {
        BeserDemo test = new BeserDemo();
        test.start(test);
    }
    private Array<Vector2> controlPoint = new Array<Vector2>();
    private Array<Image> array = new com.badlogic.gdx.utils.Array<>();
    private Road road;
    private RoadPic pic;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        road = new Road();



        Vector2 vector2[] = new Vector2[5];
        vector2[0] = new Vector2(600,600);
        vector2[1] = new Vector2(390,830);
        vector2[2] = new Vector2(110,230);
        vector2[3] = new Vector2(10,230);
        vector2[4] = new Vector2(50,230);


        controlPoint.add(vector2[0]); //起点
        controlPoint.add(vector2[1]);
        controlPoint.add(vector2[2]);
        controlPoint.add(vector2[3]);
        controlPoint.add(vector2[4]);



        for (Vector2 vector11 : controlPoint) {
            Image image = new Image(new Texture("white_cir.png"));
            stage.addActor(image);
            image.setPosition(vector11.x,vector11.y);
            image.addListener(imgaeListener);
            array.add(image);

        }

        road.initPoint(vector2);
        pic = new RoadPic(road,720,720,0,0);
        stage.addActor(pic);
        pic.update();
        pic.setTouchable(Touchable.disabled);

        stage.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                long l = System.currentTimeMillis() - lastTime;
                if (l<1000){
                    Image image = new Image(new Texture("white_cir.png"));
                    stage.addActor(image);
                    image.setPosition(x,y);
                    image.addListener(imgaeListener);
                    Image image1 = array.removeIndex(array.size - 1);
                    array.add(image);
                    array.add(image1);
                    controlPoint.clear();
                    for (int i = 0; i < array.size; i++) {
                        controlPoint.add(array.get(i).getPosition());
                    }

                    Vector2[] vector2s = controlPoint.toArray();
                    road.initPoint(vector2s);
                    pic.update();
                }
            }
        });

        IntAction intAction = new IntAction(){
            @Override
            public boolean act(float delta) {
                int value = getValue();
                System.out.println(value);
//                pic.update(value);
                return super.act(delta);
            }
        };
        intAction.setStart(0);
        intAction.setEnd(100);
        intAction.setDuration(5);
        stage.addAction(intAction);
    }


    private ClickListener imgaeListener = new ClickListener(){
        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            super.touchDragged(event, x, y, pointer);
            event.getTarget().setPosition(event.getStageX(),event.getStageY());
            controlPoint.clear();
            for (int i = 0; i < array.size; i++) {
                controlPoint.add(array.get(i).getPosition());
            }
            int size = controlPoint.size;
            Vector2[] vector2s = new Vector2[size];
            for (int i = 0; i < size; i++) {
                Vector2 vector2 = controlPoint.get(i);
                vector2s[i] = vector2;
            }
            road.initPoint(vector2s);
            pic.update();
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);
            int size = controlPoint.size;
            Vector2[] vector2s = new Vector2[size];
            for (int i = 0; i < size; i++) {
                Vector2 vector2 = controlPoint.get(i);
                vector2s[i] = vector2;
            }
            road.initPoint(vector2s);
            pic.update();
        }
    };

}
