package com.kw.gdx.zip;

import com.kw.gdx.utils.log.NLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnPackZip {
    public static void main(String[] args) {
        UnPackZip zip = new UnPackZip();
        try {
            zip.unZipFiles("data/out/level1.zip","data/unpack/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 解压到指定目录
     * @param zipPath
     * @param descDir
     * @author isea533
     */
    public static void unZipFiles(String zipPath,String descDir)throws IOException{
        File zipFile = new File(zipPath);
        File pathFile = new File(descDir);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for(Enumeration entries = zip.entries();entries.hasMoreElements();){
            ZipEntry entry = (ZipEntry)entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir+zipEntryName).replaceAll("\\*", "/");;
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if(!file.exists()){
                file.mkdirs();
                continue;
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if(new File(outPath).isDirectory()){
                continue;
            }
            //输出文件路径信息
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while((len=in.read(buf1))>0){
                out.write(buf1,0,len);
            }
            in.close();
            out.close();
        }
        zip.close();
    }
}
