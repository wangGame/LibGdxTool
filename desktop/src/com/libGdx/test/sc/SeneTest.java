package com.libGdx.test.sc;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.constant.Constant;

import java.util.ArrayList;

public class SeneTest extends Group {

    public SeneTest() {
        setSize(Constant.GAMEWIDTH, Constant.GAMEHIGHT);
        ArrayList<String> dataList = new ArrayList<>();
        FileHandle[] list = Gdx.files.internal("assets/v1").list();
        for (int i = 0; i < list.length; i++) {
            FileHandle fileHandle = list[i];
            String name = fileHandle.name();
            dataList.add(name);
        }
        Adapter<String> adapter = new Adapter<String>() {
            @Override
            public Actor createView() {
                ItemActor itemActor = new ItemActor();
                System.out.println("真正创建了一个 ItemActor");
                return itemActor;
            }

            @Override
            public void bindView(Actor view, String data, int index) {
                ItemActor itemActor = (ItemActor) view;
                itemActor.setData(data);
            }
        };
        VirtualScrollList<String> virtualList = new VirtualScrollList<>(
                Constant.GAMEWIDTH,
                Constant.GAMEHIGHT,
                500,
                adapter
        );

        virtualList.setItems(dataList);
        addActor(virtualList);
    }
}