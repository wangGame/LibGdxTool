package com.libGdx.test.asset.bean;

import com.kw.gdx.loader.bean.CsvBean;

public class CsvTest extends CsvBean {
    private int game_sort;
    private String level_id;
    private String level_num;
    private String version;

    public int getGame_sort() {
        return game_sort;
    }

    public void setGame_sort(int game_sort) {
        this.game_sort = game_sort;
    }

    public String getLevel_id() {
        return level_id;
    }

    public void setLevel_id(String level_id) {
        this.level_id = level_id;
    }

    public String getLevel_num() {
        return level_num;
    }

    public void setLevel_num(String level_num) {
        this.level_num = level_num;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
