/*
 * @(#)RowEvent.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.swing.table.model.row.event;

import java.util.EventListener;

public interface RowListener extends EventListener
{
	/**
	 *  Notification of a state change
	 */
	void stateChanged(RowEvent event);
}
