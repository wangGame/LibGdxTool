package com.ui.plist;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.ClampToEdge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Region;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.XmlReader;

public class PlistAtlas implements Disposable {

	private final ObjectSet<Texture> textures = new ObjectSet(4);
	private final Array<AtlasRegion> regions = new Array();

	public static class PlistAtlasData {
		final Array<Page> pages = new Array();
		final Array<Region> regions = new Array();

		public PlistAtlasData(FileHandle packFile, FileHandle imagesDir) {
			this(packFile, imagesDir, null, null);
		}

		public PlistAtlasData(FileHandle packFile, FileHandle imagesDir, TextureFilter minFilter, TextureFilter maxFilter) {
			try {
				XmlReader xmlReader = new XmlReader();
				String xml = packFile.readString("UTF-8");
				if (xml.charAt(0) == 65279)
					xml = xml.substring(1);
				XmlReader.Element element = xmlReader.parse(xml);

				float textureWidth = Float.parseFloat(element.getChild(0).getChild(5).getChild(1).getText());
				float textureHeight = Float.parseFloat(element.getChild(0).getChild(5).getChild(3).getText());

				TextureFilter min = minFilter != null ? minFilter : TextureFilter.Linear;
				TextureFilter max = maxFilter != null ? maxFilter : TextureFilter.Linear;

				Format format = Format.RGBA8888;

				TextureWrap repeatX = ClampToEdge;
				TextureWrap repeatY = ClampToEdge;

				String line=element.getChild(0).getChild(3).getChild(3).getText();
				FileHandle file = imagesDir.child(line);

				Page pageImage = new Page(file, textureWidth, textureHeight, min.isMipMap(), format, min, max, repeatX, repeatY);
				pages.add(pageImage);

				XmlReader.Element childs = element.getChild(0).getChild(1);
				int regionscount = childs.getChildCount();

				for (int i = 0; i < regionscount; i += 2) {
					String key = childs.getChild(i).getText();

					XmlReader.Element value = childs.getChild(i + 1);

					String text1 = value.getChild(1).getText();
					String text2 = value.getChild(3).getText();
					String text3 = value.getChild(5).getName();
					String text4 = value.getChild(7).getText();

					String[] points=text1.replace("{","").replace("}","").split(",");
					int left=Integer.parseInt(points[0]);
					int top=Integer.parseInt(points[1]);
					int width=Integer.parseInt(points[2]);
					int height=Integer.parseInt(points[3]);

					String[] offsets=text2.replace("{","").replace("}","").split(",");
					int offleft=Integer.parseInt(offsets[0]);
					int offdown=Integer.parseInt(offsets[1]);

					boolean rotate=Boolean.parseBoolean(text3);

					String[] sizes=text4.replace("{","").replace("}","").split(",");
					int oriwidth=Integer.parseInt(sizes[0]);
					int oriheight=Integer.parseInt(sizes[1]);

					Region region = new Region();
					region.page=pageImage;
					region.index = -1;
					region.name = key.replace(".png","");
					region.offsetX = offleft;
					region.offsetY = offdown;
					region.originalWidth =oriwidth ;
					region.originalHeight = oriheight;
					region.rotate = rotate;
					region.left=left;
					region.top=top;
					region.width=width;
					region.height=height;
					regions.add(region);
				}
			} catch (Exception ex) {
				throw new GdxRuntimeException("Error reading pack file: " + packFile, ex);
			}
		}

		public Array<Page> getPages () {
			return pages;
		}

		public Array<Region> getRegions () {
			return regions;
		}
	}

	public PlistAtlas() {
	}

	public PlistAtlas(String internalPackFile) {
		this(Gdx.files.internal(internalPackFile));
	}

	public PlistAtlas(FileHandle packFile) {
		this(packFile, packFile.parent());
	}

	public PlistAtlas(FileHandle packFile, FileHandle imagesDir) {
		this(new PlistAtlasData(packFile, imagesDir));
	}

	public PlistAtlas(PlistAtlasData data) {
		if (data != null) load(data);
	}

