/*
 * @(#)GraphicsState.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.graphics;

import java.awt.Graphics;

public interface GraphicsState
{
	// __________________________________________________________________________

	/**
	 * Initialise the state, this should be always a general implementation will
	 * clear the existing state.
	 */
	void initialise(Graphics g);

	/**
	 * Terminates the state, a general implementation will write back the contents
	 * of the buffer back to the GraphicsDevice
	 */
	void terminate(Graphics g);

	// __________________________________________________________________________

	/**
	 *  Creates a new Graphics
	 */
	Graphics create(int x, int y, int w , int h);
}
