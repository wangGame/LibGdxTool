package com.kw.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.kw.gdx.BaseGame;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.textureunpacker.TextureUnpacker;
import com.kw.gdx.textureunpacker.TextureUnpackerListener;
import com.kw.gdx.textureunpacker.TextureUnpackerOptions;
import com.kw.gdx.textureunpacker.TextureUnpackerResult;

/**
 * Minimal screen wrapper for the texture unpacker.
 *
 * Put your atlas and page PNGs under assets/unpack/, for example:
 * assets/unpack/skin.atlas
 * assets/unpack/skin.png
 *
 * Output is written to Gdx.files.local("unpacked").
 */
public class TextureUnpackerScreen extends BaseScreen {
    private static final String TAG = "TextureUnpackerScreen";

    private static final String ATLAS_PATH = "unpack/skeleton.atlas";
    private static final String OUTPUT_DIR = "unpacked";

    private TextureUnpacker unpacker;
    private TextureUnpackerOptions options;
    private ShapeRenderer shapes;
    private BitmapFont font;
    private Texture previewTexture;
    private TextureRegion previewRegion;
    private TextureUnpackerResult lastResult;
    private String status = "Ready";
    private String errorText = "";
    private boolean running;

    public TextureUnpackerScreen(BaseGame game) {
        super(game);
    }

    @Override
    protected void initData() {
        unpacker = new TextureUnpacker();
        options = new TextureUnpackerOptions();
        options.reversePremultipliedAlpha = true;
        options.restoreOriginalSize = true;
        options.exportNinePatch = true;
        options.overwrite = true;
        options.keepRegionFolders = true;
        options.atlasRotationIsClockwise = true;
        options.verbose = true;
    }

    @Override
    public void initView() {
        shapes = new ShapeRenderer();
        font = Asset.getAsset().loadBitFont("Ma-B_46.fnt");
        font.setColor(Color.WHITE);
        loadPreviewTexture();
        status = "Ready. Press R or Enter to unpack.";
        Gdx.app.log(TAG, "Texture Unpacker ready. atlas=" + ATLAS_PATH + ", output=" + OUTPUT_DIR);
    }

