package com.tony.puzzle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.resource.csvanddata.ReadCvs;
import com.kw.gdx.utils.log.NLog;
import com.tony.puzzle.bean.CongfigBean;
import java.io.BufferedReader;
import java.io.InputStream;

public class CsvRead {
    public static void main(String[] args) {
        ReadCvs readCvs = new ReadCvs();
        FileHandle origin = Gdx.files.internal("origin");
        FileHandle out = Gdx.files.internal("out");
        for (FileHandle fileHandle : origin.list()) {
            Array<CongfigBean> array = new Array<>();
            FileHandle internal = Gdx.files.internal(fileHandle.path() + "/config.csv");
            String s = codeString(internal);
            readCvs.readMethodMethod(array, new BufferedReader(
                    Gdx.files.internal(fileHandle.path()+"/config.csv").reader(s)) , CongfigBean.class);
            WriteCsv writeCsv = new WriteCsv(Gdx.files.internal(out.path()+"/"+fileHandle.name()));
            writeCsv.write(array);
        }
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
