package kw.artpuzzle.group;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kw.gdx.asset.Asset;

public class GrayImage3 extends Group {
   public GrayImage3(TextureRegion region){
      GrayImage2 grayImage2 = new GrayImage2(region);
      addActor(grayImage2);
      Image image = new Image(Asset.getAsset().getTexture("common/mask.png"));
      addActor(image);
      image.setSize(grayImage2.getWidth(),grayImage2.getHeight());
      setSize(grayImage2.getWidth(),grayImage2.getHeight());
   }
}
