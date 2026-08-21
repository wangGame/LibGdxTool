package com.libGdx.test.anr;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class ANRScreen implements Screen {



    private SpriteBatch batch;

    private BitmapFont font;



    private boolean block = false;



    @Override
    public void show() {


        batch =
                new SpriteBatch();


        font =
                new BitmapFont();



    }




    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(
                0,
                0,
                0,
                1
        );


        Gdx.gl.glClear(
                GL20.GL_COLOR_BUFFER_BIT
        );



        batch.begin();


        font.draw(
                batch,
                "Press SPACE create ANR",
                100,
                300
        );


        batch.end();



        if(
                Gdx.input.isKeyJustPressed(
                        Input.Keys.SPACE
                )
        ){

            createANR();

        }


    }




    /**
     * 模拟主线程卡死
     */
    private void createANR(){



        System.out.println(
                "开始阻塞LibGDX主线程"
        );



        try {


            /*
             *
             * 注意:
             *
             * 这里阻塞的是render线程
             *
             * ANRWatchDog会检测到
             *
             */


            Thread.sleep(
                    10000
            );



        }catch(Exception e){



        }



        System.out.println(
                "阻塞结束"
        );

    }






    @Override
    public void resize(
            int width,
            int height
    ) {



    }



    @Override
    public void pause() {

    }


    @Override
    public void resume() {

    }



    @Override
    public void hide() {

    }




    @Override
    public void dispose() {


        batch.dispose();

        font.dispose();


    }


}
