package kw.artpuzzle.group;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.utils.log.NLog;

import kw.artpuzzle.asset.UnloadingFile;
import kw.artpuzzle.constant.LevelConfig;
import kw.artpuzzle.group.levelgroup.BgEnterAni;
import kw.artpuzzle.json.ReadJson;
import kw.artpuzzle.json.TextureInfo;

/**
 * 每一关的图片
 */
public class LevelPicGroup extends Group {
    private String jsonFileName;
    public LevelPicGroup(String jsonFileName, ArrayMap<Integer, String> tipLineMap){
        this.jsonFileName = jsonFileName;
        Object[] objects = tipLineMap.values;
        Array<String> tipLines = new Array<>();
        for (int i = 0; i < tipLineMap.size; i++) {
            String s = (String) objects[i];
            tipLines.add(s);
        }

        setSize(680,916);
        ReadJson json = new ReadJson();
        ArrayMap<String, TextureInfo> textureInfoArrayMap = json.readFile(jsonFileName+"layout.json");

        TextureAtlas atlasPic = null;
        TextureAtlas atlasLine = null;
        if (LevelConfig.levelNum == 1){
            atlasPic = Asset.getAsset().getAtlas(jsonFileName+"levelAtlas.atlas");
            atlasLine = Asset.getAsset().getAtlas(jsonFileName+"line.atlas");
        }else {
            if (LevelConfig.useInocal){
                atlasPic = Asset.getAsset().getAtlas(jsonFileName+"levelAtlas.atlas");
                atlasLine = Asset.getAsset().getAtlas(jsonFileName+"line.atlas");
            }else {
                atlasPic = Asset.getAsset().getLocalAtlas(jsonFileName+"levelAtlas.atlas");
                atlasLine = Asset.getAsset().getLocalAtlas(jsonFileName+"line.atlas");
            }
        }


        for (ObjectMap.Entry<String, TextureInfo> stringTextureInfoEntry : textureInfoArrayMap) {
            String key = stringTextureInfoEntry.key;
            TextureInfo value = stringTextureInfoEntry.value;
            try {
//                Image tempImage = new ShaderImage2(new SpriteDrawable(atlas.createSprite(key)));
                Image tempImage = null;
                if (tipLines.contains(key,false)) {
                    tempImage = new ShaderImage2(new SpriteDrawable(atlasLine.createSprite(key)));
                }else {
                    tempImage = new ShaderImage2(new SpriteDrawable(atlasPic.createSprite(key)));
                }
                tempImage.setSize(tempImage.getWidth(),tempImage.getHeight());
                tempImage.setPosition(value.getX(),value.getY(),Align.center);
                addActor(tempImage);
                System.out.println(key);
                tempImage.setName(key);
            }catch (Exception e){
                NLog.i(e);
            }
        }
    }

    public void bgAnimation(){
        BgEnterAni bgEnterAni = new BgEnterAni(getWidth(),getHeight()+2);
        addActor(bgEnterAni.getEnterGroup());
    }

    public void dispose(){
//        atlasPic = Asset.getAsset().getAtlas(jsonFileName+"levelAtlas.atlas");
//        atlasLine = Asset.getAsset().getAtlas(jsonFileName+"line.atlas");
//        Asset.getAsset().unloadResource(jsonFileName+"levelAtlas.atlas");
//        Asset.getAsset().unloadResource(jsonFileName+"line.atlas");
        UnloadingFile.unloadResource(jsonFileName+"levelAtlas.atlas");
        UnloadingFile.unloadResource(jsonFileName+"line.atlas");
    }
}
