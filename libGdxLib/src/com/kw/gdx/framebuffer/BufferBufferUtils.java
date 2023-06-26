package com.kw.gdx.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;

public class BufferBufferUtils extends Group {
   private FrameBuffer buffer = Asset.getAsset().buffer();
   private int type = 1; //0 一直更新    1只更新一次
   private boolean isBatched;
   private Actor actor;

   public BufferBufferUtils(Actor group){
      buffer = Asset.getAsset().buffer();
      this.actor = group;
      addActor(group);
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
//      if (type == 1&&isBatched){
//         return;
//      }
      isBatched = true;
      batch.flush();

      buffer.begin();
      Gdx.gl.glClearColor(245.0f/255.0f,238.0f/255.0f,215.0f/255.0f,0);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
      super.draw(batch, parentAlpha);
      batch.flush();
      buffer.end();
   }

   public FrameBuffer getBuffer() {
      return buffer;
   }

   public TextureRegion getBufferTexture(float globalScale){
      Texture colorBufferTexture = buffer.getColorBufferTexture();
      TextureRegion region = new TextureRegion(colorBufferTexture);
      region.setRegion(0,0,(int)(actor.getWidth()),(int)(actor.getHeight()));
      region.flip(false,true);
      return region;
   }
}
