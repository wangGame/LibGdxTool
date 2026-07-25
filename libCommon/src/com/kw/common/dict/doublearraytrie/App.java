package com.kw.common.dict.doublearraytrie;

import java.io.BufferedReader;
import java.io.FileReader;

public class App {
    private static final String SOURCE_FILE =
            "assets\\dict\\word.txt";
    private static final String OUTPUT_FILE =
            "assets\\dict\\dictionary.dat";

    public static void main(String[] args) throws Exception {
        TrieNode root =
                new TrieNode();
        BufferedReader br =
                new BufferedReader(
                        new FileReader(
                                SOURCE_FILE
                        )
                );
        String line;


        while(
                (line=br.readLine())!=null
        ){


            line=line.trim();


            if(line.isEmpty())
                continue;



            TrieNode cur=root;



            for(char c:
                    line.toCharArray()){


                int code =
                        c-'a'+1;



                if(!cur.children.containsKey(code)){

                    cur.children.put(
                            code,
                            new TrieNode()
                    );

                }


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
                OUTPUT_FILE
        );



        System.out.println(
                "finish"
        );


    }
}
