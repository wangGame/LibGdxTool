package com.kw.common.dict.doublearraytrie;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;


public class DoubleArrayTrieBuilder {
    private int[] base =
            new int[1024];

    private int[] check =
            new int[1024];


    private boolean[] word =
            new boolean[1024];


    private boolean[] used =
            new boolean[1024];



    private int size = 0;



    public DoubleArrayTrieBuilder(){

        resize(1024);

    }



    private void resize(int newSize){

        base =
                Arrays.copyOf(
                        base,
                        newSize
                );

        check =
                Arrays.copyOf(
                        check,
                        newSize
                );

        word =
                Arrays.copyOf(
                        word,
                        newSize
                );

        used =
                Arrays.copyOf(
                        used,
                        newSize
                );
    }




    private void ensure(int index){

        if(index>=base.length){

            int newSize =
                    base.length*2;


            while(newSize<=index)
                newSize*=2;


            resize(newSize);

        }

    }




    /**
     * 构建
     */
    public void build(
            TrieNode root
    ){

        fetch(root);

        insert(
                Collections.singletonList(root),
                0
        );

    }




    /**
     * 找孩子
     */
    private List<Integer> fetch(
            TrieNode node
    ){

        List<Integer> result =
                new ArrayList<>();

        for(int key:
                node.children.keySet()){

            result.add(key);
        }

        return result;
    }




    private void insert(
            List<TrieNode> nodes,
            int index
    ){


        List<Integer> siblings =
                new ArrayList<>();


        for(TrieNode n:nodes){

            siblings.addAll(
                    fetch(n)
            );

        }


        if(siblings.size()==0){

            word[index]=
                    nodes.get(0).isWord;

            return;
        }



        int begin=0;


        while(true){

            begin++;


            boolean conflict=false;


            for(int c:siblings){


                int pos =
                        begin+c;


                ensure(pos);


                if(used[pos]){

                    conflict=true;
                    break;
                }

            }


            if(!conflict)
                break;

        }


        used[begin]=true;


        base[index]=begin;



        int childIndex=0;


        for(TrieNode node:nodes){


            for(
                    Map.Entry<Integer,TrieNode> e:
                    node.children.entrySet()
            ){


                int code=e.getKey();


                int pos=
                        begin+code;



                ensure(pos);


                check[pos]=index;


                used[pos]=true;


                childIndex++;


            }

        }



        for(
                Map.Entry<Integer,TrieNode> e:
                nodes.get(0).children.entrySet()
        ){

            int code=e.getKey();

            insert(
                    List.of(e.getValue()),
                    begin+code
            );

        }


    }




    public void save(
            String file
    )
            throws Exception{


        DataOutputStream out =
                new DataOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(file)
                        )
                );



        out.writeInt(base.length);



        for(int i:base)
            out.writeInt(i);


        for(int i:check)
            out.writeInt(i);


        for(boolean b:word)
            out.writeBoolean(b);



        out.close();

    }



}