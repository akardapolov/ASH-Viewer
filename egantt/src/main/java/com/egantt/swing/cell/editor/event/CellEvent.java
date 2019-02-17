/*
 * @(#)CellEvent.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
 
package com.egantt.swing.cell.editor.event;

import javax.swing.event.ChangeEvent;

/**
 *  <code>CellEvent</code> provides the changed value to a class implementing
 *   the <code>CellListener</code> interface
 */
public class CellEvent extends ChangeEvent
{
	private static final long serialVersionUID = -70741571335913199L;
	protected final Object value;
	
	public CellEvent(Object source, Object value)
	{
		super (source);
		this.value = value;
	}
}