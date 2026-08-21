package com.kw.common.dict.doublearraytrie;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;


public class DoubleArrayTrie {

    public static final int MAGIC =
            0x44415431;


    private static final int MAX_TRIE_SIZE =
            10_000_000;


    private int[] base;

    private int[] check;

    private boolean[] word;



    /**
     * 从dat读取
     */
    public DoubleArrayTrie(InputStream input)
            throws IOException {


        load(input);

    }



    private void load(InputStream input)
            throws IOException {


        DataInputStream in =
                new DataInputStream(input);



        int headerOrSize =
                in.readInt();


        int size;


        if(headerOrSize==MAGIC){

            size=
                    in.readInt();

        }else{

            size=
                    headerOrSize;

        }


        validateSize(
                size,
                in.available()
        );



        base =
                new int[size];


        check =
                new int[size];


        word =
                new boolean[size];



        for(int i=0;i<size;i++){

            base[i]=in.readInt();

        }



        for(int i=0;i<size;i++){

            check[i]=in.readInt();

        }



        for(int i=0;i<size;i++){

            word[i]=in.readBoolean();

        }



        in.close();

    }


    private void validateSize(
            int size,
            int remainingBytes
    ) throws IOException {


        if(size<=0||size>MAX_TRIE_SIZE){

            throw new IOException(
                    "Invalid trie size: "+size
            );

        }


        if(remainingBytes>0){

            long expectedBytes =
                    (long)size*Integer.BYTES*2L+
                            size;


            if(expectedBytes>remainingBytes){

                throw new IOException(
                        "Invalid trie file, expected at least "+expectedBytes+
                                " bytes but only "+remainingBytes+" remain"
                );

            }

        }

    }




    /**
     * 查询单词
     */
    public boolean contains(String text){


        int state=0;



        for(int i=0;i<text.length();i++){


            char c =
                    text.charAt(i);



            int code =
                    c-'a'+1;


            if(code<=0){

                return false;

            }



            int next =
                    base[state]+code;



            // 越界
            if(next<0||next>=check.length){

                return false;

            }



            // 父节点不匹配
            if(check[next]!=state){

                return false;

            }



            state=next;


        }



        return word[state];

    }

}
