package com.libGdx.test.sc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.utils.ImageUtil;
import com.kw.gdx.utils.ImageUtils;

/**
 * 单条列表 item。
 *
 * 注意：
 * 这个 Actor 不会创建 500 个。
 * 它只会创建屏幕可见数量 + 少量缓冲数量。
 */
public class ItemActor extends Group {
    private Image bgImg;
    private Label userLabel;

    public ItemActor() {
        bgImg = new Image(Asset.getAsset().getTexture("assets/img_1.png"));
        addActor(bgImg);
        bgImg.setSize(500,500);
        setSize(bgImg.getWidth(),bgImg.getHeight());
        setTouchable(Touchable.enabled);

        userLabel = new Label("",new Label.LabelStyle(){
            {
                font = new BitmapFont();
            }
        });
        userLabel.setFontScale(4);
        addActor(userLabel);
    }

    public void setData(String text) {
        userLabel.setText(text);
        userLabel.setPosition(getWidth()/2f,getHeight()/2f, Align.left);
        ImageUtils.changeImageTexture(bgImg,Asset.getAsset().getTexture("assets/v1/"+text));
        bgImg.setSize(500,500);
        bgImg.setPosition(getWidth()/2f, bgImg.getHeight()/2f,Align.center);

    }
}
