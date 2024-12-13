package com.libGdx.test.generator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.libGdx.test.base.LibGdxTestMain;

public class GeneratorTest extends LibGdxTestMain {
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    public static void main(String[] args) {
        GeneratorTest generatorTest = new GeneratorTest();
        generatorTest.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        generator = new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/game.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.borderColor = Color.RED;
        BitmapFont bitmapFont = generator.generateFont(parameter);
        Label label = new Label("123",new Label.LabelStyle(){
            {
                font = bitmapFont;
            }
        });
        addActor(label);
        label.setPosition(100,100);
    }
}
