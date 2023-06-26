package com.tony.puzzle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.resource.csvanddata.CsvResource;
import com.kw.gdx.resource.csvanddata.ReadCvs;
import com.kw.gdx.resource.csvanddata.WriteCsv;
import com.kw.gdx.utils.log.NLog;
import com.tony.puzzle.bean.CongfigBean;
import com.tony.puzzle.desktopnet.DeskDownload;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.charset.Charset;

import kw.artpuzzle.ArtPuzzle;

public class CsvRead {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title="TriPeaks";
        config.x = 0;
        config.y = 0;
        config.height = (int) (640*1.5F);
        config.width = (int) (360*1.5F);
        new LwjglApplication(new Game() {
            @Override
            public void create() {
                ReadCvs readCvs = new ReadCvs();
                FileHandle origin = Gdx.files.internal("origin");
                FileHandle out = Gdx.files.internal("out");
                for (FileHandle fileHandle : origin.list()) {
                    Array<Object> array = new Array<>();
                    FileHandle internal = Gdx.files.internal(fileHandle.path() + "/config.csv");
                    String s = codeString(internal);
                    readCvs.readMethodMethod(array, new BufferedReader(
                            Gdx.files.internal(fileHandle.path()+"/config.csv").reader(s)) , CongfigBean.class);
                    WriteCsv writeCsv = new WriteCsv(Gdx.files.internal(out.path()+"/"+fileHandle.name()));
                    writeCsv.write(array,CongfigBean.class);
                }
            }
        }, config);
    }

    public static String codeString(FileHandle handle) {
        try {

            InputStream bin = handle.read();
            int p = (bin.read() << 8) + bin.read();
            bin.close();
            String code = null;

            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
            }

            return code;
        }catch (Exception e){
            NLog.e("csv read error  --");
        }
        return "UTF-8";
    }

}
