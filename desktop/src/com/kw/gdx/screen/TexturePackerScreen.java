package com.kw.gdx.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.kw.gdx.BaseGame;
import com.kw.gdx.texturepacker.TexturePacker;
import com.kw.gdx.texturepacker.TexturePackerListener;
import com.kw.gdx.texturepacker.TexturePackerOptions;
import com.kw.gdx.texturepacker.TexturePackerResult;

/**
 * Minimal BaseScreen wrapper for Texture Packer.
 *
 * Put input images under assets/pack/input/.
 * Output is written to Gdx.files.local("packed").
 */
public class TexturePackerScreen extends BaseScreen {
    private static final String TAG = "TexturePackerScreen";

    private static final String INPUT_DIR = "E:\\work\\codex\\canyGame\\canyLibgdx\\assets\\pack\\input";
    private static final String OUTPUT_DIR = "packed";

    private TexturePacker packer;
    private boolean running;
    private TexturePackerResult lastResult;

    public TexturePackerScreen(BaseGame game) {
        super(game);
    }

    @Override
    protected void initData() {
        packer = new TexturePacker();
    }

    @Override
    public void initView() {
        Gdx.app.log(TAG, "Texture Packer ready. Press R to pack. input=" + INPUT_DIR + ", output=" + OUTPUT_DIR);
        startPack();
    }

    @Override
    protected void initTouch() {
        super.initTouch();
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.R) {
                    startPack();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void r() {
        startPack();
    }

    private void startPack() {
        if (running) {
            Gdx.app.log(TAG, "already running");
            return;
        }

        FileHandle inputDir = Gdx.files.internal(INPUT_DIR);
        FileHandle outputDir = Gdx.files.local(OUTPUT_DIR);

        TexturePackerOptions options = new TexturePackerOptions();
        options.packFileName = "game";
        options.stripWhitespaceX = true;
        options.stripWhitespaceY = true;
        options.rotation = true;
        options.alias = true;
        options.ignoreBlankImages = true;
        options.alphaThreshold = 3;
        options.minWidth = 16;
        options.minHeight = 16;
        options.maxWidth = 2048;
        options.maxHeight = 2048;
        options.pot = false;
        options.multipleOfFour = false;
        options.square = false;
        options.outputFormat = "png";
        options.premultiplyAlpha = true;
        options.bleed = true;
        options.paddingX = 2;
        options.paddingY = 2;
        options.edgePadding = true;
        options.duplicatePadding = true;
        options.combineSubdirectories = true;
        options.flattenPaths = false;
        options.useIndexes = true;
        options.debug = true;
        options.fast = false;
        options.packing = TexturePackerOptions.Packing.polygons;
        options.scale = new float[]{1f};
        options.scaleSuffix = new String[]{""};
        options.scaleResampling = new TexturePackerOptions.Resampling[]{TexturePackerOptions.Resampling.bicubic};

        running = true;
        packer.packAsync(inputDir, outputDir, options, new TexturePackerListener() {
            @Override
            public void onPackFinished(TexturePackerResult result) {
                running = false;
                lastResult = result;
                Gdx.app.log(TAG, "pack finished, source=" + result.sourceImageCount
                        + ", regions=" + result.packedRegionCount
                        + ", aliases=" + result.aliasRegionCount
                        + ", pages=" + result.pageCount
                        + ", atlas=" + result.atlasFiles.size
                        + ", time=" + result.elapsedMillis + "ms");
                for (int i = 0; i < result.warnings.size; i++) {
                    Gdx.app.log(TAG, "warning: " + result.warnings.get(i));
                }
            }

            @Override
            public void onPackFailed(Throwable error) {
                running = false;
                Gdx.app.error(TAG, "pack failed", error);
            }
        });
    }

    @Override
    public void dispose() {
        if (packer != null) packer.dispose();
        super.dispose();
    }

    public TexturePackerResult getLastResult() {
        return lastResult;
    }
}
