package com.kw.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.example.trace.PixmapTracer;
import com.example.trace.TraceCallback;
import com.example.trace.TraceOptions;
import com.example.trace.TraceResult;

/**
 * A concrete Screen implementation for testing/using the clean-room Trace feature.
 *
 * How to use:
 * 1. Put an image at assets/trace/test.png, or change IMAGE_PATH.
 * 2. Open this screen with: setScreen(TraceScreen.class) or game.setScreen(new TraceScreen(game)).
 * 3. Press R to refresh trace, SPACE to toggle triangle lines, ENTER to apply current result.
 */
public class TraceScreen extends BaseScreen {
    private static final String TAG = "TraceScreen";
    private static final String IMAGE_PATH = "trace/test.png";

    private PixmapTracer tracer;
    private TraceOptions options;
    private Pixmap pixmap;
    private Texture texture;
    private TracePreviewActor previewActor;
    private TraceHudActor hudActor;
    private TraceResult currentResult;
    private BitmapFont font;

    private boolean tracing;
    private boolean drawTriangles;

    public TraceScreen(BaseGame game) {
        super(game);
    }

    @Override
    protected void initData() {
        tracer = new PixmapTracer("trace-screen");

        options = new TraceOptions();
        options.alphaThreshold = 8;
        options.padding = 0f;
        options.detail = 0.25f;
        options.concavity = 0.5f;
        options.refinement = 0.5f;
        options.traceAllIslands = true;
        options.flipY = true;
        options.maxVerticesPerPolygon = 256;

        font = Asset.getAsset().loadBitFont("Ma-B_46.fnt");

        // Trace uses Pixmap alpha. Texture is only for preview drawing.
        pixmap = new Pixmap(Gdx.files.internal(IMAGE_PATH));
        texture = new Texture(pixmap);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void initView() {
        previewActor = new TracePreviewActor(texture);
        previewActor.setDrawOutline(true);
        previewActor.setDrawTriangles(drawTriangles);

        // Keep the image inside the design resolution while preserving aspect ratio.
        float maxW = Constant.GAMEWIDTH * 0.72f;
        float maxH = Constant.GAMEHIGHT * 0.72f;
        float scale = Math.min(maxW / texture.getWidth(), maxH / texture.getHeight());
        float viewW = texture.getWidth() * scale;
        float viewH = texture.getHeight() * scale;
        previewActor.setSize(viewW, viewH);
        previewActor.setPosition(Constant.GAMEWIDTH / 2f, Constant.GAMEHIGHT / 2f - 20f, Align.center);
        rootView.addActor(previewActor);

        hudActor = new TraceHudActor();
        hudActor.setBounds(20, Constant.GAMEHIGHT - 170, Constant.GAMEWIDTH - 40, 150);
        rootView.addActor(hudActor);

        refreshTrace();
    }

    @Override
    protected void initTouch() {
        super.initTouch();
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (Constant.disAble) return false;

                switch (keycode) {
                    case Input.Keys.R:
                        refreshTrace();
                        return true;
                    case Input.Keys.SPACE:
                        drawTriangles = !drawTriangles;
                        if (previewActor != null) previewActor.setDrawTriangles(drawTriangles);
                        return true;
                    case Input.Keys.ENTER:
                        applyTraceResult();
                        return true;
                    case Input.Keys.UP:
                        options.detail = Math.min(1f, options.detail + 0.05f);
                        refreshTrace();
                        return true;
                    case Input.Keys.DOWN:
                        options.detail = Math.max(0f, options.detail - 0.05f);
                        refreshTrace();
                        return true;
                    case Input.Keys.RIGHT:
                        options.alphaThreshold = Math.min(254, options.alphaThreshold + 4);
                        refreshTrace();
                        return true;
                    case Input.Keys.LEFT:
                        options.alphaThreshold = Math.max(0, options.alphaThreshold - 4);
                        refreshTrace();
                        return true;
                    case Input.Keys.NUM_1:
                        options.padding = Math.max(0f, options.padding - 0.5f);
                        refreshTrace();
                        return true;
                    case Input.Keys.NUM_2:
                        options.padding += 0.5f;
                        refreshTrace();
                        return true;
                }
                return false;
            }
        });
    }

    /** BaseScreen already maps key R to r(), keep it wired to the same behavior. */
    @Override
    protected void r() {
        refreshTrace();
    }

    private void refreshTrace() {
        if (pixmap == null || tracer == null || options == null) return;

        tracing = true;
        // TraceHudActor extends Actor, not Layout/Widget, so no invalidate() is needed.

        tracer.trace(pixmap, options, new TraceCallback() {
            @Override
            public void onTraceFinished(TraceResult result) {
                tracing = false;
                currentResult = result;

                if (previewActor != null) {
                    previewActor.setTraceResult(result);
                    previewActor.setDrawTriangles(drawTriangles);
                }

                Gdx.app.log(TAG, "trace finished, outlines=" + result.outlines.size
                        + ", vertices=" + result.getVertexCount()
                        + ", triangles=" + result.getTriangleCount()
                        + ", elapsed=" + result.elapsedMillis + "ms");
            }

            @Override
            public void onTraceFailed(Throwable error) {
                tracing = false;
                Gdx.app.error(TAG, "trace failed", error);
            }

            @Override
            public void onTraceCancelled() {
                tracing = false;
                Gdx.app.log(TAG, "trace cancelled");
            }
        });
    }

    /**
     * OK button equivalent. Replace the TODO with your own edit-mesh data assignment.
     */
    private void applyTraceResult() {
        if (currentResult == null) {
            Gdx.app.log(TAG, "no trace result to apply");
            return;
        }

        float[] vertices = currentResult.vertices; // x,y,u,v,x,y,u,v...
        short[] indices = currentResult.indices;   // triangle indices

        // TODO: connect this to your editable mesh / attachment / polygon data.
        // Example:
        // editableMesh.positions.clear();
        // editableMesh.uvs.clear();
        // editableMesh.indices.clear();
        // for (int i = 0; i < vertices.length; i += 4) {
        //     editableMesh.positions.add(new Vector2(vertices[i], vertices[i + 1]));
        //     editableMesh.uvs.add(new Vector2(vertices[i + 2], vertices[i + 3]));
        // }
        // for (short index : indices) editableMesh.indices.add(index);

        Gdx.app.log(TAG, "apply trace result, vertices=" + currentResult.getVertexCount()
                + ", triangles=" + currentResult.getTriangleCount());
    }

    @Override
    public void dispose() {
        if (tracer != null) tracer.dispose();
        if (previewActor != null) previewActor.dispose();
        if (texture != null) texture.dispose();
        if (pixmap != null) pixmap.dispose();
        if (font != null) font.dispose();
        super.dispose();
    }

    private final class TraceHudActor extends Actor {
        @Override
        public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
            font.setColor(Color.WHITE);

            String status = tracing ? "Tracing..." : "Ready";
            int outlines = currentResult == null ? 0 : currentResult.outlines.size;
            int vertices = currentResult == null ? 0 : currentResult.getVertexCount();
            int triangles = currentResult == null ? 0 : currentResult.getTriangleCount();

            String text = "Trace Demo  " + status + "\n"
                    + "R Refresh    ENTER Apply    SPACE Triangles: " + drawTriangles + "\n"
                    + "UP/DOWN Detail: " + round(options.detail)
                    + "    LEFT/RIGHT Alpha: " + options.alphaThreshold
                    + "    1/2 Padding: " + round(options.padding) + "\n"
                    + "Outlines: " + outlines + "    Vertices: " + vertices + "    Triangles: " + triangles;

            font.draw(batch, text, getX(), getY() + getHeight());
        }

        private String round(float value) {
            return String.format(java.util.Locale.US, "%.2f", value);
        }
    }
}
