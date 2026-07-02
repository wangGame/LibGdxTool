package com.libGdx.tools.unplist.convert;

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
import com.badlogic.gdx.utils.*;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.ClampToEdge;

public class ConvertPlistAtlasOther{

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

				String size = element.getChild(0).getChild(3).getChild(5).getText();
				String[] sizeNum = size.replace("{", "").replace("}", "").split(",");


				float textureWidth = Float.parseFloat(sizeNum[0]);
				float textureHeight = Float.parseFloat(sizeNum[1]);


				TextureFilter min = minFilter != null ? minFilter : TextureFilter.Linear;
				TextureFilter max = maxFilter != null ? maxFilter : TextureFilter.Linear;

				Format format = Format.RGBA8888;

				TextureWrap repeatX = ClampToEdge;
				TextureWrap repeatY = ClampToEdge;

				String line = element.getChild(0).getChild(3).getChild(3).getText();
				FileHandle file = imagesDir.child(line);

				Page pageImage = new Page(file, textureWidth, textureHeight, min.isMipMap(), format, min, max, repeatX, repeatY);
				pages.add(pageImage);

				XmlReader.Element childs = element.getChild(0).getChild(1);
				int regionscount = childs.getChildCount();

				for (int i = 0; i < regionscount; i += 2) {
					String key = childs.getChild(i).getText();

					XmlReader.Element value = childs.getChild(i + 1);

					/** ***********************************
					 * 尝试
					 **************************************/

//					int childCount = value.getChildCount();
//					for (int i1 = 0; i1 < childCount; i1++) {
//						if (value != null&&value.getChild(i1).equals("textureRect")){
//							i1++;
//							String textOne = value.getChild(i1).getText();
//						}else if (value!=null&&value.getChild(i1).equals("textureRotated")){
//							i1++;
//							String textTwo = value.getChild(i1).getText();
//						}else if (value != null && value.getChild(i1).equals("spriteSourceSize")){
//							i1++;
//							String spriteSourceSize = value.getChild(i1).getText();
//						}
//					}

					/** ***********************************
					 * 尝试
					 **************************************/
					String text1 = value.getChild(1).getText();
					String text3 = value.getChild(5).getName();
					String text4 = value.getChild(7).getText();


					String[] points = text1.replace("{", "").replace("}", "").split(",");
//					if (points.length==4) {
					int left = Integer.parseInt(points[0]);
					int top = Integer.parseInt(points[1]);
					int width = Integer.parseInt(points[2]);
					int height = Integer.parseInt(points[3]);
//					}
					int offleft = 0;
					int offdown = 0;

					boolean rotate = Boolean.parseBoolean(text3);

					String[] sizes = text4.replace("{", "").replace("}", "").split(",");
					int oriwidth = Integer.parseInt(sizes[0]);
					int oriheight = Integer.parseInt(sizes[1]);

					Region region = new Region();
					region.page = pageImage;
					region.index = -1;
					region.name = key.replace(".png", "");
					region.offsetX = offleft;
					region.offsetY = offdown;
					region.originalWidth = oriwidth;
					region.originalHeight = oriheight;
					region.rotate = rotate;
					region.left = left;
					region.top = top;
					region.width = width;
					region.height = height;
					regions.add(region);
				}
			} catch (Exception ex) {
				throw new GdxRuntimeException("Error reading pack file: " + packFile, ex);
			}
		}
	}

	private PlistAtlasData plistAtlasData;
	public ConvertPlistAtlasOther(String internalPackFile) {
		FileHandle packFile = new FileHandle(internalPackFile);
		plistAtlasData = new PlistAtlasData(packFile, packFile.parent());
	}

	public PlistAtlasData getPlistAtlasData() {
		return plistAtlasData;
	}
}