package com.kw.gdx.particle;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

public class ParticleEffectLoader extends SynchronousAssetLoader<ParticleEffect, ParticleEffectLoader.ParticleEffectParameter> {
    public ParticleEffectLoader (FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public ParticleEffect load (AssetManager am, String fileName, FileHandle file, ParticleEffectLoader.ParticleEffectParameter param) {
        ParticleEffect effect = new ParticleEffect();
        if (param != null && param.atlasFile != null)
            effect.load(file, am.get(param.atlasFile, TextureAtlas.class), param.atlasPrefix);
        else if (param != null && param.imagesDir != null)
            effect.load(file, param.imagesDir);
        else
            effect.load(file, file.parent());
        return effect;
    }

    @Override
    public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, ParticleEffectLoader.ParticleEffectParameter param) {
        Array<AssetDescriptor> deps = null;
        if (param != null && param.atlasFile != null) {
            deps = new Array();
            deps.add(new AssetDescriptor<TextureAtlas>(param.atlasFile, TextureAtlas.class));
        }
        return deps;
    }

    /** Parameter to be passed to {@link AssetManager#load(String, Class, AssetLoaderParameters)} if additional configuration is
     * necessary for the {@link com.badlogic.gdx.graphics.g2d.ParticleEffect}. */
    public static class ParticleEffectParameter extends AssetLoaderParameters<ParticleEffect> {
        /** Atlas file name. */
        public String atlasFile;
        /** Optional prefix to image names **/
        public String atlasPrefix;
        /** Image directory. */
        public FileHandle imagesDir;
    }
}
