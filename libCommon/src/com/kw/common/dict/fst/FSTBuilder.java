package com.kw.common.dict.fst;

import java.util.*;


public class FSTBuilder {


    List<State> states =
            new ArrayList<>();


    Map<State,Integer> registry =
            new HashMap<>();



    public FSTBuilder(){

        states.add(new State(0));

    }



    public void add(String word,int value){

        int current=0;


        for(char c:word.toCharArray()){


            State state=states.get(current);


            Integer next =
                    state.arcs.get(c);


            if(next==null){

                next=states.size();

                states.add(new State(next));


                state.arcs.put(c,next);

            }


            current=next;

        }


        State end=states.get(current);

        end.finalState=true;

        end.output=value;

    }



    /**
     * 最小化
     */
    public void minimize(){


        for(int i=states.size()-1;i>=0;i--){


            State s=states.get(i);


            Integer same=
                    registry.get(s);



            if(same!=null){


                replaceState(i,same);


            }else{


                registry.put(s,i);

            }

        }

    }



    private void replaceState(
            int oldId,
            int newId){


        for(State s:states){

            for(
                    Map.Entry<Character,Integer> e:
                    s.arcs.entrySet()
            ){

                if(e.getValue()==oldId){

                    e.setValue(newId);

                }

            }

        }

    }



    public FST build(){

        minimize();

        return new FST(states);

    }

}
