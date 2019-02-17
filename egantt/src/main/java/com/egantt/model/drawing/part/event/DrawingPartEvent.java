/*
 * @(#)DrawingPartEvent.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing.part.event;

import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;

/**
  * Intent: Provides notification about addtions, changes & deletions
  * from with inside the @see com.egantt.model.drawing.DrawingPart
  */
public class DrawingPartEvent extends EventObject
{
	private static final long serialVersionUID = -3820644675878607811L;

	protected final Collection additions;
	protected final Collection changes;
	protected final Collection deletions;

	public DrawingPartEvent(Object source, Collection additions, Collection changes, Collection deletions)
	{
		super(source);

		this.additions = additions;
		this.changes = changes;
		this.deletions = deletions;
	}

	//___________________________________________________________________________

	public Iterator additions()
	{
		return additions.iterator();
	}

	public Iterator changes()
	{
		return changes.iterator();
	}

	public Iterator deletions()
	{
		return deletions.iterator();
	}
}
