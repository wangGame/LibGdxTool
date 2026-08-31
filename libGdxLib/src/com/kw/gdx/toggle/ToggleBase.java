package com.kw.gdx.toggle;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ToggleBase extends Group {
    private ToggleContainer toggleContainer;
    protected boolean isSelected;
    public ToggleBase() {
        super();
        addListener();
    }

    public void addListener(){
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (toggleContainer.isMultSelect()){
                    if (isSelected){
                        unSelect();
                    }else {
                        select();
                    }
                }else {
                    select();
                }
            }
        });
    }

    public void setToggleContainer(ToggleContainer toggleContainer) {
        this.toggleContainer = toggleContainer;
    }

    public void select(){
        this.isSelected = true;
        if (!toggleContainer.isMultSelect()){
            toggleContainer.unSelectedOld(this);
        }
    }

    public void unSelect(){
        this.isSelected = false;
    }
}
