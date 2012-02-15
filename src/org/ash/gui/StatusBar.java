/*
 *-------------------
 * The StatusBar.java is part of ASH Viewer
 *-------------------
 * 
 * ASH Viewer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASH Viewer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ASH Viewer.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (c) 2009, Alex Kardapolov, All rights reserved.
 *
 */
package org.ash.gui;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * The Class StatusBar.
 */
public class StatusBar extends JLabel
{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The begin time. */
    private double beginTime;
    
    /** The end time. */
    private double endTime;
    
    /** The selection. */
    private String selection = "";
    
    /** The range window. */
    private String rangeWindow = "";
        
    /** The date format. */
    private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	/** The store labels of wait event for sum (details)*/
	private	HashMap<String, String> titleTextEvent;
    
    /**
     * Instantiates a new status bar.
     */
    public StatusBar()
    {
        this("");
    }

    /**
     * Instantiates a new status bar.
     * 
     * @param status the status
     */
    public StatusBar(String status)
    {
        super(" " + status);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        this.titleTextEvent = new HashMap<String, String>();
    }

    /**
     * Sets the status.
     * 
     * @param status the new status
     */
    public void setStatus(String status)
    {
        setText(" " + status);
    }

    /**
     * Gets the status.
     * 
     * @return the status
     */
    public String getStatus()
    {
        return getText().trim();
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
	public Dimension getPreferredSize()
    {
        int height = super.getPreferredSize().height;
        int width = getParent() == null ? super.getPreferredSize().width : getParent().getWidth();
        return new Dimension(width, height);
    }

    /**
     * Sets the range.
     * 
     * @param beginTime0 the begin time0
     * @param endTime0 the end time0
     * @param event All or event class
     */
    public void setRange(double beginTime0, double endTime0)
    {
        this.beginTime = beginTime0;
        this.endTime = endTime0;
        updateLabelString();
    }
    
    /**
     * Sets the selection.
     * 
     * @param selection0 the new selection
     * @param event All or event class
     */
    public void setSelection(String selection0)
    {
        this.selection = " Selection: "+selection0;
        updateLabelString();
    }
    
    /**
     * Sets the range window.
     * 
     * @param range0 the new range window
     * @param event All or event class
     */
    public void setRangeWindow(String range0)
    {
        this.rangeWindow = range0;
        updateLabelString();
    }

    /**
     * Set label (range) for detail charts 
     * 
     * @param title
     * @param beginTime0
     * @param endTime0
     */
    public void setLabelForDetailChart(String title, double beginTime0, double endTime0 ){
    	this.titleTextEvent.put(title, 
    			this.dateFormat.format(beginTime0)+" --- "+
    			this.dateFormat.format(endTime0)+" ");
    	this.setStatus(title+"   Range: "+
    				this.dateFormat.format(beginTime0)+" --- "+
    				this.dateFormat.format(endTime0)+" ");
    }
    
    /**
     * Update label of statusBar for Top Activity tab
     */
    public void updateLabelStringTopActivity(){
    	if (!this.selection.equalsIgnoreCase("")){
    		updateLabelString();
    	} else {
    		setStatus("");
    	}
    }
    
    /**
     * Update label of statusBar for Top Activity tab
     */
    public void updateLabelStringHistory(){
    	updateLabelStringHist();
    }
    
    /**
     * Update label of statusBar for Top Activity tab
     */
    public void updateLabelStringOther(){
    	updateLabelStringOthers();
    }
    
    /**
     * Update label of statusBar for Detail tab
     * @param event class
     */
    public void updateLabelStringDetail(String event){   
    	boolean isEmpty = true;
    	Set entries = this.titleTextEvent.entrySet();
		Iterator iter = entries.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if (key.indexOf(event)!=-1){
				this.setStatus(key+"   Range: "+value);
				isEmpty = false;
			}
		}
		if (isEmpty){
			this.setStatus("");
		}
    }
    
    /**
     * Update label string.
     */
    private void updateLabelString()
    {
    	setStatus("Top Activity  "+this.selection+
    			  this.rangeWindow+"   Range: "+
    			  this.dateFormat.format(this.beginTime)+" --- "+
    			  this.dateFormat.format(this.endTime)+" ");
    }

    /**
     * Update label string.
     */
    private void updateLabelStringHist()
    {
    	setStatus("History data");
    }
    
    /**
     * Update label string for Other.
     */
    private void updateLabelStringOthers()
    {
    	setStatus("Other data");
    }
    
}
