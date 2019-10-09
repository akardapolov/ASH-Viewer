/*
 * @(#)BasicFormatPainter.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.drawing.painter.format;

import com.egantt.awt.graphics.GraphicsContext;

import com.egantt.model.drawing.painter.PainterResources;

import java.text.Format;

public class BasicFormatPainter extends AbstractFormatPainter
{
	// __________________________________________________________________________

	protected String getValue(Object key, GraphicsContext context)
	{
		Format format = (Format) context.get(key, PainterResources.FORMAT);
		String value = format != null ? format.format(key) : key.toString();
		return value;
	}
}
