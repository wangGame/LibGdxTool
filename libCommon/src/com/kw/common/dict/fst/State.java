package com.kw.common.dict.fst;

import java.util.*;


public class State {


    int id;


    boolean finalState;


    int output;


    Map<Character,Integer> arcs
            = new TreeMap<>();


    public State(int id){
        this.id=id;
    }


    @Override
    public boolean equals(Object o){

        if(this==o)
            return true;


        if(!(o instanceof State))
            return false;


        State s=(State)o;


        return finalState==s.finalState
                &&
                output==s.output
                &&
                arcs.equals(s.arcs);

    }



    @Override
    public int hashCode(){

        return Objects.hash(
                finalState,
                output,
                arcs
        );
    }
}
