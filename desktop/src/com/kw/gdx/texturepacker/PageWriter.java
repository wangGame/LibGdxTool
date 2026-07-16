package com.kw.gdx.texturepacker;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

/**
 * Custom page writer hook.
 * Use this to add JPEG or project-specific compression.
 */
public interface PageWriter {
    void write(FileHandle file, Pixmap pixmap, TexturePackerOptions options) throws Exception;
}
