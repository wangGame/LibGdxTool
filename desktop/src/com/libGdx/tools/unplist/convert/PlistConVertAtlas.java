package com.libGdx.tools.unplist.convert;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.io.*;

/**
 * plist转换为atlas
 */
public class PlistConVertAtlas {
    private String inputPath = "./ziyuan/plist/block.plist";
    private String outputPath = "./ziyuan/plist/";
    public void convert(){
        try {
            ConvertPlistAtlasOther plistAtlas = new ConvertPlistAtlasOther(inputPath);
            ConvertPlistAtlasOther.PlistAtlasData plistAtlasData = plistAtlas.getPlistAtlasData();
            String path = "tst.atlas";
            FileWriter writer = new FileWriter(outputPath+"/"+path);
            writer.append("\n");
            writer.append(path);
            writer.append("\n");
            TextureAtlas.TextureAtlasData.Page page = plistAtlasData.pages.get(0);
            writer.append("size: "+(int)page.width+","+(int)page.height);
            writer.append("\n");
            writer.append("format: "+page.format.name());
            writer.append("\n");
            writer.append("filter: "+page.minFilter+","+page.magFilter);
            writer.append("\n");
            writer.append("repeat: none");
            Array<TextureAtlas.TextureAtlasData.Region> regions = plistAtlasData.regions;
            for (TextureAtlas.TextureAtlasData.Region region : regions) {
                writer.append("\n");
                writer.append(region.name);
                writer.append("\n");
                writer.write("  rotate: "+region.rotate+"");
                writer.append("\n");
//                if (region.splits!=null) {
//                    writer.write("  xy: " + region.splits[0] + ", " + region.splits[1]);
//                    writer.append("\n");
//                }
                writer.write("  xy: "+region.top+", "+region.left);
                writer.append("\n");
                writer.write("  size: "+region.width+", "+region.height);
                writer.append("\n");
                writer.write("  orig: "+(int)region.originalWidth+", "+(int)region.originalHeight);
                writer.append("\n");
                writer.write("  offset: "+(int)region.offsetX+", "+(int)region.offsetY);
                writer.append("\n");
                writer.write("  index: "+region.index);
            }
            writer.flush();
            writer.close();
            System.out.println(plistAtlas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        PlistConVertAtlas plist = new PlistConVertAtlas();
        plist.convert();
    }
}
