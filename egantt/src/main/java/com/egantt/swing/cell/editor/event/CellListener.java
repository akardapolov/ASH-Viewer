/*
 * @(#)CellListener.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
 
package com.egantt.swing.cell.editor.event;

/**
 *  <code>CellListener</code> provides <code>CellEvent</code> notification for when
 *  the contents of a cell changes.
 */
public interface CellListener
{
	void stateChanged(CellEvent event);
}