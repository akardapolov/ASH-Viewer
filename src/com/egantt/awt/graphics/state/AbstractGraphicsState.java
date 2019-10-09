/*
 * @(#)AbstractGraphicsState.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.graphics.state;

import com.egantt.awt.graphics.GraphicsState;

import com.egantt.util.Trace;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import java.util.Hashtable;

public abstract class AbstractGraphicsState implements GraphicsState
{
	private static final String THIS = GraphicsState2D.class.getName();
	private static final boolean TRACE = Trace.getEnable(THIS);

	protected final Hashtable hashtable = new Hashtable();

	/**
	 *  Calculated from the buffered-image
	 */
	protected transient WritableRaster raster;

	protected BufferedImage image;
	protected Rectangle clip;

	// __________________________________________________________________________

	public Graphics create(int x, int y, int w, int h)
	{
		BufferedImage image = (BufferedImage) createImage(x, y, w, h);
		if (image == null)
			return null;

		Graphics g = image.createGraphics();
		g.translate(x > 0 ? 0 : x, y > 0 ? 0 : y);
		g.setClip(0, 0, w, h);
		return g;
	}

	protected Image createImage(int x, int y, int w, int h)
	{
		WritableRaster raster = createRaster(x, y, w, h);
		if (raster == null)
			return null;

		ColorModel model = image.getColorModel();
		return raster != null
			? new BufferedImage(model, raster, model.isAlphaPremultiplied(), hashtable) : null;
	}

	/**
	 *  Creates a sub-raster of the master image if it is apropriate otherwise
	 *  it will have to create a brand-new raster image.
	 */
	protected WritableRaster createRaster(int x, int y, int w, int h)
	{
		// should not be passed in null graphics
		if (w < 1 || h < 1)
			return null;

		// should not be passed in elements outside of the raser
		if (x >= clip.x + clip.width || y >= clip.y + clip.height)
			return null;

		// calculate where to generate the raster
		int xPos = x < 0 ? clip.x : x;
		int yPos = y < 0 ? clip.y : y;

		int width = x < 0 ? x + w : w;
		width = Math.min(width, clip.width -xPos);

		int height =  y < 0 ? y + h : h;
		height = Math.min(height, clip.height -yPos);

		if (height < 1 || width < 1)
			return null;

		if (TRACE)
		{
			System.out.println("x="+x+"y="+y+"w="+w+"h="+h);
			System.out.println("xpos=" +xPos + " yPos=" +yPos + "width=" +width + "height=" + height);
			System.out.println(clip.toString());
			System.out.println();
		}
		return (WritableRaster) raster.createChild(xPos, yPos, width, height, 0, 0, null);
	}
}
