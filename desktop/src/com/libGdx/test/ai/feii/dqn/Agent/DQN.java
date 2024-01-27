package com.libGdx.test.ai.feii.dqn.Agent;

import com.libGdx.test.ai.feii.dqn.NeuralNetwork.NeuralNetwork;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public abstract class DQN implements Serializable {
	/**
	 * 
	 */
	protected Random randomGenerator;
	private static final long serialVersionUID = -2093909581694354285L;
	private double discountFactor;
	private double epsilon;
	private double epsilonMin;
	private double epsilonDecay;
	private NeuralNetwork network;
	private final List<Experience> memory;
	
	public DQN(int[] topology, double learningRate, double discountFactor) {
		network = new NeuralNetwork(topology, learningRate, 0);
		this.discountFactor = discountFactor;
		epsilon = 1.0;
		epsilonMin = 0.05;
		epsilonDecay = 0.99999;
		memory = new ArrayList<>();
	}
	
	public double getLearningRate()
	{
		return network.learningRate;
	}

	public void setLearningRate(double learningRate)
	{
		network.learningRate = clamp(learningRate, 0, 1);
	}

	public double getDiscountFactor()
	{
		return discountFactor;
	}

	public void setDiscountFactor(double discountFactor)
	{
		this.discountFactor = clamp(discountFactor, 0, 1);
	}

	public double getEpsilon()
	{
		return epsilon;
	}

	public void setEpsilon(double epsilon)
	{
		this.epsilon = clamp(epsilon, 0, 1);
	}

	public double getEpsilonMin()
	{
		return epsilonMin;
	}

	public void setEpsilonMin(double epsilonMin)
	{
		this.epsilonMin = clamp(epsilonMin, 0, 1);
	}

	public double getEpsilonDecay()
	{
		return epsilonDecay;
	}

	public void setEpsilonDecay(double epsilonDecay)
	{
		this.epsilonDecay = clamp(epsilonDecay, 0, 1);
	}

	public NeuralNetwork getNetwork()
	{
		return network;
	}

	public void setNetwork(NeuralNetwork network)
	{
		if(network != null)
		{
			System.out.println("I'm not null :)");
			this.network = network;
		}
	}

	public List<Experience> getMemory()
	{
		return memory;
	}
	
	protected abstract double[] getState();

	protected abstract GameState getGameState();
	
	protected abstract boolean isDone();
	
	protected abstract double executeActionAndGetReward(int actionIndex); // returns reward too
	
	public void step()
	{
		GameState gameState = getGameState();
		double[] state = gameState.getState();
		int[] legalActionsIndex = gameState.getLegalActions();

		double[] qValues = network.predict(state);

		double[] legalActions = new double[legalActionsIndex.length];
		for (int i = 0; i < legalActionsIndex.length; i++) {
			legalActions[i] = qValues[legalActionsIndex[i]];
		}
		int actionIndex = legalActionsIndex[getMaxIndex(legalActions)];
		if(randomGenerator.nextDouble() < epsilon) {
			int index = (int) (randomGenerator.nextDouble() * legalActions.length);
			actionIndex = legalActionsIndex[index];
		}
		double reward = executeActionAndGetReward(actionIndex);
		double[] statePrime = getGameState().getState();
		Experience exp = new Experience(state, statePrime, actionIndex, reward, isDone());
		memory.add(exp);
		learn(exp);
		if(epsilon > epsilonMin) {
			epsilon *= epsilonDecay;

		}
		replay();
	}

	public int eval_step(){
		GameState gameState = getGameState();
		double[] state = gameState.getState();
		double[] qValues = network.predict(state);

		int[] legalActionsIndex = gameState.getLegalActions();

		int actionIndex = legalActionsIndex[0];
		double xx = qValues[legalActionsIndex[0]];
		for (int actionsIndex : legalActionsIndex) {
			double qValue = qValues[actionsIndex];
			if (qValue>xx){
				xx =  qValue;
				actionIndex = actionsIndex;
			}
		}
		return actionIndex;
	}
	
	private void replay() {
		if(memory.size() >= 10000) {
			for(int i = 0; i < 50; i++) {
				Experience exp = memory.remove((int)(randomGenerator.nextDouble() * memory.size()));
				learn(exp);
			}
		}
	}
	
	private void learn(Experience exp) {
		double[] state = exp.getState();
		double[] nextState = exp.getNextState();
		double reward = exp.getReward();
		int actionIndex = exp.getActionIndex();
		boolean isDone = exp.isDone();
		
		double qTarget;
		if(isDone)
		{
			qTarget = reward;
		}
		else
		{
			double[] nextStateQValues = network.predict(nextState);
			double maxQValue = nextStateQValues[getMaxIndex(nextStateQValues)];
			qTarget = reward + discountFactor * maxQValue;
		}
		
		double[] qValues = network.predict(state);
		qValues[actionIndex] = qTarget;
		
		network.train(exp.getState(), qValues, false);
	}
	
	private int getMaxIndex(double[] outputs) {
		double highestQvalue = Double.MIN_VALUE;
		int actionIndex = 0;
		for(int i = 0; i < outputs.length; i++) {
			if(outputs[i] > highestQvalue) {
				highestQvalue = outputs[i];
				actionIndex = i;
			}
		}
		
		return actionIndex;
	}
	
	private double clamp(double value, double min, double max)
	{
		if(value >= max)
			return max;
		if(value <= min)
			return min;
		return value;
	}

	public void saveSnakeDQN(String filePath) {
		try(FileOutputStream fout = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fout);) {
			oos.writeObject(getNetwork());
		}
		catch(Exception e) {
			System.err.println("ERROR: Failure in saving network to " + filePath + ". Reason is " + e.getMessage());
		}
	}

	public void loadSnakeDQN(String filePath) {
		NeuralNetwork oldNetwork = getNetwork();
		try(FileInputStream fin = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fin);) {
			NeuralNetwork network = (NeuralNetwork) ois.readObject();
			setNetwork(network);
		} catch(Exception e) {
			setNetwork(oldNetwork);
			System.err.println("ERROR: Failure in loading network from " + filePath + ". Reason is " + e.getMessage());
		}
	}

	public abstract boolean isSuccess();

	class Experience implements Serializable {
		private static final long serialVersionUID = -3109528438140969106L;
		private final double[] state;
		private final double[] nextState;
		private final int actionIndex;
		private final double reward;
		private final boolean done;
		public Experience(double[] state, double[] nextState, int actionIndex, double reward, boolean done) {
			this.state = state;
			this.nextState = nextState;
			this.actionIndex = actionIndex;
			this.reward = reward;
			this.done = done;
		}
		
		public double[] getState()
		{
			return state;
		}
		
		public double[] getNextState()
		{
			return nextState;
		}
		
		public int getActionIndex()
		{
			return actionIndex;
		}
		
		public double getReward()
		{
			return reward;
		}
		
		public boolean isDone()
		{
			return done;
		}
	}
}