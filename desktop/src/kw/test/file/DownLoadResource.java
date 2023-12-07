package kw.test.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/7 22:44
 */
public class DownLoadResource extends LibGdxTestMain {
    private boolean finish = true;
    int index = 1;
    public static void main(String[] args) {
        DownLoadResource downLoadResource = new DownLoadResource();
        downLoadResource.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
    }

    @Override
    public void render() {
        super.render();
        if (index > 130)return;
        if (!finish)return;
        finish = false;
        Net.HttpRequest request = new Net.HttpRequest();
        request.setUrl(" https://jigsaw-api.dailyinnovation.biz/v1/library/?page="+index+"&page_size=100");
        request.setMethod("GET");
        index++;
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                String resultAsString = httpResponse.getResultAsString();
                System.out.println(resultAsString);
                FileHandle local = Gdx.files.local("leveljson/" + index + ".json");
                if (local != null) {
                    local.writeString(resultAsString,false);
                }
                finish = true;
            }

            @Override
            public void failed(Throwable t) {
                t.printStackTrace();
                finish = true;
            }

            @Override
            public void cancelled() {

            }
        });

    }
}
