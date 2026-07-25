package com.kw.common.dict.doublearraytrie;


import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;

public class WordManager {


    private DoubleArrayTrie trie;



    public void init(){


        try{


            FileInputStream inputStream = new FileInputStream("assets\\dict\\word.txt");


            trie =
                    new DoubleArrayTrie(
                            inputStream
                    );



        }catch(Exception e){

            e.printStackTrace();

        }

    }




    public boolean check(String word){


        return trie.contains(word);


    }


}