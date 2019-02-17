/*
 * @(#)DrawingStateListener.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.state.event;

import java.util.EventListener;

public interface DrawingStateListener extends EventListener
{
	void stateChanged(DrawingStateEvent e);
}
