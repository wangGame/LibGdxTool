package com.libGdx.test.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.kw.gdx.asset.Asset;
import com.kw.gdx.utils.ConvertUtil;
import com.libGdx.test.base.LibGdxTestMain;

/**
 * libgdx 和 cocos刚体坐标转换工具
 *
 *
 */
public class FileConvert extends LibGdxTestMain {
    //    libbody/woodbody.xml
    public static void main(String[] args) {
        FileConvert convert = new FileConvert();
        convert.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        String fileName = "assets/libbody/out";
        convertLibGdxFile(fileName);
        convertCocos(fileName);
    }

//
    public void convertLibGdxFile(String fileName){
        FileHandle internal = Gdx.files.internal(fileName+".xml");
        FileHandle outFile = Gdx.files.local(fileName+"out.xml");
        XmlReader xmlReader = new XmlReader();
        String xml = internal.readString("UTF-8");
        if (xml.charAt(0) == 65279)
            xml = xml.substring(1);
        XmlReader.Element element = xmlReader.parse(xml);
        for (int i = 0; i < element.getChildCount(); i++) {
            XmlReader.Element child = element.getChild(i);
            XmlReader.Element fixture = child.getChildByName("fixture");
            if (fixture == null)continue;
            Array<XmlReader.Element> polygon = fixture.getChildrenByName("polygon");
            if (polygon==null)continue;
            String name = child.getAttribute("name");
            System.out.println();
            float width = 0;
            float height = 0;

            if (Gdx.files.internal("assets/pic/"+name+".png").exists()){
                Texture texture = Asset.getAsset().getTexture("assets/pic/" + name + ".png");
                width = texture.getWidth();
                height = texture.getHeight();
            }

            for (XmlReader.Element element1 : polygon) {
                String text = element1.getText();
                String[] s = text.split(",");
                for (int i1 = 0; i1 < s.length; i1+=2) {
                    s[i1] = (ConvertUtil.convertToFloat(s[i1],0) - width/2.f)+"";
                    s[i1+1] = (ConvertUtil.convertToFloat(s[i1+1],0) - height/2.f)+"";
                }
                StringBuilder builder = new StringBuilder();
                for (String s1 : s) {
                    builder.append(s1);
                    builder.append(",");
                }
                element1.setText(builder.toString());
            }
        }
        outFile.writeString(element.toString(),false);
    }

    public void convertCocos(String fileName){
        FileHandle internal = Gdx.files.internal(fileName+".plist");
        FileHandle outFile = Gdx.files.local(fileName+"out.plist");
        XmlReader xmlReader = new XmlReader();
        String xml = internal.readString("UTF-8");
        if (xml.charAt(0) == 65279)
            xml = xml.substring(1);
        XmlReader.Element element = xmlReader.parse(xml);
        XmlReader.Element child3 = element.getChild(0);
        XmlReader.Element child = child3.getChild(7);
        String name = null;
//        System.out.println(child);
        for (int i = 0; i < child.getChildCount(); i++) {
            XmlReader.Element child1 = child.getChild(i);
            String text = child1.getText();
            float width = 0;
            float height = 0;
            if (Gdx.files.internal("assets/pic/"+name+".png").exists()){
                Texture texture = Asset.getAsset().getTexture("assets/pic/" + name + ".png");
                width = texture.getWidth();
                height = texture.getHeight();
            }

            if (text == null){
                XmlReader.Element child2 = child1.getChild(9);
                XmlReader.Element child4 = child2.getChild(0);
                XmlReader.Element child5 = child4.getChild(23);
                for (int i1 = 0; i1 < child5.getChildCount(); i1++) {
                    XmlReader.Element child6 = child5.getChild(i1);
                    for (int i2 = 0; i2 < child6.getChildCount(); i2++) {
                        XmlReader.Element child7 = child6.getChild(i2);
                        String text1 = child7.getText();
                        text1 = text1.replace("{","");
                        text1 = text1.replace("}","");
                        String[] split = text1.split(",");
                        float xx = ConvertUtil.convertToFloat(split[0],0);
                        float yy = ConvertUtil.convertToFloat(split[1],0);
                        xx = xx - width/2.0f;
                        yy = yy - height/2.0f;
                        StringBuilder builder = new StringBuilder();
                        builder.append("{");
                        builder.append(xx);
                        builder.append(",");
                        builder.append(yy);
                        builder.append("}");
                        child7.setText(builder.toString());
                    }
                }
            }else {
                System.out.println(text);
            }
        }
        outFile.writeString(element.toString(),false);
    }
}
