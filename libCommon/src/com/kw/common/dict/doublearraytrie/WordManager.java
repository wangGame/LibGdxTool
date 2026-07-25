package com.kw.common.dict.doublearraytrie;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;

public class WordManager {

    private static final String PRIMARY_DICTIONARY =
            "assets\\dict\\dictionary.dat";


    private static final String LEGACY_DICTIONARY =
            "dictionary.dat";


    private DoubleArrayTrie trie;



    public void init(){


        try{


            try(
                    FileInputStream inputStream =
                            openDictionaryStream()
            ){


                trie =
                        new DoubleArrayTrie(
                                inputStream
                        );

            }



        }catch(IOException e){

            throw new IllegalStateException(
                    "Failed to load dictionary data",
                    e
            );

        }

    }


    private FileInputStream openDictionaryStream()
            throws IOException {


        try{

            return new FileInputStream(PRIMARY_DICTIONARY);

        }catch(FileNotFoundException ignored){

        }


        try{

            return new FileInputStream(LEGACY_DICTIONARY);

        }catch(FileNotFoundException ignored){

        }


        throw new FileNotFoundException(
                "Dictionary file not found. Run App to generate " + PRIMARY_DICTIONARY
        );

    }




    public boolean check(String word){


        if(trie==null){

            return false;

        }


        return trie.contains(word);


    }


}