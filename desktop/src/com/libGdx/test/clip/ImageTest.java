package com.libGdx.test.clip;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.action.NewActions;
import com.kw.gdx.action.NumAction;
import com.kw.gdx.action.NumActionListener;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.drawable.PageFlipDrawable;
import com.kw.gdx.drawable.ShearTextureRegionDrawable;
import com.kw.gdx.listener.OrdinaryButtonListener;
import com.libGdx.test.base.LibGdxTestMain;

import java.nio.channels.AcceptPendingException;

public class ImageTest extends LibGdxTestMain {
    public static void main(String[] args) {
        ImageTest test = new ImageTest();
        test.start();
    }

    private TextureRegion region1;
    private TextureRegion region2;
    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);

        Texture texture = Asset.getAsset().getTexture("assets/shuoming.png");

        PageFlipDrawable drawable = new PageFlipDrawable(texture);
        drawable.setFromRight(true);

        PageFlipImage image = new PageFlipImage(drawable);
        image.setSize(500, 500);
        image.setPosition(400, 200);
        image.setDuration(11.8f);

        stage.addActor(image);

        image.playFlip();
//        Texture texture = Asset.getAsset().getTexture("assets/shuoming.png");
//
//        PageFlipDrawable drawable = new PageFlipDrawable(texture);
//        drawable.setFromRight(true);
//        drawable.setStrips(32);
//        drawable.setCurlStrength(0.18f);
//        drawable.setVerticalBend(0.08f);
//
//        Image image = new Image(drawable);
//        image.setSize(500, 500);
//        image.setPosition(200, 200);
//
//        stage.addActor(image);

//        showNewShear();
//        showImage(stage);
    }

    private void showNewShear() {
        region1 = new TextureRegion(Asset.getAsset().getTexture("assets/shuoming.png"));
        region2 = new TextureRegion(Asset.getAsset().getTexture("assets/0_1_41_512.jpg"));
        ShearTextureRegionDrawable shearTextureRegionDrawable = new ShearTextureRegionDrawable(region1);
        Image img = new Image(shearTextureRegionDrawable);
        addActor(img);
        img.setPosition(200, 200);
        img.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);


                img.setOrigin(Align.center);
                img.addAction(Actions.parallel(
                        Actions.sequence(Actions.scaleTo(0,1,0.1667f),
                                Actions.run(()->{
                                    if (isFlag){
                                        shearTextureRegionDrawable.updateTextureRegion(
                                                region1);
                                    }else {
                                        shearTextureRegionDrawable.updateTextureRegion(region2);
                                    }isFlag =! isFlag;

                                }),
                                Actions.scaleTo(1,1,0.1667f)),
                        Actions.sequence(
                                NewActions.numAction(0,-20,0.1667f,(v)->{
                                    shearTextureRegionDrawable.setShearX(v);
                                },(v)->{
                                    shearTextureRegionDrawable.setShearX(v);
                                }),
                                NewActions.numAction(-20,0,0.1667f,(v)->{
                                    shearTextureRegionDrawable.setShearX(v);
                                },(v)->{
                                    shearTextureRegionDrawable.setShearX(v);
                                })
                        )
                ));


            }
        });
    }

    private boolean isFlag = true;
    private void showShearAnimation(ShearTextureRegionDrawable shearTextureRegionDrawable, Image img) {
        NumAction numAction = new NumAction();
        numAction.setDuration(0.16667f);
        numAction.setStart(0);
        numAction.setEnd(-20);
        numAction.setNumActionListener(new NumActionListener() {
            @Override
            public void update(float value) {
                shearTextureRegionDrawable.setShearX(value);
            }
        });

        NumAction numAction2 = new NumAction();
        numAction2.setDuration(0.16667f);
        numAction2.setStart(-20);
        numAction2.setEnd(0);
        numAction2.setNumActionListener(new NumActionListener() {
            @Override
            public void update(float value) {
                shearTextureRegionDrawable.setShearX(value);
            }
        });

        img.setOrigin(Align.center);
        img.addAction(Actions.parallel(
                Actions.sequence(Actions.scaleTo(0,1,0.1667f),
                        Actions.run(()->{
                            if (isFlag){
                                shearTextureRegionDrawable.updateTextureRegion(
                                        region1);
                            }else {
                                shearTextureRegionDrawable.updateTextureRegion(region2);
                            }isFlag =! isFlag;

                        }),
                        Actions.scaleTo(1,1,0.1667f)),
                Actions.sequence(numAction, numAction2)
        ));
    }

    private static void showImage(Stage stage) {
        ImageXT image = new ImageXT();
        image.setPosition(200, 200);

        stage.addActor(image);


        NumAction part1 = new NumAction();
        part1.setDuration(0.16667f);
        part1.setStart(0);
        part1.setEnd(-20);
        part1.setNumActionListener(new NumActionListener() {
            @Override
            public void update(float value) {
                image.setShearXDeg(value);
            }
        });
        part1.setEndRunable(new NumActionListener() {
            @Override
            public void update(float value) {
                image.setShearXDeg(value);
            }
        });

        NumAction part2 = new NumAction();
        part2.setDuration(0.16667f);
        part2.setStart(-20);
        part2.setEnd(0);

        part2.setNumActionListener(new NumActionListener() {
            @Override
            public void update(float value) {
                image.setShearXDeg(value);
            }
        });
        part2.setEndRunable(new NumActionListener() {
            @Override
            public void update(float value) {
                image.setShearXDeg(value);
            }
        });
        image.addAction(Actions.sequence(part1, part2));


        image.addAction(Actions.sequence(
                Actions.scaleTo(0,0,0.1667f),
                Actions.scaleTo(0,1,0.1667f)
        ));
    }
}
