package com.libGdx.test.blur;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.libGdx.test.base.LibGdxTestMain;

/** LibGDX implementation of a live rounded frosted-glass view. */
public class BlurApp extends LibGdxTestMain {
    private DemoBackground background;
    private BlurGlass blurGlass;
    private ParameterPanel parameterPanel;

    public static void main(String[] args) {
        new BlurApp().start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        background = new DemoBackground();
        background.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        stage.addActor(background);

        blurGlass = new BlurGlass();
        blurGlass.setSize(430, 430);
        blurGlass.setPosition((stage.getWidth() - blurGlass.getWidth()) / 2f,
                (stage.getHeight() - blurGlass.getHeight()) / 2f);
        stage.addActor(blurGlass);

        parameterPanel = new ParameterPanel(blurGlass);
        parameterPanel.setBounds(110, 70, stage.getWidth() - 220, 300);
        stage.addActor(parameterPanel);
    }

    @Override
    public void dispose() {
        if (blurGlass != null) blurGlass.dispose();
        if (background != null) background.dispose();
        if (parameterPanel != null) parameterPanel.dispose();
        super.dispose();
    }

    /** Moving content behind the panel makes the real-time blur visible. */
    private static final class DemoBackground extends Actor implements Disposable {
        private final Texture pixel = makePixel();
        private float time;

        @Override
        public void act(float delta) {
            super.act(delta);
            time += delta;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            float width = getWidth();
            float height = getHeight();
            batch.setColor(0.07f, 0.09f, 0.16f, 1f);
            batch.draw(pixel, getX(), getY(), width, height);

            float cardW = width * 0.72f;
            float cardH = 250f;
            float travel = height + cardH;
            for (int i = 0; i < 9; i++) {
                float y = ((i * 285f + time * 105f) % travel) - cardH;
                float x = width * 0.14f + MathUtils.sin(time * 0.55f + i) * 85f;
                Color color = i % 3 == 0 ? Color.valueOf("FF5F6D")
                        : i % 3 == 1 ? Color.valueOf("35C9FF") : Color.valueOf("8D67FF");
                batch.setColor(color);
                batch.draw(pixel, x, y, cardW, cardH);
                batch.setColor(1f, 1f, 1f, 0.72f);
                batch.draw(pixel, x + 38f, y + 58f, cardW * 0.55f, 20f);
                batch.setColor(1f, 1f, 1f, 0.38f);
                batch.draw(pixel, x + 38f, y + 105f, cardW * 0.76f, 13f);
                batch.draw(pixel, x + 38f, y + 136f, cardW * 0.64f, 13f);
            }
            batch.setColor(Color.WHITE);
        }

        @Override
        public void dispose() {
            pixel.dispose();
        }
    }

    /** Captures the pixels already drawn below this actor and blurs them on the GPU. */
    private static final class BlurGlass extends Actor implements Disposable {
        private static final String VERTEX =
                "attribute vec4 a_position;\n" +
                "attribute vec4 a_color;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "uniform mat4 u_projTrans;\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_uv;\n" +
                "void main(){v_color=a_color;v_uv=a_texCoord0;gl_Position=u_projTrans*a_position;}\n";

        private static final String FRAGMENT =
                "#ifdef GL_ES\nprecision mediump float;\n#endif\n" +
                "uniform sampler2D u_texture;\n" +
                "uniform sampler2D u_scene;\n" +
                "uniform vec2 u_resolution;\n" +
                "uniform vec2 u_size;\n" +
                "uniform float u_blur;\n" +
                "uniform float u_corner;\n" +
                "uniform vec4 u_overlay;\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_uv;\n" +
                "float roundBox(vec2 p,vec2 b,float r){vec2 q=abs(p)-b+r;return min(max(q.x,q.y),0.0)+length(max(q,0.0))-r;}\n" +
                "void main(){\n" +
                " vec2 p=(v_uv-0.5)*u_size; float edge=roundBox(p,u_size*0.5,u_corner);\n" +
                " if(edge>1.0) discard;\n" +
                " vec2 uv=gl_FragCoord.xy/u_resolution; vec2 d=(u_blur*0.5)/u_resolution;\n" +
                " vec4 c=vec4(0.0);float total=0.0;\n" +
                " for(int iy=-4;iy<=4;iy++){for(int ix=-4;ix<=4;ix++){\n" +
                "  vec2 o=vec2(float(ix),float(iy));float w=exp(-dot(o,o)*0.22);\n" +
                "  c+=texture2D(u_scene,uv+o*d)*w;total+=w;\n" +
                " }}c/=total;\n" +
                " c=mix(c,u_overlay,u_overlay.a); float aa=1.0-smoothstep(0.0,1.0,edge);gl_FragColor=vec4(c.rgb,aa)*v_color;\n" +
                "}\n";

