package kw.artpuzzle.group;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kw.gdx.asset.Asset;

import kw.artpuzzle.shadermanager.ShaderFactory;
import kw.artpuzzle.shadermanager.ShaderManager;
import kw.artpuzzle.shadermanager.ShaderType;

public class ShaderImage2 extends Image {
   private ShaderProgram shadowProgram;
   private float time = 0;
   private Texture texture;
   private boolean startAnimation = false;
   private ShaderType shaderType = ShaderType.MiddleSpread;

   public ShaderImage2(SpriteDrawable spriteDrawable){
      super(spriteDrawable);
      int i = MathUtils.randomRangle(2);
      ShaderType black2 = ShaderType.black1;
      if (i == 0) {
         black2 = ShaderType.black2;
      }


      shadowProgram = ShaderManager.getManager()
              .getType(black2);//MiddleSpreadShow
      texture = Asset.getAsset().getTexture("noise.png");
   }

   @Override
   public void act(float delta) {
      super.act(delta);
      time+=delta ;
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      if (shadowProgram!=null && startAnimation) {
         batch.setShader(shadowProgram);
         int u_t = shadowProgram.getUniformLocation("u_texture2");
         Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
         texture.bind();
         shadowProgram.setUniformi(u_t,1);

         int timeLocation = shadowProgram.getUniformLocation("time");
         int su = shadowProgram.getUniformLocation("u");
         int su2 = shadowProgram.getUniformLocation("u2");
         int sv = shadowProgram.getUniformLocation("v");
         int sv2 = shadowProgram.getUniformLocation("v2");
         shadowProgram.setUniformf(timeLocation,time*0.8F);
         Drawable drawable = getDrawable();
         if(drawable instanceof TextureRegionDrawable){
            TextureRegion region = ((TextureRegionDrawable) drawable).getRegion();
            float u = region.getU();
            float v = region.getV();
            float u2 = region.getU2();
            float v2 = region.getV2();
            shadowProgram.setUniformf(su,u);
            shadowProgram.setUniformf(su2,u2);
            shadowProgram.setUniformf(sv,v);
            shadowProgram.setUniformf(sv2,v2);
         }else if (drawable instanceof SpriteDrawable){
            Sprite sprite = ((SpriteDrawable) drawable).getSprite();
            shadowProgram.setUniformf(su,sprite.getU());
            shadowProgram.setUniformf(su2,sprite.getU2());
            shadowProgram.setUniformf(sv,sprite.getV());
            shadowProgram.setUniformf(sv2,sprite.getV2());
         }
         Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
         super.draw(batch, parentAlpha);
         batch.setShader(null);
      }else {
         super.draw(batch, parentAlpha);
      }
   }

   public void setAnimation() {
      startAnimation = true;
      time = 0;
   }

   public void setShaderType(ShaderType type) {
      if (type != shaderType){
         shaderType = type;
         shadowProgram = ShaderManager.getManager().getType(type);
      }
   }

   public void setShaderByUser(int index){
      ShaderType black2 = ShaderType.black1;
      if (index == 1){
         black2 = ShaderType.black1;
      }
      shadowProgram = ShaderManager.getManager().getType(black2);

   }
}
