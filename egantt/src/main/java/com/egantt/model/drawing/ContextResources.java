/*
 * @(#)ContextResources.java
 *
 * Copyright 2002 EGANTT LLP. All rights reserved.
 * PROPRIETARY/QPL. Use is subject to license terms.
 */

package com.egantt.model.drawing;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.drawing.component.painter.PartPainter;
import com.egantt.drawing.view.ViewManager;
import com.egantt.model.drawing.axis.AxisView;
import com.egantt.model.drawing.painter.PainterState;
import com.egantt.swing.cell.editor.state.StateEditor;
import com.egantt.swing.scroll.ScrollManager;

public interface ContextResources
{
	Class AXIS_VIEW = AxisView.class;

	Class SCROLL_MANAGER = ScrollManager.class;
	
	Class VIEW_MANAGER = ViewManager.class;
	
	Class DRAWING_PAINTER = DrawingPainter.class;

	Class GRAPHICS_CONTEXT = GraphicsContext.class;

	Class PAINTER_STATE = PainterState.class;

	Class PART_PAINTER = PartPainter.class;
	
	Class STATE_EDITOR = StateEditor.class;
	
	//	________________________________________________________________________
	
	Class OTHER_PROPERTY = Object.class;
	
	//	________________________________________________________________________
}