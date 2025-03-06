package com.ui.plist;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class MiniPlistAtlasLoader extends SynchronousAssetLoader<PlistAtlas, PlistAtlasParameter> {
	private float scale;
	public MiniPlistAtlasLoader(FileHandleResolver resolver,float scale) {
		super(resolver);
		this.scale = scale;
	}

	PlistAtlas.PlistAtlasData data;

	@Override
	public PlistAtlas load (AssetManager assetManager, String fileName, FileHandle file, PlistAtlasParameter parameter) {
		for (Page page : data.getPages()) {
			Texture texture = assetManager.get(page.textureFile.path().replaceAll("\\\\", "/"), Texture.class);
			page.texture = texture;
		}
		PlistAtlas atlas = new PlistAtlas(data);
		for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {


			float wid=region.getTexture().getWidth();
			float height=region.getTexture().getHeight();


			float u = region.getU();
			float u2 = region.getU2();
			float v = region.getV();
			float v2 = region.getV2();

			u= (MathUtils.ceil(u*wid*scale)+0.25f/scale)/scale/wid;
			u2=(MathUtils.floor(u2*wid*scale)-0.25f/scale)/scale/wid;
			v= (MathUtils.ceil(v*height*scale)+0.25f/scale)/scale/height;
			v2=(MathUtils.floor(v2*height*scale)-0.25f/scale)/scale/height;

			if(u<u2){
				region.setU(u);
				region.setU2(u2);
			}
			else if(v<v2){
				region.setV(v);
				region.setV2(v2);
			}
		}
		return atlas;
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle atlasFile, PlistAtlasParameter parameter) {
		FileHandle imgDir = atlasFile.parent();
		if (parameter == null)
			data = new PlistAtlas.PlistAtlasData(atlasFile, imgDir);
		else
			data = new PlistAtlas.PlistAtlasData(atlasFile, imgDir, parameter.minFilter, parameter.magFilter);

		Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
		for (Page page : data.getPages()) {
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
