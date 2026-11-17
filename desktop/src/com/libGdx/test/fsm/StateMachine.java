package com.libGdx.test.fsm;

import com.libGdx.test.fsm.state.DownState;
import com.libGdx.test.fsm.state.LeftState;
import com.libGdx.test.fsm.state.RightState;
import com.libGdx.test.fsm.state.UpState;
import com.libGdx.test.fsm.state.base.State;

import java.util.HashMap;
import java.util.Map;

public class StateMachine {
    private Map<Direction,IParamesValue> parames;
    private Map<Direction, State> stateMachines;
    private State current;
    public StateMachine(){
        this.parames = new HashMap<>();
        this.stateMachines = new HashMap<>();
        initState();
        initParam();
    }

    private void initParam() {
        this.parames.put(Direction.UP,new IParamesValue());
        this.parames.put(Direction.DOWN,new IParamesValue());
        this.parames.put(Direction.RIGHT,new IParamesValue());
        this.parames.put(Direction.LEFT,new IParamesValue());
    }

    private void initState() {
        this.stateMachines.put(Direction.UP,new UpState());
        this.stateMachines.put(Direction.DOWN,new DownState());
        this.stateMachines.put(Direction.RIGHT,new RightState());
        this.stateMachines.put(Direction.LEFT,new LeftState());
    }

    public void setParams(Direction key,boolean value){
        if (this.parames.containsKey(key)) {
            this.parames.get(key).setActive(value);
            this.run();
        }
    }

    private void run() {
//        if (this.current == this.stateMachines.get(Direction.UP)) {
//            this.current = this.stateMachines.get(Direction.UP);
//        }else if (this.current == this.stateMachines.get(Direction.LEFT)) {
//            this.current = this.stateMachines.get(Direction.LEFT);
//        }else if (this.current == this.stateMachines.get(Direction.RIGHT)) {
//            this.current = this.stateMachines.get(Direction.RIGHT);
//        }else if (this.current == this.stateMachines.get(Direction.DOWN)) {
//            this.current = this.stateMachines.get(Direction.DOWN);
//        }else {
//
//        }
        parameState();
    }

    public void setCurrent(State current) {
        this.current = current;
        this.current.run();
    }

    private void parameState(){
        IParamesValue iParamesValueUp = this.parames.get(Direction.UP);
        if (iParamesValueUp.getActive()) {
            iParamesValueUp.setActive(false);
            setCurrent(this.stateMachines.get(Direction.UP));
        }
        IParamesValue iParamesValueDown = this.parames.get(Direction.DOWN);
        if (iParamesValueDown.getActive()) {
            iParamesValueDown.setActive(false);
            setCurrent(this.stateMachines.get(Direction.DOWN));
        }
        IParamesValue iParamesValueLeft = this.parames.get(Direction.LEFT);
        if (iParamesValueLeft.getActive()) {
            iParamesValueLeft.setActive(false);
            setCurrent(this.stateMachines.get(Direction.LEFT));
        }
        IParamesValue iParamesValueRight = this.parames.get(Direction.RIGHT);
        if (iParamesValueRight.getActive()) {
            iParamesValueRight.setActive(false);
            setCurrent(this.stateMachines.get(Direction.RIGHT));
        }

    }
}
