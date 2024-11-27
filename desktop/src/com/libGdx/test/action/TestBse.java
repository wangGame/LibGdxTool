package com.libGdx.test.action;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.esotericsoftware.spine.loader.SpineActor;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.besier.BseInterpolation;
import com.libGdx.test.base.LibGdxTestMain;

public class TestBse extends LibGdxTestMain {
    private ShapeRenderer renderer;
    public static void main(String[] args) {
        run(TestBse.class);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        renderer = new ShapeRenderer();
        SpineActor actorSpine = new SpineActor("assets/spine/quxian_ck");
        addActor(actorSpine);
        actorSpine.setPosition(500,500);
        actorSpine.addAction(Actions.delay(2,Actions.run(()->{

            Image img = new Image(Asset.getAsset().getTexture("assets/spine/A.png"));
            addActor(img);
            img.setPosition(100,500);
            img.addAction(
                    Actions.moveTo(100f,500+283.86f,1.0f, new BseInterpolation(
                            0.25f,0,0.75f,1.0f
                    ))
            );
            actorSpine.setAnimation("1",false);
        })));
    }

    @Override
    public void render() {
        super.render();

        if (renderer == null)return;
        renderer.begin(ShapeRenderer.ShapeType.Line);

        renderer.setColor(Color.RED);
        renderer.rect(0, 0, 1, 1);

        renderer.setColor(Color.BLUE);
        renderer.circle(0.2f, 0.2f, 0.5f, 40);

        renderer.setColor(Color.YELLOW);
        renderer.line(0, 0, 1, 1);

        renderer.setColor(Color.WHITE);
        renderer.box(0.1f, 0.1f, 0.1f, 0.3f, 0.25f, 0.1f);

        renderer.setColor(Color.GREEN);
        renderer.cone(0.6f, 0.6f, 0, 0.3f, 0.75f, 20);

        renderer.setColor(Color.MAGENTA);
        renderer.triangle(-0.1f, 0.1f, -0.6f, -0.1f, -0.3f, 0.5f);

        renderer.setColor(Color.CYAN);
        renderer.curve(0.0f, 0.25f, 0.2f, 0.3f, 0.3f, 0.6f, 0.1f, 0.5f, 30);

        renderer.setColor(Color.GOLD);
        renderer.ellipse(0.7f, -0.1f, 0.3f, 0.1f, 45f, 40);
        renderer.ellipse(0.7f, -0.1f, 0.3f, 0.1f, 135f);

        renderer.end();

    }
}
