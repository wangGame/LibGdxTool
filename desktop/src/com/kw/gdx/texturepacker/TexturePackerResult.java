package com.kw.gdx.texturepacker;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class TexturePackerResult {
    public final FileHandle inputDir;
    public final FileHandle outputDir;
    public final Array<FileHandle> atlasFiles = new Array<FileHandle>();
    public final Array<FileHandle> pageFiles = new Array<FileHandle>();
    public final Array<String> warnings = new Array<String>();

    public int sourceImageCount;
    public int packedRegionCount;
    public int aliasRegionCount;
    public int ignoredBlankImageCount;
    public int pageCount;
    public long elapsedMillis;

    public TexturePackerResult(FileHandle inputDir, FileHandle outputDir) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
    }
}
