package com.libGdx.test.tietu;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.asset.Asset;

public class BodyImage extends CirGroup {
    private TieTuGroup image;
    private Array<Actor> cirArray;

    public BodyImage(){
        Texture texture = Asset.getAsset().getTexture("L_Shaped.png");
        image = new TieTuGroup();
        setSize(image.getWidth(),image.getHeight());
        image.setPosition(getWidth()/2.0f,getHeight()/2.0f, Align.center);
        addActor(image);
        cirArray = new Array<>();

        Image imagec = new Image(Asset.getAsset().getTexture("ad_progress.png"));
        addActor(imagec);
        imagec.setPosition(50,25,Align.center);
        setCir(imagec);
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        image.setSize(width,height);
        image.setOrigin(Align.center);
        setOrigin(Align.center);
    }

    public void setCir(Actor image) {
        cirArray.add(image);
    }

    @Override
    protected void drawCir() {
        super.drawCir();
        for (Actor image1 : cirArray) {
            sr.circle(image1.getX(Align.center),image1.getY(Align.center),23);
        }
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        image.setColor(color);
    }
}
