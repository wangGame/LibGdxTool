package com.libGdx.test.spine;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.BoneData;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.Skin;
import com.esotericsoftware.spine.SlotData;
import com.esotericsoftware.spine.attachments.ActorAttachment;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class ActorSpine extends LibGdxTestMain {
    public static void main(String[] args) {
        ActorSpine test = new ActorSpine();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        {


            SpineActor coin = new SpineActor("assets/actorspine/coin");
            addActor(coin);

            coin.setPosition(100,400);
            for (Animation animation : coin.getsData().getAnimations()) {
                System.out.println(animation.getName());
            }


            coin.addAction(
                    Actions.sequence(
                            Actions.delay(0.3f),
                            Actions.moveToAligned(700,400,Align.center,0.2f)
                    )
            );
//            Image img = new Image(Asset.getAsset().getTexture("assets/7.png")){
//                @Override
//                public void draw(Batch batch, float parentAlpha) {
//                    super.draw(batch, parentAlpha);
//                    System.out.println(getY());
//                }
//            };
//

//            SkeletonData data = coin.getSkeleton().getData();
//            Skin defaultSkin = data.getDefaultSkin();

//            Array<SlotData> slots = data.getSlots();



//            for (Skin.SkinEntry attachment : defaultSkin.getAttachments()) {
//                System.out.println(attachment.getName());
//                if (attachment.getName().equals("ic_coin")) {
//                    ActorAttachment actorAttachment = new ActorAttachment("img");
//                    actorAttachment.setActor(img);
//                    actorAttachment.setFlower(true);
//                    attachment.setAttachment(actorAttachment);
//                }
//            }
            coin.setAnimation("obtain",true);
//

//
//            SpineActor actor = new SpineActor("assets/actorspine/quan_tb");
//            actor.setAnimation("zhuanpan_ck", true);
//            addActor(actor);
//            actor.setPosition(450, 400);
//
//            Group group = new Group();
//
//            Image img = new Image(Asset.getAsset().getTexture("assets/7.png"));
//            group.addActor(img);
//            img.setPosition(100, 100, Align.center);
//
//
//            Image image = new Image(Asset.getAsset().getTexture("assets/ad_progress.png"));
//            group.addActor(image);
//            image.setPosition(100, 100, Align.center);
//
//            ActorAttachment actorAttachment = new ActorAttachment("img");
//            actorAttachment.setActor(group);
//
//            SkeletonData data = actor.getSkeleton().getData();
//            Skin defaultSkin = data.getDefaultSkin();
//            for (Skin.SkinEntry attachment : defaultSkin.getAttachments()) {
//                if (attachment.getName().equals("xuanq2_00")) {
//                    attachment.setAttachment(actorAttachment);
//                }
//            }
        }
    }
}
