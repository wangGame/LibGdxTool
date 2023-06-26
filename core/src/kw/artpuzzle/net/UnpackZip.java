package kw.artpuzzle.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnpackZip {
    public static void unzip(String path, String savePath) {
        try {
            File file = Gdx.files.local(path).file();
            ZipFile zipFile = new ZipFile(file);
            Enumeration e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                InputStream is = zipFile.getInputStream(entry);
                String entryName = entry.getName();
                FileHandle local = Gdx.files.local(savePath + entryName);
                File dstFile = local.file();
                if (entryName.endsWith("/")) {
                    continue;
                }
                if (!local.parent().exists()) {
                    local.parent().mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(dstFile);
                byte[] buffer = new byte[8192];
                int count;
                while ((count = is.read(buffer, 0, buffer.length)) != -1) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            zipFile.close();
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
