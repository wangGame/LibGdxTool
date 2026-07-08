package com.kw.gdx.textureunpacker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Clean-room LibGDX texture atlas unpacker.
 *
 * Features:
 * 1) Reads common LibGDX/Spine .atlas text format.
 * 2) Cuts regions from atlas page PNGs.
 * 3) Restores rotated regions.
 * 4) Restores original canvas using orig/offset fields.
 * 5) Reverses premultiplied alpha when requested.
 * 6) Exports normal PNGs and .9.png files for split regions.
 */
public class TextureUnpacker {
    private static final String TAG = "TextureUnpacker";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void unpackAsync(final FileHandle atlasFile,
                            final FileHandle outputDir,
                            final TextureUnpackerOptions options,
                            final TextureUnpackerListener listener) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final TextureUnpackerResult result = unpack(atlasFile, outputDir, options);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) listener.onUnpackFinished(result);
                        }
                    });
                } catch (final Throwable error) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) listener.onUnpackFailed(error);
                        }
                    });
                }
            }
        });
    }

    public TextureUnpackerResult unpack(FileHandle atlasFile,
                                        FileHandle outputDir,
                                        TextureUnpackerOptions options) throws IOException {
        if (options == null) options = new TextureUnpackerOptions();
        if (atlasFile == null) throw new IllegalArgumentException("atlasFile cannot be null");
        if (outputDir == null) throw new IllegalArgumentException("outputDir cannot be null");
        if (!atlasFile.exists()) throw new IllegalArgumentException("Atlas file does not exist: " + atlasFile.path());
        if (atlasFile.isDirectory()) throw new IllegalArgumentException("Atlas path is a folder, not a file: " + atlasFile.path());

        long start = System.currentTimeMillis();
        outputDir.mkdirs();

        AtlasData atlas = AtlasData.parse(atlasFile);
        TextureUnpackerResult result = new TextureUnpackerResult(atlasFile, outputDir);
        result.pageCount = atlas.pages.size;
        result.regionCount = atlas.regions.size;

        ObjectMap<Page, Pixmap> pagePixmaps = new ObjectMap<Page, Pixmap>();
        try {
            for (int i = 0; i < atlas.pages.size; i++) {
                Page page = atlas.pages.get(i);
                if (!page.imageFile.exists()) {
                    throw new IOException("Atlas page image not found: " + page.imageFile.path());
                }
                Pixmap pagePixmap = new Pixmap(page.imageFile);
                if (options.reversePremultipliedAlpha || page.pma) {
                    reversePremultipliedAlpha(pagePixmap);
                }
                pagePixmaps.put(page, pagePixmap);
            }

            for (int i = 0; i < atlas.regions.size; i++) {
                Region region = atlas.regions.get(i);
                Pixmap pagePixmap = pagePixmaps.get(region.page);
                if (pagePixmap == null) {
                    throw new IOException("Missing pixmap for page: " + region.page.name);
                }

                Pixmap packed = cropRegion(pagePixmap, region, options.atlasRotationIsClockwise);
                Pixmap output = null;
                Pixmap ninePatch = null;
                try {
                    output = options.restoreOriginalSize ? restoreOriginalCanvas(packed, region) : copyPixmap(packed);

                    boolean isNinePatch = options.exportNinePatch && region.splits != null;
                    Pixmap finalPixmap = output;
                    String extension = ".png";
                    if (isNinePatch) {
                        ninePatch = buildNinePatch(output, region.splits, region.pads);
                        finalPixmap = ninePatch;
                        extension = ".9.png";
                    }

                    FileHandle out = outputDir.child(buildOutputPath(region, extension, options));
                    if (out.exists() && !options.overwrite) {
                        throw new IOException("Output file already exists: " + out.path());
                    }
                    out.parent().mkdirs();
                    PixmapIO.writePNG(out, finalPixmap);
                    result.outputFiles.add(out);

                    if (options.verbose) {
                        log("write " + out.path());
                    }
                } finally {
                    packed.dispose();
                    if (output != null) output.dispose();
                    if (ninePatch != null) ninePatch.dispose();
                }
            }
        } finally {
            for (Pixmap pixmap : pagePixmaps.values()) {
                pixmap.dispose();
            }
        }

        result.elapsedMillis = System.currentTimeMillis() - start;
        return result;
    }

    public void dispose() {
        executor.shutdownNow();
    }

    private static Pixmap cropRegion(Pixmap pagePixmap, Region region, boolean atlasRotationIsClockwise) {
        int cropWidth;
        int cropHeight;
        if (region.boundsFormat) {
            // Newer atlas files can use bounds:x,y,w,h. Here w/h are already the stored rectangle on the page.
            cropWidth = region.width;
            cropHeight = region.height;
        } else {
            // Older xy/size format stores unrotated region width/height. A rotated region occupies h*w on the page.
            cropWidth = region.rotateDegrees == 90 || region.rotateDegrees == 270 ? region.height : region.width;
            cropHeight = region.rotateDegrees == 90 || region.rotateDegrees == 270 ? region.width : region.height;
        }

        checkBounds(pagePixmap, region.x, region.y, cropWidth, cropHeight, region.name);

        Pixmap packed = new Pixmap(cropWidth, cropHeight, Pixmap.Format.RGBA8888);
        clear(packed);
        packed.drawPixmap(pagePixmap, 0, 0, region.x, region.y, cropWidth, cropHeight);

        if (region.rotateDegrees == 0) return packed;

        Pixmap restored;
        if (region.rotateDegrees == 90) {
            restored = atlasRotationIsClockwise ? rotateCounterClockwise(packed) : rotateClockwise(packed);
        } else if (region.rotateDegrees == 180) {
            restored = rotate180(packed);
        } else if (region.rotateDegrees == 270) {
            restored = atlasRotationIsClockwise ? rotateClockwise(packed) : rotateCounterClockwise(packed);
        } else {
            packed.dispose();
            throw new IllegalArgumentException("Unsupported rotate degrees: " + region.rotateDegrees + " for " + region.name);
        }
        packed.dispose();
        return restored;
    }

    private static Pixmap restoreOriginalCanvas(Pixmap packed, Region region) {
        int originalWidth = region.originalWidth > 0 ? region.originalWidth : packed.getWidth();
        int originalHeight = region.originalHeight > 0 ? region.originalHeight : packed.getHeight();

        if (originalWidth == packed.getWidth() && originalHeight == packed.getHeight()
                && region.offsetX == 0 && region.offsetY == 0) {
            return copyPixmap(packed);
        }

        Pixmap out = new Pixmap(originalWidth, originalHeight, Pixmap.Format.RGBA8888);
        clear(out);

        int drawX = region.offsetX;
        int drawY = originalHeight - packed.getHeight() - region.offsetY;
        out.drawPixmap(packed, drawX, drawY);
        return out;
    }

    private static Pixmap buildNinePatch(Pixmap content, int[] splits, int[] pads) {
        Pixmap out = new Pixmap(content.getWidth() + 2, content.getHeight() + 2, Pixmap.Format.RGBA8888);
        clear(out);
        out.drawPixmap(content, 1, 1);

        int left = splits[0];
        int right = splits[1];
        int top = splits[2];
        int bottom = splits[3];

        int stretchX0 = left + 1;
        int stretchX1 = content.getWidth() - right;
        int stretchY0 = top + 1;
        int stretchY1 = content.getHeight() - bottom;

        drawHorizontalLine(out, stretchX0, stretchX1, 0);
        drawVerticalLine(out, 0, stretchY0, stretchY1);

        if (pads != null) {
            int padLeft = pads[0];
            int padRight = pads[1];
            int padTop = pads[2];
            int padBottom = pads[3];
            int padX0 = padLeft + 1;
            int padX1 = content.getWidth() - padRight;
            int padY0 = padTop + 1;
            int padY1 = content.getHeight() - padBottom;
            drawHorizontalLine(out, padX0, padX1, out.getHeight() - 1);
            drawVerticalLine(out, out.getWidth() - 1, padY0, padY1);
        }

        return out;
    }

    private static Pixmap copyPixmap(Pixmap source) {
        Pixmap copy = new Pixmap(source.getWidth(), source.getHeight(), Pixmap.Format.RGBA8888);
        clear(copy);
        copy.drawPixmap(source, 0, 0);
        return copy;
    }

    private static Pixmap rotateCounterClockwise(Pixmap src) {
        Pixmap out = new Pixmap(src.getHeight(), src.getWidth(), Pixmap.Format.RGBA8888);
        clear(out);
        int destWidth = out.getWidth();
        int destHeight = out.getHeight();
        for (int y = 0; y < destHeight; y++) {
            for (int x = 0; x < destWidth; x++) {
                out.drawPixel(x, y, src.getPixel(src.getWidth() - 1 - y, x));
            }
        }
        return out;
    }

    private static Pixmap rotateClockwise(Pixmap src) {
        Pixmap out = new Pixmap(src.getHeight(), src.getWidth(), Pixmap.Format.RGBA8888);
        clear(out);
        int destWidth = out.getWidth();
        int destHeight = out.getHeight();
        for (int y = 0; y < destHeight; y++) {
            for (int x = 0; x < destWidth; x++) {
                out.drawPixel(x, y, src.getPixel(y, src.getHeight() - 1 - x));
            }
        }
        return out;
    }

    private static Pixmap rotate180(Pixmap src) {
        Pixmap out = new Pixmap(src.getWidth(), src.getHeight(), Pixmap.Format.RGBA8888);
        clear(out);
        int w = src.getWidth();
        int h = src.getHeight();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                out.drawPixel(x, y, src.getPixel(w - 1 - x, h - 1 - y));
            }
        }
        return out;
    }

    public static void reversePremultipliedAlpha(Pixmap pixmap) {
        int w = pixmap.getWidth();
        int h = pixmap.getHeight();
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgba = pixmap.getPixel(x, y);
                int r = rgba >>> 24 & 0xFF;
                int g = rgba >>> 16 & 0xFF;
                int b = rgba >>> 8 & 0xFF;
                int a = rgba & 0xFF;

                if (a > 0 && a < 255) {
                    float mul = 255f / a;
                    r = clamp(Math.round(r * mul));
                    g = clamp(Math.round(g * mul));
                    b = clamp(Math.round(b * mul));
                    pixmap.drawPixel(x, y, r << 24 | g << 16 | b << 8 | a);
                }
            }
        }
    }

    private static int clamp(int v) {
        return v < 0 ? 0 : (v > 255 ? 255 : v);
    }

    private static void clear(Pixmap pixmap) {
        Pixmap.Blending old = pixmap.getBlending();
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        pixmap.setBlending(old);
    }

    private static void drawHorizontalLine(Pixmap pixmap, int x0, int x1, int y) {
        x0 = Math.max(1, x0);
        x1 = Math.min(pixmap.getWidth() - 2, x1);
        if (x1 < x0) return;
        for (int x = x0; x <= x1; x++) pixmap.drawPixel(x, y, 0x000000FF);
    }

    private static void drawVerticalLine(Pixmap pixmap, int x, int y0, int y1) {
        y0 = Math.max(1, y0);
        y1 = Math.min(pixmap.getHeight() - 2, y1);
        if (y1 < y0) return;
        for (int y = y0; y <= y1; y++) pixmap.drawPixel(x, y, 0x000000FF);
    }

    private static void checkBounds(Pixmap pixmap, int x, int y, int w, int h, String name) {
        if (x < 0 || y < 0 || w <= 0 || h <= 0 || x + w > pixmap.getWidth() || y + h > pixmap.getHeight()) {
            throw new IllegalArgumentException("Region out of page bounds: " + name
                    + " x=" + x + " y=" + y + " w=" + w + " h=" + h
                    + " page=" + pixmap.getWidth() + "x" + pixmap.getHeight());
        }
    }

    private static String buildOutputPath(Region region, String extension, TextureUnpackerOptions options) {
        String name = region.name;
        if (!options.keepRegionFolders) {
            name = name.replace('\\', '_').replace('/', '_');
        }
        if (options.appendIndex && region.index != -1) {
            name += "_" + region.index;
        }
        return name + extension;
    }

    private static void log(String message) {
        if (Gdx.app != null) Gdx.app.log(TAG, message);
        else System.out.println(TAG + ": " + message);
    }

    private static void post(Runnable runnable) {
        if (Gdx.app != null) Gdx.app.postRunnable(runnable);
        else runnable.run();
    }

    private static int[] parseInts(String value) {
        String[] parts = value.split(",");
        int[] out = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            out[i] = Integer.parseInt(parts[i].trim());
        }
        return out;
    }

    private static boolean parseBoolean(String value) {
        return "true".equalsIgnoreCase(value.trim());
    }

    private static int parseRotate(String value) {
        value = value.trim();
        if ("true".equalsIgnoreCase(value)) return 90;
        if ("false".equalsIgnoreCase(value)) return 0;
        return Integer.parseInt(value);
    }

    private static class AtlasData {
        final Array<Page> pages = new Array<Page>();
        final Array<Region> regions = new Array<Region>();

        static AtlasData parse(FileHandle atlasFile) throws IOException {
            AtlasData data = new AtlasData();
            BufferedReader reader = atlasFile.reader(256);
            Page currentPage = null;
            Region currentRegion = null;
            boolean expectPage = true;

            try {
                String raw;
                while ((raw = reader.readLine()) != null) {
                    String line = raw.trim();
                    if (line.length() == 0) {
                        currentRegion = null;
                        currentPage = null;
                        expectPage = true;
                        continue;
                    }

                    int colon = line.indexOf(':');
                    if (colon == -1) {
                        if (expectPage || currentPage == null) {
                            currentPage = new Page(line, atlasFile.parent().child(line));
                            data.pages.add(currentPage);
                            currentRegion = null;
                            expectPage = false;
                        } else {
                            currentRegion = new Region(currentPage, line);
                            data.regions.add(currentRegion);
                        }
                        continue;
                    }

                    String key = line.substring(0, colon).trim();
                    String value = line.substring(colon + 1).trim();
                    if (currentRegion != null) parseRegionField(currentRegion, key, value);
                    else if (currentPage != null) parsePageField(currentPage, key, value);
                }
            } finally {
                reader.close();
            }

            for (int i = 0; i < data.regions.size; i++) {
                Region r = data.regions.get(i);
                if (r.originalWidth <= 0) r.originalWidth = r.width;
                if (r.originalHeight <= 0) r.originalHeight = r.height;
            }
            return data;
        }

        private static void parsePageField(Page page, String key, String value) {
            if ("size".equals(key)) {
                int[] v = parseInts(value);
                if (v.length >= 2) {
                    page.width = v[0];
                    page.height = v[1];
                }
            } else if ("pma".equals(key)) {
                page.pma = parseBoolean(value);
            }
        }

        private static void parseRegionField(Region region, String key, String value) {
            if ("rotate".equals(key)) {
                region.rotateDegrees = parseRotate(value);
            } else if ("xy".equals(key)) {
                int[] v = parseInts(value);
                region.x = v[0];
                region.y = v[1];
            } else if ("size".equals(key)) {
                int[] v = parseInts(value);
                region.width = v[0];
                region.height = v[1];
            } else if ("bounds".equals(key)) {
                int[] v = parseInts(value);
                region.x = v[0];
                region.y = v[1];
                region.width = v[2];
                region.height = v[3];
                region.boundsFormat = true;
            } else if ("orig".equals(key) || "original".equals(key)) {
                int[] v = parseInts(value);
                region.originalWidth = v[0];
                region.originalHeight = v[1];
            } else if ("offset".equals(key)) {
                int[] v = parseInts(value);
                region.offsetX = v[0];
                region.offsetY = v[1];
            } else if ("offsets".equals(key)) {
                int[] v = parseInts(value);
                region.offsetX = v[0];
                region.offsetY = v[1];
                region.originalWidth = v[2];
                region.originalHeight = v[3];
            } else if ("split".equals(key) || "splits".equals(key)) {
                int[] v = parseInts(value);
                if (v.length >= 4) region.splits = new int[]{v[0], v[1], v[2], v[3]};
            } else if ("pad".equals(key) || "pads".equals(key)) {
                int[] v = parseInts(value);
                if (v.length >= 4) region.pads = new int[]{v[0], v[1], v[2], v[3]};
            } else if ("index".equals(key)) {
                region.index = Integer.parseInt(value.trim());
            }
        }
    }

    private static class Page {
        final String name;
        final FileHandle imageFile;
        int width;
        int height;
        boolean pma;

        Page(String name, FileHandle imageFile) {
            this.name = name;
            this.imageFile = imageFile;
        }
    }

    private static class Region {
        final Page page;
        final String name;
        int index = -1;
        int x;
        int y;
        int width;
        int height;
        int originalWidth;
        int originalHeight;
        int offsetX;
        int offsetY;
        int rotateDegrees;
        boolean boundsFormat;
        int[] splits;
        int[] pads;

        Region(Page page, String name) {
            this.page = page;
            this.name = name;
        }
    }
}
