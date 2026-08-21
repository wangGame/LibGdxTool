package com.kw.common.dict.radix;

import java.util.ArrayList;

public class RadixTrie {


    private final RadixNode root =
            new RadixNode("");



    public void insert(String word){

        insert(root, word);

    }



    private void insert(RadixNode node,String word){


        for(RadixNode child:node.children){


            int common = commonPrefix(
                    child.text,
                    word
            );


            if(common==0)
                continue;



            // 完全匹配
            if(common == child.text.length()){


                if(common == word.length()){

                    child.isWord=true;

                }else{

                    insert(
                            child,
                            word.substring(common)
                    );

                }

                return;
            }



            // 需要拆节点

            String oldRemain =
                    child.text.substring(common);


            RadixNode oldChild =
                    new RadixNode(oldRemain);


            oldChild.isWord =
                    child.isWord;


            oldChild.children =
                    child.children;



            child.text =
                    child.text.substring(0,common);


            child.isWord =
                    word.length()==common;


            child.children =
                    new ArrayList<>();


            child.children.add(oldChild);



            if(word.length()>common){

                RadixNode newChild =
                        new RadixNode(
                                word.substring(common)
                        );

                newChild.isWord=true;


                child.children.add(newChild);
            }


            return;

        }


        // 没有匹配

        RadixNode newNode =
                new RadixNode(word);


        newNode.isWord=true;


        node.children.add(newNode);

    }




    private int commonPrefix(
            String a,
            String b
    ){

        int len=Math.min(
                a.length(),
                b.length()
        );


        int i=0;

        while(i<len &&
                a.charAt(i)==b.charAt(i)){

            i++;
        }


        return i;
    }



    public boolean contains(String word){

        return search(root,word);

    }



    private boolean search(
            RadixNode node,
            String word
    ){


        for(RadixNode child:node.children){


            if(word.startsWith(child.text)){


                if(word.length()==child.text.length()){

                    return child.isWord;

                }


                return search(
                        child,
                        word.substring(
                                child.text.length()
                        )
                );

            }

        }


        return false;
    }

}