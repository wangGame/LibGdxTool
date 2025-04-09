package com.ui.plist;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class PlistAtlasLoader extends SynchronousAssetLoader<PlistAtlas, PlistAtlasParameter> {
	public PlistAtlasLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	PlistAtlas.PlistAtlasData data;

	@Override
	public PlistAtlas load (AssetManager assetManager, String fileName, FileHandle file, PlistAtlasParameter parameter) {
		for (PlistAtlas.PlistAtlasData.Page page : data.getPages()) {
			Texture texture = assetManager.get(page.textureFile.path().replaceAll("\\\\", "/"), Texture.class);
			page.texture = texture;
		}

		return new PlistAtlas(data);
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle atlasFile, PlistAtlasParameter parameter) {
		FileHandle imgDir = atlasFile.parent();
		if (parameter == null)
			data = new PlistAtlas.PlistAtlasData(atlasFile, imgDir);
		else
			data = new PlistAtlas.PlistAtlasData(atlasFile, imgDir, parameter.minFilter, parameter.magFilter);

		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		for (PlistAtlas.PlistAtlasData.Page page : data.getPages()) {
			TextureParameter params = new TextureParameter();
			params.format = page.format;
			params.genMipMaps = page.useMipMaps;
			params.minFilter = page.minFilter;
			params.magFilter = page.magFilter;
			dependencies.add(new AssetDescriptor<Texture>(page.textureFile, Texture.class, params));
		}
		return dependencies;
	}

}
