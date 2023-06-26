package kw.artpuzzle.listener;


import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.kw.gdx.listener.MClickListener;
import com.kw.gdx.utils.basier.BseInterpolation;

public class LevelItemClickListener extends MClickListener {
    private boolean ownTarget;
    protected Actor target;
    private float scaleOriginX, scaleOriginY;
    private boolean release = true;
    boolean effect = true;
    private Action effectAction;
    private boolean working = false;
    private float releaseTimeScale;
    protected Action old;
    protected float timeScale = 1.0f;

    public void setTimeScale(float timeScale) {
        this.timeScale = timeScale;
    }

    public LevelItemClickListener(Actor target) {
        this.target = target;
        ownTarget = true;
    }

    public void setTarget(Actor target) {
        this.target = target;
        ownTarget = true;
    }

    public LevelItemClickListener() {
        ownTarget = false;
    }
    float targetScale =0.93F;
    public LevelItemClickListener(float targetScale){
        this.targetScale = targetScale;
    }

    public LevelItemClickListener(boolean effect){
        this.effect = effect;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if(working) return false;
        if(pointer>0) return false;
        boolean flag = super.touchDown(event, x, y, pointer, button);
        touchDownEffect();
        if (flag && effect) {
            if (!ownTarget) {
                target = event.getListenerActor();
            }
            if (target != null) {
                working = true;
                release = false;
                scaleOriginX = target.getScaleX();
                scaleOriginY = target.getScaleY();
                effectAction = Actions.scaleTo(scaleOriginX * targetScale, scaleOriginY * targetScale,
                        timeScale * 0.1F, new BseInterpolation(0.25F,0.0F,0.75F,1.0F));
                target.addAction(effectAction);
            }
        }
        return flag;
    }

    public void touchDownEffect(){

    }

    public void clickEffect(){

    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        if(effect && target!=null){
            releaseTimeScale = Math.abs((scaleOriginX - target.getScaleX())/(scaleOriginX*0.1F));
            target.addAction(Actions.sequence(Actions.delay(timeScale*0.1F * releaseTimeScale),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            clickEffect();
                        }
                    })));
        }else {
            clickEffect();
        }
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        if(!release) {
            release();
        }
    }

    @Override
    public void invalidateTapSquare() {
        super.invalidateTapSquare();
        if(!release) {
            release();
        }
    }

    public void setEffect(boolean effect) {
        this.effect = effect;
    }

    protected void release() {
        if(!effect){
            working = false;
            return;
        }
        release = true;
        releaseEffect();
        if (target != null) {
            target.removeAction(effectAction);
            releaseTimeScale = Math.abs((scaleOriginX - target.getScaleX())/(scaleOriginX*0.1F));
            target.addAction(Actions.sequence(Actions.scaleTo(scaleOriginX, scaleOriginY,
                    timeScale*0.1F * releaseTimeScale, Interpolation.slowFast), Actions.run(new Runnable() {
                @Override
                public void run() {
                    working = false;
                }
            })));
        }else{
            working = false;
        }
        if (!ownTarget) {
            target = null;
        }
    }

    protected void releaseEffect() {

    }
}
