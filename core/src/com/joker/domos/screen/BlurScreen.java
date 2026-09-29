package com.joker.domos.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.screen.BaseScreen;

/** Android-ready real-time frosted glass demo. */
public class BlurScreen extends BaseScreen {
    private DemoBackground background;
    private BlurGlass blurGlass;
    private ParameterPanel parameterPanel;
    private FpsDisplay fpsDisplay;

    public BlurScreen(BaseGame game) {
        super(game);
    }

    @Override
    public void initView() {
        super.initView();
        background = new DemoBackground();
        background.setBounds(0, 0, Constant.GAMEWIDTH, Constant.GAMEHIGHT);
        addActor(background);
        Image image = new Image(Asset.getAsset().getTexture("0_1_41_512.jpg"));
        background.addActor(image);
        image.setScale(19);
        blurGlass = new BlurGlass(background);
        blurGlass.setSize(430, 430);
        blurGlass.setPosition((Constant.GAMEWIDTH - blurGlass.getWidth()) / 2f,
                (Constant.GAMEHIGHT - blurGlass.getHeight()) / 2f);
        addActor(blurGlass);

        parameterPanel = new ParameterPanel(blurGlass);
        parameterPanel.setBounds(110, 50, Constant.GAMEWIDTH - 220, 390);
        addActor(parameterPanel);

        fpsDisplay = new FpsDisplay();
        fpsDisplay.setBounds(Constant.GAMEWIDTH - 330, Constant.GAMEHIGHT - 110, 290, 80);
        addActor(fpsDisplay);
    }

    @Override
    public void dispose() {
        if (blurGlass != null) blurGlass.dispose();
        if (background != null) background.dispose();
        if (parameterPanel != null) parameterPanel.dispose();
        if (fpsDisplay != null) fpsDisplay.dispose();
        super.dispose();
    }

    private static final class DemoBackground extends Group implements Disposable {
        private final SpriteBatch captureBatch = new SpriteBatch();
        private FrameBuffer frameBuffer;
        private TextureRegion screenRegion;

