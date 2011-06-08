package com.egantt.model.drawing.part;

import com.egantt.model.drawing.DrawingPart;

public interface MutableDrawingPart extends DrawingPart {
	
	public void setContext(Object key, Object value);

	public void setPainter(Object key, Object value);
	public void setState(Object key, Object value);

}