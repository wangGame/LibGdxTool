/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.ui.loader.ManagerUILoader;
import com.ui.plist.PlistAtlas;

import net.mwplay.cocostudio.ui.BaseCocoStudioUIEditor;
import net.mwplay.cocostudio.ui.model.CCExport;
import net.mwplay.cocostudio.ui.model.FileData;
import net.mwplay.cocostudio.ui.model.ObjectData;
import net.mwplay.cocostudio.ui.widget.TTFLabelStyle;
import java.util.List;

public class ManagerUIEditor extends BaseCocoStudioUIEditor implements Disposable {
	final String tag = ManagerUIEditor.class.getName();
	//cocos项目目录
	public String dirName;
	//cocos文件目录
	public String filedir;
	//新加载的资源
	public Array<String> unmanagedLoad = new Array<String>();
	//用到的assetmanager
	AssetManager assetManager;
	//销毁
	private boolean unloaded=false;

	//默认图片过滤方式
	TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter() {{
		minFilter = Texture.TextureFilter.MipMapLinearLinear;
		magFilter = Texture.TextureFilter.Linear;
	}};

	//默认bitmap过滤方式
	BitmapFontLoader.BitmapFontParameter bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter() {{
		minFilter = Texture.TextureFilter.Linear;
		magFilter = Texture.TextureFilter.Linear;
	}};

	//初始化函数
	public ManagerUIEditor (FileHandle jsonFile) {
		this(jsonFile, null);
	}

	//初始化函数
	public ManagerUIEditor (FileHandle jsonFile, String dirName) {
		this(jsonFile,dirName,null);
	}
	private CCExport export1;
	//初始化函数
	public ManagerUIEditor (FileHandle jsonFile, String dirName, AssetManager assetManager) {
		super(jsonFile);
		this.export1 = export;


		this.assetManager = assetManager;
		if (this.assetManager == null)
			this.assetManager = null;
		this.dirName = dirName;
		if (this.dirName == null) {
			this.dirName = jsonFile.parent().toString();
			if (!this.dirName.equals("")) {
				this.dirName += "/";
			}
		}
		filedir = jsonFile.parent().toString();
		if (!filedir.equals("")) {
			filedir += "/";
		}
	}

	public CCExport getExport1() {
		return export1;
	}

	//获取依赖资源
	public List<String> getResources () {
		return export.Content.Content.UsedResources;
	}

	//获取粒子路径
	@Override
	public String findParticePath (FileData filedata) {
		String name = filedata.Path;
		if (name == null || name.equals("")) {
			return null;
		}
		return dirName + name;
	}

