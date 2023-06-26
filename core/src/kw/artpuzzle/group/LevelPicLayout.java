package kw.artpuzzle.group;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.utils.ads.BannerView;
import com.kw.gdx.utils.basier.BseInterpolation;
import com.kw.gdx.utils.log.NLog;

import kw.artpuzzle.scroll.ScrollPane;

public class LevelPicLayout extends Group {
    private float startX;
    private SnapshotArray<Actor> tabeleSort ;

    public LevelPicLayout(){
        tabeleSort = new SnapshotArray<>();
    }

    public void animation(){
        startX = 0;
        SnapshotArray<Actor> children = getChildren();
        for (int i = 0; i < children.size; i++) {
            Actor actor = children.get(i);
            float v = startX;
            actor.setX(v);
//            actor.setY(124+80,Align.center);
            actor.setY(BannerView.pxToDp(50) + 55,Align.bottom);
            startX+=actor.getWidth();
        }
        setWidth(startX+100+120);
        startX = 0;
        int num = 6;
        if (tabeleSort.size<8){
            num = tabeleSort.size;
        }
        for (int i = 0; i < num; i++) {
            Actor actor = tabeleSort.get(i);
            float v = Constant.GAMEWIDTH+actor.getX();
            actor.setX(v);
            actor.addAction(
                    Actions.sequence(  Actions.delay(2.5f),
                        Actions.parallel(
                                Actions.sequence(
                                        Actions.delay(0.0667F*i),
                                        Actions.moveToAligned(startX-9.0f,actor.getY(Align.center),Align.left,
                                                0.533337f,new BseInterpolation(0,0,0.75f,1)),
                                        Actions.moveToAligned(startX,actor.getY(Align.center),Align.left,
                                                0.2f,new BseInterpolation(0.25f,0,1f,1))
                                ),
                                Actions.sequence(
                                        Actions.alpha(0),
                                        Actions.delay(0.0667F*i),
                                        Actions.fadeIn(0.4663f)
                                )
                        )));
            startX+=actor.getWidth();
        }
    }

    public float www = 0;

    public void peak(int startIndex){
        startX = 0;
        SnapshotArray<Actor> children = tabeleSort;
        for (int i = startIndex; i < children.size; i++) {
            Actor child = tabeleSort.get(i);
            child.setX(startX);
            startX += child.getWidth();
        }

//        setWidth(startX+200);
        www = startX +200;
    }

    public void peak(){
        peak(0);
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
    }

    private ScrollPane scrollPane;

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    private Action sizeToAction;
    public void upDataWidth(){
        float height = getHeight();
        sizeToAction = Actions.parallel(
                Actions.sequence(Actions.sizeTo(www, height,0.5F),Actions.run(()->{
                    LevelPicLayout.this.clearActions();
                })),
                Actions.forever(Actions.run(()->{
                    scrollPane.layout();
                    scrollPane.updateVisualScroll();
                })));
        addAction(sizeToAction);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public boolean removeActor(Actor actor, boolean unfocus) {
        boolean b = tabeleSort.removeValue(actor, unfocus);
        NLog.e("delete actor "+ b);
        return super.removeActor(actor, unfocus);
    }

    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
        tabeleSort.add(actor);
    }
}
