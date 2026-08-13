package com.libGdx.test.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.base.LibGdxTestMain;

public class FrameBufferDemo extends LibGdxTestMain {

    TextureRegion bufferTexture;
    FrameBufferGroup group;
    private Image temp;

    public static void main(String[] args) {
        FrameBufferDemo frameBufferDemo = new FrameBufferDemo();
        frameBufferDemo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        Image bg = new Image(Asset.getAsset().getTexture("white.png"));
        bg.setSize(10000, 10000);
        addActor(bg);

        Table table = new Table();
        for (int i = 0; i < 100; i++) {
            Image image = new Image(Asset.getAsset().getTexture("wood.png"));
            table.add(image);
            table.row();
        }
        table.pack();

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setSize(
                Constant.GAMEWIDTH,
                Constant.GAMEHIGHT - 200
        );

        scrollPane.setOrigin(Align.center);

        /**
         * 不要缩放 ScrollPane。
         * FBO 里面用原尺寸截图。
         */
        // scrollPane.setScale(0.4f);

        group = new FrameBufferGroup(scrollPane);
        addActor(group);
        group.setPosition(100,200);

        bufferTexture = group.getBufferTexture(1);

        temp = new Image(bufferTexture) {

            private ShaderProgram program = new ShaderProgram(
                    Gdx.files.internal("shaderjb/grayScale.vert"),
                    Gdx.files.internal("shaderjb/grayScale.glsl")
            );

            @Override
            public void draw(Batch batch, float parentAlpha) {
//                batch.setShader(program);
//
//                float v = 220f / getHeight();
//                program.setUniformf("u_bottomFade", v);
//                program.setUniformf("u_topFade", v);
//                program.setUniformf("top", bufferTexture.getV());
//                program.setUniformf("u_curve", 0.5f);

                super.draw(batch, parentAlpha);

//                batch.setShader(null);
            }

            @Override
            protected void positionChanged() {
                super.positionChanged();
                if (group != null) {
                    group.setNeedUpdate(true);
                }
            }
        };
        temp.setDebug(true);
        /**
         * 这里控制最终显示大小。
         * 比如显示为原 ScrollPane 的 0.4 倍。
         */
        float showScale = 01f;
        temp.setSize(
                scrollPane.getWidth() * showScale,
                scrollPane.getHeight() * showScale
        );

        temp.setPosition(
                Constant.GAMEWIDTH / 2f,
                Constant.GAMEHIGHT / 2f,
                Align.center
        );

        temp.setTouchable(Touchable.disabled);

        group.setDrawContent(temp);

        addActor(temp);

        System.out.println(temp.getWidth() + "   " + temp.getHeight());
    }
}