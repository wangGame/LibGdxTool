package com.libGdx.test.group;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class GroupTest extends LibGdxTestMain {
    public static void main(String[] args) {
        GroupTest test = new GroupTest();
        test.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Table group = new Table();
        addActor(group);
        group.setColor(Color.BLACK);

        Image image = new Image(Asset.getAsset().getTexture("assets/0_1_41_512.jpg"));
        group.add(image);
        group.pack();


    }
}
