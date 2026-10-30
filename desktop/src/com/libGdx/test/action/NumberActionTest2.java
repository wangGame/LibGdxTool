package com.libGdx.test.action;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.action.NumAction;
import com.kw.property.FloatProperty;
import com.libGdx.test.net.LibGdxTest;

public class NumberActionTest2 extends LibGdxTest {
    public static void main(String[] args) {
        NumberActionTest2 numberActionTest2 = new NumberActionTest2();
        numberActionTest2.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        FloatProperty<NumberValue> floatProperty = new FloatProperty<NumberValue>("") {
            @Override
            public void setValue(NumberValue object, float value) {
                object.setV((int) value);
            }

            @Override
            public Float get(NumberValue object) {
                return (float) object.getV();
            }
        };
    }
}
