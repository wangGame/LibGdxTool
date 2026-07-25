package com.kw.common.dict.fst;

import java.util.*;



public class FST {


    List<State> states;



    public FST(
            List<State> states
    ){

        this.states=states;

    }




    public Integer lookup(String word){


        int state=0;


        for(char c:word.toCharArray()){


            State s=states.get(state);



            Integer next=
                    s.arcs.get(c);



            if(next==null)
                return null;



            state=next;

        }


        State end=
                states.get(state);



        if(end.finalState)

            return end.output;


        return null;

    }

}
