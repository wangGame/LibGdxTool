package com.ui.plist;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.graphics.Texture;

public class PlistAtlasParameter extends AssetLoaderParameters<PlistAtlas> {
    /** whether to generate mipmaps **/
    public boolean genMipMaps = false;
    public Texture.TextureFilter minFilter = Texture.TextureFilter.Nearest;
    public Texture.TextureFilter magFilter = Texture.TextureFilter.Nearest;
    public Texture.TextureWrap wrapU = Texture.TextureWrap.ClampToEdge;
    public Texture.TextureWrap wrapV = Texture.TextureWrap.ClampToEdge;
}
