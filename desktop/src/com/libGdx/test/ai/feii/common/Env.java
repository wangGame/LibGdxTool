package com.libGdx.test.ai.feii.common;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class Env {
    private boolean allowStepBack;
    private Array actionRecorder;
    private int numPlayers;
    private int numActions;
    private long timeStep;
    private GameEnv game;

    public Env(){
        this.allowStepBack = false;
        this.actionRecorder = new Array();
//        this.numPlayers

    }

    private Array<Agent> agents;
    private void setDQN(Array<Agent> agents){
        this.agents = agents;
    }
    //
//    def set_agents(self, agents):
//    self.agents = agents



    public ResetResult reset(){
        ResetResult resetResult = game.initGame();
        actionRecorder = new Array();
        return new ResetResult();
    }

    public EnvStepResult step(String action,boolean rawAction){
        int decodeAction = 0;
        if (!rawAction){
            decodeAction = decodeAction(action);
        }
        timeStep+=1;
        actionRecorder.add(new RecoderBean(getPlayerId(),action));
        ResetResult stepResult = game.step(decodeAction + "");
        State state = stepResult.getState();
        int playerId = stepResult.getPlayerId();
        extractState(state);
        return new EnvStepResult();
    }

    public boolean stepBack(){
        if (!allowStepBack){
            return false;
        }
        if (!game.stepBack()){
            return false;
        }
        int playerId = getPlayerId();
        getState(playerId);
        return false;
    }

    public Array<Array<Object>> trajectories ;
    public void run(boolean isTraining) {
        trajectories = new Array<>();
        for (int i = 0; i < numPlayers; i++) {
            trajectories.add(new Array());
        }
        ResetResult reset = reset();
        State state = reset.getState();
        int playerId = reset.getPlayerId();
        trajectories.get(playerId).add(state);
        while (!isOver()) {
            String action;
            if (!isTraining){
                action = agents.get(playerId).eval_step(state);
            }else {
                action = agents.get(playerId).step(state);
            }

            EnvStepResult result = step(action,agents.get(playerId).useRaw());
            trajectories.get(playerId).add(action);

            state = reset.getState();
            playerId = reset().getPlayerId();

            if (!game.isOver()){
                trajectories.get(playerId).add(state);
            }

            for (int i = 0; i < numPlayers; i++) {
                State state1 = getState(i);
                trajectories.get(playerId).add(state1);
            }
        }
        int payOffs = getPayOffs();
        // trajectories   payOffs
    }

    public boolean isOver(){
        return game.isOver();
    }

    public int getPlayerId(){
        return game.getPlayerId();
    }

    public State getState(int playerId){
        return extractState(game.getState(playerId));
    }

    public int getPayOffs(){
        throw new RuntimeException();
    }

    public void getPrefectInformation(){
        throw new RuntimeException();
    }

    private HashMap<String, Integer> feature;
    public void getActionFeature(String action){
        feature = new HashMap<>();
        feature.put(action,1);
    }

    public void seed(){

    }

    public State extractState(State state){
        throw new RuntimeException();
    }

    public int decodeAction(String action){
        throw new RuntimeException();
    }

    public void getLegalActions(){
        throw new RuntimeException();
    }
}
