package com.kw.gdx.textureunpacker;

/**
 * Options for unpacking a LibGDX / Spine texture atlas back into individual PNG files.
 */
public class TextureUnpackerOptions {
    /**
     * Atlas pages exported with premultiplied alpha need this enabled to recover straight alpha PNGs.
     */
    public boolean reversePremultipliedAlpha = true;

    /**
     * Rebuild the original image canvas using atlas orig/offset fields.
     * When false, exports only the packed rectangle.
     */
    public boolean restoreOriginalSize = true;

    /**
     * If a region has split data, write it as .9.png with 1px black guide lines.
     */
    public boolean exportNinePatch = true;

    /**
     * If false, throws when an output file already exists.
     */
    public boolean overwrite = true;

    /**
     * Add _index to the filename when region index is not -1.
     */
    public boolean appendIndex = true;

    /**
     * Region names can contain slash paths. Keep them as folders when true.
     */
    public boolean keepRegionFolders = true;

    /**
     * Some atlas writers store rotated regions clockwise, which is what LibGDX/Spine packers normally do.
     * Leave true unless exported images look rotated the wrong way.
     */
    public boolean atlasRotationIsClockwise = true;

    /**
     * Log progress through Gdx.app.log.
     */
    public boolean verbose = true;
}
