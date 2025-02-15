package com.kw.gdx.asset;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.AssetResource;
import com.kw.gdx.resource.annotation.FtResource;
import com.kw.gdx.resource.annotation.I18BundleAnnotation;
import com.kw.gdx.resource.annotation.SpineResource;
import com.kw.gdx.resource.annotation.TextureReginAnnotation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.spine.SkeletonData;
import com.kw.gdx.constant.Configuration;
import com.esotericsoftware.spine.loader.SkeletonDataLoader;
import com.kw.gdx.mini.MiniTextureAtlasLoader;
import com.kw.gdx.mini.MiniTextureLoader;
import com.kw.gdx.utils.log.NLog;
import com.ui.ManagerUIEditor;
import com.ui.loader.ManagerUILoader;
import com.ui.plist.MiniPlistAtlasLoader;
import com.ui.plist.PlistAtlas;
import com.ui.plist.PlistAtlasLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class Asset implements Disposable {
    private static Asset asset;
    private static AssetManager assetManager;
    private static AssetManager localAssetManager;
    private int i=0;
    private SkeletonRenderer renderer;
    private FrameBuffer frameBuffer;
    public void loadAsset(Object ob){
        loadAsset(ob,Asset.assetManager);
    }

    public void loadAsset(Object ob,AssetManager assetManager) {
        Field[] declaredFields = ob.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Annotation[] annotations = declaredField.getAnnotations();
            if (annotations.length>0) {
                Annotation annotation = annotations[0];
                if (annotation instanceof SpineResource){
                    SpineResource annotation1 = (SpineResource) annotation;
                    if (annotation1.isSpine()) {
                        try {
                            String s = (String) declaredField.get(ob);
                            if (assetManager.isLoaded(s)){
                                continue;
                            }
                            assetManager.load(s, SkeletonData.class);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }else {
                        try {
                            String s = (String) declaredField.get(ob);
                            if (assetManager.isLoaded(s)){
                                continue;
                            }
                            assetManager.load((String)declaredField.get(ob), ParticleEffect.class);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }else if(annotation instanceof FtResource){
                    FtResource annotation1 = (FtResource) annotation;
                    try {
                        BitmapFontLoader.BitmapFontParameter parameter = null;
                        parameter = new BitmapFontLoader.BitmapFontParameter();
                        parameter.genMipMaps = true;
                        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
                        parameter.magFilter = Texture.TextureFilter.Linear;
                        String value = annotation1.value();
                        if (assetManager.isLoaded(value)){
                            continue;
                        }
                        assetManager.load(value, BitmapFont.class,parameter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (annotation instanceof TextureReginAnnotation){
                    TextureReginAnnotation textureReginAnnotation = (TextureReginAnnotation)annotation;
                    String value = textureReginAnnotation.value();
                    if (assetManager.isLoaded(value)){
                        continue;
                    }
                    assetManager.load(value, TextureAtlas.class);
                }else if (annotation instanceof I18BundleAnnotation){
                    I18BundleAnnotation i18BundleAnnotation = (I18BundleAnnotation) annotation;
                    String value = i18BundleAnnotation.value();
                    if (assetManager.isLoaded(value)){
                        continue;
                    }
                    assetManager.load(i18BundleAnnotation.value(), I18NBundle.class);
                }else if (annotation instanceof AssetResource){

                }
            }
        }
    }

    public void getResource(Object ob){
        getResource(ob,Asset.assetManager);
    }

    public void getResource(Object ob,AssetManager assetManager){
        Field[] declaredFields = ob.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Annotation[] annotations = declaredField.getAnnotations();
            if (annotations.length>0) {
                Annotation annotation = annotations[0];
                if(annotation instanceof FtResource){
                    FtResource ftResource = ((FtResource)annotation);
                    try {
                        declaredField.set(ob,assetManager.get(ftResource.value(), BitmapFont.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (annotation instanceof TextureReginAnnotation){
                    TextureReginAnnotation reginAnnotation = (TextureReginAnnotation) annotation;
                    try {
                        declaredField.set(ob,assetManager.get(reginAnnotation.value(), TextureAtlas.class));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else if (annotation instanceof I18BundleAnnotation){
                    I18BundleAnnotation i18BundleAnnotation = (I18BundleAnnotation) annotation;
                    try {
                        declaredField.set(ob,assetManager.get(i18BundleAnnotation.value(), I18NBundle.class));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public PlistAtlas getPlist(String path){
        return getPlist(path,Asset.assetManager);
    }

    public PlistAtlas getPlist(String path,AssetManager assetManager){
        if (assetManager.isLoaded(path)) {
            assetManager.load(path, PlistAtlas.class);
            assetManager.finishLoading();
        }
        return assetManager.get(path,PlistAtlas.class);
    }

    public TextureAtlas getAtlas(String path){
        return getAtlas(path,Asset.assetManager);
    }

    public TextureAtlas getAtlas(String path,AssetManager assetManager){
        if (!assetManager.isLoaded(path)) {
            assetManager.load(path, TextureAtlas.class);
            assetManager.finishLoading();
        }
        TextureAtlas atlas = assetManager.get(path, TextureAtlas.class);
        for (Texture texture : atlas.getTextures()) {
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        }
        return atlas;
    }
//Gdx.files.local("levelpre/level" + levlNum+"/pre.png")

    public Texture getTexture(String path) {
        return getTexture(path,Asset.assetManager);
    }

    public Texture getTexture(String path,AssetManager assetManager){
        if (!Gdx.files.internal(path).exists()){
            NLog.e("%s resouce not exist",path);
            return null;
        }
        if (!assetManager.isLoaded(path)) {
            TextureLoader.TextureParameter parameter = new TextureLoader.TextureParameter();
            parameter.magFilter = Texture.TextureFilter.Linear;
            parameter.minFilter = Texture.TextureFilter.Linear;
            assetManager.load(path, Texture.class,parameter);
            assetManager.finishLoading();
        }
        Texture texture = assetManager.get(path, Texture.class);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return assetManager.get(path,Texture.class);
    }

    public Sprite getSprite(String path){
        return getSprite(path,Asset.assetManager);
    }

    public Sprite getSprite(String path,AssetManager assetManager){
        Texture texture = getTexture(path,assetManager);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Sprite sprite = new Sprite(texture);
        return sprite;
    }

    public void disposeTexture(String path){
        disposeTexture(path,Asset.assetManager);
    }

    public void disposeTexture(String path,AssetManager assetManager){
        if (assetManager.isLoaded(path)) {
            NLog.i("%s dispose ",path);
            assetManager.unload(path);
        }
    }

    private Asset(int type){
        i++;
        if (i>=2){
            throw new RuntimeException("gun");
        }
        if (type == 0 || type == 2) {
            assetManager = getAssetManager();
        }
        if (type == 1|| type == 2) {
            localAssetManager = getLocalAssetManager();
        }
    }

    public AssetManager getAssetManager(){
        if (assetManager == null){
            assetManager = new AssetManager();
            assetManager.setLoader(ManagerUIEditor.class,new ManagerUILoader(assetManager.getFileHandleResolver()));
            assetManager.setLoader(PlistAtlas.class, new PlistAtlasLoader(assetManager.getFileHandleResolver()));
            assetManager.setLoader(SkeletonData.class,new SkeletonDataLoader(assetManager.getFileHandleResolver()));
            if (Configuration.device_state == Configuration.DeviceState.poor) {
                assetManager.setLoader(TextureAtlas.class, new MiniTextureAtlasLoader(assetManager.getFileHandleResolver(), Configuration.scale));
                assetManager.setLoader(Texture.class, new MiniTextureLoader(assetManager.getFileHandleResolver(), Configuration.scale));
                assetManager.setLoader(PlistAtlas.class, new MiniPlistAtlasLoader(assetManager.getFileHandleResolver(), Configuration.scale));
            }
            ManagerUILoader.textureParameter.genMipMaps = false;
            ManagerUILoader.textureParameter.minFilter = Texture.TextureFilter.Linear;
            ManagerUILoader.textureParameter.magFilter = Texture.TextureFilter.Linear;

            ManagerUILoader.plistAtlasParameter.genMipMaps = false;
            ManagerUILoader.plistAtlasParameter.minFilter = Texture.TextureFilter.Linear;
            ManagerUILoader.plistAtlasParameter.magFilter = Texture.TextureFilter.Linear;

            ManagerUILoader.bitmapFontParameter.genMipMaps = false;
            ManagerUILoader.bitmapFontParameter.minFilter = Texture.TextureFilter.Linear;
            ManagerUILoader.bitmapFontParameter.magFilter = Texture.TextureFilter.Linear;
        }
        return assetManager;
    }

    public AssetManager getLocalAssetManager(){
        if (localAssetManager == null){
            localAssetManager = new AssetManager(new LocalFileHandleResolver());
            localAssetManager.setLoader(TiledMap.class,new TmxMapLoader());
            localAssetManager.setLoader(ManagerUIEditor.class,new ManagerUILoader(localAssetManager.getFileHandleResolver()));
            localAssetManager.setLoader(PlistAtlas.class, new PlistAtlasLoader(localAssetManager.getFileHandleResolver()));
            localAssetManager.setLoader(SkeletonData.class,new SkeletonDataLoader(localAssetManager.getFileHandleResolver()));
            if (Configuration.device_state == Configuration.DeviceState.poor) {
                localAssetManager.setLoader(TextureAtlas.class, new MiniTextureAtlasLoader(localAssetManager.getFileHandleResolver(), Configuration.scale));
                localAssetManager.setLoader(Texture.class, new MiniTextureLoader(localAssetManager.getFileHandleResolver(), Configuration.scale));
                localAssetManager.setLoader(PlistAtlas.class, new MiniPlistAtlasLoader(localAssetManager.getFileHandleResolver(), Configuration.scale));
            }
            ManagerUILoader.textureParameter.genMipMaps = false;
            ManagerUILoader.textureParameter.minFilter = Texture.TextureFilter.Linear;
            ManagerUILoader.textureParameter.magFilter = Texture.TextureFilter.Linear;

            ManagerUILoader.plistAtlasParameter.genMipMaps = false;
            ManagerUILoader.plistAtlasParameter.minFilter = Texture.TextureFilter.Linear;
            ManagerUILoader.plistAtlasParameter.magFilter = Texture.TextureFilter.Linear;

            ManagerUILoader.bitmapFontParameter.genMipMaps = false;
            ManagerUILoader.bitmapFontParameter.minFilter = Texture.TextureFilter.Linear;
            ManagerUILoader.bitmapFontParameter.magFilter = Texture.TextureFilter.Linear;
        }
        return localAssetManager;
    }

    public static Asset getAsset() {
        if (asset==null){
            asset = new Asset(Constant.ASSETMANAGERTYPE);
        }
        return asset;
    }


    public SkeletonRenderer getRenderer() {
        if (renderer == null){
            renderer = new SkeletonRenderer();
        }
        return renderer;
    }

    public BitmapFont loadBitFont(String path){
        return loadBitFont(path,Asset.assetManager);
    }

    public Label loadLabel(String path){
        Label label = new Label("",new Label.LabelStyle(){
            {
                font  = loadBitFont(path);
            }
        });
        label.setAlignment(Align.center);
        return label;
    }

    public Image getImage(String path){
        return new Image(Asset.getAsset().getTexture(path));
    }

    public BitmapFont loadBitFont(String path,AssetManager assetManager) {
        if (!assetManager.isLoaded(path)) {
            assetManager.load(path, BitmapFont.class);
            assetManager.finishLoading();
        }
        BitmapFont bitmapFont = assetManager.get(path);
        bitmapFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return assetManager.get(path);
    }

    public float getProcess(AssetManager assetManager){
        return assetManager.getProgress();
    }

    public float getProcess(){
        return getProcess(Asset.assetManager);
    }

    public boolean update(AssetManager assetManager){
        return assetManager.update();
    }

    public boolean update(){
        return update(Asset.assetManager);
    }


    public FrameBuffer buffer(){
        if (frameBuffer == null) {
//            Graphics.BufferFormat bufferFormat = Gdx.graphics.getBufferFormat();
//            Alpha, Intensity, LuminanceAlpha, RGB565, RGBA4444, RGB888, RGBA8888;
            Graphics.BufferFormat format = Gdx.graphics.getBufferFormat();
            if(format.r < 8){
                frameBuffer = new FrameBuffer(
                        Pixmap.Format.RGB565,
                        (int) Constant.GAMEWIDTH,
                        (int) Constant.GAMEHIGHT,
                        false);
            }else{
                try {
                    frameBuffer = new FrameBuffer(
                            Pixmap.Format.RGB888,
                            (int) Constant.GAMEWIDTH,
                            (int) Constant.GAMEHIGHT,
                            false);
                }catch (Exception e){
                    frameBuffer = new FrameBuffer(
                            Pixmap.Format.RGB565,
                            (int) Constant.GAMEWIDTH,
                            (int) Constant.GAMEHIGHT,
                            false);
                }
            }
        }
        return frameBuffer;
    }

    public void unloadResource(String path) {
        unloadResource(path,Asset.assetManager);
    }

    public void unloadResource(String path,AssetManager assetManager) {
        if (assetManager.isLoaded(path)){
            assetManager.unload(path);
        }
    }

    public Image createImg(String texture){
        return new Image(Asset.getAsset().getTexture(texture));
    }

    public Image createNineImg(String texture,int left,int right,int top,int bottom){
        return createNineImg(texture,left,right,top,bottom);
    }

    public Image createNineImg(String texture,int left,int right,int top,int bottom,boolean flipX,boolean flipY){
        return new Image(new NinePatch(
                new TextureRegion(Asset.getAsset().getTexture(texture)),
                left, right, top, bottom,flipX,flipY
        ));
    }



    @Override
    public void dispose() {
        if (assetManager!=null) {
            assetManager.dispose();
        }
        if (localAssetManager!=null){
            localAssetManager.dispose();
        }
        assetManager = null;
        localAssetManager = null;
        asset = null;
        if (frameBuffer!=null) {
            frameBuffer.dispose();
        }
        frameBuffer = null;
    }
}
