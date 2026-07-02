/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.libGdx.tools.unplist.convert;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.*;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Sort;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/** Loads images from texture atlases created by TexturePacker.<br>
 * <br>
 * A TextureAtlas must be disposed to free up the resources consumed by the backing textures.
 * @author Nathan Sweet */
public class TextureAtlas implements Disposable {
    static final String[] tuple = new String[4];


    @Override
    public void dispose() {

    }

    public static class TextureAtlasData {
        public static class Page {
            public final FileHandle textureFile;
            public Texture texture;
            public final float width, height;
            public final boolean useMipMaps;
            public final Format format;
            public final TextureFilter minFilter;
            public final TextureFilter magFilter;
            public final TextureWrap uWrap;
            public final TextureWrap vWrap;

            public Page (FileHandle handle, float width, float height, boolean useMipMaps, Format format, TextureFilter minFilter,
                         TextureFilter magFilter, TextureWrap uWrap, TextureWrap vWrap) {
                this.width = width;
                this.height = height;
                this.textureFile = handle;
                this.useMipMaps = useMipMaps;
                this.format = format;
                this.minFilter = minFilter;
                this.magFilter = magFilter;
                this.uWrap = uWrap;
                this.vWrap = vWrap;
            }
        }

        public static class Region {
            public Page page;
            public int index;
            public String name;
            public float offsetX;
            public float offsetY;
            public int originalWidth;
            public int originalHeight;
            public boolean rotate;
            public int left;
            public int top;
            public int width;
            public int height;
            public boolean flip;
            public int[] splits;
            public int[] pads;
        }

        final Array<Page> pages = new Array();
        final Array<Region> regions = new Array();

