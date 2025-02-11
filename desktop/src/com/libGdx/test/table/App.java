package com.libGdx.test.table;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class App{
    public static void main(String[] args) {
        new Table(){{
            add().colspan(2);
        }};
    }
}
