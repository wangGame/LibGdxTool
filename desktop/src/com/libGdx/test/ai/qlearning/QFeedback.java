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

/** Represent the result of an action
 *
 * @author $Author: boulmier$
 * @author $Author: cortier$
 * @mavengroupid $GroupId$
 * @version $FullVersion$
 * @mavenartifactid $ArtifactId$
 */
public class QFeedback implements Cloneable, Serializable {
	private static final long serialVersionUID = 2250229614069647240L;

	private final float score;

	private final QState newState;

	/** Constructor.
	 *
	 * @param newState : new state after having take an action.
	 * @param score : score of the action.
	 */
	public QFeedback(QState newState, float score) {
		this.newState = newState;
		this.score = score;
	}

	/** Returns the score.
	 *
	 * @return the score
	 */
	public float getScore() {
		return this.score;
	}

	/** Returns the new state.
	 *
	 * @return the new state.
	 */
	public QState getNewState() {
		return this.newState;
	}
}
