package com.libGdx.test.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/7 10:55
 */
public class NetTest extends LibGdxTestMain {
    public static void main(String[] args) {
        NetTest netTest = new NetTest();
        netTest.start(netTest);
    }
//    https://content-puzzles.easybrain.com/api/v1/coord    cdn
//    https://content-puzzles.easybrain.com/api/v1/content/all_library_puzzles/data
//
//    https://content-puzzles.easybrain.com/api/v1/content/products
//    https://content-puzzles.easybrain.com/api/v1/library
//    https://content-puzzles.easybrain.com/api/v2/feature
//    https://content-puzzles.easybrain.com/api/v1/content/all_library_puzzles/data
//    https://content-puzzles.easybrain.com/api/v1/daily/all/by-month
//    https://content-puzzles.easybrain.com/api/v1/categories
//    https://content-puzzles.easybrain.com/api/v1/content/all_category_puzzles/data
//    https://content-puzzles.easybrain.com/api/v1/content/all_library_puzzles/data

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Net.HttpRequest request = new Net.HttpRequest();
        request.setUrl("https://egas.easybrain.com/api/v1/content");
        request.setMethod("GET");
        String sss = "localization_chinese_s";
//product_id=jigsaw_puzzle_038
        sss = "jigsaw_puzzles_store_item_tournaments";
        sss = "rate_us_loc";
        request.setHeader("x-easy-resource-id",sss);
//        request.setHeader("x-easy-resource-id","jigsaw_puzzles_store_item_tournaments");
//        request.setHeader("x-easy-ram","8984");
        request.setHeader("x-easy-eaid","100045");
        request.setHeader("x-easy-platform","android");
        request.setHeader("x-easy-app-version","3.9.1");
        request.setHeader("x-easy-locale","zhhans");


        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                System.out.println(httpResponse.getResultAsString());
            }

            @Override
            public void failed(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void cancelled() {
                System.out.println("cancel");
            }
        });
    }
}
