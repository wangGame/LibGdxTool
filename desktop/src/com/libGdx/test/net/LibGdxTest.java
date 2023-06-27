package com.libGdx.test.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/27 13:28
 */
public class LibGdxTest extends LibGdxTestMain {
    public static void main(String[] args) {
        LibGdxTest test = new LibGdxTest();
        test.start(test);
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        http://gaoshanren.cdn-doodlemobile.com/Art_Puzzle/level_resource/version7/level/level160.zip
        Net.HttpRequest request = new Net.HttpRequest();
        request.setUrl("https://gaoshanren.cdn-doodlemobile.com/Art_Puzzle/level_resource/version7/level/level160.zip");
        request.setMethod(Net.HttpMethods.GET);
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                byte[] result = httpResponse.getResult();
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        FileHandle fileHandle = Gdx.files.local("xxxx.zip");
                        System.out.println(fileHandle.file().getAbsoluteFile());
                        fileHandle.writeBytes(result,false);
                    }
                });
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("failed");
                t.printStackTrace();
            }

            @Override
            public void cancelled() {
                System.out.println("cancel");
            }
        });

    }
}
