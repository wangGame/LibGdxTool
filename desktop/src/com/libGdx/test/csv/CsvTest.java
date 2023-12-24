package com.libGdx.test.csv;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.resource.csvanddata.demo.CsvUtils;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/26 18:21
 */
public class CsvTest extends LibGdxTestMain {
    public static void main(String[] args) {
        CsvTest csvTest = new CsvTest();
        csvTest.start(csvTest);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Array<Bean1> common = CsvUtils.common("assets/csv/levelOrder.csv", Bean1.class);
        for (Bean1 bean : common) {
            System.out.println(bean);
        }
    }
}
