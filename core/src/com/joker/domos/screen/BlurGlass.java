package com.joker.domos.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class BlurGlass extends Actor implements Disposable {
    private static final String VERTEX = "attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\n" + "uniform mat4 u_projTrans;\nvarying vec4 v_color;\nvarying vec2 v_uv;\n" + "void main(){v_color=a_color;v_uv=a_texCoord0;gl_Position=u_projTrans*a_position;}\n";

    private static final String FRAGMENT = "#ifdef GL_ES\nprecision mediump float;\n#endif\n" + "uniform sampler2D u_texture;uniform sampler2D u_scene;uniform vec2 u_resolution;uniform vec2 u_size;\n" + "uniform float u_corner;uniform vec4 u_overlay;\n" + "varying vec4 v_color;varying vec2 v_uv;\n" + "float roundBox(vec2 p,vec2 b,float r){vec2 q=abs(p)-b+r;return min(max(q.x,q.y),0.0)+length(max(q,0.0))-r;}\n" + "void main(){vec2 p=(v_uv-0.5)*u_size;float edge=roundBox(p,u_size*0.5,u_corner);if(edge>1.0)discard;\n" + "vec2 uv=gl_FragCoord.xy/u_resolution;vec4 c=texture2D(u_scene,uv);\n" + "c=mix(c,u_overlay,u_overlay.a);float aa=1.0-smoothstep(0.0,1.0,edge);gl_FragColor=vec4(c.rgb,aa)*v_color;}\n";

    private static final String PASS_FRAGMENT = "#ifdef GL_ES\nprecision mediump float;\n#endif\n" + "uniform sampler2D u_texture;uniform sampler2D u_source;uniform vec2 u_resolution;\n" + "uniform vec2 u_direction;uniform float u_radius;\n" + "void main(){vec2 uv=gl_FragCoord.xy/u_resolution;vec2 stepv=u_direction*u_radius/(8.0*u_resolution);" + "vec4 c=vec4(0.0);float total=0.0;" + "for(int i=-8;i<=8;i++){float fi=float(i);float w=9.0-abs(fi);" + "c+=texture2D(u_source,uv+stepv*fi)*w;total+=w;}gl_FragColor=c/total;}\n";

    private final Texture pixel = makePixel();
    private final ShaderProgram shader;
    private final ShaderProgram passShader;
    private final SpriteBatch blurBatch = new SpriteBatch();
    private final Matrix4 blurProjection = new Matrix4();
    private final DemoBackground background;
    private FrameBuffer ping;
    private FrameBuffer pong;
    private float blurRadius = 45f;
    private int blurRounds = 2;
    private float cornerRadius = 65f;
    private float overlayAlpha = 128f / 255f;

    public BlurGlass(DemoBackground background) {
        this.background = background;
        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(VERTEX, FRAGMENT);
        if (!shader.isCompiled()) {
            throw new IllegalStateException("Blur shader compile failed:\n" + shader.getLog());
        }
        passShader = new ShaderProgram(VERTEX, PASS_FRAGMENT);
        if (!passShader.isCompiled()) {
            throw new IllegalStateException("Blur pass shader compile failed:\n" + passShader.getLog());
        }
        blurBatch.setShader(passShader);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int sceneWidth = background.getCaptureWidth();
        int sceneHeight = background.getCaptureHeight();
        Texture scene = renderBlur(batch, sceneWidth, sceneHeight);
        scene.bind(1);
        pixel.bind(0);
        ShaderProgram previous = batch.getShader();
        batch.setShader(shader);
        shader.setUniformi("u_scene", 1);
        shader.setUniformf("u_resolution", sceneWidth, sceneHeight);
        shader.setUniformf("u_size", getWidth(), getHeight());
        shader.setUniformf("u_corner", cornerRadius);
        shader.setUniformf("u_overlay", 1f, 1f, 1f, overlayAlpha);
        batch.setColor(1f, 1f, 1f, parentAlpha);
        batch.draw(pixel, getX(), getY(), getWidth(), getHeight());
        batch.flush();
        batch.setShader(previous);
        batch.setColor(Color.WHITE);
    }

    private Texture renderBlur(Batch stageBatch, int sceneWidth, int sceneHeight) {
        final float downsample = 2.52f;
        int width = Math.max(1, Math.round(sceneWidth / downsample));
        int height = Math.max(1, Math.round(sceneHeight / downsample));
        ensureBlurBuffers(width, height);
        float stageToFramebuffer = sceneWidth / getStage().getViewport().getWorldWidth();
        float radius = Math.min(25f, blurRadius * stageToFramebuffer / downsample);

        stageBatch.end();
        renderPass(background.getCaptureTexture(), ping, width, height, 0f, 0f, 0f);
        for (int i = 0; i < blurRounds; i++) {
            renderPass(ping.getColorBufferTexture(), pong, width, height, 1f, 0f, radius);
            renderPass(pong.getColorBufferTexture(), ping, width, height, 0f, 1f, radius);
        }
        getStage().getViewport().apply();
        stageBatch.begin();
        return ping.getColorBufferTexture();
    }

    private void renderPass(Texture source, FrameBuffer target, int width, int height, float directionX, float directionY, float radius) {
        target.begin();
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        blurProjection.setToOrtho2D(0, 0, width, height);
        blurBatch.setProjectionMatrix(blurProjection);
        blurBatch.begin();
        source.bind(1);
        pixel.bind(0);
        passShader.setUniformi("u_source", 1);
        passShader.setUniformf("u_resolution", width, height);
        passShader.setUniformf("u_direction", directionX, directionY);
        passShader.setUniformf("u_radius", radius);
        blurBatch.setColor(Color.WHITE);
        blurBatch.draw(pixel, 0, 0, width, height);
        blurBatch.end();
        target.end();
    }

    private void ensureBlurBuffers(int width, int height) {
        if (ping != null && ping.getWidth() == width && ping.getHeight() == height) return;
        if (ping != null) ping.dispose();
        if (pong != null) pong.dispose();
        ping = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        pong = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
        ping.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pong.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void setBlurRadius(float value) {
        blurRadius = value;
    }

    public void setBlurRounds(float value) {
        blurRounds = MathUtils.clamp(MathUtils.round(value), 1, 15);
    }

    public void setCornerRadius(float value) {
        cornerRadius = value;
    }

    public void setOverlayAlpha(float value) {
        overlayAlpha = value;
    }

    @Override
    public void dispose() {
        if (ping != null) ping.dispose();
        if (pong != null) pong.dispose();
        blurBatch.dispose();
        passShader.dispose();
        shader.dispose();
        pixel.dispose();
    }


    private static Texture makePixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
}
