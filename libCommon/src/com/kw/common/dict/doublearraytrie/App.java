package com.kw.common.dict.doublearraytrie;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class App {
    public static void main(String[] args) throws Exception {
        TrieNode root =
                new TrieNode();



        BufferedReader br =
                new BufferedReader(
                        new FileReader(
                                "assets\\dict\\word.txt"
                        )
                );



        String line;


        while(
                (line=br.readLine())!=null
        ){


            line=line.trim();


            if(line.length()==0)
                continue;



            TrieNode cur=root;



            for(char c:
                    line.toCharArray()){


                int code =
                        c-'a'+1;



                cur.children
                        .putIfAbsent(
                                code,
                                new TrieNode()
                        );


                cur =
                        cur.children.get(code);

            }



            cur.isWord=true;

        }


        br.close();



        DoubleArrayTrieBuilder builder =
                new DoubleArrayTrieBuilder();



        builder.build(root);



        builder.save(
                "dictionary.dat"
        );



        System.out.println(
                "finish"
        );


    }
}