	//获取textureregion
	@Override
	public TextureRegion findTextureRegion (ObjectData option, FileData filedata) {
		if (filedata == null || filedata.Path.equals("")) {
			return null;
		}
		if (filedata.Plist == null || filedata.Plist.equals("")) {
			String texturepath = dirName + filedata.Path;
			if (!assetManager.isLoaded(texturepath)) {
				assetManager.load(texturepath, Texture.class, textureParameter);
				assetManager.finishLoading();
				unmanagedLoad.add(texturepath);
				Gdx.app.debug(tag, "unmanaged load " + texturepath);
			}
			return new TextureRegion(assetManager.get(texturepath, Texture.class));
		} else {
			String atlaspath = dirName + filedata.Plist;
			if (atlaspath.endsWith(".plist")) {
				if (!assetManager.isLoaded(atlaspath)) {
					assetManager.load(atlaspath, PlistAtlas.class);
					assetManager.finishLoading();
					unmanagedLoad.add(atlaspath);
					Gdx.app.debug(tag, "unmanaged load " + atlaspath);
				}
				PlistAtlas atlas = assetManager.get(atlaspath, PlistAtlas.class);
				String text = filedata.Path.replace(".png", "");
				return atlas.findRegion(text);
			} else if (atlaspath.endsWith(".atlas")){
				if (!assetManager.isLoaded(atlaspath)) {
					assetManager.load(atlaspath, TextureAtlas.class);
					assetManager.finishLoading();
					unmanagedLoad.add(atlaspath);
					Gdx.app.debug(tag, "unmanaged load " + atlaspath);
				}
				TextureAtlas atlas = assetManager.get(atlaspath, TextureAtlas.class);
				String text = filedata.Path.replace(".png", "");
				return atlas.findRegion(text);
			}
		}
		return null;
	}
	//获取drawable
	@Override
	public Drawable findDrawable (ObjectData option, FileData fileData) {
		//显示Default
		if (fileData == null) {// 默认值不显示
			return null;
		}
		TextureRegion textureRegion = findTextureRegion(option, fileData);
		if (textureRegion == null) {
			return null;
		}
		if (option.Scale9Enable) {// 九宫格支持
			NinePatch np = new NinePatch(textureRegion, option.Scale9OriginX,
				textureRegion.getRegionWidth() - option.Scale9Width - option.Scale9OriginX, option.Scale9OriginY,
				textureRegion.getRegionHeight() - option.Scale9Height - option.Scale9OriginY);
//			np.setColor(NUtils.getColor(option.CColor, option.Alpha));
			return new NinePatchDrawable(np);
		}
		if (textureRegion instanceof TextureAtlas.AtlasRegion) {
			TextureAtlas.AtlasRegion atlasRegion = (TextureAtlas.AtlasRegion)textureRegion;
			TextureAtlas.AtlasSprite atlasSprite = new TextureAtlas.AtlasSprite(atlasRegion);
			return new SpriteDrawable(atlasSprite);
		} else {
			return new TextureRegionDrawable(textureRegion);
		}
	}

	//获取labelstyle
	@Override
	public TTFLabelStyle createLabelStyle (ObjectData option, String text, Color color) {
		FileHandle fontFile = null;
		if (fontFile == null) {
			Gdx.app.debug(option.ctype, "ttf字体不存在,使用默认字体");
		}
		BitmapFont font = null;
		font = new BitmapFont();
		return new TTFLabelStyle(new LabelStyle(font, color), fontFile, option.FontSize);
	}

	//获取字体资源
	@Override
	public BitmapFont findBitmapFont (FileData labelBMFontFile_cnb) {
		if (labelBMFontFile_cnb == null)
			return  new BitmapFont();
		BitmapFont font;
		String bmpath = dirName + labelBMFontFile_cnb.Path;
		if (!assetManager.isLoaded(bmpath)) {
			assetManager.load(bmpath, BitmapFont.class, bitmapFontParameter);
			assetManager.finishLoading();
			unmanagedLoad.add(bmpath);
			Gdx.app.debug(tag, "unmanaged load " + bmpath);
		}
		font = assetManager.get(bmpath, BitmapFont.class);
		font.setUseIntegerPositions(false);
		return font;
	}

	//获取字体
	@Override
	public BitmapFont createLabelStyleBitmapFint (ObjectData option, String text, Color color) {
		return  new BitmapFont();
	}

	//获取依赖ui
	@Override
	public BaseCocoStudioUIEditor findCoco (FileData fileData) {
		ManagerUIEditor ui;
		String uipath = dirName + fileData.Path;
		if (!assetManager.isLoaded(uipath)) {
			assetManager.load(uipath, ManagerUIEditor.class, new ManagerUILoader.ManagerUIParameter(dirName, assetManager));
			assetManager.finishLoading();
			unmanagedLoad.add(uipath);
			Gdx.app.debug(tag, "unmanaged load " + uipath);
		}
		ui = assetManager.get(uipath, ManagerUIEditor.class);
		return ui;
	}

	@Override
	public synchronized void dispose () {
		if (!unloaded) {
			unloaded = true;
			for (String unload : unmanagedLoad)
				assetManager.unload(unload);
			unmanagedLoad.clear();
		}
	}
}
