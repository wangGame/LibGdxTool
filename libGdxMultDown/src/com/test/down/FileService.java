package com.test.down;

import com.kw.gdx.file.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public  class FileService {
    public FileService() {
    }

    public Map<Integer, Integer> getData(String path) {
        return new HashMap<>();
    }

    /**
     * 保存每条线程已经下载的文件长度
     *  @param  path
     *  @param  map
     */
    public  void save(String path,  Map<Integer, Integer> map) { // int threadid, int position
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {

        }
    }

    /**
     * 实时更新每条线程已经下载的文件长度
     *  @param  path
     *  @param  map
     */
    public  void update(String path, Map<Integer, Integer> map){
        for(Map.Entry<Integer, Integer> entry : map.entrySet()) {

        }
    }

    /**
     * 当文件下载完成后，删除对应的下载记录
     *  @param  path
     */
    public  void delete(String path){
        JsonUtils.delete(path);
    }
}