	private void load (PlistAtlasData data) {
		ObjectMap<Page, Texture> pageToTexture = new ObjectMap<Page, Texture>();
		for (Page page : data.pages) {
			Texture texture = null;
			if (page.texture == null) {
				texture = new Texture(page.textureFile, page.format, page.useMipMaps);
				texture.setFilter(page.minFilter, page.magFilter);
				texture.setWrap(page.uWrap, page.vWrap);
			} else {
				texture = page.texture;
				texture.setFilter(page.minFilter, page.magFilter);
				texture.setWrap(page.uWrap, page.vWrap);
			}
			textures.add(texture);
			pageToTexture.put(page, texture);
		}

		for (Region region : data.regions) {
			int width = region.width;
			int height = region.height;
			AtlasRegion atlasRegion = new AtlasRegion(pageToTexture.get(region.page), region.left, region.top,
				region.rotate ? height : width, region.rotate ? width : height);
			atlasRegion.index = region.index;
			atlasRegion.name = region.name;
			atlasRegion.offsetX = region.offsetX;
			atlasRegion.offsetY = region.offsetY;
			atlasRegion.originalHeight = region.originalHeight;
			atlasRegion.originalWidth = region.originalWidth;
			atlasRegion.rotate = region.rotate;
			atlasRegion.splits = region.splits;
			atlasRegion.pads = region.pads;

			if (region.rotate) atlasRegion.flip(true, true);
			regions.add(atlasRegion);
		}
	}

	public AtlasRegion addRegion (String name, Texture texture, int x, int y, int width, int height) {
		textures.add(texture);
		AtlasRegion region = new AtlasRegion(texture, x, y, width, height);
		region.name = name;
		region.originalWidth = width;
		region.originalHeight = height;
		region.index = -1;
		regions.add(region);
		return region;
	}

	public AtlasRegion addRegion (String name, TextureRegion textureRegion) {
		return addRegion(name, textureRegion.getTexture(), textureRegion.getRegionX(), textureRegion.getRegionY(),
			textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
	}

	public Array<AtlasRegion> getRegions () {
		return regions;
	}

	public AtlasRegion findRegion (String name) {
		for (int i = 0, n = regions.size; i < n; i++)
			if (regions.get(i).name.equals(name)) return regions.get(i);
		return null;
	}

	public AtlasRegion findRegion (String name, int index) {
		for (int i = 0, n = regions.size; i < n; i++) {
			AtlasRegion region = regions.get(i);
			if (!region.name.equals(name)) continue;
			if (region.index != index) continue;
			return region;
		}
		return null;
	}

	public Array<AtlasRegion> findRegions (String name) {
		Array<AtlasRegion> matched = new Array();
		for (int i = 0, n = regions.size; i < n; i++) {
			AtlasRegion region = regions.get(i);
			if (region.name.equals(name)) matched.add(new AtlasRegion(region));
		}
		return matched;
	}

	public Array<Sprite> createSprites () {
		Array sprites = new Array(regions.size);
		for (int i = 0, n = regions.size; i < n; i++)
			sprites.add(newSprite(regions.get(i)));
		return sprites;
	}

	public Sprite createSprite (String name) {
		for (int i = 0, n = regions.size; i < n; i++)
			if (regions.get(i).name.equals(name)) return newSprite(regions.get(i));
		return null;
	}

	public Sprite createSprite (String name, int index) {
		for (int i = 0, n = regions.size; i < n; i++) {
			AtlasRegion region = regions.get(i);
			if (!region.name.equals(name)) continue;
			if (region.index != index) continue;
			return newSprite(regions.get(i));
		}
		return null;
	}

	public Array<Sprite> createSprites (String name) {
		Array<Sprite> matched = new Array();
		for (int i = 0, n = regions.size; i < n; i++) {
			AtlasRegion region = regions.get(i);
			if (region.name.equals(name)) matched.add(newSprite(region));
		}
		return matched;
	}

	private Sprite newSprite (AtlasRegion region) {
		if (region.packedWidth == region.originalWidth && region.packedHeight == region.originalHeight) {
			if (region.rotate) {
				Sprite sprite = new Sprite(region);
				sprite.setBounds(0, 0, region.getRegionHeight(), region.getRegionWidth());
				sprite.rotate90(true);
				return sprite;
			}
			return new Sprite(region);
		}
		return new AtlasSprite(region);
	}

	public NinePatch createPatch (String name) {
		for (int i = 0, n = regions.size; i < n; i++) {
			AtlasRegion region = regions.get(i);
			if (region.name.equals(name)) {
				int[] splits = region.splits;
				if (splits == null) throw new IllegalArgumentException("Region does not have ninepatch splits: " + name);
				NinePatch patch = new NinePatch(region, splits[0], splits[1], splits[2], splits[3]);
				if (region.pads != null) patch.setPadding(region.pads[0], region.pads[1], region.pads[2], region.pads[3]);
				return patch;
			}
		}
		return null;
	}

	public ObjectSet<Texture> getTextures () {
		return textures;
	}

	public void dispose () {
		for (Texture texture : textures)
			texture.dispose();
		textures.clear();
	}
}
