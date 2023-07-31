package com.libGdx.test.beser;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.async.AsyncTask;

/**
 * @Auther jian xian si qi
 * @Date 2023/7/17 14:05
 */
public class RoadPic extends Group implements Disposable {
    private Road road;
    private TextureRegion roadRegion;
    private float xOff;
    private float yOff;
    private int w;
    private int h;
    private  Image image;
    public RoadPic(Road rd, final int w, final int h, final float xOff, final float yOff) {
        this.road = rd;
        setSize(w, h);
        this.w = w;
        this.h = h;
        this.yOff = yOff;
        this.xOff = xOff;


    }

    public void update(){
        update(100);
    }

    public void update(int v){
        ThreadU.exec(new AsyncTask<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    Array<float[]> drawPos = RoadPic.this.road.getDrawPos(v);
                    if (xOff != 0 || yOff != 0) {
                        for (float[] pos : drawPos) {
                            pos[0] += xOff;
                            pos[1] += yOff;
                        }
                    }
                    final Pixmap pixmap = new RoadDrawU(w, h).drawPath(drawPos);
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            Texture texture = new Texture(pixmap);
                            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

                            //============轨道图片导出
                            PixmapIO.writePNG(Gdx.files.local("road.png"), pixmap);
                            //=============
                            pixmap.dispose();

                            synchronized (regionLock) {
//                                Texture texture1 = new Texture("dog_xuanguan.png");
                                if (roadRegion ==null) {
                                    roadRegion = new TextureRegion(texture);

                                    roadRegion.flip(false, true);
                                    Image image = new Image(roadRegion);
                                    RoadPic.this.addActor(image);
                                    image.pack();
                                }
                                roadRegion.setTexture(texture);


                            }



                            if (isDisposed) {
                                dispose();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

//    public void draw(Batch batch, float parentAlpha) {
//        if (roadRegion != null) {
//            DU.drawTextureByActor(batch, parentAlpha, roadRegion, this);
//        }
//    }

    boolean isDisposed;
    Object regionLock = new Object();

    @Override
    public synchronized void dispose() {
        synchronized (regionLock) {
            if (roadRegion != null) {
                roadRegion.getTexture().dispose();
                roadRegion = null;
            }
        }
        if (isDisposed) {
            isDisposed = true;
        }
    }
}
