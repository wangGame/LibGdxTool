package com.kw.common.dict.radix;

import java.io.File;
import java.nio.file.Files;

public class App {
    public static void main(String[] args) {
        RadixTrie trie =
                new RadixTrie();

        try {
            Files.readAllLines(new File("D:\\java\\LibGdxTool\\assets\\dict\\word.txt").toPath())
                    .forEach(trie::insert);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



        System.out.println(
                trie.contains("apple")
        );


        System.out.println(
                trie.contains("app")
        );


        System.out.println(
                trie.contains("application")
        );
    }
}
