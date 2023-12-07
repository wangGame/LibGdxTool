package kw.test.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.kw.gdx.utils.PythonArray;
import com.kw.gdx.utils.PythonDict;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * @Auther jian xian si qi
 * @Date 2023/12/7 23:11
 */
public class JiexiJson extends LibGdxTestMain {
    public static void main(String[] args) {
        JiexiJson jiexiJson = new JiexiJson();
        jiexiJson.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        Json json = new Json();
        FileHandle internal = Gdx.files.internal("leveljson/2.json");
        String result = internal.readString();
        PythonDict root = json.fromJson(PythonDict.class, result);
        PythonDict data = (PythonDict) root.get("data");
        PythonArray paints = (PythonArray) data.get("paints");
        for (int i = 0; i < paints.size; i++) {
            PythonDict o = (PythonDict) paints.get(i);
            String id                   =(String) o.get("id");
            String  type                =(String) o.get("type");
            String thumbnail            =(String) o.get("thumbnail");
            String resoure              =(String) o.get("resource");
//            String categories           =(String) o.get("categories");
            String mode                 =(String) o.get("mode");
            String daily                =(String) o.get("daily");
            float unlock_cost          =(Float) o.get("unlock_cost");
            String schedule_type        =(String) o.get("schedule_type");
//            String major_content_tags   =(String) o.get("major_content_tags");

            System.out.println(id+" "+type+"   "+resoure);


        }

    }
}
