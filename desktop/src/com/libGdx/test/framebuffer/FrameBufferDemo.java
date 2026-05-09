package com.libGdx.test.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.utils.ImageUtils;
import com.libGdx.test.base.LibGdxTestMain;

public class FrameBufferDemo extends LibGdxTestMain {
    TextureRegion bufferTexture;
    public static void main(String[] args) {
        FrameBufferDemo frameBufferDemo = new FrameBufferDemo();
        frameBufferDemo.start();
    }
    FrameBufferGroup group;
    private Image temp;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        ScrollPane scrollPane = new ScrollPane(new Table(){{
            for (int i = 0; i < 100; i++) {
                Image image = new Image(Asset.getAsset().getTexture("ad_progress.png"));
                add(image);
                image.setDebug(true);
                row();
            }
            pack();
        }}){

        };
        scrollPane.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        scrollPane.setDebug(true);



        group = new FrameBufferGroup();
        addActor(group);
        group.addActor(scrollPane);

        bufferTexture = group.getBufferTexture(1);
        temp = new Image(bufferTexture){
            private ShaderProgram program = new ShaderProgram(
                    Gdx.files.internal("shaderjb/grayScale.vert"),
                    Gdx.files.internal("shaderjb/grayScale.glsl")
            );
            @Override
            public void draw(Batch batch, float parentAlpha) {


                super.draw(batch, parentAlpha);

            }
        };
        temp.setDebug(true);
        addActor(temp);
        temp.setTouchable(Touchable.disabled);

        System.out.println(temp.getWidth()+"   "+temp.getHeight());
    }

    @Override
    public void render() {
//        bufferTexture = group.getBufferTexture(1);


        super.render();
        if (group!=null) {
            bufferTexture = group.getBufferTexture(1);
            ImageUtils.changeImageAtlas(temp,bufferTexture);
        }

    }
}
