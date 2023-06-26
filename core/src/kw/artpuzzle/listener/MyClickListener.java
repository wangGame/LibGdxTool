package kw.artpuzzle.listener;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import kw.artpuzzle.group.ImageActor;

public class MyClickListener extends ClickListener {
    private ImageActor targetActor;
    public MyClickListener(ImageActor imageActor){
        this.targetActor = imageActor;
    }

    public ImageActor getTargetActor() {
        return targetActor;
    }
}
