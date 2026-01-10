package com.libGdx.test.bloom;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Bloom extends ApplicationAdapter {
    public static boolean useAlphaChannelAsMask = false;
    /* renamed from: r */
    private float r;
    /* renamed from: g */
    private float g;
    private float b;
    private float a;
    private boolean blending;
    private float bloomIntensity;
    private ShaderProgram bloomShader;
    public int blurPasses;
    private ShaderProgram blurShader;
    private boolean capturing;
    private boolean disposeFBO;
    private FrameBuffer frameBuffer;
    private Mesh fullScreenQuad;

    /* renamed from: h */
    private int f1095h;
    private Texture original;
    private float originalIntensity;
    private FrameBuffer pingPongBuffer1;
    private FrameBuffer pingPongBuffer2;
    private Texture pingPongTex1;
    private Texture pingPongTex2;

    private float treshold;
    private ShaderProgram tresholdShader;

    /* renamed from: w */
    private int f1097w;

    public void resume() {
        this.bloomShader.begin();
        this.bloomShader.setUniformi("u_texture0", 0);
        this.bloomShader.setUniformi("u_texture1", 1);
        this.bloomShader.end();
        setSize(this.f1097w, this.f1095h);
        setTreshold(this.treshold);
        setBloomIntesity(this.bloomIntensity);
        setOriginalIntesity(this.originalIntensity);
        this.original = this.frameBuffer.getColorBufferTexture();
        this.pingPongTex1 = this.pingPongBuffer1.getColorBufferTexture();
        this.pingPongTex2 = this.pingPongBuffer2.getColorBufferTexture();
    }

    public Bloom(int width, int height, boolean alphaMask, boolean blending, boolean RGBA) {
        this.blurPasses = 1;
        this.blending = false;
        this.capturing = false;
        this.r = 0;
        this.g = 0;
        this.b = 0;
        this.a = 1.0f;
        this.disposeFBO = true;
        initialize(width, height, null, alphaMask, blending, RGBA);
    }

    private void initialize(int width, int height, FrameBuffer frameBuffer, boolean alphaMask, boolean blending, boolean RGBA) {
        Pixmap.Format format;
        this.blending = blending;
        if (RGBA) {
            if (blending) {
                format = Pixmap.Format.RGBA8888;
            } else {
                format = Pixmap.Format.RGB888;
            }
        } else if (blending) {
            format = Pixmap.Format.RGBA4444;
        } else {
            format = Pixmap.Format.RGB565;
        }
        if (frameBuffer == null) {
            this.frameBuffer = new FrameBuffer(format, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), alphaMask);
        } else {
            this.frameBuffer = frameBuffer;
        }
        this.pingPongBuffer1 = new FrameBuffer(format, width, height, false);
        this.pingPongBuffer2 = new FrameBuffer(format, width, height, false);
        this.original = this.frameBuffer.getColorBufferTexture();
        this.pingPongTex1 = this.pingPongBuffer1.getColorBufferTexture();
        this.pingPongTex2 = this.pingPongBuffer2.getColorBufferTexture();
        this.fullScreenQuad = createFullScreenQuad();
        String str = blending ? "alpha_" : "";
        this.bloomShader = BloomShaderLoader.createShader("screenspace", String.valueOf(str) + "bloom");
        if (useAlphaChannelAsMask) {
            this.tresholdShader = BloomShaderLoader.createShader("screenspace", "maskedtreshold");
        } else {
            this.tresholdShader = BloomShaderLoader.createShader("screenspace", String.valueOf(str) + "treshold");
        }
        this.blurShader = BloomShaderLoader.createShader("blurspace", String.valueOf(str) + "gaussian");
        setSize(width, height);
        setBloomIntesity(2.5f);
        setOriginalIntesity(0.8f);
        setTreshold(0.5f);
        this.bloomShader.begin();
        this.bloomShader.setUniformi("u_texture0", 0);
        this.bloomShader.setUniformi("u_texture1", 1);
        this.bloomShader.end();
    }

    public void setClearColor(float f, float f2, float f3, float f4) {
        this.r = f;
        this.g = f2;
        this.b = f3;
        this.a = f4;
    }

    public void capture() {
        if (!this.capturing) {
            this.capturing = true;
            this.frameBuffer.begin();
            Gdx.gl.glClearColor(this.r, this.g, this.b, this.a);
            Gdx.gl.glClear(16640);
        }
    }

    public void capturePause() {
        if (this.capturing) {
            this.capturing = false;
            this.frameBuffer.end();
        }
    }

    public void captureContinue() {
        if (!this.capturing) {
            this.capturing = true;
            this.frameBuffer.begin();
        }
    }

    public void render() {
        if (this.capturing) {
            this.capturing = false;
            this.frameBuffer.end();
        }
        Gdx.gl.glDisable(GL20.GL_BLEND);
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
        Gdx.gl.glDepthMask(false);
        gaussianBlur();
        if (this.blending) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
        this.pingPongTex1.bind(1);
        this.original.bind(0);
        this.bloomShader.begin();
        this.fullScreenQuad.render(this.bloomShader, 6);
        this.bloomShader.end();
    }

    private void gaussianBlur() {
        this.original.bind(0);
        this.pingPongBuffer1.begin();
        this.tresholdShader.begin();
        this.fullScreenQuad.render(this.tresholdShader, 6, 0, 4);
        this.tresholdShader.end();
        this.pingPongBuffer1.end();
        for (int i = 0; i < this.blurPasses; i++) {
            this.pingPongTex1.bind(0);
            this.pingPongBuffer2.begin();
            this.blurShader.begin();
            this.blurShader.setUniformf("dir", 1.0f, 0);
            this.fullScreenQuad.render(this.blurShader, 6, 0, 4);
            this.blurShader.end();
            this.pingPongBuffer2.end();
            this.pingPongTex2.bind(0);
            this.pingPongBuffer1.begin();
            this.blurShader.begin();
            this.blurShader.setUniformf("dir", 0, 1.0f);
            this.fullScreenQuad.render(this.blurShader, 6, 0, 4);
            this.blurShader.end();
            this.pingPongBuffer1.end();
        }
    }

    public void setBloomIntesity(float f) {
        this.bloomIntensity = f;
        this.bloomShader.begin();
        this.bloomShader.setUniformf("BloomIntensity", f);
        this.bloomShader.end();
    }

    public void setOriginalIntesity(float f) {
        this.originalIntensity = f;
        this.bloomShader.begin();
        this.bloomShader.setUniformf("OriginalIntensity", f);
        this.bloomShader.end();
    }

    public void setTreshold(float f) {
        this.treshold = f;
        this.tresholdShader.begin();
        this.tresholdShader.setUniformf("treshold", f, 1.0f / (1.0f - f));
        this.tresholdShader.end();
    }

    private void setSize(int i, int i2) {
        this.f1097w = i;
        this.f1095h = i2;
        this.blurShader.begin();
        this.blurShader.setUniformf("size", i, i2);
        this.blurShader.end();
    }

    public void dispose() {
        if (this.disposeFBO) {
            this.frameBuffer.dispose();
        }
        this.fullScreenQuad.dispose();
        this.pingPongBuffer1.dispose();
        this.pingPongBuffer2.dispose();
        this.blurShader.dispose();
        this.bloomShader.dispose();
        this.tresholdShader.dispose();
    }

    private Mesh createFullScreenQuad() {
        float[] fArr = {-1.0f, -1.0f, 0, 0, 1.0f, -1.0f, 1.0f,
                0, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 0, 1.0f};
        Mesh mesh = new Mesh(true, 4, 0,
                new VertexAttribute(1, 2, ShaderProgram.POSITION_ATTRIBUTE),
                new VertexAttribute(16, 2, "a_texCoord0"));
        mesh.setVertices(fArr);
        return mesh;
    }
}