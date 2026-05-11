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
import com.badlogic.gdx.utils.Align;
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
        Image image = new Image(Asset.getAsset().getTexture("white.png"));
        image.setSize(10000,10000);
        addActor(image);

        ScrollPane scrollPane = new ScrollPane(new Table() {{
            for (int i = 0; i < 100; i++) {
                Image image = new Image(Asset.getAsset().getTexture("000.png"));
                add(image);

                row();
            }
            pack();
        }});

        scrollPane.setSize(Constant.GAMEWIDTH - 400, Constant.GAMEHIGHT - 800);
        scrollPane.setOrigin(Align.center);
        group = new FrameBufferGroup(scrollPane);
        addActor(group);
        scrollPane.setOrigin(Align.center);
        scrollPane.setScale(0.4f);
        bufferTexture = group.getBufferTexture(1);
        temp = new Image(bufferTexture){
            private ShaderProgram program = new ShaderProgram(
                    Gdx.files.internal("shaderjb/grayScale.vert"),
                    Gdx.files.internal("shaderjb/grayScale.glsl")
                    );
            @Override
            public void draw(Batch batch, float parentAlpha) {

                batch.setShader(program);
//
                float v = 60.f / getHeight();
                program.setUniformf("u_bottomFade", v);
                program.setUniformf("u_topFade", v);
                program.setUniformf("top",bufferTexture.getV());
                super.draw(batch, parentAlpha);
                batch.setShader(null);
            }

            @Override
            protected void positionChanged() {
                super.positionChanged();
                group.setNeedUpdate(true);
            }
        };
        group.setDrawContent(temp);
        temp.setDebug(true);
        temp.setPosition(Constant.GAMEWIDTH/2f,Constant.GAMEHIGHT/2f, Align.center);

        // 关键：否则 temp 会挡住 ScrollPane 的触摸
        temp.setTouchable(Touchable.disabled);

        addActor(temp);

        System.out.println(temp.getWidth() + "   " + temp.getHeight());
    }
}
