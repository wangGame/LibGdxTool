package com.kw.gdx.texturepacker;

/**
 * Clean-room settings modeled after SpinePackerSettings / LibGDX TexturePacker settings.
 *
 * The defaults intentionally mirror the observed Spine 3.8 settings where possible.
 */
public class TexturePackerOptions {
    public enum Packing {
        grid,
        rectangles,
        polygons
    }

    public enum Resampling {
        nearest,
        bilinear,
        bicubic
    }

    public boolean stripWhitespaceX = true;
    public boolean stripWhitespaceY = true;
    public boolean rotation = true;
    public boolean alias = true;
    public boolean ignoreBlankImages = false;
    public int alphaThreshold = 3;

    public int minWidth = 16;
    public int minHeight = 16;
    public int maxWidth = 2048;
    public int maxHeight = 2048;
    public boolean pot = false;
    public boolean multipleOfFour = false;
    public boolean square = false;

    /** "png", "jpg", or "jpeg". PNG is supported in pure LibGDX. JPEG needs a custom PageWriter. */
    public String outputFormat = "png";
    public float jpegQuality = 0.9f;

    public boolean premultiplyAlpha = true;
    public boolean bleed = false;
    public int bleedIterations = 2;

    public float[] scale = new float[]{1.0f};
    public String[] scaleSuffix = new String[]{""};
    public Resampling[] scaleResampling = new Resampling[]{Resampling.bicubic};

    public int paddingX = 2;
    public int paddingY = 2;
    public boolean edgePadding = true;
    public boolean duplicatePadding = false;

    /** Atlas text values, for example: Nearest, Linear, MipMapLinearLinear. */
    public String filterMin = "Linear";
    public String filterMag = "Linear";
    /** Atlas text values, for example: ClampToEdge, Repeat, MirroredRepeat. */
    public String wrapX = "ClampToEdge";
    public String wrapY = "ClampToEdge";
    /** Atlas text value, for example: RGBA8888. */
    public String format = "RGBA8888";

    public String atlasExtension = ".atlas";
    public boolean combineSubdirectories = false;
    public boolean flattenPaths = false;
    public boolean useIndexes = false;
    public boolean debug = false;
    /** true uses a fast shelf packer. false uses a better MaxRects-like packer. */
    public boolean fast = false;
    public boolean limitMemory = true;
    /** Present for settings parity. This clean-room module cannot inspect Spine project meshes. */
    public boolean currentProject = true;
    public Packing packing = Packing.rectangles;

    public boolean silent = false;
    public boolean overwrite = true;
    public boolean verbose = true;

    /** Atlas page base name. Output becomes pack.png / pack.atlas by default. */
    public String packFileName = "pack";

    /** If true, write one atlas for all scales. If false, each scale writes under its suffix directory/name. */
    public boolean writeAtlasPerScale = true;

    /** Optional custom writer. Required for JPEG in core/cross-platform builds. */
    public PageWriter pageWriter;

    public TexturePackerOptions copy() {
        TexturePackerOptions copy = new TexturePackerOptions();
        copy.stripWhitespaceX = stripWhitespaceX;
        copy.stripWhitespaceY = stripWhitespaceY;
        copy.rotation = rotation;
        copy.alias = alias;
        copy.ignoreBlankImages = ignoreBlankImages;
        copy.alphaThreshold = alphaThreshold;
        copy.minWidth = minWidth;
        copy.minHeight = minHeight;
        copy.maxWidth = maxWidth;
        copy.maxHeight = maxHeight;
        copy.pot = pot;
        copy.multipleOfFour = multipleOfFour;
        copy.square = square;
        copy.outputFormat = outputFormat;
        copy.jpegQuality = jpegQuality;
        copy.premultiplyAlpha = premultiplyAlpha;
        copy.bleed = bleed;
        copy.bleedIterations = bleedIterations;
        copy.scale = copy(scale);
        copy.scaleSuffix = copy(scaleSuffix);
        copy.scaleResampling = copy(scaleResampling);
        copy.paddingX = paddingX;
        copy.paddingY = paddingY;
        copy.edgePadding = edgePadding;
        copy.duplicatePadding = duplicatePadding;
        copy.filterMin = filterMin;
        copy.filterMag = filterMag;
        copy.wrapX = wrapX;
        copy.wrapY = wrapY;
        copy.format = format;
        copy.atlasExtension = atlasExtension;
        copy.combineSubdirectories = combineSubdirectories;
        copy.flattenPaths = flattenPaths;
        copy.useIndexes = useIndexes;
        copy.debug = debug;
        copy.fast = fast;
        copy.limitMemory = limitMemory;
        copy.currentProject = currentProject;
        copy.packing = packing;
        copy.silent = silent;
        copy.overwrite = overwrite;
        copy.verbose = verbose;
        copy.packFileName = packFileName;
        copy.writeAtlasPerScale = writeAtlasPerScale;
        copy.pageWriter = pageWriter;
        return copy;
    }

