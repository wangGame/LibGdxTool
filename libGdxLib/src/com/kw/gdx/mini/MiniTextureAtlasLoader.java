package com.kw.gdx.mini;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.TextureAtlasData.Page;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/** {@link AssetLoader} to load {@link TextureAtlas} instances. Passing a {@link TextureAtlasParameter} to
 * {@link AssetManager#load(String, Class, AssetLoaderParameters)} allows to specify whether the atlas regions should be flipped
 * on the y-axis or not.
 * @author mzechner */
public class MiniTextureAtlasLoader extends SynchronousAssetLoader<TextureAtlas, MiniTextureAtlasLoader.TextureAtlasParameter> {
    final float scale;

    public MiniTextureAtlasLoader (FileHandleResolver resolver) {
        this(resolver,0.8f);
    }

    public MiniTextureAtlasLoader (FileHandleResolver resolver , float scale) {
        super(resolver);
        this.scale=scale;
    }

    TextureAtlasData data;

    @Override
    public TextureAtlas load (AssetManager assetManager, String fileName, FileHandle file, TextureAtlasParameter parameter) {
        for (Page page : data.getPages()) {
            Texture texture = assetManager.get(page.textureFile.path().replaceAll("\\\\", "/"), Texture.class);
            page.texture = texture;
        }

        TextureAtlas atlas = new TextureAtlas(data);
        Array<TextureAtlas.AtlasRegion> regions=atlas.getRegions();

        for (TextureAtlas.AtlasRegion region:regions){
            if(region.splits!=null)
                continue;

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

        data = null;
        return atlas;
    }

    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle atlasFile, TextureAtlasParameter parameter) {
        FileHandle imgDir = atlasFile.parent();

        if (parameter != null)
            data = new TextureAtlasData(atlasFile, imgDir, parameter.flip);
        else {
            data = new TextureAtlasData(atlasFile, imgDir, false);
        }

        Array<TextureAtlasData.Region> regions = data.getRegions();


        Array<AssetDescriptor> dependencies = new Array();
        for (Page page : data.getPages()) {
            TextureParameter params = new TextureParameter();
            params.format = page.format;
            params.genMipMaps = page.useMipMaps;
            params.minFilter = page.minFilter;
            params.magFilter = page.magFilter;
            dependencies.add(new AssetDescriptor(page.textureFile, Texture.class, params));
        }
        return dependencies;
    }

    static public class TextureAtlasParameter extends AssetLoaderParameters<TextureAtlas> {
        /** whether to flip the texture atlas vertically **/
        public boolean flip = false;

        public TextureAtlasParameter () {
        }

        public TextureAtlasParameter (boolean flip) {
            this.flip = flip;
        }
    }
}
