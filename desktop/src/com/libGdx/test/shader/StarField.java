package com.libGdx.test.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.IndexArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.libGdx.test.base.LibGdxTestMain;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.util.Random;

public class StarField extends LibGdxTestMain {
    private Array<Vector2> stars;
    private Array<Float> radius;
    private ShapeRenderer renderer;
    public static void main(String[] args) {
        StarField field = new StarField();
        field.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        renderer = new ShapeRenderer();
        initStars(0.1f);
    }


    public void initStars(float density){
        // TODO: Figure out how many stars to draw. You'll need the screen dimensions, which you can get using Gdx.graphics.getWidth() and Gdx.graphics.getHeight().
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        int starCount = (int)(screenHeight * screenWidth * density);
        // TODO: Create a new array of Vector2's to hold the star positions
        stars = new Array<Vector2>(starCount);
        radius = new Array<>(starCount);
        // TODO: Use java.util.Random to fill the array of star positions
        Random random = new Random();
        for (int i = 0; i < starCount; i++){
            int x = random.nextInt(screenWidth);
            int y = random.nextInt(screenHeight);
            stars.add(new Vector2(x, y));
            radius.add(random.nextFloat());
        }
    }

    private float movey = 0;

    @Override
    public void render() {
        super.render();
        if (renderer == null)return;
        renderer.begin(ShapeType.Filled);
        for (int i = 0; i < stars.size; i++) {
            renderer.circle( stars.get(i).x, (float) (stars.get(i).y-movey-Math.random()),radius.get(i));
        }
        movey += Gdx.graphics.getDeltaTime()*30;
        renderer.end();
    }
}
