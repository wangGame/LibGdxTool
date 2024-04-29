package kw.dgx.npath;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

/**
 * 可以直接切，然后拼起来
 */
public class NPath extends Group {
    private TextureRegion region;
    private TextureRegion[] textureRegions;
    private Image[] x;

    public NPath(){
        textureRegions = new TextureRegion[2];
        this.region = new TextureRegion(Asset.getAsset().getTexture("assets/0_1_41_512.jpg"));
        textureRegions[0] = new TextureRegion(region,0,0,100,300);
        textureRegions[1] = new TextureRegion(region,100,0,100,300);
        x = new Image[2];
        x[0] = new Image(textureRegions[0]);
        x[1] = new Image(textureRegions[1]);
        x[0].setWidth(500);
        addActor(x[0]);
        addActor(x[1]);
        x[1].setX(520);


        Image image = new Image(new NinePatch());
    }

}
