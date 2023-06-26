package com.esotericsoftware.spine.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

public class SkeletonDataLoader extends AsynchronousAssetLoader<SkeletonData, SkeletonDataLoader.SkeletonDataParameter> {

    String atlasFileName;
    String skeltype;
    float scale;

    SkeletonData skeletonData;

    public SkeletonDataLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SkeletonDataParameter parameter) {
        Array<AssetDescriptor> dependencies = new Array<AssetDescriptor>();
        if (file.extension().equalsIgnoreCase("json"))
            skeltype = "json";
        else
            skeltype = "binary";
        scale = 1;
        atlasFileName = file.pathWithoutExtension() + ".atlas";

        if (parameter != null) {
            if (parameter.atlasfile != null && parameter.atlasfile.length() > 0)
                atlasFileName = parameter.atlasfile;
            if (parameter.spinetype != null) skeltype = parameter.spinetype;
            scale = parameter.scale;
        }
        if (!Gdx.files.internal(atlasFileName).exists()) {
            return dependencies;
        }
        dependencies.add(new AssetDescriptor<TextureAtlas>(atlasFileName, TextureAtlas.class, null));
        return dependencies;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, SkeletonDataParameter parameter) {
        skeletonData = new SkeletonData();
        TextureAtlas textureAtlas = null;
        if (manager.isLoaded(atlasFileName, TextureAtlas.class)) {
            textureAtlas = manager.get(atlasFileName, TextureAtlas.class);
        }
        if (skeltype.equals("json")) {
            SkeletonJson skeletonJson = new SkeletonJson(new DDAtlasAttachmentLoader(textureAtlas));
            skeletonJson.setScale(scale);
            skeletonData = skeletonJson.readSkeletonData(file);
        } else if (skeltype.equals("binary")) {
            SkeletonBinary skeletonBinary = new SkeletonBinary(textureAtlas);
            skeletonBinary.setScale(scale);
            skeletonData = skeletonBinary.readSkeletonData(file);
        } else throw new GdxRuntimeException("SPINE ERROR");
    }

    @Override
    public SkeletonData loadSync(AssetManager manager, String fileName, FileHandle file, SkeletonDataParameter parameter) {
        return skeletonData;
    }

    static public class SkeletonDataParameter extends AssetLoaderParameters<SkeletonData> {
        public String atlasfile;
        public String spinetype;
        public float scale = 1;

        public SkeletonDataParameter(float scale) {
            this.scale = scale;
        }
        public SkeletonDataParameter() {
        }

    }
}
