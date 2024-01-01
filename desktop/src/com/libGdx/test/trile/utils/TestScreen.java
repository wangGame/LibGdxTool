/*
 * *****************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.libGdx.test.trile.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @author Crowni
 **/
public class TestScreen extends LibGdxTestMain {

    public static void main(String[] args) {
        TestScreen testScreen = new TestScreen();
        testScreen.start();
    }

    private ActorTailEffect ball;

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        ball = new ActorTailEffect(new Texture("assets/ball.png"));
        ball.setSize(450, 450);
        ball.setOrigin(Align.center);
        ball.setTailEffect(ActorTailEffect.TailEffect.ScaleUpEnd);
        ball.setBodyColor(Color.valueOf("c62369"));
        ball.setTailColor(Color.valueOf("dd80a8"));
        ball.setTailEffect(ActorTailEffect.TailEffect.ScaleDownEnd);
        ball.setTailEffect(ActorTailEffect.TailEffect.ScaleUpEnd);
        ball.setTailEffect(ActorTailEffect.TailEffect.ScaleDownHead);
        ball.setTailEffect(ActorTailEffect.TailEffect.ScaleUpHead);
        ball.setTailLength(180);
        stage.addActor(ball);
        stage.addListener(new ClickListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                ball.setPosition(x,y);
            }
        });
        ball.addAction(Actions.moveTo(100,1000,0.6f));
    }
}
