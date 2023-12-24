package com.libGdx.test.dyn;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.constant.Constant;

import java.util.Comparator;

/**
 * @Auther jian xian si qi
 * @Date 2023/8/4 12:24
 */
public class DaynScrollPanel extends Group {
    private ScrollPane pane;
    private Table table;
    private int index;

    private int max = 200;
    private int min = 199;
    public DaynScrollPanel(){
        table = new Table();
        pane = new ScrollPane(table,new ScrollPane.ScrollPaneStyle()){
            @Override
            public void act(float delta) {
                super.act(delta);
                pane.getScrollPercentY();

                if (pane.getScrollPercentY()<0.1){
                    float v = addGroup(true);
                    pane.validate();
                    float scrollY = pane.getScrollY();
                    pane.setScrollY(scrollY + v);
                    pane.updateVisualScroll();
                    pane.validate();
                }

                if (pane.getScrollPercentY()>0.9f){
                    addGroup(false);
                }
//                if (pane.getScrollPercentY() <0.25f){
//                    float scrollY = pane.getScrollY();
//                    float height = table.getHeight();
//                    float v = height - scrollY;
//                    addGroup();
//                    pane.setScrollY(table.getHeight() - v);
//                    System.out.println(table.getHeight() - v);
//                }
            }
        };
        pane.setScrollY(100);
        addActor(pane);

        addGroup(false);

        addGroup(false);

        addGroup(false);
        pane.setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT/2);
    }

    public float addGroup(boolean v){
        for (int i = 0; i < 4; i++) {
            if (!v) {
                Group1 image = new Group1(max++);
                table.add(image);
                table.row();
            }else {
                Group1 image = new Group1(min--);
                table.add(image);
                table.row();
            }
        }
        if (v) {
            Array<Cell> cells = table.getCells();
            cells.sort(new Comparator<Cell>() {
                @Override
                public int compare(Cell cell, Cell t1) {
                    Actor actor = cell.getActor();
                    Actor actor1 = t1.getActor();
                    if (actor instanceof Group1 && actor1 instanceof Group1) {
                        Group1 actor2 = (Group1) (actor);
                        Group1 actor3 = (Group1) (actor1);
                        return actor2.getIndex() - actor3.getIndex();
                    }
                    return 0;
                }
            });
            table.pack();
        }
        Group1 group1 = new Group1(-1);
        float v1 = 4 * group1.getHeight();
        return v1;

    }
}
