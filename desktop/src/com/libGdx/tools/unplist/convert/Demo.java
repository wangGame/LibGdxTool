package com.libGdx.tools.unplist.convert;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.FileHandler;

public class Demo {

    public static void main(String[] args) {
        System.out.println("----------");

        TextureAtlas atlas = new TextureAtlas("./ziyuan/plist/brick.atlas");
        TextureAtlas.TextureAtlasData data = atlas.getData();
        Array<TextureAtlas.TextureAtlasData.Page> pages = data.pages;


//
        StringWriter writer = new StringWriter();
        XmlWriter xml = new XmlWriter(writer);
        writer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        writer.append("\n");
        writer.append("<!DOCTYPE plist PUBLIC \"-//Apple Computer//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">");
        writer.append("\n");
        try {
            XmlWriter element = xml.element("plist")
                    .attribute("version", "1.0")
                    .element("dict")
                    .element("key").text("frames").pop()
                    .element("dict");
            for (TextureAtlas.TextureAtlasData.Region region : data.regions) {
                element.element("key")
                        .text(region.name+".png").pop()
                        .element("dict")
                        .element("key")
                        .text("frame")
                        .pop()
                        .element("string")
                        .text("{{"+(int)region.top+","+(int)region.left+"},{"+region.width+""+region.height+"}}")
                        .pop()
                        .element("key")
                        .text("offset")
                        .pop()
                        .element("string")
                        .text("{"+(int)region.offsetX+","+(int)region.offsetY+"}")
                        .pop()
                        .element("key")
                        .text("ritated")
                        .pop()
                        .element(region.rotate+"")
                        .pop()
                        .element("key")
                        .text("sourceSize")
                        .pop()
                        .element("string")
                        .text("{"+region.width+""+region.height+"}")
                        .pop().pop();
            }
            element.pop()
                    .element("key")
                    .text("metadata")
                    .pop()
                    .element("dict")
                    .element("key")
                    .text(data.getPages().get(0).format.name())
                    .pop()
                    .element("integer")
                    .text(2)
                    .pop()
                    .element("key")
                    .text("textureFileName")
                    .pop()
                    .element("string")
                    .text(data.pages.get(0).textureFile.name())
                    .pop()
                    .element("realTextureFileName")
                    .text(data.pages.get(0).textureFile.name())
                    .pop()
                    .element("key")
                    .text("size")
                    .pop()
                    .element("string")
                    .text("{"+data.pages.get(0).width+","+data.pages.get(0).height+"}")
                    .pop()
                    .pop()
                    .element("key")
                    .text("texture")
                    .pop()
                    .element("dict")
                    .element("key")
                    .text("width")
                    .pop()
                    .element("integer")
                    .text(data.pages.get(0).width)
                    .pop()
                    .element("key")
                    .text("height")
                    .pop()
                    .element("integer")
                    .text(data.pages.get(0).height)
                    .pop()
            .pop().pop().pop();
//
////            element
////                    .element("key").text("")
////                    .pop()
////                    .pop()
////                    .pop();
////            xml.flush();
////            xml.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//
        FileHandle handle = new FileHandle("ziyuan/tezz");
        handle.writeString(writer.toString(),false);

    }
//
//    public static void main(String[] args) {
//
//        TextureAtlas atlas = new TextureAtlas("brick.atlas");
//        TextureAtlas.TextureAtlasData data = atlas.getData();
//        Array<TextureAtlas.TextureAtlasData.Page> pages = data.pages;
//
//
//
//
//        StringWriter writer = new StringWriter();
//        XmlWriter xml = new XmlWriter(writer);
//        try {
//            xml.element("meow")
//                    .attribute("moo", "cow")
//                    .element("child")
//                    .attribute("moo", "cow")
//                    .element("child")
//                    .attribute("moo", "cow")
//                    .text("XML is like violence. If it doesn't solve your problem, you're not using enough of it.")
//                    .pop()
//                    .pop()
//                    .pop();
//            xml.flush();
//            xml.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(writer);
//    }
}

