/*
 * @(#)GraphicsState2D.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.awt.graphics.state;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class GraphicsState2D extends AbstractGraphicsState
{
	protected final int imageType = BufferedImage.TYPE_INT_ARGB;

	// __________________________________________________________________________

	public void initialise(Graphics g)
	{
		Rectangle bounds = g.getClipBounds();
		this.clip = new Rectangle(bounds.width + bounds.x, bounds.height + bounds.y);
		this.image = new BufferedImage(Math.max(clip.width,1), Math.max(clip.height,1), imageType);
		this.raster = image.getWritableTile(0, 0);
	}

	public void terminate(Graphics g)
	{
		g.drawImage(image, clip.x, clip.y, clip.width, clip.height, null);
		this.image = null;
	}
}
