package com.kw.common.dict.radix;

import java.util.ArrayList;
import java.util.List;


public class RadixNode {


    // 当前节点保存的字符串片段
    String text;


    // 是否一个完整单词
    boolean isWord;


    List<RadixNode> children;


    public RadixNode(String text) {
        this.text = text;
        children = new ArrayList<>();
    }
}