        @Override
        public void draw(Batch batch, float parentAlpha) {
            ensureFrameBuffer();
            batch.end();
            frameBuffer.begin();
            Gdx.gl.glClearColor(0.07f, 0.09f, 0.16f, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            captureBatch.setProjectionMatrix(batch.getProjectionMatrix());
            captureBatch.setTransformMatrix(batch.getTransformMatrix());
            captureBatch.begin();

            // Draw this Group and all of its children into the off-screen target.
            // The Stage batch is deliberately ended above, so passing it here would
            // make Image/Drawable fail with "Batch.begin must be called before draw".
            super.draw(captureBatch, parentAlpha);
            captureBatch.end();
            frameBuffer.end();
            getStage().getViewport().apply();
            batch.begin();
            batch.setColor(Color.WHITE);
            batch.draw(screenRegion, getX(), getY(), getWidth(), getHeight());
        }

        private void ensureFrameBuffer() {
            int width = Gdx.graphics.getBackBufferWidth();
            int height = Gdx.graphics.getBackBufferHeight();
            if (frameBuffer != null
                    && frameBuffer.getWidth() == width
                    && frameBuffer.getHeight() == height) return;
            if (frameBuffer != null) frameBuffer.dispose();
            frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
            frameBuffer.getColorBufferTexture().setFilter(
                    Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            screenRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
            screenRegion.flip(false, true);
        }

        private Texture getCaptureTexture() {
            return frameBuffer.getColorBufferTexture();
        }

        private int getCaptureWidth() {
            return frameBuffer.getWidth();
        }

        private int getCaptureHeight() {
            return frameBuffer.getHeight();
        }

        @Override
        public void dispose() {
            if (frameBuffer != null) frameBuffer.dispose();
            captureBatch.dispose();
        }
    }

    private static final class BlurGlass extends Actor implements Disposable {
        private static final String VERTEX =
                "attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\n" +
                "uniform mat4 u_projTrans;\nvarying vec4 v_color;\nvarying vec2 v_uv;\n" +
                "void main(){v_color=a_color;v_uv=a_texCoord0;gl_Position=u_projTrans*a_position;}\n";

        private static final String FRAGMENT =
                "#ifdef GL_ES\nprecision mediump float;\n#endif\n" +
                "uniform sampler2D u_texture;uniform sampler2D u_scene;uniform vec2 u_resolution;uniform vec2 u_size;\n" +
                "uniform float u_corner;uniform vec4 u_overlay;\n" +
                "varying vec4 v_color;varying vec2 v_uv;\n" +
                "float roundBox(vec2 p,vec2 b,float r){vec2 q=abs(p)-b+r;return min(max(q.x,q.y),0.0)+length(max(q,0.0))-r;}\n" +
                "void main(){vec2 p=(v_uv-0.5)*u_size;float edge=roundBox(p,u_size*0.5,u_corner);if(edge>1.0)discard;\n" +
                "vec2 uv=gl_FragCoord.xy/u_resolution;vec4 c=texture2D(u_scene,uv);\n" +
                "c=mix(c,u_overlay,u_overlay.a);float aa=1.0-smoothstep(0.0,1.0,edge);gl_FragColor=vec4(c.rgb,aa)*v_color;}\n";

        private static final String PASS_FRAGMENT =
                "#ifdef GL_ES\nprecision mediump float;\n#endif\n" +
                "uniform sampler2D u_texture;uniform sampler2D u_source;uniform vec2 u_resolution;\n" +
                "uniform vec2 u_direction;uniform float u_radius;\n" +
                "void main(){vec2 uv=gl_FragCoord.xy/u_resolution;vec2 stepv=u_direction*u_radius/(8.0*u_resolution);" +
                "vec4 c=vec4(0.0);float total=0.0;" +
                "for(int i=-8;i<=8;i++){float fi=float(i);float w=9.0-abs(fi);" +
                "c+=texture2D(u_source,uv+stepv*fi)*w;total+=w;}gl_FragColor=c/total;}\n";

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

        private BlurGlass(DemoBackground background) {
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

        private void renderPass(Texture source, FrameBuffer target, int width, int height,
                                float directionX, float directionY, float radius) {
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

        private void setBlurRadius(float value) { blurRadius = value; }
        private void setBlurRounds(float value) { blurRounds = MathUtils.clamp(MathUtils.round(value), 1, 15); }
        private void setCornerRadius(float value) { cornerRadius = value; }
        private void setOverlayAlpha(float value) { overlayAlpha = value; }

        @Override
        public void dispose() {
            if (ping != null) ping.dispose();
            if (pong != null) pong.dispose();
            blurBatch.dispose();
            passShader.dispose();
            shader.dispose();
            pixel.dispose();
        }
    }

    private static final class ParameterPanel extends Actor implements Disposable {
        private static final float[] MIN = {0f, 1f, 0f, 0f};
        private static final float[] MAX = {100f, 15f, 215f, 1f};
        private static final String[] LABEL = {
                "Blur radius", "Blur rounds", "Corner radius", "White overlay"
        };
        private final BlurGlass target;
        private final Texture pixel = makePixel();
        private final BitmapFont font = Asset.getAsset().loadBitFont("font/Cali_75.fnt");
        private final float[] values = {45f, 2f, 65f, 128f / 255f};
        private int dragging = -1;

        private ParameterPanel(BlurGlass target) {
            this.target = target;
            font.getData().setScale(1.05f);
            addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    dragging = rowAt(y);
                    if (dragging < 0) return false;
                    update(dragging, x);
                    return true;
                }
                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    if (dragging >= 0) update(dragging, x);
                }
                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    dragging = -1;
                }
            });
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.setColor(0.035f, 0.045f, 0.08f, 0.92f * parentAlpha);
            batch.draw(pixel, getX(), getY(), getWidth(), getHeight());
            float left = getX() + 36f;
            float trackWidth = getWidth() - 72f;
            for (int row = 0; row < 4; row++) {
                float y = getY() + getHeight() - 76f - row * 88f;
                String value = row == 3 ? MathUtils.round(values[row] * 100f) + "%"
                        : Integer.toString(MathUtils.round(values[row]));
                font.setColor(1f, 1f, 1f, parentAlpha);
                font.draw(batch, LABEL[row] + "  " + value, left, y + 47f);
                batch.setColor(1f, 1f, 1f, 0.22f * parentAlpha);
                batch.draw(pixel, left, y, trackWidth, 10f);
                float ratio = (values[row] - MIN[row]) / (MAX[row] - MIN[row]);
                batch.setColor(0.43f, 0.72f, 1f, parentAlpha);
                batch.draw(pixel, left, y, trackWidth * ratio, 10f);
                batch.draw(pixel, left + trackWidth * ratio - 13f, y - 13f, 26f, 36f);
            }
            batch.setColor(Color.WHITE);
        }

        private int rowAt(float y) {
            for (int row = 0; row < 4; row++) {
                float trackY = getHeight() - 76f - row * 88f;
                if (y >= trackY - 28f && y <= trackY + 38f) return row;
            }
            return -1;
        }

        private void update(int row, float x) {
            float ratio = MathUtils.clamp((x - 36f) / (getWidth() - 72f), 0f, 1f);
            values[row] = MIN[row] + (MAX[row] - MIN[row]) * ratio;
            if (row == 0) target.setBlurRadius(values[row]);
            else if (row == 1) {
                values[row] = MathUtils.round(values[row]);
                target.setBlurRounds(values[row]);
            } else if (row == 2) target.setCornerRadius(values[row]);
            else target.setOverlayAlpha(values[row]);
        }

        @Override
        public void dispose() {
            font.dispose();
            pixel.dispose();
        }
    }

    private static final class FpsDisplay extends Actor implements Disposable {
        private final BitmapFont font = Asset.getAsset().loadBitFont("font/Cali_75.fnt");

        private FpsDisplay() {
            font.getData().setScale(2f);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            font.setColor(1f, 1f, 1f, parentAlpha);
            font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(),
                    getX(), getY() + getHeight(), getWidth(), Align.right, false);
        }

        @Override
        public void dispose() {
            font.dispose();
        }
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
