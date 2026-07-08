package com.kw.gdx.textureunpacker;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class TextureUnpackerResult {
    public final FileHandle atlasFile;
    public final FileHandle outputDir;
    public final Array<FileHandle> outputFiles = new Array<FileHandle>();
    public int pageCount;
    public int regionCount;
    public long elapsedMillis;

    public TextureUnpackerResult(FileHandle atlasFile, FileHandle outputDir) {
        this.atlasFile = atlasFile;
        this.outputDir = outputDir;
    }
}
