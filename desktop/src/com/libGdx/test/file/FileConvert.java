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

public class FileConvert extends LibGdxTestMain {
    //    libbody/woodbody.xml
    public static void main(String[] args) {
        FileConvert convert = new FileConvert();
        convert.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        convert();
    }

    public void convert(){
        FileHandle internal = Gdx.files.internal("assets/libbody/out.plist.xml");
        FileHandle outFile = Gdx.files.local("woodbody.xml");
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
}
