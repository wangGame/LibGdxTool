/*******************************************************************************
 * Copyright (C) 2015 BOULMIER Jérôme, CORTIER Benoît
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 *******************************************************************************/

package com.libGdx.test.ai.qlearning;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

/** Q-Learning core algorithm.
 *
 * @param <Problem> is the type of the problem.
 * @author $Author: boulmier$
 * @author $Author: cortier$
 * @mavengroupid $GroupId$
 * @version $FullVersion$
 * @mavenartifactid $ArtifactId$
 */
public class QLearning<Problem extends QProblem> {
	private final Random randomGenerator = new Random();

	private Map<QState, Map<QAction, Float>> qValues = new TreeMap<>(new QStateNumberComparator());

	private final Problem problem;

	/** Constructor.
	 *
	 * @param problem : problem to learn on.
	 */
	@SuppressWarnings("boxing")
	public QLearning(Problem problem) {
		this.problem = problem;

		for (QState state : problem.getStates()) {
			Map<QAction, Float> temp = new TreeMap<>(new QActionNumberComparator());
			this.qValues.put(state, temp);

			for (QAction action : problem.getActions(state)) {
				temp.put(action, 0f);
			}
		}
	}

	public void saveQValues(URL fileName) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName.getPath()))) {
			oos.writeObject(this.qValues); // FIXME: TreeMaps cannot be serialized.
		}
	}

	@SuppressWarnings("unchecked")
	public void loadQValues(URL fileName) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName.getPath()))) {
			this.qValues = (Map<QState, Map<QAction, Float>>) ois.readObject(); // FIXME: TreeMaps cannot be serialized.
		}
	}

	/** Algorithm learns with this method.
	 *
	 * @param numberOfIterations : Number of iterations to execute this time.
	 */
	public void learn(int numberOfIterations) {
		QState currentState;
		QAction action;
		QFeedback result;

		currentState = this.problem.getCurrentState();

		for (int i = 0; i < numberOfIterations; ++i) {
			if (this.randomGenerator.nextFloat() < this.problem.getNu()) {
				currentState = this.problem.getRandomState();
			}

			if (this.randomGenerator.nextFloat() < this.problem.getRho()) {
				action = this.problem.getRandomAction(currentState);
			} else {
				action = getBestAction(currentState);
			}

			result = getFeedback(currentState, action);

			currentState = result.getNewState();
		}
	}

	/** Updates the Q-Learning graph.
	 *
	 * @param state : state from which the feedback is given.
	 * @param action : action to evaluate in the given state.
	 * @return return a feedback of the action in the given state.
	 */
	private QFeedback getFeedback(QState state, QAction action) {
		QFeedback result = this.problem.takeAction(state, action);

		QAction bestNextAction = getBestAction(result.getNewState());
		float bestNextActionValue = getQValue(result.getNewState(), bestNextAction);

		float qValue = (1f - this.problem.getAlpha())
				* getQValue(state, action)
				+ this.problem.getAlpha()
				* (result.getScore()
						+ this.problem.getGamma()
						* bestNextActionValue);

		setQValue(state, action, qValue);
		return result;
	}

	/** Replies the best action or a random action if there is several best actions.
	 *
	 * @param state : a state
	 * @return the best action.
	 */
	@SuppressWarnings("boxing")
	public QAction getBestAction(QState state) {
		Map<QAction, Float> qValuesState = this.qValues.get(state);
		List<QAction> bestActions = new ArrayList<>();
		float bestScore = Float.NEGATIVE_INFINITY;

		for (Entry<QAction, Float> entry : qValuesState.entrySet()) {
			if (entry.getValue() > bestScore) {
				bestScore = entry.getValue();

				bestActions.clear();
				bestActions.add(entry.getKey());
			} else if (entry.getValue() == bestScore) {
				bestActions.add(entry.getKey());
			}
		}

		return bestActions.get(this.randomGenerator.nextInt(bestActions.size()));
	}

	/** Returns the Q-Value corresponding to the given state and action.
	 *
	 * @param state : a state
	 * @param action : an action
	 * @return the qValue.
	 */
	public float getQValue(QState state, QAction action) {
		Float qValue = this.qValues.get(state).get(action);

		if (qValue == null) {
			return 0f;
		}

		return qValue.floatValue();
	}

	@SuppressWarnings("boxing")
	private void setQValue(QState state, QAction action, float qValue) {
		this.qValues.get(state).put(action, qValue);
	}
}
