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

import java.io.Serializable;
import java.util.List;

/** This is the abstraction of a Q-Learning problem.
 *
 * @author $Author: boulmier$
 * @author $Author: cortier$
 * @mavengroupid $GroupId$
 * @version $FullVersion$
 * @mavenartifactid $ArtifactId$
 */
public interface QProblem extends Cloneable, Serializable {

	/** Returns the learning rate.
	 *
	 * @return alpha
	 */
	float getAlpha();

	/** Returns the discount factor.
	 *
	 * @return gamma
	 */
	float getGamma();

	/** Returns a part of the exploration factor, this part allow Q-Learning algorithm to choose a random action.
	 *
	 * @return rho
	 */
	float getRho();

	/** Returns a part of the exploration factor, this part allow Q-Learning algorithm to choose a random state.
	 *
	 * @return nu
	 */
	float getNu();

	/** Returns all the states.
	 *
	 * @return a list of states.
	 */
	List<QState> getStates();

	/** Returns the current state.
	 *
	 * @return the current state
	 */
	QState getCurrentState();

	/** Returns a random state.
	 *
	 * @return a random state
	 */
	QState getRandomState();

	/** Returns actions available in the given state.
	 *
	 * @param state : a state
	 * @return a list of actions
	 */
	List<QAction> getActions(QState state);

	/** Returns a random action depending on the given state.
	 *
	 * @param state : a state
	 * @return a random action.
	 */
	QAction getRandomAction(QState state);

	/** Takes the action executed from the given state and computes a feedback
	 *
	 * @param state : a state
	 * @param action : an action available in the given state.
	 * @return the feedback of the action in the given state.
	 */
	QFeedback takeAction(QState state, QAction action);
}
