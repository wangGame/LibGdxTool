package com.kw.gdx.resource.introspector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.kw.gdx.utils.log.NLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    public static String appName = "unnamed";
    private static FileHandle sExtraAssetsHandle;

    public static FileHandle getUserWritableFile(String name) {
        FileHandle handle;
        if (PlatformUtils.isDesktop()) {
            handle = Gdx.files.external(".local/share/" + appName + "/" + name);
        } else {
            handle = Gdx.files.local(name);
        }
        return handle;
    }

    public static FileHandle getCacheDir() {
        FileHandle handle;
        if (PlatformUtils.isDesktop()) {
            handle = Gdx.files.external(".cache/" + appName);
        } else {
            if (!Gdx.files.isExternalStorageAvailable()) {
                return null;
            }
            handle = Gdx.files.absolute(Gdx.files.getExternalStoragePath() + "/" + appName);
        }
        handle.mkdirs();
        return handle;
    }

    public static void setExtraAssetsDir(String dir) {
        FileHandle handle = Gdx.files.absolute(dir);
        if (!handle.isDirectory()) {
            NLog.e("'%s' is not a directory", dir);
            return;
        }
        NLog.i("Set '%s' as the extra asset directory", handle.path());
        sExtraAssetsHandle = handle;
    }

    public static FileHandle assets(String path) {
        if (sExtraAssetsHandle != null) {
            FileHandle handle = sExtraAssetsHandle.child(path);
            if (handle.exists()) {
                return handle;
            }
        }
        return Gdx.files.internal(path);
    }

    public static XmlReader.Element parseXml(FileHandle handle) {
        XmlReader reader = new XmlReader();
        XmlReader.Element root = reader.parse(handle);
        if (root == null) {
            NLog.e("Failed to parse xml file from %s. No root element.", handle.path());
            return null;
        }
        return root;
    }

    public static String readUtf8(final InputStream in) throws IOException {
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[1024];
        while (true) {
            int length = reader.read(buffer);
            if (length == -1) {
                break;
            }
            sb.append(buffer, 0, length);
        }
        return sb.toString();
    }
}
