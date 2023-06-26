package com.tony.puzzle;

import kw.artpuzzle.json.ReadJson;

public class DealJson {
    public static void main(String[] args) {
        ReadJson json = new ReadJson();
        String path [] ={
                "level6",
        };
        for (String s : path) {
            json.deal("data/downlevel/json/src/"+s+"/layout.json",s);
        }
    }
}
