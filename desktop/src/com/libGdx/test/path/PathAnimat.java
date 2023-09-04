package com.libGdx.test.path;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.utils.basier.BseInterpolation;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/8/30 19:02
 */
public class PathAnimat extends LibGdxTestMain {
    public static void main(String[] args) {
        PathAnimat animation = new PathAnimat();
        animation.start(animation);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        double arr[][] ={
//                35.8, -58.85, 19.66, -60.46, 2.82, -62.13, -38.42, -59.86, -50.4, -40.21, -61.95,
//                -21.26, -67.48, 24.16, -29.49, 44.39, -6.21, 56.78, 48.21, 44.12, 44.59, -0.95,
//                42.56, -26.22, 12.09, -48.33, -14.97, -35.68, -33.4, -27.07, -36.2, -10.49, -33.74,
//                5.5, -31.41, 20.68, -17.14, 31.15, -3.31, 33.2, 3.85, 34.27, 20.67, 29.67, 26.44, 18.46,
//                32.8, 6.14, 29.86, -16.29, 10.78, -24.21, -2.94, -29.91, -18.99, -22.57, -23.67,
//                -10.3, -27.73, 0.33, -23.82, 20.65, -4.18, 24.92, 2.48, 26.36, 19.41, 23.09, 22.12,
//                10.01, 23.38, 3.92, 23.44, -15.35, 0.55, -16.06, -3.8, -16.19, -16.97, -11.04, -15.79, 0.46, -14.28, 15.16,
//                -2.55, 18.31, 4.25, 15.99, 8.86, 14.42, 14.99, 0.58, 1.49, -0.6, -14.26, -1.97
                {

                        -213.5,
                        316.59,
                        -247.31,
                        335.05,
                        -270.26,
                        347.57,
                        -351.78,
                        401.45,
                        -350.42,
                        411.54,
                        -331.54,
                        551.65,
                        -260.37,
                        752.91,
                        67,
                        890.49,
                        242.71,
                        964.33
                },
                {

                        -270.45,
                        326.09,
                        -245.57,
                        355.49,
                        -228.32,
                        375.89,
                        -200.13,
                        418.18,
                        -189.29,
                        426.4,
                        -85.54,
                        505.05,
                        49.26,
                        671.3,
                        66.3,
                        894.28,
                        80.83,
                        1084.32
                },{
                -267.94,
                386.84,
                -245.57,
                355.49,
                -219.8,
                319.39,
                -187.74,
                277.32,
                -179.23,
                282.44,
                -67.65,
                349.52,
                49.26,
                671.3,
                66.3,
                894.28,
                80.83,
                1084.32
        },{
                -226.75,
                389.1,
                -245.57,
                355.49,
                -270.43,
                311.12,
                -297.13,
                247.41,
                -304.11,
                249.82,
                -429.2,
                292.98,
                -154.6,
                776.51,
                66.3,
                894.28,
                235.1,
                984.28
        },{
                -207.11,
                357.55,
                -245.57,
                355.49,
                -286.25,
                353.32,
                -342.65,
                349.75,
                -345.83,
                356.42,
                -407.62,
                486.09,
                -154.6,
                776.51,
                66.3,
                894.28,
                235.1,
                984.28
        },
                {
                        -207.11,
                        357.55,
                        -245.57,
                        355.49,
                        -286.25,
                        353.32,
                        -185.84,
                        343.45,
                        -184.94,
                        344.65,
                        -113.43,
                        440.32,
                        -3.11,
                        692.35,
                        66.3,
                        894.28,
                        128.49,
                        1075.19
                },{
                -226.88,
                321.78,
                -245.57,
                355.49,
                -260.33,
                382.12,
                -282.93,
                435.31,
                -282.86,
                436.81,
                -275.61,
                593.53,
                -78.08,
                801.59,
                66.3,
                894.28,
                227.28,
                997.62
        },
                {
                        -264.91,
                        394.62,
                        -252.59,
                        358.09,
                        -249.4,
                        348.62,
                        -230.72,
                        300.33,
                        -229.67,
                        301.4,
                        -128.92,
                        404.12,
                        -33.72,
                        778.7,
                        66.3,
                        894.28,
                        191.48,
                        1038.94
                }

        };
        for (double[] doubles : arr) {
            xxxxx(stage,doubles);
        }
    }

    private void xxxxx(Stage stage, double[] arr) {
        PathAnimation pathAnimation = new PathAnimation();
        pathAnimation.setPosData(arr,0,0.25f);
        pathAnimation.setInterpolation(new BseInterpolation(0,0.01f,0.0561f,1.0f));
        pathAnimation.setDuration(0.3667f);

        Image image = new Image(Asset.getAsset().getTexture("ball.png"));
        image.addAction(
                        pathAnimation
                );
        image.setOrigin(Align.center);
        image.setScale(0.2f);
        image.setPosition(620,420, Align.center);
        stage.addActor(image);
    }
}
