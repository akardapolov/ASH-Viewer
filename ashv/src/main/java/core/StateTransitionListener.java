/**
 * This file is a part of Angry IP Scanner source code,
 * see http://www.angryip.org/ for more information.
 * Licensed under GPLv2.
 */

package core;

/**
 * StateTransitionListener
 *
 * @author Anton Keks
 */
public interface StateTransitionListener {

	/**
	 * Notifies on transition to the specified state.
	 * @param state
	 */
	void transitionTo(CurrrentState state);

}
