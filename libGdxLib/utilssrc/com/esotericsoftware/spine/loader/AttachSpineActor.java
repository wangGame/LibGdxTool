package com.esotericsoftware.spine.loader;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.esotericsoftware.spine.attachments.Attachment;
import com.esotericsoftware.spine.attachments.MeshAttachment;
import com.esotericsoftware.spine.attachments.RegionAttachment;

public class AttachSpineActor extends SpineActor {

   public AttachSpineActor(String path){
      super(path);
   }

   public AttachSpineActor(String path,String atlas){
      super(path,atlas);
   }

   public void setAttachment(String name,String resou,TextureRegion region){
      Attachment attachment = skeleton.getAttachment(name, resou);
      if(attachment instanceof RegionAttachment){
         ((RegionAttachment) attachment).setRegion(region);
      }else if (attachment instanceof MeshAttachment){
         ((MeshAttachment)attachment).setRegion(region);
      }
   }
}
