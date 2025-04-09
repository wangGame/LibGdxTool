package com.kw.gdx.loader;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.loader.bean.ArrayResult;
import com.kw.gdx.loader.bean.CsvBean;
import com.kw.gdx.loader.bean.CsvBeanParamter;
import com.kw.gdx.resource.csvanddata.CsvReader;
import com.kw.gdx.resource.csvanddata.demo.CsvUtils;

public class CsvLoader extends AsynchronousAssetLoader<ArrayResult, CsvBeanParamter> {
    private Array<? extends CsvBean> endCsv;
    private ArrayResult arrayResult;
    public CsvLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, CsvBeanParamter parameter) {
        arrayResult = new ArrayResult();
        arrayResult.array = CsvUtils.common(file, parameter.csvBean);
    }

    @Override
    public ArrayResult loadSync(AssetManager manager, String fileName, FileHandle file, CsvBeanParamter parameter) {
        return arrayResult;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, CsvBeanParamter parameter) {
        return null;
    }
}