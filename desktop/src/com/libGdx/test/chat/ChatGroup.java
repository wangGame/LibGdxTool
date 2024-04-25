package com.libGdx.test.chat;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.csvanddata.demo.CsvUtils;

public class ChatGroup extends Group {
    private Array<ChatData> common;
    public ChatGroup(){
        common = CsvUtils.common("../successP.csv", ChatData.class);
        setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
    }
    public ChatGroup(Array<ChatData> cc){
        common = cc;
        setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
    }

    public void showView(){
        float v = Constant.GAMEWIDTH / common.size;
        for (ChatData chatData : common) {
            Image image = new Image(
                    new NinePatch(
                            Asset.getAsset().getTexture("www.png"),
                            2,2,2,2
                    )
            );
            image.setSize(v,(float) chatData.getValue()*Constant.GAMEHIGHT);
            addActor(image);
            image.setX(v*chatData.getId());
        }
    }
}
