package com.kw.gdx.spine;


import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ArrayMap;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Slot;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.utils.Layer;

public class SpineReadAttribute {
    private SpineActor spineActor;
    private Actor targetActor;
    private boolean isAnimationing;
    private ArrayMap<Actor, Bone> scaleMap;
    private ArrayMap<Actor, Slot> colorMap;
    private ArrayMap<Actor, TransBean> transMap;
    private boolean onceTimes = true;
    private boolean stopController;

    public void setStopController(boolean stopController) {
        this.stopController = stopController;
    }

    public boolean isStopController() {
        return stopController;
    }

    public SpineReadAttribute(Actor actor, SpineActor spineActor){
        this.spineActor = spineActor;
        this.targetActor = actor;
    }

    public void setOnceTimes(boolean onceTimes) {
        this.onceTimes = onceTimes;
    }


    public void startAnimation(String animationName, boolean isLoop){
        startAnimation(animationName,isLoop,true);
    }

    public void startAnimation(String animationName, boolean isLoop,boolean delete){
        if (spineActor==null || targetActor == null)return;
        isAnimationing = true;
        spineActor.setAnimation(animationName,isLoop);
        spineActor.getAnimaState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void complete(AnimationState.TrackEntry entry) {
                super.complete(entry);
                isAnimationing = false;
            }
        });
        targetActor.addAction(new Action() {
            @Override
            public boolean act(float delta) {
                if (stopController)return true;
                if (scaleMap!=null){
                    if (scaleMap.size>0) {
                        for (int i = 0; i < scaleMap.size; i++) {
                            Actor actorTemp = scaleMap.getKeyAt(i);
                            Bone bone = scaleMap.get(actorTemp);
                            float scaleX = bone.getScaleX();
                            float scaleY = bone.getScaleY();
                            if (actorTemp instanceof Label){
                                Label label  = ((Label)(actorTemp));
                                ((Label) actorTemp).setFontScale(scaleX,scaleY);
                            }else {
                                actorTemp.setScale(scaleX,scaleY);
                            }
                        }
                    }
                }

                if (colorMap!=null){
                    if (colorMap.size>0) {
                        for (int i = 0; i < colorMap.size; i++) {
                            Actor actorTemp = colorMap.getKeyAt(i);
                            Slot slot = colorMap.get(actorTemp);
                            actorTemp.setColor(slot.getColor());
                        }
                    }
                }

                if (transMap!=null){
                    if (transMap.size>0) {
                        for (int i = 0; i < transMap.size; i++) {
                            Actor actorTemp = transMap.getKeyAt(i);
                            TransBean transBean = transMap.get(actorTemp);
                            Bone bone = transBean.getBone();
                            float xx = bone.getX();
                            float yy = bone.getY();
                            actorTemp.setPosition(transBean.getBaseX()+xx - transBean.getWorldOffX(),transBean.getBaseY() + yy- transBean.getWorldOffY(), Align.center);
                        }
                    }
                }

                if (!isLoop) {
                    if (delete) {
                        if (!isAnimationing) {
                            if (onceTimes) {
                                spineActor.remove();
                            }
                        }
                    }
                    return !isAnimationing;
                }else {
                    return false;
                }
            }
        });
    }

    public void setScale(String name){
        setScale(name,targetActor);
    }

    public void setScale(String name,Actor actor){
        if (actor == null)return;
        Bone bone = spineActor.getSkeleton().findBone(name);
        if (scaleMap == null) {
            scaleMap = new ArrayMap<>();
        }
        scaleMap.put(actor,bone);
    }


    public void setColor(String name) {
        setColor(name,targetActor);
    }

    public void setColor(String name,Actor actor){
        if (actor == null)return;
        Slot slot = spineActor.getSkeleton().findSlot(name);
        if (colorMap == null) {
            colorMap = new ArrayMap<>();
        }
        colorMap.put(actor,slot);
    }

    public void setTrans(float baseX,float baseY,String name) {
        setTrans(baseX,baseY,name,targetActor);
    }

    public void setTrans(float baseX,float baseY,String name,Actor actor){
        if (actor == null)return;

        Bone bone = spineActor.getSkeleton().findBone(name);
        if (transMap == null) {
            transMap = new ArrayMap<>();
        }
        TransBean transBean = new TransBean();
        transBean.setBaseX(baseX);
        transBean.setBaseY(baseY);
        transBean.setWorldOffX(0);
        transBean.setWorldOffY(-771f);
        transBean.setBone(bone);
        transMap.put(actor,transBean);
    }

    public void setTrans(float baseX,float baseY,String name,Actor actor,float wordx,float worldy){
        if (actor == null)return;

        Bone bone = spineActor.getSkeleton().findBone(name);
        if (transMap == null) {
            transMap = new ArrayMap<>();
        }
        TransBean transBean = new TransBean();
        transBean.setBaseX(baseX);
        transBean.setBaseY(baseY);
        transBean.setWorldOffX(wordx);
        transBean.setWorldOffY(worldy);
        transBean.setBone(bone);
        transMap.put(actor,transBean);
    }



    class TransBean{
        private float worldOffX;
        private float worldOffY;
        private float baseX;
        private float baseY;

        private Bone bone;

        public Bone getBone() {
            return bone;
        }

        public float getBaseX() {
            return baseX;
        }

        public void setBaseX(float baseX) {
            this.baseX = baseX;
        }

        public float getBaseY() {
            return baseY;
        }

        public void setBaseY(float baseY) {
            this.baseY = baseY;
        }

        public void setBone(Bone bone) {
            this.bone = bone;
        }

        public float getWorldOffX() {
            return worldOffX;
        }

        public void setWorldOffX(float worldOffX) {
            this.worldOffX = worldOffX;
        }

        public float getWorldOffY() {
            return worldOffY;
        }

        public void setWorldOffY(float worldOffY) {
            this.worldOffY = worldOffY;
        }
    }

}