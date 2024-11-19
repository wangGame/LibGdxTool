package com.kw.gdx.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.kw.gdx.resource.csvanddata.ConvertUtil;

import java.util.HashSet;

/**
 * file操作
 *
 * 使用方法
 *
 * eg
 * {@code
 * public class BoardFileOpetion extends BaseFileOpetion{
 *     private static BaseFileOpetion instance;
 *     public BoardFileOpetion() {
 *         super("woodfile/Board.txt");
 *     }
 *
 *     public static BaseFileOpetion getInstance(){
 *         if (instance == null) {
 *             instance = new BoardFileOpetion();
 *         }
 *         instance.updateLevel(201);
 *         return instance;
 *     }
 * }
 * }
 * ```
 */
public abstract class FileContentOpetion<T> {
    protected String saveFileName;
    private Array<T> valueArray;
    protected FileContentOpetion instance;

    protected FileContentOpetion(String saveFileName){
        this.saveFileName = saveFileName;
    }

    /**
     * over method
     * @return
     */
    protected FileHandle getLocalPlayLevelFileHandle(){
        return Gdx.files.local(saveFileName);
    }

    public Array<T> readFileArray(){
        return readFileArray(",");
    }

    public Array<T> readFileArray(String spltSub){
        if (valueArray == null) {
            valueArray = new Array<>();
        }else {
            valueArray.clear();
        }
        FileHandle local = getLocalPlayLevelFileHandle();
        if (local.exists()) {
            String string = local.readString();
            String[] s = string.split(spltSub);
            for (String s1 : s) {
                int i = ConvertUtil.convertToInt(s1, -1);
                if (i!=-1) {
                    valueArray.add((T) s1);
                }
            }
        }
        return valueArray;
    }

    public boolean updateLevel(T level) {
        return updateLevel(level,true);
    }

    public boolean updateLevel(T level,boolean isOnlyOne){
        saveArray(level);
        savePlaylevel();
        return true;
    }

    private void saveArray(T level) {
        if (valueArray ==null){
            readFileArray();
        }
        valueArray.removeValue(level,false);
        valueArray.add(level);
    }

    public void savePlaylevel(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < valueArray.size; i++) {
            T t = valueArray.get(i);
            if (i<valueArray.size-1){
                builder.append(t+",");
            }else {
                builder.append(t);
            }
        }
        FileHandle local = getLocalPlayLevelFileHandle();
        local.writeString(builder.toString(),false);
    }

    public void delete(T level){
        Array<T> ts = readFileArray();
        ts.removeValue(level,false);
        savePlaylevel();
    }
}
