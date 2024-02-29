package com.libGdx.test.xml;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlWriter;

import java.io.IOException;
import java.io.Writer;

public class XmlApp {
    public static void main(String[] args) {
        FileHandle file = new FileHandle("xml/example.xml");
        try {
            XmlWriter writer = new XmlWriter(file.writer(false));
            /**
             *
             * <root name="example">
             *   <item id="1">item 1</item>
             *   <item id="2">item 2</item>
             * </root>
             *
             **/
            writer.element("root").attribute("name", "example");
            writer.element("item").attribute("id", "1").text("item 1").pop();
            writer.element("item").attribute("id", "2").text("item 2").pop();

            writer.pop().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
