package com.kw.gdx.toggle;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Array;

public class ToggleContainer extends Table {
    private Array<ToggleBase> toggleButtons = new Array<>();
    private float paddTop;
    private float paddBottom;
    private float paddLeft;
    private float paddRight;
    private int column;
    private boolean multSelect;
    private ToggleBase selectedToggle;
    private float startEmpty;
    private float endEmpty;

    private final Value padTopValue = new Value() {
        @Override
        public float get(Actor context) {
            return paddTop;
        }
    };

    private final Value padBottomValue = new Value() {
        @Override
        public float get(Actor context) {
            return paddBottom;
        }
    };

    private final Value padLeftValue = new Value() {
        @Override
        public float get(Actor context) {
            return paddLeft;
        }
    };

    private final Value padRightValue = new Value() {
        @Override
        public float get(Actor context) {
            return paddRight;
        }
    };

    public ToggleContainer(){
        this(1);
    }

    public ToggleContainer(int column) {
        super();
        this.column = column;
        defaults().pad(
                padTopValue,
                padLeftValue,
                padBottomValue,
                padRightValue
        );
    }

    public void addToggleButton(ToggleBase toggleButton) {
        toggleButton.setToggleContainer(this);
        toggleButtons.add(toggleButton);
        add(toggleButton);
        int size = toggleButtons.size;
        if(column <= 0){
            return;
        }
        if (size % column == 0) {
            row();
        }
    }

    public void removeToggleButton(ToggleBase toggleButton) {
        toggleButtons.removeValue(toggleButton, true);
    }

    public void setMultSelect(boolean multSelect) {
        this.multSelect = multSelect;
    }

    public boolean isMultSelect() {
        return multSelect;
    }

    public void setPaddTop(float paddTop) {
        this.paddTop = paddTop;
        invalidateHierarchy();
    }

    public void setPaddBottom(float paddBottom) {
        this.paddBottom = paddBottom;
        invalidateHierarchy();
    }

    public void setPaddLeft(float paddLeft) {
        this.paddLeft = paddLeft;
        invalidateHierarchy();
    }

    public void setPaddRight(float paddRight) {
        this.paddRight = paddRight;
        invalidateHierarchy();
    }

    public float getPaddRight() {
        return paddRight;
    }

    public void unSelectedOld(ToggleBase toggleBase) {
        if (this.selectedToggle != null){
            selectedToggle.unSelect();
        }
        this.selectedToggle = toggleBase;
    }

    public Array<ToggleBase> getAllSelectToggle() {
        Array<ToggleBase> selectedToggles = new Array<>();
        for (ToggleBase r : toggleButtons) {
            if (r.isSelected) {
                selectedToggles.add(r);
            }
        }
        return selectedToggles;
    }

    public void TopEmpty(int emptyHeight, boolean isV) {
        this.startEmpty = emptyHeight;
        ToggleBase empty = new ToggleBase() {
            {
                setSize(10, emptyHeight);
            }
        };
        empty.setToggleContainer(this);
        toggleButtons.add(empty);
        add(empty);
        empty.setDebug(true);
        if (isV){
            row();
        }
    }

    public void bottomEmpty(int emptyHeight, boolean isV) {
        this.endEmpty = emptyHeight;
        ToggleBase empty = new ToggleBase() {
            {
                setSize(10, emptyHeight);
            }
        };
        empty.setToggleContainer(this);
        toggleButtons.add(empty);
        add(empty);
        empty.setDebug(true);
        if (isV){
            row();
        }
    }

    public void scrollPointX(ScrollPane scrollPane,float scrollX){
        scrollPane.validate();
        scrollPane.setScrollX(scrollX);
        scrollPane.updateVisualScroll();
    }

    public float caluHeight(int position){
        float height = 0;
        if (toggleButtons.size>0) {
            ToggleBase toggleBase = toggleButtons.get(1);
            height = toggleBase.getWidth() * position + startEmpty + paddLeft * (position+1) + paddRight * (position+1);
        }
        System.out.println(height);
        return height;
    }

    public void scrollPointY(ScrollPane scrollPane){
        scrollPane.validate();
        scrollPane.setScrollY(100);
        scrollPane.updateVisualScroll();
    }
}
