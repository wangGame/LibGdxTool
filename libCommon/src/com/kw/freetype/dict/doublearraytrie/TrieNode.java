package com.kw.freetype.dict.doublearraytrie;

import java.util.*;

public class TrieNode {

    Map<Integer, TrieNode> children =
            new TreeMap<>();

    boolean isWord;

}