    public void validate() {
        if (minWidth <= 0 || minHeight <= 0) throw new IllegalArgumentException("min page size must be > 0");
        if (maxWidth < minWidth) throw new IllegalArgumentException("maxWidth < minWidth");
        if (maxHeight < minHeight) throw new IllegalArgumentException("maxHeight < minHeight");
        if (paddingX < 0 || paddingY < 0) throw new IllegalArgumentException("padding cannot be negative");
        if (alphaThreshold < 0 || alphaThreshold > 255) throw new IllegalArgumentException("alphaThreshold must be 0..255");
        if (scale == null || scale.length == 0) scale = new float[]{1f};
        if (scaleSuffix == null || scaleSuffix.length < scale.length) {
            String[] suffix = new String[scale.length];
            for (int i = 0; i < suffix.length; i++) suffix[i] = i == 0 ? "" : (scale[i] + "x");
            scaleSuffix = suffix;
        }
        if (scaleResampling == null || scaleResampling.length < scale.length) {
            Resampling[] resampling = new Resampling[scale.length];
            for (int i = 0; i < resampling.length; i++) resampling[i] = Resampling.bicubic;
            scaleResampling = resampling;
        }
        if (pot) {
            if (!isPowerOfTwo(maxWidth)) throw new IllegalArgumentException("If pot is true, maxWidth must be a power of two: " + maxWidth);
            if (!isPowerOfTwo(maxHeight)) throw new IllegalArgumentException("If pot is true, maxHeight must be a power of two: " + maxHeight);
        }
        if (multipleOfFour) {
            if (maxWidth % 4 != 0) throw new IllegalArgumentException("If multipleOfFour is true, maxWidth must be divisible by 4: " + maxWidth);
            if (maxHeight % 4 != 0) throw new IllegalArgumentException("If multipleOfFour is true, maxHeight must be divisible by 4: " + maxHeight);
        }
    }

    public String resolveScaleOutputName(String baseName, int scaleIndex) {
        String suffix = scaleSuffix != null && scaleIndex < scaleSuffix.length ? scaleSuffix[scaleIndex] : "";
        if (suffix != null && suffix.length() > 0) return baseName + suffix;
        if (scale != null && scale.length != 1) {
            float s = scale[scaleIndex];
            String scaleText = s == (int)s ? Integer.toString((int)s) : Float.toString(s);
            return scaleText + "/" + baseName;
        }
        return baseName;
    }

    public boolean wantsJpeg() {
        return "jpg".equalsIgnoreCase(outputFormat) || "jpeg".equalsIgnoreCase(outputFormat);
    }

    public String pageExtension() {
        return wantsJpeg() ? ".jpg" : ".png";
    }

    private static boolean isPowerOfTwo(int value) {
        return value > 0 && (value & (value - 1)) == 0;
    }

    private static float[] copy(float[] value) {
        float[] copy = new float[value.length];
        System.arraycopy(value, 0, copy, 0, value.length);
        return copy;
    }

    private static String[] copy(String[] value) {
        String[] copy = new String[value.length];
        System.arraycopy(value, 0, copy, 0, value.length);
        return copy;
    }

    private static Resampling[] copy(Resampling[] value) {
        Resampling[] copy = new Resampling[value.length];
        System.arraycopy(value, 0, copy, 0, value.length);
        return copy;
    }
}
