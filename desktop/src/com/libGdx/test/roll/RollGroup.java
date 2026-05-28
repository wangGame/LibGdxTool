package com.libGdx.test.roll;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;

public class RollGroup extends Group {
    float time = 0;
    public RollGroup(){
        setSize(500,500);
        setDebug(true);
        for (int i = 0; i < 10; i++) {
            Image image = new Image(Asset.getAsset().getTexture("assets/7.png")){
                @Override
                public void act(float delta) {
                    super.act(delta);
                    setX(getX()-Math.abs(time));
                    if (getX()<=-500) {
                        setX(getX()+10*192);
                    }
                }
            };
            image.setColor((float) Math.random(),(float)Math.random(),(float)Math.random(),1.0f);
            addActor(image);
            image.setX(i*image.getWidth());
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        time += delta * fushu * 10;
        if (time<0.03f){
            fushu = 1;
        }else if (time>100){
            fushu = -1;
        }
    }
    private int fushu = -1;
}
