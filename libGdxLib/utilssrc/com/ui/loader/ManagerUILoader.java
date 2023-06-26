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

package com.ui.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.ui.ManagerUIEditor;
import com.ui.plist.PlistAtlas;
import com.ui.plist.PlistAtlasParameter;

import java.util.List;

public class ManagerUILoader extends AsynchronousAssetLoader<ManagerUIEditor, ManagerUILoader.ManagerUIParameter> {

	public static TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter() {
		{
			minFilter = Texture.TextureFilter.Nearest;
			magFilter = Texture.TextureFilter.Nearest;
			genMipMaps = false;
			wrapU = Texture.TextureWrap.ClampToEdge;
			wrapV = Texture.TextureWrap.ClampToEdge;
		}
	};

	public static PlistAtlasParameter plistAtlasParameter = new PlistAtlasParameter() {
		{
//			if (Configuration.device_state != Configuration.DeviceState.poor) {
//				genMipMaps = true;
//				minFilter = Texture.TextureFilter.MipMapLinearLinear;
//				magFilter = Texture.TextureFilter.Linear;
//			}else {
				genMipMaps = false;
			minFilter = Texture.TextureFilter.MipMapLinearLinear;
				magFilter = Texture.TextureFilter.Linear;
//			}
			wrapU = Texture.TextureWrap.ClampToEdge;
			wrapV = Texture.TextureWrap.ClampToEdge;
		}
	};

	public static BitmapFontLoader.BitmapFontParameter bitmapFontParameter = new BitmapFontLoader.BitmapFontParameter() {
		{
			minFilter = Texture.TextureFilter.MipMapLinearLinear;
			magFilter = Texture.TextureFilter.Linear;
			genMipMaps = true;
		}
	};
	private Array<ManagerUIEditor> cocoStudioUIEditors = new Array<ManagerUIEditor>();

	public ManagerUILoader (FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, ManagerUIParameter parameter) {
//
//		cocosScene = new CocosScene();
//		cocosScene.setEditor(cocoStudioUIEditor);
	}

	@Override
	public ManagerUIEditor loadSync (AssetManager manager, String fileName, FileHandle file, ManagerUIParameter parameter) {
		ManagerUIEditor cocoStudioUIEditor = cocoStudioUIEditors.get(cocoStudioUIEditors.size - 1);
		cocoStudioUIEditors.removeIndex(cocoStudioUIEditors.size - 1);
		return cocoStudioUIEditor;
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, ManagerUIParameter parameter) {
		ManagerUIEditor cocoStudioUIEditor;
		if (parameter == null)
			cocoStudioUIEditor = new ManagerUIEditor(file);
		else
			cocoStudioUIEditor = new ManagerUIEditor(file, parameter.dirname, parameter.assetManager);
		cocoStudioUIEditors.add(cocoStudioUIEditor);

		Array<AssetDescriptor> assetDescriptors = new Array<AssetDescriptor>();
		List<String> list = cocoStudioUIEditor.getResources();
		for (String name : list) {
			String fname = cocoStudioUIEditor.filedir + name;
			fname = dealDot(fname);
			if (name.endsWith(".png"))
				assetDescriptors.add(new AssetDescriptor<Texture>(fname, Texture.class, textureParameter));
			else if (name.endsWith(".json"))
				assetDescriptors.add(new AssetDescriptor<ManagerUIEditor>(fname, ManagerUIEditor.class, parameter));
			else if (name.endsWith(".plist"))
				assetDescriptors.add(new AssetDescriptor<PlistAtlas>(fname, PlistAtlas.class, plistAtlasParameter));
			else if (name.endsWith(".fnt"))
				assetDescriptors.add(new AssetDescriptor<BitmapFont>(fname, BitmapFont.class, bitmapFontParameter));
			else if (name.endsWith(".atlas"))
				assetDescriptors.add(new AssetDescriptor<TextureAtlas>(fname, TextureAtlas.class));
			else
				Gdx.app.debug(ManagerUILoader.class.getName(), "Unsolved resource");
		}
		return assetDescriptors;
	}

	private String dealDot (String fname) {
		String[] fs = fname.replaceAll("\\\\", "/").split("/");
		Array<String> ars = Array.with(fs);
		for (int i = 0; i < ars.size; i++) {
			if (i < 0)
				continue;
			if (ars.get(i).equals("..")) {
				ars.removeIndex(i);
				ars.removeIndex(i - 1);
				i -= 2;
			}
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ars.size; i++) {
			if (i != 0)
				sb.append("/");
			sb.append(ars.get(i));
		}
		return sb.toString();
	}

	static public class ManagerUIParameter extends AssetLoaderParameters<ManagerUIEditor> {
		public String dirname;
		public AssetManager assetManager;

		public ManagerUIParameter (String dir) {
			dirname = dir;
			assetManager = null;
		}

		public ManagerUIParameter (String dir, AssetManager assetManager) {
			dirname = dir;
			this.assetManager = assetManager;
		}
	}
}
