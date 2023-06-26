package com.kw.gdx.mini;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

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

/**
 * {@link AssetLoader} for {@link Texture} instances. The pixel data is loaded asynchronously. The texture is then created on the
 * rendering thread, synchronously. Passing a {@link TextureLoader.TextureParameter} to
 * {@link AssetManager#load(String, Class, AssetLoaderParameters)} allows one to specify parameters as can be passed to the
 * various Texture constructors, e.g. filtering, whether to generate mipmaps and so on.
 *
 * @author mzechner
 */
public class MiniTextureLoader extends AsynchronousAssetLoader<Texture, TextureLoader.TextureParameter> {
    static public class TextureLoaderInfo {
        String filename;
        MiniFileTextureData data;
        Texture texture;
    }

    ;

    TextureLoaderInfo info = new TextureLoaderInfo();
    final float scale;

    public MiniTextureLoader(FileHandleResolver resolver) {
        this(resolver, 0.5f);
    }

    public MiniTextureLoader(FileHandleResolver resolver, float scale) {
        super(resolver);
        this.scale = scale;
        MiniFileTextureData.copyToPOT = true;
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        info.filename = fileName;
        if (parameter == null || parameter.textureData == null) {
            Pixmap pixmap = null;
            Format format = null;
            boolean genMipMaps = false;
            info.texture = null;

            if (parameter != null) {
                format = parameter.format;
                genMipMaps = false;
                info.texture = parameter.texture;
            }

            info.data = new MiniFileTextureData(file, new Pixmap(file), format, false, scale);
        } else {
            throw new GdxRuntimeException("他妈的孔亚通");
//			info.data = parameter.textureData;
//			info.texture = parameter.texture;
        }
        if (!info.data.isPrepared()) info.data.prepare();
    }

    @Override
    public Texture loadSync(AssetManager manager, String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        if (info == null) return null;
        Texture texture = info.texture;
        if (texture != null) {
            texture.load(info.data);
        } else {
            // 2017年1月3日修改**********************************************************************
            // ******************************原方法*************************************************
//			texture = new Texture(info.data);
// ************************************************************************************
// import com.badlogic.gdx.math.MathUtils;
// ************************************************************************************
            //解决非2的n次图片用不了
            int w0 = info.data.getWidth();
            int h0 = info.data.getHeight();
            if (Gdx.gl20 != null || (MathUtils.isPowerOfTwo(w0) && MathUtils.isPowerOfTwo(h0))) {
                texture = new Texture(info.data);
            } else {
                int w1 = MathUtils.nextPowerOfTwo(w0);
                int h1 = MathUtils.nextPowerOfTwo(h0);
                texture = new Texture(w1, h1, Format.RGBA8888);
                if (!info.data.isPrepared()) info.data.prepare();
                texture.draw(info.data.consumePixmap(), 0, h1 - h0);
            }
// ***************************
        }
        if (parameter != null) {
            texture.setFilter(parameter.minFilter, parameter.magFilter);
            texture.setWrap(parameter.wrapU, parameter.wrapV);
        }
        return texture;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        return null;
    }

}