        public TextureAtlasData (FileHandle packFile, FileHandle imagesDir, boolean flip) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(packFile.read()), 64);
            try {
                Page pageImage = null;
                while (true) {
                    String line = reader.readLine();
                    if (line == null) break;
                    if (line.trim().length() == 0)
                        pageImage = null;
                    else if (pageImage == null) {
                        FileHandle file = imagesDir.child(line);

                        float width = 0, height = 0;
                        if (readTuple(reader) == 2) { // size is only optional for an atlas packed with an old TexturePacker.
                            width = Integer.parseInt(tuple[0]);
                            height = Integer.parseInt(tuple[1]);
                            readTuple(reader);
                        }
                        Format format = Format.valueOf(tuple[0]);

                        readTuple(reader);
                        TextureFilter min = TextureFilter.valueOf(tuple[0]);
                        TextureFilter max = TextureFilter.valueOf(tuple[1]);

                        String direction = readValue(reader);
                        TextureWrap repeatX = ClampToEdge;
                        TextureWrap repeatY = ClampToEdge;
                        if (direction.equals("x"))
                            repeatX = Repeat;
                        else if (direction.equals("y"))
                            repeatY = Repeat;
                        else if (direction.equals("xy")) {
                            repeatX = Repeat;
                            repeatY = Repeat;
                        }

                        pageImage = new Page(file, width, height, min.isMipMap(), format, min, max, repeatX, repeatY);
                        pages.add(pageImage);
                    } else {
                        boolean rotate = Boolean.valueOf(readValue(reader));

                        readTuple(reader);
                        int left = Integer.parseInt(tuple[0]);
                        int top = Integer.parseInt(tuple[1]);

                        readTuple(reader);
                        int width = Integer.parseInt(tuple[0]);
                        int height = Integer.parseInt(tuple[1]);

                        Region region = new Region();
                        region.page = pageImage;
                        region.left = left;
                        region.top = top;
                        region.width = width;
                        region.height = height;
                        region.name = line;
                        region.rotate = rotate;

                        if (readTuple(reader) == 4) { // split is optional
                            region.splits = new int[] {Integer.parseInt(tuple[0]), Integer.parseInt(tuple[1]),
                                    Integer.parseInt(tuple[2]), Integer.parseInt(tuple[3])};

                            if (readTuple(reader) == 4) { // pad is optional, but only present with splits
                                region.pads = new int[] {Integer.parseInt(tuple[0]), Integer.parseInt(tuple[1]),
                                        Integer.parseInt(tuple[2]), Integer.parseInt(tuple[3])};

                                readTuple(reader);
                            }
                        }

                        region.originalWidth = Integer.parseInt(tuple[0]);
                        region.originalHeight = Integer.parseInt(tuple[1]);

                        readTuple(reader);
                        region.offsetX = Integer.parseInt(tuple[0]);
                        region.offsetY = Integer.parseInt(tuple[1]);

                        region.index = Integer.parseInt(readValue(reader));

                        if (flip) region.flip = true;

                        regions.add(region);
                    }
                }
            } catch (Exception ex) {
                throw new GdxRuntimeException("Error reading pack file: " + packFile, ex);
            } finally {
                StreamUtils.closeQuietly(reader);
            }


        }

        public Array<Page> getPages () {
            return pages;
        }

        public Array<Region> getRegions () {
            return regions;
        }
    }


    static String readValue (BufferedReader reader) throws IOException {
        String line = reader.readLine();
        int colon = line.indexOf(':');
        if (colon == -1) throw new GdxRuntimeException("Invalid line: " + line);
        return line.substring(colon + 1).trim();
    }

    static int readTuple (BufferedReader reader) throws IOException {
        String line = reader.readLine();
        int colon = line.indexOf(':');
        if (colon == -1) throw new GdxRuntimeException("Invalid line: " + line);
        int i = 0, lastMatch = colon + 1;
        for (i = 0; i < 3; i++) {
            int comma = line.indexOf(',', lastMatch);
            if (comma == -1) break;
            tuple[i] = line.substring(lastMatch, comma).trim();
            lastMatch = comma + 1;
        }
        tuple[i] = line.substring(lastMatch).trim();
        return i + 1;
    }

    /** Creates an empty atlas to which regions can be added. */
    public TextureAtlas () {
    }

    /** Loads the specified pack file using {@link FileType#Internal}, using the parent directory of the pack file to find the page
     * images. */
    private TextureAtlasData data;
    public TextureAtlas (String internalPackFile) {
        FileHandle internal = new FileHandle(internalPackFile);
        data = new TextureAtlasData(internal, internal.parent(), false);
    }

    public TextureAtlasData getData() {
        return data;
    }

    static public class AtlasRegion extends TextureRegion {
        /** The number at the end of the original image file name, or -1 if none.<br>
         * <br>
         * When sprites are packed, if the original file name ends with a number, it is stored as the index and is not considered as
         * part of the sprite's name. This is useful for keeping animation frames in order.
         * @see com.badlogic.gdx.graphics.g2d.TextureAtlas#findRegions(String) */
        public int index;

        /** The name of the original image file, up to the first underscore. Underscores denote special instructions to the texture
         * packer. */
        public String name;

        /** The offset from the left of the original image to the left of the packed image, after whitespace was removed for packing. */
        public float offsetX;

        /** The offset from the bottom of the original image to the bottom of the packed image, after whitespace was removed for
         * packing. */
        public float offsetY;

        /** The width of the image, after whitespace was removed for packing. */
        public int packedWidth;

        /** The height of the image, after whitespace was removed for packing. */
        public int packedHeight;

        /** The width of the image, before whitespace was removed and rotation was applied for packing. */
        public int originalWidth;

        /** The height of the image, before whitespace was removed for packing. */
        public int originalHeight;

        /** If true, the region has been rotated 90 degrees counter clockwise. */
        public boolean rotate;

        /** The ninepatch splits, or null if not a ninepatch. Has 4 elements: left, right, top, bottom. */
        public int[] splits;

        /** The ninepatch pads, or null if not a ninepatch or the has no padding. Has 4 elements: left, right, top, bottom. */
        public int[] pads;

        public AtlasRegion (Texture texture, int x, int y, int width, int height) {
            super(texture, x, y, width, height);
            originalWidth = width;
            originalHeight = height;
            packedWidth = width;
            packedHeight = height;
        }

        public AtlasRegion (com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion region) {
            setRegion(region);
            index = region.index;
            name = region.name;
            offsetX = region.offsetX;
            offsetY = region.offsetY;
            packedWidth = region.packedWidth;
            packedHeight = region.packedHeight;
            originalWidth = region.originalWidth;
            originalHeight = region.originalHeight;
            rotate = region.rotate;
            splits = region.splits;
        }

        @Override
        /** Flips the region, adjusting the offset so the image appears to be flip as if no whitespace has been removed for packing. */
        public void flip (boolean x, boolean y) {
            super.flip(x, y);
            if (x) offsetX = originalWidth - offsetX - getRotatedPackedWidth();
            if (y) offsetY = originalHeight - offsetY - getRotatedPackedHeight();
        }

        /** Returns the packed width considering the rotate value, if it is true then it returns the packedHeight, otherwise it
         * returns the packedWidth. */
        public float getRotatedPackedWidth () {
            return rotate ? packedHeight : packedWidth;
        }

        /** Returns the packed height considering the rotate value, if it is true then it returns the packedWidth, otherwise it
         * returns the packedHeight. */
        public float getRotatedPackedHeight () {
            return rotate ? packedWidth : packedHeight;
        }

        public String toString () {
            return name;
        }
    }



}
