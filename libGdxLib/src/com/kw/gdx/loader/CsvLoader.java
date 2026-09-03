package com.kw.gdx.loader;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.loader.bean.ArrayResult;
import com.kw.gdx.loader.bean.CsvBeanParamter;
import com.kw.gdx.resource.csvanddata.demo.CsvUtils;

public class CsvLoader<T> extends AsynchronousAssetLoader<ArrayResult<T>, CsvBeanParamter<T>> {
    private ArrayResult<T> arrayResult;
    public CsvLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, CsvBeanParamter<T> parameter) {
        if (parameter == null || parameter.csvBean == null) {
            throw new IllegalArgumentException("CSV bean class is required: " + fileName);
        }
        arrayResult = new ArrayResult<>();
        arrayResult.array = CsvUtils.common(file, parameter.csvBean);
    }

    @Override
    public ArrayResult<T> loadSync(AssetManager manager, String fileName, FileHandle file, CsvBeanParamter<T> parameter) {
        return arrayResult;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, CsvBeanParamter<T> parameter) {
        return null;
    }
}
