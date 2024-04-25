package com.libGdx.test.endless;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/9/22 16:40
 */
public class MainBannerGroupBig extends Group {
    private float pageWidth;
    private Array<FloatItemGroup> allBanner;
    private float timeCount = 0;
    private int current = -2;
    private boolean enterItem;

    public MainBannerGroupBig(){
        allBanner = new Array<>();
        float scrollHeight = 280;
        setSize(720,scrollHeight);
        ArrayList<Integer> arrayList = new ArrayList<>();
        int index = -2;
        arrayList.add(1);
        arrayList.add(1);
        arrayList.add(1);
        arrayList.add(1);
        arrayList.add(1);
        arrayList.add(1);
        for (int i = 1; i <= arrayList.size(); i++) {
            FloatItemGroup group = new FloatItemGroup(i);
            addActor(group);
            pageWidth = group.getWidth();
            group.setX(index * pageWidth + 20);
            allBanner.add(group);
            index ++;
        }
        addListener(new ClickListener(){
            private float touchX;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchX = x;
                enterItem = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                for (Actor child : allBanner) {
                    if (child instanceof FloatItemGroup) {
                        ((FloatItemGroup)(child)).dragMoveX(-(x - touchX));
                    }
                }
                super.touchDragged(event, x, y, pointer);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                enterItem = false;
                for (FloatItemGroup floatItemGroup : allBanner) {
                    floatItemGroup.updateBaseGroupX();
                }
                scrollItem(x,touchX);
            }
        });
    }



    private void scrollItem(float x,float touchX) {
        if (x - touchX < -40) {
            FloatItemGroup group = allBanner.get(0);
            if (group.getX()>-500) {
                return;
            }
            current--;
            FloatItemGroup floatItemGroup = allBanner.removeIndex(0);
            FloatItemGroup floatItemGroup1 = allBanner.get(allBanner.size-1);
            floatItemGroup.setX(floatItemGroup1.getX()+floatItemGroup1.getWidth());
            floatItemGroup.updateBaseGroupX();
            allBanner.add(floatItemGroup);
        }
        if (x - touchX > 40) {
            FloatItemGroup group = allBanner.get(allBanner.size - 1);
            if (group.getX() < 1080){
                return;
            }
            current++;
            FloatItemGroup floatItemGroup = allBanner.removeIndex(allBanner.size-1);
            FloatItemGroup floatItemGroup1 = allBanner.get(0);
            floatItemGroup.setX(floatItemGroup1.getX()-floatItemGroup1.getWidth());
            floatItemGroup.updateBaseGroupX();
            allBanner.insert(0,floatItemGroup);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timeCount += delta;
        if (enterItem){
            timeCount = 0;
        }
    }
}
