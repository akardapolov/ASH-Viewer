package ext.egantt.actions;

import java.awt.Graphics;

import ext.egantt.swing.GanttTable;

public interface DrawingTool {
	
	public void intialize(GanttTable table);
	
	public void terminate();

	//	________________________________________________________________________
	
	public void paintComponent(Graphics g);
}