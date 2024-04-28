package kw.dgx.aabb;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

public class BroadImage extends Group {
    private Rectangle rectangle;
    public BroadImage(){
        rectangle = new Rectangle();
        Image image = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        addActor(image);
        rectangle.setWidth(image.getWidth());
        rectangle.setHeight(image.getHeight());
    }

    public void overlap(Circle shape2D){
        Intersector.overlaps(shape2D,rectangle);
    }

}
