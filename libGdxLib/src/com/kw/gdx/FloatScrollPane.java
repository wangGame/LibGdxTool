package com.kw.gdx;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.cocosload.CocosResource;

public class FloatScrollPane extends Group {
    private Image floatPanel;
    private Group floatGroup;
    private float lastPosY = 0;
    private float offSetUp;

    private void initTopView(Table table) {
//        Table table1 = new Table(){{
//            for (int i = 0; i < 5; i++) {
//                Group group = CocosResource.loadFile("cocos/mainBottomItem.json");
//                add(group).padLeft(-30).padTop(50);
//                Group group1 = CocosResource.loadFile("cocos/mainBottomItem.json");
//                add(group1).padLeft(30).padTop(50);
//                row();
//            }
//            pack();
//        }};

        ScrollPane pane2 = new ScrollPane(table,new ScrollPane.ScrollPaneStyle()){
            @Override
            public void act(float delta) {
                super.act(delta);
                float scrollPercentY = getScrollY();
                float v = scrollPercentY - lastPosY;
                if (floatPanel.getY()>=0 &&floatPanel.getX()<=floatGroup.getHeight()+offSetUp){
                    if (scrollPercentY<Float.MAX_VALUE&&scrollPercentY>Float.MIN_VALUE) {
                        float v1 = floatPanel.getY() + v;
                        if (v1<0)v1=0;
                        if (v1>floatGroup.getHeight()+offSetUp)v1 = floatGroup.getHeight();
                        floatPanel.setY(v1);
                        lastPosY = scrollPercentY;
                    }
                }
            }
        };
        pane2.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT-400);
        addActor(pane2);
        pane2.setY(Constant.GAMEHIGHT/2, Align.center);
        floatGroup = new Group(){
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.flush();
                if (clipBegin(getX(),getY(),getWidth(),getHeight())) {
                    super.draw(batch, parentAlpha);
                    batch.flush();
                    clipEnd();
                }
            }
        };
        addActor(floatGroup);
        floatGroup.setSize(Constant.GAMEWIDTH,200);
        floatGroup.setY(Constant.GAMEHIGHT-200,Align.top);
        floatPanel = new Image(Asset.getAsset().getTexture("loading/icon.png"));
        floatPanel.setWidth(Constant.GAMEWIDTH);
        floatGroup.addActor(floatPanel);
    }
}