        private final Texture pixel = makePixel();
        private final ShaderProgram shader;
        private Texture scene;
        private int sceneWidth;
        private int sceneHeight;
        private float blurRadius = 45f;
        private float cornerRadius = 65f;
        private float overlayAlpha = 128f / 255f;

        private BlurGlass() {
            ShaderProgram.pedantic = false;
            shader = new ShaderProgram(VERTEX, FRAGMENT);
            if (!shader.isCompiled()) {
                throw new IllegalStateException("Blur shader compile failed:\n" + shader.getLog());
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            ensureSceneTexture();
            batch.flush();
            scene.bind(1);
            Gdx.gl.glCopyTexSubImage2D(GL20.GL_TEXTURE_2D, 0, 0, 0,
                    0, 0, sceneWidth, sceneHeight);
            pixel.bind(0);

            ShaderProgram previous = batch.getShader();
            batch.setShader(shader);
            shader.setUniformi("u_scene", 1);
            shader.setUniformf("u_resolution", sceneWidth, sceneHeight);
            shader.setUniformf("u_size", getWidth(), getHeight());
            float stageToFramebuffer = sceneWidth / getStage().getViewport().getWorldWidth();
            shader.setUniformf("u_blur", blurRadius * stageToFramebuffer * 0.5f);
            shader.setUniformf("u_corner", cornerRadius);
            shader.setUniformf("u_overlay", 1f, 1f, 1f, overlayAlpha);
            batch.setColor(1f, 1f, 1f, parentAlpha);
            batch.draw(pixel, getX(), getY(), getWidth(), getHeight());
            batch.flush();
            batch.setShader(previous);
            batch.setColor(Color.WHITE);
        }

        private void ensureSceneTexture() {
            int width = Gdx.graphics.getBackBufferWidth();
            int height = Gdx.graphics.getBackBufferHeight();
            if (scene != null && width == sceneWidth && height == sceneHeight) return;
            if (scene != null) scene.dispose();
            sceneWidth = width;
            sceneHeight = height;
            scene = new Texture(width, height, Pixmap.Format.RGBA8888);
            scene.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            scene.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        }

        @Override
        public void dispose() {
            if (scene != null) scene.dispose();
            shader.dispose();
            pixel.dispose();
        }

        private void setBlurRadius(float value) {
            blurRadius = value;
        }

        private void setCornerRadius(float value) {
            cornerRadius = value;
        }

        private void setOverlayAlpha(float value) {
            overlayAlpha = value;
        }
    }

    /** Three lightweight sliders, implemented without a Scene2D Skin. */
    private static final class ParameterPanel extends Actor implements Disposable {
        private static final int BLUR = 0;
        private static final int CORNER = 1;
        private static final int OVERLAY = 2;
        private static final float[] MIN = {0f, 0f, 0f};
        private static final float[] MAX = {100f, 215f, 1f};
        private static final String[] LABEL = {"Blur radius", "Corner radius", "White overlay"};

        private final BlurGlass target;
        private final Texture pixel = makePixel();
        private final BitmapFont font = new BitmapFont();
        private final float[] values = {45f, 65f, 128f / 255f};
        private int dragging = -1;

        private ParameterPanel(BlurGlass target) {
            this.target = target;
            font.getData().setScale(1.6f);
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
            for (int row = 0; row < 3; row++) {
                float y = getY() + getHeight() - 82f - row * 88f;
                String value = row == OVERLAY
                        ? MathUtils.round(values[row] * 100f) + "%"
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

        private int rowAt(float localY) {
            for (int row = 0; row < 3; row++) {
                float y = getHeight() - 82f - row * 88f;
                if (localY >= y - 28f && localY <= y + 38f) return row;
            }
            return -1;
        }

        private void update(int row, float localX) {
            float ratio = MathUtils.clamp((localX - 36f) / (getWidth() - 72f), 0f, 1f);
            values[row] = MIN[row] + (MAX[row] - MIN[row]) * ratio;
            if (row == BLUR) target.setBlurRadius(values[row]);
            else if (row == CORNER) target.setCornerRadius(values[row]);
            else target.setOverlayAlpha(values[row]);
        }

        @Override
        public void dispose() {
            font.dispose();
            pixel.dispose();
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
