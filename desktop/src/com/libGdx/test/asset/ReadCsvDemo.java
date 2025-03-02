package com.libGdx.test.asset;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.loader.bean.ArrayResult;
import com.kw.gdx.loader.bean.CsvBeanParamter;
import com.libGdx.test.asset.bean.CsvTest;
import com.libGdx.test.base.LibGdxTestMain;

public class ReadCsvDemo extends LibGdxTestMain {
    public static void main(String[] args) {
        ReadCsvDemo readCsvDemo = new ReadCsvDemo();
        readCsvDemo.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        ArrayResult result = new ArrayResult();
        CsvBeanParamter csvBeanParamter = new CsvBeanParamter();
        csvBeanParamter.csvBean = CsvTest.class;
        Asset.getAsset().loadCsv("assets/csv/levelOrder.csv",csvBeanParamter);

        Asset.getAsset().getCsv("assets/csv/levelOrder.csv");
    }
}
