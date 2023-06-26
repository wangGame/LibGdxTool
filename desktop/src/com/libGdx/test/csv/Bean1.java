package com.libGdx.test.csv;

import com.kw.gdx.resource.csvanddata.Value;

/**
 * @Auther jian xian si qi
 * @Date 2023/6/26 18:22
 */

/**
 * 如果字段不一致的 可以使用Value注解
 */
public class Bean1 {
    /*    private int game_sort;
    private String level_id;
    private int level_num;
    private String version;*/
    @Value(value = "game_sort")
    private int gameSort;

    @Value(value = "level_id")
    private String levelId;

    @Value(value = "level_num")
    private int levelNum;

    @Value(value = "version")
    private String version;

    public int getGameSort() {
        return gameSort;
    }

    public void setGameSort(int gameSort) {
        this.gameSort = gameSort;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public int getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Bean1{" +
                "gameSort=" + gameSort +
                ", levelId='" + levelId + '\'' +
                ", levelNum=" + levelNum +
                ", version='" + version + '\'' +
                '}';
    }
}
