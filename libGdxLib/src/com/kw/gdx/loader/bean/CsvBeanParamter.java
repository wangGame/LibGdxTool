package com.kw.gdx.loader.bean;

import com.badlogic.gdx.assets.AssetLoaderParameters;

public class CsvBeanParamter<T> extends AssetLoaderParameters<ArrayResult<T>> {
    public Class<T> csvBean;
}
