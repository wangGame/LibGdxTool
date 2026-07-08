package com.kw.gdx.texturepacker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.Array;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Pure LibGDX texture packer.
 *
 * Implemented for editor/runtime tooling where gdx-tools cannot be used directly.
 * It writes common LibGDX/Spine atlas text and PNG pages through PixmapIO.
 */
public class TexturePacker {
    private static final String TAG = "TexturePacker";
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void packAsync(final FileHandle inputDir,
                          final FileHandle outputDir,
                          final TexturePackerOptions options,
                          final TexturePackerListener listener) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final TexturePackerResult result = pack(inputDir, outputDir, options);
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) listener.onPackFinished(result);
                        }
                    });
                } catch (final Throwable error) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) listener.onPackFailed(error);
                        }
                    });
                }
            }
        });
    }

    public TexturePackerResult pack(FileHandle inputDir,
                                    FileHandle outputDir,
                                    TexturePackerOptions options) throws Exception {
        if (options == null) options = new TexturePackerOptions();
        options.validate();
        if (inputDir == null) throw new IllegalArgumentException("inputDir cannot be null");
        if (outputDir == null) throw new IllegalArgumentException("outputDir cannot be null");
        if (!inputDir.exists()) throw new IllegalArgumentException("Input folder does not exist: " + inputDir.path());
        if (!inputDir.isDirectory()) throw new IllegalArgumentException("Input path is not a folder: " + inputDir.path());

        long start = System.currentTimeMillis();
        outputDir.mkdirs();

        TexturePackerResult result = new TexturePackerResult(inputDir, outputDir);
        Array<SourceImage> sources = scanImages(inputDir, options);
        result.sourceImageCount = sources.size;

        if (sources.size == 0) {
            result.warnings.add("No input images found in " + inputDir.path());
            result.elapsedMillis = System.currentTimeMillis() - start;
            return result;
        }

        for (int scaleIndex = 0; scaleIndex < options.scale.length; scaleIndex++) {
            float scale = options.scale[scaleIndex];
            TexturePackerOptions.Resampling resampling = options.scaleResampling[Math.min(scaleIndex, options.scaleResampling.length - 1)];
            String packName = options.resolveScaleOutputName(options.packFileName, scaleIndex);
            FileHandle scaleOutputDir = outputDir;
            String scalePackName = packName;
            int slash = packName.lastIndexOf('/');
            if (slash >= 0) {
                scaleOutputDir = outputDir.child(packName.substring(0, slash));
                scalePackName = packName.substring(slash + 1);
            }
            scaleOutputDir.mkdirs();

            Array<ImageItem> items = prepareImages(sources, inputDir, scale, resampling, options, result);
            if (items.size == 0) continue;

            Array<Page> pages;
            if (options.packing == TexturePackerOptions.Packing.grid) {
                pages = packGrid(items, options);
            } else if (options.fast) {
                pages = packShelf(items, options);
            } else {
                pages = packRectangles(items, options);
            }

            writePagesAndAtlas(pages, scaleOutputDir, scalePackName, options, result);
            disposeItems(items);
        }

        result.elapsedMillis = System.currentTimeMillis() - start;
        if (options.verbose && !options.silent) {
            Gdx.app.log(TAG, "packed images=" + result.packedRegionCount + ", aliases=" + result.aliasRegionCount
                    + ", pages=" + result.pageCount + ", time=" + result.elapsedMillis + "ms");
        }
        return result;
    }

    public void dispose() {
        executor.shutdownNow();
    }

    private static void post(Runnable runnable) {
        if (Gdx.app != null) Gdx.app.postRunnable(runnable);
        else runnable.run();
    }

    private Array<SourceImage> scanImages(FileHandle inputDir, TexturePackerOptions options) {
        Array<SourceImage> out = new Array<SourceImage>();
        scanImagesRecursive(inputDir, inputDir, options, out);
        return out;
    }

    private void scanImagesRecursive(FileHandle root, FileHandle dir, TexturePackerOptions options, Array<SourceImage> out) {
        FileHandle[] children = dir.list();
        for (int i = 0; i < children.length; i++) {
            FileHandle child = children[i];
            if (child.isDirectory()) {
                if (options.combineSubdirectories) scanImagesRecursive(root, child, options, out);
                continue;
            }
            String ext = child.extension().toLowerCase();
            if (!"png".equals(ext) && !"jpg".equals(ext) && !"jpeg".equals(ext)) continue;
            String relative = child.path().replace('\\', '/');
            String rootPath = root.path().replace('\\', '/');
            if (relative.startsWith(rootPath)) {
                relative = relative.substring(rootPath.length());
                if (relative.startsWith("/")) relative = relative.substring(1);
            } else {
                relative = child.name();
            }
            String name = removeExtension(relative);
            if (options.flattenPaths) name = name.replace('/', '_').replace('\\', '_');
            IndexName indexName = parseIndex(name, options.useIndexes);
            out.add(new SourceImage(child, indexName.name, indexName.index));
        }
    }

    private Array<ImageItem> prepareImages(Array<SourceImage> sources,
                                           FileHandle inputRoot,
                                           float scale,
                                           TexturePackerOptions.Resampling resampling,
                                           TexturePackerOptions options,
                                           TexturePackerResult result) {
        Array<ImageItem> items = new Array<ImageItem>();
        Map<String, ImageItem> aliasMap = new HashMap<String, ImageItem>();

        for (int i = 0; i < sources.size; i++) {
            SourceImage source = sources.get(i);
            Pixmap original = null;
            Pixmap scaled = null;
            Pixmap trimmed = null;
            try {
                original = new Pixmap(source.file);
                scaled = scale == 1f ? original : resize(original, Math.max(1, Math.round(original.getWidth() * scale)),
                        Math.max(1, Math.round(original.getHeight() * scale)), resampling);
                if (scaled != original) original.dispose();
                original = null;

                Trim trim = trim(scaled, options.stripWhitespaceX, options.stripWhitespaceY, options.alphaThreshold);
                if (trim.empty) {
                    if (options.ignoreBlankImages) {
                        result.ignoredBlankImageCount++;
                        scaled.dispose();
                        continue;
                    }
                    trim.x = 0;
                    trim.y = 0;
                    trim.width = 1;
                    trim.height = 1;
                }
                trimmed = crop(scaled, trim.x, trim.y, trim.width, trim.height);
                ImageItem item = new ImageItem();
                item.source = source;
                item.name = source.name;
                item.index = source.index;
                item.pixmap = trimmed;
                item.originalWidth = scaled.getWidth();
                item.originalHeight = scaled.getHeight();
                item.trimX = trim.x;
                item.trimY = trim.y;
                item.trimmedWidth = trimmed.getWidth();
                item.trimmedHeight = trimmed.getHeight();
                item.width = item.trimmedWidth;
                item.height = item.trimmedHeight;
                if (options.packing == TexturePackerOptions.Packing.polygons) {
                    item.polygonVertices = buildSimplePolygon(item.pixmap, options.alphaThreshold);
                }

                if (options.alias) {
                    String hash = hash(item.pixmap);
                    ImageItem master = aliasMap.get(hash);
                    if (master != null && pixmapsEqual(master.pixmap, item.pixmap)) {
                        item.aliasOf = master;
                        item.pixmap = null;
                        result.aliasRegionCount++;
                    } else {
                        aliasMap.put(hash, item);
                    }
                }
                items.add(item);
                result.packedRegionCount++;
                scaled.dispose();
            } catch (Throwable error) {
                result.warnings.add("Skipped image: " + source.file.path() + " error=" + error.getMessage());
                if (original != null) original.dispose();
                if (scaled != null && scaled != original) scaled.dispose();
                if (trimmed != null) trimmed.dispose();
            }
        }
        return items;
    }

    private Array<Page> packRectangles(Array<ImageItem> allItems, TexturePackerOptions options) {
        Array<ImageItem> remaining = realItemsSorted(allItems);
        Array<Page> pages = new Array<Page>();

        while (remaining.size > 0) {
            Page page = new Page();
            page.width = options.maxWidth;
            page.height = options.maxHeight;
            MaxRects maxRects = new MaxRects(edgeStart(options, true), edgeStart(options, false),
                    options.maxWidth - edgeStart(options, true), options.maxHeight - edgeStart(options, false));
            Array<ImageItem> notPacked = new Array<ImageItem>();

            for (int i = 0; i < remaining.size; i++) {
                ImageItem item = remaining.get(i);
                Placed placed = maxRects.insert(item, options);
                if (placed == null) {
                    notPacked.add(item);
                    continue;
                }
                item.page = page;
                item.x = placed.x;
                item.y = placed.y;
                item.rotated = placed.rotated;
                item.packedWidth = item.rotated ? item.height : item.width;
                item.packedHeight = item.rotated ? item.width : item.height;
                page.items.add(item);
            }

            if (page.items.size == 0) {
                ImageItem tooLarge = remaining.first();
                throw new RuntimeException("Image does not fit max page size: " + tooLarge.name + " "
                        + tooLarge.width + "x" + tooLarge.height + ", max=" + options.maxWidth + "x" + options.maxHeight);
            }
            shrinkPage(page, options);
            pages.add(page);
            remaining = notPacked;
        }
        addAliasesToPages(allItems);
        return pages;
    }

    private Array<Page> packShelf(Array<ImageItem> allItems, TexturePackerOptions options) {
        Array<ImageItem> real = realItemsSorted(allItems);
        Array<Page> pages = new Array<Page>();
        Page page = new Page();
        page.width = options.maxWidth;
        page.height = options.maxHeight;
        int x = edgeStart(options, true);
        int y = edgeStart(options, false);
        int shelfHeight = 0;

        for (int i = 0; i < real.size; i++) {
            ImageItem item = real.get(i);
            int w = item.width;
            int h = item.height;
            boolean rotated = false;
            if (options.rotation && h + options.paddingX <= options.maxWidth && w + options.paddingY <= options.maxHeight && h < w) {
                rotated = true;
                w = item.height;
                h = item.width;
            }
            int outerW = w + options.paddingX;
            int outerH = h + options.paddingY;
            if (x + outerW > options.maxWidth) {
                x = edgeStart(options, true);
                y += shelfHeight;
                shelfHeight = 0;
            }
            if (y + outerH > options.maxHeight) {
                shrinkPage(page, options);
                pages.add(page);
                page = new Page();
                page.width = options.maxWidth;
                page.height = options.maxHeight;
                x = edgeStart(options, true);
                y = edgeStart(options, false);
                shelfHeight = 0;
            }
            if (x + outerW > options.maxWidth || y + outerH > options.maxHeight) {
                throw new RuntimeException("Image does not fit max page size: " + item.name);
            }
            item.page = page;
            item.x = x;
            item.y = y;
            item.rotated = rotated;
            item.packedWidth = w;
            item.packedHeight = h;
            page.items.add(item);
            x += outerW;
            if (outerH > shelfHeight) shelfHeight = outerH;
        }
        if (page.items.size > 0) {
            shrinkPage(page, options);
            pages.add(page);
        }
        addAliasesToPages(allItems);
        return pages;
    }

    private Array<Page> packGrid(Array<ImageItem> allItems, TexturePackerOptions options) {
        Array<ImageItem> real = realItemsSorted(allItems);
        int cellW = 1;
        int cellH = 1;
        for (int i = 0; i < real.size; i++) {
            ImageItem item = real.get(i);
            cellW = Math.max(cellW, item.width);
            cellH = Math.max(cellH, item.height);
        }
        cellW += options.paddingX;
        cellH += options.paddingY;
        int startX = edgeStart(options, true);
        int startY = edgeStart(options, false);
        int cols = Math.max(1, (options.maxWidth - startX) / cellW);
        int rows = Math.max(1, (options.maxHeight - startY) / cellH);
        int perPage = cols * rows;
        if (perPage <= 0) throw new RuntimeException("Grid cell is larger than max page size.");

        Array<Page> pages = new Array<Page>();
        Page page = null;
        for (int i = 0; i < real.size; i++) {
            if (i % perPage == 0) {
                if (page != null) {
                    shrinkPage(page, options);
                    pages.add(page);
                }
                page = new Page();
                page.width = options.maxWidth;
                page.height = options.maxHeight;
            }
            int local = i % perPage;
            int col = local % cols;
            int row = local / cols;
            ImageItem item = real.get(i);
            item.page = page;
            item.x = startX + col * cellW;
            item.y = startY + row * cellH;
            item.rotated = false;
            item.packedWidth = item.width;
            item.packedHeight = item.height;
            page.items.add(item);
        }
        if (page != null && page.items.size > 0) {
            shrinkPage(page, options);
            pages.add(page);
        }
        addAliasesToPages(allItems);
        return pages;
    }

    private void addAliasesToPages(Array<ImageItem> allItems) {
        for (int i = 0; i < allItems.size; i++) {
            ImageItem alias = allItems.get(i);
            if (alias.aliasOf == null) continue;
            ImageItem master = alias.aliasOf;
            alias.page = master.page;
            alias.x = master.x;
            alias.y = master.y;
            alias.rotated = master.rotated;
            alias.packedWidth = master.packedWidth;
            alias.packedHeight = master.packedHeight;
            if (!master.page.items.contains(alias, true)) master.page.items.add(alias);
        }
    }

    private Array<ImageItem> realItemsSorted(Array<ImageItem> allItems) {
        ArrayList<ImageItem> list = new ArrayList<ImageItem>();
        for (int i = 0; i < allItems.size; i++) {
            ImageItem item = allItems.get(i);
            if (item.aliasOf == null) list.add(item);
        }
        Collections.sort(list, new Comparator<ImageItem>() {
            @Override
            public int compare(ImageItem a, ImageItem b) {
                int aa = a.width * a.height;
                int bb = b.width * b.height;
                if (aa != bb) return bb - aa;
                if (a.height != b.height) return b.height - a.height;
                return a.name.compareTo(b.name);
            }
        });
        Array<ImageItem> out = new Array<ImageItem>();
        for (int i = 0; i < list.size(); i++) out.add(list.get(i));
        return out;
    }

    private void shrinkPage(Page page, TexturePackerOptions options) {
        int usedW = 0;
        int usedH = 0;
        for (int i = 0; i < page.items.size; i++) {
            ImageItem item = page.items.get(i);
            if (item.aliasOf != null) continue;
            usedW = Math.max(usedW, item.x + item.packedWidth + (options.edgePadding ? options.paddingX : 0));
            usedH = Math.max(usedH, item.y + item.packedHeight + (options.edgePadding ? options.paddingY : 0));
        }
        page.width = clampPageSize(usedW, options.minWidth, options.maxWidth, options);
        page.height = clampPageSize(usedH, options.minHeight, options.maxHeight, options);
        if (options.square) {
            int m = Math.max(page.width, page.height);
            page.width = m;
            page.height = m;
        }
    }

    private int clampPageSize(int used, int min, int max, TexturePackerOptions options) {
        int value = Math.max(min, Math.min(max, used));
        if (options.pot) value = nextPowerOfTwo(value);
        if (options.multipleOfFour) value = ((value + 3) / 4) * 4;
        return Math.min(max, value);
    }

    private void writePagesAndAtlas(Array<Page> pages,
                                    FileHandle outputDir,
                                    String packName,
                                    TexturePackerOptions options,
                                    TexturePackerResult result) throws Exception {
        String imageExt = options.pageExtension();
        for (int pageIndex = 0; pageIndex < pages.size; pageIndex++) {
            Page page = pages.get(pageIndex);
            page.name = packName + (pages.size == 1 ? "" : pageIndex) + imageExt;
            Pixmap pagePixmap = new Pixmap(page.width, page.height, Pixmap.Format.RGBA8888);
            pagePixmap.setColor(0, 0, 0, 0);
            pagePixmap.fill();

            for (int i = 0; i < page.items.size; i++) {
                ImageItem item = page.items.get(i);
                if (item.aliasOf != null) continue;
                drawItem(pagePixmap, item);
                if (options.duplicatePadding) duplicatePadding(pagePixmap, item, options);
                if (options.debug) drawDebug(pagePixmap, item);
            }
            if (options.bleed) bleed(pagePixmap, Math.max(1, options.bleedIterations));
            if (options.premultiplyAlpha) premultiplyAlpha(pagePixmap);

            FileHandle pageFile = outputDir.child(page.name);
            if (!options.overwrite && pageFile.exists()) throw new IOException("Output already exists: " + pageFile.path());
            writePage(pageFile, pagePixmap, options);
            result.pageFiles.add(pageFile);
            result.pageCount++;
            pagePixmap.dispose();
        }

        FileHandle atlasFile = outputDir.child(packName + options.atlasExtension);
        if (!options.overwrite && atlasFile.exists()) throw new IOException("Output already exists: " + atlasFile.path());
        writeAtlas(atlasFile, pages, options);
        result.atlasFiles.add(atlasFile);
    }

    private void writePage(FileHandle pageFile, Pixmap pixmap, TexturePackerOptions options) throws Exception {
        if (options.pageWriter != null) {
            options.pageWriter.write(pageFile, pixmap, options);
            return;
        }
        if (options.wantsJpeg()) {
            throw new UnsupportedOperationException("JPEG output needs options.pageWriter. Use PNG in core, or add the desktop ImageIO writer from this package.");
        }
        PixmapIO.writePNG(pageFile, pixmap);
    }

    private void writeAtlas(FileHandle atlasFile, Array<Page> pages, TexturePackerOptions options) throws IOException {
        Writer writer = atlasFile.writer(false, "UTF-8");
        try {
            for (int p = 0; p < pages.size; p++) {
                Page page = pages.get(p);
                writer.write(page.name + "\n");
                writer.write("size: " + page.width + "," + page.height + "\n");
                writer.write("format: " + options.format + "\n");
                writer.write("filter: " + options.filterMin + "," + options.filterMag + "\n");
                writer.write("repeat: " + repeatValue(options.wrapX, options.wrapY) + "\n");
                if (options.premultiplyAlpha) writer.write("pma: true\n");
                writer.write("\n");

                Array<ImageItem> sorted = sortedPageItems(page.items);
                for (int i = 0; i < sorted.size; i++) {
                    ImageItem item = sorted.get(i);
                    writer.write(item.name + "\n");
                    writer.write("  rotate: " + item.rotated + "\n");
                    writer.write("  xy: " + item.x + ", " + item.y + "\n");
                    writer.write("  size: " + item.packedWidth + ", " + item.packedHeight + "\n");
                    writer.write("  orig: " + item.originalWidth + ", " + item.originalHeight + "\n");
                    int offsetX = item.trimX;
                    int offsetY = item.originalHeight - item.trimY - item.trimmedHeight;
                    writer.write("  offset: " + offsetX + ", " + offsetY + "\n");
                    writer.write("  index: " + item.index + "\n");
                    if (options.packing == TexturePackerOptions.Packing.polygons && item.polygonVertices != null) {
                        writer.write("  vertices: " + item.polygonVertices + "\n");
                    }
                }
            }
        } finally {
            writer.close();
        }
    }

    private Array<ImageItem> sortedPageItems(Array<ImageItem> in) {
        ArrayList<ImageItem> list = new ArrayList<ImageItem>();
        for (int i = 0; i < in.size; i++) list.add(in.get(i));
        Collections.sort(list, new Comparator<ImageItem>() {
            @Override
            public int compare(ImageItem a, ImageItem b) {
                int c = a.name.compareTo(b.name);
                if (c != 0) return c;
                return a.index - b.index;
            }
        });
        Array<ImageItem> out = new Array<ImageItem>();
        for (int i = 0; i < list.size(); i++) out.add(list.get(i));
        return out;
    }

    private static String repeatValue(String wrapX, String wrapY) {
        boolean rx = isRepeat(wrapX);
        boolean ry = isRepeat(wrapY);
        if (rx && ry) return "xy";
        if (rx) return "x";
        if (ry) return "y";
        return "none";
    }

    private static boolean isRepeat(String wrap) {
        return wrap != null && wrap.toLowerCase().contains("repeat") && !wrap.toLowerCase().contains("clamp");
    }

    private void drawItem(Pixmap page, ImageItem item) {
        Pixmap src = item.pixmap;
        if (!item.rotated) {
            page.drawPixmap(src, item.x, item.y);
        } else {
            for (int sy = 0; sy < src.getHeight(); sy++) {
                for (int sx = 0; sx < src.getWidth(); sx++) {
                    int color = src.getPixel(sx, sy);
                    int dx = item.x + src.getHeight() - 1 - sy;
                    int dy = item.y + sx;
                    page.drawPixel(dx, dy, color);
                }
            }
        }
    }

    private void duplicatePadding(Pixmap page, ImageItem item, TexturePackerOptions options) {
        int padX = options.paddingX;
        int padY = options.paddingY;
        if (padX <= 0 && padY <= 0) return;
        int x = item.x;
        int y = item.y;
        int w = item.packedWidth;
        int h = item.packedHeight;
        for (int i = 1; i <= padX; i++) {
            int left = x - i;
            int right = x + w - 1 + i;
            if (left >= 0) copyVertical(page, x, y, left, y, h);
            if (right < page.getWidth()) copyVertical(page, x + w - 1, y, right, y, h);
        }
        for (int i = 1; i <= padY; i++) {
            int top = y - i;
            int bottom = y + h - 1 + i;
            if (top >= 0) copyHorizontal(page, x, y, x, top, w);
            if (bottom < page.getHeight()) copyHorizontal(page, x, y + h - 1, x, bottom, w);
        }
        // Corners.
        for (int yy = y - padY; yy < y; yy++) {
            for (int xx = x - padX; xx < x; xx++) safeDraw(page, xx, yy, page.getPixel(x, y));
            for (int xx = x + w; xx < x + w + padX; xx++) safeDraw(page, xx, yy, page.getPixel(x + w - 1, y));
        }
        for (int yy = y + h; yy < y + h + padY; yy++) {
            for (int xx = x - padX; xx < x; xx++) safeDraw(page, xx, yy, page.getPixel(x, y + h - 1));
            for (int xx = x + w; xx < x + w + padX; xx++) safeDraw(page, xx, yy, page.getPixel(x + w - 1, y + h - 1));
        }
    }

    private static void copyVertical(Pixmap p, int sx, int sy, int dx, int dy, int h) {
        for (int i = 0; i < h; i++) safeDraw(p, dx, dy + i, p.getPixel(sx, sy + i));
    }

    private static void copyHorizontal(Pixmap p, int sx, int sy, int dx, int dy, int w) {
        for (int i = 0; i < w; i++) safeDraw(p, dx + i, dy, p.getPixel(sx + i, sy));
    }

    private static void safeDraw(Pixmap p, int x, int y, int color) {
        if (x >= 0 && y >= 0 && x < p.getWidth() && y < p.getHeight()) p.drawPixel(x, y, color);
    }

    private void drawDebug(Pixmap page, ImageItem item) {
        page.setColor(Color.RED);
        page.drawRectangle(item.x, item.y, item.packedWidth, item.packedHeight);
    }

    private void bleed(Pixmap pixmap, int iterations) {
        for (int it = 0; it < iterations; it++) {
            Pixmap copy = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
            copy.drawPixmap(pixmap, 0, 0);
            for (int y = 0; y < pixmap.getHeight(); y++) {
                for (int x = 0; x < pixmap.getWidth(); x++) {
                    int c = copy.getPixel(x, y);
                    int a = c & 0xff;
                    if (a != 0) continue;
                    int rgb = findNeighborRgb(copy, x, y);
                    if (rgb != -1) pixmap.drawPixel(x, y, (rgb & 0xffffff00));
                }
            }
            copy.dispose();
        }
    }

    private int findNeighborRgb(Pixmap p, int x, int y) {
        for (int yy = y - 1; yy <= y + 1; yy++) {
            for (int xx = x - 1; xx <= x + 1; xx++) {
                if (xx == x && yy == y) continue;
                if (xx < 0 || yy < 0 || xx >= p.getWidth() || yy >= p.getHeight()) continue;
                int c = p.getPixel(xx, yy);
                if ((c & 0xff) != 0) return c;
            }
        }
        return -1;
    }

    private void premultiplyAlpha(Pixmap pixmap) {
        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int c = pixmap.getPixel(x, y);
                int a = c & 0xff;
                if (a == 255) continue;
                int r = (c >>> 24) & 0xff;
                int g = (c >>> 16) & 0xff;
                int b = (c >>> 8) & 0xff;
                r = r * a / 255;
                g = g * a / 255;
                b = b * a / 255;
                pixmap.drawPixel(x, y, rgba(r, g, b, a));
            }
        }
    }

    private static Trim trim(Pixmap pixmap, boolean stripX, boolean stripY, int alphaThreshold) {
        int w = pixmap.getWidth();
        int h = pixmap.getHeight();
        int minX = w;
        int minY = h;
        int maxX = -1;
        int maxY = -1;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int a = pixmap.getPixel(x, y) & 0xff;
                if (a <= alphaThreshold) continue;
                if (x < minX) minX = x;
                if (y < minY) minY = y;
                if (x > maxX) maxX = x;
                if (y > maxY) maxY = y;
            }
        }
        Trim trim = new Trim();
        if (maxX == -1) {
            trim.empty = true;
            trim.x = 0;
            trim.y = 0;
            trim.width = 1;
            trim.height = 1;
            return trim;
        }
        trim.x = stripX ? minX : 0;
        trim.y = stripY ? minY : 0;
        trim.width = stripX ? maxX - minX + 1 : w;
        trim.height = stripY ? maxY - minY + 1 : h;
        return trim;
    }

    private static Pixmap crop(Pixmap source, int x, int y, int width, int height) {
        Pixmap out = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        out.setColor(0, 0, 0, 0);
        out.fill();
        out.drawPixmap(source, 0, 0, x, y, width, height);
        return out;
    }

    private static Pixmap resize(Pixmap source, int newW, int newH, TexturePackerOptions.Resampling resampling) {
        Pixmap out = new Pixmap(newW, newH, Pixmap.Format.RGBA8888);
        if (resampling == TexturePackerOptions.Resampling.nearest) {
            for (int y = 0; y < newH; y++) {
                int sy = Math.min(source.getHeight() - 1, Math.round((y + 0.5f) * source.getHeight() / newH - 0.5f));
                for (int x = 0; x < newW; x++) {
                    int sx = Math.min(source.getWidth() - 1, Math.round((x + 0.5f) * source.getWidth() / newW - 0.5f));
                    out.drawPixel(x, y, source.getPixel(sx, sy));
                }
            }
        } else {
            // Bicubic falls back to bilinear in this pure Pixmap implementation.
            for (int y = 0; y < newH; y++) {
                float sy = (y + 0.5f) * source.getHeight() / newH - 0.5f;
                for (int x = 0; x < newW; x++) {
                    float sx = (x + 0.5f) * source.getWidth() / newW - 0.5f;
                    out.drawPixel(x, y, sampleBilinear(source, sx, sy));
                }
            }
        }
        return out;
    }

    private static int sampleBilinear(Pixmap src, float x, float y) {
        int x0 = clamp((int)Math.floor(x), 0, src.getWidth() - 1);
        int y0 = clamp((int)Math.floor(y), 0, src.getHeight() - 1);
        int x1 = clamp(x0 + 1, 0, src.getWidth() - 1);
        int y1 = clamp(y0 + 1, 0, src.getHeight() - 1);
        float tx = x - (float)Math.floor(x);
        float ty = y - (float)Math.floor(y);
        int c00 = src.getPixel(x0, y0);
        int c10 = src.getPixel(x1, y0);
        int c01 = src.getPixel(x0, y1);
        int c11 = src.getPixel(x1, y1);
        int r = bilerp((c00 >>> 24) & 255, (c10 >>> 24) & 255, (c01 >>> 24) & 255, (c11 >>> 24) & 255, tx, ty);
        int g = bilerp((c00 >>> 16) & 255, (c10 >>> 16) & 255, (c01 >>> 16) & 255, (c11 >>> 16) & 255, tx, ty);
        int b = bilerp((c00 >>> 8) & 255, (c10 >>> 8) & 255, (c01 >>> 8) & 255, (c11 >>> 8) & 255, tx, ty);
        int a = bilerp(c00 & 255, c10 & 255, c01 & 255, c11 & 255, tx, ty);
        return rgba(r, g, b, a);
    }

    private static int bilerp(int c00, int c10, int c01, int c11, float tx, float ty) {
        float a = c00 + (c10 - c00) * tx;
        float b = c01 + (c11 - c01) * tx;
        return clamp(Math.round(a + (b - a) * ty), 0, 255);
    }

    private static String hash(Pixmap pixmap) {
        long h = 1469598103934665603L;
        h ^= pixmap.getWidth();
        h *= 1099511628211L;
        h ^= pixmap.getHeight();
        h *= 1099511628211L;
        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                h ^= pixmap.getPixel(x, y);
                h *= 1099511628211L;
            }
        }
        return Long.toHexString(h) + ":" + pixmap.getWidth() + "x" + pixmap.getHeight();
    }

    private static boolean pixmapsEqual(Pixmap a, Pixmap b) {
        if (a.getWidth() != b.getWidth() || a.getHeight() != b.getHeight()) return false;
        for (int y = 0; y < a.getHeight(); y++) {
            for (int x = 0; x < a.getWidth(); x++) {
                if (a.getPixel(x, y) != b.getPixel(x, y)) return false;
            }
        }
        return true;
    }

    private static String buildSimplePolygon(Pixmap pixmap, int alphaThreshold) {
        Trim t = trim(pixmap, true, true, alphaThreshold);
        if (t.empty) return "0,0,1,0,1,1,0,1";
        // This is intentionally conservative: polygon metadata is written as the visible bounding polygon.
        // True polygon packing would need a full convex/concave polygon packer.
        int x = t.x;
        int y = t.y;
        int w = t.width;
        int h = t.height;
        return x + "," + y + "," + (x + w) + "," + y + "," + (x + w) + "," + (y + h) + "," + x + "," + (y + h);
    }

    private static void disposeItems(Array<ImageItem> items) {
        for (int i = 0; i < items.size; i++) {
            ImageItem item = items.get(i);
            if (item.pixmap != null) item.pixmap.dispose();
            item.pixmap = null;
        }
    }

    private static int edgeStart(TexturePackerOptions options, boolean x) {
        return options.edgePadding ? (x ? options.paddingX : options.paddingY) : 0;
    }

    private static String removeExtension(String name) {
        int dot = name.lastIndexOf('.');
        if (dot == -1) return name;
        return name.substring(0, dot);
    }

    private static IndexName parseIndex(String name, boolean useIndexes) {
        IndexName out = new IndexName();
        out.name = name;
        out.index = -1;
        if (!useIndexes) return out;
        int underscore = name.lastIndexOf('_');
        if (underscore == -1 || underscore == name.length() - 1) return out;
        try {
            out.index = Integer.parseInt(name.substring(underscore + 1));
            out.name = name.substring(0, underscore);
        } catch (Throwable ignored) {
            out.index = -1;
            out.name = name;
        }
        return out;
    }

    private static int nextPowerOfTwo(int value) {
        int out = 1;
        while (out < value) out <<= 1;
        return out;
    }

    private static int clamp(int v, int min, int max) {
        return v < min ? min : (v > max ? max : v);
    }

    private static int rgba(int r, int g, int b, int a) {
        return ((r & 255) << 24) | ((g & 255) << 16) | ((b & 255) << 8) | (a & 255);
    }

    private static class SourceImage {
        final FileHandle file;
        final String name;
        final int index;

        SourceImage(FileHandle file, String name, int index) {
            this.file = file;
            this.name = name;
            this.index = index;
        }
    }

    private static class IndexName {
        String name;
        int index;
    }

    private static class ImageItem {
        SourceImage source;
        String name;
        int index;
        Pixmap pixmap;
        int originalWidth;
        int originalHeight;
        int trimX;
        int trimY;
        int trimmedWidth;
        int trimmedHeight;
        int width;
        int height;
        int x;
        int y;
        int packedWidth;
        int packedHeight;
        boolean rotated;
        Page page;
        ImageItem aliasOf;
        String polygonVertices;
    }

    private static class Trim {
        int x;
        int y;
        int width;
        int height;
        boolean empty;
    }

    private static class Page {
        String name;
        int width;
        int height;
        Array<ImageItem> items = new Array<ImageItem>();
    }

    private static class Placed {
        int x;
        int y;
        boolean rotated;
    }

    private static class Rect {
        int x;
        int y;
        int w;
        int h;

        Rect(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }

    private static class MaxRects {
        Array<Rect> free = new Array<Rect>();

        MaxRects(int x, int y, int w, int h) {
            free.add(new Rect(x, y, w, h));
        }

        Placed insert(ImageItem item, TexturePackerOptions options) {
            int bestIndex = -1;
            boolean bestRotated = false;
            int bestShort = Integer.MAX_VALUE;
            int bestLong = Integer.MAX_VALUE;
            for (int i = 0; i < free.size; i++) {
                Rect r = free.get(i);
                Score normal = score(r, item.width + options.paddingX, item.height + options.paddingY);
                if (normal != null && better(normal, bestShort, bestLong)) {
                    bestIndex = i;
                    bestRotated = false;
                    bestShort = normal.shortSide;
                    bestLong = normal.longSide;
                }
                if (options.rotation) {
                    Score rotated = score(r, item.height + options.paddingX, item.width + options.paddingY);
                    if (rotated != null && better(rotated, bestShort, bestLong)) {
                        bestIndex = i;
                        bestRotated = true;
                        bestShort = rotated.shortSide;
                        bestLong = rotated.longSide;
                    }
                }
            }
            if (bestIndex == -1) return null;
            Rect chosen = free.get(bestIndex);
            int outerW = (bestRotated ? item.height : item.width) + options.paddingX;
            int outerH = (bestRotated ? item.width : item.height) + options.paddingY;
            Placed placed = new Placed();
            placed.x = chosen.x;
            placed.y = chosen.y;
            placed.rotated = bestRotated;
            split(bestIndex, chosen.x, chosen.y, outerW, outerH);
            prune();
            return placed;
        }

        private static Score score(Rect r, int w, int h) {
            if (w > r.w || h > r.h) return null;
            Score score = new Score();
            int leftoverW = r.w - w;
            int leftoverH = r.h - h;
            score.shortSide = Math.min(leftoverW, leftoverH);
            score.longSide = Math.max(leftoverW, leftoverH);
            return score;
        }

        private static boolean better(Score score, int shortSide, int longSide) {
            return score.shortSide < shortSide || (score.shortSide == shortSide && score.longSide < longSide);
        }

        private void split(int usedIndex, int x, int y, int w, int h) {
            Rect used = new Rect(x, y, w, h);
            Array<Rect> newFree = new Array<Rect>();
            for (int i = 0; i < free.size; i++) {
                Rect r = free.get(i);
                if (!intersects(used, r)) {
                    newFree.add(r);
                    continue;
                }
                if (used.x > r.x && used.x < r.x + r.w) {
                    newFree.add(new Rect(r.x, r.y, used.x - r.x, r.h));
                }
                if (used.x + used.w < r.x + r.w) {
                    newFree.add(new Rect(used.x + used.w, r.y, r.x + r.w - (used.x + used.w), r.h));
                }
                if (used.y > r.y && used.y < r.y + r.h) {
                    newFree.add(new Rect(r.x, r.y, r.w, used.y - r.y));
                }
                if (used.y + used.h < r.y + r.h) {
                    newFree.add(new Rect(r.x, used.y + used.h, r.w, r.y + r.h - (used.y + used.h)));
                }
            }
            free = newFree;
        }

        private void prune() {
            for (int i = 0; i < free.size; i++) {
                Rect a = free.get(i);
                if (a.w <= 0 || a.h <= 0) {
                    free.removeIndex(i--);
                    continue;
                }
                for (int j = i + 1; j < free.size; j++) {
                    Rect b = free.get(j);
                    if (contains(a, b)) {
                        free.removeIndex(j--);
                    } else if (contains(b, a)) {
                        free.removeIndex(i--);
                        break;
                    }
                }
            }
        }

        private static boolean intersects(Rect a, Rect b) {
            return a.x < b.x + b.w && a.x + a.w > b.x && a.y < b.y + b.h && a.y + a.h > b.y;
        }

        private static boolean contains(Rect a, Rect b) {
            return b.x >= a.x && b.y >= a.y && b.x + b.w <= a.x + a.w && b.y + b.h <= a.y + a.h;
        }
    }

    private static class Score {
        int shortSide;
        int longSide;
    }
}
