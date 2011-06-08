/*
 * @(#)BasicJTableList.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */
package com.egantt.swing.table.list;

import java.awt.Color;

import javax.swing.JTable;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.CompoundHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;

import com.egantt.model.component.ComponentList;

/**
 *  Generates JTable's based on the Component list
 */
public class BasicJTableList extends AbstractTableList implements ComponentList
{
	// __________________________________________________________________________

	protected JXTable createTable()
	{
		return new JXTable();
	}
}
