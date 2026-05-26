package com.libGdx.test.action;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.action.NewActions;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.besier.SpineCurveUtils;
import com.libGdx.test.base.LibGdxTestMain;

public class ActionDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        ActionDemo demo = new ActionDemo();
        demo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        FileHandle internal = Gdx.files.internal("assets/actorspine/coin.json");
        System.out.println(internal);
        String jsonText = Gdx.files.internal("assets/actorspine/coin.json").readString("UTF-8");
        Array<String> names = SpineTranslateActionParser.getAnimationNames(jsonText);
        Array<String> lines = SpineTranslateActionParser.getAnimationTrackSummary(jsonText, "obtain");
        for (String line : lines) {
            System.out.println(line);
        }
        Image image = new Image(Asset.getAsset().getTexture("assets/7.png"));
        addActor(image);
//
//        image.addAction(NewActions.moveXTo(100,2, Interpolation.bounce));
//        image.addAction(NewActions.moveYTo(500,2, Interpolation.sine));

        float[] floats = SpineCurveUtils.buildSpineBezier(
                1.233f,
                0,
                1.333f,
                0,
                1.2333f,
                -80.27f,
                1.33f,
                1332.32f
        );

        float baseX = 100;
        float baseY = 100;

        image.addAction(Actions.sequence(
                NewActions.newSpineCurveMoveAction(
                        baseX, baseY,
                        0f, 0f,
                        51.85f, 36.69f,
                        0f, 0f, 0.268f, 52.42f,
                        0f, 0f, 0.268f, 36.69f,
                        0.4f,
                        0f
                ),
                NewActions.newSpineCurveMoveAction(
                        baseX, baseY,
                        51.85f, 36.69f,
                        11.09f, 19.44f,
                        0.632f, 51.85f, 0.768f, 11.09f,
                        0.632f, 36.69f, 0.768f, 19.44f,
                        0.6f,
                        0.4f
                ),
                NewActions.newSpineCurveMoveAction(
                        baseX, baseY,
                        11.09f, 19.44f,
                        179.03f, 0f,
                        1.146f, 10.48f, 1.289f, 179.03f,
                        1.146f, 19.44f, 1.289f, 0f,
                        0.4333f,
                        1f
                )
        ));

//        for (float aFloat : floats) {
//            System.out.println(aFloat);
//        }
//
//
//        SpineCurveUtils.getSpineBezierValue(
//                floats,
//        )


    }
}
