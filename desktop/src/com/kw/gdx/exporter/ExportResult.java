package com.kw.gdx.exporter;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class ExportResult {
    public final Array<FileHandle> outputFiles = new Array<FileHandle>();
    public final Array<String> warnings = new Array<String>();
    public long elapsedMillis;
}
