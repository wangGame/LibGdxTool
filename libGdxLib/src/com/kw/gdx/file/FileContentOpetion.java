package com.kw.gdx.file;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ArrayMap;
import com.kw.gdx.resource.csvanddata.ConvertUtil;

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
public abstract class FileContentOpetion {
    protected String nomalFileName;
    private ArrayMap<Integer,Integer> oldNewLevel;
    protected FileContentOpetion instance;
    protected FileContentOpetion(String nomalFileName){
        this.nomalFileName = nomalFileName;
    }

    private FileHandle getLocalPlayLevelFileHandle(){
        return Gdx.files.local(nomalFileName);
    }

    public ArrayMap<Integer,Integer> readFile(){
        oldNewLevel = new ArrayMap<>();
        FileHandle local = getLocalPlayLevelFileHandle();
        if (local.exists()) {
            String string = local.readString();
            String[] s = string.split(",");
            for (String s1 : s) {
                int i = ConvertUtil.convertToInt(s1, -1);
                if (i!=-1) {
                    oldNewLevel.put(i, i);
                }
            }
        }
        return oldNewLevel;
    }


    public boolean updateLevel(int level){
        if (oldNewLevel ==null){
            readFile();
        }
        if (oldNewLevel.containsKey(level)) {
            oldNewLevel.removeKey(level);
        }
        oldNewLevel.put(level,level);
        savePlaylevel();
        return true;
    }


    public void savePlaylevel(){
        StringBuilder builder = new StringBuilder();
        int size = oldNewLevel.size;
        Object[] keys = oldNewLevel.keys;
        for (int i = 0; i < size; i++) {
            Object key = keys[i];
            if (key instanceof Integer) {
                builder.append(key+",");
            }
        }
        if (builder.length()>0) {
            char c = builder.charAt(builder.length() - 1);
            if (c == ',') {
                builder.delete(builder.length()-1,builder.length());
            }
        }
        FileHandle local = getLocalPlayLevelFileHandle();
        local.writeString(builder.toString(),false);
    }

    public void delete(int level){
        ArrayMap<Integer, Integer> arrayMap = readFile();
        arrayMap.removeKey(level);
        savePlaylevel();
    }
}
