package com.test.down.stream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        File file2 = new File("test.txt");
        file2.createNewFile();
        FileDownloadRandomAccessFile file21 = new FileDownloadRandomAccessFile(file2);


        File file1 = new File("test.txt");
        FileDownloadRandomAccessFile file = new FileDownloadRandomAccessFile(file1);
        file.setLength(10000);
        byte[] v = new byte[10];
        for (int i = 0; i < 10; i++) {
            v[i] = 1;
        }
        file.write(v,0,10);
        file.flushAndSync();

        file.close();
        renameFileName("test.txt","test.txt1");
        read();
        try {
            if (file != null) {
                try {
                    file.flushAndSync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            if (file != null)
                try {
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void renameFileName(String oldPath, String newPath) throws IOException {
        final File oldFile = new File(oldPath);
        try {
            final File newFile = new File(newPath);
            if (newFile.exists()) {
                if (!newFile.delete()) {
                    throw new IOException("Deletion Failed");
                }
            }
            if (!oldFile.renameTo(newFile)) {
                throw new IOException("Rename Failed");
            }
        } finally {
            if (oldFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                oldFile.delete();
            }
        }
    }
    private static void read() throws IOException {
        File file2 = new File("test.txt");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file2));
        byte[] b = new byte[20];
        bufferedInputStream.read(b);
        for (byte b1 : b) {
            System.out.print(b1+"  ");
        }
        System.out.println("  ");
    }
}
