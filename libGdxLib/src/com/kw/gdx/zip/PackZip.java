package com.kw.gdx.zip;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kw.gdx.file.FileUtils;
import com.kw.gdx.md5.PreMd5;
import com.kw.gdx.md5.TestMD5;
import com.kw.gdx.utils.log.NLog;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PackZip {
    private static void createzipFile(String source, String zipName) {
        File file = new File(source);
        File target1 = new File(zipName);
        if (!target1.exists()){
            target1.mkdirs();
        }
        try {
            for (File listFile : file.listFiles()) {
                NLog.i("deal file %s ",listFile.getPath());
                long start = System.currentTimeMillis();
                String targetZipName =zipName + listFile.getName()+".zip";
                FileOutputStream target = new FileOutputStream(targetZipName);
                ZipOutputStream out = new ZipOutputStream(target);
                getZipFile(out, listFile, "");
                out.close();
                long end = System.currentTimeMillis();
                NLog.i(listFile.getName() + " use time " + (end - start) +" ms");
            }
          } catch (IOException e) {
            throw new RuntimeException("创建zip文件失败", e);
        }
    }

    private static void getZipFile(ZipOutputStream out, File file, String pareFileName) {
        try {
            File files[] = file.listFiles();
            for (File dirFile : files) {
                String temPath = pareFileName;
                if (dirFile.isDirectory()) {
                    pareFileName += dirFile.getName() + File.separator;
                    ZipEntry zipEntry = new ZipEntry(pareFileName);
                    out.putNextEntry(zipEntry);
                    getZipFile(out, dirFile, pareFileName);
                } else {
                    pareFileName += dirFile.getName();
                    FileInputStream fi = new FileInputStream(dirFile);
                    BufferedInputStream origin = new BufferedInputStream(fi);
                    ZipEntry entry = new ZipEntry(pareFileName);
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read()) != -1) {
                        out.write(count);
                    }
                    origin.close();
                }
                pareFileName = temPath;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("压缩文件异常", e);
        }
    }
    public static void main(String[] args){
        PackZip.unpackZip("level","levelout");
    }

    /**
     * 复制文件
     */
    public static boolean copyOutToTemp(String from, String to){
        NLog.i("copyOutToTemp start from %s to %s",from,to);
//        String sourceFolderPath1 = "data\\origin\\cocos";
//        String targetFolderPath1 = "data\\md5";
        String sourceFolderPath1 = from;
        String targetFolderPath1 = to;
        File targetFile = new File(to);
        if (!targetFile.exists())targetFile.mkdirs();
        File file = new File(sourceFolderPath1);
        for (File listFile : file.listFiles()) {
            if (listFile.isDirectory()){
                copyOutToTemp(listFile.getPath(),to+File.separator+listFile.getName());
            }else {
                String sp = sourceFolderPath1 + File.separator + listFile.getName();
                String tp = targetFolderPath1 + File.separator + listFile.getName();
                try {
                    FileUtils.copyFile(sp, tp);
                } catch (IOException e) {
                    e.printStackTrace();
                    targetFile.delete();
                    file.delete();
                    return false;
                }
            }
        }
        targetFile.delete();
        NLog.i("copyOutToTemp end from %s to %s",from,to);
        return true;
    }

    public static boolean copyOutToTemp(String from, FileHandle to){
        NLog.i("copyOutToTemp start from %s to %s",from,to);
//        String sourceFolderPath1 = "data\\origin\\cocos";
//        String targetFolderPath1 = "data\\md5";
        String sourceFolderPath1 = from;

        File targetFile = to.file();
        if (!targetFile.exists())targetFile.mkdirs();
        File file = new File(sourceFolderPath1);
        to.mkdirs();
        for (File listFile : file.listFiles()) {
            if (listFile.isDirectory()){
                copyOutToTemp(listFile.getPath(),to+File.separator+listFile.getName());
            }else {
                String sp = sourceFolderPath1 + File.separator + listFile.getName();
                try {
                    FileUtils.copyFile1(sp, to);
                } catch (IOException e) {
                    e.printStackTrace();
                    targetFile.delete();
                    file.delete();
                    return false;
                }
            }
        }
        targetFile.delete();
        NLog.i("copyOutToTemp end from %s to %s",from,to);
        return true;
    }


    /**
     * md5处理
     */
    public static void genMd5(String from){
        NLog.i("genMd5 start from %s ",from);
        try {
            File file = new File(from);
            File[] files = file.listFiles();
            for (File listFile : files) {
                NLog.i("deal file : %s",listFile.getPath());
                File md5file = new File(listFile.getPath()
                        +File.separator+"md5apkold.txt");
                if (!md5file.exists())md5file.createNewFile();
                FileWriter fileWriter = new FileWriter(md5file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                PreMd5.outMd5(bufferedWriter, new File(listFile.getPath()));
                bufferedWriter.close();
                fileWriter.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 压缩
     * @param s
     * @param s1
     */
    public static void step3(String s, String s1){
        NLog.i("step3 start ");
        PackZip.createzipFile(s,s1);
        NLog.i("step3 end ");
    }

    /**
     * 解压
     */
    public static boolean unpackZip(String from, String to){
        File file = new File(from);
        if (file.isFile()){
            UnPackZip z = new UnPackZip();
            String[] split = file.getName().split("\\.");
            try {
                z.unZipFiles(file.getPath(), to + split[0] + File.separator);
                file.delete();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }else {
            for (File listFile : file.listFiles()) {
                unpackZip(listFile.getPath(),to);
            }
        }
        return true;
    }

    public static boolean check(FileHandle handle){
        FileHandle fileHandle;
        if (handle.type() == Files.FileType.Internal) {
            fileHandle = Gdx.files.internal(handle.path()+"/md5apkold.txt");
        }else {
            fileHandle = Gdx.files.local(handle.path()+"/md5apkold.txt");
        }
//        File file = new File(handle.path()+"/md5apkold.txt");
        try (BufferedReader reader = new BufferedReader(fileHandle.reader())){
            String s = null;
            while ((s = reader.readLine())!=null) {
                String[] s1 = s.split("\\t");
                FileHandle tempFile;
                if (handle.type() == Files.FileType.Internal) {
                    tempFile = Gdx.files.internal(handle.path()+"/"+s1[0]);
                }else {
                    tempFile = Gdx.files.local(handle.path()+"/"+s1[0]);
                }
                if (tempFile.name().endsWith(".csv"))continue;
                String md5ByFile = TestMD5.getMd5ByFile(tempFile);
                if (!md5ByFile.equals(s1[1])) {
                    NLog.e("%s check error!",s1[0]);
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean check(String source){
        File file = new File(source+"/md5apkold.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file));){
            String lineContent = null;
            while ((lineContent = reader.readLine())!=null) {
                String[] contentSplit = lineContent.split("\\t");
                String md5ByFile = TestMD5.getMd5ByFile(new File(source+"/"+contentSplit[0]));
                if (!md5ByFile.equals(contentSplit[1])) {
                    NLog.e("%s check error!",contentSplit[0]);
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void delete(String path){
        deleteDir(new File(path));
    }

    public static boolean deleteDir(FileHandle inputFile) {
        if (inputFile.type() == Files.FileType.Internal){
            NLog.i("brain error !!!");
            return false;
        }
        if (inputFile.isDirectory()) {
            for (FileHandle fileHandle : inputFile.list()) {
                if (fileHandle == null)continue;
                boolean success = deleteDir(fileHandle);
                if (!success){
                    NLog.i(fileHandle.name()+"delele error!");
                }
            }
        }
        if (inputFile.exists()) {
            if (inputFile.delete()) {
                return true;
            } else {
                NLog.e("dir delete error!");
                return false;
            }
        }
        return true;
    }

    public static boolean deleteDir(File inputFile) {
        if (inputFile.isDirectory()) {
            String[] childrenFilePath = inputFile.list();
            for (int i = 0; i < childrenFilePath.length; i++) {
                boolean success = deleteDir(new File(inputFile, childrenFilePath[i]));
                if (!success){
                    NLog.e(childrenFilePath[i]+" delele error!");
                }
            }
        }
        if (inputFile.exists()) {
            if (inputFile.delete()) {
                return true;
            } else {
                NLog.e("dir delete error!");
                return false;
            }
        }
        return true;
    }
}
