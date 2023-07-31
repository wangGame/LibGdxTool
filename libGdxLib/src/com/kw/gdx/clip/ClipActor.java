package com.kw.gdx.clip;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ClipActor extends Actor {
    private TextureRegion region;
    private ShapeRenderer sr;
    private float clipHeight;      //裁剪的高度
    private float regionheight;   //画的高度

    private float clipWidth;      //裁剪的宽度
    private float regionwidth;   //画的宽度
    private boolean parallelogram = false;
    private float[] points;
    private float[] vertices;
    private float offset;

    private boolean clipY;
    private float progress;

    private float targetpregress;

    private float sLength,eLength;   //去掉头尾真正显示进度的

    private float speed;

    public ClipActor(TextureRegion region,boolean clipY,float sLength,float eLength){
        this.clipY=clipY;
        this.region=region;
        this.sLength=sLength;
        this.eLength=eLength;
        this.regionheight=region.getRegionHeight();
        this.regionwidth=region.getRegionWidth();
        speed=1;
        if(clipY){
            clipHeight=0;
            clipWidth=regionwidth;
        }else{
            clipWidth=0;
            clipHeight=regionheight;
        }
        progress=0;
        targetpregress=0;
        setSize(regionwidth,regionheight);
        sr = new ShapeRenderer();
        parallelogram = false;
    }

    public ClipActor(TextureRegion region, float offset, float regionheight){
        this.region=region;
        this.offset=offset;
        this.regionheight=regionheight;
        points = new float[]{0,0,0 + offset,region.getRegionHeight() ,region.getRegionWidth() + offset ,region.getRegionHeight() ,region.getRegionWidth(),0};
        clipHeight=0;
        setSize(region.getRegionWidth()+Math.abs(offset),regionheight);
        sr = new ShapeRenderer();
        parallelogram = true;
        vertices = new float[20];
    }


    public void setRegion(TextureRegion region){
        this.region=region;
    }

    public void setProgress(float pregress){
        this.targetpregress=MathUtils.clamp(pregress,0,1);
    }

    public void setProgressNow(float progress){
        this.progress= MathUtils.clamp(progress,0,1);
        this.targetpregress=this.progress;
        updateClip(this.progress);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(targetpregress>progress){
            progress+=2*delta*speed;
            if(progress>=targetpregress){
                progress=targetpregress;
            }
            updateClip(progress);
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void updateClip(float progress){
        if(clipY){
            clipHeight=sLength+(regionheight-sLength-eLength)*progress;
            if(progress==0) clipHeight=0;
            if(progress==1) clipHeight=regionheight;
        }else{
            clipWidth=sLength+(regionwidth-sLength-eLength)*progress;
            if(progress==0) clipWidth=0;
            if(progress==1) clipWidth=regionwidth;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.end();

        // Clear the buffer
        Gdx.gl.glClearDepthf(1.0f);
        Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
        // Disable writing to frame buffer and
        // Set up the depth test
        Gdx.gl.glColorMask(false, false, false, false);
        Gdx.gl.glDepthFunc(GL20.GL_LESS);
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(true);

        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setTransformMatrix(batch.getTransformMatrix());
        //sr.setColor(Color.valueOf("FFFFFF00"));
        sr.begin(ShapeRenderer.ShapeType.Filled);
//        sr.rect(getX(), getY(), clipWidth,clipHeight);
        sr.circle(getX(), getY(),clipWidth);
        sr.end();
        batch.begin();
        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glDepthMask(true);
        Gdx.gl.glDepthFunc(GL20.GL_EQUAL);
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if(parallelogram){
            drawParallelogram(batch);
        }else {
            batch.draw(this.region, getX(), getY(),regionwidth, regionheight);
        }

        batch.flush();
        // Ensure depth test is disabled so that depth
        // testing is not run on other rendering code.
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

    }

    private void drawParallelogram(Batch batch){
        float offsetX = 0;
        if(offset<0){
            offsetX = -offset;
        }

        vertices[0] = getX() +offsetX+ points[0];
        vertices[1] = getY()+ points[1];
        vertices[2] = batch.getPackedColor();
        vertices[3] = region.getU();
        vertices[4] = region.getV();

        vertices[5] = getX() +offsetX+ points[2];
        vertices[6] = getY()+ regionheight;
        vertices[7] = batch.getPackedColor();
        vertices[8] = region.getU();
        vertices[9] = region.getV2();

        vertices[10] = getX() +offsetX+ points[4];
        vertices[11] = getY()+ regionheight;
        vertices[12] = batch.getPackedColor();
        vertices[13] = region.getU2();
        vertices[14] = region.getV2();

        vertices[15] = getX() +offsetX+ points[6];
        vertices[16] = getY()+ points[7];
        vertices[17] = batch.getPackedColor();
        vertices[18] = region.getU2();
        vertices[19] = region.getV();
        batch.draw(region.getTexture(), vertices, 0, 20);
    }

    public void setClipHeight(float clipHeight) {
        this.clipHeight = clipHeight;
    }

    public float getProgress() {
        return progress;
    }
}