    @Override
    protected void initTouch() {
        super.initTouch();
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.R || keycode == Input.Keys.ENTER) {
                    startUnpack();
                    return true;
                }
                if (keycode == Input.Keys.P) {
                    options.reversePremultipliedAlpha = !options.reversePremultipliedAlpha;
                    status = "Reverse premultiplied alpha: " + options.reversePremultipliedAlpha;
                    return true;
                }
                if (keycode == Input.Keys.O) {
                    options.restoreOriginalSize = !options.restoreOriginalSize;
                    status = "Restore original size: " + options.restoreOriginalSize;
                    return true;
                }
                if (keycode == Input.Keys.N) {
                    options.exportNinePatch = !options.exportNinePatch;
                    status = "Export nine patch: " + options.exportNinePatch;
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        Gdx.gl.glClearColor(0.055f, 0.060f, 0.066f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapes.setProjectionMatrix(stage.getCamera().combined);
        game.getBatch().setProjectionMatrix(stage.getCamera().combined);

        drawPanel();
        drawText();
    }

    @Override
    protected void r() {
        startUnpack();
    }

    private void startUnpack() {
        if (running) {
            Gdx.app.log(TAG, "already running");
            return;
        }

        final FileHandle atlasFile = Gdx.files.internal(ATLAS_PATH);
        final FileHandle outputDir = Gdx.files.local(OUTPUT_DIR);

        running = true;
        lastResult = null;
        errorText = "";
        status = "Unpacking...";
        unpacker.unpackAsync(atlasFile, outputDir, options, new TextureUnpackerListener() {
            @Override
            public void onUnpackFinished(TextureUnpackerResult result) {
                running = false;
                lastResult = result;
                status = "Texture unpacking complete.";
                Gdx.app.log(TAG, "unpack complete, pages=" + result.pageCount
                        + ", regions=" + result.regionCount
                        + ", files=" + result.outputFiles.size
                        + ", elapsed=" + result.elapsedMillis + "ms"
                        + ", output=" + result.outputDir.path());
            }

            @Override
            public void onUnpackFailed(Throwable error) {
                running = false;
                errorText = error.getMessage() == null ? error.toString() : error.getMessage();
                status = "Error exporting.";
                Gdx.app.error(TAG, "unpack failed", error);
            }
        });
    }

    private void loadPreviewTexture() {
        FileHandle atlasFile = Gdx.files.internal(ATLAS_PATH);
        if (!atlasFile.exists()) {
            errorText = "Atlas file does not exist: " + ATLAS_PATH;
            return;
        }
        FileHandle pageFile = atlasFile.parent().child("jiesuan_huodong.png");
        if (!pageFile.exists()) {
            errorText = "Atlas page image does not exist: " + pageFile.path();
            return;
        }
        previewTexture = new Texture(pageFile);
        previewTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        previewRegion = new TextureRegion(previewTexture);
    }

    private void drawPanel() {
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(0.055f, 0.060f, 0.066f, 1.0f);
        shapes.rect(0, 0, width, height);
        shapes.setColor(0.090f, 0.098f, 0.106f, 1.0f);
        shapes.rect(32, height - 250, width - 64, 210);
        shapes.setColor(0.075f, 0.080f, 0.088f, 1.0f);
        shapes.rect(32, 48, width - 64, height - 330);
        shapes.end();
    }

    private void drawText() {
        Batch batch = game.getBatch();
        float width = stage.getViewport().getWorldWidth();
        float height = stage.getViewport().getWorldHeight();
        batch.begin();
        font.draw(batch, "Texture Unpacker", 52, height - 66);
        font.draw(batch, "Atlas file: " + ATLAS_PATH, 52, height - 108);
        font.draw(batch, "Output folder: " + Gdx.files.local(OUTPUT_DIR).path(), 52, height - 142);
        font.draw(batch, "R/Enter Unpack   P PMA " + onOff(options.reversePremultipliedAlpha)
                + "   O Original Size " + onOff(options.restoreOriginalSize)
                + "   N NinePatch " + onOff(options.exportNinePatch), 52, height - 176);
        font.draw(batch, "Status: " + status, 52, height - 214);
        if (errorText.length() > 0) {
            font.draw(batch, "Error: " + errorText, 52, height - 252);
        }

        if (previewRegion != null) {
            float maxW = Math.min(420, width - 104);
            float maxH = Math.max(160, height - 420);
            float scale = Math.min(maxW / previewRegion.getRegionWidth(), maxH / previewRegion.getRegionHeight());
            float drawW = previewRegion.getRegionWidth() * scale;
            float drawH = previewRegion.getRegionHeight() * scale;
            batch.draw(previewRegion, 52, 80, drawW, drawH);
            font.draw(batch, "Atlas page preview: " + previewRegion.getRegionWidth() + "x" + previewRegion.getRegionHeight(),
                    52, 70 + drawH);
        }

        if (lastResult != null) {
            float x = Math.min(520, width * 0.52f);
            float y = height - 330;
            font.draw(batch, "Result", x, y);
            font.draw(batch, "pages=" + lastResult.pageCount + " regions=" + lastResult.regionCount
                    + " files=" + lastResult.outputFiles.size + " elapsed=" + lastResult.elapsedMillis + "ms", x, y - 34);
            int count = Math.min(8, lastResult.outputFiles.size);
            for (int i = 0; i < count; i++) {
                font.draw(batch, lastResult.outputFiles.get(i).path(), x, y - 72 - i * 30);
            }
        }
        batch.end();
    }

    private static String onOff(boolean value) {
        return value ? "on" : "off";
    }

    @Override
    public void dispose() {
        if (previewTexture != null) previewTexture.dispose();
        if (shapes != null) shapes.dispose();
        if (font != null) font.dispose();
        if (unpacker != null) unpacker.dispose();
        super.dispose();
    }
}
